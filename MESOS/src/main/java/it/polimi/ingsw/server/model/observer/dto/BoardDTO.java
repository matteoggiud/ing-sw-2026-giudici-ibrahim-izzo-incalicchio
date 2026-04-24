package it.polimi.ingsw.server.model.observer.dto;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.Game;

import java.util.List;
import java.util.stream.IntStream;

/**
 * DTO representing the visible board state of a game.
 */
public record BoardDTO(
        String currentEra,
        List<CharacterCardDTO> upperCharacterCards,
        List<EventCardDTO> upperEventCards,
        List<BuildingCardDTO> upperBuildingCards,
        List<CharacterCardDTO> lowerCharacterCards,
        List<EventCardDTO> lowerEventCards,
        List<BuildingCardDTO> lowerBuildingCards,
        List<OfferBoxDTO> offerTrackBoxes,
        List<TurnOrderSlotDTO> turnOrderSlots
) {

    public BoardDTO {
        if (currentEra == null || currentEra.isBlank()) {
            throw new IllegalArgumentException("Current era cannot be null or blank.");
        }

        upperCharacterCards = List.copyOf(upperCharacterCards);
        upperEventCards = List.copyOf(upperEventCards);
        upperBuildingCards = List.copyOf(upperBuildingCards);
        lowerCharacterCards = List.copyOf(lowerCharacterCards);
        lowerEventCards = List.copyOf(lowerEventCards);
        lowerBuildingCards = List.copyOf(lowerBuildingCards);
        offerTrackBoxes = List.copyOf(offerTrackBoxes);
        turnOrderSlots = List.copyOf(turnOrderSlots);
    }

    /**
     * Builds a board DTO from the current game state.
     *
     * @param game the current game context
     * @return a DTO representing the visible board state
     */
    public static BoardDTO fromGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        return new BoardDTO(
                game.getCurrentEra().name(),
                toCharacterDTOs(game.getBoard().getUpperRow().getTribeRow().getCharacterCards()),
                toEventDTOs(game.getBoard().getUpperRow().getTribeRow().getEventCards()),
                toBuildingDTOs(game.getBoard().getUpperRow().getBuildingRow().getBuildingCards()),
                toCharacterDTOs(game.getBoard().getLowerRow().getTribeRow().getCharacterCards()),
                toEventDTOs(game.getBoard().getLowerRow().getTribeRow().getEventCards()),
                toBuildingDTOs(game.getBoard().getLowerRow().getBuildingRow().getBuildingCards()),
                toOfferBoxDTOs(game.getBoard().getOfferTrack().getBoxes()),
                toTurnOrderSlotDTOs(game.getBoard().getTurnOrderBox().getSlots())
        );
    }

    private static List<CharacterCardDTO> toCharacterDTOs(List<CharacterCard> cards) {
        return cards.stream()
                .map(card -> new CharacterCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    private static List<EventCardDTO> toEventDTOs(List<EventCard> cards) {
        return cards.stream()
                .map(card -> new EventCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    private static List<BuildingCardDTO> toBuildingDTOs(List<BuildingCard> cards) {
        return cards.stream()
                .map(card -> new BuildingCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    private static List<OfferBoxDTO> toOfferBoxDTOs(List<OfferBox> boxes) {
        return boxes.stream()
                .map(box -> new OfferBoxDTO(
                        box.getBoxId().name(),
                        box.isFree() ? null : box.getPlacedPlayer().getNickname(),
                        box.getFoodReward(),
                        box.getUpperRowPicks(),
                        box.getLowerRowPicks(),
                        ""
                ))
                .toList();
    }

    private static List<TurnOrderSlotDTO> toTurnOrderSlotDTOs(List<TurnOrderSlot> slots) {
        return IntStream.range(0, slots.size())
                .mapToObj(i -> {
                    TurnOrderSlot slot = slots.get(i);
                    return new TurnOrderSlotDTO(
                            i,
                            slot.isFree() ? null : slot.getPlacedPlayer().getNickname(),
                            slot.getFoodBonus()
                    );
                })
                .toList();
    }
}
