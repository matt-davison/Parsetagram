package com.mdavison.parsetagram.Fragments;

import android.util.Log;

import com.mdavison.parsetagram.Models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {
    @Override
    protected void queryPosts() {
        super.queryPosts();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                swipeContainer.setRefreshing(false);
                if (e != null) {
                    Log.e(TAG, "Issues with getting posts", e);
                    return;
                }
                postsAdapter.addAll(newPosts);
            }
        });
    }
}
