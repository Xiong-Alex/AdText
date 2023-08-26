package controller;

import model.DataBaseUpdater;
import view.FormatText;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class: Commands
 * @version 1.2
 * @author: Alex Xiong, Khales Rahman
 * Written: July 11, 2023
 * This class Handles commands from the user. The command is parsed, type of
 * command determined and
 * then routed to correct methods to handle the command.
 */
class Commands {

    protected static final List<String> VALID_DIRECTIONS = Arrays.asList("NORTH", "N", "EAST", "E", "SOUTH", "S", "WEST", "W");
    protected static final List<String> LETTER_DIRECTIONS = Arrays.asList("N", "E", "S", "W");
    protected static final List<String> ITEM_COMMANDS = Arrays.asList("INSPECT", "I", "GET", "G", "DROP", "D", "EQUIP", "E", "UNEQUIP", "U", "USE");
    protected static final List<String> STAT_COMMANDS = Arrays.asList("ALLOCATE", "A");

    protected Player player;
    protected Room currentRoom;
    protected Item item;
    private final DataBaseUpdater dbu;
    protected Inventory inventory;
    private final CombatController cc;

    private final FormatText ft = new FormatText();

    /**
     * Method Commands
     * Constructor for the Commands class
     * Instatiates a new player object for tracking inventory in the game
     */
    Commands() throws SQLException, ClassNotFoundException {
        //Obtain access to our Player, all Rooms and Items
        player = new Player();

        currentRoom = new Room();
        item = new Item();
        dbu = new DataBaseUpdater(); //used to update changes to sql
        inventory = new Inventory();
        cc = new CombatController();
    }


    /**
     * Method logIn
     *
     * Performs the login process for a player based on the given player status.
     *
     * @param playerStatus - an integer representing the player status:
     *                       1 for new player
     *                       2 for existing player
     * @return String - a message indicating the result of the login process
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */
    protected String logIn(int playerStatus) throws SQLException, ClassNotFoundException {
        ArrayList<Player> playerList;

        playerList = player.getAllPlayers();

        Scanner input = new Scanner(System.in);
        String userName;
        boolean playerExists = false;

        switch (playerStatus) {
            case 1 -> { //new Player

                System.out.println(ft.convertText("Creating New Player..."));
                System.out.print(ft.convertText("UserName: "));
                userName = input.nextLine();
                for (Player plr : playerList) {
                    String tempName;
                    tempName = plr.getPlayerName();

                    tempName.toUpperCase();
                    userName.toUpperCase();

                    if (tempName.contains(userName)) {
                        playerExists = true;
                        break;
                    }
                }
                if (playerExists) {
                    System.out.print(ft.convertText(userName));
                    return " already exists.\nPlease try again";
                } else {
                    dbu.createPlayer(userName);
                    boolean foundPlayer = false;

                    playerList = player.getAllPlayers(); //updates list
                    for (Player plr : playerList) {
                        String tempName;
                        tempName = plr.getPlayerName();

                        tempName.toUpperCase();
                        userName.toUpperCase();

                        if (tempName.contains(userName)) {
                            foundPlayer = true;
                            player = player.getPlayer(plr.getPlayerID());
                        }
                    }

                    if (foundPlayer) {
                        currentRoom = currentRoom.getPlayerRoom(player.getCurrentRoom(), player);
                        inventory = inventory.getInventory(player.getPlayerID());
//                        playerRooms = currentRoom.getAllPlayerRooms(player);
                        return "Welcome " + userName;
                    } else {
                        return "player created, but unable to retrieve -- Restart Application and log in";
                    }
                }
            }
            case 2 -> {
                System.out.print(ft.convertText("Enter UserName: "));
                userName = input.nextLine();
                for (Player plr : playerList) {
                    String tempName;
                    tempName = plr.getPlayerName();

                    tempName.toUpperCase();
                    userName.toUpperCase();

                    if (tempName.contains(userName)) {
                        player = player.getPlayer(plr.getPlayerID());
                        playerExists = true;
                    }
                }
                if (playerExists) {
                    currentRoom = currentRoom.getPlayerRoom(player.getCurrentRoom(), player);
                    inventory = inventory.getInventory(player.getPlayerID());
                    return "Welcome back " + userName;
                } else {
                    return "No player with that username was found.\nPlease try again.";
                }
            }
            default -> {
                return "Invalid Command Please try again";
            }
        }
    }

    /**
     * Method updateCurrentRoom
     *
     * Updates the current room based on the player's current room.
     *
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */
    private void updateCurrentRoom() throws SQLException, ClassNotFoundException {
        currentRoom = currentRoom.getPlayerRoom(player.getCurrentRoom(), player);
    }

