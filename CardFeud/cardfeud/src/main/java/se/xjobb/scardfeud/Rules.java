package se.xjobb.scardfeud;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by Lukas on 2014-01-08.
 */
public class Rules extends Fragment {
TextView rules;
LinearLayout rulesLnrMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rules_layout, container, false);
        rules = (TextView)rootView.findViewById(R.id.rules);
        rules.getBackground().setAlpha(150);

        rulesLnrMain = (LinearLayout)rootView.findViewById(R.id.rulesLnrMain);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdView adView = new AdView(getActivity());
                adView.setAdUnitId("0445b7141d9d4e1b");
                adView.setAdSize(AdSize.BANNER);
                AdRequest.Builder builder = new AdRequest.Builder();
                builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                adView.loadAd(builder.build());
                rulesLnrMain.addView(adView);
            }
        }); return rootView;
    }
}
