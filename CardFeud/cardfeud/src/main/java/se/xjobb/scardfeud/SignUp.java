package se.xjobb.scardfeud;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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


public class SignUp extends ActionBarActivity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private EditText countryCodeEditText;

    private TextView errorUsername;
    private TextView errorPassword;
    private TextView errorPasswordRepeat;
    private TextView errorCountryCode, signuptext, already_text;

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
        signuptext = (TextView)findViewById(R.id.signup_text);
        already_text = (TextView)findViewById(R.id.already_text);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        goToLoginButton = (Button) findViewById(R.id.goToLoginButton);
        //signUpButton.getBackground().setAlpha(200);
        //goToLoginButton.getBackground().setAlpha(200);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        goToLoginButton.setTypeface(tf);
        signUpButton.setTypeface(tf);
        errorCountryCode.setTypeface(tf);
        errorPassword.setTypeface(tf);
        errorPasswordRepeat.setTypeface(tf);
        errorUsername.setTypeface(tf);
        usernameEditText.setTypeface(tf);
        passwordEditText.setTypeface(tf);
        passwordRepeatEditText.setTypeface(tf);
        countryCodeEditText.setTypeface(tf);
        signuptext.setTypeface(tf);
        already_text.setTypeface(tf);

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

        Intent i = new Intent(getBaseContext(), Login.class);
        startActivity(i);
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.globe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_globe:
                AlertDialog.Builder dialog = new AlertDialog.Builder(SignUp.this);
                dialog.setTitle("Country Codes");
                dialog.setIcon(R.drawable.globe_icon);
                dialog.setMessage("Afghanistan: AF\n" +"Albania: AL\n" +"Algeria: DZ\n" +
                        "Andorra: AD\n" +"Angola: AO\n" +"Argentina: AR\n" +"Armenia: AM\n" +
                        "Australia: AU\n" +"Austria: AT\n" +"Azerbaijan: AZ\n" +"Bahamas: BS\n" +
                        "Bahrain: BH\n" +"Barbados: BB\n" +"Belarus: BY\n" +"Belgium: BE\n" +
                        "Bolivia: BO\n" +"Bosnia & Herzegowina: BA\n" +"Brazil: BR\n" +
                        "Bulgaria: BG\n" +"Cameroon: CM\n" +"Canada: CA\n" +"Chile: CL\n" +
                        "China: CN\n" +"Colombia: CO\n" +"Costa Rica: CR\n" +"Ivory Coast: CI\n" +
                        "Croatia: HR\n" +"Cuba: CU\n" +"Cyprus: CY\n" +"Czech Republic: CZ\n" +
                        "Denmark: DK\n" +"Dominican Republic: DO\n" +"Ecuador: EC\n" +"Egypt: EG\n"+
                        "El Salvador: SV\n" +"Estonia: EE\n" +"Faroe Islands: FO\n" +"Fiji: FJ\n" +
                        "Finland: FI\n" +"France: FR\n" +"Georgia: GE\n" +"Germany: DE\n" +
                        "Ghana: GH\n" +"Greeze: GR\n" +"Guatemala: GT\n" +"Honduras: HN\n" +
                        "Hong Kong: HK\n" +"Hungary: HU\n" +"Iceland: IS\n" +"India: IN\n" +
                        "Indonesia: ID\n" +"Iran: IR\n" +"Iraq: IQ\n" +"Ireland: IE\n" +"Israel: IL\n" +
                        "Italy: IT\n" +"Jamaica: JM\n" +"Japan: JP\n" +"Kazakhstan: KZ\n" +
                        "Latvia: LV\n" +"Lebanon: LB\n" +"Libyan: LY\n" +"Liechtenstein: LI\n" +
                        "Lithuania: LT\n" +"Luxembourg: LU\n" +"Macedonia: MK\n" +"Malaysia: MY\n" +
                        "Malta: MT\n" +"Mexico: MX\n" +"Moldova: MD\n" +"Monaco: MC\n" +
                        "Montenegro: ME\n" +"Morocco: MA\n" +"Nepal: NP\n" +"Netherlands. NL\n" +
                        "New Zealand: NZ\n" +"Nigeria: NG\n" +"North Korea: KP\n" +"Norway: NO\n" +
                        "Oman: OM\n" +"Pakistan: PK\n" +"Panama: PA\n" +"Paraguay: PY\n" +
                        "Peru: PE\n" +"Philippines: PH\n" +"PolandPL\n" +"Portugal: PT\n" +
                        "Puerto Rico: PR\n" +"Qatar: QA\n" +"Romania: RO\n" +"Russia: RU\n" +
                        "San Marino: SM\n" +"Saudi Arabia: SA\n" +"Senegal: SN\n" +"Serbia: RS\n" +
                        "Singapore: SG\n" +"Slovakia: SK\n" +"Slovenia: SI\n" +"Somalia: SO\n" +
                        "South Africa: ZA\n" +"South Korea: KR\n" +"Spain: ES\n" +"Sweden: SE\n" +
                        "Switzerland: CH\n" +"Syria: SY\n" +"Taiwan: TW\n" +"Thailand: TH\n" +
                        "Trinidad & Tobago: TT\n" +"Tunisia: TN\n" +"Turkey: TR\n" +"Ukraine: UA\n" +
                        "United Arab Emirates: AE\n" +"United Kingdom: GB\n" +"USA: US\n" +
                        "Uruguay: UY\n" +"Uzbekistan: UZ\n" +"Venezuela: VE\n" +"Vietnam: VN\n");
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
            if(validateInput(username, password, passwordRepeat, countryCode)){

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
