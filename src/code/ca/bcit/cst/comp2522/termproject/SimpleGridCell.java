package ca.bcit.cst.comp2522.termproject;

/**
 * Simple implementation of GridCell that stores an integer value or empty state.
 * Uses -1 as sentinel value to represent empty cells. Once a value is set,
 * the cell is considered occupied and cannot be changed (immutable after first set).
 *
 * @author Jacob
 * @version 2025-11-30
 */
public final class SimpleGridCell implements GridCell
{
    private static final int EMPTY_CELL_VALUE = -1;

    private int value;

    /**
     * Constructs a new empty GridCell.
     * Initial state is empty with value set to -1.
     */
    public SimpleGridCell()
    {
        this.value = EMPTY_CELL_VALUE;
    }

    /**
     * Returns the value stored in this cell.
     * Returns -1 if the cell is empty.
     *
     * @return the integer value in this cell, or -1 if empty
     */
    @Override
    public int getValue()
    {
        return value;
    }

    /**
     * Checks if this cell is empty (contains no value).
     *
     * @return true if cell is empty, false if it contains a value
     */
    @Override
    public boolean isEmpty()
    {
        return value == EMPTY_CELL_VALUE;
    }

    /**
     * Sets the value for this cell.
     * Should only be called on empty cells. Once set, the cell
     * is considered immutable (value cannot be changed).
     *
     * @param value the integer value to store in this cell
     * @throws IllegalStateException if cell already contains a value
     */
    @Override
    public void setValue(final int value)
    {
        if (!isEmpty())
        {
            throw new IllegalStateException("Cannot set value on non-empty cell");
        }

        this.value = value;
    }

    /**
     * Resets this cell to empty state, clearing any stored value.
     * After reset, isEmpty() returns true and getValue() returns -1.
     */
    @Override
    public void reset()
    {
        this.value = EMPTY_CELL_VALUE;
    }

    /**
     * Checks if this cell can accept the given value.
     * Cell can accept value only if it is currently empty.
     *
     * @param value the value to check for acceptance
     * @return true if the cell is empty, false if occupied
     */
    @Override
    public boolean canAcceptValue(final int value)
    {
        return isEmpty();
    }
}
