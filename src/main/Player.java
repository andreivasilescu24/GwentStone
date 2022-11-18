package main;

import java.util.ArrayList;

public final class Player {
    private ArrayList<DeckCard> handCards = new ArrayList<>();
    private ArrayList<DeckCard> deckCards = new ArrayList<>();

    private boolean isHeroKilled;
    private int round;
    private Hero hero;

    public boolean isHeroKilled() {
        return isHeroKilled;
    }

    public void setHeroKilled(final boolean heroKilled) {
        isHeroKilled = heroKilled;
    }

    //    private int index;
    private boolean turn;

    public void setRound(final int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    private int mana;

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

//    public void setIndex(int index) {
//        this.index = index;
//    }

    public void setTurn(final boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn() {
        return turn;
    }

    public ArrayList<DeckCard> getHandCards() {
        return handCards;
    }

    public ArrayList<DeckCard> getDeckCards() {
        return deckCards;
    }

    public void setHandCards(final ArrayList<DeckCard> handCards) {
        this.handCards = handCards;
    }

    public void setDeckCards(final ArrayList<DeckCard> deckCards) {
        this.deckCards = deckCards;
    }
}
