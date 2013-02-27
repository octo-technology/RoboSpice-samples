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
                executeImageRequest(adapterItemPosition, viewMetaData);
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

        viewMetaData.image.setImageDrawable(new ColorDrawable(Color.parseColor("#000000")));

        if (viewMetaData.pendingRequest != null) {
            viewMetaData.pendingRequest.cancel();
        }

        if (lastScrollState != SCROLL_STATE_FLING) {
            executeImageRequest(position, viewMetaData);
        }

        return convertView;
    }

    private void executeImageRequest(int position, ViewMetaData viewMetaData) {
        FlickrPhoto photoSource = getItem(position);
        viewMetaData.pendingRequest = imageRequestFactory.create(photoSource);
        BitmapRequestListener requestListener = new BitmapRequestListener(viewMetaData.image);
        spiceManager.execute(viewMetaData.pendingRequest,  requestListener);
    }

    private class ViewMetaData {

        public ImageView image;
        public CachedSpiceRequest<Bitmap> pendingRequest;

        public ViewMetaData(View itemView) {
            image = (ImageView)itemView.findViewById(imageRes);
        }
    }

    private class BitmapRequestListener implements RequestListener<Bitmap> {

        private final ImageView target;

        public BitmapRequestListener(ImageView target) {
            this.target = target;
        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            // maybe we should log something here...need to differentiate between regular and canceled requests
        }

        @Override
        public void onRequestSuccess(Bitmap bitmap) {

            target.setImageBitmap(bitmap);
        }
    }
}
