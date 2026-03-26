package it.polimi.ingsw.model.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class represents the game, it contains all the information about the game state,
 * the players, the board and the decks.
 * It is used to manage the game and to update the game state.
 * It is also used to save and load the game state.
 */

public class Game {
    private UUID gameId;
    private GameState gameState;
    private int playerCount;
    private List<Player> players;
    private Board board;
    private TribeDeck tribeDeck;
    private Map<Era, BuildingDeck> buildingDeck;

    /**
     * Constructor for the Game class, it initializes the game state, the players, the board and the decks.
     * @param playerCount
     */

    public Game(int playerCount) {
        this.gameId = UUID.randomUUID();
        this.gameState = new InitState();
        this.playerCount = playerCount;
        this.players = new ArrayList<>();
        this.board = new Board();
        this.tribeDeck = new TribeDeck();
        this.buildingDeck = new HashMap<>();
    }

    /**
     * This method is used to start the game, it changes the game state to the first phase of the game,
     * which is the placing totem phase.
     */

    public void startGame(){
        gameState = new PlacingTotemState();
    }

    /**
     * This method is used to check if the game has started, it returns true if the game state is not in the initial state,
     * the ended state, the lobby state or the aborted state, otherwise it returns false.
     * @return
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
     * @return
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

    public void endGame(){}

    public void getFinalRanking(){}

}

