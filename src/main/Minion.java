package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Minion extends DeckCard {
    private int health;
    private int attackDamage;

    public Minion(CardInput deck_card) {
        super(deck_card);
        this.health = deck_card.getHealth();
        this.attackDamage = deck_card.getAttackDamage();
    }

    public Minion(int mana, String description, ArrayList<String> colors, String name, int health, int attackDamage, boolean frozen, boolean has_attacked) {
        super(mana, description, colors, name, frozen, has_attacked);
        this.health = health;
        this.attackDamage = attackDamage;
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
