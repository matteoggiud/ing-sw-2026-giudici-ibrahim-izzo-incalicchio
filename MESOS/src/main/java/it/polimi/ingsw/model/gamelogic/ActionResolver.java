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

public class ActionResolver {

    private final Map<Player, ActionResolutionProgress> resolutionProgressByPlayer = new HashMap<>();

    /**
     * Resolves an extra action granted by a building effect.
     * Extra actions can only draw one card from the upper row.
     *
     * @param game the current game context
     * @param player the player resolving the extra action
     * @param selectedCard the selected upper-row card
     * @param fromUpperRow true if the selected card is from the upper row
     * @throws IllegalArgumentException if any argument is invalid
     */
    public void resolveExtraAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        if (game == null || player == null || selectedCard == null) {
            throw new IllegalArgumentException("Game, player and selectedCard cannot be null.");
        }

        if (!fromUpperRow) {
            throw new IllegalArgumentException("Extra actions can only be resolved from the upper row.");
        }

        if (!canResolveExtraAction(game, player)) {
            throw new IllegalStateException("The player has no selectable card for the extra action.");
        }

        CardRow upperRow = game.getBoard().getUpperRow();
        selectedCard.validateSelectableFrom(upperRow);
        selectedCard.resolveFromOfferRow(player, upperRow);
    }


    public void resolveRegularAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        validateArguments(game, player); // checks for null and if it's the player's turn to resolve an action

        Board board = game.getBoard();

        OfferBox box = board.getOfferTrack().getBoxByPlayer(player);

        if (isFoodOnlyBox(box)) {
            resolveFoodOnlyAction(game, player, box, selectedCard);
            return;
        }

        ActionResolutionProgress progress = getProgress(player);

        validatePickLimit(box, progress, fromUpperRow);

        CardRow row = fromUpperRow ? board.getUpperRow() : board.getLowerRow();

        if (selectedCard == null) {
            throw new IllegalArgumentException("Selected card cannot be null.");
        }

        selectedCard.validateSelectableFrom(row);
        selectedCard.resolveFromOfferRow(player, row);

        markCardPickResolved(progress, fromUpperRow);

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
        if (!hasResolvedAllRequiredCards(box, progress)) {
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
        int foodBonus = turnOrderSlot.getFoodBonus();

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


    private void markCardPickResolved(ActionResolutionProgress progress, boolean fromUpperRow) {
        if (fromUpperRow) {
            progress.upperCardsResolved++;
        } else {
            progress.lowerCardsResolved++;
        }
    }

    private boolean hasPendingUpperPick(OfferBox box, ActionResolutionProgress progress) {
        return progress.upperCardsResolved < box.getUpperRowPicks();
    }

    private boolean hasPendingLowerPick(OfferBox box, ActionResolutionProgress progress) {
        return progress.lowerCardsResolved < box.getLowerRowPicks();
    }

    private boolean hasResolvedAllRequiredCards(OfferBox box, ActionResolutionProgress progress) {
        return progress.upperCardsResolved == box.getUpperRowPicks()
                && progress.lowerCardsResolved == box.getLowerRowPicks();
    }

    private boolean hasSelectableCardForPendingPick(Board board, Player player, OfferBox box, ActionResolutionProgress progress) {
        return hasPendingUpperPick(box, progress) && board.getUpperRow().hasSelectableCardFor(player)
                || hasPendingLowerPick(box, progress) && board.getLowerRow().hasSelectableCardFor(player);
    }

    /**
     * Checks whether a player can resolve an extra action.
     * Extra actions can only draw from the upper row, so the player must have
     * at least one selectable card in the upper row.
     *
     * @param game the current game context
     * @param player the player to check
     * @return true if the player can draw at least one card from the upper row
     * @throws IllegalArgumentException if game or player is null
     */
    public boolean canResolveExtraAction(Game game, Player player) {
        if (game == null || player == null) {
            throw new IllegalArgumentException("Game and player cannot be null.");
        }

        return game.getBoard().getUpperRow().hasSelectableCardFor(player);
    }



    public void resolveAutomaticSkips(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        boolean progressed;

        do {
            progressed = false;

            Board board = game.getBoard();

            if (board.getOfferTrack().getBoxes().stream().allMatch(OfferBox::isFree)) {
                return;
            }

            Player player = board.getOfferTrack().getCurrentPlayer();
            OfferBox box = board.getOfferTrack().getBoxByPlayer(player);

            if (isFoodOnlyBox(box)) {
                resolveFoodOnlyAction(game, player, box, null);
                progressed = true;
                continue;
            }

            ActionResolutionProgress progress = getProgress(player);

            if (hasSelectableCardForPendingPick(board, player, box, progress)) {
                return;
            }

            if (hasPendingUpperPick(box, progress)) {
                markCardPickResolved(progress, true);
                progressed = true;
            }

            if (hasPendingLowerPick(box, progress)) {
                markCardPickResolved(progress, false);
                progressed = true;
            }

            if (hasResolvedAllRequiredCards(box, progress)) {
                resolutionProgressByPlayer.remove(player);
                completeAction(game, player, box);
                progressed = true;
            }

        } while (progressed);
    }



}
