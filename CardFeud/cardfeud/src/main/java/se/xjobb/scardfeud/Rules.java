package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-08.
 */
public class Rules extends Fragment {
    TextView user;
    TextView games;
    TextView fin_Games;
    Button newGame;
    Button game1;
    Button game2;
    Button fGame1;
    Button fGame2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_layout, container, false);
        user = (TextView)rootView.findViewById(R.id.user);
        games = (TextView)rootView.findViewById(R.id.games);
        fin_Games = (TextView)rootView.findViewById(R.id.fin_game);
        newGame = (Button)rootView.findViewById(R.id.Btn_newGame);
        game1 = (Button)rootView.findViewById(R.id.game1);
        game2 = (Button)rootView.findViewById(R.id.game2);
        fGame1 = (Button)rootView.findViewById(R.id.finGame1);
        fGame2 = (Button)rootView.findViewById(R.id.finGame2);
        user.getBackground().setAlpha(150);
        games.getBackground().setAlpha(150);
        fin_Games.getBackground().setAlpha(150);
        newGame.getBackground().setAlpha(150);
        game1.getBackground().setAlpha(150);
        game2.getBackground().setAlpha(150);
        fGame1.getBackground().setAlpha(150);
        fGame2.getBackground().setAlpha(150);
        return rootView;
    }
}
