package ca.bcit.cst.comp2522.termproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Concrete test session implementing a speed drill format.
 * Players must quickly identify ASL handshape letters within a time limit.
 * This is the simplest test mode, ideal for practicing letter recognition.
 *
 * <p>Test format:
 * - 20 questions total
 * - 5 seconds per question (enforced by GUI)
 * - Random order from unlocked letters
 * - Score based on correct identifications
 *
 * <p>This class demonstrates:
 * - Concrete implementation of abstract TestSession
 * - Strategy pattern (swappable test algorithm)
 * - Template Method pattern usage
 *
 * @author Jacob
 * @version 1.0
 */
public final class SpeedDrillTest extends TestSession
{
    private static final int QUESTIONS_COUNT = 20;
    private static final int TIME_LIMIT_SECONDS = 5;

    private final List<Character> questionSequence;
    private int currentQuestionIndex;
    private char currentLetter;
    private Consumer<Character> questionPresenter;
    private char playerAnswer;

    /**
     * Constructs a new SpeedDrillTest with specified letters and tracker.
     *
     * @param testLetters the letters available for testing (must not be null or empty)
     * @param tracker the mastery tracker to update (must not be null)
     * @throws IllegalArgumentException if testLetters is null/empty or tracker is null
     */
    public SpeedDrillTest(final List<Character> testLetters,
                         final MasteryTracker<Character> tracker)
    {
        super(testLetters, tracker);

        this.questionSequence = new ArrayList<>();
        this.currentQuestionIndex = ZERO_COUNT;
        this.questionPresenter = null;
    }

    /**
     * Prepares the test by generating a random sequence of 20 questions.
     * Questions are randomly selected from available test letters.
     * Some letters may appear multiple times if there are fewer than 20 letters.
     */
    @Override
    protected void prepareTest()
    {
        questionSequence.clear();

        final List<Character> availableLetters;
        availableLetters = new ArrayList<>(testLetters);

        Collections.shuffle(availableLetters);

        for(int i = ZERO_COUNT; i < QUESTIONS_COUNT; i++)
        {
            final int letterIndex;
            letterIndex = i % availableLetters.size();

            final char letter;
            letter = availableLetters.get(letterIndex);

            questionSequence.add(letter);
        }

        currentQuestionIndex = ZERO_COUNT;
        totalQuestions = ZERO_COUNT;
        correctCount = ZERO_COUNT;
    }

    /**
     * Displays test instructions to the player.
     * Instructions explain the speed drill format and scoring.
     * In GUI mode, this is called by MyGame to show instruction screen.
     */
    @Override
    protected void displayInstructions()
    {
        // Instructions are displayed by GUI (MyGame)
        // This method serves as a hook for test initialization
        // No console output needed for JavaFX implementation
    }

    /**
     * Checks if there are more questions remaining in the test.
     *
     * @return true if currentQuestionIndex < QUESTIONS_COUNT, false otherwise
     */
    @Override
    protected boolean hasMoreQuestions()
    {
        return currentQuestionIndex < QUESTIONS_COUNT;
    }

    /**
     * Presents the current question to the player.
     * Gets the next letter from questionSequence and notifies the GUI via questionPresenter.
     * In GUI mode, this triggers display of the ASL handshape image.
     */
    @Override
    protected void presentQuestion()
    {
        currentLetter = questionSequence.get(currentQuestionIndex);

        if(questionPresenter != null)
        {
            questionPresenter.accept(currentLetter);
        }
    }

    /**
     * Processes the player's answer to the current question.
     * Compares playerAnswer to currentLetter, updates counts and mastery tracker.
     * Called by MyGame after player submits an answer.
     */
    @Override
    protected void processAnswer()
    {
        totalQuestions++;

        final boolean correct;
        correct = (playerAnswer == currentLetter);

        if(correct)
        {
            correctCount++;
        }

        tracker.recordAttempt(currentLetter, correct);

        currentQuestionIndex++;
    }

    /**
     * Sets the answer provided by the player.
     * Called by MyGame when player submits their answer via GUI.
     *
     * @param answer the letter the player identified (A-Z)
     */
    public void setPlayerAnswer(final char answer)
    {
        this.playerAnswer = answer;
    }

    /**
     * Sets the question presenter callback.
     * The presenter is called with each new letter to display in the GUI.
     * Allows MyGame to receive notifications when questions change.
     *
     * <p>Demonstrates lambda/functional interface usage for callbacks.
     *
     * @param presenter consumer function that displays a letter question
     */
    public void setQuestionPresenter(final Consumer<Character> presenter)
    {
        this.questionPresenter = presenter;
    }

    /**
     * Returns the current letter being tested.
     * Used by GUI to verify answers or display correct answer after timeout.
     *
     * @return the current question letter
     */
    public char getCurrentLetter()
    {
        return currentLetter;
    }

    /**
     * Returns the current question number (1-based for display).
     *
     * @return current question number (1 to QUESTIONS_COUNT)
     */
    public int getCurrentQuestionNumber()
    {
        return currentQuestionIndex + 1;
    }

    /**
     * Returns the total number of questions in this test.
     *
     * @return total questions count (20)
     */
    public int getTotalQuestionsCount()
    {
        return QUESTIONS_COUNT;
    }

    /**
     * Returns the time limit per question in seconds.
     *
     * @return time limit in seconds (5)
     */
    public int getTimeLimitSeconds()
    {
        return TIME_LIMIT_SECONDS;
    }
}
