// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.kernel;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.common.Formulas;
import org.aestia.fight.spells.Spell;
import org.aestia.game.world.World;
import org.aestia.object.ObjectTemplate;

public class Constant {
	public static final int DEBUG_MAP_LIMIT = 30000;
	public static final String SERVER_VERSION = "0.3.5.2";
	public static final String SERVER_MAKER = "Aestia";
	public static final String CLIENT_VERSION = "1.29.1";
	public static final boolean IGNORE_VERSION = false;
	public static final int TIME_START_FIGHT = 45000;
	public static final int TIME_BY_TURN = 30000;
	public static Map<Integer, String> ZAAPI;
	public static Map<Integer, Integer> ZAAPS;
	public static final String ALL_PHOENIX = "-11;-54|2;-12|-41;-17|5;-9|25;-4|36;5|12;12|10;19|-10;13|-14;31|-43;0|-60;-3|-58;18|24;-43|27;-33";
	public static int G_BOOST;
	public static int G_RIGHT;
	public static int G_INVITE;
	public static int G_BAN;
	public static int G_ALLXP;
	public static int G_HISXP;
	public static int G_RANK;
	public static int G_POSPERCO;
	public static int G_COLLPERCO;
	public static int G_USEENCLOS;
	public static int G_AMENCLOS;
	public static int G_OTHDINDE;
	public static int H_GBLASON;
	public static int H_OBLASON;
	public static int H_GNOCODE;
	public static int H_OCANTOPEN;
	public static int C_GNOCODE;
	public static int C_OCANTOPEN;
	public static int H_GREPOS;
	public static int H_GTELE;
	public static final int ETAT_NEUTRE = 0;
	public static final int ETAT_SAOUL = 1;
	public static final int ETAT_CAPT_AME = 2;
	public static final int ETAT_PORTEUR = 3;
	public static final int ETAT_PEUREUX = 4;
	public static final int ETAT_DESORIENTE = 5;
	public static final int ETAT_ENRACINE = 6;
	public static final int ETAT_PESANTEUR = 7;
	public static final int ETAT_PORTE = 8;
	public static final int ETAT_MOTIV_SYLVESTRE = 9;
	public static final int ETAT_APPRIVOISEMENT = 10;
	public static final int ETAT_CHEVAUCHANT = 11;
	public static final int FIGHT_TYPE_CHALLENGE = 0;
	public static final int FIGHT_TYPE_AGRESSION = 1;
	public static final int FIGHT_TYPE_CONQUETE = 2;
	public static final int FIGHT_TYPE_DOPEUL = 3;
	public static final int FIGHT_TYPE_PVM = 4;
	public static final int FIGHT_TYPE_PVT = 5;
	public static final int FIGHT_STATE_INIT = 1;
	public static final int FIGHT_STATE_PLACE = 2;
	public static final int FIGHT_STATE_ACTIVE = 3;
	public static final int FIGHT_STATE_FINISHED = 4;
	public static final int ITEM_POS_NO_EQUIPED = -1;
	public static final int ITEM_POS_AMULETTE = 0;
	public static final int ITEM_POS_ARME = 1;
	public static final int ITEM_POS_ANNEAU1 = 2;
	public static final int ITEM_POS_CEINTURE = 3;
	public static final int ITEM_POS_ANNEAU2 = 4;
	public static final int ITEM_POS_BOTTES = 5;
	public static final int ITEM_POS_COIFFE = 6;
	public static final int ITEM_POS_CAPE = 7;
	public static final int ITEM_POS_FAMILIER = 8;
	public static final int ITEM_POS_DOFUS1 = 9;
	public static final int ITEM_POS_DOFUS2 = 10;
	public static final int ITEM_POS_DOFUS3 = 11;
	public static final int ITEM_POS_DOFUS4 = 12;
	public static final int ITEM_POS_DOFUS5 = 13;
	public static final int ITEM_POS_DOFUS6 = 14;
	public static final int ITEM_POS_BOUCLIER = 15;
	public static final int ITEM_POS_DRAGODINDE = 16;
	public static final int ITEM_POS_MUTATION = 20;
	public static final int ITEM_POS_ROLEPLAY_BUFF = 21;
	public static final int ITEM_POS_PNJ_SUIVEUR = 24;
	public static final int ITEM_POS_BENEDICTION = 23;
	public static final int ITEM_POS_MALEDICTION = 22;
	public static final int ITEM_POS_BONBON = 25;
	public static final int ITEM_TYPE_AMULETTE = 1;
	public static final int ITEM_TYPE_ARC = 2;
	public static final int ITEM_TYPE_BAGUETTE = 3;
	public static final int ITEM_TYPE_BATON = 4;
	public static final int ITEM_TYPE_DAGUES = 5;
	public static final int ITEM_TYPE_EPEE = 6;
	public static final int ITEM_TYPE_MARTEAU = 7;
	public static final int ITEM_TYPE_PELLE = 8;
	public static final int ITEM_TYPE_ANNEAU = 9;
	public static final int ITEM_TYPE_CEINTURE = 10;
	public static final int ITEM_TYPE_BOTTES = 11;
	public static final int ITEM_TYPE_POTION = 12;
	public static final int ITEM_TYPE_PARCHO_EXP = 13;
	public static final int ITEM_TYPE_DONS = 14;
	public static final int ITEM_TYPE_RESSOURCE = 15;
	public static final int ITEM_TYPE_COIFFE = 16;
	public static final int ITEM_TYPE_CAPE = 17;
	public static final int ITEM_TYPE_FAMILIER = 18;
	public static final int ITEM_TYPE_HACHE = 19;
	public static final int ITEM_TYPE_OUTIL = 20;
	public static final int ITEM_TYPE_PIOCHE = 21;
	public static final int ITEM_TYPE_FAUX = 22;
	public static final int ITEM_TYPE_DOFUS = 23;
	public static final int ITEM_TYPE_QUETES = 24;
	public static final int ITEM_TYPE_DOCUMENT = 25;
	public static final int ITEM_TYPE_FM_POTION = 26;
	public static final int ITEM_TYPE_TRANSFORM = 27;
	public static final int ITEM_TYPE_BOOST_FOOD = 28;
	public static final int ITEM_TYPE_BENEDICTION = 29;
	public static final int ITEM_TYPE_MALEDICTION = 30;
	public static final int ITEM_TYPE_RP_BUFF = 31;
	public static final int ITEM_TYPE_PERSO_SUIVEUR = 32;
	public static final int ITEM_TYPE_PAIN = 33;
	public static final int ITEM_TYPE_CEREALE = 34;
	public static final int ITEM_TYPE_FLEUR = 35;
	public static final int ITEM_TYPE_PLANTE = 36;
	public static final int ITEM_TYPE_BIERE = 37;
	public static final int ITEM_TYPE_BOIS = 38;
	public static final int ITEM_TYPE_MINERAIS = 39;
	public static final int ITEM_TYPE_ALLIAGE = 40;
	public static final int ITEM_TYPE_POISSON = 41;
	public static final int ITEM_TYPE_BONBON = 42;
	public static final int ITEM_TYPE_POTION_OUBLIE = 43;
	public static final int ITEM_TYPE_POTION_METIER = 44;
	public static final int ITEM_TYPE_POTION_SORT = 45;
	public static final int ITEM_TYPE_FRUIT = 46;
	public static final int ITEM_TYPE_OS = 47;
	public static final int ITEM_TYPE_POUDRE = 48;
	public static final int ITEM_TYPE_COMESTI_POISSON = 49;
	public static final int ITEM_TYPE_PIERRE_PRECIEUSE = 50;
	public static final int ITEM_TYPE_PIERRE_BRUTE = 51;
	public static final int ITEM_TYPE_FARINE = 52;
	public static final int ITEM_TYPE_PLUME = 53;
	public static final int ITEM_TYPE_POIL = 54;
	public static final int ITEM_TYPE_ETOFFE = 55;
	public static final int ITEM_TYPE_CUIR = 56;
	public static final int ITEM_TYPE_LAINE = 57;
	public static final int ITEM_TYPE_GRAINE = 58;
	public static final int ITEM_TYPE_PEAU = 59;
	public static final int ITEM_TYPE_HUILE = 60;
	public static final int ITEM_TYPE_PELUCHE = 61;
	public static final int ITEM_TYPE_POISSON_VIDE = 62;
	public static final int ITEM_TYPE_VIANDE = 63;
	public static final int ITEM_TYPE_VIANDE_CONSERVEE = 64;
	public static final int ITEM_TYPE_QUEUE = 65;
	public static final int ITEM_TYPE_METARIA = 66;
	public static final int ITEM_TYPE_LEGUME = 68;
	public static final int ITEM_TYPE_VIANDE_COMESTIBLE = 69;
	public static final int ITEM_TYPE_TEINTURE = 70;
	public static final int ITEM_TYPE_EQUIP_ALCHIMIE = 71;
	public static final int ITEM_TYPE_OEUF_FAMILIER = 72;
	public static final int ITEM_TYPE_MAITRISE = 73;
	public static final int ITEM_TYPE_FEE_ARTIFICE = 74;
	public static final int ITEM_TYPE_PARCHEMIN_SORT = 75;
	public static final int ITEM_TYPE_PARCHEMIN_CARAC = 76;
	public static final int ITEM_TYPE_CERTIFICAT_CHANIL = 77;
	public static final int ITEM_TYPE_RUNE_FORGEMAGIE = 78;
	public static final int ITEM_TYPE_BOISSON = 79;
	public static final int ITEM_TYPE_OBJET_MISSION = 80;
	public static final int ITEM_TYPE_SAC_DOS = 81;
	public static final int ITEM_TYPE_BOUCLIER = 82;
	public static final int ITEM_TYPE_PIERRE_AME = 83;
	public static final int ITEM_TYPE_CLEFS = 84;
	public static final int ITEM_TYPE_PIERRE_AME_PLEINE = 85;
	public static final int ITEM_TYPE_POPO_OUBLI_PERCEP = 86;
	public static final int ITEM_TYPE_PARCHO_RECHERCHE = 87;
	public static final int ITEM_TYPE_PIERRE_MAGIQUE = 88;
	public static final int ITEM_TYPE_CADEAUX = 89;
	public static final int ITEM_TYPE_FANTOME_FAMILIER = 90;
	public static final int ITEM_TYPE_DRAGODINDE = 91;
	public static final int ITEM_TYPE_BOUFTOU = 92;
	public static final int ITEM_TYPE_OBJET_ELEVAGE = 93;
	public static final int ITEM_TYPE_OBJET_UTILISABLE = 94;
	public static final int ITEM_TYPE_PLANCHE = 95;
	public static final int ITEM_TYPE_ECORCE = 96;
	public static final int ITEM_TYPE_CERTIF_MONTURE = 97;
	public static final int ITEM_TYPE_RACINE = 98;
	public static final int ITEM_TYPE_FILET_CAPTURE = 99;
	public static final int ITEM_TYPE_SAC_RESSOURCE = 100;
	public static final int ITEM_TYPE_ARBALETE = 102;
	public static final int ITEM_TYPE_PATTE = 103;
	public static final int ITEM_TYPE_AILE = 104;
	public static final int ITEM_TYPE_OEUF = 105;
	public static final int ITEM_TYPE_OREILLE = 106;
	public static final int ITEM_TYPE_CARAPACE = 107;
	public static final int ITEM_TYPE_BOURGEON = 108;
	public static final int ITEM_TYPE_OEIL = 109;
	public static final int ITEM_TYPE_GELEE = 110;
	public static final int ITEM_TYPE_COQUILLE = 111;
	public static final int ITEM_TYPE_PRISME = 112;
	public static final int ITEM_TYPE_OBJET_VIVANT = 113;
	public static final int ITEM_TYPE_ARME_MAGIQUE = 114;
	public static final int ITEM_TYPE_FRAGM_AME_SHUSHU = 115;
	public static final int ITEM_TYPE_POTION_FAMILIER = 116;
	public static final int ALIGNEMENT_NEUTRE = -1;
	public static final int ALIGNEMENT_BONTARIEN = 1;
	public static final int ALIGNEMENT_BRAKMARIEN = 2;
	public static final int ALIGNEMENT_MERCENAIRE = 3;
	public static final int ELEMENT_NULL = -1;
	public static final int ELEMENT_NEUTRE = 0;
	public static final int ELEMENT_TERRE = 1;
	public static final int ELEMENT_EAU = 2;
	public static final int ELEMENT_FEU = 3;
	public static final int ELEMENT_AIR = 4;
	public static final int CLASS_FECA = 1;
	public static final int CLASS_OSAMODAS = 2;
	public static final int CLASS_ENUTROF = 3;
	public static final int CLASS_SRAM = 4;
	public static final int CLASS_XELOR = 5;
	public static final int CLASS_ECAFLIP = 6;
	public static final int CLASS_ENIRIPSA = 7;
	public static final int CLASS_IOP = 8;
	public static final int CLASS_CRA = 9;
	public static final int CLASS_SADIDA = 10;
	public static final int CLASS_SACRIEUR = 11;
	public static final int CLASS_PANDAWA = 12;
	public static final int SEX_MALE = 0;
	public static final int SEX_FEMALE = 1;
	public static final int MAX_EFFECTS_ID = 1500;
	public static final int[] BEGIN_TURN_BUFF;
	public static final int[] ARMES_EFFECT_IDS;
	public static final int[] NO_BOOST_CC_IDS;
	public static final int[] STATIC_INVOCATIONS;
	public static final int[][] STATE_REQUIRED;
	public static final int[] ON_HIT_BUFFS;
	public static final int STATS_ADD_PM2 = 78;
	public static final int STATS_REM_PA = 101;
	public static final int STATS_ADD_VIE = 110;
	public static final int STATS_ADD_PA = 111;
	public static final int STATS_MULTIPLY_DOMMAGE = 114;
	public static final int STATS_ADD_CC = 115;
	public static final int STATS_REM_PO = 116;
	public static final int STATS_ADD_PO = 117;
	public static final int STATS_ADD_FORC = 118;
	public static final int STATS_ADD_AGIL = 119;
	public static final int STATS_ADD_PA2 = 120;
	public static final int STATS_ADD_DOMA = 112;
	public static final int STATS_ADD_EC = 122;
	public static final int STATS_ADD_CHAN = 123;
	public static final int STATS_ADD_SAGE = 124;
	public static final int STATS_ADD_VITA = 125;
	public static final int STATS_ADD_INTE = 126;
	public static final int STATS_REM_PM = 127;
	public static final int STATS_ADD_PM = 128;
	public static final int STATS_ADD_PERDOM = 138;
	public static final int STATS_ADD_PDOM = 142;
	public static final int STATS_REM_DOMA = 145;
	public static final int STATS_REM_CHAN = 152;
	public static final int STATS_REM_VITA = 153;
	public static final int STATS_REM_AGIL = 154;
	public static final int STATS_REM_INTE = 155;
	public static final int STATS_REM_SAGE = 156;
	public static final int STATS_REM_FORC = 157;
	public static final int STATS_ADD_PODS = 158;
	public static final int STATS_REM_PODS = 159;
	public static final int STATS_ADD_AFLEE = 160;
	public static final int STATS_ADD_MFLEE = 161;
	public static final int STATS_REM_AFLEE = 162;
	public static final int STATS_REM_MFLEE = 163;
	public static final int STATS_ADD_MAITRISE = 165;
	public static final int STATS_REM_PA2 = 168;
	public static final int STATS_REM_PM2 = 169;
	public static final int STATS_REM_CC = 171;
	public static final int STATS_ADD_INIT = 174;
	public static final int STATS_REM_INIT = 175;
	public static final int STATS_ADD_PROS = 176;
	public static final int STATS_REM_PROS = 177;
	public static final int STATS_ADD_SOIN = 178;
	public static final int STATS_REM_SOIN = 179;
	public static final int STATS_CREATURE = 182;
	public static final int STATS_ADD_RP_TER = 210;
	public static final int STATS_ADD_RP_EAU = 211;
	public static final int STATS_ADD_RP_AIR = 212;
	public static final int STATS_ADD_RP_FEU = 213;
	public static final int STATS_ADD_RP_NEU = 214;
	public static final int STATS_REM_RP_TER = 215;
	public static final int STATS_REM_RP_EAU = 216;
	public static final int STATS_REM_RP_AIR = 217;
	public static final int STATS_REM_RP_FEU = 218;
	public static final int STATS_REM_RP_NEU = 219;
	public static final int STATS_RETDOM = 220;
	public static final int STATS_TRAPDOM = 225;
	public static final int STATS_TRAPPER = 226;
	public static final int STATS_ADD_R_FEU = 240;
	public static final int STATS_ADD_R_NEU = 241;
	public static final int STATS_ADD_R_TER = 242;
	public static final int STATS_ADD_R_EAU = 243;
	public static final int STATS_ADD_R_AIR = 244;
	public static final int STATS_REM_R_FEU = 245;
	public static final int STATS_REM_R_NEU = 246;
	public static final int STATS_REM_R_TER = 247;
	public static final int STATS_REM_R_EAU = 248;
	public static final int STATS_REM_R_AIR = 249;
	public static final int STATS_ADD_RP_PVP_TER = 250;
	public static final int STATS_ADD_RP_PVP_EAU = 251;
	public static final int STATS_ADD_RP_PVP_AIR = 252;
	public static final int STATS_ADD_RP_PVP_FEU = 253;
	public static final int STATS_ADD_RP_PVP_NEU = 254;
	public static final int STATS_REM_RP_PVP_TER = 255;
	public static final int STATS_REM_RP_PVP_EAU = 256;
	public static final int STATS_REM_RP_PVP_AIR = 257;
	public static final int STATS_REM_RP_PVP_FEU = 258;
	public static final int STATS_REM_RP_PVP_NEU = 259;
	public static final int STATS_ADD_R_PVP_TER = 260;
	public static final int STATS_ADD_R_PVP_EAU = 261;
	public static final int STATS_ADD_R_PVP_AIR = 262;
	public static final int STATS_ADD_R_PVP_FEU = 263;
	public static final int STATS_ADD_R_PVP_NEU = 264;
	public static final int EFFECT_PASS_TURN = 140;
	public static final int CAPTURE_MONSTRE = 623;
	public static final int STATS_PETS_PDV = 800;
	public static final int STATS_PETS_POIDS = 806;
	public static final int STATS_PETS_REPAS = 807;
	public static final int STATS_PETS_DATE = 808;
	public static final int STATS_PETS_EPO = 940;
	public static final int STATS_PETS_SOUL = 717;
	public static final int STATS_RESIST = 812;
	public static final int STATS_OE_MATU = 948;
	public static final int STATS_OE_FORENC = 936;
	public static final int STATS_TURN = 811;
	public static final int STATS_NAME_RUNE = 985;
	public static final int STATS_NAME_TRAQUE = 989;
	public static final int STATS_DATE = 805;
	public static final int STATS_NIVEAU = 962;
	public static final int STATS_NAME_DJ = 814;
	public static final int STATS_SIGNATUREFM = 985;
	public static final int STATS_SIGNATURE = 988;
	public static final int ERR_STATS_XP = 1000;
	public static String HUNT_DETAILS_DOC;
	public static String HUNT_FRAKACIA_DOC;
	public static String HUNT_AERMYNE_DOC;
	public static String HUNT_MARZWEL_DOC;
	public static String HUNT_BRUMEN_DOC;
	public static String HUNT_MUSHA_DOC;
	public static String HUNT_OGIVOL_DOC;
	public static String HUNT_PADGREF_DOC;
	public static String HUNT_QILBIL_DOC;
	public static String HUNT_ROK_DOC;
	public static String HUNT_ZATOISHWAN_DOC;
	public static String HUNT_LETHALINE_DOC;
	public static String HUNT_FOUDUGLEN_DOC;
	public static String[][] HUNTING_QUESTS;

