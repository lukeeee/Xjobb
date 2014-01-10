package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AppSettings extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);
        Button debug = (Button)findViewById(R.id.bugga);
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getBaseContext(), SignUp.class);
                //startActivity(i);
            }

        });

    }


}