package se.xjobb.scardfeud;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

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
    Drawable win = context.getResources().getDrawable(R.drawable.win);
    Drawable loose = context.getResources().getDrawable(R.drawable.loose);

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
        Button finish = (Button)view.findViewById(R.id.finGameBtn);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.waitgame_list, null);
        }
       /*TODO if (GameListResult.getFinishedGames() == lost){
          TODO  finish.setCompoundDrawablesWithIntrinsicBounds(loose, null, null, null);
         TODO } else {
          TODO  finish.setCompoundDrawablesWithIntrinsicBounds(win, null, null, null);
        }*/


        for(Response response : GameListResult.getFinishedGames()){
            Log.i("MyTurn:", response.opponentName);

            finish.setText("You " + "won/lost" +" against "+ response.opponentName);
        }

        view.setTag(finishedGames.get(i));
        view.setOnClickListener(finListener);

        return view;
    }
}

