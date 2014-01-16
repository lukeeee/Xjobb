package se.xjobb.scardfeud;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Game extends Activity implements View.OnClickListener {

    private Button start;
    private Button high;
    private Button low;
    private Button stat;
    private Button pass;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        start = (Button)findViewById(R.id.start_game);
        high = (Button)findViewById(R.id.higher);
        low = (Button)findViewById(R.id.lower);
        stat = (Button)findViewById(R.id.gamestat);
        pass = (Button)findViewById(R.id.pass);
        high.getBackground().setAlpha(150);
        low.getBackground().setAlpha(150);
        stat.getBackground().setAlpha(150);
        pass.getBackground().setAlpha(150);
        start.setOnClickListener(this);
        stat.setOnClickListener(this);
        final ActionBar actionBar = getActionBar();

    }
    public void showProgressDialog(){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, "Refreshing", "Please Wait ", true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, "Refreshing","Please Wait...", true, false);
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
        if (view == start){
            start.setVisibility(Button.INVISIBLE);
        }
        else if (view == stat){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Game.this);
            dialog.setTitle("Game stats");
            dialog.setIcon(R.drawable.ic_action_help_d);
            dialog.setMessage("Round ONE: 6 of Hearts\n" +
                    "  Lukas pressed Higher: 1 Point\n" +
                    "  Emil   pressed Lower: -1 Point");
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
                        "* Guess if the next card will be higher or lower than the showing card by pressing Higher or Lower\n\n" +
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
                Intent refreshIntent = new Intent(getBaseContext(), Game.class);
                finish();
                showProgressDialog();
                startActivity(refreshIntent);
                return true;
        }

        return true;

    }


    // This object represents the current ongoing game_layout

    private int userId;
    private int gameId;
    private int choice;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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
}
