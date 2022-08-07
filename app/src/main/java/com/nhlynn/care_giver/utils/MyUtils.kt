package com.nhlynn.care_giver.utils

import java.text.SimpleDateFormat
import java.util.*

object MyUtils {
    fun convertTime(time: String?): String {
        return if (time.isNullOrEmpty()) {
            ""
        } else {
            val parser = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val formatter = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
            formatter.format(parser.parse(time)!!)
        }
    }
}