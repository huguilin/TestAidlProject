package com.hugl.testaidlproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Messages(
    var msg:String,
    var time:String,
    var name:String,
    var age:Int
) : Parcelable