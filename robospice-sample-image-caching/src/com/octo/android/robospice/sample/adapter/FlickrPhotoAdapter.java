package com.octo.android.robospice.sample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.sample.model.FlickrPhoto;
import com.octo.android.robospice.sample.request.FlickrImageRequestFactory;
import com.octo.android.robospice.sample.service.FlickrSpiceService;

import java.util.ArrayList;

public class FlickrPhotoAdapter extends ArrayAdapter<FlickrPhoto> implements AbsListView.OnScrollListener{

    private final int layoutRes;
    private final int imageRes;

    private int lastScrollState = SCROLL_STATE_IDLE;

    private final FlickrImageRequestFactory imageRequestFactory;
    private final SpiceManager spiceManager;

    public FlickrPhotoAdapter(Context context,
                              int layoutRes,
                              int imageRes,
                              ArrayList<FlickrPhoto> photos,
                              FlickrImageRequestFactory requestFactory) {

        super(context, -1, -1, photos);

        this.layoutRes = layoutRes;
        this.imageRes = imageRes;
        this.imageRequestFactory = requestFactory;
        this.spiceManager = new SpiceManager(FlickrSpiceService.class);
        this.spiceManager.start(getContext());
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        if (scrollState != SCROLL_STATE_FLING && lastScrollState == SCROLL_STATE_FLING)  {
            int firstVisiblePosition = absListView.getFirstVisiblePosition();
            int lastVisiblePosition = absListView.getLastVisiblePosition();
            int numVisiblePositions = lastVisiblePosition - firstVisiblePosition + 1;

            for (int i = 0; i < numVisiblePositions; i++) {

                int adapterItemPosition = firstVisiblePosition + i;
                View visibleItem = absListView.getChildAt(i);
                ViewMetaData viewMetaData = (ViewMetaData) visibleItem.getTag();

                boolean imageNotLoaded = viewMetaData.imageState == ImageState.EMPTY;
                boolean imageNotLoadingFromNetwork = viewMetaData.imageState == ImageState.LOADING_CACHE_ONLY;
                if (imageNotLoaded || imageNotLoadingFromNetwork) {
                    executeImageRequest(adapterItemPosition, viewMetaData, false);
                }
            }
        }
        lastScrollState = scrollState;

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(layoutRes, parent, false);

            AbsListView.LayoutParams params =
                    new AbsListView.LayoutParams(
                            imageRequestFactory.getTargetWidth(),
                            imageRequestFactory.getTargetHeight());
            convertView.setLayoutParams(params);

            ViewMetaData viewMetaData = new ViewMetaData(convertView);
            convertView.setTag(viewMetaData);
        }

        ViewMetaData viewMetaData = (ViewMetaData) convertView.getTag();

        //TODO: load a proper placeholder image here

        viewMetaData.image.setImageDrawable(new ColorDrawable(Color.parseColor("#000000")));
        viewMetaData.imageState = ImageState.EMPTY;

        if (viewMetaData.pendingRequest != null) {
            viewMetaData.pendingRequest.cancel();
        }

        boolean isFlinging = lastScrollState == SCROLL_STATE_FLING;
        if (isFlinging) {
            executeImageRequest(position, viewMetaData, true);
        } else {
            executeImageRequest(position, viewMetaData, false);
        }

        return convertView;
    }

    private void executeImageRequest(int position, ViewMetaData viewMetaData, boolean cacheOnly) {
        FlickrPhoto photoSource = getItem(position);
        viewMetaData.pendingRequest = imageRequestFactory.create(photoSource);
        BitmapRequestListener requestListener = new BitmapRequestListener(viewMetaData);

        if (cacheOnly) {
            viewMetaData.imageState = ImageState.LOADING_CACHE_ONLY;
            spiceManager.getFromCache(
                    viewMetaData.pendingRequest.getResultType(),
                    (String) viewMetaData.pendingRequest.getRequestCacheKey(),
                    viewMetaData.pendingRequest.getCacheDuration(),
                    requestListener);
        } else {
            viewMetaData.imageState = ImageState.LOADING_WITH_NETWORK;
            spiceManager.execute(viewMetaData.pendingRequest,  requestListener);
        }
    }

    private class ViewMetaData {

        public ImageView image;
        public CachedSpiceRequest<Bitmap> pendingRequest;
        public ImageState imageState;


        public ViewMetaData(View itemView) {
            image = (ImageView)itemView.findViewById(imageRes);
        }
    }

    private enum ImageState {
        EMPTY,
        LOADING_WITH_NETWORK,
        LOADING_CACHE_ONLY,
        LOADING_COMPLETE
    }

    private class BitmapRequestListener implements RequestListener<Bitmap> {

        private final ViewMetaData target;

        public BitmapRequestListener(ViewMetaData target) {
            this.target = target;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            // maybe we should log something here...need to differentiate between regular and canceled requests
        }

        @Override
        public void onRequestSuccess(Bitmap bitmap) {

            if (bitmap == null) {
                target.imageState = ImageState.EMPTY;
            }
            else {
                target.imageState = ImageState.LOADING_COMPLETE;
                  target.image.setImageBitmap(bitmap);
                target.image.setImageBitmap(bitmap);
            }

            target.pendingRequest = null;
        }
    }
}
