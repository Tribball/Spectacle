package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.Show;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

import okhttp3.Headers;

public class ShowDetailsFragment extends Fragment {

    public static final String TV_SHOW_URL = "https://api.themoviedb.org/3/tv/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    public static final String TAG = "ShowDetailsFragment";

    ImageView imageViewPosterImage;
    TextView textViewShowName;
    TextView textViewShowDescription;
    TextView textViewScore;
    Button buttonFollow;
    TextView textViewSeasonCount;
    TextView textViewEpisodeCount;
    RecyclerView recyclerViewEpisodes;
    Show show;

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
        textViewSeasonCount = view.findViewById(R.id.textViewSeasonCount);
        textViewEpisodeCount = view.findViewById(R.id.textViewEpisodeCount);
        recyclerViewEpisodes = view.findViewById(R.id.recyclerViewEpisodes);
    }

    private void fetchShowData(String showId) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = String.format(TV_SHOW_URL, showId);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject showResult  = json.jsonObject;
                try {
                    setDetails(showResult);
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

    private void setDetails(JSONObject show) throws JSONException {
        String showName = show.getString("name");
        String posterPath = String.format("https://image.tmdb.org/t/p/w342/%s", show.getString("poster_path"));
        Double voteAverage = show.getDouble("vote_average");
        String description = show.getString("overview");
        int numberOfSeasons = show.getInt("number_of_seasons");
        int numberOfEpisodes = show.getInt("number_of_episodes");
        Log.i(TAG, "setDetails: " + showName + " " + show.get("id"));
        textViewShowName.setText(showName);
        Glide.with(requireContext()).load(posterPath)
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewPosterImage);
        textViewShowDescription.setText(description);
        textViewScore.setText(String.format(Locale.US, "%.1f", voteAverage));
        textViewSeasonCount.setText(String.format(Locale.US, "%x seasons", numberOfSeasons));
        textViewEpisodeCount.setText(String.format(Locale.US, "%x episodes", numberOfEpisodes));
    }

    private void setPosterImageAndName() {
        Glide.with(requireContext()).load(show.getPosterImage().getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewPosterImage);
        textViewShowName.setText(show.getShowName());
    }
}