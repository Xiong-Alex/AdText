package controller;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: Exit
 * * @version 1.2
 * * @author Alex Xiong
 * * Course: ITEC 3860 Spring 2023
 * * Written: June 21, 2023
 * This class  Handles Exits in the game.
 */
public class Exit {

    private int roomID;
    private int exitID;
    private String exitDirection;
    private boolean locked;
    private int keyID;

    /**
     * Constructor: Exit
     */
    public Exit() {
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getExitID() {
        return exitID;
    }

    public void setExitID(int exitID) {
        this.exitID = exitID;
    }

    public String getExitDirection() {
        return exitDirection;
    }

    public void setExitDirection(String exitDirection) {
        this.exitDirection = exitDirection;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    /**
     * Method: toString
     *
     * @return
     */
    @Override
    public String toString() {
        return exitDirection + " " + exitID;
    }

}