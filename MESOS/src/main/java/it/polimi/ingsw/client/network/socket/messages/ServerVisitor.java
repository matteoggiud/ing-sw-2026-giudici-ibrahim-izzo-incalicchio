package it.polimi.ingsw.client.network.socket.messages;

import it.polimi.ingsw.client.network.socket.ClientSessionState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.server.network.socket.messages.GenericErrorMessage;
import it.polimi.ingsw.server.network.socket.messages.game.*;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginNeededMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.DisconnectSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;
import it.polimi.ingsw.server.model.observer.dto.*;


public class ServerVisitor implements ServerMessageVisitor {

    private final ClientSessionState sessionState;
    private final ClientView view;

    public ServerVisitor(ClientSessionState sessionState, ClientView view) {
        this.sessionState = sessionState;
        this.view = view;
    }

    public ClientSessionState getSessionState() {
        return sessionState;
    }

    @Override
    public void visit(LoginNeededMessage message) {
        // This can be handled by the main app loop now
        view.showMessage("Login is required. Please use the 'login' command.");
    }

    @Override
    public void visit(UpdatePlayersMessage message) {
        // view.showPlayers(message.getPlayers()); // Example
    }

    @Override
    public void visit(GameStartedMessage message) {
        sessionState.setActiveGameId(message.getGameId());

        view.showMessage("Game " + message.getGameId() + " started.");
        view.showMessage("Players: " + String.join(", ", message.getPlayerNicknames()));
    }


    @Override
    public void visit(BoardUpdatedMessage message) {
        sessionState.setActiveGameId(message.getGameId());

        view.showMessage("Board updated.");
    }



    @Override
    public void visit(LoginFailedMessage message) {
        view.showError(message.getMessage());
        sessionState.setNickname(null);
    }

    @Override
    public void visit(LoginSuccessMessage message) {
        view.showMessage(message.getMessage());
        sessionState.setNickname(message.getUsername());
    }

    @Override
    public void visit(AvailableGamesMessage message) {
        view.showAvailableGames(message.getGames());
    }

    @Override
    public void visit(RegisterFailedMessage message) {
        view.showError(message.getMessage());
        sessionState.setNickname(null);
    }

    @Override
    public void visit(RegisterSuccessMessage message) {
        view.showMessage(message.getMessage());
        sessionState.setNickname(message.getUsername());
    }

    @Override
    public void visit(GenericErrorMessage message) {
        view.showError(message.getMessage());
    }

    @Override
    public void visit(EraChangedMessage message) {
    }

    @Override
    public void visit(PlaceTotemTurnChangedMessage message) {
        sessionState.setActiveGameId(message.getGameId());

        view.showMessage("Place totem turn: " + message.getCurrentPlayerNickname());

        if (message.getCurrentPlayerNickname().equals(sessionState.getNickname())) {
            view.showMessage("It is your turn. Use the place command when available.");
        }
    }


    @Override
    public void visit(ResolveActionTurnChangedMessage message) {
    }

    @Override
    public void visit(ExtraActionTurnChangedMessage message) {
    }

    @Override
    public void visit(PlayerResourcesUpdatedMessage message) {
    }

    @Override
    public void visit(PlayerDeckUpdatedMessage message) {
    }

    @Override
    public void visit(GameEndedMessage message) {
    }

    @Override
    public void visit(GameInfoMessage message) {
        view.showGameInfo(message.getGameInfo());
    }

    @Override
    public void visit(DisconnectSuccessMessage message) {
        view.showMessage(message.getMessage());
        sessionState.disconnect();
    }
}