	static {
		Constant.ZAAPI = new TreeMap<Integer, String>();
		Constant.ZAAPS = new TreeMap<Integer, Integer>();
		Constant.G_BOOST = 2;
		Constant.G_RIGHT = 4;
		Constant.G_INVITE = 8;
		Constant.G_BAN = 16;
		Constant.G_ALLXP = 32;
		Constant.G_HISXP = 256;
		Constant.G_RANK = 64;
		Constant.G_POSPERCO = 128;
		Constant.G_COLLPERCO = 512;
		Constant.G_USEENCLOS = 4096;
		Constant.G_AMENCLOS = 8192;
		Constant.G_OTHDINDE = 16384;
		Constant.H_GBLASON = 2;
		Constant.H_OBLASON = 4;
		Constant.H_GNOCODE = 8;
		Constant.H_OCANTOPEN = 16;
		Constant.C_GNOCODE = 32;
		Constant.C_OCANTOPEN = 64;
		Constant.H_GREPOS = 256;
		Constant.H_GTELE = 128;
		BEGIN_TURN_BUFF = new int[] { 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 108 };
		ARMES_EFFECT_IDS = new int[] { 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101 };
		NO_BOOST_CC_IDS = new int[] { 101 };
		STATIC_INVOCATIONS = new int[] { 282, 556, 2750, 7000 };
		STATE_REQUIRED = new int[][] { { 699, 1 }, { 690, 1 } };
		ON_HIT_BUFFS = new int[] { 9, 79, 107, 788 };
		Constant.HUNT_DETAILS_DOC = "71_0706251229";
		Constant.HUNT_FRAKACIA_DOC = "63_0706251124";
		Constant.HUNT_AERMYNE_DOC = "100_0706251214";
		Constant.HUNT_MARZWEL_DOC = "96_0706251201";
		Constant.HUNT_BRUMEN_DOC = "68_0706251126";
		Constant.HUNT_MUSHA_DOC = "94_0706251138";
		Constant.HUNT_OGIVOL_DOC = "69_0706251058";
		Constant.HUNT_PADGREF_DOC = "61_0802081743";
		Constant.HUNT_QILBIL_DOC = "67_0706251223";
		Constant.HUNT_ROK_DOC = "93_0706251135";
		Constant.HUNT_ZATOISHWAN_DOC = "98_0706251211";
		Constant.HUNT_LETHALINE_DOC = "65_0706251123";
		Constant.HUNT_FOUDUGLEN_DOC = "70_0706251122";
		Constant.HUNTING_QUESTS = new String[][] { { "1988", "234", Constant.HUNT_DETAILS_DOC, "-1", "-1", "-1", "-1" },
				{ "1986", "161", Constant.HUNT_LETHALINE_DOC, "-1", "-1", "-1", "-1" },
				{ "1985", "119", Constant.HUNT_MARZWEL_DOC, "554", "7353", "117", "2552" },
				{ "1986", "120", Constant.HUNT_PADGREF_DOC, "459", "6870", "29", "2108" },
				{ "1985", "149", Constant.HUNT_FRAKACIA_DOC, "460", "6871", "30", "2109" },
				{ "1986", "150", Constant.HUNT_QILBIL_DOC, "481", "6873", "32", "2111" },
				{ "1986", "179", Constant.HUNT_BRUMEN_DOC, "464", "6874", "33", "2112" },
				{ "1986", "180", Constant.HUNT_OGIVOL_DOC, "462", "6876", "35", "2114" },
				{ "1985", "269", Constant.HUNT_MUSHA_DOC, "552", "7352", "116", "2551" },
				{ "1986", "270", Constant.HUNT_FOUDUGLEN_DOC, "463", "6875", "34", "2113" },
				{ "1985", "299", Constant.HUNT_ROK_DOC, "550", "7351", "115", "2550" },
				{ "1986", "300", Constant.HUNT_AERMYNE_DOC, "446", "7350", "119", "2554" },
				{ "1985", "329", Constant.HUNT_ZATOISHWAN_DOC, "555", "7354", "118", "2553" } };
	}

