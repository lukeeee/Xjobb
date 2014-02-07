package se.xjobb.scardfeud;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.apache.http.client.HttpResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;
import se.xjobb.scardfeud.Posters.PostGamePlay;
import se.xjobb.scardfeud.Posters.PostGameStart;


public class Game extends Activity implements View.OnClickListener {

    private Button high;
    private Button low;
    private Button stat;
    private Button pass;
    private ProgressDialog progressDialog;
    private ImageView gamecard, youFlag, opponentFlag;
    private ImageView arrowhr, arrowhl, arrowlr, arrowll, arrowpl, arrowpr;
    private static final Random rgenerator = new Random();
    private int userId;
    private TextView waiting;
    private LinearLayout lnrMain;
    private Response gameResponse;  // This object represents a current game
    private final String TAG = "CardFeud JSON Exception: ";
    private HelperClass helperClass;
    private int rematchChoice;  // represents the choice for rematch, 1 = Yes 0 = No

    /*
    private static final Integer[] mImageIds =
            { R.drawable.c_1, R.drawable.c_8, R.drawable.c_5,R.drawable.c_4,R.drawable.c_11,R.drawable.c_13,R.drawable.c_9,R.drawable.c_12,R.drawable.c_7,
                    R.drawable.c_6,R.drawable.c_10,R.drawable.c_3,R.drawable.c_2,R.drawable.s_1, R.drawable.s_8, R.drawable.s_5,R.drawable.s_4,R.drawable.s_11,R.drawable.s_13,R.drawable.s_9,R.drawable.s_12,R.drawable.s_7,
                    R.drawable.s_6,R.drawable.s_10,R.drawable.s_3,R.drawable.s_2,R.drawable.h_1, R.drawable.h_8, R.drawable.h_5,R.drawable.h_4,R.drawable.h_11,R.drawable.h_13,R.drawable.h_9,R.drawable.h_12,R.drawable.h_7,
                    R.drawable.h_6,R.drawable.h_10,R.drawable.h_3,R.drawable.h_2,R.drawable.d_1, R.drawable.d_8, R.drawable.d_5,R.drawable.d_4,R.drawable.d_11,R.drawable.d_13,R.drawable.d_9,R.drawable.d_12,R.drawable.d_7,
                    R.drawable.d_6,R.drawable.d_10,R.drawable.d_3,R.drawable.d_2}; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        high = (Button)findViewById(R.id.higher);
        low = (Button)findViewById(R.id.lower);
        stat = (Button)findViewById(R.id.gamestat);
        pass = (Button)findViewById(R.id.pass);
        gamecard = (ImageView)findViewById(R.id.gamecards);
        youFlag = (ImageView)findViewById(R.id.youflag);
        opponentFlag = (ImageView)findViewById(R.id.opponentFlag);
        waiting = (TextView)findViewById(R.id.waiting);
        arrowhl = (ImageView)findViewById(R.id.arrowhigl);
        arrowhr = (ImageView)findViewById(R.id.arrowhigr);
        arrowll = (ImageView)findViewById(R.id.arrowlowl);
        arrowlr = (ImageView)findViewById(R.id.arrowlowr);
        arrowpl = (ImageView)findViewById(R.id.arrowpassl);
        arrowpr = (ImageView)findViewById(R.id.arrowpassr);
        //waiting.getBackground().setAlpha(200);
        //high.getBackground().setAlpha(200);
        //low.getBackground().setAlpha(200);
        //stat.getBackground().setAlpha(200);
        //pass.getBackground().setAlpha(200);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        waiting.setTypeface(tf);
        high.setTypeface(tf);
        low.setTypeface(tf);
        stat.setTypeface(tf);
        pass.setTypeface(tf);
        lnrMain = (LinearLayout)findViewById(R.id.gamelnrMain);
        stat.setOnClickListener(this);
        waiting.setVisibility(View.INVISIBLE);

        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);

        high.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // when pressing "higher"
                // sendRequestToServer "1 = higher"
                sendRequestToServer(1);
                disableGamePay();
            }
        });
        low.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // when pressing "lower"
                // send RequestToServer "2 = lower"
                sendRequestToServer(2);
                disableGamePay();
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TODO handle if pass is disabled in JSON response

                // when pressing pass
                //send RequestToServer "3 = pass"
                sendRequestToServer(3);
                disableGamePay();
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdView adView = new AdView(Game.this);
                adView.setAdUnitId("0445b7141d9d4e1b");
                adView.setAdSize(AdSize.BANNER);
                AdRequest.Builder builder = new AdRequest.Builder();
                builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                adView.loadAd(builder.build());
                lnrMain.addView(adView);
            }
        });



        helperClass = new HelperClass(this);
        Intent i = getIntent();
        //i.setExtrasClassLoader(Response.class.getClass().getClassLoader());    //Exception here, but alla values are there?
        gameResponse = (Response) i.getParcelableExtra("responseObject");
        stat.setText("You  " + gameResponse.playerPoints + "-" + gameResponse.opponentPoints +"  "+ gameResponse.opponentName);
        waiting.setText("Waiting for " + gameResponse.opponentName);
        setCorrectCard();
        enableGamePlay();

        // Log.i("Response: ", gameResponse.thisRoundPoints);
       // Log.i("Response: ", gameResponse.chatUnread);
       // Log.i("Response: ", gameResponse.myTurn);
       // Log.i("Response: ", gameResponse.opponentName);
       // Log.i("Response: ", gameResponse.cardColor);
       // Log.i("Response: ", gameResponse.cardValue);
       // Log.i("Response: ", gameResponse.finishedTime);
       // Log.i("Response: ", gameResponse.lastEvent);
       // Log.i("Response: ", gameResponse.lastEventTime);  //adad
       // ----Log.i("Response: ", gameResponse.lastRoundDetails);
       // Log.i("Response: ", gameResponse.gameId);
       // Log.i("Response: ", gameResponse.startTime);
       // ----Log.i("Response: ", gameResponse.odds);
       // Log.i("Response: ", gameResponse.thisRoundDetails);
       // Log.i("Response: ", gameResponse.lastRoundPoints); ///
       // Log.i("Response: ", gameResponse.passProhibited);
       // Log.i("Response: ", gameResponse.playerErrors);
       // Log.i("Response: ", gameResponse.playerName);
       // Log.i("Response: ", gameResponse.playerOne);
       // Log.i("Response: ", gameResponse.playerPoints);
       // Log.i("Response: ", gameResponse.playerTwo);
       // Log.i("Response: ", gameResponse.playerWins);
       // Log.i("Response: ", gameResponse.opponentWins);
       // Log.i("Response: ", gameResponse.opponentErrors);
       // Log.i("Response: ", gameResponse.opponentId);
       // Log.i("Response: ", gameResponse.opponentPoints);

        try {
            String country = gameResponse.opponentName.toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            opponentFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);

            opponentFlag.setImageDrawable(drawable);
        }
        try {
            String country = User.UserDetails.getUserCountryCode().toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            youFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);

            youFlag.setImageDrawable(drawable);
        }
    }

    private void animate(){
        high.animate().translationX(710).setDuration(1000);
        low.animate().translationX(-710).setDuration(1000);
        pass.animate().translationX(710).setDuration(1000);
        arrowhl.animate().translationX(710).setDuration(1000);
        arrowpl.animate().translationX(710).setDuration(1000);
        arrowhr.animate().translationX(710).setDuration(1000);
        arrowpr.animate().translationX(710).setDuration(1000);
        arrowlr.animate().translationX(-710).setDuration(1000);
        arrowll.animate().translationX(-710).setDuration(1000);
        ObjectAnimator anim = ObjectAnimator.ofFloat(waiting, "alpha", 0f, 1f);
        anim.setDuration(2000);
        anim.start();
    }


    // used to disable game buttons
    private void disableGamePay(){
        high.setVisibility(View.INVISIBLE);
        low.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);
        arrowhl.setVisibility(View.INVISIBLE);
        arrowpl.setVisibility(View.INVISIBLE);
        arrowhr.setVisibility(View.INVISIBLE);
        arrowpr.setVisibility(View.INVISIBLE);
        arrowlr.setVisibility(View.INVISIBLE);
        arrowll.setVisibility(View.INVISIBLE);

        // TODO handle if pass is disabled in JSON response

    }

    // used to enable game buttons
    private void enableGamePlay(){
        high.setVisibility(View.VISIBLE);
        low.setVisibility(View.VISIBLE);
        pass.setVisibility(View.VISIBLE);
        waiting.setVisibility(View.INVISIBLE);
        arrowhl.setVisibility(View.VISIBLE);
        arrowpl.setVisibility(View.VISIBLE);
        arrowhr.setVisibility(View.VISIBLE);
        arrowpr.setVisibility(View.VISIBLE);
        arrowlr.setVisibility(View.VISIBLE);
        arrowll.setVisibility(View.VISIBLE);

        // TODO handle if pass is disabled in JSON response

    }

    // used to update stats
    private void updateStats(){
        stat.setText("You  " + gameResponse.playerPoints + "-" + gameResponse.opponentPoints +"  "+ gameResponse.opponentName);
    }


    // used to select the correct card from resources, with cardName and then set that card
    private void setCardFromResources(String cardName){
        int id = getResources().getIdentifier(cardName, "drawable", getPackageName());
        Drawable drawable = getResources().getDrawable(id);
        gamecard.setImageDrawable(drawable);
    }

    // used to set the correct card
    private void setCorrectCard(){
        Log.i("Card Color: ", gameResponse.cardColor);
        Log.i("Card value: ", gameResponse.cardValue);


        if(gameResponse.cardColor.contains("1")){
            // if the card should be spades
            setCardFromResources("s_" + gameResponse.cardValue);
        } else if(gameResponse.cardColor.contains("2")){
            // if the card should be cloves
            setCardFromResources("c_" + gameResponse.cardValue);
        } else if(gameResponse.cardColor.contains("3")){
            // if the card should be hearts
            setCardFromResources("h_" + gameResponse.cardValue);
        } else if(gameResponse.cardColor.contains("4")){
            // if the card should be diamonds
            setCardFromResources("d_" + gameResponse.cardValue);

        }
    }


    public void showProgressDialog(){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, null, "Loading...", true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, null,"Loading...", true, false);
        }
    }

    // hide loading dialog
    public void hideProgressDialog(){
        // if loading dialog is visible, then hide it
        if(progressDialog != null){
            progressDialog.cancel();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gameplay, menu);
        return true;
    }
    public void onClick(View view) {
       if (view == stat){
           AlertDialog.Builder dialog = new AlertDialog.Builder(Game.this);
           dialog.setTitle("Game stats");
           dialog.setIcon(R.drawable.stat);
           dialog.setMessage("Round ONE: ");
           dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           });
           dialog.show();
    }

    }


    @Override
    protected void onResume(){
        super.onResume();

        // check if user is logged out, (if the values stored are empty)
        // if they are then finish activity
        if(User.UserDetails.getUsername() == null && User.UserDetails.getIdentifier() == null){
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_chat:
                Intent settingsIntent = new Intent(getBaseContext(), AppSettings.class);
                startActivity(settingsIntent);
                return true;*/
            case R.id.action_help:
                AlertDialog.Builder dialog = new AlertDialog.Builder(Game.this);
                dialog.setTitle("Quick Help");
                dialog.setIcon(R.drawable.ic_action_help_d);
                dialog.setMessage("* Press the scoreboard to watch stats over this game\n\n" +
                        "* Guess if the next card will be higher or lower than the showing card by pressing Higher or Lower," +
                        " if you're right you get to keep playing but if you're wrong the turn goes over to your opponent\n\n" +
                        "* Press pass to pass the turn to your opponent\n\n" +
                        "* You get one point if you are right\n\n" +
                        "* You loose one point if you are wrong");
                dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
                dialog.show();

