package com.example.shorts

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class ShortClip(
    val id: Long,
    val videoUrl: String,
    val author: String,
    val pageUrl: String
)

class PageResult(val clips: List<ShortClip>, val rawCount: Int)

class ShortsApiException(message: String) : Exception(message)

object PixabayApi {
    private const val ENDPOINT = "https://pixabay.com/api/videos/"
    private const val PER_PAGE = 100
    private val http = OkHttpClient()
    private val cache = HashMap<Int, PageResult>()

    suspend fun fetchPage(page: Int): PageResult {
        cache[page]?.let { return it }
        val result = withContext(Dispatchers.IO) { download(page) }
        cache[page] = result
        return result
    }

    private fun download(page: Int): PageResult {
        val url = ENDPOINT.toHttpUrl().newBuilder()
            .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
            .addQueryParameter("order", "popular")
            .addQueryParameter("safesearch", "true")
            .addQueryParameter("min_height", "1000")
            .addQueryParameter("per_page", PER_PAGE.toString())
            .addQueryParameter("page", page.toString())
            .build()

        val body = http.newCall(Request.Builder().url(url).build()).execute().use { response ->
            when (response.code) {
                200 -> Unit
                400, 401, 403 -> throw ShortsApiException("API key ไม่ถูกต้องหรือยังไม่ได้ตั้งค่า (PIXABAY_API_KEY)")
                429 -> throw ShortsApiException("ดึงข้อมูลบ่อยเกินไป กรุณารอสักครู่แล้วลองใหม่")
                else -> throw ShortsApiException("เซิร์ฟเวอร์ตอบกลับผิดพลาด (${response.code})")
            }
            response.body?.string().orEmpty()
        }
        return parse(body)
    }

    private fun parse(json: String): PageResult {
        val hits = JSONObject(json).optJSONArray("hits") ?: return PageResult(emptyList(), 0)
        val clips = ArrayList<ShortClip>()
        for (i in 0 until hits.length()) {
            val hit = hits.getJSONObject(i)
            val videos = hit.optJSONObject("videos") ?: continue

            // เลือกไฟล์ขนาดพอดีกับมือถือ (small ก่อน แล้วค่อย medium, tiny)
            var picked: JSONObject? = null
            for (size in listOf("small", "medium", "tiny")) {
                val f = videos.optJSONObject(size) ?: continue
                if (f.optString("url").isNotEmpty() && f.optInt("width") > 0) {
                    picked = f
                    break
                }
            }
            val file = picked ?: continue
            if (file.optInt("height") <= file.optInt("width")) continue // เอาเฉพาะแนวตั้ง

            clips.add(
                ShortClip(
                    id = hit.getLong("id"),
                    videoUrl = file.getString("url"),
                    author = hit.optString("user").ifBlank { "Pixabay" },
                    pageUrl = hit.optString("pageURL").ifBlank { "https://pixabay.com" }
                )
            )
        }
        return PageResult(clips, hits.length())
    }
}
