package it.polimi.ingsw.database;

import it.polimi.ingsw.model.card.BuildingCard;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.List;

public class DbTest {
    public static void main(String[] args) {
        DataDepository d = new DataDepository();

        try {
            boolean ok = d.testConnection();
            System.out.println("Connessione DB valida: " + ok);

            List<CharacterCard> characters = d.loadCharacterCards();
            List<EventCard> events = d.loadEventCards();
            List<BuildingCard> buildings = d.loadBuildingCards();

            System.out.println("CharacterCard caricati: " + characters.size());
            System.out.println("EventCard caricati: " + events.size());
            System.out.println("BuildingCard caricati: " + buildings.size());

            characters.stream().limit(3).forEach(c ->
                    System.out.println("[CHAR] " + c.getName() + " | " + c.getType()));
            events.stream().limit(3).forEach(e ->
                    System.out.println("[EVNT] " + e.getName() + " | " + e.getEventType()));
            buildings.stream().limit(3).forEach(b ->
                    System.out.println("[BLDG] " + b.getName() + " | " + b.getEffect().getClass().getSimpleName()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
