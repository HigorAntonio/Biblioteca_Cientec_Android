package com.biblioteca.cientec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.biblioteca.cientec.R;
import com.biblioteca.cientec.fragments.LoginFragment;
import com.biblioteca.cientec.fragments.RegisterOrLoginFragment;

public class RegisterLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        Intent it = new Intent();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.rlcontainer, new RegisterOrLoginFragment())
                    .commit();
        }
    }
}
