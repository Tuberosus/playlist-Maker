package com.example.playlistmaker.data.sharing.impl

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.models.EmailData

class ExternalNavigatorImpl(private val application: Application): ExternalNavigator {
    override fun shareLink(link: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "text/plain"
        }
        application.startActivity(intent)

    }

    override fun openLink(link: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(link)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        application.startActivity(intent)
    }

    override fun sendEmail(email: EmailData) {
        val intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email.studentEmailAddress))
            putExtra(Intent.EXTRA_SUBJECT, email.emailSubject)
            putExtra(Intent.EXTRA_TEXT, email.emailText)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        application.startActivity(intent)
    }

}