package com.example.UbonGo.controller;




import android.graphics.Canvas;
import com.example.UbonGo.Main;
import com.example.UbonGo.ServerCommunication.ServerListener;
import com.example.UbonGo.ServerCommunication.ServerManager;
import com.example.UbonGo.model.GameModel;
import com.example.UbonGo.model.LobbyModel;
import com.example.UbonGo.view.StartLobbyView;
import com.example.UbonGo.view.StartedLobbyView;
import com.example.UbonGo.view.View;
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

    /**
     *Constructor which initializes all fields in the controller and sets it as current ServerListener
     * @param main the main actity the controller is active state in
     */
    public LobbyController(Main main){
        this.main=main;
        this.view=new StartLobbyView(this);
        ServerManager.getInstance().setServerListener(this);
        isAlreadyJoined=false;
    }

    /**
     *Method due to extending State from Sheep-framework. Nothing is needed for this controller, since the lobby i static.
     */
    public void update(float dt){

    }

    public void draw(Canvas canvas){
        view.drawComponents(canvas);
    }

    /**
     *Method called when the back-button in the StartLobbyView is clicked. Changes the gamestate to Menu-state
     */
    public void btnBackClicked(){
        main.changeMainController(new MenuController(main));
        ((StartLobbyView) view).removeTextFields();
    }

    /**
     *Method called when the start-new-lobby-button is clicked in the StartLobbyView. Changes the view to a StartedLobbyView and requests the server to start a new game.
     * @param playerName name of the player which is the owner of the new lobby/game created
     */
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
    /**
     *Method called when the join-lobby-button in the StartLobbyView is clicked.
     * @param playerName name of the player joining the game.
     * @param pin the pin of the game to be joined.
     */
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
    /**
     *Method called when the back-button in the StartedLobbyView has been clicked. Changes the game state back to show the StartLobbyView. Notyfies server that a player has left the game.
     */
    public void btnBackToLobbyJoiningClicked(){
        ServerManager.getInstance().removePlayer(model.getThisPlayer(), model.getPin());
        main.changeMainController(new LobbyController(main));


    }

    /**
     *Method called when the start-game-button is clicked in the StartedLobbyView(OwnerMode)(See documentation for explaination of OwnerMode)
     * Requests the server to start the game
     */
    public void btnStartGameClicked()
    {
        System.out.println("Starting the game");
        ServerManager.getInstance().startGame(model.getThisPlayer(), model.getPin());

    }

    /**
     *Method called when the dropdownlist for difficulty is changed. Notifies the server about changed difficulty.
     */
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
    /**
     *Gets the main-class for this controller. This will be needed by some views that need to show Android-modules like textboxes and dropdownlists.
     */
    public Main getMain(){
        return main;
    }


    /**
     *Method implemented since the controller is a ServerListener. This method is called when the Servermanager receives a response from the server.
     * @param type type of response
     * @param update the message received from the server
     */
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
            ServerManager.getInstance().getDifficulty(model.getPin());
        }
        else if(type==3||type==8){//Receive difficulty
            //Convert string to integer
            int difficulty=Integer.parseInt(update);

            //Update model
            model.setDifficulty(difficulty);

            if(view instanceof StartedLobbyView) {
                //Update view
                if (difficulty == 0) {
                    ((StartedLobbyView) view).writeDifficulty("easy");
                } else if (difficulty == 1) {
                    ((StartedLobbyView) view).writeDifficulty("medium");
                } else if (difficulty == 2) {
                    ((StartedLobbyView) view).writeDifficulty("hard");
                }
            }
        }
        else if(type==6){//Receive instruction to start game
            System.out.println(update);
            GameController g=new GameController(main, new GameModel(update,model.getThisPlayer(),model.getPin(), main));
            ServerManager.getInstance().setServerListener(g);
            main.changeMainController(g);
        }
        else if(type==7){//Receive instruction saying that the owner left the game and the game therefore has to end
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
        else if(type==9){//Receive error message
            try{
                ((StartLobbyView)view).setError(update);//Print error message
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    /**
     *Set the view for this controller
     * @param view new view for the controller
     */
    public void setView(View view){
        this.view=view;
    }


}
