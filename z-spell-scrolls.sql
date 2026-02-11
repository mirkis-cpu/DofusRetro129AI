-- =========================================
-- Spell Scroll Shop - DofusEmu (CyonEmu 2.9)
-- 13 NPC obchodniku (per class) na mape 1507 (Iop Temple, Astrub)
-- Kazdy prodava scrolly pro vsechny spelly dane tridy
-- Jakakoli postava se muze naucit jakykoli spell
-- =========================================

-- Zvysit GROUP_CONCAT limit
SET SESSION group_concat_max_len = 65535;

-- =========================================
-- SEKCE 1: Cleanup (idempotence - bezpecne re-run)
-- =========================================

-- Smazat use_item_actions pro spell scrolly (item IDs 20000+)
DELETE FROM use_item_actions WHERE template IN (
    -- Feca
    20003,20006,20017,20004,20002,20001,20009,20018,20020,20014,20019,20005,20016,20008,20012,20011,20010,20007,20015,20013,21901,
    -- Osamodas
    20034,20021,20023,20026,20022,20035,20028,20037,20030,20027,20024,20033,20025,20038,20036,20032,20029,20039,20040,20031,21902,
    -- Enutrof
    20051,20043,20041,20049,20042,20047,20048,20045,20053,20046,20052,20044,20050,20054,20055,20056,20058,20059,20057,20060,21903,
    -- Sram
    20061,20072,20065,20066,20068,20063,20074,20064,20079,20078,20071,20062,20069,20077,20073,20067,20070,20075,20076,20080,21904,
    -- Xelor
    20082,20081,20083,20084,20100,20092,20088,20093,20085,20096,20098,20086,20089,20090,20087,20094,20099,20095,20091,20097,21905,
    -- Ecaflip
    20102,20103,20105,20109,20113,20111,20104,20119,20101,20107,20116,20106,20117,20108,20115,20118,20110,20112,20114,20120,21906,
    -- Eniripsa
    20125,20128,20121,20124,20122,20126,20127,20123,20130,20131,20132,20133,20134,20135,20129,20136,20137,20138,20139,20140,21907,
    -- Iop
    20143,20141,20142,20144,20145,20146,20147,20148,20154,20150,20151,20155,20152,20153,20149,20156,20157,20158,20160,20159,21908,
    -- Cra
    20161,20169,20164,20163,20165,20172,20167,20168,20162,20170,20171,20166,20173,20174,20176,20175,20178,20177,20179,20180,21909,
    -- Sadida
    20183,20200,20193,20198,20195,20182,20192,20197,20189,20181,20199,20191,20186,20196,20190,20194,20185,20184,20188,20187,21910,
    -- Sacrieur
    20432,20431,20434,20444,20449,20436,20437,20439,20433,20443,20440,20442,20441,20445,20438,20446,20447,20448,20435,20450,21911,
    -- Pandawa
    20686,20692,20687,20689,20690,20691,20688,20693,20694,20695,20696,20697,20698,20699,20700,20701,20702,20703,20704,20705,21912,
    -- Zobal
    26000,26001,26002,26003,26004,26005,26006,26007,26008,26009,26010,26011,26012,26013,26014,26015
);

-- Smazat item templates pro spell scrolly
DELETE FROM item_template WHERE id IN (
    20003,20006,20017,20004,20002,20001,20009,20018,20020,20014,20019,20005,20016,20008,20012,20011,20010,20007,20015,20013,21901,
    20034,20021,20023,20026,20022,20035,20028,20037,20030,20027,20024,20033,20025,20038,20036,20032,20029,20039,20040,20031,21902,
    20051,20043,20041,20049,20042,20047,20048,20045,20053,20046,20052,20044,20050,20054,20055,20056,20058,20059,20057,20060,21903,
    20061,20072,20065,20066,20068,20063,20074,20064,20079,20078,20071,20062,20069,20077,20073,20067,20070,20075,20076,20080,21904,
    20082,20081,20083,20084,20100,20092,20088,20093,20085,20096,20098,20086,20089,20090,20087,20094,20099,20095,20091,20097,21905,
    20102,20103,20105,20109,20113,20111,20104,20119,20101,20107,20116,20106,20117,20108,20115,20118,20110,20112,20114,20120,21906,
    20125,20128,20121,20124,20122,20126,20127,20123,20130,20131,20132,20133,20134,20135,20129,20136,20137,20138,20139,20140,21907,
    20143,20141,20142,20144,20145,20146,20147,20148,20154,20150,20151,20155,20152,20153,20149,20156,20157,20158,20160,20159,21908,
    20161,20169,20164,20163,20165,20172,20167,20168,20162,20170,20171,20166,20173,20174,20176,20175,20178,20177,20179,20180,21909,
    20183,20200,20193,20198,20195,20182,20192,20197,20189,20181,20199,20191,20186,20196,20190,20194,20185,20184,20188,20187,21910,
    20432,20431,20434,20444,20449,20436,20437,20439,20433,20443,20440,20442,20441,20445,20438,20446,20447,20448,20435,20450,21911,
    20686,20692,20687,20689,20690,20691,20688,20693,20694,20695,20696,20697,20698,20699,20700,20701,20702,20703,20704,20705,21912,
    26000,26001,26002,26003,26004,26005,26006,26007,26008,26009,26010,26011,26012,26013,26014,26015
);

