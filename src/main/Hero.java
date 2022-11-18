package main;

import fileio.CardInput;

import java.util.ArrayList;

public final class Hero extends DeckCard {
    private int health;
    private final int startingHeroHealth = 30;

    public Hero(final CardInput deckCard) {
        super(deckCard);
        this.health = startingHeroHealth;
    }

    public Hero(final int mana, final String description, final ArrayList<String> colors,
                final String name, final boolean frozen, final boolean hasAttacked,
                final int health) {
        super(mana, description, colors, name, frozen, hasAttacked);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }
}
