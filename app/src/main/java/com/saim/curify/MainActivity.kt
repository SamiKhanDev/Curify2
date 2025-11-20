package com.saim.curify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.saim.DataSources.CloudinaryUploadHelper.Companion.initializeCloudinary
import com.saim.AdminModule.AuthViewModel
import com.saim.curify.databinding.ActivityMainBinding
import com.saim.data.repositories.AuthRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.saim.DataSources.CloudinaryUploadHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AuthViewModel
    companion object {
        var adminUid = "TEKfgTjdU1cfBIRshfADn279lWu1"
        var useremail = ""
        var isAdmin = false
        var names=""

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
//
//        val userad: FirebaseUser = AuthRepository().getcurrentuser()!!
//        if (userad.email.equals("curify90@gmail.com") && adminUid== currentUserUid) {
//            startActivity(Intent(this@MainActivity, AdminMainScreen::class.java))
//            finish()
//
//
//        }
        CloudinaryUploadHelper.initializeCloudinary(this)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user: FirebaseUser? = AuthRepository().getCurrentUser()
        if (user == null) {
            startActivity(Intent(this, com.saim.AuthenticationModule.LoginActivity::class.java))
            finish()
            return
        }
        names = user.displayName ?: "User"
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navHostFragment.navController)

        // Apply system bar insets so content doesn't draw under status/navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Pad top for status bar on content container
            findViewById<View>(R.id.nav_host_fragment).setPadding(0, systemBars.top, 0, 0)
            // Add bottom margin so BottomNavigationView sits above system nav bar/buttons
            val lp = bottomNavigationView.layoutParams as android.view.ViewGroup.MarginLayoutParams
            if (lp.bottomMargin != systemBars.bottom) {
                lp.bottomMargin = systemBars.bottom
                bottomNavigationView.layoutParams = lp
            }
            insets
        }

    }
}