-- Smazat NPC spawns a templates
DELETE FROM npcs WHERE npcid BETWEEN 1254 AND 1266;
DELETE FROM npc_template WHERE id BETWEEN 1254 AND 1266;

-- =========================================
-- SEKCE 2: Item Templates (scroll per spell, dynamicky z sorts tabulky)
-- Formula: item_id = 20000 + spell_id
-- =========================================

-- Feca spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (3,6,17,4,2,1,9,18,20,14,19,5,16,8,12,11,10,7,15,13,1901);

-- Osamodas spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (34,21,23,26,22,35,28,37,30,27,24,33,25,38,36,32,29,39,40,31,1902);

-- Enutrof spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (51,43,41,49,42,47,48,45,53,46,52,44,50,54,55,56,58,59,57,60,1903);

-- Sram spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (61,72,65,66,68,63,74,64,79,78,71,62,69,77,73,67,70,75,76,80,1904);

-- Xelor spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (82,81,83,84,100,92,88,93,85,96,98,86,89,90,87,94,99,95,91,97,1905);

-- Ecaflip spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (102,103,105,109,113,111,104,119,101,107,116,106,117,108,115,118,110,112,114,120,1906);

-- Eniripsa spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (125,128,121,124,122,126,127,123,130,131,132,133,134,135,129,136,137,138,139,140,1907);

-- Iop spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (143,141,142,144,145,146,147,148,154,150,151,155,152,153,149,156,157,158,160,159,1908);

-- Cra spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (161,169,164,163,165,172,167,168,162,170,171,166,173,174,176,175,178,177,179,180,1909);

-- Sadida spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (183,200,193,198,195,182,192,197,189,181,199,191,186,196,190,194,185,184,188,187,1910);

-- Sacrieur spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (432,431,434,444,449,436,437,439,433,443,440,442,441,445,438,446,447,448,435,450,1911);

-- Pandawa spells (21)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (686,692,687,689,690,691,688,693,694,695,696,697,698,699,700,701,702,703,704,705,1912);

-- Zobal spells (16)
INSERT INTO item_template (id, type, name, level, statsTemplate, pod, panoplie, prix, `condition`, armesInfos, sold, avgPrice, points)
SELECT 20000 + s.id, 75, CONCAT('Parchemin: ', s.nom), 1, '25c#0#0#0#0d0+0', 1, -1, 500000, '', '', 0, 0, 0
FROM sorts s WHERE s.id IN (6000,6001,6002,6003,6004,6005,6006,6007,6008,6009,6010,6011,6012,6013,6014,6015);

-- =========================================
-- SEKCE 3: Use Item Actions
-- Action 9 = naucit spell, Action 5 = spotrebovat scroll
-- =========================================

-- Feca - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (3,6,17,4,2,1,9,18,20,14,19,5,16,8,12,11,10,7,15,13,1901);
-- Feca - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (3,6,17,4,2,1,9,18,20,14,19,5,16,8,12,11,10,7,15,13,1901);

-- Osamodas - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (34,21,23,26,22,35,28,37,30,27,24,33,25,38,36,32,29,39,40,31,1902);
-- Osamodas - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (34,21,23,26,22,35,28,37,30,27,24,33,25,38,36,32,29,39,40,31,1902);

-- Enutrof - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (51,43,41,49,42,47,48,45,53,46,52,44,50,54,55,56,58,59,57,60,1903);
-- Enutrof - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (51,43,41,49,42,47,48,45,53,46,52,44,50,54,55,56,58,59,57,60,1903);

