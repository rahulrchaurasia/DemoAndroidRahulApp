package com.policyboss.demoandroidapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.policyboss.demoandroidapp.R;
import com.policyboss.demoandroidapp.UI.AutoCompleteDemo2.AutoCompDemo2Activity;

import kotlin.coroutines.CoroutineContext;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;

    private long SPLASH_DISPLAY_LENGTH = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView=findViewById(R.id.ImageView);

        Animation animation1= AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        // final Animation animation2=AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        // nikhil updated here : R.anim.abc_fade_out not found
        final Animation animation2=AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);


        /*
        imageView.setAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imageView.setAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                finish();
                Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

         */
        delaySometimes();
    }

    private void delaySometimes(){


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        }, SPLASH_DISPLAY_LENGTH);



    }
}