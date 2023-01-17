package com.example.typingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<TextView>(R.id.homeBtn).setOnClickListener {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPassword)
        val confirmPasswordInput = findViewById<EditText>(R.id.etPassword2)

        findViewById<TextView>(R.id.signUpBtn).setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Enter Email and Password!")
                return@setOnClickListener
            }
            if (passwordInput.text.length < 6) {
                showToast("Password too short!")
                return@setOnClickListener
            }
            if (passwordInput.text != confirmPasswordInput.text) {
                showToast("Passwords do not match")
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    }
                } else {
                    showToast("Invalid Credentials!")
                }
            }
        }
    }

    private fun showToast(label: String) {
        Toast.makeText(this, label, Toast.LENGTH_SHORT).show()
    }
}