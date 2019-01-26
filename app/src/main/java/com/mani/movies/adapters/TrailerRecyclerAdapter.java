package com.mani.movies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.datastruct.TrailerDetails;

import java.util.List;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.ViewHolder> {
    private List<TrailerDetails> trailerData;
    private LayoutInflater layoutInflater;
    private Context trailerContext;

    public TrailerRecyclerAdapter(Context context, List<TrailerDetails> data) {
        this.layoutInflater = LayoutInflater.from(context);
        trailerData = data;
        trailerContext = context;

    }

    private void playVideo(int position) {
        String videoId = trailerData.get(position).getKey();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        try {
            trailerContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            trailerContext.startActivity(webIntent);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.trailer_info_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.trailorTextView.setText(trailerData.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return trailerData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailorTextView;
        ImageView playButton;

        ViewHolder(View itemView) {
            super(itemView);
            trailorTextView = itemView.findViewById(R.id.trailer_describtion_tv);
            playButton = itemView.findViewById(R.id.trailer_play_iv);

            itemView.setOnClickListener(this);
            playButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (!trailorTextView.getText().toString().equalsIgnoreCase(
                    trailerContext.getString(R.string.trailer_unavailble))) {
                playVideo(getAdapterPosition());
            }
        }
    }
}
