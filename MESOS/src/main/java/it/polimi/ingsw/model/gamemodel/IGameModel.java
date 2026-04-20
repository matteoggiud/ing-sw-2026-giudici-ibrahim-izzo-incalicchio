package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.sql.SQLException;

/**
 * The IGameModel interface defines the core functionalities of the game model,
 * including user authentication and game actions such as drawing cards and placing totems.
 * It serves as a contract for any class that implements it,
 * ensuring that the necessary methods for game interaction are provided.
 */
public interface IGameModel {

    /**
     * Attempts to log in a user with the provided nickname and password.
     *
     * @param nickname the nickname of the user trying to log in
     * @param password the password of the user trying to log in
     * @throws SQLException if there is an error accessing the database or if the login credentials are invalid
     */
    void login(String nickname, String password) throws SQLException;

    /**
     * Attempts to register a new user with the provided nickname and password.
     *
     * @param nickname the nickname of the user trying to register
     * @param password the password of the user trying to register
     * @throws SQLException if there is an error accessing the database or if the registration fails
     */
    void register(String nickname, String password) throws SQLException;

    /**
     * Allows a player to draw a card from either the upper or lower row of the offer track.
     *
     * @param game         the current game context
     * @param player       the player attempting to draw the card
     * @param selectedCard the card the player has selected to draw
     * @param fromUpperRow a boolean indicating whether the card is being drawn from the upper row (true) or lower row (false)
     */
    void drawCard(Game game, Player player, Card selectedCard, boolean fromUpperRow);

    /**
     * Allows a player to place their totem on a specific offer box on the offer track.
     *
     * @param game     the current game context
     * @param player   the player attempting to place their totem
     * @param offerBox the offer box on which the player wants to place their totem
     */
    void Placing(Game game, Player player, OfferBox offerBox);
}
