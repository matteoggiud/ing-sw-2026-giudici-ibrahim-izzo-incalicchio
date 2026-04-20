package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.board.CardRow;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.model.core.Board;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.gamelogic.EffectResolver;
import it.polimi.ingsw.model.board.OFFER_BOX_ID;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ResolveActionsState class represents the state of the game where players resolve their
 * actions after placing their totems on the offer track.
 */

public class ResolveActionsState implements IGameState {

    private final Map<Player, ActionResolutionProgress> resolutionProgressByPlayer = new HashMap<>();

    /**
     * Transitions the game to the next state, which is the EndRoundState, after all players have resolved their actions.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndRoundState());
    }

    /**
     * Resolves one selected card for the current player.
     *
     * @param game the current game context
     * @param player the player who is resolving their action
     * @param selectedCard the selected card to resolve; can be null only if the offer box requires no card picks
     * @param fromUpperRow true if the selected card comes from the upper row, false if it comes from the lower row
     */
    @Override
    public void resolveAction(Game game, Player player, Card selectedCard, boolean fromUpperRow) {
        validateArguments(game, player);

        Board board = game.getBoard();

        if (!getCurrentPlayer(board).equals(player)) {
            throw new IllegalStateException("It's not your turn to resolve an action.");
        }

        OfferBox box = board.getOfferTrack().getBoxByPlayer(player);

        if (isFoodOnlyBox(box)) {
            resolveFoodOnlyAction(game, player, box, selectedCard);
            return;
        }

        if (selectedCard == null) {
            throw new IllegalArgumentException("Selected card cannot be null for this offer box.");
        }

        ActionResolutionProgress progress = resolutionProgressByPlayer.get(player);

        if (progress == null) {
            progress = new ActionResolutionProgress();
            resolutionProgressByPlayer.put(player, progress);
        }

        validatePickLimit(box, progress, fromUpperRow);

        CardRow row = fromUpperRow ? board.getUpperRow() : board.getLowerRow();
        String rowName = fromUpperRow ? "upper" : "lower";

        validateSelectionFromRow(row, selectedCard, rowName);
        simulateCardResolution(player, selectedCard);

        resolveCardFromRow(player, row, selectedCard);

        if (fromUpperRow) {
            progress.upperCardsResolved++;
        } else {
            progress.lowerCardsResolved++;
        }

        completeActionIfNeeded(game, player, box, progress);
    }

