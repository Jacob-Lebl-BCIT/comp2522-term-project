package ca.bcit.cst.comp2522.termproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * WordGame is a geography trivia game that tests knowledge of countries,
 * capitals, and interesting facts. Players answer 10 random questions
 * with up to 2 attempts per question. Scores are tracked across multiple
 * game sessions and saved to score.txt.
 *
 * @author Jacob
 * @version 1.0
 */
public final class WordGame
{
    private static final String COUNTRIES_DIRECTORY_PATH  = "countries";
    private static final String SCORE_FILE_PATH           = "score.txt";
    private static final String FILE_EXTENSION            = ".txt";
    private static final char   COUNTRY_CAPITAL_SEPARATOR = ':';
    private static final int    REQUIRED_NUMBER_OF_FACTS  = 3;
    private static final int    FIRST_LETTER_ASCII        = 'a';
    private static final int    LAST_LETTER_ASCII         = 'z';
    private static final char   LETTER_TO_SKIP            = 'x';

    private static final int TOTAL_QUESTIONS_PER_GAME         = 10;
    private static final int MAX_ATTEMPTS_PER_QUESTION        = 2;
    private static final int NUMBER_OF_QUESTION_TYPES         = 3;
    private static final int QUESTION_TYPE_CAPITAL_TO_COUNTRY = 0;
    private static final int QUESTION_TYPE_COUNTRY_TO_CAPITAL = 1;

    private static final String PROMPT_PLAY_AGAIN         = "Would you like to play again? (Yes/No): ";
    private static final String ERROR_INVALID_PLAY_AGAIN  = "Invalid input. Please enter Yes or No.";
    private static final String SESSION_STATS_HEADER      = "=== Session Statistics ===";

    private final World   world;
    private final Score   currentScore;
    private final Scanner userInputScanner;
    private final Random  randomGenerator;

    /**
     * Constructs a new WordGame instance.
     * Initializes the world by loading all country data from text files
     * in the countries directory. Creates a new score tracker for this
     * game session. Initializes the scanner for reading user input.
     *
     * @throws IllegalStateException if no country data files could be loaded
     */
    public WordGame()
    {
        this.world            = new World();
        this.currentScore     = new Score();
        this.userInputScanner = new Scanner(System.in);
        this.randomGenerator  = new Random();

        loadAllCountryFiles();

        if (world.isEmpty())
        {
            throw new IllegalStateException("No country data loaded. Game cannot start.");
        }
    }

    /**
     * Starts and runs the WordGame.
     * Loads country data from text files, presents 10 random geography
     * questions per game, tracks scoring across attempts, displays cumulative scores,
     * and allows the user to play multiple games in one session.
     * Saves high scores to file when user chooses to quit.
     */
    public void play()
    {
        boolean playAgain;

        if (world.isEmpty())
        {
            System.out.println("Cannot start game: no country data available.");
            return;
        }

        System.out.println("\n=== WordGame: Geography Trivia ===");
        System.out.println("Answer 10 questions about countries, capitals, and facts.");
        System.out.println("You have 2 attempts per question.");
        System.out.println("Loaded " + world.getCountryCount() + " countries.\n");

        playAgain = true;

        while (playAgain)
        {
            currentScore.incrementGamesPlayed();

            for (int questionNumber = 1; questionNumber <= TOTAL_QUESTIONS_PER_GAME; questionNumber++)
            {
                System.out.println("Question " + questionNumber + " of " + TOTAL_QUESTIONS_PER_GAME + ":");
                askQuestion();
                System.out.println();
            }

            displaySessionStats();
            playAgain = promptPlayAgain();
        }

        displayFinalScore();
    }

    /**
     * Asks a single random question and processes the user's answer.
     * Randomly selects one of three question types, generates the question,
     * allows up to two attempts, and updates the score accordingly.
     */
    private void askQuestion()
    {
        final int questionType;
        final Country selectedCountry;
        final String question;
        final String correctAnswer;

        questionType    = randomGenerator.nextInt(NUMBER_OF_QUESTION_TYPES);
        selectedCountry = world.getRandomCountry();

        if (questionType == QUESTION_TYPE_CAPITAL_TO_COUNTRY)
        {
            question      = "What country has the capital: " + selectedCountry.getCapital() + "?";
            correctAnswer = selectedCountry.getName();
        }
        else if (questionType == QUESTION_TYPE_COUNTRY_TO_CAPITAL)
        {
            question      = "What is the capital of " + selectedCountry.getName() + "?";
            correctAnswer = selectedCountry.getCapital();
        }
        else
        {
            final int factIndex;
            final String fact;

            factIndex     = randomGenerator.nextInt(REQUIRED_NUMBER_OF_FACTS);
            fact          = selectedCountry.getFact(factIndex);
            question      = "Which country does this fact describe?\n  \"" + fact + "\"";
            correctAnswer = selectedCountry.getName();
        }

        System.out.println(question);
        processAnswer(correctAnswer);
    }

