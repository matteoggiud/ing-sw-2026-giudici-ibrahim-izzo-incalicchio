
-- Character cards Era 1
INSERT INTO character_card (name, era, min_num_players, character_type, effect_params)
VALUES
    --Hunters
    ('Hunter I', 'ERA_1', 2, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter I', 'ERA_1', 2, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter I', 'ERA_1', 2, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter I', 'ERA_1', 3, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter I', 'ERA_1', 3, 'HUNTER', '{"isFoodIcon": false}'),

-- Builders
    ('Builder I', 'ERA_1', 2, 'BUILDER', '{"endGamePP": 3, "foodDiscount": 1}'),
    ('Builder I', 'ERA_1', 2, 'BUILDER', '{"endGamePP": 0, "foodDiscount": 2}'),
    ('Builder I', 'ERA_1', 5, 'BUILDER', '{"endGamePP": 1, "foodDiscount": 2}'),
    ('Builder I', 'ERA_1', 2, 'BUILDER', '{"endGamePP": 2, "foodDiscount": 1}'),

-- Gatherers
    ('Gatherer I', 'ERA_1', 2, 'GATHERER', '{}'),
    ('Gatherer I', 'ERA_1', 2, 'GATHERER', '{}'),
    ('Gatherer I', 'ERA_1', 3, 'GATHERER', '{}'),
    ('Gatherer I', 'ERA_1', 5, 'GATHERER', '{}'),

-- Artists
    ('Artist I', 'ERA_1', 2, 'ARTIST', '{}'),
    ('Artist I', 'ERA_1', 2, 'ARTIST', '{}'),
    ('Artist I', 'ERA_1', 2, 'ARTIST', '{}'),
    ('Artist I', 'ERA_1', 3, 'ARTIST', '{}'),
    ('Artist I', 'ERA_1', 4, 'ARTIST', '{}'),

-- Inventors
    ('Inventor I', 'ERA_1', 2, 'INVENTOR', '{"inventorType": "LEATHER"}'),
    ('Inventor I', 'ERA_1', 2, 'INVENTOR', '{"inventorType": "BOAT"}'),
    ('Inventor I', 'ERA_1', 2, 'INVENTOR', '{"inventorType": "ARROW"}'),
    ('Inventor I', 'ERA_1', 2, 'INVENTOR', '{"inventorType": "BREAD"}'),
    ('Inventor I', 'ERA_1', 4, 'INVENTOR', '{"inventorType": "ROPE"}'),
    ('Inventor I', 'ERA_1', 4, 'INVENTOR', '{"inventorType": "FLUTE"}'),
    ('Inventor I', 'ERA_1', 4, 'INVENTOR', '{"inventorType": "SOUP"}'),

-- Shamans
    ('Shaman I', 'ERA_1', 5, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman I', 'ERA_1', 2, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman I', 'ERA_1', 2, 'SHAMAN', '{"shamanStars": 1}'),
    ('Shaman I', 'ERA_1', 4, 'SHAMAN', '{"shamanStars": 1}');


