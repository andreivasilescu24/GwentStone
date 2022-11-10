package main;

import fileio.CardInput;

import java.util.ArrayList;

public class DeckCard {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public DeckCard(CardInput deckCard) {
        this.mana = deckCard.getMana();
        this.description = deckCard.getDescription();
        this.colors = deckCard.getColors();
        this.name = deckCard.getName();
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
