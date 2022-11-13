package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;

import java.util.ArrayList;

public class EnvironmentCardAbilities {
    public void checkType(ArrayNode output, DeckCard my_env_card, Table table, Player player, ActionsInput action) {
        if(my_env_card.getName().equals("Winterfell"))
            Winterfell(my_env_card, table, player, action);
        else if(my_env_card.getName().equals("Firestorm"))
            Firestorm(my_env_card, table, player, action);
        else if(my_env_card.getName().equals("Heart Hound")) {
            int affectedRow = action.getAffectedRow();
            int mirrorRow = 3 - affectedRow;

            if(table.getTable_cards().get(mirrorRow).size() < 5)
                Heart_Hound(my_env_card, table, player, action, mirrorRow);
            else output.addObject().put("command", action.getCommand()).
                    put("handIdx", action.getHandIdx()).put("affectedRow", action.getAffectedRow())
                    .put("error", "Cannot steal enemy card since the player's row is full.");
        }
    }

    //TODO: Make this method without references (similar to Firestorm)
    public void Winterfell(DeckCard my_env_card, Table table, Player player, ActionsInput action) {
            if(action.getAffectedRow() >= 0 && action.getAffectedRow() < table.getTable_cards().size()) {
                for (DeckCard aux_card : table.getTable_cards().get(action.getAffectedRow())) {
                    aux_card.setFrozen(true);
                }
            }
             player.setMana(player.getMana() - my_env_card.getMana());
             player.getHandCards().remove(action.getHandIdx());

    }

    public void Firestorm(DeckCard my_env_card, Table table, Player player, ActionsInput action) {
        if (action.getAffectedRow() >= 0 && action.getAffectedRow() < table.getTable_cards().size()) {
            Minion aux_minion = null;
            ArrayList<DeckCard> copy_of_row = new ArrayList<>();
            for (DeckCard aux_card : table.getTable_cards().get(action.getAffectedRow())) {
                aux_minion = new Minion(aux_card.getMana(), aux_card.getDescription(),
                        aux_card.getColors(), aux_card.getName(), ((Minion) aux_card).getHealth(),
                        ((Minion) aux_card).getAttackDamage());
                aux_minion.setHealth(aux_minion.getHealth() - 1);

                copy_of_row.add(aux_minion);
            }

            table.getTable_cards().get(action.getAffectedRow()).clear();
            table.getTable_cards().get(action.getAffectedRow()).addAll(copy_of_row);

            int index = 0;
            for (DeckCard aux_card : copy_of_row) {
                if (((Minion) aux_card).getHealth() == 0) {
                    table.getTable_cards().get(action.getAffectedRow()).remove(index);
                    index--;
                }

                index++;
            }

//            table.getTable_cards().get(action.getAffectedRow()).remove(1);
//            table.getTable_cards().get(action.getAffectedRow()).remove(1);




//            for (DeckCard aux_card : table.getTable_cards().get(action.getAffectedRow())) {
//                if(((Minion)aux_card).getHealth() == 0)
//                    table.getTable_cards().get(action.getAffectedRow());
//            }
            player.setMana(player.getMana() - my_env_card.getMana());
            player.getHandCards().remove(action.getHandIdx());

        }
    }

    public void Heart_Hound(DeckCard my_env_card, Table table, Player player, ActionsInput action, int mirrorRow) {
        int max_health = -1;
        DeckCard card_to_add = null;
        int index = -1;

        for(DeckCard aux_card : table.getTable_cards().get(action.getAffectedRow())) {
            int aux_health = ((Minion)aux_card).getHealth();
            if(aux_health > max_health) {
                max_health = aux_health;
                card_to_add = new Minion(aux_card.getMana(), aux_card.getDescription(),
                        aux_card.getColors(), aux_card.getName(), ((Minion)aux_card).getHealth(),
                        ((Minion)aux_card).getAttackDamage());
                index = table.getTable_cards().get(action.getAffectedRow()).indexOf(aux_card);
            }
        }
        table.getTable_cards().get(mirrorRow).add(card_to_add);
        table.getTable_cards().get(action.getAffectedRow()).remove(index);

        player.setMana(player.getMana() - my_env_card.getMana());
        player.getHandCards().remove(action.getHandIdx());

    }

}
