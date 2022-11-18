package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.Coordinates;

import java.util.ArrayList;
import java.util.Collections;

public final class ActionInterpretor {

    public int checkPlayerTurn(final Player player1, final Player player2) {
        int turn = 0;
        if (player1.isTurn() && !player2.isTurn()) {
            turn = 1;
        } else if (!player1.isTurn() && player2.isTurn()) {
            turn = 2;
        }

        return turn;
    }

    public void getPlayerDeck(final ArrayNode output, final Player player,
                              final ActionsInput action) {
        ArrayList<DeckCard> deckCardsAux = new ArrayList<>();
        for (DeckCard auxCard : player.getDeckCards()) {
            deckCardsAux.add(auxCard);
        }
        output.addObject().put("command", action.getCommand()).
                put("playerIdx", action.getPlayerIdx()).putPOJO("output", deckCardsAux);

    }

    public void getPlayerHero(final ArrayNode output, final Player player,
                              final ActionsInput action) {
        Hero playerHero = player.getHero();
        Hero auxHero = new Hero(playerHero.getMana(), playerHero.getDescription(),
                playerHero.getColors(), playerHero.getName(), playerHero.isFrozen(),
                playerHero.isHasAttacked(), playerHero.getHealth());

        output.addObject().put("command", action.getCommand()).put("playerIdx",
                action.getPlayerIdx()).putPOJO("output", auxHero);
    }

    public void getPlayerTurn(final ArrayNode output, final Player player1, final Player player2,
                              final ActionsInput action) {
        int turn = 0;
        if (checkPlayerTurn(player1, player2) != 0) {
            turn = checkPlayerTurn(player1, player2);
        }
        output.addObject().put("command", action.getCommand()).put("output", turn);

    }

    public void startNewRound(final Table table, final Player player1, final Player player2) {
        player1.setRound(player1.getRound() + 1);
        player2.setRound(player2.getRound() + 1);

        final int maxRoundCountIncrement = 10;

        if (player1.getRound() <= maxRoundCountIncrement
                && player2.getRound() <= maxRoundCountIncrement) {
            player1.setMana(player1.getMana() + player1.getRound());
            player2.setMana(player2.getMana() + player2.getRound());
        } else {
            player1.setMana(player1.getMana() + maxRoundCountIncrement);
            player2.setMana(player2.getMana() + maxRoundCountIncrement);
        }

        if (player1.getDeckCards().size() != 0) {
            player1.getHandCards().add(player1.getDeckCards().get(0));
            player1.getDeckCards().remove(0);
        }

        if (player2.getDeckCards().size() != 0) {
            player2.getHandCards().add(player2.getDeckCards().get(0));
            player2.getDeckCards().remove(0);
        }

        for (ArrayList<DeckCard> tableRowAux : table.getTableCards()) {
            for (DeckCard auxCard : tableRowAux) {
                auxCard.setHasAttacked(false);
            }
        }

    }

    public void endPlayerTurn(final Table table, final Player player1, final Player player2,
                              final int endPlayerTurnCounter) {

        final int maxRowLimitPlayer1 = 4;
        final int maxRowLimitPlayer2 = 2;

        if (checkPlayerTurn(player1, player2) != 0 && !player1.isHeroKilled()
                && !player2.isHeroKilled()) {
            if (checkPlayerTurn(player1, player2) == 1) {
                player1.setTurn(false);
                player2.setTurn(true);

                if (table.getTableCards().size() != 0) {
                    for (int index = 2; index < maxRowLimitPlayer1; index++) {
                        for (DeckCard auxCard : table.getTableCards().get(index)) {
                            auxCard.setHasAttacked(false);
                            auxCard.setFrozen(false);
                        }
                    }
                }
                player1.getHero().setHasAttacked(false);

            } else {
                player2.setTurn(false);
                player1.setTurn(true);

                if (table.getTableCards().size() != 0) {
                    for (int index = 0; index < maxRowLimitPlayer2; index++) {
                        for (DeckCard auxCard : table.getTableCards().get(index)) {
                            auxCard.setHasAttacked(false);
                            auxCard.setFrozen(false);
                        }
                    }
                }

                player2.getHero().setHasAttacked(false);
            }

            if (endPlayerTurnCounter % 2 == 0) {
                startNewRound(table, player1, player2);
            }
        }

    }

