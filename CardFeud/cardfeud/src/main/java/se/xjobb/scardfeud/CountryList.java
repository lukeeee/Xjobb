package se.xjobb.scardfeud;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-17.
 */
public class CountryList extends Fragment {
    TextView rules;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rules_layout, container, false);
        rules = (TextView)rootView.findViewById(R.id.rules);
        rules.getBackground().setAlpha(150);

        return rootView;
    }
}