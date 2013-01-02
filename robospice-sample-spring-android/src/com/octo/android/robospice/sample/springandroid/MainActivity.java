package com.octo.android.robospice.sample.springandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.sample.springandroid.model.ListTweets;
import com.octo.android.robospice.sample.springandroid.model.Tweet;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

public class MainActivity extends Activity {

    private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

    private SpiceManager contentManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    private ArrayAdapter<String> tweetsAdapter;

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
        contentManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        contentManager.shouldStop();
        super.onStop();
    }

    private void initUIComponents() {
        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchQuery = (EditText) findViewById(R.id.search_field);
        ListView tweetsList = (ListView) findViewById(R.id.search_results);

        tweetsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        tweetsList.setAdapter(tweetsAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRequest(searchQuery.getText().toString());
            }
        });
    }

    private void performRequest(String searchQuery) {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);

        TweetsRequest request = new TweetsRequest(searchQuery);
        lastRequestCacheKey = request.createCacheKey();
        contentManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListTweetsRequestListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(lastRequestCacheKey))
            outState.putString(KEY_LAST_REQUEST_CACHE_KEY, lastRequestCacheKey);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(KEY_LAST_REQUEST_CACHE_KEY)) {
            lastRequestCacheKey = savedInstanceState.getString(KEY_LAST_REQUEST_CACHE_KEY);
            contentManager.addListenerIfPending(ListTweets.class, lastRequestCacheKey, new ListTweetsRequestListener());
            contentManager.getFromCache(ListTweets.class, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListTweetsRequestListener());
        }
    }

    private class ListTweetsRequestListener implements RequestListener<ListTweets> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(MainActivity.this, "Error during request: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ListTweets listTweets) {

            //listTweets could be null just if contentManager.getFromCache(...) doesn't return anything.
            if (listTweets == null)
                return;

            tweetsAdapter.clear();

            final List<Tweet> tweets = listTweets.getResults();
            for (Tweet tweet : tweets) {
                tweetsAdapter.add(tweet.getText());
            }

            tweetsAdapter.notifyDataSetChanged();

            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }
}