    public void placeCard(final ArrayNode output, final Table table, final Player player,
                          final ActionsInput action, final int turn) {

        final int maxCardsOnRow = 5;

        if (action.getHandIdx() >= 0 && action.getHandIdx() < player.getHandCards().size()) {
            DeckCard cardToPlace = player.getHandCards().get(action.getHandIdx());

            if (!cardToPlace.getName().equals("Firestorm") && !cardToPlace.getName().
                    equals("Winterfell") && !cardToPlace.getName().equals("Heart Hound")) {

                if (cardToPlace.getMana() <= player.getMana()) {
                    if (turn == 1) {
                        if (cardToPlace.getName().equals("The Ripper")
                                || cardToPlace.getName().equals("Miraj")
                                || cardToPlace.getName().equals("Goliath")
                                || cardToPlace.getName().equals("Warden")) {

                            if (table.getFrontRowPlayer1().size() != maxCardsOnRow) {
                                table.getFrontRowPlayer1().add(cardToPlace);
                                table.getTableCards().removeAll(table.getTableCards());
                                Collections.addAll(table.getTableCards(),
                                        table.getBackRowPlayer2(), table.getFrontRowPlayer2(),
                                        table.getFrontRowPlayer1(),
                                        table.getBackRowPlayer1());
                            } else {
                                output.addObject().put("command", action.getCommand())
                                        .put("handIdx", action.getHandIdx())
                                        .put("error",
                                                "Cannot place card on table since row is full.");
                                return;
                            }
                        } else {
                            if (table.getBackRowPlayer1().size() != maxCardsOnRow) {
                                table.getBackRowPlayer1().add(cardToPlace);
                                table.getTableCards().removeAll(table.getTableCards());
                                Collections.addAll(table.getTableCards(),
                                        table.getBackRowPlayer2(), table.getFrontRowPlayer2(),
                                        table.getFrontRowPlayer1(), table.getBackRowPlayer1());
                            } else {
                                output.addObject().put("command", action.getCommand())
                                        .put("handIdx", action.getHandIdx())
                                        .put("error",
                                                "Cannot place card on table since row is full.");
                                return;
                            }
                        }

                    } else {
                        if (cardToPlace.getName().equals("The Ripper")
                                || cardToPlace.getName().equals("Miraj")
                                || cardToPlace.getName().equals("Goliath")
                                || cardToPlace.getName().equals("Warden")) {

                            if (table.getFrontRowPlayer2().size() != maxCardsOnRow) {
                                table.getFrontRowPlayer2().add(cardToPlace);
                                table.getTableCards().removeAll(table.getTableCards());
                                Collections.addAll(table.getTableCards(),
                                        table.getBackRowPlayer2(), table.getFrontRowPlayer2(),
                                        table.getFrontRowPlayer1(), table.getBackRowPlayer1());
                            } else {
                                output.addObject().put("command", action.getCommand())
                                        .put("handIdx", action.getHandIdx())
                                        .put("error",
                                                "Cannot place card on table since row is full.");
                                return;
                            }
                        } else {
                            if (table.getBackRowPlayer2().size() != maxCardsOnRow) {
                                table.getBackRowPlayer2().add(cardToPlace);
                                table.getTableCards().removeAll(table.getTableCards());
                                Collections.addAll(table.getTableCards(),
                                        table.getBackRowPlayer2(), table.getFrontRowPlayer2(),
                                        table.getFrontRowPlayer1(), table.getBackRowPlayer1());
                            } else {
                                output.addObject().put("command", action.getCommand())
                                        .put("handIdx", action.getHandIdx())
                                        .put("error",
                                                "Cannot place card on table since row is full.");
                                return;
                            }
                        }
                    }

                    player.getHandCards().remove(action.getHandIdx());
                    player.setMana(player.getMana() - cardToPlace.getMana());
                } else {
                    output.addObject().put("command", action.getCommand())
                            .put("handIdx", action.getHandIdx())
                            .put("error", "Not enough mana to place card on table.");
                }
            } else {
                output.addObject().put("command", action.getCommand())
                        .put("handIdx", action.getHandIdx())
                        .put("error", "Cannot place environment card on table.");
            }

        }
    }


    public void getCardsInHand(final ArrayNode output, final Player player,
                               final ActionsInput action) {

        ArrayList<DeckCard> handCardsAux = new ArrayList<>();
        for (DeckCard auxCard : player.getHandCards()) {
            handCardsAux.add(auxCard);
        }
        output.addObject().put("command", action.getCommand()).
                put("playerIdx", action.getPlayerIdx()).
                putPOJO("output", handCardsAux);

    }

