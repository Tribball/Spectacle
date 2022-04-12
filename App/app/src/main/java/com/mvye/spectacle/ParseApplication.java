package com.mvye.spectacle;

import android.app.Application;

import com.mvye.spectacle.models.Show;
import com.mvye.spectacle.models.Thread;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Show.class);
        ParseObject.registerSubclass(Thread.class);
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