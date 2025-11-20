package com.saim.curify.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.saim.curify.MainActivity
import com.saim.data.repositories.AuthRepository

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Android 12+ system splash
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Simple routing: if user is logged in, go to MainActivity; else to start_screen/Login
        val current = AuthRepository().getCurrentUser()
        if (current == null) {
            startActivity(Intent(this, start_screen::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}

