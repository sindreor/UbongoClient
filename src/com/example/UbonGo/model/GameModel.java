package com.example.UbonGo.model;

import android.util.Pair;

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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Sindre on 17.03.2016.
 */
public class GameModel {

    private GameBoard board;
    private GamePiece ghostedPiece;
    private String pin;
    private String playerName;
    private Main main;

    public GameModel(String boardData, String playerName, String pin, Main main)
    {
        this.pin=pin;
        this.playerName=playerName;

        this.main = main;
        int difficulty = 0;//Character.getNumericValue(boardData.charAt(0)); // number - 0(easy), 1(medium), 2(hard)

        List<List<Pair<Integer, Integer>>> arr = generateSlotsAndPieces(difficulty);
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
    public List<List<Pair<Integer, Integer>>> generateSlotsAndPieces(int difficulty){
        List<List<Pair<Integer, Integer>>> res = new ArrayList<>();
        String gameBoardLine;
        List<String> pieces = new ArrayList<>();
        try
        {
            InputStream is = main.getResources().openRawResource(R.raw.slots_easy);
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            //choose random board
            int lineNumber = new Random().nextInt(countLines(difficulty) + 1);
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

    private int countLines(int difficulty) throws IOException {
        InputStream is = null;
        switch (difficulty){
            case 0  :   {
                is = new BufferedInputStream(main.getResources().openRawResource(R.raw.slots_easy));
                break;
            }
            case 1  :   {
                is = new BufferedInputStream(main.getResources().openRawResource(R.raw.slots_medium));
                break;
            }
            case 2  :   {
                is = new BufferedInputStream(main.getResources().openRawResource(R.raw.slots_hard));
                break;
            }
        }
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
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

