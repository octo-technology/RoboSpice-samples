package com.octo.android.robospice.sample.springandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.sample.springandroid.model.Follower;
import com.octo.android.robospice.sample.springandroid.model.FollowerList;

public class MainActivity extends Activity {

    private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

    private SpiceManager spiceManager = new SpiceManager(
        JacksonSpringAndroidSpiceService.class);

    private ArrayAdapter<String> followersAdapter;

    private String lastRequestCacheKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);

        initUIComponents();
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private void initUIComponents() {
        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchQuery = (EditText) findViewById(R.id.search_field);
        ListView followersList = (ListView) findViewById(R.id.search_results);

        followersAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1);
        followersList.setAdapter(followersAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRequest(searchQuery.getText().toString());
                // clear focus
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_layout);
                linearLayout.requestFocus();
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);
            }
        });
    }

    private void performRequest(String user) {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);

        FollowersRequest request = new FollowersRequest(user);
        lastRequestCacheKey = request.createCacheKey();
        spiceManager.execute(request, lastRequestCacheKey,
            DurationInMillis.ONE_MINUTE, new ListFollowersRequestListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(lastRequestCacheKey)) {
            outState.putString(KEY_LAST_REQUEST_CACHE_KEY, lastRequestCacheKey);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(KEY_LAST_REQUEST_CACHE_KEY)) {
            lastRequestCacheKey = savedInstanceState
                .getString(KEY_LAST_REQUEST_CACHE_KEY);
            spiceManager.addListenerIfPending(FollowerList.class,
                lastRequestCacheKey, new ListFollowersRequestListener());
            spiceManager.getFromCache(FollowerList.class,
                lastRequestCacheKey, DurationInMillis.ONE_MINUTE,
                new ListFollowersRequestListener());
        }
    }

    private class ListFollowersRequestListener implements
        RequestListener<FollowerList> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(MainActivity.this,
                "Error during request: " + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                .show();
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        public void onRequestSuccess(FollowerList listFollowers) {

            // listFollowers could be null just if contentManager.getFromCache(...)
            // doesn't return anything.
            if (listFollowers == null) {
                return;
            }

            followersAdapter.clear();

            for (Follower follower : listFollowers) {
                followersAdapter.add(follower.getLogin());
            }

            followersAdapter.notifyDataSetChanged();

            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }
}
