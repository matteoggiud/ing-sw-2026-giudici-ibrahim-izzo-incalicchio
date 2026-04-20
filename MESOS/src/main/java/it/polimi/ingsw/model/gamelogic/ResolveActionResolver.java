package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.board.CardRow;
import it.polimi.ingsw.model.board.OFFER_BOX_ID;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.core.Board;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;

import java.util.HashMap;
import java.util.Map;

public class ResolveActionResolver {

    private final Map<Player, ActionResolutionProgress> resolutionProgressByPlayer = new HashMap<>();

    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        validateArguments(game, player); // checks for null and if it's the player's turn to resolve an action

        Board board = game.getBoard();

        OfferBox box = board.getOfferTrack().getBoxByPlayer(player);

        if (isFoodOnlyBox(box)) {
            resolveFoodOnlyAction(game, player, box, selectedCard);
            return;
        }

        //TODO: it could be null if there aren't any cards
        if (selectedCard == null) {
            throw new IllegalArgumentException("Selected card cannot be null for this offer box.");
        }

        ActionResolutionProgress progress = getProgress(player);

        validatePickLimit(box, progress, fromUpperRow);

        CardRow row = fromUpperRow ? board.getUpperRow() : board.getLowerRow();
        String rowName = fromUpperRow ? "upper" : "lower";

        selectedCard.validateSelectableFrom(row);
        selectedCard.resolveFromOfferRow(player, row);

        if (fromUpperRow) {
            progress.upperCardsResolved++;
        } else {
            progress.lowerCardsResolved++;
        }


        completeActionIfNeeded(game, player, box, progress);
    }

    private ActionResolutionProgress getProgress(Player player) {
        ActionResolutionProgress progress = resolutionProgressByPlayer.get(player);

        if (progress == null) {
            progress = new ActionResolutionProgress();
            resolutionProgressByPlayer.put(player, progress);
        }

        return progress;
    }

    private void validateArguments(Game game, Player player) {
        if (game == null || player == null) {
            throw new IllegalArgumentException("Game and player cannot be null.");
        }

        Board board = game.getBoard();

        if (!board.getOfferTrack().getCurrentPlayer().equals(player)) {
            throw new IllegalStateException("It's not your turn to resolve an action.");
        }
    }

    private boolean isFoodOnlyBox(OfferBox box) {
        return box.getBoxId() == OFFER_BOX_ID.A;
    }

    private void resolveFoodOnlyAction(Game game, Player player, OfferBox box, Card selectedCard) {
        if (selectedCard != null) {
            throw new IllegalArgumentException("Offer box A gives only food and does not allow card picks.");
        }

        player.addFood(box.getFoodReward());
        completeAction(game, player, box);
    }

    private void validatePickLimit(OfferBox box, ActionResolutionProgress progress, boolean fromUpperRow) {
        if (fromUpperRow && progress.upperCardsResolved >= box.getUpperRowPicks()) {
            throw new IllegalStateException("You cannot pick more cards from the upper row.");
        }

        if (!fromUpperRow && progress.lowerCardsResolved >= box.getLowerRowPicks()) {
            throw new IllegalStateException("You cannot pick more cards from the lower row.");
        }
    }

    private void completeActionIfNeeded(Game game, Player player, OfferBox box, ActionResolutionProgress progress) {
        if (progress.upperCardsResolved != box.getUpperRowPicks()) {
            return;
        }

        if (progress.lowerCardsResolved != box.getLowerRowPicks()) {
            return;
        }

        resolutionProgressByPlayer.remove(player);
        completeAction(game, player, box);
    }

    private void completeAction(Game game, Player player, OfferBox box) {
        Board board = game.getBoard();

        box.removePlayer();

        TurnOrderSlot turnOrderSlot = board.getTurnOrderBox().getFirstFreeSlot();
        turnOrderSlot.placePlayer(player);

        applyTurnOrderSlotEffect(player, turnOrderSlot);

        new EffectResolver(game).resolveTrigger(player);
    }

    private void applyTurnOrderSlotEffect(Player player, TurnOrderSlot turnOrderSlot) {
        int foodBonus = turnOrderSlot.getFoodBonus(); //TODO: if possible refactor this method

        if (foodBonus > 0) {
            player.addFood(foodBonus);
            return;
        }

        if (foodBonus < 0) {
            if (player.getFood() > 0) {
                player.addFood(foodBonus);
            } else {
                player.addPP(TurnOrderSlot.DEFAULT_PP_MALUS);
            }
        }
    }

    private static class ActionResolutionProgress {
        private int upperCardsResolved;
        private int lowerCardsResolved;

        ActionResolutionProgress() {
            upperCardsResolved = 0;
            lowerCardsResolved = 0;
        }
    }
}
