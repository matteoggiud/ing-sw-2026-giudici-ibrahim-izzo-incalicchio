package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.common.network.dto.mapper.GameSnapshotDTOMapper;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.core.Totem;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import it.polimi.ingsw.server.model.era.Era3;
import it.polimi.ingsw.common.network.dto.BoardDTO;
import it.polimi.ingsw.common.network.dto.GameSnapshotDTO;
import it.polimi.ingsw.common.network.dto.PlayerInfoDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSnapshotDTOMapperTest {

    @Test
    void toGameSnapshotDTO_mapsFullSnapshotCorrectly() {
        // Arrange
        Player host = new Player("host");
        host.setTotem(Totem.BLUE);
        host.addFood(2);
        host.addPP(3);

        Game game = new Game(host, 42, 2);

        Player alice = new Player("alice");
        alice.setTotem(Totem.RED);
        alice.addFood(5);
        alice.addPP(7);
        game.getPlayers().addPlayer(alice);

        game.setCurrentEra(new Era1());

        // Add one tribe card to the remaining tribe deck
        ArtistCharacterCard tribeCard = new ArtistCharacterCard(1, "Artist", new Era1(), 2, "artist_img");
        game.getTribeDeck().setTribeDeck(List.of(tribeCard));

        // Add one building card per era to the remaining building decks
        IBuildingEffect buildingEffect = new IBuildingEffect() {
        };

        BuildingCard era1Building = new BuildingCard(10, "Era1 Building", new Era1(), 2, 3, 4, buildingEffect, "b1");
        BuildingCard era2Building = new BuildingCard(11, "Era2 Building", new Era2(), 2, 4, 5, buildingEffect, "b2");
        BuildingCard era3Building = new BuildingCard(12, "Era3 Building", new Era3(), 2, 5, 6, buildingEffect, "b3");
        game.getBuildingDecks().addBuildingCards(
                List.of(era1Building),
                List.of(era2Building),
                List.of(era3Building)
        );

        // Act
        GameSnapshotDTO dto = GameSnapshotDTOMapper.toGameSnapshotDTO(game);

        // Assert
        assertEquals(42, dto.gameId());
        assertEquals(2, dto.expectedPlayerCount());
        assertFalse(dto.lastRoundTriggered());
        assertEquals("LobbyState", dto.currentStateName());

        assertEquals(2, dto.players().size());
        PlayerInfoDTO firstPlayer = dto.players().get(0);
        assertEquals("host", firstPlayer.nickname());
        assertEquals(2, firstPlayer.food());
        assertEquals(3, firstPlayer.pp());
        assertEquals("BLUE", firstPlayer.totem());

        PlayerInfoDTO secondPlayer = dto.players().get(1);
        assertEquals("alice", secondPlayer.nickname());
        assertEquals(5, secondPlayer.food());
        assertEquals(7, secondPlayer.pp());
        assertEquals("RED", secondPlayer.totem());

        BoardDTO board = dto.board();
        assertEquals("Era I", board.currentEra());
        assertTrue(board.upperCharacterCards().isEmpty());
        assertTrue(board.upperEventCards().isEmpty());
        assertTrue(board.upperBuildingCards().isEmpty());
        assertTrue(board.lowerCharacterCards().isEmpty());
        assertTrue(board.lowerEventCards().isEmpty());
        assertTrue(board.lowerBuildingCards().isEmpty());
        assertTrue(board.offerTrackBoxes().isEmpty());
        assertTrue(board.turnOrderSlots().isEmpty());

        assertEquals(List.of(1), dto.remainingTribeCardIds());
        assertEquals(List.of(10), dto.remainingEra1BuildingIds());
        assertEquals(List.of(11), dto.remainingEra2BuildingIds());
        assertEquals(List.of(12), dto.remainingEra3BuildingIds());
    }

    @Test
    void toGameSnapshotDTO_nullGame_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> GameSnapshotDTOMapper.toGameSnapshotDTO(null));
    }
}
