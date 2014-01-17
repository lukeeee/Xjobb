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
public class WorldList extends Fragment {
    TextView worldlist_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.worldlist_layout, container, false);
        worldlist_txt = (TextView)rootView.findViewById(R.id.worldlist_txt);
        worldlist_txt.getBackground().setAlpha(150);

        return rootView;
    }
}