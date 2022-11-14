package main;

import java.util.ArrayList;

public class HeroCardAbilities {

    public void checkType(int affected_row, Table table, Player player) {
        Hero player_hero = player.getHero();
        if(player_hero.getName().equals("Lord Royce"))
            Sub_Zero(affected_row, table, player);
            else if(player_hero.getName().equals("Empress Thorina"))
                Low_Blow(affected_row, table, player);
                else if(player_hero.getName().equals("King Mudface"))
                    Earth_Born(affected_row, table, player);
                    else if(player_hero.getName().equals("General Kocioraw"))
                        Blood_Thirst(affected_row, table, player);

    }

    public void Sub_Zero(int affected_row, Table table, Player player) {
        System.out.println("SUB ZERO");
        ArrayList<DeckCard> aux_row = table.getTable_cards().get(affected_row);
        int max_attack = -1;
        int max_index = -1;
        int index= 0;

        for(DeckCard aux_card : aux_row) {
            if(((Minion)aux_card).getAttackDamage() > max_attack) {
                max_attack = ((Minion)aux_card).getAttackDamage();
                max_index = index;
            }

            index++;
        }

        if(max_index != - 1)
            aux_row.get(max_index).setFrozen(true);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHas_attacked(true);

    }

    public void Low_Blow(int affected_row, Table table, Player player) {
        ArrayList<DeckCard> aux_row = table.getTable_cards().get(affected_row);
        int max_health = 0;
        int max_index = -1;
        int index= 0;

        for(DeckCard aux_card : aux_row) {
            if(((Minion)aux_card).getHealth() > max_health) {
                max_health = ((Minion)aux_card).getHealth();
                max_index = index;
            }

            index++;
        }

        if(max_index != - 1)
            aux_row.remove(max_index);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHas_attacked(true);

    }

    public void Earth_Born(int affected_row, Table table, Player player) {
        ArrayList<DeckCard> aux_row = table.getTable_cards().get(affected_row);
        for(DeckCard aux_card : aux_row)
            ((Minion)aux_card).setHealth(((Minion)aux_card).getHealth() + 1);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHas_attacked(true);
    }

    public void Blood_Thirst(int affected_row, Table table, Player player) {
        ArrayList<DeckCard> aux_row = table.getTable_cards().get(affected_row);
        for(DeckCard aux_card : aux_row)
            ((Minion)aux_card).setAttackDamage(((Minion)aux_card).getAttackDamage() + 1);

        player.setMana(player.getMana() - player.getHero().getMana());
        player.getHero().setHas_attacked(true);
    }

}