    /**
     * Retrieves the current player from the offer track.
     *
     * @param board the game board
     * @return the current player who has to resolve their action
     * @throws IllegalStateException if no player has to resolve an action
     */
    private Player getCurrentPlayer(Board board) throws IllegalStateException {
        return board.getOfferTrack().getBoxes().stream()
                .filter(box -> !box.isFree())
                .map(OfferBox::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Everyone has just taken the cards"));
    }

    /**
     * Validates the arguments passed to the resolveAction method.
     *
     * @param game the current game context
     * @param player the player who is resolving their action
     */
    private void validateArguments(Game game, Player player) {
        if (game == null || player == null) {
            throw new IllegalArgumentException("Game and player cannot be null.");
        }
    }

    /**
     * Validates that the player is not taking more cards than allowed from the selected row.
     *
     * @param box the offer box occupied by the player
     * @param progress the current resolution progress for the player
     * @param fromUpperRow true if the selected card comes from the upper row
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
     * Validates that the selected card is actually available in the specified row and that it is of the correct type.
     *
     * @param row the card row from which the card is selected
     * @param card the selected card
     * @param rowName a string representing the name of the row
     */
    private void validateSelectionFromRow(CardRow row, Card card, String rowName) {
        if (card == null) {
            throw new IllegalArgumentException("Selected card cannot be null.");
        }

        if (card instanceof EventCard) {
            throw new IllegalArgumentException("Event cards cannot be selected from the offer rows.");
        }

        if (card instanceof BuildingCard buildingCard) {
            if (!row.getBuildingRow().getBuildingCards().contains(buildingCard)) {
                throw new IllegalArgumentException("Selected building is not available in the " + rowName + " row.");
            }
        } else if (card instanceof CharacterCard characterCard) {
            if (!row.getTribeRow().getCharacterCards().contains(characterCard)) {
                throw new IllegalArgumentException("Selected character is not available in the " + rowName + " row.");
            }
        } else {
            throw new IllegalArgumentException("Unsupported card type selected.");
        }
    }

    /**
     * Simulates the resolution of one card to ensure that the player has enough resources.
     *
     * @param player the player who is resolving the card
     * @param card the selected card to simulate
     */
    private void simulateCardResolution(Player player, Card card) {
        int simulatedFood = player.getFood();
        List<CharacterCard> simulatedCharacters = new ArrayList<>(player.getPlayerDeck().getCharacters());

        if (card instanceof CharacterCard characterCard) {
            simulatedCharacters.add(characterCard);

            if (characterCard instanceof HunterCharacterCard hunterCharacterCard && hunterCharacterCard.hasFoodIcon()) {
                simulatedFood += countHunters(simulatedCharacters);
            }
        } else if (card instanceof BuildingCard buildingCard) {
            int currentDiscount = getBuilderDiscount(simulatedCharacters);
            int effectiveCost = Math.max(buildingCard.getFoodCost() - currentDiscount, 0);

            if (effectiveCost > simulatedFood) {
                throw new IllegalStateException("Not enough food to resolve the selected building card.");
            }
        }
    }



    /**
     * Resolves the selected card, applying its effects to the player and removing it from the row.
     *
     * @param player the player who is resolving the card
     * @param row the row from which the card is selected
     * @param card the selected card
     */
    private void resolveCardFromRow(Player player, CardRow row, Card card) {
        if (card instanceof BuildingCard buildingCard) {
            player.canBuyBuildingCard(buildingCard);
            row.getBuildingRow().remove(buildingCard);
        } else if (card instanceof CharacterCard characterCard) {
            row.getTribeRow().remove(characterCard);
            player.getPlayerDeck().addCard(characterCard);

            if (characterCard instanceof HunterCharacterCard hunterCharacterCard && hunterCharacterCard.hasFoodIcon()) {
                player.addFood(player.getPlayerDeck().countCharactersByType(CharacterType.HUNTER));
            }
        }
    }

    /**
     * Completes the player's action if all required picks have been resolved.
     *
     * @param game the current game context
     * @param player the player who is resolving their action
     * @param box the offer box occupied by the player
     * @param progress the current resolution progress for the player
     */
    private void completeActionIfNeeded(Game game, Player player, OfferBox box, ActionResolutionProgress progress) {
        if (!hasResolvedAllRequiredCards(box, progress)) {
            return;
        }

        Board board = game.getBoard();

        resolutionProgressByPlayer.remove(player);
        box.removePlayer();

        TurnOrderSlot turnOrderSlot = board.getTurnOrderBox().getFirstFreeSlot();
        turnOrderSlot.placePlayer(player);

        applyTurnOrderSlotEffect(player, turnOrderSlot);

        new EffectResolver(game).resolveTrigger(player);

        if (board.getOfferTrack().getBoxes().stream().allMatch(OfferBox::isFree)) {
            nextState(game);
        } else {
            nextPlayer(board);
        }
    }

    /**
     * Checks whether the player has resolved all card picks required by the offer box.
     *
     * @param box the offer box occupied by the player
     * @param progress the current resolution progress for the player
     * @return true if all required picks have been resolved
     */
    private boolean hasResolvedAllRequiredCards(OfferBox box, ActionResolutionProgress progress) {
        return progress.upperCardsResolved == box.getUpperRowPicks()
                && progress.lowerCardsResolved == box.getLowerRowPicks();
    }

    /**
     * Calculates the total food discount provided by the Builder character cards.
     *
     * @param characters the list of character cards currently in the player's deck
     * @return the total food discount from Builder character cards
     */
    private int getBuilderDiscount(List<CharacterCard> characters) {
        return characters.stream()
                .filter(BuilderCharacterCard.class::isInstance)
                .map(BuilderCharacterCard.class::cast)
                .mapToInt(BuilderCharacterCard::getBuilderFoodDiscount)
                .sum();
    }

    /**
     * Counts the number of Hunter character cards in the player's deck.
     *
     * @param characters the list of character cards currently in the player's deck
     * @return the number of Hunter character cards
     */
    private int countHunters(List<CharacterCard> characters) {
        return (int) characters.stream()
                .filter(character -> character.getType() == CharacterType.HUNTER)
                .count();
    }

    /**
     * Applies the effect of the turn order slot to the player.
     *
     * @param player the player placed in the turn order slot
     * @param turnOrderSlot the turn order slot in which the player is placed
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
                player.addPP(foodBonus * 2);
            }
        }
    }

    /**
     * Determines the next player who will resolve their action.
     *
     * @param board the game board containing the offer track and its boxes
     * @return the next player who will resolve their action
     */
    private Player nextPlayer(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }

        return board.getOfferTrack().getBoxes().stream()
                .filter(box -> !box.isFree())
                .map(OfferBox::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No next player, end of round"));
    }


    private boolean isFoodOnlyBox(OfferBox box) {
        return box.getBoxId() == OFFER_BOX_ID.A;
    }

    private void resolveFoodOnlyAction(Game game, Player player, OfferBox box, Card selectedCard) {
        if (selectedCard != null) {
            throw new IllegalArgumentException("Offer box A gives only food and does not allow card picks.");
        }

        player.addFood(box.getFoodReward());

        ActionResolutionProgress progress = new ActionResolutionProgress();
        completeActionIfNeeded(game, player, box, progress);
    }

    /**
     * Tracks how many cards the player has already resolved during the current action.
     */
    private static class ActionResolutionProgress {
        private int upperCardsResolved;
        private int lowerCardsResolved;
    }

}
