package com.example.UbonGo.model;

import android.util.Pair;

import com.example.UbonGo.DisplayElements;

import com.example.UbonGo.Main;
import com.example.UbonGo.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Sindre on 17.03.2016.
 */
public class GameModel {

    private GameBoard board;
    private GamePiece ghostedPiece;
    private Stack<GameBoard> savedBoards;
    private String pin;
    private String playerName;
    private Main main;

    public GameModel(String boardData, String playerName, String pin, Main main)
    {
        this.pin=pin;
        this.playerName=playerName;

        this.main = main;
        int difficulty = Character.getNumericValue(boardData.charAt(0)); // number - 0(easy), 1(medium), 2(hard)
        int boardId = Character.getNumericValue(boardData.charAt(1)); // 0-4

        List<List<Pair<Integer, Integer>>> arr = generateSlotsAndPieces(difficulty, boardId);
        board = new GameBoard(arr.get(0));
        for (int i=1; i<arr.size(); i++){
            board.addPiece(new GamePiece(arr.get(i)));
        }
    }

    /**
     * Reads and uses data from file to generate game board and corresponding pieces.
     * @param difficulty
     * @return
     */
    public List<List<Pair<Integer, Integer>>> generateSlotsAndPieces(int difficulty, int boardId){
        List<List<Pair<Integer, Integer>>> res = new ArrayList<>();
        String gameBoardLine;
        List<String> pieces = new ArrayList<>();
        try
        {
            InputStream is = null;
            switch (difficulty){
                case 0  :   {
                    is = main.getResources().openRawResource(R.raw.slots_easy);
                    break;
                }
                case 1  :   {
                    is = main.getResources().openRawResource(R.raw.slots_medium);
                    break;
                }
                case 2  :   {
                    is = main.getResources().openRawResource(R.raw.slots_hard);
                    break;
                }
            }
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            int lineNumber = boardId;
            while (lineNumber > 1){
                br.readLine();
                lineNumber--;
            }

            String line = br.readLine();
            String[] splits = line.split(" ");
            gameBoardLine = splits[0];
            res.add(convertToPoints(gameBoardLine));
            pieces.addAll(Arrays.asList(splits).subList(1, splits.length));
            for (String piece : pieces){
                res.add(convertToPoints(piece));
            }
            return res;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Pair<Integer, Integer>> convertToPoints(String line){
        List<Pair<Integer, Integer>> res = new ArrayList<>();
        String[] points = line.split(";");
        for (String point : points) {
            String[] coords = point.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            res.add(Pair.create(x, y));
        }
        return res;
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

