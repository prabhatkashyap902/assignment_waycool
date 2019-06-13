package com.prodev.assignment_waycool;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_screen extends AppCompatActivity {
    ImageView im;
    TextView txx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Animation an_1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate1);
        Animation an_2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        im = findViewById(R.id.icon_splass);
        txx = findViewById(R.id.splash_tx);

        im.setAnimation(an_1);
        txx.setAnimation(an_2);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(Splash_screen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
