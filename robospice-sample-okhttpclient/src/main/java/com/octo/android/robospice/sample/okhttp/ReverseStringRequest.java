package com.octo.android.robospice.sample.okhttp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import org.apache.commons.io.IOUtils;

import android.net.Uri;

import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;

public class ReverseStringRequest extends OkHttpSpiceRequest<String> {

    private String word;

    public ReverseStringRequest(String word) {
        super(String.class);
        this.word = word;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {

        // With Uri.Builder class we can build our url is a safe manner
        Uri.Builder uriBuilder = Uri.parse("http://robospice-sample.appspot.com/reverse").buildUpon();
        uriBuilder.appendQueryParameter("word", word);

        URI uri = new URI(uriBuilder.build().toString());

        HttpURLConnection connection = getOkHttpClient().open(uri.toURL());
        InputStream in = null;
        try {
            // Read the response.
            in = connection.getInputStream();
            return IOUtils.toString(in, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
