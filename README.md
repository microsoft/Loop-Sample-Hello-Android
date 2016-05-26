# Loop Android Sample - Setting up test users

These instructions will get you a copy of a Loop sample app for pulling user profiles up and running for development and testing purposes.

1. If you haven’t already, signup for a loop account (it takes seconds - https://developer.dev.loop.ms/) 
2. Create an app to get your loop app key and secret token
3. Clone this sample app (`git clone https://github.com/Microsoft/Loop-Sample-Hello-Android.git`), and open it in Android Studio. Replace the `your_app_id` and `your_app_token` constant in `LoopHelloApplication.java` with your loop app key and secret token. 
4. Create a test user from the User Table on the left side navigation
5. Replace the `YOUR_TEST_USERID` with the userid of a sample user
6. Run the app. 

After this is done, you can send create test profile signals in your dashboard and see them in your app. This mimics trips and home/work profiles that you will receive in your app from Loop. 
