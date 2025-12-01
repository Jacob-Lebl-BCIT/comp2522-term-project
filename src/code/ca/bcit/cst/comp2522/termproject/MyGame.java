package ca.bcit.cst.comp2522.termproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main game controller for ASL Learning Ladder - MyGame.
 * A JavaFX-based progressive learning game for ASL fingerspelling.
 * Demonstrates Facade pattern by providing simple interface to complex subsystems.
 *
 * <p>This class demonstrates:
 * - Facade design pattern
 * - Extensive lambda expressions in event handlers
 * - Method references for callbacks
 * - JavaFX GUI with multiple screens
 * - Threading with CountDownLatch for blocking play() method
 *
 * @author Jacob
 * @version 1.0
 */
public final class MyGame
{
    private static final int WINDOW_WIDTH_PIXELS = 800;
    private static final int WINDOW_HEIGHT_PIXELS = 600;
    private static final int PADDING_PIXELS = 20;
    private static final int SPACING_PIXELS = 10;
    private static final int IMAGE_SIZE_PIXELS = 300;
    private static final int BUTTON_WIDTH_PIXELS = 200;
    private static final String LETTERS_DIRECTORY = "lettersASL";
    private static final String IMAGE_EXTENSION = ".gif";
    private static final String TITLE = "ASL Learning Ladder";

    private static final AtomicBoolean javaFXPlatformInitialized = new AtomicBoolean(false);

    private Stage gameStage;
    private Player currentPlayer;
    private MasteryTracker<Character> masteryTracker;
    private SpeedDrillTest currentTest;
    private CountDownLatch windowCloseLatch;
    private Timeline testTimer;
    private int timeRemaining;

    /**
     * Constructs a new MyGame instance.
     */
    public MyGame()
    {
        this.masteryTracker = new MasteryTracker<>();
    }

