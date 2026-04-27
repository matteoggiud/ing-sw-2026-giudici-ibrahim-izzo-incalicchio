package it.polimi.ingsw.server.model.observer.dto;

import java.io.Serializable;
import java.util.List;

public record GameInfoDTO(int gameId, int numOfPlayers, List<String> players) implements Serializable {
}
