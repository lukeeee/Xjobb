package se.xjobb.scardfeud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-01-30.
 */
public class FinishedGameAdapter extends BaseAdapter{
    Context context;
    List<Response> finishedGames;

    public FinishedGameAdapter(Context context){
        this.context = context;
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
        // custom sorting by last event
        Collections.sort(finishedGames, new CustomComparator());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String result;
        Drawable win = context.getResources().getDrawable(R.drawable.game_win);
        Drawable loose = context.getResources().getDrawable(R.drawable.game_lost);
        int[] lost = new int[]
                {R.drawable.btn_lost};


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
            final int myPoints = Integer.parseInt(response.playerPoints);
            final int opPoints = Integer.parseInt(response.opponentPoints);
            String endTime = response.finishedTime.substring(0, 11);
            //Log.i("endTime", endTime);

            //diffrent finish text + btn + img in view
            if(myPoints> opPoints){
                finishBtn.setText("You won against\n" + response.opponentName + ", with " + myPoints
                        + "-" + opPoints + "\n"+endTime);
                gameResultIMG.setImageDrawable(win);

            } else if(opPoints > myPoints) {
                finishBtn.setBackgroundResource(lost[0]);
                finishBtn.setText("         You lost against\n         "+ response.opponentName + ", with "
                        + myPoints +"-" + opPoints + "\n         "+endTime);
                gameResultIMG.setImageDrawable(loose);

            } else {
                //oavgjort...
            }




            view.setTag(finishedGames.get(i));
            finishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean victory = false;

                    if(myPoints > opPoints){
                        SoundsVibration.start(R.raw.cheer, context);
                        SoundsVibration.vibrate(context);
                        victory = true;
                    } else if(opPoints > myPoints) {
                        SoundsVibration.start(R.raw.buu, context);
                        SoundsVibration.vibrate(context);
                        victory = false;
                    }

                    showDetailsDialog(response, victory);
                    //Log.i("fisk", response.lastRoundDetails + response.thisRoundDetails);
                }
            });


        }   return view;
    }

    // calculate how long the game lasted and return suitable String
    private String calculateGameTime(Date dateStart, Date dateFinish){

        String gameTime;
        long diffSeconds;
        long diffMinutes;
        long diffHours;

        // get difference in milliseconds
        long diff = dateFinish.getTime() - dateStart.getTime();

        diffSeconds = diff / 1000 % 60;
        diffMinutes = diff / (60 * 1000) % 60;
        diffHours = diff / (60 * 60 * 1000) % 24;


        if(diffHours > 1){
            // if the game lasted more than one hour
            gameTime = Long.toString(diffHours) + " hours and " + Long.toString(diffMinutes);

            if(diffMinutes > 1){
                // more than one minute
                gameTime += " minutes.";
            } else if(diffMinutes == 1){
                // one minute
                gameTime += " minute.";
            }

        } else if (diffHours == 1){
            // if the game lasted one hour
            gameTime = Long.toString(diffHours) + " hour and " + Long.toString(diffMinutes);

            if(diffMinutes > 1){
                // more than one minute
                gameTime += " minutes.";
            } else if(diffMinutes == 1){
                // one minute
                gameTime += " minute.";
            }

        } else if(diffMinutes > 1){
            // if the game lasted more than one minute
            gameTime = Long.toString(diffMinutes) + " minutes and " + Long.toString(diffSeconds);

            if(diffSeconds > 1){
                // more than one second
                gameTime += " seconds.";
            } else {
                // one second
                gameTime += " second.";
            }

        } else if(diffMinutes == 1){
            // if the game lasted one minute
            gameTime = Long.toString(diffMinutes) + " minute and " + Long.toString(diffSeconds);

            if(diffSeconds > 1){
                // more than one second
                gameTime += " seconds.";
            } else {
                // one second
                gameTime += " second.";
            }

        } else {
            // if the game lasted under one minute
            gameTime = Long.toString(diffSeconds) + " seconds.";
        }

        return gameTime;
    }


    // show a dialog with details about the finished game
    private void showDetailsDialog(Response gameResponse, boolean victory){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        // convert date String
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateStart = null;
        Date dateFinish = null;

        try {
            dateStart = format.parse(gameResponse.startTime);
            dateFinish = format.parse(gameResponse.finishedTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String gameTime = calculateGameTime(dateStart, dateFinish);
        String title;
        String statistics;
        String details = "Started: " + gameResponse.startTime + " \n\n" + "Finished: " + gameResponse.finishedTime + "\n\n"
                + "Time: " + gameTime + "\n\n";

        if(victory){
            // the game was won
            title = "Game won!";
            statistics = "You won against " + gameResponse.opponentName + ".\n\n" + "Score: " +
                    gameResponse.playerPoints + " - " + gameResponse.opponentPoints;
        } else {
            // the game was lost
            title = "Game lost.";
            statistics = gameResponse.opponentName + " won against you. \n\n" + "Score: " +
                    gameResponse.opponentPoints + " - " + gameResponse.playerPoints;
        }

        builder.setTitle(title);
        builder.setMessage(statistics + "\n\n" + details);
        builder.setInverseBackgroundForced(true);
        builder.setNeutralButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // just close the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}

