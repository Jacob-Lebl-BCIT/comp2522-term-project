package ca.bcit.cst.comp2522.termproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MasteryTracker generic class.
 * Tests tracking functionality, mastery calculations, filtering, and generic type support.
 *
 * @author Jacob
 * @version 1.0
 */
public final class MasteryTrackerTest
{
    private static final double DELTA = 0.01;
    private static final double MASTERY_THRESHOLD_PERCENT = 80.0;
    private static final char TEST_LETTER_A = 'A';
    private static final char TEST_LETTER_B = 'B';
    private static final char TEST_LETTER_C = 'C';
    private static final int ZERO_ATTEMPTS = 0;
    private static final int TWO_ATTEMPTS = 2;
    private static final int FIVE_ATTEMPTS = 5;
    private static final int TEN_ATTEMPTS = 10;
    private static final double EXPECTED_ZERO_PERCENT = 0.0;
    private static final double EXPECTED_FIFTY_PERCENT = 50.0;
    private static final double EXPECTED_SIXTY_PERCENT = 60.0;
    private static final double EXPECTED_EIGHTY_PERCENT = 80.0;
    private static final double EXPECTED_ONE_HUNDRED_PERCENT = 100.0;

    private MasteryTracker<Character> characterTracker;
    private MasteryTracker<String> stringTracker;

    /**
     * Sets up test fixtures before each test method.
     * Creates fresh MasteryTracker instances for testing.
     */
    @BeforeEach
    public void setUp()
    {
        characterTracker = new MasteryTracker<>();
        stringTracker = new MasteryTracker<>();
    }

    /**
     * Tests that recordAttempt correctly updates tracking data.
     * Verifies that correct and incorrect attempts are recorded accurately.
     */
    @Test
    public void testRecordAttemptUpdatesTracking()
    {
        // Record correct attempt
        characterTracker.recordAttempt(TEST_LETTER_A, true);

        final double masteryAfterOneCorrect;
        masteryAfterOneCorrect = characterTracker.getMasteryPercent(TEST_LETTER_A);

        assertEquals(EXPECTED_ONE_HUNDRED_PERCENT,
                    masteryAfterOneCorrect,
                    DELTA,
                    "One correct attempt should result in 100% mastery");

        // Record incorrect attempt
        characterTracker.recordAttempt(TEST_LETTER_A, false);

        final double masteryAfterOneIncorrect;
        masteryAfterOneIncorrect = characterTracker.getMasteryPercent(TEST_LETTER_A);

        assertEquals(EXPECTED_FIFTY_PERCENT,
                    masteryAfterOneIncorrect,
                    DELTA,
                    "One correct and one incorrect should result in 50% mastery");
    }

    /**
     * Tests that getMasteryPercent calculates percentages correctly.
     * Verifies calculation with various ratios of correct to total attempts.
     */
    @Test
    public void testGetMasteryPercentCalculatesCorrectly()
    {
        // Test with no attempts - should return 0%
        final double masteryNoAttempts;
        masteryNoAttempts = characterTracker.getMasteryPercent(TEST_LETTER_A);

        assertEquals(EXPECTED_ZERO_PERCENT,
                    masteryNoAttempts,
                    DELTA,
                    "No attempts should result in 0% mastery");

        // Test with 4 correct out of 5 attempts = 80%
        for(int i = 0; i < FIVE_ATTEMPTS - 1; i++)
        {
            characterTracker.recordAttempt(TEST_LETTER_B, true);
        }
        characterTracker.recordAttempt(TEST_LETTER_B, false);

        final double masteryEightyPercent;
        masteryEightyPercent = characterTracker.getMasteryPercent(TEST_LETTER_B);

        assertEquals(EXPECTED_EIGHTY_PERCENT,
                    masteryEightyPercent,
                    DELTA,
                    "4 correct out of 5 should result in 80% mastery");

        // Test with 3 correct out of 5 attempts = 60%
        for(int i = 0; i < FIVE_ATTEMPTS - TWO_ATTEMPTS; i++)
        {
            characterTracker.recordAttempt(TEST_LETTER_C, true);
        }
        for(int i = 0; i < TWO_ATTEMPTS; i++)
        {
            characterTracker.recordAttempt(TEST_LETTER_C, false);
        }

        final double masterySixtyPercent;
        masterySixtyPercent = characterTracker.getMasteryPercent(TEST_LETTER_C);

        assertEquals(EXPECTED_SIXTY_PERCENT,
                    masterySixtyPercent,
                    DELTA,
                    "3 correct out of 5 should result in 60% mastery");
    }

