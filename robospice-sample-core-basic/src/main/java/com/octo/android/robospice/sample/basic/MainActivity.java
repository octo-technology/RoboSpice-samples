package com.octo.android.robospice.sample.basic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity {

    private static final String KEY_RESULT = "result";

    /***
     * With {@link UncachedSpiceService} there is no cache management.
     * Remember to declare it in AndroidManifest.xml
     */
    private SpiceManager spiceManager = new SpiceManager(
        UncachedSpiceService.class);
    
    private TextView resultTextView;
    private EditText wordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        initUIComponents();
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private void initUIComponents() {
        Button reverseButton = (Button) findViewById(R.id.reverse_button);
        wordField = (EditText) findViewById(R.id.word_field);
        resultTextView = (TextView) findViewById(R.id.result);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRequest(wordField.getText().toString());
            }
        });
    }

    private void performRequest(String searchQuery) {
        resultTextView.setText("");

        MainActivity.this.setProgressBarIndeterminateVisibility(true);

        ReverseStringRequest request = new ReverseStringRequest(searchQuery);
        spiceManager.execute(request, new ReverseStringRequestListener());

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(wordField.getWindowToken(), 0);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.container);
        linearLayout.requestFocus();
    }

    private final class ReverseStringRequestListener implements
        RequestListener<String> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(MainActivity.this,
                "Error: " + spiceException.getMessage(), Toast.LENGTH_SHORT)
                .show();
        }

        @Override
        public void onRequestSuccess(String result) {
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
            resultTextView.setText(getString(R.string.result_text, result));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String result = resultTextView.getText().toString();
        if (!TextUtils.isEmpty(result)) {
            outState.putString(KEY_RESULT, result);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        spiceManager.addListenerIfPending(String.class, null,
            new ReverseStringRequestListener());

        if (savedInstanceState.containsKey(KEY_RESULT)) {
            String result = savedInstanceState.getString(KEY_RESULT);
            resultTextView.setText(result);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
