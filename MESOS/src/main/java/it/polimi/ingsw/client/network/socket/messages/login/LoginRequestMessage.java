package it.polimi.ingsw.client.network.socket.messages.login;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

public class LoginRequestMessage extends ClientMessage {
    private final String username;
    private final String passwordHash;

    public LoginRequestMessage(String username, String passwordHash) {
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
    public void dispatch(ClientVisitor  clientVisitor) {
        clientVisitor.visit(this);
    }
}
