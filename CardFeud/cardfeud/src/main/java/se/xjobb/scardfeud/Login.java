package se.xjobb.scardfeud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.xjobb.scardfeud.Posters.PostLogin;

/**
 * Created by Svempa on 2013-12-16.
 */
public class Login extends Activity implements View.OnClickListener{

    private EditText usernameEditText;
    private EditText passwordEditText;

    private TextView errorUsername;
    private TextView errorPassword;

    private Button signInButton;
    private Button goToSignUpButton;

    private ProgressDialog progressDialog;
    private HelperClass helperClass;
    private Crypt crypt;
    private boolean created = false;
    private final static int REQUEST_GOOGLE_PLAY_SERVICES = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        usernameEditText = (EditText) findViewById(R.id.usernameLoginInput);
        passwordEditText = (EditText) findViewById(R.id.passwordLoginInput);
        errorUsername = (TextView) findViewById(R.id.usernameLoginError);
        errorPassword = (TextView) findViewById(R.id.passwordLoginError);
        signInButton = (Button) findViewById(R.id.signInButton);
        goToSignUpButton = (Button) findViewById(R.id.goToSignUpButton);
        goToSignUpButton.getBackground().setAlpha(150);
        signInButton.getBackground().setAlpha(150);

        signInButton.setOnClickListener(this);
        goToSignUpButton.setOnClickListener(this);

        helperClass = new HelperClass(this);
        crypt = new Crypt();
        created = true;
        getUserDetails();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    protected void onResume(){
        super.onResume();

        if(created != true){
            // if onCreate didn't run
            getUserDetails();
        }
    }

