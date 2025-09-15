package com.example.noteapp.feature_note.domain.util

import android.content.Context
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.example.noteapp.R
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageUtils {
    private const val DEFAULT_IMAGE_NAME = "default_note_image.jpg"
    fun getDefaultImagePath(context: Context): String {
        val file = File(context.filesDir, DEFAULT_IMAGE_NAME)
        if (!file.exists()) {
            try {
                val drawable = ContextCompat.getDrawable(context, R.drawable.gallery_thumbnail) as? VectorDrawable
                if (drawable != null) {
                    val bitmap = createBitmap(
                        width = drawable.intrinsicWidth,
                        height = drawable.intrinsicHeight,
                        config = Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)

                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                    bitmap.recycle()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return file.absolutePath
    }

    fun saveImageToInternalStorage(context: Context, imageUri: Uri?): String {
        if (imageUri == null) {
            return getDefaultImagePath(context)
        }

        return try {
            val fileName = "note_image_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            context.contentResolver.openInputStream(imageUri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            getDefaultImagePath(context)
        }
    }

    fun deleteImage(path: String) {
        try {
            val defaultPath = path.substringAfterLast("/")
            // Don't delete the default image
            if (defaultPath != DEFAULT_IMAGE_NAME) {
                File(path).delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
