package loop.ms.loophello;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import ms.loop.loopsdk.core.ILoopSDKCallback;
import ms.loop.loopsdk.core.LoopSDK;
import ms.loop.loopsdk.providers.LoopLocationProvider;
import ms.loop.loopsdk.signal.SignalConfig;
import ms.loop.loopsdk.util.LoopError;

public class LoopHelloApplication extends Application implements ILoopSDKCallback {
    public static final String LOOP_SDK_STATE = "loopSdkState";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOOP_SDK_STATE, "uninitialized");
        editor.apply();

        // create an account to get your appId and appToken
        String appId = "YOUR_APP_ID";
        String appToken = "YOUR_APP_TOKEN";

        // initialize the Loop SDK with your appId and appToken
        // the last param enables location analytics
        LoopSDK.initialize(getApplicationContext(), appId, appToken, true);
    }

    // called by the Loop SDK on successful initialization
    @Override
    public void onInitialized() {
        Log.d("loop_sdk", "Successfully Initialized");

        LoopLocationProvider.start(SignalConfig.SIGNAL_SEND_MODE_BATCH);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOOP_SDK_STATE, "initialized");
        editor.apply();
    }

    // called by the Loop SDK when the provider status changes
    @Override
    public void onServiceStatusChanged(String provider, String status, Bundle bundle) {
        Log.d("loop_sdk", "Provider: " + provider + " status: " + status);
    }

    @Override
    public void onDebug(String output) {}

    // called by the Loop SDK when initialization fails
    @Override
    public void onInitializeFailed(LoopError loopError) {
        Log.d("loop_sdk", "Loop SDK failed: " + loopError.toString());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOOP_SDK_STATE, "error");
        editor.apply();
    }
}
