package com.example.smartclasscontrolpanel;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;

class NetworkController extends AsyncTask<String, Void, String> {
    public MainActivity activity;
    private String LOG_TAG =MainActivity.class.getSimpleName();
    String URL_STRING="http://backend105.herokuapp.com/projector/1/OFF";


    @Override
    protected String doInBackground(String ... parameters) {
        String Method=parameters[0];
        String URL_string=parameters[1];
        if(Method.compareToIgnoreCase("GET")==0) {
            // Create URL object
            URL url = createUrl(URL_string);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpGETRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "message", e);
            }
            StringBuilder retResponse=new StringBuilder(jsonResponse);
            Log.e("JSON RESPONSE",retResponse.toString());
            return retResponse.toString();
        }
        else if (Method.compareToIgnoreCase("POST")==0)
        {
            return (makeHttpPOSTRequest(URL_string,parameters[2]));
        }
        return "204";
    }



    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpGETRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private String makeHttpPOSTRequest(String url,String parameters) {
        URL urlObj = null;
        OutputStreamWriter writer;
        HttpURLConnection httpCon;
        int responsecode=204;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }
        try{
            httpCon = (HttpURLConnection) urlObj.openConnection();

            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            writer = new OutputStreamWriter(
                    httpCon.getOutputStream());
            writer.write(parameters);
            writer.flush();
            responsecode=httpCon.getResponseCode();

        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"IOEXCEPTION IN POST",e);
        }
        Log.e(LOG_TAG,"POST CHALTA H"+Integer.toString(responsecode));
        return Integer.toString(responsecode);
    }

    /**
     * Return an {@link Physical_stats} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */


}
