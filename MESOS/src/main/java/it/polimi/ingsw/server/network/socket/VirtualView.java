package it.polimi.ingsw.server.network.socket;


import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public interface VirtualView extends IGameObserver {
    void sendMessage(ServerMessage message);
}
