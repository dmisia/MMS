package pl.edu.pwr.lab23.i236764.adapter

import android.graphics.Bitmap

data class Image (
    val imagePath: String,
    val title: String,
    val img : Bitmap,
    val created: String,
    val type: String,
    var isStar: Boolean
)
