package it.polimi.ingsw.server.network.socket.messages;

import it.polimi.ingsw.client.network.socket.messages.game.*;
import it.polimi.ingsw.client.network.socket.messages.login.LoginSubmitMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.DisconnectMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetRankingsMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.LeftGameMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;
import it.polimi.ingsw.common.CommandHandler;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import it.polimi.ingsw.server.network.ClientSession;
import it.polimi.ingsw.server.network.GenericVirtualView;
import it.polimi.ingsw.server.network.SessionRegistry;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.DisconnectSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.LeftGameSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.RankingsMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;

import java.util.List;

public class ClientVisitor implements ClientMessageVisitor {

    private final GameController gameController;
    private final ClientSession clientSession;
    private final CommandHandler commandHandler;

    public ClientVisitor(GameController gameController, ClientSession clientSession) {
        this.gameController = gameController;
        this.clientSession = clientSession;
        this.commandHandler = new CommandHandler(gameController, clientSession);
    }

    /**
     * Returns the client session associated with this visitor.
     *
     * @return the ClientSession instance
     */
    public ClientSession getClientSession() {
        return clientSession;
    }

    /**
     * Returns the game controller associated with this visitor.
     *
     * @return the GameController instance
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Handles the client's request to log in.
     *
     * @param message the message containing the login credentials
     */
    @Override
    public void visit(LoginSubmitMessage message) {
        commandHandler.handleLogin(message.getUsername(), message.getPasswordHash());
    }

    /**
     * Handles the client's request to register a new account.
     *
     * @param message the message containing the registration credentials
     */
    @Override
    public void visit(RegisterSubmitMessage message) {
        commandHandler.handleRegistration(message.getUsername(), message.getPasswordHash());
    }

    /**
     * Handles the client's request to create a new game.
     *
     * @param message the message containing the game creation details
     */
    @Override
    public void visit(CreateGameMessage message) {
        commandHandler.handleGameCreation(message.getExpectedNumOfPlayers());
    }

    /**
     * Handles the client's request to join an existing game.
     *
     * @param message the message containing the game ID to join
     */
    @Override
    public void visit(JoinGameMessage message) {
        commandHandler.handleJoinGame(message.getGameId());
    }

    /**
     * Handles the client's request to retrieve the list of available games.
     *
     * @param message the message requesting available games
     */
    @Override
    public void visit(GetAvailableGamesMessage message) {
        commandHandler.handleGetAvailableGames();
    }

    /**
     * Handles the client's request to disconnect from the server.
     *
     * @param message the disconnect message from the client
     */
    @Override
    public void visit(DisconnectMessage message) {
        commandHandler.handleDisconnection();
    }

    /**
     * Handles the client's request to leave an active game.
     *
     * @param message the message indicating the client wants to leave the game
     */
    @Override
    public void visit(LeftGameMessage message) {
        commandHandler.handleLeftGame();
    }

    /**
     * Handles the client's request to place a totem on the game board.
     *
     * @param message the message containing the details of the place totem action
     */
    @Override
    public void visit(PlaceTotemMessage message) {
        commandHandler.handlePlaceTotem(message.getOfferBoxPosition());
    }

    /**
     * Handles the client's request to draw a card from the game.
     *
     * @param message the message containing the details of the draw card action
     */
    @Override
    public void visit(DrawCardMessage message) {
        commandHandler.handleDrawCard(message.getSelectedCardId(), message.isFromUpperRow());
    }

    /**
     * Handles the client's request to draw an extra card from the game.
     *
     * @param message the message containing the details of the extra draw card action
     */
    @Override
    public void visit(ExtraDrawMessage message) {
        commandHandler.handleExtraDraw(message.getSelectedCardId());
    }

    @Override
    public void visit(GetRankingsMessage message) {
        commandHandler.handleRankings(message.getNumPlayers());
    }
}
