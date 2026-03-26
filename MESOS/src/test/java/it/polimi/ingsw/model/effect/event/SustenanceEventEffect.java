package it.polimi.ingsw.model.effect.event;

/**
 * Pay 1 Food for every Character in the tribe. If Food is insufficient,
 * you lose the indicated PP for each unfed Character.
 * This event must always be resolved last
 */

public class SustenanceEventEffect implements IEventEffect {
    @Override
    public void ApplyEffect() {}
}
