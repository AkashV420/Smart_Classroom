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

class PhysicalDataAsyncTask extends AsyncTask<URL, Void, Physical_stats> {
    public MainActivity activity;
    private String LOG_TAG ;
    String URL_STRING="http://backend105.herokuapp.com/classroom/currentStats";

    public PhysicalDataAsyncTask(MainActivity a){
        this.activity=a;
        LOG_TAG= MainActivity.class.getSimpleName();
    }

    @Override
    protected Physical_stats doInBackground(URL... urls) {
        // Create URL object
        URL url = createUrl(URL_STRING);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"message",e);
        }

        // Extract relevant fields from the JSON response and create an {@link Physical_stats} object
        Physical_stats physicalStats = extractFeatureFromJson(jsonResponse);
//        makeHTTPPOSTreq(urlstrng,params);
        // Return the {@link Physical_stats} object as the result fo the {@link TsunamiAsyncTask}
        return physicalStats;
    }

    @Override
    protected void onPostExecute(Physical_stats physicalStats) {
        if (physicalStats == null) {
            return;
        }

        updateUi(physicalStats);
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
    private String makeHttpRequest(URL url) throws IOException {
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

    private int makeHTTPPOSTreq(String url,String parameters) {
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
        Log.e(LOG_TAG,"POST CHALTA H");
        return responsecode;
    }

    /**
     * Return an {@link Physical_stats} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private Physical_stats extractFeatureFromJson(String physicalStatsJSON) {
        try {
            JSONObject baseJsonResponse = new JSONObject(physicalStatsJSON);
            JSONObject mainObject = baseJsonResponse;

            // If there are results in the features array
            if (mainObject.length() > 0) {
                // Extract out the first feature (which is an earthquake)
                Double in_temp = mainObject.getDouble("intemp");
                Double out_temp = mainObject.getDouble("outtemp");
                Double in_humidity = mainObject.getDouble("inhumidity");
                Double out_humidity = mainObject.getDouble("outhumidity");
                Double CO2 = mainObject.getDouble("co2");
                Double power = mainObject.getDouble("power");
                // Create a new {@link Physical_stats} object
                return new Physical_stats(in_temp.floatValue(),out_temp.floatValue(),in_humidity.floatValue(),out_humidity.floatValue(),CO2.floatValue(),power.floatValue());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
    private void updateUi(Physical_stats physicalStats) {
        // Display the indoor_temp in the UI
        TextView intempTextView = (TextView) activity.findViewById(R.id.in_temp);
        intempTextView.setText(Html.fromHtml(String.format("%.2f",physicalStats.indoor_temp)+"<sup>o</sup>C"));
        TextView outTempTextView = (TextView) activity.findViewById(R.id.out_temp);
        outTempTextView.setText(Html.fromHtml(String.format("%.2f",physicalStats.outdoor_temp)+"<sup>o</sup>C"));
        TextView inhumidTextView = (TextView) activity.findViewById(R.id.in_humidity);
        inhumidTextView.setText(Html.fromHtml(String.format("%.1f",physicalStats.indoor_humidity)+"%"));
        TextView outhumidTextView = (TextView) activity.findViewById(R.id.out_humidity);
        outhumidTextView.setText(Html.fromHtml(String.format("%.1f",physicalStats.outdoor_humidity)+"%"));
        TextView CO2TextView = (TextView) activity.findViewById(R.id.co2);
        CO2TextView.setText(Html.fromHtml(Float.toString(physicalStats.CO2_conc)+"ppm"));
        TextView powerconsumptionTextView = (TextView) activity.findViewById(R.id.power_consumption);
        powerconsumptionTextView.setText(Html.fromHtml(Float.toString(physicalStats.power_consumption)+"kWhr"));

    }
}
