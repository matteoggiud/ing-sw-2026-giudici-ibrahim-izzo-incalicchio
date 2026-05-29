package it.polimi.ingsw.server.model.gamelogic;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Board;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;

import java.util.HashMap;
import java.util.Map;


public class ActionResolver {

    /**
     * A map that tracks the progress of action resolution for each player,
     * allowing the resolver to manage multiple players resolving actions simultaneously.
     */
    private final Map<Player, ActionResolutionProgress> resolutionProgressByPlayer = new HashMap<>();

    /**
     * Resolves an extra action granted by a building effect.
     * Extra actions can only draw one card from the upper row.
     *
     * @param game         the current game context
     * @param player       the player resolving the extra action
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

        new EffectResolver(game).resolveTrigger(player);
    }

    /**
     * Resolves a regular action for a player, allowing them to pick cards from the offer rows according to the rules of the offer box they are in.
     * @param game the current game context
     * @param player the player resolving the action
     * @param selectedCard the card selected by the player to resolve, which must be from the appropriate row based on the fromUpperRow parameter
     * @param fromUpperRow true if the selected card is from the upper row, false if it's from the lower row
     */
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

    /**
     * Retrieves the action resolution progress for the given player, creating a new progress object if one does not already exist.
     * @param player the player for whom to retrieve the action resolution progress
     * @return the action resolution progress for the given player
     */
    private ActionResolutionProgress getProgress(Player player) {
        ActionResolutionProgress progress = resolutionProgressByPlayer.get(player);

        if (progress == null) {
            progress = new ActionResolutionProgress();
            resolutionProgressByPlayer.put(player, progress);
        }

        return progress;
    }

    /**
     * Validates the arguments for resolving a regular action.
     * @param game the current game context, which must not be null
     * @param player the player resolving the action, which must not be null and must be the current player in the offer track
     */
    private void validateArguments(Game game, Player player) {
        if (game == null || player == null) {
            throw new IllegalArgumentException("Game and player cannot be null.");
        }

        Board board = game.getBoard();

        if (!board.getOfferTrack().getCurrentPlayer().equals(player)) {
            throw new IllegalStateException("It's not your turn to resolve an action.");
        }
    }

    /**
     * Checks if the given offer box is a food-only box.
     * @param box the offer box to check
     * @return true if the box is a food-only box, false otherwise
     */
    private boolean isFoodOnlyBox(OfferBox box) {
        return box.isFoodOnlyAction();
    }

    /**
     * Resolves an action for a food-only offer box.
     * @param game the current game context
     * @param player the player resolving the action, who will receive the food reward
     * @param box the food-only offer box being resolved
     * @param selectedCard the selected card.
     */
    private void resolveFoodOnlyAction(Game game, Player player, OfferBox box, Card selectedCard) {
        if (selectedCard != null) {
            throw new IllegalArgumentException("This offer box gives only food and does not allow card picks.");
        }

        player.addFood(box.getFoodReward());
        completeAction(game, player, box);
    }

    /**
     * Validates that the player can pick a card from the specified row
     * based on the offer box's pick limits and the player's current progress in resolving the action.
     * @param box the offer box being resolved.
     * @param progress the player's current progress in resolving the action.
     * @param fromUpperRow true if the player is trying to pick from the upper row, false if they are trying to pick from the lower row.
     */
    private void validatePickLimit(OfferBox box, ActionResolutionProgress progress, boolean fromUpperRow) {
        if (fromUpperRow && progress.upperCardsResolved >= box.getUpperRowPicks()) {
            throw new IllegalStateException("You cannot pick more cards from the upper row.");
        }

        if (!fromUpperRow && progress.lowerCardsResolved >= box.getLowerRowPicks()) {
            throw new IllegalStateException("You cannot pick more cards from the lower row.");
        }
    }

