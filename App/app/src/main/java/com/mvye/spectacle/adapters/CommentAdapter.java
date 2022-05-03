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
import com.mvye.spectacle.models.ChatMessage;
import com.mvye.spectacle.models.Comment;
import com.mvye.spectacle.models.Episode;
import com.mvye.spectacle.models.Show;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ParseUser user;
    List<Comment> comments;

    public CommentAdapter(Context context, ParseUser user, List<Comment> comments) {
        this.context = context;
        this.user = user;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View commentView = LayoutInflater.from(context).inflate(R.layout.thread_comment, parent, false);
        return new ViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bindComment(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThreadProfile;
        TextView tvThreadUsername;
        TextView tvThreadTimestamp;
        TextView tvThreadMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setupVariables(itemView);
        }

        private void setupVariables(View itemView) {
            ivThreadProfile   = itemView.findViewById(R.id.ivThreadProfile);
            tvThreadUsername  = itemView.findViewById(R.id.tvThreadUsername);
            tvThreadTimestamp = itemView.findViewById(R.id.tvThreadTimestamp);
            tvThreadMessage   = itemView.findViewById(R.id.tvThreadComment);
        }

        void bindComment(Comment comment) {
            ParseUser commentAuthor = comment.getAuthor();
            try {
                commentAuthor.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(context)
                    .load(getProfileUrl(comment.getAuthor()))
                    .circleCrop()
                    .into(ivThreadProfile);
            tvThreadUsername.setText(comment.getAuthor().getUsername());
            tvThreadTimestamp.setText(comment.getCreatedAt().toString());
            tvThreadMessage.setText(comment.getMessage());
        }
    }

    private String getProfileUrl(ParseUser sender) {
        ParseFile file = (ParseFile) sender.get("profilePicture");
        assert file != null;
        return file.getUrl();
    }
}
