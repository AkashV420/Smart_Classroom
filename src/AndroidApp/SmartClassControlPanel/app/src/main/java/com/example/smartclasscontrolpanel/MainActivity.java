package com.example.smartclasscontrolpanel;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

//openweathermap.org
//login: dassteam21
//pass: Team21@h-105
//api-key: 5be8fb425708afe3cbfc003e68de1382
/*
1 row 25 studs
total 12 rows
[0,25,50,75,100,125,150,175,200,225,250,275,300]
[ 1  2  3  4   5   6   7   8   9   10  11  12  ]

[0,25,75,125,175,225,275,315]
[ 1  2  3   4   5   6   7   ]
 */
public class MainActivity extends AppCompatActivity implements SceneDialog.SceneDialogListener, CustomSceneDialog.CustomSceneDialogListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private CardView ACCardView, LightsCardView, ProjectorCardView, OccupancyCardView;
    private Switch ACswitch, Lightswitch, Projectorswitch, Occupancyswitch, automateSwitch;
    boolean automate = true;
    Classroom h105;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_main);
        ACCardView = findViewById(R.id.AC_cardView);
        LightsCardView = findViewById(R.id.Lights_cardView);
        OccupancyCardView = findViewById(R.id.Occupancy_cardView);
        ProjectorCardView = findViewById(R.id.Projector_cardView);
        ACswitch = findViewById(R.id.AC_switch);
        Lightswitch = findViewById(R.id.Lights_switch);
        Occupancyswitch = findViewById(R.id.Occupancy_switch);
        Projectorswitch = findViewById(R.id.Projector_switch);
        automateSwitch = findViewById(R.id.Automation_switch);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e( "onPause: ","Har Bar exit krne pr" );
        SharedPreferences sharedPreferences=getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(h105);
        editor.putString("Classroom_object",json);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume: ","Har bar resume krne pr" );
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Classroom_object", null);
        Type type = new TypeToken<Classroom>() {}.getType();
        h105 = gson.fromJson(json, type);
        if (h105!=null){
            Log.e( "current occupancy :",Integer.toString(h105.getCur_occupancy()) );
        }
        if (h105 == null) {
            h105=new Classroom("H-105",315,10);
        }
        initialize_ui_elements();
    }

    public void initialize_ui_elements(){
        TextView temp_disp = findViewById(R.id.AC_temp);
        temp_disp.setText(Html.fromHtml(Integer.toString(h105.getTemp()) + "<sup>o</sup>C"));
        TextView occupancy_tv=findViewById(R.id.Occupancy_textView);
        occupancy_tv.setText(Html.fromHtml(Integer.toString(h105.getCur_occupancy())+"/"+Integer.toString(h105.getMax_occupancy())));
        PhysicalDataAsyncTask physicalDataAsyncTask=new PhysicalDataAsyncTask(this);
        TextView ac_mode_tv = findViewById(R.id.AC_mode_disp);
        ac_mode_tv.setText(Html.fromHtml(h105.getcur_mode()));
        physicalDataAsyncTask.execute();
        setRepeatingAsyncTask(this);

    }
    public void automateAC(boolean enable) {
        LinearLayout ac_control_layout = findViewById(R.id.AC_control_layout);
        disableEnableControls(!enable,ac_control_layout);
    }

    public void automateProjector(boolean enable) {
        LinearLayout projector_control_layout= findViewById(R.id.Projector_control_layout);
        disableEnableControls(!enable,projector_control_layout);
    }

    public void automateLights(boolean enable) {
        LinearLayout light_control_layout= findViewById(R.id.Lights_control_layout);
        disableEnableControls(!enable,light_control_layout);
    }

    public void automateOccupancy(boolean enable){
        LinearLayout occupancy_control_layout = findViewById(R.id.Occupancy_control_layout);
        disableEnableControls(!enable,occupancy_control_layout);
    }

    public void automateOnClick(View v) {
        //change it to call the above automate methods.
        //when individual automation is done, pass the linear layout not card layout to the disableEnableControls()
        automate = automateSwitch.isChecked();
        ACswitch.setChecked(automate);
        Lightswitch.setChecked(automate);
        Projectorswitch.setChecked(automate);
        Occupancyswitch.setChecked(automate);
        disableEnableControls(!automate, ACCardView);
        disableEnableControls(!automate, LightsCardView);
        disableEnableControls(!automate, ProjectorCardView);
        disableEnableControls(!automate, OccupancyCardView);

    }

    public void AConClick(View v) {
        TextView temp_disp = ACCardView.findViewById(R.id.AC_temp);
        switch (v.getId()) {
            case R.id.AC_temp_inc:
                temp_disp.setText(Html.fromHtml(Integer.toString(h105.inc_temp()) + "<sup>o</sup>C"));
                break;
            case R.id.AC_temp_dec:
                temp_disp.setText(Html.fromHtml(Integer.toString(h105.dec_temp()) + "<sup>o</sup>C"));
                break;
            case R.id.AC_mode:
                TextView ac_mode_disp = ACCardView.findViewById(R.id.AC_mode_disp);
                ac_mode_disp.setText(Html.fromHtml(h105.changeMode()));
                break;
            case R.id.AC_swing:
                h105.toggleSwing();
                Toast.makeText(getApplicationContext(), "Swing Toggled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.AC_switch:
                automateAC(ACswitch.isChecked());
                break;
            case R.id.AC_power:
                h105.toggleACPower();
                Toast.makeText(getApplicationContext(), "Power Toggled", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
        }
    }

    public void ProjectorOnClick(View v) {
        switch (v.getId()) {
            case R.id.Projector_power:
                h105.toggleProjectorPower();
                Toast.makeText(getApplicationContext(), "Power Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Projector_input:
                String input=h105.changeProjectorInput();
                Toast.makeText(getApplicationContext(), "Projector Input : "+ input, Toast.LENGTH_SHORT).show();
                break;
            case R.id.Projector_AspectRatio:
                String ar=h105.changeProjectorAspectRatio();
                Toast.makeText(getApplicationContext(), "Projector Aspect Ratio : "+ar, Toast.LENGTH_SHORT).show();
                break;
            case R.id.Projector_switch:
                automateProjector(Projectorswitch.isChecked());
        }
    }

    public void OccupancyOnClick(View v) {
        TextView occupancy_tv = OccupancyCardView.findViewById(R.id.Occupancy_textView);
        switch (v.getId()) {
            case R.id.Occupancy_inc:
                occupancy_tv.setText(Html.fromHtml(Integer.toString(h105.inc_occupancy()) + "/" + Integer.toString(h105.getMax_occupancy())));
                break;

            case R.id.Occupancy_dec:
                occupancy_tv.setText(Html.fromHtml(Integer.toString(h105.dec_occupancy()) + "/" + Integer.toString(h105.getMax_occupancy())));
                break;
            case R.id.Occupancy_switch:
                automateOccupancy(Occupancyswitch.isChecked());
            default:
                Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
        }
    }

    public void lightOnClick(View v) {
        switch (v.getId()) {
            case R.id.button_sceneSelect:
                Toast.makeText(getApplicationContext(), "Scene Select", Toast.LENGTH_SHORT).show();
                openDialog();
                break;
            case R.id.Lights_Board:
                h105.toggleGreenBoardLights();
                Toast.makeText(getApplicationContext(), "Green Board Focus Lights Toggled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Lights_audience:
                h105.toggleAudienceLights();
                Toast.makeText(getApplicationContext(), "Audience Light Toggled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Lights_switch:
                automateLights(Lightswitch.isChecked());
            default:
                Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialog() {
        SceneDialog sd = new SceneDialog(h105.getLights());
        sd.show(getSupportFragmentManager(), "Scene Dialog");

    }

    @Override
    public void applyScene(String states) {
        h105.setLights(states);
    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }


    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */

    private void setRepeatingAsyncTask(final MainActivity a) {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            PhysicalDataAsyncTask physicalDataAsyncTask=new PhysicalDataAsyncTask(a);
                            physicalDataAsyncTask.execute();
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"REPEATING ASYNC TASK",e);
                        }
                    }
                });
            }
        };

        // interval of one minute
        timer.schedule(task, 0, 15*1000);

    }
}
