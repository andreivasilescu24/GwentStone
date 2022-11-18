package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;

import java.util.ArrayList;

public final class EnvironmentCardAbilities {
    public void checkType(final ArrayNode output, final DeckCard my_env_card, final Table table, final Player player, final ActionsInput action) {
        if (my_env_card.getName().equals("Winterfell")) {
            Winterfell(my_env_card, table, player, action);
        } else if (my_env_card.getName().equals("Firestorm"))
            Firestorm(my_env_card, table, player, action);
        else if (my_env_card.getName().equals("Heart Hound")) {
            int affectedRow = action.getAffectedRow();
            int mirrorRow = 3 - affectedRow;

            if (table.getTableCards().get(mirrorRow).size() < 5)
                Heart_Hound(my_env_card, table, player, action, mirrorRow);
            else output.addObject().put("command", action.getCommand()).
                    put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                    .put("error", "Cannot steal enemy card since the player's row is full.");
        }
    }

    public void Winterfell(final DeckCard my_env_card, final Table table, final Player player, final ActionsInput action) {
        if (action.getAffectedRow() >= 0) {
            Minion aux_minion = null;
            ArrayList<DeckCard> copy_of_row = new ArrayList<>();
            for (DeckCard aux_card : table.getTableCards().get(action.getAffectedRow()))
                aux_card.setFrozen(true);
        }

        player.setMana(player.getMana() - my_env_card.getMana());
        player.getHandCards().remove(action.getHandIdx());

    }

    public void Firestorm(final DeckCard my_env_card, final Table table, final Player player, final ActionsInput action) {
        if (action.getAffectedRow() >= 0 && action.getAffectedRow() < table.getTableCards().size()) {
            Minion aux_minion = null;
            ArrayList<DeckCard> copy_of_row = new ArrayList<>();
            for (DeckCard aux_card : table.getTableCards().get(action.getAffectedRow())) {
                aux_minion = new Minion(aux_card.getMana(), aux_card.getDescription(),
                        aux_card.getColors(), aux_card.getName(), ((Minion) aux_card).getHealth(),
                        ((Minion) aux_card).getAttackDamage(), aux_card.isFrozen(), aux_card.isHasAttacked());

                aux_minion.setHealth(aux_minion.getHealth() - 1);

                copy_of_row.add(aux_minion);
            }

            table.getTableCards().get(action.getAffectedRow()).clear();
            table.getTableCards().get(action.getAffectedRow()).addAll(copy_of_row);

            int index = 0;
            for (DeckCard aux_card : copy_of_row) {
                if (((Minion) aux_card).getHealth() == 0) {
                    table.getTableCards().get(action.getAffectedRow()).remove(index);
                    index--;
                }

                index++;
            }

            player.setMana(player.getMana() - my_env_card.getMana());
            player.getHandCards().remove(action.getHandIdx());

        }
    }

    public void Heart_Hound(final DeckCard my_env_card, final Table table, final Player player, final ActionsInput action, final int mirrorRow) {
        int max_health = -1;
        DeckCard card_to_add = null;
        int index = -1;

        for (DeckCard aux_card : table.getTableCards().get(action.getAffectedRow())) {
            int aux_health = ((Minion) aux_card).getHealth();
            if (aux_health > max_health) {
                max_health = aux_health;
                card_to_add = new Minion(aux_card.getMana(), aux_card.getDescription(),
                        aux_card.getColors(), aux_card.getName(), ((Minion) aux_card).getHealth(),
                        ((Minion) aux_card).getAttackDamage(), aux_card.isFrozen(), aux_card.isHasAttacked());
                index = table.getTableCards().get(action.getAffectedRow()).indexOf(aux_card);
            }
        }
        table.getTableCards().get(mirrorRow).add(card_to_add);
        table.getTableCards().get(action.getAffectedRow()).remove(index);

        player.setMana(player.getMana() - my_env_card.getMana());
        player.getHandCards().remove(action.getHandIdx());

    }

}
