-- =========================================
-- GM Shop NPCs - Two Maps
-- Map 7427 (5,-19): Equipment, Weapons, Dofus, Pets
-- Map 7443 (6,-19): Resources & Consumables
-- 35 NPC - primy shop (initQuestion=-1)
-- =========================================

SET SESSION group_concat_max_len = 100000;

-- =========================================
-- CLEANUP (idempotent)
-- =========================================
DELETE FROM npcs WHERE npcid BETWEEN 2000 AND 2034;
DELETE FROM npc_reponses_actions WHERE ID BETWEEN 8000 AND 8034;
DELETE FROM npc_questions WHERE ID BETWEEN 8000 AND 8034;
DELETE FROM npc_template WHERE id BETWEEN 2000 AND 2034;

-- =========================================
-- NPC Templates - initQuestion=-1 = primy shop bez dialogu
-- =========================================

-- === MAP 7427 (5,-19) — Equipment & Weapons ===

-- Row 1: Equipment (varied gfx, sex=0)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex, color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes) VALUES
(2000, 0, 1205, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2001, 0, 70, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2002, 0, 30, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2003, 0, 9008, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2004, 0, 1211, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2005, 0, 9033, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2006, 0, 1205, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2007, 0, 70, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),

-- Row 2: Weapons (color1=6513587 green tint)
(2008, 0, 1205, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2009, 0, 70, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2010, 0, 30, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2011, 0, 9008, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2012, 0, 1211, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2013, 0, 9033, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2014, 0, 1205, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2015, 0, 70, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2016, 0, 30, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),

-- Row 3: Special (Dofus, Pet, Mount)
(2017, 0, 9008, 100, 100, 0, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2018, 0, 1211, 100, 100, 0, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2019, 0, 9033, 100, 100, 1, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),

-- === MAP 7443 (6,-19) — Resources & Consumables ===

-- Row 1: Nature resources (gfx=50, yellow/green tones)
(2020, 0, 50, 100, 100, 0, 16771366, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2021, 0, 50, 100, 100, 1, 16771366, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2022, 0, 50, 100, 100, 0, 13749677, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2023, 0, 50, 100, 100, 1, 13749677, -1, -1, '0,0,0,0', -1, 0, -1, ''),

-- Row 2: Mining resources (gfx=50, grey/blue tones)
(2024, 0, 50, 100, 100, 0, 8894508, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2025, 0, 50, 100, 100, 1, 8894508, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2026, 0, 50, 100, 100, 0, 8553090, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2027, 0, 50, 100, 100, 1, 8553090, -1, -1, '0,0,0,0', -1, 0, -1, ''),

-- Row 3: Food & Other (gfx=50, red/orange tones)
(2028, 0, 50, 100, 100, 0, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2029, 0, 50, 100, 100, 1, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2030, 0, 50, 100, 100, 0, -1, 15859712, -1, '0,0,0,0', -1, 0, -1, ''),
(2031, 0, 50, 100, 100, 1, -1, 15859712, -1, '0,0,0,0', -1, 0, -1, ''),
(2032, 0, 50, 100, 100, 0, -1, -1, 15859712, '0,0,0,0', -1, 0, -1, ''),
(2033, 0, 50, 100, 100, 1, -1, -1, 15859712, '0,0,0,0', -1, 0, -1, ''),
(2034, 0, 50, 100, 100, 0, 16750848, -1, -1, '0,0,0,0', -1, 0, -1, '');

-- =========================================
-- Fill ventes dynamically from item_template
-- =========================================

-- Map 7427: Equipment
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=1) WHERE id=2000;   -- Hat
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=2) WHERE id=2001;   -- Amulet
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=10) WHERE id=2002;  -- Belt
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=11) WHERE id=2003;  -- Boots
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=15) WHERE id=2004;  -- Ring
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=28) WHERE id=2005;  -- Cape
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=14) WHERE id=2006;  -- Shield
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=36) WHERE id=2007;  -- Backpack

-- Map 7427: Weapons
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=16) WHERE id=2008;  -- Sword
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=19) WHERE id=2009;  -- Bow
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=24) WHERE id=2010;  -- Hammer
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=17) WHERE id=2011;  -- Axe
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=25) WHERE id=2012;  -- Dagger
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=26) WHERE id=2013;  -- Staff
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=20) WHERE id=2014;  -- Wand
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=27) WHERE id=2015;  -- Scythe
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=18) WHERE id=2016;  -- Shovel

