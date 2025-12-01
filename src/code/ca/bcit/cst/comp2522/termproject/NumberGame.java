package ca.bcit.cst.comp2522.termproject;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A JavaFX-based GUI game where players place randomly generated numbers
 * in ascending order on a 4x5 grid. Players must place 20 numbers (ranging
 * from 1-1000) in sequence such that all numbers remain in ascending order.
 * Tracks wins, losses, and average successful placements across multiple games.
 * Integrates with Main.java menu system using a blocking play() method.
 *
 * @author Jacob
 * @version 2025-12-01
 */
public final class NumberGame
{
    private static final int GRID_ROWS_COUNT = 4;
    private static final int GRID_COLUMNS_COUNT = 5;
    private static final int TOTAL_CELLS_COUNT = 20;
    private static final int MIN_RANDOM_VALUE = 1;
    private static final int MAX_RANDOM_VALUE = 1000;
    private static final int WINDOW_WIDTH_PIXELS = 600;
    private static final int WINDOW_HEIGHT_PIXELS = 500;
    private static final int BUTTON_SIZE_PIXELS = 80;
    private static final int GRID_SPACING_PIXELS = 5;
    private static final int PADDING_PIXELS = 20;
    private static final int LABEL_FONT_SIZE_POINTS = 20;
    private static final int BUTTON_FONT_SIZE_POINTS = 16;
    private static final int PLATFORM_STARTUP_DELAY_MS = 100;

    private static final String WINDOW_TITLE = "NumberGame";
    private static final String CURRENT_NUMBER_PREFIX = "Current Number: ";
    private static final String WIN_TITLE = "Congratulations!";
    private static final String WIN_MESSAGE = "You placed all 20 numbers successfully!";
    private static final String LOSS_TITLE = "Game Over";
    private static final String LOSS_MESSAGE_PREFIX = "No valid placement for ";
    private static final String LOSS_MESSAGE_SUFFIX = ". You placed ";
    private static final String LOSS_MESSAGE_NUMBERS = " numbers.";
    private static final String TRY_AGAIN_BUTTON_TEXT = "Try Again";
    private static final String QUIT_BUTTON_TEXT = "Quit";
    private static final String BUTTON_STYLE = "-fx-font-size: " + BUTTON_FONT_SIZE_POINTS + "px;";
    private static final String LABEL_STYLE = "-fx-font-size: " + LABEL_FONT_SIZE_POINTS + "px; -fx-font-weight: bold;";

    private final GameStatistics sessionStatistics;
    private final Random randomNumberGenerator;

    private NumberGameBoard currentBoard;
    private int[] randomNumbersSequence;
    private int currentNumberIndex;

    private Stage gameStage;
    private Button[] gridButtons;
    private Label currentNumberLabel;

    private CountDownLatch windowCloseLatch;
    private boolean userWantsToPlayAgain;

    /**
     * Constructs a new NumberGame instance.
     * Initializes session statistics tracker and random number generator.
     * Game state is reset for each play session through the play() method.
     */
    public NumberGame()
    {
        this.sessionStatistics = new GameStatistics();
        this.randomNumberGenerator = new Random();
    }

    /**
     * Main entry point for the NumberGame.
     * Initializes JavaFX platform if needed, runs game loop allowing multiple
     * plays, and displays final statistics when user quits. This method blocks
     * until the user chooses to quit, then returns control to Main.java.
     * Sets Platform.setImplicitExit(false) to prevent JavaFX from shutting
     * down when window closes, allowing multiple game sessions.
     */
    public void play()
    {
        initializeJavaFXPlatform();
        Platform.setImplicitExit(false);

        userWantsToPlayAgain = true;

        while (userWantsToPlayAgain)
        {
            runSingleGame();
        }

        displayFinalStatistics();
    }

    /**
     * Initializes the JavaFX platform if not already running.
     * Handles the constraint that Platform.startup() can only be called
     * once per JVM. Uses defensive checking to determine if platform is
     * already initialized before attempting startup.
     */
    private void initializeJavaFXPlatform()
    {
        final boolean platformAlreadyRunning;

        platformAlreadyRunning = isJavaFXPlatformRunning();

        if (!platformAlreadyRunning)
        {
            try
            {
                Platform.startup(() -> {
                    // Empty runnable - just initializes the platform
                });
            }
            catch (final IllegalStateException exception)
            {
                // Platform already started in another way, continue
            }
        }
    }

