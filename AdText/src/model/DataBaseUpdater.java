package model;

import controller.*;

import java.sql.SQLException;

import java.util.ArrayList;

/**
 * Class: DataBaseUpdater
 * * @version 1.4
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: June 21, 2023
 * This class updates the DB with data from player commands
 */

public class DataBaseUpdater {
    SQLiteDB sdb;
    private String dbName;

    public DataBaseUpdater() {
        dbName = "GameDataFile.db";
    }
    public DataBaseUpdater(String dbName) {
        this.dbName = dbName;
    }

    //drops and item from the inventory into the current room
    /**
     * Method dropItem
     *
     * Drops the specified item from the player's inventory and adds it to the current room.
     *
     * @param item - the item to be dropped
     * @param player - the player from whose inventory the item will be dropped
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void dropItem(Item item, Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Delete from Inventory where PlayerID = " + player.getPlayerID() + " AND ItemID = " + item.getItemID();
        sdb.updateDB(sql);
        sql = "Insert INTO PlayerItemRoom(PlayerID, ItemID, RoomID) Values(" + player.getPlayerID() + ", " + item.getItemID() +", " + player.getCurrentRoom() + ")";
        sdb.updateDB(sql);
    }
    /**
     * Method monsterDrop
     *
     * Adds the specified item to the player's inventory from a monster drop.
     *
     * @param item - the item to be added to the player's inventory
     * @param player - the player who will receive the item
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void monsterDrop(Item item, Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Insert INTO PlayerItemRoom(PlayerID, ItemID, RoomID) Values(" + player.getPlayerID() + ", " + item.getItemID() +", " + player.getCurrentRoom() + ")";
        sdb.updateDB(sql);
    }

    //picks up an Item from the current room and into the inventory
    /**
     * Method pickUpItem
     *
     * Allows the player to pick up the specified item and add it to their inventory.
     *
     * @param item - the item to be picked up
     * @param player - the player who will pick up the item
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void pickUpItem(Item item, Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Insert INTO Inventory(PlayerID, ItemID) Values (" + player.getPlayerID() +", " + item.getItemID() + ")";
        sdb.updateDB(sql);
        sql = "Delete from PlayerItemRoom where ItemID = " + item.getItemID() + " AND RoomID = " + player.getCurrentRoom() + " AND PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    //Utilize this later to respawn monsters
    /**
     * Method addMonsterToPlayerRoom
     *
     * Adds the specified monster to the player's current room.
     *
     * @param monster - the monster to be added to the room
     * @param player - the player whose room the monster will be added to
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void addMonsterToPlayerRoom(Monster monster, Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Insert INTO PlayerMonsterRoom(MonsterID, RoomID, PlayerID) Values (" + monster.getMonsterID() +", " + player.getCurrentRoom() + " AND PlayerID = " + player.getPlayerID() + ")";
        sdb.updateDB(sql);
    }

    //Remove monster after defeating them
    /**
     * Method removeMonsterFromPlayerRoom
     *
     * Removes the specified monster from the player's current room.
     *
     * @param monster - the monster to be removed from the room
     * @param player - the player whose room the monster will be removed from
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void removeMonsterFromPlayerRoom(Monster monster, Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Delete from PlayerMonsterRoom where MonsterID = " + monster.getMonsterID() + " AND RoomID = " + player.getCurrentRoom() + " AND PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    //sets your current room to a new room & updates the room to be visited
    /**
     * Method updateCurrentRoom
     *
     * Updates the player's current room to the specified room and marks the room as visited.
     *
     * @param player - the player whose current room will be updated
     * @param room - the room to set as the player's current room
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void updateCurrentRoom(Player player, Room room) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET CurrentRoom = " + room.getRoomID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "UPDATE PlayerRoom SET Visited = 1 Where RoomID = " + room.getRoomID() + " and PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }


    //Equipping Armor
    /**
     * Method equipArmor
     *
     * Equips the armor item for the player.
     *
     * @param player - the player to equip the armor item for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void equipArmor(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET ArmorID = " + player.getArmor().getItemID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "Delete from Inventory where PlayerID = " + player.getPlayerID() + " AND ItemID = " + player.getArmor().getItemID();
        sdb.updateDB(sql);
    }

    //Unequipped Armor
    /**
     * Method unEquipArmor
     *
     * Unequips the armor item for the player.
     *
     * @param player - the player to unequip the armor item for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void unEquipArmor(Player player, Item armor) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Insert INTO Inventory(PlayerID, ItemID) Values (" + player.getPlayerID() +", " + armor.getItemID() + ")";
        sdb.updateDB(sql);
        sql = "UPDATE Player SET ArmorID = " + player.getArmor().getItemID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    //Equipping Weapon
    /**
     * Method equipWeapon
     *
     * Equips the weapon item for the player.
     *
     * @param player - the player to equip the weapon item for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void equipWeapon(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET WeaponID = " + player.getWeapon().getItemID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "Delete from Inventory where PlayerID = " + player.getPlayerID() + " AND ItemID = " + player.getWeapon().getItemID();
        sdb.updateDB(sql);
    }

    //Unequipped Weapon
    /**
     * Method unEquipWeapon
     *
     * Unequips the weapon item from the player.
     *
     * @param player - the player to unequip the weapon item from
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void unEquipWeapon(Player player, Item weapon) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Insert INTO Inventory(PlayerID, ItemID) Values (" + player.getPlayerID() +", " + weapon.getItemID() + ")";
        sdb.updateDB(sql);
        sql = "UPDATE Player SET WeaponID = " + player.getWeapon().getItemID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    //Equipping Accessory
    /**
     * Method equipAccessory
     *
     * Equips the accessory item for the player.
     *
     * @param player - the player to equip the accessory item for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void equipAccessory(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET AccessoryID = " + player.getAccessory().getItemID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "Delete from Inventory where PlayerID = " + player.getPlayerID() + " AND ItemID = " + player.getAccessory().getItemID();
        sdb.updateDB(sql);
    }

    //Unequipped Accessory
    /**
     * Method unEquipAccessory
     *
     * Unequips the accessory item for the player.
     *
     * @param player - the player to unequip the accessory item for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void unEquipAccessory(Player player, Item accessory) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "Insert INTO Inventory(PlayerID, ItemID) Values (" + player.getPlayerID() +", " + accessory.getItemID() + ")";
        sdb.updateDB(sql);
        sql = "UPDATE Player SET AccessoryID = " + player.getAccessory().getItemID()  + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }


    ////////////////////////////////___UPDATING___PLAYER___STATS___/////////////////////////////////////////////////////
    /*
     Additional features. Implement code if time permits.
    */

