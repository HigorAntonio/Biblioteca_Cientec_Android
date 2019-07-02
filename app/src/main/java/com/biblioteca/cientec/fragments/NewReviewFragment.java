package com.biblioteca.cientec.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewReviewFragment extends BaseFragment {
    private Context context;
    private Intent it;
    private User user;
    private Bundle params;
    private Book book;
    private float userRating;
    private RatingBar review_rating;
    private TextInputLayout txt_review;
    private Button btn_postar;
    private ProgressDialog dialog;
    private String commentHTTPMethod;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_review, container, false);
        it = getActivity().getIntent();
        user = (User) it.getSerializableExtra("user");

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Recebe os dados do livro
        params = getArguments();
        book = (Book) params.getSerializable("book");
        userRating = params.getFloat("userRating");
        params.remove("userRating");
        commentHTTPMethod = params.getString("commentHTTPMethod");
        params.remove("commentHTTPMethod");
        Toast.makeText(context, "commentHTTPMethod: "+commentHTTPMethod, Toast.LENGTH_SHORT).show();

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");

        //Troca o menu hamburguer da Navigation Drawer pela seta de voltar
        ActionBarDrawerToggle mToggle = ((HomeActivity)getActivity()).getmToggle();
        mToggle.setDrawerIndicatorEnabled(false);

        review_rating = view.findViewById(R.id.fr_new_review_rating);
        review_rating.setRating(userRating);
        txt_review = view.findViewById(R.id.fr_new_review_review);
        btn_postar = view.findViewById(R.id.fr_new_review_post);
        btn_postar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                btn_postar.setEnabled(false);

                dialog = new ProgressDialog(context);
                dialog.setTitle("");
                dialog.setMessage("Publicando avaliação...");
                dialog.show();

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                        .build();

                BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
                if (commentHTTPMethod.equals("post")) {
                    Call<String> stringCall = service.postNewReview(
                            "Bearer " + user.getToken(),
                            book.getId(), txt_review.getEditText().getText().toString(),
                            (int) review_rating.getRating()
                    );
                    stringCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                String responseString = response.body();

                                Toast.makeText(getActivity().getApplicationContext(), "Avaliação publicada com sucesso", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Não foi possível publicar a avaliação", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            btn_postar.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "Não foi possível publicar a avaliação", Toast.LENGTH_SHORT).show();
                            btn_postar.setEnabled(true);
                        }
                    });
                } else if (commentHTTPMethod.equals("put")) {

                }
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
        //da Actionbar refere de que se trata a tela (Detalhes do livro)
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(book.getTitle());
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("Avaliar este livro");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
