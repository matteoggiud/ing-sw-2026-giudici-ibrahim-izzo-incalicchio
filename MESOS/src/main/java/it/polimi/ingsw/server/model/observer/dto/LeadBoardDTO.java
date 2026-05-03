package it.polimi.ingsw.server.model.observer.dto;

import java.io.Serializable;

public record LeadBoardDTO(String nickname, int finalScore, int position, String finished_at) implements Serializable {
}
