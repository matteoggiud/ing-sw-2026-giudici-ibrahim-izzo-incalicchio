package it.polimi.ingsw.model.observer.dto;

import it.polimi.ingsw.model.card.Era;

public record BuildingCardDTO(int cardId, String name, String era, String imageKey) {
    public BuildingCardDTO {
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
