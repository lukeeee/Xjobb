package se.xjobb.scardfeud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-02-14.
 */
public class ChatAdapter extends BaseAdapter {
    //ArrayList<Response> chat;
    Context context;
    View.OnClickListener countryListener;


    public ChatAdapter(Context context, View.OnClickListener chatListener){
        this.context = context;
        //this.chat = (ArrayList) GameListResult.getChat();
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
        TextView YourText = (TextView)view.findViewById(R.id.your_text);
        TextView OppText = (TextView)view.findViewById(R.id.opp_text);



        //view.setTag(finishedGames.get(i));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
