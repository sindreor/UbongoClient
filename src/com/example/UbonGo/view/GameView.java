package com.example.UbonGo.view;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Pair;
import android.view.MotionEvent;

import com.example.UbonGo.DisplayElements;
import com.example.UbonGo.controller.GameController;
import com.example.UbonGo.model.GameBoard;
import com.example.UbonGo.model.GamePiece;

import java.util.ArrayList;

import sheep.graphics.Image;
import sheep.gui.TextButton;
import sheep.gui.WidgetAction;
import sheep.gui.WidgetListener;
import sheep.input.TouchListener;


/**
 * Created by Sindre on 17.03.2016.
 */
public class GameView implements View, TouchListener, WidgetListener {
    GameController controller;
    private TextButton flip;
    private TextButton undo;
    private Image background;
    private Image pieceImage;
    private Image emptyImage;
    private float scale;

    public GameView(GameController controller)
    {
        this.controller = controller;
        controller.addTouchListener(this);

        background = DisplayElements.getInstance().getBackground();
        pieceImage = DisplayElements.getInstance().getPieceSquare();
        emptyImage = DisplayElements.getInstance().getEmptySquare();

        flip = new TextButton(DisplayElements.getInstance().getWidth()*0.85f,
                DisplayElements.getInstance().getHeight()*0.80f,
                "Flip",
                DisplayElements.getInstance().getButtonFont(DisplayElements.getInstance().getHeight()));
        controller.addTouchListener(flip);
        flip.addWidgetListener(this);

        undo = new TextButton(DisplayElements.getInstance().getWidth()*0.65f,
                DisplayElements.getInstance().getHeight()*0.80f,
                "Undo",
                DisplayElements.getInstance().getButtonFont(DisplayElements.getInstance().getHeight()));
        controller.addTouchListener(undo);
        undo.addWidgetListener(this);
    }

    public void drawComponents(Canvas canvas){
        background.draw(canvas, 0, 0);
        flip.draw(canvas);
        undo.draw(canvas);
    }

    public void drawBoard(Canvas canvas, GameBoard board, float scale)
    {
        float width = (float)DisplayElements.getInstance().getWidth();
        float emptyWidth = emptyImage.getWidth();

        // Draw board
        for ( Pair<Integer, Integer> pair : board.getSlots()) {
            float x = pair.first * emptyWidth * scale + width / 2.0f;
            float y = pair.second * emptyWidth * scale;
            Matrix m = new Matrix();
            m.setScale(scale, scale);
            m.postTranslate(x, y);
            emptyImage.draw(canvas, m);
        }
    }

    public void drawGamePieces(Canvas canvas, GameBoard board, float scale)
    {
        float pieceWidth =  pieceImage.getWidth();
        float width = (float)DisplayElements.getInstance().getWidth();
        float height = (float)DisplayElements.getInstance().getHeight();

        // Draw pieces
        for ( GamePiece piece : board.getPieces()) {
            float pieceX = piece.getX();
            float pieceY = piece.getY();
            if (pieceX >= 0.5f)
            {
                pieceX = 0.5f + (pieceX  - 0.5f) * scale * 2.0f;
                pieceY *= scale;
            }

            for ( Pair<Integer, Integer> pair : piece.getSlots()) {
                float x = pair.first * pieceWidth * scale + pieceX * width;
                float y = pair.second * pieceWidth * scale + pieceY * height;
                Matrix m = new Matrix();
                m.setScale(scale, scale);
                m.postTranslate(x, y);
                pieceImage.draw(canvas, m);
            }
        }
    }

    public void drawGhost(Canvas canvas, GamePiece piece, float scale)
    {
        if (piece == null)
            return;

        float width = (float)DisplayElements.getInstance().getWidth();
        float height = (float)DisplayElements.getInstance().getHeight();
        float pieceWidth =  pieceImage.getWidth();
        float pieceX = piece.getX();
        float pieceY = piece.getY();

        for ( Pair<Integer, Integer> pair : piece.getSlots()) {
            float x = pair.first * pieceWidth * scale + pieceX * width;
            float y = pair.second * pieceWidth * scale + pieceY * height;
            Matrix m = new Matrix();
            m.setScale(scale, scale);
            m.postTranslate(x, y);
            pieceImage.draw(canvas, m);
        }
    }



    @Override
    public boolean onTouchDown(MotionEvent event) {
        controller.touchDown(
                event.getX(),
                event.getY());
        return false;
    }

    @Override
    public boolean onTouchUp(MotionEvent event) {
        controller.touchUp(
                event.getX(),
                event.getY());
        return false;
    }

    @Override
    public boolean onTouchMove(MotionEvent event) {
        controller.touchMove(
                event.getX(),
                event.getY());
        return false;
    }
    
    @Override
    public void actionPerformed(WidgetAction action) {
        if (action.getSource() == flip)
            controller.flip();
        if (action.getSource() == undo)
            controller.undo();
    }

}
