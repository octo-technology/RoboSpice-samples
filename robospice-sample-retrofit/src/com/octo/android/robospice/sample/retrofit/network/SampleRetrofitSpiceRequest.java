package com.octo.android.robospice.sample.retrofit.network;

import roboguice.util.temp.Ln;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.octo.android.robospice.sample.retrofit.model.Contributor;

public class SampleRetrofitSpiceRequest extends RetrofitSpiceRequest<Contributor.List, GitHub> {

    private String owner;
    private String repo;

    public SampleRetrofitSpiceRequest(String owner, String repo) {
        super(Contributor.List.class, GitHub.class);
        this.owner = owner;
        this.repo = repo;
    }

    @Override
    public Contributor.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().contributors(owner, repo);
    }
}
