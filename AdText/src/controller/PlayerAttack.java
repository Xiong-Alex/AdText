package controller;

/**
 * Class: PlayerAttack
 * * @version 1.1
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Spring 2023
 *  * Written: July 1, 2023
 * This class Handles Player attacks
 */
public class PlayerAttack {

    private int attackID;
    private String attackName;
    private double critDamage;
    private int critChance;
    private int accuracy;
    private int mp;
    private int dmg;

    public int getAttackID() {
        return attackID;
    }

    public void setAttackID(int attackID) {
        this.attackID = attackID;
    }

    public String getAttackName() {
        return attackName;
    }

    public void setAttackName(String attackName) {
        this.attackName = attackName;
    }

    public double getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(double critDamage) {
        this.critDamage = critDamage;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }
}
