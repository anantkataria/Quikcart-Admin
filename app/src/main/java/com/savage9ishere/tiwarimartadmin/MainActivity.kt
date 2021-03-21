package com.savage9ishere.tiwarimartadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.savage9ishere.tiwarimartadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val binding : ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        binding.parentLayout.visibility = View.GONE

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            auth.signInAnonymously().addOnCompleteListener {
                if(it.isSuccessful){
                    binding.parentLayout.visibility = View.VISIBLE
                }
                else {
                    Toast.makeText(this, "Anonymous sign-in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            binding.parentLayout.visibility = View.VISIBLE
        }
    }
}