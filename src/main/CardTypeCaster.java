package main;

import fileio.CardInput;

import java.util.ArrayList;

public class CardTypeCaster {

    public void cast_cards(ArrayList<CardInput> player_deck, Player player) {
        ArrayList<DeckCard> deckCards = new ArrayList<>();

        for(CardInput card_in_deck : player_deck) {
            if(card_in_deck.getName().equals("Firestorm") || card_in_deck.getName().equals("Winterfell") || card_in_deck.getName().equals("Heart Hound")) {
                Environment new_environment_card = new Environment(card_in_deck);
                deckCards.add(new_environment_card);
            }

            else {
                Minion new_minion_card = new Minion((card_in_deck));
                deckCards.add(new_minion_card);
            }

        }
        player.setDeckCards(deckCards);

    }

    public void cast_hero(CardInput hero, Player player) {
        Hero new_hero_card = new Hero(hero);
        player.setHero(new_hero_card);
    }
}
