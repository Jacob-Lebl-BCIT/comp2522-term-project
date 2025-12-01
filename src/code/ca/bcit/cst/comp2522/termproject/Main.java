package ca.bcit.cst.comp2522.termproject;

import java.util.Scanner;

/**
 * Entry point for the COMP2522 Term Project game menu system.
 * Provides a terminal-based REPL (Read-Eval-Print Loop) that allows users
 * to select and play one of three games: WordGame, NumberGame, or MyGame.
 * The menu runs in an infinite loop until the user chooses to quit.
 *
 * @author Jacob
 * @version 1.0
 */
public class Main
{
    private static final String QUIT_MESSAGE           = "bye!";
    private static final String MENU_MESSAGE           = "\n=== Menu ===";
    private static final String INVALID_CHOICE_MESSAGE = "Invalid choice, please try again.";

    private static final String  CHOICE_WORD_GAME_MESSAGE          = "Press W to play the Word game.";
    private static final String  CHOICE_NUMBER_GAME_MESSAGE        = "Press N to play the Number game.";
    private static final String  CHOICE_MYGAME_GAME_FIRST_MESSAGE  = "Press M to play the ";
    private static final String  CHOICE_MYGAME_GAME_SECOND_MESSAGE = " game.";
    private static final String  CHOICE_QUIT_MESSAGE = "Press Q to quit.";
    private static final boolean MENU_IS_DONE        = true;
    private static final boolean MENU_NOT_DONE       = false;

    private static final String MYGAME_NAME = "MyGame";


    private static final char CHOICE_QUIT   = 'Q';
    private static final char CHOICE_WORD   = 'W';
    private static final char CHOICE_NUMBER = 'N';
    private static final char CHOICE_MYGAME = 'M';

    /**
     * Entry point for the program.
     * Initializes the menu loop, displays options, reads user input to select
     * a game or quit, and executes the corresponding action. Continues until
     * user selects quit option. Properly closes Scanner resources on exit.
     *
     * @param args command-line arguments (unused in this application)
     */
    public static void main(final String[] args)
    {
        boolean done;
        char userChoice;

        done = MENU_NOT_DONE;

        try (Scanner scanner = new Scanner(System.in))
        {
            while (!done)
            {
                displayMenu();
                userChoice = getUserChoice(scanner);

                if (userChoice == CHOICE_QUIT)
                {
                    System.out.println(QUIT_MESSAGE);
                    done = MENU_IS_DONE;
                }
                else if (userChoice == CHOICE_WORD)
                {
                    try
                    {
                        final WordGame wordGame;
                        wordGame = new WordGame();
                        wordGame.play();
                    }
                    catch (final IllegalStateException exception)
                    {
                        System.out.println("Error: " + exception.getMessage());
                    }
                }
                else if (userChoice == CHOICE_NUMBER)
                {
                    final NumberGame numberGame;
                    numberGame = new NumberGame();
                    numberGame.play();
                }
                else if (userChoice == CHOICE_MYGAME)
                {
                    final MyGame myGame;
                    myGame = new MyGame();
                    myGame.play();
                }
                else
                {
                    System.out.println(INVALID_CHOICE_MESSAGE);
                }
            }
        }
    }

    /**
     * Prompts the user for input and returns the first character as uppercase.
     * Handles empty input by repeatedly prompting until valid input is received.
     * Uses a loop to avoid potential stack overflow from recursive calls.
     * Trims whitespace from input before processing.
     *
     * @param scanner the Scanner object to read user input from System.in
     * @return the first character of user input, converted to uppercase
     */
    private static char getUserChoice(final Scanner scanner)
    {
        String input;

        input = scanner.nextLine().trim();

        while (input.isEmpty())
        {
            System.out.println(INVALID_CHOICE_MESSAGE);
            input = scanner.nextLine().trim();
        }

        final char choice;
        choice = Character.toUpperCase(input.charAt(0));

        return choice;
    }

    /**
     * Displays the game selection menu to the terminal.
     * Prints a header followed by four options: WordGame, NumberGame,
     * MyGame, and Quit. Each option shows the corresponding key to press.
     */
    private static void displayMenu()
    {
        System.out.println(MENU_MESSAGE);
        System.out.println(CHOICE_WORD_GAME_MESSAGE);
        System.out.println(CHOICE_NUMBER_GAME_MESSAGE);
        System.out.println(CHOICE_MYGAME_GAME_FIRST_MESSAGE + MYGAME_NAME +
                           CHOICE_MYGAME_GAME_SECOND_MESSAGE);
        System.out.println(CHOICE_QUIT_MESSAGE);
    }
}
