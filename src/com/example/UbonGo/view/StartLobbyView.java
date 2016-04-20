package com.example.UbonGo.view;

import android.graphics.Canvas;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.UbonGo.DisplayElements;
import com.example.UbonGo.R;
import com.example.UbonGo.controller.LobbyController;

import sheep.graphics.Image;
import sheep.gui.WidgetAction;
import sheep.gui.WidgetListener;

/**
 * Created by Sindre on 17.03.2016.
 */
public class StartLobbyView implements View, WidgetListener {

    private LobbyController controller;
    private Image background;
    private PictureButton btnBack;
    private EditText txtPlayerName;
    private PictureButton btnStartNewLobby;
    private android.view.View playerNameLayout;
    private EditText txtPin;
    private PictureButton btnStartExistingLobby;
    private String errorMessage="";


    public StartLobbyView(LobbyController controller){
        this.controller=controller;
        background=DisplayElements.getInstance().getBackground();

        //Back-button
        btnBack = DisplayElements.getInstance().getBackButton();
        controller.addTouchListener(btnBack);
        btnBack.addWidgetListener(this);

        //Start-new-lobby-button
        btnStartNewLobby = DisplayElements.getInstance().getPlussButton(DisplayElements.getInstance().getWidth() * 0.75f, DisplayElements.getInstance().getHeight() * 0.3f);
        controller.addTouchListener(btnStartNewLobby);
        btnStartNewLobby.addWidgetListener(this);

        //Start-existing-lobby-button
        btnStartExistingLobby = DisplayElements.getInstance().getPlussButton(DisplayElements.getInstance().getWidth()*0.75f,DisplayElements.getInstance().getHeight()*0.5f);
        controller.addTouchListener(btnStartExistingLobby);
        btnStartExistingLobby.addWidgetListener(this);


        //To make EditText work with sheep, we have to put it as an overlay over the canvas which Sheep is using. To do this we must get the main activity class.
        playerNameLayout = new LinearLayout(controller.getMain());

        //Input-field for name
        txtPlayerName = new EditText(controller.getMain());
        txtPlayerName.setWidth(Math.round(DisplayElements.getInstance().getWidth() * 0.4f));
        txtPlayerName.setVisibility(android.view.View.VISIBLE);
        txtPlayerName.setHint("name");
        txtPlayerName.setX(DisplayElements.getInstance().getWidth() * 0.45f);
        txtPlayerName.setY(DisplayElements.getInstance().getHeight() * 0.05f);
        ((LinearLayout) playerNameLayout).addView(txtPlayerName);

        //Input-field for PIN
        txtPin = new EditText(controller.getMain());
        txtPin.setWidth(Math.round(DisplayElements.getInstance().getWidth()*0.15f));
        txtPin.setInputType(InputType.TYPE_CLASS_NUMBER);
        txtPin.setVisibility(android.view.View.VISIBLE);
        txtPin.setHint("PIN");
        txtPin.setX(txtPlayerName.getX() - DisplayElements.getInstance().getWidth() * 0.4f);
        txtPin.setY(DisplayElements.getInstance().getHeight() * 0.5f);
        ((LinearLayout) playerNameLayout).addView(txtPin);

        //The LinearLayout is added on top of the canvas
        controller.getMain().addContentView(playerNameLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

    }

    public void drawComponents(Canvas canvas){
        background.draw(canvas, 0, 0);
        btnBack.draw(canvas);
        btnStartNewLobby.draw(canvas);
        btnStartExistingLobby.draw(canvas);
        canvas.drawText("Player name:", DisplayElements.getInstance().getWidth() * 0.1f, DisplayElements.getInstance().getHeight() * 0.15f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
        canvas.drawText("Start a new lobby:", DisplayElements.getInstance().getWidth() * 0.1f, DisplayElements.getInstance().getHeight() * 0.4f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
        canvas.drawText("Join existing lobby:", DisplayElements.getInstance().getWidth()*0.1f, DisplayElements.getInstance().getHeight()*0.6f,DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
        canvas.drawText(errorMessage, DisplayElements.getInstance().getWidth()*0.15f, DisplayElements.getInstance().getHeight()*0.85f,DisplayElements.getInstance().getErrorTextFont(DisplayElements.getInstance().getHeight()));
    }

    public void removeTextFields(){
        ((ViewGroup) playerNameLayout.getParent()).removeView(playerNameLayout); //This line removes the EditText
    }

    public void actionPerformed(WidgetAction action){
        if(action.getSource() == btnBack){
            controller.btnBackClicked();

        }
        else if(action.getSource()== btnStartNewLobby){
            controller.removeTouchListener(btnBack);
            controller.btnStartNewLobbyClicked(txtPlayerName.getText()+"");

        }
        else if(action.getSource()==btnStartExistingLobby){
            controller.removeTouchListener(btnBack);
            controller.btnStartExistingLobbyClicked(txtPlayerName.getText()+"",txtPin.getText()+"");

        }
    }
    public void setError(String error){
        errorMessage=error;
    }



}