    /**
     * Method allocateSPtoMaxHp
     *
     * Allocates stat points to increase the maximum HP of the player.
     *
     * @param player - the player to allocate stat points for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void allocateSPtoMaxHp(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET MaxHP = " + player.getMaxHp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "UPDATE Player SET StatPoints = " + player.getSp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method allocateSPtoStr
     *
     * Allocates stat points to increase the STR (strength) stat of the player.
     *
     * @param player - the player to allocate stat points for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void allocateSPtoStr(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET STR = " + player.getStr() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "UPDATE Player SET StatPoints = " + player.getSp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method allocateSPtoDef
     *
     * Allocates stat points to increase the DEF (defense) stat of the player.
     *
     * @param player - the player to allocate stat points for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void allocateSPtoDef(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET DEF = " + player.getDef() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "UPDATE Player SET StatPoints = " + player.getSp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method allocateSPtoSta
     *
     * Allocates stat points to increase the STA (stamina) stat of the player.
     *
     * @param player - the player to allocate stat points for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void allocateSPtoSta(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET STA = " + player.getSta() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "UPDATE Player SET StatPoints = " + player.getSp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method allocateSPtoMaxMp
     *
     * Allocates stat points to increase the MaxMP (maximum mana points) stat of the player.
     *
     * @param player - the player to allocate stat points for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void allocateSPtoMaxMp(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET MaxMP = " + player.getMaxMp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
        sql = "UPDATE Player SET StatPoints = " + player.getSp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method updatePlayerMp
     *
     * Updates the current MP (mana points) of the player in the database.
     *
     * @param player - the player to update MP for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void updatePlayerMp(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET MP = " + player.getMaxMp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    public void updatePlayerStat(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET MaxHP = " + player.getMaxMp() + ", HP = " + player.getHp() + ", MaxMP = "
                + player.getMaxMp() + ", MP = " + player.getMp() + ", DEF = " + player.getDef() + ", STR =" + player.getStr() +
                ", STA =" + player.getSta() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }


    /**
     * Method increasePlayerLevel
     *
     * Increases the level of the player in the database.
     *
     * @param player - the player to increase the level for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void increasePlayerLevel(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET StatPoints = " + player.getSp() +
                ", Level = " + player.getLevel() + ", EXP = " + player.getExp() +
                ", MaxHP = " + player.getMaxHp() + ", HP = " + player.getHp() +
                ", MaxMP = " + player.getMaxMp() + ", MP = " + player.getMp() +
                ", DEF = " + player.getDef() + ", STA = " + player.getSta() +
                ", STR = " + player.getStr() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method increasePlayerExp
     *
     * Increases the experience points of the player in the database.
     *
     * @param player - the player to increase the experience points for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void increasePlayerExp(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET EXP = " + player.getExp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method updatePlayerHP
     *
     * Updates the player's current HP in the database.
     *
     * @param player - the player to update the HP for
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void updatePlayerHP(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE Player SET HP = " + player.getHp() + " WHERE PlayerID = " + player.getPlayerID();
        sdb.updateDB(sql);
    }

    /**
     * Method useItem
     *
     * Removes the specified item from the player's inventory.
     *
     * @param player - the player who is using the item
     * @param item - the item to be used
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void useItem(Player player, Item item) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "DELETE FROM Inventory where PlayerID = " + player.getPlayerID() + " AND ItemID = " + item.getItemID();
        sdb.updateDB(sql);
    }

    /**
     * Method roomSolved
     *
     * Marks the specified room as solved for the player.
     *
     * @param player - the player who solved the room
     * @param room - the room that has been solved
     * @param exit - the exact exit that has been solved
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void roomSolved(Player player, Room room, Exit exit) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "UPDATE PlayerRoom Set RoomSolved = 1 where PlayerID = " + player.getPlayerID() + " AND RoomID = " + room.getRoomID();
        sdb.updateDB(sql);
        sql = "UPDATE PlayerExit set Locked = 0 where PlayerID = " + player.getPlayerID() + " and RoomID = " + room.getRoomID() + " AND ExitID = " + exit.getExitID();
        sdb.updateDB(sql);

    }

    /**
     * Method createPlayer
     *
     * Creates a new player with the specified name and initializes their attributes.
     *
     * @param playerName - the name of the player
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void createPlayer(String playerName) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "INSERT into Player (UserName, HP, STR, DEF, STA, MP, Level, EXP, StatPoints, WeaponID, ArmorID, CurrentRoom, MaxHP, MaxMP, AccessoryID) VALUES (\"" + playerName + "\", 5, 2, 2, 2, 2 ,1, 0, 10, 33, 34, 1, 5, 2, 35)";
        sdb.updateDB(sql);


        ArrayList<Player> playerList = new ArrayList<>();
        Player player = new Player();
        playerList = player.getAllPlayers();

        for(Player plr : playerList) {
            String tempName = "";
            tempName = plr.getPlayerName();

            tempName.toUpperCase();
            playerName.toUpperCase();

            if(tempName.contains(playerName)) {
                player = player.getPlayer(plr.getPlayerID());
                break;
            }
        }
        try {
            populatePlayerRooms(player);
            populatePlayerExits(player);
            populatePlayerMonsters(player);
            populatePlayerItems(player);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method populatePlayerRooms
     *
     * Populates the player's room data in the database.
     *
     * @param player - the player object
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void populatePlayerRooms(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "";

        ArrayList <Room> rooms = new ArrayList<>();
        Room room = new Room();
        rooms = room.getAllRooms();


        for (Room rm : rooms) {
            sql = "INSERT into PlayerRoom (RoomID, PlayerID, Visited, RoomSolved) Values (" +
                    rm.getRoomID() + ", " + player.getPlayerID() + ", 0, 0)"; //all rooms are defaulted to not solve, despite some rooms not being puzzles
            sdb.updateDB(sql);
        }
    }

    /**
     * Method populatePlayerExits
     *
     * Populates the player's exit data in the database.
     *
     * @param player - the player object
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void populatePlayerExits(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "";

        ArrayList<Exit> exits = new ArrayList<>();

        ArrayList <Room> rooms = new ArrayList<>();
        Room room = new Room();
        rooms = room.getAllRooms();

        for (Room rm : rooms) {
            exits.addAll(rm.getExits());
        }


        for (Exit exit : exits) {
            sql = "INSERT into PlayerExit (ExitID, RoomID, Locked, PlayerID) Values (" +
                    exit.getExitID() + ", " + exit.getRoomID() + ", " + exit.isLocked() +
                    ", " + player.getPlayerID() + ")";
            sdb.updateDB(sql);
        }
    }

    /**
     * Method populatePlayerMonsters
     *
     * Populates the player's monster data in the database.
     *
     * @param player - the player object
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void populatePlayerMonsters(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "";

        //Grabs table for MonsterRoom (relationship between a monster and the room they reside in)
        MonsterRoom monsterRoom = new MonsterRoom();
        ArrayList<MonsterRoom> pmrList = monsterRoom.getAllMonsterRoom();

        //Creates a private "MonsterRoom" for the player (So monster still exists for a player even if another player kills it)
        for (MonsterRoom pmr : pmrList) {
            sql = "INSERT into PlayerMonsterRoom (MonsterID, RoomID, PlayerID) Values" +
                    " (" + pmr.getMonsterID() + ", " + pmr.getRoomID() + ", "
                    + player.getPlayerID() + ")";
            sdb.updateDB(sql);
        }
    }

    /**
     * Method populatePlayerItems
     *
     * Populates the player's item data in the database.
     *
     * @param player - the player object
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the required class is not found
     */
    public void populatePlayerItems(Player player) throws SQLException, ClassNotFoundException {
        sdb = new SQLiteDB(dbName);
        String sql = "";

        //Grabs table for ItemRoom (relationship between an Item and the room they reside in)
        ItemRoom itemRoom = new ItemRoom();
        ArrayList<ItemRoom> pirList = itemRoom.getAllItemRooms();

        //Creates a private "MonsterRoom" for the player (So item still exists for a player even if another player grabs it)
        for (ItemRoom pir : pirList) {
            sql = "INSERT into PlayerItemRoom (ItemID, RoomID, PlayerID) Values" +
                    " (" + pir.getItemID() + ", " + pir.getRoomID() + ", "
                    + player.getPlayerID() + ")";
            sdb.updateDB(sql);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
