package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.Coordinates;

import java.util.ArrayList;
import java.util.Collections;

public class ActionInterpretor {

    public int checkPlayerTurn(Player player1, Player player2) {
        int turn = 0;
        if (player1.isTurn() && !player2.isTurn())
            turn = 1;
        else if (!player1.isTurn() && player2.isTurn()) turn = 2;

        return turn;
    }

    public void getPlayerDeck(ArrayNode output, Player player, ActionsInput action) {
        ArrayList<DeckCard> deckCardsAux = new ArrayList<>();
        for (DeckCard aux_card : player.getDeckCards())
            deckCardsAux.add(aux_card);
        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", deckCardsAux);

    }

    public void getPlayerHero(ArrayNode output, Player player, ActionsInput action) {
        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player.getHero());
    }

    public void getPlayerTurn(ArrayNode output, Player player1, Player player2, ActionsInput action) {
        int turn = 0;
        if (checkPlayerTurn(player1, player2) != 0) {
            turn = checkPlayerTurn(player1, player2);
        }
        output.addObject().put("command", action.getCommand()).put("output", turn);

    }

    public void start_new_round(Player player1, Player player2) {
        player1.setRound(player1.getRound() + 1);
        player2.setRound(player2.getRound() + 1);

        if (player1.getRound() <= 10 && player2.getRound() <= 10) {
            player1.setMana(player1.getMana() + player1.getRound());
            player2.setMana(player2.getMana() + player2.getRound());
        } else {
            player1.setMana(player1.getMana() + 10);
            player2.setMana(player2.getMana() + 10);
        }

        if (player1.getDeckCards().size() != 0) {
            player1.getHandCards().add(player1.getDeckCards().get(0));
            player1.getDeckCards().remove(0);
        }

        if (player2.getDeckCards().size() != 0) {
            player2.getHandCards().add(player2.getDeckCards().get(0));
            player2.getDeckCards().remove(0);
        }

    }

    public void endPlayerTurn(Player player1, Player player2, int end_player_turn_counter) {
        if (checkPlayerTurn(player1, player2) != 0) {
            if (checkPlayerTurn(player1, player2) == 1) {
                player1.setTurn(false);
                player2.setTurn(true);
            } else {
                player2.setTurn(false);
                player1.setTurn(true);
            }
        }

        if (end_player_turn_counter % 2 == 0)
            start_new_round(player1, player2);

    }

    public void placeCard(ArrayNode output, Table table, Player player, ActionsInput action, int turn) {
        if (action.getHandIdx() >= 0 && action.getHandIdx() < player.getHandCards().size()) {
            DeckCard card_to_place = player.getHandCards().get(action.getHandIdx());

            if (!card_to_place.getName().equals("Firestorm") && !card_to_place.getName().equals("Winterfell") && !card_to_place.getName().equals("Heart Hound")) {
                if (card_to_place.getMana() <= player.getMana()) {
                    if (turn == 1) {
                        if (card_to_place.getName().equals("The Ripper") || card_to_place.getName().equals("Miraj") || card_to_place.getName().equals("Goliath") || card_to_place.getName().equals("Warden")) {
                            if (table.getFrontRow_player1().size() != 5) {
                                table.getFrontRow_player1().add(card_to_place);
                                table.getTable_cards().removeAll(table.getTable_cards());
                                Collections.addAll(table.getTable_cards(), table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
                            }
                            else {
                                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                                        .put("error", "Cannot place card on table since row is full.");
                                return;
                            }
                        } else {
                            if (table.getBackRow_player1().size() != 5) {
                                table.getBackRow_player1().add(card_to_place);
                                table.getTable_cards().removeAll(table.getTable_cards());
                                Collections.addAll(table.getTable_cards(), table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
                            }
                                else {
                                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                                        .put("error", "Cannot place card on table since row is full.");
                                return;
                            }
                        }

                    } else {
                        if (card_to_place.getName().equals("The Ripper") || card_to_place.getName().equals("Miraj") || card_to_place.getName().equals("Goliath") || card_to_place.getName().equals("Warden")) {
                            if (table.getFrontRow_player2().size() != 5) {
                                table.getFrontRow_player2().add(card_to_place);
                                table.getTable_cards().removeAll(table.getTable_cards());
                                Collections.addAll(table.getTable_cards(), table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
                            }
                            else {
                                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                                        .put("error", "Cannot place card on table since row is full.");
                                return;
                            }
                        } else {
                            if (table.getBackRow_player2().size() != 5) {
                                table.getBackRow_player2().add(card_to_place);
                                table.getTable_cards().removeAll(table.getTable_cards());
                                Collections.addAll(table.getTable_cards(), table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
                            }
                            else {
                                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                                        .put("error", "Cannot place card on table since row is full.");
                                return;
                            }
                        }
                    }

                    player.getHandCards().remove(action.getHandIdx());
                    player.setMana(player.getMana() - card_to_place.getMana());
                } else
                    output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                            .put("error", "Not enough mana to place card on table.");
            } else
                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                        .put("error", "Cannot place environment card on table.");

        }

    }

    public void getCardsInHand(ArrayNode output, Player player, ActionsInput action) {
        ArrayList<DeckCard> handCardsAux = new ArrayList<>();
        for (DeckCard aux_card : player.getHandCards())
            handCardsAux.add(aux_card);
        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", handCardsAux);
    }

    public void getPlayerMana(ArrayNode output, Player player, ActionsInput action) {
        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).put("output", player.getMana());

    }

    public void getCardsOnTable(ArrayNode output, Table table, ActionsInput action) {
//        ArrayList<ArrayList<DeckCard>> aux_table_cards = table.getTable_cards();
//        Collections.addAll(aux_table_cards, table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
        output.addObject().put("command", action.getCommand()).putPOJO("output", table.getTable_cards());
    }

    public void getCardAtPosition(ArrayNode output, Coordinates coordinates, Table table, ActionsInput action) {
        DeckCard output_card = null;

        if (coordinates.getX() == 0)
            output_card = table.getBackRow_player2().get(coordinates.getY());
        else if (coordinates.getX() == 1)
            output_card = table.getFrontRow_player2().get(coordinates.getY());
        else if (coordinates.getX() == 2)
            output_card = table.getFrontRow_player1().get(coordinates.getY());
        else if (coordinates.getX() == 3)
            output_card = table.getBackRow_player1().get(coordinates.getY());

        output.addObject().put("command", action.getCommand()).putPOJO("output", output_card);

    }

    public void getEnvironmentCardsInHand(ArrayNode output, Player player, ActionsInput action) {
        ArrayList<DeckCard> environmentCards = new ArrayList<>();
        for (DeckCard aux_card : player.getHandCards()) {
            if (aux_card.getName().equals("Firestorm") || aux_card.getName().equals("Winterfell") || aux_card.getName().equals("Heart Hound"))
                environmentCards.add(aux_card);
        }

        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", environmentCards);
    }

    public void useEnvironmentCard(ArrayNode output, Table table, Player player, ActionsInput action, int turn) {
        DeckCard my_env_card = player.getHandCards().get(action.getHandIdx());
        String my_env_card_name = my_env_card.getName();
//        System.out.println(my_env_card_name);
        if (my_env_card_name.equals("Firestorm") || my_env_card_name.equals("Winterfell") || my_env_card_name.equals("Heart Hound")) {
            if(my_env_card.getMana() <= player.getMana()) {
                if(turn == 1) {
                    if(action.getAffectedRow() == 0 || action.getAffectedRow() == 1) {
                        EnvironmentCardAbilities my_env_card_ability = new EnvironmentCardAbilities();
                        my_env_card_ability.checkType(output, my_env_card, table, player, action);
                    }

                    else {
                        output.addObject().put("command", action.getCommand()).
                                put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                                .put("error", "Chosen row does not belong to the enemy.");
                        return;
                    }

                }

                else {
                    if(action.getAffectedRow() == 2 || action.getAffectedRow() == 3) {
                        EnvironmentCardAbilities my_env_card_ability = new EnvironmentCardAbilities();
                        my_env_card_ability.checkType(output, my_env_card, table, player, action);
                    }

                    else {
                        output.addObject().put("command", action.getCommand()).
                                put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                                .put("error", "Chosen row does not belong to the enemy.");
                        return;
                    }

                }
            }

            else {
                output.addObject().put("command", action.getCommand()).
                        put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                        .put("error", "Not enough mana to use environment card.");
                return;
            }

        }

        else output.addObject().put("command", action.getCommand()).
                    put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow()).
                    put("error", "Chosen card is not of type environment.");


    }

    public void getFrozenCardsOnTable(ArrayNode output, Table table, ActionsInput action) {
        ArrayList<DeckCard> frozenCardsAux = new ArrayList<>();
        for(ArrayList<DeckCard> row_array_list : table.getTable_cards())
            for (DeckCard aux_card : row_array_list) {
                if(aux_card.isFrozen())
                    frozenCardsAux.add(aux_card);
            }

        output.addObject().put("command", action.getCommand()).putPOJO("output",frozenCardsAux);

    }



}

