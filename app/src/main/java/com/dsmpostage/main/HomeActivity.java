package com.dsmpostage.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.dsmpostage.R;
import com.dsmpostage.databinding.WelcomeActivityBinding;

public class HomeActivity  extends AppCompatActivity {

    WelcomeActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding= DataBindingUtil.setContentView(this,R.layout.welcome_activity);



//        Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.slide_up);
//        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(1000);
//        rotate.setInterpolator(new LinearInterpolator());
//        binding.img.startAnimation(rotate);
//        binding.tv.startAnimation(animSlideDown);

        binding.img.setImageResource(R.drawable.ic__lock_one);
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(binding.img, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(binding.img, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                 binding.img.setImageResource(R.drawable.ic__lock_three);
                oa2.start();
            }
        });
        oa1.start();
        oa1.setDuration(1000);
        oa2.setDuration(1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(HomeActivity.this,BaseActivity.class));
            }
        },2500);
    }

    @Override
    public void onBackPressed() {

    }
}
