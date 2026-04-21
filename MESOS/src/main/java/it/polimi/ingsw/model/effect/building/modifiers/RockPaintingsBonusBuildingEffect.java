package it.polimi.ingsw.model.effect.building.modifiers;

import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.model.effect.event.RockPaintingEventEffect;

import java.util.Optional;

/**
 * During rock paintings event, for each Artist that you have you gain a Food bonus
 */
public class RockPaintingsBonusBuildingEffect implements IBuildingEffect, IEventModifier {

    @Override
    public Optional<IEventModifier> getEventModifier() {
        return Optional.of(this);
    }


    @Override
    public void visitEvent(RockPaintingEventEffect eventCard, EffectEventResult result) {
        Player p = result.getTargetPlayer();

        // Count the number of "Artists" in the player's tribe
        int numbersOfArtists = p.getPlayerDeck().countCharactersByType(CharacterType.ARTIST);

        // For each Artist, gain 1 Food
        result.setFoodChange(result.getFoodChange() + numbersOfArtists);
    }
}
