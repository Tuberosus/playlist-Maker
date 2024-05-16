package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.models.EmailData

interface SharingRepository {
    fun getSharingUrl(): String
    fun getTermsUrl(): String
    fun getEmailData(): EmailData
}