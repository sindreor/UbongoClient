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
    private ArrayList<Pair<Integer, Integer>> slots; // TODO: Rename if it's weird

    public GamePiece(ArrayList<Pair<Integer, Integer>> slots){
        this.slots = slots;
    }

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

    public void setNewBoardPosition(Pair<Integer, Integer> newBoardPosition){
        boardPositionOfReferenceSlot = newBoardPosition;
    }

    public void setPosition(float x, float y)
    {
        if (staysOnScreen(x, y)) {
            this.x = x;
            this.y = y;
            System.out.println("new position x: " + this.x);
            System.out.println("new position y: " + this.y);
            if (x < 0.5) {
                boardPositionOfReferenceSlot = null;
            }
        }
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public List<Pair<Integer, Integer>> getSlots() {
        return slots;
    }

    public Pair<Integer, Integer> getBoardPositionOfReferenceSlot() {
        return boardPositionOfReferenceSlot;
    }

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

    public void rotate90(){
        for (int i = 0; i < slots.size(); i++) {
            Pair<Integer, Integer> slot = slots.get(i);
            Integer x = -slot.second;
            Integer y = slot.first;
            slots.set(i, Pair.create(x, y));
        }
    }

    public void flipYAxis(){
        for (int i = 0; i < slots.size(); i++) {
            Pair<Integer, Integer> slot = slots.get(i);
            Integer x = slot.first;
            Integer y = -slot.second;
            slots.set(i, Pair.create(x, y));
        }
    }

}

