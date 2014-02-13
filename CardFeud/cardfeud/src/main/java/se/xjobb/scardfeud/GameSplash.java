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
    private static int NAME_FLOAT = 1500;
    TextView you, opponent,v_char,s_char;
    String username;
    Response response;
    ArrayList<Response> myTurns;
    Animation blink, bounce,move_in;


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
        bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        you.setTypeface(tf);
        s_char.setTypeface(tf);
        v_char.setTypeface(tf);
        opponent.setTypeface(tf);

        //opacity animations on textview
        /*ObjectAnimator anim1 = ObjectAnimator.ofFloat(v_char, "alpha", 0f, 1f);
        anim1.setDuration(2500);
        anim1.start();
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(s_char, "alpha", 0f, 1f);
        anim2.setDuration(2500);
        anim2.start();*/
        v_char.startAnimation(move_in);
        s_char.startAnimation(move_in);



        you.setVisibility(View.INVISIBLE);
        opponent.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //floating animations on text and imageviews after specified time
                you.setVisibility(View.VISIBLE);
                opponent.setVisibility(View.VISIBLE);
                you.startAnimation(bounce);
                opponent.startAnimation(bounce);
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