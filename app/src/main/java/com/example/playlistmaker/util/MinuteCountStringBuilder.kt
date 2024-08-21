package com.example.playlistmaker.util

class MinuteCountStringBuilder {
    fun build(count: Int): String {
        val form = "минут"
        val form1 = "минуты"
        val form2 = "минута"
        return when (count) {
            1 -> "$count $form2"
            in 2..4 -> "$count $form1"
            else -> "$count $form"
        }
    }
}