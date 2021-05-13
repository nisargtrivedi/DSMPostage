package com.dsmpostage.main

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dsmpostage.R
import com.dsmpostage.databinding.ActivityLoginBinding
import com.dsmpostage.utility.Cognito
import com.dsmpostage.utility.Util

class LoginActivity : AppCompatActivity() {

    lateinit var obj : Cognito
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        obj = Cognito(this)

        binding.edtEmail.setText("yankitpatel91@gmail.com")
        binding.etPassword.setText("Yankit@123")

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
}