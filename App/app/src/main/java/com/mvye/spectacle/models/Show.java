package com.mvye.spectacle.models;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseRelation;

@ParseClassName("Show")
public class Show extends ParseObject {
    public static final String KEY_SHOW_NAME = "showName";
    public static final String KEY_POSTER_IMAGE = "posterImage";
    public static final String KEY_CIRCLE_IMAGE = "circleImage";
    public static final String KEY_THREADS = "threads";
    public static final String KEY_SHOW_ID = "showId";

    public Show() {}

    public String getShowName() {
        return getString(KEY_SHOW_NAME);
    }

    public ParseFile getPosterImage() {
        return getParseFile(KEY_POSTER_IMAGE);
    }

    public ParseFile getCircleImage() {
        return getParseFile(KEY_CIRCLE_IMAGE);
    }

    public ParseRelation<Thread> getThreads() {
        return getRelation(KEY_THREADS);
    }

    public String getShowId() {
        return getString(KEY_SHOW_ID);
    }

    public void setShowName(String showName) {
        put(KEY_SHOW_NAME, showName);
    }

    public void setPosterImage(ParseFile posterImage) {
        put(KEY_POSTER_IMAGE, posterImage);
    }

    public void setCircleImage(ParseFile circleImage) {
        put(KEY_CIRCLE_IMAGE, circleImage);
    }

    public void addThread(Thread thread) {
        getThreads().add(thread);
        saveInBackground();
    }

    public void removeThread(Thread thread) {
        getThreads().remove(thread);
        saveInBackground();
    }

    public void setShowId(String showId) {
        put(KEY_SHOW_ID, showId);
    }
}
