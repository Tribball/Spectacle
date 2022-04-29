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
import com.mvye.spectacle.models.Thread;

import org.w3c.dom.Comment;

import java.util.List;
import java.util.Objects;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    List<String> threadComments;
    Context context;

    public ThreadAdapter(Context context, List<String> threadComments) {
        this.threadComments = threadComments;
    }

    @NonNull
    @Override
    public ThreadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View threadView =  LayoutInflater.from(context).inflate(R.layout.item_threadcomment, parent, false);
        return new ViewHolder(threadView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadAdapter.ViewHolder holder, int position) {
        String threadComment = threadComments.get(position);
        holder.bind(threadComment);
    }

    @Override
    public int getItemCount() {
        return threadComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvComments;

        public ViewHolder(View itemView) {
            super(itemView);
            setUpVariables(itemView);
        }

        private void setUpVariables(View itemView) {
            tvComments = itemView.findViewById(R.id.tvComments);
        }

        public void bind(String threadComment) {
            tvComments.setText(threadComment);
        }
    }
}
