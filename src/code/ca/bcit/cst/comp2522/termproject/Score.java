package ca.bcit.cst.comp2522.termproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks scoring statistics for WordGame sessions including date played,
 * number of games, and answer attempt accuracy. Maintains cumulative counts
 * of correct answers on first attempt, correct on second attempt, and
 * incorrect answers after two attempts. Calculates total score using the
 * scoring formula: first attempt = 2 points, second attempt = 1 point.
 *
 * @author Jacob
 * @version 1.0
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
     * This is the primary method for accessing the calculated score.
     *
     * @return the total points earned
     */
    public int getScore()
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
     * Returns a string representation of this score in file format.
     * Format matches the specification required for score.txt and tests:
     * Date and Time: yyyy-MM-dd HH:mm:ss
     * Games Played: N
     * Correct First Attempts: N
     * Correct Second Attempts: N
     * Incorrect Attempts: N
     * Score: N points
     *
     * @return a formatted string representation of this score
     */
    @Override
    public String toString()
    {
        final DateTimeFormatter formatter;
        final String            formattedDateTime;
        final int               totalScore;
        final StringBuilder     builder;

        formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        formattedDateTime = dateTimePlayed.format(formatter);
        totalScore = getScore();

        builder = new StringBuilder();
        builder.append("Date and Time: ").append(formattedDateTime).append("\n");
        builder.append("Games Played: ").append(gamesPlayed).append("\n");
        builder.append("Correct First Attempts: ").append(correctFirstAttempt).append("\n");
        builder.append("Correct Second Attempts: ").append(correctSecondAttempt).append("\n");
        builder.append("Incorrect Attempts: ").append(incorrectTwoAttempts).append("\n");
        builder.append("Score: ").append(totalScore).append(" points\n");

        return builder.toString();
    }

    /**
     * Appends a score to the specified file.
     * Opens the file in append mode and writes the score using its toString() method.
     * Creates the file if it doesn't exist. If an IOException occurs during writing,
     * it is propagated to the caller.
     *
     * @param score    the Score object to append to the file
     * @param filename the name of the file to append to
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static void appendScoreToFile(final Score score, final String filename) throws IOException
    {
        final BufferedWriter writer;
        final String         scoreData;

        writer = new BufferedWriter(new FileWriter(filename, true));

        try
        {
            scoreData = score.toString();
            writer.write(scoreData);
            writer.flush();
        }
        finally
        {
            writer.close();
        }
    }

    /**
     * Reads all scores from the specified file.
     * Parses the file line-by-line, reading 6-line blocks for each score:
     * 1. Date and Time: yyyy-MM-dd HH:mm:ss
     * 2. Games Played: N
     * 3. Correct First Attempts: N
     * 4. Correct Second Attempts: N
     * 5. Incorrect Attempts: N
     * 6. Score: N points
     *
     * Returns an empty list if the file doesn't exist or is empty.
     * Skips malformed entries and continues parsing.
     *
     * @param filename the name of the file to read scores from
     * @return a List of Score objects read from the file
     * @throws IOException if an I/O error occurs while reading from the file
     */
    public static List<Score> readScoresFromFile(final String filename) throws IOException
    {
        final File         file;
        final List<Score>  scores;

        file = new File(filename);
        scores = new ArrayList<>();

        if (!file.exists())
        {
            return scores;
        }

        final BufferedReader reader;
        reader = new BufferedReader(new FileReader(file));

        try
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty())
                {
                    continue;
                }

                if (line.startsWith("Date and Time: "))
                {
                    final Score parsedScore;
                    parsedScore = parseScoreEntry(line, reader);

                    if (parsedScore != null)
                    {
                        scores.add(parsedScore);
                    }
                }
            }
        }
        finally
        {
            reader.close();
        }

        return scores;
    }

    /**
     * Parses a single score entry from the file.
     * Reads the date/time line (already provided) and the next 5 lines
     * to construct a Score object. Returns null if parsing fails.
     *
     * @param dateTimeLine the "Date and Time: ..." line
     * @param reader       the BufferedReader to read remaining lines from
     * @return a Score object if parsing succeeds, null otherwise
     * @throws IOException if an I/O error occurs while reading
     */
    private static Score parseScoreEntry(final String dateTimeLine,
                                          final BufferedReader reader) throws IOException
    {
        try
        {
            final DateTimeFormatter formatter;
            final LocalDateTime     dateTime;
            final String            gamesLine;
            final String            firstAttemptsLine;
            final String            secondAttemptsLine;
            final String            incorrectLine;
            final String            scoreLine;
            final int               gamesPlayed;
            final int               correctFirstAttempts;
            final int               correctSecondAttempts;
            final int               incorrectAttempts;

            formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
            dateTime = LocalDateTime.parse(dateTimeLine.substring("Date and Time: ".length()), formatter);

            gamesLine = reader.readLine();
            firstAttemptsLine = reader.readLine();
            secondAttemptsLine = reader.readLine();
            incorrectLine = reader.readLine();
            scoreLine = reader.readLine();

            if (gamesLine == null || firstAttemptsLine == null ||
                secondAttemptsLine == null || incorrectLine == null || scoreLine == null)
            {
                return null;
            }

            gamesPlayed = Integer.parseInt(gamesLine.substring("Games Played: ".length()));
            correctFirstAttempts = Integer.parseInt(firstAttemptsLine.substring("Correct First Attempts: ".length()));
            correctSecondAttempts = Integer.parseInt(secondAttemptsLine.substring("Correct Second Attempts: ".length()));
            incorrectAttempts = Integer.parseInt(incorrectLine.substring("Incorrect Attempts: ".length()));

            return new Score(dateTime, gamesPlayed, correctFirstAttempts,
                           correctSecondAttempts, incorrectAttempts);
        }
        catch (final Exception exception)
        {
            return null;
        }
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
