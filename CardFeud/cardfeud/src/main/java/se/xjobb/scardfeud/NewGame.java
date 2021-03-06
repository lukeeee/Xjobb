package se.xjobb.scardfeud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import se.xjobb.scardfeud.Posters.PostGameStart;

public class NewGame extends ActionBarActivity implements View.OnClickListener{

    private ActionBar actionBar;
    TextView user;
    TextView friends;
    Button search_player;
    Button random_player;
    private String username;
    private String userCountry;
    private boolean setFlag = false;
    ImageView flag;
    LinearLayout lnrMain, friendList;
    Boolean hasPremium = false;
    private ProgressDialog progressDialog;
    private HelperClass helperClass;
    //ListView friendList;
    private ArrayAdapter<String> adapter;
    private List<String> data;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgame);
        user = (TextView)findViewById(R.id.user2);
        friends = (TextView)findViewById(R.id.friendsText);
        friendList = (LinearLayout)findViewById(R.id.friends);
        search_player = (Button)findViewById(R.id.search_player);
        random_player = (Button)findViewById(R.id.random_player);
        flag = (ImageView)findViewById(R.id.flagMy);
        lnrMain = (LinearLayout)findViewById(R.id.lnrMain);
        //search_player.getBackground().setAlpha(200);
       // user.getBackground().setAlpha(200);
        //friends.getBackground().setAlpha(200);
        //random_player.getBackground().setAlpha(200);
        search_player.setOnClickListener(this);
        random_player.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        userCountry = User.UserDetails.getUserCountryCode();
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        search_player.setTypeface(tf);
        random_player.setTypeface(tf);
        user.setTypeface(tf);
        friends.setTypeface(tf);

        helperClass = new HelperClass(this);
        setFlag = true;
        setUserFlag();
        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);

        data = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        //friendList.setAdapter(adapter);

        db = new DatabaseHandler(this);

        FriendAdapter friendAdapter = new FriendAdapter(this, this);
        final int adapterCount = friendAdapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = friendAdapter.getView(i, null, null);
            friendList.addView(item);
        }


        if (!hasPremium){
            Log.i("Premium Madaafakka", "");
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AdView adView = new AdView(NewGame.this);
                    adView.setAdUnitId("0445b7141d9d4e1b");
                    adView.setAdSize(AdSize.BANNER);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                    adView.loadAd(builder.build());
                    lnrMain.addView(adView);
                }
            });
        }

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(!setFlag){
            setUserFlag();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        setFlag = false;
    }

    @Override
    protected void onStop(){
        super.onStop();
        setFlag = false;
    }


    private void setUserFlag(){
        user.setText(username);

        // set the user flag, if not found set default
        try {
            String country = userCountry.toLowerCase();
            int id = getResources().getIdentifier(country, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            flag.setImageDrawable(drawable);
        } catch (Resources.NotFoundException ex) {
            // if the flag can't be found
            int id = getResources().getIdentifier("globe", "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            flag.setImageDrawable(drawable);
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
        if(!helperClass.isConnected()){
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
            SoundsVibration.vibrate(NewGame.this);
            Intent i = new Intent(getBaseContext(), Search.class);
            startActivity(i);
            this.finish();
        } else if(v == random_player){
            SoundsVibration.vibrate(NewGame.this);
            challengeRandomPlayer();
        }
    }

}
