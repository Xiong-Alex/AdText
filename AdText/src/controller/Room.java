package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import model.RoomDB;
import view.FormatText;

/**
 * Class : Room
 * @author: Alex Xiong, Khales Rahman
 * @version: 1.3
 * Course: ITEC 3860
 * Written: June 23, 2023
 * This class is the business logic for the Room objects
 */
public class Room {
    private int roomID;
    private String roomName;
    private String roomDescription;
    private ArrayList<Exit> exits;
    private boolean visited;
    private boolean solved;

    private ArrayList<Monster> monsters;
    private ArrayList<Item> items;

    private final FormatText ft = new FormatText();

    /**
     * Constructor: Room
     */
    public Room() {
        RoomDB rdb = new RoomDB();
        try{
            roomID = rdb.getNextRoomID();
            }
        catch(SQLException sqe){
            System.out.println(sqe.getMessage());
        }
    }

    /**
     * Method: getRoom
     * Purpose: Retrieves a room based upon the supplied ID
     * @param id
     * @return Room
     * @throws SQLException
     */
    public Room getRoom(int id) throws SQLException, ClassNotFoundException {
        RoomDB rdb = new RoomDB();
        return rdb.getRoom(id);
    }

    public ArrayList<Room> getAllRooms() throws SQLException, ClassNotFoundException {
        RoomDB rdb = new RoomDB();
        return rdb.getAllRooms();
    }

    //Not utilizing atm, however might be useful later
    public ArrayList<Room> getAllPlayerRooms(Player player) throws SQLException, ClassNotFoundException {
        RoomDB rdb = new RoomDB();
        return rdb.getAllPlayerRooms(player);
    }

    public Room getPlayerRoom(int id, Player player) throws SQLException, ClassNotFoundException {
        RoomDB rdb = new RoomDB();
        return rdb.getPlayerRoom(id, player);
    }

    /**
     * Method: getRoomID
     * @return the roomID
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Method: setRoomID
     * @param roomID the roomID to set
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Method: getRoomName
     * @return the roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Method: setRoomName
     * @param roomName the roomName to set
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Method: getRoomDescription
     * @return the roomDescription
     */
    public String getRoomDescription() {
        return ft.breakLines(roomDescription);
    }

    /**
     * Method: setRoomDescription
     * @param roomDescription the roomDescription to set
     */
    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public ArrayList<Exit> getExits() {
        return exits;
    }

    public void setExits(ArrayList<Exit> exits) {
        this.exits = exits;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }


    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Method: toString
     * @return
     */
    @Override
    public String toString() {
        return "Room roomID - " + roomID +
                "\nroomName - " + roomName +
                "\nroomDescription - " + roomDescription +
                "\nvisited - " + visited +
                "\nItems - " + items +
                "\nMonsters - " + monsters +
                "\n\nexits - " + exits;
    }

}
