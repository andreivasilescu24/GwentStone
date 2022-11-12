package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class ActionInterpretor {

    public int checkPlayerTurn (Player player1, Player player2) {
        int turn = 0;
        if(player1.isTurn() == true && player2.isTurn() == false)
            turn = 1;
        else if(player1.isTurn() == false && player2.isTurn() == true) turn = 2;

        return turn;
    }

public void getPlayerDeck(ArrayNode output, Player player, ActionsInput action) {
    if(player.getDeckCards().size() != 0) {
        ArrayList<DeckCard> deckCardsAux = new ArrayList<>();
        for (DeckCard aux_card : player.getDeckCards())
            deckCardsAux.add(aux_card);
        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", deckCardsAux);
    }

}

public void getPlayerHero(ArrayNode output, Player player, ActionsInput action) {
    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player.getHero());
}

public void getPlayerTurn(ArrayNode output, Player player1, Player player2, ActionsInput action) {
    int turn = 0;
    if(checkPlayerTurn(player1, player2) != 0) {
        turn = checkPlayerTurn(player1, player2);
    }
    output.addObject().put("command", action.getCommand()).put("output", turn);

}

public void start_new_round (Player player1, Player player2) {
        player1.setRound(player1.getRound() + 1);
        player2.setRound(player2.getRound() + 1);

        player1.setMana(player1.getMana() + player1.getRound());
        player2.setMana(player2.getMana() + player2.getRound());

        if(player1.getDeckCards().size() != 0) {
            player1.getHandCards().add(player1.getDeckCards().get(0));
            player1.getDeckCards().remove(0);
        }

        if(player2.getDeckCards().size() != 0) {
            player2.getHandCards().add(player2.getDeckCards().get(0));
            player2.getDeckCards().remove(0);
    }

}
public void endPlayerTurn(Player player1, Player player2, int end_player_turn_counter) {
    if(checkPlayerTurn(player1, player2) != 0) {
        if(checkPlayerTurn(player1, player2) == 1) {
            player1.setTurn(false);
            player2.setTurn(true);
        }

        else {
            player2.setTurn(false);
            player1.setTurn(true);
        }
    }

    if(end_player_turn_counter % 2 == 0)
        start_new_round(player1, player2);

}

public void placeCard(Table table, Player player, ActionsInput action, int turn) {
        if(action.getHandIdx() >= 0 && action.getHandIdx() < player.getHandCards().size()) {
            DeckCard card_to_place = player.getHandCards().get(action.getHandIdx());
            if (card_to_place.getName().equals("Firestorm") == false || card_to_place.getName().equals("Winterfell") == false || card_to_place.getName().equals("Heart Hound") == false) {
                if(card_to_place.getMana() <= player.getMana()) {
                    if(turn == 1) {
                        if(card_to_place.getName().equals("The Ripper") || card_to_place.getName().equals("Miraj") || card_to_place.getName().equals("Goliath") || card_to_place.getName().equals("Warden"))
                            table.getFrontRow_player1().add(card_to_place);
                        else table.getBackRow_player1().add(card_to_place);
                    }

                    else {
                        if(card_to_place.getName().equals("The Ripper") || card_to_place.getName().equals("Miraj") || card_to_place.getName().equals("Goliath") || card_to_place.getName().equals("Warden"))
                            table.getFrontRow_player2().add(card_to_place);
                        else table.getBackRow_player2().add(card_to_place);
                    }
                }

                player.getHandCards().remove(action.getHandIdx());
                player.setMana(player.getMana() - card_to_place.getMana());
            }
        }

}

public void getCardsInHand(ArrayNode output, Player player, ActionsInput action) {
        if(player.getHandCards().size() != 0) {
            ArrayList<DeckCard> handCardsAux = new ArrayList<>();
            for(DeckCard aux_card : player.getHandCards())
                handCardsAux.add(aux_card);
            output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", handCardsAux);
        }
}

public void getPlayerMana(ArrayNode output, Player player, ActionsInput action) {
    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).put("output", player.getMana());

}

public void getCardsOnTable(ArrayNode output, Table table, ActionsInput action) {
        ArrayList<ArrayList<DeckCard>> aux_table_cards = table.getTable_cards();
        Collections.addAll(aux_table_cards, table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
        output.addObject().put("command", action.getCommand()).putPOJO("output", aux_table_cards);
}


}
