package loop.ms.loophello;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(loop.ms.loophello.R.layout.activity_main);

        final Button sendTestButton = (Button) findViewById(R.id.sendTestSignalBtn);
        final ImageView checkboxImageView = (ImageView) findViewById(R.id.checkBoxView);;
        final ImageView errorImageView = (ImageView) findViewById(R.id.errorView);;
        final TextView testCompletedView = (TextView) findViewById(R.id.testCompletedView);

        if (sendTestButton == null
                || checkboxImageView == null
                || errorImageView == null
                || testCompletedView == null) {
            Log.d(TAG, "Error loading layout resources");
        }

        Button startTestButton = (Button) findViewById(R.id.sendTestSignalBtn);
        if (startTestButton != null) {
            startTestButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkboxImageView.setVisibility(View.INVISIBLE);
                    errorImageView.setVisibility(View.INVISIBLE);
                    testCompletedView.setVisibility(View.INVISIBLE);

                    if (!LoopSDK.isInitialized()) {
                        sendTestButton.setEnabled(false);
                        errorImageView.setVisibility(View.VISIBLE);

                        testCompletedView.setText("Error initializing Loop SDK");
                        testCompletedView.setVisibility(View.VISIBLE);

                        return;
                    }

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
                                    testCompletedView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}
