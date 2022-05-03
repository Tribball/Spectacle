package com.mvye.spectacle.fragments;

import static com.mvye.spectacle.fragments.ShowLiveChatFragment.MAX_CHAT_MESSAGES_TO_SHOW;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mvye.spectacle.R;
import com.mvye.spectacle.adapters.CommentAdapter;
import com.mvye.spectacle.models.Comment;
import com.mvye.spectacle.models.Show;
import com.mvye.spectacle.models.Thread;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpisodeThreadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpisodeThreadFragment extends Fragment {

    public static final String TAG = "EpisodeThreadFragment";

    // TODO: Place the different image views, text views, etc

    RecyclerView threadRecyclerViewChat;
    EditText threadEditTextMessage;
    ImageButton threadImageButtonSend;
    TextView tvThreadTitle;
    TextView tvThreadSeason;
    TextView tvThreadEpisode;
    ImageView ivThreadPoster;

    Show show;
    Thread thread;
    String episodeName;
    String episodeOverview;
    List<Comment> comments;
    boolean isFirstLoad;
    CommentAdapter commentAdapter;

    public EpisodeThreadFragment() { }

    public static EpisodeThreadFragment newInstance(Show show, Thread thread, String episodeName, String episodeOverview) {
        EpisodeThreadFragment episodeThreadFragment = new EpisodeThreadFragment();
        Bundle args = new Bundle();
        args.putParcelable("show", show);
        args.putParcelable("thread", thread);
        args.putString("episodeName", episodeName);
        args.putString("episodeOverview", episodeOverview);
        episodeThreadFragment.setArguments(args);
        return episodeThreadFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        show = getArguments().getParcelable("show");
        thread = getArguments().getParcelable("thread");
        episodeName = getArguments().getString("episodeName");
        episodeOverview = getArguments().getString("episodeOverview");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode_thread, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupVariables(view);
        setupRecyclerView(view);
        queryComments();
        setupOnClick(view);
        setShowPosterAndTitle();
        Log.i(TAG, "This is show: " + show.getShowName() + " season " + thread.getSeasonNumber() + " episode " + thread.getEpisodeNumber() + ": " + episodeName);
        Log.i(TAG, "Overview: " + episodeOverview);
        //Log.i(TAG, "Comments: " + thread.getComments());
        //Toast.makeText(getContext(), "This is show: " + show.getShowName() + " season " + thread.getSeasonNumber() + " episode " + thread.getEpisodeNumber() + ": " + episodeName, Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), "Comments: " + thread.getTestComments(), Toast.LENGTH_LONG).show();
        // setupVariables
        // do other stuff
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupVariables(View view) {
        ivThreadPoster         = view.findViewById(R.id.ivThreadPoster);
        tvThreadTitle          = view.findViewById(R.id.tvThreadTitle);
        tvThreadSeason         = view.findViewById(R.id.tvThreadSeason);
        tvThreadEpisode        = view.findViewById(R.id.tvThreadEpisode);
        threadRecyclerViewChat = view.findViewById(R.id.threadRecyclerViewChat);
        threadEditTextMessage  = view.findViewById(R.id.threadEditTextMessage);
        threadImageButtonSend  = view.findViewById(R.id.threadImageButtonSend);
    }

    private void setupRecyclerView(View view) {
        comments = new ArrayList<>();
        isFirstLoad = true;
        commentAdapter = new CommentAdapter(getContext(), ParseUser.getCurrentUser(), comments);
        threadRecyclerViewChat.setAdapter(commentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        threadRecyclerViewChat.setLayoutManager(layoutManager);
    }

    private void queryComments() {
        ParseQuery<Comment> query = thread.getComments().getQuery();
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> commentList, ParseException e) {
                if (e != null)
                    Log.e(TAG, "Error loading comments", e);
                comments.clear();
                comments.addAll(commentList);
                commentAdapter.notifyDataSetChanged();
                if (isFirstLoad) {
                    threadRecyclerViewChat.scrollToPosition(commentList.size()-1);
                    isFirstLoad = false;
                }
            }
        });
    }

    private void setupOnClick(View view) {
        threadImageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = threadEditTextMessage.getText().toString();
                Comment comment = new Comment();
                comment.setThread(thread);
                comment.setAuthor(ParseUser.getCurrentUser());
                comment.setMessage(message);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        thread.addComment(comment);
                        thread.saveInBackground();
                    }
                });
                threadEditTextMessage.setText(null);
            }
        });
    }

    private void setShowPosterAndTitle() {
        Glide.with(requireContext()).load(show.getPosterImage().getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(ivThreadPoster);
        tvThreadTitle.setText(String.format("%s", show.getShowName()));
        tvThreadSeason.setText(String.format("Season %s", thread.getSeasonNumber()));
        tvThreadEpisode.setText(String.format("Episode %s", thread.getEpisodeNumber()));
    }
}