package se.xjobb.scardfeud;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;


/**
 * Created by Lukas on 2014-01-08.
 */
public class Start extends Fragment implements View.OnClickListener {
    TextView user;
    TextView gamestext;
    TextView waitingtext;
    TextView fin_Gamestext;
    private Button newGame;
    private String username;
    private String myCountry;
    LinearLayout games, waiting, finGames;
    ImageView flag,plus;
    FinishedGameAdapter finishedGameAdapter;
    WaitingGameAdapter waitingGameAdapter;
    AvailableGameAdapter availableGameAdapter;
    boolean finishedGameAdapterCreated = false;
    boolean waitingGameAdapterCreated = false;
    boolean availableGameAdapterCreated = false;
    Animation jiggle;
    private static int START_ACTIVITY_DELAY = 400;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_layout, container, false);
        user = (TextView)rootView.findViewById(R.id.user);
        gamestext = (TextView)rootView.findViewById(R.id.games_text);
        fin_Gamestext = (TextView)rootView.findViewById(R.id.fin_game_text);
        newGame = (Button)rootView.findViewById(R.id.Btn_newGame);
        waitingtext = (TextView)rootView.findViewById(R.id.waiting_text);
        games = (LinearLayout)rootView.findViewById(R.id.games);
        waiting = (LinearLayout)rootView.findViewById(R.id.waiting);
        finGames = (LinearLayout)rootView.findViewById(R.id.finGames);
        flag = (ImageView)rootView.findViewById(R.id.myFlag);
        newGame.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        myCountry = User.UserDetails.getUserCountryCode();
        gamestext.setVisibility(View.INVISIBLE);
        waitingtext.setVisibility(View.INVISIBLE);
        fin_Gamestext.setVisibility(View.INVISIBLE);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/hobostd.otf");
        newGame.setTypeface(tf);
        user.setTypeface(tf);
        gamestext.setTypeface(tf);
        waitingtext.setTypeface(tf);
        fin_Gamestext.setTypeface(tf);

        user.setText(username);
        jiggle = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.jiggle);

        // set the correct flag, if not found set default
        try {
            String country = myCountry.toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getActivity().getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            flag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getActivity().getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            flag.setImageDrawable(drawable);
        } catch (NullPointerException ex){
            // fixes a bug on startup
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getActivity().getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            flag.setImageDrawable(drawable);
        }

        createAvailableGameAdapter();
        createWaitingGameAdapter();
        createFinishedGameAdapter();

        MainActivity.setStartTag(getTag());

        return rootView;
    }


    @Override
    public void onClick(View view) {

        if (view == newGame){
            newGame.startAnimation(jiggle);
            SoundsVibration.vibrate(getActivity().getApplicationContext());
            //plus.startAnimation(jiggle);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            Intent ng = new Intent(getActivity().getApplicationContext(), NewGame.class);
            startActivity(ng);
                }}, START_ACTIVITY_DELAY);
        }

    }

    private void createAvailableGameAdapter(){
        availableGameAdapterCreated = true;
        availableGameAdapter = new AvailableGameAdapter(getActivity());
        final int adapterCount = availableGameAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = availableGameAdapter.getView(i, null, null);
            games.addView(item);
        }

        if(adapterCount >0){
            gamestext.setVisibility(View.VISIBLE);
        }
    }

    private void createWaitingGameAdapter(){
        waitingGameAdapterCreated = true;
        waitingGameAdapter = new WaitingGameAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.icon, new Start(), view.getTag() + "");
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        final int adapterCount = waitingGameAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = waitingGameAdapter.getView(i, null, null);
            waiting.addView(item);
        }

        if(adapterCount > 0){
            waitingtext.setVisibility(View.VISIBLE);
        }
    }

    private void createFinishedGameAdapter(){
        finishedGameAdapterCreated = true;
        finishedGameAdapter = new FinishedGameAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.icon, new Start(), view.getTag() + "");
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        final int adapterCount = finishedGameAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = finishedGameAdapter.getView(i, null, null);
            finGames.addView(item);
        }

        if(adapterCount > 0){
            fin_Gamestext.setVisibility(View.VISIBLE);
        }
    }



    // refresh the views for available games
    private void refreshAvailableGames(){
        // make sure the adapter is not null
        if(availableGameAdapter != null){
            gamestext.setVisibility(View.INVISIBLE);

            // refresh adapter
            availableGameAdapter.notifyDataSetChanged();

            // remove all views
            games.removeAllViews();

            // add all views again, to make it accurate
            for (int i = 0; i < availableGameAdapter.getCount(); i++) {
                View item = availableGameAdapter.getView(i, null, null);
                games.addView(item);
            }

            // show/hide text depending on number of items
            if(availableGameAdapter.getCount() > 0){
                gamestext.setVisibility(View.VISIBLE);
            }
        }
    }

    // refresh views for finishedGames
    private void refreshFinishedGames(){
        // make sure adapter is not null
        if(finishedGameAdapter != null){
            fin_Gamestext.setVisibility(View.INVISIBLE);

            // refresh adapter
            finishedGameAdapter.notifyDataSetChanged();

            // remove all views
            finGames.removeAllViews();

            // add all views again, to make it accurate
            for (int i = 0; i < finishedGameAdapter.getCount(); i++) {
                View item = finishedGameAdapter.getView(i, null, null);
                finGames.addView(item);
            }

            // show/hide text depending on number of items
            if(finishedGameAdapter.getCount() > 0){
                fin_Gamestext.setVisibility(View.VISIBLE);
            }
        }
    }


    // refresh views for waiting games
    private void refreshWaitingGames(){
        // make sure adapter is not null
        if(waitingGameAdapter != null){
            waitingtext.setVisibility(View.INVISIBLE);

            // refresh adapter
            waitingGameAdapter.notifyDataSetChanged();


            // remove all views
            waiting.removeAllViews();

            // add all views again to make it accurate
            for (int i = 0; i < waitingGameAdapter.getCount(); i++) {
                View item = waitingGameAdapter.getView(i, null, null);
                waiting.addView(item);
            }

            // show/hide text depending on number of items
            if(waitingGameAdapter.getCount() > 0){
                waitingtext.setVisibility(View.VISIBLE);
            }
        }
    }

    // refresh the arraylists data and adapters
    public void refresh(){

        // check if we need to create any adapters (that are not already created
        if(!GameListResult.getMyTurns().isEmpty() && !availableGameAdapterCreated){
            createAvailableGameAdapter();
        }

        if(!GameListResult.getOpponentsTurns().isEmpty() && !waitingGameAdapterCreated){
            createWaitingGameAdapter();
        }

        if(!GameListResult.getFinishedGames().isEmpty() && !finishedGameAdapterCreated){
            createFinishedGameAdapter();
        }

        // refresh all views and adapters
        refreshAvailableGames();
        refreshFinishedGames();
        refreshWaitingGames();
    }
}
