package com.octo.android.robospice.sample.ormlite.model;


import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

public final class CurrenWeatherContract
    implements BaseColumns {
  public static final String AUTHORITY = "com.octo.android.robospice.sample.ormlite.model";

  public static final String CONTENT_URI_PATH = "currenweather";

  public static final String MIMETYPE_TYPE = "currenweather";
  public static final String MIMETYPE_NAME = "com.octo.android.robospice.sample.ormlite.model.provider";

  public static final int CONTENT_URI_PATTERN_MANY = 3;
  public static final int CONTENT_URI_PATTERN_ONE = 4;

  public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

  private CurrenWeatherContract() {
  }


  public static final String HUMIDITY = "humidity";
  public static final String PRESSURE = "pressure";
  public static final String TEMP = "temp";
  public static final String TEMP_UNIT = "temp_unit";
  public static final String WEATHER = "weather";
  public static final String WEATHER_CODE = "weather_code";
  public static final String WEATHER_TEXT = "weather_text";
}