-- Map 7427: Special
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=13) WHERE id=2017;  -- Dofus
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=31) WHERE id=2018;  -- Pet
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=58) WHERE id=2019;  -- Mount

-- Map 7443: Nature Resources
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=43) WHERE id=2020;  -- Cereal
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=44) WHERE id=2021;  -- Flower
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=45) WHERE id=2022;  -- Skin
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=46) WHERE id=2023;  -- Fabric

-- Map 7443: Mining Resources
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=47) WHERE id=2024;  -- Ore
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=48) WHERE id=2025;  -- Alloy
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=49) WHERE id=2026;  -- Plank
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=50) WHERE id=2027;  -- Gem

-- Map 7443: Food & Other
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=62) WHERE id=2028;  -- Fish Raw
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=63) WHERE id=2029;  -- Fish Cooked
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=64) WHERE id=2030;  -- Preserved
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=65) WHERE id=2031;  -- Meat
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=60) WHERE id=2032;  -- Bread
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=56) WHERE id=2033;  -- Potion
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY level, id) FROM item_template WHERE type=41) WHERE id=2034;  -- Soul Stone

-- =========================================
-- NPC Spawns
-- =========================================

-- Map 7427 (5,-19) — Equipment & Weapons & Special
INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
-- Row 1: Equipment
(7427, 2000, 349, 3),   -- Hat
(7427, 2001, 351, 3),   -- Amulet
(7427, 2002, 353, 3),   -- Belt
(7427, 2003, 355, 3),   -- Boots
(7427, 2004, 357, 3),   -- Ring
(7427, 2005, 359, 3),   -- Cape
(7427, 2006, 361, 3),   -- Shield
(7427, 2007, 363, 3),   -- Backpack
-- Row 2: Weapons
(7427, 2008, 378, 3),   -- Sword
(7427, 2009, 380, 3),   -- Bow
(7427, 2010, 382, 3),   -- Hammer
(7427, 2011, 384, 3),   -- Axe
(7427, 2012, 386, 3),   -- Dagger
(7427, 2013, 388, 3),   -- Staff
(7427, 2014, 390, 3),   -- Wand
(7427, 2015, 392, 3),   -- Scythe
(7427, 2016, 394, 3),   -- Shovel
-- Row 3: Special
(7427, 2017, 407, 3),   -- Dofus
(7427, 2018, 409, 3),   -- Pet
(7427, 2019, 411, 3);   -- Mount

-- Map 7443 (6,-19) — Resources & Consumables
INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
-- Row 1: Nature
(7443, 2020, 349, 3),   -- Cereal
(7443, 2021, 351, 3),   -- Flower
(7443, 2022, 353, 3),   -- Skin
(7443, 2023, 355, 3),   -- Fabric
-- Row 2: Mining
(7443, 2024, 378, 3),   -- Ore
(7443, 2025, 380, 3),   -- Alloy
(7443, 2026, 382, 3),   -- Plank
(7443, 2027, 384, 3),   -- Gem
-- Row 3: Food & Other
(7443, 2028, 407, 3),   -- Fish Raw
(7443, 2029, 409, 3),   -- Fish Cooked
(7443, 2030, 411, 3),   -- Preserved
(7443, 2031, 413, 3),   -- Meat
(7443, 2032, 415, 3),   -- Bread
(7443, 2033, 417, 3),   -- Potion
(7443, 2034, 419, 3);   -- Soul Stone

-- =========================================
-- Verification
-- =========================================
SELECT '=== GM Shop: Equipment & Weapons (Map 7427 / 5,-19) ===' AS info;
SELECT id, initQuestion,
  (LENGTH(ventes) - LENGTH(REPLACE(ventes,',','')) + 1) AS item_count
FROM npc_template WHERE id BETWEEN 2000 AND 2019 ORDER BY id;

SELECT '=== GM Shop: Resources (Map 7443 / 6,-19) ===' AS info;
SELECT id, initQuestion,
  (LENGTH(ventes) - LENGTH(REPLACE(ventes,',','')) + 1) AS item_count
FROM npc_template WHERE id BETWEEN 2020 AND 2034 ORDER BY id;

SELECT COUNT(*) AS total_npc_spawns FROM npcs WHERE npcid BETWEEN 2000 AND 2034;
