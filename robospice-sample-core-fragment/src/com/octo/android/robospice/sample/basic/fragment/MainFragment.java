package com.octo.android.robospice.sample.basic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainFragment extends Fragment {

    /***
     * With {@link UncachedSpiceService} there is no cache management. Remember to declare it in
     * AndroidManifest.xml
     */
    private SpiceManager spiceManager = new SpiceManager(SampleInMemorySpiceService.class);

    private TextView resultTextView;
    private EditText wordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUIComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
        if (wordField.getText() != null) {
            spiceManager.addListenerIfPending(String.class, wordField.getText().toString(), new ReverseStringRequestListener());
        }
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private void initUIComponents() {
        Button reverseButton = (Button) getView().findViewById(R.id.reverse_button);
        wordField = (EditText) getView().findViewById(R.id.word_field);
        resultTextView = (TextView) getView().findViewById(R.id.result);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRequest(wordField.getText().toString());
            }
        });
    }

    private void performRequest(String searchQuery) {
        resultTextView.setText("");

        MainFragment.this.getActivity().setProgressBarIndeterminateVisibility(true);

        ReverseStringRequest request = new ReverseStringRequest(searchQuery);
        spiceManager.execute(request, searchQuery, DurationInMillis.ALWAYS_RETURNED, new ReverseStringRequestListener());

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(wordField.getWindowToken(), 0);

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.container);
        linearLayout.requestFocus();
    }

    private final class ReverseStringRequestListener implements RequestListener<String> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "Error: " + spiceException.getMessage(), Toast.LENGTH_SHORT).show();
            MainFragment.this.getActivity().setProgressBarIndeterminateVisibility(false);
        }

        @Override
        public void onRequestSuccess(String result) {
            MainFragment.this.getActivity().setProgressBarIndeterminateVisibility(false);
            resultTextView.setText(getString(R.string.result_text, result));
        }
    }
}