    public void getPlayerMana(final ArrayNode output, final Player player,
                              final ActionsInput action) {

        output.addObject().put("command", action.getCommand())
                .put("playerIdx", action.getPlayerIdx())
                .put("output", player.getMana());

    }

    public void getCardsOnTable(final ArrayNode output, final Table table,
                                final ActionsInput action) {
        ArrayList<ArrayList<DeckCard>> auxTableCards = new ArrayList<>();

        auxTableCards.addAll(table.getTableCards());
        output.addObject().put("command", action.getCommand())
                .putPOJO("output", auxTableCards);
    }

    public DeckCard getCardAtPositionHelper(final Coordinates coordinates, final Table table) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        final int backRowPlayer1 = 3;
        final int frontRowPlayer1 = 2;
        final int frontRowPlayer2 = 1;
        final int backRowPlayer2 = 0;

        if (x == backRowPlayer2 && x < table.getTableCards().size()
                && y < table.getTableCards().get(x).size()) {
            return table.getBackRowPlayer2().get(coordinates.getY());
        } else if (x == frontRowPlayer2 && x < table.getTableCards().size()
                && y < table.getTableCards().get(x).size()) {
            return table.getFrontRowPlayer2().get(coordinates.getY());
        } else if (x == frontRowPlayer1 && x < table.getTableCards().size()
                && y < table.getTableCards().get(x).size()) {
            return table.getFrontRowPlayer1().get(coordinates.getY());
        } else if (x == backRowPlayer1 && x < table.getTableCards().size()
                && y < table.getTableCards().get(x).size()) {
            return table.getBackRowPlayer1().get(coordinates.getY());
        }