    /**
     * Checks if the JavaFX platform is currently running.
     * Attempts to execute a runnable on the JavaFX thread and checks if
     * it completes successfully. Returns true if platform is running,
     * false otherwise.
     *
     * @return true if JavaFX platform is initialized and running, false otherwise
     */
    private boolean isJavaFXPlatformRunning()
    {
        final AtomicBoolean running;

        running = new AtomicBoolean(false);

        try
        {
            Platform.runLater(() -> running.set(true));
            Thread.sleep(PLATFORM_STARTUP_DELAY_MS);
            return running.get();
        }
        catch (final IllegalStateException exception)
        {
            return false;
        }
        catch (final InterruptedException exception)
        {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Runs a single game instance from start to finish.
     * Resets game state, generates random numbers, creates and shows
     * the game window, then blocks using CountDownLatch until the game
     * completes (win, loss, or quit). When the latch counts down, this
     * method returns and control flows back to play() loop.
     */
    private void runSingleGame()
    {
        currentBoard = new NumberGameBoard();
        randomNumbersSequence = generateRandomNumbers();
        currentNumberIndex = 0;
        windowCloseLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            setupGameWindow();
            gameStage.show();
        });

        try
        {
            windowCloseLatch.await();
        }
        catch (final InterruptedException exception)
        {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Generates an array of 20 random integers between MIN_RANDOM_VALUE
     * and MAX_RANDOM_VALUE inclusive. Each number is independently generated
     * and there may be duplicates. Returns a new array for each game.
     *
     * @return array of 20 random integers for the game
     */
    private int[] generateRandomNumbers()
    {
        final int[] numbers;
        final int randomRange;

        numbers = new int[TOTAL_CELLS_COUNT];
        randomRange = MAX_RANDOM_VALUE - MIN_RANDOM_VALUE + 1;

        for (int i = 0; i < TOTAL_CELLS_COUNT; i++)
        {
            numbers[i] = randomNumberGenerator.nextInt(randomRange) + MIN_RANDOM_VALUE;
        }

        return numbers;
    }

    /**
     * Creates and configures the JavaFX game window.
     * Builds a VBox containing a label for current number display and a
     * GridPane with 4x5 grid of buttons. Sets up button click handlers,
     * window close handler, and displays the first number. Must be called
     * on JavaFX application thread.
     */
    private void setupGameWindow()
    {
        final VBox root;
        final Scene scene;
        final GridPane gridPane;

        gameStage = new Stage();
        gameStage.setTitle(WINDOW_TITLE);

        currentNumberLabel = new Label();
        currentNumberLabel.setStyle(LABEL_STYLE);
        updateCurrentNumberLabel();

        gridButtons = new Button[TOTAL_CELLS_COUNT];
        gridPane = createGridPane();

        root = new VBox(PADDING_PIXELS);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(currentNumberLabel, gridPane);

        scene = new Scene(root, WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);
        gameStage.setScene(scene);

        gameStage.setOnCloseRequest(event -> handleWindowClose());
    }

    /**
     * Creates and configures the GridPane containing the 4x5 button grid.
     * Initializes all 20 buttons with empty text, sets their size and style,
     * and attaches click handlers that pass the button's position index.
     * Returns the configured GridPane ready for display.
     *
     * @return GridPane containing 20 buttons in 4x5 layout
     */
    private GridPane createGridPane()
    {
        final GridPane gridPane;

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(GRID_SPACING_PIXELS);
        gridPane.setVgap(GRID_SPACING_PIXELS);
        gridPane.setPadding(new Insets(PADDING_PIXELS));

        for (int i = 0; i < TOTAL_CELLS_COUNT; i++)
        {
            final Button button;
            final int position;
            final int rowIndex;
            final int colIndex;

            position = i;
            button = new Button();
            button.setMinSize(BUTTON_SIZE_PIXELS, BUTTON_SIZE_PIXELS);
            button.setMaxSize(BUTTON_SIZE_PIXELS, BUTTON_SIZE_PIXELS);
            button.setStyle(BUTTON_STYLE);
            button.setOnAction(event -> handleButtonClick(position));

            gridButtons[position] = button;

            rowIndex = i / GRID_COLUMNS_COUNT;
            colIndex = i % GRID_COLUMNS_COUNT;
            gridPane.add(button, colIndex, rowIndex);
        }

        return gridPane;
    }

    /**
     * Updates the current number label to display the next number to be placed.
     * Retrieves the number from randomNumbersSequence at currentNumberIndex
     * and formats it for display. Should be called after each successful
     * placement and at game start.
     */
    private void updateCurrentNumberLabel()
    {
        final int currentNumber;
        final String labelText;

        currentNumber = randomNumbersSequence[currentNumberIndex];
        labelText = CURRENT_NUMBER_PREFIX + currentNumber;
        currentNumberLabel.setText(labelText);
    }

    /**
     * Handles button click events for grid buttons.
     * Validates if the current number can be placed at the clicked position
     * using the NumberGameBoard. If valid, places the number, updates the
     * button display, disables the button, increments to next number, and
     * checks for win/loss conditions. Invalid clicks are silently ignored.
     *
     * @param position the index (0-19) of the button that was clicked
     */
    private void handleButtonClick(final int position)
    {
        final int currentNumber;
        final boolean canPlace;

        if (currentNumberIndex >= TOTAL_CELLS_COUNT)
        {
            return;
        }

        currentNumber = randomNumbersSequence[currentNumberIndex];
        canPlace = currentBoard.canPlaceNumber(currentNumber, position);

        if (canPlace)
        {
            try
            {
                currentBoard.placeNumber(currentNumber, position);
                gridButtons[position].setText(String.valueOf(currentNumber));
                gridButtons[position].setDisable(true);
                currentNumberIndex++;

                checkGameStatus();
            }
            catch (final IllegalStateException exception)
            {
                // Placement failed unexpectedly, ignore
            }
        }
    }

    /**
     * Checks the current game status to determine if game has ended.
     * If all 20 numbers are placed (board full), triggers win sequence.
     * If current number cannot be placed anywhere on board, triggers loss
     * sequence. Otherwise, updates display for next number and continues play.
     */
    private void checkGameStatus()
    {
        final boolean boardFull;

        boardFull = currentBoard.isBoardFull();

        if (boardFull)
        {
            handleWin();
        }
        else if (currentNumberIndex < TOTAL_CELLS_COUNT)
        {
            final int nextNumber;
            final boolean hasValidPlacement;

            nextNumber = randomNumbersSequence[currentNumberIndex];
            hasValidPlacement = currentBoard.hasValidPlacement(nextNumber);

            if (hasValidPlacement)
            {
                updateCurrentNumberLabel();
            }
            else
            {
                handleLoss();
            }
        }
    }

    /**
     * Handles the win condition when all 20 numbers are successfully placed.
     * Records win in statistics, displays congratulations alert with Try Again
     * and Quit options. Based on user choice, either sets flag to play again
     * or quit, then closes window and counts down latch to unblock runSingleGame().
     */
    private void handleWin()
    {
        final Alert alert;
        final ButtonType tryAgainButton;
        final ButtonType quitButton;

        sessionStatistics.recordWin(TOTAL_CELLS_COUNT);

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(WIN_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(WIN_MESSAGE);

        tryAgainButton = new ButtonType(TRY_AGAIN_BUTTON_TEXT);
        quitButton = new ButtonType(QUIT_BUTTON_TEXT);
        alert.getButtonTypes().setAll(tryAgainButton, quitButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == tryAgainButton)
            {
                userWantsToPlayAgain = true;
            }
            else
            {
                userWantsToPlayAgain = false;
            }

            gameStage.close();
            windowCloseLatch.countDown();
        });
    }

    /**
     * Handles the loss condition when no valid placement exists for current number.
     * Records loss with number of successful placements in statistics, displays
     * game over alert showing the problematic number and placement count, with
     * Try Again and Quit options. Based on user choice, sets flag to play again
     * or quit, then closes window and counts down latch.
     */
    private void handleLoss()
    {
        final Alert alert;
        final ButtonType tryAgainButton;
        final ButtonType quitButton;
        final int currentNumber;
        final int placementCount;
        final String message;

        currentNumber = randomNumbersSequence[currentNumberIndex];
        placementCount = currentBoard.getFilledCellsCount();

        sessionStatistics.recordLoss(placementCount);

        message = LOSS_MESSAGE_PREFIX + currentNumber +
                  LOSS_MESSAGE_SUFFIX + placementCount +
                  LOSS_MESSAGE_NUMBERS;

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LOSS_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(message);

        tryAgainButton = new ButtonType(TRY_AGAIN_BUTTON_TEXT);
        quitButton = new ButtonType(QUIT_BUTTON_TEXT);
        alert.getButtonTypes().setAll(tryAgainButton, quitButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == tryAgainButton)
            {
                userWantsToPlayAgain = true;
            }
            else
            {
                userWantsToPlayAgain = false;
            }

            gameStage.close();
            windowCloseLatch.countDown();
        });
    }

    /**
     * Handles window close events when user clicks the X button.
     * Treats window close as quit action - sets play again flag to false,
     * closes the stage, and counts down the latch to unblock runSingleGame()
     * and return control to Main menu.
     */
    private void handleWindowClose()
    {
        userWantsToPlayAgain = false;
        gameStage.close();
        windowCloseLatch.countDown();
    }

    /**
     * Displays final session statistics to the terminal.
     * Retrieves formatted statistics message from GameStatistics showing
     * wins, losses, total placements, and average placements per game.
     * Called when user quits, right before play() returns to Main menu.
     */
    private void displayFinalStatistics()
    {
        final String statisticsMessage;

        statisticsMessage = sessionStatistics.getStatisticsMessage();
        System.out.println("\n" + statisticsMessage);
    }
}
