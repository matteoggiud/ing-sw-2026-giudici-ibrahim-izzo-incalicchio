package it.polimi.ingsw.server.model.observer.dto;

public record PlayerInfoDTO(String nickname, int food, int pp, String totem, PlayerDeckDTO deck) {
}
