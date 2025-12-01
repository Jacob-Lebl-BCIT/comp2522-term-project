package ca.bcit.cst.comp2522.termproject;

/**
 * Represents a cell in the NumberGame grid that can hold an integer value.
 * Provides contract for checking cell state, setting values, and validating
 * whether a value can be placed in the cell according to game rules.
 *
 * @author Jacob
 * @version 1.0
 */
public interface GridCell
{
    /**
     * Returns the value stored in this cell.
     * If the cell is empty, returns a sentinel value (typically -1).
     *
     * @return the integer value in this cell, or -1 if empty
     */
    int getValue();

    /**
     * Checks if this cell is empty (contains no value).
     *
     * @return true if cell is empty, false if it contains a value
     */
    boolean isEmpty();

    /**
     * Sets the value for this cell.
     * Should only be called on empty cells. Behavior is undefined
     * if called on a cell that already contains a value.
     *
     * @param value the integer value to store in this cell
     */
    void setValue(final int value);

    /**
     * Resets this cell to empty state, clearing any stored value.
     * After reset, isEmpty() should return true.
     */
    void reset();

    /**
     * Checks if this cell can accept the given value according to
     * game placement rules. This may depend on the cell's current state
     * and the value being placed.
     *
     * @param value the value to check for acceptance
     * @return true if the cell can accept this value, false otherwise
     */
    boolean canAcceptValue(final int value);
}
