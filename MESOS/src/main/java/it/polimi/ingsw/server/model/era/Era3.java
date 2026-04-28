package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.TribeCard;

import java.util.List;

public class Era3 extends Era {
    public Era3() {}

    @Override
    public void selfSortbyEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        if(tribeCard.isFinalEvent()){
            finalEvents.add(tribeCard);
        }else{
            eraIII.add(tribeCard);
        }
    }
}
