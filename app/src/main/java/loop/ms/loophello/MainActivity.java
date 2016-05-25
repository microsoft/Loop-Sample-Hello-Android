package loop.ms.loophello;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ms.loop.loopsdk.core.ISignalListener;
import ms.loop.loopsdk.core.LoopSDK;
import ms.loop.loopsdk.profile.IProfileListener;
import ms.loop.loopsdk.profile.Item;
import ms.loop.loopsdk.profile.ItemList;
import ms.loop.loopsdk.profile.LoopTestItem;
import ms.loop.loopsdk.profile.LoopTestList;
import ms.loop.loopsdk.profile.Profile;
import ms.loop.loopsdk.signal.Signal;
import ms.loop.loopsdk.util.LoopError;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(loop.ms.loophello.R.layout.activity_main);

        final ImageView checkboxImageView = (ImageView) findViewById(R.id.checkBoxView);;
        final TextView testCompletedView = (TextView) findViewById(R.id.testCompletedView);

        LoopSDK.registerSignalListener("myListener", "*", new ISignalListener() {
            @Override
            public void onSignal(final Signal signal) {
                // onSignal triggers when a new signal is received
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Log.d("onSignal received: ", signal.name);
                        if (checkboxImageView != null) {
                            checkboxImageView.setVisibility(View.VISIBLE);
                        }
                        if (testCompletedView != null) {
                            testCompletedView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        Button startTestButton = (Button) findViewById(R.id.sendTestSignalBtn);
        if (startTestButton != null) {
            startTestButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sends a test signal
                    Signal signal = new Signal();
                    signal.initialize("/test", "device.test");
                    signal.namespace = "/system";
                    LoopSDK.processSignal(signal);
                }
            });
        }

    }
}
