package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Stats extends Activity implements View.OnClickListener {

private Button rule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        rule = (Button)findViewById(R.id.rule);
        rule.setOnClickListener(this);


    }

    public void onClick(View view) {

        if (view == rule){
            Intent ru = new Intent(getBaseContext(), Rules.class);
            startActivity(ru);
        }

    }


}
