package controller;

/**
 * Class: MonsterAttack
 * * @version 1
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 28, 2023
 * This class handles what attacks which monsters have
 */

public class MonsterAttack {
    private int attackID;
    private String attackName;
    private double critDamage;
    private int critChance;
    private int accuracy;

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
}
