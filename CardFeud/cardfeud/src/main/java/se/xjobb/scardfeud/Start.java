package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lukas on 2014-01-08.
 */
public class Start extends Activity {
    TextView user;
    TextView games;
    TextView fin_Games;
    Button newGame;
    Button game1;
    Button game2;
    Button fGame1;
    Button fGame2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        user = (TextView)findViewById(R.id.user);
        games = (TextView)findViewById(R.id.games);
        fin_Games = (TextView)findViewById(R.id.fin_game);
        newGame = (Button)findViewById(R.id.Btn_newGame);
        game1 = (Button)findViewById(R.id.game1);
        game2 = (Button)findViewById(R.id.game2);
        fGame1 = (Button)findViewById(R.id.finGame1);
        fGame2 = (Button)findViewById(R.id.finGame2);
        user.getBackground().setAlpha(150);
        games.getBackground().setAlpha(150);
        fin_Games.getBackground().setAlpha(150);
        newGame.getBackground().setAlpha(150);
        game1.getBackground().setAlpha(150);
        game2.getBackground().setAlpha(150);
        fGame1.getBackground().setAlpha(150);
        fGame2.getBackground().setAlpha(150);
    }
}
