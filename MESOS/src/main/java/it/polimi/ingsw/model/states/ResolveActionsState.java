package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.CharacterType;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.gamelogic.EffectResolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The ResolveActionsState class represents the state of the game where players resolve their
 * actions after placing their totems on the offer track.
 */

public class ResolveActionsState implements IGameState {
    /**
     * Transitions the game to the next state, which is the EndRoundState, after all players have resolved their actions.
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndRoundState());
    }

    /**
     * Resolves the player's action by validating their card selections, applying the rewards from the offer box,
     * @param game the current game context
     * @param player the player who is resolving their action
     * @param upperCards the list of cards selected from the upper row of the offer track
     * @param lowerCards the list of cards selected from the lower row of the offer track
     */
    @Override
    public void resolveAction(Game game, Player player, ArrayList<Card> upperCards, ArrayList<Card> lowerCards) {
        validateArguments(game, player, upperCards, lowerCards);

        Board board = game.getBoard();

        // Check if the player is the current player
        if (!getCurrentPlayer(board).equals(player)) {
            throw new IllegalStateException("It's not your turn to resolve an action.");
        }

        OfferBox box = board.getOfferTrack().getBoxByPlayer(player);

        // Check if the player has picked the correct number of cards according to the offer box
        if (upperCards.size() != box.getUpperRowPicks() || lowerCards.size() != box.getLowerRowPicks()) {
            throw new IllegalStateException("You must pick the exact number of cards indicated by the offer box.");
        }

        // Check for duplicate cards in the selection
        validateNoDuplicates(upperCards, lowerCards);
        // Check if the selected cards are actually available in the respective rows
        validateCardsSelection(board, upperCards, lowerCards);
        // Simulate the resolution to ensure the player has enough resources to resolve the selected cards
        simulateResolution(player, box, upperCards, lowerCards);

        // Box A gives food and no cards. The bonus is applied as part of the action resolution.
        player.addFood(box.getFoodReward());
        // Now we can resolve the cards, knowing that the player has enough resources to do so.
        resolveCardsFromRow(player, board.getUpperRow(), upperCards);
        resolveCardsFromRow(player, board.getLowerRow(), lowerCards);

        // After resolving the cards, we can remove the player from the offer box and place them in the turn order box.
        box.removePlayer();

        TurnOrderSlot turnOrderSlot = board.getTurnOrderBox().getFirstFreeSlot();
        turnOrderSlot.placePlayer(player);
        // Apply the turn order slot effect immediately after placing the player in the slot
        applyTurnOrderSlotEffect(player, turnOrderSlot);

        new EffectResolver(game).resolveTrigger(player);

        // If all players have resolved their actions and the offer track is empty, we can move to the next state.
        if (board.getOfferTrack().getBoxes().stream().allMatch(OfferBox::isFree)) {
            nextState(game);
        }else{
            Player nextPlayer = nextPlayer(board);
        }
    }

