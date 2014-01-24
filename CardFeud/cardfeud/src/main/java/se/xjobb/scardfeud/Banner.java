package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.ads.*;


public class Banner extends Activity {
    LinearLayout lnrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lnrMain = (LinearLayout) findViewById(R.id.lnrMain);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdView adView = new AdView(Banner.this);
                adView.setAdUnitId("0445b7141d9d4e1b");
                adView.setAdSize(AdSize.BANNER);
                AdRequest.Builder builder = new AdRequest.Builder();
                builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                adView.loadAd(builder.build());
                lnrMain.addView(adView);
            }
        });

    }
}