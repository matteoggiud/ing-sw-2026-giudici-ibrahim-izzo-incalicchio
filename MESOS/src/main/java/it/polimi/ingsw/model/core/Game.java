package it.polimi.ingsw.model.core;

import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.card.TribeCard;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.states.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class represents the game, it contains all the information about the game state,
 * the players, the board and the decks.
 * It is used to manage the game and to update the game state.
 * It is also used to save and load the game state.
 */

public class Game {

    /**
     * The game id is generated randomly using the UUID class, it is used to identify the game
     */
    private final UUID gameId;

    /**
     * The game state is initialized to the initial state, which is the state before the game starts, then it follow the state interface
     */
    private IGameState gameState;

    /**
     * The number of players.
     */
    private int numberOfPlayers;

    /**
     * The players list contains all the players in the game
     */
    private final ArrayList<Player> players;

    /**
     * Board referes to the playground
     */
    private final Board board;

    /**
     * TribeDeck are all the TribeCard
     */
    private final TribeDeck tribeDeck;

    /**
     * BuildingDeck is a Map that associate every building with a particular Era
     */
    private final HashMap<Era, BuildingDeck> buildingDeck;

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
    private IGameRuleSet ruleSet;

    /**
     * The turn order of the players, it is used to manage the turn order of the players during the game.
     */
    private ArrayList<Player> turnOrder;

    /**
     * Constructor for the Game class, it initializes the game state, the players, the board and the decks.
     *
     * @param playerCount
     * @throws IllegalArgumentException if the player count is less than 2 or greater than 5
     */
    public Game() throws IllegalArgumentException {
        this.gameId = UUID.randomUUID();
        this.gameState = new LobbyState();
        this.numberOfPlayers = 0;
        this.players = new ArrayList<>();
        this.board = new Board();
        this.tribeDeck = new TribeDeck();
        this.buildingDeck = new HashMap<Era, BuildingDeck>();
        this.currentEra = Era.ERA_1;
        this.lastRoundTriggered = false;
        this.ruleSet = new StandardRuleSet();

    }

    /**
     * This method is used to get the game id, it returns the game id of the game.
     *
     * @return The gameId
     */
    public UUID getGameId() {
        return this.gameId;
    }

    /**
     * This method is used to get the game state, it returns the game state of the game.
     * Once it's returned you need to use the instanceof to know exactly wich state
     * you are in
     *
     * @return The GameState
     */
    public IGameState getGameState() {
        return gameState;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }


    /**
     * This method is used to get the players list, it returns the players list of the game.
     *
     * @return The Players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * This method is used to get the board, it returns the board of the game.
     *
     * @return The Board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Gets the current era of the game.
     *
     * @return The current era of the game.
     */
    public Era getCurrentEra() {
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
     * @return The rule set of the game
     */
    public IGameRuleSet getRuleSet() {
        return ruleSet;
    }

    /**
     * Gets the turn order of the players.
     * @return The turn order of the players
     */
    public ArrayList<Player> getTurnOrder() {
        return new ArrayList<Player>(turnOrder);
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

    /**
     * This method is used to start the game, it changes the game state to the first phase of the game,
     * which is the placing totem phase.
     *
     * @throws IllegalArgumentException if the number of players is less than 2 or greater than 5
     */
    public void startGame() throws IllegalArgumentException {
        gameState = new PlacingTotemState();

        if ((this.players.size() < ruleSet.getMinPlayers()) || (this.players.size() > ruleSet.getMaxPlayers())) {
            throw new IllegalArgumentException("The number of players to play needs to be between 2 and 5");
        }

    }

    /**
     * This method is used to check if the game has started, it returns true if the game state is not in the initial state,
     * the ended state, the lobby state or the aborted state, otherwise it returns false.
     *
     * @return True if the game has started, false otherwise
     */
    public boolean isStarted() {
        if ((gameState instanceof LobbyState) || (gameState instanceof EndedState) || (gameState instanceof LobbyState) || (gameState instanceof AbortedState)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method is used to check if the game has ended,
     * it returns true if the game state is in the ended state,
     * otherwise it returns false.
     *
     * @return True if the game has ended, false otherwise
     */
    public boolean isEnded() {
        if (gameState instanceof EndedState) {
            return true;
        } else {
            return false;
        }
    }

    public void transferUpperTribeToLower() { // da portare nel game state
        board.transferUpperTribeToLower();
    }

    /**
     * This method is used to refill the upper row of the board with new tribe cards.
     * It handles the transition between eras.
     */
    public void refillUpperRow(int upperRowSize) { // da portare nel game state
        for (int i = 0; i < upperRowSize; i++) {
            TribeCard tribeCard = tribeDeck.draw();

            board.addTribeUpperRow(tribeCard);
            if (tribeCard.getEra() != currentEra) {
                changeEra(tribeCard.getEra());
            }

        }
    }

    /**
     * This method is used when the era changes, it updates the board and the building cards according to the new era.
     *
     * @param newEra the new era to which the game is transitioning
     * @throws IllegalArgumentException if the new era is null or if it is ERA_1.
     */
    public void changeEra(Era newEra) throws IllegalArgumentException { // da portare nel game state
        if (newEra == null || newEra == Era.ERA_1) {
            throw new IllegalArgumentException("Invalid era transition: new era cannot be null or ERA_1");
        }

        this.currentEra = newEra; // Update the current era to the new era

        if (currentEra == Era.ERA_3) {
            board.clearLowerBuildingRow(); // Clear the lower building row if we are transitioning to Era III
        }

        board.transferUpperBuildingToLower(); // Move the upper building row to the lower building row
        board.placeInUpperBuildingRow(buildingDeck.get(currentEra)); // Place new building cards in the upper building row based on the new era
    }

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
    public void endGame() {
        this.gameState = new EndedState();
    }

    public void getFinalRanking() throws IllegalStateException { // da portare nel game state

        if (!(this.gameState instanceof ComputeFinalScoreState)) {
            throw new IllegalStateException("The game state is not FinalScoreState");
        }

    }

}