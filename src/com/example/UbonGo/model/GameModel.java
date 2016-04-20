package com.example.UbonGo.model;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Sindre on 17.03.2016.
 */
public class GameModel {

    private GameBoard board;
    private GamePiece ghostedPiece;
    private String pin;
    private String playerName;

    public GameModel(String boardData, String playerName, String pin)
    {
        this.pin=pin;
        this.playerName=playerName;

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
        GamePiece p = getPiece(position);
        board.setNewPiecePosition(p, boardRelativeCoordinate);
    }

    public void movePieceToOff(Pair<Float, Float> startPosition, Pair<Float, Float> endPosition)
    {
        GamePiece p = getPiece(startPosition);
        System.out.println("(" + startPosition.first + ", " + startPosition.second + ")");
        System.out.println("Got for OffMove: " + p);

        if (p != null){
            //calculate upper left corner
            float positionX = p.getX() + (endPosition.first - startPosition.first);
            float positionY = p.getY() + (endPosition.second - startPosition.second);

            p.setPosition(positionX, positionY);

        }
    }

    public GamePiece getPiece(Pair<Float, Float> pos)
    {
        return board.getPiece(pos.first, pos.second);
    }

    public void rotate(Pair<Float, Float> pos)
    {
        GamePiece p = getPiece(pos);
        if (p != null)
            p.rotate90();
    }

    public void flip(Pair<Float, Float> pos)
    {
        GamePiece p = getPiece(pos);
        p.flipYAxis();
    }

    public void undo()
    {
        // TODO: Make this undo your last move.
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

    /**
     * If there's a ghost, this changes its position.
     * @param position
     */
    public void setGhostedPiecePosition(Pair<Float, Float> position)
    {
        if (ghostedPiece != null)
            ghostedPiece.setPosition(position.first, position.second);
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getPin(){
        return pin;
    }
}

