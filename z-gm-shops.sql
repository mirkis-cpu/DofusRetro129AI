-- =========================================
-- GM Shop NPCs - Map 7443 (6,-19 Astrub)
-- 33 NPC obchodniku - primy shop (initQuestion=-1, ventes naplneny)
-- Kazdy NPC prodava vsechny itemy dane kategorie
-- =========================================

SET SESSION group_concat_max_len = 100000;

-- =========================================
-- CLEANUP (idempotent)
-- =========================================
DELETE FROM npcs WHERE npcid BETWEEN 2000 AND 2032;
DELETE FROM npc_reponses_actions WHERE ID BETWEEN 8000 AND 8032;
DELETE FROM npc_questions WHERE ID BETWEEN 8000 AND 8032;
DELETE FROM npc_template WHERE id BETWEEN 2000 AND 2032;

-- =========================================
-- NPC Templates - initQuestion=-1 = primy shop bez dialogu
-- ventes se naplni dynamicky z item_template
-- =========================================

-- Equipment row 1 (sex=0)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex, color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes) VALUES
(2000, 0, 1205, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2001, 0, 70, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2002, 0, 30, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2003, 0, 9008, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2004, 0, 1211, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2005, 0, 9033, 100, 100, 0, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
-- Equipment row 2 (sex=1)
(2006, 0, 1205, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2007, 0, 70, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2008, 0, 30, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2009, 0, 9008, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2010, 0, 1211, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2011, 0, 9033, 100, 100, 1, -1, -1, -1, '0,0,0,0', -1, 0, -1, ''),
-- Weapons row 3 (color1=6513587)
(2012, 0, 1205, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2013, 0, 70, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2014, 0, 30, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2015, 0, 9008, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2016, 0, 1211, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2017, 0, 9033, 100, 100, 0, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
-- Consumables row 4
(2018, 0, 1205, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2019, 0, 70, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2020, 0, 30, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2021, 0, 9008, 100, 100, 1, 6513587, -1, -1, '0,0,0,0', -1, 0, -1, ''),
-- Resources row 5
(2022, 0, 50, 100, 100, 0, 16771366, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2023, 0, 50, 100, 100, 1, 16771366, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2024, 0, 50, 100, 100, 0, 13749677, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2025, 0, 50, 100, 100, 1, 13749677, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2026, 0, 50, 100, 100, 0, 8894508, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2027, 0, 50, 100, 100, 1, 8894508, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2028, 0, 50, 100, 100, 0, 8553090, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2029, 0, 50, 100, 100, 1, 8553090, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2030, 0, 50, 100, 100, 0, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2031, 0, 50, 100, 100, 1, 15859712, -1, -1, '0,0,0,0', -1, 0, -1, ''),
(2032, 0, 50, 100, 100, 0, -1, 15859712, -1, '0,0,0,0', -1, 0, -1, '');

-- =========================================
-- Fill ventes dynamically from item_template
-- NPC ID -> item_template.type mapping:
-- 2000=Amulet(2), 2001=Boots(11), 2002=Dofus(13), 2003=Shield(14)
-- 2004=Ring(15), 2005=Cape(28), 2006=Backpack(36), 2007=Sword(16)
-- 2008=Axe(17), 2009=Shovel(18), 2010=Bow(19), 2011=Wand(20)
-- 2012=Dagger(25), 2013=Staff(26), 2014=Hammer(24), 2015=Scythe(27)
-- 2016=Pet(31), 2017=Soul Stone(41), 2018=Potion(56), 2019=Mount(58)
-- 2020=Bread(60), 2021=Meat(65)
-- 2022=Cereal(43), 2023=Flower(44), 2024=Skin(45), 2025=Fabric(46)
-- 2026=Ore(47), 2027=Alloy(48), 2028=Plank(49), 2029=Gem(50)
-- 2030=Fish Raw(62), 2031=Fish Cooked(63), 2032=Preserved(64)
-- =========================================
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=2) WHERE id=2000;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=11) WHERE id=2001;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=13) WHERE id=2002;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=14) WHERE id=2003;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=15) WHERE id=2004;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=28) WHERE id=2005;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=36) WHERE id=2006;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=16) WHERE id=2007;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=17) WHERE id=2008;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=18) WHERE id=2009;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=19) WHERE id=2010;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=20) WHERE id=2011;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=25) WHERE id=2012;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=26) WHERE id=2013;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=24) WHERE id=2014;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=27) WHERE id=2015;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=31) WHERE id=2016;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=41) WHERE id=2017;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=56) WHERE id=2018;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=58) WHERE id=2019;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=60) WHERE id=2020;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=65) WHERE id=2021;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=43) WHERE id=2022;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=44) WHERE id=2023;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=45) WHERE id=2024;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=46) WHERE id=2025;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=47) WHERE id=2026;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=48) WHERE id=2027;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=49) WHERE id=2028;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=50) WHERE id=2029;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=62) WHERE id=2030;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=63) WHERE id=2031;
UPDATE npc_template SET ventes=(SELECT GROUP_CONCAT(id ORDER BY id) FROM item_template WHERE type=64) WHERE id=2032;

-- =========================================
-- NPC Spawns on map 7443 (6,-19 Astrub - GM Shop from MOTD)
-- =========================================
INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
(7443, 2000, 349, 3),   -- Amulet
(7443, 2001, 351, 3),   -- Boots
(7443, 2002, 353, 3),   -- Dofus
(7443, 2003, 355, 3),   -- Shield
(7443, 2004, 357, 3),   -- Ring
(7443, 2005, 359, 3),   -- Cape/Cloak
(7443, 2006, 361, 3),   -- Backpack
(7443, 2007, 378, 3),   -- Sword
(7443, 2008, 380, 3),   -- Axe
(7443, 2009, 382, 3),   -- Shovel
(7443, 2010, 384, 3),   -- Bow
(7443, 2011, 386, 3),   -- Wand
(7443, 2012, 388, 3),   -- Dagger
(7443, 2013, 390, 3),   -- Staff
(7443, 2014, 407, 3),   -- Hammer
(7443, 2015, 409, 3),   -- Scythe
(7443, 2016, 411, 3),   -- Pet
(7443, 2017, 413, 3),   -- Soul Stone
(7443, 2018, 415, 3),   -- Potion
(7443, 2019, 417, 3),   -- Mount
(7443, 2020, 419, 3),   -- Bread
(7443, 2021, 436, 3),   -- Meat
(7443, 2022, 438, 3),   -- Resource: Cereal
(7443, 2023, 440, 3),   -- Resource: Flower
(7443, 2024, 442, 3),   -- Resource: Skin
(7443, 2025, 444, 3),   -- Resource: Fabric
(7443, 2026, 446, 3),   -- Resource: Ore
(7443, 2027, 448, 3),   -- Resource: Alloy
(7443, 2028, 465, 3),   -- Resource: Plank
(7443, 2029, 467, 3),   -- Resource: Precious Stone
(7443, 2030, 469, 3),   -- Resource: Fish (Raw)
(7443, 2031, 471, 3),   -- Resource: Fish (Cooked)
(7443, 2032, 473, 3);   -- Resource: Preserved

-- =========================================
-- Verification
-- =========================================
SELECT '=== GM Shop NPCs ===' AS info;
SELECT id, initQuestion,
  (LENGTH(ventes) - LENGTH(REPLACE(ventes,',','')) + 1) AS item_count
FROM npc_template WHERE id BETWEEN 2000 AND 2032 ORDER BY id;
SELECT COUNT(*) AS npc_spawns FROM npcs WHERE npcid BETWEEN 2000 AND 2032;
