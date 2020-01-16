package com.example.travelapp

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class Glide : AppGlideModule()

fun AppCompatImageView.loadImage(url: String) {
    GlideApp.with(this)
        .load(url)
        .centerCrop()
        .dontAnimate()
        .into(this)
}