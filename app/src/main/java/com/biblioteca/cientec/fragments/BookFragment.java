package com.biblioteca.cientec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.CommentRecyclerViewAdapter;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookFragment extends BaseFragment {

    private Context context;
    private String book_name;
    private String image_url;
    private Bundle params;

    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mUserImageUrls = new ArrayList<>();
    private ArrayList<Integer> mRatings = new ArrayList<>();
    private ArrayList<String> mRatingDates = new ArrayList<>();
    private ArrayList<String> mComments = new ArrayList<>();

    private LinearLayout sobre_este_livro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Troca o menu hamburguer da Navigation Drawer pela seta de voltar
        ActionBarDrawerToggle mToggle = ((HomeActivity)getActivity()).getmToggle();
        mToggle.setDrawerIndicatorEnabled(false);

        //Recebe o nome e a imagem do livro do BookFragment
        params = getArguments();
        book_name = params.getString("book_name");
        image_url = params.getString("image_url");

        TextView txt_book_name = view.findViewById(R.id.fr_book_name);
        ImageView book_image = view.findViewById(R.id.fr_book_image);

        //Carrega o nome e imagem da capa do livro correto (recebidos do BookFragment) no layout
        txt_book_name.setText(book_name);
        Glide.with(getContext())
                .asBitmap()
                .load(image_url)
                .into(book_image);

        //Carrega os comentários da parte de "Avaliações e resenhas" no RecyclerView
        if (mUserNames.isEmpty()) {
            getRatings();
        }
        RecyclerView recyclerView = view.findViewById(R.id.fr_book_recyclerView);
        initRecyclerView(recyclerView);

        //Preenche a data de devolução com o dia previsto para a entrega caso o
        //usuário pegue o livro emprestado
        final TextView devolution_date = view.findViewById(R.id.fr_book_devolution_date);
        devolution_date.setText(getDevolutuionDate());

        final Button btn_emprestimo = view.findViewById(R.id.btn_emprestimo);
        btn_emprestimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_emprestimo.getText().equals("Pegar emprestado")) {
                    btn_emprestimo.setBackgroundResource(R.drawable.blue_button_ripple);
                    btn_emprestimo.setText("Devolver livro");

                    //Atualiza a data de devolução
                    devolution_date.setText(getDevolutuionDate());
                } else if (btn_emprestimo.getText().equals("Devolver livro")) {
                    btn_emprestimo.setBackgroundResource(R.drawable.green_button_ripple);
                    btn_emprestimo.setText("Pegar emprestado");
                }
            }
        });

        //Carrega a tela com as informações do livro
        sobre_este_livro = view.findViewById(R.id.fr_book_sobre_este_livro);
        sobre_este_livro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new SobreEsteLivroFragment();
                myFragment.setArguments(params);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.containerHome, myFragment);
                ft.addToBackStack(null);
                ft.commit();
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
        ((HomeActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_book, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getRatings() {
        mUserNames.add("User1");
        mUserImageUrls.add("https://images-eds-ssl.xboxlive.com/image?url=8Oaj9Ryq1G1_p3lLnXlsaZgGzAie6Mnu24_PawYuDYIoH77pJ.X5Z.MqQPibUVTcS9jr0n8i7LY1tL3U7AiafeupMTZQuDOSyxGOvIx91kUec.3ySDDfcb5rOW0cxonX&format=jpg&h=253&w=253");
        mRatings.add(4);
        mRatingDates.add("12/06/2019");
        mComments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.");

        mUserNames.add("User2");
        mUserImageUrls.add("https://images-eds-ssl.xboxlive.com/image?url=8Oaj9Ryq1G1_p3lLnXlsaZgGzAie6Mnu24_PawYuDYIoH77pJ.X5Z.MqQPibUVTcS9jr0n8i7LY1tL3U7AiafSwJmHWPM6ACTB2wtC7kVEJ2Fdtsnst9_4aGOnnc7Cpc&format=jpg&h=253&w=253");
        mRatings.add(5);
        mRatingDates.add("12/06/2019");
        mComments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.");

        mUserNames.add("User3");
        mUserImageUrls.add("https://images-eds-ssl.xboxlive.com/image?url=8Oaj9Ryq1G1_p3lLnXlsaZgGzAie6Mnu24_PawYuDYIoH77pJ.X5Z.MqQPibUVTcS9jr0n8i7LY1tL3U7Aiaff9fXwZrQpVVndgZJjXjfSUo3nR1fjth9UJh_XO0U7lH&format=jpg&h=253&w=253");
        mRatings.add(3);
        mRatingDates.add("12/06/2019");
        mComments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.");
    }

    private String getDevolutuionDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Now use today date.
        calendar.add(Calendar.DATE, 30); // Adding 30 days
        String date = df.format(calendar.getTime());
        return date;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CommentRecyclerViewAdapter adapter = new CommentRecyclerViewAdapter(context, mUserNames, mUserImageUrls,
                mRatings, mRatingDates, mComments);
        recyclerView.setAdapter(adapter);
    }
}
