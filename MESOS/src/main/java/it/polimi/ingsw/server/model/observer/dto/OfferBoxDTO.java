package it.polimi.ingsw.server.model.observer.dto;

import it.polimi.ingsw.server.model.board.OFFER_BOX_ID;

import java.io.Serializable;
import java.util.Arrays;

public record OfferBoxDTO(String boxId, String placedPlayerNickname, int foodReward, int upperRowPicks,
                          int lowerRowPicks, String imageKey) implements Serializable {

    public OfferBoxDTO {
        if (boxId == null)
            throw new IllegalArgumentException("boxId cannot be null");

        if (Arrays.stream(OFFER_BOX_ID.values()).noneMatch(id -> id.name().equals(boxId)))
            throw new IllegalArgumentException("Invalid boxId: " + boxId);

        if (foodReward < 0 || lowerRowPicks < 0 || upperRowPicks < 0)
            throw new IllegalArgumentException("Invalid parameters");
    }
}
