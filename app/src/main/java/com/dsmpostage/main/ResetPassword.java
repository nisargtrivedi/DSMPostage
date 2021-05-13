package com.dsmpostage.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.dsmpostage.R;
import com.dsmpostage.databinding.ActivityResetPasswordBinding;
import com.dsmpostage.utility.Util;

public class ResetPassword extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_reset_password);


        if(getIntent().getExtras()!=null) {


            binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(TextUtils.isEmpty(binding.edtEmail.getText().toString())){
                        Util.showDialog(ResetPassword.this,"Please enter email address");
                    }else if(!Util.isValidEmail(binding.edtEmail.getText().toString())){
                        Util.showDialog(ResetPassword.this,"Please enter proper email address");
                    }else if(TextUtils.isEmpty(binding.etPassword.getText().toString())){
                        Util.showDialog(ResetPassword.this,"Please enter old password");
                    }else if(TextUtils.isEmpty(binding.etNewpassword.getText().toString())){
                        Util.showDialog(ResetPassword.this,"Please enter new password");
                    }else {
                        if(DSMPostage.getInstance().isNetworkAvailable()) {
                            ChallengeContinuation continuation = (ChallengeContinuation) getIntent().getSerializableExtra("config");
                            NewPasswordContinuation newPasswordContinuation = (NewPasswordContinuation) continuation;
                            newPasswordContinuation.setPassword(binding.etNewpassword.getText().toString());
                            continuation.continueTask();
                        }else{
                            Util.showDialog(ResetPassword.this,"No Internet Available");
                        }
                    }
                }
            });

        }
    }
}
