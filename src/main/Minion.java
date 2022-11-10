package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Minion extends DeckCard {
    private int health;
    private int attackDamage;

    public Minion(CardInput deck_card) {
        super(deck_card);
        health = deck_card.getHealth();
        attackDamage = deck_card.getAttackDamage();
    }

    public int getHealth() {
        return health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
}
