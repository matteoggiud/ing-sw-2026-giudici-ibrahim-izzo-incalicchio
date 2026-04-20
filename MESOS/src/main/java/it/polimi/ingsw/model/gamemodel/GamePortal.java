package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public class GamePortal implements IGameModel {
    @Override
    public void drawCard(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        if (game == null || player == null || selectedCard == null) {
            throw new IllegalArgumentException("Game, player, selectedCard and lowerCards cannot be null");
        }

        game.getGameState().resolveAction(game, player, selectedCard, fromUpperRow);
    }

    @Override
    public void Placing(Game game, Player player, OfferBox offerBox) {
        if (game == null || player == null || offerBox == null) {
            throw new IllegalArgumentException("Game, player e offerBox cannot be null");
        }

        game.getGameState().placeTotem(game, player, offerBox);
    }

    @Override
    public void login(String nickname, String password) throws SQLException {
        boolean successfull=false;
        Authenticator db = new Authenticator();
        if (nickname == null || password == null) {
            throw new IllegalArgumentException("Nickname and password cannot be null");
        }
        successfull = db.loginUser(nickname, password);
        if (!successfull) {
            throw new IllegalArgumentException("Invalid nickname or password");
        }else{
            //We need to think about the menu interface
        }
    }

    @Override
    public void register(String nickname, String password) throws SQLException {
        boolean successfull=false;
        Authenticator db = new Authenticator();
        if (nickname == null || password == null) {
            throw new IllegalArgumentException("Nickname and password cannot be null");
        }
        successfull = db.registerUser(nickname, password);
        if (!successfull) {
            throw new IllegalArgumentException("Registration failed");
        }else{
            login(nickname, password);
        }
    }
}
