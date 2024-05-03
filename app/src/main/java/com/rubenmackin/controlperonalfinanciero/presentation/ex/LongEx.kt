package com.rubenmackin.controlperonalfinanciero.presentation.ex

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long?.milisToDate():String{
    this ?: return ""
    return try {
        val date = Date(this)
        val sdf = SimpleDateFormat("EEEE dd MMMM", Locale.getDefault())
        sdf.format(date)
    }catch (e: Exception){
        ""
    }
}