    /**
     * This method retrieves the current player from the board's turn order box.
     *
     * @param board the game board
     * @return the current player who is placing their totem
     * @throws IllegalStateException if everyone has just placed the totem in the offer box
     */
    private Player getCurrentPlayer(Board board) throws IllegalStateException {
        return board.getOfferTrack().getBoxes().stream()
                .filter(box -> !box.isFree())
                .map(OfferBox::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Everyone has just taken the cards"));
    }

    /**
     * Validates the arguments passed to the resolveAction method, ensuring that none of them are null.
     * @param game the current game context
     * @param player the player who is resolving their action
     * @param upperCards the list of cards selected from the upper row of the offer track
     * @param lowerCards the list of cards selected from the lower row of the offer track
     */
    private void validateArguments(Game game, Player player, ArrayList<Card> upperCards, ArrayList<Card> lowerCards) {
        if (game == null || player == null || upperCards == null || lowerCards == null) {
            throw new IllegalArgumentException("Game, player and selected cards cannot be null.");
        }
    }

    /**
     * Validates that there are no duplicate cards in the combined selection of upper and lower cards.
     * @param upperCards the list of cards selected from the upper row of the offer track
     * @param lowerCards the list of cards selected from the lower row of the offer track
     */
    private void validateNoDuplicates(List<Card> upperCards, List<Card> lowerCards) {
        Set<Card> uniqueCards = new HashSet<>();
        for (Card card : upperCards) {
            if (!uniqueCards.add(card)) {
                throw new IllegalArgumentException("Duplicate card selected: " + card.getName());
            }
        }
        for (Card card : lowerCards) {
            if (!uniqueCards.add(card)) {
                throw new IllegalArgumentException("Duplicate card selected: " + card.getName());
            }
        }
    }

    /**
     * Validates that the selected cards are actually available in the respective rows
     * of the offer track and that they are of the correct type.
     * @param board the game board containing the offer track and its rows
     * @param upperCards the list of cards selected from the upper row of the offer track
     * @param lowerCards the list of cards selected from the lower row of the offer track
     */
    private void validateCardsSelection(Board board, List<Card> upperCards, List<Card> lowerCards) {
        validateSelectionFromRow(board.getUpperRow(), upperCards, "upper");
        validateSelectionFromRow(board.getLowerRow(), lowerCards, "lower");
    }

    /**
     * Validates that the selected cards from a specific row are actually available in that row and that they are of the correct type.
     * @param row the card row (upper or lower) from which the cards are selected
     * @param selectedCards the list of cards selected from the specified row
     * @param rowName a string representing the name of the row (for error messages)
     */
    private void validateSelectionFromRow(it.polimi.ingsw.model.board.CardRow row, List<Card> selectedCards, String rowName) {
        for (Card card : selectedCards) {
            if (card == null) {
                throw new IllegalArgumentException("Selected cards cannot contain null values.");
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
    }

    /**
     * Simulates the resolution of the selected cards to ensure that the player has enough resources to resolve them.
     * @param player the player who is resolving their action
     * @param box the offer box from which the player is selecting cards, used to calculate the initial food reward
     * @param upperCards the list of cards selected from the upper row of the offer track
     * @param lowerCards the list of cards selected from the lower row of the offer track
     */
    private void simulateResolution(Player player, OfferBox box, List<Card> upperCards, List<Card> lowerCards) {
        int simulatedFood = player.getFood() + box.getFoodReward();
        List<CharacterCard> simulatedCharacters = new ArrayList<>(player.getPlayerDeck().getCharacters());

        simulatedFood = simulateCardsList(upperCards, simulatedFood, simulatedCharacters);
        simulatedFood = simulateCardsList(lowerCards, simulatedFood, simulatedCharacters);
    }

    /**
     * Simulates the resolution of a list of cards, updating the simulated food and character list accordingly.
     * @param cards the list of cards to simulate the resolution for
     * @param simulatedFood the current amount of simulated food available to the player
     * @param simulatedCharacters the current list of simulated character cards in the player's deck, which may be modified by the resolution of the cards
     * @return the updated amount of simulated food after resolving the cards
     */
    private int simulateCardsList(List<Card> cards, int simulatedFood, List<CharacterCard> simulatedCharacters) {
        for (Card card : cards) {
            if (card instanceof CharacterCard characterCard) {
                simulatedCharacters.add(characterCard);
                if (characterCard instanceof HunterCharacterCard hunterCharacterCard && hunterCharacterCard.hasFoodIcon()) {
                    simulatedFood += countHunters(simulatedCharacters);
                }
            } else if (card instanceof BuildingCard buildingCard) {
                int currentDiscount = getBuilderDiscount(simulatedCharacters);
                int effectiveCost = Math.max(buildingCard.getFoodCost() - currentDiscount, 0);
                if (effectiveCost > simulatedFood) {
                    throw new IllegalStateException("Not enough food to resolve the selected building cards.");
                }
                simulatedFood -= effectiveCost;
            }
        }

        return simulatedFood;
    }

    /**
     * Resolves the selected cards from a specific row, applying their effects to the player and updating the row accordingly.
     * @param player the player who is resolving their action
     * @param row the card row (upper or lower) from which the cards are selected, which will be updated by removing the resolved cards
     * @param cards the list of cards to resolve, which will be applied to the player and removed from the row
     */
    private void resolveCardsFromRow(Player player, it.polimi.ingsw.model.board.CardRow row, List<Card> cards) {
        for (Card card : cards) {
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
    }

    /**
     * Calculates the total food discount provided by the Builder character cards in the player's deck.
     * @param characters the list of character cards currently in the player's deck
     * @return the total food discount from Builder character cards, which is the sum of the food discounts provided by each Builder card
     */
    private int getBuilderDiscount(List<CharacterCard> characters) {
        return characters.stream()
                .filter(BuilderCharacterCard.class::isInstance)
                .map(BuilderCharacterCard.class::cast)
                .mapToInt(BuilderCharacterCard::getBuilderFoodDiscount)
                .sum();
    }

    /**
     * Counts the number of Hunter character cards in the player's deck, which determines the food bonus provided by Hunter cards with a food icon.
     * @param characters the list of character cards currently in the player's deck
     * @return the number of Hunter character cards, which is used to calculate the food bonus from Hunter cards with a food icon
     */
    private int countHunters(List<CharacterCard> characters) {
        return (int) characters.stream()
                .filter(character -> character.getType() == CharacterType.HUNTER)
                .count();
    }

    /**
     * Applies the effect of the turn order slot to the player, which may provide a food bonus or a PP bonus depending on the specific slot.
     * @param player the player who is placed in the turn order slot and will receive the effect
     * @param turnOrderSlot the turn order slot in which the player is placed, which determines the specific effect to be applied (food bonus or PP bonus)
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
     * Determines the next player who will resolve their action
     * @param board the game board containing the offer track and its boxes
     * @return the next player who will resolve their action
     */
    private Player nextPlayer(Board board){
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }

        return board.getOfferTrack().getBoxes().stream()
                .filter(box -> !box.isFree())
                .map(OfferBox::getPlacedPlayer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No next player, end of round"));
    }
}
