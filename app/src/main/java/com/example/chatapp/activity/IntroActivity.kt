package com.example.chatapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        Handler(Looper.getMainLooper()).postDelayed({
            auth = FirebaseAuth.getInstance()
            auth.addAuthStateListener { firebaseAuth ->
                if (firebaseAuth.currentUser != null) {
                    val intent = Intent(this, UsersActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 1300)
    }
}
