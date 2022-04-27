package com.mvye.spectacle;

import android.app.Application;
import android.util.Log;

import com.mvye.spectacle.models.ChatMessage;
import com.mvye.spectacle.models.ChatRoom;
import com.mvye.spectacle.models.Comment;
import com.mvye.spectacle.models.Show;
import com.mvye.spectacle.models.Thread;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ParseApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Show.class);
        ParseObject.registerSubclass(Thread.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(ChatRoom.class);
        ParseObject.registerSubclass(ChatMessage.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fGx7bgUDXlRKBjh2x62hiRUTXrufAYtApzXSb9uY")
                .clientKey("FV4MT0IymZ8cUqIazwNR17G7ONd9QU8qQjitn9XR")
                .server("https://parseapi.back4app.com")
                .build()
        );

        // New test creation of object below
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}