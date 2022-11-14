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
        ((Minion)card_attacked).setAttackDamage(((Minion)card_attacked).getAttackDamage() - 2);
        if(((Minion)card_attacked).getAttackDamage() < 0)
            ((Minion)card_attacked).setAttackDamage(0);

        card_attacker.setHas_attacked(true);
    }

    public void Skyjack(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        int health_attacker = ((Minion)card_attacker).getHealth();
        int health_attacked = ((Minion)card_attacked).getHealth();

        DeckCard new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(), card_attacked.getColors(),
                card_attacked.getName(), health_attacker, ((Minion)card_attacked).getAttackDamage(),
                card_attacked.isFrozen(), card_attacked.isHas_attacked());

        DeckCard new_card_attacker = new Minion(card_attacker.getMana(), card_attacker.getDescription(), card_attacker.getColors(),
                card_attacker.getName(), health_attacked, ((Minion)card_attacker).getAttackDamage(),
                card_attacker.isFrozen(), card_attacker.isHas_attacked());

        new_card_attacker.setHas_attacked(true);

        table.getTable_cards().get(coordinates_attacked.getX()).set(coordinates_attacked.getY(), new_card_attacked);
        table.getTable_cards().get(coordinates_attacker.getX()).set(coordinates_attacker.getY(), new_card_attacker);


//        ((Minion)card_attacker).setHealth(health_attacked);
//        ((Minion)card_attacked).setHealth(health_attacker);


    }

    public void Shapeshift(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        int card_attacked_health = ((Minion)card_attacked).getHealth();
        int card_attacked_attack_damage = ((Minion)card_attacked).getAttackDamage();
        DeckCard new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(), card_attacked.getColors(),
                card_attacked.getName(), card_attacked_attack_damage, card_attacked_health,
                card_attacked.isFrozen(), card_attacked.isHas_attacked());

//        ((Minion)card_attacked).setHealth(card_attacked_attack_damage);
//        ((Minion)card_attacked).setAttackDamage(card_attacked_health);

        table.getTable_cards().get(coordinates_attacked.getX()).set(coordinates_attacked.getY(), new_card_attacked);
        if(((Minion)table.getTable_cards().get(coordinates_attacked.getX()).get(coordinates_attacked.getY())).getHealth() == 0)
            table.getTable_cards().get(coordinates_attacked.getX()).remove(coordinates_attacked.getY());

//        for(DeckCard aux_card : table.getTable_cards().get(coordinates_attacked.getX()))
//            if(((Minion)aux_card).getHealth() == 0)
//                table.getTable_cards().get(coordinates_attacked.getX()).remove(aux_card);

        card_attacker.setHas_attacked(true);
    }

    public void Gods_Plan(DeckCard card_attacker, DeckCard card_attacked, Table table, Coordinates coordinates_attacked, Coordinates coordinates_attacker) {
        DeckCard new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(), card_attacked.getColors(),
                card_attacked.getName(), ((Minion)card_attacked).getHealth() + 2, ((Minion)card_attacked).getAttackDamage(),
                card_attacked.isFrozen(), card_attacked.isHas_attacked());

        table.getTable_cards().get(coordinates_attacked.getX()).set(coordinates_attacked.getY(), new_card_attacked);
        card_attacker.setHas_attacked(true);
    }

}
