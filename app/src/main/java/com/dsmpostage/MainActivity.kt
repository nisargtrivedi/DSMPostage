package com.dsmpostage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dsmpostage.main.HomeActivity
import com.dsmpostage.main.LoginActivity
import com.dsmpostage.utility.AppPreferences
import com.dsmpostage.utility.Cognito

class MainActivity : AppCompatActivity() {

   lateinit var appPreference: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        appPreference = AppPreferences(this)

        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */

            if(!appPreference.getString("REFRESH_TOKEN").isNullOrEmpty() ) {

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