package com.example.typingapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val db = FirebaseDatabase.getInstance().getReference("userInfo")
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTimeButton(view.findViewById(R.id.first), 15)
        initTimeButton(view.findViewById(R.id.second), 30)
    }

    private fun initTimeButton(btn: Button, time: Number) {
        btn.setOnClickListener {
            val settings = requireActivity().getSharedPreferences("Settings", 0)
            val editor = settings.edit()
            editor.putLong("Time", time.toLong())
            editor.apply()
        }
    }
}
