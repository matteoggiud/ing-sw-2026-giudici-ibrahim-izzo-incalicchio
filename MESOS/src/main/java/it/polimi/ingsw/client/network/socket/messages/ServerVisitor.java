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
import it.polimi.ingsw.server.network.socket.messages.menu.LeftGameSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;

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
        view.showMessage("Login is required. Please use the 'login' command.");
    }

    @Override
    public void visit(UpdatePlayersMessage message) {
        view.showMessage("Players: " + String.join(", ", message.getPlayerNicknames()));
    }

    @Override
    public void visit(GameStartedMessage message) {
        sessionState.setActiveGameId(message.getGameId());
        sessionState.clearPlayersInfo();
        sessionState.setCurrentPhase("STARTED");
        sessionState.setStatusMessage("Game started.");
        view.showGameStarted(message.getGameId(), message.getPlayerNicknames());
    }


    @Override
    public void visit(BoardUpdatedMessage message) {
        sessionState.setBoard(message.getBoard());
        view.showBoardSummary(message.getBoard());
    }

    @Override
    public void visit(LoginFailedMessage message) {
        view.showError(message.getMessage());
        sessionState.setNickname(null);
    }

    @Override
    public void visit(LoginSuccessMessage message) {
        sessionState.setNickname(message.getUsername());
        sessionState.setCurrentPhase("MENU");
        sessionState.setStatusMessage(message.getMessage());
        view.showMessage(message.getMessage());
    }


    @Override
    public void visit(AvailableGamesMessage message) {
        sessionState.setAvailableGames(message.getGames());
        sessionState.setCurrentPhase("MENU");
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
        sessionState.setErrorMessage(message.getMessage());
        view.showError(message.getMessage());
    }

    @Override
    public void visit(EraChangedMessage message) {
        sessionState.setStatusMessage("Era changed: " + message.getNewEra());
        view.showMessage("Era changed: " + message.getNewEra());
    }

    @Override
    public void visit(PlaceTotemTurnChangedMessage message) {
        boolean isMyTurn = message.getCurrentPlayerNickname().equals(sessionState.getNickname());
        view.showPlaceTotemTurn(message.getCurrentPlayerNickname(), isMyTurn);
    }

    @Override
    public void visit(ResolveActionTurnChangedMessage message) {
        boolean isMyTurn = message.getCurrentPlayerNickname().equals(sessionState.getNickname());
        view.showResolveActionTurn(message.getCurrentPlayerNickname(), isMyTurn);
    }

    @Override
    public void visit(ExtraActionTurnChangedMessage message) {
        boolean isMyTurn = message.getCurrentPlayerNickname().equals(sessionState.getNickname());
        view.showExtraActionTurn(message.getCurrentPlayerNickname(), isMyTurn);
    }

    @Override
    public void visit(PlayerResourcesUpdatedMessage message) {
        // Deprecated for the TUI: player stats are shown from PlayerInfoChangedMessage snapshots.
    }

    @Override
    public void visit(PlayerDeckUpdatedMessage message) {
        view.showPlayerDeck(
                message.getPlayerNickname(),
                message.getPlayerDeck(),
                message.getPlayerNickname().equals(sessionState.getNickname())
        );
    }

    @Override
    public void visit(GameEndedMessage message) {
        sessionState.setActiveGameId(null);
        sessionState.setCurrentPhase("GAME_ENDED");
        sessionState.setFinalRanking(message.getFinalRanking());
        view.showGameEnded(message.getGameId(), message.getFinalRanking());
    }


    @Override
    public void visit(GameInfoMessage message) {
        sessionState.setActiveGameId(message.getGameInfo().gameId());
        sessionState.setCurrentGameInfo(message.getGameInfo());
        sessionState.setCurrentPhase("LOBBY");
        view.showGameInfo(message.getGameInfo());
    }


    @Override
    public void visit(DisconnectSuccessMessage message) {
        view.showMessage(message.getMessage());
        sessionState.disconnect();
    }

    @Override
    public void visit(LeftGameSuccessMessage message) {
        sessionState.clearGameState();
        sessionState.setStatusMessage(message.getMessage());
        view.showMessage(message.getMessage());
    }


    @Override
    public void visit(PlayerInfoChangedMessage message) {
        sessionState.updatePlayersInfo(message.getPlayerInfos());
        view.showPlayersInfo(sessionState.getPlayersInfo(), sessionState.getNickname());
    }


    @Override
    public void visit(ResolvingEventMessage resolvingEventMessage) {
        sessionState.setResolvingEventName(resolvingEventMessage.getEventCard().name());
        view.showMessage("Resolving event: " + resolvingEventMessage.getEventCard().name());
    }

}