    /**
     * Processes user answers for a question, allowing up to two attempts.
     * Compares user input with the correct answer (case-insensitive).
     * Updates score based on whether answer was correct on first attempt,
     * second attempt, or incorrect after two attempts.
     *
     * @param correctAnswer the correct answer to compare against
     */
    private void processAnswer(final String correctAnswer)
    {
        for (int attemptNumber = 1; attemptNumber <= MAX_ATTEMPTS_PER_QUESTION; attemptNumber++)
        {
            System.out.print("Your answer (attempt " + attemptNumber + "): ");

            final String userAnswer;
            userAnswer = userInputScanner.nextLine().trim();

            if (isCorrectAnswer(userAnswer, correctAnswer))
            {
                System.out.println("Correct!");

                if (attemptNumber == 1)
                {
                    currentScore.incrementCorrectFirstAttempt();
                }
                else
                {
                    currentScore.incrementCorrectSecondAttempt();
                }

                return;
            }
            else
            {
                if (attemptNumber < MAX_ATTEMPTS_PER_QUESTION)
                {
                    System.out.println("Incorrect. Try again!");
                }
                else
                {
                    System.out.println("Incorrect. The answer was: " + correctAnswer);
                    currentScore.incrementIncorrectTwoAttempts();
                }
            }
        }
    }

