package it.polimi.ingsw.server.model.menu;

import it.polimi.ingsw.server.model.core.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager instance;
    private final Map<String, Player> loggedInPlayers;

    private PlayerManager() {
        loggedInPlayers = new ConcurrentHashMap<>();
    }

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
