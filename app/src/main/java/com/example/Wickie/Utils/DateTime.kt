package com.example.Wickie.Utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getCurrentDate() : String{
    val sdf =  SimpleDateFormat("dd/MM/yyyy")
    val currentDate = sdf.format(Date())
    return currentDate.toString()
}

fun getCurrentTime(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}
