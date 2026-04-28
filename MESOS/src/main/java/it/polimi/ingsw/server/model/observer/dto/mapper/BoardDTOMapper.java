package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.BuildingCardDTO;
import it.polimi.ingsw.server.model.observer.dto.CharacterCardDTO;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;
import it.polimi.ingsw.server.model.observer.dto.OfferBoxDTO;
import it.polimi.ingsw.server.model.observer.dto.TurnOrderSlotDTO;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class responsible for mapping the visible board state into observer DTOs.
 */
public final class BoardDTOMapper {

    private BoardDTOMapper() {
    }

    /**
     * Maps the current visible board state of a game to a board DTO.
     *
     * @param game the current game context
     * @return the DTO representing the visible board state
     */
    public static BoardDTO toBoardDTO(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        return new BoardDTO(
                game.getCurrentEra().name(),
                toCharacterCardDTOs(game.getBoard().getUpperRow().getTribeRow().getCharacterCards()),
                toEventCardDTOs(game.getBoard().getUpperRow().getTribeRow().getEventCards()),
                toBuildingCardDTOs(game.getBoard().getUpperRow().getBuildingRow().getBuildingCards()),
                toCharacterCardDTOs(game.getBoard().getLowerRow().getTribeRow().getCharacterCards()),
                toEventCardDTOs(game.getBoard().getLowerRow().getTribeRow().getEventCards()),
                toBuildingCardDTOs(game.getBoard().getLowerRow().getBuildingRow().getBuildingCards()),
                toOfferBoxDTOs(game.getBoard().getOfferTrack().getBoxes()),
                toTurnOrderSlotDTOs(game.getBoard().getTurnOrderBox().getSlots())
        );
    }

    /**
     * Maps a list of character cards to their DTO representations.
     *
     * @param cards the character cards to map
     * @return the corresponding character card DTOs
     */
    private static List<CharacterCardDTO> toCharacterCardDTOs(List<CharacterCard> cards) {
        return cards.stream()
                .map(card -> new CharacterCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    /**
     * Maps a list of event cards to their DTO representations.
     *
     * @param cards the event cards to map
     * @return the corresponding event card DTOs
     */
    private static List<EventCardDTO> toEventCardDTOs(List<EventCard> cards) {
        return cards.stream()
                .map(card -> new EventCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    /**
     * Maps a list of building cards to their DTO representations.
     *
     * @param cards the building cards to map
     * @return the corresponding building card DTOs
     */
    private static List<BuildingCardDTO> toBuildingCardDTOs(List<BuildingCard> cards) {
        return cards.stream()
                .map(card -> new BuildingCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    /**
     * Maps a list of offer boxes to their DTO representations.
     *
     * @param boxes the offer boxes to map
     * @return the corresponding offer box DTOs
     */
    private static List<OfferBoxDTO> toOfferBoxDTOs(List<OfferBox> boxes) {
        return IntStream.range(0, boxes.size())
                .mapToObj(i -> {
                    OfferBox box = boxes.get(i);

                    return new OfferBoxDTO(
                            i + 1,
                            box.isFree() ? null : box.getPlacedPlayer().getNickname(),
                            box.getFoodReward(),
                            box.getUpperRowPicks(),
                            box.getLowerRowPicks(),
                            ""
                    );
                })
                .toList();
    }


    /**
     * Maps a list of turn order slots to their DTO representations.
     *
     * @param slots the turn order slots to map
     * @return the corresponding turn order slot DTOs
     */
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
