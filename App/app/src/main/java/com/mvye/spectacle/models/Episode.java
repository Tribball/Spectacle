package com.mvye.spectacle.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Episode {
    int seasonNumber;
    int episodeNumber;
    String episodeName;
    String episodeOverview;

    public Episode() {}

    public Episode(JSONObject episode) throws JSONException {
        seasonNumber = episode.getInt("season_number");
        episodeNumber = episode.getInt("episode_number");
        episodeName = episode.getString("name");
        episodeOverview = episode.getString("overview");
    }

    public static List<Episode> fromJsonArray(JSONArray episodeList) throws JSONException{
        List<Episode> episodes = new ArrayList<>();
        for (int i = 0; i < episodeList.length(); i++) {
            episodes.add(new Episode(episodeList.getJSONObject(i)));
        }
        return episodes;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getEpisodeOverview() {
        return episodeOverview;
    }
}
