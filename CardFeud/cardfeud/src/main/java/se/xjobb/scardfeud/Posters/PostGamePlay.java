package se.xjobb.scardfeud.Posters;

import android.app.IntentService;
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

import se.xjobb.scardfeud.Game;
import se.xjobb.scardfeud.InvitationResponse;
import se.xjobb.scardfeud.MainActivity;
import se.xjobb.scardfeud.NewGame;
import se.xjobb.scardfeud.Search;

/**
 * Created by Svempa on 2014-01-17.
 */
public class PostGamePlay {
    private String userId;
    private String userIdentifier;
    private String gameId;
    private String choice;   //1 = higher, 2 = lower, 3 = pass, 0 = get game data
    private Game gameCallback = null;

    public PostGamePlay(int userId, String userIdentifier, String gameId, int choice , Game gameCallback) {
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.gameId = gameId;
        this.choice = Integer.toString(choice);
        this.gameCallback = gameCallback;
    }


    public void postRequest(){

        if(gameCallback != null){
            // if the request is coming from Game
            gameCallback.showProgressDialog();
        }

        new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gameplay&user=" + userId + "&sid=" + userIdentifier + "&gameid=" + gameId +"&choice=" + choice + "&res=json");
    }


    public static String postGamePlayRequest(String url){
        InputStream inputStream;
        String result;
        try {

            // timeout parameters
            HttpParams httpParams = new BasicHttpParams();
            int timeoutConnection = 3000;
            int timeoutSocket = 5000;
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
            Log.e("Exception Timeout: ", ex.getMessage());
        } catch (SocketTimeoutException ex) {
            result = "Server Timeout";
            Log.e("Exception Timeout: ", ex.getMessage());
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "Did not work!";
        }

        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return postGamePlayRequest(urls[0]);
        }

        // process result
        private void feedBackGame(String result, Game gameCallback){
            if(result.contains("Did not work!")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorDialog("An error has occurred! Please try again.", choice);
            } else if (result.contains("Server Error")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorDialog("A server error has occurred! Please try again.", choice);
            } else if (result.contains("Wrong account details")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorDialog("Account Error! Please try logging in again.", choice);
            } else if (result.contains("Something went wrong")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorDialog("We are sorry something went wrong! Please try again.", choice);
            } else if (result.contains("Server Timeout")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorDialog("The server is not responding! Please try again.", choice);
            } else {
                gameCallback.finishRequest(result);
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result){

            if (gameCallback != null){
                // process result
                feedBackGame(result, gameCallback);

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

