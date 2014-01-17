package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-08.
 */
public class Start extends Fragment implements View.OnClickListener {
    TextView user;
    TextView gamestext;
    TextView waitingtext;
    TextView fin_Gamestext;
    Button newGame;
    ListView games;
    ListView waiting;
    ListView finGames;
    private String username;
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
        games.getBackground().setAlpha(150);
        waiting.getBackground().setAlpha(150);
        finGames.getBackground().setAlpha(150);
        user.getBackground().setAlpha(150);
        gamestext.getBackground().setAlpha(150);
        fin_Gamestext.getBackground().setAlpha(150);
        newGame.getBackground().setAlpha(150);
        waitingtext.getBackground().setAlpha(150);
        newGame.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        Drawable myFlag = getResources().getDrawable(R.drawable.se);
        user.setText(username);
        flag.setImageDrawable(myFlag);
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
