package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;
import it.polimi.ingsw.model.card.CharacterType;

/**
 * For every Artist that you have you gain a Food bonus
 */
public class ArtistEffect implements IBuildingEffect {

    /**
     * The amount of food gained by the player for each artist that they have
     */
    private int FoodGained;

    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodGained * N_Subject,0,0,0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return CharacterType.ARTIST;
    }

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    @Override
    public EventType getEventEffectType() {
        return EventType.ROCK_PAINTINGS;
    }
}
