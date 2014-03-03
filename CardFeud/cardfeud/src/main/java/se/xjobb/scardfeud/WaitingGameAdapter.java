package se.xjobb.scardfeud;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;
import se.xjobb.scardfeud.JsonGetClasses.ResponseParcelable;

/**
 * Created by Lukas on 2014-01-30.
 */
public class WaitingGameAdapter extends BaseAdapter {
    Context context;
    List<Response> waitingGames;
    ImageView waitFlag;


    public WaitingGameAdapter(Context context){
        this.context = context;
        waitingGames = GameListResult.getOpponentsTurns();
    }

    @Override
    public int getCount() {
        return waitingGames.size();
    }

    @Override
    public Object getItem(int i) {
        return waitingGames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        waitingGames.clear();
        waitingGames = GameListResult.getOpponentsTurns();
        // custom sorting by last event
        Collections.sort(waitingGames, new CustomComparator());
        Log.i("Games waiting: ", Integer.toString(GameListResult.getOpponentsTurns().size()));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.waitgame_list, null);
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/hobostd.otf");

        Button wait = (Button)view.findViewById(R.id.waitGameBtn);
        waitFlag = (ImageView)view.findViewById(R.id.waitFlag);

        //wait.getBackground().setAlpha(200);
        final Response response = waitingGames.get(i);
        try {
            String country = response.opponentCountry.toLowerCase();
            int id = 0;
            id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());

            if(id == 0){
                id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            }

            waitFlag.setImageBitmap(decodeFile(id));
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = context.getResources().getIdentifier("globe", "drawable", context.getPackageName());
            waitFlag.setImageBitmap(decodeFile(id));
        }

        wait.setText(response.opponentName + "\nScore " + response.playerPoints + "-" + response.opponentPoints + "\n" + response.lastEventTime);
        wait.setTypeface(tf);
        view.setTag(waitingGames.get(i));

        wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundsVibration.start(R.raw.clock, context);
                SoundsVibration.vibrate(context);

                // start "Game" with the current game
                Intent i = new Intent(context, Game.class);
                Bundle b = new Bundle();

                ResponseParcelable responseParcelable = new ResponseParcelable(response);
                b.putParcelable("responseObject", responseParcelable);
                i.putExtra("responseObject", b);
                context.startActivity(i);
            }
        });

        return view;
    }

    // used to decode bitmap
    private Bitmap decodeFile(int resourceId){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resourceId, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 80;  //   SET SIZE HERE, WAS 180 before

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeResource(context.getResources(), resourceId, o2);
        } catch (Resources.NotFoundException e) {}
        return null;
    }
}

