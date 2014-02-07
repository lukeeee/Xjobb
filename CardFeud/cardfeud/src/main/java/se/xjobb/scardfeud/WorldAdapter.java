package se.xjobb.scardfeud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Lukas on 2014-02-07.
 */
public class WorldAdapter extends BaseAdapter {
    //ArrayList<Response> ;
    Context context;
    View.OnClickListener worldListener;


    public WorldAdapter(Context context, View.OnClickListener worldListener){
        this.context = context;
        ///this.finishedGames = (ArrayList) GameListResult.getFinishedGames();
        //this.finListener = finListener;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.fingames_list, null);
        }






        //view.setTag(finishedGames.get(i));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}