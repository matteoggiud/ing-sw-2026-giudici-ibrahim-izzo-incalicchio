package it.polimi.ingsw.client.network.socket;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;

public interface VirtualServer {

   void sendMessage(ClientMessage message);

}
