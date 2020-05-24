package com.example.smartclasscontrolpanel;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.ScatteringByteChannel;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DeviceController {
    static final String BaseURL="http://backend105.herokuapp.com";
    public static void setACtemp(int temp)
    {
        String url=BaseURL+"/ac1/set_temp/"+Integer.toString(temp);
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static void setACmode(String mode)
    {
        String url=BaseURL+"/ac1/set_mode/"+mode;
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static void setACswing(String state)
    {
        String url=BaseURL+"/ac1/set_swing/"+state;
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static void setACpower(String state)
    {
        String url=BaseURL+"/ac1/power/"+state;
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static void setlights(String state)
    {
        String url=BaseURL+"/lights/activate",light_states="current_config="+state;
        String[] params={"POST",url,light_states};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static String getlights()
    {
        String url=BaseURL+"/lights/current_config",light_states="1111111111111111111";
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        String lightJSON="";
        try {
        lightJSON = nt.execute(params).get().toString();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();}

        try {
            JSONObject baseJsonResponse = new JSONObject(lightJSON);
//            light_states=baseJsonResponse.getString("current_config");

            // If there are results in the features array
            if (baseJsonResponse.length() > 0) {
                // Extract out the first feature (which is an earthquake)
                light_states=baseJsonResponse.getString("current_config");
                Log.e("JSON Lights",light_states);
                return light_states;
            }
        } catch (JSONException e) {
            Log.e("Parse Error", "Problem parsing the JSON results", e);
        }
        return light_states;
    }
    public static void setProjectorpower(String state)
    {
        String url=BaseURL+"/projector1/power/"+state;
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static void setProjectorInput(String mode)
    {
        String url=BaseURL+"/projector1/input/"+mode;
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
    public static void setProjectorAspectRatio(String ratio)
    {
        String url=BaseURL+"/projector1/aspectRatio/"+ratio;
        String[] params={"GET",url};
        Log.e(params[0],params[1]);
        NetworkController nt = new NetworkController();
        Log.e("ReponseCode", String.valueOf(nt.execute(params)));
    }
}
