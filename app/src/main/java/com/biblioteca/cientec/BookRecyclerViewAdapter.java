package com.biblioteca.cientec;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.Models.Author;
import com.biblioteca.cientec.Models.Book;
import com.biblioteca.cientec.Models.User;
import com.biblioteca.cientec.fragments.BookFragment;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    //private ArrayList<String> mNames = new ArrayList<>();
    //private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<Book> mBooks = new ArrayList<>();
    private ArrayList<Author> mAuthors = new ArrayList<>();
    private Context mContext;
    private User user;

    public BookRecyclerViewAdapter(Context mContext, /*ArrayList<String> mNames, ArrayList<String> mImageUrls,*/
                                    ArrayList<Book> mBooks, ArrayList<Author> mAuthors, User user) {
        //this.mNames = mNames;
        //this.mImageUrls = mImageUrls;
        this.mBooks = mBooks;
        this.mAuthors = mAuthors;
        this.mContext = mContext;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_book, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mBooks.get(position).getCoverUrl())
                .into(viewHolder.book_image);

        viewHolder.book_name.setText(mBooks.get(position).getTitle());

        //OnClick do card do livro
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new BookFragment();
                Bundle params = new Bundle();
                params.putSerializable("book", mBooks.get(position));
                params.putSerializable("author", mAuthors.get(position));
                myFragment.setArguments(params);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containerHome, myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView book_image;
        TextView book_name;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.book_image = itemView.findViewById(R.id.book_image);
            this.book_name = itemView.findViewById(R.id.book_name);
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
