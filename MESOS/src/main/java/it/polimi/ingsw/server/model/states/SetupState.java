package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.gamelogic.GameBuilder;


public class SetupState implements IGameState {

    @Override
    public void nextState(Game game) {
        game.setGameState(new PlacingTotemState());
        game.getGameState().executePhase(game);
    }

    /**
     * Executes the setup phase of the game.
     * After preparing the game, it notifies observers about:
     * - the current board state
     * - each player's initial resources
     * - the first player who must place a totem
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        GameBuilder builder = new GameBuilder(game);

        // Prepare the board, decks, turn order and initial resources.
        builder.prepareGame();

        // The setup is complete, so the next active phase is placing totems.
        nextState(game);


    }




}
