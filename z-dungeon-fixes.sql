-- Dungeon fixes override - fixes broken dungeon chains and inaccessible dungeons
-- Generated: 2026-02-11 (v2 - corrected Bworks/Minotoror confusion)
-- Applied after CyonDBFULL.sql and drops override

-- ============================================================
-- FIX 1: Donjon des Bworks - NPC entry correction
-- Original NPC 775 response 2794 pointed to non-existent map 9570.
-- Previous fix wrongly pointed it to 9565 (Minotoror area!).
-- Correct target: map 9750 (first room of real Bworks dungeon).
-- Chain 9750→9751→...→9760→9767(exit) already exists in DB.
-- ============================================================
UPDATE npc_reponses_actions SET args='9750,200,8135,9470' WHERE id=2794 AND type=15;

-- ============================================================
-- FIX 2: Labyrinthe du Minotoror (NEW dungeon)
-- Maps 9554-9880 (subareas 210, 319) are Minotoror labyrinth.
-- Key: 7924 (Clef du Labyrinthe du Minotoror)
-- Entry via NPC 775 on map 9470, response 9901.
-- ============================================================

-- NPC response 9901: Minotoror entry (key 7924)
INSERT IGNORE INTO npc_reponses_actions VALUES (9901, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9901, 15, '9554,200,7924,9470');

-- Minotoror chain (progressive difficulty, 9 rooms + boss):
-- 9554 (Serpiplume) → 9557 (Serpiplume, Scaratos) → 9562 (Scaratos, Serpiplume)
-- → 9563 (Gamino, Scaratos, Deminoboule, Serpiplume) → 9565 (Serpiplume, Gamino)
-- → 9576 (Gamino, Serpiplume, Scaratos) → 9577 (Mominotor boss + guards)
-- → 9879 (Minotot, Minotoror, Mominotor, Deminoboule) → 9880 (Minotoror BOSS)
-- → exit to overworld 9470
INSERT INTO endfight_action VALUES (9554, 4, 0, '9557,200', '');
INSERT INTO endfight_action VALUES (9557, 4, 0, '9562,200', '');
INSERT INTO endfight_action VALUES (9562, 4, 0, '9563,200', '');
INSERT INTO endfight_action VALUES (9563, 4, 0, '9565,200', '');
INSERT INTO endfight_action VALUES (9565, 4, 0, '9576,200', '');
INSERT INTO endfight_action VALUES (9576, 4, 0, '9577,200', '');
INSERT INTO endfight_action VALUES (9577, 4, 0, '9879,200', '');
INSERT INTO endfight_action VALUES (9879, 4, 0, '9880,200', '');
INSERT INTO endfight_action VALUES (9880, 4, 0, '9470,200', '');

-- ============================================================
-- FIX 3: Chain 9778→1298 (Scarafeuille extended) - add NPC entry
-- NPC 781 on map 1298, question 3179, add second response 9902
-- ============================================================
INSERT IGNORE INTO npc_reponses_actions VALUES (9902, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9902, 15, '9778,200,0,1298');

UPDATE npc_questions SET responses='2944;9902' WHERE ID=3179;

-- ============================================================
-- FIX 4: Donjon du Maitre Corbac - create chain from existing maps
-- Maps 9589-9599 have Corbac monsters
-- Map 9591 has Maitre Corbac (289) as boss
-- Key: 7926 (Clef du Donjon du Maitre Corbac)
-- ============================================================

-- Corbac chain: 9589→9590→9592→9593→9594→9596→9599→9591(boss)→9470
INSERT INTO endfight_action VALUES (9589, 4, 0, '9590,200', '');
INSERT INTO endfight_action VALUES (9590, 4, 0, '9592,200', '');
INSERT INTO endfight_action VALUES (9592, 4, 0, '9593,200', '');
INSERT INTO endfight_action VALUES (9593, 4, 0, '9594,200', '');
INSERT INTO endfight_action VALUES (9594, 4, 0, '9596,200', '');
INSERT INTO endfight_action VALUES (9596, 4, 0, '9599,200', '');
INSERT INTO endfight_action VALUES (9599, 4, 0, '9591,200', '');
INSERT INTO endfight_action VALUES (9591, 4, 0, '9470,200', '');

-- NPC entry for Maitre Corbac dungeon (response 9903, key 7926)
INSERT IGNORE INTO npc_reponses_actions VALUES (9903, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9903, 15, '9589,200,7926,9470');

-- ============================================================
-- FIX 5: Update NPC 775 question 3172 to include all dungeon options
-- Response 2794: Donjon des Bworks (key 8135) → map 9750
-- Response 9901: Labyrinthe du Minotoror (key 7924) → map 9554
-- Response 9903: Donjon du Maitre Corbac (key 7926) → map 9589
-- ============================================================
UPDATE npc_questions SET responses='2794;9901;9903' WHERE ID=3172;

-- ============================================================
-- FIX 6: Incarnam Dungeon - extend chain and add NPC entry
-- Current chain: 10360→10361 (only 2 rooms)
-- Extended: 10360→10361→10362→10363→exit to 10352
-- Monsters: Chafer Prepubere (996), Milimilou (1001), Larve (405)
-- Key: 8545 (Clef du Donjon d'Incarnam)
-- ============================================================

-- Delete old incomplete chain
DELETE FROM endfight_action WHERE map=10360 AND fighttype=4;

-- New extended chain (4 rooms + exit)
INSERT INTO endfight_action VALUES (10360, 4, 0, '10361,200', '');
INSERT INTO endfight_action VALUES (10361, 4, 0, '10362,200', '');
INSERT INTO endfight_action VALUES (10362, 4, 0, '10363,200', '');
INSERT INTO endfight_action VALUES (10363, 4, 0, '10352,200', '');

-- NPC entry for Incarnam dungeon
-- Add question 3665 for NPC 859 on map 10317 (Incarnam Lake area)
-- Response 9904 teleports to dungeon entry 10360 with key 8545
INSERT IGNORE INTO npc_reponses_actions VALUES (9904, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9904, 15, '10360,200,8545,10317');

-- Create the missing question entry for NPC 859
INSERT IGNORE INTO npc_questions VALUES (3665, '9904', '', '', 0);
