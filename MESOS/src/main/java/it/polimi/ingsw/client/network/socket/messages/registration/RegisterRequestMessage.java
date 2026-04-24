package it.polimi.ingsw.client.network.socket.messages.registration;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientVisitor;

public class RegisterRequestMessage extends ClientMessage {
    private String username;
    private String password;

    public RegisterRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {return username;}
    public String getPassword() {return password;}

    @Override
    public void dispatch(ClientVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
