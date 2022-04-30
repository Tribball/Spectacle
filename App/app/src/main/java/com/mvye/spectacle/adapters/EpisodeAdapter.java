package com.mvye.spectacle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    public interface OnButtonClickListener {
        void OnButtonClickListener(int position);
    }

    Context context;
    List<Episode> episodes;
    OnButtonClickListener buttonClickListener;

    public EpisodeAdapter(Context context, List<Episode> episodes, OnButtonClickListener buttonClickListener) {
        this.context = context;
        this.episodes = episodes;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View episodeView = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new ViewHolder(episodeView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        holder.bind(episode);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setupVariables(itemView);
        }

        private void setupVariables(View itemView) {
            button = itemView.findViewById(R.id.button);
        }

        public void bind(Episode episode) {
            String episodeNumber = String.format("%s", episode.getEpisodeNumber());
            button.setText(episodeNumber);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClickListener.OnButtonClickListener(getAdapterPosition());
                }
            });
        }
    }
}
