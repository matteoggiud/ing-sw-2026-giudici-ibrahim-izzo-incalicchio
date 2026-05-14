package it.polimi.ingsw.server.model.gamelogic;

import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.*;
import it.polimi.ingsw.server.model.deck.BuildingDecks;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.deck.BuildingDeck;
import it.polimi.ingsw.server.model.deck.TribeDeck;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import it.polimi.ingsw.server.model.era.Era3;
import it.polimi.ingsw.server.model.observer.dto.BuildingCardDTO;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.CharacterCardDTO;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.model.observer.dto.OfferBoxDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.TurnOrderSlotDTO;
import it.polimi.ingsw.server.model.ruleset.IGameRuleSet;
import it.polimi.ingsw.server.model.states.WaitingForReconnectState;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class GameBuilder {

    private final Game game;

    /**
     * Constructs a GameBuilder with the specified Game instance.
     *
     * @param game the Game instance to be used for building the game. Must not be
     *             null.
     */
    public GameBuilder(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Game cannot be null");
        this.game = game;
    }

    public void prepareGame() {
        DataDepository db = new DataDepository();
        List<TribeCard> allTribeCards;
        List<BuildingCard> allBuildingCards;

        // We use the try-catch to avoid adding on every method that use these parameter
        // a throw SQLException
        try {
            allTribeCards = db.getAllTribeCards();
            allBuildingCards = db.getBuildingCards();
        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in GameBuilder: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load cards from the database");
        }

        game.setCurrentEra(new Era1());

        // Initialize the tribe deck, building decks, offer track, turn order box, and
        // fill the card rows according to the game rules and the available cards.
        initializePlayerStats();
        initializeTribeDeck(allTribeCards);
        initializeBuildingDecks(allBuildingCards);
        initializeOfferTrack();
        initializeTurnOrderBox();
        initializePlayersTotems();
        initialFillCardRows();

    }

    private void initializePlayerStats() {
        for (Player player : game.getPlayers().getPlayers()) {
            player.reset();
        }
    }

    /**
     * Initializes Tribe Deck.
     *
     * @param allCards all Tribe Cards
     */
    private void initializeTribeDeck(List<TribeCard> allCards) {
        if (allCards == null) {
            throw new IllegalArgumentException("allCards and game cannot be null");
        }

        int numOfPlayers = game.getNumberOfPlayers();

        // Filter tribe cards by player count.
        List<TribeCard> validCards = new ArrayList<>(
                allCards.stream().filter(card -> card.isAvailableForPlayerCount(numOfPlayers)).toList());

        // Define the composition of the tribe deck.
        List<TribeCard> era_I = new ArrayList<>();
        List<TribeCard> era_II = new ArrayList<>();
        List<TribeCard> era_III = new ArrayList<>();
        List<TribeCard> finalEvents = new ArrayList<>();

        // Divide the valid tribe cards into their respective eras and separate final
        // event cards from the rest of era 3 cards.
        for (TribeCard card : validCards) {
            card.selfSort(era_I, era_II, era_III, finalEvents);
        }

        // Shuffle the cards within each era to ensure a random distribution of cards in
        // the tribe deck.
        Collections.shuffle(era_I);
        Collections.shuffle(era_II);
        Collections.shuffle(era_III);
        Collections.shuffle(finalEvents);

        // Let's compose the tribe deck by adding the cards from each era in the correct
        // order.
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
        List<BuildingCard> validBuildings = new ArrayList<>(
                allBuildings.stream().filter(b -> b.isAvailableForPlayerCount(game.getNumberOfPlayers())).toList());

        Collections.shuffle(validBuildings);

        List<BuildingCard> deckCompositionEra1 = new ArrayList<>();
        List<BuildingCard> deckCompositionEra2 = new ArrayList<>();
        List<BuildingCard> deckCompositionEra3 = new ArrayList<>();

        // Divide the valid building cards into their respective eras according to the
        // rules defined in the game rule set.
        for (BuildingCard card : validBuildings) {
            card.getEra().selfAdd(deckCompositionEra1, deckCompositionEra2, deckCompositionEra3, card,
                    game.getNumberOfPlayers());
        }

        // Add the selected building cards to the corresponding building decks in the
        // game.
        game.getBuildingDecks().setBuildingDeck(deckCompositionEra1, deckCompositionEra2, deckCompositionEra3);

    }

    /**
     * Initializes the offer track for the game using the provided rule set.
     */
    public void initializeOfferTrack() {
        IGameRuleSet ruleSet = game.getRuleSet();

        // Create the offer boxes for the offer track based on the number of players in
        // the game using the rule set.
        List<OfferBox> offerBoxes = ruleSet.createOfferBoxes(game.getNumberOfPlayers());

        // Initialize the offer track in the game with the created offer boxes.
        game.getBoard().getOfferTrack().initializeOfferTrack(offerBoxes);
    }

    /**
     * Initializes the turn order box for the game using the provided rule set.
     */
    private void initializeTurnOrderBox() {
        IGameRuleSet ruleSet = game.getRuleSet();

        // Create the turn order slots for the turn order box based on the number of
        // players in the game using the rule set.
        List<TurnOrderSlot> turnOrderSlots = ruleSet.createTurnOrderSlots(game.getNumberOfPlayers());

        // Initialize the turn order box in the game with the created turn order slots.
        game.getBoard().getTurnOrderBox().initializeTurnOrderBox(turnOrderSlots);

        // Place the players in the turn order box according to the game rules and the
        // created turn order slots.
        initializePlacedPlayers(game.getBoard().getTurnOrderBox());
    }

    /**
     * Initializes the players' totems by assigning each player a unique totem from
     * the available totems.
     */
    private void initializePlayersTotems() {
        Random random = new Random();
        List<Player> players = game.getPlayers().getShuffledPlayers();
        List<Totem> totems = new ArrayList<>(Arrays.asList(Totem.values()));
        Collections.shuffle(totems, random);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.setTotem(totems.get(i));
        }
    }

    private void initializePlacedPlayers(TurnOrderBox turnOrderBox) {
        Random random = new Random();
        List<Player> players = game.getPlayers().getPlayers();
        Collections.shuffle(players, random);

        for (Player player : players) {
            // Place the player in the first free slot.
            TurnOrderSlot slot = turnOrderBox.getFirstFreeSlot();
            slot.placePlayer(player);

            // Give the player the initial amount of food according to the game rules and
            // the player's position in the turn order.
            int initialPlayerFood = game.getRuleSet().getInitialFood(turnOrderBox.getIndexBySlot(slot));
            player.addFood(initialPlayerFood);
        }
    }

    /**
     * Creates the list of players for the game based on the provided nicknames and
     * rule set.
     *
     * @param nicknames a list of player nicknames to be used for creating Player
     *                  objects.
     *                  The size of this list determines the number of players in
     *                  the game.
     * @param ruleSet   the game rule set used to validate the number of players
     *                  and determine initial food amounts.
     * @return a list of Player objects initialized with the provided nicknames,
     *         assigned Totems, and starting food according to the rule set.
     */
    /*
     * public static List<Player> createPlayers(List<String> nicknames, IGameRuleSet
     * ruleSet) {
     * if (nicknames == null || ruleSet == null) {
     * throw new IllegalArgumentException("Nicknames and rule set cannot be null");
     * }
     * 
     * int numPlayers = nicknames.size();
     * if (numPlayers < StandardRuleSet.MIN_PLAYERS || numPlayers >
     * StandardRuleSet.MAX_PLAYERS) {
     * throw new IllegalArgumentException(
     * "Number of players not valid: " + numPlayers +
     * " (has to be between " + StandardRuleSet.MIN_PLAYERS + " and " +
     * StandardRuleSet.MAX_PLAYERS + ")"
     * );
     * }
     * 
     * Totem[] availableTotems = Totem.values();
     * if (numPlayers > availableTotems.length) {
     * throw new IllegalStateException("Insufficient Totems for " + numPlayers +
     * " players");
     * }
     * 
     * List<Player> players = new ArrayList<>(numPlayers);
     * 
     * for (int i = 0; i < numPlayers; i++) {
     * String nickname = nicknames.get(i);
     * if (nickname == null || nickname.isBlank()) {
     * throw new IllegalArgumentException("Not valid nicknames in position " + i);
     * }
     * players.add(new Player(nickname, availableTotems[i]));
     * }
     * 
     * Collections.shuffle(players);
     * 
     * for (int i = 0; i < players.size(); i++) {
     * players.get(i).addFood(ruleSet.getInitialFood(i));
     * }
     * 
     * return players;
     * }
     */

    /**
     * Fills the lower and upper card row for the game according to the rules
     * defined in the rule set.
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
            if (drawnCard.goesInUpperRow()) {
                upperRow.addCard(drawnCard);
            } else {
                lowerRow.addCard(drawnCard);
            }
        }

        while (upperRow.countRow() < upperRowTarget) {
            upperRow.addCard(tribeDeck.drawCard());
        }

        BuildingDeck initialBuildingDeck = game.getBuildingDecks().getBuildingDeck(new Era1());
        upperRow.addBuildingRowFrom(initialBuildingDeck);
    }

    public static Game reconstructGame(GameSnapshotDTO snapshot, DataDepository depository) {

        // 1. RICOSTRUISCI I PLAYERS
        Players playersObj = new Players();
        for (PlayerInfoDTO pDto : snapshot.players()) {
            Player p = new Player(pDto.nickname());
            p.disconnect(); // I players ricostruiti partono tutti disconnected, poi quando si riconnettono
                            // vengono messi online
            p.setFood(pDto.food());
            p.setPp(pDto.pp());
            p.setTotem(Totem.valueOf(pDto.totem()));

            // Ricrea il deck personale
            // Character cards
            List<CharacterCardDTO> characterCards = pDto.deck().characterCards();
            if (characterCards != null) {
                for (CharacterCardDTO cDto : characterCards) {
                    int cardId = cDto.cardId();
                    CharacterCard card = depository.getCharacterCardById(cardId);
                    if (card == null) {
                        System.err.println("[RECOVER] Character card not found for id: " + cardId + " (player: "
                                + pDto.nickname() + ")");
                        continue;
                    }
                    // Add card in player deck and call onAddToPlayerDeck to update player deck
                    // stats
                    p.getPlayerDeck().addCard(card);
                    card.onAddToPlayerDeck(p.getPlayerDeck());
                }
            }

            // Building cards
            List<BuildingCardDTO> buildingCards = pDto.deck().buildingCards();
            for (BuildingCardDTO bDto : buildingCards) {
                int cardId = bDto.cardId();
                BuildingCard card = depository.getBuildingCardById(cardId);
                if (card == null) {
                    System.err.println("[RECOVER] Building card not found for id: " + cardId + " (player: "
                            + pDto.nickname() + ")");
                    continue;
                }
                p.getPlayerDeck().addCard(card);
            }

            playersObj.addPlayer(p);
        }

        // 2. RICOSTRUISCI LA BOARD E L'OFFER BOX
        Board board = new Board();
        BoardDTO boardDTO = snapshot.board();

        // Upper character cards
        List<CharacterCardDTO> upperChars = boardDTO.upperCharacterCards();
        for (CharacterCardDTO cDto : upperChars) {
            int cardId = cDto.cardId();
            CharacterCard card = depository.getCharacterCardById(cardId);
            if (card == null) {
                System.err.println("[RECOVER] Upper character card not found for id: " + cardId);
                continue;
            }
            board.getUpperRow().addCard(card);
        }

        // Upper event cards
        List<EventCardDTO> upperEvents = boardDTO.upperEventCards();
        for (EventCardDTO eDto : upperEvents) {
            int cardId = eDto.cardId();
            EventCard card = depository.getEventCardById(cardId);
            if (card == null) {
                System.err.println("[RECOVER] Upper event card not found for id: " + cardId);
                continue;
            }
            board.getUpperRow().addCard(card);
        }

        // Upper building cards
        List<BuildingCardDTO> upperBuildings = boardDTO.upperBuildingCards();
        for (BuildingCardDTO bDto : upperBuildings) {
            int cardId = bDto.cardId();
            BuildingCard card = depository.getBuildingCardById(cardId);
            if (card == null) {
                System.err.println("[RECOVER] Upper building card not found for id: " + cardId);
                continue;
            }
            board.getUpperRow().getBuildingRow().add(card);
        }

        // Lower character cards
        List<CharacterCardDTO> lowerChars = boardDTO.lowerCharacterCards();
        for (CharacterCardDTO cDto : lowerChars) {
            int cardId = cDto.cardId();
            CharacterCard card = depository.getCharacterCardById(cardId);
            if (card == null) {
                System.err.println("[RECOVER] Lower character card not found for id: " + cardId);
                continue;
            }
            board.getLowerRow().addCard(card);
        }

        // Lower event cards
        List<EventCardDTO> lowerEvents = boardDTO.lowerEventCards();
        for (EventCardDTO eDto : lowerEvents) {
            int cardId = eDto.cardId();
            EventCard card = depository.getEventCardById(cardId);
            if (card == null) {
                System.err.println("[RECOVER] Lower event card not found for id: " + cardId);
                continue;
            }
            board.getLowerRow().addCard(card);

        }

        // Lower building cards
        List<BuildingCardDTO> lowerBuildings = boardDTO.lowerBuildingCards();
        for (BuildingCardDTO bDto : lowerBuildings) {
            int cardId = bDto.cardId();
            BuildingCard card = depository.getBuildingCardById(cardId);
            if (card == null) {
                System.err.println("[RECOVER] Lower building card not found for id: " + cardId);
                continue;
            }
            board.getLowerRow().getBuildingRow().add(card);
        }

        // Offer track
        List<OfferBoxDTO> offerDtos = boardDTO.offerTrackBoxes();
        if (offerDtos != null && !offerDtos.isEmpty()) {
            // Ensure order by position
            // offerDtos.sort(Comparator.comparingInt(OfferBoxDTO::position));
            List<OfferBox> offerBoxes = new ArrayList<>();
            for (OfferBoxDTO ob : offerDtos) {
                OfferBox box = new OfferBox(ob.foodReward(), ob.upperRowPicks(), ob.lowerRowPicks());
                if (ob.placedPlayerNickname() != null && !ob.placedPlayerNickname().isBlank()) {
                    try {
                        Player placed = playersObj.getPlayers().stream()
                                .filter(p -> p.getNickname().equals(ob.placedPlayerNickname()))
                                .findFirst()
                                .orElse(null);
                        if (placed != null) {
                            box.placePlayer(placed);
                        }
                    } catch (Exception ex) {
                        System.err.println("[ERROR] Exception caught in GameBuilder: Could not place player "
                                + ob.placedPlayerNickname() + " on offer box: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                offerBoxes.add(box);
            }
            board.getOfferTrack().initializeOfferTrack(offerBoxes);
        }

        // Turn order slots
        List<TurnOrderSlotDTO> slotDtos = boardDTO.turnOrderSlots();
        if (slotDtos != null && !slotDtos.isEmpty()) {
            // slotDtos.sort(Comparator.comparingInt(TurnOrderSlotDTO::position));
            List<TurnOrderSlot> slots = new ArrayList<>();
            for (TurnOrderSlotDTO ts : slotDtos) {
                TurnOrderSlot slot = new TurnOrderSlot(ts.foodBonus());
                if (ts.placedPlayerNickname() != null && !ts.placedPlayerNickname().isBlank()) {
                    try {
                        Player placed = playersObj.getPlayers().stream()
                                .filter(p -> p.getNickname().equals(ts.placedPlayerNickname()))
                                .findFirst()
                                .orElse(null);
                        if (placed != null) {
                            slot.placePlayer(placed);
                        }
                    } catch (Exception ex) {
                        System.err.println("[ERROR] Exception caught in GameBuilder: Could not place player "
                                + ts.placedPlayerNickname() + " on turn order slot: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                slots.add(slot);
            }
            board.getTurnOrderBox().initializeTurnOrderBox(slots);
        }

        // 3. RICOSTRUISCI I DECK GLOBALI
        TribeDeck tribeDeck = new TribeDeck();
        // Svuoti il deck creato di default e ci metti le carte salvate
        tribeDeck.getTribeDeck().clear();
        for (int id : snapshot.remainingTribeCardIds()) {
            try {
                TribeCard card = (TribeCard) depository.getCardbyId(id);

                tribeDeck.getTribeDeck().add(card);
            } catch (Exception ex) {
                System.err.println("[ERROR] Exception caught in GameBuilder: Tribe card not found for id: " + id);
                ex.printStackTrace();
            }
        }

        List<BuildingCard> buildingsEra1 = new ArrayList<>();
        List<BuildingCard> buildingsEra2 = new ArrayList<>();
        List<BuildingCard> buildingsEra3 = new ArrayList<>();

        for (int id : snapshot.remainingEra1BuildingIds()) {
            BuildingCard card = depository.getBuildingCardById(id);
            if (card == null) {
                System.err.println("[RECOVER] Era 1 building card not found for id: " + id);
            }
            buildingsEra1.add(card);
        }
        for (int id : snapshot.remainingEra2BuildingIds()) {
            BuildingCard card = depository.getBuildingCardById(id);
            if (card == null) {
                System.err.println("[RECOVER] Era 2 building card not found for id: " + id);
            }
            buildingsEra2.add(card);
        }
        for (int id : snapshot.remainingEra3BuildingIds()) {
            BuildingCard card = depository.getBuildingCardById(id);
            if (card == null) {
                System.err.println("[RECOVER] Era 3 building card not found for id: " + id);
            }
            buildingsEra3.add(card);
        }

        BuildingDecks buildingDecks = new BuildingDecks(buildingsEra1, buildingsEra2, buildingsEra3);
        // 4. CREA LO STATO INIZIALE (WaitingForReconnectState)
        List<String> pendingNicks = snapshot.players().stream().map(PlayerInfoDTO::nickname)
                .collect(Collectors.toList());
        WaitingForReconnectState waitState = new WaitingForReconnectState(snapshot.currentStateName(), pendingNicks);

        Era currentEra = switch (snapshot.board().currentEra()) {
            case "Era I" -> new Era1();
            case "Era II" -> new Era2();
            case "Era III" -> new Era3();
            default -> throw new IllegalStateException("Unexpected value: " + snapshot.board().currentEra());
        };
        // 5. ASSEMBLA IL GIOCO
        // (Assumiamo che se non avevi salvato l'Era e il lastRound, li ricavi o usi dei
        // valori di default per ora)
        Game game = new Game(snapshot.gameId(), snapshot.expectedPlayerCount(), playersObj, board, waitState, tribeDeck,
                buildingDecks, currentEra, snapshot.lastRoundTriggered());
        return game;
    }
}