-- Sram - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (61,72,65,66,68,63,74,64,79,78,71,62,69,77,73,67,70,75,76,80,1904);
-- Sram - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (61,72,65,66,68,63,74,64,79,78,71,62,69,77,73,67,70,75,76,80,1904);

-- Xelor - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (82,81,83,84,100,92,88,93,85,96,98,86,89,90,87,94,99,95,91,97,1905);
-- Xelor - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (82,81,83,84,100,92,88,93,85,96,98,86,89,90,87,94,99,95,91,97,1905);

-- Ecaflip - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (102,103,105,109,113,111,104,119,101,107,116,106,117,108,115,118,110,112,114,120,1906);
-- Ecaflip - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (102,103,105,109,113,111,104,119,101,107,116,106,117,108,115,118,110,112,114,120,1906);

-- Eniripsa - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (125,128,121,124,122,126,127,123,130,131,132,133,134,135,129,136,137,138,139,140,1907);
-- Eniripsa - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (125,128,121,124,122,126,127,123,130,131,132,133,134,135,129,136,137,138,139,140,1907);

-- Iop - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (143,141,142,144,145,146,147,148,154,150,151,155,152,153,149,156,157,158,160,159,1908);
-- Iop - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (143,141,142,144,145,146,147,148,154,150,151,155,152,153,149,156,157,158,160,159,1908);

-- Cra - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (161,169,164,163,165,172,167,168,162,170,171,166,173,174,176,175,178,177,179,180,1909);
-- Cra - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (161,169,164,163,165,172,167,168,162,170,171,166,173,174,176,175,178,177,179,180,1909);

-- Sadida - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (183,200,193,198,195,182,192,197,189,181,199,191,186,196,190,194,185,184,188,187,1910);
-- Sadida - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (183,200,193,198,195,182,192,197,189,181,199,191,186,196,190,194,185,184,188,187,1910);

-- Sacrieur - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (432,431,434,444,449,436,437,439,433,443,440,442,441,445,438,446,447,448,435,450,1911);
-- Sacrieur - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (432,431,434,444,449,436,437,439,433,443,440,442,441,445,438,446,447,448,435,450,1911);

-- Pandawa - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (686,692,687,689,690,691,688,693,694,695,696,697,698,699,700,701,702,703,704,705,1912);
-- Pandawa - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (686,692,687,689,690,691,688,693,694,695,696,697,698,699,700,701,702,703,704,705,1912);

-- Zobal - learn spell
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 9, CAST(s.id AS CHAR) FROM sorts s WHERE s.id IN (6000,6001,6002,6003,6004,6005,6006,6007,6008,6009,6010,6011,6012,6013,6014,6015);
-- Zobal - consume scroll
INSERT INTO use_item_actions (template, type, args)
SELECT 20000 + s.id, 5, CONCAT(20000 + s.id, ',-1') FROM sorts s WHERE s.id IN (6000,6001,6002,6003,6004,6005,6006,6007,6008,6009,6010,6011,6012,6013,6014,6015);

-- =========================================
-- SEKCE 4: NPC Templates (13 obchodniku, per class)
-- initQuestion = -1 → primy shop (zadny dialog)
-- ID > 1234 → automaticky buy/sell UI (NPC_tmpl.java hack)
-- =========================================

-- 1. Feca Shop (NPC 1254)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1254, 0, 30, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015,20016,20017,20018,20019,20020,21901');

-- 2. Osamodas Shop (NPC 1255)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1255, 0, 91, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20021,20022,20023,20024,20025,20026,20027,20028,20029,20030,20031,20032,20033,20034,20035,20036,20037,20038,20039,20040,21902');

-- 3. Enutrof Shop (NPC 1256)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1256, 0, 51, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20041,20042,20043,20044,20045,20046,20047,20048,20049,20050,20051,20052,20053,20054,20055,20056,20057,20058,20059,20060,21903');

-- 4. Sram Shop (NPC 1257)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1257, 0, 40, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20061,20062,20063,20064,20065,20066,20067,20068,20069,20070,20071,20072,20073,20074,20075,20076,20077,20078,20079,20080,21904');

-- 5. Xelor Shop (NPC 1258)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1258, 0, 61, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20081,20082,20083,20084,20085,20086,20087,20088,20089,20090,20091,20092,20093,20094,20095,20096,20097,20098,20099,20100,21905');

-- 6. Ecaflip Shop (NPC 1259)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1259, 0, 70, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20101,20102,20103,20104,20105,20106,20107,20108,20109,20110,20111,20112,20113,20114,20115,20116,20117,20118,20119,20120,21906');

