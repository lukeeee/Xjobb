package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Stats extends Activity implements View.OnClickListener {

private Button gameplay;
private Button start;
private Button rule;
private Button newgame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        gameplay = (Button)findViewById(R.id.gameplay);
        start = (Button)findViewById(R.id.start);
        rule = (Button)findViewById(R.id.rule);
        newgame = (Button)findViewById(R.id.newgame);
        gameplay.setOnClickListener(this);
        start.setOnClickListener(this);
        rule.setOnClickListener(this);
        newgame.setOnClickListener(this);


    }

    public void onClick(View view) {
        if (view == gameplay){
            Intent gp = new Intent(getBaseContext(), Game.class);
            startActivity(gp);
        }
        else if (view == start){
            Intent st = new Intent(getBaseContext(), Start.class);
            startActivity(st);
        }
        else if (view == rule){
            Intent ru = new Intent(getBaseContext(), Rules.class);
            startActivity(ru);
        }
        else if (view == newgame){
            Intent ng = new Intent(getBaseContext(), NewGame.class);
            startActivity(ng);
        }
    }


}
