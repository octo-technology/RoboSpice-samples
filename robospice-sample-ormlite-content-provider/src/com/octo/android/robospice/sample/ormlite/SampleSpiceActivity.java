package com.octo.android.robospice.sample.ormlite;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.sample.ormlite.model.CurrenWeatherContract;
import com.octo.android.robospice.sample.ormlite.model.WeatherContract;
import com.octo.android.robospice.sample.ormlite.network.SampleXmlRequest;

/**
 * This sample demonstrates how to use RoboSpice to perform simple network requests.
 * @author sni
 */
public class SampleSpiceActivity extends BaseSampleSpiceActivity {

    // ============================================================================================
    // ATTRIBUTES
    // ============================================================================================

    private TextView mLoremTextView;

    private SampleXmlRequest weatherRequest;

    private WeatherObserver weatherObserver;

    // ============================================================================================
    // ACTIVITY LIFE CYCLE
    // ============================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main);

        mLoremTextView = (TextView) findViewById(R.id.textview_lorem_ipsum);

        weatherRequest = new SampleXmlRequest("75000");
    }

    @Override
    protected void onStart() {
        super.onStart();

        setProgressBarIndeterminate(false);
        setProgressBarVisibility(true);

        try {
            weatherObserver = new WeatherObserver(new Handler());
            getContentResolver().registerContentObserver(WeatherContract.CONTENT_URI, true, weatherObserver);
        } catch (Exception e) {
            mLoremTextView.setText("Impossible to observer changes for " + WeatherContract.CONTENT_URI);
            e.printStackTrace();
        }

        getSpiceManager().execute(weatherRequest, new Integer(0), DurationInMillis.ALWAYS_EXPIRED, null);

    }

    // ============================================================================================
    // PRIVATE METHODS
    // ============================================================================================

    private String getTemperatureViaContentProvider() {
        String temperature = "";
        Cursor c = getContentResolver().query(CurrenWeatherContract.CONTENT_URI, null, null, null, null);
        if (c.moveToNext()) {
            temperature = c.getString(c.getColumnIndex(CurrenWeatherContract.TEMP));
        }
        c.close();
        return temperature;
    }

    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================

    public class WeatherObserver extends ContentObserver {
        public WeatherObserver(Handler handler) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            String originalText = getString(R.string.textview_text);
            String temperature = getTemperatureViaContentProvider();
            mLoremTextView.setText(originalText + temperature);
        }
    }

}
