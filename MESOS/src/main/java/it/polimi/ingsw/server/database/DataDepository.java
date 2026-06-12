package it.polimi.ingsw.server.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.server.model.board.OfferBox;
import it.polimi.ingsw.server.model.card.*;
import it.polimi.ingsw.server.model.card.characters.*;
import it.polimi.ingsw.server.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.endgame.DoubleBuilderBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.endgame.EndPpCardsSetBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.endgame.FlatPrestigePointBuildingCard;
import it.polimi.ingsw.server.model.effect.building.endgame.PpPerCharacterBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.extraction.ExtraCardBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.modifiers.*;
import it.polimi.ingsw.server.model.effect.building.shamanicstars.ShamanicStarsBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.triggers.TotemBonusBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.triggers.TriggerCardsSetBuildingEffect;
import it.polimi.ingsw.server.model.effect.building.triggers.TriggerInventorsCoupleBuildingEffect;
import it.polimi.ingsw.server.model.effect.event.HuntEventEffect;
import it.polimi.ingsw.server.model.effect.event.RockPaintingEventEffect;
import it.polimi.ingsw.server.model.effect.event.ShamanicRitualEventEffect;
import it.polimi.ingsw.server.model.effect.event.SustenanceEventEffect;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import it.polimi.ingsw.server.model.era.Era3;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;
import it.polimi.ingsw.common.network.dto.GameSnapshotDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataDepository {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final ConcurrentHashMap<Integer, BuildingCard> buildingCardCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, EventCard> eventCardCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, CharacterCard> characterCardCache = new ConcurrentHashMap<>();
    private static boolean isIsCacheLoaded = false;

    public synchronized void loadCache() {
        if (!isIsCacheLoaded) {
            try {
                getEventCards().forEach(card -> eventCardCache.put(card.getCardId(), card));
                getCharacterCards().forEach(card -> characterCardCache.put(card.getCardId(), card));
                getBuildingCards().forEach(card -> buildingCardCache.put(card.getCardId(), card));
                isIsCacheLoaded = true;
                System.out.println("[INFO] DataDepository: Card cache loaded successfully, Total cards: " + (eventCardCache.size() + characterCardCache.size() + buildingCardCache.size()));
            } catch (SQLException e) {
                System.err.println("[ERROR] Exception caught while loading character cards into cache: " + e.getMessage());
            }
        }
    }

    public Card getCardById(int cardId) {
        if (!isIsCacheLoaded) {
            loadCache();
        }

        Card card = characterCardCache.get(cardId);
        if (card != null) return card;

        card = buildingCardCache.get(cardId);
        if (card != null) return card;

        card = eventCardCache.get(cardId);
        if (card == null) {
            System.err.println("[WARN] Card with id " + cardId + " not found in cache.");
        }
        return card;
    }

    /**
     * Test the connection, return true if the connection is valid
     *
     * @return true if the connection is valid, false otherwise
     * @throws SQLException
     */
    public boolean testConnection() throws SQLException {
        try (Connection c = DatabaseManager.getConnection()) {
            return c != null && !c.isClosed() && c.isValid(3);
        }
    }

    /**
     * Load all the tribe cards (character and event) from the database
     *
     * @return a list of tribe cards
     * @throws SQLException
     */
    public List<TribeCard> getAllTribeCards() throws SQLException {
        List<TribeCard> allTribeCard = new ArrayList<>();
        allTribeCard.addAll(getCharacterCards());
        allTribeCard.addAll(getEventCards());
        return allTribeCard;
    }

    public List<CharacterCard> getArtistCards() throws SQLException {
        String sql = "SELECT id,name, era, min_num_players, character_type, effect_params,image_key FROM character_card JOIN card ON card_id=character_card_id WHERE character_type = 'ARTIST'";

        List<CharacterCard> allArtistCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                String imageKey = rs.getString("image_key");

                allArtistCard.add(new ArtistCharacterCard(cardId, name, era, minNumPlayers, imageKey));
            }
        }
        return allArtistCard;
    }

    public List<CharacterCard> getBuilderCards() throws SQLException {
        String sql = "SELECT id,name, era, min_num_players, character_type, effect_params,image_key FROM character_card JOIN card ON card_id=character_card_id WHERE character_type = 'BUILDER'";

        List<CharacterCard> allBuilderCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                String imageKey = rs.getString("image_key");

                allBuilderCard.add(new BuilderCharacterCard(cardId, name, era, minNumPlayers, params.path("endGamePP").asInt(0), params.path("foodDiscount").asInt(0), imageKey));
            }
        }
        return allBuilderCard;
    }

    public List<CharacterCard> getGathererCards() throws SQLException {
        String sql = "SELECT id,name, era, min_num_players, character_type, effect_params,image_key FROM character_card JOIN card ON card_id=character_card_id WHERE character_type = 'GATHERER'";

        List<CharacterCard> allGathererCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                String imageKey = rs.getString("image_key");

                allGathererCard.add(new GathererCharacterCard(cardId, name, era, minNumPlayers, imageKey));
            }
        }
        return allGathererCard;
    }

    public List<CharacterCard> getHunterCards() throws SQLException {
        String sql = "SELECT id,name, era, min_num_players, character_type, effect_params,image_key FROM character_card JOIN card ON card_id=character_card_id WHERE character_type = 'HUNTER'";

        List<CharacterCard> allHunterCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                String imageKey = rs.getString("image_key");

                allHunterCard.add(new HunterCharacterCard(cardId, name, era, minNumPlayers, params.path("isFoodIcon").asBoolean(false), imageKey));
            }
        }
        return allHunterCard;
    }

    public List<CharacterCard> getInventorCards() throws SQLException {
        String sql = "SELECT id,name, era, min_num_players, character_type, effect_params,image_key FROM character_card JOIN card ON card_id=character_card_id WHERE character_type = 'INVENTOR'";

        List<CharacterCard> allInventorCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                String imageKey = rs.getString("image_key");

                allInventorCard.add(new InventorCharacterCard(cardId, name, era, minNumPlayers, InventorType.valueOf(params.path("inventorType").asText("ARROW")), imageKey));
            }
        }
        return allInventorCard;
    }

    public List<CharacterCard> getShamanCards() throws SQLException {
        String sql = "SELECT id,name, era, min_num_players, character_type, effect_params,image_key FROM character_card JOIN card ON card_id=character_card_id WHERE character_type = 'SHAMAN'";

        List<CharacterCard> allShamanCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                String imageKey = rs.getString("image_key");

                allShamanCard.add(new ShamanCharacterCard(cardId, name, era, minNumPlayers, params.path("shamanStars").asInt(1), imageKey));
            }
        }
        return allShamanCard;
    }

    public List<CharacterCard> getCharacterCards() throws SQLException {
        List<CharacterCard> allCharacterCard = new ArrayList<>();
        allCharacterCard.addAll(getHunterCards());
        allCharacterCard.addAll(getInventorCards());
        allCharacterCard.addAll(getShamanCards());
        allCharacterCard.addAll(getArtistCards());
        allCharacterCard.addAll(getBuilderCards());
        allCharacterCard.addAll(getGathererCards());
        return allCharacterCard;
    }

    public List<EventCard> getHunterEventCards() throws SQLException {
        String sql = "SELECT id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold, image_key FROM event_card JOIN card ON event_card_id=card_id WHERE event_type = 'HUNT'";

        List<EventCard> allHuntEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");
                String imageKey = rs.getString("image_key");

                allHuntEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new HuntEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold, imageKey));
            }
        }
        return allHuntEventCard;
    }

    public List<EventCard> getRockPaintingEventCards() throws SQLException {
        String sql = "SELECT id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold, image_key FROM event_card JOIN card ON event_card_id=card_id WHERE event_type = 'ROCK_PAINTINGS'";

        List<EventCard> allRockPaintingEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");
                String imageKey = rs.getString("image_key");

                allRockPaintingEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new RockPaintingEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold, imageKey));
            }
        }
        return allRockPaintingEventCard;
    }

    public List<EventCard> getShamanicRitualEventCards() throws SQLException {
        String sql = "SELECT id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold, image_key FROM event_card JOIN card ON event_card_id=card_id WHERE event_type = 'SHAMANIC_RITUAL'";

        List<EventCard> allShamanicRitualEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");
                String imageKey = rs.getString("image_key");

                allShamanicRitualEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new ShamanicRitualEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold, imageKey));
            }
        }
        return allShamanicRitualEventCard;
    }

    public List<EventCard> getSustenanceEventCards() throws SQLException {
        String sql = "SELECT id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold, image_key FROM event_card JOIN card ON event_card_id=card_id WHERE event_type = 'SUSTENANCE'";

        List<EventCard> allSustenanceEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");
                String imageKey = rs.getString("image_key");

                allSustenanceEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new SustenanceEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold, imageKey));
            }
        }
        return allSustenanceEventCard;
    }

    public List<EventCard> getEventCards() throws SQLException {
        List<EventCard> allEventCards = new ArrayList<>();
        allEventCards.addAll(getHunterEventCards());
        allEventCards.addAll(getRockPaintingEventCards());
        allEventCards.addAll(getShamanicRitualEventCards());
        allEventCards.addAll(getSustenanceEventCards());
        return allEventCards;
    }

    /**
     * Load all the building cards from the database
     *
     * @return a list of building cards
     * @throws SQLException
     */
    public List<BuildingCard> getBuildingCards() throws SQLException {
        String sql = "SELECT id, name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params, image_key FROM building_card JOIN card ON card_id=building_card_id";

        List<BuildingCard> allBuildingCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("id");
                String name = rs.getString("name");
                Era era = createEra(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                int foodCost = rs.getInt("food_cost");
                int endGamePp = rs.getInt("end_game_pp");
                String effectId = rs.getString("effect_id");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                IBuildingEffect effect = createBuildingEffect(effectId, params);
                String imageKey = rs.getString("image_key");

                allBuildingCard.add(new BuildingCard(cardId, name, era, minNumPlayers, foodCost, endGamePp, effect, imageKey));
            }
        }
        return allBuildingCard;
    }

    /**
     * Create a building effect based on the effect id and parameters
     *
     * @param effectId
     * @param params
     * @return
     */
    private IBuildingEffect createBuildingEffect(String effectId, JsonNode params) {
        return switch (effectId) {
            case "DOUBLEBUILDERBUILDINGEFFECT" -> new DoubleBuilderBuildingEffect();
            case "ENDPPCARDSSETBUILDINGEFFECT" -> new EndPpCardsSetBuildingEffect();
            case "FLATPRESTIGEPOINTBUILDINGCARD" -> new FlatPrestigePointBuildingCard();
            case "PPPERCHARACTERBUILDINGEFFECT" ->
                    new PpPerCharacterBuildingEffect(params.path("pp").asInt(1), CharacterType.valueOf(params.path("characterType").asText("HUNTER")));
            case "EXTRACARDBUILDINGEFFECT" -> new ExtraCardBuildingEffect();
            case "HUNTBONUSBUILDINGEFFECT" -> new HuntBonusBuildingEffect();
            case "ROCKPAINTINGSBONUSBUILDINGEFFECT" -> new RockPaintingsBonusBuildingEffect();
            case "SHAMANICDOUBLEBUILDINGEFFECT" -> new ShamanicDoubleBuildingEffect();
            case "SHAMANICIMMUNITYBUILDINGEFFECT" -> new ShamanicImmunityBuildingEffect();
            case "SUSTENANCEDISCOUNTBUILDINGEFFECT" ->
                    new SustenanceDiscountBuildingEffect(CharacterType.valueOf(params.path("characterType").asText("GATHERER")));
            case "SHAMANICSTARSBUILDINGEFFECT" -> new ShamanicStarsBuildingEffect();
            case "TOTEMBONUSBUILDINGEFFECT" -> new TotemBonusBuildingEffect();
            case "TRIGGERCARDSSETBUILDINGEFFECT" -> new TriggerCardsSetBuildingEffect();
            case "TRIGGERINVENTORSCOUPLEBUILDINGEFFECT" -> new TriggerInventorsCoupleBuildingEffect();
            default -> throw new IllegalArgumentException("effectId non gestito: " + effectId);
        };
    }

    private JsonNode parseJsonOrEmpty(String raw) {
        try {
            if (raw == null || raw.isBlank()) {
                return mapper.createObjectNode();
            }
            return mapper.readTree(raw);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in DataDepository: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("effectParams JSON non valido: " + raw, e);
        }
    }

    /**
     * Load a card by its id from the database
     *
     * @param cardId the id of the card to load
     * @return the card with the specified id, or null if no such card exists
     */
    public CharacterCard getCharacterCardById(int cardId) {
        if (!isIsCacheLoaded) {
            loadCache();
        }
        return characterCardCache.get(cardId);
    }

    public BuildingCard getBuildingCardById(int cardId) {
        if (!isIsCacheLoaded) {
            loadCache();
        }
        return buildingCardCache.get(cardId);
    }

    public EventCard getEventCardById(int cardId) {
        if (!isIsCacheLoaded) {
            loadCache();
        }
        return eventCardCache.get(cardId);
    }

    private Era createEra(String era) {
        switch (era) {
            case "ERA_1" -> {
                return new Era1();
            }
            case "ERA_2" -> {
                return new Era2();
            }
            case "ERA_3" -> {
                return new Era3();
            }
            default -> throw new IllegalArgumentException("Invalid Era: " + era);
        }
    }

    public List<LeadBoardDTO> getLeadBoard(int numPlayers) {
        String rankings = """
                SELECT
                    p.nickname,
                    SUM(gp.final_score) AS total_score,
                    MAX(g.finished_at) AS finished_at
                FROM player p
                INNER JOIN game_player gp ON p.player_id = gp.player_id
                INNER JOIN game g ON gp.game_id = g.game_id
                WHERE g.finished_at IS NOT NULL
                  AND g.expected_players = ?
                GROUP BY p.nickname
                ORDER BY total_score DESC, p.nickname ASC;
                """;

        List<LeadBoardDTO> leadBoardDTO = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(rankings)) {

            ps.setInt(1, numPlayers);

            try (ResultSet rs = ps.executeQuery()) {
                int position = 1;

                while (rs.next()) {
                    String nickname = rs.getString("nickname");
                    int finalScore = rs.getInt("total_score");
                    String finishedAt = rs.getString("finished_at");

                    leadBoardDTO.add(new LeadBoardDTO(nickname, finalScore, position++, finishedAt));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DataDepository: " + e.getMessage());
            e.printStackTrace();
        }

        return leadBoardDTO;
    }

    /**
     * Loads the saved game snapshot from the database for a specific game.
     *
     * @param gameId the unique identifier of the game
     * @return the GameSnapshotDTO if found, otherwise null
     */
    public GameSnapshotDTO loadGameSnapshot(int gameId) {
        String query = "SELECT game_snapshot FROM game WHERE game_id = ?";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, gameId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String jsonSnapshot = rs.getString("game_snapshot");
                    if (jsonSnapshot != null && !jsonSnapshot.isBlank()) {
                        return mapper.readValue(jsonSnapshot, GameSnapshotDTO.class);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception caught in DataDepository (loadGameSnapshot): " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return null if no snapshot exists or an error occurred
    }

    /**
     * Retrieves a list of game IDs for all games that are currently marked as in progress.
     *
     * @return a list of game IDs
     */
    public List<Integer> getInProgressGames() {
        String query = "SELECT game_id FROM game WHERE in_progress = true";
        List<Integer> gameIds = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                gameIds.add(rs.getInt("game_id"));
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DataDepository (getInProgressGames): " + e.getMessage());
            e.printStackTrace();
        }

        return gameIds;
    }

}