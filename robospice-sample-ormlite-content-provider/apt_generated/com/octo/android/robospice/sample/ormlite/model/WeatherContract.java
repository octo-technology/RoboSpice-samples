package com.octo.android.robospice.sample.ormlite.model;


import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

public final class WeatherContract
    implements BaseColumns {
  public static final String AUTHORITY = "com.octo.android.robospice.sample.ormlite.model";

  public static final String CONTENT_URI_PATH = "weather";

  public static final String MIMETYPE_TYPE = "weather";
  public static final String MIMETYPE_NAME = "com.octo.android.robospice.sample.ormlite.model.provider";

  public static final int CONTENT_URI_PATTERN_MANY = 1;
  public static final int CONTENT_URI_PATTERN_ONE = 2;

  public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();


  public static final String EMPTYFIELD = "emptyField";
}
