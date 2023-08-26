package controller;



import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: Weapon
 * * @version 1.2
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 24, 2023
 * This class  Handles Weapons in the game.
 */
public class Weapon extends Item{

    private int attack;

    /**
     * Constructor: Weapon
     */
    public Weapon() {
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Method: toString
     * @return
     */
    @Override
    public String toString() {
        return super.toString() + "\n-> Attack - " + attack;
    }

}