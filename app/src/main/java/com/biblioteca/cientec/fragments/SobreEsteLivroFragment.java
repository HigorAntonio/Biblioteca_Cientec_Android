package com.biblioteca.cientec.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.Review;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SobreEsteLivroFragment extends BaseFragment {

    private Context context;
    private Intent it;
    private User user;
    private Bundle params;
    private Book book;
    private TextView txt_book_description;
    private TextView txt_original_title;
    private TextView txt_publisher;
    private TextView txt_edition;
    private TextView txt_num_pages;
    private TextView txt_isbn;
    private TextView txt_language;
    private TextView txt_genres;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sobre_este_livro, container, false);

        it = getActivity().getIntent();
        user = (User) it.getSerializableExtra("user");

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Recebe os dados do livro
        params = getArguments();
        book = (Book) params.getSerializable("book");

        txt_book_description = view.findViewById(R.id.fr_sobre_este_livro_description);
        txt_publisher = view.findViewById(R.id.fr_sobre_este_livro_publisher);
        txt_original_title = view.findViewById(R.id.fr_sobre_este_livro_original_title);
        txt_edition = view.findViewById(R.id.fr_sobre_este_livro_edition);
        txt_num_pages = view.findViewById(R.id.fr_sobre_este_livro_num_pages);
        txt_isbn = view.findViewById(R.id.fr_sobre_este_livro_isbn);
        txt_language = view.findViewById(R.id.fr_sobre_este_livro_language);
        txt_genres = view.findViewById(R.id.fr_sobre_este_livro_genres);
        //Coloca os valores recebidos em suas devidas posições no layout
        txt_book_description.setText(book.getDescription());
        txt_original_title.setText(book.getOriginalTile());
        if (book.getPublisher().equals("")) {
            txt_publisher.setText("Indisponível");
        } else {
            txt_publisher.setText(book.getPublisher());
        }
        if (book.getEdition() == -1) {
            txt_edition.setText("Indisponível");
        } else {
            txt_edition.setText(((Integer)book.getEdition()).toString());
        }
        txt_num_pages.setText(((Integer)book.getNumberOfPages()).toString());
        if (book.getIsbn().equals("")) {
            txt_isbn.setText("Indisponível");
        } else {
            txt_isbn.setText(book.getIsbn());
        }
        txt_language.setText(book.getLanguage());

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BibliotecaCientecAPIService.BASE_URL)
                .build();

        BibliotecaCientecAPIService service = retrofit.create(BibliotecaCientecAPIService.class);
        Call<String> stringCall = service.getBookGenres("Bearer "+user.getToken(),
                book.getId());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();

                    JSONObject root = null;
                    String genres = "";
                    try {
                        root = new JSONObject(responseString);
                        JSONArray jsonArrayBookGenres = root.getJSONArray("bookGenres");
                        genres += jsonArrayBookGenres.getJSONObject(0).optString("name");
                        for (int i = 1; i < jsonArrayBookGenres.length(); i++) {
                            genres += " / " + jsonArrayBookGenres.getJSONObject(i).optString("name");;
                        }
                        txt_genres.setText(genres);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context,"Não foi possível carregar os gêneros do livro",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,
                        "Não foi possível carregar os gêneros do livro. Verifique sua conexão",
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
        //da Actionbar refere de que se trata a tela (Detalhes do livro)
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(book.getTitle());
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("Detalhes");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }
}
