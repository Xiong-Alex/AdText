# ProjectEthanAlexKhales
"Adventurer Text" is a text base video game where the player can interact using the console. The program will enable the player to traverse a dungeon while collecting loot, solving five puzzles, and battling five monsters. 
The game contains 30 rooms, and the player must first solve a puzzle, then fight a monster to progress to the next section of rooms. They can equip armor, weapons, scrolls, health potions, and accessories from more than 30 items. 
The game has a help system to provide guidance, a list of commands, an inventory, and a login system. It tracks the player's scores and rooms visited and automatically saves the player's progress.

# How to play:
When launching the code, you will be asked if you are a returning or new player. Please enter 1 if you are a new player or 2 if you are a returning player.
After answering the question, you will have to enter a username. If you are a returning player, please enter the username of the account you used before. Otherwise, please create a new username; you will use this username to login back into your account.
Once you finish the login, the game will start, and you will begin at the "Stone corridor".
Your goal is to reach the end of the dungeon while solving puzzles and defeating monsters.
You can type "look" to see the description, items, and available exits for the room you are currently in.
To move to another room, type one of the available exits into the console; for example, if one of the exits is north, you will type "North".
Some rooms will contain items that can be listed using the "look" command. To pick up an item, type "get" followed by the item name. For example, "get iorn sword". After you pick up the item, it will be transferred to your inventory which stores all the items you pick up; to view your inventory, type "Inventory". If you want to view the details of an item in your inventory, type "Inspect <Item Name>" (ex. "Inspect Iron Sword"). To equip or unequip any items that are in your inventory type "Equip" or "Unequip" followed by the item type and then the item's name. For example, if you want to equip an iron sword, you would type "Equip Weapon Iron Sword". Some of the items can be used to interact with a specific room—to do this, type "Use <Item Name>" (ex. "Use Bronze Key") while in the room you want to use the item on.

# INSTALLATION: 
- SDK must be set to openjdk-19 (Oracle OpenJDK version 20.0.1)
- JAR file: sqlite-jdbc-3.42.0.0 must be installed.
  - Download JAR file to local machine 
  - Open Project Structures menu
  - Click Libraries under Project Settings
  - Click the plus sign
  - find/add JAR file to project 

Type "help" in the console to see the list of possible commands for the game.

# Possible commands:
-Look: View Room
-<Direction>: Move to the next room via Exit
-Get <Item Name>: Pick up Item
-Drop <Item Name>: Drop Item
-Player: View Player
-Inventory: Check Inventory
-Inspect <Item Name>: Reveals description & other attributes of Item
-Equip/Unequip armor <armor Name>: Equip & Unequip Armor
-Equip/Unequip weapon <weapon Name>: Equip & Unequip Weapon
-Exit or X: will end the game
-Use <Item Name>: uses the item if allowed
-Allocate(A) # SP to <STAT>: Allocates your SP to your Stats
