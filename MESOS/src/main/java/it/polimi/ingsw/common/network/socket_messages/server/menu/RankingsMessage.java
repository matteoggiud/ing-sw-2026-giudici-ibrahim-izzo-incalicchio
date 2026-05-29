package it.polimi.ingsw.common.network.socket_messages.server.menu;

import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

import java.util.List;

public class RankingsMessage extends ServerMessage {
    private final List<LeadBoardDTO> rankings;

    public RankingsMessage(List<LeadBoardDTO> rankings) {
        this.rankings = rankings;
    }

    public List<LeadBoardDTO> getRankings() {
        return rankings;
    }

    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
