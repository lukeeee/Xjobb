package se.xjobb.scardfeud;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-01-30.
 */
public class WaitingGameAdapter extends BaseAdapter {
    ArrayList<Response> opponentsTurns;
    Context context;
    View.OnClickListener waitingListener;
    ImageView waitFlag;


    public WaitingGameAdapter(Context context, View.OnClickListener waitingListener){
        this.context = context;
        this.opponentsTurns = (ArrayList) GameListResult.getOpponentsTurns();
        this.waitingListener = waitingListener;
    }

    @Override
    public int getCount() {
        return opponentsTurns.size();
    }

    @Override
    public Object getItem(int i) {
        return opponentsTurns.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.waitgame_list, null);
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");

        Button wait = (Button)view.findViewById(R.id.waitGameBtn);
        waitFlag = (ImageView)view.findViewById(R.id.waitFlag);


        wait.getBackground().setAlpha(100);
        Response response = GameListResult.getOpponentsTurns().get(i);
        try {
            String country = response.opponentName.toLowerCase();
            int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            waitFlag.setBackground(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);

            waitFlag.setBackground(drawable);
        }
        wait.setText("Waiting for " + response.opponentName + "\nScore " + response.playerPoints + "-" + response.opponentPoints);
        wait.setTypeface(tf);
        view.setTag(opponentsTurns.get(i));
        view.setOnClickListener(waitingListener);

        return view;
    }
}

