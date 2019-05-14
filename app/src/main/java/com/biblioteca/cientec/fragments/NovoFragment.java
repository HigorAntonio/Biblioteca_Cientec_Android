package com.biblioteca.cientec.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NovoFragment extends BaseFragment {
    private TextView txt;
    private TextView txt2;
    private ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.novo_fragment, container, false);
        txt = (TextView) view.findViewById(R.id.textView);
        txt2 = (TextView) view.findViewById(R.id.textView2);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        Intent it = getActivity().getIntent();
        User user = (User) it.getSerializableExtra("user");
        txt.setText("Você conseguiu "+user.getName()+"! ;)");


        Glide.with(getActivity().getApplicationContext()).asGif()
                .load("https://media.giphy.com/media/XreQmk7ETCak0/giphy.gif")
                .into(imageView);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                .build();

        BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
        Call<String> stringCall = service.getProjects("Bearer "+user.getToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    txt2.setText(responseString);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return view;
    }
}
