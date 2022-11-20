package main;

import java.util.ArrayList;

public final class HeroCardAbilities {

    public void checkType(final int affectedRow, final Table table, final Player player) {
        Hero playerHero = player.getHero();
        if (playerHero.getName().equals("Lord Royce")) {
            subZero(affectedRow, table, player);
        } else if (playerHero.getName().equals("Empress Thorina")) {
            lowBlow(affectedRow, table, player);
        } else if (playerHero.getName().equals("King Mudface")) {
            earthBorn(affectedRow, table, player);
        } else if (playerHero.getName().equals("General Kocioraw")) {
            bloodThirst(affectedRow, table, player);
        }

    }

    public void subZero(final int affectedRow, final Table table, final Player player) {
        ArrayList<DeckCard> auxRow = table.getTableCards().get(affectedRow);
        int maxAttack = -1;
        int maxIndex = -1;
        int index = 0;

        for (DeckCard auxCard : auxRow) {
            if (((Minion) auxCard).getAttackDamage() > maxAttack) {
                maxAttack = ((Minion) auxCard).getAttackDamage();
                maxIndex = index;
            }

            index++;
        }

        if (maxIndex != -1) {
            auxRow.get(maxIndex).setFrozen(true);
        }

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);

    }

    public void lowBlow(final int affectedRow, final Table table, final Player player) {
        ArrayList<DeckCard> auxRow = table.getTableCards().get(affectedRow);
        int maxHealth = 0;
        int maxIndex = -1;
        int index = 0;

        for (DeckCard auxCard : auxRow) {
            if (((Minion) auxCard).getHealth() > maxHealth) {
                maxHealth = ((Minion) auxCard).getHealth();
                maxIndex = index;
            }

            index++;
        }

        if (maxIndex != -1) {
            auxRow.remove(maxIndex);
        }

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);

    }

    public void earthBorn(final int affectedRow, final Table table, final Player player) {
        ArrayList<DeckCard> auxRow = table.getTableCards().get(affectedRow);
        for (DeckCard auxCard : auxRow) {
            ((Minion) auxCard).setHealth(((Minion) auxCard).getHealth() + 1);
        }

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);
    }

    public void bloodThirst(final int affectedRow, final Table table, final Player player) {
        ArrayList<DeckCard> auxRow = table.getTableCards().get(affectedRow);
        for (DeckCard auxCard : auxRow) {
            ((Minion) auxCard).setAttackDamage(((Minion) auxCard).getAttackDamage() + 1);
        }

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);
    }

}
