package com.example.UbonGo.controller;

import android.graphics.Canvas;
import com.example.UbonGo.GameAudio;
import com.example.UbonGo.Main;
import com.example.UbonGo.R;
import com.example.UbonGo.model.GeneralSettings;
import com.example.UbonGo.view.MainMenuView;
import com.example.UbonGo.view.OptionsView;
import com.example.UbonGo.view.View;
import sheep.game.State;


/**
 * Created by Sindre on 17.03.2016.
 */

public class MenuController extends State{
    private View view;
    private Main main;
    private GeneralSettings model;

    /**
     *Constructor that initializes
     * @param the main activity the controller is active state in
     */
    public MenuController(Main main){
        view =new MainMenuView(this);
        this.main=main;
        model=new GeneralSettings();
        GameAudio.getInstance().playMusic(R.raw.ubongo);
        GameAudio.getInstance().setVolume(model.getVolume());
    }

    /**
     *Method implemented because controller is extending State from the Sheep library. For each update the volume is checked to adjust the volume for the music.
     */
    public void update(float dt){
        GameAudio.getInstance().setVolume(model.getVolume());
    }

    public void draw(Canvas canvas){
        view.drawComponents(canvas);

    }
    /**
     * Navigates to the lobby by setting the LobbyController as the new state
     */
    public void btnStartGameClicked(){
        main.changeMainController(new LobbyController(main));
    }

    /**
     *Method called when the options-button in the MainMenuView is clicked
     */
    public void btnOptionsClicked(){
        this.view=new OptionsView(this);
        ((OptionsView) view).changeVolumeText(model.getVolume()+"");
    }

    /**
     *Method called when the back-button in the OptionsView is clicked
     */
    public void btnBackToMainClicked(){
        this.view=new MainMenuView(this);
    }


    /**
     *Method called when the volume-up-button in the OptionsView is clicked
     */
    public void btnVolumeUpClicked(){
        try {
            model.setVolume(model.getVolume() + 5);
            ((OptionsView) view).changeVolumeText(model.getVolume()+"");
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    /**
     *Method called when the volume-down-button in the OptionsView is clicked
     */
    public void btnVolumeDownClicked(){
        try {
            model.setVolume(model.getVolume() - 5);
            ((OptionsView) view).changeVolumeText(model.getVolume() + "");
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }








}
