package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Hero extends DeckCard {
    private int health;
    public Hero(CardInput deck_card) {
        super(deck_card);
        this.health = 30;
    }

    public Hero(int mana, String description, ArrayList<String> colors, String name, boolean frozen, boolean has_attacked, int health) {
        super(mana, description, colors, name, frozen, has_attacked);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
