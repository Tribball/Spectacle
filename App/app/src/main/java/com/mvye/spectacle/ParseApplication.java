package com.mvye.spectacle;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register your parse models
//        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fGx7bgUDXlRKBjh2x62hiRUTXrufAYtApzXSb9uY")
                .clientKey("FV4MT0IymZ8cUqIazwNR17G7ONd9QU8qQjitn9XR")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}