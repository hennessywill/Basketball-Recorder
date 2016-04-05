package com.will.basketballrecorder;

/**
 * Created by willhennessy on 4/5/16.
 */
public class GameEvent {

    private String action;
    private float x;
    private float y;

    public GameEvent(String action, float x, float y) {
        this.action = action;
        this.x = x;
        this.y = y;
    }

    public String getAction() {
        return action;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
