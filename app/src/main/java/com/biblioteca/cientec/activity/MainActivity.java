package com.biblioteca.cientec.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.biblioteca.cientec.R;
import com.biblioteca.cientec.fragments.LoginFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }
}
