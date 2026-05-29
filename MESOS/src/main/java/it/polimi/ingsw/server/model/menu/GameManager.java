package it.polimi.ingsw.server.model.menu;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import java.util.*;

/**
 * Singleton class that manages the active games in the system.
 */
public class GameManager {

    /**
     * The singleton instance of the GameManager class.
     */
    private static GameManager instance;

    /**
     * A map that holds the active games, where the key is the unique identifier of the game and the value is the Game object.
     */
    private final Map<Integer, Game> activeGames;

    /**
     * Private constructor to prevent instantiation from outside the class. Initializes the activeGames map.
     */
    private GameManager() {
        activeGames = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the GameManager class. If the instance does not exist, it creates a new one.
     * @return
     */
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }


    /**
     * Creates a new game with the specified host player, game id, and expected player count.
     *
     * @param host                the player who will host the game
     * @param gameId              the unique identifier for the game
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
     * Inserts an existing game (retrieved from the DB) into the active game map.
     *
     * @param game the reconstructed game
     */
    public void restoreGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        activeGames.put(game.getGameId(), game);
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


    /**
     * Returns a list of all available games that are not ended and are joinable.
     * @return a list of available games
     */
    public List<Game> getAvailableGames() {
        return activeGames.values().stream()
                .filter(game -> !game.isEnded())
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
        if (game.getPlayers().getHost().equals(player)) {
            activeGames.remove(gameId);
            return;
        }
        game.getGameState().removePlayer(game, player);
    }

    /**
     * Finds the game id of the game that a player with the given nickname is currently in.
     *
     * @param nickname the nickname of the player to search for
     * @return an Optional containing the game id if a player with the given nickname is found
     * in an active game, or an empty Optional if no such player is found
     */
    public Optional<Integer> findGameIdByPlayerNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or blank.");
        }

        return activeGames.values().stream()
                .filter(game -> !game.isEnded())
                .filter(game -> game.getPlayers().getPlayers().stream()
                        .anyMatch(player -> player.getNickname().equals(nickname)))
                .map(Game::getGameId)
                .findFirst();
    }


    /**
     * Removes a game from the active games map by its unique identifier. Before removing the game, it clears all observers associated with the game to prevent memory leaks and ensure proper cleanup of resources.
     * @param gameId the unique identifier of the game to be removed
     */
    public void removeGame(int gameId) {
        getGame(gameId).getObserverManager().clearObservers();
        activeGames.remove(gameId);
    }
}