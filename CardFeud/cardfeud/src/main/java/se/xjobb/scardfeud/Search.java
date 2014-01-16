package se.xjobb.scardfeud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import se.xjobb.scardfeud.Posters.PostSearch;

/**
 * Created by Svempa on 2014-01-16.
 */
public class Search extends Activity implements View.OnClickListener, EditText.OnEditorActionListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        searchText = (EditText) findViewById(R.id.search_edit_text);
        linearLayout = findViewById(R.id.search);
        relativeLayout = findViewById(R.id.search_container);

        searchText.setOnEditorActionListener(this);
        helperClass = new HelperClass(this);
        scale = this.getResources().getDisplayMetrics().density;
        pixelLayout = (int) (300 * scale + 0.5f);
        pixelMargin = (int) (30 * scale + 0.5f);
        imageLayout = (int) (48 * scale + 0.5f);
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
        foundUserButton = new Button(this);
        foundUserButton.setOnClickListener(this);
        foundUserButton.setBackgroundResource(R.drawable.button);
        foundUserButton.getBackground().setAlpha(150);
        foundUserButton.setText(foundUser.getUsername());
        foundUserButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        foundUserButton.setTypeface(null, Typeface.BOLD);
        foundUserButton.setId(4);
        foundUserButton.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
                pixelLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsButton.setMargins(0, pixelMargin, 0 ,0);
        foundUserButton.setLayoutParams(paramsButton);

        ((RelativeLayout) relativeLayout).addView(foundUserButton);


        foundUserImage = new ImageView(this);
        foundUserImage.setImageResource(R.drawable.se);

        RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
                imageLayout, imageLayout);
        paramsImage.setMargins(0, pixelMargin, 0 ,0);
        foundUserImage.setLayoutParams(paramsImage);

        ((RelativeLayout) relativeLayout).addView(foundUserImage);
    }


    // show loading dialog
    public void showProgressDialog(){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, null, "Searching...", true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, null, "Searching...", true, false);
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
    public void finishActivity(User foundUserIn){
        this.foundUser = foundUserIn;

        if(foundUser != null){
            addUserButton();
        } else {
            addTextView();
        }

    }

    // show error dialog
    public void showErrorDialog(String message){
        progressDialog = null;
        helperClass.showErrorDialog(message);
    }


    @Override
    public void onClick(View v) {
        if(v == foundUserButton){
            // start game vs that user here
            Log.i("Button Clicked: ", "Should start a game here");
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // remove old results
            removeOldResults();
            // get search string
            searchQuery = searchText.getText().toString();
            searchQuery.replaceAll("\\s+", "");

            if(searchQuery.length() < 1 || searchQuery.length() > 30){
                searchQuery = null;
                addTextView();
            }

            if(User.UserDetails.getUserId() != 0 && User.UserDetails.getIdentifier() != null
                    && searchQuery != null){
                if(helperClass.isConnected() != true){
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

// TODO add function to the button so we start a new game vs player onclick