    /**
     * Checks if the player has resolved all required card picks for the offer box and completes the action if so.
     * @param game the current game context
     * @param player the player resolving the action
     * @param box the offer box being resolved
     * @param progress the player's current progress in resolving the action
     */
    private void completeActionIfNeeded(Game game, Player player, OfferBox box, ActionResolutionProgress progress) {
        if (!hasResolvedAllRequiredCards(box, progress)) {
            return;
        }
        resolutionProgressByPlayer.remove(player);
        completeAction(game, player, box);
    }


    /**
     * Completes the action for the player by removing them from the offer box, placing them in the first free turn order slot.
     * @param game the current game context
     * @param player the player whose action is being completed
     * @param box the offer box from which the player is being removed
     */
    private void completeAction(Game game, Player player, OfferBox box) {
        Board board = game.getBoard();

        box.removePlayer();

        TurnOrderSlot turnOrderSlot = board.getTurnOrderBox().getFirstFreeSlot();
        turnOrderSlot.placePlayer(player);

        applyTurnOrderSlotEffect(player, turnOrderSlot);

        new EffectResolver(game).resolveTrigger(player);
    }

    /**
     * Applies the effect of the turn order slot to the player.
     * @param player the player to whom the turn order slot effect will be applied
     * @param turnOrderSlot the turn order slot whose effect will be applied to the player
     */
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


    /**
     * Marks a card pick as resolved for the player, incrementing the appropriate counter in the action resolution progress based on whether the pick was from the upper or lower row.
     * @param progress the player's current progress in resolving the action.
     * @param fromUpperRow true if the resolved card pick was from the upper row, false if it was from the lower row.
     */
    private void markCardPickResolved(ActionResolutionProgress progress, boolean fromUpperRow) {
        if (fromUpperRow) {
            progress.upperCardsResolved++;
        } else {
            progress.lowerCardsResolved++;
        }
    }

    /**
     * Checks if the player has pending card picks for the offer box.
     * @param box the offer box being resolved
     * @param progress the player's current progress in resolving the action
     * @return true if the player has pending card picks for the offer box, false otherwise
     */
    private boolean hasPendingUpperPick(OfferBox box, ActionResolutionProgress progress) {
        return progress.upperCardsResolved < box.getUpperRowPicks();
    }

    /**
     * Checks if the player has pending card picks for the offer box.
     * @param box the offer box being resolved
     * @param progress the player's current progress in resolving the action
     * @return true if the player has pending card picks for the offer box, false otherwise
     */
    private boolean hasPendingLowerPick(OfferBox box, ActionResolutionProgress progress) {
        return progress.lowerCardsResolved < box.getLowerRowPicks();
    }

    /**
     * Checks if the player has resolved all required card picks for the offer box based on their current progress and the box's pick limits.
     * @param box the offer box being resolved
     * @param progress the player's current progress in resolving the action
     * @return true if the player has resolved all required card picks for the offer box, false otherwise
     */
    private boolean hasResolvedAllRequiredCards(OfferBox box, ActionResolutionProgress progress) {
        return progress.upperCardsResolved == box.getUpperRowPicks()
                && progress.lowerCardsResolved == box.getLowerRowPicks();
    }

    /**
     * Checks if the player has any selectable cards in the offer rows for pending picks based on their current progress and the box's pick limits.
     * @param board the current game board.
     * @param player the player for whom to check for selectable cards.
     * @param box the offer box being resolved.
     * @param progress the player's current progress in resolving the action.
     * @return true if the player has at least one selectable card in the appropriate row for any pending picks, false otherwise
     */
    private boolean hasSelectableCardForPendingPick(Board board, Player player, OfferBox box, ActionResolutionProgress progress) {
        return hasPendingUpperPick(box, progress) && board.getUpperRow().hasSelectableCardFor(player)
                || hasPendingLowerPick(box, progress) && board.getLowerRow().hasSelectableCardFor(player);
    }

    /**
     * Checks whether a player can resolve an extra action.
     * Extra actions can only draw from the upper row, so the player must have
     * at least one selectable card in the upper row.
     *
     * @param game   the current game context
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


    /**
     * Resolves any automatic skips for the current player in the offer track, allowing the game to progress smoothly without requiring player input for actions that have no valid choices.
     * @param game the current game context.
     */
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
