package se.xjobb.scardfeud;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
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
    ImageView youFlag, oppFlag;
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
        youFlag = (ImageView)findViewById(R.id.yourFlag);
        oppFlag = (ImageView)findViewById(R.id.oppFlag);
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



        Intent i = getIntent();
       // i.setExtrasClassLoader(Response.class.getClass().getClassLoader());   //Exception here, but alla values are there???
        response = (Response) i.getParcelableExtra("responseObject");
        opponent.setText(response.opponentName);
        try {
            String country = response.opponentName.toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            oppFlag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);

            oppFlag.setImageDrawable(drawable);
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
        v_char.animate().x(230f).setDuration(1500);
        s_char.animate().x(360f).setDuration(1500);


        you.setVisibility(View.INVISIBLE);
        opponent.setVisibility(View.INVISIBLE);
        youFlag.setVisibility(View.INVISIBLE);
        oppFlag.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //floating animations on text and imageviews after specified time
                you.setVisibility(View.VISIBLE);
                opponent.setVisibility(View.VISIBLE);
                youFlag.setVisibility(View.VISIBLE);
                oppFlag.setVisibility(View.VISIBLE);
                PropertyValuesHolder pvhX1 = PropertyValuesHolder.ofFloat("x", 220f);
                PropertyValuesHolder pvhY1 = PropertyValuesHolder.ofFloat("y", 280f);
                ObjectAnimator.ofPropertyValuesHolder(you, pvhX1, pvhY1).setDuration(1000).start();
                PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("x", 90f);
                PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("y", 295f);
                ObjectAnimator.ofPropertyValuesHolder(youFlag, pvhX2, pvhY2).setDuration(1000).start();

                opponent.animate().x(150f).y(850f).setDuration(1000);
                oppFlag.animate().x(500f).y(880f).setDuration(1000);
                SoundsVibration.start(R.raw.tada, GameSplash.this);
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