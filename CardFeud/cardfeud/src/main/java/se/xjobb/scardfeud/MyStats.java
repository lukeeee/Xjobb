package se.xjobb.scardfeud;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-17.
 */
public class MyStats extends Fragment {
    TextView personalstat_txt;
    private String username;
    ImageView flag;
    private String myCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stats_layout, container, false);
        personalstat_txt = (TextView)rootView.findViewById(R.id.personalstat_txt);
        personalstat_txt.getBackground().setAlpha(200);
        username = User.UserDetails.getUsername();
        flag = (ImageView)rootView.findViewById(R.id.my_flag);
        myCountry = User.UserDetails.getUserCountryCode();
        String country = myCountry.toLowerCase();
        int id = getResources().getIdentifier(country, "drawable", getActivity().getPackageName());
        Drawable drawable = getResources().getDrawable(id);
        flag.setImageDrawable(drawable);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/hobostd.otf");
        personalstat_txt.setTypeface(tf);
        if(username.endsWith("s")){
            personalstat_txt.setText(username + " " + "Stats");
        }else{
            personalstat_txt.setText(username + "'s Stats");
        }

        return rootView;
    }
}