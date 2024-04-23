package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME
import com.example.playlistmaker.R
import com.example.playlistmaker.SETTING_PREFERENCES

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backAction = findViewById<ImageView>(R.id.imageBackAction)
        backAction.setOnClickListener {
            finish()
        }

        val shareClick = findViewById<FrameLayout>(R.id.layoutShare)
        shareClick.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.url_practicum))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportClick = findViewById<FrameLayout>(R.id.layoutSupport)
        supportClick.setOnClickListener {

            Intent().apply {
                action =Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_default_text))
                startActivity(this)
            }
        }

        val userAgreementClick = findViewById<FrameLayout>(R.id.layoutUserAgreement)
        userAgreementClick.setOnClickListener {
            val url = Uri.parse(getString(R.string.url_agreement))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        val sharedPrefs = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)

            sharedPrefs.edit()
                .putBoolean(DARK_THEME, checked)
                .apply()
        }
    }

}