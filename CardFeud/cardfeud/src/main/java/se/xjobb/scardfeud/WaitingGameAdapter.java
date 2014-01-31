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
public class WaitingGameAdapter extends BaseAdapter {
    ArrayList<Response> opponentsTurns;
    Context context;
    View.OnClickListener waitingListener;


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

        Button wait = (Button)view.findViewById(R.id.waitGameBtn);
        wait.getBackground().setAlpha(150);
        for(Response response : GameListResult.getOpponentsTurns()){
            Log.i("MyTurn:", response.opponentName);

            wait.setText("Waiting for " + response.opponentName);
        }

        view.setTag(opponentsTurns.get(i));
        view.setOnClickListener(waitingListener);

        return view;
    }
}

