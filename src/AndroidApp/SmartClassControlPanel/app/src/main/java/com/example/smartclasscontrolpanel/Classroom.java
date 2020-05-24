package com.example.smartclasscontrolpanel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.smartclasscontrolpanel.Physical_stats;

public class Classroom implements Parcelable {
    private String Name,light_states;
    private boolean automate = true,swing=true,ac_power=true,greenboardlights=false,audiencelights=false,projector_power=true;
    private int temp = 23, ac_cur_mode = 0,cur_projector_input=0,cur_projector_ratio=0;
    private int cur_occupancy = 170;
    final int  max_temp = 26, min_temp = 16, ac_modes_count = 3,max_inputs=3,max_ratios=3;
    private int max_occupancy = 315, occupancy_step = 10;
    final String[] ac_mode_list = {"DRY", "COOL", "FAN"},input_list={"HDMI1","HDMI2","VGA"},ratio_list={"16:9","4:3","3:2"};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(light_states);
        dest.writeBoolean(automate);
        dest.writeBoolean(swing);
        dest.writeBoolean(ac_power);
        dest.writeBoolean(greenboardlights);
        dest.writeBoolean(audiencelights);
        dest.writeBoolean(projector_power);
        dest.writeInt(temp);
        dest.writeInt(ac_cur_mode);
        dest.writeInt(cur_projector_input);
        dest.writeInt(cur_projector_ratio);
        dest.writeInt(cur_occupancy);
        dest.writeInt(max_occupancy);
        dest.writeInt(occupancy_step);

    }
    public static final Parcelable.Creator<Classroom> CREATOR
            = new Parcelable.Creator<Classroom>() {
        public Classroom createFromParcel(Parcel in) {
            return new Classroom(in);
        }

        public Classroom[] newArray(int size) {
            return new Classroom[size];
        }
    };


    public Classroom(Parcel source){
        this.Name=source.readString();
        this.light_states=source.readString();
        this.automate=source.readBoolean();
        this.swing=source.readBoolean();
        this.ac_power=source.readBoolean();
        this.greenboardlights=source.readBoolean();
        this.audiencelights=source.readBoolean();
        this.projector_power=source.readBoolean();
        this.temp=source.readInt();
        this.ac_cur_mode=source.readInt();
        this.cur_projector_input=source.readInt();
        this.cur_projector_ratio=source.readInt();
        this.cur_occupancy=source.readInt();
        this.max_occupancy=source.readInt();
        this.occupancy_step=source.readInt();
    }

    public String getcur_mode() {
        return ac_mode_list[ac_cur_mode];
    }

    public enum AC_MODES{DRY,COOL,FAN}
    public Physical_stats physicalStats;
    public Classroom(String Name,int max_occupancy,int occupancy_step){
        this.Name=Name;
        this.max_occupancy=max_occupancy;
        this.occupancy_step=occupancy_step;
        light_states=DeviceController.getlights();
    }

    public void setCur_occupancy(int new_occupancy){
        this.cur_occupancy=new_occupancy;
    }

    public int getCur_occupancy(){
        return cur_occupancy;
    }
    public int inc_occupancy(){
        if(cur_occupancy+occupancy_step<max_occupancy)
            cur_occupancy+=occupancy_step;
        else
            cur_occupancy=max_occupancy;
        return cur_occupancy;
    }
    public int dec_occupancy(){
        if(cur_occupancy-occupancy_step>0)
            cur_occupancy-=occupancy_step;
        else
            cur_occupancy=0;
        return cur_occupancy;
    }
    public int getTemp(){
        return temp;
    }
    public int inc_temp(){
        if(temp<max_temp)
            temp++;
        else
            temp=max_temp;
        DeviceController.setACtemp(temp);
        return temp;
    }
    public int dec_temp(){
        if(temp>min_temp)
            temp--;
        else
            temp=min_temp;
        DeviceController.setACtemp(temp);
        return temp;
    }
    public String changeMode(){
        ac_cur_mode=(ac_cur_mode+1)%ac_modes_count;
        DeviceController.setACmode(ac_mode_list[ac_cur_mode]);
        return ac_mode_list[ac_cur_mode];
    }
    public void toggleSwing(){
        swing=!swing;
        if (swing) {
            DeviceController.setACswing("ON");
        } else {
            DeviceController.setACswing("OFF");
        }
    }
    public void toggleACPower(){
        ac_power=!ac_power;
        if (ac_power) {
            DeviceController.setACpower("ON");
        } else {
            DeviceController.setACpower("OFF");
        }
    }
    public void setLights(String light_states)
    {
        this.light_states=light_states;
        DeviceController.setlights(light_states);
    }
    public String getLights(){
        return light_states;
    }
    public void toggleGreenBoardLights()
    {
        greenboardlights=!greenboardlights;
        StringBuilder new_state=new StringBuilder(light_states);
        for(int i='P'-'A';i<new_state.length();i++)
        {
            if(greenboardlights)
                new_state.setCharAt(i,'0');
            else
                new_state.setCharAt(i,'1');
        }
        light_states=new_state.toString();
        DeviceController.setlights(light_states);
    }
    public void toggleAudienceLights()
    {
        audiencelights=!audiencelights;
        StringBuilder new_state=new StringBuilder(light_states);
        for(int i=0;i<=('L'-'A');i++)
        {
            if(audiencelights)
                new_state.setCharAt(i,'0');
            else
                new_state.setCharAt(i,'1');
        }
        light_states=new_state.toString();
        Log.e("toggleAudienceLights: ",light_states);
        DeviceController.setlights(light_states);
    }
    public void toggleProjectorPower(){
        projector_power=!projector_power;
        if (projector_power) {
            DeviceController.setProjectorpower("ON");
        } else {
            DeviceController.setProjectorpower("OFF");
        }
    }
    public String changeProjectorInput(){
        cur_projector_input=(cur_projector_input+1)%max_inputs;
        DeviceController.setProjectorInput(input_list[cur_projector_input]);
        return input_list[cur_projector_input];
    }
    public String changeProjectorAspectRatio(){
        cur_projector_ratio=(cur_projector_ratio+1)%max_ratios;
        DeviceController.setProjectorAspectRatio(ratio_list[cur_projector_ratio]);
        return ratio_list[cur_projector_ratio];
    }
    public int getMax_occupancy(){return max_occupancy;}
    public int getCurOccupancy(){return cur_occupancy;}
    public String getClassName()
    {
        return Name;
    }

}
