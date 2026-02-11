-- ============================================================================
-- Consumable Items Fix: Missing use_item_actions entries
-- Fixes items that do nothing when used from inventory
-- ============================================================================

-- Prevent duplicate inserts
DELETE FROM `use_item_actions` WHERE `template` IN (
  281, 989, 990, 991, 992, 993, 994, 995,
  2332, 6643, 6644, 6646, 6647,
  6666, 6667, 6668, 6669, 6670,
  7804, 8948, 8949, 8950, 8951, 8952, 8953, 8954, 8955,
  9472, 9636, 9637, 9638, 9639, 9640, 9641, 9642, 9643,
  6857, 8145,
  695, 713, 714, 715, 716, 717, 878, 879,
  10382, 10383, 10384, 10385, 10386, 10387, 10388,
  10389, 10390, 10391, 10392, 10393, 10394,
  10395, 10396, 10397, 10398, 10399, 10400,
  10401, 10402, 10403, 10404, 10405, 10407
);

-- ============================================================================
-- CANDY / FRUIT (Type 42 - Bonbon) - HP Healing
-- Action 10 = Heal HP (min,max), Action 5 = Consume item (templateID,-1)
-- ============================================================================

-- Pomme (Apple) - 30 HP
INSERT INTO `use_item_actions` VALUES ('281', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('281', '5', '281,-1');

-- Shigekax Citron - 100 HP
INSERT INTO `use_item_actions` VALUES ('989', '10', '100,100');
INSERT INTO `use_item_actions` VALUES ('989', '5', '989,-1');

-- Shigekax Fraise - 70 HP
INSERT INTO `use_item_actions` VALUES ('990', '10', '70,70');
INSERT INTO `use_item_actions` VALUES ('990', '5', '990,-1');

-- Shigekax Menthe - 200 HP
INSERT INTO `use_item_actions` VALUES ('991', '10', '200,200');
INSERT INTO `use_item_actions` VALUES ('991', '5', '991,-1');

-- Shigekax Cerise - 50 HP
INSERT INTO `use_item_actions` VALUES ('992', '10', '50,50');
INSERT INTO `use_item_actions` VALUES ('992', '5', '992,-1');

-- Vertgely - 51-100 HP
INSERT INTO `use_item_actions` VALUES ('993', '10', '51,100');
INSERT INTO `use_item_actions` VALUES ('993', '5', '993,-1');

-- Rougely - 101-200 HP
INSERT INTO `use_item_actions` VALUES ('994', '10', '101,200');
INSERT INTO `use_item_actions` VALUES ('994', '5', '994,-1');

-- Blugely - 11-20 HP
INSERT INTO `use_item_actions` VALUES ('995', '10', '11,20');
INSERT INTO `use_item_actions` VALUES ('995', '5', '995,-1');

-- Bonbon de Consolation - 5 HP
INSERT INTO `use_item_actions` VALUES ('2332', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('2332', '5', '2332,-1');

-- Booden Glace - 50 HP
INSERT INTO `use_item_actions` VALUES ('6643', '10', '50,50');
INSERT INTO `use_item_actions` VALUES ('6643', '5', '6643,-1');

-- Cone Wayal - 40 HP
INSERT INTO `use_item_actions` VALUES ('6644', '10', '40,40');
INSERT INTO `use_item_actions` VALUES ('6644', '5', '6644,-1');

-- Glace Bontarienne - 30 HP
INSERT INTO `use_item_actions` VALUES ('6646', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('6646', '5', '6646,-1');

-- Magma Brakmarien - 30 HP
INSERT INTO `use_item_actions` VALUES ('6647', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('6647', '5', '6647,-1');

-- Sucette Tofu - 6-10 HP
INSERT INTO `use_item_actions` VALUES ('6666', '10', '6,10');
INSERT INTO `use_item_actions` VALUES ('6666', '5', '6666,-1');

-- Sucette Trool - 51-150 HP
INSERT INTO `use_item_actions` VALUES ('6667', '10', '51,150');
INSERT INTO `use_item_actions` VALUES ('6667', '5', '6667,-1');

-- Hot Dog de Tofu - 26-75 HP
INSERT INTO `use_item_actions` VALUES ('6668', '10', '26,75');
INSERT INTO `use_item_actions` VALUES ('6668', '5', '6668,-1');

-- Burger de Trool - 51-150 HP
INSERT INTO `use_item_actions` VALUES ('6669', '10', '51,150');
INSERT INTO `use_item_actions` VALUES ('6669', '5', '6669,-1');

-- Frites de Tournesol Sauvage - 51-150 HP
INSERT INTO `use_item_actions` VALUES ('6670', '10', '51,150');
INSERT INTO `use_item_actions` VALUES ('6670', '5', '6670,-1');

-- Bonbon Energetique - 5000 HP
INSERT INTO `use_item_actions` VALUES ('7804', '10', '5000,5000');
INSERT INTO `use_item_actions` VALUES ('7804', '5', '7804,-1');

-- Shigekax Caramel - 5 HP
INSERT INTO `use_item_actions` VALUES ('8948', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8948', '5', '8948,-1');

-- Shigekax Banane - 5 HP
INSERT INTO `use_item_actions` VALUES ('8949', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8949', '5', '8949,-1');

-- Shigekax Orange - 5 HP
INSERT INTO `use_item_actions` VALUES ('8950', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8950', '5', '8950,-1');

-- Shigekax Poire - 5 HP
INSERT INTO `use_item_actions` VALUES ('8951', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8951', '5', '8951,-1');

-- Shigekax Pomme - 5 HP
INSERT INTO `use_item_actions` VALUES ('8952', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8952', '5', '8952,-1');

-- Shigekax Chocolat - 5 HP
INSERT INTO `use_item_actions` VALUES ('8953', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8953', '5', '8953,-1');

-- Shigekax Vanille - 5 HP
INSERT INTO `use_item_actions` VALUES ('8954', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8954', '5', '8954,-1');

-- Shigekax Melon - 5 HP
INSERT INTO `use_item_actions` VALUES ('8955', '10', '5,5');
INSERT INTO `use_item_actions` VALUES ('8955', '5', '8955,-1');

-- Shigekax Passion - 11-20 HP
INSERT INTO `use_item_actions` VALUES ('9472', '10', '11,20');
INSERT INTO `use_item_actions` VALUES ('9472', '5', '9472,-1');

-- Shigekax Reinette - 50 HP
INSERT INTO `use_item_actions` VALUES ('9636', '10', '50,50');
INSERT INTO `use_item_actions` VALUES ('9636', '5', '9636,-1');

-- Shigekax Griotte - 30 HP
INSERT INTO `use_item_actions` VALUES ('9637', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('9637', '5', '9637,-1');

-- Shigekax Indigo - 30 HP
INSERT INTO `use_item_actions` VALUES ('9638', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('9638', '5', '9638,-1');

-- Shigekax Coco - 30 HP
INSERT INTO `use_item_actions` VALUES ('9639', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('9639', '5', '9639,-1');

-- Shigekax Prune - 150 HP
INSERT INTO `use_item_actions` VALUES ('9640', '10', '150,150');
INSERT INTO `use_item_actions` VALUES ('9640', '5', '9640,-1');

-- Shigekax Praline - 30 HP
INSERT INTO `use_item_actions` VALUES ('9641', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('9641', '5', '9641,-1');

-- Shigekax Mirabelle - 30 HP
INSERT INTO `use_item_actions` VALUES ('9642', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('9642', '5', '9642,-1');

-- Shigekax Acidule - 30 HP
INSERT INTO `use_item_actions` VALUES ('9643', '10', '30,30');
INSERT INTO `use_item_actions` VALUES ('9643', '5', '9643,-1');

-- ============================================================================
-- BEER (Type 37 - Biere) - HP Healing
-- ============================================================================

-- Biere d'Astrub - 1 HP (crappy beer)
INSERT INTO `use_item_actions` VALUES ('6857', '10', '1,1');
INSERT INTO `use_item_actions` VALUES ('6857', '5', '6857,-1');

-- Biere de Bwork - 501-1000 HP
INSERT INTO `use_item_actions` VALUES ('8145', '10', '501,1000');
INSERT INTO `use_item_actions` VALUES ('8145', '5', '8145,-1');

-- ============================================================================
-- PROFESSION PARCHMENTS (Type 13) - Learn Job
-- Action 6 = Learn Job (jobID), Action 5 = Consume item
-- ============================================================================

-- Parchemin de Bucheron → Job 2
INSERT INTO `use_item_actions` VALUES ('695', '6', '2');
INSERT INTO `use_item_actions` VALUES ('695', '5', '695,-1');

-- Parchemin de Forgeur d'epee → Job 11
INSERT INTO `use_item_actions` VALUES ('713', '6', '11');
INSERT INTO `use_item_actions` VALUES ('713', '5', '713,-1');

-- Parchemin de Forgeur de dagues → Job 17
INSERT INTO `use_item_actions` VALUES ('714', '6', '17');
INSERT INTO `use_item_actions` VALUES ('714', '5', '714,-1');

-- Parchemin de Sculpteur d'arcs → Job 13
INSERT INTO `use_item_actions` VALUES ('715', '6', '13');
INSERT INTO `use_item_actions` VALUES ('715', '5', '715,-1');

-- Parchemin de Sculpteur de batons → Job 18
INSERT INTO `use_item_actions` VALUES ('716', '6', '18');
INSERT INTO `use_item_actions` VALUES ('716', '5', '716,-1');

-- Parchemin de Sculpteur de baguettes → Job 19
INSERT INTO `use_item_actions` VALUES ('717', '6', '19');
INSERT INTO `use_item_actions` VALUES ('717', '5', '717,-1');

-- Parchemin de Boulanger → Job 25
INSERT INTO `use_item_actions` VALUES ('878', '6', '25');
INSERT INTO `use_item_actions` VALUES ('878', '5', '878,-1');

-- Parchemin de Cordonnier → Job 15
INSERT INTO `use_item_actions` VALUES ('879', '6', '15');
INSERT INTO `use_item_actions` VALUES ('879', '5', '879,-1');

-- Parchemin de Bijoutier → Job 16
INSERT INTO `use_item_actions` VALUES ('10382', '6', '16');
INSERT INTO `use_item_actions` VALUES ('10382', '5', '10382,-1');

-- Parchemin de Joaillomage → Job 63
INSERT INTO `use_item_actions` VALUES ('10383', '6', '63');
INSERT INTO `use_item_actions` VALUES ('10383', '5', '10383,-1');

-- Parchemin de Boucher → Job 56
INSERT INTO `use_item_actions` VALUES ('10384', '6', '56');
INSERT INTO `use_item_actions` VALUES ('10384', '5', '10384,-1');

-- Parchemin de Bricoleur → Job 65
INSERT INTO `use_item_actions` VALUES ('10385', '6', '65');
INSERT INTO `use_item_actions` VALUES ('10385', '5', '10385,-1');

-- Parchemin de Forgemage d'Epees → Job 44
INSERT INTO `use_item_actions` VALUES ('10386', '6', '44');
INSERT INTO `use_item_actions` VALUES ('10386', '5', '10386,-1');

-- Parchemin de Forgeur de Marteaux → Job 14
INSERT INTO `use_item_actions` VALUES ('10387', '6', '14');
INSERT INTO `use_item_actions` VALUES ('10387', '5', '10387,-1');

-- Parchemin de Forgemage de Marteaux → Job 45
INSERT INTO `use_item_actions` VALUES ('10388', '6', '45');
INSERT INTO `use_item_actions` VALUES ('10388', '5', '10388,-1');

-- Parchemin de Forgeur de Pelles → Job 20
INSERT INTO `use_item_actions` VALUES ('10389', '6', '20');
INSERT INTO `use_item_actions` VALUES ('10389', '5', '10389,-1');

-- Parchemin de Forgemage de Pelles → Job 46
INSERT INTO `use_item_actions` VALUES ('10390', '6', '46');
INSERT INTO `use_item_actions` VALUES ('10390', '5', '10390,-1');

-- Parchemin de Sculptemage de Batons → Job 50
INSERT INTO `use_item_actions` VALUES ('10391', '6', '50');
INSERT INTO `use_item_actions` VALUES ('10391', '5', '10391,-1');

-- Parchemin de Sculptemage de Baguettes → Job 49
INSERT INTO `use_item_actions` VALUES ('10392', '6', '49');
INSERT INTO `use_item_actions` VALUES ('10392', '5', '10392,-1');

-- Parchemin de Forgemage de Dagues → Job 43
INSERT INTO `use_item_actions` VALUES ('10393', '6', '43');
INSERT INTO `use_item_actions` VALUES ('10393', '5', '10393,-1');

-- Parchemin de Sculptemage d'Arcs → Job 48
INSERT INTO `use_item_actions` VALUES ('10394', '6', '48');
INSERT INTO `use_item_actions` VALUES ('10394', '5', '10394,-1');

-- Parchemin de Forgeur de Haches → Job 31
INSERT INTO `use_item_actions` VALUES ('10395', '6', '31');
INSERT INTO `use_item_actions` VALUES ('10395', '5', '10395,-1');

-- Parchemin de Forgemage de Haches → Job 47
INSERT INTO `use_item_actions` VALUES ('10396', '6', '47');
INSERT INTO `use_item_actions` VALUES ('10396', '5', '10396,-1');

-- Parchemin de Forgeur de Boucliers → Job 60
INSERT INTO `use_item_actions` VALUES ('10397', '6', '60');
INSERT INTO `use_item_actions` VALUES ('10397', '5', '10397,-1');

-- Parchemin de Mineur → Job 24
INSERT INTO `use_item_actions` VALUES ('10398', '6', '24');
INSERT INTO `use_item_actions` VALUES ('10398', '5', '10398,-1');

-- Parchemin de Poissonnier → Job 58
INSERT INTO `use_item_actions` VALUES ('10399', '6', '58');
INSERT INTO `use_item_actions` VALUES ('10399', '5', '10399,-1');

-- Parchemin de Tailleur → Job 27
INSERT INTO `use_item_actions` VALUES ('10400', '6', '27');
INSERT INTO `use_item_actions` VALUES ('10400', '5', '10400,-1');

-- Parchemin de Costumage → Job 64
INSERT INTO `use_item_actions` VALUES ('10401', '6', '64');
INSERT INTO `use_item_actions` VALUES ('10401', '5', '10401,-1');

-- Parchemin d'Alchimiste → Job 26
INSERT INTO `use_item_actions` VALUES ('10402', '6', '26');
INSERT INTO `use_item_actions` VALUES ('10402', '5', '10402,-1');

-- Parchemin de Paysan → Job 28
INSERT INTO `use_item_actions` VALUES ('10403', '6', '28');
INSERT INTO `use_item_actions` VALUES ('10403', '5', '10403,-1');

-- Parchemin de Chasseur → Job 41
INSERT INTO `use_item_actions` VALUES ('10404', '6', '41');
INSERT INTO `use_item_actions` VALUES ('10404', '5', '10404,-1');

-- Parchemin de Pecheur → Job 36
INSERT INTO `use_item_actions` VALUES ('10405', '6', '36');
INSERT INTO `use_item_actions` VALUES ('10405', '5', '10405,-1');

-- Parchemin de Cordomage → Job 62
INSERT INTO `use_item_actions` VALUES ('10407', '6', '62');
INSERT INTO `use_item_actions` VALUES ('10407', '5', '10407,-1');
