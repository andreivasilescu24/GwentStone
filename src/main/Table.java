package main;

import java.util.ArrayList;

public class Table {
    private ArrayList<ArrayList<DeckCard>> table_cards= new ArrayList<>();
    private ArrayList<DeckCard> frontRow_player1 = new ArrayList<>();
    private ArrayList<DeckCard> frontRow_player2 = new ArrayList<>();

    private ArrayList<DeckCard> backRow_player1 = new ArrayList<>();
    private ArrayList<DeckCard> backRow_player2 = new ArrayList<>();

    public ArrayList<ArrayList<DeckCard>> getTable_cards() {
        return table_cards;
    }

    public void setTable_cards(ArrayList<ArrayList<DeckCard>> table_cards) {
        this.table_cards = table_cards;
    }

    public ArrayList<DeckCard> getFrontRow_player1() {
        return frontRow_player1;
    }

    public ArrayList<DeckCard> getFrontRow_player2() {
        return frontRow_player2;
    }

    public ArrayList<DeckCard> getBackRow_player1() {
        return backRow_player1;
    }

    public ArrayList<DeckCard> getBackRow_player2() {
        return backRow_player2;
    }

    public void setFrontRow_player1(ArrayList<DeckCard> frontRow_player1) {
        this.frontRow_player1 = frontRow_player1;
    }

    public void setFrontRow_player2(ArrayList<DeckCard> frontRow_player2) {
        this.frontRow_player2 = frontRow_player2;
    }

    public void setBackRow_player1(ArrayList<DeckCard> backRow_player1) {
        this.backRow_player1 = backRow_player1;
    }

    public void setBackRow_player2(ArrayList<DeckCard> backRow_player2) {
        this.backRow_player2 = backRow_player2;
    }
}
