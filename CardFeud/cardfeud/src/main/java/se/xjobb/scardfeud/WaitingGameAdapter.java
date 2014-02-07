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

import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-01-30.
 */
public class WaitingGameAdapter extends BaseAdapter {
    Context context;
    List<Response> waitingGames;
    View.OnClickListener waitingListener;
    ImageView waitFlag;


    public WaitingGameAdapter(Context context, View.OnClickListener waitingListener){
        this.context = context;
        this.waitingListener = waitingListener;
        waitingGames = GameListResult.getOpponentsTurns();
    }

    @Override
    public int getCount() {
        return waitingGames.size();
    }

    @Override
    public Object getItem(int i) {
        return waitingGames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        waitingGames.clear();
        waitingGames = GameListResult.getOpponentsTurns();
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


        //wait.getBackground().setAlpha(200);
        Response response = waitingGames.get(i);
        try {
            String country = response.opponentName.toLowerCase();
            int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            waitFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);

            waitFlag.setImageDrawable(drawable);
        }
        wait.setText("for: " + response.opponentName + "\nScore " + response.playerPoints + "-" + response.opponentPoints + "\n"+response.lastEventTime);
        wait.setTypeface(tf);
        view.setTag(waitingGames.get(i));
        view.setOnClickListener(waitingListener);

        return view;
    }


}

