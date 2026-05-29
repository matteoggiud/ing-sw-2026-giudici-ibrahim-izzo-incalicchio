package it.polimi.ingsw.server.model.menu;

import it.polimi.ingsw.server.model.core.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class that manages the logged-in players in the system.
 */
public class PlayerManager {

    /**
     * The singleton instance of the PlayerManager class.
     */
    private static PlayerManager instance;

    /**
     * A map that holds the logged-in players, where the key is the player's nickname and the value is the Player object.
     */
    private final Map<String, Player> loggedInPlayers;


    /**
     * Private constructor to prevent instantiation from outside the class. Initializes the loggedInPlayers map.
     */
    private PlayerManager() {
        loggedInPlayers = new ConcurrentHashMap<>();
    }


    /**
     * Returns the singleton instance of the PlayerManager class. If the instance does not exist, it creates a new one.
     * @return the singleton instance of the PlayerManager class
     */
    public static synchronized PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }


    /**
     * Adds a player to the logged-in players map.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if (player == null || player.getNickname() == null) {
            throw new IllegalArgumentException("Player and player nickname cannot be null.");
        }
        loggedInPlayers.put(player.getNickname(), player);
    }

    /**
     * Removes a player from the logged-in players map.
     *
     * @param nickname the nickname of the player to remove
     */
    public void removePlayer(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null.");
        }
        loggedInPlayers.remove(nickname);
    }

    /**
     * Retrieves a player by their nickname.
     *
     * @param nickname the nickname of the player to retrieve
     * @return the Player object associated with the given nickname.
     */
    public Player getPlayer(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null.");
        }
        return loggedInPlayers.get(nickname);
    }

    /**
     * Checks if a player with the given nickname is currently logged in.
     *
     * @param nickname the nickname of the player to check
     * @return true if the player is logged in, false otherwise
     */
    public boolean isLoggedIn(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null.");
        }
        return loggedInPlayers.containsKey(nickname);
    }
}
