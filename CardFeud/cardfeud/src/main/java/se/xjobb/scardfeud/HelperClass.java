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

    public HelperClass(Context context){
        this.context = context;
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

}
