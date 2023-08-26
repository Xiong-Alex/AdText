package controller;

import model.ItemDB;
import model.ItemDB;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: Item
 * * @version 1.2
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 27, 2023
 * This class  Handles Items in the game.
 */
public class Item {

    private int itemID;
    private String itemName;
    private String itemDescription;
    private String useDialogue;

    /**
     * Constructor: Item
     */
    public Item() {
        ItemDB idb = new ItemDB();
        try{
            itemID = idb.getNextItemID();
        }
        catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
    }

    /**
     * Method: getItem
     * Purpose: Retrieves a Item based upon the supplied ID
     * @param id
     * @return Item
     * @throws SQLException
     */
    public Item getItem(int id) throws SQLException, ClassNotFoundException {
        ItemDB idb = new ItemDB();
        return idb.getItem(id);
    }

    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
        ItemDB idb = new ItemDB();
        return idb.getAllItems();
    }

    /**
     * Method: getItemID
     * @return the ItemID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Method: setItemID
     * @param itemID the ItemID to set
     */
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /**
     * Method: getItemName
     * @return the ItemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Method: setItemName
     * @param ItemName the ItemName to set
     */
    public void setItemName(String ItemName) {
        this.itemName = ItemName;
    }

    /**
     * Method: getItemDescription
     * @return the ItemDescription
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Method: setItemDescription
     * @param ItemDescription the ItemDescription to set
     */
    public void setItemDescription(String ItemDescription) {
        this.itemDescription = ItemDescription;
    }

    public String getUseDialogue() {
        return useDialogue;
    }

    public void setUseDialogue(String useDialogue) {
        this.useDialogue = useDialogue;
    }

    /**
     * Method: toString
     * @return
     */
    @Override
    public String toString() {
        return itemName;
    }

}