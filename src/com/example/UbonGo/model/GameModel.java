package com.example.UbonGo.model;

import android.util.Pair;

import com.example.UbonGo.DisplayElements;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Sindre on 17.03.2016.
 */
public class GameModel {

    private GameBoard board;
    private GamePiece ghostedPiece;
    private Stack<GameBoard> savedBoards;
    private String pin;
    private String playerName;

    public GameModel(String boardData, String playerName, String pin)
    {
        this.pin=pin;
        this.playerName=playerName;
        savedBoards = new Stack<>();

        // TODO: Use board data to generate board.

        // TESTING CODE!!!!!!!!!
        ArrayList<Pair<Integer, Integer>> slots = new ArrayList<>();
        slots.add(Pair.create(0, 0));
        slots.add(Pair.create(1, 0));
        slots.add(Pair.create(2, 0));
        slots.add(Pair.create(0, 1));
        slots.add(Pair.create(1, 1));
        slots.add(Pair.create(2, 1));
        slots.add(Pair.create(0, 2));
        slots.add(Pair.create(1, 2));
        slots.add(Pair.create(2, 2));
        slots.add(Pair.create(0, 3));
        slots.add(Pair.create(1, 3));
        slots.add(Pair.create(2, 3));

        ArrayList<Pair<Integer, Integer>> slots2 = new ArrayList<>();
        slots2.add(Pair.create(0, 0));
        slots2.add(Pair.create(1, 0));
        slots2.add(Pair.create(2, 0));
        slots2.add(Pair.create(0, 1));
        slots2.add(Pair.create(1, 1));

        ArrayList<Pair<Integer, Integer>> slots3 = new ArrayList<>();
        slots3.add(Pair.create(0, 0));
        slots3.add(Pair.create(1, 0));
        slots3.add(Pair.create(2, 0));

        ArrayList<Pair<Integer, Integer>> slots4 = new ArrayList<>();
        slots4.add(Pair.create(0, 0));

        board = new GameBoard(slots);
        board.addPiece(new GamePiece(new ArrayList<>(slots2)));
        board.addPiece(new GamePiece(new ArrayList<>(slots3)));
        board.addPiece(new GamePiece(new ArrayList<>(slots3)));
        board.addPiece(new GamePiece(new ArrayList<>(slots4)));
    }



    public GameBoard getBoard()
    {
        return board;
    }

    public void movePieceToOn(Pair<Float, Float> position, Pair<Integer, Integer> boardRelativeCoordinate)
    {
        savedBoards.push(new GameBoard(board));
        GamePiece p = getPiece(position);
        if (p != null){
            board.setNewPiecePosition(p, boardRelativeCoordinate);
        }
    }

    public void movePieceToOff(Pair<Float, Float> startPosition, Pair<Float, Float> endPosition)
    {
        savedBoards.push(new GameBoard(board));
        GamePiece p = getPiece(startPosition);
        System.out.println("(" + startPosition.first + ", " + startPosition.second + ")");
        System.out.println("Got for OffMove: " + p);

        if (p != null){

            //calculate upper left corner
            float pieceWidth = DisplayElements.getInstance().getPieceSquare().getWidth();
            float screenWidth = DisplayElements.getInstance().getWidth();
            float cornerPositionX = endPosition.first - pieceWidth / 2 / screenWidth;

            float pieceHeight = DisplayElements.getInstance().getPieceSquare().getHeight();
            float screenHeight = DisplayElements.getInstance().getHeight();
            float cornerPositionY = endPosition.second - pieceHeight / 2 / screenHeight;

            p.setPosition(cornerPositionX, cornerPositionY);
        }
    }

    public GamePiece getPiece(Pair<Float, Float> pos)
    {
        return board.getPiece(pos.first, pos.second);
    }

    public void rotate(Pair<Float, Float> pos)
    {
        savedBoards.push(new GameBoard(board));
        GamePiece p = getPiece(pos);
        if (p != null) {
            p.rotate90();
            Pair<Integer, Integer> referencePosition = p.getBoardPositionOfReferenceSlot();
            //if the piece has a reference position on the board, check if the rotated piece has collisions
            if ((referencePosition != null && !board.isPositionFree(p, referencePosition))
                    || !p.staysOnScreen(p.getX(), p.getY()))
            {
                setToStartPosition(p);
            }
        }
    }

    /**
     * Sets the piece back to the 0,0 position.
     * @param p the piece to set back.
     */
    private void setToStartPosition(GamePiece p) {
        // calculate shift for rotated pieces.
        Integer minX = 0;
        Integer minY = 0;
        for (Pair<Integer, Integer> slot : p.getSlots()){
            if (minX > slot.first){
                minX = slot.first;
            }
            if (minY > slot.second){
                minY = slot.second;
            }
        }

        //calculate upper left corner of off board position (start position)
        float pieceWidth = DisplayElements.getInstance().getPieceSquare().getWidth();
        int screenWidth = DisplayElements.getInstance().getWidth();
        float positionOffBoardX =  -1 * minX * pieceWidth / screenWidth;

        float pieceHeight = DisplayElements.getInstance().getPieceSquare().getHeight();
        int screenHeight = DisplayElements.getInstance().getHeight();
        float positionOffBoardY =  -1 * minY * pieceHeight / screenHeight;

        p.setPosition(positionOffBoardX, positionOffBoardY);
    }

    /**
     * replaces the current board with a saved one, if available.
     */
    public void undo()
    {
        if (!savedBoards.empty()){
            board = savedBoards.pop();
        }
    }

    public boolean isCompleted()
    {
        return board.isCompleted();
    }


    public GamePiece getGhostedPiece() {
        return ghostedPiece;
    }

    public void setGhostedPiece(GamePiece ghostedPiece) {
        if (ghostedPiece != null){
            this.ghostedPiece = new GamePiece(ghostedPiece);
        }else{
            this.ghostedPiece = null;
        }
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getPin(){
        return pin;
    }
}

