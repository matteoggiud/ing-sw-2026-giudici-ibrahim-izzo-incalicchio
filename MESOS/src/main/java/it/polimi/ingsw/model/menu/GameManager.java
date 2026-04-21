package it.polimi.ingsw.model.menu;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.util.*;

public class GameManager {
    private final Map<UUID, Game> activeGames;

    public GameManager() {
        activeGames = new HashMap<>();
    }

    /**
     * Creates a new game with the specified host player and adds it to the active games map.
     *
     * @param host the player who will host the game
     * @return the UUID of the newly created game
     */
    public UUID createGame(Player host) {
        if (host == null) {
            throw new IllegalArgumentException("Host player cannot be null.");
        }
        Game newGame = new Game(host);
        activeGames.put(newGame.getGameId(), newGame);
        return newGame.getGameId();
    }

    public List<Game> getAvailableGames() {
        return activeGames.values().stream()
                .filter(g -> g.getGameState().isJoinable(g))
                .filter(g -> g.getPlayers().isFull())
                .toList();
    }

    /**
     * Allows a player to join an existing game by its UUID.
     *
     * @param gameId the UUID of the game to join
     * @param player the player who wants to join the game
     * @throws IllegalArgumentException if the game does not exist or if the player cannot join the game
     */
    public void joinGame(UUID gameId, Player player) {
        Game game = activeGames.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + gameId + " does not exist.");
        }

        game.getPlayers().addPlayer(player);
    }

    /**
     *
     * Allows a player to leave a game by its UUID.
     *
     * @param gameId the UUID of the game to leave
     * @param player the player who wants to leave the game
     */
    public void leaveGame(UUID gameId, Player player) {
        Game game = activeGames.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + gameId + " does not exist.");
        }

        game.getPlayers().removePlayer(player);
    }
}
