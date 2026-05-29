package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.common.network.dto.PlayerInfoDTO;

import java.util.List;

public class PlayerInfosChangedEvent implements IGameEvent {
    private final int gameId;
    private final List<PlayerInfoDTO> playerInfos;

    public PlayerInfosChangedEvent(int gameId, List<PlayerInfoDTO> playerInfos) {
        this.gameId = gameId;
        this.playerInfos = playerInfos;
    }

    /**
     * Returns the list of player information DTOs representing the current state of all players in the game.
     * @return the list of player information DTOs, never null
     */
    public List<PlayerInfoDTO> getPlayerInfos() {
        return playerInfos;
    }


    /**
     * Returns the unique identifier of the game to which this event pertains.
     * @return the game id
     */
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
