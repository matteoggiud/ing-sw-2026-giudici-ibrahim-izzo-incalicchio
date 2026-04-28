package it.polimi.ingsw.server.model.observer.dto;

import java.io.Serializable;

public record OfferBoxDTO(int position, String placedPlayerNickname, int foodReward, int upperRowPicks,
                          int lowerRowPicks, String imageKey) implements Serializable {

    public OfferBoxDTO {
        if (position < 1) {
            throw new IllegalArgumentException("Position must be positive");
        }

        if (foodReward < 0 || lowerRowPicks < 0 || upperRowPicks < 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
    }
}
