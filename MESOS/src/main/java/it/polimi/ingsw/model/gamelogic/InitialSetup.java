package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.board.*;
import java.util.*;
import java.util.stream.Collectors;

public class InitialSetup {

    /**
     *
     * Creates a tribe deck for the game.
     * @param allCards      The complete list of tribe cards available in the game.
     * @param numPlayers   The number of players participating in the game.
     * @return
     */
    public static TribeDeck createTribeDeck(List<TribeCard> allCards, int numPlayers) {

        List<TribeCard> validCards = allCards.stream()
                .filter(c -> c.isAvailableForPlayerCount(numPlayers))
                .toList();

        List<TribeCard> era1 = new ArrayList<>();
        List<TribeCard> era2 = new ArrayList<>();
        List<TribeCard> era3 = new ArrayList<>();
        List<TribeCard> finalEvents = new ArrayList<>();


        for (TribeCard card : validCards) {
            if (card instanceof EventCard && ((EventCard) card).isSpecialCard()) {
                finalEvents.add(card);
            } else {
                switch (card.getEra()) {
                    case ERA_1 -> era1.add(card);
                    case ERA_2 -> era2.add(card);
                    case ERA_3 -> era3.add(card);
                }
            }
        }

        Collections.shuffle(era1);
        Collections.shuffle(era2);
        Collections.shuffle(era3);
        Collections.shuffle(finalEvents);

        List<TribeCard> deckComposition = new ArrayList<>();

        deckComposition.addAll(era1);
        deckComposition.addAll(era2);
        deckComposition.addAll(era3);
        deckComposition.addAll(finalEvents);

        return new TribeDeck(deckComposition);
    }


    /**
     *
     * Creates a building deck for the game.
     * @param allBuildings The complete list of building cards available in the game.
     * @param numPlayers   The number of players participating in the game.
     * @return
     */
    public static BuildingDeck createBuildingDeck(List<BuildingCard> allBuildings, int numPlayers) {
        Map<Era, List<BuildingCard>> fullSelection = new HashMap<>();


        for (Era era : Era.values()) {


            List<BuildingCard> availableForEra = allBuildings.stream()
                    .filter(b -> b.getEra() == era)
                    .filter(b -> b.isAvailableForPlayerCount(numPlayers))
                    .collect(Collectors.toList());

            Collections.shuffle(availableForEra);

            int limit = getBuildingLimit(numPlayers, era);

            List<BuildingCard> finalSelectionForEra = availableForEra.stream()
                    .limit(limit)
                    .collect(Collectors.toList());

            fullSelection.put(era, finalSelectionForEra);
        }

        return new BuildingDeck(fullSelection);
    }

    private static int getBuildingLimit(int numPlayers, Era era) {
        if (numPlayers <= 3) {
            if (era == Era.ERA_1) return 2;
            if (era == Era.ERA_2) return 2;
            return 4;
        } else if (numPlayers == 4) {
            if (era == Era.ERA_1) return 3;
            if (era == Era.ERA_2) return 2;
            return 4;
        } else {
            if (era == Era.ERA_1) return 3;
            if (era == Era.ERA_2) return 2;
            return 5;
        }
    }

    /**
     *
     * Creates an offer track for the game.
     * @param allBoxes    The complete list of offer boxes available in the game.
     * @param numPlayers   The number of players participating in the game.
     * @return
     */
    public static OfferTrack createOfferTrack(List<OfferBox> allBoxes, int numPlayers) {

        List<OfferBox> filtered = allBoxes.stream()
                .filter(box -> isBoxRequired(box.getBoxId(), numPlayers))
                .sorted(Comparator.comparing(OfferBox::getBoxId))
                .toList();

        ArrayList<OfferBox> boxesArrayList = new ArrayList<>(filtered);

        return new OfferTrack(boxesArrayList);
    }

    /**
     *
     * Determines whether a specific offer box is required for the game
     * based on its ID and the number of players.
     * @param id          The ID of the offer box to check.
     * @param numPlayers   The number of players participating in the game.
     * @return true if the offer box is required for the game, false otherwise.
     */
    private static boolean isBoxRequired(OFFER_BOX_ID id, int numPlayers) {
        return switch (id) {
            case A, B, C, D -> true;
            case E -> numPlayers >= 4;
            case F -> numPlayers >= 5;
            default -> false;
        };
    }


    /**
     *
     * Creates a list of players for the game by assigning each player a nickname and a unique totem,
     * and shuffling the order of the players. Additionally, it assigns a starting amount of food to each player
     * based on their position in the shuffled list.
     * @param nicknames  The list of nicknames for the players participating in the game.
     * @return A list of Player objects representing the players in the game,
     *         each with a unique totem and a starting amount of food.
     */
    public static List<Player> createPlayers(List<String> nicknames) {
        List<Player> players = new ArrayList<>();

        Totem[] availableTotems = Totem.values();

        for (int i = 0; i < nicknames.size(); i++) {
            players.add(new Player(nicknames.get(i), availableTotems[i]));
        }

        Collections.shuffle(players);

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            int startingFood = switch (i) {
                case 0      -> 2;
                case 1, 2   -> 3;
                case 3, 4   -> 4;
                default     -> 0;
            };

            p.addFood(startingFood);
        }

        return players;
    }

    /**
     * Creates the lower card row for the game.
     *
     * @param game The game object containing the tribe deck and the list of players.
     * @return A list of TribeCard objects representing the lower card row for the game.
     */
    public static List<TribeCard> createLowerCardRow(Game game) {
        TribeDeck deck = game.getBoard().getTribeDeck();
        int numPlayers = game.getPlayers().size();

        List<TribeCard> lowerRow = new ArrayList<>();
        List<TribeCard> tempEvents = new ArrayList<>(); // "Messe da parte"

        int target = numPlayers + 1;

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
     *
     * Creates the upper card row for the game.
     * @param game The game object containing the tribe deck from which to draw the cards for the upper row.
     * @return A list of TribeCard objects representing the upper card row for the game.
     *
     */
    public static List<TribeCard> createUpperCardRow(Game game) {
        TribeDeck deck = game.getBoard().getTribeDeck();
        List<TribeCard> upperRow = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            upperRow.add(deck.draw());
        }

        return upperRow;
    }



}