    /**
     * Main entry point for the game.
     * Blocks until the player quits the game.
     * Integrates with Main.java menu system via blocking behavior.
     */
    public void play()
    {
        initializeJavaFXPlatform();

        Platform.setImplicitExit(false);

        windowCloseLatch = new CountDownLatch(1);

        Platform.runLater(this::showPlayerSelectionScreen);

        try
        {
            windowCloseLatch.await();
        }
        catch(final InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Initializes JavaFX platform if not already initialized.
     * Platform.startup() can only be called once per JVM.
     */
    private void initializeJavaFXPlatform()
    {
        if(!javaFXPlatformInitialized.getAndSet(true))
        {
            Platform.startup(() -> {});
        }
    }

    /**
     * Shows the player selection screen.
     * Allows user to select existing player or create new one.
     * Demonstrates lambda expressions in button event handlers.
     */
    private void showPlayerSelectionScreen()
    {
        final VBox layout;
        layout = new VBox(SPACING_PIXELS);
        layout.setPadding(new Insets(PADDING_PIXELS));
        layout.setAlignment(Pos.CENTER);

        final Label titleLabel;
        titleLabel = new Label("ASL Learning Ladder");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        final Label instructionLabel;
        instructionLabel = new Label("Select a player or create new:");

        final ListView<String> playerListView;
        playerListView = new ListView<>();

        loadPlayerNames().forEach(playerListView.getItems()::add);

        final TextField newPlayerField;
        newPlayerField = new TextField();
        newPlayerField.setPromptText("Enter new player name");
        newPlayerField.setMaxWidth(BUTTON_WIDTH_PIXELS);

        final Button selectButton;
        selectButton = new Button("Select Player");
        selectButton.setMinWidth(BUTTON_WIDTH_PIXELS);
        selectButton.setOnAction(event -> {
            final String selectedName;
            selectedName = playerListView.getSelectionModel().getSelectedItem();

            if(selectedName != null)
            {
                loadPlayer(selectedName);
            }
        });

        final Button createButton;
        createButton = new Button("Create New Player");
        createButton.setMinWidth(BUTTON_WIDTH_PIXELS);
        createButton.setOnAction(event -> {
            final String playerName;
            playerName = newPlayerField.getText().trim();

            if(!playerName.isEmpty())
            {
                createNewPlayer(playerName);
            }
        });

        final Button quitButton;
        quitButton = new Button("Quit");
        quitButton.setMinWidth(BUTTON_WIDTH_PIXELS);
        quitButton.setOnAction(event -> closeGame());

        layout.getChildren().addAll(titleLabel, instructionLabel, playerListView,
                                   newPlayerField, selectButton, createButton, quitButton);

        final Scene scene;
        scene = new Scene(layout, WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        setupStage(scene);
    }

    /**
     * Loads list of player names from players directory.
     * Demonstrates method reference with stream operations.
     *
     * @return list of player names
     */
    private List<String> loadPlayerNames()
    {
        final List<String> names;
        names = new ArrayList<>();

        final File playersDir;
        playersDir = new File("players");

        if(playersDir.exists() && playersDir.isDirectory())
        {
            final File[] files;
            files = playersDir.listFiles((dir, name) -> name.endsWith(".txt"));

            if(files != null)
            {
                for(final File file : files)
                {
                    final String name;
                    name = file.getName().replace(".txt", "");

                    names.add(name);
                }
            }
        }

        return names;
    }

    /**
     * Loads existing player from file.
     *
     * @param playerName the name of the player to load
     */
    private void loadPlayer(final String playerName)
    {
        try
        {
            currentPlayer = Player.load(playerName);
            syncMasteryTrackerFromPlayer();
            showMainMenuScreen();
        }
        catch(final IOException e)
        {
            System.err.println("Error loading player: " + e.getMessage());
        }
    }

    /**
     * Creates a new player with the specified name.
     *
     * @param playerName the name for the new player
     */
    private void createNewPlayer(final String playerName)
    {
        currentPlayer = new Player(playerName, LocalDateTime.now());
        masteryTracker = new MasteryTracker<>();
        showMainMenuScreen();
    }

    /**
     * Synchronizes MasteryTracker with Player's letter mastery data.
     * Called after loading a player from file.
     */
    private void syncMasteryTrackerFromPlayer()
    {
        masteryTracker = new MasteryTracker<>();

        final Set<Character> unlockedLetters;
        unlockedLetters = currentPlayer.getUnlockedLetters();

        for(final char letter : unlockedLetters)
        {
            final double mastery;
            mastery = currentPlayer.getLetterMastery(letter);

            if(mastery > 0.0)
            {
                final int totalAttempts = 10;
                final int correctAttempts = (int) (mastery / 100.0 * totalAttempts);

                for(int i = 0; i < correctAttempts; i++)
                {
                    masteryTracker.recordAttempt(letter, true);
                }

                for(int i = correctAttempts; i < totalAttempts; i++)
                {
                    masteryTracker.recordAttempt(letter, false);
                }
            }
        }
    }

    /**
     * Shows the main menu screen with tier progress and test options.
     * Demonstrates extensive lambda usage in button handlers.
     */
    private void showMainMenuScreen()
    {
        final VBox layout;
        layout = new VBox(SPACING_PIXELS);
        layout.setPadding(new Insets(PADDING_PIXELS));
        layout.setAlignment(Pos.CENTER);

        final Label welcomeLabel;
        welcomeLabel = new Label("Welcome, " + currentPlayer.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        final Label tierLabel;
        tierLabel = new Label("Unlocked Letters: " + currentPlayer.getUnlockedLetters().size());

        final Label masteryLabel;
        masteryLabel = new Label(String.format("Overall Mastery: %.1f%%",
                                              currentPlayer.getTotalMasteryPercent()));

        final Button speedDrillButton;
        speedDrillButton = new Button("Start Speed Drill Test");
        speedDrillButton.setMinWidth(BUTTON_WIDTH_PIXELS);
        speedDrillButton.setOnAction(event -> startSpeedDrillTest());

        final Button saveQuitButton;
        saveQuitButton = new Button("Save & Quit");
        saveQuitButton.setMinWidth(BUTTON_WIDTH_PIXELS);
        saveQuitButton.setOnAction(event -> saveAndQuit());

        layout.getChildren().addAll(welcomeLabel, tierLabel, masteryLabel,
                                   speedDrillButton, saveQuitButton);

        final Scene scene;
        scene = new Scene(layout, WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        setupStage(scene);
    }

    /**
     * Starts a Speed Drill test session.
     * Creates test, shows test screen, and manages test flow.
     */
    private void startSpeedDrillTest()
    {
        final List<Character> testLetters;
        testLetters = new ArrayList<>(currentPlayer.getUnlockedLetters());

        currentTest = new SpeedDrillTest(testLetters, masteryTracker);

        currentTest.setQuestionPresenter(this::displayQuestion);

        currentTest.prepareTest();
        currentTest.displayInstructions();

        showTestScreen();

        advanceToNextQuestion();
    }

    /**
     * Advances to the next question in the current test.
     * Uses method reference for timer callback.
     */
    private void advanceToNextQuestion()
    {
        if(currentTest.hasMoreQuestions())
        {
            currentTest.presentQuestion();
            startQuestionTimer();
        }
        else
        {
            finishTest();
        }
    }

    /**
     * Displays the current test question (letter).
     * Loads and shows ASL handshape image.
     *
     * @param letter the letter to display
     */
    private void displayQuestion(final Character letter)
    {
        // Question display is handled by showTestScreen() which updates with current letter
    }

    /**
     * Shows the test screen with ASL image and answer input.
     * Demonstrates lambda expressions and image loading.
     */
    private void showTestScreen()
    {
        final BorderPane layout;
        layout = new BorderPane();
        layout.setPadding(new Insets(PADDING_PIXELS));

        final VBox topBox;
        topBox = new VBox(SPACING_PIXELS);
        topBox.setAlignment(Pos.CENTER);

        final Label questionNumberLabel;
        questionNumberLabel = new Label(String.format("Question %d/%d",
                                                     currentTest.getCurrentQuestionNumber(),
                                                     currentTest.getTotalQuestionsCount()));
        questionNumberLabel.setStyle("-fx-font-size: 18px;");

        final Label timerLabel;
        timerLabel = new Label("Time: 5");
        timerLabel.setStyle("-fx-font-size: 16px;");

        topBox.getChildren().addAll(questionNumberLabel, timerLabel);

        final ImageView imageView;
        imageView = loadLetterImage(currentTest.getCurrentLetter());

        final VBox centerBox;
        centerBox = new VBox(SPACING_PIXELS);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(imageView);

        final HBox bottomBox;
        bottomBox = new HBox(SPACING_PIXELS);
        bottomBox.setAlignment(Pos.CENTER);

        final TextField answerField;
        answerField = new TextField();
        answerField.setPromptText("Enter letter (A-Z)");
        answerField.setMaxWidth(BUTTON_WIDTH_PIXELS);

        final Button submitButton;
        submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            final String answer;
            answer = answerField.getText().trim().toUpperCase();

            if(!answer.isEmpty())
            {
                stopQuestionTimer();
                submitAnswer(answer.charAt(0));
                answerField.clear();
            }
        });

        bottomBox.getChildren().addAll(answerField, submitButton);

        layout.setTop(topBox);
        layout.setCenter(centerBox);
        layout.setBottom(bottomBox);

        final Scene scene;
        scene = new Scene(layout, WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        setupStage(scene);
    }

    /**
     * Loads ASL handshape image for specified letter.
     * Demonstrates file I/O and error handling.
     *
     * @param letter the letter to load image for
     * @return ImageView containing the letter image
     */
    private ImageView loadLetterImage(final char letter)
    {
        final ImageView imageView;
        imageView = new ImageView();

        try
        {
            final String filename;
            filename = LETTERS_DIRECTORY + "/" + Character.toLowerCase(letter) + IMAGE_EXTENSION;

            final FileInputStream imageStream;
            imageStream = new FileInputStream(filename);

            final Image image;
            image = new Image(imageStream);

            imageView.setImage(image);
            imageView.setFitWidth(IMAGE_SIZE_PIXELS);
            imageView.setFitHeight(IMAGE_SIZE_PIXELS);
            imageView.setPreserveRatio(true);
        }
        catch(final FileNotFoundException e)
        {
            final Label errorLabel;
            errorLabel = new Label("Image not found: " + letter);

            imageView.setImage(null);
        }

        return imageView;
    }

    /**
     * Starts the countdown timer for the current question.
     * Uses lambda expression for timer action.
     */
    private void startQuestionTimer()
    {
        timeRemaining = currentTest.getTimeLimitSeconds();

        testTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;

            if(timeRemaining <= 0)
            {
                stopQuestionTimer();
                submitAnswer('\0');
            }
        }));

        testTimer.setCycleCount(currentTest.getTimeLimitSeconds());
        testTimer.play();
    }

    /**
     * Stops the question timer.
     */
    private void stopQuestionTimer()
    {
        if(testTimer != null)
        {
            testTimer.stop();
        }
    }

    /**
     * Submits the player's answer and advances to next question.
     *
     * @param answer the letter answer provided by player
     */
    private void submitAnswer(final char answer)
    {
        currentTest.setPlayerAnswer(answer);
        currentTest.processAnswer();

        advanceToNextQuestion();
    }

    /**
     * Finishes the current test and shows results.
     * Updates player mastery data from test results.
     */
    private void finishTest()
    {
        final TestSession.TestResult result;
        result = currentTest.calculateResults();

        updatePlayerMasteryFromTracker();

        currentPlayer.recordTestCompletion(result.getCorrectAnswers());

        showResultsScreen(result);
    }

    /**
     * Updates player's letter mastery from MasteryTracker data.
     */
    private void updatePlayerMasteryFromTracker()
    {
        for(final char letter : currentPlayer.getUnlockedLetters())
        {
            final double mastery;
            mastery = masteryTracker.getMasteryPercent(letter);

            currentPlayer.updateLetterMastery(letter, mastery);
        }
    }

    /**
     * Shows the results screen after test completion.
     * Demonstrates string formatting and conditional UI logic.
     *
     * @param result the test result to display
     */
    private void showResultsScreen(final TestSession.TestResult result)
    {
        final VBox layout;
        layout = new VBox(SPACING_PIXELS);
        layout.setPadding(new Insets(PADDING_PIXELS));
        layout.setAlignment(Pos.CENTER);

        final Label titleLabel;
        titleLabel = new Label("Test Complete!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        final Label scoreLabel;
        scoreLabel = new Label(String.format("Score: %d/%d (%.1f%%)",
                                            result.getCorrectAnswers(),
                                            result.getTotalQuestions(),
                                            result.getPercentageScore()));
        scoreLabel.setStyle("-fx-font-size: 18px;");

        final Label passLabel;
        passLabel = new Label(result.isPassed() ? "PASSED!" : "Keep practicing!");
        passLabel.setStyle(result.isPassed() ?
                          "-fx-text-fill: green; -fx-font-size: 20px; -fx-font-weight: bold;" :
                          "-fx-text-fill: orange; -fx-font-size: 20px;");

        final Button menuButton;
        menuButton = new Button("Back to Menu");
        menuButton.setMinWidth(BUTTON_WIDTH_PIXELS);
        menuButton.setOnAction(event -> showMainMenuScreen());

        layout.getChildren().addAll(titleLabel, scoreLabel, passLabel, menuButton);

        final Scene scene;
        scene = new Scene(layout, WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        setupStage(scene);
    }

    /**
     * Saves player data and quits the game.
     * Returns control to Main.java menu.
     */
    private void saveAndQuit()
    {
        try
        {
            currentPlayer.save();
            closeGame();
        }
        catch(final IOException e)
        {
            System.err.println("Error saving player: " + e.getMessage());
            closeGame();
        }
    }

    /**
     * Closes the game window and releases the blocking latch.
     * Returns control to Main.java menu system.
     */
    private void closeGame()
    {
        if(gameStage != null)
        {
            gameStage.close();
        }

        windowCloseLatch.countDown();
    }

    /**
     * Sets up the stage with a new scene.
     * Creates new stage if needed, or updates existing stage.
     *
     * @param scene the scene to display
     */
    private void setupStage(final Scene scene)
    {
        if(gameStage == null)
        {
            gameStage = new Stage();
            gameStage.setTitle(TITLE);
            gameStage.setOnCloseRequest(event -> closeGame());
        }

        gameStage.setScene(scene);

        if(!gameStage.isShowing())
        {
            gameStage.show();
        }
    }
}
