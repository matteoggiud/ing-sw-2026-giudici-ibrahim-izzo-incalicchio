package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;

public interface VirtualServer {

    void login(String username, String passwordHash);

    void register(String username, String password);

    void createGame();

    void joinGame(int gameId);

    void disconnect();
}
