package com.example.typingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<TextView>(R.id.loginBtn).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        findViewById<TextView>(R.id.signupBtn).setOnClickListener {
            Firebase.auth.signInWithEmailAndPassword("test@gmail.com", "test123").addOnCompleteListener {
                if (it.isSuccessful) {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
