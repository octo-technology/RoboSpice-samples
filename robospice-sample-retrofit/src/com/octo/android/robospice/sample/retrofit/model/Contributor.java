package com.octo.android.robospice.sample.retrofit.model;

import java.util.ArrayList;

public class Contributor {
    public String login;
    public int contributions;

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Contributor> {
    }
}
