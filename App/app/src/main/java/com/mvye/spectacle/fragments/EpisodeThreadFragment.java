package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mvye.spectacle.R;
import com.mvye.spectacle.models.Episode;
import com.mvye.spectacle.models.Show;
import com.mvye.spectacle.models.Thread;

public class EpisodeThreadFragment extends Fragment {

    public static final String TAG = "EpisodeThreadFragment";

    // TODO: Place the different image views, text views, etc

    Show show;
    Thread thread;
    String episodeName;
    String episodeOverview;

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
        Log.i(TAG, "This is show: " + show.getShowName() + " season " + thread.getSeasonNumber() + " episode " + thread.getEpisodeNumber() + ": " + episodeName);
        Log.i(TAG, "Overview: " + episodeOverview);
        Toast.makeText(getContext(), "This is show: " + show.getShowName() + " season " + thread.getSeasonNumber() + " episode " + thread.getEpisodeNumber() + ": " + episodeName, Toast.LENGTH_LONG).show();
        // setupVariables
        // do other stuff
        super.onViewCreated(view, savedInstanceState);
    }
}