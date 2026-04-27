package it.polimi.ingsw.server.model.gamemodel;

import it.polimi.ingsw.server.controller.VirtualView;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;

import java.util.Map;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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
     * Logs out the user with the specified nickname.
     *
     * @param nickname the nickname of the user to log out
     * @throws IllegalArgumentException
     */
    void logout(String nickname) throws IllegalArgumentException;

    /**
     * Retrieves a currently logged-in player by nickname.
     *
     * @param nickname the nickname of the player to retrieve
     * @return the player associated with the given nickname
     * @throws IllegalArgumentException if the nickname is null
     * @throws IllegalStateException    if no logged-in player exists with that nickname
     */
    Player getLoggedPlayer(String nickname);

    /**
     * Checks whether a player is currently logged in.
     *
     * @param nickname the nickname of the player to check
     * @return true if the player is logged in, false otherwise
     * @throws IllegalArgumentException if the nickname is null
     */
    boolean isLoggedIn(String nickname);


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
     * @param gameId         the current game context
     * @param nickname       the player attempting to draw the card
     * @param selectedCardId the card the player has selected to draw
     * @param fromUpperRow   a boolean indicating whether the card is being drawn from the upper row (true) or lower row (false)
     */
    void drawCard(int gameId, String nickname, int selectedCardId, boolean fromUpperRow);

    /**
     * Allows a player to place their totem on a specific offer box on the offer track.
     *
     * @param gameId   the current game context
     * @param nickname the player attempting to place their totem
     * @param offerBox the offer box on which the player wants to place their totem
     */
    void placing(int gameId, String nickname, OfferBox offerBox);

    /**
     * Creates a new game with the specified host player.
     *
     * @param hostPlayer the player who will host the new game
     * @return the unique identifier (UUID) of the newly created game
     */
    int createGame(String hostPlayer);

    /**
     * Creates a new game with the specified host player and expected player count.
     * @param hostPlayer the player who will host the new game
     * @param expectedPlayerCount the expected number of players for the game
     * @return the unique identifier (UUID) of the newly created game
     */
    int createGame(String hostPlayer, int expectedPlayerCount);

    /**
     * Checks if the game with the specified id is ready to start (i.e., has enough players)
     * and starts it if it is ready.
     * @param gameId the unique identifier of the game to check and potentially start
     */
    void startGameIfReady(int gameId);


    /**
     * Retrieves an active game by its unique identifier.
     *
     * @param gameId the unique identifier of the game
     * @return the game associated with the given id
     * @throws IllegalArgumentException if the game id is null or no game exists with that id
     */
    Game getGame(int gameId);

    /**
     * Starts an existing game.
     * Only the host player is allowed to start the game, and the game must have
     * at least the minimum number of players required by the rules.
     *
     * @param gameId       the unique identifier of the game to start
     * @param hostNickname the nickname of the player requesting the start
     * @throws IllegalArgumentException if any argument is null
     * @throws IllegalStateException    if the requester is not the host or there are not enough players
     */
    void startGame(int gameId, String hostNickname);


    /**
     * Retrieves a list of all available games that players can join.
     *
     * @return a list of available games
     */
    List<GameInfoDTO> getAvailableGames();

    /**
     * Allows a player to join an existing game using the game's unique identifier (UUID).
     *
     * @param gameId the unique identifier of the game the player wants to join
     * @param player the player attempting to join the game
     */
    void joinGame(int gameId, String player);

    /**
     * Allows a player to leave a game they have joined using the game's unique identifier (UUID).
     *
     * @param gameId the unique identifier of the game the player wants to leave
     * @param player the player attempting to leave the game
     */
    void leaveGame(int gameId, String player);

    /**
     * Retrieves the final ranking of a game when the game has reached
     * the scoring phase.
     *
     * @param gameId the unique identifier of the game
     * @return a map of players and their final scores sorted by ranking
     * @throws IllegalArgumentException if the game id is null
     * @throws IllegalStateException    if the game is not in a phase that exposes the final ranking yet
     */
    Map<Player, Integer> getFinalRanking(int gameId);

    /**
     * Checks whether the specified player has a pending extra action in the given game.
     *
     * @param gameId   the unique identifier of the game
     * @param nickname the nickname of the player to check
     * @return true if the player has a pending extra action, false otherwise
     * @throws IllegalArgumentException if any argument is null or invalid
     * @throws IllegalStateException    if the player is not logged in
     */
    boolean hasPendingExtraAction(int gameId, String nickname);

    /**
     * Retrieves the final ranking of a game using player nicknames as keys.
     * This representation is more suitable for TUI and controller code.
     *
     * @param gameId the unique identifier of the game
     * @return a map of player nicknames and their final scores sorted by ranking
     * @throws IllegalArgumentException if the game id is null
     * @throws IllegalStateException    if the game is not in a phase that exposes the final ranking yet
     */
    Map<String, Integer> getFinalRankingByNickname(int gameId);

    /**
     * Adds a virtual view as an observer to the game with the specified id, allowing the virtual view to receive updates about the game's state and events.
     *
     * @param gameId      the unique identifier of the game to which the virtual view will be added as an observer
     * @param virtualView the virtual view to be added as an observer to the specified game
     */
    void addObserver(int gameId, IGameObserver virtualView);

    /**
     * Removes a virtual view as an observer from the game with the specified id, preventing the virtual view from receiving further updates about the game's state and events.
     *
     * @param gameId the unique identifier of the game from which the virtual view will be removed as an observer
     * @param virtualView  the virtual view to be removed as an observer from the specified game
     */
    void removeObserver(int gameId, IGameObserver virtualView);

    /**
     * Marks a player as disconnected
     * @param gameId the unique identifier of the game in which the player is participating
     * @param nickname the nickname of the player to be marked as disconnected
     */
    void markPlayerDisconnected(int gameId, String nickname);

    /**
     * Marks a player as reconnected
     * @param nickname the nickname of the player to be marked as reconnected
     *
     * @return the unique identifier of the game in which the player is participating
     */
    Integer getActiveGameIdForPlayer(String nickname);

}
