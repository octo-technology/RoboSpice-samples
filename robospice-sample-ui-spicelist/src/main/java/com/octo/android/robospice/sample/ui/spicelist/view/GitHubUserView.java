package com.octo.android.robospice.sample.ui.spicelist.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.sample.ui.spicelist.R;
import com.octo.android.robospice.sample.ui.spicelist.model.GitHubUser;
import com.octo.android.robospice.spicelist.SpiceListItemView;

public class GitHubUserView extends RelativeLayout implements SpiceListItemView<GitHubUser> {

    private TextView userNameTextView;
    private TextView githubContentTextView;
    private ImageView thumbImageView;
    private GitHubUser gitHubUser;

    public GitHubUserView(Context context) {
        super(context);
        inflateView(context);
    }

    private void inflateView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_cell_github, this);
        this.userNameTextView = (TextView) this.findViewById(R.id.user_name_textview);
        this.githubContentTextView = (TextView) this.findViewById(R.id.github_content_textview);
        this.thumbImageView = (ImageView) this.findViewById(R.id.octo_thumbnail_imageview);
    }

    @Override
    public void update(GitHubUser gitHubUser) {
        this.gitHubUser = gitHubUser;
        userNameTextView.setText(gitHubUser.getName());
        githubContentTextView.setText(String.valueOf(gitHubUser.getScore()));
    }

    @Override
    public GitHubUser getData() {
        return gitHubUser;
    }

    @Override
    public ImageView getImageView(int imageIndex) {
        return thumbImageView;
    }

    @Override
    public int getImageViewCount() {
        return 1;
    }
}