        return null;

    }


    public void getCardAtPosition(final ArrayNode output, final Coordinates coordinates,
                                  final Table table, final ActionsInput action) {

        DeckCard outputCard = getCardAtPositionHelper(coordinates, table);
        if (outputCard != null) {
            DeckCard copyCard = new Minion(outputCard.getMana(), outputCard.getDescription(),
                    outputCard.getColors(), outputCard.getName(),
                    ((Minion) outputCard).getHealth(), ((Minion) outputCard).getAttackDamage(),
                    outputCard.isFrozen(), outputCard.isHasAttacked());

            output.addObject().put("command", action.getCommand())
                    .put("x", coordinates.getX())
                    .put("y", coordinates.getY()).putPOJO("output", copyCard);
        } else {
            output.addObject().put("command", action.getCommand())
                    .put("x", coordinates.getX())
                    .put("y", coordinates.getY())
                    .putPOJO("output", "No card available at that position.");
        }

    }

    public void getEnvironmentCardsInHand(final ArrayNode output, final Player player,
                                          final ActionsInput action) {
        ArrayList<DeckCard> environmentCards = new ArrayList<>();
        for (DeckCard auxCard : player.getHandCards()) {
            if (auxCard.getName().equals("Firestorm")
                    || auxCard.getName().equals("Winterfell")
                    || auxCard.getName().equals("Heart Hound")) {
                environmentCards.add(auxCard);
            }
        }

        output.addObject().put("command", action.getCommand())
                .put("playerIdx", action.getPlayerIdx())
                .putPOJO("output", environmentCards);
    }

    public void useEnvironmentCard(final ArrayNode output, final Table table, final Player player,
                                   final ActionsInput action, final int turn) {
        DeckCard myEnvCard = player.getHandCards().get(action.getHandIdx());
        String myEnvCardName = myEnvCard.getName();

        final int backRowPlayer1 = 3;
        final int frontRowPlayer1 = 2;
        final int frontRowPlayer2 = 1;
        final int backRowPlayer2 = 0;

        if (myEnvCardName.equals("Firestorm")
                || myEnvCardName.equals("Winterfell")
                || myEnvCardName.equals("Heart Hound")) {

            if (myEnvCard.getMana() <= player.getMana()) {
                if (turn == 1) {
                    if (action.getAffectedRow() == backRowPlayer2
                            || action.getAffectedRow() == frontRowPlayer2) {
                        EnvironmentCardAbilities myEnvCardAbility =
                                new EnvironmentCardAbilities();
                        myEnvCardAbility.checkType(output, myEnvCard, table, player, action);
                    } else {
                        output.addObject().put("command", action.getCommand())
                                .put("handIdx", action.getHandIdx())
                                .put("affectedRow", action.getAffectedRow())
                                .put("error", "Chosen row does not belong to the enemy.");
                    }

                } else {
                    if (action.getAffectedRow() == backRowPlayer1
                            || action.getAffectedRow() == frontRowPlayer1) {
                        EnvironmentCardAbilities myEnvCardAbility =
                                new EnvironmentCardAbilities();
                        myEnvCardAbility.checkType(output, myEnvCard, table, player, action);
                    } else {
                        output.addObject().put("command", action.getCommand())
                                .put("handIdx", action.getHandIdx())
                                .put("affectedRow", action.getAffectedRow())
                                .put("error", "Chosen row does not belong to the enemy.");
                    }

                }
            } else {
                output.addObject().put("command", action.getCommand())
                        .put("handIdx", action.getHandIdx())
                        .put("affectedRow", action.getAffectedRow())
                        .put("error", "Not enough mana to use environment card.");
            }

        } else {
            output.addObject().put("command", action.getCommand())
                    .put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                    .put("error", "Chosen card is not of type environment.");
        }
    }

    public void getFrozenCardsOnTable(final ArrayNode output, final Table table,
                                      final ActionsInput action) {
        ArrayList<DeckCard> frozenCardsAux = new ArrayList<>();
        for (ArrayList<DeckCard> rowArrayList : table.getTableCards()) {
            for (DeckCard auxCard : rowArrayList) {
                if (auxCard.isFrozen()) {
                    frozenCardsAux.add(auxCard);
                }
            }
        }

        output.addObject().put("command", action.getCommand()).putPOJO("output", frozenCardsAux);

    }

    public boolean checkTank(final DeckCard card) {
        if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
            return true;
        }

        return false;
    }

    public boolean checkExistsTank(final Table table, final int turn) {
        final int maxRowLimitPlayer1 = 4;
        final int maxRowLimitPlayer2 = 2;

        if (turn == 1) {
            for (int index = 0; index < maxRowLimitPlayer2; index++) {
                for (DeckCard auxCard : table.getTableCards().get(index)) {
                    if (checkTank(auxCard)) {
                        return true;
                    }
                }
            }
            return false;

        } else {
            for (int index = 2; index < maxRowLimitPlayer1; index++) {
                for (DeckCard auxCard : table.getTableCards().get(index)) {
                    if (checkTank(auxCard)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void cardUsesAttack(final ArrayNode output, final Table table, final ActionsInput action,
                               final Coordinates coordinatesAttacker,
                               final Coordinates coordinatesAttacked, final int turn) {

        int attackedX = coordinatesAttacked.getX();
        int attackedY = coordinatesAttacked.getY();

        final int backRowPlayer1 = 3;
        final int frontRowPlayer1 = 2;
        final int frontRowPlayer2 = 1;
        final int backRowPlayer2 = 0;

        DeckCard cardAttacker = getCardAtPositionHelper(coordinatesAttacker, table);
        DeckCard cardAttacked = getCardAtPositionHelper(coordinatesAttacked, table);

        if ((turn == 1 && (attackedX == backRowPlayer2 || attackedX == frontRowPlayer2))
                || (turn == 2 && (attackedX == backRowPlayer1 || attackedX == frontRowPlayer1))) {
            if (cardAttacker != null && !cardAttacker.isHasAttacked()) {
                if (!cardAttacker.isFrozen()) {
                    if (checkExistsTank(table, turn) && checkTank(cardAttacked)
                            || !checkExistsTank(table, turn)) {
                        Minion newCardAttacked = new Minion(cardAttacked.getMana(),
                                cardAttacked.getDescription(), cardAttacked.getColors(),
                                cardAttacked.getName(), ((Minion) cardAttacked).getHealth(),
                                ((Minion) cardAttacked).getAttackDamage(),
                                cardAttacked.isFrozen(), cardAttacked.isHasAttacked());

                        newCardAttacked.setHealth(newCardAttacked.getHealth()
                                - ((Minion) cardAttacker).getAttackDamage());

                        cardAttacker.setHasAttacked(true);

                        table.getTableCards().get(attackedX).set(attackedY, newCardAttacked);

                        if (((Minion) table.getTableCards().get(attackedX)
                                .get(attackedY)).getHealth() <= 0) {
                            table.getTableCards().get(attackedX).remove(attackedY);
                        }

                    } else {
                        output.addObject().put("command", action.getCommand())
                                .putPOJO("cardAttacker", coordinatesAttacker)
                                .putPOJO("cardAttacked", coordinatesAttacked).
                                put("error", "Attacked card is not of type 'Tank'.");
                    }

                } else {
                    output.addObject().put("command", action.getCommand())
                            .putPOJO("cardAttacker", coordinatesAttacker)
                            .putPOJO("cardAttacked", coordinatesAttacked)
                            .put("error", "Attacker card is frozen.");
                }
            } else {
                output.addObject().put("command", action.getCommand())
                        .putPOJO("cardAttacker", coordinatesAttacker)
                        .putPOJO("cardAttacked", coordinatesAttacked)
                        .put("error", "Attacker card has already attacked this turn.");
            }
        } else {
            output.addObject().put("command", action.getCommand())
                    .putPOJO("cardAttacker", coordinatesAttacker)
                    .putPOJO("cardAttacked", coordinatesAttacked)
                    .put("error", "Attacked card does not belong to the enemy.");
        }

    }

    public void cardUsesAbility(final ArrayNode output, final Table table,
                                final ActionsInput action, final Coordinates coordinatesAttacker,
                                final Coordinates coordinatesAttacked, final int turn) {

        int attackedX = coordinatesAttacked.getX();
        int attackedY = coordinatesAttacked.getY();

        final int backRowPlayer1 = 3;
        final int frontRowPlayer1 = 2;
        final int frontRowPlayer2 = 1;
        final int backRowPlayer2 = 0;

        DeckCard cardAttacker = getCardAtPositionHelper(coordinatesAttacker, table);
        DeckCard cardAttacked = getCardAtPositionHelper(coordinatesAttacked, table);

        if (cardAttacker == null || cardAttacked == null) {
            return;
        }

        if (!cardAttacker.isFrozen()) {
            if (!cardAttacker.isHasAttacked()) {
                if (cardAttacker.getName().equals("Disciple")) {
                    if ((turn == 1 && (attackedX == frontRowPlayer1 || attackedX == backRowPlayer1))
                            || (turn == 2 && (attackedX == frontRowPlayer2
                            || attackedX == backRowPlayer2))) {
                        MinionCardAbilities minionCardAbilities = new MinionCardAbilities();
                        minionCardAbilities.Gods_Plan(cardAttacker,
                                cardAttacked, table, coordinatesAttacked, coordinatesAttacker);
                    } else {
                        output.addObject().put("command", action.getCommand())
                                .putPOJO("cardAttacker", coordinatesAttacker)
                                .putPOJO("cardAttacked", coordinatesAttacked)
                                .put("error",
                                        "Attacked card does not belong to the current player.");
                    }
                } else if (cardAttacker.getName().equals("The Ripper")
                        || cardAttacker.getName().equals("Miraj")
                        || cardAttacker.getName().equals("The Cursed One")) {
                    if ((turn == 1 && (attackedX == backRowPlayer2 || attackedX == frontRowPlayer2))
                            || (turn == 2 && (attackedX == backRowPlayer1
                            || attackedX == frontRowPlayer1))) {
                        if (checkExistsTank(table, turn) && checkTank(cardAttacked)
                                || !checkExistsTank(table, turn)) {
                            MinionCardAbilities minionCardAbilities = new MinionCardAbilities();
                            minionCardAbilities.checkType(cardAttacker, cardAttacked,
                                    table, coordinatesAttacked, coordinatesAttacker);

                        } else {
                            output.addObject().put("command", action.getCommand())
                                    .putPOJO("cardAttacker", coordinatesAttacker)
                                    .putPOJO("cardAttacked", coordinatesAttacked)
                                    .put("error", "Attacked card is not of type 'Tank'.");
                        }
                    } else {
                        output.addObject().put("command", action.getCommand())
                                .putPOJO("cardAttacker", coordinatesAttacker)
                                .putPOJO("cardAttacked", coordinatesAttacked)
                                .put("error", "Attacked card does not belong to the enemy.");
                    }
                }
            } else {
                output.addObject().put("command", action.getCommand())
                        .putPOJO("cardAttacker", coordinatesAttacker)
                        .putPOJO("cardAttacked", coordinatesAttacked)
                        .put("error", "Attacker card has already attacked this turn.");
            }


        } else {
            output.addObject().put("command", action.getCommand())
                    .putPOJO("cardAttacker", coordinatesAttacker)
                    .putPOJO("cardAttacked", coordinatesAttacked)
                    .put("error", "Attacker card is frozen.");
        }

    }

    public void useAttackHero(final ArrayNode output, final Coordinates coordinatesAttacker,
                              final ActionsInput action, final Player player,
                              final Table table, final int turn) {

        DeckCard cardAttacker = getCardAtPositionHelper(coordinatesAttacker, table);

        if (cardAttacker != null && !cardAttacker.isFrozen()) {
            if (!cardAttacker.isHasAttacked()) {
                if (!checkExistsTank(table, turn)) {
                    Hero attackedHero = player.getHero();
                    attackedHero.setHealth(attackedHero.getHealth() - ((Minion) cardAttacker)
                            .getAttackDamage());

                    cardAttacker.setHasAttacked(true);

                    if (attackedHero.getHealth() <= 0) {
                        player.setHeroKilled(true);
                        if (turn == 1) {
                            output.addObject().put("gameEnded",
                                    "Player one killed the enemy hero.");
                            GameStatistics.setPlayerOneWins(GameStatistics.getPlayerOneWins() + 1);
                        } else {
                            output.addObject().put("gameEnded",
                                    "Player two killed the enemy hero.");
                            GameStatistics.setPlayerTwoWins(GameStatistics.getPlayerTwoWins() + 1);
                        }
                    }
                } else {
                    output.addObject().put("command", action.getCommand())
                            .putPOJO("cardAttacker", coordinatesAttacker)
                            .put("error", "Attacked card is not of type 'Tank'.");
                }

            } else {
                output.addObject().put("command", action.getCommand())
                        .putPOJO("cardAttacker", coordinatesAttacker)
                        .put("error", "Attacker card has already attacked this turn.");
            }
        } else {
            output.addObject().put("command", action.getCommand())
                    .putPOJO("cardAttacker", coordinatesAttacker)
                    .put("error", "Attacker card is frozen.");
        }

    }

    public void useHeroAbility(final ArrayNode output, final ActionsInput action,
                               final Table table, final int turn, final Player player) {

        Hero playerHero = player.getHero();
        int affectedRow = action.getAffectedRow();

        final int backRowPlayer1 = 3;
        final int frontRowPlayer1 = 2;
        final int frontRowPlayer2 = 1;
        final int backRowPlayer2 = 0;

        if (playerHero.getMana() <= player.getMana()) {
            if (!playerHero.isHasAttacked()) {
                if (playerHero.getName().equals("Lord Royce")
                        || playerHero.getName().equals("Empress Thorina")) {
                    if ((turn == 1 && (affectedRow == backRowPlayer2
                            || affectedRow == frontRowPlayer2))
                            || (turn == 2 && (affectedRow == backRowPlayer1
                            || affectedRow == frontRowPlayer1))) {
                        HeroCardAbilities heroCardAbilities = new HeroCardAbilities();
                        heroCardAbilities.checkType(affectedRow, table, player);
                    } else {
                        output.addObject().put("command", action.getCommand())
                                .put("affectedRow", affectedRow)
                                .put("error", "Selected row does not belong to the enemy.");
                    }
                } else if (playerHero.getName().equals("General Kocioraw")
                        || playerHero.getName().equals("King Mudface")) {
                    if ((turn == 1 && (affectedRow == frontRowPlayer1
                            || affectedRow == backRowPlayer1))
                            || (turn == 2 && (affectedRow == frontRowPlayer2
                            || affectedRow == backRowPlayer2))) {
                        HeroCardAbilities heroCardAbilities = new HeroCardAbilities();
                        heroCardAbilities.checkType(affectedRow, table, player);
                    } else {
                        output.addObject().put("command", action.getCommand())
                                .put("affectedRow", affectedRow)
                                .put("error",
                                        "Selected row does not belong to the current player.");
                    }
                }

            } else {
                output.addObject().put("command", action.getCommand())
                        .put("affectedRow", affectedRow)
                        .put("error", "Hero has already attacked this turn.");
            }

        } else {
            output.addObject().put("command", action.getCommand())
                    .put("affectedRow", affectedRow)
                    .put("error", "Not enough mana to use hero's ability.");
        }

    }

}


