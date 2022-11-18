package main;

public final class GameStatistics {
    private static int gamesPlayed;
    private static int playerOneWins;
    private static int playerTwoWins;

    private GameStatistics() {

    }
    public static int getGamesPlayed() {
        return gamesPlayed;
    }

    public static int getPlayerOneWins() {
        return playerOneWins;
    }

    public static int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public static void setGamesPlayed(final int gamesPlayed) {
        GameStatistics.gamesPlayed = gamesPlayed;
    }

    public static void setPlayerOneWins(final int playerOneWins) {
        GameStatistics.playerOneWins = playerOneWins;
    }

    public static void setPlayerTwoWins(final int playerTwoWins) {
        GameStatistics.playerTwoWins = playerTwoWins;
    }
}
