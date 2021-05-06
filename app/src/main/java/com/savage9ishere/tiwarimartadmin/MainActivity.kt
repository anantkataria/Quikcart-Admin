package com.savage9ishere.tiwarimartadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.savage9ishere.tiwarimartadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val binding : ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        val navView: BottomNavigationView = binding.bnv

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.ordersFragment, R.id.categoriesFragment, R.id.closeStoreFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        //return super.onSupportNavigateUp()
        return navController.navigateUp()
    }
}