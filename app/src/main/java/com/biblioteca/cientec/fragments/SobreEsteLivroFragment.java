package com.biblioteca.cientec.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;

public class SobreEsteLivroFragment extends BaseFragment {

    private Bundle params;
    private String book_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sobre_este_livro, container, false);

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Recebe o nome e a imagem do livro do BookFragment
        params = getArguments();
        book_name = params.getString("book_name");

        return view;
    }

    //Altera o menu da ActionBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        //Coloca o nome do livro como título da Actionbar e o subtítulo
        //da Actionbar refere de que se trata a tela (Detalhes do livro)
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(book_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("Detalhes");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Recebe o nome e a imagem do livro do BookFragment
        params = getArguments();
        book_name = params.getString("book_name");
    }

    @Override
    public void onResume() {
        super.onResume();
        //Recebe o nome e a imagem do livro do BookFragment
        params = getArguments();
        book_name = params.getString("book_name");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }
}
