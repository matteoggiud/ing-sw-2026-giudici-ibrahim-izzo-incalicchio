package it.polimi.ingsw.model.core;

import java.util.ArrayList;

public class Players {
    private final ArrayList<Player> players;

    public Players() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player newPlayer) {
        // TODO: add checks for null, duplicates, and max number of players
        players.add(newPlayer);
    }

    /**
     * The first player in the list is always the host (the one who created the lobby).
     *
     * @return the host player
     */
    public Player getHost() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No players in the lobby");
        }
        return players.getFirst(); // The first player in the list is the host
    }

    /**
     * Removes a player from the lobby.
     *
     * @throws IllegalArgumentException if the player is not in the lobby
     */
    public void removePlayer(Player player) throws IllegalArgumentException {
        if(!players.contains(player)) {
            throw new IllegalArgumentException("Player not in the lobby");
        }
        players.remove(player);
    }

    public int size() {
        return players.size();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }
}
