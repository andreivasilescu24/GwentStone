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

        Player player1 = new Player();
        Player player2 = new Player();

        Table table = new Table();

        ActionsInput actionsInput;
        CardInput cardInput;
        Coordinates coordinates = new Coordinates();
        DecksInput decksInput;
        GameInput gameInput;
        Input input;
        StartGameInput startGameInput;

        DecksInput player1_all_decks, player2_all_decks;

        // TODO: Change indexing as 0 and make some general rule for "get" method
        startGameInput = inputData.getGames().get(0).getStartGame();

        int startingPlayer = startGameInput.getStartingPlayer();
        if(startingPlayer == 1)
            player1.setTurn(true);
        else player2.setTurn(true);

        // obtin toate deck-urile
        player1_all_decks = inputData.getPlayerOneDecks();
        player2_all_decks = inputData.getPlayerTwoDecks();

        // obtin index-urile corespunzatoare deck-urilor celor 2 jucatori
        int index_player1_deck = startGameInput.getPlayerOneDeckIdx();
        int index_player2_deck = startGameInput.getPlayerTwoDeckIdx();

        // obtin deck-ul de la index
        ArrayList<CardInput> player1_deck = player1_all_decks.getDecks().get(index_player1_deck);
        ArrayList<CardInput> player2_deck = player2_all_decks.getDecks().get(index_player2_deck);

        // amestec cartile din deck-uri
        Collections.shuffle(player1_deck, new Random(startGameInput.getShuffleSeed()));
        Collections.shuffle(player2_deck, new Random(startGameInput.getShuffleSeed()));

        // Cast deck cards
        CardTypeCaster card_caster = new CardTypeCaster();
        card_caster.cast_cards(player1_deck, player1);
        card_caster.cast_cards(player2_deck, player2);

        // introducem in array-ul de carti din mana, prima carte trasa din deck
        player1.getHandCards().add(player1.getDeckCards().get(0));
        player2.getHandCards().add(player2.getDeckCards().get(0));

        // elimin din deck prima carte trasa
        player1.getDeckCards().remove(0);
        player2.getDeckCards().remove(0);

        // obtin cei doi eroi corespunzatori fiecarui jucator
        CardInput player1_hero = startGameInput.getPlayerOneHero();
        CardInput player2_hero = startGameInput.getPlayerTwoHero();

        card_caster.cast_hero(player1_hero, player1);
        card_caster.cast_hero(player2_hero, player2);


        //obtin actiunile
        ArrayList<ActionsInput> actions = inputData.getGames().get(0).getActions();
        String actual_action;

        ActionInterpretor actionInterpretor = new ActionInterpretor();

//        for(DeckCard test_card : player1.getDeckCards())
//            System.out.println(test_card.getName() + " " + test_card.getMana());
//        System.out.println("*************");
//        for(DeckCard test_card : player2.getDeckCards())
//            System.out.println(test_card.getName() + " " + test_card.getMana());
//        System.out.println("**********************");
//        System.out.println(player2.getDeckCards());
//        System.out.println(player2.getHandCards());
//

        player1.setRound(1);
        player2.setRound(1);
        player1.setMana(player1.getRound());
        player2.setMana(player2.getRound());

        int end_player_turn_counter = 0;

        for(ActionsInput action : actions) {
            actual_action = action.getCommand();
            if(actual_action.equals("getPlayerDeck")) {
                if (action.getPlayerIdx() == 1)
                    actionInterpretor.getPlayerDeck(output, player1, action);
                else
                    actionInterpretor.getPlayerDeck(output, player2, action);
                }

            else if(actual_action.equals("getPlayerHero")) {
                if (action.getPlayerIdx() == 1)
                    actionInterpretor.getPlayerHero(output, player1, action);
                else
                    actionInterpretor.getPlayerHero(output, player2, action);
            }

            else if(actual_action.equals("getPlayerTurn"))
                actionInterpretor.getPlayerTurn(output, player1, player2, action);

            else if(actual_action.equals("endPlayerTurn")) {
                end_player_turn_counter++;
                actionInterpretor.endPlayerTurn(table, player1, player2, end_player_turn_counter);
            }

            else if(actual_action.equals("placeCard")) {
                int turn = actionInterpretor.checkPlayerTurn(player1, player2);
                if(turn != 0)
                    if(turn == 1)
                        actionInterpretor.placeCard(output, table, player1, action, turn);
                    else actionInterpretor.placeCard(output, table, player2, action, turn);
            }

            else if(actual_action.equals("getCardsInHand")) {
                if(action.getPlayerIdx() == 1)
                    actionInterpretor.getCardsInHand(output, player1, action);
                else actionInterpretor.getCardsInHand(output, player2, action);
            }

            else if(actual_action.equals("getPlayerMana")) {
                if(action.getPlayerIdx() == 1)
                    actionInterpretor.getPlayerMana(output, player1, action);
                else actionInterpretor.getPlayerMana(output, player2, action);
            }

            else if(actual_action.equals("getCardsOnTable")) {
                actionInterpretor.getCardsOnTable(output, table, action);
            }

            else if(actual_action.equals("getCardAtPosition")) {
                coordinates.setX(action.getX());
                coordinates.setY(action.getY());
                actionInterpretor.getCardAtPosition(output, coordinates, table, action);
            }

            else if(actual_action.equals("getEnvironmentCardsInHand")) {
                if(action.getPlayerIdx() == 1)
                    actionInterpretor.getEnvironmentCardsInHand(output, player1, action);
                else actionInterpretor.getEnvironmentCardsInHand(output, player2, action);
            }

            else if(actual_action.equals("useEnvironmentCard")) {
                int turn = actionInterpretor.checkPlayerTurn(player1, player2);
                if(turn != 0)
                    if(turn == 1)
                        actionInterpretor.useEnvironmentCard(output, table, player1, action, turn);
                    else actionInterpretor.useEnvironmentCard(output, table, player2, action, turn);
            }

            else if(actual_action.equals("getFrozenCardsOnTable"))
                    actionInterpretor.getFrozenCardsOnTable(output, table, action);

            else if(actual_action.equals("cardUsesAttack")) {
                int turn = actionInterpretor.checkPlayerTurn(player1, player2);

                Coordinates coordinates_attacker = new Coordinates();
                Coordinates coordinates_attacked = new Coordinates();
                coordinates_attacker.setX(action.getCardAttacker().getX());
                coordinates_attacker.setY(action.getCardAttacker().getY());
                coordinates_attacked.setX(action.getCardAttacked().getX());
                coordinates_attacked.setY(action.getCardAttacked().getY());

                actionInterpretor.cardUsesAttack(output, table, action, coordinates_attacker, coordinates_attacked, turn);
            }
        }
        System.out.println("******");

//        System.out.println("***********");

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}