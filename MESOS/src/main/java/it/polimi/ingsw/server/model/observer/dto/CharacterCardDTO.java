package it.polimi.ingsw.server.model.observer.dto;

import java.io.Serializable;

public record CharacterCardDTO(
        int cardId,
        String name,
        String era,
        String imageKey,
        int shamanStars,
        boolean foodIcon,
        String inventorType
) implements Serializable {

    public CharacterCardDTO {
        if (cardId <= 0) throw new IllegalArgumentException("Invalid cardId: " + cardId);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
        if (era == null || era.isBlank()) throw new IllegalArgumentException("era cannot be null or blank");
        if (imageKey == null || imageKey.isBlank()) throw new IllegalArgumentException("imageKey cannot be null or blank");

        inventorType = inventorType == null ? "" : inventorType;
    }
}
