package com.example.UbonGo;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.UbonGo.view.PictureButton;

import sheep.graphics.Font;
import sheep.graphics.Image;

/**
 * Created by Sindre on 17.03.2016.
 */
public class DisplayElements {

    private static DisplayElements instance=null;

    //Singleton
    public static DisplayElements getInstance(){
        if(instance==null){
            instance=new DisplayElements();
        }
        return instance;
    }
    //The screen width, set in main inCreate
    private int width;

    //The screen height, set in main inCreate
    private int height;

    /**
     *Method for getting the screen width
     @return  the width of the screen
     */
    public int getWidth(){
        return width;
    }


    /**
     *Method for getting the screen height
     @return the height of the screen
     */
    public int getHeight(){
        return  height;
    }


    /**
     *Method for remembering the screen height
     * @param height screen height
     */
    public void setHeight(int height){
        this.height=height;
    }

    /**
     *Method for remembering the screen with
     * @param width screen width
     */
    public void setWidth(int width){
        this.width=width;
    }


    //Font used for all the buttons in the game
    public Paint[] getButtonFont(int screenHeight){
        Paint[] font= {new Font(255, 255, 255, screenHeight*0.07F, Typeface.SANS_SERIF, 1),new Font(255, 0, 0, screenHeight*0.07F, Typeface.SANS_SERIF, 1)};
        return font;

    }

    //Font used for text in the game
    public Paint getTextFont(int screenHeight){
        Paint font= new Font(255, 255, 255, screenHeight*0.07F, Typeface.SANS_SERIF, 1);
        return font;

    }

    //Font used for error messages in the game
    public Paint getErrorTextFont(int screenHeight){
        Paint font= new Font(255, 0, 0, screenHeight*0.07F, Typeface.SANS_SERIF, 1);
        return font;

    }

    //The back-button will always be placed on the same side, it can therefore be generalized with this method.
    public PictureButton getBackButton(){
        return new PictureButton(R.drawable.back_button,getWidth()*0.05f,getHeight()*0.7f);
    }

    //Pluss-button
    public PictureButton getPlussButton(float x, float y){
        return new PictureButton(R.drawable.pluss_button,x,y);
    }

    //Minus-button
    public PictureButton getMinusButton(float x, float y){
        return new PictureButton(R.drawable.minus_button,x,y);
    }

    //Background
    public Image getBackground(){
       return new Image(R.drawable.ubongo_background_color);
    }

    //Game-Logo
    public Image getGameLogo(){
        return new Image(R.drawable.ubongo_background_text);
    }

    //Pieces in board
    public Image getPieceSquare() {
        return new Image(R.drawable.square);
    }

    //Empty pieces in board
    public Image getEmptySquare(){
        return new Image(R.drawable.empty_square);
    }

}
