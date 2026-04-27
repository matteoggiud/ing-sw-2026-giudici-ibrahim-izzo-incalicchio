package it.polimi.ingsw.client.network.socket.messages;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.socket.ClientSessionState;
import it.polimi.ingsw.server.network.socket.messages.GenericErrorMessage;
import it.polimi.ingsw.server.network.socket.messages.game.*;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginNeededMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;

//TODO: implement the visitor pattern for all the server messages
public class ServerVisitor implements ServerMessageVisitor {

    private final ClientSessionState sessionState;
    private final ClientController controller;

    public ServerVisitor(ClientSessionState sessionState, ClientController controller) {
        this.sessionState = sessionState;
        this.controller = controller;
    }

    public ClientSessionState getSessionState() {
        return sessionState;
    }

    @Override
    public void visit(LoginNeededMessage message) {
        controller.login();
    }

    /**
     * Visits an UpdatePlayersMessage, which contains the list of players currently in the game. This message is sent to all clients whenever a player joins or leaves the game.
     *
     * @param message the UpdatePlayersMessage to visit
     */
    @Override
    public void visit(UpdatePlayersMessage message) {

    }

    /**
     * Visits a GameStartedMessage, which indicates that the game has started and contains the initial state of the game board. This message is sent to all clients when the game starts.
     *
     * @param message the GameStartedMessage to visit
     */
    @Override
    public void visit(GameStartedMessage message) {

    }

    /**
     * Visits a BoardUpdatedMessage, which contains the updated state of the game board after a player's action. This message is sent to all clients whenever the game board changes.
     *
     * @param message the BoardUpdatedMessage to visit
     */
    @Override
    public void visit(BoardUpdatedMessage message) {

    }

    /**
     * Visits a LoginFailedMessage, which indicates that the client's login attempt has failed. This message is sent to the client when the login attempt fails, for example due to incorrect credentials or a username that is already in use.
     *
     * @param message the LoginFailedMessage to visit
     */
    @Override
    public void visit(LoginFailedMessage message) {
        controller.showError(message.getMessage());
        sessionState.setNickname(null);
    }

    /**
     * Visits a LoginSuccessMessage, which indicates that the client's login attempt has succeeded. This message is sent to the client when the login attempt is successful, and may contain additional information such as the client's username or a welcome message.
     *
     * @param message the LoginSuccessMessage to visit
     */
    @Override
    public void visit(LoginSuccessMessage message) {
        controller.showMessage(message.getMessage());
        sessionState.setNickname(message.getUsername());
    }

    /**
     * Visits an AvailableGamesMessage, which contains a list of available games that the client can join. This message is sent to the client when they request the list of available games, and may be updated periodically as new games are created or existing games are filled.
     *
     * @param message the AvailableGamesMessage to visit
     */
    @Override
    public void visit(AvailableGamesMessage message) {

    }

    /**
     * Visits a RegisterFailedMessage, which indicates that the client's registration attempt has failed. This message is sent to the client when the registration attempt fails, for example due to a username that is already in use or invalid registration information.
     *
     * @param message the RegisterFailedMessage to visit
     */
    @Override
    public void visit(RegisterFailedMessage message) {
    }

    /**
     * Visits a RegisterSuccessMessage, which indicates that the client's registration attempt has succeeded. This message is sent to the client when the registration attempt is successful, and may contain additional information such as the client's username or a welcome message.
     *
     * @param message the RegisterSuccessMessage to visit
     */
    @Override
    public void visit(RegisterSuccessMessage message) {
    }

    /**
     * Visits a GenericErrorMessage, which indicates that an error has occurred on the server. This message is sent to the client when an unexpected error occurs, and may contain additional information about the error such as an error code or a detailed error message.
     *
     * @param message the GenericErrorMessage to visit
     */
    @Override
    public void visit(GenericErrorMessage message) {
    }

    /**
     * Visits an EraChangedMessage, which indicates that the game era has changed.
     *
     * @param message the EraChangedMessage to visit
     */
    @Override
    public void visit(EraChangedMessage message) {
    }

    /**
     * Visits a PlaceTotemTurnChangedMessage, which indicates the turn changed during the totem placement phase.
     *
     * @param message the PlaceTotemTurnChangedMessage to visit
     */
    @Override
    public void visit(PlaceTotemTurnChangedMessage message) {
    }

    /**
     * Visits a ResolveActionTurnChangedMessage, which indicates the turn changed during the action resolution phase.
     *
     * @param message the ResolveActionTurnChangedMessage to visit
     */
    @Override
    public void visit(ResolveActionTurnChangedMessage message) {
    }

    /**
     * Visits an ExtraActionTurnChangedMessage, which indicates the turn changed during the extra action phase.
     *
     * @param message the ExtraActionTurnChangedMessage to visit
     */
    @Override
    public void visit(ExtraActionTurnChangedMessage message) {
    }

    /**
     * Visits a PlayerResourcesUpdatedMessage, which indicates that a player's resources have been updated.
     *
     * @param message the PlayerResourcesUpdatedMessage to visit
     */
    @Override
    public void visit(PlayerResourcesUpdatedMessage message) {
    }

    /**
     * Visits a PlayerDeckUpdatedMessage, which indicates that a player's deck has been updated.
     *
     * @param message the PlayerDeckUpdatedMessage to visit
     */
    @Override
    public void visit(PlayerDeckUpdatedMessage message) {
    }

    /**
     * Visits a GameEndedMessage, which indicates that the game has ended and contains the final ranking of the players.
     * This message is sent to all clients when the game ends.
     *
     * @param message the GameEndedMessage to visit
     */
    @Override
    public void visit(GameEndedMessage message) {
    }
}

