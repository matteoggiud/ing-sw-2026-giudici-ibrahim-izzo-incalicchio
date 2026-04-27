package it.polimi.ingsw.server.model.menu;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;

import java.util.*;

public class GameManager {
    private final Map<Integer, Game> activeGames;

    public GameManager() {
        activeGames = new HashMap<>();
    }

    /**
     * Creates a new game with the specified host player and game id
     * @param host the player who will host the game
     * @param gameId the unique identifier for the game
     * @return the unique identifier of the created game
     */
    public int createGame(Player host, int gameId) {
        return createGame(host, gameId, StandardRuleSet.MIN_PLAYERS);
    }

    /**
     * Creates a new game with the specified host player, game id, and expected player count.
     * @param host the player who will host the game
     * @param gameId the unique identifier for the game
     * @param expectedPlayerCount the expected number of players for the game
     * @return the unique identifier of the created game
     */
    public int createGame(Player host, int gameId, int expectedPlayerCount) {
        if (host == null) {
            throw new IllegalArgumentException("Host player cannot be null.");
        }

        Game newGame = new Game(host, gameId, expectedPlayerCount);
        activeGames.put(newGame.getGameId(), newGame);
        return newGame.getGameId();
    }


    /**
     * Retrieves an active game by its unique identifier.
     *
     * @param gameId the unique identifier of the game
     * @return the game associated with the given id
     * @throws IllegalArgumentException if the game id is null or no game exists with that id
     */
    public Game getGame(int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }

        Game game = activeGames.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + gameId + " does not exist.");
        }

        return game;
    }


    public List<Game> getAvailableGames() {
        return activeGames.values().stream()
                .filter(g -> g.getGameState().isJoinable(g))
                .toList();
    }

    /**
     * Allows a player to join an existing game by its UUID.
     *
     * @param gameId the UUID of the game to join
     * @param player the player who wants to join the game
     * @throws IllegalArgumentException if the game does not exist or if the player cannot join the game
     */
    public void joinGame(int gameId, Player player) {
        Game game = getGame(gameId);
        game.getGameState().addPlayer(game, player);
    }


    /**
     *
     * Allows a player to leave a game by its UUID.
     *
     * @param gameId the UUID of the game to leave
     * @param player the player who wants to leave the game
     */
    public void leaveGame(int gameId, Player player) {
        Game game = getGame(gameId);
        game.getGameState().removePlayer(game, player);
    }

}
