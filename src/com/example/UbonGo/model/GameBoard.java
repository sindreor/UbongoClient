package com.example.UbonGo.model;

import android.util.Pair;

import com.example.UbonGo.DisplayElements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 05/04/2016.
 */
public class GameBoard{

    private List<GamePiece> pieces;
    private List<Pair<Integer, Integer>> slots;

    /**
     * main constructor to create a game board.
     * The slots are Pairs of (x,y) coordinates that make up a grid
     * starting with (0,0) in the upper left corner.
     * @param slots (x,y) pairs that build the game board.
     */
    public GameBoard(List<Pair<Integer, Integer>> slots){
        pieces = new ArrayList<>();
        this.slots = slots;
    }

    /**
     * Copy constructor
     * @param boardToCopy board that should be copied.
     */
    public GameBoard(GameBoard boardToCopy){
        this.pieces = new ArrayList<GamePiece>();
        for (GamePiece piece : boardToCopy.pieces){
            pieces.add(new GamePiece(piece));
        }

        this.slots = new ArrayList<Pair<Integer, Integer>>();
        for (Pair<Integer, Integer> slot : boardToCopy.slots){
            slots.add(new Pair<Integer, Integer>(slot.first,slot.second));
        }
    }

    /***
     * adds a piece to the game board.
     * @param piece the piece that should be added.
     */
    public void addPiece(GamePiece piece){
        pieces.add(piece);
    }

    /***
     * checks if the game board is completely filled with game pieces.
     * @return true, if game board is filled; false, if there are free slots left.
     */
    public boolean isCompleted(){
        for (Pair<Integer, Integer> slot : slots){
            boolean slotHasPiece = false;
            for (GamePiece piece : pieces){
                Pair<Integer, Integer> piecePosition = piece.getBoardPositionOfReferenceSlot();
                for (Pair<Integer,Integer> pieceSlot : piece.getSlots()) {
                    if (piecePosition != null) {
                        if (piecePosition.first + pieceSlot.first == slot.first
                                && piecePosition.second + pieceSlot.second == slot.second)
                        {
                            slotHasPiece = true;
                        }
                    }
                }
            }
            if (slotHasPiece == false) {
                return false;
            }
        }
        return true;
    }


    /***
     * Is the position free from other pieces and belongs to the available part of the game board?
     * @param pieceToMove the piece that should be moved to the newPosition.
     * @param newPosition the position the piece should be moved to.
     * @return true, if piece can be placed; false, if piece can not be placed.
     */
    public boolean isPositionFree(GamePiece pieceToMove, Pair<Integer, Integer> newPosition){

        for (Pair<Integer, Integer> newPieceSlot : pieceToMove.getSlots()){
            //check if there is a slot at the board
            boolean slotAvailable = false;
            for (Pair<Integer,Integer> boardSlot : slots){
                if (newPosition.first + newPieceSlot.first == boardSlot.first
                        && newPosition.second + newPieceSlot.second == boardSlot.second)
                {
                    slotAvailable = true;
                }
            }
            if (slotAvailable == false){
                return false;
            }

            //check if the slot is free
            for (GamePiece otherPiece : pieces){
                if (otherPiece != pieceToMove) {
                    Pair<Integer, Integer> otherPiecePosition = otherPiece.getBoardPositionOfReferenceSlot();
                    if (otherPiecePosition != null) {
                        for (Pair<Integer, Integer> otherPieceSlot : otherPiece.getSlots()) {
                            if (otherPiecePosition.first + otherPieceSlot.first == newPosition.first + newPieceSlot.first &&
                                    otherPiecePosition.second + otherPieceSlot.second == newPosition.second + newPieceSlot.second)
                            {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /***
     * Tries to set a piece to a new positions. Doesn't move the piece,
     * if the position is not possible.
     * @param piece the piece that should be set to a new position.
     * @param newPosition the new position of the piece.
     */
    public void setNewPiecePosition(GamePiece piece, Pair<Integer, Integer> newPosition){
        if (isPositionFree(piece, newPosition)){
            Pair<Float, Float> totalPosition = calculateTotalPosition(newPosition);
            piece.setPosition(totalPosition.first, totalPosition.second);
            piece.setNewBoardPosition(newPosition);
        }
    }

    /***
     * Calculates where the piece is placed according to the whole screen.
     * It is given as percentage of the screen position
     * @param slot slot for which the position is calculated.
     * @return percentage at the screen with 0,0 at upper left corner.
     */
    private Pair<Float, Float> calculateTotalPosition(Pair<Integer, Integer> slot){

        float x = (slot.first * DisplayElements.getInstance().getPieceSquare().getWidth() /
                DisplayElements.getInstance().getWidth() * 0.5f + 0.5f); //magic numbers -> only right half
        float y = slot.second * DisplayElements.getInstance().getPieceSquare().getHeight() /
                DisplayElements.getInstance().getHeight();
        return new Pair<Float, Float>(x, y);
    }

    /**
     * Determines the piece according to the coordinates of the whole game screen.
     * @param x phone screen position in percentage
     * @param y phone screen position in percentage
     * @return piece that is placed at this position.
     */
    public GamePiece getPiece(float x, float y) {
        for (int i = 0; i < pieces.size(); i++){
            GamePiece piece = pieces.get(i);
            for (Pair<Integer,Integer> pieceSlot : piece.getSlots()){
                float verticalSlotSize = DisplayElements.getInstance().getPieceSquare().getHeight()
                        / DisplayElements.getInstance().getHeight();
                float horizontalSlotSize = DisplayElements.getInstance().getPieceSquare().getWidth()
                        / DisplayElements.getInstance().getWidth();

                boolean fitsVertical = piece.getY() + verticalSlotSize * pieceSlot.second <= y &&
                        y <= piece.getY() + verticalSlotSize * (pieceSlot.second + 1);
                boolean fitsHorizontal = piece.getX() + horizontalSlotSize * pieceSlot.first <= x &&
                        x <= piece.getX() + horizontalSlotSize * (pieceSlot.first + 1);

                if (fitsVertical && fitsHorizontal) {
                    return piece;
                }
            }
        }
        return null;
    }

    /**
     * Gives all the slots that are added to the Game board.
     * @return Slots of the game board.
     */
    public List<Pair<Integer, Integer>> getSlots()
    {
        return slots;
    }

    /**
     * Gives all the pieces that are added to the Game board.
     * @return pieces of the game board.
     */
    public List<GamePiece> getPieces()
    {
        return pieces;
    }

}


