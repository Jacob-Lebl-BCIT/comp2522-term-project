package ca.bcit.cst.comp2522.termproject;

import java.util.Scanner;

/**
 * Offers a menu in the terminal (within an infinite loop) to select a choice of game.
 */
public class Main
{
    private static final String QUIT_MESSAGE = "bye!";
    private static final String MENU_MESSAGE = "\n=== Menu ===";

    private static final String CHOICE_WORD_GAME_MESSAGE          = "Press W to play the Word game.";
    private static final String CHOICE_NUMBER_GAME_MESSAGE        = "Press N to play the Number game.";
    private static final String CHOICE_MYGAME_GAME_FIRST_MESSAGE  = "Press M to play the ";
    private static final String CHOICE_MYGAME_GAME_SECOND_MESSAGE = " game.";
    private static final String CHOICE_QUIT_MESSAGE               = "Press Q to quit.";
    private static final boolean MENU_NOT_DONE = false;
    private static final boolean MENU_IS_DONE = true;

    private static final String MYGAME_NAME = "MyGame";

    private static final String CHOICE_QUIT   = "q";
    private static final String CHOICE_WORD   = "w";
    private static final String CHOICE_NUMBER = "n";
    private static final String CHOICE_MYGAME = "m";

    /**
     * main, runs the program.
     * @param args unused.
     */
    public static void main(final String[] args)
    {
        boolean done;
        done = MENU_NOT_DONE;

        String choice;

        try (Scanner scanner = new Scanner(System.in))
        {
            while (!done)
            {
                displayMenu();
                choice = scanner.nextLine();


                if (choice.equalsIgnoreCase(CHOICE_QUIT))
                {
                    System.out.println(QUIT_MESSAGE);
                    done = MENU_IS_DONE;
                }

            }


        }
    }

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
