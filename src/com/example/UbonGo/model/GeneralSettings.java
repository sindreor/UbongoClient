package com.example.UbonGo.model;

import com.example.UbonGo.GameAudio;

/**
 * Created by Sindre on 17.03.2016.
 */
public class GeneralSettings {
    private float volume;

    public GeneralSettings(){
        volume=70;
    }

    public float getVolume(){
        return volume;
    }
    public void setVolume(float volume){
        if(volume<=100 && volume>=0){
            this.volume=volume;
        }
        else{
            throw new IllegalStateException("Volume can not be more than 100 or less than 0");
        }
    }
}
