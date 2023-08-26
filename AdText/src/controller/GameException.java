package controller;

import view.FormatText;

import java.io.IOException;

/**
 * Class: GameException
 * * Course: ITEC 3860 Spring 2023
 * * Written: June 21, 2023
 * This class  Handles Exits in the game.
 */

public class GameException extends IOException {

    private final FormatText ft = new FormatText();
    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
