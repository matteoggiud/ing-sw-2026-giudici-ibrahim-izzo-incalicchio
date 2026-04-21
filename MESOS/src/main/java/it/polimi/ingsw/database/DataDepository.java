package it.polimi.ingsw.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.characters.*;
import it.polimi.ingsw.model.core.Player;
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
import java.util.Date;
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
        if(c != null && !c.isClosed() && c.isValid(3)){
            return true;
        }else{
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

    public List<CharacterCard> getCharacterCards() throws SQLException {
        String sql = "SELECT name, era, min_num_players, character_type, effect_params FROM character_card";

        List<CharacterCard> allCharacterCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                CharacterType type = CharacterType.valueOf(rs.getString("character_type"));
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));

                allCharacterCard.add(createCharacterCard(name, era, minNumPlayers, type, params));
            }
        }
        return allCharacterCard;
    }

    /**
     * Load all the event cards from the database
     *
     * @return a list of event cards
     * @throws SQLException
     */
    public List<EventCard> getEventCards() throws SQLException {
        String sql = "SELECT name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp, malus_pp, threshold FROM event_card";

        List<EventCard> allEventCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                boolean isFinalEvent = rs.getBoolean("is_final_event");
                EventType eventType = EventType.valueOf(rs.getString("event_type"));
                IEventEffect eventEffect = createEventEffect(eventType);
                int gainedFood = rs.getInt("gained_food");
                int malusFood = rs.getInt("malus_food");
                int gainedPp = rs.getInt("gained_pp");
                int malusPp = rs.getInt("malus_pp");
                int threshold = rs.getInt("threshold");

                allEventCard.add(new EventCard(name, era, minNumPlayers, isFinalEvent, eventType, eventEffect, gainedFood, malusFood, gainedPp, malusPp, threshold));
            }
        }
        return allEventCard;
    }

    /**
     * Load all the building cards from the database
     *
     * @return a list of building cards
     * @throws SQLException
     */
    public List<BuildingCard> getBuildingCards() throws SQLException {
        String sql = "SELECT name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params FROM building_card";

        List<BuildingCard> allBuildingCard = new ArrayList<>();

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                Era era = Era.valueOf(rs.getString("era"));
                int minNumPlayers = rs.getInt("min_num_players");
                int foodCost = rs.getInt("food_cost");
                int endGamePp = rs.getInt("end_game_pp");
                String effectId = rs.getString("effect_id");
                JsonNode params = parseJsonOrEmpty(rs.getString("effect_params"));
                IBuildingEffect effect = createBuildingEffect(effectId, params);

                allBuildingCard.add(new BuildingCard(name, era, minNumPlayers, foodCost, endGamePp, effect));
            }
        }
        return allBuildingCard;
    }

    /**
     * Create a character card based on the type and parameters
     *
     * @param name
     * @param era
     * @param minNumPlayers
     * @param type
     * @param params
     * @return
     */
    private CharacterCard createCharacterCard(String name, Era era, int minNumPlayers, CharacterType type, JsonNode params) {
        return switch (type) {
            case GATHERER -> new GathererCharacterCard(name, era, minNumPlayers);
            case ARTIST -> new ArtistCharacterCard(name, era, minNumPlayers);
            case HUNTER -> new HunterCharacterCard(name, era, minNumPlayers, params.path("isFoodIcon").asBoolean(false));
            case SHAMAN -> new ShamanCharacterCard(name, era, minNumPlayers, params.path("shamanStars").asInt(1));
            case BUILDER -> new BuilderCharacterCard(name, era, minNumPlayers, params.path("endGamePP").asInt(0), params.path("foodDiscount").asInt(0));
            //The parameter ARROW is the default parameter choose in the case that the effectParam is not filled
            case INVENTOR -> new InventorCharacterCard(name, era, minNumPlayers, InventorType.valueOf(params.path("inventorType").asText("ARROW")));
        };
    }

    /**
     * Create an event effect based on the event type
     *
     * @param eventType
     * @return
     */
    private IEventEffect createEventEffect(EventType eventType) {
        if(eventType == null){
            throw new NullPointerException("eventType is null");
        }else{
            switch (eventType) {
                case HUNT -> {return new HuntEventEffect();}
                case SHAMANIC_RITUAL -> {return new ShamanicRitualEventEffect();}
                case ROCK_PAINTINGS -> {return new RockPaintingEventEffect();}
                case SUSTENANCE -> {return new SustenanceEventEffect();}
                default -> throw new IllegalArgumentException("eventType non gestito: " + eventType);
            }
        }
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
            case "PPPERCHARACTERBUILDINGEFFECT" -> new PpPerCharacterBuildingEffect(params.path("pp").asInt(1), CharacterType.valueOf(params.path("characterType").asText("HUNTER")));
            case "EXTRACARDBUILDINGEFFECT" -> new ExtraCardBuildingEffect();
            case "HUNTBONUSBUILDINGEFFECT" -> new HuntBonusBuildingEffect();
            case "ROCKPAINTINGSBONUSBUILDINGEFFECT" -> new RockPaintingsBonusBuildingEffect();
            case "SHAMANICDOUBLEBUILDINGEFFECT" -> new ShamanicDoubleBuildingEffect();
            case "SHAMANICIMMUNITYBUILDINGEFFECT" -> new ShamanicImmunityBuildingEffect();
            case "SUSTENANCEDISCOUNTBUILDINGEFFECT" -> new SustenanceDiscountBuildingEffect(CharacterType.valueOf(params.path("characterType").asText("GATHERER")));
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
}
