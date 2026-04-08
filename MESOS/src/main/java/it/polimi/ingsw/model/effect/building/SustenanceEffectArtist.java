package it.polimi.ingsw.model.effect.building;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.effect.EffectBuildingResult;

public class SustenanceEffectArtist implements IBuildingEffect {

    /**
     * The amount of Food discounted for each Inventor that the player has
     */
    private int FoodDiscount;

    /**
     * Apply the effect of the building card based on the number of subjects (N_Subject) that are relevant to the effect.
     *
     * @param N_Subject
     * @return
     */
    @Override
    public EffectBuildingResult ApplyEffect(int N_Subject) {
        return new EffectBuildingResult(FoodDiscount * N_Subject, 0, 0, 0);
    }

    /**
     * Get the type of the building effect, which can be used to determine how the effect should be applied or displayed in the game.
     *
     * @return
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
        return EventType.SUSTENANCE;
    }
}
