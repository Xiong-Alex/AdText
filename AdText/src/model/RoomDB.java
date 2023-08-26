package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.*;

/**
 * Class : RoomDB
 * @author: Alex Xiong
 * @version: 1.0
 * Course: ITEC 3860
 * Written: July 7, 2023
 * This class handles all of the DB interactions for Rooms
 */
public class RoomDB {
    /**
     * Method: getNextRoomID
     * Purpose: gets the next ID for a room
     * @return int
     */
    public int getNextRoomID() throws SQLException {
        SQLiteDB sdb = null;
        try {
            sdb = new SQLiteDB();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        int next = sdb.getMaxValue("roomID", "room") + 1;
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();

        return next;
    }

    /**
     * Method: getRoom
     * Purpose: Gets a room based upon the supplied ID
     * @param id
     * @return Room
     * @throws SQLException
     */
    public Room getRoom(int id) throws SQLException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        Room rm = new Room();
        String sql = "Select * from Room WHERE roomID = " + id;
        ResultSet rs = sdb.queryDB(sql);
        if (rs.next()) {
            rm.setRoomID(rs.getInt("roomID"));
            rm.setRoomName(rs.getString("Name"));
            rm.setRoomDescription(rs.getString("UnsolvedDescription"));
//            rm.setVisited(rs.getBoolean("Visited"));

            //Exits subquery
            ArrayList<Exit> exits = new ArrayList<>();
            ResultSet ers = sdb.queryDB("Select * from Exit where RoomID = " + rm.getRoomID());
            while (ers.next()) {
                Exit exit = new Exit();

                exit.setRoomID(rm.getRoomID());
                exit.setExitID(ers.getInt("ExitID"));
                exit.setExitDirection(ers.getString("Direction"));
                exit.setKeyID(ers.getInt("KeyID"));
                exit.setLocked(ers.getBoolean("Locked"));
                exits.add(exit);
            }

            //Items subquery
            ArrayList<Item> items = new ArrayList<>();
            ResultSet irs = sdb.queryDB("Select * from ItemRoom  where RoomID = " + rm.getRoomID());
            while(irs.next()) {
                Item item = new Item();
                items.add(item.getItem(irs.getInt("ItemID")));
            }

            //Monster subquery
            ArrayList<Monster> monsters = new ArrayList<>();
            ResultSet mrs = sdb.queryDB("Select * from MonsterRoom where RoomID = " + rm.getRoomID());
            while(mrs.next()) {
                Monster monster = new Monster();
                monsters.add(monster.getMonster(mrs.getInt("MonsterID")));
            }

            rm.setItems(items);
            rm.setMonsters(monsters);
            rm.setExits(exits);
        }
        else {
            throw new SQLException("Room " + id + " not found");
        }
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return rm;
    }

    /**
     * Method: getAllRooms
     * Purpose: gets all rooms
     * @return ArrayList<Room>
     * @throws SQLException
     */
    public ArrayList<Room> getAllRooms() throws SQLException, ClassNotFoundException {
        ArrayList<Room> rooms = new ArrayList<Room>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from Room";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            Room rm = new Room();
            rm.setRoomID(rs.getInt("RoomID"));
            rm.setRoomName(rs.getString("Name"));
            rm.setRoomDescription(rs.getString("UnsolvedDescription"));
//            rm.setVisited(rs.getBoolean("Visited"));

            //Subquery
            ArrayList<Exit> exits = new ArrayList<>();
            ResultSet ers = sdb.queryDB("Select * from Exit where RoomID = " + rm.getRoomID());
            while (ers.next()) {
                Exit exit = new Exit();

                exit.setRoomID(rm.getRoomID());
                exit.setExitID(ers.getInt("ExitID"));
                exit.setExitDirection(ers.getString("Direction"));
                exit.setKeyID(ers.getInt("KeyID"));
                exit.setLocked(ers.getBoolean("Locked"));

                exits.add(exit);
            }

            //Items subquery
            ArrayList<Item> items = new ArrayList<>();
            ResultSet irs = sdb.queryDB("Select * from ItemRoom  where RoomID = " + rm.getRoomID());
            while(irs.next()) {
                Item item = new Item();
                items.add(item.getItem(irs.getInt("ItemID")));
            }

            //Monster subquery
            ArrayList<Monster> monsters = new ArrayList<>();
            ResultSet mrs = sdb.queryDB("Select * from MonsterRoom where RoomID = " + rm.getRoomID());
            while(mrs.next()) {
                Monster monster = new Monster();
                monsters.add(monster.getMonster(mrs.getInt("MonsterID")));
            }

            rm.setItems(items);
            rm.setMonsters(monsters);
            rm.setExits(exits);

            rooms.add(rm);
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return rooms;
    }
    ///////////////////////////

