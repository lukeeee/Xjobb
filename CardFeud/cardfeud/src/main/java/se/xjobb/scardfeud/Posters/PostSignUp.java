package se.xjobb.scardfeud.Posters;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import se.xjobb.scardfeud.SignUp;
import se.xjobb.scardfeud.User;

/**
 * Created by Svempa on 2014-01-09.
 */
public class PostSignUp {

    private User user;
    private SignUp callback;

    public PostSignUp(SignUp callback, User user){
        this.user = user;
        this.callback = callback;
    }

    public void postJson(){
        callback.showProgressDialog();
        new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=signup&res=json");
    }


    public static String postSignUp(String url, User user){
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
            HttpPost httpPost = new HttpPost(url);

            int statusCode;


            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("username", user.getUsername()));
            nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
            nameValuePairs.add(new BasicNameValuePair("countrycode", user.getCountryCode()));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();
            statusCode = httpResponse.getStatusLine().getStatusCode();


            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }

            // if there is a server error
            if(statusCode == 500){
                result = "Server Error";
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
            return postSignUp(urls[0], user);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if(result.contains("Did not work!")){
                callback.hideProgressDialog();
                callback.showErrorDialog("A error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                callback.hideProgressDialog();
                callback.showErrorDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Server Timeout")){
                    callback.hideProgressDialog();
                    callback.showErrorDialog("The server is not responding! Please try again.");
            } else {
                int userId = 0;
                String userIdentifier = null;

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    userId = Integer.parseInt(jsonObject.getString("userid"));
                    userIdentifier = jsonObject.getString("identifier");
                } catch (JSONException e) {
                    Log.e("Exception JSON: ", e.getMessage());
                }


                if(userId == -1){
                    // username is taken
                    callback.hideProgressDialog();
                    callback.showFeedbackToast("Username is in use, Please try a different username.");
                } else if (userId != -1 && userId != 0 && userIdentifier != null){
                    // new user added
                    User.UserDetails.setIdentifier(userIdentifier);
                    User.UserDetails.setUserId(userId);
                    callback.hideProgressDialog();
                    callback.finishActivity(User.UserDetails.getUsername() + " registered successfully!");

                }
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


//TODO add retry on server timeout??