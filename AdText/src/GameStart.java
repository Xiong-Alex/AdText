import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import view.FormatText;
import controller.GameController;
import controller.GameException;


/**
 * Class: GameStart
 *
 * @version 1.4
 * @author: Alex Xiong, Khales Rahman
 * * Course: ITEC 3860 Summer 2023
 * * Written: July 5, 2023
 * This class is the UI class for Adventure Text. This class will control all
 * user aspects
 * of these games.
 */
public class GameStart {

    private final Scanner input;
    private final GameController gc;
    private final FormatText ft;

    /**
     * Method GameStart
     * Constructor for the GameStart class
     * Creates an instance of the GameController class which is the interface into the controller package
     */
    public GameStart() {

        gc = new GameController();
        input = new Scanner(System.in);
        ft = new FormatText();

    }

    /**
     * Method playGame
     * Allows the player to play the game.
     * Prints an introduction message
     * Loops until the user chooses to exit.
     * Prints the current rooms display text if the direction is valid.
     * If an invalid direction is entered, catches the exception and prints the message in that exception.
     * Calls getCommand to get users input.
     * Passes the user's command to Commands, executeCommand for processing. This will handle move, item, look,
     * and backpack commands.
     */
    private void playGame() throws InterruptedException {

        boolean validLogin = false;

        String gameName = """

                ░█████╗░██████╗░██╗░░░██╗███████╗███╗░░██╗████████╗██╗░░░██╗██████╗░███████╗  ████████╗███████╗██╗░░██╗████████╗
                ██╔══██╗██╔══██╗██║░░░██║██╔════╝████╗░██║╚══██╔══╝██║░░░██║██╔══██╗██╔════╝  ╚══██╔══╝██╔════╝╚██╗██╔╝╚══██╔══╝
                ███████║██║░░██║╚██╗░██╔╝█████╗░░██╔██╗██║░░░██║░░░██║░░░██║██████╔╝█████╗░░  ░░░██║░░░█████╗░░░╚███╔╝░░░░██║░░░
                ██╔══██║██║░░██║░╚████╔╝░██╔══╝░░██║╚████║░░░██║░░░██║░░░██║██╔══██╗██╔══╝░░  ░░░██║░░░██╔══╝░░░██╔██╗░░░░██║░░░
                ██║░░██║██████╔╝░░╚██╔╝░░███████╗██║░╚███║░░░██║░░░╚██████╔╝██║░░██║███████╗  ░░░██║░░░███████╗██╔╝╚██╗░░░██║░░░
                ╚═╝░░╚═╝╚═════╝░░░░╚═╝░░░╚══════╝╚═╝░░╚══╝░░░╚═╝░░░░╚═════╝░╚═╝░░╚═╝╚══════╝  ░░░╚═╝░░░╚══════╝╚═╝░░╚═╝░░░╚═╝░░░
                """;
        ft.printWithDelay(gameName, TimeUnit.MILLISECONDS, 2);


        String gameIntro = """
                ----------
                Welcome to The Adventure Text game. You will proceed through rooms based upon your entries.
                You can navigate by using the entire direction or just the first letter.
                You can view a room using the 'Look' command.
                Enter 'Help' to view full list of possible commands.
                To exit(X) the game, enter exit or x
                ----------""";

        ft.printWithDelay(gameIntro, TimeUnit.MILLISECONDS, 10);

        do {
            String promptPlayer = "Are you a returning player or new player?\n" +
                    "1: New Player | 2: Returning Player";
            ft.printWithDelay(promptPlayer, TimeUnit.MILLISECONDS, 10);
            System.out.print(ft.convertText("Input 1 or 2: "));
            int cmd = input.nextInt();
            try {
                String logIn = gc.logIn(cmd);
                String npf = "No player with that username was found.\nPlease try again.";
                String ae = " already exists.\nPlease try again";
                String ic = "Invalid Command Please try again";
                ft.printWithDelay(logIn, TimeUnit.MILLISECONDS, 10);
                if (!logIn.equals(npf) && !logIn.equals(ae) && !logIn.equals(ic)) {
                    validLogin = true;
                } else System.out.println("----------");

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (!validLogin);


        String display;
        try {
            display = gc.displayFirstRoom();
            ft.printWithDelay(display, TimeUnit.MILLISECONDS, 10);
            System.out.println("----------");
        } catch (GameException e) {
            System.out.println(e.getMessage());
        }

        String response = "";
        System.out.print(ft.convertText("Enter a command: "));
        String command = getCommand();
        boolean firstGo = true;
        do {
            if (!firstGo) {
                System.out.print("----------\n");
                System.out.print(ft.convertText("Enter another command: "));
            }
            firstGo = false;
            command = getCommand();
            try {
                response = gc.executeCommand(command);
                ft.printWithDelay(response, TimeUnit.MILLISECONDS, 10);
            } catch (GameException | SQLException | ClassNotFoundException ge) {
                System.out.println(ge.getMessage());
            }
        } while (!response.equalsIgnoreCase("Exit"));

        System.out.println(ft.convertText("Thank you for playing my game."));
    }

    /**
     * Method getCommand
     * Prompts the user for their input and returns this to playGame
     *
     * @return String - the command entered by the user.
     */
    private String getCommand() {
        return input.nextLine();
    }

    /**
     * Method main
     * Starts the game, initializes the Scanner, calls playGame and then closes the Scanner
     * If the data file is not found, prints a message and exits.
     * If the data file is found and successfully loaded, prints the map by calling the
     * GameController printMap method and printing the String that methods returns.
     *
     * @param args - String[]
     */
    public static void main(String[] args) {

        GameStart ad = new GameStart();
        boolean fileFound = false;
        try {
            ad.gc.printMap();
            fileFound = true;
        } catch (GameException | SQLException | ClassNotFoundException ge) {
            System.out.println(ge.getMessage());
        }

        if (fileFound) {
            try {
                ad.playGame();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Could not load game :(");
        }
        ad.input.close();


    }

}