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

import java.util.Locale;

import se.xjobb.scardfeud.JsonGetClasses.Response;
import se.xjobb.scardfeud.Posters.PostGameList;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    String[] menuTitle = {"Play","Rules"};
    int[] menuImage = new int[]
            {R.drawable.ic_play,R.drawable.ic_rules};
    private ProgressDialog progressDialog;
    private boolean isCreated = false;
    HelperClass helperClass = new HelperClass(this);

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

    // called when Async Post is finished
    // Here the json data is processed
    public void finishRequest(String result){
        Log.i("RESULTAT: ", result);

        if(result.contains("invitations")){
            Log.i("invite: ","true");
        }

        if(result.contains("my_turn")){
            Log.i("my_turn: ","true");

        }

        if(result.contains("opponents_turn")){
            Log.i("opponents_turn: ","true");

        }

        if(result.contains("finished")){
            Log.i("finished: ","true");

        }

        JSONObject jsonResponse = null;
        JSONObject jsonMyTurn = null;
        JSONObject jsonInvitations = null;
        JSONObject jsonOpponentsTurn = null;
        JSONObject jsonFinished = null;


        // try to get JSON Response
        try {
             jsonResponse = new JSONObject(result);

        } catch (JSONException ex) {
            Log.e("CardFeud JSON Exception: ", ex.getMessage());
        }

        //JSONObject jsonInvitation = jsonResponse.getJSONObject("invitations");



  /*

        try {
            // try to get all invitations and save as java objects
            //JSONObject jsonInvitation = jsonResponse.getJSONObject("invitations");
            JSONObject jsonFirstInvitation = jsonInvitation.getJSONObject("1");

            Gson gson = new Gson();
            Response response = gson.fromJson(jsonFirstInvitation.toString(), Response.class);
            Log.i("KUK ", response.playerName);

        /*
        Use GSON object for these aswell??

        // parse json string int's to int.
        // how to handle several responses etc...

        // my_turn object looks just as the responses



        } catch (JSONException ex) {
            Log.e("CardFeud JSON Exception: ", ex.getMessage());
        }  /*


        /*
        GameListResponse invitationResponse = gson.fromJson(result, GameListResponse.class);
        List<Response> responses = invitationResponse.responses;

        for(Response invitation : responses){
            Log.i("Test: ", invitation.gameId);
        }  */



        /* {"responses":
            {"1":
                {"game_id":"290215","
                start_time":"2014-01-17 16:24:29","
                finished_time":"0000-00-00 00:00:00","
                lastevent":"2014-01-17 16:24:29","
                lastevent_time":"289h 23m","
                player_1":"51318","
                player_2":"51308","
                player_name":"emiltest","
                opponent_id":"51318","
                opponent_name":"search test","
                card_color":"0","
                card_value":"0","
                pass_prohibited":"0","
                last_round_details":"","
                this_round_details":"","
                last_round_points":"0","
                this_round_points":"0","
                my_turn":0,"
                opponent_points":"0","
                opponent_errors":"0","o
                pponent_wins":0,"
                player_points":"0","
                player_errors":"0","
                player_wins":0,"
                chat_unread":0,"
                odds":""}
            }
          }  */

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