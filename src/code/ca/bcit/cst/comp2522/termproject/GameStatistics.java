package ca.bcit.cst.comp2522.termproject;

/**
 * Tracks game statistics for NumberGame across multiple play sessions.
 * Records wins, losses, total placements, and calculates average successful
 * placements per game. Provides formatted output matching PDF specification.
 *
 * @author Jacob
 * @version 2025-11-30
 */
public final class GameStatistics
{
    private int totalGamesPlayed;
    private int totalWins;
    private int totalLosses;
    private int totalSuccessfulPlacements;

    /**
     * Constructs a new GameStatistics tracker.
     * Initializes all counters to zero.
     */
    public GameStatistics()
    {
        this.totalGamesPlayed      = 0;
        this.totalWins             = 0;
        this.totalLosses           = 0;
        this.totalSuccessfulPlacements = 0;
    }

    /**
     * Records a game win with the number of successful placements.
     * Increments wins counter, games played counter, and adds placements to total.
     *
     * @param successfulPlacements number of placements made before winning (should be 20)
     */
    public void recordWin(final int successfulPlacements)
    {
        totalWins++;
        totalGamesPlayed++;
        totalSuccessfulPlacements += successfulPlacements;
    }

    /**
     * Records a game loss with the number of successful placements before losing.
     * Increments losses counter, games played counter, and adds placements to total.
     *
     * @param successfulPlacements number of placements made before losing
     */
    public void recordLoss(final int successfulPlacements)
    {
        totalLosses++;
        totalGamesPlayed++;
        totalSuccessfulPlacements += successfulPlacements;
    }

    /**
     * Calculates the average number of successful placements per game.
     * Returns 0.0 if no games have been played.
     *
     * @return average placements per game as a double
     */
    public double getAveragePlacements()
    {
        if (totalGamesPlayed == 0)
        {
            return 0.0;
        }

        final double average;
        average = (double) totalSuccessfulPlacements / totalGamesPlayed;

        return average;
    }

    /**
     * Returns the total number of games played (wins + losses).
     *
     * @return total games played count
     */
    public int getTotalGamesPlayed()
    {
        return totalGamesPlayed;
    }

    /**
     * Returns the total number of wins.
     *
     * @return wins count
     */
    public int getTotalWins()
    {
        return totalWins;
    }

    /**
     * Returns the total number of losses.
     *
     * @return losses count
     */
    public int getTotalLosses()
    {
        return totalLosses;
    }

    /**
     * Generates a formatted statistics message matching PDF specification.
     * Examples:
     * - "You lost 1 out of 1 game, with 12 successful placements, an average of 12 per game"
     * - "You won 1 out of 2 games and you lost 1 out of 2 games, with 24 successful placements, an average of 12 per game"
     *
     * @return formatted statistics message as String
     */
    public String getStatisticsMessage()
    {
        if (totalGamesPlayed == 0)
        {
            return "No games played yet.";
        }

        final StringBuilder message;
        final String gamesWord;
        final double average;

        message = new StringBuilder();
        gamesWord = (totalGamesPlayed == 1) ? "game" : "games";
        average = getAveragePlacements();

        // Build message based on win/loss counts
        if (totalWins > 0 && totalLosses > 0)
        {
            // Both wins and losses
            message.append("You won ").append(totalWins).append(" out of ")
                   .append(totalGamesPlayed).append(" ").append(gamesWord);
            message.append(" and you lost ").append(totalLosses).append(" out of ")
                   .append(totalGamesPlayed).append(" ").append(gamesWord);
        }
        else if (totalWins > 0)
        {
            // Only wins
            message.append("You won ").append(totalWins).append(" out of ")
                   .append(totalGamesPlayed).append(" ").append(gamesWord);
        }
        else
        {
            // Only losses
            message.append("You lost ").append(totalLosses).append(" out of ")
                   .append(totalGamesPlayed).append(" ").append(gamesWord);
        }

        message.append(", with ").append(totalSuccessfulPlacements)
               .append(" successful placements, an average of ");
        message.append(String.format("%.2f", average)).append(" per game");

        return message.toString();
    }
}
