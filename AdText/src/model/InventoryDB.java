package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.Inventory;
import controller.Item;
import controller.Weapon;

/**
 * Class : InventoryDB
 * @author: Alex Xiong
 * @version: 1.0
 * Course: ITEC 3860
 * Written: June 26, 2023
 * This class handles all of the DB interactions for Inventorys
 */
public class InventoryDB {
    /**
     * Method: getNextInventoryID
     * Purpose: gets the next ID for an Inventory
     * @return int
     */
    public int getNextInventory() throws SQLException {
        SQLiteDB sdb = null;
        try {
            sdb = new SQLiteDB();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        int next = sdb.getMaxValue("ItemID", "Inventory") + 1;
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();

        return next;
    }

    /**
     * Method: getInventory
     * Purpose: Gets Inventory based upon the supplied ID
     * @param playerID
     * @return Inventory
     * @throws SQLException
     */
    public Inventory getInventory(int playerID) throws SQLException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        Inventory inventory = new Inventory();
        String sql = "Select * from Inventory WHERE PlayerID = " + playerID;

        ResultSet rs = sdb.queryDB(sql);

        // ========================================NEED TO FIX so it throws SQL exception================
//        if (rs.next()) {



            inventory.setPlayerID(rs.getInt("PlayerID"));

            while (rs.next()) {

                if(inventory.getInventory() != null) {
                    ArrayList <Item> updatedList = inventory.getInventory();
                    Item item = new Item(); //Obtain access to all items
                    updatedList.add(item.getItem(rs.getInt("ItemID"))); //grabs correct Item
                    inventory.setInventory(updatedList); //updates inventory with new item

                } else {
                    ArrayList <Item> updatedList = new ArrayList<>();
                    Item item = new Item(); //Obtain access to all items
                    updatedList.add(item.getItem(rs.getInt("ItemID"))); //grabs correct Item
                    inventory.setInventory(updatedList); //updates inventory with new item
                }
            }




//        }
//        else {
//            throw new SQLException("PlayerID " + playerID + " not found");
//        }
        //Close the SQLiteDB connection since SQLite only allows one updater



        sdb.close();
        return inventory;
    }
}
