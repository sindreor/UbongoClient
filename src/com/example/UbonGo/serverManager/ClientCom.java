package com.example.UbonGo.serverManager;

import com.example.UbonGo.ServerCommunication.ServerListener;

import java.util.ArrayList;

/**
 * Created by Sindre on 17.03.2016.
 */
public class ClientCom  {
    private static ClientCom instance=null;

    public static ClientCom getInstance(){
        if(instance==null){
            instance=new ClientCom();
        }
        return instance;
    }


    /**
     * Called by the startLobbyView. Creates a new lobby with a unique pin on the server.
     * @return The unique pin received from the server
     */
    public String startLobby(){
        //TODO: Implement this method
        return "0000";//Temporary return
    }

    /**
     * Adds a player to a lobby
     * @param player The name of the player
     * @param pin The pin for the lobby which the player should be joined to
     * @return A string containing game information, using the following format: player|player|player|player|player
     */
    public ArrayList<String> joinPlayer(String player, String pin){
        //TODO: Implement this method
        //Should throw IllegalArgumentException if the pin does not exist!!
        ArrayList<String> players=new ArrayList<String>();
        players.add(player);
        players.add("Ann");
        players.add("Ann");
        players.add("Frida");
        players.add("Ann");
        players.add("Ann");
        players.add("Frida");
        players.add("Ann");
        players.add("Ann");
        players.add("Frida");
        players.add("Ann");
        players.add("Ann");
        players.add("Frida");
        return players;
    }
    /**
     * Gets the difficulty for a lobby specified by the pin
     * @param pin The pin for the lobby
     * @return The difficulty of the lobby with the pin
     */
    public int getDifficulty(String pin){
        //TODO: Implement this method
        return 0;
    }

    /**
     * Sets the difficulty for a lobby
     * @param pin The pin for the lobby
     */
    public void setDifficulty(String pin, int difficulty){
        //TODO: Implement this method
    }


}
