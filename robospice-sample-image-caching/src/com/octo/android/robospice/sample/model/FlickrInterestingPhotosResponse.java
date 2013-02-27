package com.octo.android.robospice.sample.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class FlickrInterestingPhotosResponse extends FlickrResponse {

    @Element(name="photos", required = false)
    private FlickrPhotoList photoList;

    public FlickrPhotoList getPhotoList() {
        return photoList;
    }
}
