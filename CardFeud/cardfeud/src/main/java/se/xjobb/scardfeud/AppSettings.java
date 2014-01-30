package se.xjobb.scardfeud;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class AppSettings extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Button logoutButton;
    private HelperClass helperClass;
    private String username;
    private Button game;
    private Button countryDebug;
    private Button myAccount;
    private Button about_btn;
    private TextView versionText;
    private ToggleButton soundToggleButton;
    private ToggleButton notificationSoundToggleButton;
    private ToggleButton vibrationToggleButton;
    private boolean created = false;
    private final String TAG = "CardFeud SharedPrefs Exception";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);

        logoutButton = (Button)findViewById(R.id.logout);
        game = (Button)findViewById(R.id.game);
        countryDebug = (Button)findViewById(R.id.debug_country);
        myAccount = (Button)findViewById(R.id.myAccount);
        about_btn = (Button)findViewById(R.id.about_us);
        versionText = (TextView)findViewById(R.id.version_text);
        soundToggleButton = (ToggleButton)findViewById(R.id.sounds);
        notificationSoundToggleButton = (ToggleButton)findViewById(R.id.notification_sound);
        vibrationToggleButton = (ToggleButton)findViewById(R.id.vibration);
        about_btn.getBackground().setAlpha(0);
        myAccount.getBackground().setAlpha(0);
        logoutButton.getBackground().setAlpha(0);

        logoutButton.setOnClickListener(this);
        game.setOnClickListener(this);
        countryDebug.setOnClickListener(this);
        soundToggleButton.setOnCheckedChangeListener(this);
        notificationSoundToggleButton.setOnCheckedChangeListener(this);
        vibrationToggleButton.setOnCheckedChangeListener(this);

        helperClass = new HelperClass(this);
        created = true;
        setSwitchStatus();
        setVersionText();

        final ActionBar actionBar = getActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);
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
                versionText.setText("Version:                       " + User.UserDetails.getAppVersion());
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
