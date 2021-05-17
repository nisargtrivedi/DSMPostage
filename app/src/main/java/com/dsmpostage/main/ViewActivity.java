package com.dsmpostage.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.dsmpostage.R;
import com.dsmpostage.databinding.ViewImageBinding;

public class ViewActivity extends AppCompatActivity {

    ViewImageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.view_image);
        binding.btnClosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(getIntent().getExtras()!=null){
            Glide
                    .with(this)
                    .load(getIntent().getStringExtra("data"))
                    .centerCrop()
                    .into(binding.imgView);
            binding.imgView.setRotation(90);
        }

    }

}
