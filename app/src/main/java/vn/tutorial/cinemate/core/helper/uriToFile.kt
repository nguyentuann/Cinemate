package vn.tutorial.cinemate.core.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import vn.tutorial.cinemate.core.util.LogUtil
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale

fun uriToFile(uri: Uri, context: Context, maxWidth: Int = 800, maxHeight: Int = 800, quality: Int = 80): File? {
    return try {
        // 1. Mở InputStream từ Uri
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // 2. Tính tỉ lệ resize
        val ratio = minOf(maxWidth.toFloat() / originalBitmap.width, maxHeight.toFloat() / originalBitmap.height)
        val newWidth = (originalBitmap.width * ratio).toInt()
        val newHeight = (originalBitmap.height * ratio).toInt()

        // 3. Resize bitmap
        val resizedBitmap = originalBitmap.scale(newWidth, newHeight)

        // 4. Tạo file tạm
        val file = File(context.cacheDir, "avatar_temp_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)

        // 5. Nén ảnh JPEG để giảm dung lượng
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()

        file
    } catch (e: Exception) {
        LogUtil("Convert Uri to File failed")
        e.printStackTrace()
        null
    }
}
