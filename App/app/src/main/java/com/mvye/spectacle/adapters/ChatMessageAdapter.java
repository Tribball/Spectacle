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
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.ChatMessage;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private static final int MESSAGE_OUTGOING = 10101;
    private static final int MESSAGE_INCOMING = 11111;

    Context context;
    ParseUser user;
    List<ChatMessage> messages;

    public ChatMessageAdapter(Context context, ParseUser user, List<ChatMessage> messages) {
        this.context = context;
        this.user = user;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_OUTGOING) {
            View messageView = LayoutInflater.from(context).inflate(R.layout.livechat_outgoing, parent, false);
            return new OutgoingChatMessageViewHolder(messageView);
        }
        else if (viewType == MESSAGE_INCOMING) {
            View messageView = LayoutInflater.from(context).inflate(R.layout.livechat_incoming, parent, false);
            return new IncomingChatMessageViewHolder(messageView);
        }
        else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bindMessage(message);
    }

    public void addItem(ChatMessage message) {
        this.messages.add(message);
        notifyItemInserted(getItemCount()-1);
    }

    public void removeItem(ChatMessage message) {
        for (int i = 0; i < getItemCount(); i++) {
            if (messages.get(i).getObjectId().equals(message.getObjectId())) {
                messages.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, getItemCount());
                return;
            }
        }
    }

    public void updateItem(ChatMessage message) {
        for (int i = 0; i < getItemCount(); i++) {
            if (messages.get(i).getObjectId().equals(message.getObjectId())) {
                messages.set(i, message);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isCurrentUser(position)) {
            return MESSAGE_OUTGOING;
        }
        else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isCurrentUser(int position) {
        ChatMessage message = messages.get(position);
        return message.getSender().getObjectId().equals(user.getObjectId());
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(ChatMessage message);
    }

    public class OutgoingChatMessageViewHolder extends ViewHolder {
        ImageView imageViewProfileMe;
        TextView textViewTimestamp;
        TextView textViewMessageMe;

        public OutgoingChatMessageViewHolder(View itemView) {
            super(itemView);
            setupVariables(itemView);
        }

        private void setupVariables(View itemView) {
            imageViewProfileMe = itemView.findViewById(R.id.imageViewProfileMe);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewMessageMe = itemView.findViewById(R.id.textViewSelfLiveChatComment);
        }

        @Override
        void bindMessage(ChatMessage message) {
            ParseUser otherUser = message.getSender();
            try {
                otherUser.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(context)
                    .load(getProfileUrl(message.getSender()))
                    .circleCrop()
                    .into(imageViewProfileMe);
            textViewTimestamp.setText(message.getCreatedAt().toString());
            textViewMessageMe.setText(message.getMessage());
        }
    }

    public class IncomingChatMessageViewHolder extends ViewHolder {
        ImageView imageViewProfileOther;
        TextView textViewUsernameOther;
        TextView textViewTimeStampOther;
        TextView textViewMessageOther;

        public IncomingChatMessageViewHolder(View itemView) {
            super(itemView);
            setupVariables(itemView);
        }

        private void setupVariables(View itemView) {
            imageViewProfileOther = itemView.findViewById(R.id.imageViewProfileOther);
            textViewUsernameOther = itemView.findViewById(R.id.textViewUsername);
            textViewTimeStampOther = itemView.findViewById(R.id.textViewTimestamp);
            textViewMessageOther = itemView.findViewById(R.id.textViewChatBody);
        }

        @Override
        void bindMessage(ChatMessage message) {
            ParseUser otherUser = message.getSender();
            try {
                otherUser.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(context)
                    .load(getProfileUrl(message.getSender()))
                    .circleCrop()
                    .into(imageViewProfileOther);
            textViewUsernameOther.setText(message.getSender().getUsername());
            textViewTimeStampOther.setText(message.getCreatedAt().toString());
            textViewMessageOther.setText(message.getMessage());
        }
    }

    private String getProfileUrl(ParseUser sender) {
        ParseFile file = (ParseFile) sender.get("profilePicture");
        assert file != null;
        return file.getUrl();
    }
}
