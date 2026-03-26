package it.polimi.ingsw.model.core;

import java.util.UUID;

public class Game {
    private UUID gameId;
    GameState gameState;
    int playerCount;
    List<Player> players;
    Board board;
    TribeDeck tribeDeck;
    Map<Era, BuildingDeck> buildingDeck;
}
