package controller;

import model.ItemDB;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: ItemRoom
 * * @version 1
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 24, 2023
 * This class handles which rooms are populated with items
 */

public class ItemRoom {

    private int itemID;
    private int roomID;

    public ArrayList<ItemRoom> getAllItemRooms() throws SQLException, ClassNotFoundException {
        ItemDB idb = new ItemDB();
        return idb.getAllItemRooms();
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
