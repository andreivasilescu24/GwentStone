package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Player {
    private ArrayList<CardInput> handCards = new ArrayList<>();
    private ArrayList<DeckCard> deck_cards = new ArrayList<>();

    private Hero hero;
    private int index;
    private boolean turn;

    public int getIndex() {
        return index;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn() {
        return turn;
    }

    public ArrayList<CardInput> getHandCards() {
        return handCards;
    }

    public ArrayList<DeckCard> getDeck_cards() {
        return deck_cards;
    }

    public void setHandCards(ArrayList<CardInput> handCards) {
        this.handCards = handCards;
    }

    public void setDeck_cards(ArrayList<DeckCard> deck_cards) {
        this.deck_cards = deck_cards;
    }
}
