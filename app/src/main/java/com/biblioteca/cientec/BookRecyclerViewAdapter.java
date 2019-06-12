package com.biblioteca.cientec;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblioteca.cientec.fragments.BookFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public BookRecyclerViewAdapter(Context mContext, ArrayList<String> mNames, ArrayList<String> mImageUrls) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mContext = mContext;
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
                .load(mImageUrls.get(position))
                .into(viewHolder.book_image);

        viewHolder.book_name.setText(mNames.get(position));

        //OnClick do card do livro
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new BookFragment();
                Bundle params = new Bundle();
                params.putString("book_name", mNames.get(position));
                params.putString("image_url", mImageUrls.get(position));
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
        return mNames.size();
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
