# Loop Android Sample - Connecting to the Loop platform

These instructions will get you a copy of a Loop sample app that will send a test signal to the Loop platform.

  0. If you haven’t already, signup for a Loop account and create an app on the [Loop Developer Site](https://www.loop.ms)
  0. Get the sample app
    0. Clone this sample app `git clone https://github.com/Microsoft/Loop-Sample-Hello-Android.git`
    0. Open it in Android Studio
    0. Add your appId and appToken in `LoopHelloApplication.java OnCreate`

    ```
        String appId = "YOUR_APP_ID";
        String appToken = "YOUR_APP_TOKEN";
    ```
  0. Create test users in your user dashboard (user link in the left navigation)
  0. Fill in the userId and deviceId in `LoopTestUserApplication.java OnCreate` with a test user's userId and deviceId obtained from the [Loop Developer Site](https://www.loop.ms)

    ```
        String userId = "YOUR_USER_ID";
        String deviceId = "YOUR_DEVICE_ID";
    ```
  0. Build and run the app

After this is done, you can create test signals to update the test profile. This is the same mechanism using test signals that the Loop uses when it creates home & work profiles.
