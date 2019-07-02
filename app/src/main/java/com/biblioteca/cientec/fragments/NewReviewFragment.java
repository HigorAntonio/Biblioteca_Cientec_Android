package com.biblioteca.cientec.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;

public class NewReviewFragment extends BaseFragment {
    private Context context;
    private Intent it;
    private User user;
    private Bundle params;
    private Book book;
    private float userRating;
    private RatingBar review_rating;

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
        Toast.makeText(context, "commentHTTPMethod: "+params.getString("commentHTTPMethod"), Toast.LENGTH_SHORT).show();

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");

        //Troca o menu hamburguer da Navigation Drawer pela seta de voltar
        ActionBarDrawerToggle mToggle = ((HomeActivity)getActivity()).getmToggle();
        mToggle.setDrawerIndicatorEnabled(false);

        review_rating = view.findViewById(R.id.fr_new_review_rating);
        review_rating.setRating(userRating);

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
