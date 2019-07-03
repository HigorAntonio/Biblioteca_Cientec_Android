package com.biblioteca.cientec.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.BibliotecaCientecAPIService;
import com.biblioteca.cientec.CommentRecyclerViewAdapter;
import com.biblioteca.cientec.Models.Author;
import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.Review;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.R;
import com.biblioteca.cientec.activity.HomeActivity;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BookFragment extends BaseFragment {

    private Context context;
    private Intent it;
    private User user;
    private Book book;
    private Author author;
    private Bundle params;

    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mUserImageUrls = new ArrayList<>();
    //private ArrayList<Integer> mRatings = new ArrayList<>();
    //private ArrayList<String> mRatingDates = new ArrayList<>();
    //private ArrayList<String> mComments = new ArrayList<>();
    private ArrayList<Review> mReviews = new ArrayList<>();
    private RecyclerView commentsRecyclerView;

    private LinearLayout sobre_este_livro;
    private LinearLayout all_reviews;
    private TextView txt_book_name;
    private ImageView book_image;
    private TextView txt_book_author;
    private TextView txt_book_publisher;
    private TextView devolution_date;
    private Button btn_emprestimo;
    private TextView txt_book_description;
    private TextView txt_new_review;
    private RatingBar review_rating;
    private float userRating;
    private LinearLayout my_rating;
    private TextView txt_my_comment;
    private TextView txt_avaliacao;
    private TextView sub_txt_avaliacao;
    private Review myReview;
    private TextView my_comment_user_name;
    private RatingBar my_comment_rating;
    private TextView my_comment_date;
    private CircleImageView my_comment_profile_image;
    private String commentHTTPMethod;
    private TextView txt_book_rating;
    private TextView txt_book_num_pages;
    private TextView txt_book_review_rating;
    private TextView txt_book_num_reviews;
    private TextView txt_book_reviews_num_reviews;
    private RatingBar book_review_rating;
    private double bookRating;
    private int numReviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        it = getActivity().getIntent();
        user = (User) it.getSerializableExtra("user");

        //Torna possível trocar o menu da ActionBar
        setHasOptionsMenu(true);

        //Volta o título e o subtítulo da Actionbar para seus valores iniciais
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("");

        //Troca o menu hamburguer da Navigation Drawer pela seta de voltar
        ActionBarDrawerToggle mToggle = ((HomeActivity)getActivity()).getmToggle();
        mToggle.setDrawerIndicatorEnabled(false);

        //Recebe as informações do livro e do autor, enviados pelo BookFragment através do BookRecyclerViewAdapter
        params = getArguments();
        book = (Book) params.getSerializable("book");
        author = (Author) params.getSerializable("author");
        txt_book_name = view.findViewById(R.id.fr_book_name);
        book_image = view.findViewById(R.id.fr_book_image);
        txt_book_author = view.findViewById(R.id.fr_book_author);
        txt_book_publisher = view.findViewById(R.id.fr_book_publisher);
        txt_book_description = view.findViewById(R.id.fr_book_description);
        txt_book_rating = view.findViewById(R.id.fr_book_book_rating);
        txt_book_num_pages = view.findViewById(R.id.fr_book_num_pages);
        txt_book_review_rating = view.findViewById(R.id.fr_book_book_review_rating);
        txt_book_num_reviews = view.findViewById(R.id.fr_book_num_reviews);
        txt_book_reviews_num_reviews = view.findViewById(R.id.fr_book_reviews_num_reviews);
        book_review_rating = view.findViewById(R.id.fr_book_review_rating_bar);

        //Carrega o nome, imagem da capa e outras informações do livro correto
        // (recebidos do BookFragment) no layout
        txt_book_name.setText(book.getTitle());
        Glide.with(context)
                .asBitmap()
                .load(book.getCoverUrl())
                .into(book_image);
        txt_book_author.setText(author.getName());
        txt_book_publisher.setText(book.getPublisher());
        txt_book_description.setText(book.getDescription());
        txt_book_num_pages.setText(((Integer)book.getNumberOfPages()).toString());

        //Carrega os comentários da parte de "Avaliações e resenhas" no RecyclerView
        commentsRecyclerView = view.findViewById(R.id.fr_book_recyclerView);

        //Preenche a data de devolução com o dia previsto para a entrega caso o
        //usuário pegue o livro emprestado
        devolution_date = view.findViewById(R.id.fr_book_devolution_date);
        devolution_date.setText(getDevolutuionDate());

        btn_emprestimo = view.findViewById(R.id.btn_emprestimo);
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

        txt_new_review = view.findViewById(R.id.fr_book_new_review);
        review_rating = view.findViewById(R.id.fr_book_review_rating);
        my_rating = view.findViewById(R.id.fr_book_my_rating);
        txt_my_comment = view.findViewById(R.id.fr_book_my_comment);
        txt_avaliacao = view.findViewById(R.id.fr_book_txt_avaliacao);
        sub_txt_avaliacao = view.findViewById(R.id.fr_book_sub_txt_avaliacao);
        my_comment_user_name = view.findViewById(R.id.fr_book_my_username);
        my_comment_rating = view.findViewById(R.id.fr_book_my_comment_rating);
        my_comment_date = view.findViewById(R.id.fr_book_data_rating);
        my_comment_profile_image = view.findViewById(R.id.fr_book_comment_profile_image);
        userRating = 0;
        commentHTTPMethod = "";

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
                    myReview = new Review();
                    try {
                        root = new JSONObject(responseString);
                        JSONArray jsonArrayUserReview = root.getJSONArray("userReview");
                        JSONObject jsonUserReview = jsonArrayUserReview.getJSONObject(0);
                        myReview.setUserId(jsonUserReview.optInt("userId"));
                        myReview.setBookId(jsonUserReview.optInt("bookId"));
                        myReview.setRating(jsonUserReview.optInt("rating"));
                        myReview.setReview(jsonUserReview.optString("review"));
                        myReview.setUpdatedAt(jsonUserReview.optString("updatedAt"));
                        Log.d("Review", "userId: "+myReview.getUserId() + " bookId: "+myReview.getBookId()+
                                " review: "+myReview.getReview()+" rating: "+myReview.getRating()+
                                "updatedAt: "+myReview.getUpdatedAt());
                        userRating = myReview.getRating();

                        my_comment_user_name.setText(user.getName());
                        my_comment_rating.setRating(myReview.getRating());
                        txt_my_comment.setText(myReview.getReview());
                        my_comment_date.setText(formataData(myReview.getUpdatedAt()
                                .substring(0, 10)
                                .replace("-", "/")));

                        //Exibe a avaliação do usuário, caso exista uma, ou a ratingBar para o usuario avaliar o livro
                        if (!responseString.equals("[]")) {
                            txt_avaliacao.setText("Sua avaliação");
                            sub_txt_avaliacao.setVisibility(View.GONE);
                            review_rating.setVisibility(View.GONE);
                            my_rating.setVisibility(View.VISIBLE);
                            txt_new_review.setText("EDITE SUA AVALIAÇÃO");
                            commentHTTPMethod = "put";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        txt_avaliacao.setText("Avaliar este livro");
                        sub_txt_avaliacao.setVisibility(View.VISIBLE);
                        review_rating.setVisibility(View.VISIBLE);
                        my_rating.setVisibility(View.GONE);
                        txt_new_review.setText("ESCREVA UMA RESENHA");
                        commentHTTPMethod = "post";
                    }
                    try {
                        JSONArray jsonArrayBookReviewStats = root.getJSONArray("stats");
                        JSONObject jsonBookReviewStats = jsonArrayBookReviewStats.getJSONObject(0);
                        bookRating = jsonBookReviewStats.getDouble("bookRating");
                        numReviews = jsonBookReviewStats.getInt("numReviews");
                        NumberFormat formatarFloat = new DecimalFormat("0.0");
                        txt_book_rating.setText(formatarFloat.format(bookRating).replace(".", ","));
                        txt_book_review_rating.setText(formatarFloat.format(bookRating).replace(".", ","));
                        book_review_rating.setRating((float) bookRating);
                        txt_book_num_reviews.setText(numReviews + " avaliações");
                        txt_book_reviews_num_reviews.setText(((Integer) numReviews).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        mUserNames.clear();
                        mReviews.clear();
                        JSONArray jsonArrayBookAllReviews = root.getJSONArray("reviews");
                        for (int i = 0; i < Math.min(jsonArrayBookAllReviews.length(), 3); i++) {
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
                    }

                } else {
                    Toast.makeText(context,"Não foi possível carregar os dados da sua avaliação do livro",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,
                        "Não foi possível carregar os dados da sua avaliação do livro. Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Carrega a tela com todas as reviews do livro
        all_reviews = view.findViewById(R.id.fr_book_all_reviews);
        all_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment myFragment = new AllCommentsFragment();
                myFragment.setArguments(params);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.containerHome, myFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //Carrega a tela onde o usuário escreve uma review e deixa uma nota para o livro
        txt_new_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.putFloat("userRating", userRating);
                params.putString("commentHTTPMethod", commentHTTPMethod);
                params.putString("userReview", myReview.getReview());
                Fragment myFragment = new NewReviewFragment();
                myFragment.setArguments(params);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.containerHome, myFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        review_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) { // Faz com que o listener não dispare ao redesenhar a tela quando
                    // o onBackPressed do fragment NewReviewFragment for chamado
                    userRating = rating;
                    params.putFloat("userRating", userRating);
                    params.putString("commentHTTPMethod", commentHTTPMethod);
                    params.putString("userReview", myReview.getReview());
                    Fragment myFragment = new NewReviewFragment();
                    myFragment.setArguments(params);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.containerHome, myFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                review_rating.setRating(0);
            }
        });

        txt_my_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_my_comment.getLineCount() == 4) {
                    txt_my_comment.setMaxLines(1000);
                } else {
                    txt_my_comment.setMaxLines(4);
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
        ((HomeActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_book, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

//    private void getRatings() {
//        mUserNames.add("User1");
//        mUserImageUrls.add("https://images-eds-ssl.xboxlive.com/image?url=8Oaj9Ryq1G1_p3lLnXlsaZgGzAie6Mnu24_PawYuDYIoH77pJ.X5Z.MqQPibUVTcS9jr0n8i7LY1tL3U7AiafeupMTZQuDOSyxGOvIx91kUec.3ySDDfcb5rOW0cxonX&format=jpg&h=253&w=253");
//        mRatings.add(4);
//        mRatingDates.add("12/06/2019");
//        mComments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
//                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
//                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
//                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
//                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
//                "culpa qui officia deserunt mollit anim id est laborum.");
//
//        mUserNames.add("User2");
//        mUserImageUrls.add("https://images-eds-ssl.xboxlive.com/image?url=8Oaj9Ryq1G1_p3lLnXlsaZgGzAie6Mnu24_PawYuDYIoH77pJ.X5Z.MqQPibUVTcS9jr0n8i7LY1tL3U7AiafSwJmHWPM6ACTB2wtC7kVEJ2Fdtsnst9_4aGOnnc7Cpc&format=jpg&h=253&w=253");
//        mRatings.add(5);
//        mRatingDates.add("12/06/2019");
//        mComments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
//                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
//                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
//                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
//                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
//                "culpa qui officia deserunt mollit anim id est laborum.");
//
//        mUserNames.add("User3");
//        mUserImageUrls.add("https://images-eds-ssl.xboxlive.com/image?url=8Oaj9Ryq1G1_p3lLnXlsaZgGzAie6Mnu24_PawYuDYIoH77pJ.X5Z.MqQPibUVTcS9jr0n8i7LY1tL3U7Aiaff9fXwZrQpVVndgZJjXjfSUo3nR1fjth9UJh_XO0U7lH&format=jpg&h=253&w=253");
//        mRatings.add(3);
//        mRatingDates.add("12/06/2019");
//        mComments.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
//                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
//                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
//                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
//                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
//                "culpa qui officia deserunt mollit anim id est laborum.");
//    }

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
                mReviews);
        recyclerView.setAdapter(adapter);
    }

    private String formataData(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat brdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return brdf.format(c.getTime());
    }
}
