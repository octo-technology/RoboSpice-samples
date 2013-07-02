package com.octo.android.robospice.sample.request;

import java.util.HashMap;

import com.octo.android.robospice.sample.model.FlickrInterestingPhotosResponse;
import com.octo.android.robospice.sample.model.FlickrPhotoList;

/**
 * A Request to download the list of interesting photos.
 * @author David Stemmer
 * @see FlickrPhotoList
 */
public class FlickrInterestingPhotosRequest extends FlickrRequest<FlickrPhotoList> {

    private final String apiKey;

    public FlickrInterestingPhotosRequest(String apiKey) {
        super(FlickrPhotoList.class);
        this.apiKey = apiKey;
    }

    @Override
    public FlickrPhotoList loadDataFromNetwork() throws Exception {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "flickr.interestingness.getList");
        params.put("api_key", apiKey);

        String pathTemplate = getServiceUrl() + "?method={method}&api_key={api_key}";
        FlickrInterestingPhotosResponse response = getRestTemplate().getForObject(pathTemplate,
            FlickrInterestingPhotosResponse.class, params);

        if (!response.success()) {
            throw response.getException();
        }

        return response.getPhotoList();
    }

}
