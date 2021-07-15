package com.dsmpostage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dsmpostage.main.HomeActivity
import com.dsmpostage.main.LoginActivity
import com.dsmpostage.utility.AppPreferences

class MainActivity : AppCompatActivity() {

   lateinit var appPreference: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        appPreference = AppPreferences(this)

        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */

            if(!appPreference.getString("USERNAME").isNullOrEmpty() ) {

                val mainIntent = Intent(this, HomeActivity::class.java)
                startActivity(mainIntent)
                finish()
            }else {
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }


        }, 3000)

    }
    
}