package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.era.Era;

import java.util.List;

/**
 * This class is a wrapper for the building decks, it contains a map that associate every era with the corresponding building deck, it is used to manage the building decks in the game.
 */
public class BuildingDecks {

    private final BuildingDeck buildingDecksEra1;
    private final BuildingDeck buildingDecksEra2;
    private final BuildingDeck buildingDecksEra3;

    /**
     * Constructs a new BuildingDecks object, initializing the building decks map as an empty EnumMap of Era to BuildingDeck.
     */
    public BuildingDecks() {
        this.buildingDecksEra1 = new BuildingDeck();
        this.buildingDecksEra2 = new BuildingDeck();
        this.buildingDecksEra3 = new BuildingDeck();
    }

    /**
     * This method allows to get the building deck corresponding to a specific era.
     *
     * @param era The era for which to retrieve the building deck.
     * @return the building deck corresponding to the specified era.
     */
    public BuildingDeck getBuildingDeck(Era era) {
        if (era == null)
            throw new IllegalArgumentException("Era cannot be null");

        return era.selectBuildingDeck(buildingDecksEra1, buildingDecksEra2, buildingDecksEra3);
    }

    /**
     * This method allows to add a building card to the corresponding building deck based on the era of the card.
     *
     * @throws IllegalArgumentException if the cards parameter is null.
     */
    public void addBuildingCards(List<BuildingCard> cardsEra1, List<BuildingCard> cardsEra2, List<BuildingCard> cardsEra3) throws IllegalArgumentException {
        if (cardsEra1 == null || cardsEra2 == null || cardsEra3 == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }

        this.buildingDecksEra1.addBuildingCards(cardsEra1);
        this.buildingDecksEra2.addBuildingCards(cardsEra2);
        this.buildingDecksEra3.addBuildingCards(cardsEra3);
    }

    /**
     * This method allows to set the building decks with a new composition of building cards
     *
     * @throws IllegalArgumentException if the deckComposition parameter is null or empty
     */
    public void setBuildingDeck(List<BuildingCard> cardsEra1, List<BuildingCard> cardsEra2, List<BuildingCard> cardsEra3) {
        if (cardsEra1 == null || cardsEra2 == null || cardsEra3 == null || cardsEra1.isEmpty() || cardsEra2.isEmpty() || cardsEra3.isEmpty()) {
            throw new IllegalArgumentException("Deck composition cannot be null or empty");
        }

        this.buildingDecksEra1.addBuildingCards(cardsEra1);
        this.buildingDecksEra2.addBuildingCards(cardsEra2);
        this.buildingDecksEra3.addBuildingCards(cardsEra3);

    }
}
