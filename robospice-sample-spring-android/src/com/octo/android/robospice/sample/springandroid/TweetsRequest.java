package com.octo.android.robospice.sample.springandroid;

import android.net.Uri;

import com.octo.android.robospice.sample.springandroid.model.ListTweets;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class TweetsRequest extends SpringAndroidSpiceRequest< ListTweets > {

    private String keyword;

    public TweetsRequest( String keyword ) {
        super( ListTweets.class );
        this.keyword = keyword;
    }

    @Override
    public ListTweets loadDataFromNetwork() throws Exception {

        // With Uri.Builder class we can build our url is a safe manner
        Uri.Builder uriBuilder = Uri.parse( "http://search.twitter.com/search.json" ).buildUpon();
        uriBuilder.appendQueryParameter( "q", keyword );
        uriBuilder.appendQueryParameter( "rpp", "20" );
        uriBuilder.appendQueryParameter( "lang", "en" );

        String url = uriBuilder.build().toString();

        return getRestTemplate().getForObject( url, ListTweets.class );
    }

    /**
     * This method generates a unique cache key for this request. In this case our cache key depends just on the
     * keyword.
     * 
     * @return
     */
    public String createCacheKey() {
        return "tweets." + keyword;
    }
}