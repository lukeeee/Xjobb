package se.xjobb.scardfeud;

import android.content.Context;
import android.content.Intent;
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
public class AvailableGameAdapter extends BaseAdapter {
    ArrayList<Response> myTurns;
    Context context;
    private String username;
    ImageView playFlag;



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

        Button play = (Button)view.findViewById(R.id.playGameBtn);
        playFlag = (ImageView)view.findViewById(R.id.playFlag);
        final Response response = GameListResult.getMyTurns().get(i);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");
        try {
            String country = response.opponentName.toLowerCase();
            int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            playFlag.setBackground(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);

            playFlag.setBackground(drawable);
        }
        play.setText("against: " + response.opponentName + "\nScore " + response.playerPoints + "-" + response.opponentPoints);
        play.setTypeface(tf);


        //play.getBackground().setAlpha(200);
        view.setTag(myTurns.get(i));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, GameSplash.class);
                i.putExtra("opponentName", response.opponentName);
                i.putExtra("gameId", response.gameId);
                i.putExtra("playerName", response.playerName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        return view;
    }
}
