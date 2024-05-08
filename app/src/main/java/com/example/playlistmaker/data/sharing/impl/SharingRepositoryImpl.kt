package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingRepositoryImpl(
    private val context: Context,

): SharingRepository {
    override fun getSharingUrl(): String {
        return context.getString(R.string.url_practicum)
    }

    override fun getTermsUrl(): String {
        return context.getString(R.string.url_agreement)
    }

    override fun getEmailData(): EmailData {
        return EmailData(
            studentEmailAddress = context.getString(R.string.student_email),
            emailSubject = context.getString(R.string.support_email_subject),
            emailText = context.getString(R.string.support_default_text),
        )
    }
}