package com.octo.android.robospice.sample.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class FlickrException extends Exception {

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
