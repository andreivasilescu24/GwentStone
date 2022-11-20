package main;

import fileio.Coordinates;

public final class MinionCardAbilities {

    public void checkType(final DeckCard cardAttacker, final DeckCard cardAttacked,
                          final Table table, final Coordinates coordinatesAttacked,
                          final Coordinates coordinatesAttacker) {

        if (cardAttacker.getName().equals("The Ripper")) {
            weakKnees(cardAttacker, cardAttacked);
        } else if (cardAttacker.getName().equals("Miraj")) {
            skyjack(cardAttacker, cardAttacked, table, coordinatesAttacked, coordinatesAttacker);
        } else if (cardAttacker.getName().equals("The Cursed One")) {
            shapeshift(cardAttacker, cardAttacked, table, coordinatesAttacked);
        }
    }

    public void weakKnees(final DeckCard cardAttacker, final DeckCard cardAttacked) {
        ((Minion) cardAttacked).setAttackDamage(((Minion) cardAttacked).getAttackDamage() - 2);
        if (((Minion) cardAttacked).getAttackDamage() < 0) {
            ((Minion) cardAttacked).setAttackDamage(0);
        }

        cardAttacker.setHasAttacked(true);
    }

    public void skyjack(final DeckCard cardAttacker, final DeckCard cardAttacked, final Table table,
                        final Coordinates coordinatesAttacked,
                        final Coordinates coordinatesAttacker) {
        int healthAttacker = ((Minion) cardAttacker).getHealth();
        int healthAttacked = ((Minion) cardAttacked).getHealth();

        DeckCard newCardAttacked = new Minion(cardAttacked.getMana(),
                cardAttacked.getDescription(), cardAttacked.getColors(),
                cardAttacked.getName(), healthAttacker, ((Minion) cardAttacked).getAttackDamage(),
                cardAttacked.isFrozen(), cardAttacked.isHasAttacked());

        DeckCard newCardAttacker = new Minion(cardAttacker.getMana(),
                cardAttacker.getDescription(), cardAttacker.getColors(),
                cardAttacker.getName(), healthAttacked, ((Minion) cardAttacker).getAttackDamage(),
                cardAttacker.isFrozen(), cardAttacker.isHasAttacked());

        newCardAttacker.setHasAttacked(true);

        table.getTableCards().get(coordinatesAttacked.getX())
                .set(coordinatesAttacked.getY(), newCardAttacked);
        table.getTableCards().get(coordinatesAttacker.getX())
                .set(coordinatesAttacker.getY(), newCardAttacker);

    }

    public void shapeshift(final DeckCard cardAttacker, final DeckCard cardAttacked,
                           final Table table, final Coordinates coordinatesAttacked) {
        int cardAttackedHealth = ((Minion) cardAttacked).getHealth();
        int cardAttackedAttackDamage = ((Minion) cardAttacked).getAttackDamage();
        DeckCard newCardAttacked = new Minion(cardAttacked.getMana(),
                cardAttacked.getDescription(), cardAttacked.getColors(),
                cardAttacked.getName(), cardAttackedAttackDamage, cardAttackedHealth,
                cardAttacked.isFrozen(), cardAttacked.isHasAttacked());

        table.getTableCards().get(coordinatesAttacked.getX())
                .set(coordinatesAttacked.getY(), newCardAttacked);
        if (((Minion) table.getTableCards().get(coordinatesAttacked.getX())
                .get(coordinatesAttacked.getY())).getHealth() == 0) {

            table.getTableCards().get(coordinatesAttacked.getX())
                    .remove(coordinatesAttacked.getY());
        }

        cardAttacker.setHasAttacked(true);
    }

    public void godsPlan(final DeckCard cardAttacker, final DeckCard cardAttacked,
                         final Table table, final Coordinates coordinatesAttacked) {
        DeckCard newCardAttacked = new Minion(cardAttacked.getMana(),
                cardAttacked.getDescription(), cardAttacked.getColors(),
                cardAttacked.getName(), ((Minion) cardAttacked).getHealth() + 2,
                ((Minion) cardAttacked).getAttackDamage(),
                cardAttacked.isFrozen(), cardAttacked.isHasAttacked());

        table.getTableCards().get(coordinatesAttacked.getX())
                .set(coordinatesAttacked.getY(), newCardAttacked);

        cardAttacker.setHasAttacked(true);
    }

}
