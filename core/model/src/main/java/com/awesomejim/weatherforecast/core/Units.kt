package com.awesomejim.weatherforecast.core

import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Units(val value: String, val tempLabel: String) {
    STANDARD("standard", "°F"),
    METRIC("metric", "°C"),
    IMPERIAL("imperial", "°F"),
}


fun printTrace(exception: Exception){
    val sw = StringWriter()
    exception.printStackTrace(PrintWriter(sw))
    val exceptionAsString = sw.toString()
    println(exceptionAsString)
}


fun getDate(utcInMillis: Long, formatPattern: String): String {
    val sdf = SimpleDateFormat(formatPattern, Locale.getDefault())
    val dateFormat = Date(utcInMillis * 1000)
    return sdf.format(dateFormat)
}

fun getUpdatedOnDate(date: Date): String {
    val DATE_FORMAT = "EEE, d MMM yyyy HH:mm aa"
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}