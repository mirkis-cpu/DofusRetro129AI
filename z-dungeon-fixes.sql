-- Dungeon fixes override - fixes broken dungeon chains and inaccessible dungeons
-- Generated: 2026-02-11
-- Applied after CyonDBFULL.sql and drops override

-- ============================================================
-- FIX 1: Donjon des Bworks - NPC teleports to non-existent map 9570
-- Redirect to existing map 9565 (first Bwork dungeon room)
-- ============================================================
UPDATE npc_reponses_actions SET args='9565,200,8135,9470' WHERE id=2794 AND type=15;

-- Add endfight_action chain for Bwork dungeon rooms
-- 9565 (Serpiplume, Gamino) → 9572 (Serpiplume) → 9576 (Gamino, Serpiplume, Scaratos) → 9577 (Mominotor boss)
INSERT INTO endfight_action VALUES (9565, 4, 0, '9572,200', '');
INSERT INTO endfight_action VALUES (9572, 4, 0, '9576,200', '');
INSERT INTO endfight_action VALUES (9576, 4, 0, '9577,200', '');
-- After boss fight on 9577, teleport back to overworld entrance (map 9470)
INSERT INTO endfight_action VALUES (9577, 4, 0, '9470,200', '');

-- ============================================================
-- FIX 2: Chain 9750→9767 (Bwork variant) - add NPC entry
-- Exit NPC 776 on map 9767 teleports to 9470
-- So we add a response to NPC 775 on map 9470 for this chain too
-- We need a new question with two dungeon options
-- ============================================================

-- First check: chain 9750 uses Bwork monsters (Bworkette 792, Bwork Mage 53, Bwork Archer 74)
-- The exit on map 9767 has NPC 776 that gives item 8000 and teleports to 9470
-- We need a key for this dungeon - use existing key or none
-- For now, add a scripted_cell on map 9470 that teleports to 9750 (no key required)
-- Actually, let's add it as a second NPC response to NPC 775

-- Create new question with both dungeon options (Bworks + Bwork variant)
-- Note: NPC 775 currently has question 3172 with only response 2794
-- We'll add a new response for the 9750 chain

-- New response ID: use 9901 (high number to avoid conflicts)
INSERT IGNORE INTO npc_reponses_actions VALUES (9901, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9901, 15, '9750,200,0,9470');

-- Update question 3172 to include both responses
UPDATE npc_questions SET responses='2794;9901' WHERE ID=3172;

-- ============================================================
-- FIX 3: Chain 9778→1298 (Scarafeuille extended) - add NPC entry
-- Map 1298 already has NPC 781 (question 3179, response 2944 for Scarafeuilles chain 5)
-- Add a second response to NPC 781's question for this extended chain
-- ============================================================

-- New response ID: 9902
INSERT IGNORE INTO npc_reponses_actions VALUES (9902, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9902, 15, '9778,200,0,1298');

-- Update question 3179 to include both responses
UPDATE npc_questions SET responses='2944;9902' WHERE ID=3179;

-- ============================================================
-- FIX 4: Chain 10360→10361 (Incarnam mini-dungeon) - add NPC entry
-- These are Incarnam-level monsters (Petit Tofu, Larve, Arakne)
-- Need to find an appropriate NPC/location in Incarnam
-- Add a scripted cell teleport on a nearby map instead
-- Map 10360 coords: 6,5,447 - check for nearby accessible maps
-- ============================================================

-- For Incarnam mini-dungeon, add a scripted cell on the map
-- that leads to 10360. We need to find a suitable overworld map.
-- For now, add a direct scripted_cell entry from a nearby Incarnam map
-- Using map 10300 area (typical Incarnam zone)
-- Actually, we don't know which map connects to 10360, so we'll create
-- an NPC-based entry. Let's place a simple NPC on the map adjacent to 10360.
-- Skip this for now - needs map adjacency investigation

-- ============================================================
-- FIX 5: Maitre Corbac dungeon - create chain from existing maps
-- Maps 9589-9599 have Corbac monsters
-- Map 9591 has Maitre Corbac (289) as boss
-- Key: 7926 (Clef du Donjon du Maitre Corbac)
-- ============================================================

-- Create endfight_action chain
-- Room progression: 9589→9590→9591(boss) then to 9592→9593→9594→9596→9599
-- Actually, 9591 has the boss (Maitre Corbac 289), so let's chain up to boss then exit
-- Pattern: rooms before boss → boss room → exit to overworld
INSERT INTO endfight_action VALUES (9589, 4, 0, '9590,200', '');
INSERT INTO endfight_action VALUES (9590, 4, 0, '9592,200', '');
INSERT INTO endfight_action VALUES (9592, 4, 0, '9593,200', '');
INSERT INTO endfight_action VALUES (9593, 4, 0, '9594,200', '');
INSERT INTO endfight_action VALUES (9594, 4, 0, '9596,200', '');
INSERT INTO endfight_action VALUES (9596, 4, 0, '9599,200', '');
INSERT INTO endfight_action VALUES (9599, 4, 0, '9591,200', '');
-- After boss fight (Maitre Corbac on 9591), exit to overworld
-- Exit to map 9470 area (same Bwork/Corbac region)
INSERT INTO endfight_action VALUES (9591, 4, 0, '9470,200', '');

-- Add NPC entry for Maitre Corbac dungeon
-- Reuse NPC 775 on map 9470 - add third response option
-- New response ID: 9903
INSERT IGNORE INTO npc_reponses_actions VALUES (9903, 1, 'DV');
INSERT IGNORE INTO npc_reponses_actions VALUES (9903, 15, '9589,200,7926,9470');

-- Update question 3172 to include all three dungeon options
UPDATE npc_questions SET responses='2794;9901;9903' WHERE ID=3172;
