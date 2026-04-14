package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.ruleset.IGameRuleSet;

import java.util.*;


public class GameBuilder {

    private final Game game;

    /**
     * Constructs a GameBuilder with the specified Game instance.
     *
     * @param game the Game instance to be used for building the game. Must not be null.
     */
    public GameBuilder(Game game) {
        if (game == null) throw new IllegalArgumentException("Game cannot be null");
        this.game = game;
    }

    public void prepareGame() {
        List<TribeCard> allTribeCards = new ArrayList<>(); //TODO: get all tribe cards from db
        List<BuildingCard> allBuildingCards = new ArrayList<>();

        game.setCurrentEra(Era.ERA_1);

        // Initialize the tribe deck, building decks, offer track, turn order box, and fill the card rows according to the game rules and the available cards.
        initializeTribeDeck(allTribeCards);
        initializeBuildingDecks(allBuildingCards);
        initializeOfferTrack();
        initializeTurnOrderBox();
        initializePlayersTotems();
        initialFillCardRows();

    }

    /**
     * Initializes Tribe Deck.
     *
     * @param allCards all Tribe Cards
     */
    //TODO: implement DataRepository to get all the possible cards from db
    private void initializeTribeDeck(List<TribeCard> allCards) {
        if (allCards == null) {
            throw new IllegalArgumentException("allCards and game cannot be null");
        }

        int numOfPlayers = game.getNumberOfPlayers();

        // Filter tribe cards by player count.
        List<TribeCard> validCards = new ArrayList<>(allCards.stream()
                .filter(card -> card.isAvailableForPlayerCount(numOfPlayers))
                .toList()
        );


        // Define the composition of the tribe deck.
        List<TribeCard> era_I = new ArrayList<>();
        List<TribeCard> era_II = new ArrayList<>();
        List<TribeCard> era_III = new ArrayList<>();
        List<TribeCard> finalEvents = new ArrayList<>();

        // Divide the valid tribe cards into their respective eras and separate final event cards from the rest of era 3 cards.
        for (TribeCard card : validCards) {
            if (card.getEra() == Era.ERA_3 && card instanceof EventCard eventCard && eventCard.isFinalEvent()) {
                finalEvents.add(eventCard);
            } else {
                switch (card.getEra()) {
                    case Era.ERA_1 -> era_I.add(card);
                    case Era.ERA_2 -> era_II.add(card);
                    case Era.ERA_3 -> era_III.add(card);
                }
            }
        }

        // Shuffle the cards within each era to ensure a random distribution of cards in the tribe deck.
        Collections.shuffle(era_I);
        Collections.shuffle(era_II);
        Collections.shuffle(era_III);
        Collections.shuffle(finalEvents);

        // Let's compose the tribe deck by adding the cards from each era in the correct order.
        List<TribeCard> finalDeck = new ArrayList<>();
        finalDeck.addAll(era_I);
        finalDeck.addAll(era_II);
        finalDeck.addAll(era_III);
        finalDeck.addAll(finalEvents);

        // Set the composed tribe deck in the game.
        game.getTribeDeck().setTribeDeck(finalDeck);
    }

    /**
     * Initializes the building decks.
     *
     * @param allBuildings a list of all available building cards.
     */
    private void initializeBuildingDecks(List<BuildingCard> allBuildings) {
        if (allBuildings == null) {
            throw new IllegalArgumentException("allBuildings and game cannot be null");
        }

        IGameRuleSet ruleSet = game.getRuleSet();

        // Filter buildings by player count.
        List<BuildingCard> validBuildings = new ArrayList<>(allBuildings.stream()
                .filter(b -> b.isAvailableForPlayerCount(game.getNumberOfPlayers()))
                .toList());

        Collections.shuffle(validBuildings);

        // Gets the limit for each era and fills the building decks accordingly, ensuring we don't exceed the limit for any era.
        List<BuildingCard> deckComposition = new ArrayList<>();
        for (Era era : Era.values()) {
            int limit = ruleSet.getBuildingLimit(era, game.getNumberOfPlayers());
            validBuildings.stream().filter(b -> b.getEra().equals(era)).limit(limit).forEach(deckComposition::add);
        }

        // Add the selected building cards to the corresponding building decks in the game.
        game.getBuildingDecks().setBuildingDeck(deckComposition);

    }


    /**
     * Initializes the offer track for the game using the provided rule set.
     */
    public void initializeOfferTrack() {
        IGameRuleSet ruleSet = game.getRuleSet();

        // Create the offer boxes for the offer track based on the number of players in the game using the rule set.
        List<OfferBox> offerBoxes = ruleSet.createOfferBoxes(game.getNumberOfPlayers());

        // Initialize the offer track in the game with the created offer boxes.
        game.getBoard().getOfferTrack().initializeOfferTrack(offerBoxes);
    }

