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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mUserNames;
    private ArrayList<String> mUserImageUrls;
    private ArrayList<Integer> mRatings;
    private ArrayList<String> mRatingDates;
    private ArrayList<String> mComments;
    private Context mContext;

    public CommentRecyclerViewAdapter(Context mContext, ArrayList<String> mUserNames,
                                      ArrayList<String> mUserImageUrls, ArrayList<Integer> mRatings,
                                      ArrayList<String> mRatingDates, ArrayList<String> mComments) {
        this.mUserNames = mUserNames;
        this.mUserImageUrls = mUserImageUrls;
        this.mRatings = mRatings;
        this.mRatingDates = mRatingDates;
        this.mComments = mComments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mUserImageUrls.get(position))
                .into(viewHolder.profile_image);
        viewHolder.username.setText(mUserNames.get(position));
        viewHolder.rating.setRating(mRatings.get(position));
        viewHolder.data_rating.setText(mRatingDates.get(position));
        viewHolder.comment.setText(mComments.get(position));

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
            this.username = itemView.findViewById(R.id.comment_book_username);
            this.profile_image = itemView.findViewById(R.id.comment_book_profile_image);
            this.rating = itemView.findViewById(R.id.comment_book_rating);
            this.data_rating = itemView.findViewById(R.id.comment_book_data_rating);
            this.comment = itemView.findViewById(R.id.comment_book_comment);
            this.parentLayout = itemView.findViewById(R.id.comment_book_parent_layout);
        }
    }
}
