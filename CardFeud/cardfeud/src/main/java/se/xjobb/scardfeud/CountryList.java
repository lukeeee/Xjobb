package se.xjobb.scardfeud;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-17.
 */
public class CountryList extends Fragment {
    TextView coulist_txt;
    ListView countryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.countrylist_layout, container, false);
        coulist_txt = (TextView)rootView.findViewById(R.id.coulist_txt);
        countryList = (ListView)rootView.findViewById(R.id.countrylist);
        coulist_txt.getBackground().setAlpha(150);
        countryList.getBackground().setAlpha(150);

        return rootView;
    }
}