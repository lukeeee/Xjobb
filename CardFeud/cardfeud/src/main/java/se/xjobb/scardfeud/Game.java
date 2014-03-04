package se.xjobb.scardfeud;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.Response;
import se.xjobb.scardfeud.JsonGetClasses.ResponseParcelable;
import se.xjobb.scardfeud.Posters.PostGameList;
import se.xjobb.scardfeud.Posters.PostGamePlay;
import se.xjobb.scardfeud.Posters.PostGameStart;


public class Game extends ActionBarActivity implements View.OnClickListener, ShakeEventListener.OnShakeListener {

    private ActionBar actionBar;
    private Button high;
    private Button low;
    private Button stat;
    private Button pass;
    private ProgressDialog progressDialog;
    private ImageView gamecard;
    private ImageView arrowhr, arrowhl, arrowlr, arrowll, arrowpl, arrowpr, youFlag, opponentFlag;
    private TextView waiting;
    private ViewAnimator animationView;
    private LinearLayout lnrMain;
    private AdView adView;
    private Response gameResponse;  // This object represents a current game
    int rematchChoice = 0;
    private final String TAG = "CardFeud JSON Exception: ";
    private HelperClass helperClass;
    private boolean gameOver;
    private boolean isCreated = false;
    private boolean refresh = false;
    private boolean adViewStarted = false;
    List<AlertDialog> alertDialogs;
    Animation animRotate, Bounce, move_right,move_left,fade_in;
    Boolean hasPremium = false;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;


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
        animationView = (ViewAnimator)findViewById(R.id.tjenis);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        waiting.setTypeface(tf);
        high.setTypeface(tf);
        low.setTypeface(tf);
        stat.setTypeface(tf);
        pass.setTypeface(tf);
        lnrMain = (LinearLayout)findViewById(R.id.gamelnrMain);
        stat.setOnClickListener(this);
        waiting.setOnClickListener(this);
        waiting.setVisibility(View.INVISIBLE);

