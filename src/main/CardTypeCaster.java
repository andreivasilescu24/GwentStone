package main;

import fileio.CardInput;

import java.util.ArrayList;

public class CardTypeCaster {

    public void castCards(final ArrayList<CardInput> playerDeck, final Player player) {
        ArrayList<DeckCard> deckCards = new ArrayList<>();

        for (CardInput cardInDeck : playerDeck) {
            if (cardInDeck.getName().equals("Firestorm")
                    || cardInDeck.getName().equals("Winterfell")
                    || cardInDeck.getName().equals("Heart Hound")) {
                Environment newEnvironmentCard = new Environment(cardInDeck);
                deckCards.add(newEnvironmentCard);
            } else {
                Minion newMinionCard = new Minion((cardInDeck));
                deckCards.add(newMinionCard);
            }

        }
        player.setDeckCards(deckCards);

    }

    public void castHero(final CardInput hero, final Player player) {
        Hero newHeroCard = new Hero(hero);
        player.setHero(newHeroCard);
    }
}
