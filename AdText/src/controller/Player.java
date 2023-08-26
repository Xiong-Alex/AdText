package controller;

import model.PlayerDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class: Player
 * * @version 1.2
 * * @author Alex Xiong
 *  * Course: ITEC 3860 Spring 2023
 *  * Written: June 21, 2023
 * This class  Handles Players in the game.
 */
public class Player {

    private int playerID;
    private String playerName;
    private int level;
    private int exp;
    private int maxHp;
    private int hp;
    private int str;
    private int def;
    private int sta;
    private int maxMp;
    private int mp;
    private int sp;//Stat Points
    private Weapon weapon;
    private Armor armor;
    private Accessory accessory;
    private ArrayList<PlayerAttack> attackList;

    private int currentRoom;


    /**
     * Constructor: Player
     */
    public Player() {
        PlayerDB pdb = new PlayerDB();
        try{
            playerID = pdb.getNextPlayerID();
        }
        catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
    }

    /**
     * Method: getPlayer
     * Purpose: Retrieves a player based upon the supplied ID
     * @param id
     * @return Player
     * @throws SQLException
     */
    public Player getPlayer(int id) throws SQLException, ClassNotFoundException {
        PlayerDB pdb = new PlayerDB();
        return pdb.getPlayer(id);
    }

    public ArrayList<Player> getAllPlayers() throws SQLException, ClassNotFoundException {
        PlayerDB pdb = new PlayerDB();
        return pdb.getAllPlayers();
    }

    /**
     * Method: getPlayerID
     * @return the playerID
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Method: setPlayerID
     * @param playerID the playerID to set
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Method: getPlayerName
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Method: setPlayerName
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    /**
     * Method: getAllLevels
     */
    public HashMap<Integer, Integer> getAllLevels() throws SQLException, ClassNotFoundException{
        PlayerDB pdb = new PlayerDB();
        return pdb.getAllLevels();
    }

    /////////////////////////////////////////////////////


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
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

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public int getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public void setAccessory(Accessory accessory) {
        this.accessory = accessory;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }


    public ArrayList<PlayerAttack> getAttackList() {
        return attackList;
    }

    public void setAttackList(ArrayList<PlayerAttack> attackList) {
        this.attackList = attackList;
    }

    /**
     * Method: toString
     * @return
     */
    @Override
    public String toString() {
        return "PlayerID = " + playerID + "\nPlayerName = " + playerName + "\nCurrent Room = " + currentRoom +
                "\nLevel = " + level + "\nEXP = " + exp +
                "\nWeapon = " + weapon.getItemName() + "\nArmor = " + armor.getItemName() +
                "\nHP = " + hp + "\nSTR = " + str + "\nMP = " + mp + "\nDEF = " +
                def + "\nSTA = " + sta + "\nSP = " + sp;
    }

}