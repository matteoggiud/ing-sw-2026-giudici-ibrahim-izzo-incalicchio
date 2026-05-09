package it.polimi.ingsw.server.model.observer.dto;

import java.io.Serializable;

public record PlayerInfoDTO(
        String nickname,
        int food,
        int pp,
        String totem,
        PlayerDeckDTO deck,
        PlayerStatsDTO stats
) implements Serializable {
}
