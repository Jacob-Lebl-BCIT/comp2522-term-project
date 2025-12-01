package ca.bcit.cst.comp2522.termproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player class.
 * Tests Comparable implementation, tier unlocking logic, and mastery tracking.
 *
 * @author Jacob
 * @version 1.0
 */
final class PlayerTest
{
    private static final String PLAYER_NAME = "TestPlayer";
    private static final char LETTER_A = 'A';
    private static final char LETTER_B = 'B';
    private static final char LETTER_C = 'C';
    private static final char LETTER_D = 'D';
    private static final char LETTER_E = 'E';
    private static final double MASTERY_BELOW_THRESHOLD = 75.0;
    private static final double MASTERY_AT_THRESHOLD = 80.0;
    private static final double MASTERY_ABOVE_THRESHOLD = 85.0;
    private static final double HIGH_MASTERY = 90.0;
    private static final double DELTA = 0.01;
    private static final int ZERO_VALUE = 0;
    private static final int EXPECTED_GREATER_THAN = 1;
    private static final int EXPECTED_LESS_THAN = -1;

    private Player player;

    /**
     * Sets up test fixtures before each test method.
     * Creates a fresh Player instance for testing.
     */
    @BeforeEach
    void setUp()
    {
        player = new Player(PLAYER_NAME, LocalDateTime.now());
    }

    /**
     * Tests that compareTo ranks players by total mastery percentage in descending order.
     * Higher mastery should rank higher (compareTo returns negative).
     */
    @Test
    void testCompareToRanksByMastery()
    {
        final Player playerHighMastery;
        final Player playerLowMastery;
        final Player playerEqualMastery;

        playerHighMastery = new Player("HighPlayer", LocalDateTime.now());
        playerLowMastery = new Player("LowPlayer", LocalDateTime.now());
        playerEqualMastery = new Player("EqualPlayer", LocalDateTime.now());

        // Set up high mastery player (90% on A, 90% on B, 90% on C) = 90% average
        playerHighMastery.updateLetterMastery(LETTER_A, HIGH_MASTERY);
        playerHighMastery.updateLetterMastery(LETTER_B, HIGH_MASTERY);
        playerHighMastery.updateLetterMastery(LETTER_C, HIGH_MASTERY);

        // Set up low mastery player (75% on A only) = 75% average
        playerLowMastery.updateLetterMastery(LETTER_A, MASTERY_BELOW_THRESHOLD);

        // Set up equal mastery player (90% on A, 90% on B, 90% on C) = 90% average
        playerEqualMastery.updateLetterMastery(LETTER_A, HIGH_MASTERY);
        playerEqualMastery.updateLetterMastery(LETTER_B, HIGH_MASTERY);
        playerEqualMastery.updateLetterMastery(LETTER_C, HIGH_MASTERY);

        // High mastery player should rank higher than low mastery player (negative compareTo)
        final int highVsLowComparison;
        highVsLowComparison = playerHighMastery.compareTo(playerLowMastery);

        assertTrue(highVsLowComparison < ZERO_VALUE,
                  "Player with higher mastery should rank higher (compareTo < 0)");

        // Low mastery player should rank lower than high mastery player (positive compareTo)
        final int lowVsHighComparison;
        lowVsHighComparison = playerLowMastery.compareTo(playerHighMastery);

        assertTrue(lowVsHighComparison > ZERO_VALUE,
                  "Player with lower mastery should rank lower (compareTo > 0)");

        // Equal mastery players should have compareTo == 0
        final int equalComparison;
        equalComparison = playerHighMastery.compareTo(playerEqualMastery);

        assertEquals(ZERO_VALUE,
                    equalComparison,
                    "Players with equal mastery should have compareTo == 0");
    }

