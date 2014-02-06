package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-02-05.
 */
public class GameSplash extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    TextView you, opponent,v_char,s_char;
    String username;
    Response response;
    ArrayList<Response> myTurns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_splash);
        you = (TextView)findViewById(R.id.you_text);
        opponent = (TextView)findViewById(R.id.opponent_text);
        v_char = (TextView)findViewById(R.id.v_char);
        s_char = (TextView)findViewById(R.id.s_char);
        username = User.UserDetails.getUsername();


        Intent i = getIntent();
        i.setExtrasClassLoader(Response.class.getClass().getClassLoader());   //Exception here, but alla values are there???
        response = (Response) i.getParcelableExtra("responseObject");
        opponent.setText(response.opponentName);

        you.setText(username);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        you.setTypeface(tf);
        s_char.setTypeface(tf);
        v_char.setTypeface(tf);
        opponent.setTypeface(tf);

        new Handler().postDelayed(new Runnable() {
            //Show splash screen for duration of timer

            @Override
            public void run() {
                //Start main activity when timer is over
                Intent ix = new Intent(GameSplash.this, Game.class);
                ix.putExtra("responseObject", (Parcelable) response);
                startActivity(ix);

                // Close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}