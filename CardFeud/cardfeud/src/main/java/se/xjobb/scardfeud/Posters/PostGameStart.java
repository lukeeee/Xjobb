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

import se.xjobb.scardfeud.NewGame;
import se.xjobb.scardfeud.Search;

/**
 * Created by Svempa on 2014-01-17.
 */
public class PostGameStart {
    private String userId;
    private String opponent;
    private String userIdentifier;
    private NewGame newGameCallback = null;
    private Search searchCallback = null;

    public PostGameStart(int userId, String userIdentifier, int opponent , NewGame newGameCallback){
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.opponent = Integer.toString(opponent);
        this.newGameCallback = newGameCallback;
    }

    public PostGameStart(int userId, String userIdentifier, int opponent , Search searchCallback){
        this.userId = Integer.toString(userId);
        this.userIdentifier = userIdentifier;
        this.opponent = Integer.toString(opponent);
        this.searchCallback = searchCallback;
    }


    public void postRequest(){

        new HttpAsyncTask().execute("http://dev.cardfeud.com/app/index.php?f=gamestart&user=" + userId + "&sid=" + userIdentifier +"&opponent=" + opponent + "&res=json");

        if(newGameCallback != null){
            // if the request is coming from NewGame
            newGameCallback.showProgressDialog();

        } else if(searchCallback != null){
            // if the request us coming from Search
            searchCallback.showProgressDialog("Loading...");
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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result){

            if(newGameCallback != null){
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
            } else if (searchCallback != null){
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

