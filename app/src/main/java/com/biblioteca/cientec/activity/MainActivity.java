package com.biblioteca.cientec.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.biblioteca.cientec.R;

public class MainActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(MainActivity.this, RegisterLoginActivity.class);
                startActivity(it);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
