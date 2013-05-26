package com.octo.android.robospice.sample.retrofit.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class SampleRetrofitSpiceService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "https://api.github.com";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(GitHub.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}
