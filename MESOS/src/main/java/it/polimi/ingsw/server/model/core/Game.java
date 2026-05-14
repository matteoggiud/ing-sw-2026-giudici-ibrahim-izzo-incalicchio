package it.polimi.ingsw.server.model.core;

import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.deck.BuildingDecks;
import it.polimi.ingsw.server.model.deck.TribeDeck;
import it.polimi.ingsw.server.model.observer.ObserverManager;
import it.polimi.ingsw.server.model.ruleset.IGameRuleSet;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;
import it.polimi.ingsw.server.model.states.EndedState;
import it.polimi.ingsw.server.model.states.IGameState;
import it.polimi.ingsw.server.model.states.LobbyState;

import java.util.Map;

/**
 * This class represents the game instance
 */

public class Game {

    /**
     * The game id is generated randomly using the UUID class, it is used to identify the game
     */
    private final int gameId;

    /**
     * The players list contains all the players in the game
     */
    private final Players players;

    /**
     * Board referees to the playground
     */
    private final Board board;

    /**
     * The game state is initialized to the initial state, which is the state before the game starts, then it follow the state interface
     */
    private IGameState gameState;

    /**
     * TribeDeck are all the TribeCard
     */
    private final TribeDeck tribeDeck;

    /**
     * BuildingDeck is a Map that associate every building with a particular Era
     */
    private final BuildingDecks buildingDecks;

    /**
     * The current era in which the game is
     */
    private Era currentEra;

    /**
     * This boolean flag indicates whether the last round of the game has been triggered.
     */
    private boolean lastRoundTriggered;

    /**
     * The rule set of the game
     */
    private final IGameRuleSet ruleSet;

    /**
     * The responsible for managing the observers of the game.
     */
    private final ObserverManager observerManager;

    /**
     * The expected number of players in the game
     */
    private final int expectedPlayerCount;


    /**
     * Constructor for the Game class, it initializes the game state, the players, the board and the decks.
     *
     * @param hostPlayer the player who created the game
     * @throws IllegalArgumentException if the player count is less than 2 or greater than 5
     */
    public Game(Player hostPlayer, int gameId) throws IllegalArgumentException {
        this(hostPlayer, gameId, StandardRuleSet.MIN_PLAYERS);
    }

    /**
     * Constructor for the Game class, it initializes the game state, the players, the board and the decks.
     * @param hostPlayer the player who created the game
     * @param gameId the unique identifier of the game
     * @param expectedPlayerCount the expected number of players in the game, must be between 2 and 5
     * @throws IllegalArgumentException if the player count is less than 2 or greater than 5, or if the host player is null
     */
    public Game(Player hostPlayer, int gameId, int expectedPlayerCount) throws IllegalArgumentException {
        if (hostPlayer == null) {
            throw new IllegalArgumentException("Host player cannot be null");
        }
        if (expectedPlayerCount < StandardRuleSet.MIN_PLAYERS || expectedPlayerCount > StandardRuleSet.MAX_PLAYERS) {
            throw new IllegalArgumentException("Expected player count must be between "
                    + StandardRuleSet.MIN_PLAYERS + " and " + StandardRuleSet.MAX_PLAYERS);
        }

        this.gameId = gameId;
        this.expectedPlayerCount = expectedPlayerCount;
        this.players = new Players();
        this.board = new Board();
        this.gameState = new LobbyState();
        this.tribeDeck = new TribeDeck();
        this.buildingDecks = new BuildingDecks();
        this.currentEra = null;
        this.lastRoundTriggered = false;
        this.ruleSet = new StandardRuleSet();
        this.observerManager = new ObserverManager();

        players.addPlayer(hostPlayer);
    }

    /**
     * Constructor for GameBuilder to reconstruct a game from a snapshot.
     * Takes all state parameters explicitly.
     */
    public Game(int gameId, int expectedPlayerCount, Players players, Board board, IGameState gameState, 
                TribeDeck tribeDeck, BuildingDecks buildingDecks, Era currentEra, boolean lastRoundTriggered) {
        this.gameId = gameId;
        this.expectedPlayerCount = expectedPlayerCount;
        this.players = players;
        this.board = board;
        this.gameState = gameState;
        this.tribeDeck = tribeDeck;
        this.buildingDecks = buildingDecks;
        this.currentEra = currentEra;
        this.lastRoundTriggered = lastRoundTriggered;
        
        this.ruleSet = new StandardRuleSet();
        this.observerManager = new ObserverManager();
    }


    /**
     * Gets the unique identifier of the game.
     *
     * @return The gameId
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Gets the host player of the game.
     */
    public Player getHostPlayer() {
        return this.players.getHost();
    }

    /**
     * Gets the list of players in the game.
     *
     * @return the list of players in the game
     */
    public Players getPlayers() {
        return this.players;
    }

    /**
     * Gets the game board.
     *
     * @return the Board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * This method is used to get the current game states.
     *
     * @return the current game state
     */
    public IGameState getGameState() {
        return gameState;
    }