                return true;
            case R.id.action_refresh:
                // refresh
                sendRequestToServer(0);
                return true;
        }

        return true;

    }

    private void sendRequestToServer(int choice){
        PostGamePlay postGamePlay = new PostGamePlay(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), gameResponse.gameId, choice, this);
        postGamePlay.postRequest();
    }

    // used to send the rematch data to server
    private void sendRematchPost(){

        if(!helperClass.isConnected()){
            helperClass.showNetworkErrorDialog();
            // add retry dialog
        } else {
            // send request for a rematch
            PostGameStart postGameStart = new PostGameStart(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), gameResponse.opponentId, rematchChoice, this);
            postGameStart.postRequest();
        }
    }

    // show a popup with game result, and the possibility to request a rematch
    private void showGameFinishedPopUp(final Response response){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Game Finished");
        dialog.setIcon(R.drawable.invite);

        if(!response.playerWins.contentEquals("0")){
            // if the current player wins
            dialog.setMessage("Congratulations! \n\n" + "You won against " + response.opponentName + "\n\n" +
                    response.playerPoints + " - " + response.opponentPoints);
            SoundsVibration.vibrate(this);
            SoundsVibration.start(R.raw.applause, Game.this);

        } else if (!response.opponentWins.contentEquals("0")){
            // if the opponent wins
            dialog.setMessage("Sorry! \n\n" + response.opponentName + " won against you. \n\n" +
                    response.opponentPoints + " - " + response.playerPoints);
            SoundsVibration.vibrate(this);
            SoundsVibration.start(R.raw.sad, Game.this);

        }

        dialog.setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // rematch
                rematchChoice = 1;
                sendRematchPost();
                dialog.cancel();
            }
        });
        /*
        dialog.setNeutralButton("Brag on Facebook", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {

               // TODO Implement brag on facebook
                // brag on facebook
                dialog.cancel();
            }
        });
        */
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // rematch
                rematchChoice = 0;
                sendRematchPost();
                dialog.cancel();
            }
        });
        dialog.show();

    }

    // show error feedback and offer retry on game rematch post
    public void showErrorRematchDialog(String message){
        progressDialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("An Error has occurred!");
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send request to the server again.
                sendRematchPost();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // just close the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    // finish method when posting rematch data
    public void finishRematchRequest(){
        if(rematchChoice == 1){
            // if we want to play a rematch
            Toast.makeText(this, "Rematch request sent!", 1000).show();
        }

        //Intent i = new Intent(getBaseContext(), MainActivity.class);
        //startActivity(i);

        //
        this.finish();
    }

    // show error feedback on game play
    public void showErrorDialog(String message){
        progressDialog = null;
        disableGamePay();
        helperClass.showErrorDialog(message);
    }


    // finish method when posting game play
    public void finishRequest(String result){
        Log.i("Result: ", result);

        try{
            final Gson gson = new Gson();

            // create Response object with all the JSON values
            gameResponse = gson.fromJson(result, Response.class);

        } catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }

        setCorrectCard();
        updateStats();
        if(gameResponse.lastRoundDetails != null){
            Toast.makeText(this, gameResponse.lastRoundDetails, 1000).show();
        }

        if(gameResponse.myTurn.contains("1")){
            // it is my turn
            enableGamePlay();
        } else {
            // it's not my turn
            disableGamePay();
            waiting.setVisibility(View.VISIBLE);

            /*
            // get myTurnList
            List<Response> listMyTurn = GameListResult.getMyTurns();
            // remove this object
            listMyTurn.remove(gameResponse);
            // set list
            GameListResult.setMyTurns(listMyTurn);

            if(gameResponse.finishedTime.contentEquals("0000-00-00 00:00:00")){
                // get waitingGameList
                List<Response> listWaiting = GameListResult.getOpponentsTurns();
                // add object
                listWaiting.add(gameResponse);
                // set list
                GameListResult.setOpponentsTurns(listWaiting);
            } else {
                // get finishedGamesList
                List<Response> listFinished = GameListResult.getFinishedGames();
                // add object
                listFinished.add(gameResponse);
                // set list
                GameListResult.setFinishedGames(listFinished);
            } */
        }

        // if the finished time field is set, the game is over and we show a dialog.
        if(!gameResponse.finishedTime.contentEquals("0000-00-00 00:00:00")){
            // the game is over
            disableGamePay();
            showGameFinishedPopUp(gameResponse);
        }
    }


    // This object represents the current ongoing game_layout


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
/*
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
    */
}
