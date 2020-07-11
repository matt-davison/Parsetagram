package com.mdavison.parsetagram.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mdavison.parsetagram.Activities.PostDetailsActivity;
import com.mdavison.parsetagram.Adapters.PostsAdapter;
import com.mdavison.parsetagram.Adapters.ProfilePostsAdapter;
import com.mdavison.parsetagram.Models.Post;
import com.mdavison.parsetagram.R;
import com.mdavison.parsetagram.Support.ItemClickSupport;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    //TODO: Remove and use compose's
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 7;
    public static final String photoFileName = "photo.jpg";

    private ProfilePostsAdapter postsAdapter;
    private RecyclerView rvPosts;
    private List<Post> allPosts;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        postsAdapter = new ProfilePostsAdapter(getContext(), allPosts);
        rvPosts.setAdapter(postsAdapter);
        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), 3);
        rvPosts.setLayoutManager(layoutManager);
        queryPosts();
        ItemClickSupport.addTo(rvPosts).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView,
                                              int position, View v) {
                        Intent i = new Intent(getContext(),
                                PostDetailsActivity.class);
                        i.putExtra("post",
                                Parcels.wrap(allPosts.get(position)));
                        startActivity(i);
                    }
                });
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage =
                        BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues with getting posts", e);
                    return;
                }
                postsAdapter.addAll(newPosts);
            }
        });
    }

    //TODO: Move to a static helper class
    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific
        // directories.
        // This way, we don't need to request external read/write runtime
        // permissions.
        File mediaStorageDir = new File(getContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}
