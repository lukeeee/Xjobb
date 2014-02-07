package se.xjobb.scardfeud;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
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
public class AvailableGameAdapter extends BaseAdapter {
    ArrayList<Response> myTurns;
    Context context;
    private String username;




    public AvailableGameAdapter(Context context){
        this.context = context;
        this.myTurns = (ArrayList) GameListResult.getMyTurns();
    }

    @Override
    public int getCount() {
        return myTurns.size();
    }

    @Override
    public Object getItem(int i) {
        return myTurns.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.playgame_list, null);
        }

        final Button play = (Button)view.findViewById(R.id.playGameBtn);
        final ImageView playFlag = (ImageView)view.findViewById(R.id.playFlag);
        final Response response = GameListResult.getMyTurns().get(i);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");
        try {
            String country = response.opponentName.toLowerCase();
            int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            playFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);

            playFlag.setImageDrawable(drawable);
        }
        play.setText("against: " + response.opponentName + "\nScore " + response.playerPoints + "-" + response.opponentPoints + "\n"+response.lastEventTime);
        play.setTypeface(tf);

        final int START_ACTIVITY_DELAY = 300;
        final ImageView arrow_game = (ImageView)view.findViewById(R.id.arrow_game);

        //play.getBackground().setAlpha(200);
        view.setTag(myTurns.get(i));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundsVibration.vibrate(context);
                play.animate().translationX(710).setDuration(600);
                playFlag.animate().translationX(710).setDuration(600);
                arrow_game.animate().translationX(710).setDuration(600);
                //delay the gamestart for animations
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                Intent i = new Intent(context, GameSplash.class);
                i.putExtra("responseObject", (Parcelable) response);
                Log.i("INFO", response.chatUnread);
                context.startActivity(i);
                    }}, START_ACTIVITY_DELAY);
            }
        });

        return view;
    }
}
