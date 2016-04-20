package com.example.UbonGo.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.Iterator;
import java.util.LinkedList;

import sheep.game.Game;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.gui.TextButton;
import sheep.gui.Widget;
import sheep.gui.WidgetAction;
import sheep.gui.WidgetListener;
import sheep.input.TouchListener;
import sheep.math.BoundingBox;

/**
 * Created by Sindre on 31.03.2016.
 */
public class PictureButton extends Widget {
    private Drawable picture;
    public static final int STATE_IDLE = 0;
    public static final int STATE_DOWN = 1;
    public static final int STATE_SIZE = 2;
    private float x;
    private float y;
    int state;
    BoundingBox box;
    private LinkedList<WidgetListener> listeners = new LinkedList();

    public PictureButton(int i, float x, float y) {
        this(Game.getInstance().getResources().getDrawable(i),x,y);
    }

    public PictureButton(Drawable picture, float x, float y){
        this.x=x;
        this.y=y;
        picture.setBounds(0, 0, picture.getMinimumWidth(), picture.getMinimumHeight());
        this.picture = picture;
        Rect bounds=new Rect();
        bounds.set((int)x,(int)y+picture.getMinimumHeight(),(int)x+picture.getMinimumHeight(),(int)y);
        box =new BoundingBox(bounds);
    }
    public void draw(Canvas canvas){
        canvas.save();
        canvas.translate(x, y);
        this.picture.draw(canvas);
        canvas.restore();
    }

    public void addWidgetListener(WidgetListener listener) {
        this.listeners.add(listener);
    }

    public void removeWidgetListener(WidgetListener listener) {
        this.listeners.remove(listener);
    }

    public boolean onTouchDown(MotionEvent event) {
        if(this.box.contains(event.getX(), event.getY())) {
            this.state = 1;
            return true;
        } else {
            return false;
        }
    }

    public boolean onTouchMove(MotionEvent event) {
        if(this.box.contains(event.getX(), event.getY())) {
            this.state = 1;
            return true;
        } else {
            this.state = 0;
            return false;
        }
    }

    public boolean onTouchUp(MotionEvent event) {
        if(!this.box.contains(event.getX(), event.getY())) {
            return false;
        } else {
            this.state = 0;
            Iterator var3 = this.listeners.iterator();

            while(var3.hasNext()) {
                WidgetListener l = (WidgetListener)var3.next();
                l.actionPerformed(new WidgetAction(this));
            }

            return true;
        }
    }

}
