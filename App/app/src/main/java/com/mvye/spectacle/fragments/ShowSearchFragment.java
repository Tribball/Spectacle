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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.ChatMessage;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class ShowSearchFragment extends Fragment {

    public static final String TV_SEARCH_URL = "https://api.themoviedb.org/3/search/tv?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&page=1&query=%s";
    public static final String TAG = "ShowLiveChatFragment";

    EditText editTextShowSearch;
    ImageButton imageButtonSend;
    ImageView imageViewShowPoster;
    TextView textViewChatTitle;
    TextView textViewShowDescription;
    Button buttonAddShow;

    AsyncHttpClient client;

    public ShowSearchFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupVariable(view);
        setVariabesInvisible();
        setupClient();
        setupButtons();
    }

    private void setupVariable(View view) {
        editTextShowSearch = view.findViewById(R.id.editTextShowSearch);
        imageButtonSend = view.findViewById(R.id.imageButtonSend);
        imageViewShowPoster = view.findViewById(R.id.imageViewShowPoster);
        textViewChatTitle = view.findViewById(R.id.textViewChatTitle);
        textViewShowDescription = view.findViewById(R.id.textViewShowDescription);
        buttonAddShow = view.findViewById(R.id.buttonAddShow);
    }

    private void setVariabesInvisible() {
        imageViewShowPoster.setVisibility(View.INVISIBLE);
        textViewChatTitle.setVisibility(View.INVISIBLE);
        textViewShowDescription.setVisibility(View.INVISIBLE);
        buttonAddShow.setVisibility(View.INVISIBLE);
    }

    private void setupClient() {
        client = new AsyncHttpClient();
    }

    private void setupButtons() {
        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editTextShowSearch.getText().toString();
                String url = String.format(TV_SEARCH_URL, query);
                client.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONObject showResult  = json.jsonObject;
                        Log.i(TAG, "onSuccess: " + showResult.toString());
                        try {
                            getDetails(showResult);
                        } catch (JSONException e) {
                            Log.e(TAG, "onSuccess: jsonarray error");
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure: " + statusCode + " " + headers + " " + response);
                    }
                });
            }
        });
    }

    private void getDetails(JSONObject showResult) throws JSONException {
        JSONArray results = showResult.getJSONArray("results");
        JSONObject show = results.getJSONObject(0);
        String showName = show.getString("name");
        String posterPath = String.format("https://image.tmdb.org/t/p/w342/%s", show.getString("poster_path"));
        String description = show.getString("overview");
        populateShowDetails(showName, posterPath, description);
    }

    private void populateShowDetails(String showName, String posterPath, String description) {
        textViewChatTitle.setText(showName);
        Glide.with(requireContext()).load(posterPath)
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewShowPoster);
        textViewShowDescription.setText(description);
        setVariablesVisible();
    }

    private void setVariablesVisible() {
        imageViewShowPoster.setVisibility(View.VISIBLE);
        textViewChatTitle.setVisibility(View.VISIBLE);
        textViewShowDescription.setVisibility(View.VISIBLE);
        buttonAddShow.setVisibility(View.VISIBLE);
    }
}