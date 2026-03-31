package it.polimi.ingsw.model.core;

import it.polimi.ingsw.model.card.Era;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class represents the game, it contains all the information about the game state,
 * the players, the board and the decks.
 * It is used to manage the game and to update the game state.
 * It is also used to save and load the game state.
 */

public class Game {

    /** The game id is generated randomly using the UUID class, it is used to identify the game */
    private UUID gameId;

    /** The game state is initialized to the initial state, which is the state before the game starts, then it follow the state interface */
    private GameState gameState;

    /** The player count is the number of players in the game */
    private int playerCount;

    /** The players list contains all the players in the game */
    private List<Player> players;

    /** Board referes to the playground */
    private Board board;

    /** TribeDeck are all the TribeCard */
    private TribeDeck tribeDeck;

    /** BuildingDeck is a Map that associate every building with a particular Era */
    private Map<Era, BuildingDeck> buildingDeck;

    /**
     * Constructor for the Game class, it initializes the game state, the players, the board and the decks.
     *
     * @param playerCount
     * @throws IllegalArgumentException if the player count is less than 2 or greater than 5
     */
    public Game(int playerCount) throws IllegalArgumentException {
        this.gameId = UUID.randomUUID();
        this.gameState = new InitState();
        this.playerCount = playerCount;
        this.players = new ArrayList<>();
        this.board = new Board();
        this.tribeDeck = new TribeDeck();
        this.buildingDeck = new Map<Era, BuildingDeck>();

        if((playerCount < 2) || (playerCount > 5)) {
            throw new IllegalArgumentException("The number of players to play needs to be between 2 and 5");
        }

    }

    /**
     * This method is used to get the game id, it returns the game id of the game.
     *
     * @return The gameId
     */
    public UUID getGameId() {return this.gameId;}

    /**
     * This method is used to get the game state, it returns the game state of the game.
     * Once it's returned you need to use the instanceof to know exactly wich state
     * you are in
     *
     * @return The GameState
     */
    public GameState getGameState() {return gameState;}

    /**
     * This method is used to get the player count, it returns the player count of the game.
     *
     * @return The PlayerCount
     */
    public int getPlayerCount() {return this.playerCount;}

    /**
     * This method is used to get the players list, it returns the players list of the game.
     *
     * @return The Players
     */
    public List<Player> getPlayers() {return this.players;}

    /**
     * This method is used to get the board, it returns the board of the game.
     *
     * @return The Board
     */
    public Board getBoard() {return this.board;}

    /**
     * This method is used to start the game, it changes the game state to the first phase of the game,
     * which is the placing totem phase.
     *
     * @throws IllegalArgumentException if the number of players is less than 2 or greater than 5
     */
    public void startGame() throws IllegalArgumentException {
        gameState = new PlacingTotemState();

        if((this.players.size() < 2) || (this.players.size() > 5)){
            throw new IllegalArgumentException("The number of players to play needs to be between 2 and 5");
        }

    }

    /**
     * This method is used to check if the game has started, it returns true if the game state is not in the initial state,
     * the ended state, the lobby state or the aborted state, otherwise it returns false.
     *
     * @return True if the game has started, false otherwise
     */
    public boolean isStarted(){
        if((gameState instanceof InitState) || (gameState instanceof EndedStatus) || (gameState instanceof LobbyState) || (gameState instanceof AbortedState)){
            return false;
        }else{
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
    public boolean isEnded(){
        if(gameState instanceof EndedStatus){
            return true;
        }else{
            return false;
        }
    }

    /**
     * WORK IN PROGRESS DO NOT TOUCH
     */
    public void advancePhase(){}

    public void advanceRound(){}

    public void beginNewEra(){}

    /**
     * This method is used to end the game, it changes the game state to the ended state.
     * The rest will be managed by the state pattern.
     */
    public void endGame(){
        this.gameState = new EndedStatus();
    }

    public void getFinalRanking() throws IllegalStateException{

        if(!(this.gameState instanceof FinalScoreState)){
            throw new IllegalStateException("The game state is not FinalScoreState");
        }

    }

}