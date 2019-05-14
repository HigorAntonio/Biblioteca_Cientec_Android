package com.biblioteca.cientec.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.biblioteca.cientec.R;
import com.biblioteca.cientec.fragments.NovoFragment;
import com.biblioteca.cientec.fragments.RegisterOrLoginFragment;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerHome, new NovoFragment())
                    .commit();
        }
    }
}
