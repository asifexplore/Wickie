package com.example.Wickie.Utils

import java.security.AccessControlContext
import java.text.SimpleDateFormat
import java.util.*

//fun date.toString(format: String, locale: Locale = Locale.getDefault(), applicationContext: AccessControlContext): String {
//    val formatter = SimpleDateFormat(format, locale)
//    return formatter.format(applicationContext)
//}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}