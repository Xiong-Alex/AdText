package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import controller.*;

/**
 * Class : PlayerDB.java
 * @author: Alex Xiong
 * @version: 1.0
 * Course: ITEC 3860
 * Written: June 26, 2023
 * This class handles all of the DB interactions for Players
 */
public class PlayerDB {
    /**
     * Method: getNextPlayerID
     * Purpose: gets the next ID for a Player
     * @return int
     */
    public int getNextPlayerID() throws SQLException {
        SQLiteDB sdb = null;
        try {
            sdb = new SQLiteDB();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        int next = sdb.getMaxValue("PlayerID", "Player") + 1;
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();

        return next;
    }

    /**
     * Method: getPlayer
     * Purpose: Gets a Player based upon the supplied ID
     * @param id
     * @return Player
     * @throws SQLException
     */
    public Player getPlayer(int id) throws SQLException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        Player player = new Player();
        String sql = "Select * from Player WHERE PlayerID = " + id;
        ResultSet rs = sdb.queryDB(sql);
        if (rs.next()) {
            player.setPlayerID(rs.getInt("PlayerID"));
            player.setPlayerName(rs.getString("UserName"));
            player.setLevel(rs.getInt("Level"));
            player.setExp(rs.getInt("EXP"));
            player.setHp(rs.getInt("HP"));
            player.setStr(rs.getInt("STR"));
            player.setMp(rs.getInt("MP"));
            player.setDef(rs.getInt("DEF"));
            player.setSta(rs.getInt("STA"));
            player.setSp(rs.getInt("StatPoints"));
            player.setMaxHp(rs.getInt("MaxHP"));
            player.setMaxMp(rs.getInt("MaxMp"));

            if(rs.getInt("CurrentRoom") == 0) {
                player.setCurrentRoom(1);
            } else {
                player.setCurrentRoom(rs.getInt("CurrentRoom"));
            }

            Armor armor = new Armor();
            Weapon weapon = new Weapon();
            Accessory accessory = new Accessory();

            player.setArmor((Armor) armor.getItem(rs.getInt("ArmorID")));
            player.setWeapon((Weapon) weapon.getItem(rs.getInt("WeaponID")));
            player.setAccessory((Accessory) accessory.getItem(rs.getInt("AccessoryID")));

            //Grabbing all player attacks
            String sqlForMoves = "Select * from PlayerAttacks";
            ResultSet mrs = sdb.queryDB(sqlForMoves);

            ArrayList<PlayerAttack> playerAttacks = new ArrayList<>();
            while(mrs.next()) {
                PlayerAttack playerAttack = new PlayerAttack();
                playerAttack.setAttackName(mrs.getString("AttackName"));
                playerAttack.setAttackID(mrs.getInt("AttackID"));
                playerAttack.setAccuracy(mrs.getInt("Accuracy"));
                playerAttack.setCritChance(mrs.getInt("CritChance"));
                playerAttack.setCritDamage(mrs.getDouble("CritDamage"));
                playerAttack.setMp(mrs.getInt("MP"));
                playerAttack.setDmg(mrs.getInt("DMG"));

                playerAttacks.add(playerAttack);
            }
            player.setAttackList(playerAttacks);
        }
        else {
            throw new SQLException("Player " + id + " not found");
        }
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return player;
    }

    /**
     * Method: getAllPlayers
     * Purpose: gets all Players
     * @return ArrayList<Player>
     * @throws SQLException
     */
    public ArrayList<Player> getAllPlayers() throws SQLException, ClassNotFoundException {
        ArrayList<Player> Players = new ArrayList<Player>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from Player";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            Player player = new Player();

            player.setPlayerID(rs.getInt("PlayerID"));
            player.setPlayerName(rs.getString("UserName"));
            player.setLevel(rs.getInt("Level"));
            player.setExp(rs.getInt("EXP"));
            player.setHp(rs.getInt("HP"));
            player.setStr(rs.getInt("STR"));
            player.setMp(rs.getInt("MP"));
            player.setDef(rs.getInt("DEF"));
            player.setSta(rs.getInt("STA"));
            player.setSp(rs.getInt("StatPoints"));
            player.setMaxHp(rs.getInt("MaxHP"));
            player.setMaxMp(rs.getInt("MaxMp"));


            if(rs.getInt("CurrentRoom") == 0) {
                player.setCurrentRoom(1);
            } else {
                player.setCurrentRoom(rs.getInt("CurrentRoom"));
            }
//Its broken idk why, however I only need to get userName and ID through this method
//            Armor armor = new Armor();
//            Weapon weapon = new Weapon();
//            armor = (Armor) armor.getItem(rs.getInt("ArmorID"));
//            weapon = (Weapon) weapon.getItem(rs.getInt("WeaponID"));
//
//            player.setArmor(armor);
//            player.setWeapon(weapon);

            //Grabbing all player attacks
            String sqlForMoves = "Select * from PlayerAttacks";
            ResultSet mrs = sdb.queryDB(sqlForMoves);

            ArrayList<PlayerAttack> playerAttacks = new ArrayList<>();
            while(mrs.next()) {
                PlayerAttack playerAttack = new PlayerAttack();
                playerAttack.setAttackName(mrs.getString("AttackName"));
                playerAttack.setAttackID(mrs.getInt("AttackID"));
                playerAttack.setAccuracy(mrs.getInt("Accuracy"));
                playerAttack.setCritChance(mrs.getInt("CritChance"));
                playerAttack.setCritDamage(mrs.getDouble("CritDamage"));
                playerAttack.setMp(mrs.getInt("MP"));
                playerAttack.setDmg(mrs.getInt("DMG"));


                playerAttacks.add(playerAttack);
            }
            player.setAttackList(playerAttacks);

            Players.add(player);
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return Players;
    }

    public HashMap<Integer, Integer> getAllLevels() throws SQLException, ClassNotFoundException {
        HashMap<Integer,Integer> levels = new HashMap<>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from Level";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            levels.put(rs.getInt("Level"), rs.getInt("EXP"));
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return levels;
    }
}
