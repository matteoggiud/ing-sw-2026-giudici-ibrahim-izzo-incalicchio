package it.polimi.ingsw.model.effect;

import it.polimi.ingsw.model.core.Player;

import java.util.HashMap;

public class EffectEventResult {
    private HashMap<Player, Integer> playerFoodResults;
    private HashMap<Player, Integer> playerPPResults;

    public void putPlayerFoodResults(Player p, int N_Subject) {
        this.playerFoodResults.put(p, N_Subject);
    }

    public void putPlayerPPResults(Player p, int N_Subject) {
        this.playerPPResults.put(p, N_Subject);
    }
}
