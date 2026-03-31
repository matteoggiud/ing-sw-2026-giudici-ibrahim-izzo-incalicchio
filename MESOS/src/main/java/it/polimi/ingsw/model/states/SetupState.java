package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.*;

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
        IGameRuleSet ruleSet = game.getRuleSet();
        int numPlayers = game.getNumberOfPlayers();

        //game.getBoard().setTurnOrderBox(ruleSet.createTurnOrderSlots(numPlayers));
    }


    }
