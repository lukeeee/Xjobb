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

import se.xjobb.scardfeud.Posters.PostGameStart;


/**
 * Created by Lukas on 2014-02-27.
 */
public class FriendAdapter extends BaseAdapter {
    List<Friends> friends;
    Context context;
    DatabaseHandler db;
    NewGame ng;


    public FriendAdapter(Context context, NewGame ng){
        this.context = context;
        this.db = new DatabaseHandler(context);
        this.friends = (ArrayList) db.getAllContacts();
        this.ng = ng;
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

        Button chall = (Button)view.findViewById(R.id.challengeBtn);
        ImageView challengeFlag = (ImageView)view.findViewById(R.id.challengeFlag);


        //wait.getBackground().setAlpha(200);
        final Friends friend = db.getAllContacts().get(i);


        try {
            String country = friend.getCountry().toLowerCase();
            int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            challengeFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);

            challengeFlag.setImageDrawable(drawable);
        }

        chall.setText(friend.getFriend().toString());
        chall.setTypeface(tf);
        view.setTag(friends.get(i));

        chall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass helperClass = new HelperClass(context);
                if (!helperClass.isConnected()) {
                    helperClass.showNetworkErrorDialog();
                    // add retry to dialog.
                } else {
                    // challenge friend
                    PostGameStart postGameStart = new PostGameStart(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), friend.getID(), ng);
                    postGameStart.postRequest();
                }
            }
        });

        return view;
    }
}

