package ca.bcit.cst.comp2522.termproject;

/**
 * Abstract representation of a game board for NumberGame.
 * Manages an array of GridCells and provides template methods
 * for board operations like placement validation and reset.
 * Subclasses must implement specific game rules for number placement.
 *
 * @author Jacob
 * @version 2025-11-30
 */
public abstract class AbstractGameBoard
{
    protected static final int BOARD_SIZE = 20;

    protected final GridCell[] cells;
    protected int filledCellsCount;

    /**
     * Constructs a new game board with the specified size.
     * Initializes all cells to empty state.
     *
     * @param sizeInCells the number of cells in the board
     * @throws IllegalArgumentException if size is less than or equal to zero
     */
    public AbstractGameBoard(final int sizeInCells)
    {
        if (sizeInCells <= 0)
        {
            throw new IllegalArgumentException("Board size must be positive");
        }

        this.cells = new GridCell[sizeInCells];
        this.filledCellsCount = 0;

        for (int i = 0; i < sizeInCells; i++)
        {
            cells[i] = new SimpleGridCell();
        }
    }

    /**
     * Resets the board to initial empty state.
     * Clears all cell values and resets the filled cells counter.
     */
    public abstract void resetBoard();

    /**
     * Checks if a number can be placed at the specified position
     * according to game rules (ascending order).
     *
     * @param value    the number to place
     * @param position the index position in the cells array (0-19)
     * @return true if placement is valid, false otherwise
     */
    public abstract boolean canPlaceNumber(final int value, final int position);

    /**
     * Places a number at the specified position on the board.
     * Updates the filled cells counter.
     *
     * @param value    the number to place
     * @param position the index position in the cells array (0-19)
     * @throws IllegalArgumentException if position is out of bounds
     * @throws IllegalStateException    if placement is invalid
     */
    public abstract void placeNumber(final int value, final int position);

    /**
     * Checks if the given placement maintains ascending order.
     * Validates that all filled cells before position have values less than
     * the given value, and all filled cells after have values greater than it.
     *
     * @param value    the number to validate
     * @param position the position to check
     * @return true if placement maintains order, false otherwise
     */
    public abstract boolean isValidPlacement(final int value, final int position);

    /**
     * Returns the number of cells that have been filled with values.
     *
     * @return count of filled cells
     */
    public int getFilledCellsCount()
    {
        return filledCellsCount;
    }

    /**
     * Returns the total number of cells on the board.
     *
     * @return board size (number of cells)
     */
    public int getBoardSize()
    {
        return cells.length;
    }

    /**
     * Checks if the board is completely filled (all cells occupied).
     *
     * @return true if all cells filled, false otherwise
     */
    public boolean isBoardFull()
    {
        return filledCellsCount == cells.length;
    }

    /**
     * Returns the value at the specified cell position.
     * Returns -1 if the cell is empty.
     *
     * @param position the index position in the cells array
     * @return the value at that position, or -1 if empty
     * @throws ArrayIndexOutOfBoundsException if position is invalid
     */
    public int getValueAt(final int position)
    {
        return cells[position].getValue();
    }
}
