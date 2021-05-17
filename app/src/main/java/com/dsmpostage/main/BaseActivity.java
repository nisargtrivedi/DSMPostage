package com.dsmpostage.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dsmpostage.GeneralSettingsFragment;
import com.dsmpostage.HomeFragment;
import com.dsmpostage.LogFragment;
import com.dsmpostage.ProfileFragment;
import com.dsmpostage.R;
import com.dsmpostage.ScanFragment;
import com.dsmpostage.databinding.MainContainerActivityBinding;
import com.dsmpostage.utility.Util;

public class BaseActivity extends AppCompatActivity {


    public MainContainerActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding= DataBindingUtil.setContentView(this,R.layout.main_container_activity);
        replaceFragment(new HomeFragment());
        menuSelection();

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
    private void menuSelection(){
        binding.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTitle.setText("PROFILE SETTINGS");
                replaceFragment(new ProfileFragment());
                Util.setBg(1,binding.img1,binding.img2,binding.img3,binding.img4,binding.img5);
            }
        });
        binding.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTitle.setText("HOME");
                replaceFragment(new HomeFragment());
                Util.setBg(2,binding.img1,binding.img2,binding.img3,binding.img4,binding.img5);
            }
        });
        binding.ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTitle.setText("SCAN QR CODE");
                replaceFragment(new ScanFragment());
                Util.setBg(3,binding.img1,binding.img2,binding.img3,binding.img4,binding.img5);
            }
        });
        binding.ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTitle.setText("ACTIVITY LOGS");
                replaceFragment(new LogFragment());
                Util.setBg(4,binding.img1,binding.img2,binding.img3,binding.img4,binding.img5);
            }
        });
        binding.ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvTitle.setText("GENERAL SETTINGS");
                replaceFragment(new GeneralSettingsFragment());
                Util.setBg(5,binding.img1,binding.img2,binding.img3,binding.img4,binding.img5);
            }
        });
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm, null);
        dialogBuilder.setView(dialogView);

        TextView tvMsg=dialogView.findViewById(R.id.tvMsg);
        com.google.android.material.button.MaterialButton btnCancel =  dialogView.findViewById(R.id.btnCancel);
        com.google.android.material.button.MaterialButton btnOk =  dialogView.findViewById(R.id.btnOk);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        tvMsg.setText("Are you sure want exit from this application ?");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishAffinity();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


//        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Close Application")
//                .setMessage("Are you sure you want exit from this application?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }
}
