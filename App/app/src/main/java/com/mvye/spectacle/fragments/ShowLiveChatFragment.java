package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.ChatMessage;
import com.mvye.spectacle.models.ChatRoom;
import com.mvye.spectacle.models.Show;

import java.util.List;

public class ShowLiveChatFragment extends Fragment {

    public static final String TAG = "ShowLiveChatFragment";

    ImageView imageViewChatPoster;
    TextView textViewChatTitle;
    RecyclerView recyclerViewChat;
    EditText editTextMessage;
    ImageButton imageButtonSend;

    Show show;
    ChatRoom room;
    List<ChatMessage> messages;

    public ShowLiveChatFragment() { }

    public static ShowLiveChatFragment newInstance(Show show, ChatRoom room) {
        ShowLiveChatFragment showLiveChatFragment = new ShowLiveChatFragment();
        Bundle args = new Bundle();
        args.putParcelable("show", show);
        args.putParcelable("room", room);
        showLiveChatFragment.setArguments(args);
        return showLiveChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        show = getArguments().getParcelable("show");
        room = getArguments().getParcelable("room");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_live_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupVariables(view);
        setShowPosterAndTitle();
        Toast.makeText(getContext(), "This is a livechat for " + show.getShowName(), Toast.LENGTH_SHORT).show();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupVariables(View view) {
        imageViewChatPoster = view.findViewById(R.id.imageViewChatPoster);
        textViewChatTitle = view.findViewById(R.id.textViewChatTitle);
        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        imageButtonSend = view.findViewById(R.id.imageButtonSend);
    }

    private void setShowPosterAndTitle() {
        Glide.with(requireContext()).load(show.getPosterImage().getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewChatPoster);
        textViewChatTitle.setText(String.format("%s Chat", show.getShowName()));
    }
}