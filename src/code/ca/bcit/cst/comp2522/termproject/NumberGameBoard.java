package ca.bcit.cst.comp2522.termproject;

/**
 * Concrete implementation of game board for NumberGame.
 * Enforces ascending order placement rule: numbers must be placed
 * in ascending order from left to right, top to bottom (index 0-19).
 * Validates placements to ensure all numbers before a position are smaller
 * and all numbers after are larger.
 *
 * @author Jacob
 * @version 1.0
 */
public final class NumberGameBoard extends AbstractGameBoard
{
    /**
     * Constructs a new NumberGame board with 20 cells.
     * Initializes all cells to empty state.
     */
    public NumberGameBoard()
    {
        super(BOARD_SIZE);
    }

    /**
     * Resets the board to initial empty state.
     * Clears all cell values and resets the filled cells counter to zero.
     */
    @Override
    public void resetBoard()
    {
        for (final GridCell cell : cells)
        {
            cell.reset();
        }

        filledCellsCount = 0;
    }

    /**
     * Checks if a number can be placed at the specified position
     * according to ascending order rules.
     * Validates that the cell is empty and that placement maintains
     * ascending order with surrounding filled cells.
     *
     * @param value    the number to place
     * @param position the index position in the cells array (0-19)
     * @return true if placement is valid, false otherwise
     */
    @Override
    public boolean canPlaceNumber(final int value, final int position)
    {
        if (position < 0 || position >= cells.length)
        {
            return false;
        }

        if (!cells[position].isEmpty())
        {
            return false;
        }

        return isValidPlacement(value, position);
    }

    /**
     * Places a number at the specified position on the board.
     * Updates the filled cells counter.
     * Throws exceptions if placement is invalid.
     *
     * @param value    the number to place
     * @param position the index position in the cells array (0-19)
     * @throws IllegalArgumentException if position is out of bounds
     * @throws IllegalStateException    if placement violates ascending order or cell is occupied
     */
    @Override
    public void placeNumber(final int value, final int position)
    {
        if (position < 0 || position >= cells.length)
        {
            throw new IllegalArgumentException("Position out of bounds: " + position);
        }

        if (!cells[position].isEmpty())
        {
            throw new IllegalStateException("Cell at position " + position + " is already occupied");
        }

        if (!isValidPlacement(value, position))
        {
            throw new IllegalStateException("Placement violates ascending order");
        }

        cells[position].setValue(value);
        filledCellsCount++;
    }

    /**
     * Checks if the given placement maintains ascending order.
     * Validates that:
     * - All filled cells before position have values less than the given value
     * - All filled cells after position have values greater than the given value
     * This ensures the board remains in strictly ascending order.
     *
     * @param value    the number to validate
     * @param position the position to check
     * @return true if placement maintains ascending order, false otherwise
     */
    @Override
    public boolean isValidPlacement(final int value, final int position)
    {
        // Check all positions before this one
        for (int i = 0; i < position; i++)
        {
            if (!cells[i].isEmpty())
            {
                final int cellValue;
                cellValue = cells[i].getValue();

                if (cellValue >= value)
                {
                    return false;
                }
            }
        }

        // Check all positions after this one
        for (int i = position + 1; i < cells.length; i++)
        {
            if (!cells[i].isEmpty())
            {
                final int cellValue;
                cellValue = cells[i].getValue();

                if (cellValue <= value)
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the given value can be placed anywhere on the current board.
     * Used to determine if the game is lost (no valid placements remaining).
     *
     * @param value the value to check for possible placements
     * @return true if at least one valid placement exists, false if game is lost
     */
    public boolean hasValidPlacement(final int value)
    {
        for (int i = 0; i < cells.length; i++)
        {
            if (canPlaceNumber(value, i))
            {
                return true;
            }
        }

        return false;
    }
}
