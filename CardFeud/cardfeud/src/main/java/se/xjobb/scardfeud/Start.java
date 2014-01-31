package se.xjobb.scardfeud;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
    private ListView games, waiting, finGames;
    private List<Response> myTurns;
    private List<Response> opponentsTurns;
    private List<Response> finishedGames;
    private String username;
    private String myCountry;
    ImageView flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_layout, container, false);
        user = (TextView)rootView.findViewById(R.id.user);
        gamestext = (TextView)rootView.findViewById(R.id.games_text);
        fin_Gamestext = (TextView)rootView.findViewById(R.id.fin_game_text);
        newGame = (Button)rootView.findViewById(R.id.Btn_newGame);
        waitingtext = (TextView)rootView.findViewById(R.id.waiting_text);
        games = (ListView)rootView.findViewById(R.id.games);
        waiting = (ListView)rootView.findViewById(R.id.waiting);
        finGames = (ListView)rootView.findViewById(R.id.finGames);
        flag = (ImageView)rootView.findViewById(R.id.myFlag);
        user.getBackground().setAlpha(150);
        gamestext.getBackground().setAlpha(150);
        fin_Gamestext.getBackground().setAlpha(150);
        newGame.getBackground().setAlpha(150);
        waitingtext.getBackground().setAlpha(150);
        newGame.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        myCountry = User.UserDetails.getUserCountryCode();
        gamestext.setVisibility(View.INVISIBLE);
        waitingtext.setVisibility(View.INVISIBLE);
        fin_Gamestext.setVisibility(View.INVISIBLE);

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
        if(!myTurns.isEmpty()) {
            AvailableGameAdapter availableGameAdapter = new AvailableGameAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.icon, new Start(), view.getTag() + "");
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });
            games.setAdapter(availableGameAdapter);
            gamestext.setVisibility(View.VISIBLE);
        }
        if(!opponentsTurns.isEmpty()) {
            WaitingGameAdapter waitingGameAdapter = new WaitingGameAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.icon, new Start(), view.getTag() + "");
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });
            waiting.setAdapter(waitingGameAdapter);
            waitingtext.setVisibility(View.VISIBLE);
        }
        if(!finishedGames.isEmpty()) {
            FinishedGameAdapter finishedGameAdapter = new FinishedGameAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.icon, new Start(), view.getTag() + "");
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });
            finGames.setAdapter(finishedGameAdapter);
            fin_Gamestext.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    @Override
    public void onClick(View view) {

        if (view == newGame){
            Intent ng = new Intent(getActivity().getApplicationContext(), NewGame.class);
            startActivity(ng);
        }
    }


}
