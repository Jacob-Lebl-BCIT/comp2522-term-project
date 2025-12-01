package ca.bcit.cst.comp2522.termproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a player in the ASL Learning Ladder game.
 * Tracks player progress including unlocked letters, mastery percentages per letter,
 * and cumulative statistics. Players can be ranked by total mastery using the Comparable interface.
 *
 * <p>This class demonstrates:
 * - Comparable interface implementation
 * - Nested class (Statistics inner class)
 * - File I/O with try-with-resources
 * - Collections (Set, Map)
 *
 * @author Jacob
 * @version 1.0
 */
public final class Player implements Comparable<Player>
{
    private static final String PLAYERS_DIRECTORY = "players";
    private static final String FILE_EXTENSION = ".txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int LETTERS_PER_TIER_COUNT = 5;
    private static final int TIER_UNLOCK_REQUIRED_MASTERED_COUNT = 4;
    private static final double MASTERY_THRESHOLD_PERCENT = 80.0;
    private static final char FIRST_LETTER = 'A';
    private static final int FIRST_TIER_START_OFFSET = 0;
    private static final double ZERO_MASTERY = 0.0;
    private static final int ZERO_COUNT = 0;

    private final String name;
    private final LocalDateTime creationDate;
    private final Set<Character> unlockedLetters;
    private final Map<Character, Double> letterMasteryMap;
    private int totalTestsTaken;
    private int totalCorrectAnswers;