	public static int getQuestByMobSkin(final int mobSkin) {
		for (int v = 0; v < Constant.HUNTING_QUESTS.length; ++v) {
			if (World.getMonstre(Integer.parseInt(Constant.HUNTING_QUESTS[v][3])) != null
					&& World.getMonstre(Integer.parseInt(Constant.HUNTING_QUESTS[v][3])).getGfxId() == mobSkin) {
				return Integer.parseInt(Constant.HUNTING_QUESTS[v][5]);
			}
		}
		return -1;
	}

	public static int getSkinByHuntMob(final int mobId) {
		for (int v = 0; v < Constant.HUNTING_QUESTS.length; ++v) {
			if (Integer.parseInt(Constant.HUNTING_QUESTS[v][3]) == mobId) {
				return World.getMonstre(mobId).getGfxId();
			}
		}
		return -1;
	}

	public static int getItemByHuntMob(final int mobId) {
		for (int v = 0; v < Constant.HUNTING_QUESTS.length; ++v) {
			if (Integer.parseInt(Constant.HUNTING_QUESTS[v][3]) == mobId) {
				return Integer.parseInt(Constant.HUNTING_QUESTS[v][4]);
			}
		}
		return -1;
	}

	public static int getItemByMobSkin(final int mobSkin) {
		for (int v = 0; v < Constant.HUNTING_QUESTS.length; ++v) {
			if (World.getMonstre(Integer.parseInt(Constant.HUNTING_QUESTS[v][3])) != null
					&& World.getMonstre(Integer.parseInt(Constant.HUNTING_QUESTS[v][3])).getGfxId() == mobSkin) {
				return Integer.parseInt(Constant.HUNTING_QUESTS[v][4]);
			}
		}
		return -1;
	}

	public static String getDocNameByBornePos(final int borneId, final int cellid) {
		for (int v = 0; v < Constant.HUNTING_QUESTS.length; ++v) {
			if (Integer.parseInt(Constant.HUNTING_QUESTS[v][0]) == borneId
					&& Integer.parseInt(Constant.HUNTING_QUESTS[v][1]) == cellid) {
				return Constant.HUNTING_QUESTS[v][2];
			}
		}
		return "";
	}

	public static short getClassStatueMap(final int classID) {
		final short pos = 10298;
		switch (classID) {
		case 1: {
			return 7398;
		}
		case 2: {
			return 7545;
		}
		case 3: {
			return 7442;
		}
		case 4: {
			return 7392;
		}
		case 5: {
			return 7332;
		}
		case 6: {
			return 7446;
		}
		case 7: {
			return 7361;
		}
		case 8: {
			return 7427;
		}
		case 9: {
			return 7378;
		}
		case 10: {
			return 7395;
		}
		case 11: {
			return 7336;
		}
		case 12: {
			return 8035;
		}
		case 13: {
			return 7427;
		}
		default: {
			return pos;
		}
		}
	}

	public static int getClassStatueCell(final int classID) {
		final int pos = 314;
		switch (classID) {
		case 1: {
			return 299;
		}
		case 2: {
			return 311;
		}
		case 3: {
			return 255;
		}
		case 4: {
			return 282;
		}
		case 5: {
			return 326;
		}
		case 6: {
			return 300;
		}
		case 7: {
			return 207;
		}
		case 8: {
			return 282;
		}
		case 9: {
			return 368;
		}
		case 10: {
			return 370;
		}
		case 11: {
			return 197;
		}
		case 12: {
			return 384;
		}
		case 13: {
			return 282;
		}
		default: {
			return pos;
		}
		}
	}

	public static short getStartMap(final int classID) {
		short pos = 10298;
		switch (classID) {
		case 1: {
			pos = 10300;
			break;
		}
		case 2: {
			pos = 10284;
			break;
		}
		case 3: {
			pos = 10299;
			break;
		}
		case 4: {
			pos = 10285;
			break;
		}
		case 5: {
			pos = 10298;
			break;
		}
		case 6: {
			pos = 10276;
			break;
		}
		case 7: {
			pos = 10283;
			break;
		}
		case 8: {
			pos = 10294;
			break;
		}
		case 9: {
			pos = 10292;
			break;
		}
		case 10: {
			pos = 10279;
			break;
		}
		case 11: {
			pos = 10296;
			break;
		}
		case 12: {
			pos = 10289;
			break;
		}
		}
		return pos;
	}

	public static int getStartCell(final int classID) {
		int pos = 314;
		switch (classID) {
		case 1: {
			pos = 323;
			break;
		}
		case 2: {
			pos = 372;
			break;
		}
		case 3: {
			pos = 271;
			break;
		}
		case 4: {
			pos = 263;
			break;
		}
		case 5: {
			pos = 300;
			break;
		}
		case 6: {
			pos = 296;
			break;
		}
		case 7: {
			pos = 299;
			break;
		}
		case 8: {
			pos = 280;
			break;
		}
		case 9: {
			pos = 284;
			break;
		}
		case 10: {
			pos = 254;
			break;
		}
		case 11: {
			pos = 243;
			break;
		}
		case 12: {
			pos = 236;
			break;
		}
		}
		return pos;
	}

	public static TreeMap<Integer, Character> getStartSortsPlaces(final int classID) {
		final TreeMap<Integer, Character> start = new TreeMap<Integer, Character>();
		switch (classID) {
		case 1: {
			start.put(3, 'b');
			start.put(6, 'c');
			start.put(17, 'd');
			break;
		}
		case 4: {
			start.put(61, 'b');
			start.put(72, 'c');
			start.put(65, 'd');
			break;
		}
		case 7: {
			start.put(125, 'b');
			start.put(128, 'c');
			start.put(121, 'd');
			break;
		}
		case 6: {
			start.put(102, 'b');
			start.put(103, 'c');
			start.put(105, 'd');
			break;
		}
		case 9: {
			start.put(161, 'b');
			start.put(169, 'c');
			start.put(164, 'd');
			break;
		}
		case 8: {
			start.put(143, 'b');
			start.put(141, 'c');
			start.put(142, 'd');
			break;
		}
		case 10: {
			start.put(183, 'b');
			start.put(200, 'c');
			start.put(193, 'd');
			break;
		}
		case 2: {
			start.put(34, 'b');
			start.put(21, 'c');
			start.put(23, 'd');
			break;
		}
		case 5: {
			start.put(82, 'b');
			start.put(81, 'c');
			start.put(83, 'd');
			break;
		}
		case 12: {
			start.put(686, 'b');
			start.put(692, 'c');
			start.put(687, 'd');
			break;
		}
		case 3: {
			start.put(51, 'b');
			start.put(43, 'c');
			start.put(41, 'd');
			break;
		}
		case 11: {
			start.put(432, 'b');
			start.put(431, 'c');
			start.put(434, 'd');
			break;
		}
		}
		return start;
	}

	public static TreeMap<Integer, Spell.SortStats> getStartSorts(final int classID) {
		final TreeMap<Integer, Spell.SortStats> start = new TreeMap<Integer, Spell.SortStats>();
		switch (classID) {
		case 1: {
			start.put(3, World.getSort(3).getStatsByLevel(1));
			start.put(6, World.getSort(6).getStatsByLevel(1));
			start.put(17, World.getSort(17).getStatsByLevel(1));
			break;
		}
		case 4: {
			start.put(61, World.getSort(61).getStatsByLevel(1));
			start.put(72, World.getSort(72).getStatsByLevel(1));
			start.put(65, World.getSort(65).getStatsByLevel(1));
			break;
		}
		case 7: {
			start.put(125, World.getSort(125).getStatsByLevel(1));
			start.put(128, World.getSort(128).getStatsByLevel(1));
			start.put(121, World.getSort(121).getStatsByLevel(1));
			break;
		}
		case 6: {
			start.put(102, World.getSort(102).getStatsByLevel(1));
			start.put(103, World.getSort(103).getStatsByLevel(1));
			start.put(105, World.getSort(105).getStatsByLevel(1));
			break;
		}
		case 9: {
			start.put(161, World.getSort(161).getStatsByLevel(1));
			start.put(169, World.getSort(169).getStatsByLevel(1));
			start.put(164, World.getSort(164).getStatsByLevel(1));
			break;
		}
		case 8: {
			start.put(143, World.getSort(143).getStatsByLevel(1));
			start.put(141, World.getSort(141).getStatsByLevel(1));
			start.put(142, World.getSort(142).getStatsByLevel(1));
			break;
		}
		case 10: {
			start.put(183, World.getSort(183).getStatsByLevel(1));
			start.put(200, World.getSort(200).getStatsByLevel(1));
			start.put(193, World.getSort(193).getStatsByLevel(1));
			break;
		}
		case 2: {
			start.put(34, World.getSort(34).getStatsByLevel(1));
			start.put(21, World.getSort(21).getStatsByLevel(1));
			start.put(23, World.getSort(23).getStatsByLevel(1));
			break;
		}
		case 5: {
			start.put(82, World.getSort(82).getStatsByLevel(1));
			start.put(81, World.getSort(81).getStatsByLevel(1));
			start.put(83, World.getSort(83).getStatsByLevel(1));
			break;
		}
		case 12: {
			start.put(686, World.getSort(686).getStatsByLevel(1));
			start.put(692, World.getSort(692).getStatsByLevel(1));
			start.put(687, World.getSort(687).getStatsByLevel(1));
			break;
		}
		case 3: {
			start.put(51, World.getSort(51).getStatsByLevel(1));
			start.put(43, World.getSort(43).getStatsByLevel(1));
			start.put(41, World.getSort(41).getStatsByLevel(1));
			break;
		}
		case 11: {
			start.put(432, World.getSort(432).getStatsByLevel(1));
			start.put(431, World.getSort(431).getStatsByLevel(1));
			start.put(434, World.getSort(434).getStatsByLevel(1));
			break;
		}
		}
		return start;
	}

