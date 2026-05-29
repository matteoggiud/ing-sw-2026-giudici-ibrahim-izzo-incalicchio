package it.polimi.ingsw.server.model.effect.building.shamanicstars;

import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * During the Shamanic Ritual your tribe gains 3 additional Shamanic Stars.
 */
public class ShamanicStarsBuildingEffect implements IBuildingEffect, IShamanicStarsProvider {
    private static final int GAINED_SHAMANIC_STARS = 3;

    /**
     * This method returns an Optional containing this instance as an IShamanicStarsProvider,
     * indicating that this building effect provides a bonus
     * to the number of shamanic stars during the Shamanic Ritual Event.
     * @return An Optional containing this instance as an IShamanicStarsProvider.
     */
    @Override
    public Optional<IShamanicStarsProvider> getShamanicStarsProvider() {
        return Optional.of(this);
    }

    /**
     * If the building modifies the input of shamanic stars calculation, it returns the number of bonus stars, otherwise it returns 0
     *
     * @return the number of bonus stars provided by the building.
     */
    @Override
    public int getBonusShamanStars() {
        return GAINED_SHAMANIC_STARS;
    }
}
