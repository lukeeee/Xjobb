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
                getSharedPreferences(helperClass.getPrefsIdentifier(), MODE_PRIVATE).edit().putString("identifier", User.UserDetails.getIdentifier()).commit();
            }

            if(User.UserDetails.getUsername() != null){
                getSharedPreferences(helperClass.getPrefsUsername(), MODE_PRIVATE).edit().putString("username", User.UserDetails.getUsername()).commit();
            }

            //  if(User.UserDetails.getDeviceId() != null){
            //      getSharedPreferences(helperClass.getPrefsDeviceId(), MODE_PRIVATE).edit().putString("deviceId", User.UserDetails.getDeviceId()).commit();
            //  }

        } catch (Exception ex){
            Log.e("Exception: ", ex.getMessage());
        }
    }


    public void finishActivity(String message){
        showFeedbackToast(message);
        saveValues();

        System.out.println("Username: " + User.UserDetails.getUsername());
        System.out.println("UserId: " + User.UserDetails.getUserId());
        System.out.println("Identifier: " + User.UserDetails.getIdentifier());

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

//TODO save values to shared prefs, both after sign up and login
//TODO also read values when app opens, if values set, no need for login.
//TODO send user in to app after sign up
//TODO also set device_id when sign in and sign up!!
//TODO once logged in, stay in!! no need to show login several times


// call to PostDeivceId from PostLogin/PostSignUp and then from PostDeviceId (instead of from PostLogin etc) stop the progressDialog (stop before if error's occur)