package com.example.UbonGo.model;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 05/04/2016.
 */
public class GamePiece{

    private float x;
    private float y;
    private Pair<Integer, Integer> positionOfUpperLeftPiece;
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

        if (positionOfUpperLeftPiece != null) {
            positionOfUpperLeftPiece = new Pair<>(
                    gamePieceToCopy.getPositionOfUpperLeftPiece().first.intValue(),
                    gamePieceToCopy.getPositionOfUpperLeftPiece().second.intValue());
        }
    }

    public void setNewBoardPosition(Pair<Integer, Integer> newBoardPosition){
        positionOfUpperLeftPiece = newBoardPosition;
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
        System.out.println("new position x: " + this.x);
        System.out.println("new position y: " + this.y);
        if (x < 0.5){
            positionOfUpperLeftPiece = null;
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

    public ArrayList<Pair<Integer, Integer>> getSlots() {
        return slots;
    }

    public Pair<Integer, Integer> getPositionOfUpperLeftPiece() {
        return positionOfUpperLeftPiece;
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
