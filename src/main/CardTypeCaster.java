package main;

import fileio.CardInput;

import java.util.ArrayList;

public final class CardTypeCaster {

    /**
     *
     * @param playerDeck array of uncasted cards (player's deck cards, extracted from the input
     *                   file
     * @param player the player whose "deckCards" array will be set
     *
     * this function gets an array of deck cards and casts each card to a type (environment
     * or minion) depending on the name of the card, adds all the casted cards to a new array
     * of "DeckCard" type of cards, a general class, which is extended by each possible type of
     * card, and assigns this array to the "deckCards" field of the player. This step will help
     * because each type of card will have different fields.
     */
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

    /**
     *
     * @param hero player's hero, but uncasted
     * @param player player whose hero will be casted and assigned to its "hero" field
     */
    public void castHero(final CardInput hero, final Player player) {
        Hero newHeroCard = new Hero(hero);
        player.setHero(newHeroCard);
    }
}
