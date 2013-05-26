package com.octo.android.robospice.sample.core;

import java.io.File;

import roboguice.util.temp.Ln;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.simple.BitmapRequest;
import com.octo.android.robospice.request.simple.SimpleTextRequest;

/**
 * This sample demonstrates how to use RoboSpice to perform simple network requests.
 * @author sni
 */
public class SampleSpiceActivity extends BaseSampleSpiceActivity {

    private static final String EARTH_IMAGE_CACHE_KEY = "image";
    // ============================================================================================
    // ATTRIBUTES
    // ============================================================================================

    private TextView mLoremTextView;
    private ImageView mImageView;

    private SimpleTextRequest loremRequest;
    private BitmapRequest imageRequest;

    // ============================================================================================
    // ACTIVITY LIFE CYCLE
    // ============================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main);

        mLoremTextView = (TextView) findViewById(R.id.textview_lorem_ipsum);
        mImageView = (ImageView) findViewById(R.id.textview_image);

        loremRequest = new SimpleTextRequest("http://www.loremipsum.de/downloads/original.txt");
        File cacheFile = new File(getApplication().getCacheDir(), "earth.jpg");
        imageRequest = new BitmapRequest("http://earthobservatory.nasa.gov/blogs/elegantfigures/files/2011/10/globe_west_2048.jpg", cacheFile);
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        activityManager.getMemoryInfo(mi);
        System.out.println(mi.availMem);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setProgressBarIndeterminate(false);
        setProgressBarVisibility(true);

        getSpiceManager().execute(loremRequest, "txt", DurationInMillis.ONE_MINUTE, new LoremRequestListener());
        getSpiceManager().execute(imageRequest, EARTH_IMAGE_CACHE_KEY, 5 * DurationInMillis.ONE_MINUTE, new ImageRequestListener());
    }

    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================

    public final class LoremRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SampleSpiceActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {
            Toast.makeText(SampleSpiceActivity.this, "success", Toast.LENGTH_SHORT).show();
            String originalText = getString(R.string.textview_text);
            mLoremTextView.setText(originalText + result);
        }
    }

    public final class ImageRequestListener implements RequestListener<Bitmap>, RequestProgressListener {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SampleSpiceActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Bitmap bitmap) {
            Toast.makeText(SampleSpiceActivity.this, "success", Toast.LENGTH_SHORT).show();
            mImageView.setImageBitmap(bitmap);
            setProgressBarVisibility(false);
            setProgressBarIndeterminateVisibility(false);
        }

        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {
            switch (progress.getStatus()) {
                case LOADING_FROM_NETWORK:
                    setProgressBarIndeterminate(false);
                    setProgress((int) (progress.getProgress() * 10000));
                    break;
                default:
                    break;
            }
            Ln.d("Binary progress : %s = %d", progress.getStatus(), Math.round(100 * progress.getProgress()));
        }
    }

}
