package com.mvye.spectacle.models;

import com.mvye.spectacle.models.Comment;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_FOLLOWING = "following";

    public User() {}

    public ParseRelation<ParseObject> getFollowing(ParseUser currentUser) {
        return getRelation(KEY_FOLLOWING);
    }
}
