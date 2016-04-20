package com.example.UbonGo.view;

import android.graphics.Canvas;

import com.example.UbonGo.DisplayElements;
import com.example.UbonGo.R;
import com.example.UbonGo.controller.MenuController;
import sheep.graphics.Image;
import sheep.gui.TextButton;
import sheep.gui.WidgetAction;
import sheep.gui.WidgetListener;

/**
 * Created by Sindre on 17.03.2016.
 */
public class MainMenuView implements WidgetListener, View{

    private MenuController controller;
    private TextButton btnStartGame;
    private Image background;
    private Image backgroundLogo;
    private TextButton btnOptions;

    public MainMenuView(MenuController controller){
        this.controller=controller;
        background=DisplayElements.getInstance().getBackground();
        backgroundLogo=DisplayElements.getInstance().getGameLogo();


        //Start Game-button
        btnStartGame = new TextButton(DisplayElements.getInstance().getWidth()*0.15f, DisplayElements.getInstance().getHeight()*0.75f, "Start game", DisplayElements.getInstance().getButtonFont(DisplayElements.getInstance().getHeight()));
        controller.addTouchListener(btnStartGame);
        btnStartGame.addWidgetListener(this);

        //Options-button
        btnOptions=new TextButton(DisplayElements.getInstance().getWidth()*0.5f, DisplayElements.getInstance().getHeight()*0.75f, "Options", DisplayElements.getInstance().getButtonFont(DisplayElements.getInstance().getHeight()));
        controller.addTouchListener(btnOptions);
        btnOptions.addWidgetListener(this);
    }

    public void drawComponents(Canvas canvas){
        background.draw(canvas,0,0);
        backgroundLogo.draw(canvas,(DisplayElements.getInstance().getWidth()-backgroundLogo.getWidth())*0.5f,(DisplayElements.getInstance().getHeight())*0.15f);
        btnStartGame.draw(canvas);
        btnOptions.draw(canvas);

    }
    public void actionPerformed(WidgetAction action) {
        if (action.getSource() == btnStartGame) {
            controller.btnStartGameClicked(); //Navigates to the lobby
        }
        else if(action.getSource() ==btnOptions){
            controller.btnOptionsClicked();
        }
    }


}
