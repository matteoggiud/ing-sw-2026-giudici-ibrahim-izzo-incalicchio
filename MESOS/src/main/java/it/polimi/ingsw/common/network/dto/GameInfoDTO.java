package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;
import java.util.List;

public record GameInfoDTO(int gameId, int numOfPlayers, List<String> players, int expectedPlayerCount) implements Serializable {
}
