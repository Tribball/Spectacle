package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvye.spectacle.R;
<<<<<<< Updated upstream
=======
import com.mvye.spectacle.adapters.ThreadAdapter;
import com.mvye.spectacle.models.Episode;
import com.mvye.spectacle.models.Show;
import com.mvye.spectacle.models.Thread;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.w3c.dom.Comment;

import java.util.List;
>>>>>>> Stashed changes

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpisodeThreadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpisodeThreadFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

<<<<<<< Updated upstream
    public EpisodeThreadFragment() {
        // Required empty public constructor
    }
=======
    Show show;
    Thread thread;
    Comment comment;
    String episodeName;
    String episodeOverview;
    RecyclerView rvEpisodeThread;
    ThreadAdapter threadAdapter;
    List<String> comments;

    public EpisodeThreadFragment() { }
>>>>>>> Stashed changes

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EpisodeThreadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EpisodeThreadFragment newInstance(String param1, String param2) {
        EpisodeThreadFragment fragment = new EpisodeThreadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode_thread, container, false);
    }
<<<<<<< Updated upstream
=======

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupVariables(view);
        Log.i(TAG, "This is show: " + show.getShowName() + " season " + thread.getSeasonNumber() + " episode " + thread.getEpisodeNumber() + ": " + episodeName);
        Log.i(TAG, "Overview: " + episodeOverview);
        Toast.makeText(getContext(), "This is show: " + show.getShowName() + " season " + thread.getSeasonNumber() + " episode " + thread.getEpisodeNumber() + ": " + episodeName, Toast.LENGTH_LONG).show();
        // setupVariables
        // do other stuff
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupVariables(View view) {
        rvEpisodeThread = view.findViewById(R.id.rvEpisodeThread);
    }

    private void populateThreadRecyclerView() {
        ParseQuery<Comment> query = thread.getComments().getQuery();
        query.include("Thread");
        int seasonNumber = episodes.get(position).getSeasonNumber();
        int episodeNumber = episodes.get(position).getEpisodeNumber();
        String
        Log.i(TAG, "OnButtonClickListener: Clicked season " + seasonNumber + " episode " + episodeNumber);
        query.whereEqualTo(Thread.KEY_SEASON_NUMBER, seasonNumber);
        query.whereEqualTo(Thread.KEY_EPISODE_NUMBER, episodeNumber);
        query.findInBackground(new FindCallback<Thread>() {
            @Override
            public void done(List<Thread> threads, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting threads", e);
                    return;
                }
                if (threads.isEmpty()) {
                    // create new thread
                    Thread thread = new Thread();
                    thread.setShow(show);
                    thread.setSeasonNumber(seasonNumber);
                    thread.setEpisodeNumber(episodeNumber);
                    thread.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // add to show
                            show.addThread(thread);
                            // send to fragment
                            openEpisodeThreadFragment(thread, episodes.get(position));
                        }
                    });
                }
                else {
                    // send to fragment
                    openEpisodeThreadFragment(threads.get(0), episodes.get(position));
                }
            }
        });
        threadAdapter = new ThreadAdapter(getContext(), comments);
        rvEpisodeThread.setAdapter(threadAdapter);
        rvEpisodeThread.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }
>>>>>>> Stashed changes
}