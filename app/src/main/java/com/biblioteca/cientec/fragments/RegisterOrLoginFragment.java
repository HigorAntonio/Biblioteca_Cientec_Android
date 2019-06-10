package com.biblioteca.cientec.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.biblioteca.cientec.R;

public class RegisterOrLoginFragment extends BaseFragment {
    private Button btnInscrever;
    private Button btnEntrar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_or_login, container, false);

        btnInscrever = (Button) view.findViewById(R.id.inscrever);
        btnEntrar = (Button) view.findViewById(R.id.entrar);

        btnInscrever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.rlcontainer, new UserRegisterFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.rlcontainer, new LoginFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }
}