        alertDialogs = new ArrayList<AlertDialog>(); // keeping track of all dialogs

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        Bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        move_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_r);
        move_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_left);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        //animRotate.setAnimationListener(this);

        isCreated = true;
        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(this);


        high.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // when pressing "higher"
                if(!gameResponse.cardValue.equals("13")){
                    // sendRequestToServer "1 = higher"
                    SoundsVibration.vibrate(Game.this);
                    sendRequestToServer(1);
                    //SoundsVibration.start(R.raw.drop, Game.this);
                    //disableGamePay();
                } else {
                    Toast.makeText(getApplicationContext(), "There is nothing higher than King", 1000).show();
                }

            }
        });
        low.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // when pressing "lower"
                if(!gameResponse.cardValue.equals("1")){
                    // send RequestToServer "2 = lower"
                    SoundsVibration.vibrate(Game.this);
                    sendRequestToServer(2);
                    // SoundsVibration.start(R.raw.drop, Game.this);
                    //disableGamePay();
                } else {
                    Toast.makeText(getApplicationContext(), "There is nothing lower than an Ace", 1000).show();
                }

            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!gameResponse.passProhibited.equals("1")){
                    // when pressing pass
                    //send RequestToServer "3 = pass"
                    SoundsVibration.vibrate(Game.this);
                    sendRequestToServer(3);
                    //disableGamePay();
                } else {
                    Toast.makeText(getApplicationContext(), "Can't pass on this card, since you opponent already passed!", 1000).show();
                }
            }
        });
        if (!hasPremium){
            Log.i("Premium Madaafakka", "");
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(adView == null){
                        adView = new AdView(Game.this);
                        adView.setAdUnitId("0445b7141d9d4e1b");
                        adView.setAdSize(AdSize.BANNER);
                        AdRequest.Builder builder = new AdRequest.Builder();
                        builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                        adView.loadAd(builder.build());
                        adViewStarted = true;
                        lnrMain.addView(adView);
                    }

                }
            });
        }



        helperClass = new HelperClass(this);

        Intent i = getIntent();
        Bundle b = i.getBundleExtra("responseObject");
        ResponseParcelable responseParcelable = (ResponseParcelable) b.getParcelable("responseObject");
        gameResponse = responseParcelable.getResponse();

        stat.setText("You  " + gameResponse.playerPoints + "-" + gameResponse.opponentPoints +"  "+ gameResponse.opponentName);
        waiting.setText("Waiting for " + gameResponse.opponentName);
        setCorrectCard();

        if(gameResponse.myTurn.equals("1")){
            // it's my turn
            enableGamePlay();
        } else {
            // it's not my turn
            disableGamePlay();
            waiting.setVisibility(View.VISIBLE);
        }

        try {
            String country = gameResponse.opponentCountry.toLowerCase();
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

    public void animate(){
        waiting.setVisibility(View.VISIBLE);
        high.startAnimation(move_right);
        pass.startAnimation(move_right);
        arrowhl.startAnimation(move_right);
        arrowpl.startAnimation(move_right);
        arrowhr.startAnimation(move_right);
        arrowpr.startAnimation(move_right);
        waiting.startAnimation(fade_in);
        low.startAnimation(move_left);
        arrowlr.startAnimation(move_left);
        arrowll.startAnimation(move_left);
    }


    // used to disable game buttons
    private void disableGamePlay(){
        high.setVisibility(View.INVISIBLE);
        low.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);
        arrowhl.setVisibility(View.INVISIBLE);
        arrowpl.setVisibility(View.INVISIBLE);
        arrowhr.setVisibility(View.INVISIBLE);
        arrowpr.setVisibility(View.INVISIBLE);
        arrowlr.setVisibility(View.INVISIBLE);
        arrowll.setVisibility(View.INVISIBLE);
        waiting.clearAnimation();
    }

    // used to enable game buttons
    public void enableGamePlay(){
        high.setVisibility(View.VISIBLE);
        high.clearAnimation();
        low.setVisibility(View.VISIBLE);
        low.clearAnimation();
        pass.setVisibility(View.VISIBLE);
        pass.clearAnimation();
        waiting.setVisibility(View.GONE);
        waiting.clearAnimation();
        arrowhl.setVisibility(View.VISIBLE);
        arrowhl.clearAnimation();
        arrowpl.setVisibility(View.VISIBLE);
        arrowpl.clearAnimation();
        arrowhr.setVisibility(View.VISIBLE);
        arrowhr.clearAnimation();
        arrowpr.setVisibility(View.VISIBLE);
        arrowpr.clearAnimation();
        arrowlr.setVisibility(View.VISIBLE);
        arrowlr.clearAnimation();
        arrowll.setVisibility(View.VISIBLE);
        arrowll.clearAnimation();
    }

    // used to update stats
    private void updateStats(){
        stat.setText("You  " + gameResponse.playerPoints + "-" + gameResponse.opponentPoints +"  "+ gameResponse.opponentName);
    }

    // used to decode bitmap
    private Bitmap decodeFile(int resourceId){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), resourceId, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 180;  //180

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeResource(getResources(), resourceId, o2);
        } catch (Resources.NotFoundException e) {}
        return null;
    }

    // used to select the correct card from resources, with cardName and then set that card
    private void setCardFromResources(String cardName){
        int id = getResources().getIdentifier(cardName, "drawable", getPackageName());

        gamecard.setImageBitmap(decodeFile(id));
        animationView.startAnimation(Bounce);

        SoundsVibration.start(R.raw.swoosh, Game.this);
    }

    // used to set the correct card
    private void setCorrectCard(){

        if(gameResponse.cardColor.contains("1")){
            // if the card should be spades
            setCardFromResources("h_" + gameResponse.cardValue);
        } else if(gameResponse.cardColor.contains("2")){
            // if the card should be cloves
            setCardFromResources("d_" + gameResponse.cardValue);
        } else if(gameResponse.cardColor.contains("3")){
            // if the card should be hearts
            setCardFromResources("c_" + gameResponse.cardValue);
        } else if(gameResponse.cardColor.contains("4")){
            // if the card should be diamonds
            setCardFromResources("s_" + gameResponse.cardValue);
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
        else if (view == waiting){
           waiting.startAnimation(animRotate);


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

        // update the game if onCreate was not called.
        // So if screen goes black i game and we "start" the screen again game will refresh
        if(!isCreated){
            refresh = true;
            sendRequestToServer(0);
        }

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        isCreated = false;
        // we don't need to show animations here.. sendGameListUpdateRequest(true);

    }

    @Override
    protected void onPause(){
        super.onPause();

        if(adViewStarted){
            adView.destroy();
        }

        // check if there was any invitation dialogs from before and dismiss them (they will be loaded again in onResume)
        for(AlertDialog alertDialog : alertDialogs){
            alertDialog.dismiss();
        }
        // the clear list
        alertDialogs.clear();
        mSensorManager.unregisterListener(mSensorListener);
    }
    @Override
    public void onShake() {
        // refresh
        refresh = true;
        sendRequestToServer(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                refresh = true;
                sendRequestToServer(0);
                return true;

            case R.id.action_chat:
                Intent chatIntent = new Intent(getApplicationContext(), Chat.class);
                startActivity(chatIntent);
                return true;
        }



        return true;

    }

    // used to show correct feedback onClick
    private void checkPointsAndShowFeedback(){
        if(!refresh){
            // if we are not refreshing
            try{
                if(!gameResponse.thisRoundPoints.equals("0")){
                    // we got a point
                    Toast.makeText(this, "Point gained!", 500).show();
                } else if(gameResponse.lastRoundPoints.equals("-1")){
                    // we lost one point
                    Toast.makeText(this, "Point lost!", 500).show();
                } else if(gameResponse.passProhibited.equals("1")){
                    // do nothing
                }

                // Handle when A is and an A is coming, should not show wrong toast then
            } catch(NullPointerException ex){

            }
        }

    }


    private void sendRequestToServer(int choice){
        if(!helperClass.isConnected()){
            helperClass.showNetworkErrorDialog();
            // add retry dialog
        } else {
            PostGamePlay postGamePlay = new PostGamePlay(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), gameResponse.gameId, choice, this);
            postGamePlay.postRequest();
        }
    }

    // used to send the rematch data to server
    private void sendRematchPost(){

        if(!helperClass.isConnected()){
            helperClass.showNetworkErrorDialog();
            // add retry dialog
        } else {
            // send request for a rematch  ( 1 = new match )
            PostGameStart postGameStart = new PostGameStart(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), gameResponse.opponentId, 1, this);
            postGameStart.postRequest();
        }
    }

    // used to send a gameList request to update current games
    private void sendGameListUpdateRequest(boolean gameIsOver, boolean gameRefresh){
        if(!helperClass.isConnected()){
            helperClass.showNetworkErrorDialog();
            // add retry dialog
        } else {
            // if the game is in progress
            if(!gameIsOver && !gameRefresh){
                PostGameList postGameList = new PostGameList(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), this, true, false, false);
                postGameList.postRequest();
            } else if(gameRefresh){
                // game refresh
                PostGameList postGameList = new PostGameList(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), this, true, false, true);
                postGameList.postRequest();
            } else {
                // the game is in finished
                PostGameList postGameList = new PostGameList(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), this, true, true, false);
                postGameList.postRequest();
            }

        }
    }

    // save the result json string to shared prefs
    public void saveResult(String result){
        try{
            getSharedPreferences(helperClass.getPrefsResult(), MODE_PRIVATE).edit().putString("gameListResult", result).commit();
        } catch (Exception ex) {
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }

    }

    // show error feedback and offer retry on game rematch post
    public void showErrorGameListDialog(String message){
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
                sendGameListUpdateRequest(gameOver, false);
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



    // show a popup with game result, and the possibility to request a rematch
    private void showGameFinishedPopUp(final Response response){
        AlertDialog alertDialog;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Game Finished");
        dialog.setIcon(R.drawable.invite);

        int playerScore = Integer.parseInt(response.playerPoints);
        int opponentScore = Integer.parseInt(response.opponentPoints);

        if(playerScore > opponentScore){
            // if the current player wins
            dialog.setMessage("Congratulations! \n\n" + "You won against " + response.opponentName + "\n\n" +
                    response.playerPoints + " - " + response.opponentPoints);
            SoundsVibration.vibrate(this);
            SoundsVibration.start(R.raw.applause, Game.this);

        } else if (opponentScore > playerScore){
            // if the opponent wins
            dialog.setMessage("Sorry! \n\n" + response.opponentName + " won against you. \n\n" +
                    response.opponentPoints + " - " + response.playerPoints);
            SoundsVibration.vibrate(this);
            SoundsVibration.start(R.raw.sad, Game.this);

        } else {
            // if it's a tie
            dialog.setMessage("Sorry! \n\n" + "It was a tie! \n\n" +
                    response.opponentPoints + " - " + response.playerPoints);
            SoundsVibration.vibrate(this);
            SoundsVibration.start(R.raw.sad, Game.this);

        }

        dialog.setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // rematch, gameList will be refreshed in this request as well
                sendRematchPost();
                // use .dismiss() here so the .cancel code won't be called
                dialog.dismiss();
            }
        });
        /*
        dialog.setNeutralButton("Brag on Facebook", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {

               // TODO Implement brag on facebook
                // brag on facebook
                dialog.dismiss();
            }
        });
        */

        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // if the user clicks the cancel button
                // show progressDialog and update gameList
                showProgressDialog();
                sendGameListUpdateRequest(true, false);
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // if the user clicks "Back" button on phone
                // show progressDialog and update gameList
                showProgressDialog();
                sendGameListUpdateRequest(true, false);
                dialog.cancel();
            }
        });
        alertDialog = dialog.create();
        alertDialog.show();
        alertDialogs.add(alertDialog); // add this dialog to a list with dialogs

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
                rematchChoice = 1;
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

    // get the correct hasRated value from shared prefs
    private boolean getSavedHasRated(){
        boolean hasRated = false;

        try{
            hasRated = getSharedPreferences(helperClass.getPrefsRate(), MODE_PRIVATE).getBoolean("hasRated", false);
        } catch (Exception ex){
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }

        return hasRated;
    }

    // save if the user has rated the app to shared prefs
    private void saveHasRated(){
        try{
            getSharedPreferences(helperClass.getPrefsRate(), MODE_PRIVATE).edit().putBoolean("hasRated", User.UserDetails.getHasRated()).commit();
        } catch (Exception ex) {
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }
    }

    private void ratingDialog(final Game game){
        // get if the user has rated or not from shared prefs
        User.UserDetails.setHasRated(getSavedHasRated());

        if (!User.UserDetails.getHasRated()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Rate Us");
            builder.setMessage("Please rate us on Google play");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // open google play
                    Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Couldn't launch the market", Toast.LENGTH_LONG).show();
                    }
                    // set and save if rated
                    User.UserDetails.setHasRated(true);
                    saveHasRated();
                    dialog.dismiss();
                    game.finish();
                }
            });
            builder.setNeutralButton("Ask me later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // open next finished game
                    // set and save if rated
                    User.UserDetails.setHasRated(false);
                    saveHasRated();
                    dialog.dismiss();
                    game.finish();
                }
            });
            builder.setNegativeButton("Don't ask me again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // just close the dialog and don't remind later
                    // get and save if rated
                    User.UserDetails.setHasRated(true);
                    saveHasRated();
                    dialog.dismiss();
                    game.finish();
                }
            });

            AlertDialog alert = builder.create();
            alertDialogs.add(alert);
            alert.show();
        } else {
            this.finish();
        }

    }


    // update gameList's when the game is finished
    public void gameOverListUpdate(){
        // true == gameOver
        sendGameListUpdateRequest(gameOver, false);
    }

    // finish method when posting rematch data
    public void finishRematchRequest(){
        if(rematchChoice == 1){
            // if we want to play a rematch
            Toast.makeText(this, "Rematch request sent!", 1000).show();
        }

        ratingDialog(this);
    }

    // show error feedback on game play
    public void showErrorDialog(String message, String gamePlayChoice){
        progressDialog = null;
        disableGamePlay();

        final int choice = Integer.parseInt(gamePlayChoice);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("An Error has occurred!");
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send request to the server again. (with the same choice as before)
                sendRequestToServer(choice);
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


    // check opponentName, if it's greater than 12 chars then we modify it
    private Response checkAndModifyOpponentName(Response response){
        if(response.opponentName.length() > 12){
            // remove everything after 9 chars and add "..."
            response.opponentName = response.opponentName.substring(0, 9) + "...";
        }
        return response;
    }

    // finish method when posting game play
    public void finishRequest(String result){
        Log.i("Result: ", result);

        try{
            final Gson gson = new Gson();

            // create Response object with all the JSON values
            gameResponse = gson.fromJson(result, Response.class);

            // check and modify name
            gameResponse = checkAndModifyOpponentName(gameResponse);

        } catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }

        setCorrectCard();
        updateStats();

        checkPointsAndShowFeedback();

        if(gameResponse.myTurn.contains("1")){
            // it is my turn
            // if we are not refreshing
            if(!refresh){
                // hide the progress dialog here
                hideProgressDialog();
                enableGamePlay();
            } else {
                // we used the refresh function
                // it's not my turn, don't hide progress here (since we will hide it later when the other request is done)
                // send request to update gameList, game is in progress (gameOver == false)
                gameOver = false;
                sendGameListUpdateRequest(gameOver, true);
                refresh = false;
            }


        } else {
            // it's not my turn, don't hide progress here (since we will hide it later when the other request is done)
            // send request to update gameList, game is in progress (gameOver == false)
            gameOver = false;
            sendGameListUpdateRequest(gameOver, false);

            // animate(); is now called from the poster class
        }

        // if the finished time field is set, the game is over and we show a dialog.
        if(!gameResponse.finishedTime.contentEquals("0000-00-00 00:00:00")){
            // the game is over
            disableGamePlay();
            gameOver = true;
            showGameFinishedPopUp(gameResponse);
        }
    }

    @Override
    protected void onDestroy() {
        if(adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }
}

