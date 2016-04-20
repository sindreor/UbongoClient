package com.example.UbonGo.model;

import android.util.Pair;

import com.example.UbonGo.DisplayElements;

import java.util.ArrayList;

/**
 * Created by Julia on 05/04/2016.
 */
public class GameBoard{

    private ArrayList<GamePiece> pieces;
    private ArrayList<Pair<Integer, Integer>> slots;

    public GameBoard( ArrayList<Pair<Integer, Integer>> slots){
        pieces = new ArrayList<>();
        this.slots = slots;
    }

    public void addPiece(GamePiece piece){
        pieces.add(piece);
    }

    public boolean isCompleted(){
        for (Pair<Integer, Integer> slot : slots){
            boolean slotHasPiece = false;
            for (GamePiece piece : pieces){
                Pair<Integer, Integer> piecePosition = piece.getPositionOfUpperLeftPiece();
                for (Pair<Integer,Integer> pieceSlot : piece.getSlots()) {
                    if (piecePosition != null) {
                        if (piecePosition.first + pieceSlot.first == slot.first && piecePosition.second + pieceSlot.second == slot.second) {
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
     * Is the position free from other pieces and belongs to the available part of the gameboard?
     * @param newPiece
     * @param newPosition
     * @return true, if piece can be placed; false, if piece can not be placed.
     */
    private boolean isPositionFree(GamePiece newPiece, Pair<Integer, Integer> newPosition){

        for (Pair<Integer, Integer> newPieceSlot : newPiece.getSlots()){
            //check if there is a slot at the board
            boolean slotAvailable = false;
            for (Pair<Integer,Integer> boardSlot : slots){
                if (newPosition.first + newPieceSlot.first == boardSlot.first && newPosition.second + newPieceSlot.second == boardSlot.second){
                    slotAvailable = true;
                }
            }
            if (slotAvailable == false){
                return false;
            }

            //check if the slot is free
            for (GamePiece otherPiece : pieces){
                Pair<Integer, Integer> otherPiecePosition = otherPiece.getPositionOfUpperLeftPiece();
                if (otherPiecePosition != null) {
                    for (Pair<Integer, Integer> otherPieceSlot : otherPiece.getSlots()) {
                        if (otherPiecePosition.first + otherPieceSlot.first == newPosition.first + newPieceSlot.first &&
                                otherPiecePosition.second + otherPieceSlot.second == newPosition.second + newPieceSlot.second){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /***
     * Tries to set a piece to a new positions.
     * @param piece
     * @param newPosition
     */
    public void setNewPiecePosition(GamePiece piece, Pair<Integer, Integer> newPosition){
        if (isPositionFree(piece, newPosition)){
            Pair<Float, Float> totalPosition = calculateTotalPosition(newPosition);
            piece.setPosition(totalPosition.first, totalPosition.second);
            piece.setNewBoardPosition(newPosition);
        }
    }

    private Pair<Float, Float> calculateTotalPosition(Pair<Integer, Integer> slot){

        float x = (slot.first * DisplayElements.getInstance().getPieceSquare().getWidth() /
                DisplayElements.getInstance().getWidth() * 0.5f + 0.5f); //magic numbers -> only right half
        float y = slot.second * DisplayElements.getInstance().getPieceSquare().getHeight() /
                DisplayElements.getInstance().getHeight();
        return new Pair<Float, Float>(x, y);
    }

    public GamePiece getPiece(float x, float y) {
        for (int i = 0; i < pieces.size(); i++){
            GamePiece piece = pieces.get(i);
            for (Pair<Integer,Integer> pieceSlot : piece.getSlots()){
                double verticalSlotSize = DisplayElements.getInstance().getPieceSquare().getHeight()
                        / DisplayElements.getInstance().getHeight();
                double horizontalSlotSize = DisplayElements.getInstance().getPieceSquare().getWidth()
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

    public ArrayList<Pair<Integer, Integer>> getSlots()
    {
        return slots;
    }

    public ArrayList<GamePiece> getPieces()
    {
        return pieces;
    }
}


