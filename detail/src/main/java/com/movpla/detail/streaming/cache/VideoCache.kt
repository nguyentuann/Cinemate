package com.movpla.detail.streaming.cache

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.security.MessageDigest

class VideoCache(
    private val context: Context,
    private val maxCacheSize: Long = 500 * 1024 * 1024, // 500MB
    private val client: OkHttpClient = OkHttpClient()
) {
    
    private val cacheDir: File by lazy {
        File(context.cacheDir, "video_cache").apply {
            if (!exists()) mkdirs()
        }
    }
    
    suspend fun get(url: String): String? = withContext(Dispatchers.IO) {
        val cacheKey = generateCacheKey(url)
        val cacheFile = File(cacheDir, cacheKey)
        
        if (cacheFile.exists()) {
            cacheFile.absolutePath
        } else {
            null
        }
    }
    
    suspend fun set(url: String, data: ByteArray): String = withContext(Dispatchers.IO) {
        val cacheKey = generateCacheKey(url)
        val cacheFile = File(cacheDir, cacheKey)
        
        cacheFile.writeBytes(data)
        
        // Clean cache if needed
        cleanCacheIfNeeded()
        
        cacheFile.absolutePath
    }
    
    suspend fun download(url: String): String = withContext(Dispatchers.IO) {
        val cached = get(url)
        if (cached != null) {
            return@withContext cached
        }
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        val response = client.newCall(request).execute()
        
        if (!response.isSuccessful) {
            throw Exception("Failed to download: ${response.code}")
        }
        
        val data = response.body?.bytes() 
            ?: throw Exception("Empty response body")
        
        set(url, data)
    }
    
    fun clear() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }
    
    fun getCacheSize(): Long {
        return cacheDir.listFiles()?.sumOf { it.length() } ?: 0L
    }
    
    private fun cleanCacheIfNeeded() {
        val currentSize = getCacheSize()
        
        if (currentSize > maxCacheSize) {
            val files = cacheDir.listFiles()?.sortedBy { it.lastModified() } ?: return
            
            var sizeToFree = currentSize - (maxCacheSize * 0.8).toLong()
            
            for (file in files) {
                if (sizeToFree <= 0) break
                
                sizeToFree -= file.length()
                file.delete()
            }
        }
    }
    
    private fun generateCacheKey(url: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val digest = md5.digest(url.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
