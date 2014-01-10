package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Stats extends Activity implements View.OnClickListener {

private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        start = (Button)findViewById(R.id.start_game);
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
