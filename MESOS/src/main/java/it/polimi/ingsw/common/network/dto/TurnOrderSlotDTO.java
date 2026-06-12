package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;

/**
 * DTO representing one slot of the turn order track.
 *
 * @param position the position of the slot in the turn order track
 * @param placedPlayerNickname the nickname of the player in the slot, or null if the slot is free
 * @param foodBonus the food bonus or malus associated with the slot
 */
public record TurnOrderSlotDTO(int position, String placedPlayerNickname, int foodBonus) implements Serializable {

    public TurnOrderSlotDTO {
        if (position < 0) {
            throw new IllegalArgumentException("Position cannot be negative.");
        }
    }
}