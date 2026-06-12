package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;

public record PlayerStatsDTO(
        int pp,
        int food,
        int characterCount,
        int sustenanceDiscount,
        int shamanStars,
        int builderDiscount,
        int differentInventorIcons,
        int gathererCount,
        int hunterCount,
        int builderCount,
        int shamanCount,
        int artistCount,
        int inventorCount
) implements Serializable {
}

