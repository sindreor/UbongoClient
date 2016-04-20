package com.example.UbonGo.view;

import android.graphics.Canvas;


import com.example.UbonGo.DisplayElements;
import com.example.UbonGo.R;
import com.example.UbonGo.controller.GameController;
import sheep.graphics.Image;


/**
 * Created by Sindre on 20.04.2016.
 */
public class WinView implements View {
    private GameController controller;
    private Image background;
    private String winner;



    public WinView(GameController controller, String winner){
        this.controller=controller;
        background=new Image(R.drawable.ubongo_background_color);
        this.winner=winner;
    }

    public void drawComponents(Canvas canvas){
        background.draw(canvas, 0, 0);
        canvas.drawText(winner+" won the game!", DisplayElements.getInstance().getWidth() * 0.5f, DisplayElements.getInstance().getHeight()*0.5f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()+100));
    }



}
