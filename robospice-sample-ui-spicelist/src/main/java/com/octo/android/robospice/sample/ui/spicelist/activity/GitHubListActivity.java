package com.octo.android.robospice.sample.ui.spicelist.activity;

import roboguice.util.temp.Ln;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.sample.ui.spicelist.R;
import com.octo.android.robospice.sample.ui.spicelist.adapter.GitHubUserListAdapter;
import com.octo.android.robospice.sample.ui.spicelist.model.GitHubUser;
import com.octo.android.robospice.sample.ui.spicelist.network.GitHubRequest;
import com.octo.android.robospice.spicelist.okhttp.OkHttpBitmapSpiceManager;

public class GitHubListActivity extends Activity {

    private ListView githubListView;
    private View loadingView;

    private GitHubUserListAdapter gitHubUserListAdapter;

    private SpiceManager spiceManagerJson = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private OkHttpBitmapSpiceManager spiceManagerBinary = new OkHttpBitmapSpiceManager();

    // --------------------------------------------------------------------------------------------
    // ACTIVITY LIFECYCLE
    // --------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Ln.getConfig().setLoggingLevel(Log.ERROR);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_githublist);

        githubListView = (ListView) findViewById(R.id.listview_github);
        loadingView = findViewById(R.id.loading_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManagerJson.start(this);
        spiceManagerBinary.start(this);

        loadListGithub();
    }

    @Override
    public void onStop() {
        spiceManagerJson.shouldStop();
        spiceManagerBinary.shouldStop();
        super.onStop();
    }

    // --------------------------------------------------------------------------------------------
    // PRIVATE
    // --------------------------------------------------------------------------------------------

    private void updateListViewContent(GitHubUser.List users) {
        gitHubUserListAdapter = new GitHubUserListAdapter(this, spiceManagerBinary, users);
        githubListView.setAdapter(gitHubUserListAdapter);

        loadingView.setVisibility(View.GONE);
        githubListView.setVisibility(View.VISIBLE);
    }

    private void loadListGithub() {
        setProgressBarIndeterminateVisibility(true);
        spiceManagerJson.execute(new GitHubRequest("android"), "github", DurationInMillis.ONE_SECOND * 10,
                new GitHubUserListListener());
    }

    // --------------------------------------------------------------------------------------------
    // PRIVATE
    // --------------------------------------------------------------------------------------------

    private class GitHubUserListListener implements RequestListener<GitHubUser.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgressBarIndeterminateVisibility(false);
            Toast.makeText(GitHubListActivity.this, "Impossible to get the list of users", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(GitHubUser.List result) {
            setProgressBarIndeterminateVisibility(false);
            updateListViewContent(result);
        }
    }

}
