package controller;



import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: Armor
 * * @version 1.2
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 23, 2023
 * This class  Handles Armors in the game.
 */
public class Armor extends Item{

    private int defense;

    /**
     * Constructor: Armor
     */
    public Armor() {
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Method: toString
     * @return
     */
    @Override
    public String toString() {
        return super.toString() + "\n-> Defense - " + defense;
    }

}