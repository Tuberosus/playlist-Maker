package com.example.playlistmaker.data.media

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.media.api.ExternalFilesNavigator
import java.io.File
import java.io.FileOutputStream

class ExternalFilesNavigatorImpl(
    private val application: Application
) : ExternalFilesNavigator {

    companion object {
        private const val PICTURE_ALBUM = "playlist_album"
    }

    override fun saveImageToPrivateStorage(uri: Uri, fileName: String): String {
        val filePath = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PICTURE_ALBUM)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "$fileName.jpg")
        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.path
    }

    override fun getImageFromPrivateStorage(name: String): Uri {
        val filePath = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PICTURE_ALBUM)
        val file = File(filePath, "$name.jpg")
        return file.toUri()
    }


}