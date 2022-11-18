package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;

import java.util.ArrayList;

public class DeckCard {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    @JsonIgnore
    private boolean frozen;
    @JsonIgnore
    private boolean hasAttacked;

    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public DeckCard(final CardInput deckCard) {
        this.mana = deckCard.getMana();
        this.description = deckCard.getDescription();
        this.colors = deckCard.getColors();
        this.name = deckCard.getName();
        this.frozen = false;
        this.hasAttacked = false;
    }

    public DeckCard(final int mana, final String description, final ArrayList<String> colors,
                    final String name, final boolean frozen, final boolean hasAttacked) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.frozen = frozen;
        this.hasAttacked = hasAttacked;
    }

    public int getMana() {
        return mana;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
