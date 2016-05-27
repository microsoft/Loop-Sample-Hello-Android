package loop.ms.loophello;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ms.loop.loopsdk.api.LoopHttpError;
import ms.loop.loopsdk.core.ILoopServiceCallback;
import ms.loop.loopsdk.core.LoopSDK;
import ms.loop.loopsdk.util.LoopError;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public Button sendTestButton = null;
    public ImageView checkboxImageView = null;
    public ImageView errorImageView = null;
    public TextView testCompletedView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(loop.ms.loophello.R.layout.activity_main);

        sendTestButton = (Button) findViewById(R.id.sendTestSignalBtn);
        checkboxImageView = (ImageView) findViewById(R.id.checkBoxView);
        errorImageView = (ImageView) findViewById(R.id.errorView);
        testCompletedView = (TextView) findViewById(R.id.testCompletedView);

        if (sendTestButton == null
                || checkboxImageView == null
                || errorImageView == null
                || testCompletedView == null) {
            Log.d(TAG, "Error loading layout resources");

        }

        checkLoopSDKState();

        sendTestButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxImageView.setVisibility(View.INVISIBLE);
                errorImageView.setVisibility(View.INVISIBLE);
                testCompletedView.setVisibility(View.INVISIBLE);
                testCompletedView.setTextColor(Color.green(Color.GREEN));

                LoopSDK.sendTestSignal(new ILoopServiceCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer signalsUploaded) {
                        Log.d(TAG, "sendTestSignal success statusCode: 201");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run(){
                                checkboxImageView.setVisibility(View.VISIBLE);
                                testCompletedView.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onError(LoopError error) {
                        String errorStart = "Send test error";
                        String errorEnd = "";

                        if (error.getClass() == LoopHttpError.class) {
                            LoopHttpError httpError = (LoopHttpError) error;
                            errorEnd = "\nStatus code: " + httpError.status + "\nReason: " + httpError.reason;
                            Log.d(TAG, errorStart + errorEnd);
                        }
                        else {
                            Log.d(TAG, errorStart);
                        }

                        final String errorMessage = errorStart + errorEnd;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorImageView.setVisibility(View.VISIBLE);

                                testCompletedView.setText(errorMessage);
                                testCompletedView.setTextColor(Color.BLACK);
                                testCompletedView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == LoopHelloApplication.LOOP_SDK_STATE) {
            String sdkState = sharedPreferences.getString(LoopHelloApplication.LOOP_SDK_STATE, "uninitialized");
            if (sdkState != "uninitialized") {
                setupInitialUI(sdkState == "initialized");
            }
        }
    }

    private void checkLoopSDKState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String sdkState = sharedPreferences.getString(LoopHelloApplication.LOOP_SDK_STATE, "uninitialized");
        if (sdkState != "uninitialized") {
            setupInitialUI(sdkState == "initialized");
        }
    }

    private void setupInitialUI(Boolean sdkSuccessfullyInitialized) {
        sendTestButton.setEnabled(sdkSuccessfullyInitialized);
        errorImageView.setVisibility(sdkSuccessfullyInitialized ? View.INVISIBLE : View.VISIBLE);

        if (!sdkSuccessfullyInitialized) {
            String errorMessage = "Error initializing Loop SDK";
            if (LoopSDK.appId == "YOUR_APP_ID" || LoopSDK.appToken == "YOUR_APP_TOKEN") {
                errorMessage = "No App Id or Token.\nSee onCreate in LoopHelloApplication\nCreate an app at developer.dev.loop.ms";
            }

            testCompletedView.setText(errorMessage);
            testCompletedView.setTextColor(Color.BLACK);
            testCompletedView.setVisibility(View.VISIBLE);
        }
    }
}
