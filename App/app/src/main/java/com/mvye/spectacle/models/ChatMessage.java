package com.mvye.spectacle.models;

import com.parse.ParseObject;

public class ChatMessage extends ParseObject {
    public static final String KEY_SENDER = "sender";
    public static final String KEY_ROOM = "room";
    public static final String KEY_MESSAGE = "message";

    public ChatMessage() {}
}
