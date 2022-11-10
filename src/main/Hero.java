package main;

import fileio.CardInput;

public class Hero extends DeckCard {
    private int health;
    public Hero(CardInput deck_card) {
        super(deck_card);
        health = 30;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