    /**
     * Method validateCommand
     * returns an int that tells what type of command.
     * 1 for movement (N, S, E, W, U, D)
     * 2 for item commands (G, R, I)
     * 3 for Look (L)
     * 4 for Backpack (B)
     * EXIT_COMMAND for exit (X)
     * throws an exception for an invalid command
     *
     * @param cmdLine - String containing the command entered by the user
     * @return int - the integer for the command received. If not recognized, returns 0 for an invalid command
     * @throws GameException
     */
    private int validateCommand(String cmdLine) throws GameException {

        if (cmdLine.contains(" ")) { //If the command has two parts (ex: Inspect Item), it'll grab the initial command
            String[] cmdLines = cmdLine.split(" ", 2);
            cmdLine = cmdLines[0];
        }

        if (VALID_DIRECTIONS.contains(cmdLine)) {
            return 1;
        } else if (ITEM_COMMANDS.contains(cmdLine)) {
            return 2;
        } else if (cmdLine.equalsIgnoreCase("LOOK")) { //Look
            return 3;
        } else if (cmdLine.equalsIgnoreCase("INVENTORY")) { //Inventory
            return 4;
        } else if (cmdLine.equalsIgnoreCase("HELP")) { //Help
            return 5;
        } else if (cmdLine.equalsIgnoreCase("PLAYER")) { //terminates
            return 6;
        } else if (STAT_COMMANDS.contains(cmdLine)) { //terminates
            return 7;
        }else if (cmdLine.equalsIgnoreCase("EXIT") || cmdLine.equalsIgnoreCase("X")) { //terminates
            System.out.println(ft.convertText("Thank you for playing ADVENTURE TEXT\nGoodbye :)"));
            System.exit(0);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Method executeCommand
     * returns the String to be displayed to the user based on the user's command
     * Calls the correct command method or returns the String for the command entered.
     * throws an exception for an invalid command
     * @param cmd - String that contains the command entered by the user
     * @return String - the response to the command
     * @throws GameException
     */
    protected String executeCommand(String cmd) throws GameException, SQLException, ClassNotFoundException, InterruptedException {
        cmd = cmd.toUpperCase();
        int commandType = validateCommand(cmd);

        if(commandType == 1) {
            return move(cmd);
        } else if(commandType == 2) {
            return itemCommand(cmd);
        } else if(commandType == 3) {
            return look();
        } else if(commandType == 4) {
            return checkInventory();
        } else if(commandType == 5) {
            return help();
        } else if(commandType == 6) {
            return checkPlayer();
        }else if(commandType == 7) {
            return allocateStats(cmd);
        }else {
            throw new GameException("Invalid Command\nPlease try again");
        }
    }

    /**
     * Method move
     * returns the String for the new Room the user is entering
     * Calls Room validDirection to ensure that the direction is valid for this room.
     * If the direction is valid,
     * Updates the room to be visited by updating the room
     * Updates the current Room in Player
     * If the direction is not valid,
     * throws an exception for an invalid direction
     *
     * @param roomDirection - String that contains the command entered by the user
     * @return String - the new room the user is moving to
     * @throws GameException
     */
    public String move(String roomDirection) throws GameException, SQLException, ClassNotFoundException, InterruptedException {
        boolean directionExists = false;
        Exit roomExit = new Exit();

        if (LETTER_DIRECTIONS.contains(roomDirection)) {
            roomDirection = getFullDirection(roomDirection);
        }

        ArrayList<Exit> roomExits = currentRoom.getExits();
        for (Exit exit : roomExits) {
            if (exit.getExitDirection().equalsIgnoreCase(roomDirection) && !exit.isLocked()) {
                roomExit = exit;
                directionExists = true;
                break;
            }
        }


        if (directionExists) {
            player.setCurrentRoom(roomExit.getExitID());
            updateCurrentRoom();

            if(currentRoom.getMonsters() != null && currentRoom.getMonsters().size() != 0) {
                ft.printWithDelay("You are in the " + currentRoom.getRoomName(), TimeUnit.MILLISECONDS, 10);
                ft.printWithDelay(currentRoom.getRoomDescription(), TimeUnit.MILLISECONDS, 10);
                //Engages in battle and returns dropped items
                ArrayList<Item> itemsDropped = cc.battle(player,currentRoom);
                //has to beat enemies

                if (itemsDropped != null && itemsDropped.size() != 0) {
                    ArrayList<Item> updatedRoomItems = currentRoom.getItems();
                    updatedRoomItems.addAll(itemsDropped);

                    currentRoom.setItems(updatedRoomItems);
                }

                //set up to only update current room if player survives the monster encounter
                dbu.updateCurrentRoom(player, currentRoom);
                checkIfLevelUp();
                return "You are in the " + currentRoom.getRoomName()  + "\nVisited: " + currentRoom.isVisited();
            } else {
                dbu.updateCurrentRoom(player, currentRoom);
                return "You are in the " + currentRoom.getRoomName()  + "\nVisited: " + currentRoom.isVisited();
            }

        } else {
            throw new GameException(roomDirection + ft.convertText(" is not a possible direction."));
        }
    }

    /**
     * Method getFullDirection
     * returns the full direction of a one letter move command
     * This will allow player to make move commands with one letter of the cardinal directions
     * such as N for north, E for east, ect.
     *
     * @param moveLetter - String that contains the letter move command the player entered
     * @return String - the full direction the player entered
     */
    public String getFullDirection(String moveLetter) {
        if (moveLetter.equalsIgnoreCase("N")) {
            return "NORTH";
        }
        if (moveLetter.equalsIgnoreCase("W")) {
            return "WEST";
        }
        if (moveLetter.equalsIgnoreCase("E")) {
            return "EAST";
        } else {
            return "SOUTH";
        }
    }

    /**
     * Method itemCommand
     * returns the String for the Item based on the user's command
     * Calls different methods to handle the Item interactions
     * throws an exception for an invalid command or action,
     * For example, item not in the room and the user tries to pick it up
     * @param cmd - String that contains the command entered by the user
     * @return String - the response to the user's command
     * @throws GameException
     */
    private String itemCommand(String cmd) throws GameException, SQLException, ClassNotFoundException {


        String[] cmdLines = cmd.split(" ", 2);

        String command = "";
        String itemName = ""; //grabs Item name

        try {
             command = cmdLines[0];
             itemName = cmdLines[1]; //grabs Item name
        } catch (Exception e) {
            return "Invalid Command";
        }

        if (command.equalsIgnoreCase("INSPECT")) {
            return inspectItem(itemName);
        } else if (command.equalsIgnoreCase("GET")) {
            return getItem(itemName);
        } else if (command.equalsIgnoreCase("DROP")) {
            return dropItem(itemName);
        } else if (command.equalsIgnoreCase("EQUIP")) {
            return equipEquipment(itemName);
        } else if (command.equalsIgnoreCase("UNEQUIP")) {
            return unEquipEquipment(itemName);
        } else if(command.equalsIgnoreCase("USE")){
            return useItem(itemName);
        }else {
            throw new GameException(cmd + " is not a valid Item Command\nPlease try again");
        }
    }

    /**
     * Method useItem
     *
     * Uses the specified item from the player's inventory.
     *
     * @param itemName - the name of the item to be used
     * @return String - a message indicating the result of using the item
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */
    private String useItem(String itemName) throws SQLException, ClassNotFoundException {

        ArrayList<Item> inventoryItems = inventory.getInventory();
        Item item = new Item();
        boolean itemFound = false;

        if(inventoryItems.size() == 0) {
            return "You have no items in your inventory";
        }

        for(Item itm : inventoryItems) {
            String s = itm.getItemName().toUpperCase();

            if (s.contains(itemName)) {
                itemFound = true;
                item = itm;
                break;
            }
        }

        if (!itemFound) {
            return itemName + " not found";
        }

        //found item
        boolean isConsumable = false;
        boolean isKey = false;

        Consumable consumable = new Consumable();
        try {
            consumable = (Consumable) item;
            isConsumable = true;
        } catch (ClassCastException e) {
        }

        if(isConsumable) {
            //use consumable
            int restoreAmount = 0;
            int currentAmount = 0;
            int maxAmount = 0;

            if(consumable.getHp() != 0){ //health pots
                restoreAmount = consumable.getHp();
                currentAmount = player.getHp();
                maxAmount = player.getMaxHp();

                currentAmount += restoreAmount;
                if (currentAmount > maxAmount) {
                    currentAmount = maxAmount; //defaults to full if over-heal
                }

                //Update player HP | Object and the DB
                player.setHp(currentAmount);

                inventoryItems.remove(item);
                inventory.setInventory(inventoryItems);

                dbu.useItem(player, item);
                dbu.updatePlayerHP(player);
                return item.getUseDialogue();


            } else { // mana pots
                restoreAmount = consumable.getMp();
                currentAmount = player.getMp();
                maxAmount = player.getMaxMp();

                currentAmount += restoreAmount;
                if (currentAmount > maxAmount) {
                    currentAmount = maxAmount; //defaults to full if over-heal
                }

                //Update player MP | removed item | Object and the DB
                inventoryItems.remove(item);
                player.setMp(currentAmount);
                inventory.setInventory(inventoryItems);

                dbu.useItem(player, item);
                dbu.updatePlayerMp(player);
                return item.getUseDialogue();
            }
        }

        Exit exit = new Exit();
        for (Exit ext : currentRoom.getExits()) {
            if (item.getItemID() == ext.getKeyID()) {
                isKey = true;
                exit = ext;
            }
        }

        if(isKey) {
            //Replaced the locked/hidden exit with an unlocked exit
            ArrayList<Exit> currentRoomExits = currentRoom.getExits();
            currentRoomExits.remove(exit);
            exit.setLocked(false);
            currentRoomExits.add(exit);
            currentRoom.setExits(currentRoomExits);


            //Remove item from inventory
            inventoryItems.remove(item);
            inventory.setInventory(inventoryItems);

            //Update DB
            dbu.useItem(player, item);
            dbu.roomSolved(player, currentRoom, exit);
            currentRoom = currentRoom.getPlayerRoom(currentRoom.getRoomID(), player);

            //Call the dialogue for the item
            return item.getUseDialogue();
        }

        return itemName + " does nothing";
    }

    /**
     * Method look
     *
     * Performs a look action in the current room, providing information about the room's description, available exits, and items.
     *
     * @return String - the information about the current room, including its description, items, and available exits
     * @throws GameException - if a game-related exception occurs
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */
    private String look() throws GameException, SQLException, ClassNotFoundException {
        ArrayList<Item> itemsInRoom = currentRoom.getItems();
        ArrayList<String> items = new ArrayList<>();

        ArrayList<Exit> roomExits = currentRoom.getExits();
        ArrayList<Exit> unlockedExits = new ArrayList<>();
        for (Exit exit : roomExits) {
            if(!exit.isLocked()) {
                unlockedExits.add(exit);
            }
        }

        StringBuilder exitString = new StringBuilder("Available exits:");
        for (Exit exit : unlockedExits){
            Room rm = new Room();
            rm = rm.getRoom(exit.getExitID());
            exitString.append("\n").append("-").append(exit.getExitDirection()).append(": ").append(rm.getRoomName());
        }


        for (Item item : itemsInRoom) {
            items.add(item.getItemName());
        }


        StringBuilder itemString = new StringBuilder("Items in the room:\n");
        for (Item item : itemsInRoom) {
            itemString.append("-").append(item.getItemName()).append("\n");
        }

        return "You are in the " + currentRoom.getRoomName() + "\n" +
                currentRoom.getRoomDescription() + "\n----------\n" +
                itemString + "----------\n" +
                exitString;
        //should probably fix room toString and just call it
    }

    /**
     * Method get
     * returns the String for the item to be added to the Player's inventory
     * Updates the room to remove the item from the room
     * Updates Player to add item to the backpack
     * throws an exception if the item is not in the room
     * @param itemName - String that contains the itemName entered by the user
     * @return String - item has been added to inventory
     * @throws GameException
     */
    private String getItem(String itemName) throws GameException, SQLException, ClassNotFoundException {
        ArrayList<Item> roomItems = currentRoom.getItems();
        ArrayList<Item> inventoryItems = inventory.getInventory();

        Item getThisItem = new Item();
        boolean itemFound = false;

        if(inventory.getInventory() == null) {
            inventoryItems = new ArrayList<>();
        }

        if(inventoryItems.size() == 10) {
            return "Inventory Full\nUnable to pick up " + itemName;
        }

        for (Item item : roomItems) {
            //Something's broken, I'm unable to simply do if(item.getItemName().equalsIgnoresCase(itemName)
            //So, I'm going about this in a round about way
            //needs to be redone
            String s = item.getItemName().toUpperCase();
            if (s.contains(itemName)) {
                getThisItem = item;
                itemFound = true;
                break;
            }
        }

        if (itemFound) {
            inventoryItems.add(getThisItem);
            roomItems.remove(getThisItem);

            inventory.setInventory(inventoryItems);
            currentRoom.setItems(roomItems);
            dbu.pickUpItem(getThisItem, player);

            return "You picked up " + getThisItem.getItemName();
        } else {
            return itemName + " not found. Please try again.";
        }

    }

    /**
     * Method dropItem
     * returns the String for dropping the item
     * Updates the room to add the item to the room
     * Updates the Player by removing the item from the backpack
     * throws an exception for if Item is not in the inventory
     * @param itemName - String that contains the itemName entered by the user
     * @return String - the Item has been dropped
     * @throws GameException
     */
    private String dropItem(String itemName) throws GameException, SQLException, ClassNotFoundException {
        ArrayList<Item> roomItems = currentRoom.getItems();
        ArrayList<Item> inventoryItems = inventory.getInventory();
        Item dropThisItem = new Item();
        boolean itemFound = false;

        for (Item item : inventoryItems) {
            //Something's broken, I'm unable to simply do if(item.getItemName().equalsIgnoresCase(itemName)
            //So, I'm going about this in a round about way
            //needs to be redone
            String s = item.getItemName().toUpperCase();
            if (s.contains(itemName)) {
                itemFound = true;
                dropThisItem = item;
                break;
            }
        }

        if(itemFound) {
            inventoryItems.remove(dropThisItem);
            roomItems.add(dropThisItem);
            dbu.dropItem(dropThisItem, player);

            return "You dropped " + dropThisItem.getItemName();
        } else {
            return itemName + " not found. Please try again.";
        }

    }

    /**
     * Method inspectItem
     * returns the String of the item or an "I don't see " the item message
     * @param itemName - String that contains the itemName entered by the user
     * @return String - the String for the look command
     * @throws GameException
     */
    private String inspectItem(String itemName) throws GameException, SQLException, ClassNotFoundException {
        ArrayList<Item> allItems = new ArrayList<>(); //Grabs allItems in the room and on the player
        Item inspectThisItem = new Item();
        boolean itemFound = false;

        if (currentRoom.getItems() != null) {
            allItems.addAll(currentRoom.getItems());
        }

        if (inventory.getInventory() != null) {
            allItems.addAll(inventory.getInventory());
        }

        allItems.add(player.getArmor());
        allItems.add(player.getWeapon());
        allItems.add(player.getAccessory());

        for(Item item : allItems) {
            //Something's broken, I'm unable to simply do if(item.getItemName().equalsIgnoresCase(itemName)
            //So, I'm going about this in a round about way
            //needs to be redone
            String s = item.getItemName().toUpperCase();


            if (s.contains(itemName)) {
                itemFound = true;
                inspectThisItem = item;
                break;
            }
        }

        if (itemFound) {

            if (inspectThisItem.getClass() == Weapon.class) {
                Weapon weapon = (Weapon) inspectThisItem;
                return weapon.getItemName() + " " +  weapon.getItemDescription() +
                        "\nAttack: " + weapon.getAttack();
            } else if (inspectThisItem.getClass() == Armor.class) {
                Armor armor = (Armor) inspectThisItem;
                return armor.getItemName() + " " +  armor.getItemDescription() +
                        "\nDefense: " + armor.getDefense();
            } else if (inspectThisItem.getClass() == Accessory.class) {
                Accessory accessory = (Accessory) inspectThisItem;
                return accessory.getItemName() + " " + accessory.getItemDescription() +
                        "\nHP: " + accessory.getHp() +
                        "\nMP: " + accessory.getMp() +
                        "\nSTR: " + accessory.getStr() +
                        "\nDEF: " + accessory.getDef() +
                        "\nSTA: " + accessory.getSta();
            } else if (inspectThisItem.getClass() == Consumable.class) {
                Consumable consumable = (Consumable) inspectThisItem;
                boolean isHealthPotion = consumable.getHp() > 0;

                if (isHealthPotion) {
                    return consumable.getItemName() + " " + consumable.getItemDescription() +
                            "\nHP: " + consumable.getHp();
                } else {
                    return consumable.getItemName() + " " + consumable.getItemDescription() +
                            "\nMP: " + consumable.getMp();
                }
            } else {
                return inspectThisItem.getItemName() + " " + inspectThisItem.getItemDescription();
            }
        }

        return itemName + " not found. Please try again.";
    }


    /**
     * Method equipWeapon
     *
     * Equips a weapon from the player's inventory.
     *
     * @param itemName - the name of the weapon to be equipped
     * @return String - a message indicating the result of equipping the weapon
     * @throws GameException - if a game-related exception occurs
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */
    private String equipEquipment(String itemName) throws GameException, SQLException, ClassNotFoundException {
        ArrayList<Item> inventoryItems = inventory.getInventory();
        Item equipThisEquipment = new Item();
        boolean equipmentFound = false;

        for (Item item : inventoryItems) {
            //Something's broken, I'm unable to simply do if(item.getItemName().equalsIgnoresCase(itemName)
            //So, I'm going about this in a round about way
            //needs to be redone
            String s = item.getItemName().toUpperCase();
            if (s.contains(itemName)) {
                equipmentFound = true;
                equipThisEquipment =  item;
                break;
            }
        }

        if(equipThisEquipment.getClass() == Weapon.class) {

            if(player.getWeapon().getItemID() != 33 && equipmentFound) {
                return "You currently have " + player.getWeapon() + " equipped.\n" +
                        "To equip " + equipThisEquipment.getItemName() + " you need\n to unequip your current weapon";
            }

            if(equipmentFound) {
                player.setWeapon((Weapon) equipThisEquipment);
                inventoryItems.remove(equipThisEquipment);
                inventory.setInventory(inventoryItems);

                dbu.equipWeapon(player);

                return "You equipped " + equipThisEquipment.getItemName();
            }

        } else if(equipThisEquipment.getClass() == Armor.class) {
            if(player.getArmor().getItemID() != 34 && equipmentFound) {
                return "You currently have " + player.getArmor() + " equipped.\n" +
                        "To equip " + equipThisEquipment.getItemName() + " you need\n to unequip your current armor";
            }

            if(equipmentFound) {
                player.setArmor((Armor) equipThisEquipment);
                inventoryItems.remove(equipThisEquipment);
                inventory.setInventory(inventoryItems);

                dbu.equipArmor(player);

                return "You equipped " + equipThisEquipment.getItemName();
            }
        } else if(equipThisEquipment.getClass() == Accessory.class) {

            if(player.getAccessory().getItemID() != 35 && equipmentFound) {
                return "You currently have " + player.getArmor() + " equipped.\n" +
                        "To equip " + equipThisEquipment.getItemName() + " you need\n to unequip your current accessory";
            }

            if(equipmentFound) {
                player.setAccessory((Accessory) equipThisEquipment);
                dbu.equipAccessory(player);

                inventoryItems.remove(equipThisEquipment);
                inventory.setInventory(inventoryItems);

                player.setMaxHp(player.getMaxHp() + ((Accessory) equipThisEquipment).getHp());
                player.setHp(player.getHp() + ((Accessory) equipThisEquipment).getHp());

                player.setMaxMp(player.getMaxMp() + ((Accessory) equipThisEquipment).getMp());
                player.setMp(player.getMp() + ((Accessory) equipThisEquipment).getMp());

                player.setStr(player.getStr() + ((Accessory) equipThisEquipment).getStr());
                player.setSta(player.getSta() + ((Accessory) equipThisEquipment).getSta());
                player.setDef(player.getDef() + ((Accessory) equipThisEquipment).getDef());

                dbu.updatePlayerStat(player);

                return "You equipped " + equipThisEquipment.getItemName();
            }

        }

        //if equipment not found/equipable it'll default to this.
        return itemName + " not found. Please try again.";
    }

    /**
     * Method unEquipWeapon
     *
     * Unequips a weapon and places it back into the player's inventory.
     *
     * @param itemName - the name of the weapon to be unequipped
     * @return String - a message indicating the result of unequipping the weapon
     * @throws GameException - if a game-related exception occurs
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */
    private String unEquipEquipment(String itemName) throws GameException, SQLException, ClassNotFoundException {
        ArrayList<Item> inventoryItems = inventory.getInventory();
        Item unEquipThisEquipment = new Item();
        ArrayList<Item> playerEquipment = new ArrayList<>();
        boolean equipmentFound = false;

        if (inventoryItems == null) {
            inventoryItems = new ArrayList<>();
        }


        if(player.getWeapon().getItemID() != 33) {
            playerEquipment.add(player.getWeapon());
        }

        if(player.getArmor().getItemID() != 34) {
            playerEquipment.add(player.getArmor());
        }

        if(player.getAccessory().getItemID() != 35) {
            playerEquipment.add(player.getAccessory());
        }

        for (Item item : playerEquipment) {
            //Something's broken, I'm unable to simply do if(item.getItemName().equalsIgnoresCase(itemName)
            //So, I'm going about this in a round about way
            //needs to be redone
            String s = item.getItemName().toUpperCase();
            if (s.contains(itemName)) {
                equipmentFound = true;
                unEquipThisEquipment =  item;
                break;
            }
        }

        if(inventoryItems.size() == 10) {
            return "Inventory Full\nUnable unequip " + itemName;
        }

        if(unEquipThisEquipment.getClass() == Weapon.class) {

            if(equipmentFound) {
                player.setWeapon((Weapon)item.getItem(33)); //sets to empty
                inventoryItems.add(unEquipThisEquipment);
                inventory.setInventory(inventoryItems);

                dbu.unEquipWeapon(player, unEquipThisEquipment);

                return "You unequipped " + unEquipThisEquipment.getItemName();
            }

        } else if(unEquipThisEquipment.getClass() == Armor.class) {

            if (equipmentFound) {
                player.setArmor((Armor) item.getItem(34)); //sets to empty
                inventoryItems.add(unEquipThisEquipment);
                inventory.setInventory(inventoryItems);

                dbu.unEquipArmor(player, unEquipThisEquipment); //defaults to Robe

                return "You unequipped " + unEquipThisEquipment.getItemName();
            }

        }  else if(unEquipThisEquipment.getClass() == Accessory.class) {

            if(equipmentFound) {
                player.setAccessory((Accessory) item.getItem(35)); //sets to empty
                inventoryItems.add(unEquipThisEquipment);
                inventory.setInventory(inventoryItems);

                dbu.unEquipAccessory(player, unEquipThisEquipment);

                player.setMaxHp(player.getMaxHp() - ((Accessory) unEquipThisEquipment).getHp());
                player.setHp(player.getHp() - ((Accessory) unEquipThisEquipment).getHp());

                player.setMaxMp(player.getMaxMp() - ((Accessory) unEquipThisEquipment).getMp());
                player.setMp(player.getMp() + ((Accessory) unEquipThisEquipment).getMp());

                player.setStr(player.getStr() - ((Accessory) unEquipThisEquipment).getStr());
                player.setSta(player.getSta() - ((Accessory) unEquipThisEquipment).getSta());
                player.setDef(player.getDef() - ((Accessory) unEquipThisEquipment).getDef());

                dbu.updatePlayerStat(player);

                return "You unequipped " + unEquipThisEquipment.getItemName();
            }
        }
        return itemName + " not found. Please try again.";
    }

    /**
     * Method equipArmor
     *
     * Equips an armor from the player's inventory.
     *
     * @param itemName - the name of the armor to be equipped
     * @return String - a message indicating the result of equipping the armor
     * @throws GameException - if a game-related exception occurs
     * @throws SQLException - if a database error occurs
     * @throws ClassNotFoundException - if the database driver class is not found
     */


    /**
     * Method checkInventory
     *
     * Retrieves the list of items in the player's inventory.
     *
     * @return String - a string representation of the items in the inventory
     * @throws GameException - if a game-related exception occurs
     */
    private String checkInventory() throws GameException {
        ArrayList<Item> inventoryItems = inventory.getInventory();
        ArrayList<String> itemNames = new ArrayList<>();

        if (inventoryItems == null) {
            return "Your Inventory is empty";
        }

        for(Item item: inventoryItems) {
            itemNames.add(item.getItemName());
        }

        return itemNames.toString();
    }

    /**
     * Method checkPlayer
     *
     * Retrieves the player's information and statistics.
     *
     * @return String - a string representation of the player's information
     * @throws GameException - if a game-related exception occurs
     */
    private String checkPlayer() throws GameException {
        Accessory accessory = player.getAccessory();
        boolean incStats = accessory.getHp() + accessory.getMp() + accessory.getAtk() + accessory.getDef() + accessory.getSta() + accessory.getStr() > 0;

        if (incStats) {
            return player.getPlayerName() + " #" + player.getPlayerID() +
                    "\nArmor: " + player.getArmor().getItemName() +
                    "\nWeapon: " + player.getWeapon().getItemName() +
                    "\nAccessory: " + player.getAccessory().getItemName() +
                    "\n----------" +
                    "\nLevel: " + player.getLevel() +
                    "\nEXP: " + player.getExp() +
                    "\nStat Points: " + player.getSp() +
                    "\nHP: " + player.getHp() +
                    "\nMP: " + player.getMp() +
                    "\n----------" +
                    "\nMax HP: " + player.getMaxHp() + " <+" + accessory.getHp() + ">" +
                    "\nMax MP: " + player.getMaxMp() + " <+" + accessory.getMp() + ">" +
                    "\nSTR: " + player.getStr() + " <+" + accessory.getStr() + ">" +
                    "\nDEF: " + player.getDef() + " <+" + accessory.getDef() + ">" +
                    "\nSTA: " + player.getSta() + " <+" + accessory.getSta() + ">";

        } else {
            return player.getPlayerName() + " #" + player.getPlayerID() +
                    "\nArmor: " + player.getArmor().getItemName() +
                    "\nWeapon: " + player.getWeapon().getItemName() +
                    "\nAccessory: " + player.getAccessory().getItemName() +
                    "\n---------------" +
                    "\nLevel: " + player.getLevel() +
                    "\nEXP: " + player.getExp() +
                    "\nStat Points: " + player.getSp() +
                    "\nHP: " + player.getHp() +
                    "\nMP: " + player.getMp() +
                    "\n---------------" +
                    "\nMax HP: " + player.getMaxHp() +
                    "\nMax MP: " + player.getMaxMp() +
                    "\nSTR: " + player.getStr() +
                    "\nDEF: " + player.getDef() +
                    "\nSTA: " + player.getSta();

            }
        }

    private void checkIfLevelUp () throws SQLException, ClassNotFoundException, InterruptedException{
        HashMap<Integer,Integer> levels = new HashMap<>();
        levels = player.getAllLevels();

        Player oldPlayer = new Player();
        oldPlayer.setLevel(player.getLevel());
        oldPlayer.setExp(player.getExp());
        oldPlayer.setSp(player.getSp());
        oldPlayer.setMaxHp(player.getMaxHp());
        oldPlayer.setHp(player.getHp());
        oldPlayer.setMaxMp(player.getMaxMp());
        oldPlayer.setMp(player.getMp());
        oldPlayer.setSta(player.getSta());
        oldPlayer.setStr(player.getStr());
        oldPlayer.setDef(player.getDef());


        boolean leveledUp = false;

        while (true) {
            int playerExp = player.getExp();
            int expRequired = levels.get(player.getLevel());
            if(playerExp >= expRequired) {
                player.setExp(player.getExp() - levels.get(player.getLevel())); //subtracts exp from exp required to level up
                player.setLevel(player.getLevel() + 1); //levels up the player
                player.setSp(player.getSp() + 5);

                player.setMaxHp(player.getMaxHp() + 1);
                player.setHp(player.getHp() + 1);
                player.setMaxMp(player.getMaxMp() + 1);
                player.setMaxMp(player.getMp() + 1);
                player.setStr(player.getStr() + 1);
                player.setSta(player.getSta() + 1);
                player.setDef(player.getDef() + 1);

                dbu.increasePlayerLevel(player);
                leveledUp = true;
            } else if (player.getExp() < levels.get(player.getLevel())) {
                break;
            }
        }

        if(leveledUp) {
            String newStats =
                    "Level: " + oldPlayer.getLevel() + " -> " + player.getLevel() +
                    "\nEXP: " + oldPlayer.getExp() + " -> " + player.getExp() +
                    "\nStat Points: " + oldPlayer.getSp() + " -> " + player.getSp() +
                    "\nHP: " + oldPlayer.getHp() + " -> " + player.getHp() +
                    "\nMP: " + oldPlayer.getMp() + " -> " + player.getMp() +
                    "\n---------------" +
                    "\nMax HP: " + oldPlayer.getMaxHp() + " -> " + player.getMaxHp() +
                    "\nMax MP: " + oldPlayer.getMaxMp() + " -> " + player.getMaxMp() +
                    "\nSTR: " + oldPlayer.getStr() + " -> " + player.getStr() +
                    "\nDEF: " + oldPlayer.getDef() + " -> " + player.getDef() +
                    "\nSTA: " + oldPlayer.getSta() + " -> " + player.getSta() +
                    "\n---------------";

            ft.printWithDelay(newStats, TimeUnit.MILLISECONDS, 10);
        }
    }

    /**
     * Method help
     *
     * Provides a list of possible commands and their descriptions.
     *
     * @return String - a string containing the list of possible commands
     * @throws GameException - if a game-related exception occurs
     */
    private String help() throws GameException {
        return "Possible commands:\n" +
                "-Look: View Room\n" +
                "-<Direction>: Move to the next room via Exit\n" +
                "-Get <Item Name>: Pick up Item\n" +
                "-Drop <Item Name>: Drop Item\n" +
                "-Player: View Player\n" +
                "-Inventory: Check Inventory\n" +
                "-Inspect <Item Name>: Reveals description & other attributes of Item\n" +
                "-Equip <Equipment Name>: Equip Armor, Accessories or Weapon\n" +
                "-Unequip <Equpiment Name>: Unequip Armor, Accessories, or Weapon\n" +
                "-Exit(X): will end the game\n" +
                "-Use <Item Name>: uses the item if allowed\n" +
                "-Allocate(A) # SP to <STAT>: Allocates your SP to your Stats";
    }



    private String allocateStats(String cmd) throws GameException, SQLException, ClassNotFoundException {

        String[] commands = cmd.split(" ");
        //Allocate x stats to xStat

        boolean validCommand = false;
        int numberOfStats = 0;
        String stat = "";

        try{
            numberOfStats = Integer.parseInt(commands[1]);
            stat = commands[4];
            validCommand = true;
        } catch (Exception e) {
           return "Invalid Input";
        }

        if(!validCommand) {
            return "Invalid Command";
        }

        if(player.getSp() < numberOfStats) {
            return "You don't have enough SP\nCurrent SP: " + player.getSp() +"\nRequesting: " + numberOfStats;
        }

        switch (stat) {
            case "MAXHP" :
                player.setMaxHp(player.getMaxHp() + numberOfStats);
                player.setSp(player.getSp() - numberOfStats);
                dbu.allocateSPtoMaxHp(player);
                return "MaxHP: " + (player.getMaxHp() - numberOfStats) + " -> " + player.getMaxHp();
            case "MAXMP" :
                player.setMaxMp(player.getMaxMp() + numberOfStats);
                player.setSp(player.getSp() - numberOfStats);
                dbu.allocateSPtoMaxMp(player);
                return "MaxMP: " + (player.getMaxMp() - numberOfStats) + " -> " + player.getMaxMp();
            case "STR" :
                player.setStr(player.getStr() + numberOfStats);
                player.setSp(player.getSp() - numberOfStats);
                dbu.allocateSPtoStr(player);
                return "STR: " + (player.getStr() - numberOfStats) + " -> " + player.getStr();
            case "DEF" :
                player.setDef(player.getDef() + numberOfStats);
                player.setSp(player.getSp() - numberOfStats);
                dbu.allocateSPtoDef(player);
                return "DEF: " + (player.getDef() - numberOfStats) + " -> " + player.getDef();
            case "STA" :
                player.setSta(player.getSta() + numberOfStats);
                player.setSp(player.getSp() - numberOfStats);
                dbu.allocateSPtoSta(player);
                return "STA: " + (player.getSta() - numberOfStats) + " -> " + player.getSta();
            default:
                return "Invalid Stat\nValid Stats: MaxHP, MaxMP, STR, DEF, STA";

        }
    }

}




