package com.biblioteca.cientec.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.BookRecyclerViewAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends BaseFragment {

    private Context context;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Intent it = getActivity().getIntent();
        User user = (User) it.getSerializableExtra("user");

        getImages();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView(recyclerView);
        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView1);
        initRecyclerView(recyclerView1);
        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        initRecyclerView(recyclerView2);
        RecyclerView recyclerView3 = view.findViewById(R.id.recyclerView3);
        initRecyclerView(recyclerView3);
        RecyclerView recyclerView4 = view.findViewById(R.id.recyclerView4);
        initRecyclerView(recyclerView4);
        RecyclerView recyclerView5 = view.findViewById(R.id.recyclerView5);
        initRecyclerView(recyclerView5);

        CardView cardViewCategory0 = view.findViewById(R.id.cardView_category0);
        cardViewCategory0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "cardViewCategory0 clicado", Toast.LENGTH_SHORT).show();
            }
        });

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

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void getImages() {
        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/102/42865102.jpg");
        mNames.add("Sapiens - Uma Breve História da Humanidade");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/354/1692354.jpg");
        mNames.add("O Conto da Aia");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/877/1831877.jpg");
        mNames.add("A Revolução dos Bichos");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/750/2823750.jpg");
        mNames.add("1984");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/090/2011319090.jpg");
        mNames.add("O Espadachim de Carvao");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/877/22137877.jpg");
        mNames.add("A Batalha do Apocalipse: da Queda dos Anjos ao Crepúsculo do Mundo");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/272/2011573272.jpg");
        mNames.add("O Ultimo Reino - Cronicas Saxonicas - Vol. 1");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/671/30142671.jpg");
        mNames.add("Rápido e Devagar");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/846/37019846.jpg");
        mNames.add("Androides Sonham Com Ovelhas Elétricas?");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/426/42747426.jpg");
        mNames.add("Eu, Robo");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/992/23052992.jpg");
        mNames.add("Jurassic Park");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/860/23052860.jpg");
        mNames.add("O Planeta dos Macacos");

        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/016/3152016.jpg");
        mNames.add("Laranja Mecânica");

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BookRecyclerViewAdapter adapter = new BookRecyclerViewAdapter(context, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }
}
