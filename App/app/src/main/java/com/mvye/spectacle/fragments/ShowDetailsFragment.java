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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.button.MaterialButton;
import com.mvye.spectacle.R;
import com.mvye.spectacle.adapters.EpisodeAdapter;
import com.mvye.spectacle.models.ChatRoom;
import com.mvye.spectacle.models.Comment;
import com.mvye.spectacle.models.Episode;
import com.mvye.spectacle.models.Show;
import com.mvye.spectacle.models.Thread;
import com.mvye.spectacle.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class ShowDetailsFragment extends Fragment {

    public static final String TV_SHOW_URL = "https://api.themoviedb.org/3/tv/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    public static final String TV_SHOW_SEASONS = "https://api.themoviedb.org/3/tv/%s/season/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    public static final String TAG = "ShowDetailsFragment";

    ImageView imageViewPosterImage;
    TextView textViewShowName;
    TextView textViewShowDescription;
    TextView textViewScore;
    Button buttonFollow;
    Spinner spinnerSeasons;
    RecyclerView recyclerViewEpisodes;
    MaterialButton buttonLiveChat;
    User user;

    Show show;
    List<Episode> episodes;
    EpisodeAdapter episodeAdapter;

    public ShowDetailsFragment() {}

    public static ShowDetailsFragment newInstance(Show show) {
        ShowDetailsFragment showDetailsFragment = new ShowDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("show", show);
        showDetailsFragment.setArguments(args);
        return showDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        show = getArguments().getParcelable("show");
        user = getArguments().getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupVariables(view);
        setupButtons();
        String showId = show.getShowId();
        if (showId != null) {
            fetchShowData(showId);
        }
        else {
            setPosterImageAndName();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupVariables(View view) {
        imageViewPosterImage = view.findViewById(R.id.imageViewPosterImage);
        textViewShowName = view.findViewById(R.id.textViewShowName);
        textViewShowDescription = view.findViewById(R.id.textViewShowDescription);
        textViewScore = view.findViewById(R.id.textViewScore);
        buttonFollow = view.findViewById(R.id.buttonFollow);
        spinnerSeasons = view.findViewById(R.id.spinnerSeasons);
        recyclerViewEpisodes = view.findViewById(R.id.recyclerViewEpisodes);
        buttonLiveChat = view.findViewById(R.id.buttonLiveChat);
    }

    private void setupButtons() {
       ParseUser currentUser = ParseUser.getCurrentUser();
       ParseQuery<ParseObject> query = currentUser.getRelation("following").getQuery();
       query.include("Show");
       query.whereEqualTo("objectId", show.getObjectId());
       query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> shows, ParseException e) {
               if (shows.size() != 0) {
                   buttonFollow.setText("Unfollow");
               }
               else {
                   buttonFollow.setText("Follow");
               }
           }
       });
        buttonLiveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatRoom room = show.getRoom();
                if (room != null) {
                    // open existing room
                    openLiveChatFragment(room);
                }
                else {
                    // create new room
                    room = new ChatRoom();
                    room.setShow(show);
                    ChatRoom finalRoom = room;
                    room.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            show.setRoom(finalRoom);
                            show.saveInBackground();
                            openLiveChatFragment(finalRoom);
                        }
                    });
                }
            }
        });
    }

    private void openLiveChatFragment(ChatRoom room) {
        ShowLiveChatFragment liveChatFragment = ShowLiveChatFragment.newInstance(show, room);
        getParentFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, liveChatFragment).addToBackStack("").commit();
    }

    private void fetchShowData(String showId) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = String.format(TV_SHOW_URL, showId);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject showResult  = json.jsonObject;
                try {
                    getDetails(showResult);
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: jsonarray error");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + statusCode + " " + headers + " " + response);
                setPosterImageAndName();
            }
        });
    }

    private void getDetails(JSONObject show) throws JSONException {
        String showName = show.getString("name");
        String posterPath = String.format("https://image.tmdb.org/t/p/w342/%s", show.getString("poster_path"));
        Double voteAverage = show.getDouble("vote_average");
        String description = show.getString("overview");
        int numberOfSeasons = show.getInt("number_of_seasons");
        int numberOfEpisodes = show.getInt("number_of_episodes");
        setShowDetails(showName, posterPath, voteAverage, description, numberOfSeasons, numberOfEpisodes);
        JSONArray seasonsArray = show.getJSONArray("seasons");
        populateSeasons(seasonsArray);
    }

    private void populateSeasons(JSONArray seasonsArray) throws JSONException {
        int amountOfSeasons = seasonsArray.length();
        String[] seasons = new String[amountOfSeasons];
        for (int i = 0; i < amountOfSeasons; i++) {
            seasons[i] = seasonsArray.getJSONObject(i).getString("name");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, seasons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeasons.setAdapter(adapter);
        spinnerSeasons.setSelection(1);
        spinnerSeasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i(TAG, "onItemSelected: position " + position + " " + adapterView.getItemAtPosition(position).toString());
                fetchShowSeasonEpisodes(show.getShowId(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchShowSeasonEpisodes(String showId, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = String.format(TV_SHOW_SEASONS, showId, position);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject episodeResults  = json.jsonObject;
                try {
                    getEpisodeDetails(episodeResults);
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: jsonarray error");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + statusCode + " " + headers + " " + response);
                setPosterImageAndName();
            }
        });
    }

    private void getEpisodeDetails(JSONObject results) throws JSONException {
        JSONArray episodesList = results.getJSONArray("episodes");
        episodes = new ArrayList<>();
        episodes.addAll(Episode.fromJsonArray(episodesList));
        populateEpisodeRecyclerView();
    }

    private void populateEpisodeRecyclerView() {
        EpisodeAdapter.OnButtonClickListener onButtonClickListener = new EpisodeAdapter.OnButtonClickListener() {
            @Override
            public void OnButtonClickListener(int position) {
                ParseQuery<Thread> query = show.getThreads().getQuery();
                query.include("Thread");
                int seasonNumber = episodes.get(position).getSeasonNumber();
                int episodeNumber = episodes.get(position).getEpisodeNumber();
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
            }
        };
        episodeAdapter = new EpisodeAdapter(getContext(), episodes, onButtonClickListener);
        recyclerViewEpisodes.setAdapter(episodeAdapter);
        recyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void openEpisodeThreadFragment(Thread thread, Episode episode) {
        EpisodeThreadFragment episodeThreadFragment = EpisodeThreadFragment.newInstance(show, thread, episode.getEpisodeName(), episode.getEpisodeOverview());
        getParentFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, episodeThreadFragment).addToBackStack("").commit();
    }

    private void setShowDetails(String showName, String posterPath, Double voteAverage, String description, int numberOfSeasons, int numberOfEpisodes) {
        textViewShowName.setText(showName);
        Glide.with(requireContext()).load(posterPath)
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewPosterImage);
        textViewShowDescription.setText(description);
        textViewScore.setText(String.format(Locale.US, "%.1f/10", voteAverage));
    }


    private void setPosterImageAndName() {
        Glide.with(requireContext()).load(show.getPosterImage().getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewPosterImage);
        textViewShowName.setText(show.getShowName());
    }
}