package it.polimi.ingsw.client.view;


public record Credentials(String nickname, String passwordHash) {
    //TODO: hashing the passwordHash
}
