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
        Hero player_hero = player.getHero();
        Hero aux_hero = new Hero(player_hero.getMana(), player_hero.getDescription(), player_hero.getColors(), player_hero.getName(), player_hero.isFrozen(),
                player_hero.isHas_attacked(), player_hero.getHealth());
        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", aux_hero);
    }

    public void getPlayerTurn(ArrayNode output, Player player1, Player player2, ActionsInput action) {
        int turn = 0;
        if (checkPlayerTurn(player1, player2) != 0) {
            turn = checkPlayerTurn(player1, player2);
        }
        output.addObject().put("command", action.getCommand()).put("output", turn);

    }

    public void start_new_round(Table table, Player player1, Player player2) {
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

        for (ArrayList<DeckCard> table_row_aux : table.getTable_cards())
            for (DeckCard aux_card : table_row_aux)
                aux_card.setHas_attacked(false);

    }

    public void endPlayerTurn(Table table, Player player1, Player player2, int end_player_turn_counter) {
        if (checkPlayerTurn(player1, player2) != 0 && !player1.isHeroKilled() && !player2.isHeroKilled()) {
            if (checkPlayerTurn(player1, player2) == 1) {
                player1.setTurn(false);
                player2.setTurn(true);

                if (table.getTable_cards().size() != 0)
                    for (int index = 2; index < 4; index++)
                        for (DeckCard aux_card : table.getTable_cards().get(index)) {
                            aux_card.setHas_attacked(false);
                            aux_card.setFrozen(false);
                        }
                player1.getHero().setHas_attacked(false);

            } else {
                player2.setTurn(false);
                player1.setTurn(true);

                if (table.getTable_cards().size() != 0)
                    for (int index = 0; index < 2; index++)
                        for (DeckCard aux_card : table.getTable_cards().get(index)) {
                            aux_card.setHas_attacked(false);
                            aux_card.setFrozen(false);
                        }
                player2.getHero().setHas_attacked(false);
            }

            if (end_player_turn_counter % 2 == 0)
                start_new_round(table, player1, player2);
        }

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
                            } else {
                                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                                        .put("error", "Cannot place card on table since row is full.");
                                return;
                            }
                        } else {
                            if (table.getBackRow_player1().size() != 5) {
                                table.getBackRow_player1().add(card_to_place);
                                table.getTable_cards().removeAll(table.getTable_cards());
                                Collections.addAll(table.getTable_cards(), table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
                            } else {
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
                            } else {
                                output.addObject().put("command", action.getCommand()).put("handIdx", action.getHandIdx())
                                        .put("error", "Cannot place card on table since row is full.");
                                return;
                            }
                        } else {
                            if (table.getBackRow_player2().size() != 5) {
                                table.getBackRow_player2().add(card_to_place);
                                table.getTable_cards().removeAll(table.getTable_cards());
                                Collections.addAll(table.getTable_cards(), table.getBackRow_player2(), table.getFrontRow_player2(), table.getFrontRow_player1(), table.getBackRow_player1());
                            } else {
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
        ArrayList<ArrayList<DeckCard>> aux_table_cards = new ArrayList<>();
//
        aux_table_cards.addAll(table.getTable_cards());
//            for (DeckCard aux_card : table.getTable_cards().get(index))
//                aux_table_cards.get(index).add(aux_card);
//        }
        output.addObject().put("command", action.getCommand()).putPOJO("output", aux_table_cards);
    }

    public DeckCard getCardAtPosition_helper(Coordinates coordinates, Table table) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        if (x == 0 && x < table.getTable_cards().size() && y < table.getTable_cards().get(x).size())
            return table.getBackRow_player2().get(coordinates.getY());
        else if (x == 1 && x < table.getTable_cards().size() && y < table.getTable_cards().get(x).size())
            return table.getFrontRow_player2().get(coordinates.getY());
        else if (x == 2 && x < table.getTable_cards().size() && y < table.getTable_cards().get(x).size())
            return table.getFrontRow_player1().get(coordinates.getY());
        else if (x == 3 && x < table.getTable_cards().size() && y < table.getTable_cards().get(x).size())
            return table.getBackRow_player1().get(coordinates.getY());

        return null;

    }


    public void getCardAtPosition(ArrayNode output, Coordinates coordinates, Table table, ActionsInput action) {
        DeckCard output_card = getCardAtPosition_helper(coordinates, table);
        if (output_card != null) {
            DeckCard copy_card = new Minion(output_card.getMana(), output_card.getDescription(), output_card.getColors(),
                    output_card.getName(), ((Minion)output_card).getHealth(), ((Minion)output_card).getAttackDamage(),
                    output_card.isFrozen(), output_card.isHas_attacked());

            output.addObject().put("command", action.getCommand()).put("x", coordinates.getX()).
                    put("y", coordinates.getY()).putPOJO("output", copy_card);
        }
        else output.addObject().put("command", action.getCommand()).put("x", coordinates.getX()).
                put("y", coordinates.getY()).putPOJO("output", "No card available at that position.");

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
            if (my_env_card.getMana() <= player.getMana()) {
                if (turn == 1) {
                    if (action.getAffectedRow() == 0 || action.getAffectedRow() == 1) {
                        EnvironmentCardAbilities my_env_card_ability = new EnvironmentCardAbilities();
                        my_env_card_ability.checkType(output, my_env_card, table, player, action);
                    } else {
                        output.addObject().put("command", action.getCommand()).
                                put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                                .put("error", "Chosen row does not belong to the enemy.");
                        return;
                    }

                } else {
                    if (action.getAffectedRow() == 2 || action.getAffectedRow() == 3) {
                        EnvironmentCardAbilities my_env_card_ability = new EnvironmentCardAbilities();
                        my_env_card_ability.checkType(output, my_env_card, table, player, action);
                    } else {
                        output.addObject().put("command", action.getCommand()).
                                put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                                .put("error", "Chosen row does not belong to the enemy.");
                        return;
                    }

                }
            } else {
                output.addObject().put("command", action.getCommand()).
                        put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                        .put("error", "Not enough mana to use environment card.");
                return;
            }

        } else output.addObject().put("command", action.getCommand()).
                put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow()).
                put("error", "Chosen card is not of type environment.");


    }

    public void getFrozenCardsOnTable(ArrayNode output, Table table, ActionsInput action) {
        ArrayList<DeckCard> frozenCardsAux = new ArrayList<>();
        for (ArrayList<DeckCard> row_array_list : table.getTable_cards())
            for (DeckCard aux_card : row_array_list) {
                if (aux_card.isFrozen())
                    frozenCardsAux.add(aux_card);
            }

        output.addObject().put("command", action.getCommand()).putPOJO("output", frozenCardsAux);

    }

    public boolean checkTank(DeckCard card) {
        if (card.getName().equals("Goliath") || card.getName().equals("Warden"))
            return true;
        return false;
    }

    public boolean check_existsTank(Table table, int turn) {
        if (turn == 1) {
            for (int index = 0; index < 2; index++)
                for (DeckCard aux_card : table.getTable_cards().get(index))
                    if (checkTank(aux_card))
                        return true;
            return false;
        } else {
            for (int index = 2; index < 4; index++)
                for (DeckCard aux_card : table.getTable_cards().get(index))
                    if (checkTank(aux_card))
                        return true;
            return false;
        }
    }

    public void cardUsesAttack(ArrayNode output, Table table, ActionsInput action, Coordinates coordinates_attacker, Coordinates coordinates_attacked, int turn) {
        int x_attacker = coordinates_attacker.getX();
        int y_attacker = coordinates_attacker.getY();
        int x_attacked = coordinates_attacked.getX();
        int y_attacked = coordinates_attacked.getY();

        DeckCard card_attacker = getCardAtPosition_helper(coordinates_attacker, table);
        DeckCard card_attacked = getCardAtPosition_helper(coordinates_attacked, table);

        if ((turn == 1 && (x_attacked == 0 || x_attacked == 1)) || (turn == 2 && (x_attacked == 2 || x_attacked == 3))) {
            if (card_attacker != null && !card_attacker.isHas_attacked()) {
                if (!card_attacker.isFrozen()) {
                    if (check_existsTank(table, turn) && checkTank(card_attacked) || !check_existsTank(table, turn)) {
                        Minion new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(),
                                card_attacked.getColors(), card_attacked.getName(), ((Minion) card_attacked).getHealth(),
                                ((Minion) card_attacked).getAttackDamage(), card_attacked.isFrozen(), card_attacked.isHas_attacked());
                        new_card_attacked.setHealth(new_card_attacked.getHealth() - ((Minion) card_attacker).getAttackDamage());

                        card_attacker.setHas_attacked(true);
//                        System.out.println(new_card_attacked.getName() + " " + new_card_attacked.getHealth() + " "
//                        +card_attacker.getName() + ((Minion)card_attacker).getAttackDamage());

                        table.getTable_cards().get(x_attacked).set(y_attacked, new_card_attacked);

                        if (((Minion) table.getTable_cards().get(x_attacked).get(y_attacked)).getHealth() <= 0)
                            table.getTable_cards().get(x_attacked).remove(y_attacked);

                    } else {
                        output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                                .putPOJO("cardAttacked", coordinates_attacked).
                                put("error", "Attacked card is not of type 'Tank'.");
                        return;
                    }

                } else {
                    output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                            .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacker card is frozen.");
                    return;
                }
            } else {
                output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                        .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacker card has already attacked this turn.");
                return;
            }
        } else {
            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                    .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacked card does not belong to the enemy.");
            return;
        }

    }

    public void cardUsesAbility(ArrayNode output, Table table, ActionsInput action, Coordinates coordinates_attacker, Coordinates coordinates_attacked, int turn) {
        int x_attacker = coordinates_attacker.getX();
        int y_attacker = coordinates_attacker.getY();
        int x_attacked = coordinates_attacked.getX();
        int y_attacked = coordinates_attacked.getY();

        DeckCard card_attacker = getCardAtPosition_helper(coordinates_attacker, table);
        DeckCard card_attacked = getCardAtPosition_helper(coordinates_attacked, table);

        if(card_attacker == null || card_attacked == null)
            return;

        if (!card_attacker.isFrozen()) {
            if (!card_attacker.isHas_attacked()) {
                if (card_attacker.getName().equals("Disciple")) {
                    if ((turn == 1 && (x_attacked == 2 || x_attacked == 3)) || (turn == 2 && (x_attacked == 0 || x_attacked == 1))) {
                            MinionCardAbilities minionCardAbilities = new MinionCardAbilities();
                            minionCardAbilities.Gods_Plan(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
                    } else {
                        output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                                .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacked card does not belong to the current player.");
                        return;
                    }
                } else if (card_attacker.getName().equals("The Ripper") || card_attacker.getName().equals("Miraj") || card_attacker.getName().equals("The Cursed One")) {
                    if ((turn == 1 && (x_attacked == 0 || x_attacked == 1)) || (turn == 2 && (x_attacked == 2 || x_attacked == 3))) {
                        if (check_existsTank(table, turn) && checkTank(card_attacked) || !check_existsTank(table, turn)) {
                            MinionCardAbilities minionCardAbilities = new MinionCardAbilities();
                            minionCardAbilities.checkType(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);

                        } else {
                            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                                    .putPOJO("cardAttacked", coordinates_attacked).
                                    put("error", "Attacked card is not of type 'Tank'.");
                            return;
                        }
                    } else {
                        output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                                .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacked card does not belong to the enemy.");
                        return;
                    }
                }
            } else {
                output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                        .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacker card has already attacked this turn.");
                return;
            }


        } else {
            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker)
                    .putPOJO("cardAttacked", coordinates_attacked).put("error", "Attacker card is frozen.");
            return;
        }


    }

