package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;

public class PlayerInfoChangedEvent implements IGameEvent {
    private final int gameId;
    private final PlayerInfoDTO playerInfo;

    public PlayerInfoChangedEvent(int gameId, PlayerInfoDTO playerInfo) {
        this.gameId = gameId;
        this.playerInfo = playerInfo;
    }

    public PlayerInfoDTO getPlayerInfo() {
        return playerInfo;
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
