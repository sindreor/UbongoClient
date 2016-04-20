package com.example.UbonGo.serverManager;

public class ServerManager {
    private static ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }

    public void makeTestCall(){
        System.out.print("made call");
    }
}
