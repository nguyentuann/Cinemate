package com.movpla.detail.streaming.network

import com.movpla.detail.streaming.data.model.StreamingData
import com.movpla.detail.streaming.data.model.SubtitleTrack
import com.movpla.detail.streaming.data.model.VideoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class StreamingApi(
    private val baseUrl: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    
    suspend fun getStreamingData(
        movieId: String,
        episodeId: String? = null
    ): Result<StreamingData> = withContext(Dispatchers.IO) {
        try {
            val url = if (episodeId != null) {
                "$baseUrl/watch/$movieId/$episodeId"
            } else {
                "$baseUrl/watch/$movieId"
            }
            
            val request = Request.Builder()
                .url(url)
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Failed to fetch streaming data: ${response.code}")
                )
            }
            
            val responseBody = response.body?.string() 
                ?: return@withContext Result.failure(Exception("Empty response"))
            
            val data = parseStreamingData(responseBody)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun parseStreamingData(json: String): StreamingData {
        val jsonObject = JSONObject(json)
        
        val sources = mutableListOf<VideoSource>()
        val sourcesArray = jsonObject.optJSONArray("sources") ?: JSONArray()
        for (i in 0 until sourcesArray.length()) {
            val sourceObj = sourcesArray.getJSONObject(i)
            sources.add(
                VideoSource(
                    url = sourceObj.getString("url"),
                    type = sourceObj.optString("type", "application/x-mpegURL"),
                    quality = sourceObj.optString("quality", null)
                )
            )
        }
        
        val subtitles = mutableListOf<SubtitleTrack>()
        val subtitlesArray = jsonObject.optJSONArray("subtitles") ?: JSONArray()
        for (i in 0 until subtitlesArray.length()) {
            val subObj = subtitlesArray.getJSONObject(i)
            subtitles.add(
                SubtitleTrack(
                    url = subObj.getString("url"),
                    lang = subObj.getString("lang"),
                    label = subObj.getString("label"),
                    isDefault = subObj.optBoolean("default", false)
                )
            )
        }
        
        val skipIntro = jsonObject.optJSONObject("intro")?.let {
            com.movpla.detail.streaming.data.model.SkipTime(
                start = it.getDouble("start").toFloat(),
                end = it.getDouble("end").toFloat()
            )
        }
        
        val skipOutro = jsonObject.optJSONObject("outro")?.let {
            com.movpla.detail.streaming.data.model.SkipTime(
                start = it.getDouble("start").toFloat(),
                end = it.getDouble("end").toFloat()
            )
        }
        
        return StreamingData(
            sources = sources,
            subtitles = subtitles,
            thumbnails = jsonObject.optString("thumbnails", null),
            skipIntro = skipIntro,
            skipOutro = skipOutro
        )
    }
}
