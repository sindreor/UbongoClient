package com.example.UbonGo.controller;

import android.graphics.Canvas;
import android.widget.Button;

import com.example.UbonGo.GameAudio;
import com.example.UbonGo.Main;
import com.example.UbonGo.R;
import com.example.UbonGo.model.GeneralSettings;
import com.example.UbonGo.view.MainMenuView;
import com.example.UbonGo.view.OptionsView;
import com.example.UbonGo.view.View;

import sheep.game.Game;
import sheep.game.State;


/**
 * Created by Sindre on 17.03.2016.
 */

public class MenuController extends State{
    private View view;
    private Main main;
    private GeneralSettings model;

    public MenuController(Main main){
        view =new MainMenuView(this);
        this.main=main;
        model=new GeneralSettings();
        GameAudio.getInstance().playMusic(R.raw.ubongo);
        GameAudio.getInstance().setVolume(model.getVolume());
    }

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


    public void btnOptionsClicked(){
        this.view=new OptionsView(this);
        ((OptionsView) view).changeVolumeText(model.getVolume()+"");
    }

    public void btnBackToMainClicked(){
        this.view=new MainMenuView(this);
    }

    public void btnVolumeUpClicked(){
        try {
            model.setVolume(model.getVolume() + 5);
            ((OptionsView) view).changeVolumeText(model.getVolume()+"");
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

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
