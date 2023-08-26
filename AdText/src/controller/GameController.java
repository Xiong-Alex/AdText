package controller;

import controller.Commands;
import controller.GameException;
import model.RoomDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class: GameController
 * @version 1.2
 * @author: Alex Xiong, Khales Rahman
 * Course: ITEC 3860 Summer 2023
 * Written: July 6, 2023
 * This class � Is the controller interface for mini game 2
 */
public class GameController {

    public static final int FIRST_ROOM = 1;
    private final Commands commands;

    /**
     * Method GameController
     * Constructor for the GameController class
     * Instatiates the Commands object for the game
     */
    public GameController() {
        try {
            commands = new Commands();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method displayFirstRoom
     * Retrieves the String for the first room
     *
     * @return - the first room display String
     * @throws GameException - if the first room is not found.
     */
    public String displayFirstRoom() throws GameException {
        return "You are currently in the " + commands.currentRoom.getRoomName();
    }
    //Fetch Room one by default if new Game.
    //Once Saving has been implemented, fetch current room from save

    /**
     * Method executeCommand
     * Handles the user input from Adventure
     * Sends the user's command to Commands for processing
     * throws an exception if the command is not valid
     *
     * @param cmd - String
     * @return String - the result from the command
     * @throws GameException if the command is invalid
     */
    public String executeCommand(String cmd) throws GameException, SQLException, ClassNotFoundException {
        String message;
        try {
            message = commands.executeCommand(cmd);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Objects.requireNonNullElse(message, "Error loading command\nPlease try again or enter 'help' to see full list of valid commands");
    }

    /**
     * Method printMap
     * Handles the print map command from Adventure
     * Builds a  String representation of the current map
     *
     * @return String - the String of the current map
     * @throws GameException if one of the files is not found or has an error
     */
    public String printMap() throws GameException, SQLException, ClassNotFoundException {
        RoomDB rdb = new RoomDB();
        ArrayList<Room> rooms = rdb.getAllRooms();
        StringBuilder map = new StringBuilder("Room Info:\n");
        for (Room room : rooms) {
            map.append(room.getRoomName()).append(" - ").append(room.getRoomDescription()).append("\n");
        }
        map.append("----------\n");
        return map.toString();
    }

    public String logIn(int playerStatus) throws SQLException, ClassNotFoundException {
        return commands.logIn(playerStatus);
    }
}