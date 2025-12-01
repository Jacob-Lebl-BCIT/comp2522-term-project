package ca.bcit.cst.comp2522.termproject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tracks scoring statistics for WordGame sessions including date played,
 * number of games, and answer attempt accuracy. Maintains cumulative counts
 * of correct answers on first attempt, correct on second attempt, and
 * incorrect answers after two attempts. Calculates total score using the
 * scoring formula: first attempt = 2 points, second attempt = 1 point.
 *
 * @author Jacob
 * @version 2025-11-30
 */
public final class Score
{
    private static final int    POINTS_FOR_FIRST_ATTEMPT_CORRECT  = 2;
    private static final int    POINTS_FOR_SECOND_ATTEMPT_CORRECT = 1;
    private static final int    POINTS_FOR_INCORRECT              = 0;
    private static final String DATE_TIME_FORMAT_PATTERN          = "yyyy-MM-dd HH:mm:ss";

    private final LocalDateTime dateTimePlayed;
    private       int           gamesPlayed;
    private       int           correctFirstAttempt;
    private       int           correctSecondAttempt;
    private       int           incorrectTwoAttempts;

    /**
     * Constructs a new Score with the current date and time.
     * Initializes all game statistics to zero. The date and time are set
     * to the moment this constructor is called and cannot be changed.
     */
    public Score()
    {
        this.dateTimePlayed = LocalDateTime.now();
        this.gamesPlayed = 0;
        this.correctFirstAttempt = 0;
        this.correctSecondAttempt = 0;
        this.incorrectTwoAttempts = 0;
    }

    /**
     * Constructs a Score with specified date, time, and statistics.
     * Used for reconstructing scores from saved data or testing purposes.
     * All count parameters must be non-negative.
     *
     * @param dateTimePlayed         the date and time when games were played
     * @param gamesPlayed            the total number of games played
     * @param correctFirstAttempt    count of questions answered correctly on first try
     * @param correctSecondAttempt   count of questions answered correctly on second try
     * @param incorrectTwoAttempts   count of questions answered incorrectly after two tries
     * @throws IllegalArgumentException if dateTimePlayed is null or any count is negative
     */
    public Score(final LocalDateTime dateTimePlayed,
                 final int gamesPlayed,
                 final int correctFirstAttempt,
                 final int correctSecondAttempt,
                 final int incorrectTwoAttempts)
    {
        validateDateTime(dateTimePlayed);
        validateCount(gamesPlayed, "games played");
        validateCount(correctFirstAttempt, "correct first attempt");
        validateCount(correctSecondAttempt, "correct second attempt");
        validateCount(incorrectTwoAttempts, "incorrect two attempts");

        this.dateTimePlayed = dateTimePlayed;
        this.gamesPlayed = gamesPlayed;
        this.correctFirstAttempt = correctFirstAttempt;
        this.correctSecondAttempt = correctSecondAttempt;
        this.incorrectTwoAttempts = incorrectTwoAttempts;
    }

    /**
     * Returns the date and time when this score was recorded.
     *
     * @return the LocalDateTime when games were played
     */
    public LocalDateTime getDateTimePlayed()
    {
        return dateTimePlayed;
    }

    /**
     * Returns the total number of games played in this score session.
     *
     * @return the count of games played
     */
    public int getGamesPlayed()
    {
        return gamesPlayed;
    }

    /**
     * Returns the number of questions answered correctly on the first attempt.
     *
     * @return the count of first-attempt correct answers
     */
    public int getCorrectFirstAttempt()
    {
        return correctFirstAttempt;
    }

    /**
     * Returns the number of questions answered correctly on the second attempt.
     *
     * @return the count of second-attempt correct answers
     */
    public int getCorrectSecondAttempt()
    {
        return correctSecondAttempt;
    }

    /**
     * Returns the number of questions answered incorrectly after two attempts.
     *
     * @return the count of questions failed after two tries
     */
    public int getIncorrectTwoAttempts()
    {
        return incorrectTwoAttempts;
    }

