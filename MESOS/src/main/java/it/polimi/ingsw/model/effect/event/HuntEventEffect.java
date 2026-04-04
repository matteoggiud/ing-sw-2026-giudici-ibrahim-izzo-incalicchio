package it.polimi.ingsw.model.effect.event;

import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.effect.EffectEventResult;

import static it.polimi.ingsw.model.card.CharacterType.HUNTER;

/**
 *Each Hunter in the tribe generates 1 Food and the amount of Prestige Points indicated on the Event card
 */

public class HuntEventEffect implements IEventEffect {
    @Override
    public EffectEventResult ApplyEffect(Game game, EventCard eventCard) {
        int n_hunter = 0;
        EffectEventResult effectEventResult = new EffectEventResult();
        for(Player p : game.getPlayers().getShuffledPlayers()){
            for(CharacterCard c : p.getPlayerDeck().getCharacters()){
                if(c.getType().equals(HUNTER)){
                    n_hunter++;
                }
            }
            effectEventResult.putPlayerFoodResults(p, n_hunter * eventCard.getGainedFood());
            effectEventResult.putPlayerPPResults(p, n_hunter * eventCard.getGainedPP());
        }
        return effectEventResult;
    }
    @Override
    public EventType getEventEffectType(){
        return EventType.HUNT;
    }
}
