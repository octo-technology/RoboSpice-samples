package com.octo.android.robospice.sample.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class FlickrPhotoList {

    @ElementList(inline=true, entry = "photo")
    private List<FlickrPhoto> photos;

    @Attribute(name="page")
    private int currentPage;

    @Attribute(name="pages")
    private int numPages;

    @Attribute(name="perpage")
    private int photosPerPage;

    @Attribute(name="total")
    private int totalPages;


    public List<FlickrPhoto> getPhotos() {
        return photos;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNumPages() {
        return numPages;
    }

    public int getPhotosPerPage() {
        return photosPerPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
