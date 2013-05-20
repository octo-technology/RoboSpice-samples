package com.octo.android.robospice.sample.retrofit.network;

import roboguice.util.temp.Ln;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest2;
import com.octo.android.robospice.sample.retrofit.model.Contributor;

public class SampleRetrofitSpiceRequest extends RetrofitSpiceRequest2<Contributor.List, GitHub> {

    private final static String BASE_URL = "https://api.github.com";
    private String owner;
    private String repo;

    public SampleRetrofitSpiceRequest(String owner, String repo) {
        super(Contributor.List.class, GitHub.class, BASE_URL);
        this.owner = owner;
        this.repo = repo;
    }

    @Override
    public Contributor.List loadDataFromNetwork() {
        Ln.d("Call web service " + BASE_URL);
        return getRetrofitedService().contributors(owner, repo);
    }
}