    /**
     * Increments the count of games played by one.
     */
    public void incrementGamesPlayed()
    {
        gamesPlayed++;
    }

    /**
     * Increments the count of questions answered correctly on first attempt.
     */
    public void incrementCorrectFirstAttempt()
    {
        correctFirstAttempt++;
    }

    /**
     * Increments the count of questions answered correctly on second attempt.
     */
    public void incrementCorrectSecondAttempt()
    {
        correctSecondAttempt++;
    }

    /**
     * Increments the count of questions answered incorrectly after two attempts.
     */
    public void incrementIncorrectTwoAttempts()
    {
        incorrectTwoAttempts++;
    }

    /**
     * Calculates and returns the total score based on answer accuracy.
     * Scoring formula: first attempt correct = 2 points,
     *                  second attempt correct = 1 point,
     *                  incorrect after two attempts = 0 points.
     *
     * @return the total points earned
     */
    public int calculateTotalScore()
    {
        final int firstAttemptPoints;
        final int secondAttemptPoints;
        final int incorrectPoints;
        final int totalScore;

        firstAttemptPoints = correctFirstAttempt * POINTS_FOR_FIRST_ATTEMPT_CORRECT;
        secondAttemptPoints = correctSecondAttempt * POINTS_FOR_SECOND_ATTEMPT_CORRECT;
        incorrectPoints = incorrectTwoAttempts * POINTS_FOR_INCORRECT;
        totalScore = firstAttemptPoints + secondAttemptPoints + incorrectPoints;

        return totalScore;
    }

    /**
     * Formats this score as a multi-line string suitable for writing to score.txt.
     * Format matches the specification:
     * Date and Time: yyyy-MM-dd HH:mm:ss
     * Games Played: N
     * Correct First Attempts: N
     * Correct Second Attempts: N
     * Incorrect Attempts: N
     * Total Score: N points
     *
     * @return a formatted string representation of this score
     */
    public String toFileFormat()
    {
        final DateTimeFormatter formatter;
        final String            formattedDateTime;
        final int               totalScore;
        final StringBuilder     builder;

        formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        formattedDateTime = dateTimePlayed.format(formatter);
        totalScore = calculateTotalScore();

        builder = new StringBuilder();
        builder.append("Date and Time: ").append(formattedDateTime).append("\n");
        builder.append("Games Played: ").append(gamesPlayed).append("\n");
        builder.append("Correct First Attempts: ").append(correctFirstAttempt).append("\n");
        builder.append("Correct Second Attempts: ").append(correctSecondAttempt).append("\n");
        builder.append("Incorrect Attempts: ").append(incorrectTwoAttempts).append("\n");
        builder.append("Total Score: ").append(totalScore).append(" points\n");

        return builder.toString();
    }

    /**
     * Returns a string representation of this score for debugging.
     * Format: "Score{date=..., games=N, total=N points}"
     *
     * @return a string representation of this score
     */
    @Override
    public String toString()
    {
        final DateTimeFormatter formatter;
        final String            formattedDateTime;
        final int               totalScore;

        formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        formattedDateTime = dateTimePlayed.format(formatter);
        totalScore = calculateTotalScore();

        return "Score{date=" + formattedDateTime +
               ", games=" + gamesPlayed +
               ", total=" + totalScore + " points}";
    }

    /**
     * Validates that a date time is not null.
     *
     * @param dateTime the LocalDateTime to validate
     * @throws IllegalArgumentException if dateTime is null
     */
    private static void validateDateTime(final LocalDateTime dateTime)
    {
        if (dateTime == null)
        {
            throw new IllegalArgumentException("Date time cannot be null");
        }
    }

    /**
     * Validates that a count value is non-negative.
     *
     * @param count     the count value to validate
     * @param fieldName the name of the field being validated (for error messages)
     * @throws IllegalArgumentException if count is negative
     */
    private static void validateCount(final int count, final String fieldName)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException(
                fieldName + " cannot be negative, but was: " + count
            );
        }
    }
}
