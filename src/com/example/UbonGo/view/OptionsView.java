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
 * Created by Sindre on 18.03.2016.
 */
public class OptionsView implements View, WidgetListener {

    private MenuController controller;
    private Image background;
    private PictureButton btnBackToMain;
    private PictureButton btnVolumeDown;
    private PictureButton btnVolumeUp;
    private String volume;



    public OptionsView(MenuController controller){
        this.controller=controller;
        background=new Image(R.drawable.ubongo_background_color);

        //Back-button
        btnBackToMain=DisplayElements.getInstance().getBackButton();
        controller.addTouchListener(btnBackToMain);
        btnBackToMain.addWidgetListener(this);

        //Volume down-button
        btnVolumeDown=DisplayElements.getInstance().getMinusButton(DisplayElements.getInstance().getWidth()*0.1f,DisplayElements.getInstance().getHeight()*0.4f);
        controller.addTouchListener(btnVolumeDown);
        btnVolumeDown.addWidgetListener(this);

        //Volume up-button
        btnVolumeUp=DisplayElements.getInstance().getPlussButton(DisplayElements.getInstance().getWidth()*0.3f, DisplayElements.getInstance().getHeight()*0.4f);
        controller.addTouchListener(btnVolumeUp);
        btnVolumeUp.addWidgetListener(this);



    }

    public void drawComponents(Canvas canvas){
        background.draw(canvas, 0, 0);
        btnBackToMain.draw(canvas);
        btnVolumeDown.draw(canvas);
        btnVolumeUp.draw(canvas);
        //canvas.drawText(volume, 30, 30, new Font(0, 0, 0, 30, Typeface.SANS_SERIF, 1));
        canvas.drawText(volume, DisplayElements.getInstance().getWidth() * 0.2f, DisplayElements.getInstance().getHeight()*0.5f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
    }

    public void changeVolumeText(String volume){
        this.volume=volume;
    }

    public void actionPerformed(WidgetAction action){
        if(action.getSource()==btnBackToMain){
            controller.btnBackToMainClicked();
        }
        else if(action.getSource()==btnVolumeUp){
            controller.btnVolumeUpClicked();
        }
        else if(action.getSource()==btnVolumeDown){
            controller.btnVolumeDownClicked();
        }
    }
}
