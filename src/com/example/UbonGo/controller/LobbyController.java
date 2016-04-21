package com.example.UbonGo.controller;



import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.UbonGo.Main;
import com.example.UbonGo.ServerCommunication.ServerListener;
import com.example.UbonGo.ServerCommunication.ServerManager;
import com.example.UbonGo.model.GameModel;
import com.example.UbonGo.model.LobbyModel;
import com.example.UbonGo.serverManager.ClientCom;
import com.example.UbonGo.view.StartLobbyView;
import com.example.UbonGo.view.StartedLobbyView;
import com.example.UbonGo.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sheep.game.State;
import sheep.input.KeyboardListener;

/**
 * Created by Sindre on 17.03.2016.
 */
public class LobbyController extends State implements KeyboardListener, ServerListener {
    private Main main;
    private LobbyModel model;
    private View view;
    private boolean isAlreadyJoined;//This is used when a playerlist is received. If the player was the new player, the GUI should change view, otherwise only the playerlist should be updated in the current view.

    public LobbyController(Main main){
        this.main=main;
        this.view=new StartLobbyView(this);
        ServerManager.getInstance().setServerListener(this);
        isAlreadyJoined=false;
    }

    public void update(float dt){

    }

    public void draw(Canvas canvas){
        view.drawComponents(canvas);
    }

    public void btnBackClicked(){
        main.changeMainController(new MenuController(main));
        ((StartLobbyView) view).removeTextFields();
    }

    public void btnStartNewLobbyClicked(String playerName){
        try{

            //Use model to evaluate data before sending data to server, exceptions may be thrown
            model=new LobbyModel();
            model.setThisPlayer(playerName);

            //Update view
            ((StartLobbyView) view).removeTextFields();
            this.view=new StartedLobbyView(this,true); //The parameter is true, since this player is the owner. The gui for the owner will therefore be different, to give access to owner functionality
            isAlreadyJoined=true;

            //Send evaluated data to server
            ServerManager.getInstance().startLobby(model.getThisPlayer());

        }
        //Catch exceptions from the evaluation
        catch(IllegalArgumentException e){
            ((StartLobbyView) view).setError(e.getMessage()); //Prints the error message
        }



    }

    public void btnStartExistingLobbyClicked(String playerName,String pin){
        try {

            //Evaluate input with model
            model = new LobbyModel();
            model.setPin(pin);
            model.setThisPlayer(playerName);

            //Send evaluated data to server
            ServerManager.getInstance().joinPlayer(model.getThisPlayer(), pin, false);


        }
        catch(IllegalArgumentException e){
            ((StartLobbyView) view).setError(e.getMessage()); //Prints the error message
        }


    }

    public void btnBackToLobbyJoiningClicked(){
        ServerManager.getInstance().removePlayer(model.getThisPlayer(), model.getPin());
        main.changeMainController(new LobbyController(main));


    }

    public void btnStartGameClicked()
    {
        System.out.println("Starting the game");
        ServerManager.getInstance().startGame(model.getThisPlayer(), model.getPin());

    }

    public void dropDownChanged(String value){
        if("easy".equals(value)) {
            model.setDifficulty(0);
        }
        else if("medium".equals(value)){
            model.setDifficulty(1);
        }
        else if("hard".equals(value)){
            model.setDifficulty(2);
        }
        ServerManager.getInstance().setDifficulty(model.getPin(),model.getDifficulty()+"");
    }
    public Main getMain(){
        return main;
    }

    public void receiveUpdate(int type, String update){
        if(type==1){//Receive pin
            //Update model
            model.setPin(update);
            //Update view
            ((StartedLobbyView)view).writePin(update);
            ServerManager.getInstance().joinPlayer(model.getThisPlayer(),model.getPin(),true);

        }
        else if(type==2){//Receive playerlist when joining game
            //Convert string to list
            List<String> players=Arrays.asList(update.substring(1, update.length() - 1).split(", "));
            //Update model
            model.setPlayerList(players);
            //Update view
                if (!isAlreadyJoined) {
                    final View v=new StartedLobbyView(this,false);
                    ((StartedLobbyView)v).setPlayersList(model.getPlayers());
                    ((StartedLobbyView) v).writePin(model.getPin());
                    try {
                        //This is done, since only the UI-Thread is allowed to do these operations. Android limitation
                        main.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((StartLobbyView) view).removeTextFields();
                                view = v;//Owner-parameter set to false.
                                isAlreadyJoined = true;
                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                model.setThisPlayer(players.get(players.size()-1));

                }
                else {
                    ((StartedLobbyView) view).setPlayersList(model.getPlayers());
                }





        }
        else if(type==3){//Receive difficulty
            //Convert string to integer
            int difficulty=Integer.parseInt(update);

            //Update model
            model.setDifficulty(difficulty);

            //Update view
            if(difficulty==0){
                ((StartedLobbyView) view).writeDifficulty("easy");
            }
            else if(difficulty==1){
                ((StartedLobbyView) view).writeDifficulty("medium");
            }
            else if(difficulty==2){
                ((StartedLobbyView) view).writeDifficulty("hard");
            }
        }
        else if(type==6){
            System.out.println(update);
            GameController g=new GameController(main, new GameModel(update,model.getThisPlayer(),model.getPin(), main));
            ServerManager.getInstance().setServerListener(g);
            main.changeMainController(g);
        }
        else if(type==7){
            if(update.equals("Owner left")){
                //Navigate back, the lobby is closed
                try {
                    //This is done, since only the UI-Thread is allowed to do these operations. Android limitation
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            main.changeMainController(new LobbyController(main));
                        }
                    });
                }
                    catch(Exception e){
                        e.printStackTrace();
                    }

            }
            else{
                //Convert string to list
                List<String> players=Arrays.asList(update.substring(1, update.length() - 1).split(", "));
                //Update model
                model.setPlayerList(players);
                //Update view
                ((StartedLobbyView) view).setPlayersList(model.getPlayers());
            }
        }
        else if(type==8){//Receive error message
            try{
                ((StartLobbyView)view).setError(update);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    public void setView(View view){
        this.view=view;
    }


}
