package com.octo.android.robospice.sample.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class FlickrPhoto {

    @Attribute
    private String id;

    @Attribute
    private String owner;

    @Attribute
    private String secret;

    @Attribute
    private String server;

    @Attribute
    private String farm;

    @Attribute
    private String title;

    @Attribute(name = "ispublic")
    private boolean isPublic;

    @Attribute(name = "isfriend")
    private boolean isFriend;

    @Attribute(name = "isfamily")
    private boolean isFamily;

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isFamily() {
        return isFamily;
    }
}
