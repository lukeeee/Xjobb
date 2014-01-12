package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Stats extends Activity implements View.OnClickListener {

private Button start;
private Button high;
private Button low;
private Button stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        start = (Button)findViewById(R.id.start_game);
        high = (Button)findViewById(R.id.higher);
        low = (Button)findViewById(R.id.lower);
        stat = (Button)findViewById(R.id.gamestat);
        high.getBackground().setAlpha(99);
        low.getBackground().setAlpha(99);
        stat.getBackground().setAlpha(99);
        start.setOnClickListener(this);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gameplay, menu);
        return true;
    }
    public void onClick(View view) {
        if (view == start){
            start.setVisibility(Button.INVISIBLE);
        }
    }

}
