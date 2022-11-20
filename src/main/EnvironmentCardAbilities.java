package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;

import java.util.ArrayList;

public final class EnvironmentCardAbilities {
    public void checkType(final ArrayNode output, final DeckCard myEnvCard, final Table table,
                          final Player player, final ActionsInput action) {

        final int maxCardsOnRow = 5;
        final int maxRowIndex = 3;

        if (myEnvCard.getName().equals("Winterfell")) {
            winterfell(myEnvCard, table, player, action);
        } else if (myEnvCard.getName().equals("Firestorm")) {
            firestorm(myEnvCard, table, player, action);
        } else if (myEnvCard.getName().equals("Heart Hound")) {
            int affectedRow = action.getAffectedRow();
            int mirrorRow = maxRowIndex - affectedRow;

            if (table.getTableCards().get(mirrorRow).size() < maxCardsOnRow) {
                heartHound(myEnvCard, table, player, action, mirrorRow);
            } else {
                output.addObject().put("command", action.getCommand())
                        .put("handIdx", action.getHandIdx())
                        .put("affectedRow", action.getAffectedRow())
                        .put("error", "Cannot steal enemy card since the player's row is full.");
            }
        }
    }

    public void winterfell(final DeckCard myEnvCard, final Table table,
                           final Player player, final ActionsInput action) {
        if (action.getAffectedRow() >= 0) {
            for (DeckCard auxCard : table.getTableCards().get(action.getAffectedRow())) {
                auxCard.setFrozen(true);
            }
        }

        player.setMana(player.getMana() - myEnvCard.getMana());
        player.getHandCards().remove(action.getHandIdx());

    }

    public void firestorm(final DeckCard myEnvCard, final Table table,
                          final Player player, final ActionsInput action) {

        if (action.getAffectedRow() >= 0
                && action.getAffectedRow() < table.getTableCards().size()) {

            Minion auxMinion;
            ArrayList<DeckCard> copyOfRow = new ArrayList<>();
            for (DeckCard auxCard : table.getTableCards().get(action.getAffectedRow())) {
                auxMinion = new Minion(auxCard.getMana(), auxCard.getDescription(),
                        auxCard.getColors(), auxCard.getName(), ((Minion) auxCard).getHealth(),
                        ((Minion) auxCard).getAttackDamage(), auxCard.isFrozen(),
                        auxCard.isHasAttacked());

                auxMinion.setHealth(auxMinion.getHealth() - 1);

                copyOfRow.add(auxMinion);
            }

            table.getTableCards().get(action.getAffectedRow()).clear();
            table.getTableCards().get(action.getAffectedRow()).addAll(copyOfRow);

            int index = 0;
            for (DeckCard auxCard : copyOfRow) {
                if (((Minion) auxCard).getHealth() == 0) {
                    table.getTableCards().get(action.getAffectedRow()).remove(index);
                    index--;
                }

                index++;
            }

            player.setMana(player.getMana() - myEnvCard.getMana());
            player.getHandCards().remove(action.getHandIdx());

        }
    }

    public void heartHound(final DeckCard myEnvCard, final Table table, final Player player,
                           final ActionsInput action, final int mirrorRow) {

        int maxHealth = -1;
        DeckCard cardToAdd = null;
        int index = -1;

        for (DeckCard auxCard : table.getTableCards().get(action.getAffectedRow())) {
            int auxHealth = ((Minion) auxCard).getHealth();
            if (auxHealth > maxHealth) {
                maxHealth = auxHealth;
                cardToAdd = new Minion(auxCard.getMana(), auxCard.getDescription(),
                        auxCard.getColors(), auxCard.getName(), ((Minion) auxCard).getHealth(),
                        ((Minion) auxCard).getAttackDamage(), auxCard.isFrozen(),
                        auxCard.isHasAttacked());

                index = table.getTableCards().get(action.getAffectedRow()).indexOf(auxCard);
            }
        }
        table.getTableCards().get(mirrorRow).add(cardToAdd);
        table.getTableCards().get(action.getAffectedRow()).remove(index);

        player.setMana(player.getMana() - myEnvCard.getMana());
        player.getHandCards().remove(action.getHandIdx());

    }

}
