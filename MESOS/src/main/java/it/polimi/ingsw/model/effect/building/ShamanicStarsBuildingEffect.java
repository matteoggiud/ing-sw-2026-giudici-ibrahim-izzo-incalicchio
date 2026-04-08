package it.polimi.ingsw.model.effect.building;

/**
 * During the Shamanic Ritual your tribe gains 3 additional Shamanic Stars.
 */
public class ShamanicStarsBuildingEffect implements IBuildingEffect {

    /**
     * If the building modifies the input of shamanic stars calculation, it returns the number of bonus stars, otherwise it returns 0
     *
     * @return the number of bonus stars provided by the building.
     */
    @Override
    public int getBonusShamanStars() {
        return 3;
    }
}
