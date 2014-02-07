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
import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-01-30.
 */
public class FinishedGameAdapter extends BaseAdapter{
    Context context;
    View.OnClickListener finListener;
    List<Response> finishedGames;

    public FinishedGameAdapter(Context context, View.OnClickListener finListener){
        this.context = context;
        this.finListener = finListener;
        finishedGames = GameListResult.getFinishedGames();
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        finishedGames.clear();
        finishedGames = GameListResult.getFinishedGames();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String result;
        Drawable win = context.getResources().getDrawable(R.drawable.game_win);
        Drawable loose = context.getResources().getDrawable(R.drawable.game_lost);


        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.fingames_list, null);
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");
        Button finishBtn = (Button)view.findViewById(R.id.finGameBtn);
        ImageView gameResultIMG = (ImageView)view.findViewById(R.id.gameResult_img);
        ImageView finFlag = (ImageView)view.findViewById(R.id.finFlag);


        final Response response = finishedGames.get(i);
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

            finishBtn.setTypeface(tf);
            int myPoints = Integer.parseInt(response.playerPoints);
            int opPoints = Integer.parseInt(response.opponentPoints);


            if(myPoints> opPoints){
                finishBtn.setText("You won against\n" + response.opponentName + " " + myPoints + "-" + opPoints);
                gameResultIMG.setImageDrawable(win);

            } else if(opPoints > myPoints) {
                finishBtn.setText("You lost against\n"+ response.opponentName + " " + myPoints +"-" + opPoints);
                gameResultIMG.setImageDrawable(loose);

            } else {
                //oavgjort...
            }
        }



        view.setTag(finishedGames.get(i));
        view.setOnClickListener(finListener);

        return view;
    }
}

