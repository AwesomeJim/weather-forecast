package com.awesomejim.weatherforecast.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.awesomejim.weatherforecast.core.designsystem.R


val Lato = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_medium, FontWeight.Medium),
    Font(R.font.lato_semibold, FontWeight.SemiBold),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_black, FontWeight.Black),
)


val AppTypography = Typography(
    // max / min temp
    labelLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp,
        fontSize = 16.sp
    ),
    // Day forecast
    labelMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp,
        fontSize = 14.sp
    ),
    // Used for time in week card
    labelSmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp,
        fontSize = 10.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W400,
        lineHeight = 24.sp,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W400,
        lineHeight = 20.sp,
        fontSize = 14.sp
    ),
    // used for sunny/cloudy
    bodySmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        fontSize = 11.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W400,
        lineHeight = 36.sp,
        fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W400,
        lineHeight = 32.sp,
        fontSize = 24.sp
    ),
    displayLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 120.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W400,
        lineHeight = 52.sp,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W400,
        lineHeight = 44.sp,
        fontSize = 36.sp
    ),
    // top app bar
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        fontSize = 14.sp
    ),
)