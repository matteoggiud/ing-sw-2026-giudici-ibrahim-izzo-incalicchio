package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;

public record BuildingCardDTO(
        int cardId,
        String name,
        String era,
        String imageKey,
        int foodCost
) implements Serializable {

    public BuildingCardDTO {
        if (cardId <= 0) throw new IllegalArgumentException("Invalid cardId: " + cardId);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
        if (era == null || era.isBlank()) throw new IllegalArgumentException("era cannot be null or blank");
        if (foodCost < 0) throw new IllegalArgumentException("foodCost cannot be negative");
        if (imageKey == null || imageKey.isBlank()) throw new IllegalArgumentException("imageKey cannot be null or blank");
    }
}
