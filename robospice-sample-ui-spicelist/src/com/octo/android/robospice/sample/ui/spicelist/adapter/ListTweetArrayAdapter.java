package com.octo.android.robospice.sample.ui.spicelist.adapter;

import java.io.File;

import android.content.Context;
import android.view.ViewGroup;

import com.octo.android.robospice.request.simple.BitmapRequest;
import com.octo.android.robospice.sample.ui.spicelist.model.ListTweets;
import com.octo.android.robospice.sample.ui.spicelist.model.Tweet;
import com.octo.android.robospice.sample.ui.spicelist.view.TweetListItemView;
import com.octo.android.robospice.spicelist.BitmapSpiceManager;
import com.octo.android.robospice.spicelist.SpiceArrayAdapter;
import com.octo.android.robospice.spicelist.SpiceListItemView;

/**
 * An example {@link SpiceArrayAdapter}.
 * @author jva
 * @author stp
 * @author sni
 */
public class ListTweetArrayAdapter extends SpiceArrayAdapter<Tweet> {

    // --------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------------------------------------------------

    public ListTweetArrayAdapter(Context context, BitmapSpiceManager spiceManagerBitmap,
        ListTweets tweets) {
        super(context, spiceManagerBitmap, tweets.getResults());
    }

    @Override
    public BitmapRequest createRequest(Tweet tweet, int requestImageWidth, int requestImageHeight) {
        File tempFile = new File(getContext().getCacheDir(), "THUMB_IMAGE_TEMP_"
            + tweet.getFrom_user());
        return new BitmapRequest(tweet.getProfile_image_url(), requestImageWidth,
            requestImageHeight, tempFile);
    }

    @Override
    public SpiceListItemView<Tweet> createView(Context context, ViewGroup parent) {
        return new TweetListItemView(getContext());
    }
}
