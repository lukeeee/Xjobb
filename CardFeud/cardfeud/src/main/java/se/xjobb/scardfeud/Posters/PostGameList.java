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

import se.xjobb.scardfeud.Game;
import se.xjobb.scardfeud.MainActivity;
import se.xjobb.scardfeud.NewGame;
import se.xjobb.scardfeud.Search;

/**
 * Created by Svempa on 2014-01-29.
 */
public class PostGameList {
    private String userId;
    private String userIdentifier;
    private MainActivity callback;
    private Game gameCallback;
    private boolean refresh = false;
    private boolean gameOver = false;
    private boolean gamePlayRefresh = false;

    public PostGameList(int userId, String userIdentifier, MainActivity callback){
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.callback = callback;
    }

    public PostGameList(int userId, String userIdentifier, MainActivity callback, boolean refresh){
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.callback = callback;
        this.refresh = refresh;
    }

    // if we are refreshing from "Game"
    public PostGameList(int userId, String userIdentifier, Game gameCallback, boolean refresh, boolean gameOver, boolean gamePlayRefresh){
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.refresh = refresh;
        this.gameCallback = gameCallback;
        this.gameOver = gameOver;
        this.gamePlayRefresh = gamePlayRefresh;
    }




    public void postRequest(){

        if(!refresh){
            new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamelist&user=" + userId + "&sid=" + userIdentifier + "&res=json");
            callback.showProgressDialog();

        } else {
            new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamelist&user=" + userId + "&sid=" + userIdentifier + "&res=json");
            // we are refreshing after posting responses to game invites and feedback is already visible
        }

    }


    public static String postGameRequest(String url){
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
            return postGameRequest(urls[0]);
        }


        private void feedBackMain(String result){
            if(result.contains("Did not work!")){
                callback.hideProgressDialog();
                callback.showErrorDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                callback.hideProgressDialog();
                callback.showErrorDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                callback.hideProgressDialog();
                callback.showErrorDialog("Account Error! Please try logging in again.");
            } else if (result.contains("Something went wrong")){
                callback.hideProgressDialog();
                callback.showErrorDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                callback.hideProgressDialog();
                callback.showErrorDialog("The server is not responding! Please try again.");
            } else {
                callback.hideProgressDialog();
                callback.saveResult(result);
                callback.finishRequest(result);
            }
        }

        private void feedBackGame(String result){
            if(result.contains("Did not work!")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorGameListDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorGameListDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorGameListDialog("Account Error! Please try logging in again.");
            } else if (result.contains("Something went wrong")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorGameListDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorGameListDialog("The server is not responding! Please try again.");
            } else {

                if(!gameOver && !gamePlayRefresh){
                    // game is in progress
                    gameCallback.hideProgressDialog();
                    gameCallback.saveResult(result);
                    gameCallback.animate();
                } else if(gamePlayRefresh) {
                    // we are refreshing in gamePlay
                    gameCallback.hideProgressDialog();
                    gameCallback.saveResult(result);
                    gameCallback.enableGamePlay();
                } else {
                    // the game is finished
                    gameCallback.hideProgressDialog();
                    gameCallback.saveResult(result);
                    gameCallback.finishRematchRequest();
                }
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result){
            if(callback != null){
                // if the request was from "MainActivity"
                feedBackMain(result);
            } else if(gameCallback != null){
                // if the request was from "Game"
                feedBackGame(result);
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