public void useAttackHero(ArrayNode output, Coordinates coordinates_attacker, ActionsInput action, Player player, Table table, int turn) {
    int x_attacker = coordinates_attacker.getX();
    int y_attacker = coordinates_attacker.getX();

    DeckCard card_attacker = getCardAtPosition_helper(coordinates_attacker, table);

    if(card_attacker != null && !card_attacker.isFrozen()) {
        if(!card_attacker.isHas_attacked()) {
            if(!check_existsTank(table, turn)) {
                Hero attacked_hero = player.getHero();
                attacked_hero.setHealth(attacked_hero.getHealth() - ((Minion)card_attacker).getAttackDamage());

                card_attacker.setHas_attacked(true);

                if(attacked_hero.getHealth() <= 0) {
                    player.setHeroKilled(true);
                    if(turn == 1) {
                        output.addObject().put("gameEnded", "Player one killed the enemy hero.");
                        GameStatistics.setPlayerOneWins(GameStatistics.getPlayerOneWins() + 1);
                    }
                    else {
                        output.addObject().put("gameEnded", "Player two killed the enemy hero.");
                        GameStatistics.setPlayerTwoWins(GameStatistics.getPlayerTwoWins() + 1);
                    }
                }
            }

            else {
                output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker).
                        put("error", "Attacked card is not of type 'Tank'.");
                return;
            }

        }

        else {
            output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker).
                    put("error", "Attacker card has already attacked this turn.");
            return;
        }
    }

    else {
        output.addObject().put("command", action.getCommand()).putPOJO("cardAttacker", coordinates_attacker).
                put("error", "Attacker card is frozen.");
        return;
    }



    }

    public void useHeroAbility(ArrayNode output, ActionsInput action, Table table, int turn, Player player) {
        Hero player_hero = player.getHero();
        int affected_row = action.getAffectedRow();

        if(player_hero.getMana() <= player.getMana()) {
            if(!player_hero.isHas_attacked()) {
                if(player_hero.getName().equals("Lord Royce") || player_hero.getName().equals("Empress Thorina")) {
                    if((turn == 1 && (affected_row == 0 || affected_row == 1)) || (turn == 2 && (affected_row == 2 || affected_row == 3))) {
                        HeroCardAbilities heroCardAbilities = new HeroCardAbilities();
                        heroCardAbilities.checkType(affected_row, table, player);
                    }
                    else {
                        output.addObject().put("command", action.getCommand()).put("affectedRow", affected_row).
                                put("error", "Selected row does not belong to the enemy.");
                        return;
                    }
                }

                else if(player_hero.getName().equals("General Kocioraw") || player_hero.getName().equals("King Mudface")) {
                    if((turn == 1 && (affected_row == 2 || affected_row == 3)) || (turn == 2 && (affected_row == 0 || affected_row == 1))) {
                        HeroCardAbilities heroCardAbilities = new HeroCardAbilities();
                        heroCardAbilities.checkType(affected_row, table, player);
                    }
                    else {
                        output.addObject().put("command", action.getCommand()).put("affectedRow", affected_row).
                                put("error", "Selected row does not belong to the current player.");
                        return;
                    }
                }

            }

            else {
                output.addObject().put("command", action.getCommand()).put("affectedRow", affected_row).
                        put("error", "Hero has already attacked this turn.");
                return;
            }

        }

        else {
            output.addObject().put("command", action.getCommand()).put("affectedRow", affected_row).
                    put("error", "Not enough mana to use hero's ability.");
            return;
        }

    }



}


