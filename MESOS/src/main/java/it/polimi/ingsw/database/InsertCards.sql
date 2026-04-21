
-- Character cards Era 1
INSERT INTO character_card (name, era, min_num_players, character_type, effect_params)
VALUES
    --Hunters
    ('Hunter 1', 'ERA_1', null, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter 2', 'ERA_1', null, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter 3', 'ERA_1', null, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter 4', 'ERA_1', 3, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter 5', 'ERA_1', 3, 'HUNTER', '{"isFoodIcon": false}'),

-- Builders
    ('Builder 1', 'ERA_1', null, 'BUILDER', '{"endGamePP": 3, "foodDiscount": 1}'),
    ('Builder 2', 'ERA_1', null, 'BUILDER', '{"endGamePP": 0, "foodDiscount": 2}'),
    ('Builder 3', 'ERA_1', 5, 'BUILDER', '{"endGamePP": 1, "foodDiscount": 2}'),
    ('Builder 4', 'ERA_1', null, 'BUILDER', '{"endGamePP": 2, "foodDiscount": 1}'),

-- Gatherers
    ('Gatherer 1', 'ERA_1', null, 'GATHERER', '{}'),
    ('Gatherer 2', 'ERA_1', null, 'GATHERER', '{}'),
    ('Gatherer 3', 'ERA_1', 3, 'GATHERER', '{}'),
    ('Gatherer 4', 'ERA_1', 5, 'GATHERER', '{}'),

-- Artists
    ('Artist 1', 'ERA_1', null, 'ARTIST', '{}'),
    ('Artist 2', 'ERA_1', null, 'ARTIST', '{}'),
    ('Artist 3', 'ERA_1', null, 'ARTIST', '{}'),
    ('Artist 4', 'ERA_1', 3, 'ARTIST', '{}'),
    ('Artist 5', 'ERA_1', 4, 'ARTIST', '{}'),

-- Inventors
    ('Inventor 1', 'ERA_1', null, 'INVENTOR', '{"inventorType": "LEATHER"}'),
    ('Inventor 2', 'ERA_1', null, 'INVENTOR', '{"inventorType": "BOAT"}'),
    ('Inventor 3', 'ERA_1', null, 'INVENTOR', '{"inventorType": "ARROW"}'),
    ('Inventor 4', 'ERA_1', null, 'INVENTOR', '{"inventorType": "BREAD"}'),
    ('Inventor 5', 'ERA_1', 4, 'INVENTOR', '{"inventorType": "ROPE"}'),
    ('Inventor 6', 'ERA_1', 4, 'INVENTOR', '{"inventorType": "FLUTE"}'),
    ('Inventor 7', 'ERA_1', 4, 'INVENTOR', '{"inventorType": "SOUP"}'),

-- Shamans
    ('Shaman 1', 'ERA_1', 5, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman 2', 'ERA_1', null, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman 3', 'ERA_1', null, 'SHAMAN', '{"shamanStars": 1}'),
    ('Shaman 3', 'ERA_1', 4, 'SHAMAN', '{"shamanStars": 1}');


