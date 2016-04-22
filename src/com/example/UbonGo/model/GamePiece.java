package com.example.UbonGo.model;

import android.util.Pair;

import com.example.UbonGo.DisplayElements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 05/04/2016.
 */
public class GamePiece{

    private float x;
    private float y;
    private Pair<Integer, Integer> boardPositionOfReferenceSlot;
    private List<Pair<Integer, Integer>> slots;

    public GamePiece(List<Pair<Integer, Integer>> slots){
        this.slots = slots;
    }

    /**
     * Copy constructor for a game piece.
     * @param gamePieceToCopy piece that should be copied.
     */
    public GamePiece(GamePiece gamePieceToCopy){
        this.slots = new ArrayList<>();
        for (Pair<Integer, Integer> slotToCopy : gamePieceToCopy.getSlots()){
            slots.add(Pair.create(slotToCopy.first.intValue(), slotToCopy.second.intValue()));
        }
        this.x = gamePieceToCopy.getX();
        this.y = gamePieceToCopy.getY();

        if (boardPositionOfReferenceSlot != null) {
            boardPositionOfReferenceSlot = new Pair<>(
                    gamePieceToCopy.getBoardPositionOfReferenceSlot().first.intValue(),
                    gamePieceToCopy.getBoardPositionOfReferenceSlot().second.intValue());
        }
    }

    /**
     * Sets the piece to a new position on the board grid.
     * @param newBoardPosition position on the board grid.
     */
    public void setNewBoardPosition(Pair<Integer, Integer> newBoardPosition){
        boardPositionOfReferenceSlot = newBoardPosition;
    }

    /**
     * Sets the piece to a new position on the phone screen.
     * @param x horizontal screen position in percentage.
     * @param y vertical screen position in percentage.
     */
    public void setPosition(float x, float y)
    {
        if (staysOnScreen(x, y)) {
            this.x = x;
            this.y = y;
            if (x < 0.5) {
                boardPositionOfReferenceSlot = null;
            }
        }
    }

    /**
     * Gets the X-position of the piece.
     * @return horizontal position in percentage.
     */
    public float getX()
    {
        return x;
    }

    /**
     * Gets the Y-position of the piece.
     * @return vertical position in percentage.
     */
    public float getY()
    {
        return y;
    }

    /**
     * Gets the Slots of the piece.
     * @return the slots of the piece.
     */
    public List<Pair<Integer, Integer>> getSlots() {
        return slots;
    }

    /**
     * Gets the board position of the (0,0) slot.
     * @return position of (0,0) slot.
     */
    public Pair<Integer, Integer> getBoardPositionOfReferenceSlot() {
        return boardPositionOfReferenceSlot;
    }

    /**
     * Tests if the piece would leave the screen.
     * @param newX new screen x position in percentage.
     * @param newY new screen y position in percentage.
     * @return true, if the piece would stay in the screen, false if the piece would leave the
     * screen more than 0.7 * times the shape block size.
     */
    public boolean staysOnScreen(float newX, float newY){
        for (Pair<Integer, Integer> slot : slots){
            float pieceWidthPercentage = DisplayElements.getInstance().getPieceSquare().getWidth()
                    / DisplayElements.getInstance().getWidth();
            float pieceHeightPercentage = DisplayElements.getInstance().getPieceSquare().getHeight()
                    / DisplayElements.getInstance().getHeight();

            if (newX + slot.first * pieceWidthPercentage < - pieceWidthPercentage * 0.7f
                    || newY + slot.second * pieceHeightPercentage < - pieceHeightPercentage * 0.7f
                    || newX + (slot.first + 1) * pieceWidthPercentage > 1.0f + pieceWidthPercentage * 0.7f
                    || newY + (slot.second + 1) * pieceHeightPercentage > 1.0f + pieceHeightPercentage * 0.7f){

                return false;
            }
        }
        return true;
    }

    /**
     * Rotates the piece 90 degrees in clockwise direction.
     */
    public void rotate90(){
        for (int i = 0; i < slots.size(); i++) {
            Pair<Integer, Integer> slot = slots.get(i);
            Integer x = -slot.second;
            Integer y = slot.first;
            slots.set(i, Pair.create(x, y));
        }
    }

    /**
     * Mirrors the Shape on the Y-Axis.
     */
    public void flipYAxis(){
        for (int i = 0; i < slots.size(); i++) {
            Pair<Integer, Integer> slot = slots.get(i);
            Integer x = slot.first;
            Integer y = -slot.second;
            slots.set(i, Pair.create(x, y));
        }
    }
}
