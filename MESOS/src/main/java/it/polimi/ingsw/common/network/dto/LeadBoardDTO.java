package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;

public record LeadBoardDTO(String nickname, int finalScore, int position, String finished_at) implements Serializable {
}