-- Character cards Era 2
INSERT INTO character_card (name, era, min_num_players, character_type, effect_params)
VALUES
    --Hunters
    ('Hunter 1', 'ERA_2', null, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter 2', 'ERA_2', null, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter 3', 'ERA_2', 3, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter 4', 'ERA_2', null, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter 5', 'ERA_2', 4, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter 6', 'ERA_2', 5, 'HUNTER', '{"isFoodIcon": false}'),

    -- Builders
    ('Builder 1', 'ERA_2', null, 'BUILDER', '{"endGamePP": 4, "foodDiscount": 1}'),
    ('Builder 2', 'ERA_2', null, 'BUILDER', '{"endGamePP": 1, "foodDiscount": 2}'),
    ('Builder 3', 'ERA_2', 3, 'BUILDER', '{"endGamePP": 2, "foodDiscount": 1}'),
    ('Builder 4', 'ERA_2', null, 'BUILDER', '{"endGamePP": 3, "foodDiscount": 2}'),

    -- Gatherers
    ('Gatherer 1', 'ERA_2', null, 'GATHERER', '{}'),
    ('Gatherer 2', 'ERA_2', 3, 'GATHERER', '{}'),
    ('Gatherer 3', 'ERA_2', 4, 'GATHERER', '{}'),
    ('Gatherer 4', 'ERA_2', 5, 'GATHERER', '{}'),

    -- Artists
    ('Artist 1', 'ERA_2', 3, 'ARTIST', '{}'),
    ('Artist 2', 'ERA_2', null, 'ARTIST', '{}'),
    ('Artist 3', 'ERA_2', null, 'ARTIST', '{}'),
    ('Artist 4', 'ERA_2', null, 'ARTIST', '{}'),

-- Inventors
    ('Inventor 1', 'ERA_2', null, 'INVENTOR', '{"inventorType": "TOTEM"}'),
    ('Inventor 2', 'ERA_2', 4, 'INVENTOR', '{"inventorType": "HOOK"}'),
    ('Inventor 3', 'ERA_2', null, 'INVENTOR', '{"inventorType": "ROPE"}'),
    ('Inventor 4', 'ERA_2', null, 'INVENTOR', '{"inventorType": "FLUTE"}'),
    ('Inventor 5', 'ERA_2', null, 'INVENTOR', '{"inventorType": "SOUP"}'),
    ('Inventor 6', 'ERA_2', null, 'INVENTOR', '{"inventorType": "LEATHER"}'),

-- Shamans
    ('Shaman 1', 'ERA_2', null, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman 2', 'ERA_2', null, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman 3', 'ERA_2', 5, 'SHAMAN', '{"shamanStars": 1}'),
    ('Shaman 3', 'ERA_2', 5, 'SHAMAN', '{"shamanStars": 2}');


-- Character cards Era 3
INSERT INTO character_card (name, era, min_num_players, character_type, effect_params)
VALUES
    --Hunters
    ('Hunter 1', 'ERA_3', 5, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter 2', 'ERA_3', null, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter 3', 'ERA_3', null, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter 4', 'ERA_3', null, 'HUNTER', '{"isFoodIcon": true}'),

    -- Builders
    ('Builder 1', 'ERA_3', null, 'BUILDER', '{"endGamePP": 5, "foodDiscount": 1}'),
    ('Builder 2', 'ERA_3', null, 'BUILDER', '{"endGamePP": 3, "foodDiscount": 2}'),
    ('Builder 3', 'ERA_3', 5, 'BUILDER', '{"endGamePP": 4, "foodDiscount": 1}'),
    ('Builder 4', 'ERA_3', null, 'BUILDER', '{"endGamePP": 2, "foodDiscount": 2}'),

    -- Gatherers
    ('Gatherer 1', 'ERA_3', 5, 'GATHERER', '{}'),
    ('Gatherer 2', 'ERA_3', 4, 'GATHERER', '{}'),
    ('Gatherer 3', 'ERA_3', null, 'GATHERER', '{}'),

    -- Artists
    ('Artist 1', 'ERA_3', 5, 'ARTIST', '{}'),
    ('Artist 2', 'ERA_3', null, 'ARTIST', '{}'),
    ('Artist 3', 'ERA_3', null, 'ARTIST', '{}'),
    ('Artist 4', 'ERA_3', null, 'ARTIST', '{}'),

-- Inventors
    ('Inventor 1', 'ERA_3', 4, 'INVENTOR', '{"inventorType": "CHAIN"}'),
    ('Inventor 2', 'ERA_3', 3, 'INVENTOR', '{"inventorType": "BOAT"}'),
    ('Inventor 3', 'ERA_3', 3, 'INVENTOR', '{"inventorType": "ARROW"}'),
    ('Inventor 4', 'ERA_3', null, 'INVENTOR', '{"inventorType": "TOTEM"}'),
    ('Inventor 5', 'ERA_3', null, 'INVENTOR', '{"inventorType": "HOOK"}'),
    ('Inventor 6', 'ERA_3', null, 'INVENTOR', '{"inventorType": "CHAIN"}'),
    ('Inventor 7', 'ERA_3', null, 'INVENTOR', '{"inventorType": "BREAD"}'),

-- Shamans
    ('Shaman 1', 'ERA_3', 3, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman 2', 'ERA_3', null, 'SHAMAN', '{"shamanStars": 3}'),
    ('Shaman 3', 'ERA_3', null, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman 3', 'ERA_3', null, 'SHAMAN', '{"shamanStars": 3}'),
    ('Shaman 3', 'ERA_3', 4, 'SHAMAN', '{"shamanStars": 2}');



-- Event cards Era 1
INSERT INTO event_card (name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp,
                        malus_pp, threshold)
VALUES ('Hunt 1', 'ERA_1', null, false, 'HUNT', 1, 0, 1, 0, 0),
       ('Sustenance 1', 'ERA_1', null, false, 'SUSTENANCE', 0, 1, 0, 1, 0),
       ('Ritual Event', 'ERA_1', null, false, 'SHAMANIC_RITUAL', 0, 0, 5, 3, 0),
       ('Rock Paintings 1', 'ERA_1', null, false, 'ROCK_PAINTINGS', 0, 0, 1, 2, 1);

-- Event cards Era 2
INSERT INTO event_card (name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp,
                        malus_pp, threshold)
VALUES ('Hunt 1', 'ERA_2', null, false, 'HUNT', 1, 0, 2, 0, 0),
       ('Sustenance 1', 'ERA_2', null, false, 'SUSTENANCE', 0, 1, 0, 2, 0),
       ('Ritual Event', 'ERA_2', null, false, 'SHAMANIC_RITUAL', 0, 0, 10, 5, 0),
       ('Rock Paintings 1', 'ERA_2', null, false, 'ROCK_PAINTINGS', 0, 0, 2, 2, 2);

-- Event cards Era 3
INSERT INTO event_card (name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp,
                        malus_pp, threshold)
VALUES ('Hunt 1', 'ERA_3', null, false, 'HUNT', 1, 0, 3, 0, 0),
       ('Rock Paintings 1', 'ERA_3', null, false, 'ROCK_PAINTINGS', 0, 0, 3, 2, 3),
       ('Sustenance 1', 'ERA_3', null, false, 'SUSTENANCE', 0, 1, 0, 3, 0),
       ('Ritual Event', 'ERA_3', null, true, 'SHAMANIC_RITUAL', 0, 0, 15, 7, 0);

-- Building cards Era 1
INSERT INTO building_card (name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params)
VALUES ('', 'ERA_1', null, 4, 3, 'TRIGGERCARDSSETBUILDINGEFFECT', '{}'),
       ('', 'ERA_1', null, 4, 4, 'SUSTENANCEDISCOUNTBUILDINGEFFECT', '{"characterType": "GATHERER"}'),
       ('', 'ERA_1', null, 5, 3, 'SUSTENANCEDISCOUNTBUILDINGEFFECT', '{"characterType": "ARTIST"}'),
       ('', 'ERA_1', null, 5, 2, 'SHAMANICIMMUNITYBUILDINGEFFECT', '{}'),
       ('', 'ERA_1', null, 3, 3, 'TOTEMBONUSBUILDINGEFFECT', '{}'),
       ('', 'ERA_1', null, 3, 4, 'TRIGGERINVENTORSCOUPLEBUILDINGEFFECT', '{}');

-- Building cards Era 2
INSERT INTO building_card (name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params)
VALUES ('', 'ERA_2', null, 7, 0, 'SHAMANICDOUBLEBUILDINGEFFECT', '{}'),
       ('', 'ERA_2', null, 6, 4, 'SHAMANICSTARSBUILDINGEFFECT', '{}'),
       ('', 'ERA_2', null, 7, 4, 'SUSTENANCEDISCOUNTBUILDINGEFFECT', '{"characterType": "INVENTOR"}'),
       ('', 'ERA_2', null, 7, 2, 'HUNTBONUSBUILDINGEFFECT', '{}'),
       ('', 'ERA_2', null, 6, 4, 'DOUBLEBUILDERBUILDINGEFFECT', '{}'),
       ('', 'ERA_2', null, 5, 6, 'ROCKPAINTINGSBONUSBUILDINGEFFECT', '{}'),
       ('', 'ERA_2', null, 5, 6, 'ENDPPCARDSSETBUILDINGEFFECT', '{}');

-- Building cards Era 3
INSERT INTO building_card (name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params)
VALUES ('', 'ERA_3', null, 8, 8, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 3, "characterType": "HUNTER"}'),
       ('', 'ERA_3', null, 7, 6, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "GATHERER"}'),
       ('', 'ERA_3', null, 7, 4, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "SHAMAN"}'),
       ('', 'ERA_3', null, 6, 3, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "BUILDER"}'),
       ('', 'ERA_3', null, 7, 4, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "ARTIST"}'),
       ('', 'ERA_3', null, 6, 6, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 2, "characterType": "INVENTOR"}'),
       ('', 'ERA_3', null, 9, 3, 'EXTRACARDBUILDINGEFFECT', '{}'),
       ('', 'ERA_3', null, 10, 0, 'FLATPRESTIGEPOINTBUILDINGCARD', '{}');
