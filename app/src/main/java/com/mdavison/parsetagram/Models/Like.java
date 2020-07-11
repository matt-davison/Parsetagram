package com.mdavison.parsetagram.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Represents a like
 */
@ParseClassName("Like")
public class Like extends ParseObject {

    public static final String KEY_USER = "owner";
    public static final String KEY_POST = "post";

    public void setOwner(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseUser getOwner() {
        return getParseUser(KEY_USER);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

    public Post getPost() {
        return (Post) getParseObject(KEY_POST);
    }
}
