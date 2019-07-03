package com.biblioteca.cientec.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biblioteca.cientec.AllCommentsRecyclerViewAdapter;
import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.Review;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
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

public class AllCommentsFragment extends BaseFragment {

    private Context context;
    private Intent it;
    private User user;
    private Bundle params;
    private Book book;
    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mUserImageUrls = new ArrayList<>();
    private ArrayList<Review> mReviews = new ArrayList<>();
    private RecyclerView commentsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_comments, container, false);

        it = getActivity().getIntent();
        user = (User) it.getSerializableExtra("user");

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Recebe os dados do livro
        params = getArguments();
        book = (Book) params.getSerializable("book");

        commentsRecyclerView = view.findViewById(R.id.fr_all_comments_recyclerView);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                .build();

        BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
        // Solicita os dados da avaliação do usuário para o livro selecionado e retorna os
        //dados caso o usuário tenha feito uma avaliação
        Call<String> stringCall = service.getMyReview("Bearer "+user.getToken(),
                book.getId());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    Log.d("Review", "resposta: "+responseString);
                    JSONObject root = null;
                    try {
                        root = new JSONObject(responseString);
                        mUserNames.clear();
                        mReviews.clear();
                        JSONArray jsonArrayBookAllReviews = root.getJSONArray("reviews");
                        for (int i = 0; i < jsonArrayBookAllReviews.length(); i++) {
                            JSONObject jsonBookAllReviews = jsonArrayBookAllReviews.getJSONObject(i);
                            if (!jsonBookAllReviews.optString("review").equals("")) {
                                mUserNames.add(jsonBookAllReviews.optString("userName"));
                                Review r = new Review();
                                r.setUserId(jsonBookAllReviews.optInt("userId"));
                                r.setBookId(jsonBookAllReviews.optInt("bookId"));
                                r.setRating(jsonBookAllReviews.optInt("rating"));
                                r.setReview(jsonBookAllReviews.optString("review"));
                                r.setUpdatedAt(jsonBookAllReviews.optString("updatedAt"));
                                mReviews.add(r);
                            }
                        }
                        //Carrega os comentários da parte de "Avaliações e resenhas" no RecyclerView
                        initRecyclerView(commentsRecyclerView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context,"Não foi possível carregar as avaliações do livro",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,"Não foi possível carregar as avaliações do livro",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,
                        "Não foi possível carregar as avaliações do livro. Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    //Altera o menu da ActionBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        //Coloca o nome do livro como título da Actionbar e o subtítulo
        //da Actionbar refere de que se trata a tela (Avaliações e resenhas do livro)
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(book.getTitle());
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("Avaliações e resenhas");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AllCommentsRecyclerViewAdapter adapter = new AllCommentsRecyclerViewAdapter(context, mUserNames, mUserImageUrls,
                mReviews);
        recyclerView.setAdapter(adapter);
    }
}
