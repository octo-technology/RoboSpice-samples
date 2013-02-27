package com.octo.android.robospice.sample.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.GridView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.sample.R;
import com.octo.android.robospice.sample.adapter.FlickrPhotoAdapter;
import com.octo.android.robospice.sample.model.FlickrPhoto;
import com.octo.android.robospice.sample.model.FlickrPhotoList;
import com.octo.android.robospice.sample.request.FlickrImageRequestFactory;
import com.octo.android.robospice.sample.request.FlickrInterestingPhotosRequest;
import com.octo.android.robospice.sample.service.FlickrSpiceService;

import java.util.ArrayList;


public class SampleActivity extends Activity {

    private static final String FLICKR_API_KEY = "cd3660161605453352f8839e32d2e3fc";

    private static final int PHOTO_WIDTH = 150;
    private static final int MIN_COLUMNS = 2;
    private static final int MAX_COLUMNS = 4;

    private GridView imageGrid;
    private FlickrPhotoAdapter photoAdapter;

    private SpiceManager contentManager = new SpiceManager(
        FlickrSpiceService.class);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        bindViews();
    }

    private void bindViews() {

        imageGrid = (GridView) findViewById(R.id.image_grid);

        int gridSize = sizeColumnsToFit(imageGrid, PHOTO_WIDTH, MIN_COLUMNS, MAX_COLUMNS);

        FlickrImageRequestFactory imageRequestFactory = new FlickrImageRequestFactory(this);
        imageRequestFactory
                .setPhotoFormat(FlickrImageRequestFactory.LARGE_THUMB_SQUARE)
                .setSampleSize(gridSize, gridSize);

        photoAdapter = new FlickrPhotoAdapter(this, R.layout.grid_view_item,
            R.id.image, new ArrayList<FlickrPhoto>(), imageRequestFactory);
        imageGrid.setAdapter(photoAdapter);
        imageGrid.setOnScrollListener(photoAdapter);
    }


    @TargetApi(13)
    private int sizeColumnsToFit(GridView grid, int minColumnWidth, int minColumns, int maxColumns) {

        Display display = getWindowManager().getDefaultDisplay();

        int screenWidth;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            screenWidth = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        int numColumns = screenWidth / minColumnWidth;
        numColumns = Math.min(numColumns, maxColumns);
        numColumns = Math.max(numColumns, minColumns);
        int remainingSpace = screenWidth - (numColumns * minColumnWidth);
        int columnWidth = minColumnWidth + (remainingSpace / numColumns);

        grid.setNumColumns(numColumns);
        grid.setColumnWidth(columnWidth);

        return columnWidth;
    }

    @Override
    protected void onStart() {
        contentManager.start(this);

        FlickrInterestingPhotosRequest request = new FlickrInterestingPhotosRequest(FLICKR_API_KEY);
        PhotoListRequestListener requestListener = new PhotoListRequestListener();

        contentManager.execute(request, requestListener);

        super.onStart();
    }

    @Override
    protected void onStop() {
        contentManager.shouldStop();
        super.onStop();
    }

    private class PhotoListRequestListener implements
        RequestListener<FlickrPhotoList> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            // need to display refresh image
        }

        @Override
        public void onRequestSuccess(FlickrPhotoList flickrPhotoList) {
            for (FlickrPhoto photo : flickrPhotoList.getPhotos()) {
                photoAdapter.add(photo);
            }
        }
    }

}