    /**
     * Tests that getWeakSkills correctly filters skills below threshold.
     * Verifies filtering logic and sorting by mastery percentage.
     */
    @Test
    public void testGetWeakSkillsFiltersCorrectly()
    {
        // Letter A: 100% mastery (10/10 correct)
        for(int i = 0; i < TEN_ATTEMPTS; i++)
        {
            characterTracker.recordAttempt(TEST_LETTER_A, true);
        }

        // Letter B: 60% mastery (3/5 correct)
        for(int i = 0; i < FIVE_ATTEMPTS - TWO_ATTEMPTS; i++)
        {
            characterTracker.recordAttempt(TEST_LETTER_B, true);
        }
        for(int i = 0; i < TWO_ATTEMPTS; i++)
        {
            characterTracker.recordAttempt(TEST_LETTER_B, false);
        }

        // Letter C: 50% mastery (1/2 correct)
        characterTracker.recordAttempt(TEST_LETTER_C, true);
        characterTracker.recordAttempt(TEST_LETTER_C, false);

        // Get weak skills below 80% threshold
        final List<Character> weakSkills;
        weakSkills = characterTracker.getWeakSkills(MASTERY_THRESHOLD_PERCENT);

        assertEquals(TWO_ATTEMPTS,
                    weakSkills.size(),
                    "Should return 2 weak skills (B and C) below 80%");

        assertFalse(weakSkills.contains(TEST_LETTER_A),
                   "Letter A (100%) should not be in weak skills");

        assertTrue(weakSkills.contains(TEST_LETTER_B),
                  "Letter B (60%) should be in weak skills");

        assertTrue(weakSkills.contains(TEST_LETTER_C),
                  "Letter C (50%) should be in weak skills");

        // Verify sorting (lowest mastery first)
        assertEquals(TEST_LETTER_C,
                    weakSkills.get(ZERO_ATTEMPTS),
                    "Letter C (50%) should be first (lowest mastery)");

        assertEquals(TEST_LETTER_B,
                    weakSkills.get(1),
                    "Letter B (60%) should be second");
    }

    /**
     * Tests that MasteryTracker works with multiple generic types.
     * Verifies generic functionality with Character and String types.
     */
    @Test
    public void testGenericWorksWithMultipleTypes()
    {
        final String skillOne = "Fingerspelling";
        final String skillTwo = "Receptive";
        final String skillThree = "Expressive";

        // Test with String type
        stringTracker.recordAttempt(skillOne, true);
        stringTracker.recordAttempt(skillOne, true);

        final double masterySkillOne;
        masterySkillOne = stringTracker.getMasteryPercent(skillOne);

        assertEquals(EXPECTED_ONE_HUNDRED_PERCENT,
                    masterySkillOne,
                    DELTA,
                    "String-based tracker should calculate 100% mastery correctly");

        stringTracker.recordAttempt(skillTwo, true);
        stringTracker.recordAttempt(skillTwo, false);

        final double masterySkillTwo;
        masterySkillTwo = stringTracker.getMasteryPercent(skillTwo);

        assertEquals(EXPECTED_FIFTY_PERCENT,
                    masterySkillTwo,
                    DELTA,
                    "String-based tracker should calculate 50% mastery correctly");

        // Test filtering with String type
        stringTracker.recordAttempt(skillThree, false);
        stringTracker.recordAttempt(skillThree, false);

        final List<String> weakStringSkills;
        weakStringSkills = stringTracker.getWeakSkills(MASTERY_THRESHOLD_PERCENT);

        assertEquals(TWO_ATTEMPTS,
                    weakStringSkills.size(),
                    "String-based tracker should filter weak skills correctly");

        assertTrue(weakStringSkills.contains(skillTwo),
                  "Should contain skills below 80% threshold");
    }
}
