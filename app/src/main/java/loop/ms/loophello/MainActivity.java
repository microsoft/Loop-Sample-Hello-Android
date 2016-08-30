package loop.ms.loophello;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

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
    private GoogleApiClient googleApiClient;

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

                LoopSDK.sendTestLocationSignal(new ILoopServiceCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer signalsUploaded) {
                        Log.d(TAG, "send location success statusCode: 201");

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

    @Override
    public void onStart(){
        super.onStart();

        if (!isLocationTurnedOn()){
            openLocationServiceSettingPage();
        }
    }

    public boolean isLocationTurnedOn() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean locationEnabled = false;

        try {
            locationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (locationEnabled) {
                locationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception ex) {
        }
        return locationEnabled;
    }

    public  void openLocationServiceSettingPage() {
        try {
            if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API).build();
                googleApiClient.connect();
            }

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, 0x1);
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });

        } catch (Exception e) {
        }
    }
}
