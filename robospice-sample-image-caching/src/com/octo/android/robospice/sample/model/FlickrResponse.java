package com.octo.android.robospice.sample.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class FlickrResponse {

    @Attribute(name = "stat")
    private String status;

    @Element(name = "err", required = false)
    private FlickrException exception;

    public boolean success() {
        return status.equals("ok");
    }

    public FlickrException getException() {
        return exception;
    }
}