    /**
     * Constructs a new Player with specified name and creation date.
     * Initializes the player with tier 1 (letters A-E) unlocked by default.
     *
     * @param name the player's name (must not be null or empty)
     * @param creationDate the date and time when the player was created (must not be null)
     * @throws IllegalArgumentException if name is null/empty or creationDate is null
     */
    public Player(final String name,
                 final LocalDateTime creationDate)
    {
        if(name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if(creationDate == null)
        {
            throw new IllegalArgumentException("Creation date cannot be null");
        }

        this.name = name;
        this.creationDate = creationDate;
        this.unlockedLetters = new HashSet<>();
        this.letterMasteryMap = new HashMap<>();
        this.totalTestsTaken = ZERO_COUNT;
        this.totalCorrectAnswers = ZERO_COUNT;

        unlockFirstTier();
    }

    /**
     * Unlocks the first tier of letters (A-E) for the player.
     * Called automatically during player construction.
     */
    private void unlockFirstTier()
    {
        for(int i = FIRST_TIER_START_OFFSET; i < LETTERS_PER_TIER_COUNT; i++)
        {
            final char letter;
            letter = (char) (FIRST_LETTER + i);

            unlockedLetters.add(letter);
        }
    }

    /**
     * Updates the mastery percentage for a specific letter.
     * If the letter already has a mastery value, it is overwritten.
     *
     * @param letter the letter to update (must be A-Z)
     * @param masteryPercent the mastery percentage (0.0 to 100.0)
     * @throws IllegalArgumentException if letter is not A-Z or masteryPercent is out of range
     */
    public void updateLetterMastery(final char letter,
                                   final double masteryPercent)
    {
        if(letter < 'A' || letter > 'Z')
        {
            throw new IllegalArgumentException("Letter must be between A and Z");
        }

        if(masteryPercent < ZERO_MASTERY || masteryPercent > 100.0)
        {
            throw new IllegalArgumentException("Mastery percent must be between 0.0 and 100.0");
        }

        letterMasteryMap.put(letter, masteryPercent);
    }

    /**
     * Returns the mastery percentage for a specific letter.
     * Returns 0.0 if the letter has no mastery data.
     *
     * @param letter the letter to query (must be A-Z)
     * @return the mastery percentage (0.0 to 100.0)
     * @throws IllegalArgumentException if letter is not A-Z
     */
    public double getLetterMastery(final char letter)
    {
        if(letter < 'A' || letter > 'Z')
        {
            throw new IllegalArgumentException("Letter must be between A and Z");
        }

        final Double mastery;
        mastery = letterMasteryMap.get(letter);

        if(mastery == null)
        {
            return ZERO_MASTERY;
        }

        return mastery;
    }

    /**
     * Calculates and returns the total mastery percentage across all practiced letters.
     * Total mastery is the average mastery of all letters with mastery data.
     * Returns 0.0 if no letters have been practiced.
     *
     * @return the average mastery percentage across all practiced letters
     */
    public double getTotalMasteryPercent()
    {
        if(letterMasteryMap.isEmpty())
        {
            return ZERO_MASTERY;
        }

        double sum;
        sum = ZERO_MASTERY;

        for(final Double mastery : letterMasteryMap.values())
        {
            sum += mastery;
        }

        final double averageMastery;
        averageMastery = sum / letterMasteryMap.size();

        return averageMastery;
    }

    /**
     * Checks if the player can unlock the next tier of letters.
     * Returns true if at least 4 out of 5 letters in the current tier
     * have mastery >= 80%.
     *
     * @return true if next tier can be unlocked, false otherwise
     */
    public boolean canUnlockNextTier()
    {
        final int currentTierNumber;
        currentTierNumber = getCurrentTier();

        final int startIndex;
        startIndex = (currentTierNumber - 1) * LETTERS_PER_TIER_COUNT;

        int masteredCount;
        masteredCount = ZERO_COUNT;

        for(int i = startIndex; i < startIndex + LETTERS_PER_TIER_COUNT; i++)
        {
            final char letter;
            letter = (char) (FIRST_LETTER + i);

            final double mastery;
            mastery = getLetterMastery(letter);

            if(mastery >= MASTERY_THRESHOLD_PERCENT)
            {
                masteredCount++;
            }
        }

        return masteredCount >= TIER_UNLOCK_REQUIRED_MASTERED_COUNT;
    }

    /**
     * Unlocks the next tier of letters if unlocking conditions are met.
     * Adds 5 new letters to the unlocked set.
     *
     * @throws IllegalStateException if next tier cannot be unlocked or no more tiers available
     */
    public void unlockNextTier()
    {
        if(!canUnlockNextTier())
        {
            throw new IllegalStateException("Cannot unlock next tier - insufficient mastery");
        }

        final int currentTier;
        currentTier = getCurrentTier();

        final int nextTierStartIndex;
        nextTierStartIndex = currentTier * LETTERS_PER_TIER_COUNT;

        for(int i = nextTierStartIndex; i < nextTierStartIndex + LETTERS_PER_TIER_COUNT; i++)
        {
            if(i >= 26)
            {
                throw new IllegalStateException("No more tiers to unlock");
            }

            final char letter;
            letter = (char) (FIRST_LETTER + i);

            unlockedLetters.add(letter);
        }
    }

    /**
     * Returns the current tier number based on unlocked letters count.
     * Each tier contains 5 letters.
     *
     * @return the current tier number (1-5)
     */
    private int getCurrentTier()
    {
        final int tierNumber;
        tierNumber = (unlockedLetters.size() - 1) / LETTERS_PER_TIER_COUNT + 1;

        return tierNumber;
    }

    /**
     * Returns a defensive copy of the unlocked letters set.
     *
     * @return set of unlocked letters
     */
    public Set<Character> getUnlockedLetters()
    {
        final Set<Character> copy;
        copy = new HashSet<>(unlockedLetters);

        return copy;
    }

    /**
     * Records test completion and updates statistics.
     *
     * @param correctAnswers the number of correct answers in the test
     * @throws IllegalArgumentException if correctAnswers is negative
     */
    public void recordTestCompletion(final int correctAnswers)
    {
        if(correctAnswers < ZERO_COUNT)
        {
            throw new IllegalArgumentException("Correct answers cannot be negative");
        }

        totalTestsTaken++;
        totalCorrectAnswers += correctAnswers;
    }

    /**
     * Returns player statistics as a Statistics object.
     *
     * @return Statistics object containing player metrics
     */
    public Statistics getStatistics()
    {
        final Statistics stats;
        stats = new Statistics(totalTestsTaken, totalCorrectAnswers, getTotalMasteryPercent());

        return stats;
    }

    /**
     * Compares this player to another player based on total mastery percentage.
     * Higher mastery ranks higher (returns negative when this > other).
     * Implements descending order sorting.
     *
     * @param other the other player to compare to (must not be null)
     * @return negative if this player ranks higher, positive if lower, 0 if equal
     * @throws NullPointerException if other is null
     */
    @Override
    public int compareTo(final Player other)
    {
        if(other == null)
        {
            throw new NullPointerException("Cannot compare to null player");
        }

        final double thisMastery;
        final double otherMastery;

        thisMastery = this.getTotalMasteryPercent();
        otherMastery = other.getTotalMasteryPercent();

        // Descending order: higher mastery ranks higher
        return Double.compare(otherMastery, thisMastery);
    }

    /**
     * Saves this player's data to a file in the players directory.
     * File format:
     * - Line 1: PlayerName: [name]
     * - Line 2: CreationDate: [yyyy-MM-dd HH:mm:ss]
     * - Line 3: UnlockedLetters: [comma-separated letters]
     * - Line 4: LetterMastery: [letter:percent,letter:percent,...]
     * - Line 5: TotalTests: [count]
     * - Line 6: TotalCorrect: [count]
     *
     * @throws IOException if file cannot be written
     */
    public void save() throws IOException
    {
        final Path playersDir;
        playersDir = Paths.get(PLAYERS_DIRECTORY);

        if(!Files.exists(playersDir))
        {
            Files.createDirectories(playersDir);
        }

        final String filename;
        filename = PLAYERS_DIRECTORY + "/" + name + FILE_EXTENSION;

        try(final BufferedWriter writer = new BufferedWriter(new FileWriter(filename)))
        {
            writer.write("PlayerName: " + name);
            writer.newLine();

            writer.write("CreationDate: " + creationDate.format(DATE_TIME_FORMATTER));
            writer.newLine();

            writer.write("UnlockedLetters: " + formatUnlockedLetters());
            writer.newLine();

            writer.write("LetterMastery: " + formatLetterMastery());
            writer.newLine();

            writer.write("TotalTests: " + totalTestsTaken);
            writer.newLine();

            writer.write("TotalCorrect: " + totalCorrectAnswers);
            writer.newLine();
        }
    }

    /**
     * Formats unlocked letters as comma-separated string.
     *
     * @return comma-separated string of unlocked letters
     */
    private String formatUnlockedLetters()
    {
        final StringBuilder builder;
        builder = new StringBuilder();

        boolean first;
        first = true;

        for(final char letter : unlockedLetters)
        {
            if(!first)
            {
                builder.append(",");
            }
            builder.append(letter);
            first = false;
        }

        return builder.toString();
    }

    /**
     * Formats letter mastery data as comma-separated letter:percent pairs.
     *
     * @return formatted mastery string
     */
    private String formatLetterMastery()
    {
        final StringBuilder builder;
        builder = new StringBuilder();

        boolean first;
        first = true;

        for(final Map.Entry<Character, Double> entry : letterMasteryMap.entrySet())
        {
            if(!first)
            {
                builder.append(",");
            }

            builder.append(entry.getKey());
            builder.append(":");
            builder.append(entry.getValue());

            first = false;
        }

        return builder.toString();
    }

    /**
     * Loads a player from a file.
     *
     * @param playerName the name of the player to load
     * @return the loaded Player object
     * @throws IOException if file cannot be read
     * @throws IllegalArgumentException if file format is invalid
     */
    public static Player load(final String playerName) throws IOException
    {
        final String filename;
        filename = PLAYERS_DIRECTORY + "/" + playerName + FILE_EXTENSION;

        try(final BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            final String nameLine;
            final String dateLine;
            final String unlockedLine;
            final String masteryLine;
            final String testsLine;
            final String correctLine;

            nameLine = reader.readLine();
            dateLine = reader.readLine();
            unlockedLine = reader.readLine();
            masteryLine = reader.readLine();
            testsLine = reader.readLine();
            correctLine = reader.readLine();

            if(nameLine == null || dateLine == null || unlockedLine == null ||
               masteryLine == null || testsLine == null || correctLine == null)
            {
                throw new IOException("Invalid player file format");
            }

            final String name;
            final LocalDateTime creationDate;

            name = nameLine.split(": ")[1];
            creationDate = LocalDateTime.parse(dateLine.split(": ")[1], DATE_TIME_FORMATTER);

            final Player player;
            player = new Player(name, creationDate);

            player.unlockedLetters.clear();
            parseUnlockedLetters(player, unlockedLine.split(": ")[1]);

            parseLetterMastery(player, masteryLine.split(": ")[1]);

            player.totalTestsTaken = Integer.parseInt(testsLine.split(": ")[1]);
            player.totalCorrectAnswers = Integer.parseInt(correctLine.split(": ")[1]);

            return player;
        }
    }

    /**
     * Parses unlocked letters from file data.
     *
     * @param player the player to update
     * @param data the comma-separated letters string
     */
    private static void parseUnlockedLetters(final Player player,
                                            final String data)
    {
        if(data.isEmpty())
        {
            return;
        }

        final String[] letters;
        letters = data.split(",");

        for(final String letterStr : letters)
        {
            final char letter;
            letter = letterStr.charAt(0);

            player.unlockedLetters.add(letter);
        }
    }

    /**
     * Parses letter mastery data from file.
     *
     * @param player the player to update
     * @param data the comma-separated letter:percent pairs
     */
    private static void parseLetterMastery(final Player player,
                                          final String data)
    {
        if(data.isEmpty())
        {
            return;
        }

        final String[] pairs;
        pairs = data.split(",");

        for(final String pair : pairs)
        {
            final String[] parts;
            parts = pair.split(":");

            final char letter;
            final double percent;

            letter = parts[0].charAt(0);
            percent = Double.parseDouble(parts[1]);

            player.letterMasteryMap.put(letter, percent);
        }
    }

    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Nested class that encapsulates player statistics.
     * Provides aggregated metrics about player performance.
     *
     * <p>This inner class demonstrates:
     * - Nested class for encapsulating related data
     * - Immutability (all fields final)
     * - Single responsibility (statistics only)
     */
    public static final class Statistics
    {
        private final int totalTestsTaken;
        private final int totalCorrectAnswers;
        private final double averageMasteryPercent;

        /**
         * Constructs a Statistics object with specified values.
         *
         * @param totalTestsTaken total number of tests taken
         * @param totalCorrectAnswers total correct answers across all tests
         * @param averageMasteryPercent average mastery percentage
         */
        private Statistics(final int totalTestsTaken,
                          final int totalCorrectAnswers,
                          final double averageMasteryPercent)
        {
            this.totalTestsTaken = totalTestsTaken;
            this.totalCorrectAnswers = totalCorrectAnswers;
            this.averageMasteryPercent = averageMasteryPercent;
        }

        /**
         * Returns the total number of tests taken.
         *
         * @return tests taken count
         */
        public int getTotalTestsTaken()
        {
            return totalTestsTaken;
        }

        /**
         * Returns the total correct answers across all tests.
         *
         * @return total correct answers
         */
        public int getTotalCorrectAnswers()
        {
            return totalCorrectAnswers;
        }

        /**
         * Returns the average mastery percentage.
         *
         * @return average mastery percent
         */
        public double getAverageMasteryPercent()
        {
            return averageMasteryPercent;
        }

        /**
         * Formats statistics as a readable string.
         *
         * @return formatted statistics string
         */
        @Override
        public String toString()
        {
            return String.format("Tests: %d, Correct: %d, Avg Mastery: %.1f%%",
                               totalTestsTaken,
                               totalCorrectAnswers,
                               averageMasteryPercent);
        }
    }
}
