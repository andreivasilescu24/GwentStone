package main;

import fileio.CardInput;

import java.util.ArrayList;

public final class Minion extends DeckCard {
    private int health;
    private int attackDamage;

    public Minion(final CardInput deckCard) {
        super(deckCard);
        this.health = deckCard.getHealth();
        this.attackDamage = deckCard.getAttackDamage();
    }

    public Minion(final int mana, final String description, final ArrayList<String> colors,
                  final String name, final int health, final int attackDamage, final boolean frozen,
                  final boolean hasAttacked) {
        super(mana, description, colors, name, frozen, hasAttacked);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }
}
