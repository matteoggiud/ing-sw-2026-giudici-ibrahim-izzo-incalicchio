package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IGameModel {
    void login(String nickname, String password) throws SQLException;
    void register(String nickname, String password) throws SQLException;
    void drawCard(Game game, Player player, Card selectedCard, boolean fromUpperRow);
    void Placing(Game game, Player player, OfferBox offerBox);
}
