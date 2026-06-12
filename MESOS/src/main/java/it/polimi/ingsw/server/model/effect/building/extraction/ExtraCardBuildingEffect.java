package it.polimi.ingsw.server.model.effect.building.extraction;

import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;

import java.util.Optional;

/**
 * This class represents a building effect that grants an extra action to the player.
 * It implements both IBuildingEffect and IExtraActionEffect interfaces,
 * indicating that it is a building effect that provides an extra action to the player when activated.
 */
public class ExtraCardBuildingEffect implements IBuildingEffect, IExtraActionEffect {

    /**
     * This method is called to check if the building effect has an extra action.
     *
     * @return true if the building effect has an extra action, false otherwise.
     */
    @Override
    public boolean hasExtraAction() {
        return true;
    }

    /**
     * If the building implements IExtraActionEffect it returns an Optional containing himself.
     *
     * @return an Optional containing the IExtraActionEffect provided by this building effect.
     */
    @Override
    public Optional<IExtraActionEffect> getExtraActionEffect() {
        return Optional.of(this);
    }
}
