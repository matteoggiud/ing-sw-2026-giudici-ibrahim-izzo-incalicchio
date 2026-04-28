package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.network.socket.messages.game.CreateGameMessage;
import it.polimi.ingsw.client.network.socket.messages.game.JoinGameMessage;
import it.polimi.ingsw.client.network.socket.messages.login.LoginSubmitMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.DisconnectMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.GetAvailableGamesMessage;
import it.polimi.ingsw.client.network.socket.messages.menu.LeftGameMessage;
import it.polimi.ingsw.client.network.socket.messages.registration.RegisterSubmitMessage;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;

public class ClientController {
    private final ClientView view;
    private final VirtualServer network;


    public ClientController(ClientView view, VirtualServer network) {
        this.view = view;
        this.network = network;
    }

    public void login() {
        Credentials credentials = view.requestLoginCredentials();
        network.sendMessage(new LoginSubmitMessage(credentials.nickname(), credentials.passwordHash()));
    }

    public void register() {
        Credentials credentials = view.requestRegisterCredentials();
        network.sendMessage(new RegisterSubmitMessage(credentials.nickname(), credentials.passwordHash()));
    }

    public void showError(String message) {
        view.showError(message);
    }

    public void showMessage(String message) {
        view.showMessage(message);
    }

    public void createGame(int expectedNumOfPlayers) {
        network.sendMessage(new CreateGameMessage(expectedNumOfPlayers));
    }

    public void joinGame(int gameId) {
        network.sendMessage(new JoinGameMessage(gameId));
    }

    public void getAvailableGames() {
        network.sendMessage(new GetAvailableGamesMessage());
    }

    public void disconnect() {
        network.sendMessage(new DisconnectMessage());
    }

    public void leftGame() {
        network.sendMessage(new LeftGameMessage());
    }
}
