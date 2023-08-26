package controller;



import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: Accessory
 * * @version 1.2
 * * @author Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 22, 2023
 * This class  Handles Accessories in the game.
 */
public class Accessory extends Item{

    private int hp;
    private int def;
    private int sta;
    private int str;
    private int mp;
    private int atk;


    /**
     * Constructor: Accessory
     */
    public Accessory() {
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getSta() {
        return sta;
    }

    public void setSta(int sta) {
        this.sta = sta;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    /**
     * Method: toString
     * @return
     * UDPATE LATER
     */
    @Override
    public String toString() {
        return super.toString();
    }

}