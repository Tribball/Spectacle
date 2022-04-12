package com.mvye.spectacle.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_THREAD = "thread";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_MESSAGE = "message";

    public Comment() {}

    public Thread getThread() {
        return (Thread) getParseObject(KEY_THREAD);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setThread(Thread thread) {
        put(KEY_THREAD, thread);
    }

    public void setKeyAuthor(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public void setKeyMessage(String message) {
        put(KEY_MESSAGE, message);
    }
}
