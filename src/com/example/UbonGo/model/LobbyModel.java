package com.example.UbonGo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sindre on 17.03.2016.
 */
public class LobbyModel {
    private List<String> players;
    private int difficulty;
    private String pin;
    private String thisPlayer;

    public LobbyModel(){
        players=new ArrayList<String>();
        difficulty=0;

    }

    public void setThisPlayer(String player){

        if(player.length()<1){//Evaluating name
            throw  new IllegalArgumentException("Missing player name");
        }
        this.thisPlayer=player;

    }

    public String getThisPlayer(){
        return thisPlayer;
    }

    public void removePlayer(String player){
        players.remove(player);
    }
    public void setDifficulty(int difficulty){
        if(difficulty==0||difficulty==1||difficulty==2){
            this.difficulty=difficulty;
        }
        else{
            throw new IllegalArgumentException("Difficulty can only be 0, 1 og 2");
        }
    }
    public int getDifficulty(){
        return difficulty;
    }

    public List<String> getPlayers(){
        return players;
    }

    public String getPin(){
        return pin;
    }
    public void setPin(String pin){
        if(pin.length()!=4){
            throw new IllegalArgumentException("Pin must be 4 digits");
        }
        this.pin=pin;
    }

    public void setPlayerList(List<String> players){
        this.players=players;
    }




}
