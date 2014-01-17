package se.xjobb.scardfeud;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-17.
 */
public class MyStats extends Fragment {
    TextView personalstat_txt;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stats_layout, container, false);
        personalstat_txt = (TextView)rootView.findViewById(R.id.personalstat_txt);
        personalstat_txt.getBackground().setAlpha(150);
        username = User.UserDetails.getUsername();
        Drawable myFlag = getResources().getDrawable(R.drawable.se);
        if(username.endsWith("s")){
            personalstat_txt.setText(username + " " + "Stats");
            personalstat_txt.setCompoundDrawablesWithIntrinsicBounds(myFlag, null, null, null);
        }else{
            personalstat_txt.setText(username + "'s Stats");
            personalstat_txt.setCompoundDrawablesWithIntrinsicBounds(myFlag, null, null, null);
        }

        return rootView;
    }
}