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
    private boolean has_attacked;

    public void setHas_attacked(boolean has_attacked) {
        this.has_attacked = has_attacked;
    }

    public boolean isHas_attacked() {
        return has_attacked;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public DeckCard(CardInput deckCard) {
        this.mana = deckCard.getMana();
        this.description = deckCard.getDescription();
        this.colors = deckCard.getColors();
        this.name = deckCard.getName();
        this.frozen = false;
        this.has_attacked = false;
    }

    public DeckCard(int mana, String description, ArrayList<String> colors, String name, boolean frozen, boolean has_attacked) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.frozen = frozen;
        this.has_attacked = has_attacked;
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

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public void setName(String name) {
        this.name = name;
    }
}
