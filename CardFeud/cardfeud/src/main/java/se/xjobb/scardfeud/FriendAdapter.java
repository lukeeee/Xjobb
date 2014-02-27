package se.xjobb.scardfeud;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lukas on 2014-02-27.
 */
public class FriendAdapter extends BaseAdapter {
    List<Friends> friends;
    Context context;
    DatabaseHandler db;


    public FriendAdapter(Context context){
        this.context = context;
        this.db = new DatabaseHandler(context);
        this.friends = (ArrayList) db.getAllContacts();
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        return friends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.friendlist, null);
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");

        Button wait = (Button)view.findViewById(R.id.waitGameBtn);


        //wait.getBackground().setAlpha(200);
        final Friends frie = db.getAllContacts().get(i);

        wait.setText("Challange your friend " + frie.getFriend());
        wait.setTypeface(tf);
        view.setTag(friends.get(i));

        wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundsVibration.start(R.raw.clock, context);
                SoundsVibration.vibrate(context);
            }
        });

        return view;
    }
}

