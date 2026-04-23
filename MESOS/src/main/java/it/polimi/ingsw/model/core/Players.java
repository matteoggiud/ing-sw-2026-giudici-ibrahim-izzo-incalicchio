package it.polimi.ingsw.model.core;

import it.polimi.ingsw.model.ruleset.StandardRuleSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Players {
    private final List<Player> players;

    public Players() {
        this.players = new ArrayList<>();
    }

    /**
     * Adds a new player to the lobby.
     *
     * @param newPlayer the player to be added to the lobby
     * @throws IllegalArgumentException if the player is null or already in the lobby
     * @throws IllegalStateException if the lobby is already full
     */
    public void addPlayer(Player newPlayer) throws IllegalArgumentException, IllegalStateException {
        if (newPlayer == null)
            throw new IllegalArgumentException("Player cannot be null");
        if (this.contains(newPlayer))
            throw new IllegalArgumentException("Player already exists");
        if (isFull())
            throw new IllegalStateException("Lobby is full");


        this.players.add(newPlayer);
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

    /**
     * Returns the number of players currently in the lobby.
     *
     * @return the number of players in the lobby
     */
    public int size() {
        return players.size();
    }

    /**
     * Returns the players in their stable lobby order.
     *
     * @return a copy of the players currently in the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }


    /**
     * Checks if the lobby is empty
     *
     * @return true if the lobby is empty
     */
    public boolean isEmpty() {
        return players.isEmpty();
    }

    /**
     * Checks if the lobby is full.
     * @return true if the lobby is full
     */
    public boolean isFull() {
        return this.size() == StandardRuleSet.MAX_PLAYERS;
    }

    /**
     * Checks if a player is in the lobby
     *
     * @param player the player to check for
     * @return true if the player is in the lobby
     */
    public boolean contains(Player player) {
        return players.contains(player);
    }

    /**
     * Returns a list of players in the lobby in random order.
     */
    public List<Player> getShuffledPlayers() {
        List<Player> shuffledPlayers = new ArrayList<>(players);
        Collections.shuffle(shuffledPlayers);
        return shuffledPlayers;
    }
}
