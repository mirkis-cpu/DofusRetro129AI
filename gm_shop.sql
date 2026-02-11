-- =========================================
-- GM Shop NPC System for DofusEmu (CyonEmu 2.9)
-- 19 NPC obchodniku na mape Astrub centrum (7453)
-- Kazdy prodava vsechny itemy jedne kategorie
-- =========================================

-- KRITICKE: Zvysit limit GROUP_CONCAT (default 1024 je malo pro 500+ itemu)
SET SESSION group_concat_max_len = 65535;

-- =========================================
-- SEKCE 1: NPC Templates (19 shop vendoru)
-- =========================================

-- 1. Hatky / Coiffe (type 16)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1235, 0, 30, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7609,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 16;

-- 2. Cape (type 17)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1236, 0, 91, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7610,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 17;

-- 3. Amulety (type 1)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1237, 0, 51, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7611,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 1;

-- 4. Prsteny / Ring (type 9)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1238, 0, 70, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7612,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 9;

-- 5. Pasky / Belt (type 10)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1239, 0, 40, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7613,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 10;

-- 6. Boty / Boots (type 11)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1240, 0, 61, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7614,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 11;

-- 7. Stity / Shield (type 82)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1241, 0, 80, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7615,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 82;

-- 8. Mece / Sword (type 6)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1242, 0, 81, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7616,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 6;

-- 9. Dyky / Dagger (type 5)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1243, 0, 41, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7617,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 5;

-- 10. Luky / Bow (type 2)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1244, 0, 90, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7618,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 2;

-- 11. Hulky / Wand (type 3)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1245, 0, 50, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7619,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 3;

-- 12. Hole / Staff (type 4)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1246, 0, 100, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7620,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 4;

-- 13. Kladiva / Hammer (type 7)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1247, 0, 10, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7621,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 7;

-- 14. Lopaty / Shovel (type 8)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1248, 0, 31, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7622,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 8;

-- 15. Sekery / Axe (type 19)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1249, 0, 110, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7623,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 19;

-- 16. Peti / Pet (type 18)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1250, 0, 20, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, 7624,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 18;

-- 17. Dofusy (type 23)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1251, 0, 121, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7625,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 23;

-- 18. Potiony (type 12)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1252, 0, 71, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7626,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 12;

-- 19. Ressource (type 15)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork,
    initQuestion, ventes)
SELECT 1253, 0, 101, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, 7627,
    IFNULL(GROUP_CONCAT(id ORDER BY level, id SEPARATOR ','), '')
FROM item_template WHERE type = 15;

-- =========================================
-- SEKCE 2: NPC Questions (19 zaznamu)
-- =========================================
INSERT INTO npc_questions (ID, responses, params, cond, ifFalse) VALUES
(7609, '7715', '', '', 0),
(7610, '7716', '', '', 0),
(7611, '7717', '', '', 0),
(7612, '7718', '', '', 0),
(7613, '7719', '', '', 0),
(7614, '7720', '', '', 0),
(7615, '7721', '', '', 0),
(7616, '7722', '', '', 0),
(7617, '7723', '', '', 0),
(7618, '7724', '', '', 0),
(7619, '7725', '', '', 0),
(7620, '7726', '', '', 0),
(7621, '7727', '', '', 0),
(7622, '7728', '', '', 0),
(7623, '7729', '', '', 0),
(7624, '7730', '', '', 0),
(7625, '7731', '', '', 0),
(7626, '7732', '', '', 0),
(7627, '7733', '', '', 0);

-- =========================================
-- SEKCE 3: NPC Response Actions (19 zaznamu - vsechny zaviraji dialog)
-- type=1, args='DV' = zavre dialog (Action.java:131-134)
-- =========================================
INSERT INTO npc_reponses_actions (ID, type, args) VALUES
(7715, 1, 'DV'),
(7716, 1, 'DV'),
(7717, 1, 'DV'),
(7718, 1, 'DV'),
(7719, 1, 'DV'),
(7720, 1, 'DV'),
(7721, 1, 'DV'),
(7722, 1, 'DV'),
(7723, 1, 'DV'),
(7724, 1, 'DV'),
(7725, 1, 'DV'),
(7726, 1, 'DV'),
(7727, 1, 'DV'),
(7728, 1, 'DV'),
(7729, 1, 'DV'),
(7730, 1, 'DV'),
(7731, 1, 'DV'),
(7732, 1, 'DV'),
(7733, 1, 'DV');

-- =========================================
-- SEKCE 4: NPC Spawn na mape 7453 (Astrub centrum)
-- orientation=3 = smer jih (k hraci)
-- Existujici NPC: ID 780 na cell 187
-- =========================================
INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
-- Equipment rada
(7453, 1235, 100, 3),   -- Hatky
(7453, 1236, 114, 3),   -- Cape
(7453, 1237, 128, 3),   -- Amulety
(7453, 1238, 142, 3),   -- Prsteny
(7453, 1239, 156, 3),   -- Pasky
(7453, 1240, 170, 3),   -- Boty
(7453, 1241, 199, 3),   -- Stity
-- Zbrane rada
(7453, 1242, 215, 3),   -- Mece
(7453, 1243, 229, 3),   -- Dyky
(7453, 1244, 243, 3),   -- Luky
(7453, 1245, 257, 3),   -- Hulky
(7453, 1246, 271, 3),   -- Hole
(7453, 1247, 285, 3),   -- Kladiva
(7453, 1248, 299, 3),   -- Lopaty
(7453, 1249, 313, 3),   -- Sekery
-- Ostatni
(7453, 1250, 370, 3),   -- Peti
(7453, 1251, 384, 3),   -- Dofusy
(7453, 1252, 398, 3),   -- Potiony
(7453, 1253, 412, 3);   -- Ressource

-- =========================================
-- SEKCE 5: Verifikace
-- =========================================
SELECT id, gfxID, initQuestion,
    LENGTH(ventes) AS ventes_bytes,
    CASE WHEN ventes = '' THEN 0
         ELSE (LENGTH(ventes) - LENGTH(REPLACE(ventes, ',', '')) + 1)
    END AS item_count
FROM npc_template
WHERE id BETWEEN 1235 AND 1253
ORDER BY id;
