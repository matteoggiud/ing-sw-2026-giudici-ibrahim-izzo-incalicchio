package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.client.game.*;
import it.polimi.ingsw.common.network.socket_messages.client.menu.*;
import it.polimi.ingsw.common.network.socket_messages.client.login.LoginSubmitMessage;
import it.polimi.ingsw.common.network.socket_messages.client.registration.RegisterSubmitMessage;


public class SocketVirtualServer implements GenericVirtualServer {

    private final SocketServerConnection socketServerConnection;

    public SocketVirtualServer(SocketServerConnection socketServerConnection) {
        this.socketServerConnection = socketServerConnection;
    }

    public void sendMessage(ClientMessage message) {
        socketServerConnection.sendMessage(message);
    }

    @Override
    public void onLoginRequest(String username, String password) {
        sendMessage(new LoginSubmitMessage(username, password));
    }

    @Override
    public void onRegisterMessage(String username, String password) {
        sendMessage(new RegisterSubmitMessage(username, password));
    }

    @Override
    public void onCreateGameMessage(int numPlayers) {
        sendMessage(new CreateGameMessage(numPlayers));
    }

    @Override
    public void onJoinGameMessage(int gameId) {
        sendMessage(new JoinGameMessage(gameId));
    }

    @Override
    public void onGetAvailableGamesMessage() {
        sendMessage(new GetAvailableGamesMessage());
    }

    @Override
    public void onDisconnectMessage() {
        sendMessage(new DisconnectMessage());
    }

    @Override
    public void onPlaceTotemMessage(int offerBoxPosition) {
        sendMessage(new PlaceTotemMessage(offerBoxPosition));
    }

    @Override
    public void onLeftGameMessage() {
        sendMessage(new LeftGameMessage());
    }

    @Override
    public void onDrawCardMessage(int cardId, boolean fromUpperRow) {
        sendMessage(new DrawCardMessage(cardId, fromUpperRow));
    }

    @Override
    public void onDrawExtraCardMessage(int cardId) {
        sendMessage(new ExtraDrawMessage(cardId));
    }

    @Override
    public void onGetRankings(int numPlayers) {
        sendMessage(new GetRankingsMessage(numPlayers));
    }

    @Override
    public void onLeftLobbyMessage() {
        sendMessage(new LeftLobbyMessage());
    }
}
