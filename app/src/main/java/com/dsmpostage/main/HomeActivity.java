package com.dsmpostage.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsmpostage.R;

public class HomeActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        ImageView img=findViewById(R.id.img);

        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setInterpolator(new LinearInterpolator());
        img.startAnimation(rotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();startActivity(new Intent(HomeActivity.this,BaseActivity.class));
            }
        },2500);
    }

    @Override
    public void onBackPressed() {

    }
}
