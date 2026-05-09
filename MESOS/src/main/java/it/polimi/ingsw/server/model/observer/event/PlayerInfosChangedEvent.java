package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;

import java.util.List;

public class PlayerInfosChangedEvent implements IGameEvent {
    private final int gameId;
    private final List<PlayerInfoDTO> playerInfos;

    public PlayerInfosChangedEvent(int gameId, List<PlayerInfoDTO> playerInfos) {
        this.gameId = gameId;
        this.playerInfos = playerInfos;
    }

    public List<PlayerInfoDTO> getPlayerInfos() {
        return playerInfos;
    }

    public int getGameId() {
        return gameId;
    }

    /**
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
