package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.gamelogic.GameBuilder;

public class SetupState implements IGameState{
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
        GameBuilder  builder = new GameBuilder();

        //builder.prepareBoard(game);
        //game.getBoard().setTurnOrderBox(ruleSet.createTurnOrderSlots(numPlayers));
    }


    }
