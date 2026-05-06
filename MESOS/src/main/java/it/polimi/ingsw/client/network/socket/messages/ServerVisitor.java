package it.polimi.ingsw.client.network.socket.messages;

import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.socket.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.server.network.socket.messages.GenericErrorMessage;
import it.polimi.ingsw.server.network.socket.messages.game.*;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginNeededMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.DisconnectSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.LeftGameSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.RankingsMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;

import javax.swing.text.View;

public class ServerVisitor implements ServerMessageVisitor {

    private final ViewController viewController;

    public ServerVisitor(ViewController viewController) {
        this.viewController = viewController;
    }

    public ViewController getViewController() {
        return viewController;
    }

    @Override
    public void visit(LoginNeededMessage message) {
        viewController.onAuthenticationNeeded(message.getMessage());
    }

    @Override
    public void visit(UpdatePlayersMessage message) {
        viewController.onPlayerChanged(message.getPlayerNicknames());
    }

    @Override
    public void visit(GameStartedMessage message) {
        viewController.onGameStarted(message.getGameId(), message.getPlayerNicknames());
    }


    @Override
    public void visit(BoardUpdatedMessage message) {
        viewController.onBoardChanged(message.getGameId(), message.getBoard());
    }

    @Override
    public void visit(LoginFailedMessage message) {
        viewController.onLoginFailed(message.getMessage(), message.getUsername());
    }

    @Override
    public void visit(LoginSuccessMessage message) {
        viewController.onLoginSuccess(message.getMessage(), message.getUsername());
    }


    @Override
    public void visit(AvailableGamesMessage message) {
        viewController.onAvailableGames(message.getGames());
    }

    @Override
    public void visit(RegisterFailedMessage message) {
        viewController.onRegisterFailed(message.getMessage(), message.getUsername());
    }

    @Override
    public void visit(RegisterSuccessMessage message) {
        viewController.onRegisterSuccess(message.getMessage(), message.getUsername());
    }

    @Override
    public void visit(GenericErrorMessage message) {
        viewController.onGenericError(message.getMessage());
    }

    @Override
    public void visit(EraChangedMessage message) {
        viewController.onEraChanged(message.getGameId(), message.getNewEra());
    }

    @Override
    public void visit(PlaceTotemTurnChangedMessage message) {
        viewController.onPlaceTotemTurnChanged(message.getGameId(), message.getCurrentPlayerNickname());
    }

    @Override
    public void visit(ResolveActionTurnChangedMessage message) {
        viewController.onResolveActionTurnChanged(message.getGameId(), message.getCurrentPlayerNickname());
    }

    @Override
    public void visit(ExtraActionTurnChangedMessage message) {
        viewController.onExtraActionTurnChanged(message.getGameId(), message.getCurrentPlayerNickname());
    }

    @Override
    @Deprecated
    public void visit(PlayerResourcesUpdatedMessage message) {
        // Deprecated for the TUI: player stats are shown from PlayerInfoChangedMessage snapshots.
    }

    @Override
    public void visit(PlayerDeckUpdatedMessage message) {
        viewController.onPlayerDeckUpdated(message.getGameId(), message.getPlayerNickname(), message.getPlayerDeck());
    }

    @Override
    public void visit(GameEndedMessage message) {
        viewController.onGameEnded(message.getGameId(), message.getFinalRanking(), message.getNumPlayers(), message.getHistoricalLeaderboard());
    }


    @Override
    public void visit(GameInfoMessage message) {
        viewController.onJoinedGame(message.getGameInfo());
    }


    @Override
    public void visit(DisconnectSuccessMessage message) {
        viewController.onDisconnectionSuccess(message.getMessage());
    }

    @Override
    public void visit(LeftGameSuccessMessage message) {
        viewController.onLeftGameSuccess(message.getMessage());
    }


    @Override
    public void visit(PlayerInfoChangedMessage message) {
        viewController.onPlayerInfosChanged(message.getGameId(), message.getPlayerInfos());
    }


    @Override
    public void visit(ResolvingEventMessage message) {
        viewController.onResolvingEvent(message.getGameId(), message.getEventCard());
    }

    @Override
    public void visit(RankingsMessage message) {
        viewController.onRankingsReceived(message.getRankings());
    }

    @Override
    public void visit(GameCrashedMessage gameCrashedMessage) {
        viewController.onGameCrashed(gameCrashedMessage.getMessage());
    }
}
