package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.ads.*;

public class Banner extends Activity {
    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create the adView.
        adView = new AdView(this);
        adView.setAdUnitId("MY_AD_UNIT_ID");
        adView.setAdSize(AdSize.BANNER);

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout".
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

        // Add the adView to it.
        layout.addView(adView);

        // Initiate a generic request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load the adView with the ad request.
        adView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }
}