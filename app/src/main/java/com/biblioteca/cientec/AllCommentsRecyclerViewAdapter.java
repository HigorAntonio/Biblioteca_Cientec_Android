package com.biblioteca.cientec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.biblioteca.cientec.Models.Review;
import com.biblioteca.cientec.Models.User;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllCommentsRecyclerViewAdapter extends RecyclerView.Adapter<AllCommentsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mUserNames;
    private ArrayList<String> mUserImageUrls;
    private ArrayList<Review> mReviews;
    private Context mContext;

    public AllCommentsRecyclerViewAdapter(Context mContext, ArrayList<String> mUserNames,
                                      ArrayList<String> mUserImageUrls, ArrayList<Review> mReviews) {
        this.mUserNames = mUserNames;
        this.mUserImageUrls = mUserImageUrls;
        this.mReviews = mReviews;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_all_comments, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
//        Glide.with(mContext)
//                .asBitmap()
//                .load(mUserImageUrls.get(position))
//                .into(viewHolder.profile_image);
        viewHolder.username.setText(mUserNames.get(position));
        viewHolder.rating.setRating(mReviews.get(position).getRating());
        viewHolder.data_rating.setText(formataData(mReviews
                .get(position).getUpdatedAt().substring(0, 10)
                .replace("-", "/")));
        viewHolder.comment.setText(mReviews.get(position).getReview());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.comment.getLineCount() == 4) {
                    viewHolder.comment.setMaxLines(1000);
                } else {
                    viewHolder.comment.setMaxLines(4);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        CircleImageView profile_image;
        RatingBar rating;
        TextView data_rating;
        TextView comment;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.comment_all_book_username);
            this.profile_image = itemView.findViewById(R.id.comment_all_book_profile_image);
            this.rating = itemView.findViewById(R.id.comment_all_book_rating);
            this.data_rating = itemView.findViewById(R.id.comment_all_book_data_rating);
            this.comment = itemView.findViewById(R.id.comment_all_book_comment);
            this.parentLayout = itemView.findViewById(R.id.comment_all_book_parent_layout);
        }
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

