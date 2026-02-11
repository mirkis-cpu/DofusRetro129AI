// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.world;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.command.server.Commandes;
import org.aestia.command.server.Groupes;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.Pet;
import org.aestia.entity.PetEntry;
import org.aestia.entity.Prism;
import org.aestia.entity.monster.Monster;
import org.aestia.entity.npc.NpcAnswer;
import org.aestia.entity.npc.NpcQuestion;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.GameClient;
import org.aestia.hdv.Hdv;
import org.aestia.hdv.HdvEntry;
import org.aestia.job.Job;
import org.aestia.job.maging.Rune;
import org.aestia.kernel.Boutique;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.InteractiveObject;
import org.aestia.map.MountPark;
import org.aestia.map.SchemaFight;
import org.aestia.map.laby.Dc;
import org.aestia.map.laby.Toror;
import org.aestia.object.ObjectSet;
import org.aestia.object.ObjectTemplate;
import org.aestia.object.entity.Fragment;
import org.aestia.object.entity.SoulStone;
import org.aestia.other.Animation;
import org.aestia.other.Dopeul;
import org.aestia.other.Guild;
import org.aestia.other.House;
import org.aestia.other.Trunk;
import org.aestia.other.Tutorial;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

public class World {
	private static Map<Integer, Account> Comptes;
	private static Map<String, Integer> ComptebyName;
	private static Map<Integer, Player> Persos;
	private static Map<Short, org.aestia.map.Map> Maps;
	private static Map<Short, SchemaFight> SchemaFights;
	private static Map<Integer, org.aestia.object.Object> Objets;
	private static Map<Integer, ExpLevel> ExpLevels;
	private static Map<Integer, Spell> Sorts;
	private static Map<Integer, ObjectTemplate> ObjTemplates;
	private static Map<Integer, Monster> MobTemplates;
	private static Map<Integer, NpcTemplate> NPCTemplates;
	private static Map<Integer, NpcQuestion> NPCQuestions;
	private static Map<Integer, NpcAnswer> NpcAnswer;
	private static Map<Integer, InteractiveObject.InteractiveObjectTemplate> IOTemplate;
	private static Map<Integer, Dragodinde> Dragodindes;
	private static Map<Integer, SuperArea> SuperAreas;
	private static Map<Integer, Area> Areas;
	private static Map<Integer, SubArea> SubAreas;
	private static Map<Integer, Job> Jobs;
	private static Map<Integer, ArrayList<Couple<Integer, Integer>>> Crafts;
	private static Map<Integer, ObjectSet> ItemSets;
	private static Map<Integer, Guild> Guildes;
	private static Map<Integer, Hdv> Hdvs;
	private static Map<Integer, Map<Integer, ArrayList<HdvEntry>>> hdvsItems;
	private static Map<Integer, Player> Married;
	private static Map<Integer, Animation> Animations;
	private static Map<Short, MountPark> MountPark;
	private static Map<Integer, Trunk> Trunks;
	private static Map<Integer, Collector> Collectors;
	private static Map<Integer, House> Houses;
	private static Map<Short, Collection<Integer>> Seller;
	private static StringBuilder Challenges;
	private static Map<Integer, Prism> Prismes;
	private static Map<Integer, Map<String, String>> fullmorphs;
	private static Map<Integer, Pet> Pets;
	private static Map<Integer, PetEntry> PetsEntry;
	private static Map<String, Map<String, String>> mobGroupFix;
	private static Map<Integer, Map<String, Map<String, Integer>>> extraMonstre;
	private static Map<Integer, org.aestia.map.Map> extraMonstreOnMap;
	private static Map<Integer, Tutorial> Tutorial;
	private static int nextHdvID;
	private static int nextHdvItemID;
	private static int nextLigneID;
	private static int nextObjetID;

	static {
		World.Comptes = new TreeMap<Integer, Account>();
		World.ComptebyName = new TreeMap<String, Integer>();
		World.Persos = new TreeMap<Integer, Player>();
		World.Maps = new TreeMap<Short, org.aestia.map.Map>();
		World.SchemaFights = new TreeMap<Short, SchemaFight>();
		World.Objets = new TreeMap<Integer, org.aestia.object.Object>();
		World.ExpLevels = new TreeMap<Integer, ExpLevel>();
		World.Sorts = new TreeMap<Integer, Spell>();
		World.ObjTemplates = new TreeMap<Integer, ObjectTemplate>();
		World.MobTemplates = new TreeMap<Integer, Monster>();
		World.NPCTemplates = new TreeMap<Integer, NpcTemplate>();
		World.NPCQuestions = new TreeMap<Integer, NpcQuestion>();
		World.NpcAnswer = new TreeMap<Integer, NpcAnswer>();
		World.IOTemplate = new TreeMap<Integer, InteractiveObject.InteractiveObjectTemplate>();
		World.Dragodindes = new TreeMap<Integer, Dragodinde>();
		World.SuperAreas = new TreeMap<Integer, SuperArea>();
		World.Areas = new TreeMap<Integer, Area>();
		World.SubAreas = new TreeMap<Integer, SubArea>();
		World.Jobs = new TreeMap<Integer, Job>();
		World.Crafts = new TreeMap<Integer, ArrayList<Couple<Integer, Integer>>>();
		World.ItemSets = new TreeMap<Integer, ObjectSet>();
		World.Guildes = new TreeMap<Integer, Guild>();
		World.Hdvs = new TreeMap<Integer, Hdv>();
		World.hdvsItems = new HashMap<Integer, Map<Integer, ArrayList<HdvEntry>>>();
		World.Married = new TreeMap<Integer, Player>();
		World.Animations = new TreeMap<Integer, Animation>();
		World.MountPark = new TreeMap<Short, MountPark>();
		World.Trunks = new TreeMap<Integer, Trunk>();
		World.Collectors = new TreeMap<Integer, Collector>();
		World.Houses = new TreeMap<Integer, House>();
		World.Seller = new TreeMap<Short, Collection<Integer>>();
		World.Challenges = new StringBuilder();
		World.Prismes = new TreeMap<Integer, Prism>();
		World.fullmorphs = new HashMap<Integer, Map<String, String>>();
		World.Pets = new TreeMap<Integer, Pet>();
		World.PetsEntry = new TreeMap<Integer, PetEntry>();
		World.mobGroupFix = new HashMap<String, Map<String, String>>();
		World.extraMonstre = new TreeMap<Integer, Map<String, Map<String, Integer>>>();
		World.extraMonstreOnMap = new TreeMap<Integer, org.aestia.map.Map>();
		World.Tutorial = new TreeMap<Integer, Tutorial>();
	}

	public static Map<Integer, Account> getAccounts() {
		return World.Comptes;
	}

	public static Map<Integer, Account> getAccountsByIp(final String Ip) {
		final Map<Integer, Account> newAccounts = new TreeMap<Integer, Account>();
		for (final Map.Entry<Integer, Account> entry : World.Comptes.entrySet()) {
			if (entry.getValue().getLastIP().equalsIgnoreCase(Ip)) {
				newAccounts.put(newAccounts.size(), entry.getValue());
			}
		}
		return newAccounts;
	}

	public static Map<String, Integer> getAccountsByName() {
		return World.ComptebyName;
	}

	public static Map<Integer, Player> getPlayers() {
		return World.Persos;
	}

	public static Map<Short, org.aestia.map.Map> getMaps() {
		return World.Maps;
	}

	public static Map<Integer, org.aestia.object.Object> getObjects() {
		return World.Objets;
	}

	public static Map<Integer, ExpLevel> getExpLevels() {
		return World.ExpLevels;
	}

	public static Map<Integer, Spell> getSpells() {
		return World.Sorts;
	}

	public static Map<Integer, ObjectTemplate> getObjectsTemplates() {
		return World.ObjTemplates;
	}

	public static Map<Integer, Monster> getMobsTemplates() {
		return World.MobTemplates;
	}

	public static Map<Integer, NpcTemplate> getNpcTemplates() {
		return World.NPCTemplates;
	}

	public static Map<Integer, NpcQuestion> getNpcQuestions() {
		return World.NPCQuestions;
	}

	public static Map<Integer, NpcAnswer> getNpcAnswer() {
		return World.NpcAnswer;
	}

	public static Map<Integer, InteractiveObject.InteractiveObjectTemplate> getIOTemplates() {
		return World.IOTemplate;
	}

	public static Map<Integer, Dragodinde> getMounts() {
		return World.Dragodindes;
	}

	public static Map<Integer, SuperArea> getSuperAreas() {
		return World.SuperAreas;
	}

	public static Map<Integer, Area> getAreas() {
		return World.Areas;
	}

	public static Map<Integer, SubArea> getSubAreas() {
		return World.SubAreas;
	}

	public static Map<Integer, Job> getJobs() {
		return World.Jobs;
	}

	public static Map<Integer, ArrayList<Couple<Integer, Integer>>> getCrafts() {
		return World.Crafts;
	}

	public static Map<Integer, ObjectSet> getItemSets() {
		return World.ItemSets;
	}

	public static Map<Integer, Guild> getGuilds() {
		return World.Guildes;
	}

	public static Map<Integer, Hdv> getHdvs() {
		return World.Hdvs;
	}

	public static Map<Integer, Map<Integer, ArrayList<HdvEntry>>> getHdvsItems() {
		return World.hdvsItems;
	}

	public static Map<Integer, Player> getMarried() {
		return World.Married;
	}

	public static Map<Integer, Animation> getAnimations() {
		return World.Animations;
	}

	public static Map<Short, MountPark> getMountparks() {
		return World.MountPark;
	}

	public static Map<Integer, Trunk> getTrunks() {
		return World.Trunks;
	}

	public static Map<Integer, Collector> getCollectors() {
		return World.Collectors;
	}

	public static Map<Integer, House> getHouses() {
		return World.Houses;
	}

	public static Map<Short, Collection<Integer>> getSellers() {
		return World.Seller;
	}

	public static StringBuilder getChallenges() {
		return World.Challenges;
	}

	public static Map<Integer, Prism> getPrisms() {
		return World.Prismes;
	}

	public static Map<Integer, Map<String, String>> getFullmorphs() {
		return World.fullmorphs;
	}

	public static Map<Integer, Pet> getPets() {
		return World.Pets;
	}

