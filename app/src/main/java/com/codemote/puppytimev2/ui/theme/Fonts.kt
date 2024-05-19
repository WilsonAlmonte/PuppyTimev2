package com.codemote.puppytimev2.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.codemote.puppytimev2.R

fun getBodyFont(): FontFamily {
    val light = Font(R.font.inter_light, FontWeight.Light)
    val regular = Font(R.font.inter_regular, FontWeight.Normal)
    val bold = Font(R.font.inter_bold, FontWeight.Bold)
    val black = Font(R.font.inter_black, FontWeight.Black)

    return FontFamily(light, regular, bold, black)
}

fun getDisplayFont(): FontFamily {
    val light = Font(R.font.poppins_light, FontWeight.Light)
    val regular = Font(R.font.poppins_regular, FontWeight.Normal)
    val bold = Font(R.font.poppins_bold, FontWeight.Bold)
    val black = Font(R.font.poppins_black, FontWeight.Black)

    return FontFamily(light, regular, bold, black)
}