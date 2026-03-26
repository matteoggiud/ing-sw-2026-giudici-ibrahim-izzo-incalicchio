package it.polimi.ingsw.Model.Board;
import java.util.List;
import java.util.ArrayList;


import it.polimi.ingsw.Model.Card.TribeCard;

public class TribeRow{
    private List<TribeCard> tribeCards = new ArrayList<>();


    public void add(TribeCard card) {
        this.tribeCards.add(card);
    }

    public void remove(TribeCard card) {
        this.tribeCards.remove(card);
    }

    public List<CharacterCard> getCharacterCards() {
        List<CharacterCard> characters = new ArrayList<>();
               for (int i = 0; i < tribeCards.size(); i++) {
                    TribeCard c = tribeCards.get(i);
                    if (c instanceof CharacterCard) {
                        characters.add((CharacterCard) c);
                    }
        }
        return characters;
    }

    public List<EventCard> getEventCards() {
        List<EventCard> events = new ArrayList<>();
        for (int i = 0; i < tribeCards.size(); i++) {
            TribeCard c = tribeCards.get(i);
            if (c instanceof EventCard) {
                events.add((EventCard) c);
            }
        }
        return events;
    }

    public void transferTribeCardsTo(TribeRow other) {
        for (int i = 0; i < this.tribeCards.size(); i++) {
            other.add(this.tribeCards.get(i));
        }
        this.tribeCards.clear();
    }
}
