package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.common.network.dto.mapper.GameInfoDTOMapper;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoDTOMapperTest {

    /**
     * Tests mapping a single {@link Game} instance to a {@link GameInfoDTO}.
     * Verifies that the game's core identifying information and player list are correctly captured.
     */
    @Test
    void toGameInfoDTO_mapsFieldsCorrectly() {
        // Arrange
        Player host = new Player("host");
        Game game = new Game(host, 42, 3);
        game.getPlayers().addPlayer(new Player("alice"));
        game.getPlayers().addPlayer(new Player("bob"));

        // Act
        GameInfoDTO dto = GameInfoDTOMapper.toGameInfoDTO(game);

        // Assert
        assertEquals(42, dto.gameId());
        assertEquals(3, dto.numOfPlayers());
        assertEquals(List.of("host", "alice", "bob"), dto.players());
        assertEquals(3, dto.expectedPlayerCount());
    }

    /**
     * Tests the defensive behavior of {@link GameInfoDTOMapper#toGameInfoDTO(Game)}.
     * Expects an {@link IllegalArgumentException} to be thrown when attempting to map a null game.
     */
    @Test
    void toGameInfoDTO_nullGame_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> GameInfoDTOMapper.toGameInfoDTO(null));
    }

    /**
     * Tests mapping a list of multiple {@link Game} instances to a list of {@link GameInfoDTO}s.
     * Verifies that each game in the list is mapped successfully.
     */
    @Test
    void toGameInfoDTOs_mapsListCorrectly() {
        // Arrange
        Game game1 = new Game(new Player("host1"), 1, 2);
        game1.getPlayers().addPlayer(new Player("alice"));

        Game game2 = new Game(new Player("host2"), 2, 3);
        game2.getPlayers().addPlayer(new Player("bob"));
        game2.getPlayers().addPlayer(new Player("carol"));

        // Act
        List<GameInfoDTO> dtos = GameInfoDTOMapper.toGameInfoDTOs(Arrays.asList(game1, game2));

        // Assert
        assertEquals(2, dtos.size());

        GameInfoDTO first = dtos.get(0);
        assertEquals(1, first.gameId());
        assertEquals(2, first.numOfPlayers());
        assertEquals(List.of("host1", "alice"), first.players());
        assertEquals(2, first.expectedPlayerCount());

        GameInfoDTO second = dtos.get(1);
        assertEquals(2, second.gameId());
        assertEquals(3, second.numOfPlayers());
        assertEquals(List.of("host2", "bob", "carol"), second.players());
        assertEquals(3, second.expectedPlayerCount());
    }

    /**
     * Tests that mapping a null list of games via {@link GameInfoDTOMapper#toGameInfoDTOs(List)}
     * safely throws an {@link IllegalArgumentException}.
     */
    @Test
    void toGameInfoDTOs_nullList_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> GameInfoDTOMapper.toGameInfoDTOs(null));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown if the input list contains a null element.
     * This prevents generating corrupted DTO lists.
     */
    @Test
    void toGameInfoDTOs_listContainingNull_throwsException() {
        // Arrange
        Game game = new Game(new Player("host"), 1, 2);
        List<Game> games = new ArrayList<>();
        games.add(game);
        games.add(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> GameInfoDTOMapper.toGameInfoDTOs(games));
    }
}
