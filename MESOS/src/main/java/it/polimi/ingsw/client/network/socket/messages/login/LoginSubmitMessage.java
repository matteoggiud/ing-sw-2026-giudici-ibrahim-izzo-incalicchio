package it.polimi.ingsw.client.network.socket.messages.login;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

public class LoginSubmitMessage extends ClientMessage {
    private final String username;
    private final String passwordHash;

    public LoginSubmitMessage(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public void dispatch(ClientMessageVisitor  clientVisitor) {
        clientVisitor.visit(this);
    }
}
