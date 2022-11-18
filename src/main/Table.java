package main;

import java.util.ArrayList;

public final class Table {
    private ArrayList<ArrayList<DeckCard>> tableCards = new ArrayList<>();
    private ArrayList<DeckCard> frontRowPlayer1 = new ArrayList<>();
    private ArrayList<DeckCard> frontRowPlayer2 = new ArrayList<>();

    private ArrayList<DeckCard> backRowPlayer1 = new ArrayList<>();
    private ArrayList<DeckCard> backRowPlayer2 = new ArrayList<>();

    public ArrayList<ArrayList<DeckCard>> getTableCards() {
        return tableCards;
    }

    public ArrayList<DeckCard> getFrontRowPlayer1() {
        return frontRowPlayer1;
    }

    public ArrayList<DeckCard> getFrontRowPlayer2() {
        return frontRowPlayer2;
    }

    public ArrayList<DeckCard> getBackRowPlayer1() {
        return backRowPlayer1;
    }

    public ArrayList<DeckCard> getBackRowPlayer2() {
        return backRowPlayer2;
    }

}
