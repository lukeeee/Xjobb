package se.xjobb.scardfeud.Posters;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import se.xjobb.scardfeud.Login;
import se.xjobb.scardfeud.User;

/**
 * Created by Svempa on 2014-01-09.
 */
public class PostDeviceId {
    private String userId;
    private String userIdentifier;
    private String deviceId;
    private Login callback;

    public PostDeviceId(int userId, String userIdentifier, String deviceId, Login callback){
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.deviceId = deviceId;
        this.callback = callback;
    }

    public void postRequest(){
        new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=set_device_id&user=" + userId + "&sid=" + userIdentifier + "&device_id=" + deviceId + "&res=json");
    }


    public static String postDeviceId(String url){
        InputStream inputStream;
        String result;
        try {

            // timeout parameters
            HttpParams httpParams = new BasicHttpParams();
            int timeoutConnection = 4000;
            int timeoutSocket = 6000;
            HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpGet = new HttpGet(url);

            int statusCode;

            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            statusCode = httpResponse.getStatusLine().getStatusCode();


            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }

            // if the request was sent
            if(statusCode == 200){
                result = "Request Sent";
            }

            // if there is a server error
            if(statusCode == 500){
                result = "Server Error";
            }

            // wrong combination of userId and identifier
            if(statusCode == 403){
                result = "Wrong account details";
            }

            // to much data sent in
            if(statusCode == 400){
                result = "Something went wrong";
            }

        } catch (ConnectTimeoutException ex) {
            result = "Server Timeout";
        } catch (SocketTimeoutException ex) {
            result = "Server Timeout";
        } catch (Exception e) {
            result = "Did not work!";
        }

        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return postDeviceId(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result){

            if(result.contains("Did not work!")){
                callback.hideProgressDialog();
                callback.showErrorDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                callback.hideProgressDialog();
                callback.showErrorDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                callback.hideProgressDialog();
                callback.showErrorDialog("Account Error! Please try logging in again.");
            } else if(result.contains("Something went wrong")){
                callback.hideProgressDialog();
                callback.showErrorDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                callback.hideProgressDialog();
                callback.showErrorDialog("The server is not responding! Please try again.");
            } else if(result.contains("Request Sent")){
                callback.hideProgressDialog();
                callback.finishActivity(User.UserDetails.getUsername() + " logged in successfully!");
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}