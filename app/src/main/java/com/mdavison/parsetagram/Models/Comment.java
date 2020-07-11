package com.mdavison.parsetagram.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public ParseUser getOwner()  {
        return getParseUser("owner");
    }

    public void setPost(Post post) {
        put("post", post);
    }

    public Post getPost()  {
        return (Post) getParseObject("post");
    }
}
