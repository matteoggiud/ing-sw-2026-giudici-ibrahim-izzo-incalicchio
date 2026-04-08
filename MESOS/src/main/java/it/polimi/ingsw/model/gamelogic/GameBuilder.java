package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.ruleset.IGameRuleSet;
import it.polimi.ingsw.model.ruleset.StandardRuleSet;

import java.util.*;

//TODO: Valute if we want to make this class a non static, because this class has a lot of side effects

public class GameBuilder {

    /**
     *
     *  Initializes Tribe Deck.
     * @param allCards all Tribe Cards
     * @param game     The game object containing the ruleset and the list of players.
     */
    public static void initializeTribeDeck (List<TribeCard> allCards, Game game) {
        if (allCards == null || game == null) {
            throw new IllegalArgumentException("allCards and game cannot be null");
        }

        IGameRuleSet ruleSet = game.getRuleSet();


        if (game.getNumberOfPlayers() < StandardRuleSet.MIN_PLAYERS || game.getNumberOfPlayers() > StandardRuleSet.MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Invalid player count: " + game.getNumberOfPlayers() +
                            " (expected between " + StandardRuleSet.MIN_PLAYERS + " and " + StandardRuleSet.MAX_PLAYERS + ")"
            );
        }

        List<TribeCard> validCards = new ArrayList<> (allCards.stream()
                .filter(card -> card.isAvailableForPlayerCount(game.getNumberOfPlayers()))
                .toList()
        );

        Collections.shuffle(validCards);

        List<TribeCard> deckComposition = new ArrayList<>();
        for (Era era : Era.values()) {
            validCards.stream().filter(b -> b.getEra().equals(era)).forEach(deckComposition::add);

        }
        game.getTribeDeck().addTribesCards(deckComposition);

    }

    public static void initializeBuildingDecks(List<BuildingCard> allBuildings, Game game) {
        if (allBuildings == null || game == null) {
            throw new IllegalArgumentException("allBuildings and game cannot be null");
        }

        IGameRuleSet ruleSet = game.getRuleSet();

        // Filter buildings by player count first to reduce the number of cards we need to process for each era.
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
        game.getBuildingDecks().addBuildingCards(deckComposition);

    }


    /**
     * Creates the offer track for the game using the provided rule set.
     *
     * @param numPlayers the number of players participating in the game.
     * @param ruleSet    the game rule set used to generate the offer boxes.
     * @return an OfferTrack initialized according to the ruleset.
     */
    public static OfferTrack createOfferTrack(int numPlayers, IGameRuleSet ruleSet) {
        if (ruleSet == null) {
            throw new IllegalArgumentException("ruleSet cannot be null");
        }

        ArrayList<OfferBox> boxes = ruleSet.createOfferBoxes(numPlayers);
        return new OfferTrack(boxes);
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
    public static List<Player> createPlayers(List<String> nicknames, IGameRuleSet ruleSet) {
        if (nicknames == null || ruleSet == null) {
            throw new IllegalArgumentException("Nicknames and rule set cannot be null");
        }

        int numPlayers = nicknames.size();
        if (numPlayers < StandardRuleSet.MIN_PLAYERS || numPlayers >  StandardRuleSet.MAX_PLAYERS) {
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
    }


    /**
     * Creates the lower card row for the game.
     *
     * @param game    The game object containing the tribe deck and the list of players.
     * @param ruleSet The game rule set that defines the size of the lower card row based on the number of players.
     * @return A list of TribeCard objects representing the lower card row for the game.
     */

    public static List<TribeCard> createLowerCardRow(Game game, IGameRuleSet ruleSet) {
        if (game == null || ruleSet == null) {
            throw new IllegalArgumentException("game and ruleSet cannot be null");
        }

        TribeDeck deck = game.getTribeDeck();
        int numPlayers = game.getPlayers().size();
        int target = ruleSet.getLowerRowSize(numPlayers);

        List<TribeCard> lowerRow = new ArrayList<>();
        List<TribeCard> tempEvents = new ArrayList<>();

        // Fill lower row ignoring EventCards, then restore them on top preserving order.
        while (lowerRow.size() < target) {
            TribeCard card = deck.draw();
            if (card instanceof EventCard) {
                tempEvents.add(card);
            } else {
                lowerRow.add(card);
            }
        }

        for (int i = tempEvents.size() - 1; i >= 0; i--) {
            deck.addCardToTop(tempEvents.get(i));
        }

        return lowerRow;
    }


    /**
     * Creates the upper card row for the game.
     *
     * @param game    The game object containing the tribe deck and the list of players.
     * @param ruleSet The game rule set that defines the size of the upper card row based on the number of players.
     * @return A list of TribeCard objects representing the upper card row for the game.
     */

    public static List<TribeCard> createUpperCardRow(Game game, IGameRuleSet ruleSet) {
        if (game == null || ruleSet == null) {
            throw new IllegalArgumentException("game and ruleSet cannot be null");
        }

        TribeDeck deck = game.getTribeDeck();
        int numPlayers = game.getPlayers().size();
        int target = ruleSet.getUpperRowSize(numPlayers);

        List<TribeCard> upperRow = new ArrayList<>(target);

        // Draw exactly the number of cards required by the ruleset.
        for (int i = 0; i < target; i++) {
            upperRow.add(deck.draw());
        }

        return upperRow;
    }


}