    /**
     * Method: getPlayerRoom
     * Purpose: Gets a room based upon the supplied ID
     * @param id
     * @return Room
     * @throws SQLException
     */
    public Room getPlayerRoom(int id, Player player) throws SQLException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        Room rm = new Room();
        String sql = "Select r.RoomID, r.Name, r.UnsolvedDescription, r.SolvedDescription, p.RoomSolved, p.Visited from Room r JOIN PlayerRoom p On r.RoomID = p.RoomID where p.PlayerID = " + player.getPlayerID() + " AND r.RoomID = " + id;
        ResultSet rs = sdb.queryDB(sql);
        if (rs.next()) {
            rm.setRoomID(rs.getInt("roomID"));
            rm.setRoomName(rs.getString("Name"));
            rm.setVisited(rs.getBoolean("Visited"));

            if(rs.getInt("roomID") == 1) {
                rm.setVisited(true);
            }

            if(rs.getBoolean("RoomSolved")) {
                rm.setRoomDescription(rs.getString("SolvedDescription"));
            } else {
                rm.setRoomDescription(rs.getString("UnsolvedDescription"));
            }

            //Exits subquery
            ArrayList<Exit> exits = new ArrayList<>();


            ResultSet ers = sdb.queryDB("Select p.ExitID, p.RoomID, " +
                    "p.Locked, e.KeyID, e.Direction from PlayerExit p JOIN " +
                    "Exit e ON p.ExitID = e.ExitID AND p.RoomID = e.RoomID WHERE p.PlayerID = "
                    + player.getPlayerID() + " AND p.RoomID = " + id);

            while (ers.next()) {
                Exit exit = new Exit();

                exit.setRoomID(rm.getRoomID());
                exit.setExitID(ers.getInt("ExitID"));
                exit.setExitDirection(ers.getString("Direction"));
                exit.setKeyID(ers.getInt("KeyID"));
                exit.setLocked(ers.getBoolean("Locked"));
                exits.add(exit);
            }

            //Items subquery
            ArrayList<Item> items = new ArrayList<>();
            ResultSet irs = sdb.queryDB("Select * from PlayerItemRoom where RoomID = " + rm.getRoomID() + " AND PlayerID = " + player.getPlayerID());
            while(irs.next()) {
                Item item = new Item();
                items.add(item.getItem(irs.getInt("ItemID")));
            }

            //Monster subquery
            ArrayList<Monster> monsters = new ArrayList<>();
            ResultSet mrs = sdb.queryDB("Select * from PlayerMonsterRoom where RoomID = " + rm.getRoomID() + " AND PlayerID = " + player.getPlayerID());
            while(mrs.next()) {
                Monster monster = new Monster();
                monsters.add(monster.getMonster(mrs.getInt("MonsterID")));
            }

            rm.setItems(items);
            rm.setMonsters(monsters);
            rm.setExits(exits);
        }
        else {
            throw new SQLException("Room " + id + " not found");
        }
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return rm;
    }

    /**
     * Method: getAllPlayerRooms
     * Purpose: gets all rooms
     * @return ArrayList<Room>
     * @throws SQLException
     */
    public ArrayList<Room> getAllPlayerRooms(Player player) throws SQLException, ClassNotFoundException {
        ArrayList<Room> rooms = new ArrayList<Room>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select r.RoomID, r.Name, r.UnsolvedDescription, r.SolvedDescription, p.RoomSolved, p.Visited from Room r JOIN PlayerRoom p On r.RoomID = p.RoomID where p.PlayerID = " + player.getPlayerID();

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            Room rm = new Room();
            rm.setRoomID(rs.getInt("RoomID"));
            rm.setRoomName(rs.getString("Name"));
            rm.setVisited(rs.getBoolean("Visited"));

            if(rs.getBoolean("RoomSolved")) {
                rm.setRoomDescription(rs.getString("SolvedDescription"));
            } else {
                rm.setRoomDescription(rs.getString("UnsolvedDescription"));
            }

            //Subquery
            ArrayList<Exit> exits = new ArrayList<>();
            ResultSet ers = sdb.queryDB("Select p.ExitID, p.RoomID, " +
                    "p.Locked, e.KeyID, e.Direction from PlayerExit p JOIN " +
                    "Exit e ON p.ExitID = e.ExitID AND p.RoomID = e.RoomID WHERE p.PlayerID = "
                    + player.getPlayerID());
            while (ers.next()) {
                Exit exit = new Exit();

                exit.setRoomID(rm.getRoomID());
                exit.setExitID(ers.getInt("ExitID"));
                exit.setExitDirection(ers.getString("Direction"));
                exit.setKeyID(ers.getInt("KeyID"));
                exit.setLocked(ers.getBoolean("Locked"));
                exits.add(exit);
            }

            //Items subquery
            ArrayList<Item> items = new ArrayList<>();
            ResultSet irs = sdb.queryDB("Select * from ItemRoom  where RoomID = " + rm.getRoomID());
            while(irs.next()) {
                Item item = new Item();
                items.add(item.getItem(irs.getInt("ItemID")));
            }

            //Monster subquery
            ArrayList<Monster> monsters = new ArrayList<>();
            ResultSet mrs = sdb.queryDB("Select * from PlayerMonsterRoom where RoomID = " + rm.getRoomID() + " AND PlayerID = " + player.getPlayerID());
            while(mrs.next()) {
                Monster monster = new Monster();
                monsters.add(monster.getMonster(mrs.getInt("MonsterID")));
            }

            rm.setItems(items);
            rm.setMonsters(monsters);
            rm.setExits(exits);

            rooms.add(rm);
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return rooms;
    }
}
