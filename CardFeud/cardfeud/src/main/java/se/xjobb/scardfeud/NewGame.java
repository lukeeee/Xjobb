package se.xjobb.scardfeud;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewGame extends Activity implements View.OnClickListener{
    TextView user;
    TextView friends;
    Button search_player;
    Button random_player;
    private String username;
    ImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgame);
        user = (TextView)findViewById(R.id.user2);
        friends = (TextView)findViewById(R.id.friends);
        search_player = (Button)findViewById(R.id.search_player);
        random_player = (Button)findViewById(R.id.random_player);
        flag = (ImageView)findViewById(R.id.flagMy);
        search_player.getBackground().setAlpha(150);
        user.getBackground().setAlpha(150);
        friends.getBackground().setAlpha(150);
        random_player.getBackground().setAlpha(150);
        search_player.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        Drawable myFlag = getResources().getDrawable(R.drawable.se);
        user.setText(username);
        flag.setImageDrawable(myFlag);
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
