package com.mani.movies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.datastruct.ReviewDetails;

import java.util.List;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    private List<ReviewDetails> reviewData;
    private LayoutInflater layoutInflater;

    public ReviewRecyclerAdapter(Context context, List<ReviewDetails> data) {
        this.layoutInflater = LayoutInflater.from(context);
        reviewData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.review_info_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.reviewAuthor.setText(reviewData.get(i).getReviewAuthor());
        viewHolder.reviewContent.setText(reviewData.get(i).getReviewContent());
    }

    @Override
    public int getItemCount() {
        return reviewData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;

        ViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author_tv);
            reviewContent = itemView.findViewById(R.id.review_content_tv);

        }
    }
}
