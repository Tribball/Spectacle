package com.mvye.spectacle.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("Thread")
public class Thread extends ParseObject {
    public static final String KEY_SHOW = "show";
    public static final String KEY_SEASON_NUMBER = "seasonNumber";
    public static final String KEY_EPISODE_NUMBER = "episodeNumber";
    public static final String KEY_COMMENTS = "comments";

    public Thread() {}

    public Show getShow() {
        return (Show) getParseObject(KEY_SHOW);
    }

    public int getSeasonNumber() {
        return (int) getNumber(KEY_SEASON_NUMBER);
    }

    public int getEpisodeNumber() {
        return (int) getNumber(KEY_EPISODE_NUMBER);
    }

    public ParseRelation<Comment> getComments() {
        return getRelation(KEY_COMMENTS);
    }

    public void setShow(Show show) {
        put(KEY_SHOW, show);
    }

    public void setSeasonNumber(int seasonNumber) {
        put(KEY_SEASON_NUMBER, seasonNumber);
    }

    public void setEpisodeNumber(int episodeNumber) {
        put(KEY_EPISODE_NUMBER, episodeNumber);
    }

    public void addComment(Comment comment) {
        getComments().add(comment);
        saveInBackground();
    }

    public void removeComment(Comment comment) {
        getComments().remove(comment);
        saveInBackground();
    }
}
