package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        GameStatistics.setPlayerTwoWins(0);
        GameStatistics.setPlayerOneWins(0);
        GameStatistics.setGamesPlayed(0);

        for (GameInput actualGame : inputData.getGames()) {
            GameStatistics.setGamesPlayed(GameStatistics.getGamesPlayed() + 1);

            Player player1 = new Player();
            Player player2 = new Player();

            Table table = new Table();

            Coordinates coordinates = new Coordinates();
            StartGameInput startGameInput = actualGame.getStartGame();

            int startingPlayer = startGameInput.getStartingPlayer();
            if (startingPlayer == 1) {
                player1.setTurn(true);
            } else {
                player2.setTurn(true);
            }

            DecksInput player1AllDecks = inputData.getPlayerOneDecks();
            DecksInput player2AllDecks = inputData.getPlayerTwoDecks();

            int indexPlayer1Deck = startGameInput.getPlayerOneDeckIdx();
            int indexPlayer2Deck = startGameInput.getPlayerTwoDeckIdx();

            ArrayList<CardInput> player1Deck = player1AllDecks.getDecks().get(indexPlayer1Deck);
            ArrayList<CardInput> player2Deck = player2AllDecks.getDecks().get(indexPlayer2Deck);

            Deck player1ChosenDeck = new Deck();
            player1ChosenDeck.getDeck().addAll(player1Deck);

            Deck player2ChosenDeck = new Deck();
            player2ChosenDeck.getDeck().addAll(player2Deck);

            Collections.shuffle(player1ChosenDeck.getDeck(),
                    new Random(startGameInput.getShuffleSeed()));
            Collections.shuffle(player2ChosenDeck.getDeck(),
                    new Random(startGameInput.getShuffleSeed()));


            // Cast deck cards to a type (environment or minion)
            CardTypeCaster cardCaster = new CardTypeCaster();
            cardCaster.castCards(player1ChosenDeck.getDeck(), player1);
            cardCaster.castCards(player2ChosenDeck.getDeck(), player2);

            /* The first card in the deck will be drawn as soon as the first round starts
            *  so it will be put in player's hand cards
            */
            player1.getHandCards().add(player1.getDeckCards().get(0));
            player2.getHandCards().add(player2.getDeckCards().get(0));

            // remove the first card in the deck, as it was drawn from the deck and put in the hand cards
            player1.getDeckCards().remove(0);
            player2.getDeckCards().remove(0);

            CardInput player1Hero = startGameInput.getPlayerOneHero();
            CardInput player2Hero = startGameInput.getPlayerTwoHero();

            cardCaster.castHero(player1Hero, player1);
            cardCaster.castHero(player2Hero, player2);

            ArrayList<ActionsInput> actions = actualGame.getActions();
            String actualAction;

            ActionInterpretor actionInterpretor = new ActionInterpretor();

            player1.setRound(1);
            player2.setRound(1);
            player1.setMana(player1.getRound());
            player2.setMana(player2.getRound());

            int endPlayerTurnCounter = 0;

            for (ActionsInput action : actions) {
                actualAction = action.getCommand();
                if (actualAction.equals("getPlayerDeck")) {
                    if (action.getPlayerIdx() == 1) {
                        actionInterpretor.getPlayerDeck(output, player1, action);
                    } else {
                        actionInterpretor.getPlayerDeck(output, player2, action);
                    }
                } else if (actualAction.equals("getPlayerHero")) {
                    if (action.getPlayerIdx() == 1) {
                        actionInterpretor.getPlayerHero(output, player1, action);
                    } else {
                        actionInterpretor.getPlayerHero(output, player2, action);
                    }
                } else if (actualAction.equals("getPlayerTurn")) {
                    actionInterpretor.getPlayerTurn(output, player1, player2, action);
                } else if (actualAction.equals("endPlayerTurn")) {
                    endPlayerTurnCounter++;
                    actionInterpretor.endPlayerTurn(table, player1, player2, endPlayerTurnCounter);
                } else if (actualAction.equals("placeCard")) {
                    int turn = actionInterpretor.checkPlayerTurn(player1, player2);
                    if (turn != 0) {
                        if (turn == 1) {
                            actionInterpretor.placeCard(output, table, player1, action, turn);
                        } else {
                            actionInterpretor.placeCard(output, table, player2, action, turn);
                        }
                    }
                } else if (actualAction.equals("getCardsInHand")) {
                    if (action.getPlayerIdx() == 1) {
                        actionInterpretor.getCardsInHand(output, player1, action);
                    } else {
                        actionInterpretor.getCardsInHand(output, player2, action);
                    }
                } else if (actualAction.equals("getPlayerMana")) {
                    if (action.getPlayerIdx() == 1) {
                        actionInterpretor.getPlayerMana(output, player1, action);
                    } else {
                        actionInterpretor.getPlayerMana(output, player2, action);
                    }
                } else if (actualAction.equals("getCardsOnTable")) {
                    actionInterpretor.getCardsOnTable(output, table, action);
                } else if (actualAction.equals("getCardAtPosition")) {
                    coordinates.setX(action.getX());
                    coordinates.setY(action.getY());
                    actionInterpretor.getCardAtPosition(output, coordinates, table, action);
                } else if (actualAction.equals("getEnvironmentCardsInHand")) {
                    if (action.getPlayerIdx() == 1) {
                        actionInterpretor.getEnvironmentCardsInHand(output, player1, action);
                    } else {
                        actionInterpretor.getEnvironmentCardsInHand(output, player2, action);
                    }
                } else if (actualAction.equals("useEnvironmentCard")) {
                    int turn = actionInterpretor.checkPlayerTurn(player1, player2);
                    if (turn != 0) {
                        if (turn == 1) {
                            actionInterpretor.useEnvironmentCard(output, table, player1, action,
                                    turn);
                        } else {
                            actionInterpretor.useEnvironmentCard(output, table, player2, action,
                                    turn);
                        }
                    }
                } else if (actualAction.equals("getFrozenCardsOnTable")) {
                    actionInterpretor.getFrozenCardsOnTable(output, table, action);
                } else if (actualAction.equals("cardUsesAttack")) {
                    int turn = actionInterpretor.checkPlayerTurn(player1, player2);

                    Coordinates coordinatesAttacker = new Coordinates();
                    Coordinates coordinatesAttacked = new Coordinates();
                    coordinatesAttacker.setX(action.getCardAttacker().getX());
                    coordinatesAttacker.setY(action.getCardAttacker().getY());
                    coordinatesAttacked.setX(action.getCardAttacked().getX());
                    coordinatesAttacked.setY(action.getCardAttacked().getY());

                    actionInterpretor.cardUsesAttack(output, table, action, coordinatesAttacker,
                            coordinatesAttacked, turn);
                } else if (actualAction.equals("cardUsesAbility")) {
                    int turn = actionInterpretor.checkPlayerTurn(player1, player2);

                    Coordinates coordinatesAttacker = new Coordinates();
                    Coordinates coordinatesAttacked = new Coordinates();
                    coordinatesAttacker.setX(action.getCardAttacker().getX());
                    coordinatesAttacker.setY(action.getCardAttacker().getY());
                    coordinatesAttacked.setX(action.getCardAttacked().getX());
                    coordinatesAttacked.setY(action.getCardAttacked().getY());

                    actionInterpretor.cardUsesAbility(output, table, action, coordinatesAttacker,
                            coordinatesAttacked, turn);
                } else if (actualAction.equals("useAttackHero")) {
                    Coordinates coordinatesAttacker = new Coordinates();
                    coordinatesAttacker.setX(action.getCardAttacker().getX());
                    coordinatesAttacker.setY(action.getCardAttacker().getY());

                    int turn = actionInterpretor.checkPlayerTurn(player1, player2);
                    if (turn != 0) {
                        if (turn == 1) {
                            actionInterpretor.useAttackHero(output, coordinatesAttacker, action,
                                    player2, table, turn);
                        } else {
                            actionInterpretor.useAttackHero(output, coordinatesAttacker, action,
                                    player1, table, turn);
                        }
                    }
                } else if (actualAction.equals("useHeroAbility")) {
                    int turn = actionInterpretor.checkPlayerTurn(player1, player2);
                    if (turn == 1) {
                        actionInterpretor.useHeroAbility(output, action, table, turn, player1);
                    } else {
                        actionInterpretor.useHeroAbility(output, action, table, turn, player2);
                    }
                } else if (actualAction.equals("getPlayerOneWins")) {
                    output.addObject().put("command", action.getCommand())
                            .put("output", GameStatistics.getPlayerOneWins());
                } else if (actualAction.equals("getPlayerTwoWins")) {
                    output.addObject().put("command", action.getCommand())
                            .put("output", GameStatistics.getPlayerTwoWins());
                } else if (actualAction.equals("getTotalGamesPlayed")) {
                    output.addObject().put("command", action.getCommand())
                            .put("output", GameStatistics.getGamesPlayed());
                }
            }

        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);

    }

}
