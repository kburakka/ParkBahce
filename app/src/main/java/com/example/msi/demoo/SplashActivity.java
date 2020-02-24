package com.example.msi.demoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1700;

    private ImageView image;
    private TextView ankageo;

    private Animation upToDown,downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image = findViewById(R.id.splash_image);
        ankageo = findViewById(R.id.splash_ankageo);
        upToDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_to_down);
        downToUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_to_up);

        image.setAnimation(upToDown);
        ankageo.setAnimation(downToUp);

        /* New Handler to start the TempMainActivity close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

//        startService(new Intent(getBaseContext(), ClearCacheInService.class));
    }
}
