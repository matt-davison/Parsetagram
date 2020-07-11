package com.mdavison.parsetagram.Applications;

import android.app.Application;

import com.mdavison.parsetagram.BuildConfig;
import com.mdavison.parsetagram.Models.Comment;
import com.mdavison.parsetagram.Models.Like;
import com.mdavison.parsetagram.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Registers custom ParseObjects
 */
public class ParseApplication extends Application {

    public static final String CLIENT_KEY = BuildConfig.CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Like.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(new Parse.Configuration.Builder(this).applicationId(
                "mdavison-parsetagram") // should correspond to APP_ID env
                // variable
                .clientKey(null)  // set explicitly unless clientKey is
                // explicitly configured on Parse server
                .server("https://mdavison-parsetagram.herokuapp.com/parse/")
                .build());
    }
}
