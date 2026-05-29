package it.polimi.ingsw.common.network.socket_messages.client.registration;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

public class RegisterSubmitMessage extends ClientMessage {
    private final String username;
    private final String password;

    public RegisterSubmitMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return password;
    }

    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
