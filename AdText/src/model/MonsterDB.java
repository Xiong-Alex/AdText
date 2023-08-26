package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import controller.Item;
import controller.Monster;
import controller.MonsterAttack;
import controller.MonsterRoom;

/**
 * Class : MonsterDB.java
 * @author: Alex Xiong
 * @version: 1.0
 * Course: ITEC 3860
 * Written: June 26, 2023
 * This class handles all of the DB interactions for Monsters
 */
public class MonsterDB {
    /**
     * Method: getNextMonsterID
     * Purpose: gets the next ID for an Monster
     * @return int
     */
    public int getNextMonsterID() throws SQLException {
        SQLiteDB sdb = null;
        try {
            sdb = new SQLiteDB();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        int next = sdb.getMaxValue("MonsterID", "Monster") + 1;
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();

        return next;
    }

    /**
     * Method: getMonster
     * Purpose: Gets a Monster based upon the supplied ID
     * @param id
     * @return Monster
     * @throws SQLException
     */
    public Monster getMonster(int id) throws SQLException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();
        Monster monster = new Monster();
        String sql = "Select * from Monster WHERE MonsterID = " + id;
        ResultSet rs = sdb.queryDB(sql);
        if (rs.next()) {
            monster.setMonsterID(rs.getInt("MonsterID"));
            monster.setMonsterName(rs.getString("Name"));
            monster.setMonsterDescription(rs.getString("Description"));
            monster.setHp(rs.getInt("HP"));
            monster.setMinDamage(rs.getInt("MinDamage"));
            monster.setMaxDamage(rs.getInt("MaxDamage"));
            monster.setExp(rs.getInt("exp"));

            //Subquery
            HashMap<Item,Integer> itemDrops = new HashMap<>();
            ResultSet idrs = sdb.queryDB("Select * from MonsterDrop where MonsterID = " +rs.getInt("MonsterID"));
            while(idrs.next()) {
                Item item = new Item();
                Integer dropChance;

                item = item.getItem(idrs.getInt("ItemID"));
                dropChance = idrs.getInt("DropChance");

                itemDrops.put(item, dropChance);
            }

            ArrayList<MonsterAttack> attackList = new ArrayList<>();
            ResultSet ars =sdb.queryDB("Select * from MonsterAttacks where MonsterID = " + id);
            while(ars.next()) {
                MonsterAttack attack = new MonsterAttack();
                attack.setAttackID(ars.getInt("AttackID"));
                attack.setAttackName(ars.getString("AttackName"));
                attack.setAccuracy(ars.getInt("Accuracy"));
                attack.setCritChance(ars.getInt("CritChance"));
                attack.setCritDamage(ars.getDouble("CritDamage"));

                attackList.add(attack);
            }

            monster.setDrops(itemDrops);
            monster.setAttackList(attackList);
        }
        else {
            throw new SQLException("Monster " + id + " not found");
        }
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return monster;
    }

    /**
     * Method: getAllMonsters
     * Purpose: gets all monsters
     * @return ArrayList<Monster>
     * @throws SQLException
     */
    public ArrayList<Monster> getAllMonsters() throws SQLException, ClassNotFoundException {
        ArrayList<Monster> monsters = new ArrayList<Monster>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from Monster";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            Monster monster = new Monster();
            monster.setMonsterID(rs.getInt("MonsterID"));
            monster.setMonsterName(rs.getString("Name"));
            monster.setMonsterDescription(rs.getString("Description"));
            monster.setHp(rs.getInt("HP"));
            monster.setMinDamage(rs.getInt("MinDamage"));
            monster.setMaxDamage(rs.getInt("MaxDamage"));
            monster.setExp(rs.getInt("exp"));

            //Subquery
            HashMap<Item,Integer> itemDrops = new HashMap<>();
            ResultSet idrs = sdb.queryDB("Select * from MonsterDrop where MonsterID = " +rs.getInt("MonsterID"));
            while(idrs.next()) {
                Item item = new Item();
                Integer dropChance;

                item = item.getItem(idrs.getInt("ItemID"));
                dropChance = idrs.getInt("DropChance");

                itemDrops.put(item, dropChance);
            }

            ArrayList<MonsterAttack> attackList = new ArrayList<>();
            ResultSet ars =sdb.queryDB("Select * from MonsterAttacks where MonsterID = " + rs.getInt("MonsterID"));
            while(ars.next()) {
                MonsterAttack attack = new MonsterAttack();
                attack.setAttackID(ars.getInt("AttackID"));
                attack.setAttackName(ars.getString("AttackName"));
                attack.setAccuracy(ars.getInt("Accuracy"));
                attack.setCritChance(ars.getInt("CritChance"));
                attack.setCritDamage(ars.getDouble("CritDamage"));

                attackList.add(attack);
            }

            monster.setAttackList(attackList);
            monster.setDrops(itemDrops);
            monsters.add(monster);
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return monsters;
    }

    public ArrayList<MonsterRoom> getAllPlayerMonsters() throws SQLException, ClassNotFoundException {
        ArrayList<MonsterRoom> monsterRoom = new ArrayList<MonsterRoom>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from MonsterRoom";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            MonsterRoom pmr = new MonsterRoom();
            pmr.setRoomID(rs.getInt("RoomID"));
            pmr.setMonsterID(rs.getInt("MonsterID"));

            monsterRoom.add(pmr);
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return monsterRoom;
    }



}
