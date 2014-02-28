package se.xjobb.scardfeud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Svempa on 2014-01-08.
 */
public class HelperClass {

    private Context context;

    private final String KEY = "xoD98a5sCdh5e4g3s23XE7d5PfV9";
    private final String PREFSUSERID = "cardFeudUserId";
    private final String PREFSIDENTIFIER = "cardFeudIdentifier";
    private final String PREFSDEVICEREGID = "cardFeudDeviceId";
    private final String PREFSUSERNAME = "cardFeudUsername";
    private final String PREFSCOUNTRYCODE = "cardFeudCountryCode";
    private final String PREFSAPPVERSION = "cardFeudAppVersion";
    private final String PREFSSOUND = "cardFeudSound";
    private final String PREFNOTIFICATIONSSOUND = "cardFeudNotificationSound";
    private final String PREFSVIBRATION = "cardFeudVibration";
    private final String PREFSRESULT = "cardFeudResult";
    private final String PREFSRATE = "cardFeudHasRated";
    private String projectNumber = "155115390012";
    // Constructor to set Context
    public HelperClass(Context context){
        this.context = context;
    }

    public String getPrefsUserId(){
        return this.PREFSUSERID;
    }

    public String getPrefsIdentifier(){
        return this.PREFSIDENTIFIER;
    }

    public String getPrefsUsername(){
        return this.PREFSUSERNAME;
    }

    public String getPrefsDeviceRegId(){
        return this.PREFSDEVICEREGID;
    }

    public String getPrefsCountryCode(){
        return this.PREFSCOUNTRYCODE;
    }

    public String getPrefsAppVersion() {
        return this.PREFSAPPVERSION;
    }

    public String getProjectNumber() {
        return this.projectNumber;
    }

    public String getPrefsSound(){
        return this.PREFSSOUND;
    }

    public String getPrefsVibration() {
        return this.PREFSVIBRATION;
    }

    public String getPrefsNotificationSound() {
        return this.PREFNOTIFICATIONSSOUND;
    }

    public String getPrefsResult() { return this.PREFSRESULT; }

    public String getPrefsRate() { return this.PREFSRATE; }

    public String getKey(){
        return this.KEY;
    }

    public boolean isConnected(){

        ConnectivityManager connMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }

    }

    // show the error dialog
    public void showNetworkErrorDialog(){
        // if an error occurred

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("You need internet for the app to work!");
        builder.setMessage("Please turn on your internet connection!");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    // show the error dialog
    public void showErrorDialog(String message){
        // if an error occurred

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("An Error has occurred!");
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
