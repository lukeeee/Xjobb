package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class AppSettings extends Activity implements View.OnClickListener{

    private Button logoutButton;
    private HelperClass helperClass;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);

        logoutButton = (Button)findViewById(R.id.logout);

        logoutButton.setOnClickListener(this);

        helperClass = new HelperClass(this);
    }


    // save the empty values to shared prefs
    public void saveValues(){
        try{
            getSharedPreferences(helperClass.getPrefsUserId(), MODE_PRIVATE).edit().putInt("userId", User.UserDetails.getUserId()).commit();
            getSharedPreferences(helperClass.getPrefsIdentifier(), MODE_PRIVATE).edit().putString("identifier", User.UserDetails.getIdentifier()).commit();
            getSharedPreferences(helperClass.getPrefsUsername(), MODE_PRIVATE).edit().putString("username", User.UserDetails.getUsername()).commit();
            getSharedPreferences(helperClass.getPrefsCountryCode(), MODE_PRIVATE).edit().putString("countrycode", User.UserDetails.getUserCountryCode()).commit();

            //getSharedPreferences(helperClass.getPrefsDeviceRegId(), MODE_PRIVATE).edit().putString("deviceRegId", User.UserDetails.getDeviceRegId()).commit();

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
        }
    }
}