-- Character cards Era 2
INSERT INTO character_card (name, era, min_num_players, character_type, effect_params)
VALUES
    --Hunters
    ('Hunter II', 'ERA_2', 2, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter II', 'ERA_2', 2, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter II', 'ERA_2', 3, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter II', 'ERA_2', 2, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter II', 'ERA_2', 4, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter II', 'ERA_2', 5, 'HUNTER', '{"isFoodIcon": false}'),

    -- Builders
    ('Builder II', 'ERA_2', 2, 'BUILDER', '{"endGamePP": 4, "foodDiscount": 1}'),
    ('Builder II', 'ERA_2', 2, 'BUILDER', '{"endGamePP": 1, "foodDiscount": 2}'),
    ('Builder II', 'ERA_2', 3, 'BUILDER', '{"endGamePP": 2, "foodDiscount": 1}'),
    ('Builder II', 'ERA_2', 2, 'BUILDER', '{"endGamePP": 3, "foodDiscount": 2}'),

    -- Gatherers
    ('Gatherer II', 'ERA_2', 2, 'GATHERER', '{}'),
    ('Gatherer II', 'ERA_2', 3, 'GATHERER', '{}'),
    ('Gatherer II', 'ERA_2', 4, 'GATHERER', '{}'),
    ('Gatherer II', 'ERA_2', 5, 'GATHERER', '{}'),

    -- Artists
    ('Artist II', 'ERA_2', 3, 'ARTIST', '{}'),
    ('Artist II', 'ERA_2', 2, 'ARTIST', '{}'),
    ('Artist II', 'ERA_2', 2, 'ARTIST', '{}'),
    ('Artist II', 'ERA_2', 2, 'ARTIST', '{}'),

-- Inventors
    ('Inventor II', 'ERA_2', 2, 'INVENTOR', '{"inventorType": "TOTEM"}'),
    ('Inventor II', 'ERA_2', 4, 'INVENTOR', '{"inventorType": "HOOK"}'),
    ('Inventor II', 'ERA_2', 2, 'INVENTOR', '{"inventorType": "ROPE"}'),
    ('Inventor II', 'ERA_2', 2, 'INVENTOR', '{"inventorType": "FLUTE"}'),
    ('Inventor II', 'ERA_2', 2, 'INVENTOR', '{"inventorType": "SOUP"}'),
    ('Inventor II', 'ERA_2', 2, 'INVENTOR', '{"inventorType": "LEATHER"}'),

-- Shamans
    ('Shaman II', 'ERA_2', 2, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman II', 'ERA_2', 2, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman II', 'ERA_2', 5, 'SHAMAN', '{"shamanStars": 1}'),
    ('Shaman II', 'ERA_2', 5, 'SHAMAN', '{"shamanStars": 2}');


-- Character cards Era 3
INSERT INTO character_card (name, era, min_num_players, character_type, effect_params)
VALUES
    --Hunters
    ('Hunter III', 'ERA_3', 5, 'HUNTER', '{"isFoodIcon": true}'),
    ('Hunter III', 'ERA_3', 2, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter III', 'ERA_3', 2, 'HUNTER', '{"isFoodIcon": false}'),
    ('Hunter III', 'ERA_3', 2, 'HUNTER', '{"isFoodIcon": true}'),

    -- Builders
    ('Builder III', 'ERA_3', 2, 'BUILDER', '{"endGamePP": 5, "foodDiscount": 1}'),
    ('Builder III', 'ERA_3', 2, 'BUILDER', '{"endGamePP": 3, "foodDiscount": 2}'),
    ('Builder III', 'ERA_3', 5, 'BUILDER', '{"endGamePP": 4, "foodDiscount": 1}'),
    ('Builder III', 'ERA_3', 2, 'BUILDER', '{"endGamePP": 2, "foodDiscount": 2}'),

    -- Gatherers
    ('Gatherer III', 'ERA_3', 5, 'GATHERER', '{}'),
    ('Gatherer III', 'ERA_3', 4, 'GATHERER', '{}'),
    ('Gatherer III', 'ERA_3', 2, 'GATHERER', '{}'),

    -- Artists
    ('Artist III', 'ERA_3', 5, 'ARTIST', '{}'),
    ('Artist III', 'ERA_3', 2, 'ARTIST', '{}'),
    ('Artist III', 'ERA_3', 2, 'ARTIST', '{}'),
    ('Artist III', 'ERA_3', 2, 'ARTIST', '{}'),

-- Inventors
    ('Inventor III', 'ERA_3', 4, 'INVENTOR', '{"inventorType": "CHAIN"}'),
    ('Inventor III', 'ERA_3', 3, 'INVENTOR', '{"inventorType": "BOAT"}'),
    ('Inventor III', 'ERA_3', 3, 'INVENTOR', '{"inventorType": "ARROW"}'),
    ('Inventor III', 'ERA_3', 2, 'INVENTOR', '{"inventorType": "TOTEM"}'),
    ('Inventor III', 'ERA_3', 2, 'INVENTOR', '{"inventorType": "HOOK"}'),
    ('Inventor III', 'ERA_3', 2, 'INVENTOR', '{"inventorType": "CHAIN"}'),
    ('Inventor III', 'ERA_3', 2, 'INVENTOR', '{"inventorType": "BREAD"}'),
-- Shamans
    ('Shaman III', 'ERA_3', 3, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman III', 'ERA_3', 2, 'SHAMAN', '{"shamanStars": 3}'),
    ('Shaman III', 'ERA_3', 2, 'SHAMAN', '{"shamanStars": 2}'),
    ('Shaman III', 'ERA_3', 2, 'SHAMAN', '{"shamanStars": 3}'),
    ('Shaman III', 'ERA_3', 4, 'SHAMAN', '{"shamanStars": 2}');



-- Event cards Era 1
INSERT INTO event_card (name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp,
                        malus_pp, threshold)
VALUES ('Hunt I', 'ERA_1', 2, false, 'HUNT', 1, 0, 1, 0, 0),
       ('Sustenance I', 'ERA_1', 2, false, 'SUSTENANCE', 0, 1, 0, 1, 0),
       ('Ritual Event I', 'ERA_1', 2, false, 'SHAMANIC_RITUAL', 0, 0, 5, 3, 0),
       ('Rock Paintings I', 'ERA_1', 2, false, 'ROCK_PAINTINGS', 0, 0, 1, 2, 1);

-- Event cards Era 2
INSERT INTO event_card (name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp,
                        malus_pp, threshold)
VALUES ('Hunt II', 'ERA_2', 2, false, 'HUNT', 1, 0, 2, 0, 0),
       ('Sustenance II', 'ERA_2', 2, false, 'SUSTENANCE', 0, 1, 0, 2, 0),
       ('Ritual Event II', 'ERA_2', 2, false, 'SHAMANIC_RITUAL', 0, 0, 10, 5, 0),
       ('Rock Paintings II', 'ERA_2', 2, false, 'ROCK_PAINTINGS', 0, 0, 2, 2, 2);

-- Event cards Era 3
INSERT INTO event_card (name, era, min_num_players, is_final_event, event_type, gained_food, malus_food, gained_pp,
                        malus_pp, threshold)
VALUES ('Hunt III', 'ERA_3', 2, false, 'HUNT', 1, 0, 3, 0, 0),
       ('Rock Paintings III', 'ERA_3', 2, false, 'ROCK_PAINTINGS', 0, 0, 3, 2, 3),
       ('Sustenance III', 'ERA_3', 2, true, 'SUSTENANCE', 0, 1, 0, 3, 0),
       ('Ritual Event III', 'ERA_3', 2, true, 'SHAMANIC_RITUAL', 0, 0, 15, 7, 0);

-- Building cards Era 1
INSERT INTO building_card (name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params)
VALUES ('Set Monument I', 'ERA_1', 2, 4, 3, 'TRIGGERCARDSSETBUILDINGEFFECT', '{}'),
       ('Gatherer Pantry I', 'ERA_1', 2, 4, 4, 'SUSTENANCEDISCOUNTBUILDINGEFFECT', '{"characterType": "GATHERER"}'),
       ('Artists Pantry I', 'ERA_1', 2, 5, 3, 'SUSTENANCEDISCOUNTBUILDINGEFFECT', '{"characterType": "ARTIST"}'),
       ('Shaman Immunity Hut I', 'ERA_1', 2, 5, 2, 'SHAMANICIMMUNITYBUILDINGEFFECT', '{}'),
       ('Totem Shrine I', 'ERA_1', 2, 3, 3, 'TOTEMBONUSBUILDINGEFFECT', '{}'),
       ('Inventors Pair Lodge I', 'ERA_1', 2, 3, 4, 'TRIGGERINVENTORSCOUPLEBUILDINGEFFECT', '{}');

-- Building cards Era 2
INSERT INTO building_card (name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params)
VALUES ('Shaman Double Temple II', 'ERA_2', 2, 7, 0, 'SHAMANICDOUBLEBUILDINGEFFECT', '{}'),
       ('Shaman Stars Temple II', 'ERA_2', 2, 6, 4, 'SHAMANICSTARSBUILDINGEFFECT', '{}'),
       ('Inventor Pantry II', 'ERA_2', 2, 7, 4, 'SUSTENANCEDISCOUNTBUILDINGEFFECT', '{"characterType": "INVENTOR"}'),
       ('Hunt Camp II', 'ERA_2', 2, 7, 2, 'HUNTBONUSBUILDINGEFFECT', '{}'),
       ('Master Builders Hall II', 'ERA_2', 2, 6, 4, 'DOUBLEBUILDERBUILDINGEFFECT', '{}'),
       ('Rock Gallery II', 'ERA_2', 2, 5, 6, 'ROCKPAINTINGSBONUSBUILDINGEFFECT', '{}'),
       ('Set Monument II', 'ERA_2', 2, 5, 6, 'ENDPPCARDSSETBUILDINGEFFECT', '{}');

-- Building cards Era 3
INSERT INTO building_card (name, era, min_num_players, food_cost, end_game_pp, effect_id, effect_params)
VALUES ('Hunters Hall III', 'ERA_3', 2, 8, 8, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 3, "characterType": "HUNTER"}'),
       ('Gatherers Hall III', 'ERA_3', 2, 7, 6, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "GATHERER"}'),
       ('Shamans Hall III', 'ERA_3', 2, 7, 4, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "SHAMAN"}'),
       ('Builders Hall III', 'ERA_3', 2, 6, 3, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "BUILDER"}'),
       ('Artists Hall III', 'ERA_3', 2, 7, 4, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 4, "characterType": "ARTIST"}'),
       ('Inventors Hall III', 'ERA_3', 2, 6, 6, 'PPPERCHARACTERBUILDINGEFFECT', '{"pp": 2, "characterType": "INVENTOR"}'),
       ('Extra Draw Pavilion III', 'ERA_3', 2, 9, 3, 'EXTRACARDBUILDINGEFFECT', '{}'),
       ('Prestige Palace III', 'ERA_3', 2, 10, 0, 'FLATPRESTIGEPOINTBUILDINGCARD', '{}');

INSERT INTO card (character_card_id, image_key)
SELECT c.card_id, 'CHARACTER_' || c.card_id
FROM character_card c ON CONFLICT (character_card_id) DO NOTHING;

INSERT INTO card (building_card_id, image_key)
SELECT b.card_id, 'BUILDING_' || b.card_id
FROM building_card b ON CONFLICT (building_card_id) DO NOTHING;

INSERT INTO card (event_card_id, image_key)
SELECT e.card_id, 'EVENT_' || e.card_id
FROM event_card e ON CONFLICT (event_card_id) DO NOTHING;

