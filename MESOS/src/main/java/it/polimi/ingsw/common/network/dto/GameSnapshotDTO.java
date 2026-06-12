package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing a complete snapshot of the game state.
 * Used for persisting the game to the database to recover from server crashes.
 */
public record GameSnapshotDTO(
        int gameId,
        int expectedPlayerCount,
        boolean lastRoundTriggered,
        String currentStateName, // e.g., "LobbyState", "SetupState", "PlacingTotemState"

        List<PlayerInfoDTO> players,
        BoardDTO board,

        // Hidden state required for exact reconstruction
        List<Integer> remainingTribeCardIds,
        List<Integer> remainingEra1BuildingIds,
        List<Integer> remainingEra2BuildingIds,
        List<Integer> remainingEra3BuildingIds) implements Serializable {

    public GameSnapshotDTO {
        if (currentStateName == null || currentStateName.isBlank()) {
            throw new IllegalArgumentException("Current state name cannot be null or blank.");
        }

        // Checks for null lists are not needed since records automatically throw
        // NullPointerException if any component is null.
        players = List.copyOf(players);

        if (remainingTribeCardIds != null)
            remainingTribeCardIds = List.copyOf(remainingTribeCardIds);
        if (remainingEra1BuildingIds != null)
            remainingEra1BuildingIds = List.copyOf(remainingEra1BuildingIds);
        if (remainingEra2BuildingIds != null)
            remainingEra2BuildingIds = List.copyOf(remainingEra2BuildingIds);
        if (remainingEra3BuildingIds != null)
            remainingEra3BuildingIds = List.copyOf(remainingEra3BuildingIds);
    }
}
