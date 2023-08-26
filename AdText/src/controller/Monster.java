package controller;

import model.MonsterDB;
import model.MonsterDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class: Monster
 * * @version 1.2
 *  * Course: ITEC 3860 Spring 2023
 *  * Written: June 24, 2023
 * This class  Handles Monsters in the game.
 */
public class Monster {

    private int monsterID;
    private String monsterName;
    private String monsterDescription;
    private int hp;
    private int minDamage;
    private int maxDamage;
    private int exp;
    private HashMap<Item, Integer> drops; //possible drops, <The Item, the probability>

    private ArrayList<MonsterAttack> attackList;

    /**
     * Constructor: Monster
     */
    public Monster() {
        MonsterDB mdb = new MonsterDB();
        try{
            monsterID = mdb.getNextMonsterID();
        }
        catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
    }

    /**
     * Method: getMonster
     * Purpose: Retrieves a Monster based upon the supplied ID
     * @param id
     * @return Monster
     * @throws SQLException
     */
    public Monster getMonster(int id) throws SQLException, ClassNotFoundException {
        MonsterDB mdb = new MonsterDB();
        return mdb.getMonster(id);
    }

    public ArrayList<Monster> getAllMonsters() throws SQLException, ClassNotFoundException {
        MonsterDB mdb = new MonsterDB();
        return mdb.getAllMonsters();
    }

    /**
     * Method: getMonsterID
     * @return the MonsterID
     */
    public int getMonsterID() {
        return monsterID;
    }

    /**
     * Method: setMonsterID
     * @param monsterID the MonsterID to set
     */
    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    /**
     * Method: getMonsterName
     * @return the MonsterName
     */
    public String getMonsterName() {
        return monsterName;
    }

    /**
     * Method: setMonsterName
     * @param monsterName the monnsterName to set
     */
    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    /**
     * Method: getMonsterDescription
     * @return the monsterDescription
     */
    public String getMonsterDescription() {
        return monsterDescription;
    }

    /**
     * Method: setMonsterDescription
     * @param monsterDescription the monsterDescription to set
     */
    public void setMonsterDescription(String monsterDescription) {
        this.monsterDescription = monsterDescription;
    }

    /**
     * Method: get minDamage
     * @return the minDamage
     */
    public int getMinDamage() {
        return minDamage;
    }

    /**
     * Method: setMinDamage
     * @param minDamage the minDamage to set
     */
    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    /**
     * Method: getMaxDamage
     * @return the maxDamage
     */
    public int getMaxDamage() {
        return maxDamage;
    }

    /**
     * Method: setMaxDamage
     * @param maxDamage the maxDamage to set
     */
    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public HashMap<Item, Integer> getDrops() {
        return drops;
    }

    public void setDrops(HashMap<Item, Integer> drops) {
        this.drops = drops;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public ArrayList<MonsterAttack> getAttackList() {
        return attackList;
    }

    public void setAttackList(ArrayList<MonsterAttack> attackList) {
        this.attackList = attackList;
    }

    /**
     * Method: toString
     * @return
     */



    @Override
    public String toString() {
        return "Monster MonsterID = " + monsterID + "\nMonsterName = " + monsterName +
                "\nMonsterDescription = " + monsterDescription + "\nHP = " + hp +
                "\nMaxDamage = " + maxDamage + "\nMinDamage = " + minDamage +
                "\nExperience = " + exp + " \nDrops = " + drops;
    }

}