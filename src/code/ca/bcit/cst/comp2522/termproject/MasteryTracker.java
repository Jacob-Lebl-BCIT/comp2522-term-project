package ca.bcit.cst.comp2522.termproject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generic tracker for mastering skills of any type.
 * Tracks attempts and successes for each skill, calculates mastery percentages,
 * and provides filtering capabilities to identify weak skills needing practice.
 *
 * <p>This class demonstrates:
 * - Custom generics (parameterized type T)
 * - Nested classes (SkillData inner class)
 * - Streaming and filtering operations
 * - Method references
 *
 * @param <T> the type of skill being tracked (e.g., Character for letters, String for skill names)
 * @author Jacob
 * @version 1.0
 */
public final class MasteryTracker<T>
{
    private static final int ZERO_ATTEMPTS = 0;
    private static final int PERCENT_MULTIPLIER = 100;
    private static final double DEFAULT_MASTERY_PERCENT = 0.0;

    private final Map<T, SkillData> masteryMap;

    /**
     * Constructs a new MasteryTracker with an empty skill tracking map.
     */
    public MasteryTracker()
    {
        this.masteryMap = new HashMap<>();
    }

    /**
     * Records an attempt at a specific skill.
     * Creates a new SkillData entry if this is the first attempt at the skill.
     * Updates the existing entry with the result of the attempt.
     *
     * @param skill the skill being attempted (must not be null)
     * @param correct true if the attempt was successful, false otherwise
     * @throws IllegalArgumentException if skill is null
     */
    public void recordAttempt(final T skill,
                             final boolean correct)
    {
        if(skill == null)
        {
            throw new IllegalArgumentException("Skill cannot be null");
        }

        final SkillData skillData;

        if(masteryMap.containsKey(skill))
        {
            skillData = masteryMap.get(skill);
        }
        else
        {
            skillData = new SkillData();
            masteryMap.put(skill, skillData);
        }

        skillData.recordAttempt(correct);
    }

    /**
     * Calculates and returns the mastery percentage for a specific skill.
     * Mastery percentage is the ratio of correct attempts to total attempts, multiplied by 100.
     * Returns 0.0 if the skill has never been attempted.
     *
     * @param skill the skill to check (must not be null)
     * @return the mastery percentage (0.0 to 100.0)
     * @throws IllegalArgumentException if skill is null
     */
    public double getMasteryPercent(final T skill)
    {
        if(skill == null)
        {
            throw new IllegalArgumentException("Skill cannot be null");
        }

        final SkillData skillData;
        skillData = masteryMap.get(skill);

        if(skillData == null)
        {
            return DEFAULT_MASTERY_PERCENT;
        }

        return skillData.getMasteryPercent();
    }

    /**
     * Returns a list of skills with mastery below the specified threshold.
     * The returned list is sorted by mastery percentage in ascending order (weakest first).
     * Uses stream operations to filter, sort, and collect results.
     *
     * <p>Demonstrates:
     * - Lambda expressions for filtering
     * - Method reference for mapping
     * - Stream pipeline operations
     *
     * @param thresholdPercent the mastery threshold percentage (0.0 to 100.0)
     * @return list of skills below threshold, sorted by mastery (weakest first)
     * @throws IllegalArgumentException if thresholdPercent is negative or greater than 100
     */
    public List<T> getWeakSkills(final double thresholdPercent)
    {
        if(thresholdPercent < DEFAULT_MASTERY_PERCENT || thresholdPercent > PERCENT_MULTIPLIER)
        {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 100.0");
        }

        final List<T> weakSkills;
        weakSkills = masteryMap.entrySet().stream()
                .filter(entry -> entry.getValue().getMasteryPercent() < thresholdPercent)
                .sorted(Comparator.comparingDouble(entry -> entry.getValue().getMasteryPercent()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return weakSkills;
    }

    /**
     * Returns a list of all skills sorted by mastery percentage in ascending order.
     * Uses method reference for obtaining mastery percentages during sorting.
     *
     * <p>Demonstrates method references with instance methods.
     *
     * @return list of all tracked skills sorted by mastery (weakest first)
     */
    public List<T> getAllSkillsSorted()
    {
        final List<T> sortedSkills;
        sortedSkills = masteryMap.keySet().stream()
                .sorted(Comparator.comparing(this::getMasteryPercent))
                .collect(Collectors.toList());

        return sortedSkills;
    }

    /**
     * Returns a snapshot of current mastery data as a map.
     * The returned map contains each skill mapped to its current mastery percentage.
     * Creates a new map to prevent external modification of internal state.
     *
     * @return map of skills to their mastery percentages
     */
    public Map<T, Double> getMasterySnapshot()
    {
        final Map<T, Double> snapshot;
        snapshot = new HashMap<>();

        for(final Map.Entry<T, SkillData> entry : masteryMap.entrySet())
        {
            final T skill;
            final double masteryPercent;

            skill = entry.getKey();
            masteryPercent = entry.getValue().getMasteryPercent();

            snapshot.put(skill, masteryPercent);
        }

        return snapshot;
    }

    /**
     * Inner class that encapsulates tracking data for a single skill.
     * Tracks total attempts and correct attempts to calculate mastery percentage.
     *
     * <p>This nested class demonstrates:
     * - Encapsulation of helper data
     * - Single responsibility (tracking attempts for one skill)
     * - Private inner class (implementation detail of MasteryTracker)
     */
    private static final class SkillData
    {
        private int totalAttempts;
        private int correctAttempts;

        /**
         * Constructs a new SkillData with zero attempts.
         */
        private SkillData()
        {
            this.totalAttempts = ZERO_ATTEMPTS;
            this.correctAttempts = ZERO_ATTEMPTS;
        }

        /**
         * Records an attempt and updates tracking data.
         *
         * @param correct true if the attempt was successful, false otherwise
         */
        private void recordAttempt(final boolean correct)
        {
            totalAttempts++;

            if(correct)
            {
                correctAttempts++;
            }
        }

        /**
         * Calculates and returns the mastery percentage.
         * Returns 0.0 if no attempts have been made to avoid division by zero.
         *
         * @return the mastery percentage (0.0 to 100.0)
         */
        private double getMasteryPercent()
        {
            if(totalAttempts == ZERO_ATTEMPTS)
            {
                return DEFAULT_MASTERY_PERCENT;
            }

            final double masteryPercent;
            masteryPercent = (double) correctAttempts / totalAttempts * PERCENT_MULTIPLIER;

            return masteryPercent;
        }
    }
}