	public static int getReqPtsToBoostStatsByClass(final int classID, final int statID, final int val) {
		Label_1774: {
			switch (statID) {
			case 11: {
				return 1;
			}
			case 12: {
				return 3;
			}
			case 10: {
				switch (classID) {
				case 11: {
					return 3;
				}
				case 1: {
					if (val < 50) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 250) {
						return 4;
					}
					return 5;
				}
				case 5: {
					if (val < 50) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 250) {
						return 4;
					}
					return 5;
				}
				case 4: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 2: {
					if (val < 50) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 250) {
						return 4;
					}
					return 5;
				}
				case 7: {
					if (val < 50) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 250) {
						return 4;
					}
					return 5;
				}
				case 12: {
					if (val < 50) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					return 3;
				}
				case 10: {
					if (val < 50) {
						return 1;
					}
					if (val < 250) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 9: {
					if (val < 50) {
						return 1;
					}
					if (val < 150) {
						return 2;
					}
					if (val < 250) {
						return 3;
					}
					if (val < 350) {
						return 4;
					}
					return 5;
				}
				case 3: {
					if (val < 50) {
						return 1;
					}
					if (val < 150) {
						return 2;
					}
					if (val < 250) {
						return 3;
					}
					if (val < 350) {
						return 4;
					}
					return 5;
				}
				case 6: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 8: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				default: {
					break Label_1774;
				}
				}
			}
			case 13: {
				switch (classID) {
				case 1: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 5: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 11: {
					return 3;
				}
				case 4: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 10: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 12: {
					if (val < 50) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					return 3;
				}
				case 8: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 3: {
					if (val < 100) {
						return 1;
					}
					if (val < 150) {
						return 2;
					}
					if (val < 230) {
						return 3;
					}
					if (val < 330) {
						return 4;
					}
					return 5;
				}
				case 2: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 6: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 7: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 9: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				default: {
					break Label_1774;
				}
				}
			}
			case 14: {
				switch (classID) {
				case 1: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 5: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 11: {
					return 3;
				}
				case 4: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 10: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 12: {
					if (val < 50) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					return 3;
				}
				case 7: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 8: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 3: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 6: {
					if (val < 50) {
						return 1;
					}
					if (val < 100) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 200) {
						return 4;
					}
					return 5;
				}
				case 9: {
					if (val < 50) {
						return 1;
					}
					if (val < 100) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 200) {
						return 4;
					}
					return 5;
				}
				case 2: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				default: {
					break Label_1774;
				}
				}
			}
			case 15: {
				switch (classID) {
				case 5: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 1: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 11: {
					return 3;
				}
				case 4: {
					if (val < 50) {
						return 2;
					}
					if (val < 150) {
						return 3;
					}
					if (val < 250) {
						return 4;
					}
					return 5;
				}
				case 10: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 3: {
					if (val < 20) {
						return 1;
					}
					if (val < 60) {
						return 2;
					}
					if (val < 100) {
						return 3;
					}
					if (val < 140) {
						return 4;
					}
					return 5;
				}
				case 12: {
					if (val < 50) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					return 3;
				}
				case 8: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				case 7: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 9: {
					if (val < 50) {
						return 1;
					}
					if (val < 150) {
						return 2;
					}
					if (val < 250) {
						return 3;
					}
					if (val < 350) {
						return 4;
					}
					return 5;
				}
				case 2: {
					if (val < 100) {
						return 1;
					}
					if (val < 200) {
						return 2;
					}
					if (val < 300) {
						return 3;
					}
					if (val < 400) {
						return 4;
					}
					return 5;
				}
				case 6: {
					if (val < 20) {
						return 1;
					}
					if (val < 40) {
						return 2;
					}
					if (val < 60) {
						return 3;
					}
					if (val < 80) {
						return 4;
					}
					return 5;
				}
				default: {
					break Label_1774;
				}
				}
			}
			}
		}
		return 5;
	}

	public static void onLevelUpSpells(final Player perso, final int lvl) {
		switch (perso.getClasse()) {
		case 1: {
			if (lvl == 3) {
				perso.learnSpell(4, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(2, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(1, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(9, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(18, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(20, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(14, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(19, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(5, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(16, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(8, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(12, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(11, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(10, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(7, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(15, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(13, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1901, 1, true, false, false);
				break;
			}
			break;
		}
		case 2: {
			if (lvl == 3) {
				perso.learnSpell(26, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(22, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(35, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(28, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(37, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(30, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(27, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(24, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(33, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(25, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(38, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(36, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(32, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(29, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(39, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(40, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(31, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1902, 1, true, false, false);
				break;
			}
			break;
		}
		case 3: {
			if (lvl == 3) {
				perso.learnSpell(49, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(42, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(47, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(48, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(45, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(53, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(46, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(52, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(44, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(50, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(54, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(55, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(56, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(58, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(59, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(57, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(60, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1903, 1, true, false, false);
				break;
			}
			break;
		}
		case 4: {
			if (lvl == 3) {
				perso.learnSpell(66, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(68, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(63, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(74, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(64, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(79, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(78, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(71, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(62, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(69, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(77, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(73, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(67, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(70, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(75, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(76, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(80, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1904, 1, true, false, false);
				break;
			}
			break;
		}
		case 5: {
			if (lvl == 3) {
				perso.learnSpell(84, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(100, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(92, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(88, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(93, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(85, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(96, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(98, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(86, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(89, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(90, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(87, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(94, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(99, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(95, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(91, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(97, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1905, 1, true, false, false);
				break;
			}
			break;
		}
		case 6: {
			if (lvl == 3) {
				perso.learnSpell(109, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(113, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(111, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(104, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(119, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(101, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(107, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(116, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(106, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(117, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(108, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(115, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(118, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(110, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(112, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(114, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(120, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1906, 1, true, false, false);
				break;
			}
			break;
		}
		case 7: {
			if (lvl == 3) {
				perso.learnSpell(124, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(122, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(126, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(127, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(123, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(130, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(131, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(132, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(133, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(134, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(135, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(129, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(136, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(137, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(138, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(139, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(140, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1907, 1, true, false, false);
				break;
			}
			break;
		}
		case 8: {
			if (lvl == 3) {
				perso.learnSpell(144, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(145, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(146, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(147, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(148, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(154, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(150, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(151, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(155, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(152, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(153, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(149, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(156, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(157, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(158, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(160, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(159, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1908, 1, true, false, false);
				break;
			}
			break;
		}
		case 9: {
			if (lvl == 3) {
				perso.learnSpell(163, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(165, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(172, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(167, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(168, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(162, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(170, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(171, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(166, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(173, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(174, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(176, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(175, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(178, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(177, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(179, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(180, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1909, 1, true, false, false);
				break;
			}
			break;
		}
		case 10: {
			if (lvl == 3) {
				perso.learnSpell(198, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(195, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(182, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(192, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(197, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(189, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(181, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(199, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(191, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(186, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(196, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(190, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(194, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(185, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(184, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(188, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(187, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1910, 1, true, false, false);
				break;
			}
			break;
		}
		case 11: {
			if (lvl == 3) {
				perso.learnSpell(444, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(449, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(436, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(437, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(439, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(433, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(443, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(440, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(442, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(441, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(445, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(438, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(446, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(447, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(448, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(435, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(450, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1911, 1, true, false, false);
				break;
			}
			break;
		}
		case 12: {
			if (lvl == 3) {
				perso.learnSpell(689, 1, true, false, false);
			}
			if (lvl == 6) {
				perso.learnSpell(690, 1, true, false, false);
			}
			if (lvl == 9) {
				perso.learnSpell(691, 1, true, false, false);
			}
			if (lvl == 13) {
				perso.learnSpell(688, 1, true, false, false);
			}
			if (lvl == 17) {
				perso.learnSpell(693, 1, true, false, false);
			}
			if (lvl == 21) {
				perso.learnSpell(694, 1, true, false, false);
			}
			if (lvl == 26) {
				perso.learnSpell(695, 1, true, false, false);
			}
			if (lvl == 31) {
				perso.learnSpell(696, 1, true, false, false);
			}
			if (lvl == 36) {
				perso.learnSpell(697, 1, true, false, false);
			}
			if (lvl == 42) {
				perso.learnSpell(698, 1, true, false, false);
			}
			if (lvl == 48) {
				perso.learnSpell(699, 1, true, false, false);
			}
			if (lvl == 54) {
				perso.learnSpell(700, 1, true, false, false);
			}
			if (lvl == 60) {
				perso.learnSpell(701, 1, true, false, false);
			}
			if (lvl == 70) {
				perso.learnSpell(702, 1, true, false, false);
			}
			if (lvl == 80) {
				perso.learnSpell(703, 1, true, false, false);
			}
			if (lvl == 90) {
				perso.learnSpell(704, 1, true, false, false);
			}
			if (lvl == 100) {
				perso.learnSpell(705, 1, true, false, false);
			}
			if (lvl == 200) {
				perso.learnSpell(1912, 1, true, false, false);
				break;
			}
			break;
		}
		}
	}

	public static int getGlyphColor(final int spell) {
		switch (spell) {
		case 10:
		case 2033: {
			return 4;
		}
		case 12:
		case 2034: {
			return 3;
		}
		case 13:
		case 2035: {
			return 6;
		}
		case 15:
		case 2036: {
			return 5;
		}
		case 17:
		case 2037: {
			return 2;
		}
		default: {
			return 4;
		}
		}
	}

	public static int getTrapsColor(final int spell) {
		switch (spell) {
		case 65: {
			return 7;
		}
		case 69: {
			return 10;
		}
		case 71:
		case 2068: {
			return 9;
		}
		case 73: {
			return 12;
		}
		case 77:
		case 2071: {
			return 11;
		}
		case 79:
		case 2072: {
			return 8;
		}
		case 80: {
			return 13;
		}
		default: {
			return 7;
		}
		}
	}

	public static Stats getMountStats(final int color, final int lvl) {
		final Stats stats = new Stats();
		switch (color) {
		case 3: {
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(119, (int) (lvl / 1.25));
			break;
		}
		case 10: {
			stats.addOneStat(125, lvl);
			break;
		}
		case 20: {
			stats.addOneStat(174, lvl * 10);
			break;
		}
		case 18: {
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(124, (int) (lvl / 2.5));
			break;
		}
		case 38: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(125, lvl);
			stats.addOneStat(182, lvl / 50);
			break;
		}
		case 46: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(124, lvl / 4);
			break;
		}
		case 33: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 17: {
			stats.addOneStat(123, (int) (lvl / 1.25));
			stats.addOneStat(125, lvl / 2);
			break;
		}
		case 62: {
			stats.addOneStat(125, (int) (lvl * 1.5));
			stats.addOneStat(123, (int) (lvl / 1.65));
			break;
		}
		case 12: {
			stats.addOneStat(125, (int) (lvl * 1.5));
			stats.addOneStat(119, (int) (lvl / 1.65));
			break;
		}
		case 36: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(123, (int) (lvl / 1.65));
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 19: {
			stats.addOneStat(118, (int) (lvl / 1.25));
			stats.addOneStat(125, lvl / 2);
			break;
		}
		case 22: {
			stats.addOneStat(126, (int) (lvl / 1.25));
			stats.addOneStat(125, lvl / 2);
			break;
		}
		case 48: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(126, (int) (lvl / 1.65));
			break;
		}
		case 65: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(123, lvl / 2);
			stats.addOneStat(118, lvl / 2);
			break;
		}
		case 67: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(138, lvl / 2);
			stats.addOneStat(126, lvl / 2);
			break;
		}
		case 54: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(118, lvl / 2);
			stats.addOneStat(119, lvl / 2);
			break;
		}
		case 53: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(119, lvl / 2);
			stats.addOneStat(126, lvl / 2);
			break;
		}
		case 76: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(126, lvl / 2);
			stats.addOneStat(118, lvl / 2);
			break;
		}
		case 37: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(119, (int) (lvl / 1.65));
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 44: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(123, (int) (lvl / 1.65));
			break;
		}
		case 42: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(119, (int) (lvl / 1.65));
			break;
		}
		case 51: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(123, lvl / 2);
			stats.addOneStat(119, lvl / 2);
			break;
		}
		case 71: {
			stats.addOneStat(125, (int) (lvl * 1.5));
			stats.addOneStat(118, (int) (lvl / 1.65));
			break;
		}
		case 70: {
			stats.addOneStat(125, (int) (lvl * 1.5));
			stats.addOneStat(126, (int) (lvl / 1.65));
			break;
		}
		case 41: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(118, (int) (lvl / 1.65));
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 40: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(126, (int) (lvl / 1.65));
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 49: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(118, (int) (lvl / 1.65));
			break;
		}
		case 16: {
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(138, lvl / 2);
			break;
		}
		case 15: {
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(176, (int) (lvl / 1.25));
			break;
		}
		case 11: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(138, (int) (lvl / 2.5));
			break;
		}
		case 69: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(176, (int) (lvl / 2.5));
			break;
		}
		case 39: {
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(125, lvl / 2);
			stats.addOneStat(176, (int) (lvl / 2.5));
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 45: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(138, (int) (lvl / 2.5));
			stats.addOneStat(124, lvl / 4);
			break;
		}
		case 47: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(176, (int) (lvl / 2.5));
			stats.addOneStat(124, lvl / 4);
			break;
		}
		case 61: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(123, (int) (lvl / 2.5));
			stats.addOneStat(138, (int) (lvl / 2.5));
			break;
		}
		case 63: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(123, (int) (lvl / 1.65));
			stats.addOneStat(176, (int) (lvl / 2.5));
			break;
		}
		case 9: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(119, (int) (lvl / 2.5));
			stats.addOneStat(138, (int) (lvl / 2.5));
			break;
		}
		case 52: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(119, (int) (lvl / 1.65));
			stats.addOneStat(176, (int) (lvl / 2.5));
			break;
		}
		case 68: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(118, (int) (lvl / 1.65));
			stats.addOneStat(138, (int) (lvl / 2.5));
			break;
		}
		case 73: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(118, (int) (lvl / 1.65));
			stats.addOneStat(176, (int) (lvl / 2.5));
			break;
		}
		case 72: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(126, (int) (lvl / 1.65));
			stats.addOneStat(138, (int) (lvl / 2.5));
			break;
		}
		case 66: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(138, (int) (lvl / 2.5));
			stats.addOneStat(176, (int) (lvl / 2.5));
			break;
		}
		case 21: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 23: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(117, lvl / 50);
			break;
		}
		case 57: {
			stats.addOneStat(125, lvl * 3);
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 84: {
			stats.addOneStat(125, lvl * 3);
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 35: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(128, lvl / 100);
			stats.addOneStat(182, lvl / 100);
			stats.addOneStat(174, lvl * 5);
			break;
		}
		case 77: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(174, lvl * 5);
			stats.addOneStat(117, lvl / 100);
			stats.addOneStat(182, lvl / 100);
			break;
		}
		case 43: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 78: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(124, lvl / 4);
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 55: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(123, (int) (lvl / 3.33));
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 82: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(123, (int) (lvl / 1.65));
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 50: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(119, (int) (lvl / 3.33));
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 79: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(119, (int) (lvl / 1.65));
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 60: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(118, (int) (lvl / 3.33));
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 87: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(118, (int) (lvl / 1.65));
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 59: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(126, (int) (lvl / 3.33));
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 86: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(126, (int) (lvl / 1.65));
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 56: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(138, (int) (lvl / 3.33));
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 83: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(138, (int) (lvl / 1.65));
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 58: {
			stats.addOneStat(125, lvl);
			stats.addOneStat(176, (int) (lvl / 3.33));
			stats.addOneStat(128, lvl / 100);
			break;
		}
		case 85: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(176, (int) (lvl / 1.65));
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 80: {
			stats.addOneStat(125, lvl * 2);
			stats.addOneStat(128, lvl / 100);
			stats.addOneStat(117, lvl / 100);
			break;
		}
		case 88: {
			stats.addOneStat(138, lvl / 2);
			stats.addOneStat(212, lvl / 20);
			stats.addOneStat(211, lvl / 20);
			stats.addOneStat(210, lvl / 20);
			stats.addOneStat(213, lvl / 20);
			stats.addOneStat(214, lvl / 20);
			break;
		}
		case 75: {
			stats.addOneStat(138, lvl / 2);
			stats.addOneStat(212, lvl / 20);
			stats.addOneStat(211, lvl / 20);
			stats.addOneStat(210, lvl / 20);
			stats.addOneStat(213, lvl / 20);
			stats.addOneStat(214, lvl / 20);
			break;
		}
		}
		return stats;
	}

	public static ObjectTemplate getParchoTemplateByMountColor(final int color) {
		switch (color) {
		case 2: {
			return World.getObjTemplate(7807);
		}
		case 3: {
			return World.getObjTemplate(7808);
		}
		case 4: {
			return World.getObjTemplate(7809);
		}
		case 9: {
			return World.getObjTemplate(7810);
		}
		case 10: {
			return World.getObjTemplate(7811);
		}
		case 11: {
			return World.getObjTemplate(7812);
		}
		case 12: {
			return World.getObjTemplate(7813);
		}
		case 15: {
			return World.getObjTemplate(7814);
		}
		case 16: {
			return World.getObjTemplate(7815);
		}
		case 17: {
			return World.getObjTemplate(7816);
		}
		case 18: {
			return World.getObjTemplate(7817);
		}
		case 19: {
			return World.getObjTemplate(7818);
		}
		case 20: {
			return World.getObjTemplate(7819);
		}
		case 21: {
			return World.getObjTemplate(7820);
		}
		case 22: {
			return World.getObjTemplate(7821);
		}
		case 23: {
			return World.getObjTemplate(7822);
		}
		case 33: {
			return World.getObjTemplate(7823);
		}
		case 34: {
			return World.getObjTemplate(7824);
		}
		case 35: {
			return World.getObjTemplate(7825);
		}
		case 36: {
			return World.getObjTemplate(7826);
		}
		case 37: {
			return World.getObjTemplate(7827);
		}
		case 38: {
			return World.getObjTemplate(7828);
		}
		case 39: {
			return World.getObjTemplate(7829);
		}
		case 40: {
			return World.getObjTemplate(7830);
		}
		case 41: {
			return World.getObjTemplate(7831);
		}
		case 42: {
			return World.getObjTemplate(7832);
		}
		case 43: {
			return World.getObjTemplate(7833);
		}
		case 44: {
			return World.getObjTemplate(7834);
		}
		case 45: {
			return World.getObjTemplate(7835);
		}
		case 46: {
			return World.getObjTemplate(7836);
		}
		case 47: {
			return World.getObjTemplate(7837);
		}
		case 48: {
			return World.getObjTemplate(7838);
		}
		case 49: {
			return World.getObjTemplate(7839);
		}
		case 50: {
			return World.getObjTemplate(7840);
		}
		case 51: {
			return World.getObjTemplate(7841);
		}
		case 52: {
			return World.getObjTemplate(7842);
		}
		case 53: {
			return World.getObjTemplate(7843);
		}
		case 54: {
			return World.getObjTemplate(7844);
		}
		case 55: {
			return World.getObjTemplate(7845);
		}
		case 56: {
			return World.getObjTemplate(7846);
		}
		case 57: {
			return World.getObjTemplate(7847);
		}
		case 58: {
			return World.getObjTemplate(7848);
		}
		case 59: {
			return World.getObjTemplate(7849);
		}
		case 60: {
			return World.getObjTemplate(7850);
		}
		case 61: {
			return World.getObjTemplate(7851);
		}
		case 62: {
			return World.getObjTemplate(7852);
		}
		case 63: {
			return World.getObjTemplate(7853);
		}
		case 64: {
			return World.getObjTemplate(7854);
		}
		case 65: {
			return World.getObjTemplate(7855);
		}
		case 66: {
			return World.getObjTemplate(7856);
		}
		case 67: {
			return World.getObjTemplate(7857);
		}
		case 68: {
			return World.getObjTemplate(7858);
		}
		case 69: {
			return World.getObjTemplate(7859);
		}
		case 70: {
			return World.getObjTemplate(7860);
		}
		case 71: {
			return World.getObjTemplate(7861);
		}
		case 72: {
			return World.getObjTemplate(7862);
		}
		case 73: {
			return World.getObjTemplate(7863);
		}
		case 74: {
			return World.getObjTemplate(7864);
		}
		case 75: {
			return World.getObjTemplate(7865);
		}
		case 76: {
			return World.getObjTemplate(7866);
		}
		case 77: {
			return World.getObjTemplate(7867);
		}
		case 78: {
			return World.getObjTemplate(7868);
		}
		case 79: {
			return World.getObjTemplate(7869);
		}
		case 80: {
			return World.getObjTemplate(7870);
		}
		case 82: {
			return World.getObjTemplate(7871);
		}
		case 83: {
			return World.getObjTemplate(7872);
		}
		case 84: {
			return World.getObjTemplate(7873);
		}
		case 85: {
			return World.getObjTemplate(7874);
		}
		case 86: {
			return World.getObjTemplate(7875);
		}
		case 87: {
			return World.getObjTemplate(7876);
		}
		case 88: {
			return World.getObjTemplate(9582);
		}
		default: {
			return null;
		}
		}
	}

	public static int getMountColorByParchoTemplate(final int tID) {
		for (int a = 1; a < 100; ++a) {
			if (getParchoTemplateByMountColor(a) != null && getParchoTemplateByMountColor(a).getId() == tID) {
				return a;
			}
		}
		return -1;
	}

	public static int getNearCellidUnused(final Player _perso) {
		int cellFront = 0;
		int cellBack = 0;
		int cellRight = 0;
		int cellLeft = 0;
		int cell = 0;
		int calcul = 0;
		final org.aestia.map.Map map = _perso.getCurMap();
		if (map == null) {
			return -1;
		}
		final World.SubArea sub = map.getSubArea();
		if (sub == null) {
			return -1;
		}
		final World.Area area = sub.getArea();
		if (area == null) {
			return -1;
		}
		if ((area.get_id() == 7 || area.get_id() == 11) && map.getW() == 19 && map.getH() == 22) {
			cellFront = 19;
			cellBack = 19;
			cellRight = 18;
			cellLeft = 18;
		} else {
			cellFront = 15;
			cellBack = 15;
			cellRight = 14;
			cellLeft = 14;
		}
		cell = _perso.getCurCell().getId();
		calcul = cell + cellFront;
		if (map.getCase(calcul).getDroppedItem(false) == null && map.getCases().get(calcul).getCharacters().isEmpty()
				&& map.getCases().get(calcul).isWalkable(false) && map.getCases().get(calcul).getObject() == null) {
			return calcul;
		}
		calcul = 0;
		calcul = cell - cellBack;
		if (map.getCase(calcul).getDroppedItem(false) == null && map.getCases().get(calcul).getCharacters().isEmpty()
				&& map.getCases().get(calcul).isWalkable(false) && map.getCases().get(calcul).getObject() == null) {
			return calcul;
		}
		calcul = 0;
		calcul = cell + cellRight;
		if (map.getCase(calcul).getDroppedItem(false) == null && map.getCases().get(calcul).getCharacters().isEmpty()
				&& map.getCases().get(calcul).isWalkable(false) && map.getCases().get(calcul).getObject() == null) {
			return calcul;
		}
		calcul = 0;
		calcul = cell - cellLeft;
		if (map.getCase(calcul).getDroppedItem(false) == null && map.getCases().get(calcul).getCharacters().isEmpty()
				&& map.getCases().get(calcul).isWalkable(false) && map.getCases().get(calcul).getObject() == null) {
			return calcul;
		}
		return -1;
	}

	public static boolean isValidPlaceForItem(final ObjectTemplate template, final int place) {
		if (template.getType() == 41 && place == 16) {
			return true;
		}
		switch (template.getType()) {
		case 1: {
			if (place == 0) {
				return true;
			}
			break;
		}
		case 113: {
			if (template.getId() == 9233 && place == 7) {
				return true;
			}
			if (template.getId() == 9234 && place == 6) {
				return true;
			}
			if (template.getId() == 9255 && place == 0) {
				return true;
			}
			if (template.getId() == 9256 && (place == 2 || place == 4)) {
				return true;
			}
			break;
		}
		case 114: {
			if (place == 1) {
				return true;
			}
			break;
		}
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 19:
		case 20:
		case 21:
		case 22:
		case 83:
		case 99: {
			if (place == 1) {
				return true;
			}
			break;
		}
		case 9: {
			if (place == 2 || place == 4) {
				return true;
			}
			break;
		}
		case 10: {
			if (place == 3) {
				return true;
			}
			break;
		}
		case 11: {
			if (place == 5) {
				return true;
			}
			break;
		}
		case 16: {
			if (place == 6) {
				return true;
			}
			break;
		}
		case 17:
		case 81: {
			if (place == 7) {
				return true;
			}
			break;
		}
		case 18: {
			if (place == 8) {
				return true;
			}
			break;
		}
		case 23: {
			if (place == 9 || place == 10 || place == 11 || place == 12 || place == 13 || place == 14) {
				return true;
			}
			break;
		}
		case 82: {
			if (place == 15) {
				return true;
			}
			break;
		}
		case 12:
		case 13:
		case 14:
		case 28:
		case 33:
		case 37:
		case 41:
		case 42:
		case 49:
		case 63:
		case 64:
		case 69:
		case 70:
		case 73:
		case 74:
		case 79:
		case 85:
		case 87:
		case 89:
		case 93:
		case 94:
		case 112: {
			if (place >= 35 && place <= 48) {
				return true;
			}
			break;
		}
		}
		return false;
	}

	public static void tpCim(final int idArea, final Player perso) {
		switch (idArea) {
		case 45: {
			perso.teleport((short) 10342, 222);
			return;
		}
		case 0:
		case 5:
		case 29:
		case 39:
		case 40:
		case 43:
		case 44: {
			perso.teleport((short) 1174, 279);
			return;
		}
		case 3:
		case 4:
		case 6:
		case 18:
		case 25:
		case 27:
		case 41: {
			perso.teleport((short) 8534, 196);
			return;
		}
		case 2: {
			perso.teleport((short) 420, 408);
			return;
		}
		case 1: {
			perso.teleport((short) 844, 370);
			return;
		}
		case 7: {
			perso.teleport((short) 4285, 572);
			return;
		}
		case 8:
		case 14:
		case 15:
		case 16:
		case 32: {
			perso.teleport((short) 4748, 133);
			return;
		}
		case 11:
		case 12:
		case 13:
		case 33: {
			perso.teleport((short) 5719, 196);
			return;
		}
		case 19:
		case 22:
		case 23: {
			perso.teleport((short) 7910, 381);
			return;
		}
		case 20:
		case 21:
		case 24: {
			perso.teleport((short) 8054, 115);
			return;
		}
		case 28:
		case 34:
		case 35:
		case 36: {
			perso.teleport((short) 9231, 257);
			return;
		}
		case 30: {
			perso.teleport((short) 9539, 128);
			return;
		}
		case 31: {
			if (perso.isGhost()) {
				perso.teleport((short) 9558, 268);
				return;
			}
			perso.teleport((short) 9558, 224);
			return;
		}
		case 37: {
			perso.teleport((short) 7796, 433);
			return;
		}
		case 42: {
			perso.teleport((short) 8534, 196);
			return;
		}
		case 46:
		case 47: {
			perso.teleport((short) 10590, 302);
			return;
		}
		case 26: {
			perso.teleport((short) 9398, 268);
			break;
		}
		}
		perso.teleport((short) 8534, 196);
	}

	public static boolean isTaverne(final org.aestia.map.Map map) {
		switch (map.getId()) {
		case 7573: {
			return true;
		}
		case 7572: {
			return true;
		}
		case 7574: {
			return true;
		}
		case 465: {
			return true;
		}
		case 463: {
			return true;
		}
		case 6064: {
			return true;
		}
		case 461: {
			return true;
		}
		case 462: {
			return true;
		}
		case 5867: {
			return true;
		}
		case 6197: {
			return true;
		}
		case 6021: {
			return true;
		}
		case 6044: {
			return true;
		}
		case 8196: {
			return true;
		}
		case 6055: {
			return true;
		}
		case 8195: {
			return true;
		}
		case 1905: {
			return true;
		}
		case 1907: {
			return true;
		}
		case 6049: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static int getLevelForChevalier(final Player target) {
		final int lvl = target.getLevel();
		if (lvl <= 50) {
			return 50;
		}
		if (lvl <= 80 && lvl > 50) {
			return 80;
		}
		if (lvl <= 110 && lvl > 80) {
			return 110;
		}
		if (lvl <= 140 && lvl > 110) {
			return 140;
		}
		if (lvl <= 170 && lvl > 140) {
			return 170;
		}
		if (lvl <= 500 && lvl > 170) {
			return 200;
		}
		return 200;
	}

	public static String getStatsOfCandy(final int id, final int turn) {
		String a = World.getObjTemplate(id).getStrTemplate();
		a = String.valueOf(a) + ",32b#64#0#" + Integer.toHexString(turn) + "#0d0+1;";
		return a;
	}

	public static String getStatsOfMascotte() {
		String a = String.valueOf(Integer.toHexString(148)) + "#0#0#0#0d0+1,";
		a = String.valueOf(a) + "32b#64#0#" + Integer.toHexString(1) + "#0d0+1;";
		return a;
	}

	public static String getStringColorDragodinde(final int color) {
		switch (color) {
		case 1: {
			return "16772045,-1,16772045";
		}
		case 3: {
			return "1245184,393216,1245184";
		}
		case 6: {
			return "16747520,-1,16747520";
		}
		case 9: {
			return "1182992,16777200,16777200";
		}
		case 10: {
			return "16747520,-1,16747520";
		}
		case 11: {
			return "16747520,16777200,16777200";
		}
		case 12: {
			return "16747520,1703936,1774084";
		}
		case 15: {
			return "4251856,-1,4251856";
		}
		case 16: {
			return "16777200,16777200,16777200";
		}
		case 17: {
			return "4915330,-1,4915330";
		}
		case 18: {
			return "16766720,16766720,16766720";
		}
		case 19: {
			return "14423100,-1,14423100";
		}
		case 20: {
			return "16772045,-1,16772045";
		}
		case 21: {
			return "3329330,-1,3329330";
		}
		case 22: {
			return "15859954,16777200,15859954";
		}
		case 23: {
			return "14524637,-1,14524637";
		}
		case 33: {
			return "16772045,16766720,16766720";
		}
		case 34: {
			return "16772045,1245184,1245184";
		}
		case 35: {
			return "16772045,3329330,3329330";
		}
		case 36: {
			return "16772045,4915330,4915330";
		}
		case 37: {
			return "16772045,16777200,16777200";
		}
		case 38: {
			return "16772045,16747520,16747520";
		}
		case 39: {
			return "16772045,4251856,4251856";
		}
		case 40: {
			return "16772045,15859954,15859954";
		}
		case 41: {
			return "16772045,14423100,14423100";
		}
		case 42: {
			return "1245184,16766720,16766720";
		}
		case 43: {
			return "16766720,3329330,3329330";
		}
		case 44: {
			return "16766720,4915330,4915330";
		}
		case 45: {
			return "16766720,16777200,16777200";
		}
		case 46: {
			return "16766720,16747520,16747520";
		}
		case 47: {
			return "16766720,4251856,4251856";
		}
		case 48: {
			return "16766720,15859954,15859954";
		}
		case 49: {
			return "16766720,14423100,14423100";
		}
		case 50: {
			return "1245184,3329330,3329330";
		}
		case 51: {
			return "4915330,4915330,1245184";
		}
		case 52: {
			return "1245184,4251856,4251856";
		}
		case 53: {
			return "15859954,0,0";
		}
		case 54: {
			return "14423100,14423100,1245184";
		}
		case 55: {
			return "3329330,4915330,4915330";
		}
		case 56: {
			return "3329330,16777200,16777200";
		}
		case 57: {
			return "3329330,16747520,16747520";
		}
		case 58: {
			return "3329330,4251856,4251856";
		}
		case 59: {
			return "3329330,15859954,15859954";
		}
		case 60: {
			return "3329330,14423100,14423100";
		}
		case 61: {
			return "4915330,16777200,16777200";
		}
		case 62: {
			return "4915330,16747520,16747520";
		}
		case 63: {
			return "4915330,4251856,4251856";
		}
		case 64: {
			return "4915330,15859954,15859954";
		}
		case 65: {
			return "14423100,4915330,4915330";
		}
		case 66: {
			return "16777200,4251856,4251856";
		}
		case 67: {
			return "16777200,16731355,16711910";
		}
		case 68: {
			return "14423100,16777200,16777200";
		}
		case 69: {
			return "4251856,16747520,16747520";
		}
		case 70: {
			return "14315734,16747520,16747520";
		}
		case 71: {
			return "14423100,16747520,16747520";
		}
		case 72: {
			return "15859954,4251856,4251856";
		}
		case 73: {
			return "14423100,4251856,4251856";
		}
		case 74: {
			return "16766720,16766720,16766720";
		}
		case 76: {
			return "14315734,14423100,14423100";
		}
		case 77: {
			return "14524637,16772045,16772045";
		}
		case 78: {
			return "14524637,16766720,16766720";
		}
		case 79: {
			return "14524637,1245184,1245184";
		}
		case 80: {
			return "14524637,3329330,3329330";
		}
		case 82: {
			return "14524637,4915330,4915330";
		}
		case 83: {
			return "14524637,16777200,16777200";
		}
		case 84: {
			return "14524637,16747520,16747520";
		}
		case 85: {
			return "14524637,4251856,4251856";
		}
		case 86: {
			return "14524637,15859954,15859954";
		}
		case 87: {
			return "14524637,14423100,14423100";
		}
		default: {
			return "-1,-1,-1";
		}
		}
	}

	public static int getGeneration(final int color) {
		switch (color) {
		case 10:
		case 18:
		case 20: {
			return 1;
		}
		case 33:
		case 38:
		case 46: {
			return 2;
		}
		case 3:
		case 17: {
			return 3;
		}
		case 12:
		case 34:
		case 36:
		case 42:
		case 44:
		case 51:
		case 62: {
			return 4;
		}
		case 19:
		case 22: {
			return 5;
		}
		case 40:
		case 41:
		case 48:
		case 49:
		case 53:
		case 54:
		case 64:
		case 65:
		case 70:
		case 71:
		case 76: {
			return 6;
		}
		case 15:
		case 16: {
			return 7;
		}
		case 9:
		case 11:
		case 37:
		case 39:
		case 45:
		case 47:
		case 52:
		case 61:
		case 63:
		case 66:
		case 67:
		case 68:
		case 69:
		case 72:
		case 73: {
			return 8;
		}
		case 21:
		case 23: {
			return 9;
		}
		case 35:
		case 43:
		case 50:
		case 55:
		case 56:
		case 57:
		case 58:
		case 59:
		case 60:
		case 77:
		case 78:
		case 79:
		case 80:
		case 82:
		case 83:
		case 84:
		case 85:
		case 86: {
			return 10;
		}
		default: {
			return 1;
		}
		}
	}

	public static int colorToEtable(int color1, int color2) {
		int colorcria = 1;
		int A = 0;
		int B = 0;
		int C = 0;
		if (color1 == 75) {
			color1 = 10;
		}
		if (color2 == 75) {
			color2 = 10;
		}
		if (color1 > color2) {
			A = color2;
			B = color1;
		} else if (color1 <= color2) {
			A = color1;
			B = color2;
		}
		if (A == 10 && B == 18) {
			C = 46;
		} else if (A == 10 && B == 20) {
			C = 38;
		} else if (A == 18 && B == 20) {
			C = 33;
		} else if (A == 33 && B == 38) {
			C = 17;
		} else if (A == 33 && B == 46) {
			C = 3;
		} else if (A == 10 && B == 17) {
			C = 62;
		} else if (A == 10 && B == 3) {
			C = 12;
		} else if (A == 17 && B == 20) {
			C = 36;
		} else if (A == 3 && B == 20) {
			C = 34;
		} else if (A == 17 && B == 18) {
			C = 44;
		} else if (A == 3 && B == 18) {
			C = 42;
		} else if (A == 3 && B == 17) {
			C = 51;
		} else if (A == 38 && B == 51) {
			C = 19;
		} else if (A == 46 && B == 51) {
			C = 22;
		} else if (A == 10 && B == 19) {
			C = 71;
		} else if (A == 10 && B == 22) {
			C = 70;
		} else if (A == 19 && B == 20) {
			C = 41;
		} else if (A == 20 && B == 22) {
			C = 40;
		} else if (A == 18 && B == 19) {
			C = 49;
		} else if (A == 18 && B == 22) {
			C = 48;
		} else if (A == 17 && B == 19) {
			C = 65;
		} else if (A == 17 && B == 22) {
			C = 64;
		} else if (A == 3 && B == 19) {
			C = 54;
		} else if (A == 3 && B == 22) {
			C = 53;
		} else if (A == 19 && B == 22) {
			C = 76;
		} else if (A == 53 && B == 76) {
			C = 15;
		} else if (A == 65 && B == 76) {
			C = 16;
		} else if (A == 10 && B == 16) {
			C = 11;
		} else if (A == 10 && B == 15) {
			C = 69;
		} else if (A == 16 && B == 20) {
			C = 37;
		} else if (A == 15 && B == 20) {
			C = 39;
		} else if (A == 16 && B == 18) {
			C = 45;
		} else if (A == 15 && B == 18) {
			C = 47;
		} else if (A == 16 && B == 17) {
			C = 61;
		} else if (A == 15 && B == 17) {
			C = 63;
		} else if (A == 3 && B == 16) {
			C = 9;
		} else if (A == 3 && B == 15) {
			C = 52;
		} else if (A == 16 && B == 19) {
			C = 68;
		} else if (A == 15 && B == 19) {
			C = 73;
		} else if (A == 16 && B == 22) {
			C = 67;
		} else if (A == 15 && B == 22) {
			C = 72;
		} else if (A == 15 && B == 16) {
			C = 66;
		} else if (A == 66 && B == 68) {
			C = 21;
		} else if (A == 66 && B == 72) {
			C = 23;
		} else if (A == 10 && B == 21) {
			C = 57;
		} else if (A == 20 && B == 21) {
			C = 35;
		} else if (A == 18 && B == 21) {
			C = 43;
		} else if (A == 3 && B == 21) {
			C = 50;
		} else if (A == 17 && B == 21) {
			C = 55;
		} else if (A == 16 && B == 21) {
			C = 56;
		} else if (A == 15 && B == 21) {
			C = 58;
		} else if (A == 21 && B == 22) {
			C = 59;
		} else if (A == 19 && B == 21) {
			C = 60;
		} else if (A == 20 && B == 23) {
			C = 77;
		} else if (A == 18 && B == 23) {
			C = 78;
		} else if (A == 3 && B == 23) {
			C = 79;
		} else if (A == 21 && B == 23) {
			C = 80;
		} else if (A == 17 && B == 23) {
			C = 82;
		} else if (A == 16 && B == 23) {
			C = 83;
		} else if (A == 10 && B == 23) {
			C = 84;
		} else if (A == 15 && B == 23) {
			C = 85;
		} else if (A == 22 && B == 23) {
			C = 86;
		} else if (A == 19 && B == 23) {
			C = 87;
		}
		final ArrayList<Integer> posibles = new ArrayList<Integer>();
		posibles.add(18);
		posibles.add(20);
		posibles.add(A);
		posibles.add(B);
		posibles.add(10);
		posibles.add(18);
		if (C != 0) {
			for (int j = 11; j > getGeneration(C); --j) {
				posibles.add(C);
			}
		}
		colorcria = posibles.get(Formulas.getRandomValue(0, posibles.size() - 1));
		return colorcria;
	}

	public static int getParchoByIdPets(final int id) {
		switch (id) {
		case 10802: {
			return 10806;
		}
		case 10107: {
			return 10135;
		}
		case 10106: {
			return 10134;
		}
		case 9795: {
			return 9810;
		}
		case 9624: {
			return 9685;
		}
		case 9623: {
			return 9684;
		}
		case 9620: {
			return 9683;
		}
		case 9619: {
			return 9682;
		}
		case 9617: {
			return 9675;
		}
		case 9594: {
			return 9598;
		}
		case 8693: {
			return 8707;
		}
		case 8677: {
			return 8684;
		}
		case 8561: {
			return 8564;
		}
		case 8211: {
			return 8544;
		}
		case 8155: {
			return 8179;
		}
		case 8154: {
			return 8178;
		}
		case 8153: {
			return 8175;
		}
		case 8151: {
			return 8176;
		}
		case 8000: {
			return 8180;
		}
		case 7911: {
			return 8526;
		}
		case 7892: {
			return 7896;
		}
		case 7891: {
			return 7895;
		}
		case 7714: {
			return 8708;
		}
		case 7713: {
			return 9681;
		}
		case 7712: {
			return 9680;
		}
		case 7711: {
			return 9679;
		}
		case 7710: {
			return 9678;
		}
		case 7709: {
			return 9677;
		}
		case 7708: {
			return 9676;
		}
		case 7707: {
			return 9674;
		}
		case 7706: {
			return 8685;
		}
		case 7705: {
			return 8889;
		}
		case 7704: {
			return 8888;
		}
		case 7703: {
			return 8421;
		}
		case 7524: {
			return 8887;
		}
		case 7522: {
			return 7535;
		}
		case 7520: {
			return 7533;
		}
		case 7519: {
			return 7534;
		}
		case 7518: {
			return 7532;
		}
		case 7415: {
			return 7419;
		}
		case 7414: {
			return 7418;
		}
		case 6978: {
			return 7417;
		}
		case 6716: {
			return 7420;
		}
		case 2077: {
			return 2098;
		}
		case 2076: {
			return 2101;
		}
		case 2075: {
			return 2100;
		}
		case 2074: {
			return 2099;
		}
		case 1748: {
			return 2102;
		}
		case 1728: {
			return 1735;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getPetsByIdParcho(final int id) {
		switch (id) {
		case 10806: {
			return 10802;
		}
		case 10135: {
			return 10107;
		}
		case 10134: {
			return 10106;
		}
		case 9810: {
			return 9795;
		}
		case 9685: {
			return 9624;
		}
		case 9684: {
			return 9623;
		}
		case 9683: {
			return 9620;
		}
		case 9682: {
			return 9619;
		}
		case 9675: {
			return 9617;
		}
		case 9598: {
			return 9594;
		}
		case 8707: {
			return 8693;
		}
		case 8684: {
			return 8677;
		}
		case 8564: {
			return 8561;
		}
		case 8544: {
			return 8211;
		}
		case 8179: {
			return 8155;
		}
		case 8178: {
			return 8154;
		}
		case 8175: {
			return 8153;
		}
		case 8176: {
			return 8151;
		}
		case 8180: {
			return 8000;
		}
		case 8526: {
			return 7911;
		}
		case 7896: {
			return 7892;
		}
		case 7895: {
			return 7891;
		}
		case 8708: {
			return 7714;
		}
		case 9681: {
			return 7713;
		}
		case 9680: {
			return 7712;
		}
		case 9679: {
			return 7711;
		}
		case 9678: {
			return 7710;
		}
		case 9677: {
			return 7709;
		}
		case 9676: {
			return 7708;
		}
		case 9674: {
			return 7707;
		}
		case 8685: {
			return 7706;
		}
		case 8889: {
			return 7705;
		}
		case 8888: {
			return 7704;
		}
		case 8421: {
			return 7703;
		}
		case 8887: {
			return 7524;
		}
		case 7535: {
			return 7522;
		}
		case 7533: {
			return 7520;
		}
		case 7534: {
			return 7519;
		}
		case 7532: {
			return 7518;
		}
		case 7419: {
			return 7415;
		}
		case 7418: {
			return 7414;
		}
		case 7417: {
			return 6978;
		}
		case 7420: {
			return 6716;
		}
		case 2098: {
			return 2077;
		}
		case 2101: {
			return 2076;
		}
		case 2100: {
			return 2075;
		}
		case 2099: {
			return 2074;
		}
		case 2102: {
			return 1748;
		}
		case 1735: {
			return 1728;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getDoplonDopeul(final int IDmob) {
		switch (IDmob) {
		case 168: {
			return 10302;
		}
		case 165: {
			return 10303;
		}
		case 166: {
			return 10304;
		}
		case 162: {
			return 10305;
		}
		case 160: {
			return 10306;
		}
		case 167: {
			return 10307;
		}
		case 161: {
			return 10308;
		}
		case 2691: {
			return 10309;
		}
		case 455: {
			return 10310;
		}
		case 169: {
			return 10311;
		}
		case 163: {
			return 10312;
		}
		case 164: {
			return 10313;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getIDdoplonByMapID(final int IDmap) {
		switch (IDmap) {
		case 6926: {
			return 10312;
		}
		case 1470: {
			return 10305;
		}
		case 1461: {
			return 10303;
		}
		case 6949: {
			return 10310;
		}
		case 1556: {
			return 10302;
		}
		case 1549: {
			return 10307;
		}
		case 1469: {
			return 10313;
		}
		case 487: {
			return 10304;
		}
		case 490: {
			return 10308;
		}
		case 177: {
			return 10306;
		}
		case 1466: {
			return 10311;
		}
		case 8207: {
			return 10309;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getStarAlea() {
		final int rand = Formulas.getRandomValue(1, 100);
		if (rand > 54 && rand < 90) {
			final int i = Formulas.getRandomValue(1, 15);
			switch (i) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5: {
				return 15;
			}
			case 6:
			case 7:
			case 8:
			case 9: {
				return 30;
			}
			case 10:
			case 11:
			case 12: {
				return 45;
			}
			case 13:
			case 14: {
				return 60;
			}
			case 15: {
				return 75;
			}
			}
		}
		if (rand >= 90) {
			final int i = Formulas.getRandomValue(1, 15);
			switch (i) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5: {
				return 90;
			}
			case 6:
			case 7:
			case 8:
			case 9: {
				return 105;
			}
			case 10:
			case 11:
			case 12: {
				return 120;
			}
			case 13:
			case 14: {
				return 135;
			}
			case 15: {
				return 150;
			}
			}
		}
		return 0;
	}

	public static int getArmeSoin(final int idArme) {
		switch (idArme) {
		case 7172: {
			return 100;
		}
		case 7156: {
			return 80;
		}
		case 1355: {
			return 42;
		}
		case 7182: {
			return 100;
		}
		case 7040: {
			return 10;
		}
		case 6539: {
			return 80;
		}
		case 6519: {
			return 23;
		}
		case 8118: {
			return 30;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getSectionByDopeuls(final int id) {
		switch (id) {
		case 160: {
			return 1;
		}
		case 161: {
			return 2;
		}
		case 162: {
			return 3;
		}
		case 163: {
			return 4;
		}
		case 164: {
			return 5;
		}
		case 165: {
			return 6;
		}
		case 166: {
			return 7;
		}
		case 167: {
			return 8;
		}
		case 168: {
			return 9;
		}
		case 169: {
			return 10;
		}
		case 455: {
			return 11;
		}
		case 2691: {
			return 12;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getCertificatByDopeuls(final int id) {
		switch (id) {
		case 160: {
			return 10293;
		}
		case 161: {
			return 10295;
		}
		case 162: {
			return 10292;
		}
		case 163: {
			return 10299;
		}
		case 164: {
			return 10300;
		}
		case 165: {
			return 10290;
		}
		case 166: {
			return 10291;
		}
		case 167: {
			return 10294;
		}
		case 168: {
			return 10289;
		}
		case 169: {
			return 10298;
		}
		case 455: {
			return 10297;
		}
		case 2691: {
			return 10296;
		}
		default: {
			return -1;
		}
		}
	}

	public static boolean isCertificatDopeuls(final int id) {
		switch (id) {
		case 10289:
		case 10290:
		case 10291:
		case 10292:
		case 10293:
		case 10294:
		case 10295:
		case 10296:
		case 10297:
		case 10298:
		case 10299:
		case 10300: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static int getItemIdByMascotteId(final int id) {
		switch (id) {
		case 10118: {
			return 1498;
		}
		case 10078: {
			return 70;
		}
		case 10077: {
			return -1;
		}
		case 10009: {
			return 90;
		}
		case 9993: {
			return 71;
		}
		case 9096: {
			return 30;
		}
		case 9061: {
			return 40;
		}
		case 8563: {
			return 1076;
		}
		case 7425: {
			return 1588;
		}
		case 7354: {
			return 1264;
		}
		case 7353: {
			return 1076;
		}
		case 7352: {
			return 1153;
		}
		case 7351: {
			return 1248;
		}
		case 7350: {
			return 1228;
		}
		case 7062: {
			return 9001;
		}
		case 6876: {
			return 1245;
		}
		case 6875: {
			return 1249;
		}
		case 6874: {
			return 70;
		}
		case 6873: {
			return 1243;
		}
		case 6872: {
			return 50;
		}
		case 6871: {
			return 1247;
		}
		case 6870: {
			return 1246;
		}
		case 6869: {
			return 9043;
		}
		case 6832: {
			return -1;
		}
		case 6768: {
			return 9001;
		}
		case 2272: {
			return 1577;
		}
		case 2169: {
			return 1205;
		}
		case 2152: {
			return 1001;
		}
		case 2134: {
			return 1205;
		}
		case 2132: {
			return 9004;
		}
		case 2130: {
			return 1001;
		}
		case 2082: {
			return 1208;
		}
		default: {
			return -1;
		}
		}
	}

	public static boolean isIncarnationWeapon(final int id) {
		switch (id) {
		case 9544:
		case 9545:
		case 9546:
		case 9547:
		case 9548:
		case 10125:
		case 10126:
		case 10127:
		case 10133: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static boolean isTourmenteurWeapon(final int id) {
		switch (id) {
		case 9544:
		case 9545:
		case 9546:
		case 9547:
		case 9548: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static boolean isBanditsWeapon(final int id) {
		switch (id) {
		case 10125:
		case 10126:
		case 10127:
		case 10133: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static int getSpecialSpellByClasse(final int classe) {
		switch (classe) {
		case 1: {
			return 422;
		}
		case 2: {
			return 420;
		}
		case 3: {
			return 425;
		}
		case 4: {
			return 416;
		}
		case 5: {
			return 424;
		}
		case 6: {
			return 412;
		}
		case 7: {
			return 427;
		}
		case 8: {
			return 410;
		}
		case 9: {
			return 418;
		}
		case 10: {
			return 426;
		}
		case 11: {
			return 421;
		}
		case 12: {
			return 423;
		}
		default: {
			return 0;
		}
		}
	}

	public static boolean isFlacGelee(final int id) {
		switch (id) {
		case 2430:
		case 2431:
		case 2432:
		case 2433: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static boolean isDoplon(final int id) {
		switch (id) {
		case 10302:
		case 10303:
		case 10304:
		case 10305:
		case 10306:
		case 10307:
		case 10308:
		case 10309:
		case 10310:
		case 10311:
		case 10312:
		case 10313: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static boolean isInMorphDonjon(final int id) {
		switch (id) {
		case 8716:
		case 8718:
		case 8719:
		case 8979:
		case 8980:
		case 8981:
		case 8982:
		case 8983:
		case 8984:
		case 9121:
		case 9122:
		case 9123:
		case 9716: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static int[] getOppositeStats(final int statsId) {
		if (statsId == 217) {
			return new int[] { 210, 211, 213, 214 };
		}
		if (statsId == 216) {
			return new int[] { 210, 212, 213, 214 };
		}
		if (statsId == 218) {
			return new int[] { 210, 211, 212, 214 };
		}
		if (statsId == 219) {
			return new int[] { 210, 211, 212, 214 };
		}
		if (statsId == 215) {
			return new int[] { 211, 212, 213, 214 };
		}
		return null;
	}
}
