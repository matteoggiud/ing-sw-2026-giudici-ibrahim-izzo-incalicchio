package it.polimi.ingsw.client.network.socket.messages.registration;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

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
