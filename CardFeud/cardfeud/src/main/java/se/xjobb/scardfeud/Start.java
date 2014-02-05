package se.xjobb.scardfeud;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;


/**
 * Created by Lukas on 2014-01-08.
 */
public class Start extends Fragment implements View.OnClickListener {
    TextView user;
    TextView gamestext;
    TextView waitingtext;
    TextView fin_Gamestext;
    private Button newGame;
    private List<Response> myTurns;
    private List<Response> opponentsTurns;
    private List<Response> finishedGames;
    private String username;
    private String myCountry;
    LinearLayout games, waiting, finGames;
    ImageView flag;
    FinishedGameAdapter finishedGameAdapter;
    WaitingGameAdapter waitingGameAdapter;
    AvailableGameAdapter availableGameAdapter;
    boolean finishedGameAdapterCreated = false;
    boolean waitingGameAdapterCreated = false;
    boolean availableGameAdapterCreated = false;

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
        user.getBackground().setAlpha(200);
        gamestext.getBackground().setAlpha(200);
        fin_Gamestext.getBackground().setAlpha(200);
        waitingtext.getBackground().setAlpha(200);
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
        }
        myTurns = GameListResult.getMyTurns();
        opponentsTurns = GameListResult.getOpponentsTurns();
        finishedGames = GameListResult.getFinishedGames();

        createAvailableGameAdapter();
        createWaitingGameAdapter();
        createFinishedGameAdapter();

        MainActivity.setStartTag(getTag());
        flag.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View view) {

        if (view == newGame){
            Intent ng = new Intent(getActivity().getApplicationContext(), NewGame.class);
            startActivity(ng);
        } else if (view == flag){
            flag.animate().translationX(210);

        }
    }

    private void createAvailableGameAdapter(){
        if(!myTurns.isEmpty()) {
            availableGameAdapterCreated = true;
            availableGameAdapter = new AvailableGameAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity().getApplicationContext(), GameSplash.class);
                    startActivity(i);
                    /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.icon, new Start(), view.getTag() + "");
                    ft.addToBackStack(null);
                    ft.commit();*/


                }
            });
            final int adapterCount = availableGameAdapter.getCount();

            for (int i = 0; i < adapterCount; i++) {
                View item = availableGameAdapter.getView(i, null, null);
                games.addView(item);
            }
            gamestext.setVisibility(View.VISIBLE);
        }
    }

    private void createWaitingGameAdapter(){
        if(!opponentsTurns.isEmpty()) {
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
            waitingtext.setVisibility(View.VISIBLE);
        }
    }

    private void createFinishedGameAdapter(){
        if(!finishedGames.isEmpty()) {
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
            fin_Gamestext.setVisibility(View.VISIBLE);
        }
    }


    // refresh the arraylists data and adapters
    public void refresh(){
        myTurns = GameListResult.getMyTurns();
        opponentsTurns = GameListResult.getOpponentsTurns();
        finishedGames = GameListResult.getFinishedGames();

        // check if the arraylist adapters is instantiated
        if(!availableGameAdapterCreated){
            createAvailableGameAdapter();
        }

        if(!finishedGameAdapterCreated){
            createFinishedGameAdapter();
        }

        if(!waitingGameAdapterCreated){
            createWaitingGameAdapter();
        }


        // make sure tha adapters are not null
        if(availableGameAdapter != null){
            availableGameAdapter.notifyDataSetChanged();
        }

        if(finishedGameAdapter != null){
            finishedGameAdapter.notifyDataSetChanged();
        }

        if(waitingGameAdapter != null){
            waitingGameAdapter.notifyDataSetChanged();
        }
    }
}
