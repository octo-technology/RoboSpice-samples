package com.octo.android.robospice.sample.request;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * A request to download images from Flickr.
 * @author David Stemmer
 */
public abstract class FlickrRequest<RESULT> extends SpringAndroidSpiceRequest<RESULT> {

    private static final String FLICKR_SERVICE_URL = "http://api.flickr.com/services/rest/";

    protected FlickrRequest(Class<RESULT> clazz) {
        super(clazz);
    }

    protected String getServiceUrl() {
        return FLICKR_SERVICE_URL;
    }

}
