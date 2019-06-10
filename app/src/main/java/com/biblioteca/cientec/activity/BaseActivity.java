package com.biblioteca.cientec.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.biblioteca.cientec.R;

public class BaseActivity extends AppCompatActivity {

    protected void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
