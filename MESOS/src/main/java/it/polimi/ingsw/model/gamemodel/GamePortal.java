package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The GamePortal class serves as the main entry point for interacting with the game model.
 * It implements the IGameModel interface, providing concrete implementations for user authentication and game actions such as drawing cards and placing totems.
 *
 */
public class GamePortal implements IGameModel {
    @Override
    public void drawCard(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        if (game == null || player == null || selectedCard == null) {
            throw new IllegalArgumentException("Game, player, selectedCard and lowerCards cannot be null");
        }

        game.getGameState().resolveAction(game, player, selectedCard, fromUpperRow);
    }

    /**
     * Allows a player to place their totem on a specific offer box on the offer track.
     *
     * @param game     the current game context
     * @param player   the player attempting to place their totem
     * @param offerBox the offer box on which the player wants to place their totem
     */
    @Override
    public void Placing(Game game, Player player, OfferBox offerBox) {
        if (game == null || player == null || offerBox == null) {
            throw new IllegalArgumentException("Game, player e offerBox cannot be null");
        }

        game.getGameState().placeTotem(game, player, offerBox);
    }

    /**
     * Attempts to log in a user with the provided nickname and password.
     *
     * @param nickname the nickname of the user trying to log in
     * @param password the password of the user trying to log in
     * @throws SQLException if there is an error accessing the database or if the login credentials are invalid
     */
    @Override
    public void login(String nickname, String password) throws SQLException {
        boolean successfull = false;
        Authenticator db = new Authenticator();
        if (nickname == null || password == null) {
            throw new IllegalArgumentException("Nickname and password cannot be null");
        }
        successfull = db.loginUser(nickname, password);
        if (!successfull) {
            throw new IllegalArgumentException("Invalid nickname or password");
        } else {
            //We need to think about the menu interface
        }
    }

    /**
     * Attempts to register a new user with the provided nickname and password.
     *
     * @param nickname the nickname of the user trying to register
     * @param password the password of the user trying to register
     * @throws SQLException if there is an error accessing the database or if the registration fails
     */
    @Override
    public void register(String nickname, String password) throws SQLException {
        boolean successfull = false;
        Authenticator db = new Authenticator();
        if (nickname == null || password == null) {
            throw new IllegalArgumentException("Nickname and password cannot be null");
        }
        successfull = db.registerUser(nickname, password);
        if (!successfull) {
            throw new IllegalArgumentException("Registration failed");
        } else {
            login(nickname, password);
        }
    }
}
