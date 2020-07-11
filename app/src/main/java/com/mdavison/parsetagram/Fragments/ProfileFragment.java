package com.mdavison.parsetagram.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.mdavison.parsetagram.Activities.PostDetailsActivity;
import com.mdavison.parsetagram.Adapters.PostsAdapter;
import com.mdavison.parsetagram.Adapters.ProfilePostsAdapter;
import com.mdavison.parsetagram.Models.Post;
import com.mdavison.parsetagram.R;
import com.mdavison.parsetagram.Support.ItemClickSupport;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    //TODO: Remove and use compose's
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 7;
    public static final String photoFileName = "photo.jpg";

    private ProfilePostsAdapter postsAdapter;
    private RecyclerView rvPosts;
    private List<Post> allPosts;
    private File photoFile;

    private TextView tvUsername;
    private ImageView ivProfile;
    private ParseUser currentUser;
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
        currentUser = ParseUser.getCurrentUser();
        rvPosts = view.findViewById(R.id.rvPosts);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        ParseFile profileImage = (ParseFile) currentUser.get("picture");
        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl())
                    .into(ivProfile);
        }
        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
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
        query.whereEqualTo(Post.KEY_USER, currentUser);
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
                ivProfile.setImageBitmap(takenImage);
                currentUser.put("picture", new ParseFile(photoFile));
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                            Toast.makeText(getContext(), "Error while saving!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Profile Picture Updated!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider
                .getUriForFile(getContext(), "com" + ".codepath.fileprovider",
                        photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
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
