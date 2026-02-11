-- ==============================
-- GM Shop v2 Fix
-- Fix NPC dialogu, presun na jednu mapu, cenova politika
-- ==============================

-- 1. Fix initQuestion → -1 (jako fungujici shop NPC 812, 816, 1158)
UPDATE npc_template SET initQuestion = -1 WHERE id BETWEEN 1235 AND 1253;

-- 2. Cleanup nepotrebnych otazek/odpovedi
DELETE FROM npc_reponses_actions WHERE ID BETWEEN 7715 AND 7733;
DELETE FROM npc_questions WHERE ID BETWEEN 7609 AND 7627;

-- 3. Presun VSECH NPC na mapu 7443 (6,-19 = Astrub, vpravo od Zaapu)
DELETE FROM npcs WHERE npcid BETWEEN 1235 AND 1253;

INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
-- Equipment rada
(7443, 1235, 71, 3),    -- Hatky
(7443, 1236, 85, 3),    -- Cape
(7443, 1237, 99, 3),    -- Amulety
(7443, 1238, 113, 3),   -- Prsteny
(7443, 1239, 141, 3),   -- Pasky
(7443, 1240, 155, 3),   -- Boty
(7443, 1241, 169, 3),   -- Stity
-- Zbrane rada
(7443, 1242, 197, 3),   -- Mece
(7443, 1243, 211, 3),   -- Dyky
(7443, 1244, 225, 3),   -- Luky
(7443, 1245, 239, 3),   -- Hulky
(7443, 1246, 253, 3),   -- Hole
(7443, 1247, 267, 3),   -- Kladiva
(7443, 1248, 281, 3),   -- Lopaty
(7443, 1249, 295, 3),   -- Sekery
-- Ostatni
(7443, 1250, 351, 3),   -- Peti
(7443, 1251, 365, 3),   -- Dofusy
(7443, 1252, 379, 3),   -- Potiony
(7443, 1253, 393, 3);   -- Ressource

-- 4. Cenova politika
-- Equipment a zbrane: level² × 200
UPDATE item_template SET prix = level * level * 200
WHERE type IN (1,2,3,4,5,6,7,8,9,10,11,16,17,19,82) AND level > 0;

-- Potiony a ressource: level² × 50
UPDATE item_template SET prix = level * level * 50
WHERE type IN (12,15) AND level > 0;

-- Peti: fixni 5M
UPDATE item_template SET prix = 5000000 WHERE type = 18;

-- Dofusy: fixni 50M
UPDATE item_template SET prix = 50000000 WHERE type = 23;

-- 5. Verifikace
SELECT '=== NPC Templates ===' as info;
SELECT id, initQuestion FROM npc_template WHERE id BETWEEN 1235 AND 1253;

SELECT '=== NPC Spawns ===' as info;
SELECT mapid, npcid, cellid FROM npcs WHERE npcid BETWEEN 1235 AND 1253 ORDER BY npcid;

SELECT '=== Vzorkove ceny (equipment) ===' as info;
SELECT name, level, type, prix FROM item_template
WHERE level IN (20,40,60,80,100,120,150,200) AND type = 6
ORDER BY level LIMIT 10;

SELECT '=== Vzorkove ceny (potiony) ===' as info;
SELECT name, level, type, prix FROM item_template
WHERE type = 12 AND level > 0 ORDER BY level LIMIT 5;
