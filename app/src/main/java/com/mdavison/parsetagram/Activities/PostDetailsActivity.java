package com.mdavison.parsetagram.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mdavison.parsetagram.Models.Post;
import com.mdavison.parsetagram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Post post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        final ParseUser user = post.getUser();
        TextView tvUsername = findViewById(R.id.tvUsername);
        ImageView ivImage = findViewById(R.id.ivImage);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvDate = findViewById(R.id.tvDate);
        ImageView ivProfile = findViewById(R.id.ivProfile);

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
    }
}