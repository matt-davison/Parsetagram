package com.mdavison.parsetagram.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mdavison.parsetagram.Activities.PostDetailsActivity;
import com.mdavison.parsetagram.Adapters.PostsAdapter;
import com.mdavison.parsetagram.Models.Post;
import com.mdavison.parsetagram.R;
import com.mdavison.parsetagram.Support.EndlessRecyclerViewScrollListener;
import com.mdavison.parsetagram.Support.Extras;
import com.mdavison.parsetagram.Support.ItemClickSupport;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * This Fragment implements the home feed story
 */
public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    protected PostsAdapter postsAdapter;
    protected SwipeRefreshLayout swipeContainer;
    private RecyclerView rvPosts;
    private List<Post> allPosts;
    private EndlessRecyclerViewScrollListener scrollListener;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts);
        rvPosts.setAdapter(postsAdapter);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);
        queryPosts();

        swipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        postsAdapter.clear();
                        queryPosts();
                    }
                });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        ItemClickSupport.addTo(rvPosts).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView,
                                              int position, View v) {
                        Intent i = new Intent(getContext(),
                                PostDetailsActivity.class);
                        i.putExtra(Extras.EXTRA_POST,
                                Parcels.wrap(allPosts.get(position)));
                        startActivity(i);
                    }
                });
        scrollListener =
                new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount,
                                           RecyclerView view) {
                        queryMorePosts(allPosts.size());
                    }
                };
        rvPosts.addOnScrollListener(scrollListener);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED);
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

    private void queryMorePosts(int offset) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setSkip(offset);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED);
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