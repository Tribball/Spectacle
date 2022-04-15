package com.mvye.spectacle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.Show;

import java.util.List;
import java.util.Objects;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    public interface OnPosterImageClickListener {
        void OnPosterImageClickListener(int postion);
    }

    Context context;
    List<Show> shows;
    OnPosterImageClickListener posterImageClickListener;

    public ShowAdapter(Context context, List<Show> shows, OnPosterImageClickListener posterImageClickListener) {
        this.context = context;
        this.shows = shows;
        this.posterImageClickListener = posterImageClickListener;
    }

    @NonNull
    @Override
    public ShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View showView = LayoutInflater.from(context).inflate(R.layout.item_show, parent, false);
        return new ViewHolder(showView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAdapter.ViewHolder holder, int position) {
        Show show = shows.get(position);
        holder.bind(show);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public void clear() {
        shows.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Show> showList) {
        shows.addAll(showList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPosterImage;

        public ViewHolder(View itemView) {
            super(itemView);
            setUpVariables(itemView);
        }

        private void setUpVariables(View itemView) {
            imageViewPosterImage = itemView.findViewById(R.id.imageViewPosterImage);
        }

        public void bind(Show show) {
            Glide.with(context).load(show.getPosterImage().getUrl())
                               .override(Target.SIZE_ORIGINAL)
                               .into(imageViewPosterImage);
            imageViewPosterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    posterImageClickListener.OnPosterImageClickListener(getAdapterPosition());
                }
            });
        }

    }
}
