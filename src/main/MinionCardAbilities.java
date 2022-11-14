package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.Coordinates;

public class MinionCardAbilities {

    public void checkType(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        if(card_attacker.getName().equals("The Ripper"))
            Weak_Knees(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
            else if(card_attacker.getName().equals("Miraj"))
                Skyjack(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
                else if(card_attacker.getName().equals("The Cursed One"))
                    Shapeshift(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
    }

    public void Weak_Knees(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        ((Minion)card_attacked).setHealth(((Minion)card_attacked).getHealth() - 2);
    }

    public void Skyjack(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        int health_attacker = ((Minion)card_attacker).getHealth();
        int health_attacked = ((Minion)card_attacked).getHealth();

        ((Minion)card_attacker).setHealth(health_attacked);
        ((Minion)card_attacked).setHealth(health_attacker);
    }

    public void Shapeshift(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        int card_attacked_health = ((Minion)card_attacked).getHealth();
        int card_attacked_attack_damage = ((Minion)card_attacked).getAttackDamage();

        ((Minion)card_attacked).setHealth(card_attacked_attack_damage);
        ((Minion)card_attacked).setAttackDamage(card_attacked_health);

        for(DeckCard aux_card : table.getTable_cards().get(coordinates_attacked.getX()))
            if(((Minion)aux_card).getHealth() == 0)
                table.getTable_cards().get(coordinates_attacked.getX()).remove(aux_card);
    }

    public void Gods_Plan(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        ((Minion)card_attacked).setHealth(((Minion)card_attacked).getHealth() + 2);
    }

}
