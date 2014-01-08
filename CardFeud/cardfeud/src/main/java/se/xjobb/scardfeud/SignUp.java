package se.xjobb.scardfeud;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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

        signUpButton.setOnClickListener(this);
        goToLoginButton.setOnClickListener(this);

    }



    private void validateInput(String username, String password, String passwordRepeat, String countryCode){

        if(username.length() > 30 || username.length() < 2){
            usernameEditText.setBackgroundResource(R.drawable.error);
            usernameEditText.setPadding(20, 0, 0, 0);
            errorUsername.setText("2-30 chars");
        }


        if(password.length() > 30 || password.length() < 2){
            passwordEditText.setBackgroundResource(R.drawable.error);
            passwordEditText.setPadding(20, 0, 0, 0);
            errorPassword.setText("2-30 chars");
        }

        if(!password.equals(passwordRepeat)){
            passwordRepeatEditText.setBackgroundResource(R.drawable.error);
            passwordRepeatEditText.setPadding(20, 0, 0, 0);

            passwordEditText.setBackgroundResource(R.drawable.error);
            passwordEditText.setPadding(20, 0, 0, 0);

            errorPasswordRepeat.setText("Passwords do not match");
        }

        if(countryCode.length() > 2 || countryCode.length() < 2){
            countryCodeEditText.setBackgroundResource(R.drawable.error);
            countryCodeEditText.setPadding(20, 0, 0, 0);
            errorCountryCode.setText("2 chars needed");
        }
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
            signUpButton.setBackgroundResource(R.drawable.shape_pressed);
            setNormalLayout();

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordRepeat = passwordRepeatEditText.getText().toString();
            String countryCode = countryCodeEditText.getText().toString().toUpperCase();

            // check input
            validateInput(username, password, passwordRepeat, countryCode);

            // send json request ( new class )



        } else if (v == goToLoginButton) {
            goToLoginButton.setBackgroundResource(R.drawable.shape_pressed);
            // if we already have a account
            // go to log in

        }

    }
}


//TODO use a solid measurement instead? like screensize/2 instead of dp unit?