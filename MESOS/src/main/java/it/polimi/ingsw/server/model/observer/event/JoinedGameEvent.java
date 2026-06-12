package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;

/**
 * Event emitted when a player successfully joins a game, providing updated game information.
 */
public class JoinedGameEvent implements IGameEvent {

    private final GameInfoDTO gameInfo;

    /**
     * Constructs a new joined game event with the provided game information.
     * @param gameInfo the DTO containing the updated game information after a player has joined
     */
    public JoinedGameEvent(GameInfoDTO gameInfo) {
        this.gameInfo = gameInfo;
    }

    /**
     * Returns the DTO containing the updated game information after a player has joined.
     * @return the game information DTO
     */
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
