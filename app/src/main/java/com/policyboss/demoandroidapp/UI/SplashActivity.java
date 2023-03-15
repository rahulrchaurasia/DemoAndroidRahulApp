package com.policyboss.demoandroidapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.policyboss.demoandroidapp.R;
import com.policyboss.demoandroidapp.UI.AutoCompleteDemo2.AutoCompDemo2Activity;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView=findViewById(R.id.ImageView);

        Animation animation1= AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        // final Animation animation2=AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        // nikhil updated here : R.anim.abc_fade_out not found
        final Animation animation2=AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);



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
                Intent intent=new Intent(SplashActivity.this, AutoCompDemo2Activity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
}