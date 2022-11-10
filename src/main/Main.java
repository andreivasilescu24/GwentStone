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

        ActionsInput actionsInput;
        CardInput cardInput;
        Coordinates coordinates;
        DecksInput decksInput;
        GameInput gameInput;
        Input input;
        StartGameInput startGameInput;

        DecksInput player1_all_decks, player2_all_decks;

        startGameInput = inputData.getGames().get(0).getStartGame();

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

        // introducem in array-ul de carti din mana, prima carte trasa din deck
        player1.getHandCards().add(player1_deck.get(0));
        player2.getHandCards().add(player2_deck.get(0));

        // elimin din deck prima carte trasa
        player1_deck.remove(0);
        player2_deck.remove(0);

        // obtin cei doi eroi corespunzatori fiecarui jucator
        CardInput player1_hero = startGameInput.getPlayerOneHero();
        CardInput player2_hero = startGameInput.getPlayerTwoHero();

        //obtin actiunile
        ArrayList<ActionsInput> actions = inputData.getGames().get(0).getActions();
        String actual_action;
        for(ActionsInput action : actions) {
            actual_action = action.getCommand();
            System.out.println(actual_action);
            output.addObject().put("command", actual_action).put("playerIdx", action.getPlayerIdx())
                    .putPOJO("output", player1_deck);
//            if(actual_action.getCommand().equals("getPlayerDeck"))
//                else if(actual_action.getCommand().equals("getPlayerHero"))
//                    else if(actual_action.getCommand().equals("getPlayerTurn"))

        }

//        System.out.println(player1_hero + "\n" + player2_hero);

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
