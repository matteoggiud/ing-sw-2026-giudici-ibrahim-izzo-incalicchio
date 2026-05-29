package it.polimi.ingsw.common.network.socket_messages.visitors;

import it.polimi.ingsw.common.network.socket_messages.client.game.*;
import it.polimi.ingsw.common.network.socket_messages.client.menu.*;
import it.polimi.ingsw.common.network.socket_messages.client.login.LoginSubmitMessage;
import it.polimi.ingsw.common.network.socket_messages.client.registration.RegisterSubmitMessage;
import it.polimi.ingsw.server.network.CommandHandler;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.network.ClientSession;

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

    @Override
    public void visit(LeftLobbyMessage message){
        commandHandler.handleLeftLobby();
    }
}
