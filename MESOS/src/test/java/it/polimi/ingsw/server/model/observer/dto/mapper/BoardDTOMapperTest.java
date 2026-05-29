package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.common.network.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.server.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.common.network.dto.BoardDTO;
import it.polimi.ingsw.common.network.dto.CharacterCardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardDTOMapperTest {

    private Game game;

    @BeforeEach
    void setUp() {
        Player host = new Player("host");
        game = new Game(host, 1, 2);
        game.getPlayers().addPlayer(new Player("alice"));

        Era1 era1 = new Era1();
        game.setCurrentEra(era1);

        // Add only one character card to keep the setup minimal and stable.
        ArtistCharacterCard upperCharacter = new ArtistCharacterCard(
                1, "Upper Artist", era1, 2, "upperCharacter"
        );
        game.getBoard().getUpperRow().addCard(upperCharacter);
    }

    @Test
    void toBoardDTO_mapsMinimalBoardStateCorrectly() {
        // Act
        BoardDTO dto = BoardDTOMapper.toBoardDTO(game);

        // Assert
        assertEquals("Era I", dto.currentEra());

        assertEquals(1, dto.upperCharacterCards().size());
        assertTrue(dto.upperEventCards().isEmpty());
        assertTrue(dto.upperBuildingCards().isEmpty());

        assertTrue(dto.lowerCharacterCards().isEmpty());
        assertTrue(dto.lowerEventCards().isEmpty());
        assertTrue(dto.lowerBuildingCards().isEmpty());

        assertTrue(dto.offerTrackBoxes().isEmpty());
        assertTrue(dto.turnOrderSlots().isEmpty());

        CharacterCardDTO upperCharacter = dto.upperCharacterCards().get(0);
        assertEquals(1, upperCharacter.cardId());
        assertEquals("Upper Artist", upperCharacter.name());
        assertEquals("Era I", upperCharacter.era());
        assertEquals("upperCharacter", upperCharacter.imageKey());
        assertEquals(0, upperCharacter.shamanStars());
        assertFalse(upperCharacter.foodIcon());
        assertEquals("", upperCharacter.inventorType());
    }

    @Test
    void toBoardDTO_nullGame_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> BoardDTOMapper.toBoardDTO(null));
    }

    @Test
    void toBoardDTO_withoutCurrentEra_throwsException() {
        // Arrange
        Player host = new Player("h");
        Game gameWithoutEra = new Game(host, 2, 2);
        gameWithoutEra.getPlayers().addPlayer(new Player("a"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> BoardDTOMapper.toBoardDTO(gameWithoutEra));
    }
}
