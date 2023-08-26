package controller;

import model.MonsterDB;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class: MonsterRoom
 * * @version 1
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 24, 2023
 * This class handles which rooms are populated with monsters
 */

public class MonsterRoom {
    private int monsterID;
    private  int roomID;

    public ArrayList<MonsterRoom> getAllMonsterRoom() throws SQLException, ClassNotFoundException {
        MonsterDB mdb = new MonsterDB();
        return mdb.getAllPlayerMonsters();
    }

    public int getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

}
