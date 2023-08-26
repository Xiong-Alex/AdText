package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.*;

/**
 * Class : ItemDB.java
 * @author: Alex Xiong
 * @version: 1.0
 * Course: ITEC 3860
 * Written: June 26, 2023
 * This class handles all of the DB interactions for Items
 */
public class ItemDB {
    /**
     * Method: getNextItemID
     * Purpose: gets the next ID for an item
     * @return int
     */
    public int getNextItemID() throws SQLException {
        SQLiteDB sdb = null;
        try {
            sdb = new SQLiteDB();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        int next = sdb.getMaxValue("ItemID", "Item") + 1;
        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();

        return next;
    }

    /**
     * Method: getItem
     * Purpose: Gets an Item based upon the supplied ID
     * @param id
     * @return Item
     * @throws SQLException
     */
    public Item getItem(int id) throws SQLException, ClassNotFoundException {
        SQLiteDB sdb = new SQLiteDB();

        Item item = new Item();
        Armor armor = new Armor();
        Weapon weapon = new Weapon();
        Accessory accessory = new Accessory();
        Consumable consumable = new Consumable();

        String sql = "Select * from Item WHERE ItemID = " + id;
        ResultSet rs = sdb.queryDB(sql);
        if (rs.next()) {

            boolean isArmor = false;
            boolean isWeapon = false;
            boolean isAccessory = false;
            boolean isConsumable = false;

            String checkIfArmor = "SELECT * from Item JOIN Armor where ItemID = ArmorID AND ItemID = " + id;
            String checkIfWeapon = "SELECT * from Item JOIN Weapon where ItemID = WeaponID AND ItemID = " + id;
            String checkIfAccessory = "SELECT * from Item JOIN Accessory where ItemID = AccessoryID AND ItemID = " + id;
            String checkIfConsumable = "SELECT * from Item JOIN Consumable where ItemID = ConsumableID AND ConsumableID = " + id;


            if(sdb.queryDB(checkIfArmor).next()) {
                isArmor = true;
            } else if(sdb.queryDB(checkIfWeapon).next()) {
                isWeapon = true;
            } else if(sdb.queryDB(checkIfAccessory).next()) {
                isAccessory = true;
            } else if(sdb.queryDB(checkIfConsumable).next()) {
                isConsumable = true;
            }

            if (isArmor) {

                String subSql = "Select i.ItemID, i.Name, i.Description, i.UseDialogue, a.Defense from Item i JOIN Armor a on ItemID = ArmorID where ArmorID = ";
                ResultSet ars = sdb.queryDB(subSql + id);

                armor.setItemID(ars.getInt("ItemID"));
                armor.setItemName(ars.getString("Name"));
                armor.setItemDescription(ars.getString("Description"));
                armor.setDefense(ars.getInt("Defense"));
                armor.setUseDialogue(ars.getString("UseDialogue"));

                //Close the SQLiteDB connection since SQLite only allows one updater
                sdb.close();
                return armor;
            } else if (isWeapon) {

                String subSql = "Select i.ItemID, i.Name, i.Description, i.UseDialogue, w.Attack from Item i JOIN Weapon w on ItemID = WeaponID where WeaponID = ";
                ResultSet ars = sdb.queryDB(subSql + id);

                weapon.setItemID(ars.getInt("ItemID"));
                weapon.setItemName(ars.getString("Name"));
                weapon.setItemDescription(ars.getString("Description"));
                weapon.setAttack(ars.getInt("Attack"));
                weapon.setUseDialogue(ars.getString("UseDialogue"));

                //Close the SQLiteDB connection since SQLite only allows one updater
                sdb.close();
                return weapon;
            } else if (isAccessory) {

                String subSql = "Select i.ItemID, i.Name, i.Description, i.UseDialogue, a.HP, a.ATK, a.DEF, a.STA, a.STR, a.MP from Item i JOIN Accessory a on ItemID = AccessoryID where AccessoryID = ";
                ResultSet ars = sdb.queryDB(subSql + id);

                accessory.setItemID(ars.getInt("ItemID"));
                accessory.setItemName(ars.getString("Name"));
                accessory.setItemDescription(ars.getString("Description"));
                accessory.setUseDialogue(ars.getString("UseDialogue"));

                accessory.setAtk(ars.getInt("ATK"));
                accessory.setHp(ars.getInt("HP"));
                accessory.setDef(ars.getInt("DEF"));
                accessory.setMp(ars.getInt("MP"));
                accessory.setSta(ars.getInt("STA"));
                accessory.setStr(ars.getInt("STR"));

                //Close the SQLiteDB connection since SQLite only allows one updater
                sdb.close();
                return accessory;
            } else if (isConsumable) {
                String subSql = "Select * from Item Join Consumable ON ItemID = ConsumableID Where ConsumableID = ";
                ResultSet crs = sdb.queryDB(subSql + id);

                consumable.setItemID(crs.getInt("ItemID"));
                consumable.setItemName(crs.getString("Name"));
                consumable.setItemDescription(crs.getString("Description"));
                consumable.setUseDialogue(crs.getString("UseDialogue"));

                //checks if Health or Mana Potion
                if(crs.getInt("HP") != 0) {
                    consumable.setHp(crs.getInt("HP"));
                } else {
                    consumable.setMp(crs.getInt("MP"));
                }

                sdb.close();
                return consumable;

            } else {

                item.setItemID(rs.getInt("ItemID"));
                item.setItemName(rs.getString("Name"));
                item.setItemDescription(rs.getString("Description"));
                item.setUseDialogue(rs.getString("UseDialogue"));

                //Close the SQLiteDB connection since SQLite only allows one updater
                sdb.close();
                return item;
            }
        }
        else {
            throw new SQLException("Item " + id + " not found");
        }
    }

    /**
     * Method: getAllItems
     * Purpose: gets all items
     * @return ArrayList<Item>
     * @throws SQLException
     */
    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> items = new ArrayList<Item>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from Item";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            Item item = new Item();
            Armor armor = new Armor();
            Weapon weapon = new Weapon();
            Accessory accessory = new Accessory();
            Consumable consumable = new Consumable();


            boolean isArmor = false;
            boolean isWeapon = false;
            boolean isAccessory = false;
            boolean isConsumable = false;

            String checkIfArmor = "SELECT * from Item JOIN Armor where ItemID = ArmorID AND ItemID = " + rs.getInt("ItemID");
            String checkIfWeapon = "SELECT * from Item JOIN Weapon where ItemID = WeaponID AND ItemID = " + rs.getInt("ItemID");
            String checkIfAccessory = "SELECT * from Item JOIN Accessory where ItemID = AccessoryID AND ItemID = " + rs.getInt("ItemID");
            String checkIfConsumable = "SELECT * from Item JOIN Consumable where ItemID = ConsumableID AND ConsumableID = " + rs.getInt("ItemID");



            if(sdb.queryDB(checkIfArmor).next()) {
                isArmor = true;
            } else if(sdb.queryDB(checkIfWeapon).next()) {
                isWeapon = true;
            } else if(sdb.queryDB(checkIfAccessory).next()) {
                isAccessory = true;
            } else if(sdb.queryDB(checkIfConsumable).next()) {
                isConsumable = true;
            }

            if (isArmor) {

                String subSql = "Select i.ItemID, i.Name, i.Description, i.UseDialogue, a.Defense from Item i JOIN Armor a on ItemID = ArmorID where ArmorID = ";
                ResultSet ars = sdb.queryDB(subSql + rs.getInt("ItemID"));

                armor.setItemID(ars.getInt("ItemID"));
                armor.setItemName(ars.getString("Name"));
                armor.setItemDescription(ars.getString("Description"));
                armor.setDefense(ars.getInt("Defense"));
                armor.setUseDialogue(ars.getString("UseDialogue"));


                items.add(armor);
            } else if (isWeapon) {

                String subSql = "Select i.ItemID, i.Name, i.Description, i.UseDialogue, w.Attack from Item i JOIN Weapon w on ItemID = WeaponID where WeaponID = ";
                ResultSet ars = sdb.queryDB(subSql + rs.getInt("ItemID"));

                weapon.setItemID(ars.getInt("ItemID"));
                weapon.setItemName(ars.getString("Name"));
                weapon.setItemDescription(ars.getString("Description"));
                weapon.setAttack(ars.getInt("Attack"));
                weapon.setUseDialogue(ars.getString("UseDialogue"));


                items.add(weapon);
            } else if (isAccessory) {

                String subSql = "Select i.ItemID, i.Name, i.Description, i.UseDialogue, a.HP, a.ATK, a.DEF, a.STA, a.STR, a.MP from Item i JOIN Accessory a on ItemID = AccessoryID where AccessoryID = ";
                ResultSet ars = sdb.queryDB(subSql + rs.getInt("ItemID"));

                accessory.setItemID(ars.getInt("ItemID"));
                accessory.setItemName(ars.getString("Name"));
                accessory.setItemDescription(ars.getString("Description"));
                accessory.setUseDialogue(ars.getString("UseDialogue"));

                accessory.setAtk(ars.getInt("ATK"));
                accessory.setHp(ars.getInt("HP"));
                accessory.setDef(ars.getInt("DEF"));
                accessory.setMp(ars.getInt("MP"));
                accessory.setSta(ars.getInt("STA"));
                accessory.setStr(ars.getInt("STR"));

                //Close the SQLiteDB connection since SQLite only allows one updater
                sdb.close();
                items.add(accessory);
            } else if (isConsumable) {
                String subSql = "Select * from Item Join Consumable ON ItemID = ConsumableID Where ConsumableID = ";
                ResultSet crs = sdb.queryDB(subSql + rs.getInt("ItemID"));

                consumable.setItemID(crs.getInt("ItemID"));
                consumable.setItemName(crs.getString("Name"));
                consumable.setItemDescription(crs.getString("Description"));
                consumable.setUseDialogue(crs.getString("UseDialogue"));

                //checks if Health or Mana Potion
                if(crs.getInt("HP") != 0) {
                    consumable.setHp(crs.getInt("HP"));
                } else {
                    consumable.setMp(crs.getInt("MP"));
                }

                sdb.close();
                items.add(consumable);

            } else {
                item.setItemID(rs.getInt("ItemID"));
                item.setItemName(rs.getString("Name"));
                item.setItemDescription(rs.getString("Description"));
                item.setUseDialogue(rs.getString("UseDialogue"));

                items.add(item);
            }

        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return items;
    }

    public ArrayList<ItemRoom> getAllItemRooms() throws SQLException, ClassNotFoundException {
        ArrayList<ItemRoom> itemRoom = new ArrayList<ItemRoom>();
        SQLiteDB sdb = new SQLiteDB();
        String sql = "Select * from ItemRoom";

        ResultSet rs = sdb.queryDB(sql);

        while (rs.next()) {
            ItemRoom pir = new ItemRoom();
            pir.setRoomID(rs.getInt("RoomID"));
            pir.setItemID(rs.getInt("ItemID"));


            itemRoom.add(pir);
        }

        //Close the SQLiteDB connection since SQLite only allows one updater
        sdb.close();
        return itemRoom;
    }
}
