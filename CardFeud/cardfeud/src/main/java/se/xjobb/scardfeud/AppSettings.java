package se.xjobb.scardfeud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;


public class AppSettings extends ActionBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private ActionBar actionBar;
    private Button logoutButton;
    private HelperClass helperClass;
    private String username, myCountry;
    private Button myAccount;
    private Button about_btn, premium;
    private TextView versionText, appsoundstxt, appnottxt, appvibtxt, settings;
    private ToggleButton soundToggleButton;
    private ToggleButton notificationSoundToggleButton;
    private ToggleButton vibrationToggleButton;
    private boolean created = false;
    private final String TAG = "CardFeud SharedPrefs Exception ";
    private LinearLayout lnrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);

        logoutButton = (Button)findViewById(R.id.logout);
        myAccount = (Button)findViewById(R.id.myAccount);
        about_btn = (Button)findViewById(R.id.about_us);
        premium = (Button)findViewById(R.id.premium);
        versionText = (TextView)findViewById(R.id.version_text);
        appnottxt = (TextView)findViewById(R.id.appnottxt);
        appsoundstxt = (TextView)findViewById(R.id.appsoundtxt);
        appvibtxt = (TextView)findViewById(R.id.appvibtxt);
        settings = (TextView)findViewById(R.id.settings);
        soundToggleButton = (ToggleButton)findViewById(R.id.sounds);
        notificationSoundToggleButton = (ToggleButton)findViewById(R.id.notification_sound);
        vibrationToggleButton = (ToggleButton)findViewById(R.id.vibration);
        lnrMain = (LinearLayout)findViewById(R.id.settingslnrMain);
        about_btn.getBackground().setAlpha(0);
        myAccount.getBackground().setAlpha(0);
        logoutButton.getBackground().setAlpha(0);
        premium.getBackground().setAlpha(0);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        logoutButton.setTypeface(tf);
        myAccount.setTypeface(tf);
        about_btn.setTypeface(tf);
        versionText.setTypeface(tf);
        premium.setTypeface(tf);
        vibrationToggleButton.setTypeface(tf);
        soundToggleButton.setTypeface(tf);
        notificationSoundToggleButton.setTypeface(tf);
        appnottxt.setTypeface(tf);
        settings.setTypeface(tf);
        appsoundstxt.setTypeface(tf);
        appvibtxt.setTypeface(tf);

        logoutButton.setOnClickListener(this);
        soundToggleButton.setOnCheckedChangeListener(this);
        notificationSoundToggleButton.setOnCheckedChangeListener(this);
        vibrationToggleButton.setOnCheckedChangeListener(this);
        myAccount.setOnClickListener(this);
        about_btn.setOnClickListener(this);
        username = User.UserDetails.getUsername();
        myCountry = User.UserDetails.getUserCountryCode();
        helperClass = new HelperClass(this);
        premium.setOnClickListener(this);
        created = true;
        setSwitchStatus();
        setVersionText();

        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);

        Log.i("hallo", myCountry);

        if(myCountry.equals("US")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AdView adView = new AdView(AppSettings.this);
                    adView.setAdUnitId("0445b7141d9d4e1b");
                    adView.setAdSize(AdSize.BANNER);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                    adView.loadAd(builder.build());
                    lnrMain.addView(adView);
                }
            });

        }else{

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!created){
            // onCreate didn't run so we set switches status here
            Log.i("onResume", "YEE");
            setSwitchStatus();
            setVersionText();
        }

    }

    // set version text
    private void setVersionText(){
        if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            // large device layout
            if(User.UserDetails.getAppVersion() != 0){
                versionText.setText("Version:       " + User.UserDetails.getAppVersion());
            }

        } else {
            // normal size device layout

            if(User.UserDetails.getAppVersion() != 0){
                versionText.setText("Version:                   " + User.UserDetails.getAppVersion());
            }
        }
    }

    // set the status for the switches
    private void setSwitchStatus(){
       if(User.UserDetails.getSound()){
           // game sound if on and switch is set to on
           soundToggleButton.setChecked(true);
       } else {
           soundToggleButton.setChecked(false);
       }

       if(User.UserDetails.getVibration()){
           // vibration is on and switch is set to on
           vibrationToggleButton.setChecked(true);
       } else {
           vibrationToggleButton.setChecked(false);
       }

       if(User.UserDetails.getNotificationSound()){
           // sound for notifications is on and switch is set to on
           notificationSoundToggleButton.setChecked(true);
       } else {
           notificationSoundToggleButton.setChecked(false);
       }
   }

    // save settings for sound/vibration
    private void saveSettings(int choice){

        if(choice == 1){
            // save game sound settings
            try{
                getSharedPreferences(helperClass.getPrefsSound(), MODE_PRIVATE).edit().putBoolean("sound", User.UserDetails.getSound()).commit();
            } catch (Exception ex){
                Log.e(TAG, ex.getMessage());
            }
        } else if(choice == 2){
            // save vibration settings
            try{
                getSharedPreferences(helperClass.getPrefsVibration(), MODE_PRIVATE).edit().putBoolean("vibration", User.UserDetails.getVibration()).commit();
            } catch (Exception ex){
                Log.e(TAG, ex.getMessage());
            }
        } else if(choice == 3){
            // save notification sound settings
            try{
                getSharedPreferences(helperClass.getPrefsNotificationSound(), MODE_PRIVATE).edit().putBoolean("notificationSound", User.UserDetails.getNotificationSound()).commit();
            } catch (Exception ex){
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    // save the empty values to shared prefs
    private void saveValues(){
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

        // need to clear these as well
        GameListResult.setFinishedGames(new ArrayList<Response>());
        GameListResult.setInvitations(new ArrayList<Response>());
        GameListResult.setOpponentsTurns(new ArrayList<Response>());
        GameListResult.setMyTurns(new ArrayList<Response>());
    }

   // show feedback and go to "android home"
    private void finishActivity(){

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
            logoutButton.setTextColor(getResources().getColor(R.color.ColorBlack));
            finishActivity();
        } else if (v == premium){
            AlertDialog.Builder dialog = new AlertDialog.Builder(AppSettings.this);
            dialog.setTitle("Premium");
            dialog.setIcon(R.drawable.stat);
            dialog.setMessage("Go Premium and you get\nPersonal stats\nHigh Score in your country\nHigh score in world\nAnd no Ads");
            dialog.setPositiveButton("Go Premium", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }else if (v == myAccount){
            myAccount.setTextColor(getResources().getColor(R.color.ColorBlack));
            AlertDialog.Builder dialog = new AlertDialog.Builder(AppSettings.this);
            dialog.setTitle("My Account");
            dialog.setIcon(R.drawable.ic_action_person);
            dialog.setMessage("Name: " + username + "\nCountry: " + myCountry);
            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    myAccount.setTextColor(getResources().getColor(R.color.ColorWhite));
                }
            });
            dialog.show();
        }
        else if (v == about_btn){
            about_btn.setTextColor(getResources().getColor(R.color.ColorBlack));
            AlertDialog.Builder dialog = new AlertDialog.Builder(AppSettings.this);
            dialog.setTitle("About Cardfeud");
            dialog.setIcon(R.drawable.ic_action_about_d);
            dialog.setMessage("Cardfeud has been available on iPhone since 2011, and now its launched for Android\n\n" +
                    "This is a card game that involves you to guess whether the next card will be higher or lower than the current card\n\n" +
                    "You can meet your friends who have android phones or iphone or just meet random user\n\n" +
                    "So tell your friends to download it on Google Play or AppStore and compete to see who's the best");
            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    about_btn.setTextColor(getResources().getColor(R.color.ColorWhite));
                }
            });
            dialog.show();
        }

        }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView == soundToggleButton){
            if(isChecked){
                // sound for game is on
                User.UserDetails.setSound(true);
            } else {
                // sound for game off
                User.UserDetails.setSound(false);
            }
            // save new settings for game sounds
            saveSettings(1);

        } else if (buttonView == vibrationToggleButton){
            if(isChecked){
                // vibration on
                User.UserDetails.setVibration(true);
            } else {
                // vibration off
                User.UserDetails.setVibration(false);
            }
            // save new settings for vibration
            saveSettings(2);
        } else if (buttonView == notificationSoundToggleButton){
            if(isChecked){
                // sound for notifications on
                User.UserDetails.setNotificationSound(true);
            } else {
                // sound for notifications off
                User.UserDetails.setNotificationSound(false);
            }
            // save new settings for notification sound
            saveSettings(3);
        }

    }
}
