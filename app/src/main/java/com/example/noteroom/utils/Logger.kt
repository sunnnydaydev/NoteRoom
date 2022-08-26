package com.example.noteroom.utils

import android.util.Log

/**
 * Create by SunnyDay /08/26 14:47:33
 */
object Logger {
    fun d(tag:String,msg:()->String){
       Log.d(tag,msg.invoke())
    }
}