package se.xjobb.scardfeud;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private Button newGame,showFinGame,hideFinGame;
    private String username;
    private String myCountry;
    LinearLayout games, waiting, finGames;
    ImageView flag,arrw1,arrw2;
    FinishedGameAdapter finishedGameAdapter;
    WaitingGameAdapter waitingGameAdapter;
    AvailableGameAdapter availableGameAdapter;
    boolean finishedGameAdapterCreated = false;
    boolean waitingGameAdapterCreated = false;
    boolean availableGameAdapterCreated = false;
    Animation jiggle;
    private static int START_ACTIVITY_DELAY = 350;
    HelperClass helperClass = new HelperClass(getActivity());


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
        showFinGame = (Button)rootView.findViewById(R.id.showFinGame);
        hideFinGame = (Button)rootView.findViewById(R.id.hideFinGame);
        arrw1 = (ImageView)rootView.findViewById(R.id.showFinGame2);
        arrw2 = (ImageView)rootView.findViewById(R.id.showFinGame1);
        newGame.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        myCountry = User.UserDetails.getUserCountryCode();
        gamestext.setVisibility(View.INVISIBLE);
        waitingtext.setVisibility(View.INVISIBLE);
        fin_Gamestext.setVisibility(View.GONE);
        showFinGame.setVisibility(View.GONE);
        hideFinGame.setVisibility(View.GONE);
        arrw2.setVisibility(View.GONE);
        arrw1.setVisibility(View.GONE);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/hobostd.otf");
        newGame.setTypeface(tf);
        user.setTypeface(tf);
        gamestext.setTypeface(tf);
        waitingtext.setTypeface(tf);
        fin_Gamestext.setTypeface(tf);
        showFinGame.setTypeface(tf);
        hideFinGame.setTypeface(tf);

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
        showFinGame.setOnClickListener(this);
        hideFinGame.setOnClickListener(this);
        User.UserDetails.setHasHiddenFGames(getSavedHasHiddenFinishedGames());

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
        } else if(view == hideFinGame){
            hideFinishedGames();
            User.UserDetails.setHasHiddenFGames(true);
            saveHasHiddenFinishedGames();

        } else if(view == showFinGame){
            showFinishedGames();
            User.UserDetails.setHasHiddenFGames(false);
            saveHasHiddenFinishedGames();
        }

    }

    // show all finished games
    private void showFinishedGames(){
        finGames.setVisibility(View.VISIBLE);
        hideFinGame.setVisibility(View.VISIBLE);
        fin_Gamestext.setVisibility(View.VISIBLE);
        showFinGame.setVisibility(View.GONE);
        arrw2.setVisibility(View.GONE);
        arrw1.setVisibility(View.GONE);
    }

    // hide all finished games
    private void hideFinishedGames(){
        finGames.setVisibility(View.GONE);
        hideFinGame.setVisibility(View.GONE);
        fin_Gamestext.setVisibility(View.GONE);
        showFinGame.setVisibility(View.VISIBLE);
        arrw2.setVisibility(View.VISIBLE);
        arrw1.setVisibility(View.VISIBLE);
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
        waitingGameAdapter = new WaitingGameAdapter(getActivity());

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
        finishedGameAdapter = new FinishedGameAdapter(getActivity());

        final int adapterCount = finishedGameAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = finishedGameAdapter.getView(i, null, null);
            finGames.addView(item);
        }


        // show/hide text depending on number of items
        if(finishedGameAdapter.getCount() > 0){
            if (User.UserDetails.getHasHiddenFGames()){
                hideFinGame.setVisibility(View.GONE);
                fin_Gamestext.setVisibility(View.GONE);
                showFinGame.setVisibility(View.VISIBLE);
            } else {
                hideFinGame.setVisibility(View.VISIBLE);
                fin_Gamestext.setVisibility(View.VISIBLE);
                showFinGame.setVisibility(View.GONE);
            }
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
                if (User.UserDetails.getHasHiddenFGames()){
                    showTrue();
                } else {
<<<<<<< HEAD
                    fin_Gamestext.setVisibility(View.GONE);
                    hideFinGame.setVisibility(View.VISIBLE);
=======
                    showFalse();
>>>>>>> 8a80f5ab644805ae5950c19cb9e686c6940183f6
                }
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
    private void showFalse(){
        hideFinGame.setVisibility(View.VISIBLE);
        fin_Gamestext.setVisibility(View.VISIBLE);
        finGames.setVisibility(View.VISIBLE);
        showFinGame.setVisibility(View.GONE);
        arrw1.setVisibility(View.GONE);
        arrw2.setVisibility(View.GONE);
    }
    private void showTrue(){
        hideFinGame.setVisibility(View.GONE);
        fin_Gamestext.setVisibility(View.GONE);
        finGames.setVisibility(View.GONE);
        showFinGame.setVisibility(View.VISIBLE);
        arrw1.setVisibility(View.VISIBLE);
        arrw2.setVisibility(View.VISIBLE);

    }
    private boolean getSavedHasHiddenFinishedGames(){
        boolean hasHiddenFGames = false;

        try{
            hasHiddenFGames = getActivity().getSharedPreferences(helperClass.getPrefsHideFGames(), MainActivity.MODE_PRIVATE).getBoolean("hasHiddenFGames", false);
        } catch (Exception ex){
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }

        return hasHiddenFGames;
    }
    private void saveHasHiddenFinishedGames(){
        try{
            getActivity().getSharedPreferences(helperClass.getPrefsHideFGames(), MainActivity.MODE_PRIVATE).edit().putBoolean("hasHiddenFGames", User.UserDetails.getHasHiddenFGames()).commit();
        } catch (Exception ex) {
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }
    }
}
