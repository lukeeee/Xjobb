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
public class PostGameStart {
    private String userId;
    private String opponent;
    private String userIdentifier;
    private String rematch;
    private InvitationResponse invitationResponse;
    private NewGame newGameCallback = null;
    private Search searchCallback = null;
    private MainActivity mainActivityInvitationResponseCallback = null;
    private Game gameCallback = null;

    public PostGameStart(int userId, String userIdentifier, int opponent , NewGame newGameCallback) {
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.opponent = Integer.toString(opponent);
        this.newGameCallback = newGameCallback;
    }

    public PostGameStart(int userId, String userIdentifier, int opponent , Search searchCallback) {
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.opponent = Integer.toString(opponent);
        this.searchCallback = searchCallback;
    }

    public PostGameStart(int userId, String userIdentifier, InvitationResponse invitationResponse,
                         MainActivity mainActivityInvitationResponseCallback) {
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.invitationResponse = invitationResponse;
        this.mainActivityInvitationResponseCallback = mainActivityInvitationResponseCallback;
    }

    public PostGameStart(int userId, String userIdentifier, String opponent, int rematch, Game gameCallback) {
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.opponent = opponent;
        this.rematch = Integer.toString(rematch);
        this.gameCallback = gameCallback;
    }


    public void postRequest(){


        if(newGameCallback != null){
            // if the request is coming from NewGame
            new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamestart&user=" + userId + "&sid=" + userIdentifier +"&opponent=" + opponent + "&res=json");
            newGameCallback.showProgressDialog();

        } else if(searchCallback != null){
            // if the request us coming from Search
            new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamestart&user=" + userId + "&sid=" + userIdentifier +"&opponent=" + opponent + "&res=json");
            searchCallback.showProgressDialog("Loading...");

        } else if(mainActivityInvitationResponseCallback != null){
            // if we are answering game invitation requests
            new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamestart&user=" + userId + "&sid=" + userIdentifier +"&opponent=" + Integer.toString(invitationResponse.getOpponentId()) + "&answer=" + Integer.toString(invitationResponse.getAnswer()) +"&res=json");
            // dialog is already showing...  (MainActivity shows it)
        } else if (gameCallback != null){
            new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamestart&user=" + userId + "&sid=" + userIdentifier +"&opponent=" + opponent + "&rematch=" + rematch +"&res=json");
            gameCallback.showProgressDialog();;
        }
    }


    public static String postGameRequest(String url){
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
            return postGameRequest(urls[0]);
        }

        // Process result for NewGame class
        private void feedBackNewGame(String result, NewGame newGameCallback){
            if(result.contains("Did not work!")){
                newGameCallback.hideProgressDialog();
                newGameCallback.showErrorDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                newGameCallback.hideProgressDialog();
                newGameCallback.showErrorDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                newGameCallback.hideProgressDialog();
                newGameCallback.showErrorDialog("Account Error! Please try logging in again.");
            } else if (result.contains("Something went wrong")){
                newGameCallback.hideProgressDialog();
                newGameCallback.showErrorDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                newGameCallback.hideProgressDialog();
                newGameCallback.showErrorDialog("The server is not responding! Please try again.");
            } else if(result.contains("Request Sent")){
                newGameCallback.hideProgressDialog();
                newGameCallback.finishRequest();
            }
        }


        // process result for Search class
        private void feedBackSearch(String result, Search searchCallback){
            if(result.contains("Did not work!")){
                searchCallback.hideProgressDialog();
                searchCallback.showErrorDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                searchCallback.hideProgressDialog();
                searchCallback.showErrorDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                searchCallback.hideProgressDialog();
                searchCallback.showErrorDialog("Account Error! Please try logging in again.");
            } else if (result.contains("Something went wrong")){
                searchCallback.hideProgressDialog();
                searchCallback.showErrorDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                searchCallback.hideProgressDialog();
                searchCallback.showErrorDialog("The server is not responding! Please try again.");
            } else if(result.contains("Request Sent")){
                searchCallback.hideProgressDialog();
                searchCallback.finishRequest();
            }
        }

        // process result for MainActivity class, when answering game invitation
        private void feedBackMainActivityGameInvitation(String result, MainActivity mainActivityInvitationResponseCallback){
            if(result.contains("Did not work!")){
                mainActivityInvitationResponseCallback.hideProgressDialog();
                mainActivityInvitationResponseCallback.showErrorDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                mainActivityInvitationResponseCallback.hideProgressDialog();
                mainActivityInvitationResponseCallback.showErrorDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                mainActivityInvitationResponseCallback.hideProgressDialog();
                mainActivityInvitationResponseCallback.showErrorDialog("Account Error! Please try logging in again.");
            } else if (result.contains("Something went wrong")){
                mainActivityInvitationResponseCallback.hideProgressDialog();
                mainActivityInvitationResponseCallback.showErrorDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                mainActivityInvitationResponseCallback.hideProgressDialog();
                mainActivityInvitationResponseCallback.showErrorDialog("The server is not responding! Please try again.");
            } else if(result.contains("Request Sent")){
                mainActivityInvitationResponseCallback.getGameLists(true);
            }
        }

        // process result for Game class, when requesting rematch (yes or no)
        private void feedBackGame(String result, Game gameCallback){
            if(result.contains("Did not work!")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorRematchDialog("An error has occurred! Please try again.");
            } else if (result.contains("Server Error")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorRematchDialog("A server error has occurred! Please try again.");
            } else if (result.contains("Wrong account details")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorRematchDialog("Account Error! Please try logging in again.");
            } else if (result.contains("Something went wrong")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorRematchDialog("We are sorry something went wrong! Please try again.");
            } else if (result.contains("Server Timeout")){
                gameCallback.hideProgressDialog();
                gameCallback.showErrorRematchDialog("The server is not responding! Please try again.");
            } else if(result.contains("Request Sent")){
                gameCallback.hideProgressDialog();
                gameCallback.finishRematchRequest();
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result){

            if(newGameCallback != null){
                // process result
                feedBackNewGame(result, newGameCallback);

            } else if (searchCallback != null){
                // process result
                feedBackSearch(result, searchCallback);

            } else if (mainActivityInvitationResponseCallback != null){
                // process result
                feedBackMainActivityGameInvitation(result, mainActivityInvitationResponseCallback);

            } else if (gameCallback != null){
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