    /**
     * Tests that canUnlockNextTier returns true only when 4 out of 5 letters
     * in the current tier have mastery >= 80%.
     */
    @Test
    void testCanUnlockNextTierRequiresFourMastered()
    {
        // Initially, should not be able to unlock (no mastery data)
        assertFalse(player.canUnlockNextTier(),
                   "Should not unlock next tier with no mastery data");

        // Add 3 letters at 80%+ mastery (not enough - need 4)
        player.updateLetterMastery(LETTER_A, MASTERY_AT_THRESHOLD);
        player.updateLetterMastery(LETTER_B, MASTERY_ABOVE_THRESHOLD);
        player.updateLetterMastery(LETTER_C, HIGH_MASTERY);

        assertFalse(player.canUnlockNextTier(),
                   "Should not unlock with only 3 letters at 80%+");

        // Add 4th letter at 80%+ mastery (should unlock now)
        player.updateLetterMastery(LETTER_D, MASTERY_AT_THRESHOLD);

        assertTrue(player.canUnlockNextTier(),
                  "Should unlock when 4 letters reach 80%+ mastery");

        // Verify still true with 5th letter below threshold
        player.updateLetterMastery(LETTER_E, MASTERY_BELOW_THRESHOLD);

        assertTrue(player.canUnlockNextTier(),
                  "Should still unlock with 4 at 80%+ and 1 below");

        // Test edge case: exactly 80% should count as mastered
        final Player edgeCasePlayer;
        edgeCasePlayer = new Player("EdgePlayer", LocalDateTime.now());

        edgeCasePlayer.updateLetterMastery(LETTER_A, MASTERY_AT_THRESHOLD);
        edgeCasePlayer.updateLetterMastery(LETTER_B, MASTERY_AT_THRESHOLD);
        edgeCasePlayer.updateLetterMastery(LETTER_C, MASTERY_AT_THRESHOLD);
        edgeCasePlayer.updateLetterMastery(LETTER_D, MASTERY_AT_THRESHOLD);

        assertTrue(edgeCasePlayer.canUnlockNextTier(),
                  "Exactly 80% should count as mastered");
    }

    /**
     * Tests that updateLetterMastery correctly updates and retrieves mastery percentages.
     * Verifies that mastery data is stored and retrieved accurately.
     */
    @Test
    void testUpdateLetterMasteryUpdatesCorrectly()
    {
        // Update mastery for letter A
        player.updateLetterMastery(LETTER_A, MASTERY_ABOVE_THRESHOLD);

        final double retrievedMasteryA;
        retrievedMasteryA = player.getLetterMastery(LETTER_A);

        assertEquals(MASTERY_ABOVE_THRESHOLD,
                    retrievedMasteryA,
                    DELTA,
                    "Should retrieve same mastery value that was set");

        // Update multiple letters
        player.updateLetterMastery(LETTER_B, HIGH_MASTERY);
        player.updateLetterMastery(LETTER_C, MASTERY_BELOW_THRESHOLD);

        final double retrievedMasteryB;
        final double retrievedMasteryC;

        retrievedMasteryB = player.getLetterMastery(LETTER_B);
        retrievedMasteryC = player.getLetterMastery(LETTER_C);

        assertEquals(HIGH_MASTERY,
                    retrievedMasteryB,
                    DELTA,
                    "Letter B mastery should be " + HIGH_MASTERY);

        assertEquals(MASTERY_BELOW_THRESHOLD,
                    retrievedMasteryC,
                    DELTA,
                    "Letter C mastery should be " + MASTERY_BELOW_THRESHOLD);

        // Update same letter again (should overwrite)
        player.updateLetterMastery(LETTER_A, HIGH_MASTERY);

        final double updatedMasteryA;
        updatedMasteryA = player.getLetterMastery(LETTER_A);

        assertEquals(HIGH_MASTERY,
                    updatedMasteryA,
                    DELTA,
                    "Updated mastery should overwrite previous value");

        // Test total mastery percent calculation
        final double totalMastery;
        totalMastery = player.getTotalMasteryPercent();

        // Total should be average of A, B, C = (90 + 90 + 75) / 3 = 85
        final double expectedAverage = (HIGH_MASTERY + HIGH_MASTERY + MASTERY_BELOW_THRESHOLD) / 3.0;

        assertEquals(expectedAverage,
                    totalMastery,
                    DELTA,
                    "Total mastery should be average of all letter masteries");
    }
}
