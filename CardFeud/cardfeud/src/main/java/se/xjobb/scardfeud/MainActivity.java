package se.xjobb.scardfeud;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import se.xjobb.scardfeud.JsonGetClasses.GameListResult;
import se.xjobb.scardfeud.JsonGetClasses.Response;
import se.xjobb.scardfeud.Posters.PostGameList;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    String[] menuTitle = {"Play","Rules"};
    int[] menuImage = new int[]
            {R.drawable.ic_play,R.drawable.ic_rules};
    private ProgressDialog progressDialog;
    private boolean isCreated = false;
    HelperClass helperClass = new HelperClass(this);
    private final String TAG = "CardFeud JSON Exception: ";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            //.setIcon(menuImage[i])
                            .setTabListener(this));
        }

        checkUserDetails();
        getGameLists();
        isCreated = true;

    }

    // check user details
    private void checkUserDetails(){
        // check if user is logged out, (if the values stored are empty)
        // if they are then finish activity

        if(User.UserDetails.getUsername() == null && User.UserDetails.getIdentifier() == null){
            this.finish();
        }
    }

    // get all objects of a certain type in the json response and save them to a list
    private List<Response> getResponses(JSONObject serverResponse, String objectName){
        List<Response> responses = new ArrayList<Response>();
        JSONObject jsonObject;
        try{
            final Gson gson = new Gson();
            jsonObject = serverResponse.getJSONObject(objectName);
            final Iterator<String> keys = jsonObject.keys();

            // add all responses to the list as Response objects
            while(keys.hasNext()) {
                final String key = keys.next();
                responses.add(gson.fromJson(jsonObject.getJSONObject(key).toString(), Response.class));
            }

        } catch (JSONException ex){
            Log.e(TAG, ex.getMessage());
        }

        return responses;
    }

    // called when Async Post is finished
    // Here where the json data is processed
    public void finishRequest(String result){
        Log.i("RESULTAT: ", result);

        JSONObject jsonResponse = null;

        // try to get JSON Response
        try {
            jsonResponse = new JSONObject(result);

        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }

        // check which JSON objects that are present
        if(jsonResponse != null){

            // if there is invitations present in response
            if(!jsonResponse.isNull("invitations")){
                Log.i("invite: ","true");

                // get and store all invitation response objects in a static list
                GameListResult.setInvitations(getResponses(jsonResponse, "invitations"));


                for(Response response : GameListResult.getInvitations()){
                    Log.i("invitation: ", response.opponentName);
                }
            }

            if(!jsonResponse.isNull("my_turn")){
                Log.i("my_turn: ","true");

                // get and store all my_turn response objects in a static list
                GameListResult.setMyTurns(getResponses(jsonResponse, "my_turn"));
            }

            if(!jsonResponse.isNull("opponents_turn")){
                Log.i("opponents_turn: ","true");

                // get and store all opponents_turn response objects in a static list
                GameListResult.setOpponentsTurns(getResponses(jsonResponse, "opponents_turn"));
            }

            if(!jsonResponse.isNull("finished")){
                Log.i("finished: ","true");

                // get and store all finished response objects in a static list
                GameListResult.setFinishedGames(getResponses(jsonResponse, "finished"));
            }
        }
    }

    // used to get current games/invites to games
    private void getGameLists(){

        if(!helperClass.isConnected()){
            helperClass.showNetworkErrorDialog();
            // add retry dialog
        } else {
            // post to get the game responses/current games from server
            PostGameList postGameList = new PostGameList(User.UserDetails.getUserId(), User.UserDetails.getIdentifier(), this);
            postGameList.postRequest();
        }
    }

    public void showProgressDialog(){
        if(progressDialog == null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, null,"Loading...", true, false);
        } else {
            progressDialog.cancel();
            progressDialog = ProgressDialog.show(this, null,"Loading...", true, false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(!isCreated){
            // if onCreate was not called
            checkUserDetails();
            //getGameLists();  If we want to search for new games each time we re-enter activity
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        isCreated = false;
    }

    @Override
    protected void onStop(){
        super.onStop();
        isCreated = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getBaseContext(), AppSettings.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_stats:
                Intent statsIntent = new Intent(getBaseContext(), Stats.class);
                startActivity(statsIntent);
                return true;
            case R.id.action_refresh:
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                return true;
        }

        return true;

        }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            switch (position) {
                case 0:
                    return new Start();
                case 1:
                    return new Rules();
                default:
                    return new Start();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_activity_start).toUpperCase(l);
                case 1:
                    return getString(R.string.title_activity_rules).toUpperCase(l);
            }
            return null;
        }
    }

}