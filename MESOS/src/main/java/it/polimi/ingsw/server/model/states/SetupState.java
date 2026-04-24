package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamelogic.GameBuilder;

public class SetupState implements IGameState {
    @Override
    public void nextState(Game game) {
        game.setGameState(new PlacingTotemState());
    }

    /**
     * Executes the actions associated with the setup phase of the game.
     * This includes initializing the board.
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        GameBuilder builder = new GameBuilder(game);

        // Prepare the game by initializing the board, decks, and other necessary components.
        builder.prepareGame();

        nextState(game);
    }

}
