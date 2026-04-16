package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.util.ArrayList;

public interface IGameModel {
    void addPlayerToMenu(String nickname, String password);
    void drawCard(Game game, Player player, ArrayList<Card> upperCards, ArrayList<Card> lowerCards);
    void Placing(Game game, Player player, OfferBox offerBox);
}
