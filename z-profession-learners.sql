-- =========================================
-- Profession Learner NPCs - Map 7411 (4,-19 Astrub Zaap)
-- 22 basic professions + 11 FM specializations = 33 NPCs
-- Click NPC → learn profession directly (bonusValue = job ID)
-- No dialog needed - server handles via bonusValue check
-- =========================================

-- =========================================
-- CLEANUP (idempotent)
-- =========================================
DELETE FROM npcs WHERE npcid BETWEEN 2100 AND 2132;
DELETE FROM npc_reponses_actions WHERE ID BETWEEN 8100 AND 8132;
DELETE FROM npc_questions WHERE ID BETWEEN 8100 AND 8132;
DELETE FROM npc_template WHERE id BETWEEN 2100 AND 2132;

-- =========================================
-- NPC Templates - profession learners
-- initQuestion = -1 (no dialog)
-- bonusValue = job ID (server uses this to teach profession)
-- Various gfx to distinguish them visually
-- =========================================
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex, color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes) VALUES
-- Basic professions (22) - gathering/crafting
(2100, 2,  51,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Bucheron (Lumberjack)
(2101, 11, 80,  100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Cordonnier (Shoemaker)
(2102, 13, 91,  100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Bijoutier (Jeweller)
(2103, 14, 30,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Sculpteur (Carver)
(2104, 15, 10,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgeur Epees (Swordsmith)
(2105, 16, 40,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgeur Marteaux (Hammersmith)
(2106, 17, 61,  100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgeur Haches (Axesmith)
(2107, 18, 70,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgeur Pelles (Shovelsmith)
(2108, 19, 100, 100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgeur Dagues (Daggersmith)
(2109, 20, 110, 100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Sculpteur Arcs (Bowcarver)
(2110, 24, 121, 100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Mineur (Miner)
(2111, 25, 101, 100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Boulanger (Baker)
(2112, 26, 50,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Alchimiste (Alchemist)
(2113, 27, 20,  100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Tailleur (Tailor)
(2114, 28, 11,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Paysan (Farmer)
(2115, 31, 41,  100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Pecheur (Fisherman)
(2116, 36, 31,  100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Chasseur (Hunter)
(2117, 41, 9008,100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Boucher (Butcher)
(2118, 56, 9033,100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Poissonnier (Fish Monger)
(2119, 58, 1205,100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Bricoleur (Handyman)
(2120, 60, 1211,100, 100, 1, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Sculpteur Batons (Wandcarver)
(2121, 43, 9045,100, 100, 0, 3355443, -1, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgeur Boucliers (Shieldsmith)
-- FM Specializations (11) - color2 = orange to distinguish
(2122, 44, 10,  100, 100, 1, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Cordomagus (FM Shoemaker)
(2123, 45, 91,  100, 100, 0, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Joaillomage (FM Jeweller)
(2124, 46, 20,  100, 100, 0, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Costumage (FM Tailor)
(2125, 47, 40,  100, 100, 1, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgemage Epees (FM Swords)
(2126, 48, 100, 100, 100, 1, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgemage Dagues (FM Daggers)
(2127, 49, 61,  100, 100, 0, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgemage Marteaux (FM Hammers)
(2128, 50, 70,  100, 100, 1, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgemage Pelles (FM Shovels)
(2129, 62, 30,  100, 100, 0, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgemage Haches (FM Axes)
(2130, 63, 110, 100, 100, 1, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Forgemage Arcs (FM Bows)
(2131, 64, 121, 100, 100, 0, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, ''),  -- Sculptemage Batons (FM Wands)
(2132, 65, 101, 100, 100, 1, -1, 16744448, -1, '0,0,0,0', -1, 0, -1, '');  -- Sculptemage Baguettes (FM Staffs)

-- =========================================
-- NPC Spawns on map 7411 (4,-19 Astrub Zaap)
-- Row layout: cells 120-273 area (upper part of map near zaap)
-- =========================================
INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
-- Basic professions - rows near zaap
(7411, 2100, 120, 3),   -- Bucheron
(7411, 2101, 122, 3),   -- Cordonnier
(7411, 2102, 124, 3),   -- Bijoutier
(7411, 2103, 126, 3),   -- Sculpteur
(7411, 2104, 128, 3),   -- Forgeur Epees
(7411, 2105, 130, 3),   -- Forgeur Marteaux
(7411, 2106, 149, 3),   -- Forgeur Haches
(7411, 2107, 151, 3),   -- Forgeur Pelles
(7411, 2108, 153, 3),   -- Forgeur Dagues
(7411, 2109, 155, 3),   -- Sculpteur Arcs
(7411, 2110, 157, 3),   -- Mineur
(7411, 2111, 159, 3),   -- Boulanger
(7411, 2112, 178, 3),   -- Alchimiste
(7411, 2113, 180, 3),   -- Tailleur
(7411, 2114, 182, 3),   -- Paysan
(7411, 2115, 184, 3),   -- Pecheur
(7411, 2116, 186, 3),   -- Chasseur
(7411, 2117, 188, 3),   -- Boucher
(7411, 2118, 207, 3),   -- Poissonnier
(7411, 2119, 209, 3),   -- Bricoleur
(7411, 2120, 211, 3),   -- Sculpteur Batons
(7411, 2121, 213, 3),   -- Forgeur Boucliers
-- FM Specializations
(7411, 2122, 236, 3),   -- Cordomagus
(7411, 2123, 238, 3),   -- Joaillomage
(7411, 2124, 240, 3),   -- Costumage
(7411, 2125, 242, 3),   -- Forgemage Epees
(7411, 2126, 244, 3),   -- Forgemage Dagues
(7411, 2127, 246, 3),   -- Forgemage Marteaux
(7411, 2128, 265, 3),   -- Forgemage Pelles
(7411, 2129, 267, 3),   -- Forgemage Haches
(7411, 2130, 269, 3),   -- Forgemage Arcs
(7411, 2131, 271, 3),   -- Sculptemage Batons
(7411, 2132, 273, 3);   -- Sculptemage Baguettes

-- =========================================
-- Verification
-- =========================================
SELECT '=== Profession Learners ===' AS info;
SELECT COUNT(*) AS templates FROM npc_template WHERE id BETWEEN 2100 AND 2132;
SELECT COUNT(*) AS spawns FROM npcs WHERE npcid BETWEEN 2100 AND 2132;
