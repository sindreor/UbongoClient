package com.example.UbonGo.ServerCommunication;

/**
 * Created by Sindre on 17.03.2016.
 */
public interface ServerListener {
    public void receiveUpdate(int type, String update);
}