	public static Map<Integer, PetEntry> getPetsEntry() {
		return World.PetsEntry;
	}

	public static Map<String, Map<String, String>> getMobGroupFix() {
		return World.mobGroupFix;
	}

	public static Map<Integer, Map<String, Map<String, Integer>>> getExtraMonsters() {
		return World.extraMonstre;
	}

	public static Map<Integer, org.aestia.map.Map> getExtraMonstersOnMap() {
		return World.extraMonstreOnMap;
	}

	public static Map<Integer, Tutorial> getTutorials() {
		return World.Tutorial;
	}

	public static void createWorld() {
		final long time = System.currentTimeMillis();
		Console.println("Creation du monde :", Console.Color.YELLOW);
		Database.getStatique().getCommandeData().load();
		Console.println("- Chargement des " + Commandes.size() + " commandes d administrateurs.", Console.Color.GAME);
		Database.getStatique().getGroupeData().load();
		Console.println("- Chargement des " + Groupes.size() + " groupes d administrateurs.", Console.Color.GAME);
		Database.getStatique().getFull_morphData().load();
		Console.println("- Chargement des " + getFullmorphs().size() + " tranformations completes.",
				Console.Color.GAME);
		Database.getStatique().getExtra_monsterData().load();
		Console.println("- Chargement des " + getExtraMonsters().size() + " extra-monstres.", Console.Color.GAME);
		Database.getStatique().getExperienceData().load();
		Console.println("- Chargement des " + World.ExpLevels.size() + " niveaux.", Console.Color.GAME);
		Database.getStatique().getSortData().load();
		Console.println("- Chargement des " + World.Sorts.size() + " sorts.", Console.Color.GAME);
		Database.getStatique().getMonsterData().load();
		Console.println("- Chargement des " + World.MobTemplates.size() + " monstres.", Console.Color.GAME);
		Database.getStatique().getItem_templateData().load();
		Console.println("- Chargement des " + World.ObjTemplates.size() + " templates d'objet.", Console.Color.GAME);
		Console.println("- Chargement des " + Boutique.items.size() + " items boutique.", Console.Color.GAME);
		Database.getStatique().getItemData().load();
		Console.println("- Chargement des " + World.Objets.size() + " objets.", Console.Color.GAME);
		Database.getStatique().getNpc_templateData().load();
		Console.println("- Chargement des " + World.NPCTemplates.size() + " PNJs.", Console.Color.GAME);
		Database.getStatique().getNpc_questionData().load();
		Console.println("- Chargement des " + getNpcQuestions().size() + " questions de PNJ.", Console.Color.GAME);
		Database.getStatique().getNpc_reponses_actionData().load();
		Console.println("- Chargement des " + getNpcAnswer().size() + " reponses de PNJ.", Console.Color.GAME);
		Database.getStatique().getQuest_objectifData().load();
		Database.getStatique().getQuest_etapeData().load();
		Console.println("- Chargement des " + Quest_Etape.getQuestEtapeList().size() + " etapes de quetes.",
				Console.Color.GAME);
		Database.getStatique().getQuest_dataData().load();
		Console.println("- Chargement des " + Quest.getQuestDataList().size() + " quetes.", Console.Color.GAME);
		Database.getStatique().getNpc_templateData().loadQuest();
		Console.println("- Ajout des quetes sur les templates de PNJ.", Console.Color.GAME);
		final int numero = Database.getGame().getPrismeData().load();
		Console.println("- Chargement des " + numero + " prismes.", Console.Color.GAME);
		Database.getStatique().getArea_dataData().load();
		Database.getGame().getArea_dataData().load();
		Console.println("- Chargement des " + World.Areas.size() + " zones.", Console.Color.GAME);
		Database.getStatique().getSubarea_dataData().load();
		Database.getGame().getSubarea_dataData().load();
		Console.println("- Chargement des " + getSubAreas().size() + " sous zones.", Console.Color.GAME);
		Database.getStatique().getInteractive_objects_dataData().load();
		Console.println("- Chargement des " + World.IOTemplate.size() + " templates d objet interactif.",
				Console.Color.GAME);
		Database.getStatique().getCraftData().load();
		Console.println("- Chargement des " + World.Crafts.size() + " recettes d objet.", Console.Color.GAME);
		Database.getStatique().getJobs_dataData().load();
		Console.println("- Chargement des " + World.Jobs.size() + " metiers.", Console.Color.GAME);
		Database.getStatique().getItemsetData().load();
		Console.println("- Chargement des " + World.ItemSets.size() + " panoplies.", Console.Color.GAME);
		Database.getStatique().getMapData().load();
		Console.println("- Chargement des " + World.Maps.size() + " maps.", Console.Color.GAME);
		Database.getStatique().getSchemafightData().load();
		Console.println("- Chargement des " + World.SchemaFights.size() + " schemafights.", Console.Color.GAME);
		int nbr = Database.getStatique().getScripted_cellData().load();
		Console.println("- Chargement des " + nbr + " triggers.", Console.Color.GAME);
		nbr = Database.getStatique().getEndfight_actionData().load();
		Console.println("- Chargement des " + nbr + " fins d action.", Console.Color.GAME);
		nbr = Database.getStatique().getNpcData().load();
		Console.println("- Chargement des " + nbr + " PNJs.", Console.Color.GAME);
		nbr = Database.getStatique().getObjectsactionData().load();
		Console.println("- Chargement des " + nbr + " actions d objet.", Console.Color.GAME);
		Database.getStatique().getDropData().load();
		Console.println("- Ajout des drops.", Console.Color.GAME);
		Database.getStatique().getAnimationData().load();
		Console.println("- Chargement des " + World.Animations.size() + " animations.", Console.Color.GAME);
		Database.getStatique().getServerData().loggedZero();
		Console.println("- Deconnexion de tous les personnages du serveur " + Main.serverId + ".", Console.Color.GAME);
		Database.getStatique().getAccountData().load();
		Console.println("- Chargement des " + World.Comptes.size() + " comptes.", Console.Color.GAME);
		Database.getStatique().getPlayerData().load();
		Console.println("- Chargement des " + World.Persos.size() + " personnages.", Console.Color.GAME);
		Database.getGame().getGuildData().load();
		Console.println("- Chargement des " + getGuilds().size() + " guildes.", Console.Color.GAME);
		Database.getGame().getGuild_memberData().load();
		Console.println("- Ajouts des membres de guildes.", Console.Color.GAME);
		nbr = Database.getStatique().getPets_dataData().load();
		Console.println("- Chargement des " + nbr + " familiers.", Console.Color.GAME);
		nbr = Database.getStatique().getPetData().load();
		Console.println("- Chargement des " + nbr + " entrees de familier.", Console.Color.GAME);
		Database.getStatique().getMounts_dataData().load();
		Console.println("- Chargement des " + World.Dragodindes.size() + " montures.", Console.Color.GAME);
		Database.getStatique().getTutorielData().load();
		Console.println("- Chargement des " + World.Tutorial.size() + " foires.", Console.Color.GAME);
		Database.getStatique().getMountpark_dataData().load();
		nbr = Database.getGame().getMountpark_dataData().load();
		Console.println("- Chargement des " + nbr + " enclos.", Console.Color.GAME);
		nbr = Database.getGame().getPercepteurData().load();
		Console.println("- Chargement des " + nbr + " percepteurs.", Console.Color.GAME);
		Database.getStatique().getHouseData().load();
		nbr = Database.getGame().getHouseData().load();
		Console.println("- Chargement des " + nbr + " maisons.", Console.Color.GAME);
		Database.getStatique().getCoffreData().load();
		nbr = Database.getGame().getCoffreData().load();
		Console.println("- Chargement des " + nbr + " coffres.", Console.Color.GAME);
		nbr = Database.getStatique().getZaapData().load();
		Console.println("- Chargement des " + nbr + " zaaps.", Console.Color.GAME);
		nbr = Database.getStatique().getZaapiData().load();
		Console.println("- Chargement des " + nbr + " zaapis.", Console.Color.GAME);
		Database.getStatique().getChallengeData().load();
		Console.println("- Chargement des " + World.Challenges.toString().split(";").length + " challenges.",
				Console.Color.GAME);
		Database.getStatique().getHdvData().load();
		Console.println("- Chargement des " + getHdvs().size() + " hotels de vente.", Console.Color.GAME);
		Database.getGame().getHdvs_itemData().load();
		Console.println("- Ajout des items en vente.", Console.Color.GAME);
		Database.getStatique().getDonjonData().load();
		Console.println("- Chargement des " + Dopeul.getDonjons().size() + " donjons.", Console.Color.GAME);
		try {
			loadExtraMonster();
			Console.println("- Ajout des extra-monstres sur les maps.", Console.Color.GAME);
		} catch (Exception e) {
			e.printStackTrace();
			Console.println("Erreur lors de l ajout des extra-monstres sur les maps : " + e.getMessage(),
					Console.Color.ERROR);
		}
		try {
			loadMonsterOnMap();
			Console.println("- Ajout des monstres sur les maps.", Console.Color.GAME);
		} catch (Exception e) {
			e.printStackTrace();
			Console.println("Erreur lors de l ajout des monstres sur les maps : " + e.getMessage(),
					Console.Color.ERROR);
		}
		Database.getStatique().getRuneData().load();
		Console.println("- Chargement des " + Rune.runes.size() + " runes.", Console.Color.GAME);
		World.nextObjetID = Database.getStatique().getItemData().getNextObjetID();
		Console.println("- Chargement des bandits de Cania.", Console.Color.GAME);
		Database.getGame().getBanditData().load();
		Console.println("- Initialisation du labyrinthe du DC.", Console.Color.GAME);
		Dc.initialize();
		Console.println("- Initialisation du labyrinthe du Toror.", Console.Color.GAME);
		Toror.initialize();
		Boutique.initPacket();
		Database.getStatique().getServerData().updateTime(time);
		System.out.println("Le serveur a fini de charger : "
				+ new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.FRANCE).format(new Date()) + "\n"
				+ "Le serveur a \u00e9t\u00e9 charg\u00e9 en "
				+ new SimpleDateFormat("mm", Locale.FRANCE).format(System.currentTimeMillis() - time) + " minutes et "
				+ new SimpleDateFormat("ss", Locale.FRANCE).format(System.currentTimeMillis() - time) + " secondes.");
	}

	public static void addExtraMonster(final int idMob, final String superArea, final String subArea,
			final int chances) {
		final Map<String, Map<String, Integer>> map = new TreeMap<String, Map<String, Integer>>();
		final Map<String, Integer> _map = new HashMap<String, Integer>();
		_map.put(subArea, chances);
		map.put(superArea, _map);
		World.extraMonstre.put(idMob, map);
	}

	public static Map<Integer, org.aestia.map.Map> getExtraMonsterOnMap() {
		return World.extraMonstreOnMap;
	}

	public static void loadExtraMonster() {
		final ArrayList<org.aestia.map.Map> mapPossible = new ArrayList<org.aestia.map.Map>();
		for (final Map.Entry<Integer, Map<String, Map<String, Integer>>> i : World.extraMonstre.entrySet()) {
			Map<String, Map<String, Integer>> map = new TreeMap<String, Map<String, Integer>>();
			map = i.getValue();
			for (final Map.Entry<String, Map<String, Integer>> areaChances : map.entrySet()) {
				Integer chances = null;
				for (final Map.Entry<String, Integer> _e : areaChances.getValue().entrySet()) {
					final Integer _c = _e.getValue();
					if (_c != null && _c != -1) {
						chances = _c;
					}
				}
				if (!areaChances.getKey().equals("")) {
					String[] split;
					for (int length = (split = areaChances.getKey().split(",")).length, j = 0; j < length; ++j) {
						final String ar = split[j];
						final Area Area = World.Areas.get(Integer.parseInt(ar));
						for (final org.aestia.map.Map Map : Area.getMaps()) {
							if (Map == null) {
								continue;
							}
							if (Map.haveMobFix()) {
								continue;
							}
							if (!Map.isPossibleToPutMonster()) {
								continue;
							}
							if (chances != null) {
								Map.addMobExtra(i.getKey(), chances);
							} else {
								if (mapPossible.contains(Map)) {
									continue;
								}
								mapPossible.add(Map);
							}
						}
					}
				}
				if (!areaChances.getValue().equals("")) {
					for (final Map.Entry<String, Integer> area : areaChances.getValue().entrySet()) {
						final String areas = area.getKey();
						String[] split2;
						for (int length2 = (split2 = areas.split(",")).length, k = 0; k < length2; ++k) {
							final String sub = split2[k];
							SubArea subArea = null;
							try {
								subArea = World.SubAreas.get(Integer.parseInt(sub));
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (subArea != null) {
								for (final org.aestia.map.Map Map2 : subArea.getMaps()) {
									if (Map2 != null) {
										if (Map2.equals("")) {
											continue;
										}
										if (Map2.haveMobFix()) {
											continue;
										}
										if (!Map2.isPossibleToPutMonster()) {
											continue;
										}
										if (chances != null) {
											Map2.addMobExtra(i.getKey(), chances);
										}
										if (mapPossible.contains(Map2)) {
											continue;
										}
										mapPossible.add(Map2);
									}
								}
							}
						}
					}
				}
			}
			if (mapPossible.size() <= 0) {
				Console.println("#1# Erreur extraMonster : " + i.getKey(), Console.Color.ERROR);
				mapPossible.clear();
			} else {
				org.aestia.map.Map aleaMap = null;
				if (mapPossible.size() == 1) {
					aleaMap = mapPossible.get(0);
				} else {
					aleaMap = mapPossible.get(Formulas.getRandomValue(0, mapPossible.size() - 1));
				}
				if (aleaMap == null) {
					Console.println("#2# Erreur extraMonster : " + i.getKey(), Console.Color.ERROR);
					mapPossible.clear();
				} else {
					if (getMonstre(i.getKey()) == null) {
						Console.println("#3# Erreur extraMonster : " + i.getKey(), Console.Color.ERROR);
					}
					if (aleaMap.loadExtraMonsterOnMap(i.getKey())) {
						World.extraMonstreOnMap.put(i.getKey(), aleaMap);
					} else {
						Console.println("#4# Erreur extraMonster : " + i.getKey(), Console.Color.ERROR);
					}
					mapPossible.clear();
				}
			}
		}
	}

	public static Map<String, String> getGroupFix(final int map, final int cell) {
		return World.mobGroupFix.get(String.valueOf(map) + ";" + cell);
	}

	public static void addGroupFix(final String str, final String mob, final int Time) {
		World.mobGroupFix.put(str, new HashMap<String, String>());
		World.mobGroupFix.get(str).put("groupData", mob);
		World.mobGroupFix.get(str).put("timer", new StringBuilder(String.valueOf(Time)).toString());
	}

	public static Collection<SubArea> getAllSubArea() {
		return World.SubAreas.values();
	}

	public static void loadMonsterOnMap() {
		for (final org.aestia.map.Map i : World.Maps.values()) {
			if (i == null) {
				continue;
			}
			i.loadMonsterOnMap();
		}
	}

	public static Area getArea(final int areaID) {
		return World.Areas.get(areaID);
	}

	public static SuperArea getSuperArea(final int areaID) {
		return World.SuperAreas.get(areaID);
	}

	public static SubArea getSubArea(final int areaID) {
		return World.SubAreas.get(areaID);
	}

	public static void addArea(final Area area) {
		World.Areas.put(area.get_id(), area);
	}

	public static void addSuperArea(final SuperArea SA) {
		World.SuperAreas.put(SA.get_id(), SA);
	}

	public static void addSubArea(final SubArea SA) {
		World.SubAreas.put(SA.getId(), SA);
	}

	public static String getSousZoneStateString() {
		String str = "";
		boolean first = false;
		for (final SubArea subarea : World.SubAreas.values()) {
			if (!subarea.getConquistable()) {
				continue;
			}
			if (first) {
				str = String.valueOf(str) + "|";
			}
			str = String.valueOf(str) + subarea.getId() + ";" + subarea.getAlignement();
			first = true;
		}
		return str;
	}

	public static void addNpcAnswer(final NpcAnswer rep) {
		World.NpcAnswer.put(rep.getId(), rep);
	}

	public static NpcAnswer getNpcAnswer(final int guid) {
		return World.NpcAnswer.get(guid);
	}

	public static double getBalanceArea(final Area area, final int alignement) {
		int cant = 0;
		for (final SubArea subarea : World.SubAreas.values()) {
			if (subarea.getArea() == area && subarea.getAlignement() == alignement) {
				++cant;
			}
		}
		if (cant == 0) {
			return 0.0;
		}
		return Math.rint(1000 * cant / area.getSubAreas().size() / 10);
	}

	public static double getBalanceWorld(final int alignement) {
		int cant = 0;
		for (final SubArea subarea : World.SubAreas.values()) {
			if (subarea.getAlignement() == alignement) {
				++cant;
			}
		}
		if (cant == 0) {
			return 0.0;
		}
		return Math.rint(10 * cant / 4 / 10);
	}

	public static int getExpLevelSize() {
		return World.ExpLevels.size();
	}

	public static void addExpLevel(final int lvl, final ExpLevel exp) {
		World.ExpLevels.put(lvl, exp);
	}

	public static Account getCompte(final int guid) {
		return World.Comptes.get(guid);
	}

	public static void addNPCQuestion(final NpcQuestion quest) {
		World.NPCQuestions.put(quest.getId(), quest);
	}

	public static NpcQuestion getNPCQuestion(final int guid) {
		return World.NPCQuestions.get(guid);
	}

	public static NpcTemplate getNPCTemplate(final int guid) {
		return World.NPCTemplates.get(guid);
	}

	public static void addNpcTemplate(final NpcTemplate temp) {
		World.NPCTemplates.put(temp.get_id(), temp);
	}

	public static org.aestia.map.Map getMap(final short id) {
		return World.Maps.get(id);
	}

	public static void addMap(final org.aestia.map.Map map) {
		if (!World.Maps.containsKey(map.getId())) {
			World.Maps.put(map.getId(), map);
		}
	}

	public static void delMap(final org.aestia.map.Map map) {
		if (World.Maps.containsKey(map.getId())) {
			World.Maps.remove(map.getId());
		}
	}

	public static Account getCompteByName(final String name) {
		return (World.ComptebyName.get(name.toLowerCase()) != null)
				? World.Comptes.get(World.ComptebyName.get(name.toLowerCase())) : null;
	}

	public static Player getPersonnage(final int guid) {
		return World.Persos.get(guid);
	}

	public static void addAccount(final Account compte) {
		World.Comptes.put(compte.getGuid(), compte);
		World.ComptebyName.put(compte.getName().toLowerCase(), compte.getGuid());
	}

	public static void addAccountbyName(final Account compte) {
		World.ComptebyName.put(compte.getName(), compte.getGuid());
	}

	public static void addPersonnage(final Player perso) {
		World.Persos.put(perso.getId(), perso);
	}

	public static Player getPersoByName(final String name) {
		final ArrayList<Player> Ps = new ArrayList<Player>();
		Ps.addAll(World.Persos.values());
		for (final Player P : Ps) {
			if (P.getName().equalsIgnoreCase(name)) {
				return P;
			}
		}
		return null;
	}

	public static void deletePerso(final Player perso) {
		if (perso.get_guild() != null) {
			if (perso.get_guild().getMembers().size() <= 1) {
				removeGuild(perso.get_guild().getId());
			} else if (perso.getGuildMember().getRank() == 1) {
				final int curMaxRight = 0;
				Player Meneur = null;
				for (final Player newMeneur : perso.get_guild().getMembers()) {
					if (newMeneur == perso) {
						continue;
					}
					if (newMeneur.getGuildMember().getRights() >= curMaxRight) {
						continue;
					}
					Meneur = newMeneur;
				}
				perso.get_guild().removeMember(perso);
				Meneur.getGuildMember().setRank(1);
			} else {
				perso.get_guild().removeMember(perso);
			}
		}
		perso.remove();
		unloadPerso(perso.getId());
		World.Persos.remove(perso.getId());
	}

	public static void unloadPerso(final Player perso) {
		unloadPerso(perso.getId());
		World.Persos.remove(perso.getId());
	}

	public static long getPersoXpMin(int _lvl) {
		if (_lvl > getExpLevelSize()) {
			_lvl = getExpLevelSize();
		}
		if (_lvl < 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl).perso;
	}

	public static long getPersoXpMax(int _lvl) {
		if (_lvl >= getExpLevelSize()) {
			_lvl = getExpLevelSize() - 1;
		}
		if (_lvl <= 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl + 1).perso;
	}

	public static long getTourmenteursXpMin(int _lvl) {
		if (_lvl > getExpLevelSize()) {
			_lvl = getExpLevelSize();
		}
		if (_lvl < 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl).tourmenteurs;
	}

	public static long getTourmenteursXpMax(int _lvl) {
		if (_lvl >= getExpLevelSize()) {
			_lvl = getExpLevelSize() - 1;
		}
		if (_lvl <= 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl + 1).tourmenteurs;
	}

	public static long getBanditsXpMin(int _lvl) {
		if (_lvl > getExpLevelSize()) {
			_lvl = getExpLevelSize();
		}
		if (_lvl < 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl).bandits;
	}

	public static long getBanditsXpMax(int _lvl) {
		if (_lvl >= getExpLevelSize()) {
			_lvl = getExpLevelSize() - 1;
		}
		if (_lvl <= 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl + 1).bandits;
	}

	public static void addSort(final Spell sort) {
		World.Sorts.put(sort.getSpellID(), sort);
	}

	public static Spell getSort(final int id) {
		return World.Sorts.get(id);
	}

	public static Spell delSort(final int id) {
		return World.Sorts.remove(id);
	}

	public static void addObjTemplate(final ObjectTemplate obj) {
		World.ObjTemplates.put(obj.getId(), obj);
	}

	public static ObjectTemplate getObjTemplate(final int id) {
		return World.ObjTemplates.get(id);
	}

	public static boolean updateMapPlaces(final SchemaFight obj, final int id) {
		return Database.getStatique().getMapData().updatePlaces(obj, id);
	}

	public static void editSchema(final SchemaFight obj, final short id) {
		obj.setId(id);
	}

	public static boolean newSchemaFight(final SchemaFight obj) {
		final boolean sql = Database.getStatique().getSchemafightData().add(obj);
		if (sql) {
			addSchemaFight(obj);
		}
		return sql;
	}

	public static boolean delSchemaFight(final SchemaFight obj) {
		final boolean sql = Database.getStatique().getSchemafightData().delete(obj);
		if (sql) {
			World.SchemaFights.remove(obj.getId());
		}
		return sql;
	}

	public static void addSchemaFight(final SchemaFight obj) {
		World.SchemaFights.put(obj.getId(), obj);
	}

	public static SchemaFight getSchemaFight(final int id) {
		for (final Map.Entry<Short, SchemaFight> entry : World.SchemaFights.entrySet()) {
			final Short tmp = entry.getKey();
			if (tmp == id) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static SchemaFight getSchemaFight(final String places) {
		for (final Map.Entry<Short, SchemaFight> entry : World.SchemaFights.entrySet()) {
			final SchemaFight tmp = entry.getValue();
			if (tmp.getPlacesStr().equals(places)) {
				return tmp;
			}
		}
		return null;
	}

	public static Map<Short, SchemaFight> getSchemaFights() {
		return World.SchemaFights;
	}

	public static synchronized int getNewItemGuid() {
		return ++World.nextObjetID;
	}

	public static void addMobTemplate(final int id, final Monster mob) {
		World.MobTemplates.put(id, mob);
	}

	public static Monster getMonstre(final int id) {
		return World.MobTemplates.get(id);
	}

	public static Collection<Monster> getMonstres() {
		return World.MobTemplates.values();
	}

	public static List<Player> getOnlinePersos() {
		final List<Player> online = new ArrayList<Player>();
		for (final Map.Entry<Integer, Player> perso : World.Persos.entrySet()) {
			if (perso.getValue() == null) {
				continue;
			}
			if (!perso.getValue().isOnline() || perso.getValue().getGameClient() == null) {
				continue;
			}
			online.add(perso.getValue());
		}
		return online;
	}

	public static String getStatOfAlign() {
		int ange = 0;
		int demon = 0;
		int total = 0;
		for (final Player i : getPlayers().values()) {
			if (i == null) {
				continue;
			}
			if (i.get_align() == 1) {
				++ange;
			}
			if (i.get_align() == 2) {
				++demon;
			}
			++total;
		}
		ange /= total;
		demon /= total;
		if (ange > demon) {
			return "Les Br\u00e2kmarien sont actuellement en minorit\u00e9, je peux donc te proposer de rejoindre les rangs Br\u00e2kmarien ?";
		}
		if (demon > ange) {
			return "Les Bontarien sont actuellement en minorit\u00e9, je peux donc te proposer de rejoindre les rangs Bontarien ?";
		}
		if (demon == ange) {
			return " Aucune milice est actuellement en minorit\u00e9, je peux donc te proposer de rejoindre al\u00e9atoirement une milice ?";
		}
		return "Undefined";
	}

	public static List<org.aestia.map.Map> getAllMaps() {
		final List<org.aestia.map.Map> allMap = new ArrayList<org.aestia.map.Map>();
		for (final Map.Entry<Short, org.aestia.map.Map> map : World.Maps.entrySet()) {
			if (map != null) {
				allMap.add(map.getValue());
			}
		}
		return allMap;
	}

	public static void addObjet(final org.aestia.object.Object item, final boolean saveSQL) {
		if (item == null) {
			return;
		}
		World.Objets.put(item.getGuid(), item);
		if (saveSQL) {
			Database.getStatique().getItemData().saveNew(item);
		}
	}

	public static org.aestia.object.Object getObjet(final int guid) {
		return World.Objets.get(guid);
	}

	public static void removeItem(final int guid) {
		World.Objets.remove(guid);
		Database.getStatique().getItemData().delete(guid);
	}

	public static void addIOTemplate(final InteractiveObject.InteractiveObjectTemplate IOT) {
		World.IOTemplate.put(IOT.getId(), IOT);
	}

	public static Dragodinde getDragoByID(final int id) {
		return World.Dragodindes.get(id);
	}

	public static void addDragodinde(final Dragodinde DD) {
		World.Dragodindes.put(DD.getId(), DD);
	}

	public static void removeDragodinde(final int DID) {
		World.Dragodindes.remove(DID);
	}

	public static void addTutorial(final Tutorial tutorial) {
		World.Tutorial.put(tutorial.getId(), tutorial);
	}

	public static Tutorial getTutorial(final int id) {
		return World.Tutorial.get(id);
	}

	public static void RefreshAllMob() {
		for (final org.aestia.map.Map map : World.Maps.values()) {
			map.refreshSpawns();
		}
	}

	public static ExpLevel getExpLevel(final int lvl) {
		return World.ExpLevels.get(lvl);
	}

	public static InteractiveObject.InteractiveObjectTemplate getIOTemplate(final int id) {
		return World.IOTemplate.get(id);
	}

	public static Job getMetier(final int id) {
		return World.Jobs.get(id);
	}

	public static void addJob(final Job metier) {
		World.Jobs.put(metier.getId(), metier);
	}

	public static void addCraft(final int id, final ArrayList<Couple<Integer, Integer>> m) {
		World.Crafts.put(id, m);
	}

	public static ArrayList<Couple<Integer, Integer>> getCraft(final int i) {
		return World.Crafts.get(i);
	}

	public static void addFullMorph(final int morphID, final String name, final int gfxID, final String spells,
			final String[] args) {
		if (World.fullmorphs.get(morphID) != null) {
			return;
		}
		World.fullmorphs.put(morphID, new HashMap<String, String>());
		World.fullmorphs.get(morphID).put("name", name);
		World.fullmorphs.get(morphID).put("gfxid", new StringBuilder(String.valueOf(gfxID)).toString());
		World.fullmorphs.get(morphID).put("spells", spells);
		if (args != null) {
			World.fullmorphs.get(morphID).put("vie", args[0]);
			World.fullmorphs.get(morphID).put("pa", args[1]);
			World.fullmorphs.get(morphID).put("pm", args[2]);
			World.fullmorphs.get(morphID).put("vitalite", args[3]);
			World.fullmorphs.get(morphID).put("sagesse", args[4]);
			World.fullmorphs.get(morphID).put("terre", args[5]);
			World.fullmorphs.get(morphID).put("feu", args[6]);
			World.fullmorphs.get(morphID).put("eau", args[7]);
			World.fullmorphs.get(morphID).put("air", args[8]);
			World.fullmorphs.get(morphID).put("initiative", args[9]);
			World.fullmorphs.get(morphID).put("stats", args[10]);
			World.fullmorphs.get(morphID).put("donjon", args[11]);
		}
	}

	public static Map<String, String> getFullMorph(final int morphID) {
		return World.fullmorphs.get(morphID);
	}

	public static int getObjectByIngredientForJob(final ArrayList<Integer> list,
			final Map<Integer, Integer> ingredients) {
		if (list == null) {
			return -1;
		}
		for (final int tID : list) {
			final ArrayList<Couple<Integer, Integer>> craft = getCraft(tID);
			if (craft == null) {
				continue;
			}
			if (craft.size() != ingredients.size()) {
				continue;
			}
			boolean ok = true;
			for (final Couple<Integer, Integer> c : craft) {
				if (!(ingredients.get(c.first) + " ").equals(c.second + " ")) {
					ok = false;
				}
			}
			if (ok) {
				return tID;
			}
		}
		return -1;
	}

	public static Account getCompteByPseudo(final String p) {
		for (final Account C : World.Comptes.values()) {
			if (C.getPseudo().equals(p)) {
				return C;
			}
		}
		return null;
	}

	public static void addItemSet(final ObjectSet itemSet) {
		World.ItemSets.put(itemSet.getId(), itemSet);
	}

	public static ObjectSet getItemSet(final int tID) {
		return World.ItemSets.get(tID);
	}

	public static int getItemSetNumber() {
		return World.ItemSets.size();
	}

	public static int getNextIdForMount() {
		int max = -101;
		for (final int a : World.Dragodindes.keySet()) {
			if (a < max) {
				max = a;
			}
		}
		return max - 3;
	}

	public static org.aestia.map.Map getMapByPosAndCont(final int mapX, final int mapY, final int contID) {
		for (final org.aestia.map.Map map : World.Maps.values()) {
			if (map.getX() == mapX && map.getY() == mapY
					&& map.getSubArea().getArea().get_superArea().get_id() == contID) {
				return map;
			}
		}
		return null;
	}

	public static ArrayList<org.aestia.map.Map> getMapByPosInArray(final int mapX, final int mapY) {
		final ArrayList<org.aestia.map.Map> i = new ArrayList<org.aestia.map.Map>();
		for (final org.aestia.map.Map map : World.Maps.values()) {
			if (map.getX() == mapX && map.getY() == mapY) {
				i.add(map);
			}
		}
		return i;
	}

	public static ArrayList<org.aestia.map.Map> getMapByPosInArrayPlayer(final int mapX, final int mapY,
			final Player p) {
		final ArrayList<org.aestia.map.Map> i = new ArrayList<org.aestia.map.Map>();
		for (final org.aestia.map.Map map : World.Maps.values()) {
			if (map.getX() == mapX && map.getY() == mapY && map.getSubArea().getArea().get_superArea().get_id() == p
					.getCurMap().getSubArea().getArea().get_superArea().get_id()) {
				i.add(map);
			}
		}
		return i;
	}

	public static void addGuild(final Guild g, final boolean save) {
		World.Guildes.put(g.getId(), g);
		if (save) {
			Database.getGame().getGuildData().add(g);
		}
	}

	public static int getNextHighestGuildID() {
		if (World.Guildes.isEmpty()) {
			return 1;
		}
		int n = 0;
		for (final int x : World.Guildes.keySet()) {
			if (n < x) {
				n = x;
			}
		}
		return n + 1;
	}

	public static boolean guildNameIsUsed(final String name) {
		for (final Guild g : World.Guildes.values()) {
			if (g.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean guildEmblemIsUsed(final String emb) {
		for (final Guild g : World.Guildes.values()) {
			if (g.getEmblem().equals(emb)) {
				return true;
			}
		}
		return false;
	}

	public static Guild getGuild(final int i) {
		return World.Guildes.get(i);
	}

	public static int getGuildByName(final String name) {
		for (final Guild g : World.Guildes.values()) {
			if (g.getName().equalsIgnoreCase(name)) {
				return g.getId();
			}
		}
		return -1;
	}

	public static long getGuildXpMax(int _lvl) {
		if (_lvl >= 200) {
			_lvl = 199;
		}
		if (_lvl <= 1) {
			_lvl = 1;
		}
		return World.ExpLevels.get(_lvl + 1).guilde;
	}

	public static void ReassignAccountToChar(final Account C) {
		C.getPersos().clear();
		Database.getStatique().getPlayerData().loadByAccountId(C.getGuid());
		for (final Player P : World.Persos.values()) {
			if (P.getAccID() == C.getGuid()) {
				P.setAccount(C);
			}
		}
	}

	public static int getZaapCellIdByMapId(final short i) {
		for (final Map.Entry<Integer, Integer> zaap : Constant.ZAAPS.entrySet()) {
			if (zaap.getKey() == i) {
				return zaap.getValue();
			}
		}
		return -1;
	}

	public static int getEncloCellIdByMapId(final short i) {
		if (getMap(i).getMountPark() != null && getMap(i).getMountPark().getCell() > 0) {
			return getMap(i).getMountPark().getCell();
		}
		return -1;
	}

	public static void delDragoByID(final int getId) {
		World.Dragodindes.remove(getId);
	}

	public static void removeGuild(final int id) {
		House.removeHouseGuild(id);
		org.aestia.map.Map.removeMountPark(id);
		Collector.removeCollector(id);
		World.Guildes.remove(id);
		Database.getGame().getGuild_memberData().deleteAll(id);
		Database.getGame().getGuildData().delete(id);
	}

	public static boolean ipIsUsed(final String ip) {
		for (final Account c : World.Comptes.values()) {
			if (c.getCurIP().compareTo(ip) == 0) {
				return true;
			}
		}
		return false;
	}

	public static int ipIsUsed2(final String ip) {
		int i = 0;
		for (final Account c : World.Comptes.values()) {
			if (c.getCurIP().compareTo(ip) == 0) {
				++i;
			}
		}
		return i;
	}

	public static boolean ipIsUsed3(final Account acc, final String ip) {
		final Player perso = acc.getCurPerso();
		for (final Account c : World.Comptes.values()) {
			if (c.getCurIP().compareTo(ip) == 0 && c != null) {
				if (acc == null) {
					continue;
				}
				final Player persoC = c.getCurPerso();
				if (persoC.getGroupe() != null && perso.getGroupe() == null) {
					return true;
				}
				if (persoC.getGroupe() == null && perso.getGroupe() != null) {
					return true;
				}
				continue;
			}
		}
		return false;
	}

	public static void unloadPerso(final int g) {
		Player toRem = World.Persos.get(g);
		synchronized (toRem.getItems()) {
			if (!toRem.getItems().isEmpty()) {
				for (final Map.Entry<Integer, org.aestia.object.Object> curObj : toRem.getItems().entrySet()) {
					World.Objets.remove(curObj.getKey());
				}
			}
		}
		// monitorexit(toRem.getItems())
		toRem = null;
	}

	public static org.aestia.object.Object newObjet(final int Guid, final int template, final int qua, final int pos,
			final String strStats, final int puit) {
		if (getObjTemplate(template) == null) {
			Console.println("Erreur items. Template " + template + " inexistant.", Console.Color.ERROR);
			return null;
		}
		if (template == 8378) {
			return new Fragment(Guid, strStats);
		}
		if (getObjTemplate(template).getType() == 85) {
			return new SoulStone(Guid, qua, template, pos, strStats);
		}
		if (getObjTemplate(template).getType() == 24) {
			if (!Constant.isCertificatDopeuls(getObjTemplate(template).getId())) {
				if (getObjTemplate(template).getId() != 6653) {
					return new org.aestia.object.Object(Guid, template, qua, pos, strStats, 0);
				}
			}
			try {
				final Map<Integer, String> txtStat = new TreeMap<Integer, String>();
				txtStat.put(805, new StringBuilder(String.valueOf(strStats.substring(3))).toString());
				return new org.aestia.object.Object(Guid, template, qua, -1, new Stats(false, null),
						new ArrayList<SpellEffect>(), new TreeMap<Integer, Integer>(), txtStat, puit);
			} catch (Exception e) {
				e.printStackTrace();
				return new org.aestia.object.Object(Guid, template, qua, pos, strStats, 0);
			}
		}
		return new org.aestia.object.Object(Guid, template, qua, pos, strStats, 0);
	}

	public static Map<Integer, Integer> getChangeHdv() {
		final Map<Integer, Integer> changeHdv = new HashMap<Integer, Integer>();
		changeHdv.put(8753, 8759);
		changeHdv.put(4607, 4271);
		changeHdv.put(4622, 4216);
		changeHdv.put(4627, 4232);
		changeHdv.put(5112, 4178);
		changeHdv.put(4562, 4183);
		changeHdv.put(8754, 8760);
		changeHdv.put(5317, 4098);
		changeHdv.put(4615, 4247);
		changeHdv.put(4646, 4262);
		changeHdv.put(8756, 8757);
		changeHdv.put(4618, 4174);
		changeHdv.put(4588, 4172);
		changeHdv.put(8482, 10129);
		changeHdv.put(4595, 4287);
		changeHdv.put(4630, 2221);
		changeHdv.put(5311, 4179);
		changeHdv.put(4629, 4299);
		return changeHdv;
	}

	public static int changeHdv(int map) {
		final Map<Integer, Integer> changeHdv = getChangeHdv();
		if (changeHdv.containsKey(map)) {
			map = changeHdv.get(map);
		}
		return map;
	}

	public static Hdv getHdv(final int map) {
		return World.Hdvs.get(changeHdv(map));
	}

	public static synchronized int getNextHdvID() {
		return ++World.nextHdvID;
	}

	public static synchronized void setNextHdvID(final int id) {
		World.nextHdvID = id;
	}

	public static synchronized int getNextHdvItemID() {
		return ++World.nextHdvItemID;
	}

	public static synchronized void setNextHdvItemID(final int id) {
		World.nextHdvItemID = id;
	}

	public static synchronized int getNextLigneID() {
		return ++World.nextLigneID;
	}

	public static synchronized void setNextLigneID(final int id) {
		World.nextLigneID = id;
	}

	public static void addHdvItem(final int compteID, final int hdvID, final HdvEntry toAdd) {
		if (World.hdvsItems.get(compteID) == null) {
			World.hdvsItems.put(compteID, new HashMap<Integer, ArrayList<HdvEntry>>());
		}
		if (World.hdvsItems.get(compteID).get(hdvID) == null) {
			World.hdvsItems.get(compteID).put(hdvID, new ArrayList<HdvEntry>());
		}
		World.hdvsItems.get(compteID).get(hdvID).add(toAdd);
	}

	public static void removeHdvItem(final int compteID, final int hdvID, final HdvEntry toDel) {
		World.hdvsItems.get(compteID).get(hdvID).remove(toDel);
	}

	public static int getHdvNumber() {
		return World.Hdvs.size();
	}

	public static int getHdvObjetsNumber() {
		int size = 0;
		for (final Map<Integer, ArrayList<HdvEntry>> curCompte : World.hdvsItems.values()) {
			for (final ArrayList<HdvEntry> curHdv : curCompte.values()) {
				size += curHdv.size();
			}
		}
		return size;
	}

	public static void addHdv(final Hdv toAdd) {
		World.Hdvs.put(toAdd.getHdvId(), toAdd);
	}

	public static Map<Integer, ArrayList<HdvEntry>> getMyItems(final int compteID) {
		if (World.hdvsItems.get(compteID) == null) {
			World.hdvsItems.put(compteID, new HashMap<Integer, ArrayList<HdvEntry>>());
		}
		return World.hdvsItems.get(compteID);
	}

	public static Collection<ObjectTemplate> getObjTemplates() {
		return World.ObjTemplates.values();
	}

	public static Player getMarried(final int ordre) {
		return World.Married.get(ordre);
	}

	public static void AddMarried(final int ordre, final Player perso) {
		World.Married.remove(ordre);
		World.Married.put(ordre, perso);
	}

	public static void PriestRequest(final Player perso, final org.aestia.map.Map Map, final int IdPretre) {
		final Player Homme = World.Married.get(0);
		final Player Femme = World.Married.get(1);
		if (Homme.getWife() != 0) {
			SocketManager.GAME_SEND_MESSAGE_TO_MAP(Map,
					String.valueOf(Homme.getName()) + " est d\u00e9j\u00e0 marier !",
					Config.getInstance().colorMessage);
			return;
		}
		if (Femme.getWife() != 0) {
			SocketManager.GAME_SEND_MESSAGE_TO_MAP(Map,
					String.valueOf(Femme.getName()) + " est d\u00e9j\u00e0 marier !",
					Config.getInstance().colorMessage);
			return;
		}
		SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(perso.getCurMap(), "", -1, "Pr\u00eatre",
				String.valueOf(perso.getName()) + " acceptez-vous d'\u00e9pouser "
						+ getMarried((perso.getSexe() != 1) ? 1 : 0).getName() + " ?");
		SocketManager.GAME_SEND_WEDDING(Map, 617, (Homme == perso) ? Homme.getId() : Femme.getId(),
				(Homme == perso) ? Femme.getId() : Homme.getId(), IdPretre);
	}

	public static void Wedding(final Player Homme, final Player Femme, final int isOK) {
		if (isOK > 0) {
			SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(Homme.getCurMap(), "", -1, "Pr\u00eatre", "Je d\u00e9clare "
					+ Homme.getName() + " et " + Femme.getName() + " unis par les liens sacr\u00e9s du mariage.");
			Homme.MarryTo(Femme);
			Femme.MarryTo(Homme);
		} else {
			SocketManager.GAME_SEND_Im_PACKET_TO_MAP(Homme.getCurMap(),
					"048;" + Homme.getName() + "~" + Femme.getName());
		}
		World.Married.get(0).setisOK(0);
		World.Married.get(1).setisOK(0);
		World.Married.clear();
	}

	public static Animation getAnimation(final int AnimationId) {
		return World.Animations.get(AnimationId);
	}

	public static void addAnimation(final Animation animation) {
		World.Animations.put(animation.getId(), animation);
	}

	public static void addHouse(final House house) {
		World.Houses.put(house.getId(), house);
	}

	public static House getHouse(final int id) {
		return World.Houses.get(id);
	}

	public static void addCollector(final Collector Collector) {
		World.Collectors.put(Collector.getId(), Collector);
	}

	public static Collector getCollector(final int CollectorID) {
		return World.Collectors.get(CollectorID);
	}

	public static void addTrunk(final Trunk trunk) {
		World.Trunks.put(trunk.getId(), trunk);
	}

	public static Trunk getTrunk(final int id) {
		return World.Trunks.get(id);
	}

	public static void addMountPark(final MountPark mp) {
		World.MountPark.put(mp.getMap().getId(), mp);
	}

	public static Map<Short, MountPark> getMountPark() {
		return World.MountPark;
	}

	public static String parseMPtoGuild(final int GuildID) {
		final Guild G = getGuild(GuildID);
		final byte enclosMax = (byte) Math.floor(G.getLvl() / 10);
		final StringBuilder packet = new StringBuilder();
		packet.append(enclosMax);
		for (final Map.Entry<Short, MountPark> mp : World.MountPark.entrySet()) {
			if (mp.getValue().getGuild() != null && mp.getValue().getGuild().getId() == GuildID) {
				packet.append("|").append(mp.getValue().getMap().getId()).append(";").append(mp.getValue().getSize())
						.append(";").append(mp.getValue().getMaxObject());
				if (mp.getValue().getListOfRaising().size() <= 0) {
					continue;
				}
				packet.append(";");
				boolean primero = false;
				for (final Integer id : mp.getValue().getListOfRaising()) {
					final Dragodinde dd = getDragoByID(id);
					if (dd != null) {
						if (primero) {
							packet.append(",");
						}
						packet.append(String.valueOf(dd.getColor()) + "," + dd.getName() + ",");
						if (getPersonnage(dd.getPerso()) == null) {
							packet.append("Sans maitre");
						} else {
							packet.append(getPersonnage(dd.getPerso()).getName());
						}
						primero = true;
					}
				}
			}
		}
		return packet.toString();
	}

	public static int totalMPGuild(final int GuildID) {
		int i = 0;
		for (final Map.Entry<Short, MountPark> mp : World.MountPark.entrySet()) {
			if (mp.getValue().getGuild() != null && mp.getValue().getGuild().getId() == GuildID) {
				++i;
			}
		}
		return i;
	}

	public static void addChallenge(final String chal) {
		if (!World.Challenges.toString().isEmpty()) {
			World.Challenges.append(";");
		}
		World.Challenges.append(chal);
	}

	public static synchronized void addPrisme(final Prism Prisme) {
		World.Prismes.put(Prisme.getId(), Prisme);
	}

	public static Prism getPrisme(final int id) {
		return World.Prismes.get(id);
	}

	public static void removePrisme(final int id) {
		World.Prismes.remove(id);
	}

	public static Collection<Prism> AllPrisme() {
		if (World.Prismes.size() > 0) {
			return World.Prismes.values();
		}
		return null;
	}

	public static String PrismesGeoposition(final int alignement) {
		String str = "";
		boolean first = false;
		int subareas = 0;
		for (final SubArea subarea : World.SubAreas.values()) {
			if (!subarea.getConquistable()) {
				continue;
			}
			if (first) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + subarea.getId() + ","
					+ ((subarea.getAlignement() == 0) ? -1 : subarea.getAlignement()) + ",0,";
			if (getPrisme(subarea.getPrismId()) == null) {
				str = String.valueOf(str) + "0,1";
			} else {
				str = String.valueOf(str) + ((subarea.getPrismId() == 0) ? 0 : getPrisme(subarea.getPrismId()).getMap())
						+ ",1";
			}
			first = true;
			++subareas;
		}
		if (alignement == 1) {
			str = String.valueOf(str) + "|" + Area._bontas;
		} else if (alignement == 2) {
			str = String.valueOf(str) + "|" + Area._brakmars;
		}
		str = String.valueOf(str) + "|" + World.Areas.size() + "|";
		first = false;
		for (final Area area : World.Areas.values()) {
			if (area.getalignement() == 0) {
				continue;
			}
			if (first) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + area.get_id() + "," + area.getalignement() + ",1,"
					+ ((area.getPrismeID() != 0) ? 1 : 0);
			first = true;
		}
		if (alignement == 1) {
			str = String.valueOf(Area._bontas) + "|" + subareas + "|" + (subareas - (SubArea.bontas + SubArea.brakmars))
					+ "|" + str;
		} else if (alignement == 2) {
			str = String.valueOf(Area._brakmars) + "|" + subareas + "|"
					+ (subareas - (SubArea.bontas + SubArea.brakmars)) + "|" + str;
		}
		return str;
	}

	public static void showPrismes(final Player perso) {
		for (final SubArea subarea : World.SubAreas.values()) {
			if (subarea.getAlignement() == 0) {
				continue;
			}
			SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(perso,
					String.valueOf(subarea.getId()) + "|" + subarea.getAlignement() + "|1");
		}
	}

	public static synchronized int getNextIDPrisme() {
		int max = -102;
		for (final int a : World.Prismes.keySet()) {
			if (a < max) {
				max = a;
			}
		}
		return max - 3;
	}

	public static void addPets(final Pet pets) {
		World.Pets.put(pets.getTemplateId(), pets);
	}

	public static Pet getPets(final int Tid) {
		return World.Pets.get(Tid);
	}

	public static void addPetsEntry(final PetEntry pets) {
		World.PetsEntry.put(pets.getObjectId(), pets);
	}

	public static PetEntry getPetsEntry(final int guid) {
		return World.PetsEntry.get(guid);
	}

	public static PetEntry removePetsEntry(final int guid) {
		return World.PetsEntry.remove(guid);
	}

	public static String getChallengeFromConditions(final boolean sevEnn, final boolean sevAll, final boolean bothSex,
			final boolean EvenEnn, final boolean MoreEnn, final boolean hasCaw, final boolean hasChaf,
			final boolean hasRoul, final boolean hasArak, final int isBoss, final boolean ecartLvlPlayer,
			final boolean hasArround, final boolean hasDisciple, final boolean isSolo) {
		final StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		boolean isGood = false;
		int cond = 0;
		String[] split;
		for (int length = (split = World.Challenges.toString().split(";")).length, i = 0; i < length; ++i) {
			final String chal = split[i];
			if (!isFirst && isGood) {
				toReturn.append(";");
			}
			isGood = true;
			final int id = Integer.parseInt(chal.split(",")[0]);
			cond = Integer.parseInt(chal.split(",")[4]);
			if ((cond & 0x1) == 0x1 && !sevEnn) {
				isGood = false;
			}
			if ((cond >> 1 & 0x1) == 0x1 && !sevAll) {
				isGood = false;
			}
			if ((cond >> 2 & 0x1) == 0x1 && !bothSex) {
				isGood = false;
			}
			if ((cond >> 3 & 0x1) == 0x1 && !EvenEnn) {
				isGood = false;
			}
			if ((cond >> 4 & 0x1) == 0x1 && !MoreEnn) {
				isGood = false;
			}
			if (!hasCaw && id == 7) {
				isGood = false;
			}
			if (!hasChaf && id == 12) {
				isGood = false;
			}
			if (!hasRoul && id == 14) {
				isGood = false;
			}
			if (!hasArak && id == 15) {
				isGood = false;
			}
			if (!ecartLvlPlayer && id == 48) {
				isGood = false;
			}
			if (isBoss != -1 && id == 5) {
				isGood = false;
			}
			if (!hasArround && id == 36) {
				isGood = false;
			}
			if (!hasDisciple && id == 19) {
				isGood = false;
			}
			switch (id) {
			case 44:
			case 45:
			case 46:
			case 47: {
				if (isSolo) {
					isGood = false;
					break;
				}
				break;
			}
			}
			Label_0879: {
				switch (isBoss) {
				case 1045: {
					switch (id) {
					case 1:
					case 2:
					case 8:
					case 37: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 1072:
				case 1085:
				case 1086:
				case 1087: {
					switch (id) {
					case 20:
					case 36: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 1071: {
					switch (id) {
					case 9:
					case 17:
					case 22:
					case 47: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 780: {
					switch (id) {
					case 3:
					case 4:
					case 25:
					case 31:
					case 32:
					case 34:
					case 35: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 113: {
					switch (id) {
					case 7:
					case 12:
					case 15:
					case 41: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 612: {
					switch (id) {
					case 20:
					case 37: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 478:
				case 568:
				case 940: {
					switch (id) {
					case 20: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 1188: {
					switch (id) {
					case 20:
					case 44:
					case 46: {
						isGood = false;
						break;
					}
					}
					break;
				}
				case 865:
				case 866: {
					switch (id) {
					case 31:
					case 32: {
						isGood = false;
						break Label_0879;
					}
					}
					break;
				}
				}
			}
			if (isGood) {
				toReturn.append(chal);
			}
			isFirst = false;
		}
		return toReturn.toString();
	}

	public static void verifyClone(final Player p) {
		if (p.getCurCell() != null && p.get_fight() == null && p.getCurCell().getCharacters().containsKey(p.getId())) {
			p.getCurCell().removeCharacter(p.getId());
			Database.getStatique().getPlayerData().update(p, true);
		}
		if (p.isOnline()) {
			Database.getStatique().getPlayerData().update(p, true);
		}
	}

	public static ArrayList<String> getRandomChallenge(final int nombreChal, final String challenges) {
		final String MovingChals = ";1;2;8;36;37;39;40;";
		boolean hasMovingChal = false;
		final String TargetChals = ";3;4;10;25;31;32;34;35;38;42;";
		boolean hasTargetChal = false;
		final String SpellChals = ";5;6;9;11;19;20;24;41;";
		boolean hasSpellChal = false;
		final String KillerChals = ";28;29;30;44;45;46;48;";
		boolean hasKillerChal = false;
		final String HealChals = ";18;43;";
		boolean hasHealChal = false;
		int compteur = 0;
		int i = 0;
		final ArrayList<String> toReturn = new ArrayList<String>();
		String chal = new String();
		while (compteur < 100 && toReturn.size() < nombreChal) {
			++compteur;
			i = Formulas.getRandomValue(1, challenges.split(";").length);
			chal = challenges.split(";")[i - 1];
			if (!toReturn.contains(chal)) {
				if (MovingChals.contains(";" + chal.split(",")[0] + ";")) {
					if (!hasMovingChal) {
						hasMovingChal = true;
						toReturn.add(chal);
						continue;
					}
					continue;
				} else if (TargetChals.contains(";" + chal.split(",")[0] + ";")) {
					if (!hasTargetChal) {
						hasTargetChal = true;
						toReturn.add(chal);
						continue;
					}
					continue;
				} else if (SpellChals.contains(";" + chal.split(",")[0] + ";")) {
					if (!hasSpellChal) {
						hasSpellChal = true;
						toReturn.add(chal);
						continue;
					}
					continue;
				} else if (KillerChals.contains(";" + chal.split(",")[0] + ";")) {
					if (!hasKillerChal) {
						hasKillerChal = true;
						toReturn.add(chal);
						continue;
					}
					continue;
				} else if (HealChals.contains(";" + chal.split(",")[0] + ";")) {
					if (!hasHealChal) {
						hasHealChal = true;
						toReturn.add(chal);
						continue;
					}
					continue;
				} else {
					toReturn.add(chal);
				}
			}
			++compteur;
		}
		return toReturn;
	}

	public static Collector getCollectorById(final int id) {
		return getCollectors().get(id);
	}

	public static Collector getCollectorByMap(final int id) {
		for (final Map.Entry<Integer, Collector> Collector : getCollectors().entrySet()) {
			final org.aestia.map.Map map = getMap(Collector.getValue().getMap());
			if (map.getId() == id) {
				return Collector.getValue();
			}
		}
		return null;
	}

	public static List<Player> getPersonnageOnSameMap(final int IDmap) {
		final List<Player> present = new ArrayList<Player>();
		for (final Map.Entry<Integer, Player> perso : World.Persos.entrySet()) {
			if (perso.getValue().getCurMap().getId() == IDmap) {
				present.add(perso.getValue());
			}
		}
		return present;
	}

	public static void reloadPlayerGroup() {
		for (final GameClient client : Main.gameServer.getClients().values()) {
			if (client == null) {
				continue;
			}
			if (client.getPersonnage() == null) {
				continue;
			}
			Database.getStatique().getPlayerData().reloadGroup(client.getPersonnage());
		}
	}

	public static void reloadDrops() {
		Database.getStatique().getDropData().reload();
	}

	public static void reloadEndFightActions() {
		Database.getStatique().getEndfight_actionData().reload();
	}

	public static void reloadNpcs() {
		Database.getStatique().getNpc_templateData().reload();
		World.NPCQuestions.clear();
		Database.getStatique().getNpc_questionData().load();
		World.NpcAnswer.clear();
		Database.getStatique().getNpc_reponses_actionData().load();
	}

	public static void reloadHouses() {
		World.Houses.clear();
		Database.getStatique().getHouseData().load();
		Database.getGame().getHouseData().load();
	}

	public static void reloadTrunks() {
		World.Trunks.clear();
		Database.getStatique().getCoffreData().load();
		Database.getGame().getCoffreData().load();
	}

	public static void reloadMaps() {
		Database.getStatique().getMapData().reload();
	}

	public static void reloadMountParks(final int i) {
		Database.getStatique().getMountpark_dataData().reload(i);
		Database.getGame().getMountpark_dataData().reload(i);
	}

	public static void reloadMonsters() {
		Database.getStatique().getMonsterData().reload();
	}

	public static void reloadQuests() {
		Database.getStatique().getQuest_dataData().load();
	}

	public static void reloadObjectsActions() {
		Database.getStatique().getObjectsactionData().reload();
	}

	public static void reloadSpells() {
		Database.getStatique().getSortData().load();
	}

	public static void reloadItems() {
		Database.getStatique().getItem_templateData().load();
	}

	public static void addSeller(final Player player) {
		synchronized (player.getStoreItems()) {
			if (player.getStoreItems().isEmpty()) {
				// monitorexit(player.getStoreItems())
				return;
			}
		}
		// monitorexit(player.getStoreItems())
		final short map = player.getCurMap().getId();
		if (World.Seller.get(map) == null) {
			final ArrayList<Integer> players = new ArrayList<Integer>();
			players.add(player.getId());
			World.Seller.put(map, players);
		} else {
			final ArrayList<Integer> players = new ArrayList<Integer>();
			players.add(player.getId());
			players.addAll(World.Seller.get(map));
			World.Seller.remove(map);
			World.Seller.put(map, players);
		}
	}

	public static Collection<Integer> getSeller(final short map) {
		return World.Seller.get(map);
	}

	public static void removeSeller(final int player, final short map) {
		World.Seller.get(map).remove(player);
	}

	public static double getPwrPerEffet(final int effect) {
		double r = 0.0;
		switch (effect) {
		case 111: {
			r = 100.0;
			break;
		}
		case 78: {
			r = 90.0;
			break;
		}
		case 110: {
			r = 0.25;
			break;
		}
		case 114: {
			r = 100.0;
			break;
		}
		case 115: {
			r = 30.0;
			break;
		}
		case 117: {
			r = 51.0;
			break;
		}
		case 118: {
			r = 1.0;
			break;
		}
		case 119: {
			r = 1.0;
			break;
		}
		case 120: {
			r = 100.0;
			break;
		}
		case 112: {
			r = 20.0;
			break;
		}
		case 122: {
			r = 1.0;
			break;
		}
		case 123: {
			r = 1.0;
			break;
		}
		case 124: {
			r = 3.0;
			break;
		}
		case 125: {
			r = 0.25;
			break;
		}
		case 126: {
			r = 1.0;
			break;
		}
		case 128: {
			r = 90.0;
			break;
		}
		case 138: {
			r = 2.0;
			break;
		}
		case 142: {
			r = 2.0;
			break;
		}
		case 158: {
			r = 0.25;
			break;
		}
		case 160: {
			r = 1.0;
			break;
		}
		case 161: {
			r = 1.0;
			break;
		}
		case 174: {
			r = 0.1;
			break;
		}
		case 176: {
			r = 3.0;
			break;
		}
		case 178: {
			r = 20.0;
			break;
		}
		case 182: {
			r = 30.0;
			break;
		}
		case 210: {
			r = 6.0;
			break;
		}
		case 211: {
			r = 6.0;
			break;
		}
		case 212: {
			r = 6.0;
			break;
		}
		case 213: {
			r = 6.0;
			break;
		}
		case 214: {
			r = 6.0;
			break;
		}
		case 225: {
			r = 15.0;
			break;
		}
		case 226: {
			r = 2.0;
			break;
		}
		case 240: {
			r = 2.0;
			break;
		}
		case 241: {
			r = 2.0;
			break;
		}
		case 242: {
			r = 2.0;
			break;
		}
		case 243: {
			r = 2.0;
			break;
		}
		case 244: {
			r = 2.0;
			break;
		}
		case 250: {
			r = 6.0;
			break;
		}
		case 251: {
			r = 6.0;
			break;
		}
		case 252: {
			r = 6.0;
			break;
		}
		case 253: {
			r = 6.0;
			break;
		}
		case 254: {
			r = 6.0;
			break;
		}
		case 260: {
			r = 2.0;
			break;
		}
		case 261: {
			r = 2.0;
			break;
		}
		case 262: {
			r = 2.0;
			break;
		}
		case 263: {
			r = 2.0;
			break;
		}
		case 264: {
			r = 2.0;
			break;
		}
		}
		return r;
	}

	public static double getOverPerEffet(final int effect) {
		double r = 0.0;
		switch (effect) {
		case 111: {
			r = 0.0;
			break;
		}
		case 78: {
			r = 404.0;
			break;
		}
		case 110: {
			r = 404.0;
			break;
		}
		case 114: {
			r = 0.0;
			break;
		}
		case 115: {
			r = 3.0;
			break;
		}
		case 117: {
			r = 0.0;
			break;
		}
		case 118: {
			r = 101.0;
			break;
		}
		case 119: {
			r = 101.0;
			break;
		}
		case 120: {
			r = 0.0;
			break;
		}
		case 112: {
			r = 5.0;
			break;
		}
		case 122: {
			r = 0.0;
			break;
		}
		case 123: {
			r = 101.0;
			break;
		}
		case 124: {
			r = 33.0;
			break;
		}
		case 125: {
			r = 404.0;
			break;
		}
		case 126: {
			r = 101.0;
			break;
		}
		case 128: {
			r = 0.0;
			break;
		}
		case 138: {
			r = 50.0;
			break;
		}
		case 142: {
			r = 50.0;
			break;
		}
		case 158: {
			r = 404.0;
			break;
		}
		case 160: {
			r = 0.0;
			break;
		}
		case 161: {
			r = 0.0;
			break;
		}
		case 174: {
			r = 1010.0;
			break;
		}
		case 176: {
			r = 33.0;
			break;
		}
		case 178: {
			r = 5.0;
			break;
		}
		case 182: {
			r = 3.0;
			break;
		}
		case 210: {
			r = 16.0;
			break;
		}
		case 211: {
			r = 16.0;
			break;
		}
		case 212: {
			r = 16.0;
			break;
		}
		case 213: {
			r = 16.0;
			break;
		}
		case 214: {
			r = 16.0;
			break;
		}
		case 225: {
			r = 6.0;
			break;
		}
		case 226: {
			r = 50.0;
			break;
		}
		case 240: {
			r = 50.0;
			break;
		}
		case 241: {
			r = 50.0;
			break;
		}
		case 242: {
			r = 50.0;
			break;
		}
		case 243: {
			r = 50.0;
			break;
		}
		case 244: {
			r = 50.0;
			break;
		}
		case 250: {
			r = 16.0;
			break;
		}
		case 251: {
			r = 16.0;
			break;
		}
		case 252: {
			r = 16.0;
			break;
		}
		case 253: {
			r = 16.0;
			break;
		}
		case 254: {
			r = 16.0;
			break;
		}
		case 260: {
			r = 50.0;
			break;
		}
		case 261: {
			r = 50.0;
			break;
		}
		case 262: {
			r = 50.0;
			break;
		}
		case 263: {
			r = 50.0;
			break;
		}
		case 264: {
			r = 50.0;
			break;
		}
		}
		return r;
	}

	public static double getTauxObtentionIntermediaire(final double bonus, final boolean b1, final boolean b2) {
		double taux = bonus;
		if (b1) {
			if (bonus == 100.0) {
				taux += 2.0 * getTauxObtentionIntermediaire(30.0, true, b2);
			}
			if (bonus == 30.0) {
				taux += 2.0 * getTauxObtentionIntermediaire(10.0, !b2, b2);
			}
			if (bonus == 10.0) {
				taux += 2.0 * getTauxObtentionIntermediaire(3.0, b2, b2);
			} else if (bonus == 3.0) {
				taux += 2.0 * getTauxObtentionIntermediaire(1.0, false, b2);
			}
		}
		return taux;
	}

	public static int getMetierByMaging(final int idMaging) {
		int mId = -1;
		switch (idMaging) {
		case 43: {
			mId = 17;
			break;
		}
		case 44: {
			mId = 11;
			break;
		}
		case 45: {
			mId = 14;
			break;
		}
		case 46: {
			mId = 20;
			break;
		}
		case 47: {
			mId = 31;
			break;
		}
		case 48: {
			mId = 13;
			break;
		}
		case 49: {
			mId = 19;
			break;
		}
		case 50: {
			mId = 18;
			break;
		}
		case 62: {
			mId = 15;
			break;
		}
		case 63: {
			mId = 16;
			break;
		}
		case 64: {
			mId = 27;
			break;
		}
		}
		return mId;
	}

	public static int getTempleByClasse(final int classe) {
		int temple = -1;
		switch (classe) {
		case 1: {
			temple = 1554;
			break;
		}
		case 2: {
			temple = 1546;
			break;
		}
		case 3: {
			temple = 1470;
			break;
		}
		case 4: {
			temple = 6926;
			break;
		}
		case 5: {
			temple = 1469;
			break;
		}
		case 6: {
			temple = 1544;
			break;
		}
		case 7: {
			temple = 6928;
			break;
		}
		case 8: {
			temple = 1549;
			break;
		}
		case 9: {
			temple = 1558;
			break;
		}
		case 10: {
			temple = 1466;
			break;
		}
		case 11: {
			temple = 6949;
			break;
		}
		case 12: {
			temple = 8490;
			break;
		}
		}
		return temple;
	}

	public static class Drop {
		private int template;
		private int prospection;
		private float taux;
		private int max;
		private int level;
		private int action;
		private int max_combat;
		private String condition;

		public Drop(final int template, final int prospection, final float taux, final int max, final int max_combat,
				final int level, final int action, final String condition) {
			this.template = template;
			this.prospection = prospection;
			this.taux = taux;
			this.max = max;
			this.setMax_combat(max_combat);
			this.level = level;
			this.action = action;
			this.condition = condition;
		}

		public int getTemplateId() {
			return this.template;
		}

		public int getProspection() {
			return this.prospection;
		}

		public float getTaux() {
			return this.taux;
		}

		public void setMax(final int max) {
			this.max = max;
		}

		public int getMax() {
			return this.max;
		}

		public int getLevel() {
			return this.level;
		}

		public int getAction() {
			return this.action;
		}

		public String getCondition() {
			return this.condition;
		}

		public int getMax_combat() {
			return this.max_combat;
		}

		public void setMax_combat(final int max_combat) {
			this.max_combat = max_combat;
		}
	}

	public static class SuperArea {
		private int _id;
		private ArrayList<Area> _areas;

		public SuperArea(final int a_id) {
			this._areas = new ArrayList<Area>();
			this._id = a_id;
		}

		public void addArea(final Area A) {
			this._areas.add(A);
		}

		public int get_id() {
			return this._id;
		}
	}

	public static class Area {
		private int _id;
		private SuperArea _superArea;
		private String _name;
		private ArrayList<SubArea> _subAreas;
		private int _alignement;
		public static int _bontas;
		public static int _brakmars;
		private int _Prisme;

		static {
			Area._bontas = 0;
			Area._brakmars = 0;
		}

		public Area(final int id, final int superArea, final String nom) {
			this._subAreas = new ArrayList<SubArea>();
			this._Prisme = 0;
			this._id = id;
			this._name = nom;
			this._superArea = World.getSuperArea(superArea);
			if (this._superArea == null) {
				World.addSuperArea(this._superArea = new SuperArea(superArea));
			}
			this._alignement = 0;
			this._Prisme = 0;
		}

		public String get_name() {
			return this._name;
		}

		public int get_id() {
			return this._id;
		}

		public int getalignement() {
			return this._alignement;
		}

		public int getPrismeID() {
			return this._Prisme;
		}

		public void setPrismeID(final int Prisme) {
			this._Prisme = Prisme;
		}

		public void setalignement(final int alignement) {
			if (this._alignement == 1 && alignement == -1) {
				--Area._bontas;
			} else if (this._alignement == 2 && alignement == -1) {
				--Area._brakmars;
			} else if (this._alignement == -1 && alignement == 1) {
				++Area._bontas;
			} else if (this._alignement == -1 && alignement == 2) {
				++Area._brakmars;
			}
			this._alignement = alignement;
		}

		public SuperArea get_superArea() {
			return this._superArea;
		}

		public void addSubArea(final SubArea sa) {
			this._subAreas.add(sa);
		}

		public ArrayList<SubArea> getSubAreas() {
			return this._subAreas;
		}

		public ArrayList<org.aestia.map.Map> getMaps() {
			final ArrayList<org.aestia.map.Map> maps = new ArrayList<org.aestia.map.Map>();
			for (final SubArea SA : this._subAreas) {
				maps.addAll(SA.getMaps());
			}
			return maps;
		}
	}

	public static class SubArea {
		private int id;
		private Area area;
		private int alignement;
		private String name;
		private ArrayList<org.aestia.map.Map> maps;
		private boolean conquistable;
		private int prism;
		public static int bontas;
		public static int brakmars;

		static {
			SubArea.bontas = 0;
			SubArea.brakmars = 0;
		}

		public SubArea(final int id, final int area, final String name) {
			this.maps = new ArrayList<org.aestia.map.Map>();
			this.id = id;
			this.name = name;
			this.area = World.getArea(area);
		}

		public void setConquistable(final int conquistable) {
			this.conquistable = (conquistable == 0);
		}

		public int getId() {
			return this.id;
		}

		public Area getArea() {
			return this.area;
		}

		public int getAlignement() {
			return this.alignement;
		}

		public void setAlignement(final int alignement) {
			if (this.alignement == 1 && alignement == -1) {
				--SubArea.bontas;
			} else if (this.alignement == 2 && alignement == -1) {
				--SubArea.brakmars;
			} else if (this.alignement == -1 && alignement == 1) {
				++SubArea.bontas;
			} else if (this.alignement == -1 && alignement == 2) {
				++SubArea.brakmars;
			}
			this.alignement = alignement;
		}

		public String getName() {
			return this.name;
		}

		public ArrayList<org.aestia.map.Map> getMaps() {
			return this.maps;
		}

		public void addMap(final org.aestia.map.Map Map) {
			this.maps.add(Map);
		}

		public boolean getConquistable() {
			return this.conquistable;
		}

		public int getPrismId() {
			return this.prism;
		}

		public void setPrismId(final int prism) {
			this.prism = prism;
		}
	}

	public static class Couple<L, R> {
		public L first;
		public R second;

		public Couple(final L s, final R i) {
			this.first = s;
			this.second = i;
		}
	}

	public static class ExpLevel {
		public long perso;
		public int metier;
		public int dinde;
		public int pvp;
		public long guilde;
		public long tourmenteurs;
		public long bandits;

		public ExpLevel(final long c, final int m, final int d, final int p, final long t, final long b) {
			this.perso = c;
			this.metier = m;
			this.dinde = d;
			this.pvp = p;
			this.guilde = this.perso * 10L;
			this.tourmenteurs = t;
			this.bandits = b;
		}
	}
}
