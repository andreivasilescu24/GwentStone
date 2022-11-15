package main;

public class GameStatistics {
    private static int gamesPlayed;
    private static int playerOneWins;
    private static int playerTwoWins;

    public static int getGamesPlayed() {
        return gamesPlayed;
    }

    public static int getPlayerOneWins() {
        return playerOneWins;
    }

    public static int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public static void setGamesPlayed(int gamesPlayed) {
        GameStatistics.gamesPlayed = gamesPlayed;
    }

    public static void setPlayerOneWins(int playerOneWins) {
        GameStatistics.playerOneWins = playerOneWins;
    }

    public static void setPlayerTwoWins(int playerTwoWins) {
        GameStatistics.playerTwoWins = playerTwoWins;
    }
}
