package com.octo.android.robospice.sample.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Represents Flickr exceptions.
 */
@Root
public class FlickrException extends Exception {

    private static final long serialVersionUID = 782878521149909868L;

    @Attribute
    private int code;

    @Attribute
    private String msg;

    @Override
    public String getMessage() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
