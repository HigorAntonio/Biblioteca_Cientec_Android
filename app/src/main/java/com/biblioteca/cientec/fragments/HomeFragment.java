package com.biblioteca.cientec.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.Author;
import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.BookRecyclerViewAdapter;
import com.biblioteca.cientec.activity.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends BaseFragment {

    private View view;
    private Context context;
    private Intent it;
    private ArrayList<Book> mBooks0 = new ArrayList<>();
    private ArrayList<Author> mAuthors0 = new ArrayList<>();
    private ArrayList<Book> mBooks1 = new ArrayList<>();
    private ArrayList<Author> mAuthors1 = new ArrayList<>();
    private ArrayList<Book> mBooks2 = new ArrayList<>();
    private ArrayList<Author> mAuthors2 = new ArrayList<>();
    //private ArrayList<String> mNames = new ArrayList<>();
    //private ArrayList<String> mImageUrls = new ArrayList<>();
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        it = getActivity().getIntent();
        user = (User) it.getSerializableExtra("user");

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");

        //Troca a seta de voltar pelo menu hamburguer da Navigation Drawer
        ActionBarDrawerToggle mToggle = ((HomeActivity)getActivity()).getmToggle();
        mToggle.setDrawerIndicatorEnabled(true);

        /*if (mNames.isEmpty()) {
            getImages();
        }
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
        initRecyclerView(recyclerView5);*/

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
        // Card view da categoria0
        Call<String> stringCall = service.getRecentBooks("Bearer "+user.getToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    Log.d("Author", "res: "+responseString);
                    JSONArray root = null;
                    try {
                        root = new JSONArray(responseString);
                        mBooks0.clear();
                        mAuthors0.clear();
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject jsonBook = root.getJSONObject(i);
                            Book b = new Book();
                            b.setId(jsonBook.optInt("bookId"));
                            b.setIsbn(jsonBook.optString("isbn"));
                            b.setTitle(jsonBook.optString("title"));
                            b.setOriginalTile(jsonBook.optString("originalTitle"));
                            b.setEdition(jsonBook.optInt("edition"));
                            b.setPublisher(jsonBook.optString("publisher"));
                            b.setCoverUrl(jsonBook.optString("coverUrl").replace("localhost", "10.0.2.2"));
                            b.setCoverName(jsonBook.optString("coverName"));
                            b.setDescription(jsonBook.optString("description"));
                            b.setAuthorId(jsonBook.optString("authorId"));
                            b.setNumberOfPages(jsonBook.optInt("numberOfPages"));
                            b.setLanguage(jsonBook.optString("language"));
                            mBooks0.add(b);
                            Author a = new Author();
                            a.setId(jsonBook.optInt("authorId"));
                            a.setName(jsonBook.optString("authorName"));
                            a.setAbout(jsonBook.optString("authorAbout"));
                            mAuthors0.add(a);
                            Log.d("Author", "id: "+a.getId()+" name: "+a.getName()+" about: "+a.getAbout());
                        }

                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView0);
                        initRecyclerView0(recyclerView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context,"Não foi possível carregar os dados",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,
                        "Não foi possível carregar os dados. Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Card view da categoria1
        stringCall = service.getPopularBooks("Bearer "+user.getToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    Log.d("Author", "res: "+responseString);
                    JSONArray root = null;
                    try {
                        root = new JSONArray(responseString);
                        mBooks1.clear();
                        mAuthors1.clear();
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject jsonBook = root.getJSONObject(i);
                            Book b = new Book();
                            b.setId(jsonBook.optInt("bookId"));
                            b.setIsbn(jsonBook.optString("isbn"));
                            b.setTitle(jsonBook.optString("title"));
                            b.setOriginalTile(jsonBook.optString("originalTitle"));
                            b.setEdition(jsonBook.optInt("edition"));
                            b.setPublisher(jsonBook.optString("publisher"));
                            b.setCoverUrl(jsonBook.optString("coverUrl").replace("localhost", "10.0.2.2"));
                            b.setCoverName(jsonBook.optString("coverName"));
                            b.setDescription(jsonBook.optString("description"));
                            b.setAuthorId(jsonBook.optString("authorId"));
                            b.setNumberOfPages(jsonBook.optInt("numberOfPages"));
                            b.setLanguage(jsonBook.optString("language"));
                            mBooks1.add(b);
                            Author a = new Author();
                            a.setId(jsonBook.optInt("authorId"));
                            a.setName(jsonBook.optString("authorName"));
                            a.setAbout(jsonBook.optString("authorAbout"));
                            mAuthors1.add(a);
                            Log.d("Author", "id: "+a.getId()+" name: "+a.getName()+" about: "+a.getAbout());
                        }

                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
                        initRecyclerView1(recyclerView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context,"Não foi possível carregar os dados",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,
                        "Não foi possível carregar os dados. Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Card view da categoria2
        stringCall = service.getBestRatedBooks("Bearer "+user.getToken());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    Log.d("Author", "res: "+responseString);
                    JSONArray root = null;
                    try {
                        root = new JSONArray(responseString);
                        mBooks2.clear();
                        mAuthors2.clear();
                        for (int i = 0; i < root.length(); i++) {
                            JSONObject jsonBook = root.getJSONObject(i);
                            Book b = new Book();
                            b.setId(jsonBook.optInt("bookId"));
                            b.setIsbn(jsonBook.optString("isbn"));
                            b.setTitle(jsonBook.optString("title"));
                            b.setOriginalTile(jsonBook.optString("originalTitle"));
                            b.setEdition(jsonBook.optInt("edition"));
                            b.setPublisher(jsonBook.optString("publisher"));
                            b.setCoverUrl(jsonBook.optString("coverUrl").replace("localhost", "10.0.2.2"));
                            b.setCoverName(jsonBook.optString("coverName"));
                            b.setDescription(jsonBook.optString("description"));
                            b.setAuthorId(jsonBook.optString("authorId"));
                            b.setNumberOfPages(jsonBook.optInt("numberOfPages"));
                            b.setLanguage(jsonBook.optString("language"));
                            mBooks2.add(b);
                            Author a = new Author();
                            a.setId(jsonBook.optInt("authorId"));
                            a.setName(jsonBook.optString("authorName"));
                            a.setAbout(jsonBook.optString("authorAbout"));
                            mAuthors2.add(a);
                            Log.d("Author", "id: "+a.getId()+" name: "+a.getName()+" about: "+a.getAbout());
                        }

                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
                        initRecyclerView2(recyclerView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context,"Não foi possível carregar os dados",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,
                        "Não foi possível carregar os dados. Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Limpa a backstack caso o usuário use a navegação pelos fragments e
        //retorne à tela inicial
        getActivity()
                .getSupportFragmentManager()
                .popBackStack(null, getActivity().
                        getSupportFragmentManager()
                        .POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

//    private void getImages() {
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/102/42865102.jpg");
//        mNames.add("Sapiens - Uma Breve História da Humanidade");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/354/1692354.jpg");
//        mNames.add("O Conto da Aia");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/877/1831877.jpg");
//        mNames.add("A Revolução dos Bichos");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/750/2823750.jpg");
//        mNames.add("1984");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/090/2011319090.jpg");
//        mNames.add("O Espadachim de Carvao");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/877/22137877.jpg");
//        mNames.add("A Batalha do Apocalipse: da Queda dos Anjos ao Crepúsculo do Mundo");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/272/2011573272.jpg");
//        mNames.add("O Ultimo Reino - Cronicas Saxonicas - Vol. 1");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/671/30142671.jpg");
//        mNames.add("Rápido e Devagar");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/846/37019846.jpg");
//        mNames.add("Androides Sonham Com Ovelhas Elétricas?");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/426/42747426.jpg");
//        mNames.add("Eu, Robo");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/992/23052992.jpg");
//        mNames.add("Jurassic Park");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/860/23052860.jpg");
//        mNames.add("O Planeta dos Macacos");
//
//        mImageUrls.add("https://statics.livrariacultura.net.br/products/capas/016/3152016.jpg");
//        mNames.add("Laranja Mecânica");
//
//    }

    private void initRecyclerView0(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BookRecyclerViewAdapter adapter = new BookRecyclerViewAdapter(context, /*mNames, mImageUrls*/ mBooks0, mAuthors0, user);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView1(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BookRecyclerViewAdapter adapter = new BookRecyclerViewAdapter(context, /*mNames, mImageUrls*/ mBooks1, mAuthors1, user);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView2(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BookRecyclerViewAdapter adapter = new BookRecyclerViewAdapter(context, /*mNames, mImageUrls*/ mBooks2, mAuthors2, user);
        recyclerView.setAdapter(adapter);
    }
}
