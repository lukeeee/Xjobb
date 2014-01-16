package se.xjobb.scardfeud;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewGame extends Activity implements View.OnClickListener{
    TextView user;
    TextView friends;
    Button search_player;
    Button random_player;
    Button one;
    Button two;
    Button three;
    Button four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgame);
        user = (TextView)findViewById(R.id.user2);
        friends = (TextView)findViewById(R.id.friends);
        search_player = (Button)findViewById(R.id.search_player);
        random_player = (Button)findViewById(R.id.random_player);
        one = (Button)findViewById(R.id.one);
        two = (Button)findViewById(R.id.two);
        three = (Button)findViewById(R.id.three);
        four = (Button)findViewById(R.id.four);
        search_player.getBackground().setAlpha(150);
        user.getBackground().setAlpha(150);
        friends.getBackground().setAlpha(150);
        random_player.getBackground().setAlpha(150);
        one.getBackground().setAlpha(150);
        two.getBackground().setAlpha(150);
        three.getBackground().setAlpha(150);
        four.getBackground().setAlpha(150);

        search_player.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {

        if(v == search_player){
            Intent i = new Intent(getBaseContext(), Search.class);
            startActivity(i);
            this.finish();
        }
    }
}
