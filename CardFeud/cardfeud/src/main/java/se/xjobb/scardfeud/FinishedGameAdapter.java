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
public class FinishedGameAdapter extends BaseAdapter{
    ArrayList<Response> finishedGames;
    Context context;
    View.OnClickListener finListener;
    Drawable win = context.getResources().getDrawable(R.drawable.game_win);
    Drawable loose = context.getResources().getDrawable(R.drawable.game_lost);

    public FinishedGameAdapter(Context context, View.OnClickListener finListener){
        this.context = context;
        this.finishedGames = (ArrayList) GameListResult.getFinishedGames();
        this.finListener = finListener;
    }

    @Override
    public int getCount() {
        return finishedGames.size();
    }

    @Override
    public Object getItem(int i) {
        return finishedGames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String result;
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");
        Button finish = (Button)view.findViewById(R.id.finGameBtn);
        ImageView gameResultIMG = (ImageView)view.findViewById(R.id.gameResult_img);
        ImageView finFlag = (ImageView)view.findViewById(R.id.finFlag);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.fingames_list, null);
        }




        Response response = GameListResult.getFinishedGames().get(i);
        try {
            String country = response.opponentName.toLowerCase();
            int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            finFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);

            finFlag.setImageDrawable(drawable);

        finish.setText("You " + "won/lost" +" against "+ response.opponentName);
        finish.setTypeface(tf);
        if (GameListResult.getFinishedGames() == response.opponentWins){
            gameResultIMG.setImageDrawable(loose);
        } else if (GameListResult.getFinishedGames() == response.playerWins) {
            gameResultIMG.setImageDrawable(win);
        }
        }


        view.setTag(finishedGames.get(i));
        view.setOnClickListener(finListener);

        return view;
    }
}

