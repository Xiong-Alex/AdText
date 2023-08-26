package controller;

/**
 * Class: Consumable
 * * @version 1
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 21, 2023
 * This class branches from Item and creates a Consumable Object
 */

public class Consumable extends Item{

    private int hp;
    private int mp;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    @Override
    public String toString() {
        if(hp != 0) {
            return super.toString() + " HP: " + hp;
        } else  {
            return super.toString() + "MP: " + mp;
        }
    }
}
