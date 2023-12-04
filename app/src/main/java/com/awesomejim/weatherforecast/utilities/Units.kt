package com.awesomejim.weatherforecast.utilities

import java.io.PrintWriter
import java.io.StringWriter

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