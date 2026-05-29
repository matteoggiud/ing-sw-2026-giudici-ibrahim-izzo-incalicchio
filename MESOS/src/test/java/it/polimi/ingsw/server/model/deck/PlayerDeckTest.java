package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.card.characters.BuilderCharacterCard;
import it.polimi.ingsw.server.model.card.characters.GathererCharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorCharacterCard;
import it.polimi.ingsw.server.model.card.characters.InventorType;
import it.polimi.ingsw.server.model.card.characters.ShamanCharacterCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.endgame.IEndGameScoring;
import it.polimi.ingsw.server.model.effect.building.extraction.IExtraActionEffect;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.server.model.effect.building.shamanicstars.IShamanicStarsProvider;
import it.polimi.ingsw.server.model.effect.building.triggers.IEventTrigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDeckTest {

    private PlayerDeck playerDeck;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        playerDeck = new PlayerDeck();
        nextCardId = 1;
        era1 = new Era1();
    }

    @Test
    void getCharacters() {
        assertNotNull(playerDeck.getCharacters());
        assertTrue(playerDeck.getCharacters().isEmpty());

        playerDeck.addCharacterCard(new GathererCharacterCard(nextCardId++, "Gatherer", era1, 2, ""));
        assertEquals(1, playerDeck.getCharacters().size());

        var characters = playerDeck.getCharacters();
        characters.clear();
        assertEquals(1, playerDeck.getCharacters().size());
    }

    @Test
    void getBuildings() {
        assertNotNull(playerDeck.getBuildings());
        assertTrue(playerDeck.getBuildings().isEmpty());

        playerDeck.addBuildingCard(newBuilding(new EmptyEffect()));
        assertEquals(1, playerDeck.getBuildings().size());

        var buildings = playerDeck.getBuildings();
        buildings.clear();
        assertEquals(1, playerDeck.getBuildings().size());
    }

    @Test
    void countCharactersByType() {
        assertEquals(0, playerDeck.countCharactersByType(CharacterType.GATHERER));

        playerDeck.addCharacterCard(new GathererCharacterCard(nextCardId++, "G1", era1, 2, ""));
        playerDeck.addCharacterCard(new GathererCharacterCard(nextCardId++, "G2", era1, 2, ""));
        playerDeck.addCharacterCard(new BuilderCharacterCard(nextCardId++, "B1", era1, 2, 0, 1, ""));

        assertEquals(2, playerDeck.countCharactersByType(CharacterType.GATHERER));
        assertEquals(1, playerDeck.countCharactersByType(CharacterType.BUILDER));
        assertEquals(0, playerDeck.countCharactersByType(CharacterType.SHAMAN));
    }

    @Test
    void incrementTypeCount() {
        assertEquals(0, playerDeck.countCharactersByType(CharacterType.INVENTOR));

        playerDeck.incrementTypeCount(CharacterType.INVENTOR);
        playerDeck.incrementTypeCount(CharacterType.INVENTOR);

        assertEquals(2, playerDeck.countCharactersByType(CharacterType.INVENTOR));
    }

    @Test
    void addBuildingCard() {
        BuildingCard buildingCard = newBuilding(new EmptyEffect());

        playerDeck.addBuildingCard(buildingCard);

        assertEquals(1, playerDeck.getBuildings().size());
        assertSame(buildingCard, playerDeck.getBuildings().get(0));
    }

    @Test
    void addCharacterCard() {
        GathererCharacterCard gatherer = new GathererCharacterCard(nextCardId++, "Gatherer", era1, 2, "");

        playerDeck.addCharacterCard(gatherer);

        assertEquals(1, playerDeck.getCharacters().size());
        assertSame(gatherer, playerDeck.getCharacters().get(0));
        assertEquals(1, playerDeck.countCharactersByType(CharacterType.GATHERER));
    }

    @Test
    void addCard() {
        BuildingCard buildingCard = newBuilding(new EmptyEffect());
        BuilderCharacterCard builder = new BuilderCharacterCard(nextCardId++, "Builder", era1, 2, 0, 2, "");

        playerDeck.addCard(buildingCard);
        playerDeck.addCard(builder);

        assertEquals(1, playerDeck.getBuildings().size());
        assertEquals(1, playerDeck.getCharacters().size());
        assertSame(buildingCard, playerDeck.getBuildings().get(0));
        assertSame(builder, playerDeck.getCharacters().get(0));
        assertEquals(1, playerDeck.countCharactersByType(CharacterType.BUILDER));
    }

    @Test
    void getTotalBuilderDiscount() {
        assertEquals(0, playerDeck.getTotalBuilderDiscount());

        playerDeck.addCharacterCard(new BuilderCharacterCard(nextCardId++, "B1", era1, 2, 0, 2, ""));
        playerDeck.addCharacterCard(new BuilderCharacterCard(nextCardId++, "B2", era1, 2, 0, 3, ""));

        assertEquals(5, playerDeck.getTotalBuilderDiscount());
    }

    @Test
    void getTotalGathererDiscount() {
        assertEquals(0, playerDeck.getTotalGathererDiscount());

        playerDeck.addCharacterCard(new GathererCharacterCard(nextCardId++, "G1", era1, 2, ""));
        playerDeck.addCharacterCard(new GathererCharacterCard(nextCardId++, "G2", era1, 2, ""));

        assertEquals(6, playerDeck.getTotalGathererDiscount());
    }

    @Test
    void getTotalShamanStars() {
        playerDeck.addCharacterCard(new ShamanCharacterCard(nextCardId++, "S1", era1, 2, 2, ""));
        playerDeck.addCharacterCard(new ShamanCharacterCard(nextCardId++, "S2", era1, 2, 3, ""));
        playerDeck.addBuildingCard(newBuilding(new ShamanStarsEffect(4)));

        assertEquals(9, playerDeck.getTotalShamanStars());
    }

    @Test
    void getDifferentInventorItem() {
        assertEquals(0, playerDeck.getDifferentInventorItem());

        playerDeck.addCharacterCard(new InventorCharacterCard(nextCardId++, "I1", era1, 2, InventorType.BOAT, ""));
        playerDeck.addCharacterCard(new InventorCharacterCard(nextCardId++, "I2", era1, 2, InventorType.BOAT, ""));
        playerDeck.addCharacterCard(new InventorCharacterCard(nextCardId++, "I3", era1, 2, InventorType.ARROW, ""));

        assertEquals(2, playerDeck.getDifferentInventorItem());
    }

    @Test
    void getActiveBuildingEventModifiers() {
        ModifierEffect modifierEffect = new ModifierEffect();
        playerDeck.addBuildingCard(newBuilding(modifierEffect));
        playerDeck.addBuildingCard(newBuilding(new EmptyEffect()));

        var modifiers = playerDeck.getActiveBuildingEventModifiers();

        assertEquals(1, modifiers.size());
        assertSame(modifierEffect, modifiers.get(0));
    }

    @Test
    void getActiveBuildingEventTriggers() {
        TriggerEffect triggerEffect = new TriggerEffect();
        playerDeck.addBuildingCard(newBuilding(triggerEffect));
        playerDeck.addBuildingCard(newBuilding(new EmptyEffect()));

        var triggers = playerDeck.getActiveBuildingEventTriggers();

        assertEquals(1, triggers.size());
        assertSame(triggerEffect, triggers.get(0));
    }

    @Test
    void getActiveEndGameBuildingScorings() {
        EndGameEffect endGameEffect = new EndGameEffect();
        playerDeck.addBuildingCard(newBuilding(endGameEffect));
        playerDeck.addBuildingCard(newBuilding(new EmptyEffect()));

        var scorings = playerDeck.getActiveEndGameBuildingScorings();

        assertEquals(1, scorings.size());
        assertSame(endGameEffect, scorings.get(0));
    }

    @Test
    void getActiveExtractionEffects() {
        ExtraActionEffect extraActionEffect = new ExtraActionEffect();
        playerDeck.addBuildingCard(newBuilding(extraActionEffect));
        playerDeck.addBuildingCard(newBuilding(new EmptyEffect()));

        var extraEffects = playerDeck.getActiveExtractionEffects();

        assertEquals(1, extraEffects.size());
        assertSame(extraActionEffect, extraEffects.get(0));
    }

    private BuildingCard newBuilding(IBuildingEffect effect) {
        return new BuildingCard(nextCardId++, "Building" + nextCardId, era1, 2, 5, 2, effect, "");
    }

    private static final class EmptyEffect implements IBuildingEffect {
    }

    private static final class ShamanStarsEffect implements IBuildingEffect, IShamanicStarsProvider {
        private final int bonusStars;

        private ShamanStarsEffect(int bonusStars) {
            this.bonusStars = bonusStars;
        }

        @Override
        public int getBonusShamanStars() {
            return bonusStars;
        }

        @Override
        public Optional<IShamanicStarsProvider> getShamanicStarsProvider() {
            return Optional.of(this);
        }
    }

    private static final class ModifierEffect implements IBuildingEffect, IEventModifier {
        @Override
        public Optional<IEventModifier> getEventModifier() {
            return Optional.of(this);
        }
    }

    private static final class TriggerEffect implements IBuildingEffect, IEventTrigger {
        @Override
        public int getTrigger(Player player, Game game) {
            return 0;
        }

        @Override
        public Optional<IEventTrigger> getEventTrigger() {
            return Optional.of(this);
        }
    }

    private static final class EndGameEffect implements IBuildingEffect, IEndGameScoring {
        @Override
        public int calculateEndGameBonus(Player player) {
            return 1;
        }

        @Override
        public Optional<IEndGameScoring> getEndGameScoring() {
            return Optional.of(this);
        }
    }

    private static final class ExtraActionEffect implements IBuildingEffect, IExtraActionEffect {
        @Override
        public boolean hasExtraAction() {
            return true;
        }

        @Override
        public Optional<IExtraActionEffect> getExtraActionEffect() {
            return Optional.of(this);
        }
    }
}
