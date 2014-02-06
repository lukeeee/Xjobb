package se.xjobb.scardfeud;

import android.graphics.Typeface;
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
public class WorldList extends Fragment {
    TextView worldlist_txt;
    ListView worldList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.worldlist_layout, container, false);
        worldlist_txt = (TextView)rootView.findViewById(R.id.worldlist_txt);
        worldList = (ListView)rootView.findViewById(R.id.worldlist);
        //worldList.getBackground().setAlpha(200);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/hobostd.otf");
        worldlist_txt.setTypeface(tf);
        //worldlist_txt.getBackground().setAlpha(200);

        return rootView;
    }
}