    /**
     * Initializes the turn order box for the game using the provided rule set.
     */
    private void initializeTurnOrderBox() {
        IGameRuleSet ruleSet = game.getRuleSet();

        // Create the turn order slots for the turn order box based on the number of players in the game using the rule set.
        List<TurnOrderSlot> turnOrderSlots = ruleSet.createTurnOrderSlots(game.getNumberOfPlayers());

        // Initialize the turn order box in the game with the created turn order slots.
        game.getBoard().getTurnOrderBox().initializeTurnOrderBox(turnOrderSlots);

        // Place the players in the turn order box according to the game rules and the created turn order slots.
        initializePlacedPlayers(game.getBoard().getTurnOrderBox());
    }

    /**
     * Initializes the players' totems by assigning each player a unique totem from the available totems.
     */
    private void initializePlayersTotems() {
        List<Player> players = game.getPlayers().getShuffledPlayers();
        Totem[] totems = Totem.values();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.setTotem(totems[i]);
        }
    }


    private void initializePlacedPlayers(TurnOrderBox turnOrderBox) {
        List<Player> players = game.getPlayers().getShuffledPlayers();

        for (Player player : players) {
            // Place the player in the first free slot.
            TurnOrderSlot slot = turnOrderBox.getFirstFreeSlot();
            slot.placePlayer(player);

            // Give the player the initial amount of food according to the game rules and the player's position in the turn order.
            int initialPlayerFood = game.getRuleSet().getInitialFood(turnOrderBox.getIndexBySlot(slot));
            player.addFood(initialPlayerFood);
        }
    }



    /**
     * Creates the list of players for the game based on the provided nicknames and rule set.
     *
     * @param nicknames a list of player nicknames to be used for creating Player objects.
     *                  The size of this list determines the number of players in the game.
     * @param ruleSet   the game rule set used to validate the number of players
     *                  and determine initial food amounts.
     * @return a list of Player objects initialized with the provided nicknames,
     * assigned Totems, and starting food according to the rule set.
     */
    /*public static List<Player> createPlayers(List<String> nicknames, IGameRuleSet ruleSet) {
        if (nicknames == null || ruleSet == null) {
            throw new IllegalArgumentException("Nicknames and rule set cannot be null");
        }

        int numPlayers = nicknames.size();
        if (numPlayers < StandardRuleSet.MIN_PLAYERS || numPlayers > StandardRuleSet.MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Number of players not valid: " + numPlayers +
                            " (has to be between " + StandardRuleSet.MIN_PLAYERS + " and " + StandardRuleSet.MAX_PLAYERS + ")"
            );
        }

        Totem[] availableTotems = Totem.values();
        if (numPlayers > availableTotems.length) {
            throw new IllegalStateException("Insufficient Totems for " + numPlayers + " players");
        }

        List<Player> players = new ArrayList<>(numPlayers);

        for (int i = 0; i < numPlayers; i++) {
            String nickname = nicknames.get(i);
            if (nickname == null || nickname.isBlank()) {
                throw new IllegalArgumentException("Not valid nicknames in position " + i);
            }
            players.add(new Player(nickname, availableTotems[i]));
        }

        Collections.shuffle(players);

        for (int i = 0; i < players.size(); i++) {
            players.get(i).addFood(ruleSet.getInitialFood(i));
        }

        return players;
    } */

    /**
     * Fills the lower and upper card row for the game according to the rules defined in the rule set.
     */
    private void initialFillCardRows() {
        TribeDeck tribeDeck = game.getTribeDeck();
        int numPlayers = game.getNumberOfPlayers();
        int lowerRowTarget = game.getRuleSet().getLowerRowSize(numPlayers);
        int upperRowTarget = game.getRuleSet().getUpperRowSize(numPlayers);
        CardRow lowerRow = game.getBoard().getLowerRow();
        CardRow upperRow = game.getBoard().getUpperRow();

        // Clear the card rows before filling them to ensure they start empty.
        lowerRow.clearRow();
        upperRow.clearRow();


        while (lowerRow.countRow() < lowerRowTarget) {
            TribeCard drawnCard = tribeDeck.drawCard();
            if (drawnCard instanceof EventCard) {
                upperRow.addCard(drawnCard);
            } else {
                lowerRow.addCard(drawnCard);
            }
        }

        while (upperRow.countRow() < upperRowTarget) {
            upperRow.addCard(tribeDeck.drawCard());
        }

        BuildingDeck initialBuildingDeck = game.getBuildingDecks().getBuildingDeck(Era.ERA_1);
        upperRow.addBuildingRowFrom(initialBuildingDeck);
    }


}
