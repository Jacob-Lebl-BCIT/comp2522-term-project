package ca.bcit.cst.comp2522.termproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all test sessions in the ASL Learning Ladder game.
 * Implements the Template Method design pattern to define the structure of a test session
 * while allowing subclasses to customize specific steps.
 *
 * <p>The test flow is defined in the final runTest() method:
 * 1. Prepare the test
 * 2. Display instructions
 * 3. While there are more questions:
 *    a. Present question
 *    b. Process answer
 * 4. Calculate and return results
 *
 * <p>This class demonstrates:
 * - Template Method design pattern
 * - Abstract class with both abstract and concrete methods
 * - Final method to prevent overriding the template structure
 *
 * @author Jacob
 * @version 1.0
 */
public abstract class TestSession
{
    protected static final int ZERO_COUNT = 0;
    protected static final int PERCENT_MULTIPLIER = 100;

    protected final List<Character> testLetters;
    protected final MasteryTracker<Character> tracker;
    protected int correctCount;
    protected int totalQuestions;

    /**
     * Constructs a new TestSession with specified test letters and mastery tracker.
     *
     * @param testLetters the letters to test (must not be null or empty)
     * @param tracker the mastery tracker to update (must not be null)
     * @throws IllegalArgumentException if testLetters is null/empty or tracker is null
     */
    protected TestSession(final List<Character> testLetters,
                         final MasteryTracker<Character> tracker)
    {
        if(testLetters == null || testLetters.isEmpty())
        {
            throw new IllegalArgumentException("Test letters cannot be null or empty");
        }

        if(tracker == null)
        {
            throw new IllegalArgumentException("Tracker cannot be null");
        }

        this.testLetters = new ArrayList<>(testLetters);
        this.tracker = tracker;
        this.correctCount = ZERO_COUNT;
        this.totalQuestions = ZERO_COUNT;
    }

    /**
     * Template method that defines the test execution flow.
     * This method is final to prevent subclasses from changing the overall structure.
     * Subclasses customize behavior by implementing the abstract methods called within.
     *
     * <p>Test flow:
     * 1. prepareTest() - Set up test-specific data
     * 2. displayInstructions() - Show instructions to player
     * 3. Loop while hasMoreQuestions():
     *    - presentQuestion() - Display current question
     *    - processAnswer() - Get and validate answer, update counts
     * 4. calculateResults() - Compute final results
     *
     * @return TestResult containing test outcomes and statistics
     */
    public final TestResult runTest()
    {
        prepareTest();
        displayInstructions();

        while(hasMoreQuestions())
        {
            presentQuestion();
            processAnswer();
        }

        return calculateResults();
    }

    /**
     * Prepares the test by setting up questions, shuffling order, or initializing state.
     * Called once before the test begins.
     * Subclasses must implement their specific preparation logic.
     */
    protected abstract void prepareTest();

    /**
     * Displays test instructions to the player.
     * Called once after preparation but before first question.
     * Subclasses should provide test-specific instructions.
     */
    protected abstract void displayInstructions();

    /**
     * Checks if there are more questions remaining in the test.
     * Called before each question to determine if test should continue.
     *
     * @return true if more questions remain, false if test is complete
     */
    protected abstract boolean hasMoreQuestions();

    /**
     * Presents the current question to the player.
     * This may involve displaying an image, text prompt, or interactive element.
     * Called for each question in the test.
     */
    protected abstract void presentQuestion();

    /**
     * Processes the player's answer to the current question.
     * Should get the answer, validate it, update correctCount and totalQuestions,
     * and record the attempt in the mastery tracker.
     * Called immediately after each presentQuestion() call.
     */
    protected abstract void processAnswer();

    /**
     * Calculates and returns the final test results.
     * This concrete method provides shared result calculation logic.
     * Called once after all questions have been processed.
     *
     * <p>Calculates:
     * - Total questions asked
     * - Correct answers count
     * - Percentage score
     * - Pass/fail status (80% threshold)
     *
     * @return TestResult object containing all test outcomes
     */
    protected TestResult calculateResults()
    {
        final double percentageScore;

        if(totalQuestions == ZERO_COUNT)
        {
            percentageScore = 0.0;
        }
        else
        {
            percentageScore = (double) correctCount / totalQuestions * PERCENT_MULTIPLIER;
        }

        final boolean passed;
        final double passingThreshold = 80.0;
        passed = percentageScore >= passingThreshold;

        final TestResult result;
        result = new TestResult(totalQuestions, correctCount, percentageScore, passed);

        return result;
    }

    /**
     * Returns the current correct answer count.
     *
     * @return number of correct answers so far
     */
    protected int getCorrectCount()
    {
        return correctCount;
    }

    /**
     * Returns the total number of questions asked so far.
     *
     * @return total questions count
     */
    protected int getTotalQuestions()
    {
        return totalQuestions;
    }

    /**
     * Immutable data class representing the results of a completed test session.
     * Contains all relevant statistics and outcomes from the test.
     *
     * <p>This nested class demonstrates:
     * - Nested class for encapsulating test result data
     * - Immutability (all fields final, no setters)
     * - Single responsibility (result data only)
     */
    public static final class TestResult
    {
        private final int totalQuestions;
        private final int correctAnswers;
        private final double percentageScore;
        private final boolean passed;

        /**
         * Constructs a TestResult with specified values.
         *
         * @param totalQuestions total number of questions in the test
         * @param correctAnswers number of correct answers
         * @param percentageScore percentage score (0.0 to 100.0)
         * @param passed whether the test was passed (typically >= 80%)
         */
        private TestResult(final int totalQuestions,
                          final int correctAnswers,
                          final double percentageScore,
                          final boolean passed)
        {
            this.totalQuestions = totalQuestions;
            this.correctAnswers = correctAnswers;
            this.percentageScore = percentageScore;
            this.passed = passed;
        }

        /**
         * Returns the total number of questions in the test.
         *
         * @return total questions
         */
        public int getTotalQuestions()
        {
            return totalQuestions;
        }

        /**
         * Returns the number of correct answers.
         *
         * @return correct answers count
         */
        public int getCorrectAnswers()
        {
            return correctAnswers;
        }

        /**
         * Returns the percentage score.
         *
         * @return percentage (0.0 to 100.0)
         */
        public double getPercentageScore()
        {
            return percentageScore;
        }

        /**
         * Returns whether the test was passed.
         *
         * @return true if passed, false otherwise
         */
        public boolean isPassed()
        {
            return passed;
        }

        /**
         * Formats the test result as a readable string.
         *
         * @return formatted result string
         */
        @Override
        public String toString()
        {
            return String.format("Test Result: %d/%d correct (%.1f%%) - %s",
                               correctAnswers,
                               totalQuestions,
                               percentageScore,
                               passed ? "PASSED" : "FAILED");
        }
    }
}
