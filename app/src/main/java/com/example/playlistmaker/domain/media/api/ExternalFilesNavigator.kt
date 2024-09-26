package com.example.playlistmaker.domain.media.api

import android.net.Uri

interface ExternalFilesNavigator {
    fun saveImageToPrivateStorage(uri: Uri, fileName: String): String
    fun getImageFromPrivateStorage(name: String): Uri
    fun deleteImage(imageDir: String)
}