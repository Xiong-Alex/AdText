package controller;

import model.DataBaseUpdater;
import view.FormatText;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class: Combat Controller
 * * @version 1.2
 * * @author: Alex Xiong
 *  * Course: ITEC 3860 Summer 2023
 *  * Written: July 7, 2023
 * This class all the combat in the game.
 */

public class CombatController {

    DataBaseUpdater dbu = new DataBaseUpdater();
    Player player = new Player();
    Room room = new Room();
    Scanner moveInput = new Scanner(System.in);
    Random random = new Random();
    ArrayList<Item> droppedItems = new ArrayList<>();
    ArrayList<Monster> defeatedMonsters = new ArrayList<>();
    private final FormatText ft = new FormatText();


    /**
     * Executes a battle between the player and the monsters in the room.
     *
     * @param player The player participating in the battle
     * @param room   The room containing the monsters to battle
     * @return An ArrayList of items dropped by defeated monsters
     * @throws SQLException           If a database error occurs
     * @throws ClassNotFoundException If the required class is not found
     * @throws InterruptedException   If the battle is interrupted
     */
    public ArrayList<Item> battle(Player player, Room room) throws SQLException, ClassNotFoundException, InterruptedException {
        this.player = player;
        this.room = room;

        ArrayList<Monster> roomMonsters = room.getMonsters();
        Monster monster = new Monster();
        int monsterCount = roomMonsters.size();
        int monsterMaxHealth;

        ft.printWithDelay("You've encountered a monster: ", TimeUnit.MILLISECONDS, 10);
        ft.printWithDelay("Unable to escape, you need to fight!", TimeUnit.MILLISECONDS, 10);
        ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);

        monster = roomMonsters.get(monsterCount - 1);
        monsterMaxHealth = monster.getHp();

        //While there are still monsters, the loop will continue
        while (monsterCount != 0) {
            ft.printWithDelay("Player HP: " + player.getHp() + "/" + player.getMaxHp(), TimeUnit.MILLISECONDS, 10);
            ft.printWithDelay(monster.getMonsterName() + " HP: " + monster.getHp() + "/" + monsterMaxHealth, TimeUnit.MILLISECONDS, 10);
            ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
            displayPlayerMoves();
            ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
            playerAttack(monster);
            ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
            if (monster.getHp() <= 0) {
                ft.printWithDelay("You defeated " + monster.getMonsterName(), TimeUnit.MILLISECONDS, 10);
                ft.printWithDelay("You've gained " + monster.getExp() + " EXP!", TimeUnit.MILLISECONDS, 10);
                //ft.printWithDelay( ,TimeUnit.MILLISECONDS, 10);

                monsterDrop(monster);

                defeatedMonsters.add(monster);

                monsterCount -= 1;

                if (monsterCount > 0) {
                    ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
                    ft.printWithDelay("Another Monster has appeared.", TimeUnit.MILLISECONDS, 10);

                    //updates monster object to next monster
                    monster = roomMonsters.get(monsterCount - 1);
                    monsterMaxHealth = monster.getHp();
                }

            } else {
                //monster attacks now
                monsterAttack(monster);
                ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
                if (player.getHp() <= 0) { //if Player Dies
                    ft.printWithDelay("You died.\nGAME OVER", TimeUnit.MILLISECONDS, 10);

                    System.exit(0);
                }
            }

        }

