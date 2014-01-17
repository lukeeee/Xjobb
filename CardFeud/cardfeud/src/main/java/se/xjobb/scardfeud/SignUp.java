package se.xjobb.scardfeud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.xjobb.scardfeud.Posters.PostSignUp;


public class SignUp extends Activity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private EditText countryCodeEditText;

    private TextView errorUsername;
    private TextView errorPassword;
    private TextView errorPasswordRepeat;
    private TextView errorCountryCode;

    private Button signUpButton;
    private Button goToLoginButton;

    private ProgressDialog progressDialog;
    private HelperClass helperClass;
    private Crypt crypt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        usernameEditText = (EditText) findViewById(R.id.usernameInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        passwordRepeatEditText = (EditText) findViewById(R.id.passwordRepeatInput);
        countryCodeEditText = (EditText) findViewById(R.id.countryCodeInput);

        errorUsername = (TextView) findViewById(R.id.usernameError);
        errorPassword = (TextView) findViewById(R.id.passwordError);
        errorPasswordRepeat = (TextView) findViewById(R.id.passwordRepeatError);
        errorCountryCode = (TextView) findViewById(R.id.coutryCodeError);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        goToLoginButton = (Button) findViewById(R.id.goToLoginButton);
        signUpButton.getBackground().setAlpha(150);
        goToLoginButton.getBackground().setAlpha(150);

        signUpButton.setOnClickListener(this);
        goToLoginButton.setOnClickListener(this);

        helperClass = new HelperClass(this);
        crypt = new Crypt();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

          //  if(User.UserDetails.getDeviceRegId() != null){
          //      getSharedPreferences(helperClass.getPrefsDeviceRegId(), MODE_PRIVATE).edit().putString("deviceRegId", User.UserDetails.getDeviceRegId()).commit();
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(SignUp.this);
                dialog.setTitle("About CardFeud");
                dialog.setIcon(R.drawable.ic_action_about_d);
                dialog.setMessage("* Press the scoreboard to watch stats over this game\n\n" +
                        "* Guess if the next card will be higher or lower than the showing card by pressing Higher or Lower\n\n" +
                        "* Press pass to pass the turn to your opponent\n\n" +
                        "* You get one point if you are right\n\n" +
                        "* You loose one point if you are wrong");
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


    private boolean validateInput(String username, String password, String passwordRepeat,
                               String countryCode){

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


        if(!password.equals(passwordRepeat)){
            passwordRepeatEditText.setBackgroundResource(R.drawable.error);
            passwordRepeatEditText.setPadding(20, 0, 0, 0);

            passwordEditText.setBackgroundResource(R.drawable.error);
            passwordEditText.setPadding(20, 0, 0, 0);

            errorPasswordRepeat.setText("Passwords do not match");
            return false;
        }

        if(countryCode.length() > 2 || countryCode.length() < 2){
            countryCodeEditText.setBackgroundResource(R.drawable.error);
            countryCodeEditText.setPadding(20, 0, 0, 0);
            errorCountryCode.setText("2 chars needed");

            return false;
        }

        Matcher matcherCountry = pattern.matcher(countryCode);
        boolean foundCountry = matcherCountry.find();

        if(foundCountry){
            countryCodeEditText.setBackgroundResource(R.drawable.error);
            countryCodeEditText.setPadding(20, 0, 0, 0);
            errorCountryCode.setText("Whitespaces not allowed in Country Code");

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

        passwordRepeatEditText.setBackgroundResource(R.drawable.shape);
        passwordRepeatEditText.setPadding(20, 0, 0, 0);
        errorPasswordRepeat.setText("");

        countryCodeEditText.setBackgroundResource(R.drawable.shape);
        countryCodeEditText.setPadding(20, 0, 0, 0);
        errorCountryCode.setText("");
    }

    @Override
    public void onClick(View v) {

        if (v == signUpButton){

            // if we are signing up
            setNormalLayout();

            String username = null;
            String password = null;
            String passwordRepeat = null;
            String countryCode = null;

            try{
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                passwordRepeat = passwordRepeatEditText.getText().toString();
                countryCode = countryCodeEditText.getText().toString().toUpperCase();

            } catch (NullPointerException ex) {
                Log.e("Null Exception: ", ex.getMessage());
            }


            // check input
            if(validateInput(username, password, passwordRepeat, countryCode) != false){

                username = username.trim();
                password = password.trim();
                countryCode = countryCode.trim();

                // create user object
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setCountryCode(countryCode);

                // set static username and country code value
                User.UserDetails.setUsername(username);
                User.UserDetails.setUserCountryCode(countryCode);

                if(helperClass.isConnected() != true){
                    helperClass.showNetworkErrorDialog();
                    // add retry to dialog.
                } else {
                    // http request to sign up new user
                    PostSignUp postSignUp = new PostSignUp(this, newUser);
                    postSignUp.postJson();
                }

            }


        } else if (v == goToLoginButton) {
            // if we already have a account, go to login
            Intent i = new Intent(getBaseContext(), Login.class);
            startActivity(i);
            this.finish();
        }

    }
}
