package com.capstone.healme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.healme.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val intent = Intent(this, MainActivity::class.java)

        binding.ivSplash.alpha = 0f
        binding.ivSplash.animate()
            .setDuration(1500)
            .alpha(1f)
            .withEndAction {
                binding.ivSplash.animate()
                    .setDuration(1000)
                    .rotationBy(360f)
                    .withEndAction {
                        startActivity(intent)
                        finish()
                    }
            }
    }
}