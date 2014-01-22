package se.xjobb.scardfeud;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


public class Game extends Activity implements View.OnClickListener {

    private Button start;
    private Button high;
    private Button low;
    private Button stat;
    private Button pass;
    private ProgressDialog progressDialog;
    private ImageView gamecards;
    private static final Random rgenerator = new Random();
    private int userId;
    private int gameId;
    private int choice;
    private TextView waiting;

    private static final Integer[] mImageIds =
            { R.drawable.c_a, R.drawable.c_eight, R.drawable.c_five,R.drawable.c_four,R.drawable.c_j,R.drawable.c_k,R.drawable.c_nine,R.drawable.c_q,R.drawable.c_seven,
                    R.drawable.c_six,R.drawable.c_ten,R.drawable.c_three,R.drawable.c_two,R.drawable.s_a, R.drawable.s_eight, R.drawable.s_five,R.drawable.s_four,R.drawable.s_j,R.drawable.s_k,R.drawable.s_nine,R.drawable.s_q,R.drawable.s_seven,
                    R.drawable.s_six,R.drawable.s_ten,R.drawable.s_three,R.drawable.s_two,R.drawable.h_a, R.drawable.h_eight, R.drawable.h_five,R.drawable.h_four,R.drawable.h_j,R.drawable.h_k,R.drawable.h_nine,R.drawable.h_q,R.drawable.h_seven,
                    R.drawable.h_six,R.drawable.h_ten,R.drawable.h_three,R.drawable.h_two,R.drawable.d_a, R.drawable.d_eight, R.drawable.d_five,R.drawable.d_four,R.drawable.d_j,R.drawable.d_k,R.drawable.d_nine,R.drawable.d_q,R.drawable.d_seven,
                    R.drawable.d_six,R.drawable.d_ten,R.drawable.d_three,R.drawable.d_two};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        high = (Button)findViewById(R.id.higher);
        low = (Button)findViewById(R.id.lower);
        stat = (Button)findViewById(R.id.gamestat);
        pass = (Button)findViewById(R.id.pass);
        gamecards = (ImageView)findViewById(R.id.gamecards);
        waiting = (TextView)findViewById(R.id.waiting);
        waiting.getBackground().setAlpha(150);
        high.getBackground().setAlpha(150);
        low.getBackground().setAlpha(150);
        stat.getBackground().setAlpha(150);
        pass.getBackground().setAlpha(150);
        stat.setOnClickListener(this);
        waiting.setVisibility(View.INVISIBLE);
        waiting.setText("Waiting for " + "null");
        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);
        Integer q = mImageIds[rgenerator.nextInt(mImageIds.length)];
        changeImageResource();
        high.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeImageResource();
                high.setVisibility(View.INVISIBLE);
                low.setVisibility(View.INVISIBLE);
                pass.setVisibility(View.INVISIBLE);
                waiting.setVisibility(View.VISIBLE);
            }
        });
        low.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeImageResource();
                high.setVisibility(View.INVISIBLE);
                low.setVisibility(View.INVISIBLE);
                pass.setVisibility(View.INVISIBLE);
                waiting.setVisibility(View.VISIBLE);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeImageResource();
                high.setVisibility(View.INVISIBLE);
                low.setVisibility(View.INVISIBLE);
                pass.setVisibility(View.INVISIBLE);
                waiting.setVisibility(View.VISIBLE);
            }
        });

    }
    public void changeImageResource()
    {
        int i = rgenerator.nextInt(51);
        gamecards.setImageResource(mImageIds[i]);
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
        /*if (view == start){
            start.setVisibility(Button.INVISIBLE);
        }*/
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
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                showProgressDialog();
                return true;
        }

        return true;

    }


    // This object represents the current ongoing game_layout


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
