package se.xjobb.scardfeud;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import se.xjobb.scardfeud.Posters.PostGameStart;

public class NewGame extends Activity implements View.OnClickListener{
    TextView user;
    TextView friends;
    Button search_player;
    Button random_player;
    private String username;
    private String userCountry;
    ImageView flag;
    private String myCountry;

    private ProgressDialog progressDialog;
    private HelperClass helperClass;

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
        random_player.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        Drawable myFlag = getResources().getDrawable(R.drawable.se);
        user.setText(username);
        myCountry = User.UserDetails.getUserCountryCode();
        String country = myCountry.toLowerCase();
        int id = getResources().getIdentifier(country, "drawable", this.getPackageName());
        Drawable drawable = getResources().getDrawable(id);
        flag.setImageDrawable(drawable);
        userCountry = User.UserDetails.getUserCountryCode();

        helperClass = new HelperClass(this);
    }


    @Override
    protected void onResume(){
        super.onResume();
        user.setText(username);

        try {
            String country = userCountry.toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            flag.setImageDrawable(drawable);
        } catch (RuntimeException ex) {
            Toast.makeText(this, "Finns inte!", 1000).show();
        }


    }

    // show loading dialog
    public void showProgressDialog(){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, null, "Loading...", true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, null, "Loading...", true, false);
        }
    }

    // hide loading dialog
    public void hideProgressDialog(){
        // if loading dialog is visible, then hide it
        if(progressDialog != null){
            progressDialog.cancel();
        }
    }

    // finish request
    public void finishRequest(){
        Toast.makeText(this, "Game request sent!", 1000).show();
    }

    // show error dialog
    public void showErrorDialog(String message){
        progressDialog = null;
        helperClass.showErrorDialog(message);
    }


    private void challengeRandomPlayer(){
        if(helperClass.isConnected() != true){
            helperClass.showNetworkErrorDialog();
            // add retry to dialog.
        } else {
            // http request to challenge random opponent
            // 0 means random opponent
            PostGameStart postGameStart = new PostGameStart(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), 0, this);
            postGameStart.postRequest();
        }
    }

    @Override
    public void onClick(View v) {

        if(v == search_player){
            Intent i = new Intent(getBaseContext(), Search.class);
            startActivity(i);
            this.finish();
        } else if(v == random_player){
            //challengeRandomPlayer();
            Toast.makeText(this, "Disabled! :)", 1000).show();
        }
    }
}
