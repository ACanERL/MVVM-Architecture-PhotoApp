package com.example.phototest.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.phototest.databinding.ActivityPhotoDetailBinding
import java.net.HttpURLConnection
import java.net.URL

class PhotoDetail : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val portrait = intent.getStringExtra("key")

            Glide.with(this@PhotoDetail)
                .load(portrait
                )
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.image)
        }
    }
}