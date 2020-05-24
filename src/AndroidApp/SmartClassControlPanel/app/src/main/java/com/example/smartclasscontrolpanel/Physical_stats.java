package com.example.smartclasscontrolpanel;

import java.lang.Cloneable;

public class Physical_stats implements Cloneable {
    public float indoor_temp,outdoor_temp,indoor_humidity,outdoor_humidity,CO2_conc,power_consumption;
    public Physical_stats(float indoor_temp,float outdoor_temp,float indoor_humidity,float outdoor_humidity,float CO2_conc,float power_consumption)
    {
        this.indoor_temp=indoor_temp;
        this.outdoor_temp=outdoor_temp;
        this.indoor_humidity=indoor_humidity;
        this.outdoor_humidity=outdoor_humidity;
        this.CO2_conc=CO2_conc;
        this.power_consumption=power_consumption;
    }
}