    /**
     * Gets the tribe deck.
     *
     * @return the tribe deck of the game
     */
    public TribeDeck getTribeDeck() {
        return tribeDeck;
    }

    /**
     * Gets the building decks for each era.
     *
     * @return a HashMap associating each Era with its corresponding BuildingDeck
     */
    public BuildingDecks getBuildingDecks() {
        return buildingDecks;
    }

    /**
     * Gets the current era of the game.
     *
     * @return The current era of the game.
     * @throws IllegalStateException if the current era is null, which means the game has not started yet.
     */
    public Era getCurrentEra() throws IllegalStateException {
        if (this.currentEra == null) throw new IllegalStateException("The game has not started yet");
        return this.currentEra;
    }

    /**
     * Gets the lastRoundTriggered flag, which indicates whether the last round of the game has been triggered.
     *
     * @return true if the last round has been triggered, false otherwise.
     */
    public boolean isLastRoundTriggered() {
        return lastRoundTriggered;
    }

    /**
     * Gets the rule set of the game
     *
     * @return The rule set of the game
     */
    public IGameRuleSet getRuleSet() {
        return ruleSet;
    }

    /**
     * Gets the observer manager of the game, which is responsible for managing the observers of the game.
     *
     * @return the observer manager of the game
     */
    public ObserverManager getObserverManager() { return observerManager; }

    /**
     * Gets the number of the players in the game.
     *
     * @return the number of players in the game
     */
    public int getNumberOfPlayers() {
        return this.players.size();
    }

    /**
     * Gets the expected number of players in the game
     * @return the expected number of players in the game
     */
    public int getExpectedPlayerCount() {
        return expectedPlayerCount;
    }


    /**
     * Sets the game state to the specified game state. This method is used to update the game state during the game.
     *
     * @param gameState the new game state to set
     * @throws IllegalArgumentException if the provided game state is null
     */
    public void setGameState(IGameState gameState) throws IllegalArgumentException {
        if (gameState == null) {
            throw new IllegalArgumentException("The game state cannot be null");
        }
        this.gameState = gameState;
    }

    /**
     * Sets the lastRoundTriggered flag to the specified value.
     *
     * @param lastRoundTriggered the new value for the lastRoundTriggered flag
     */
    public void setLastRoundTriggered(boolean lastRoundTriggered) {
        this.lastRoundTriggered = lastRoundTriggered;
    }

    public void setCurrentEra(Era currentEra) throws IllegalStateException {
        if (currentEra == null) {
            throw new IllegalArgumentException("The current era cannot be null");
        }
        this.currentEra = currentEra;
    }

    /**
     * This method is used to check if the game has ended.
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isEnded() {
        return gameState.isEnded();
    }


    public void transferUpperTribeToLower() { // da portare nel game state
        board.transferUpperTribeToLower();
    }

    /**
     * This method is used to refill the upper row of the board with new tribe cards.
     * It handles the transition between eras.
     */
   /* public void refillUpperRow(int upperRowSize) { // da portare nel game state
        for (int i = 0; i < upperRowSize; i++) {
            TribeCard tribeCard = tribeDeck.draw();

            board.addTribeUpperRow(tribeCard);
            if (tribeCard.getEra() != currentEra) {
                changeEra(tribeCard.getEra());
            }

        }
    }*/

    /**
     * This method is used when the era changes, it updates the board and the building cards according to the new era.
     *
     * @param newEra the new era to which the game is transitioning
     * @throws IllegalArgumentException if the new era is null or if it is ERA_1.
     */
    /*public void changeEra(Era newEra) throws IllegalArgumentException { // da portare nel game state
        if (newEra == null || newEra == Era.ERA_1) {
            throw new IllegalArgumentException("Invalid era transition: new era cannot be null or ERA_1");
        }

        this.currentEra = newEra; // Update the current era to the new era

        if (currentEra == Era.ERA_3) {
            board.clearLowerBuildingRow(); // Clear the lower building row if we are transitioning to Era III
        }

        board.transferUpperBuildingToLower(); // Move the upper building row to the lower building row
        board.placeInUpperBuildingRow(buildingDecks.get(currentEra)); // Place new building cards in the upper building row based on the new era
    }*/

    /**
     * This method checks if the current round is the last round of the game by verifying if the tribe deck is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isLastRound() {
        return tribeDeck.isEmpty();
    }

    /**
     * WORK IN PROGRESS DO NOT TOUCH
     */
    public void advancePhase() { // da portare nel game state
    }

    public void advanceRound() { // da portare nel game state
    }


    /**
     * This method is used to end the game, it changes the game state to the ended state.
     * The rest will be managed by the state pattern.
     */
    public void endGame(Map<Player, Integer> finalRanking) {
        this.gameState = new EndedState(finalRanking);
    }

    /**
     * Returns the final ranking when the current game state supports it.
     *
     * @return a map of players and their final scores sorted by ranking
     * @throws IllegalStateException if the current state does not expose the final ranking yet
     */
    public Map<Player, Integer> getFinalRanking() {
        return gameState.getFinalRanking(this);
    }


}