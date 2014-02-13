package se.xjobb.scardfeud;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-01-30.
 */
public class AvailableGameAdapter extends BaseAdapter {
    Context context;
    ImageView playFlag;
    List<Response> availableGames;

    public AvailableGameAdapter(Context context){
        this.context = context;
        availableGames = GameListResult.getMyTurns();
    }

    @Override
    public int getCount() {
        return availableGames.size();
    }

    @Override
    public Object getItem(int i) {
        return availableGames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        availableGames.clear();
        availableGames = GameListResult.getMyTurns();
        // custom sorting by last event
        Collections.sort(availableGames, new CustomComparator());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.playgame_list, null);
        }

        final Button play = (Button)view.findViewById(R.id.playGameBtn);
        playFlag = (ImageView)view.findViewById(R.id.playFlag);
        final Response response = availableGames.get(i);
        final ImageView playFlag = (ImageView)view.findViewById(R.id.playFlag);
        final Animation move;
        move = AnimationUtils.loadAnimation(context, R.anim.move_right);
        //new font on text

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
        //set text to opponent name and score
        play.setText("against: " + response.opponentName + "\nScore " + response.playerPoints + "-" + response.opponentPoints + "\n"+response.lastEventTime);
        play.setTypeface(tf);

        final int START_ACTIVITY_DELAY = 500;
        final ImageView arrow_game = (ImageView)view.findViewById(R.id.arrow_game);

        //play.getBackground().setAlpha(200);
        view.setTag(availableGames.get(i));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vibration onclick and animations
                SoundsVibration.vibrate(context);

                play.startAnimation(move);
                playFlag.startAnimation(move);
                arrow_game.startAnimation(move);

                //delay the gamestart for animations
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, GameSplash.class);
                        i.putExtra("responseObject", (Parcelable) response);
                        context.startActivity(i);
                    }}, START_ACTIVITY_DELAY);
            }
        });

        return view;
    }
}
