package com.example.UbonGo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.example.UbonGo.ServerCommunication.ServerManager;
import com.example.UbonGo.controller.GameController;
import com.example.UbonGo.controller.MenuController;
import com.example.UbonGo.view.GameView;

import sheep.audio.Sound;
import sheep.game.Game;
import sheep.game.State;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Game game;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Disables rotation and forces landscape orientation

        //Server stuff
        // force for internet connection permissions
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // connect to the server

        //For some reason this is called twice and results in an IllegalStateException, the try statement is ignoring the repeated call.
        try {
            ServerManager.getInstance().execute();
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
        //Serverstuff end*/

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        DisplayElements.getInstance().setHeight(dm.heightPixels);
        DisplayElements.getInstance().setWidth(dm.widthPixels);


        game = new Game(this, null);
        // Push the main state.
        game.pushState(new MenuController(this)); //This displays the main menu when the game is opened.
        // View the game.
        setContentView(game);
    }

    /**
     * Called when you want to change the state, for instance when you want to go from main menu to lobby
     * @param controller the controller-class you want to change to
     */
    public void changeMainController(State controller){
        game.pushState(controller);
    }



    public void onBackPressed(){ //The back button closes the game, we may want to change this...
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);

    }
}


