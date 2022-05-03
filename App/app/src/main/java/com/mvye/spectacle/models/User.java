package com.mvye.spectacle.models;

import com.mvye.spectacle.models.Comment;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String KEY_FOLLOWING = "following";

    public User() {}

    public ParseRelation<Show> getFollowing(ParseUser currentUser) {
        return getRelation(KEY_FOLLOWING);
    }
}
