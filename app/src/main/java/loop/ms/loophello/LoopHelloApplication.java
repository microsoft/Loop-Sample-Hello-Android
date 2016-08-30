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

        // initialize the Loop SDK. create an account to get your appId and appToken
        String appId = "test-project-dev-dce9076d";
        String appToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb3V0ZXMiOlt7Im1ldGhvZCI6InBvc3QiLCJwYXRoIjoiL3YyLjAvYXBwL3Rlc3QtcHJvamVjdC1kZXYtZGNlOTA3NmQvdXNlciJ9LHsibWV0aG9kIjoiZGVsZXRlIiwicGF0aCI6Ii92Mi4wL2FwcC90ZXN0LXByb2plY3QtZGV2LWRjZTkwNzZkL3VzZXIifSx7Im1ldGhvZCI6InBvc3QiLCJwYXRoIjoiL3YyLjAvYXBwL3Rlc3QtcHJvamVjdC1kZXYtZGNlOTA3NmQvbG9naW4ifSx7Im1ldGhvZCI6ImdldCIsInBhdGgiOiIvdjIuMC9hcHAvdGVzdC1wcm9qZWN0LWRldi1kY2U5MDc2ZC91c2VyIn0seyJtZXRob2QiOiJnZXQiLCJwYXRoIjoiL3YyLjAvYXBwL3Rlc3QtcHJvamVjdC1kZXYtZGNlOTA3NmQvdXNlci9bXi8uXSsifSx7Im1ldGhvZCI6InBvc3QiLCJwYXRoIjoiL3YyLjAvYXBwL3Rlc3QtcHJvamVjdC1kZXYtZGNlOTA3NmQvbWFuYWdlLy4qIn0seyJtZXRob2QiOiJnZXQiLCJwYXRoIjoiL3YyLjAvYXBwL3Rlc3QtcHJvamVjdC1kZXYtZGNlOTA3NmQvbWFuYWdlLy4qIn0seyJtZXRob2QiOiJkZWxldGUiLCJwYXRoIjoiL3YyLjAvYXBwL3Rlc3QtcHJvamVjdC1kZXYtZGNlOTA3NmQvbWFuYWdlLy4qIn1dLCJpc3MiOiJNaWNyb3NvZnQgTE9PUCBBdXRoIFYyIiwic3ViIjoidGVzdC1wcm9qZWN0LWRldi1kY2U5MDc2ZCJ9.gif-sN47PFaiNewOPsHCOr87bOsxNuXAU4KyDNqKgBs";

        String userId = "2d9aded5-60cd-4ac7-8648-b2da4babcfe1";
        String deviceId = "1752c39c-8db1-42aa-8490-5a65d93c902e";

        LoopSDK.initialize(this, appId, appToken, userId, deviceId);
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
    public void onDebug(String output) {

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
