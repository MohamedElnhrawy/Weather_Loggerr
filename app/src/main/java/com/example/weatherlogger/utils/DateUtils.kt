package com.example.weatherlogger.utils

import java.text.SimpleDateFormat


fun String.formatDate(): String {
    val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val formatter = SimpleDateFormat("d MMM y, h:mma")
    return formatter.format(parser.parse(this))
}
