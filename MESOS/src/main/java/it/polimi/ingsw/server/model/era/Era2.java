package it.polimi.ingsw.server.model.era;

import it.polimi.ingsw.server.model.card.TribeCard;

import java.util.List;

public class Era2 extends Era{
    public Era2() {}

    @Override
    public void selfSortbyEra(TribeCard tribeCard, List<TribeCard> eraI, List<TribeCard> eraII, List<TribeCard> eraIII, List<TribeCard> finalEvents) {
        eraII.add(tribeCard);
    }
}
