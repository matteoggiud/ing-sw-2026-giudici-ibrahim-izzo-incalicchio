package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;

public interface VirtualServer {

   void sendMessage(ClientMessage message);

}
