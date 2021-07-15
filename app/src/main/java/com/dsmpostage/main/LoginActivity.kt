package com.dsmpostage.main

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.dsmpostage.R
import com.dsmpostage.databinding.ActivityLoginBinding
import com.dsmpostage.utility.AppPreferences
import com.dsmpostage.utility.Cognito
import com.dsmpostage.utility.KeyBoardHandling
import com.dsmpostage.utility.Util
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    lateinit var obj : Cognito
    lateinit var binding : ActivityLoginBinding

    var appPreferences: AppPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        obj = Cognito(this)

        //binding.edtEmail.setText("sharsh.2109@gmail.com")
        //binding.etPassword.setText("Siimteq#123")

        binding.btnSignIn.setOnClickListener {
            if(TextUtils.isEmpty(binding.edtEmail.text.toString().trim())){
                Util.showDialog(this,"Please enter email address")
            } else if(!Util.isValidEmail(binding.edtEmail.text.toString().trim())){
                Util.showDialog(this,"Please enter proper email address")
            } else if(TextUtils.isEmpty(binding.etPassword.text.toString().trim())){
                Util.showDialog(this,"Please enter password")
            }else{
                if(DSMPostage.getInstance().isNetworkAvailable) {
                    KeyBoardHandling.hideSoftKeyboard(this@LoginActivity)
                    obj.userLogin(
                        binding.edtEmail.text.toString().trim(),
                        binding.etPassword.text.toString().trim()
                    )
                }else{
                    Util.showDialog(this,"No Internet Available")
                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            val mainIntent = Intent(this, ForgotPassword::class.java)
            startActivity(mainIntent)
            finish()
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