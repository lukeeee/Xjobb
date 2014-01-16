package se.xjobb.scardfeud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import se.xjobb.scardfeud.Posters.PostSearch;

/**
 * Created by Svempa on 2014-01-16.
 */
public class Search extends Activity implements View.OnClickListener{

    private Button searchButton;

    private String searchQuery;
    private ProgressDialog progressDialog;
    private HelperClass helperClass;
    private User foundUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(this);

        searchQuery = "Lukas";
        helperClass = new HelperClass(this);
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
    public void finishActivity(User foundUser){
        this.foundUser = foundUser;
        Toast.makeText(this, foundUser.getUsername(), 1000).show();
    }

    // show error dialog
    public void showErrorDialog(String message){
        progressDialog = null;
        helperClass.showErrorDialog(message);
    }


    @Override
    public void onClick(View v) {
        if(v == searchButton){

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
        }
    }
}
