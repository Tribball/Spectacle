package com.mvye.spectacle.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

@ParseClassName("ChatRoom")
public class ChatRoom extends ParseObject {
    public static final String KEY_SHOW = "show";
    public static final String KEY_MESSAGES = "messages";

    public ChatRoom() {}

    public Show getShow() {
        return (Show) getParseObject(KEY_SHOW);
    }

    public ParseRelation<ChatMessage> getChatMessages() {
        return getRelation(KEY_MESSAGES);
    }

    public void setShow(Show show) {
        put(KEY_SHOW, show);
    }

    public void addMessage(ChatMessage message) {
        getChatMessages().add(message);
        saveInBackground();
    }

    public void removeMessage(ChatMessage message) {
        getChatMessages().remove(message);
        saveInBackground();
    }
}
