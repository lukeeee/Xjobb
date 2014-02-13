package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Lukas on 2014-02-05.
 */
public class GameSplash extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    private static int FIRST_FLOAT = 500;
    private static int NAME_FLOAT = 1500;
    TextView you, opponent,v_char,s_char;
    String username;
    Response response;
    ArrayList<Response> myTurns;
    Animation blink, bounce1,bounce2,move_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_splash);
        you = (TextView)findViewById(R.id.you_text);
        opponent = (TextView)findViewById(R.id.opponent_text);
        v_char = (TextView)findViewById(R.id.v_char);
        s_char = (TextView)findViewById(R.id.s_char);
        username = User.UserDetails.getUsername();
        try {
            String country = User.UserDetails.getUserCountryCode().toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            you.setBackgroundDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);

            you.setBackgroundDrawable(drawable);
        }
        bounce1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        bounce2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        move_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_move);



        Intent i = getIntent();
        // i.setExtrasClassLoader(Response.class.getClass().getClassLoader());   //Exception here, but alla values are there???
        response = (Response) i.getParcelableExtra("responseObject");
        opponent.setText(response.opponentName);
        try {
            String country = response.opponentName.toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            opponent.setBackgroundDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);

            opponent.setBackgroundDrawable(drawable);
        }

        you.setText(username);
        //change font
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        you.setTypeface(tf);
        s_char.setTypeface(tf);
        v_char.setTypeface(tf);
        opponent.setTypeface(tf);
        you.setVisibility(View.INVISIBLE);
        s_char.setVisibility(View.INVISIBLE);
        v_char.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                you.setVisibility(View.VISIBLE);
                s_char.setVisibility(View.VISIBLE);
                v_char.setVisibility(View.VISIBLE);
                you.startAnimation(bounce1);
                SoundsVibration.start(R.raw.drop, GameSplash.this);
                //animation float in
                v_char.startAnimation(move_in);
                s_char.startAnimation(move_in);
                onStart();
            }}, FIRST_FLOAT);



        opponent.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //bounce animations on text and imageviews after specified time
                opponent.setVisibility(View.VISIBLE);
                opponent.startAnimation(bounce2);
                SoundsVibration.start(R.raw.drop, GameSplash.this);
                onStart();
            }}, NAME_FLOAT);
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