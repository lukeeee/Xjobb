package se.xjobb.scardfeud;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
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
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.playgame_list, null);
        }

        Button play = (Button)view.findViewById(R.id.playGameBtn);
        playFlag = (ImageView)view.findViewById(R.id.playFlag);
        final Response response = availableGames.get(i);
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



        //play.getBackground().setAlpha(200);
        view.setTag(availableGames.get(i));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, GameSplash.class);
                i.putExtra("responseObject", (Parcelable) response);
                context.startActivity(i);
            }
        });

        return view;
    }
}
