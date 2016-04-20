package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    static ServerSocket serverSocket;

    public Server() {

    }

    public void run() {
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server connected on port: 7777.");
        } catch (IOException e) {
            System.out.println("Could not listen on port: 7777.");
            System.exit(-1);
        }
        while (listening) {
            try {
                Socket socket = serverSocket.accept();            //waiting for client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                out.flush();
            } catch (IOException e) {
                System.out.println("Could not connect the client");
                System.exit(-1);
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Could not close the server socket");
            System.exit(-1);
        }
    }
}
