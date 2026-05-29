package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.common.network.dto.mapper.PlayerDeckDTOMapper;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.common.network.dto.BuildingCardDTO;
import it.polimi.ingsw.common.network.dto.CharacterCardDTO;
import it.polimi.ingsw.common.network.dto.PlayerDeckDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDeckDTOMapperTest {

    /**
     * Tests that an empty deck of a newly created {@link Player} maps to a valid {@link PlayerDeckDTO}
     * containing empty lists of character and building cards.
     */
    @Test
    void toPlayerDeckDTO_mapsEmptyDeckCorrectly() {
        // Arrange
        Player player = new Player("alice");

        // Act
        PlayerDeckDTO dto = PlayerDeckDTOMapper.toPlayerDeckDTO(player);

        // Assert
        assertNotNull(dto);
        assertTrue(dto.characterCards().isEmpty());
        assertTrue(dto.buildingCards().isEmpty());
    }

    /**
     * Tests that a player's deck populated with both character and building cards maps correctly.
     * Validates that all relevant properties of individual cards are translated faithfully to their respective DTOs.
     */
    @Test
    void toPlayerDeckDTO_mapsCharactersAndBuildingsCorrectly() {
        // Arrange
        Player player = new Player("alice");
        Era1 era1 = new Era1();

        ArtistCharacterCard artist = new ArtistCharacterCard(1, "Artist", era1, 2, "artist_img");
        IBuildingEffect buildingEffect = new IBuildingEffect() {
        };
        BuildingCard building = new BuildingCard(2, "Building", era1, 2, 4, 5, buildingEffect, "building_img");

        player.getPlayerDeck().addCard(artist);
        player.getPlayerDeck().addCard(building);

        // Act
        PlayerDeckDTO dto = PlayerDeckDTOMapper.toPlayerDeckDTO(player);

        // Assert
        assertEquals(1, dto.characterCards().size());
        assertEquals(1, dto.buildingCards().size());

        CharacterCardDTO characterDTO = dto.characterCards().get(0);
        assertAll(
                () -> assertEquals(1, characterDTO.cardId()),
                () -> assertEquals("Artist", characterDTO.name()),
                () -> assertEquals("Era I", characterDTO.era()),
                () -> assertEquals("artist_img", characterDTO.imageKey()),
                () -> assertEquals(0, characterDTO.shamanStars()),
                () -> assertFalse(characterDTO.foodIcon()),
                () -> assertEquals("", characterDTO.inventorType())
        );

        BuildingCardDTO buildingDTO = dto.buildingCards().get(0);
        assertAll(
                () -> assertEquals(2, buildingDTO.cardId()),
                () -> assertEquals("Building", buildingDTO.name()),
                () -> assertEquals("Era I", buildingDTO.era()),
                () -> assertEquals("building_img", buildingDTO.imageKey()),
                () -> assertEquals(4, buildingDTO.foodCost())
        );
    }

    /**
     * Tests the null safety of {@link PlayerDeckDTOMapper#toPlayerDeckDTO(Player)}.
     * Expects an {@link IllegalArgumentException} to be thrown when a null player is provided.
     */
    @Test
    void toPlayerDeckDTO_nullPlayer_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> PlayerDeckDTOMapper.toPlayerDeckDTO(null));
    }
}
