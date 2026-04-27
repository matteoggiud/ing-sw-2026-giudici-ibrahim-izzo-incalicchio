package it.polimi.ingsw.client.network.socket.messages;

import it.polimi.ingsw.server.network.socket.messages.GenericErrorMessage;
import it.polimi.ingsw.server.network.socket.messages.game.*;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginNeededMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;



public interface ServerMessageVisitor {


    /**
     *
     * Visits a LoginNeededMessage, which indicates that the client needs to log in before performing any other action.
     *
     * @param message the LoginNeededMessage to visit
     */
    void visit(LoginNeededMessage message);

    /**
     * Visits an UpdatePlayersMessage, which contains the list of players currently in the game. This message is sent to all clients whenever a player joins or leaves the game.
     *
     * @param message the UpdatePlayersMessage to visit
     */
    void visit(UpdatePlayersMessage message);

    /**
     * Visits a GameStartedMessage, which indicates that the game has started and contains the initial state of the game board. This message is sent to all clients when the game starts.
     *
     * @param message the GameStartedMessage to visit
     */
    void visit(GameStartedMessage message);

    /**
     * Visits a BoardUpdatedMessage, which contains the updated state of the game board after a player's action. This message is sent to all clients whenever the game board changes.
     *
     * @param message the BoardUpdatedMessage to visit
     */
    void visit(BoardUpdatedMessage message);

    /**
     * Visits a LoginFailedMessage, which indicates that the client's login attempt has failed. This message is sent to the client when the login attempt fails, for example due to incorrect credentials or a username that is already in use.
     *
     * @param message the LoginFailedMessage to visit
     */
    void visit(LoginFailedMessage message);

    /**
     * Visits a LoginSuccessMessage, which indicates that the client's login attempt has succeeded. This message is sent to the client when the login attempt is successful, and may contain additional information such as the client's username or a welcome message.
     *
     * @param message the LoginSuccessMessage to visit
     */
    void visit(LoginSuccessMessage message);

    /**
     * Visits an AvailableGamesMessage, which contains a list of available games that the client can join. This message is sent to the client when they request the list of available games, and may be updated periodically as new games are created or existing games are filled.
     *
     * @param message the AvailableGamesMessage to visit
     */
    void visit(AvailableGamesMessage message);

    /**
     * Visits a RegisterFailedMessage, which indicates that the client's registration attempt has failed. This message is sent to the client when the registration attempt fails, for example due to a username that is already in use or invalid registration information.
     * @param message the RegisterFailedMessage to visit
     */
    void visit(RegisterFailedMessage message);

    /**
     * Visits a RegisterSuccessMessage, which indicates that the client's registration attempt has succeeded. This message is sent to the client when the registration attempt is successful, and may contain additional information such as the client's username or a welcome message.
     * @param message the RegisterSuccessMessage to visit
     */
    void visit(RegisterSuccessMessage message);

    /**
     * Visits a GenericErrorMessage, which indicates that an error has occurred on the server. This message is sent to the client when an unexpected error occurs, and may contain additional information about the error such as an error code or a detailed error message.
     *
     * @param message the GenericErrorMessage to visit
     */
    void visit(GenericErrorMessage message);

    /**
     * Visits an EraChangedMessage, which indicates that the game era has changed.
     *
     * @param message the EraChangedMessage to visit
     */
    void visit(EraChangedMessage message);

    /**
     * Visits a PlaceTotemTurnChangedMessage, which indicates the turn changed during the totem placement phase.
     *
     * @param message the PlaceTotemTurnChangedMessage to visit
     */
    void visit(PlaceTotemTurnChangedMessage message);

    /**
     * Visits a ResolveActionTurnChangedMessage, which indicates the turn changed during the action resolution phase.
     *
     * @param message the ResolveActionTurnChangedMessage to visit
     */
    void visit(ResolveActionTurnChangedMessage message);

    /**
     * Visits an ExtraActionTurnChangedMessage, which indicates the turn changed during the extra action phase.
     *
     * @param message the ExtraActionTurnChangedMessage to visit
     */
    void visit(ExtraActionTurnChangedMessage message);

    /**
     * Visits a PlayerResourcesUpdatedMessage, which indicates that a player's resources have changed.
     *
     * @param message the PlayerResourcesUpdatedMessage to visit
     */
    void visit(PlayerResourcesUpdatedMessage message);

    void visit(PlayerDeckUpdatedMessage message);
}