    // check if google play service is available
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        Log.i("CardFeud: ",
                "checkGooglePlayServicesAvailable, connectionStatusCode="
                        + connectionStatusCode);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }

        return true;
    }

    // show a dialog if google play service ain't available
    private void showGooglePlayServicesAvailabilityErrorDialog(
        final int connectionStatusCode) {

        final Activity activity = this;

        runOnUiThread(new Runnable() {
            public void run() {

                final Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, activity,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                if (dialog == null) {
                    Log.e("CardFeud: ",
                            "couldn't get GooglePlayServicesUtil.getErrorDialog");

                }
                dialog.show();
            }
        });
    }


    private void getUserDetails(){

        try{
            User.UserDetails.setUserId(getSharedPreferences(helperClass.getPrefsUserId(), MODE_PRIVATE).getInt("userId", 0));
            User.UserDetails.setUsername(getSharedPreferences(helperClass.getPrefsUsername(), MODE_PRIVATE).getString("username", null));
            User.UserDetails.setUserCountryCode(getSharedPreferences(helperClass.getPrefsCountryCode(), MODE_PRIVATE).getString("countrycode", null));

            // decrypt the identifier
            User.UserDetails.setIdentifier(crypt.decrypt(helperClass.getKey(), getSharedPreferences(helperClass.getPrefsIdentifier(), MODE_PRIVATE).getString("identifier", null)));

        } catch (Exception ex) {
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }


        if(User.UserDetails.getUsername() != null && User.UserDetails.getIdentifier() != null
                && User.UserDetails.getUserId() != 0 && User.UserDetails.getUserCountryCode() != null){

            // we found all stored values and do not need to login again.
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            this.finish();
        } else {
            // if we are coming from sign up
            if(User.UserDetails.getUsername() != null){
                usernameEditText.setText(User.UserDetails.getUsername());
            }
        }

    }


    // show loading dialog
    public void showProgressDialog(){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, "Loading", "Please Wait...", true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, "Loading", "Please Wait...", true, false);
        }
    }

    // hide loading dialog
    public void hideProgressDialog(){
        // if loading dialog is visible, then hide it
        if(progressDialog != null){
            progressDialog.cancel();
        }
    }

    // show error dialog
    public void showErrorDialog(String message){
        progressDialog = null;
        helperClass.showErrorDialog(message);
    }

    // show feedback
    public void showFeedbackToast(String message){
        Toast.makeText(this, message, 1000).show();
    }

    // save values to shared prefs
    public void saveValues(){
        try{
            if(User.UserDetails.getUserId() != 0){
                getSharedPreferences(helperClass.getPrefsUserId(), MODE_PRIVATE).edit().putInt("userId", User.UserDetails.getUserId()).commit();
            }

            if(User.UserDetails.getIdentifier() != null){
                // encrypt identifier
                String encryptedIdentifier = crypt.encrypt(helperClass.getKey(), User.UserDetails.getIdentifier());
                getSharedPreferences(helperClass.getPrefsIdentifier(), MODE_PRIVATE).edit().putString("identifier", encryptedIdentifier).commit();
            }

            if(User.UserDetails.getUsername() != null){
                getSharedPreferences(helperClass.getPrefsUsername(), MODE_PRIVATE).edit().putString("username", User.UserDetails.getUsername()).commit();
            }

            if(User.UserDetails.getUserCountryCode() != null){
                getSharedPreferences(helperClass.getPrefsCountryCode(), MODE_PRIVATE).edit().putString("countrycode", User.UserDetails.getUserCountryCode()).commit();
            }

            //  if(User.UserDetails.getDeviceId() != null){
            //      getSharedPreferences(helperClass.getPrefsDeviceId(), MODE_PRIVATE).edit().putString("deviceId", User.UserDetails.getDeviceId()).commit();
            //  }

        } catch (Exception ex){
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }
    }


    public void finishActivity(String message){
        showFeedbackToast(message);
        saveValues();

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                dialog.setTitle("About CardFeud");
                dialog.setIcon(R.drawable.ic_action_about_d);
                dialog.setMessage("Cardfeud has been on the iPhone since 2011, and now it has launched for Android\n\n" +
                        "This is a card game that involves you to guess whether the next card will be higher or lower than the current card\n\n" +
                        "You can meet friends who have android phones or iphone or just meet random user\n\n" +
                        "So tell your friends to download the app in the AppStore or Google Play and compete to see who is best");
                dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                return true;

        }

        return true;

    }

    private boolean validateInput(String username, String password){

        if(username.length() > 30 || username.length() < 2){
            usernameEditText.setBackgroundResource(R.drawable.error);
            usernameEditText.setPadding(20, 0, 0, 0);
            errorUsername.setText("2-30 chars");
            return false;
        }

        if(username.matches("^\\s*$")){
            usernameEditText.setBackgroundResource(R.drawable.error);
            usernameEditText.setPadding(20, 0, 0, 0);
            errorUsername.setText("Can't be only whitespace");
            return false;
        }


        if(password.length() > 30 || password.length() < 2){
            passwordEditText.setBackgroundResource(R.drawable.error);
            passwordEditText.setPadding(20, 0, 0, 0);
            errorPassword.setText("2-30 chars");
            return false;
        }

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(password);
        boolean found = matcher.find();

        if(found){
            passwordEditText.setBackgroundResource(R.drawable.error);
            passwordEditText.setPadding(20, 0, 0, 0);
            errorPassword.setText("Whitespace not allowed in password");
            return false;
        }


        return true;
    }


    private void setNormalLayout(){
        // set normal background, padding and text
        usernameEditText.setBackgroundResource(R.drawable.shape);
        usernameEditText.setPadding(20, 0, 0, 0);
        errorUsername.setText("");

        passwordEditText.setBackgroundResource(R.drawable.shape);
        passwordEditText.setPadding(20, 0, 0, 0);
        errorPassword.setText("");
    }


    @Override
    public void onClick(View v) {

        // make sure Google Play Service is available
        if(checkGooglePlayServicesAvailable()) {

            if(v == signInButton){
                // if we are logging in
                setNormalLayout();

                String username = null;
                String password = null;

                try{
                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();

                } catch (NullPointerException ex) {
                    Log.e("Null Exception: ", ex.getMessage());
                }


                // check input
                if(validateInput(username, password) != false){

                    username = username.trim();
                    password = password.trim();

                    // create user object
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword(password);

                    // set static username value
                    User.UserDetails.setUsername(username);

                    if(helperClass.isConnected() != true){
                        helperClass.showNetworkErrorDialog();
                        // add retry to dialog.
                    } else {
                        // http request to sign up new user
                        PostLogin postSignUp = new PostLogin(this, newUser);
                        postSignUp.postJson();
                    }

                }
            } else if(v == goToSignUpButton){
                // if we don't have an account, go to sign up
                Intent i = new Intent(getBaseContext(), SignUp.class);
                startActivity(i);
                this.finish();
            }
        }

    }


}



// Do we need to replace the identifier each time? or can we use the same all the time?

//Can't simply use device id?
// call to PostDeivceId from PostLogin/PostSignUp and then from PostDeviceId (instead of from PostLogin etc) stop the progressDialog (stop before if error's occur)
// http://developer.android.com/google/gcm/index.html
// http://rdcworld-android.blogspot.in/2013/11/android-push-notification-google-cloud.html



/* Overview of gcm: You send a request to google server from your android phone.
 You receive a registration id as a response. You will then have to send this registration id to the server from where you wish to
 send notifications to the mobile. Using this registration id you can then send notification to the device. */