        ft.printWithDelay("You've emerged victorious in this encounter.", TimeUnit.MILLISECONDS, 10);
        ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);


        //IDK why, but closing this scanner affects the input Scanner in the GameStart.MAIN
        //moveInput.close();

        //updating DB at the end of the battle
        if (droppedItems != null || droppedItems.size() != 0) {
            //update DB
            for (Item item : droppedItems) {
                dbu.dropItem(item, player);
            }
        }

        for (Monster mstr : defeatedMonsters) {
            player.setExp(player.getExp() + mstr.getExp());
            dbu.removeMonsterFromPlayerRoom(mstr, player);
        }

        dbu.increasePlayerExp(player);
        dbu.updatePlayerHP(player);
        dbu.updatePlayerMp(player);


        return droppedItems;
    }

    /**
     * Method monsterDrop
     * <p>
     * Handles the dropping of items by a defeated monster.
     *
     * @param monster The defeated monster
     * @throws ClassNotFoundException - if the required class is not found
     * @throws InterruptedException   - if the process is interrupted
     */
    private void monsterDrop(Monster monster) throws ClassNotFoundException, InterruptedException {
        HashMap<Item, Integer> monsterDrops = monster.getDrops();
        ArrayList<Item> itemDrops = new ArrayList<>();
        boolean itemIsDropped = false;

        int rngChance = random.nextInt(100) + 1;

        //Item list of all possible drops from this monster
        for (Item item : monsterDrops.keySet()) {
            if (rngChance <= monsterDrops.get(item)) {
                itemDrops.add(item);
                itemIsDropped = true;
            }
        }

        if (itemIsDropped) { //adds all item and prints out the monster drops

            droppedItems.addAll(itemDrops);
            ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
            ft.printWithDelay(monster.getMonsterName() + " dropped:", TimeUnit.MILLISECONDS, 10);
            for (Item item : itemDrops) {
                ft.printWithDelay(item.getItemName(), TimeUnit.MILLISECONDS, 10);
            }
            ft.printWithDelay("-----------------------", TimeUnit.MILLISECONDS, 10);
        }
    }

    /**
     * Method monsterAttack
     * <p>
     * Executes a monster's attack on the player.
     *
     * @param monster The monster performing the attack
     * @throws InterruptedException - if the process is interrupted
     */
    private void monsterAttack(Monster monster) throws InterruptedException {
        int playerDefense = player.getDef() + player.getArmor().getDefense() + player.getAccessory().getDef();

        ArrayList<MonsterAttack> monsterAttacks = monster.getAttackList();
        int monsterMoveID = random.nextInt(monsterAttacks.size());
        MonsterAttack attack = monsterAttacks.get(monsterMoveID);

        int moveDMG = determineMonsterAttack(monster, attack);

        if (moveDMG == -1) {
            ft.printWithDelay(monster.getMonsterName() + " used " + attack.getAttackName() + " and missed.", TimeUnit.MILLISECONDS, 10);
        } else {
            int damageAfterDefense = moveDMG - playerDefense;

            //check if critical hit
            if (moveDMG > monster.getMaxDamage()) {
                ft.printWithDelay(monster.getMonsterName() + " used " + attack.getAttackName() + " and landed a critical hit", TimeUnit.MILLISECONDS, 10);
            } else {
                ft.printWithDelay(monster.getMonsterName() + " used " + attack.getAttackName() + " and landed a hit", TimeUnit.MILLISECONDS, 10);
            }


            if (damageAfterDefense <= 0) { //no need to update if no dmg is dealt.
                ft.printWithDelay("However, you're defense negated the attack", TimeUnit.MILLISECONDS, 10);
            } else {
                ft.printWithDelay("dealing " + damageAfterDefense + " damage.", TimeUnit.MILLISECONDS, 10);
                player.setHp(player.getHp() - damageAfterDefense);
            }
        }

    }

    /**
     * Method determineMonsterAttack
     * <p>
     * Determines the damage dealt by a monster's attack.
     *
     * @param monster The monster performing the attack
     * @param move    The specific attack move used by the monster
     * @return int - The damage dealt by the monster's attack, or -1 if the attack missed
     */
    private int determineMonsterAttack(Monster monster, MonsterAttack move) {
        //Attack is based off on the combined: STR, weaponDMG, AcessoryStatDMG, MoveDMG
        int monsterBaseDamage = random.nextInt(monster.getMinDamage() + 1, monster.getMaxDamage() + 1);
        int monsterDamage = monsterBaseDamage;

        double critDamage = move.getCritDamage();
        int critChance = move.getCritChance();
        int accuracy = move.getAccuracy();


        int rngCrit = random.nextInt(100) + 1;
        int rngAccuracy = random.nextInt(100) + 1;

        boolean isCrit = rngCrit > 0 && rngCrit <= critChance;
        boolean isAccurate = rngCrit > 0 && rngAccuracy <= accuracy;

        if (!isAccurate) {
            return -1;
        }

        if (isCrit) {
            monsterDamage *= critDamage;
        }

        return monsterDamage;
    }

    /**
     * Method playerAttack
     * <p>
     * Performs an attack by the player on the specified monster.
     *
     * @param monster The monster being attacked
     * @throws InterruptedException - if the attack is interrupted
     */
    private void playerAttack(Monster monster) throws InterruptedException {
        PlayerAttack move = new PlayerAttack();
        boolean foundAttack = false;
        int cmd;
        boolean validCmd = true;

        do {
            ft.printWithDelay("Choose Attack #:", TimeUnit.MILLISECONDS, 10);
            try {
                cmd = getCommand();
                break;
            } catch (InputMismatchException e) {
                ft.printWithDelay("Invalid Input", TimeUnit.MILLISECONDS, 10);
            }
        } while (true);


        for (PlayerAttack attack : player.getAttackList()) {
            if (attack.getAttackID() == cmd) {
                move = attack;
                foundAttack = true;
                break;
            }
        }

        if (!foundAttack) {
            ft.printWithDelay("Invalid Attack #", TimeUnit.MILLISECONDS, 10);
            playerAttack(monster);
        }

        int moveDMG = determinePlayerAttack(move);

        if (moveDMG == -1) {
            ft.printWithDelay("You used " + move.getAttackName() + " and ya missed " + monster.getMonsterName(), TimeUnit.MILLISECONDS, 10);
        } else if (player.getMp() < move.getMp()) { //Player doesn't have enough mp
            ft.printWithDelay("You don't have enough Mana (MP) for this move", TimeUnit.MILLISECONDS, 10);
            ft.printWithDelay("Player MP: " + player.getMp() + "/" + player.getMaxMp(), TimeUnit.MILLISECONDS, 10);
            ft.printWithDelay("Move MP: " + move.getMp(), TimeUnit.MILLISECONDS, 10);
            playerAttack(monster);
        } else {
            ft.printWithDelay("You used " + move.getAttackName() + " and attacked " + monster.getMonsterName() + ",", TimeUnit.MILLISECONDS, 10);
            if (moveDMG > move.getDmg() + player.getStr() + player.getWeapon().getAttack() + player.getAccessory().getAtk()) { //check if crit
                ft.printWithDelay("critical hit, you dealt " + moveDMG + " damage.", TimeUnit.MILLISECONDS, 10);
            } else { //normal attack
                ft.printWithDelay("dealing " + moveDMG + " damage.", TimeUnit.MILLISECONDS, 10);
            }


            monster.setHp((monster.getHp() - moveDMG));

            if (move.getMp() != 0) { //if move costs MP, reduce it.
                player.setMp(player.getMp() - move.getMp());
            }
        }

    }

    /**
     * Method getCommand
     * Prompts the user for their input and returns this to playGame
     * Prevents infinite loop from occurring in
     *
     * @return int - the command entered by the user.
     */
    private int getCommand() throws InterruptedException {
        int cmd;
        while (true) {
            try {
                cmd = Integer.parseInt(moveInput.nextLine());
                break;
            } catch (NumberFormatException e) {
                ft.printWithDelay("Invalid input\nPlease try again", TimeUnit.MILLISECONDS, 10);
            }
        }
        return cmd;
    }

    /**
     * Method determinePlayerAttack
     * <p>
     * Determines the damage of a player attack based on the specified move.
     *
     * @param move The player attack move
     * @return int - The damage dealt by the player attack
     */
    private int determinePlayerAttack(PlayerAttack move) {
        //Attack is based off on the combined: STR, weaponDMG, AcessoryStatDMG, MoveDMG
        int playerDamage = player.getStr() + player.getWeapon().getAttack() + player.getAccessory().getAtk() + move.getDmg();

        double critDamage = move.getCritDamage();
        int critChance = move.getCritChance();
        int accuracy = move.getAccuracy();

        int rngCrit = random.nextInt(100) + 1;
        int rngAccuracy = random.nextInt(100) + 1;

        boolean isCrit = rngCrit > 0 && rngCrit <= critChance;
        boolean isAccurate = rngCrit > 0 && rngAccuracy <= accuracy;

        if (!isAccurate) {
            return -1;
        }

        if (isCrit) {
            playerDamage *= critDamage;
        }


        return playerDamage;
    }


    /**
     * Method displayPlayerMoves
     * <p>
     * Displays the list of player moves/attacks.
     *
     * @throws InterruptedException - if the display is interrupted
     */
    private void displayPlayerMoves() throws InterruptedException {
        ft.printWithDelay("Player Moves:", TimeUnit.MILLISECONDS, 10);
        for (PlayerAttack attack : player.getAttackList()) {
            ft.printWithDelay(" #" + attack.getAttackID() + " | " + attack.getAttackName(), TimeUnit.MILLISECONDS, 10);
        }
    }


}
