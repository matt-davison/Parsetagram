package com.mdavison.parsetagram.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdavison.parsetagram.Models.Post;
import com.mdavison.parsetagram.R;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class PostsAdapter
        extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    protected final Context context;
    protected final List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvDate;
        private ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl())
                        .into(ivImage);
            }
            ParseFile profileImage = (ParseFile) post.getUser().get("picture");
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl())
                        .into(ivProfile);
            }
            long now = new Date().getTime();
            String relativeDate = DateUtils
                    .getRelativeTimeSpanString(post.getCreatedAt().getTime(),
                            now, DateUtils.SECOND_IN_MILLIS).toString();
            tvDate.setText(relativeDate);
        }
    }
}
