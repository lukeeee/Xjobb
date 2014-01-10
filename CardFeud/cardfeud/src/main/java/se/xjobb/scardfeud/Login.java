package se.xjobb.scardfeud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        signInButton.setOnClickListener(this);
        goToSignUpButton.setOnClickListener(this);

        helperClass = new HelperClass(this);
        crypt = new Crypt();
    }


    @Override
    protected void onResume(){
        super.onResume();

        try{
            User.UserDetails.setUserId(getSharedPreferences(helperClass.getPrefsUserId(), MODE_PRIVATE).getInt("userId", 0));
            User.UserDetails.setUsername(getSharedPreferences(helperClass.getPrefsUsername(), MODE_PRIVATE).getString("username", null));
            // decrypt the identifier
            User.UserDetails.setIdentifier(crypt.decrypt(helperClass.getKey(), getSharedPreferences(helperClass.getPrefsIdentifier(), MODE_PRIVATE).getString("identifier", null)));

        } catch (Exception ex) {
            Log.e("Exception SharedPrefs: ", ex.getMessage());
        }


        if(User.UserDetails.getUsername() != null && User.UserDetails.getIdentifier() != null
                && User.UserDetails.getUserId() != 0){

            // we found all stored values and do not need to login again.
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            this.finish();
        }

        System.out.println("Username: " + User.UserDetails.getUsername());
        System.out.println("Identifier: " + User.UserDetails.getIdentifier());
        System.out.println("UserId: " + User.UserDetails.getUserId());

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




    private boolean validateInput(String username, String password){

        if(username.length() > 30 || username.length() < 2){
            usernameEditText.setBackgroundResource(R.drawable.error);
            usernameEditText.setPadding(20, 0, 0, 0);
            errorUsername.setText("2-30 chars");
            return false;
        }


        if(password.length() > 30 || password.length() < 2){
            passwordEditText.setBackgroundResource(R.drawable.error);
            passwordEditText.setPadding(20, 0, 0, 0);
            errorPassword.setText("2-30 chars");
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


//TODO Use Crypt class for identifier
//TODO use a solid measurement instead? like screensize/2 instead of dp unit?
//TODO also set device_id when sign in and sign up!! Can't simply use device id?
//TODO once logged in, stay in!! no need to show login several times

// Do we need to replace the identifier each time? or can we use the same all the time?

// call to PostDeivceId from PostLogin/PostSignUp and then from PostDeviceId (instead of from PostLogin etc) stop the progressDialog (stop before if error's occur)

// http://developer.android.com/google/gcm/index.html

/* Overview of gcm: You send a request to google server from your android phone.
 You receive a registration id as a response. You will then have to send this registration id to the server from where you wish to
 send notifications to the mobile. Using this registration id you can then send notification to the device. */