package it.polimi.ingsw.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.characters.*;
import it.polimi.ingsw.model.effect.building.IBuildingEffect;
import it.polimi.ingsw.model.effect.building.endgame.*;
import it.polimi.ingsw.model.effect.building.extraction.ExtraCardBuildingEffect;
import it.polimi.ingsw.model.effect.building.modifiers.*;
import it.polimi.ingsw.model.effect.building.shamanicstars.ShamanicStarsBuildingEffect;
import it.polimi.ingsw.model.effect.building.triggers.TotemBonusBuildingEffect;
import it.polimi.ingsw.model.effect.building.triggers.TriggerCardsSetBuildingEffect;
import it.polimi.ingsw.model.effect.building.triggers.TriggerInventorsCoupleBuildingEffect;
import it.polimi.ingsw.model.effect.event.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataDepository {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test the connection, return true if the connection is valid
     *
     * @return true if the connection is valid, false otherwise
     * @throws SQLException
     */
    public boolean testConnection() throws SQLException {
        Connection c = DatabaseManager.getConnection();
        if (c != null && !c.isClosed() && c.isValid(3)) {
            return true;
        } else {
            return false;
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
        String sql = "SELECT card_id, name, era, min_num_players, character_type, effect_params FROM character_card WHERE character_type = 'ARTIST'";

        List<CharacterCard> allArtistCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allArtistCard.add(new ArtistCharacterCard(cardId, name, era, minNumPlayers));
            }
        }
        return allArtistCard;
    }

    public List<CharacterCard> getBuilderCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, character_type, effect_params FROM character_card WHERE character_type = 'BUILDER'";

        List<CharacterCard> allBuilderCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allBuilderCard.add(new BuilderCharacterCard(cardId, name, era, minNumPlayers, params.path("endGamePP").asInt(0), params.path("foodDiscount").asInt(0)));
            }
        }
        return allBuilderCard;
    }

    public List<CharacterCard> getGathererCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, character_type, effect_params FROM character_card WHERE character_type = 'GATHERER'";

        List<CharacterCard> allGathererCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allGathererCard.add(new GathererCharacterCard(cardId, name, era, minNumPlayers));
            }
        }
        return allGathererCard;
    }

    public List<CharacterCard> getHunterCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, character_type, effect_params FROM character_card WHERE character_type = 'HUNTER'";

        List<CharacterCard> allHunterCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allHunterCard.add(new HunterCharacterCard(cardId, name, era, minNumPlayers, params.path("isFoodIcon").asBoolean(false)));
            }
        }
        return allHunterCard;
    }

    public List<CharacterCard> getInventorCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, character_type, effect_params FROM character_card WHERE character_type = 'INVENTOR'";

        List<CharacterCard> allInventorCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allInventorCard.add(new InventorCharacterCard(cardId, name, era, minNumPlayers, InventorType.valueOf(params.path("inventorType").asText("ARROW"))));
            }
        }
        return allInventorCard;
    }

    public List<CharacterCard> getShamanCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, character_type, effect_params FROM character_card WHERE character_type = 'SHAMAN'";

        List<CharacterCard> allShamanCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allShamanCard.add(new ShamanCharacterCard(cardId, name, era, minNumPlayers, params.path("shamanStars").asInt(1)));
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
        String sql = "SELECT card_id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold FROM event_card WHERE event_type = 'HUNT'";

        List<EventCard> allHunterEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");

                allHunterEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new HuntEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold));
            }
        }
        return allHunterEventCard;
    }

    public List<EventCard> getRockPaintingEventCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold FROM event_card WHERE event_type = 'ROCK_PAINTINGS'";

        List<EventCard> allRockPaintingEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");

                allRockPaintingEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new RockPaintingEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold));
            }
        }
        return allRockPaintingEventCard;
    }

    public List<EventCard> getShamanicRitualEventCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold FROM event_card WHERE event_type = 'SHAMANIC_RITUAL'";

        List<EventCard> allShamanicRitualEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");

                allShamanicRitualEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new ShamanicRitualEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold));
            }
        }
        return allShamanicRitualEventCard;
    }

    public List<EventCard> getSustenanceEventCards() throws SQLException {
        String sql = "SELECT card_id, name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold FROM event_card WHERE event_type = 'SUSTENANCE'";

        List<EventCard> allSustenanceEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");

                allSustenanceEventCard.add(new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, new SustenanceEventEffect(), gainedFood, malusFood, gainedPp, malusPp, threshold));
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
        String sql = "SELECT card_id, name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params FROM building_card";

        List<BuildingCard> allBuildingCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                int foodCost = rs.getInt("food_cost");
                int endGamePp = rs.getInt("end_game_pp");
                String effectId = rs.getString("effect_id");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                IBuildingEffect effect = createBuildingEffect(effectId, params);

                allBuildingCard.add(new BuildingCard(cardId, name, era, minNumPlayers, foodCost, endGamePp, effect));
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
        //TODO: refactor effect id
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
        String query = "SELECT * FROM character_card WHERE card_id = ?";
        CharacterCard card = null;

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, cardId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Era era = Era.valueOf(rs.getString("era"));
                    int minNumPlayers = rs.getInt("min_num_players");
                    CharacterType characterType = CharacterType.valueOf(rs.getString("character_type"));
                    JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                    return switch (characterType) {
                        case ARTIST -> card = new ArtistCharacterCard(cardId, name, era, minNumPlayers);
                        case BUILDER -> card =
                                new BuilderCharacterCard(cardId, name, era, minNumPlayers, params.path("endGamePP").asInt(0), params.path("foodDiscount").asInt(0));
                        case GATHERER -> card = new GathererCharacterCard(cardId, name, era, minNumPlayers);
                        case HUNTER -> card =
                                new HunterCharacterCard(cardId, name, era, minNumPlayers, params.path("isFoodIcon").asBoolean(false));
                        case INVENTOR -> card =
                                new InventorCharacterCard(cardId, name, era, minNumPlayers, InventorType.valueOf(params.path("inventorType").asText("ARROW")));
                        case SHAMAN -> card =
                                new ShamanCharacterCard(cardId, name, era, minNumPlayers, params.path("shamanStars").asInt(1));
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public BuildingCard getBuildingCardById(int cardId) {
        String query = "SELECT * FROM building_card WHERE card_id = ?";
        BuildingCard card = null;

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, cardId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Era era = Era.valueOf(rs.getString("era"));
                    int minNumPlayers = rs.getInt("min_num_players");
                    int foodCost = rs.getInt("food_cost");
                    int endGamePp = rs.getInt("end_game_pp");
                    String effectId = rs.getString("effect_id");
                    JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                    IBuildingEffect effect = createBuildingEffect(effectId, params);

                    card = new BuildingCard(cardId, name, era, minNumPlayers, foodCost, endGamePp, effect);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return card;
    }

    public EventCard getEventCardById(int cardId) {
        String query = "SELECT * FROM event_card WHERE card_id = ?";
        EventCard card = null;

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, cardId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Era era = Era.valueOf(rs.getString("era"));
                    int minNumPlayers = rs.getInt("min_num_players");
                    boolean isFinalEvent = rs.getBoolean("is_final_event");
                    String eventTypeStr = rs.getString("event_type");
                    int gainedFood = rs.getInt("gained_food");
                    int malusFood = rs.getInt("malus_food");
                    int gainedPp = rs.getInt("gained_pp");
                    int malusPp = rs.getInt("malus_pp");
                    int threshold = rs.getInt("threshold");

                    card = new EventCard(cardId, name, era, minNumPlayers, isFinalEvent, switch (eventTypeStr) {
                        case "HUNT" -> new HuntEventEffect();
                        case "ROCK_PAINTINGS" -> new RockPaintingEventEffect();
                        case "SHAMANIC_RITUAL" -> new ShamanicRitualEventEffect();
                        case "SUSTENANCE" -> new SustenanceEventEffect();
                        default -> throw new IllegalArgumentException("eventType invalid: " + eventTypeStr);
                    }, gainedFood, malusFood, gainedPp, malusPp, threshold);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }
}