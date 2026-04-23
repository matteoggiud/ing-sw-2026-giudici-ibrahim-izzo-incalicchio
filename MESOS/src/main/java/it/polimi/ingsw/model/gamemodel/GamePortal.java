package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.menu.GameManager;
import it.polimi.ingsw.model.menu.PlayerManager;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * The GamePortal class serves as the main entry point for interacting with the game model.
 * It implements the IGameModel interface, providing concrete implementations for user authentication and game actions such as drawing cards and placing totems.
 *
 */
public class GamePortal implements IGameModel {

    private final PlayerManager playerManager;

    private final GameManager gameManager;

    public GamePortal() {
        this.playerManager = new PlayerManager();
        this.gameManager = new GameManager();
    }


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
    public void placing(Game game, Player player, OfferBox offerBox) {
        if (game == null || player == null || offerBox == null) {
            throw new IllegalArgumentException("Game, player e offerBox cannot be null");
        }

        game.getGameState().placeTotem(game, player, offerBox);
    }

    /**
     * Creates a new game with the specified host player.
     *
     * @param hostNickname the player who will host the new game
     * @return the unique identifier of the newly created game
     */
    @Override
    public UUID createGame(String hostNickname) {
        Player host = playerManager.getPlayer(hostNickname);
        if (host == null) {
            throw new IllegalStateException("Host player with nickname " + hostNickname + " does not exist.");
        }
        return gameManager.createGame(host);
    }

    /**
     * Gets all available games that players can join.
     *
     * @return a list of available games
     */
    @Override
    public List<Game> getAvailableGames() {
        return gameManager.getAvailableGames();
    }

    /**
     * Allows a player to join an existing game using the game's unique identifier (UUID).
     *
     * @param gameId the unique identifier of the game the player wants to join
     * @param player the player attempting to join the game
     */
    @Override
    public void joinGame(UUID gameId, String player) {
        Player playerToJoin = playerManager.getPlayer(player);
        if (playerToJoin == null) {
            throw new IllegalStateException("Player with nickname " + player + " does not exist.");
        }
        gameManager.joinGame(gameId, playerToJoin);
    }

    /**
     * Allows a player to leave a game they have joined using the game's unique identifier (UUID).
     *
     * @param gameId the unique identifier of the game the player wants to leave
     * @param player the player attempting to leave the game
     */
    @Override
    public void leaveGame(UUID gameId, String player) {
        Player playerToLeave = playerManager.getPlayer(player);
        if (playerToLeave == null) {
            throw new IllegalStateException("Player with nickname " + player + " does not exist.");
        }
        gameManager.leaveGame(gameId, playerToLeave);

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
        if (successfull) {
            playerManager.addPlayer(new Player(nickname));
        } else {
            throw new IllegalArgumentException("Invalid nickname or password");
        }
    }

    @Override
    public void logout(String nickname) throws IllegalArgumentException{
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null");
        }
        playerManager.removePlayer(nickname);
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
