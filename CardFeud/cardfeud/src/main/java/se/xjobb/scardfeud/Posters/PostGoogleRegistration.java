package se.xjobb.scardfeud.Posters;

import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import se.xjobb.scardfeud.Login;
import se.xjobb.scardfeud.User;

/**
 * Created by Svempa on 2014-01-22.
 */
public class PostGoogleRegistration {
    private Login callback;
    private String senderId;
    private GoogleCloudMessaging gcm;

    public PostGoogleRegistration(Login callback, String senderId){
        this.callback = callback;
        this.senderId = senderId;
    }

    public void register(){
        registerInBackground();
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(callback);
                    }
                    User.UserDetails.setDeviceRegId(gcm.register(senderId));
                    result = "Success";

                } catch (IOException ex) {
                    result = "Error";
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.contains("Error")){
                    callback.hideProgressDialog();
                    callback.showErrorDialog("A error has occurred! Please try again.");
                } else if (result.contains("Success")){
                    // if the registration was successful
                    callback.hideProgressDialog();
                    callback.registerDevice();
                } else {
                    callback.hideProgressDialog();
                    callback.showErrorDialog("A error has occurred! Please try again.");
                }
            }
        }.execute(null, null, null);
    }
}
