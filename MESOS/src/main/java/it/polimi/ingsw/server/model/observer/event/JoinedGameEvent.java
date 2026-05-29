package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;

public class JoinedGameEvent implements IGameEvent {

    private final GameInfoDTO gameInfo;

    public JoinedGameEvent(GameInfoDTO gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameInfoDTO getGameInfo() {
        return gameInfo;
    }

    /**
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
