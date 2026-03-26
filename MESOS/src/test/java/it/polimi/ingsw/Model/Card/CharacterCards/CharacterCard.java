package it.polimi.ingsw.Model.Card.CharacterCards;

import it.polimi.ingsw.Model.Card.Card;
import it.polimi.ingsw.Model.Card.Era;

import java.util.UUID;

public class CharacterCard extends Card {
    private final CharacterType type;


    public CharacterCard(UUID cardId, String name, Era era, int minNumPlayers, CharacterType type) {
        super(cardId, name, era, minNumPlayers);
        this.type = type;
    }

    public CharacterType getType() {
        return type;
    }

}
