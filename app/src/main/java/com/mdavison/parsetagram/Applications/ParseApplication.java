package com.mdavison.parsetagram.Applications;

import android.app.Application;

import com.mdavison.parsetagram.BuildConfig;
import com.mdavison.parsetagram.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    public static final String CLIENT_KEY = BuildConfig.CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("mdavison-parsetagram") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://mdavison-parsetagram.herokuapp.com/parse/").build());
    }
}