    /**
     * Compares user's answer with the correct answer.
     * Comparison is case-insensitive and ignores leading/trailing whitespace.
     *
     * @param userAnswer    the answer provided by the user
     * @param correctAnswer the correct answer to compare against
     * @return true if the answers match, false otherwise
     */
    private boolean isCorrectAnswer(final String userAnswer,
                                    final String correctAnswer)
    {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    /**
     * Prompts the user if they want to play another game.
     * Accepts "Yes" or "No" in any letter case (or "y"/"n").
     * Re-prompts with error message on invalid input.
     *
     * @return true if user wants to play again, false otherwise
     */
    private boolean promptPlayAgain()
    {
        while (true)
        {
            System.out.print(PROMPT_PLAY_AGAIN);

            final String input;
            input = userInputScanner.nextLine().trim();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
            {
                return true;
            }
            else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
            {
                return false;
            }
            else
            {
                System.out.println(ERROR_INVALID_PLAY_AGAIN);
            }
        }
    }

    /**
     * Calculates the average score per game for a Score object.
     * Formula: total points divided by number of games played.
     *
     * @param score the Score object to calculate average for
     * @return average points per game as a double
     */
    private double calculateAverage(final Score score)
    {
        final int totalPoints;
        final int games;
        final double average;

        totalPoints = score.getScore();
        games = score.getGamesPlayed();
        average = (double) totalPoints / games;

        return average;
    }

    /**
     * Displays current session statistics after each game.
     * Shows games played, correct/incorrect attempts, total score, and average.
     * Does NOT save to file or check high scores (that happens when session ends).
     */
    private void displaySessionStats()
    {
        final int totalScore;
        final double average;

        System.out.println("\n" + SESSION_STATS_HEADER);
        System.out.println("Games played this session: " + currentScore.getGamesPlayed());
        System.out.println("Correct on first attempt:  " + currentScore.getCorrectFirstAttempt());
        System.out.println("Correct on second attempt: " + currentScore.getCorrectSecondAttempt());
        System.out.println("Incorrect after 2 attempts: " + currentScore.getIncorrectTwoAttempts());

        totalScore = currentScore.getScore();
        average = calculateAverage(currentScore);

        System.out.println("Total Score: " + totalScore + " points");
        System.out.println(String.format("Average: %.2f points per game\n", average));
    }

    /**
     * Displays the final score summary at the end of the session.
     * Saves the score to score.txt and checks if it's a new high score.
     * Called only when user chooses not to play again.
     */
    private void displayFinalScore()
    {
        saveScoreToFile();
        checkAndAnnounceHighScore();
    }

    /**
     * Saves the current score to the score.txt file.
     * Appends the score data to the existing file, or creates a new file
     * if it doesn't exist. Uses the Score.appendScoreToFile() static method.
     * Prints error message if save fails.
     */
    private void saveScoreToFile()
    {
        try
        {
            Score.appendScoreToFile(currentScore, SCORE_FILE_PATH);
        }
        catch (final IOException exception)
        {
            System.out.println("Warning: Could not save score to file: " + exception.getMessage());
        }
    }

    /**
     * Checks if the current session's average score is a new high score.
     * Reads all previous scores from score.txt, calculates averages,
     * and announces results with proper formatting showing averages (not totals),
     * previous record details including date and time.
     * Handles file reading errors gracefully.
     */
    private void checkAndAnnounceHighScore()
    {
        try
        {
            final double currentAverage;
            final List<Score> previousScores;

            currentAverage = calculateAverage(currentScore);
            previousScores = Score.readScoresFromFile(SCORE_FILE_PATH);

            if (previousScores.isEmpty())
            {
                System.out.println(String.format(
                    "CONGRATULATIONS! You are the new high score with an average of %.2f points per game!",
                    currentAverage
                ));
                return;
            }

            Score highestScore;
            double highestAverage;

            highestScore = null;
            highestAverage = 0.0;

            for (final Score score : previousScores)
            {
                final double average;
                average = calculateAverage(score);

                if (average > highestAverage)
                {
                    highestAverage = average;
                    highestScore = score;
                }
            }

            if (currentAverage > highestAverage)
            {
                final DateTimeFormatter formatter;
                final String formattedDateTime;

                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss");
                formattedDateTime = highestScore.getDateTimePlayed().format(formatter);

                System.out.println(String.format(
                    "CONGRATULATIONS! You are the new high score with an average of %.2f points per game; " +
                    "the previous record was %.2f points per game on %s",
                    currentAverage,
                    highestAverage,
                    formattedDateTime
                ));
            }
            else if (currentAverage == highestAverage && highestAverage > 0.0)
            {
                System.out.println(String.format(
                    "Tied the high score: %.2f points per game!",
                    currentAverage
                ));
            }
            else
            {
                final DateTimeFormatter formatter;
                final String formattedDateTime;

                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss");
                formattedDateTime = highestScore.getDateTimePlayed().format(formatter);

                System.out.println(String.format(
                    "You did not beat the high score of %.2f points per game from %s",
                    highestAverage,
                    formattedDateTime
                ));
            }
        }
        catch (final IOException exception)
        {
            System.out.println("Warning: Could not read previous scores: " + exception.getMessage());
        }
    }

    /**
     * Loads all country data files from the countries directory.
     * Iterates through files a.txt to z.txt (skipping x.txt which does not exist).
     * For each file that exists, parses the country data and adds countries
     * to the world collection. Prints error messages for files that cannot
     * be read, but continues processing remaining files.
     */
    private void loadAllCountryFiles()
    {
        for (int letterAscii = FIRST_LETTER_ASCII; letterAscii <= LAST_LETTER_ASCII; letterAscii++)
        {
            final char letter;
            letter = (char) letterAscii;

            if (letter == LETTER_TO_SKIP)
            {
                continue;
            }

            final String fileName;
            fileName = letter + FILE_EXTENSION;

            loadCountryFile(fileName);
        }
    }

    /**
     * Loads country data from a single file in the countries directory.
     * Parses the file line by line, creating Country objects and adding them
     * to the world collection. Handles IOException by printing error message
     * and continuing execution.
     *
     * @param fileName the name of the file to load (e.g., "a.txt")
     */
    private void loadCountryFile(final String fileName)
    {
        final String filePath;
        final File file;

        filePath = COUNTRIES_DIRECTORY_PATH + File.separator + fileName;
        file     = new File(filePath);

        if (!file.exists())
        {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            parseCountryFile(reader);
        }
        catch (final IOException exception)
        {
            System.out.println("Warning: Could not read file " + fileName + ": " + exception.getMessage());
        }
    }

    /**
     * Parses country data from a BufferedReader.
     * Reads lines in groups of 5: blank line, country:capital, fact1, fact2, fact3.
     * Creates Country objects and adds them to the world.
     * Continues until end of file is reached.
     *
     * @param reader the BufferedReader to read country data from
     * @throws IOException if an I/O error occurs while reading
     */
    private void parseCountryFile(final BufferedReader reader) throws IOException
    {
        String line;

        while ((line = reader.readLine()) != null)
        {
            if (line.trim().isEmpty())
            {
                continue;
            }

            if (line.contains(String.valueOf(COUNTRY_CAPITAL_SEPARATOR)))
            {
                parseAndAddCountry(line, reader);
            }
        }
    }

    /**
     * Parses a single country entry and adds it to the world.
     * Reads the country:capital line, then reads three fact lines.
     * Creates a Country object and adds it to the world collection.
     * Throws exceptions if the data format is invalid or incomplete.
     *
     * @param countryCapitalLine the line containing "CountryName:CapitalCity"
     * @param reader             the BufferedReader to read fact lines from
     * @throws IOException              if an I/O error occurs while reading facts
     * @throws IllegalArgumentException if country:capital format is invalid or Country validation fails
     * @throws IllegalStateException    if facts are incomplete (less than 3 facts available)
     */
    private void parseAndAddCountry(final String countryCapitalLine,
                                    final BufferedReader reader) throws IOException
    {
        final String[] parts;
        parts = countryCapitalLine.split(String.valueOf(COUNTRY_CAPITAL_SEPARATOR));

        if (parts.length != 2)
        {
            throw new IllegalArgumentException("Invalid country:capital format: " + countryCapitalLine);
        }

        final String countryName;
        final String capitalName;
        final String[] facts;

        countryName = parts[0].trim();
        capitalName = parts[1].trim();
        facts       = new String[REQUIRED_NUMBER_OF_FACTS];

        for (int i = 0; i < REQUIRED_NUMBER_OF_FACTS; i++)
        {
            final String factLine;
            factLine = reader.readLine();

            if (factLine == null)
            {
                throw new IllegalStateException("Incomplete facts for country: " + countryName);
            }

            facts[i] = factLine.trim();
        }

        final Country country;
        country = new Country(countryName, capitalName, facts);
        world.addCountry(country);
    }
}
