package com.mdavison.parsetagram.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdavison.parsetagram.Models.Like;
import com.mdavison.parsetagram.Models.Post;
import com.mdavison.parsetagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    public static final String TAG = "PostDetailsActivity";
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvDate;
    private ImageView ivProfile;
    private TextView tvLikes;
    private ImageView ivLike;
    private RecyclerView tvComments;
    private EditText etComment;
    private ImageView ivComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        final Post post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        final ParseUser user = post.getUser();
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        ivProfile = findViewById(R.id.ivProfile);
        tvLikes = findViewById(R.id.tvLikes);
        ivLike = findViewById(R.id.ivLike);
        tvComments = findViewById(R.id.rvComments);
        etComment = findViewById(R.id.etComment);
        ivComment = findViewById(R.id.ivComment);

        LinearLayout llProfile = findViewById(R.id.llProfile);
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostDetailsActivity.this, ProfileDetailsActivity.class);
                i.putExtra("user",
                        Parcels.wrap(user));
                startActivity(i);
            }
        });

        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        ParseFile profileImage = (ParseFile) user.get("picture");
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl())
                    .into(ivProfile);
        }
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        }

        long now = new Date().getTime();
        String relativeDate = DateUtils
                .getRelativeTimeSpanString(post.getCreatedAt().getTime(), now,
                        DateUtils.SECOND_IN_MILLIS).toString();
        tvDate.setText(relativeDate);

        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Like.KEY_POST, post);
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues with getting posts", e);
                    return;
                }
                if (likes.size() == 0) {
                    ivLike.setImageResource(R.drawable.ufi_heart_active);
                } else {
                    ivLike.setImageResource(R.drawable.ufi_heart);
                    likes.get(0).deleteInBackground();
                }
                tvLikes.setText(Integer.toString(likes.size()));
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
                query.include(Like.KEY_USER);
                query.whereEqualTo(Like.KEY_POST, post);
                query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<Like>() {
                    @Override
                    public void done(List<Like> likes, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues with getting posts", e);
                            return;
                        }
                        if (likes.size() == 0) {
                            ivLike.setImageResource(R.drawable.ufi_heart_active);
                        } else {
                            ivLike.setImageResource(R.drawable.ufi_heart);
                            likes.get(0).deleteInBackground();
                        }
                        tvLikes.setText(Integer.toString(likes.size()));
                    }
                });

                Like like = new Like();
                like.setPost(post);
                like.setOwner(ParseUser.getCurrentUser());
                like.saveInBackground();

            }
        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED);
        query.whereEqualTo(Post.KEY_USER, currentUser);
         */
    }
}