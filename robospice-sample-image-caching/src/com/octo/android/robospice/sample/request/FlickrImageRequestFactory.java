package com.octo.android.robospice.sample.request;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import roboguice.util.temp.Ln;
import android.content.Context;
import android.graphics.Bitmap;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.simple.BitmapRequest;
import com.octo.android.robospice.sample.model.FlickrPhoto;

/**
 * A customizable RoboSpice requests factory to download images from Flickr.
 * @author David Stemmer
 */
public class FlickrImageRequestFactory {

    private final Context context;

    private int targetWidth = -1;
    private int targetHeight = -1;
    private String photoSizeSuffix;

    public static final String SMALL_THUMB_SQUARE = "s";
    public static final String LARGE_THUMB_SQUARE = "q";
    public static final String THUMBNAIL = "t";
    public static final String SMALL_240 = "m";
    public static final String SMALL_320 = "n";
    public static final String MEDIUM_500 = "-";
    public static final String MEDIUM_640 = "z";
    public static final String MEDIUM_800 = "c";
    public static final String LARGE_1024 = "b";
    public static final String ORIGINAL = "o";

    public FlickrImageRequestFactory(Context context) {
        this.context = context;
    }

    public FlickrImageRequestFactory setSampleSize(int height, int width) {
        targetWidth = width;
        targetHeight = height;
        return this;
    }

    public FlickrImageRequestFactory setPhotoFormat(String format) {
        photoSizeSuffix = format;
        return this;
    }

    public CachedSpiceRequest<Bitmap> create(FlickrPhoto photoSource) {

        String photoUrlFormat = "http://farm%s.staticflickr.com/%s/%s_%s_%s.jpg";
        String photoUrl = String.format(photoUrlFormat, photoSource.getFarm(),
            photoSource.getServer(), photoSource.getId(), photoSource.getSecret(), photoSizeSuffix);
        File cacheFile = null;
        String filename = null;
        try {
            filename = URLEncoder.encode(photoUrl, "UTF-8");
            cacheFile = new File(context.getCacheDir(), filename);
        } catch (UnsupportedEncodingException e) {
            Ln.e(e);
        }

        BitmapRequest request = new BitmapRequest(photoUrl, targetWidth, targetHeight, cacheFile);
        return new CachedSpiceRequest<Bitmap>(request, filename, DurationInMillis.ONE_MINUTE * 10);
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public int getTargetWidth() {
        return targetWidth;
    }
}
