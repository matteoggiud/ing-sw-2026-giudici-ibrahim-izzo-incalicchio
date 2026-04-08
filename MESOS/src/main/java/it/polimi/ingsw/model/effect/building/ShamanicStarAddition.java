package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

/**
 * During the Shamanic Event you get the additional Stars in the Building
 */
public class ShamanicStarAddition implements IBuildingEffect {

    /**
     * The amount of Stars that the player gets during the Shamanic Event
     */
    private int Star;

    /**
     * Apply the effect of gaining additional Stars during the Shamanic Event based on the number of Stars that the player would gain (N_Subject)
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(0,0,Star,0);
    }

    /**
     * Give the type of character that this effect is related to
     *
     * @return type of character
     */
    @Override
    public CharacterType getSubjectType() {
        return CharacterType.SHAMAN;
    }

    /**
     * Gets the event type associated with this building effect, which can be used to determine when the effect should be triggered during the game.
     *
     * @return the event type associated with this building effect
     */
    @Override
    public EventType getEventEffectType() {
        return EventType.SHAMANIC_RITUAL;
    }
}
