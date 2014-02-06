package se.xjobb.scardfeud;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-17.
 */
public class CountryList extends Fragment {
    TextView coulist_txt;
    ListView countryList;
    ImageView country_flag;
    private String myCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.countrylist_layout, container, false);
        coulist_txt = (TextView)rootView.findViewById(R.id.coulist_txt);
        countryList = (ListView)rootView.findViewById(R.id.countrylist);
        country_flag = (ImageView)rootView.findViewById(R.id.country_flag);
        //coulist_txt.getBackground().setAlpha(200);
        //countryList.getBackground().setAlpha(200);
        myCountry = User.UserDetails.getUserCountryCode();
        String country = myCountry.toLowerCase();
        int id = getResources().getIdentifier(country, "drawable", getActivity().getPackageName());
        Drawable drawable = getResources().getDrawable(id);
        country_flag.setImageDrawable(drawable);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/hobostd.otf");
        coulist_txt.setTypeface(tf);

        return rootView;
    }
}