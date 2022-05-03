package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.mvye.spectacle.adapters.ChatMessageAdapter;
import com.mvye.spectacle.models.ChatMessage;
import com.mvye.spectacle.models.ChatRoom;
import com.mvye.spectacle.models.Show;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowLiveChatFragment extends Fragment {

    public static final String TAG = "ShowLiveChatFragment";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    ImageView imageViewChatPoster;
    TextView textViewChatTitle;
    RecyclerView recyclerViewChat;
    EditText editTextMessage;
    ImageButton imageButtonSend;

    Show show;
    ChatRoom room;
    List<ChatMessage> messages;
    ChatMessageAdapter chatMessageAdapter;
    boolean isFirstLoad;
    private ParseLiveQueryClient parseLiveQueryClient;
    private SubscriptionHandling<ChatMessage> subscriptionHandling;

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
        setupRecyclerView(view);
        queryChatMessages();
        setupLiveQueries();
        setupOnClick(view);
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

    private void setupRecyclerView(View view) {
        messages = new ArrayList<>();
        isFirstLoad = true;
        chatMessageAdapter = new ChatMessageAdapter(getContext(), ParseUser.getCurrentUser(), messages);
        recyclerViewChat.setAdapter(chatMessageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewChat.setLayoutManager(layoutManager);
    }

    private void queryChatMessages() {
        ParseQuery<ChatMessage> query = room.getChatMessages().getQuery();
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ChatMessage>() {
            @Override
            public void done(List<ChatMessage> messageList, ParseException e) {
                if (e != null)
                    Log.e(TAG, "Error loading messages", e);
                messages.clear();
                messages.addAll(messageList);
                chatMessageAdapter.notifyDataSetChanged();
                if (isFirstLoad) {
                    recyclerViewChat.scrollToPosition(messageList.size()-1);
                    isFirstLoad = false;
                }
            }
        });
    }

    private void setupLiveQueries() {
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<ChatMessage> query = new ParseQuery<>(ChatMessage.class);
        query.whereEqualTo(ChatMessage.KEY_ROOM, room);
        subscriptionHandling = parseLiveQueryClient.subscribe(query);
        subscriptionHandling.handleSubscribe(subscribedQuery -> {
            liveQuery();
        });
    }

    // tried to be quirky with this function
    private void liveQuery() {
        if (subscriptionHandling != null) {
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, chatMessage) -> {
                requireActivity().runOnUiThread(() -> {
                    chatMessageAdapter.addItem(chatMessage);
                    chatMessageAdapter.addItem(chatMessage);
                    recyclerViewChat.scrollToPosition(messages.size()-1);
                });
            });
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.DELETE, (query, chatMessage) -> {
                requireActivity().runOnUiThread(() -> {
                    chatMessageAdapter.removeItem(chatMessage);
                });
            });
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.DELETE, (query, chatMessage) -> {
                requireActivity().runOnUiThread(() -> {
                    chatMessageAdapter.updateItem(chatMessage);
                });
            });
        }
    }

    private void setupOnClick(View view) {
        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMessage.getText().toString();
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setRoom(room);
                chatMessage.setSender(ParseUser.getCurrentUser());
                chatMessage.setMessage(message);
                chatMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        room.addMessage(chatMessage);
                        room.saveInBackground();
                    }
                });
                editTextMessage.setText(null);
            }
        });
    }

    private void setShowPosterAndTitle() {
        Glide.with(requireContext()).load(show.getPosterImage().getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewChatPoster);
        textViewChatTitle.setText(String.format("%s Chat", show.getShowName()));
    }
}