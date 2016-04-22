package com.example.UbonGo.controller;

import android.graphics.Canvas;
import android.util.Pair;

import com.example.UbonGo.DisplayElements;
import com.example.UbonGo.Main;
import com.example.UbonGo.ServerCommunication.ServerListener;
import com.example.UbonGo.ServerCommunication.ServerManager;
import com.example.UbonGo.model.GameModel;
import com.example.UbonGo.model.GamePiece;
import com.example.UbonGo.serverManager.ClientCom;
import com.example.UbonGo.view.GameView;
import com.example.UbonGo.view.View;
import com.example.UbonGo.view.WinView;

import sheep.game.State;

/**
 * Created by Sindre on 17.03.2016.
 */
public class GameController extends State implements ServerListener {
    Main main;
    View view;
    GameModel gameModel;
    GamePiece lastSelectedPiece;
    GamePiece selectedPiece;
    Pair<Float, Float> startPosition;
    Long downPressedTime;
    float scale;
    boolean won=false;

    public GameController(Main m, GameModel model) // TODO: change this so that networking decides what board is played, or something
    {
        main = m;
        view = new GameView(this);
        gameModel = model; // TODO: set boardString
        downPressedTime = System.currentTimeMillis();
        scale =  DisplayElements.getInstance().getHeight() / (DisplayElements.getInstance().getEmptySquare().getWidth() * 8);
    }

    public void update(float dt){

    }

    public void draw(Canvas canvas){
        view.drawComponents(canvas);
        if(!won) {
            ((GameView) view).drawBoard(canvas, gameModel.getBoard(), scale);
            ((GameView) view).drawGamePieces(canvas, gameModel.getBoard(), scale);
            ((GameView) view).drawGhost(canvas, gameModel.getGhostedPiece(), scale);
        }
    }

    public void flip(){
        if (lastSelectedPiece != null)
            lastSelectedPiece.flipYAxis();
    }

    public void undo(){
        lastSelectedPiece = null;
        gameModel.undo();
    }

    public void touchDown(float x, float y)
    {
        float relativeX = x / DisplayElements.getInstance().getWidth();
        float relativeY = y / DisplayElements.getInstance().getHeight();

        // Fix the relative if it's on the board
        if (relativeX >= 0.5f)
        {
            relativeX = (relativeX - 0.5f) * 0.5f / scale + 0.5f;
            relativeY /= scale;
        }

        // Set the start position used for moving pieces
        startPosition = Pair.create(relativeX, relativeY);

        // Get the targeted piece
        selectedPiece = gameModel.getPiece(Pair.create(relativeX, relativeY));

        // Set last selected piece
        if (selectedPiece != null)
            lastSelectedPiece = selectedPiece;

        // Check if double tap
        if (System.currentTimeMillis() - downPressedTime < 200) // Tap time 200ms
        {
            System.out.println("Double tapped!");
            gameModel.rotate(Pair.create(relativeX, relativeY));
        }
        downPressedTime = System.currentTimeMillis();

        // Set ghost
        gameModel.setGhostedPiece(selectedPiece);

    }

    public void touchMove(float x, float y)
    {
        // Move ghost piece
        if (gameModel.getGhostedPiece() != null)
        {
            float screenWidth = DisplayElements.getInstance().getWidth();
            float screenHeight = DisplayElements.getInstance().getHeight();
            float imgWidth = DisplayElements.getInstance().getPieceSquare().getWidth();

            float relX = (x - imgWidth / 2.0f * scale) / screenWidth;
            float relY = (y - imgWidth / 2.0f * scale) / screenHeight;
            gameModel.getGhostedPiece().setPosition(relX, relY);
        }
    }

    public void touchUp(float x, float y)
    {
        float relativeX = x / DisplayElements.getInstance().getWidth();
        float relativeY = y / DisplayElements.getInstance().getHeight();

        // If no piece was selected, don't do nothing
        if (selectedPiece == null)
            return;

        // Check what side of the screen it was dropped
        if (relativeX < 0.5f)
        { // Left side
            gameModel.movePieceToOff(startPosition, Pair.create(relativeX, relativeY));
        }
        else
        { // Right side
            float widthHalf = DisplayElements.getInstance().getWidth() / 2.0f;
            float imgWidth = DisplayElements.getInstance().getEmptySquare().getWidth() * scale;
            int boardX =  (int)((x - widthHalf) / imgWidth);
            int boardY =  (int)(y / imgWidth);

            System.out.println("Raw: " + (x - widthHalf) + ", " + y);
            System.out.println("Placing at: " + boardX + ", " + boardY);
            gameModel.movePieceToOn(startPosition, Pair.create(boardX, boardY)); // TODO: Fix this; A probable cause of piece misplacement
        }

        // Unselect piece
        selectedPiece = null;

        // Remove ghost
        gameModel.setGhostedPiece(null);

        // Game completion
        if (gameModel.isCompleted())
        {
            ServerManager.getInstance().finishGame(gameModel.getPlayerName(),gameModel.getPin());
            System.out.println("YOU WIN!!!");
            System.out.println("YOU WIN!!!");
            System.out.println("YOU WIN!!!");
            System.out.println("YOU WIN!!!");
            System.out.println("YOU WIN!!!");
            System.out.println("YOU WIN!!!");
            // You win! TODO: Implement what happens when the player wins
        }
    }
    public void receiveUpdate(int type, String update){
        if(type==4){
            won=true;
            view=new WinView(this,update);
        }

    }
}
