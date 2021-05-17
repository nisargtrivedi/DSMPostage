package com.dsmpostage.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.regions.Regions
import com.dsmpostage.R
import com.dsmpostage.databinding.ActivityLoginBinding
import com.dsmpostage.utility.AppPreferences
import com.dsmpostage.utility.Cognito
import com.dsmpostage.utility.Util
import com.google.android.material.button.MaterialButton
import java.io.Serializable

class LoginActivity : AppCompatActivity() {

    lateinit var obj : Cognito
    lateinit var binding : ActivityLoginBinding

    var appPreferences: AppPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        obj = Cognito(this)

        //binding.edtEmail.setText("nisarg.trivedi1192@gmail.com")
        //binding.etPassword.setText("Nisarg@123")

        binding.btnSignIn.setOnClickListener {
            if(TextUtils.isEmpty(binding.edtEmail.text.toString().trim())){
                Util.showDialog(this,"Please enter email address")
            } else if(!Util.isValidEmail(binding.edtEmail.text.toString().trim())){
                Util.showDialog(this,"Please enter proper email address")
            } else if(TextUtils.isEmpty(binding.etPassword.text.toString().trim())){
                Util.showDialog(this,"Please enter password")
            }else{

                if(DSMPostage.getInstance().isNetworkAvailable) {
                    obj.userLogin(
                        binding.edtEmail.text.toString().trim(),
                        binding.etPassword.text.toString().trim()
                    )
                }else{
                    Util.showDialog(this,"No Internet Available")
                }
            }
        }

    }


    override fun onBackPressed() {
        val dialogBuilder =
            AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_confirm, null)
        dialogBuilder.setView(dialogView)

        val tvMsg = dialogView.findViewById<TextView>(R.id.tvMsg)
        val btnCancel: MaterialButton = dialogView.findViewById(R.id.btnCancel)
        val btnOk: MaterialButton = dialogView.findViewById(R.id.btnOk)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        tvMsg.text = "Are you sure want exit from this application ?"

        btnOk.setOnClickListener {
            alertDialog.dismiss()
            finishAffinity()
        }
        btnCancel.setOnClickListener { alertDialog.dismiss() }

    }
}