package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Deck {
    private ArrayList<CardInput> deck = new ArrayList<>();

    public ArrayList<CardInput> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<CardInput> deck) {
        this.deck = deck;
    }
}
