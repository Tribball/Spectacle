package com.mvye.spectacle.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ChatMessage")
public class ChatMessage extends ParseObject {
    public static final String KEY_SENDER = "sender";
    public static final String KEY_ROOM = "room";
    public static final String KEY_MESSAGE = "message";

    public ChatMessage() {}

    public ParseUser getSender() {
        return getParseUser(KEY_SENDER);
    }

    public ChatRoom getRoom() {
        return (ChatRoom) getParseObject(KEY_ROOM);
    }

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setSender(ParseUser user) {
        put(KEY_SENDER, user);
    }

    public void setRoom(ChatRoom room) {
        put(KEY_ROOM, room);
    }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }
}
