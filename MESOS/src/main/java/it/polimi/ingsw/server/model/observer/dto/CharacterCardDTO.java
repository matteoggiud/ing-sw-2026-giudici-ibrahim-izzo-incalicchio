package it.polimi.ingsw.server.model.observer.dto;

import it.polimi.ingsw.server.model.card.Era;

import java.io.Serializable;

public record CharacterCardDTO(int cardId, String name, String era, String imageKey) implements Serializable {

    public CharacterCardDTO {
        if (cardId <= 0) throw new IllegalArgumentException("Invalid cardId: " + cardId);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
        if (era == null || era.isBlank()) throw new IllegalArgumentException("era cannot be null or blank");

        try {
            Era.valueOf(era);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid era: " + era, e);
        }
    }
}
