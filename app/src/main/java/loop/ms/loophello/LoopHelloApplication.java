package loop.ms.loophello;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import ms.loop.loopsdk.core.ILoopSDKCallback;
import ms.loop.loopsdk.core.LoopSDK;
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

        // initialize the Loop SDK. create an account to get your appId and appToken
        String appId = "sdk-v2-sample-8f475be8";
        String appToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6InNkay12Mi1zYW1wbGUtOGY0NzViZTgiLCJhcHBLZXkiOiI0NmM0MmRiYTAxOTI0MmUzYjA4YjI5NGRhODE4OGNlYyIsImFsbG93ZWRSb3V0ZXMiOlt7Im1ldGhvZCI6InBvc3QiLCJwYXRoIjoiL3YyLjAvYXBwL3Nkay12Mi1zYW1wbGUtOGY0NzViZTgvdXNlciJ9LHsibWV0aG9kIjoiZGVsZXRlIiwicGF0aCI6Ii92Mi4wL2FwcC9zZGstdjItc2FtcGxlLThmNDc1YmU4L3VzZXIifSx7Im1ldGhvZCI6InBvc3QiLCJwYXRoIjoiL3YyLjAvYXBwL3Nkay12Mi1zYW1wbGUtOGY0NzViZTgvbG9naW4ifSx7Im1ldGhvZCI6ImdldCIsInBhdGgiOiIvdjIuMC9hcHAvc2RrLXYyLXNhbXBsZS04ZjQ3NWJlOC91c2VyIn1dLCJpYXQiOjE0NjMxNzQyMzUsImlzcyI6Ikxvb3AgQXV0aCB2MiIsInN1YiI6InNkay12Mi1zYW1wbGUtOGY0NzViZTgifQ.AXnQdIxcr_XG6Ri8IRC4W4s7P70FGK8B2jHmGChLo8c";
        LoopSDK.initialize(this, appId, appToken);
    }

    // called by the Loop SDK on successful initialization
    @Override
    public void onInitialized() {
        Log.d("loop_sdk", "Successfully Initialized");

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
