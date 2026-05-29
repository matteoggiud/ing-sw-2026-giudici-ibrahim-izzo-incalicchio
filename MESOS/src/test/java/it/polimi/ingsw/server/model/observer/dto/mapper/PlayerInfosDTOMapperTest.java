package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.common.network.dto.mapper.PlayerInfosDTOMapper;
import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.characters.ArtistCharacterCard;
import it.polimi.ingsw.server.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorCharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.core.Totem;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.common.network.dto.PlayerDeckDTO;
import it.polimi.ingsw.common.network.dto.PlayerInfoDTO;
import it.polimi.ingsw.common.network.dto.PlayerStatsDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInfosDTOMapperTest {

    /**
     * Tests that a {@link Player} with assorted stats and cards is correctly mapped to a {@link PlayerInfoDTO}.
     * This verifies that basic properties, deck contents, and derived statistics are all accurately populated.
     */
    @Test
    void toPlayerInfoDTO_mapsPlayerFieldsDeckAndStatsCorrectly() {
        // Arrange
        Player player = new Player("alice");
        player.setTotem(Totem.BLUE);
        player.addFood(6);
        player.addPP(9);

        Era1 era1 = new Era1();

        // Add character cards to generate meaningful stats
        ArtistCharacterCard artist = new ArtistCharacterCard(1, "Artist", era1, 2, "artist_img");
        BuilderCharacterCard builder = new BuilderCharacterCard(2, "Builder", era1, 2, 4, 2, "builder_img");
        InventorCharacterCard inventor = new InventorCharacterCard(3, "Inventor", era1, 2, InventorType.BOAT, "inventor_img");

        player.getPlayerDeck().addCard(artist);
        player.getPlayerDeck().addCard(builder);
        player.getPlayerDeck().addCard(inventor);

        // Add a building card so the deck DTO has both sections populated
        IBuildingEffect buildingEffect = new IBuildingEffect() {
        };
        BuildingCard building = new BuildingCard(4, "Building", era1, 2, 5, 3, buildingEffect, "building_img");
        player.getPlayerDeck().addCard(building);

        // Act
        PlayerInfoDTO dto = PlayerInfosDTOMapper.toPlayerInfoDTO(player);

        // Assert - basic player data
        assertEquals("alice", dto.nickname());
        assertEquals(6, dto.food());
        assertEquals(9, dto.pp());
        assertEquals("BLUE", dto.totem());

        // Assert - deck DTO
        PlayerDeckDTO deckDTO = dto.deck();
        assertEquals(3, deckDTO.characterCards().size());
        assertEquals(1, deckDTO.buildingCards().size());

        // Assert - stats DTO
        PlayerStatsDTO stats = dto.stats();
        assertEquals(9, stats.pp());
        assertEquals(6, stats.food());
        assertEquals(3, stats.characterCount());
        assertEquals(0, stats.sustenanceDiscount());
        assertEquals(0, stats.shamanStars());
        assertEquals(2, stats.builderDiscount());
        assertEquals(1, stats.differentInventorIcons());
        assertEquals(0, stats.gathererCount());
        assertEquals(0, stats.hunterCount());
        assertEquals(1, stats.builderCount());
        assertEquals(0, stats.shamanCount());
        assertEquals(1, stats.artistCount());
        assertEquals(1, stats.inventorCount());
    }

    /**
     * Tests mapping a list of multiple {@link Player} instances to a list of {@link PlayerInfoDTO}s.
     * Ensures that all players are mapped and their original ordering is preserved.
     */
    @Test
    void toPlayerInfoDTOList_mapsPlayersInOrder() {
        // Arrange
        Player first = new Player("first");
        first.setTotem(Totem.RED);
        first.addFood(1);
        first.addPP(2);

        Player second = new Player("second");
        second.setTotem(Totem.YELLOW);
        second.addFood(3);
        second.addPP(4);

        // Act
        List<PlayerInfoDTO> dtos = PlayerInfosDTOMapper.toPlayerInfoDTOList(List.of(first, second));

        // Assert
        assertEquals(2, dtos.size());

        PlayerInfoDTO firstDTO = dtos.get(0);
        assertEquals("first", firstDTO.nickname());
        assertEquals("RED", firstDTO.totem());
        assertEquals(1, firstDTO.food());
        assertEquals(2, firstDTO.pp());

        PlayerInfoDTO secondDTO = dtos.get(1);
        assertEquals("second", secondDTO.nickname());
        assertEquals("YELLOW", secondDTO.totem());
        assertEquals(3, secondDTO.food());
        assertEquals(4, secondDTO.pp());
    }
}
