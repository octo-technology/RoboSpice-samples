package com.octo.android.robospice.sample.basic.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
    }

}
