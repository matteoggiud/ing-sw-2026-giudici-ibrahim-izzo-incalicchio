package it.polimi.ingsw.common.network.dto;

import it.polimi.ingsw.server.model.era.Era;

import java.io.Serializable;

public record EventCardDTO(int cardId, String name, String era, String imageKey) implements Serializable {

    public EventCardDTO {
        if (cardId <= 0) throw new IllegalArgumentException("Invalid cardId: " + cardId);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
        if (era == null || era.isBlank()) throw new IllegalArgumentException("era cannot be null or blank");
        if (imageKey == null || imageKey.isBlank()) throw new IllegalArgumentException("imageKey cannot be null or blank");
    }
}