-- 7. Eniripsa Shop (NPC 1260)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1260, 0, 80, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20121,20122,20123,20124,20125,20126,20127,20128,20129,20130,20131,20132,20133,20134,20135,20136,20137,20138,20139,20140,21907');

-- 8. Iop Shop (NPC 1261)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1261, 0, 10, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20141,20142,20143,20144,20145,20146,20147,20148,20149,20150,20151,20152,20153,20154,20155,20156,20157,20158,20159,20160,21908');

-- 9. Cra Shop (NPC 1262)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1262, 0, 50, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20161,20162,20163,20164,20165,20166,20167,20168,20169,20170,20171,20172,20173,20174,20175,20176,20177,20178,20179,20180,21909');

-- 10. Sadida Shop (NPC 1263)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1263, 0, 100, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20181,20182,20183,20184,20185,20186,20187,20188,20189,20190,20191,20192,20193,20194,20195,20196,20197,20198,20199,20200,21910');

-- 11. Sacrieur Shop (NPC 1264)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1264, 0, 110, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20431,20432,20433,20434,20435,20436,20437,20438,20439,20440,20441,20442,20443,20444,20445,20446,20447,20448,20449,20450,21911');

-- 12. Pandawa Shop (NPC 1265)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1265, 0, 121, 100, 100, 0, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20686,20687,20688,20689,20690,20691,20692,20693,20694,20695,20696,20697,20698,20699,20700,20701,20702,20703,20704,20705,21912');

-- 13. Zobal Shop (NPC 1266) - spell 447 sdileny se Sacrieur (item 20447 uz existuje)
INSERT INTO npc_template (id, bonusValue, gfxID, scaleX, scaleY, sex,
    color1, color2, color3, accessories, extraClip, customArtWork, initQuestion, ventes)
VALUES (1266, 0, 101, 100, 100, 1, -1, -1, -1, '0,0,0,0,0', -1, 0, -1,
    '20447,26000,26001,26002,26003,26004,26005,26006,26007,26008,26009,26010,26011,26012,26013,26014,26015');

-- =========================================
-- SEKCE 5: NPC Spawns na mape 1507 (Iop Temple, Astrub)
-- Row 30 = siroky otevreny prostor v dolni casti templeu
-- Cells 558-570 (13 walkable cells v rade)
-- orientation=3 = smer jih
-- =========================================

INSERT INTO npcs (mapid, npcid, cellid, orientation) VALUES
(1507, 1254, 558, 3),   -- Feca Shop
(1507, 1255, 559, 3),   -- Osamodas Shop
(1507, 1256, 560, 3),   -- Enutrof Shop
(1507, 1257, 561, 3),   -- Sram Shop
(1507, 1258, 562, 3),   -- Xelor Shop
(1507, 1259, 563, 3),   -- Ecaflip Shop
(1507, 1260, 564, 3),   -- Eniripsa Shop
(1507, 1261, 565, 3),   -- Iop Shop
(1507, 1262, 566, 3),   -- Cra Shop
(1507, 1263, 567, 3),   -- Sadida Shop
(1507, 1264, 568, 3),   -- Sacrieur Shop
(1507, 1265, 569, 3),   -- Pandawa Shop
(1507, 1266, 570, 3);   -- Zobal Shop

-- =========================================
-- SEKCE 6: Verifikace
-- =========================================

SELECT '=== Spell Scroll Items ===' AS info;
SELECT COUNT(*) AS total_scrolls FROM item_template WHERE id >= 20000 AND type = 75;

SELECT '=== Use Actions ===' AS info;
SELECT type, COUNT(*) AS action_count FROM use_item_actions
WHERE template >= 20000 GROUP BY type ORDER BY type;

SELECT '=== NPC Templates ===' AS info;
SELECT id,
    LENGTH(ventes) AS ventes_bytes,
    CASE WHEN ventes = '' THEN 0
         ELSE (LENGTH(ventes) - LENGTH(REPLACE(ventes, ',', '')) + 1)
    END AS item_count
FROM npc_template WHERE id BETWEEN 1254 AND 1266 ORDER BY id;

SELECT '=== NPC Spawns ===' AS info;
SELECT mapid, npcid, cellid FROM npcs WHERE npcid BETWEEN 1254 AND 1266 ORDER BY npcid;

SELECT '=== Vzorkove scrolly ===' AS info;
SELECT id, name, prix FROM item_template WHERE id >= 20000 AND type = 75 ORDER BY id LIMIT 10;
