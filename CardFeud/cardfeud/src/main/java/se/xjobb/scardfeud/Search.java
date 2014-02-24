package se.xjobb.scardfeud;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import se.xjobb.scardfeud.Posters.PostGameStart;
import se.xjobb.scardfeud.Posters.PostSearch;

/**
 * Created by Svempa on 2014-01-16.
 */
public class Search extends ActionBarActivity implements View.OnClickListener, EditText.OnEditorActionListener{

    private ActionBar actionBar;
    private View linearLayout;
    private View relativeLayout;
    private ImageView foundUserImage;
    private TextView noResults;
    private Button foundUserButton;
    private EditText searchText;

    private String searchQuery;
    private ProgressDialog progressDialog;
    private HelperClass helperClass;
    private User foundUser;
    float scale = 0;
    int pixelLayout;
    int pixelMargin;
    int imageLayout;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        searchText = (EditText) findViewById(R.id.search_edit_text);
        linearLayout = findViewById(R.id.search);
        relativeLayout = findViewById(R.id.search_container);

        searchText.setOnEditorActionListener(this);
        helperClass = new HelperClass(this);
        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        searchText.setTypeface(tf);


        //Determine screen size
        if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // Extra Large Screen
            scale = this.getResources().getDisplayMetrics().density;
            pixelLayout = (int) (600 * scale + 0.5f);
            pixelMargin = (int) (60 * scale + 0.5f);
            imageLayout = (int) (96 * scale + 0.5f);
            textSize = 70;
        } else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            // Small Size Screen
            scale = this.getResources().getDisplayMetrics().density;
            pixelLayout = (int) (300 * scale + 0.5f);
            pixelMargin = (int) (30 * scale + 0.5f);
            imageLayout = (int) (34 * scale + 0.5f);
            textSize = 20;
        } else {
            // Normal Size Screen
            scale = this.getResources().getDisplayMetrics().density;
            pixelLayout = (int) (300 * scale + 0.5f);
            pixelMargin = (int) (30 * scale + 0.5f);
            imageLayout = (int) (48 * scale + 0.5f);
            textSize = 20;
        }
    }


    private void removeOldResults(){
        // remove text feedback for no results found
        ((LinearLayout) linearLayout).removeView(noResults);
        noResults = null;

        // remove the button with the found user
        ((RelativeLayout) relativeLayout).removeView(foundUserButton);
        ((RelativeLayout) relativeLayout).removeView(foundUserImage);
        foundUserButton = null;
        foundUserImage = null;
    }

    // add text feedback for no results found
    private void addTextView(){
        noResults = new TextView(this);
        noResults.setText("No matches found.");
        noResults.setTextColor(getResources().getColor(R.color.ColorBlack));
        noResults.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        noResults.setTypeface(Typeface.DEFAULT_BOLD);
        noResults.setGravity(Gravity.CENTER);
        noResults.setId(5);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                pixelLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.CENTER;
        params.setMargins(0, pixelMargin, 0 ,0);

        noResults.setLayoutParams(params);
        ((LinearLayout) linearLayout).addView(noResults);
    }

    // add a button with the found user
    private void addUserButton(){
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/hobostd.otf");
        foundUserButton = new Button(this);
        foundUserButton.setOnClickListener(this);
        foundUserButton.setBackgroundResource(R.drawable.button);
        //foundUserButton.getBackground().setAlpha(150);
        foundUserButton.setText(foundUser.getUsername());
        foundUserButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        foundUserButton.setTypeface(null, Typeface.BOLD);
        foundUserButton.setId(4);
        foundUserButton.setGravity(Gravity.CENTER);
        foundUserButton.setTypeface(tf);

        RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
                pixelLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsButton.setMargins(0, pixelMargin, 0 ,0);
        foundUserButton.setLayoutParams(paramsButton);

        ((RelativeLayout) relativeLayout).addView(foundUserButton);


        foundUserImage = new ImageView(this);
        foundUserImage.setImageResource(R.drawable.globe);

        RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
                imageLayout, imageLayout);
        paramsImage.setMargins(0, pixelMargin, 0 ,0);
        foundUserImage.setLayoutParams(paramsImage);

        ((RelativeLayout) relativeLayout).addView(foundUserImage);
    }


    // show loading dialog
    public void showProgressDialog(String message){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, null, message, true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, null, message, true, false);
        }
    }

    // hide loading dialog
    public void hideProgressDialog(){
        // if loading dialog is visible, then hide it
        if(progressDialog != null){
            progressDialog.cancel();
        }
    }

    // method to set found user
    public void finishSearch(User foundUserIn){
        this.foundUser = foundUserIn;

        if(foundUser != null){
            addUserButton();
        } else {
            addTextView();
        }

    }

    // finish request
    public void finishRequest(){
        Toast.makeText(this, "Game request sent!", 1000).show();
    }

    // show error dialog
    public void showErrorDialog(String message){
        progressDialog = null;
        helperClass.showErrorDialog(message);
    }


    private void challengePlayer(){
        if(helperClass.isConnected() != true){
            helperClass.showNetworkErrorDialog();
            // add retry to dialog.
        } else {
            if(foundUser.getUserId() != 0){
                // http request to challenge the user we found with given id
                PostGameStart postGameStart = new PostGameStart(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), foundUser.getUserId(), this);
                postGameStart.postRequest();
            }
        }
    }



    @Override
    public void onClick(View v) {
        if(v == foundUserButton){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Search.this);
            dialog.setTitle("Challenge Player");
            dialog.setIcon(R.drawable.ic_action_help_d);
            dialog.setMessage("Do You want to Challenge " + foundUser.getUsername() + "?\nOr add " + foundUser.getUsername() + " to your Friends?");
            dialog.setPositiveButton("Challenge", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    challengePlayer();
                }
            });
            dialog.setNeutralButton("+ Friend", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
            // challenge found player
            //challengePlayer();
        
    }
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            // remove old results
            removeOldResults();
            // get search string
            searchQuery = searchText.getText().toString();

            // check if query is all whitespaces
            if(searchQuery.matches("^\\s*$")){
                searchQuery = "";
            }

            // trim all trailing whitespaces
            searchQuery = searchQuery.trim();
            // replace all whitespaces in search string with url appropriate char
            searchQuery = searchQuery.replace(" ", "%20");

            if(searchQuery.length() < 1 || searchQuery.length() > 30){
                searchQuery = null;
                addTextView();
            }

            if(User.UserDetails.getUserId() != 0 && User.UserDetails.getIdentifier() != null
                    && searchQuery != null){
                if(!helperClass.isConnected()){
                    helperClass.showNetworkErrorDialog();
                    // add retry to dialog.
                } else {
                    // http request to search for the user
                    PostSearch postSearch = new PostSearch(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), searchQuery, this);
                    postSearch.postQuery();
                }
            }
            return true;
        }

        return false;
    }


}




// TODO if or when we get country code from the server then use it to display the correct flag
