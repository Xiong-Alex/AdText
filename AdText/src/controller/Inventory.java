package controller;

import model.InventoryDB;
import model.InventoryDB;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: Inventory
 * * @version 1.2
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Spring 2023
 *  * Written: July 1, 2023
 * This class  Handles Inventory in the game.
 */
public class Inventory {

    private int playerID;
    private ArrayList<Item> inventory;

    /**
     * Constructor: Inventory
     */
    public Inventory() {
        InventoryDB idb = new InventoryDB();
        try{
            playerID = idb.getNextInventory();
        }
        catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
    }

    /**
     * Method: getInventory
     * Purpose: Retrieves a Inventory based upon the supplied ID
     * @param id
     * @return Inventory
     * @throws SQLException
     */
    public Inventory getInventory(int id) throws SQLException, ClassNotFoundException {
        InventoryDB idb = new InventoryDB();
        return idb.getInventory(id);
    }

//    public ArrayList<Inventory> getAllInventory() throws SQLException, ClassNotFoundException {
//        InventoryDB idb = new InventoryDB();
//        return idb.getAllInventorys();
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method: toString
     * @return
     */
    @Override
    public String toString() {
        return  "PlayerID " + playerID + " Inventory\n" + inventory;
    }

}