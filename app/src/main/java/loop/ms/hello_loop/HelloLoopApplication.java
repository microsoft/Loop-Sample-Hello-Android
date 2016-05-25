package loop.ms.loophello;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import ms.loop.loopsdk.core.ILoopSDKCallback;
import ms.loop.loopsdk.core.LoopSDK;
import ms.loop.loopsdk.util.LoopError;

public class HelloLoopApplication extends Application implements ILoopSDKCallback {
    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the Loop SDK. create an account to get your appId and appToken
        String appId = "YOUR_APP_ID";
        String appToken = "YOUR_APP_TOKEN";
        LoopSDK.initialize(this, appId, appToken);
    }

    // called by the Loop SDK on successful initalization
    @Override
    public void onInitialized() {
        Log.d("loop_sdk", "Successfully Initialized");
    }

    // called by the Loop SDK when the provider status changes
    @Override
    public void onServiceStatusChanged(String provider, String status, Bundle bundle) {
        Log.d("loop_sdk", "Provider: " + provider + " status: " + status);
    }

    // called by the Loop SDK when initialization fails
    @Override
    public void onInitializeFailed(LoopError loopError){
        Log.d("loop_sdk", "Loop SDK failed: " + loopError.toString());
    }
}
