package se.xjobb.scardfeud;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AppSettings extends Activity implements View.OnClickListener{

    private Button logoutButton;
    private HelperClass helperClass;
    private String username;
    private Button game;
    private Button countryDebug;
    private Button myAccount;
    private Button about_btn;
    private Button debug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);

        logoutButton = (Button)findViewById(R.id.logout);
        game = (Button)findViewById(R.id.game);
        countryDebug = (Button)findViewById(R.id.debug_country);
        myAccount = (Button)findViewById(R.id.myAccount);
        about_btn = (Button)findViewById(R.id.about_us);
        debug = (Button)findViewById(R.id.debug);
        about_btn.getBackground().setAlpha(0);
        myAccount.getBackground().setAlpha(0);
        logoutButton.getBackground().setAlpha(0);

        logoutButton.setOnClickListener(this);
        game.setOnClickListener(this);
        countryDebug.setOnClickListener(this);
        debug.setOnClickListener(this);

        helperClass = new HelperClass(this);
        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);
    }


    // save the empty values to shared prefs
    public void saveValues(){
        try{
            getSharedPreferences(helperClass.getPrefsUserId(), MODE_PRIVATE).edit().putInt("userId", User.UserDetails.getUserId()).commit();
            getSharedPreferences(helperClass.getPrefsIdentifier(), MODE_PRIVATE).edit().putString("identifier", User.UserDetails.getIdentifier()).commit();
            getSharedPreferences(helperClass.getPrefsUsername(), MODE_PRIVATE).edit().putString("username", User.UserDetails.getUsername()).commit();
            getSharedPreferences(helperClass.getPrefsCountryCode(), MODE_PRIVATE).edit().putString("countrycode", User.UserDetails.getUserCountryCode()).commit();
            getSharedPreferences(helperClass.getPrefsDeviceRegId(), MODE_PRIVATE).edit().putString("deviceRegId", User.UserDetails.getDeviceRegId()).commit();

        } catch (Exception ex){
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }
    }

   // clear all stored values
    private void clearValues(){
        User.UserDetails.setUsername(null);
        User.UserDetails.setIdentifier(null);
        User.UserDetails.setUserId(0);
        User.UserDetails.setUserCountryCode(null);
        User.UserDetails.setDeviceRegId(null);
    }

   // show feedback and go to "android home"
    private void finishActivity(){
        username = User.UserDetails.getUsername();
        clearValues();
        saveValues();
        Toast.makeText(this, username + " logged out!", 1000).show();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v == logoutButton){
            finishActivity();
        } else if (v == game){
            Intent gp = new Intent(getApplicationContext(), Game.class);
            startActivity(gp);
        } else if (v == debug){
            Intent p = new Intent(getApplicationContext(), Banner.class);
            startActivity(p);
        } else if(v == countryDebug){
            // debug for temporary change of country
            List<String> debugCountries = new ArrayList<String>();
            debugCountries.add("SE");
            debugCountries.add("DE");
            debugCountries.add("NG");
            debugCountries.add("US");
            debugCountries.add("GB");
            debugCountries.add("ES");
            debugCountries.add("BG");
            debugCountries.add("XX");
            debugCountries.add("C_");
            debugCountries.add("PE");
            debugCountries.add("IS");


            int random = (int )(Math.random() * 10);
            String country = debugCountries.get(random);
            User.UserDetails.setUserCountryCode(country);
            Toast.makeText(this, "Country changed to: " + country, 1000).show();
        }
    }
}