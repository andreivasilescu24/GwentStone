package main;

import java.util.ArrayList;

public final class HeroCardAbilities {

    public void checkType(final int affected_row, final Table table, final Player player) {
        Hero player_hero = player.getHero();
        if (player_hero.getName().equals("Lord Royce"))
            Sub_Zero(affected_row, table, player);
        else if (player_hero.getName().equals("Empress Thorina"))
            Low_Blow(affected_row, table, player);
        else if (player_hero.getName().equals("King Mudface"))
            Earth_Born(affected_row, table, player);
        else if (player_hero.getName().equals("General Kocioraw"))
            Blood_Thirst(affected_row, table, player);

    }

    public void Sub_Zero(final int affected_row, final Table table, final Player player) {
        ArrayList<DeckCard> aux_row = table.getTableCards().get(affected_row);
        int max_attack = -1;
        int max_index = -1;
        int index = 0;

        for (DeckCard aux_card : aux_row) {
            if (((Minion) aux_card).getAttackDamage() > max_attack) {
                max_attack = ((Minion) aux_card).getAttackDamage();
                max_index = index;
            }

            index++;
        }

        if (max_index != -1)
            aux_row.get(max_index).setFrozen(true);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);

    }

    public void Low_Blow(final int affected_row, final Table table, final Player player) {
        ArrayList<DeckCard> aux_row = table.getTableCards().get(affected_row);
        int max_health = 0;
        int max_index = -1;
        int index = 0;

        for (DeckCard aux_card : aux_row) {
            if (((Minion) aux_card).getHealth() > max_health) {
                max_health = ((Minion) aux_card).getHealth();
                max_index = index;
            }

            index++;
        }

        if (max_index != -1)
            aux_row.remove(max_index);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);

    }

    public void Earth_Born(final int affected_row, final Table table, final Player player) {
        ArrayList<DeckCard> aux_row = table.getTableCards().get(affected_row);
        for (DeckCard aux_card : aux_row)
            ((Minion) aux_card).setHealth(((Minion) aux_card).getHealth() + 1);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);
    }

    public void Blood_Thirst(final int affected_row, final Table table, final Player player) {
        ArrayList<DeckCard> aux_row = table.getTableCards().get(affected_row);
        for (DeckCard aux_card : aux_row)
            ((Minion) aux_card).setAttackDamage(((Minion) aux_card).getAttackDamage() + 1);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHasAttacked(true);
    }

}
