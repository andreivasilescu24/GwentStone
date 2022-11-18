package main;

import fileio.Coordinates;

public final class MinionCardAbilities {

    public void checkType(final DeckCard card_attacker, final DeckCard card_attacked, final Table table,
                          final Coordinates coordinates_attacked, final Coordinates coordinates_attacker) {
        if (card_attacker.getName().equals("The Ripper"))
            Weak_Knees(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
        else if (card_attacker.getName().equals("Miraj"))
            Skyjack(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
        else if (card_attacker.getName().equals("The Cursed One"))
            Shapeshift(card_attacker, card_attacked, table, coordinates_attacked, coordinates_attacker);
    }

    public void Weak_Knees(final DeckCard card_attacker, final DeckCard card_attacked, final Table table,
                           final Coordinates coordinates_attacked, final Coordinates coordinates_attacker) {
        ((Minion) card_attacked).setAttackDamage(((Minion) card_attacked).getAttackDamage() - 2);
        if (((Minion) card_attacked).getAttackDamage() < 0)
            ((Minion) card_attacked).setAttackDamage(0);

        card_attacker.setHasAttacked(true);
    }

    public void Skyjack(final DeckCard card_attacker, final DeckCard card_attacked, final Table table,
                        final Coordinates coordinates_attacked, final Coordinates coordinates_attacker) {
        int health_attacker = ((Minion) card_attacker).getHealth();
        int health_attacked = ((Minion) card_attacked).getHealth();

        DeckCard new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(), card_attacked.getColors(),
                card_attacked.getName(), health_attacker, ((Minion) card_attacked).getAttackDamage(),
                card_attacked.isFrozen(), card_attacked.isHasAttacked());

        DeckCard new_card_attacker = new Minion(card_attacker.getMana(), card_attacker.getDescription(), card_attacker.getColors(),
                card_attacker.getName(), health_attacked, ((Minion) card_attacker).getAttackDamage(),
                card_attacker.isFrozen(), card_attacker.isHasAttacked());

        new_card_attacker.setHasAttacked(true);

        table.getTableCards().get(coordinates_attacked.getX()).set(coordinates_attacked.getY(), new_card_attacked);
        table.getTableCards().get(coordinates_attacker.getX()).set(coordinates_attacker.getY(), new_card_attacker);

    }

    public void Shapeshift(final DeckCard card_attacker, final DeckCard card_attacked, final Table table,
                           final Coordinates coordinates_attacked, final Coordinates coordinates_attacker) {
        int card_attacked_health = ((Minion) card_attacked).getHealth();
        int card_attacked_attack_damage = ((Minion) card_attacked).getAttackDamage();
        DeckCard new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(), card_attacked.getColors(),
                card_attacked.getName(), card_attacked_attack_damage, card_attacked_health,
                card_attacked.isFrozen(), card_attacked.isHasAttacked());

        table.getTableCards().get(coordinates_attacked.getX()).set(coordinates_attacked.getY(), new_card_attacked);
        if (((Minion) table.getTableCards().get(coordinates_attacked.getX()).get(coordinates_attacked.getY())).getHealth() == 0)
            table.getTableCards().get(coordinates_attacked.getX()).remove(coordinates_attacked.getY());

        card_attacker.setHasAttacked(true);
    }

    public void Gods_Plan(final DeckCard card_attacker, final DeckCard card_attacked, final Table table,
                          final Coordinates coordinates_attacked, final Coordinates coordinates_attacker) {
        DeckCard new_card_attacked = new Minion(card_attacked.getMana(), card_attacked.getDescription(), card_attacked.getColors(),
                card_attacked.getName(), ((Minion) card_attacked).getHealth() + 2, ((Minion) card_attacked).getAttackDamage(),
                card_attacked.isFrozen(), card_attacked.isHasAttacked());

        table.getTableCards().get(coordinates_attacked.getX()).set(coordinates_attacked.getY(), new_card_attacked);
        card_attacker.setHasAttacked(true);
    }

}
