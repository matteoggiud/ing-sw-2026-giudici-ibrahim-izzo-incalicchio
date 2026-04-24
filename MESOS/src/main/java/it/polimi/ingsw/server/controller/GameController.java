package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;

import java.sql.SQLException;
import java.util.UUID;

public class GameController {
    private GamePortal tunnel;

    /**
     * Constructs a GameController with the specified GamePortal instance.
     *
     * @param tunnel the GamePortal instance that serves as the main entry point for interacting with the game model
     */
    public GameController(GamePortal tunnel) {
        this.tunnel = tunnel;
    }

    /**
     * Allows a player to log in to the game using their username and password
     *
     * @param username the username of the player attempting to log in
     * @param password the password of the player attempting to log in
     */
    public void login(String username, String password) {
        try{
            tunnel.login(username, password);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Allows a new player to register an account using a username and password
     *
     * @param username the desired username for the new player account
     * @param password the desired password for the new player account
     */
    public void register(String username, String password) {
        try {
            tunnel.register(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows a player to join an existing game using the game's unique identifier and the player's username
     *
     * @param game the unique identifier of the game the player wishes to join
     * @param player the username of the player attempting to join the game
     */
    public void join(UUID game, String player) {
        tunnel.joinGame(game, player);
    }

    /**
     * Allows a player to leave a game they have joined
     *
     * @param game the unique identifier of the game the player wishes to leave
     * @param player the username of the player attempting to leave the game
     */
    public void leaveGame(UUID game, String player) {
        tunnel.leaveGame(game, player);
    }

    /**
     * Allows a player to log out of the game, ending their session and disconnecting them from the server
     *
     * @param player the username of the player attempting to log out
     */
    public void logout(String player) {
        tunnel.logout(player);
    }

    /**
     * Allows a player to create a new game, which will be added to the list of available games that other players can join
     *
     * @param player the username of the player attempting to create a new game
     */
    public void createGame(String player) {
        tunnel.createGame(player);
    }

    /**
     * Allows a player to draw a card from the game
     * @param game the game from which the player is drawing a card
     * @param player the player who is drawing the card
     * @param selectedCard the card that the player has selected to draw
     * @param fromUpperRow a boolean indicating whether the card is being drawn from the upper row of the game board
     */
    public void drawCard(Game game, Player player, Card selectedCard, boolean fromUpperRow){
        tunnel.drawCard(game, player, selectedCard, fromUpperRow);
    }

    /**
     * Allows a player to place a Totem on the OfferBox
     *
     * @param game the game in which the player is placing a Totem
     * @param player the player who is placing the Totem
     * @param offerBox the OfferBox on which the player is placing the Totem
     */
    public void placing(Game game, Player player, OfferBox offerBox){
        tunnel.placing(game, player, offerBox);
    }

    /**
     * Retrieves a list of available games that players can join
     */
    public void getAvailableGames(){
        tunnel.getAvailableGames();
    }
}
