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

    /**
     *Constructor initializing the fields for te model
     */
    public LobbyModel(){
        players=new ArrayList<String>();
        difficulty=0;

    }

    /**
     *Set and evaluate the playername which the player has chosen or received from the server
     * @param player name of player
     */
    public void setThisPlayer(String player){

        if(player.length()<1){//Evaluating name
            throw  new IllegalArgumentException("Missing player name");
        }
        this.thisPlayer=player;

    }

    /**
     *Get the name of the player for the lobby
     * @return The name of the player
     */
    public String getThisPlayer(){
        return thisPlayer;
    }


    public void removePlayer(String player){
        players.remove(player);
    }

    /**
     *Set and evaluate the difficulty-value for the game
     * @param difficulty as int with value 0, 1 or 2
     */
    public void setDifficulty(int difficulty){
        if(difficulty==0||difficulty==1||difficulty==2){
            this.difficulty=difficulty;
        }
        else{
            throw new IllegalArgumentException("Difficulty can only be 0, 1 og 2");
        }
    }

    /**
     *Get the difficulty value for the game
     * @return difficulty-value
     */
    public int getDifficulty(){
        return difficulty;
    }

    /**
     *Get the list of all players joined to the lobby
     * @return list of players in the lobby
     */
    public List<String> getPlayers(){
        return players;
    }

    /**
     *Gets the pin to join the lobby
     * @return pin represented as a String
     */
    public String getPin(){
        return pin;
    }

    /**
     *Set the pin for the lobby
     * @param pin the pin as a String
     */
    public void setPin(String pin){
        if(pin.length()!=4){
            throw new IllegalArgumentException("Pin must be 4 digits");
        }
        this.pin=pin;
    }

    /**
     *Set the list of players. This will be called when a list is received from the server.
     * @param players the list of players
     */
    public void setPlayerList(List<String> players){
        this.players=players;
    }




}
