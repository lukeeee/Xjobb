package se.xjobb.scardfeud;

import android.content.Context;
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
public class AvailableGameAdapter extends BaseAdapter {
    ArrayList<Response> myTurns;
    Context context;
    View.OnClickListener myTurnsListener;
    private String username;




    public AvailableGameAdapter(Context context, View.OnClickListener myTurnsListener){
        this.context = context;
        this.myTurns = (ArrayList) GameListResult.getMyTurns();
        this.myTurnsListener = myTurnsListener;
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
        for(Response response : GameListResult.getMyTurns()){
            Log.i("MyTurn:", response.opponentName);

            play.setText("Your turn against " + response.opponentName);
        }
        play.getBackground().setAlpha(150);
        view.setTag(myTurns.get(i));
        view.setOnClickListener(myTurnsListener);

        return view;
    }
}
