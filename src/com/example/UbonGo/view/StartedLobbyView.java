package com.example.UbonGo.view;

import android.graphics.Canvas;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.example.UbonGo.DisplayElements;
import com.example.UbonGo.controller.LobbyController;
import java.util.ArrayList;
import java.util.List;
import sheep.graphics.Image;
import sheep.gui.TextButton;
import sheep.gui.WidgetAction;
import sheep.gui.WidgetListener;

/**
 * Created by Sindre on 17.03.2016.
 */
public class StartedLobbyView implements View, WidgetListener {

    private boolean isOwner;
    private Image background;
    private LobbyController controller;
    private PictureButton btnBackToLobbyJoining;
    private List<String> players=new ArrayList<String>();
    private android.view.View layout;
    private String difficulty="";
    private String pin="";
    private Spinner dropdown;
    private TextButton btnStartGame;

    /**
     *Constructor which initializes the fields for the view
     * @param controller the controller which holds and controls the view
     * @param isOwner used to set decide if the view is in OwnerMode or not
     */
    public StartedLobbyView(LobbyController controller, boolean isOwner){
        this.controller=controller;
        this.isOwner=isOwner;
        background= DisplayElements.getInstance().getBackground();

        //Back-button
        btnBackToLobbyJoining = DisplayElements.getInstance().getBackButton();
        controller.addTouchListener(btnBackToLobbyJoining);
        btnBackToLobbyJoining.addWidgetListener(this);
        if(isOwner){
            layout=new LinearLayout(controller.getMain());
            dropdown = new Spinner(controller.getMain());
            String[] items = new String[]{"easy", "medium", "hard"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(controller.getMain(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setX(DisplayElements.getInstance().getWidth() * 0.5f);
            dropdown.setY(DisplayElements.getInstance().getHeight() * 0.15f);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                    difficultyChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    difficultyChanged();
                }
            });
            ((LinearLayout)layout).addView(dropdown);
            controller.getMain().addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
            //Start game button
            btnStartGame=new TextButton(DisplayElements.getInstance().getWidth()*0.5f, DisplayElements.getInstance().getHeight()*0.75f, "Start game", DisplayElements.getInstance().getButtonFont(Math.round(DisplayElements.getInstance().getHeight() * 1.5f)));
            controller.addTouchListener(btnStartGame);
            btnStartGame.addWidgetListener(this);
        }



    }
    /**
     *Called when the dropdown-list is changed, forwards it to the controller
     */
    private void difficultyChanged(){
        controller.dropDownChanged((String) dropdown.getSelectedItem());
    }

    /**
     *All views should implement this method which draws on the canvas when called
     * @param canvas
     */
    public void drawComponents(Canvas canvas){
        background.draw(canvas, 0, 0);
        btnBackToLobbyJoining.draw(canvas);
        canvas.drawText("Players:", DisplayElements.getInstance().getWidth() * 0.05f, DisplayElements.getInstance().getHeight() * 0.1f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));

        //Draw playerlist:
        float textpos=DisplayElements.getInstance().getHeight() * 0.2f;
        for(String player:players) {
            if(textpos<DisplayElements.getInstance().getHeight()*0.7f) {
                canvas.drawText(player, DisplayElements.getInstance().getWidth() * 0.1f, textpos, DisplayElements.getInstance().getTextFont(Math.round(DisplayElements.getInstance().getHeight() * 0.8f)));
                textpos += DisplayElements.getInstance().getHeight() * 0.05f;
            }
            else{
                canvas.drawText(player+"...", DisplayElements.getInstance().getWidth() * 0.1f,textpos, DisplayElements.getInstance().getTextFont(Math.round(DisplayElements.getInstance().getHeight() * 0.8f)));
                break;
            }
        }

        //Draw difficulty and start burton
        if(isOwner) {
            canvas.drawText("Difficulty: ", DisplayElements.getInstance().getWidth() * 0.5f, DisplayElements.getInstance().getHeight() * 0.1f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
            btnStartGame.draw(canvas);
        }
        else{
            canvas.drawText("Difficulty: "+difficulty, DisplayElements.getInstance().getWidth() * 0.5f, DisplayElements.getInstance().getHeight() * 0.1f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
            canvas.drawText("Game will start soon...", DisplayElements.getInstance().getWidth() * 0.4f, DisplayElements.getInstance().getHeight() * 0.8f, DisplayElements.getInstance().getTextFont(Math.round(DisplayElements.getInstance().getHeight() * 1.5f)));
        }

        //Draw pin
        if(isOwner) {
            canvas.drawText("Game PIN: "+pin, DisplayElements.getInstance().getWidth() * 0.5f, DisplayElements.getInstance().getHeight() * 0.55f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));
        }
        else{
            canvas.drawText("Game PIN: "+pin, DisplayElements.getInstance().getWidth() * 0.5f, DisplayElements.getInstance().getHeight() * 0.55f, DisplayElements.getInstance().getTextFont(DisplayElements.getInstance().getHeight()));

        }

    }

    /**
     *Method implemented since the view is implementing WidgetListener. Picks up when buttons are clicked and forwards it to the controller.
     * @param action action received from Widget
     */
    public void actionPerformed(WidgetAction action){
        if(action.getSource() == btnBackToLobbyJoining){
            controller.btnBackToLobbyJoiningClicked();
            if(isOwner){
                ((ViewGroup) layout.getParent()).removeView(layout);
            }
        }
        if (action.getSource() == btnStartGame && isOwner) {
            controller.btnStartGameClicked();
            ((ViewGroup) layout.getParent()).removeView(layout);
        }
    }
    /**
     *Set the playerlist that should be drawned
     * @param players list of players that should be displayed in the view.
     */
    public void setPlayersList(List<String> players){
        this.players=players;
    }

    /**
     *Sets the difficulty-String that should be showed drawned in the view
     * @param difficulty the difficulty that should be shown
     */
    public void writeDifficulty(String difficulty){
        this.difficulty=difficulty;
    }

    /**
     *Sets the pin that should be drawed in the view
     * @param pin the pin that should be drawed.
     */
    public void writePin(String pin){this.pin=pin; };

}
