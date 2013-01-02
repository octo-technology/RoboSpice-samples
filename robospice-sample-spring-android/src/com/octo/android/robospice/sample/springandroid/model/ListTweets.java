package com.octo.android.robospice.sample.springandroid.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListTweets {


    private List<Tweet> results;

    public List<Tweet> getResults() {
        return results;
    }

    public void setResults(List<Tweet> results) {
        this.results = results;
    }
}