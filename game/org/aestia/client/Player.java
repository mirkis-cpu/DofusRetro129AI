// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.aestia.client.other.Group;
import org.aestia.client.other.Restriction;
import org.aestia.client.other.Stalk;
import org.aestia.client.other.Stats;
import org.aestia.command.server.Groupes;
import org.aestia.common.ConditionParser;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.Pet;
import org.aestia.entity.PetEntry;
import org.aestia.entity.Prism;
import org.aestia.entity.exchange.PlayerExchange;
import org.aestia.entity.monster.Monster;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.GameAction;
import org.aestia.game.GameClient;
import org.aestia.game.GameServer;
import org.aestia.game.scheduler.GlobalManager;
import org.aestia.game.scheduler.Manageable;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.job.Job;
import org.aestia.job.JobAction;
import org.aestia.job.JobConstant;
import org.aestia.job.JobStat;
import org.aestia.job.maging.Concasseur;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.kernel.Reboot;
import org.aestia.map.Case;
import org.aestia.map.InteractiveObject;
import org.aestia.map.MountPark;
import org.aestia.map.laby.Dc;
import org.aestia.map.laby.Toror;
import org.aestia.object.ObjectSet;
import org.aestia.object.ObjectTemplate;
import org.aestia.other.Dopeul;
import org.aestia.other.Guild;
import org.aestia.other.House;
import org.aestia.other.Trunk;
import org.aestia.other.Tutorial;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

public class Player {
	public boolean boutique = false;
	public int lastVita;
	
	private int id;
	private String name;
	private int sexe;
	private int classe;
	private int color1;
	private int color2;
	private int color3;
	private int level;
	private int energy;
	private long exp;
	private int curPdv;
	private int maxPdv;
	public Stats stats;
	private Stats statsParcho;
	private long _kamas;
	private int _spellPts;
	private int _capital;
	private int _size;
	private int _gfxID;
	private int _orientation;
	private Account _compte;
	private int _accID;
	private boolean canAggro;
	public boolean isNew;
	private Map<Integer, Integer> staticEmote;
	private Map<Integer, Integer> dynamicEmote;
	private byte _align;
	private int _deshonor;
	private int _honor;
	private boolean _showWings;
	private int _aLvl;
	private Guild.GuildMember _guildMember;
	private boolean _showFriendConnection;
	private String _canaux;
	private Fight _fight;
	private boolean _away;
	private org.aestia.map.Map curMap;
	private Case curCell;
	private boolean _ready;
	private boolean _isOnline;
	private Group _group;
	private int _duelID;
	private Map<Integer, SpellEffect> _buffs;
	private Map<Integer, org.aestia.object.Object> _items;
	private String _savePos;
	private int _emoteActive;
	private int savestat;
	private int _isTradingWith;
	private ArrayList<Integer> isCraftingType;
	private PlayerExchange curExchange;
	private PlayerExchange.NpcExchange curNpcExchange;
	private PlayerExchange.NpcExchangePets curNpcExchangePets;
	private PlayerExchange.NpcRessurectPets curNpcRessurectPets;
	private int _isTalkingWith;
	private int _inviting;
	public boolean _isCraft;
	private JobAction _curJobAction;
	private Map<Integer, JobStat> _metiers;
	private MountPark inMountPark;
	private Dragodinde _mount;
	private int _mountXpGive;
	private boolean _onMount;
	public boolean _inDD;
	private boolean _isInBank;
	private boolean _isZaaping;
	private ArrayList<Short> _zaaps;
	public boolean _isAbsent;
	public boolean _isInvisible;
	private boolean _isForgetingSpell;
	private Map<Integer, Spell.SortStats> _sorts;
	private Map<Integer, Character> _sortsPlaces;
	public boolean _isClone;
	private int _isOnCollectorID;
	public Manageable dialogTimer;
	public Manageable pointsTimer;
	public Manageable checkVote;
	private byte _title;
	private int _wife;
	private int _isOK;
	public Map<Integer, Player> _Follower;
	public Player _Follows;
	private int isDead;
	private boolean isGhost;
	private int _Speed;
	private Trunk _curTrunk;
	private House _curHouse;
	private boolean _seeSeller;
	private Map<Integer, Integer> _storeItems;
	private boolean _metierPublic;
	private boolean _livreArti;
	private Concasseur concasseur;
	private int hasEndFight;
	private Monster.MobGroup hasMobGroup;
	private ArrayList<Integer> _itemClasse;
	private Map<Integer, World.Couple<Integer, Integer>> _itemClasseSpell;
	private int _bendHechizo;
	private int _bendEfecto;
	private int _bendModif;
	private long _timeInTaverne;
	private GameAction _gameAction;
	private boolean _changeName;
	private boolean _spec;
	private Stalk _traqued;
	private boolean doAction;
	public boolean isInEnnemyFaction;
	public long enteredOnEnnemyFaction;
	private boolean _morphMode;
	private int _morphId;
	private Map<Integer, Spell.SortStats> _saveSorts;
	private Map<Integer, Character> _saveSortsPlaces;
	private int _saveSpellPts;
	private int pa;
	private int pm;
	private int vitalite;
	private int sagesse;
	private int terre;
	private int feu;
	private int eau;
	private int air;
	private int initiative;
	private boolean useStats;
	public boolean donjon;
	private boolean useCac;
	private short oldMap;
	private int oldCell;
	private Tutorial tutorial;
	private String _allTitle;
	private boolean isBlocked;
	private int action;
	public long timeToSave;
	private boolean sitted;
	private int regenRate;
	private long regenTime;
	public int thatMap;
	public int thatCell;
	public boolean walkFast;
	public boolean getCases;
	public ArrayList<Integer> thisCases;
	public ArrayList<org.aestia.map.Map> mapGeoPos;
	public ArrayList<org.aestia.map.Map> allMap;
	public ArrayList<Integer> itemLost;
	public boolean mpToTp;
	private boolean isInPrivateArea;
	private Groupes groupe;
	private boolean isInvisible;
	public final Restriction restriction;
	public boolean noall;
	private Map<Integer, Quest.Quest_Perso> questList;
	private ArrayList<Integer> shootingMonster;

	public Player(final int id, final String name, final int groupe, final int sexe, final int classe, final int color1,
			final int color2, final int color3, final long _kamas, final int pts, final int _capital, final int energy,
			final int level, final long exp, final int _size, final int _gfxid, final byte alignement,
			final int _compte, final Map<Integer, Integer> stats, final byte seeFriend, final byte seeAlign,
			final byte seeSeller, final String canaux, final short map, final int cell, String stuff,
			final String storeObjets, final int pdvPer, final String spells, final String savePos, final String jobs,
			final int mountXp, final int mount, final int honor, final int deshonor, final int alvl, final String z,
			final byte title, final int wifeGuid, String morphMode, final String allTitle, final String emotes,
			final long prison, final boolean isNew, final String parcho, final long timeDeblo, final boolean noall) {
		this.statsParcho = new Stats(true);
		this._orientation = 1;
		this.canAggro = true;
		this.isNew = false;
		this.staticEmote = new HashMap<Integer, Integer>();
		this.dynamicEmote = new HashMap<Integer, Integer>();
		this._align = 0;
		this._deshonor = 0;
		this._honor = 0;
		this._showWings = false;
		this._aLvl = 0;
		this._ready = false;
		this._isOnline = false;
		this._duelID = -1;
		this._buffs = new TreeMap<Integer, SpellEffect>();
		this._items = new TreeMap<Integer, org.aestia.object.Object>();
		this._emoteActive = 0;
		this._isTradingWith = 0;
		this.isCraftingType = new ArrayList<Integer>();
		this._isTalkingWith = 0;
		this._inviting = 0;
		this._metiers = new TreeMap<Integer, JobStat>();
		this._mountXpGive = 0;
		this._onMount = false;
		this._inDD = false;
		this._isZaaping = false;
		this._zaaps = new ArrayList<Short>();
		this._isAbsent = false;
		this._isInvisible = false;
		this._isForgetingSpell = false;
		this._sorts = new TreeMap<Integer, Spell.SortStats>();
		this._sortsPlaces = new TreeMap<Integer, Character>();
		this._isClone = false;
		this._isOnCollectorID = 0;
		this.dialogTimer = null;
		this.pointsTimer = null;
		this.checkVote = null;
		this._title = 0;
		this._wife = 0;
		this._isOK = 0;
		this._Follower = new TreeMap<Integer, Player>();
		this._Follows = null;
		this.isDead = 0;
		this.isGhost = false;
		this._Speed = 0;
		this._seeSeller = false;
		this._storeItems = new TreeMap<Integer, Integer>();
		this._metierPublic = false;
		this._livreArti = false;
		this.hasEndFight = -1;
		this.hasMobGroup = null;
		this._itemClasse = new ArrayList<Integer>();
		this._itemClasseSpell = new TreeMap<Integer, World.Couple<Integer, Integer>>();
		this._bendHechizo = 0;
		this._bendEfecto = 0;
		this._bendModif = 0;
		this._timeInTaverne = 0L;
		this._gameAction = null;
		this._morphMode = false;
		this._saveSorts = new TreeMap<Integer, Spell.SortStats>();
		this._saveSortsPlaces = new TreeMap<Integer, Character>();
		this.pa = 0;
		this.pm = 0;
		this.vitalite = 0;
		this.sagesse = 0;
		this.terre = 0;
		this.feu = 0;
		this.eau = 0;
		this.air = 0;
		this.initiative = 0;
		this.useStats = false;
		this.useCac = true;
		this.oldMap = 0;
		this.oldCell = 0;
		this.tutorial = null;
		this._allTitle = "";
		this.isBlocked = false;
		this.action = -1;
		this.timeToSave = -1L;
		this.regenRate = 2000;
		this.regenTime = -1L;
		this.thatMap = -1;
		this.thatCell = -1;
		this.walkFast = false;
		this.getCases = false;
		this.thisCases = new ArrayList<Integer>();
		this.mapGeoPos = new ArrayList<org.aestia.map.Map>();
		this.allMap = new ArrayList<org.aestia.map.Map>();
		this.itemLost = new ArrayList<Integer>();
		this.mpToTp = false;
		this.isInPrivateArea = false;
		this.isInvisible = false;
		this.noall = false;
		this.questList = new TreeMap<Integer, Quest.Quest_Perso>();
		this.shootingMonster = new ArrayList<Integer>();
		this.id = id;
		this.noall = noall;
		this.name = name;
		this.groupe = Groupes.getGroupeById(groupe);
		this.sexe = sexe;
		this.classe = classe;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this._kamas = _kamas;
		this._capital = _capital;
		this._align = alignement;
		this._honor = honor;
		this._deshonor = deshonor;
		this._aLvl = alvl;
		this.energy = energy;
		this.level = level;
		this.exp = exp;
		if (mount != -1) {
			this._mount = World.getDragoByID(mount);
		}
		this._size = _size;
		this._gfxID = _gfxid;
		this._mountXpGive = mountXp;
		this.stats = new Stats(stats, true, this);
		this._accID = _compte;
		this._compte = World.getCompte(_compte);
		this._showFriendConnection = (seeFriend == 1);
		this._wife = wifeGuid;
		this._metierPublic = false;
		this._title = title;
		this._changeName = false;
		this._allTitle = allTitle;
		if (seeSeller == 1) {
			this._seeSeller = true;
		} else {
			this._seeSeller = false;
		}
		this.savestat = 0;
		this._canaux = canaux;
		this.curMap = World.getMap(map);
		this._savePos = savePos;
		this.isNew = isNew;
		this.regenTime = System.currentTimeMillis();
		Database.getStatique().getQuest_persoData().loadPerso(this);
		this.restriction = Restriction.get(this.id);
		this.restriction.timeDeblo = timeDeblo;
		try {
			if (!emotes.isEmpty()) {
				String[] split;
				for (int length = (split = emotes.split(";")).length, k = 0; k < length; ++k) {
					final String i = split[k];
					this.addStaticEmote(Integer.parseInt(i));
				}
			}
			if (!morphMode.equals("")) {
				if (morphMode.equals("0")) {
					morphMode = "0;0";
				}
				final String[] j = morphMode.split(";");
				if (j[0].equals("1")) {
					this._morphMode = true;
				} else {
					this._morphMode = false;
				}
				if (!j[1].equals("")) {
					this._morphId = Integer.parseInt(j[1]);
				}
			}
			if (this._morphMode) {
				this._saveSpellPts = pts;
			} else {
				this._spellPts = pts;
			}
			if (prison != 0L) {
				this.isInEnnemyFaction = true;
				this.enteredOnEnnemyFaction = prison;
			}
			if (this.get_align() != 0) {
				this._showWings = (seeAlign == 1);
			} else {
				this._showWings = false;
			}
			if (this.curMap == null && World.getMap((short) 7411) != null) {
				this.curMap = World.getMap((short) 7411);
				this.curCell = this.curMap.getCase(311);
			} else {
				if (this.curMap == null && World.getMap((short) 7411) == null) {
					GameServer.addToLog(
							"Personnage mal positione, et position de d\u00e9part non valide. Fermeture du serveur.");
					Main.stop();
					return;
				}
				if (this.curMap != null) {
					this.curCell = this.curMap.getCase(cell);
					if (this.curCell == null) {
						this.curMap = World.getMap((short) 7411);
						this.curCell = this.curMap.getCase(311);
					}
				}
			}
			if (!z.equalsIgnoreCase("")) {
				String[] split2;
				for (int length2 = (split2 = z.split(",")).length, l = 0; l < length2; ++l) {
					final String str = split2[l];
					try {
						this._zaaps.add(Short.parseShort(str));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (!isNew && (this.curMap == null || this.curCell == null)) {
				GameServer.addToLog("Map ou case de d\u00e9part du personnage " + name + " invalide.");
				GameServer.addToLog("Map ou case par d\u00e9faut invalide.");
				GameServer.addToLog("Le serveur ne peut se lancer.");
				this.getGameClient().getWaiter().addNext(new Runnable() {
					@Override
					public void run() {
						Main.stop();
					}
				}, 3000L);
				return;
			}
			if (!stuff.equals("")) {
				if (stuff.charAt(stuff.length() - 1) == '|') {
					stuff = stuff.substring(0, stuff.length() - 1);
				}
				Database.getStatique().getItemData().load(stuff.replace("|", ","));
			}
			String[] split3;
			for (int length3 = (split3 = stuff.split("\\|")).length, n = 0; n < length3; ++n) {
				final String item = split3[n];
				if (!item.equals("")) {
					final String[] infos = item.split(":");
					int guid = 0;
					try {
						guid = Integer.parseInt(infos[0]);
					} catch (Exception e2) {
						e2.printStackTrace();
						continue;
					}
					final org.aestia.object.Object obj = World.getObjet(guid);
					if (obj != null) {
						synchronized (this._items) {
							this._items.put(obj.getGuid(), obj);
						}
						// monitorexit(this._items)
					}
				}
			}
			try {
				if (parcho != null && !parcho.equalsIgnoreCase("")) {
					String[] split4;
					for (int length4 = (split4 = parcho.split("\\;")).length, n2 = 0; n2 < length4; ++n2) {
						final String stat = split4[n2];
						if (!stat.equalsIgnoreCase("")) {
							this.statsParcho.addOneStat(Integer.parseInt(stat.split("\\,")[0]),
									Integer.parseInt(stat.split("\\,")[1]));
						}
					}
				}
			} catch (Exception e3) {
				Console.println("Erreur sur la colonne parcho du personnage " + this.id + ".", Console.Color.ERROR);
				e3.printStackTrace();
			}
			if (!storeObjets.equals("")) {
				String[] split5;
				for (int length5 = (split5 = storeObjets.split("\\|")).length, n3 = 0; n3 < length5; ++n3) {
					final String _storeObjets = split5[n3];
					final String[] infos = _storeObjets.split(",");
					int guid = 0;
					int price = 0;
					try {
						guid = Integer.parseInt(infos[0]);
						price = Integer.parseInt(infos[1]);
					} catch (Exception e4) {
						e4.printStackTrace();
						continue;
					}
					final org.aestia.object.Object obj2 = World.getObjet(guid);
					if (obj2 != null) {
						synchronized (this._storeItems) {
							this._storeItems.put(obj2.getGuid(), price);
						}
						// monitorexit(this._storeItems)
					}
				}
			}
			this.maxPdv = (this.level - 1) * 5 + 55 + this.getTotalStats().getEffect(125)
					+ this.getTotalStats().getEffect(110);
			if (this.curPdv <= 0) {
				this.curPdv = 1;
			}
			if (pdvPer > 100) {
				this.curPdv = this.maxPdv * 100 / 100;
			} else {
				this.curPdv = this.maxPdv * pdvPer / 100;
			}
			if (this.curPdv <= 0) {
				this.curPdv = 1;
			}
			this.parseSpells(spells);
			if (!jobs.equals("")) {
				String[] split6;
				for (int length6 = (split6 = jobs.split(";")).length, n4 = 0; n4 < length6; ++n4) {
					final String aJobData = split6[n4];
					final String[] infos = aJobData.split(",");
					try {
						final int jobID = Integer.parseInt(infos[0]);
						final long xp = Long.parseLong(infos[1]);
						final Job m = World.getMetier(jobID);
						final JobStat SM = this._metiers.get(this.learnJob(m));
						SM.addXp(this, xp, false);
					} catch (Exception e5) {
						e5.printStackTrace();
						Console.println("## Erreur personnage id : " + this.id, Console.Color.ERROR);
					}
				}
			}
			if (this.energy == 0) {
				this.set_Ghosts();
			}
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	public Player(final int id, final String name, final int groupe, final int sexe, final int classe, final int color1,
			final int color2, final int color3, final int level, final int _size, final int _gfxid,
			final Map<Integer, Integer> stats, String stuff, final int pdvPer, final byte seeAlign, final int mount,
			final int alvl, final byte alignement) {
		this.statsParcho = new Stats(true);
		this._orientation = 1;
		this.canAggro = true;
		this.isNew = false;
		this.staticEmote = new HashMap<Integer, Integer>();
		this.dynamicEmote = new HashMap<Integer, Integer>();
		this._align = 0;
		this._deshonor = 0;
		this._honor = 0;
		this._showWings = false;
		this._aLvl = 0;
		this._ready = false;
		this._isOnline = false;
		this._duelID = -1;
		this._buffs = new TreeMap<Integer, SpellEffect>();
		this._items = new TreeMap<Integer, org.aestia.object.Object>();
		this._emoteActive = 0;
		this._isTradingWith = 0;
		this.isCraftingType = new ArrayList<Integer>();
		this._isTalkingWith = 0;
		this._inviting = 0;
		this._metiers = new TreeMap<Integer, JobStat>();
		this._mountXpGive = 0;
		this._onMount = false;
		this._inDD = false;
		this._isZaaping = false;
		this._zaaps = new ArrayList<Short>();
		this._isAbsent = false;
		this._isInvisible = false;
		this._isForgetingSpell = false;
		this._sorts = new TreeMap<Integer, Spell.SortStats>();
		this._sortsPlaces = new TreeMap<Integer, Character>();
		this._isClone = false;
		this._isOnCollectorID = 0;
		this.dialogTimer = null;
		this.pointsTimer = null;
		this.checkVote = null;
		this._title = 0;
		this._wife = 0;
		this._isOK = 0;
		this._Follower = new TreeMap<Integer, Player>();
		this._Follows = null;
		this.isDead = 0;
		this.isGhost = false;
		this._Speed = 0;
		this._seeSeller = false;
		this._storeItems = new TreeMap<Integer, Integer>();
		this._metierPublic = false;
		this._livreArti = false;
		this.hasEndFight = -1;
		this.hasMobGroup = null;
		this._itemClasse = new ArrayList<Integer>();
		this._itemClasseSpell = new TreeMap<Integer, World.Couple<Integer, Integer>>();
		this._bendHechizo = 0;
		this._bendEfecto = 0;
		this._bendModif = 0;
		this._timeInTaverne = 0L;
		this._gameAction = null;
		this._morphMode = false;
		this._saveSorts = new TreeMap<Integer, Spell.SortStats>();
		this._saveSortsPlaces = new TreeMap<Integer, Character>();
		this.pa = 0;
		this.pm = 0;
		this.vitalite = 0;
		this.sagesse = 0;
		this.terre = 0;
		this.feu = 0;
		this.eau = 0;
		this.air = 0;
		this.initiative = 0;
		this.useStats = false;
		this.useCac = true;
		this.oldMap = 0;
		this.oldCell = 0;
		this.tutorial = null;
		this._allTitle = "";
		this.isBlocked = false;
		this.action = -1;
		this.timeToSave = -1L;
		this.regenRate = 2000;
		this.regenTime = -1L;
		this.thatMap = -1;
		this.thatCell = -1;
		this.walkFast = false;
		this.getCases = false;
		this.thisCases = new ArrayList<Integer>();
		this.mapGeoPos = new ArrayList<org.aestia.map.Map>();
		this.allMap = new ArrayList<org.aestia.map.Map>();
		this.itemLost = new ArrayList<Integer>();
		this.mpToTp = false;
		this.isInPrivateArea = false;
		this.isInvisible = false;
		this.noall = false;
		this.questList = new TreeMap<Integer, Quest.Quest_Perso>();
		this.shootingMonster = new ArrayList<Integer>();
		this.id = id;
		this.name = name;
		this.groupe = Groupes.getGroupeById(groupe);
		this.sexe = sexe;
		this.classe = classe;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.level = level;
		this._aLvl = alvl;
		this._size = _size;
		this._gfxID = _gfxid;
		this.stats = new Stats(stats, true, this);
		this._changeName = false;
		this.restriction = null;
		this.set_isClone(true);
		if (!stuff.equals("")) {
			if (stuff.charAt(stuff.length() - 1) == '|') {
				stuff = stuff.substring(0, stuff.length() - 1);
			}
			Database.getStatique().getItemData().load(stuff.replace("|", ","));
		}
		String[] split;
		for (int length = (split = stuff.split("\\|")).length, i = 0; i < length; ++i) {
			final String item = split[i];
			if (!item.equals("")) {
				final String[] infos = item.split(":");
				final int guid = Integer.parseInt(infos[0]);
				final org.aestia.object.Object obj = World.getObjet(guid);
				if (obj != null) {
					synchronized (this._items) {
						this._items.put(obj.getGuid(), obj);
					}
					// monitorexit(this._items)
				}
			}
		}
		this.maxPdv = (this.level - 1) * 5 + 50 + this.getStats().getEffect(125);
		this.curPdv = this.maxPdv * pdvPer / 100;
		this._align = alignement;
		if (this.get_align() != 0) {
			this._showWings = (seeAlign == 1);
		} else {
			this._showWings = false;
		}
		if (mount != -1) {
			this._mount = World.getDragoByID(mount);
		}
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		Database.getStatique().getPlayerData().updateInfos(this);
		this._changeName = false;
		if (this.getGuildMember() != null) {
			Database.getGame().getGuild_memberData().update(this);
		}
	}

	public Groupes getGroupe() {
		return this.groupe;
	}

	public void setGroupe(final Groupes groupe, final boolean reload) {
		this.groupe = groupe;
		if (reload) {
			Database.getStatique().getPlayerData().updateGroupe(this);
		}
	}

	public void setInvisible(final boolean b) {
		this.isInvisible = b;
	}

	public boolean isInvisible() {
		return this.isInvisible;
	}

	public int getSexe() {
		return this.sexe;
	}

	public void setSexe(final int sexe) {
		this.sexe = sexe;
	}

	public int getClasse() {
		return this.classe;
	}

	public void setClasse(final int classe) {
		this.classe = classe;
	}

	public int getColor1() {
		return this.color1;
	}

	public int getColor2() {
		return this.color2;
	}

	public int getColor3() {
		return this.color3;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public int getEnergy() {
		return this.energy;
	}

	public void setEnergy(final int energy) {
		this.energy = energy;
	}

	public long getExp() {
		return this.exp;
	}

	public void setExp(final long exp) {
		this.exp = exp;
	}

	public int getCurPdv() {
		this.refreshLife(false);
		return this.curPdv;
	}

	public void setPdv(final int pdv) {
		this.curPdv = pdv;
		if (this.curPdv >= this.maxPdv) {
			this.curPdv = this.maxPdv;
		}
		if (this._group != null) {
			SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(this._group, this);
		}
	}

	public int getMaxPdv() {
		return this.maxPdv;
	}

	public void setMaxPdv(final int maxPdv) {
		this.maxPdv = maxPdv;
		SocketManager.GAME_SEND_STATS_PACKET(this);
		if (this._group != null) {
			SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(this._group, this);
		}
	}

	public Stats getStats() {
		if (this.useStats) {
			return this.newStatsMorph();
		}
		return this.stats;
	}

	public Stats getStatsParcho() {
		return this.statsParcho;
	}

	public String parseStatsParcho() {
		String parcho = "";
		for (final Map.Entry<Integer, Integer> i : this.statsParcho.getMap().entrySet()) {
			parcho = String.valueOf(parcho)
					+ (parcho.isEmpty() ? (i.getKey() + "," + i.getValue()) : (";" + i.getKey() + "," + i.getValue()));
		}
		return parcho;
	}

	public void setTutorial(final Tutorial tuto) {
		this.tutorial = tuto;
	}

	public Tutorial getTutorial() {
		return this.tutorial;
	}

	public void setDoAction(final boolean b) {
		this.doAction = b;
	}

	public boolean getDoAction() {
		return this.doAction;
	}

	public void setRoleplayBuff(final int id) {
		int objTemplate = 0;
		switch (id) {
		case 10673: {
			objTemplate = 10844;
			break;
		}
		case 10669: {
			objTemplate = 10681;
			break;
		}
		}
		if (objTemplate == 0) {
			return;
		}
		if (this.getObjetByPos(21) != null) {
			final int guid = this.getObjetByPos(21).getGuid();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			this.deleteItem(guid);
		}
		final org.aestia.object.Object obj = World.getObjTemplate(objTemplate).createNewRoleplayBuff();
		this.addObjet(obj, false);
		World.addObjet(obj, true);
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void setBenediction(final int id) {
		if (this.getObjetByPos(23) != null) {
			final int guid = this.getObjetByPos(23).getGuid();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			this.deleteItem(guid);
		}
		if (id == 0) {
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
			return;
		}
		int turn = 0;
		switch (id) {
		case 10682: {
			turn = 20;
			break;
		}
		default: {
			turn = 1;
			break;
		}
		}
		final org.aestia.object.Object obj = World.getObjTemplate(id).createNewBenediction(turn);
		this.addObjet(obj, false);
		World.addObjet(obj, true);
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void setMalediction(final int id) {
		int objTemplate = 0;
		switch (id) {
		case 10827: {
			objTemplate = 10838;
			break;
		}
		default: {
			objTemplate = id;
			break;
		}
		}
		if (objTemplate == 0) {
			return;
		}
		if (this.getObjetByPos(22) != null) {
			final int guid = this.getObjetByPos(22).getGuid();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			this.deleteItem(guid);
		}
		if (id == 0) {
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
			return;
		}
		final org.aestia.object.Object obj = World.getObjTemplate(objTemplate).createNewMalediction();
		this.addObjet(obj, false);
		World.addObjet(obj, true);
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void setMascotte(final int id) {
		if (this.getObjetByPos(24) != null) {
			final int guid = this.getObjetByPos(24).getGuid();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			this.deleteItem(guid);
		}
		if (id == 0) {
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
			return;
		}
		final org.aestia.object.Object obj = World.getObjTemplate(id).createNewFollowPnj(1);
		if (obj != null && this.addObjet(obj, false)) {
			World.addObjet(obj, true);
		}
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void setCandy(final int id) {
		if (this.getObjetByPos(25) != null) {
			final int guid = this.getObjetByPos(25).getGuid();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			this.deleteItem(guid);
		}
		int turn = 30;
		switch (id) {
		case 8948:
		case 8949:
		case 8950:
		case 8951:
		case 8952:
		case 8953:
		case 8954:
		case 8955: {
			turn = 5;
			break;
		}
		case 10665: {
			turn = 20;
			break;
		}
		default: {
			turn = 30;
			break;
		}
		}
		final org.aestia.object.Object obj = World.getObjTemplate(id).createNewCandy(turn);
		this.addObjet(obj, false);
		World.addObjet(obj, true);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void calculTurnCandy() {
		org.aestia.object.Object obj = null;
		if (this.getObjetByPos(25) != null) {
			obj = this.getObjetByPos(25);
			obj.getStats().addOneStat(811, -1);
			if (obj.getStats().getEffect(811) <= 0) {
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
				this.deleteItem(obj.getGuid());
			} else {
				SocketManager.GAME_SEND_UPDATE_ITEM(this, obj);
			}
		}
		if (this.getObjetByPos(24) != null) {
			obj = this.getObjetByPos(24);
			obj.getStats().addOneStat(811, -1);
			if (obj.getStats().getEffect(811) <= 0) {
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
				this.deleteItem(obj.getGuid());
			} else {
				SocketManager.GAME_SEND_UPDATE_ITEM(this, obj);
			}
		}
		if (this.getObjetByPos(23) != null) {
			obj = this.getObjetByPos(23);
			obj.getStats().addOneStat(811, -1);
			if (obj.getStats().getEffect(811) <= 0) {
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
				this.deleteItem(obj.getGuid());
			} else {
				SocketManager.GAME_SEND_UPDATE_ITEM(this, obj);
			}
		}
		if (this.getObjetByPos(22) != null) {
			obj = this.getObjetByPos(22);
			obj.getStats().addOneStat(811, -1);
			if (obj.getStats().getEffect(811) <= 0) {
				this._gfxID = this.getClasse() * 10 + this.getSexe();
				SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
				switch (obj.getTemplate().getId()) {
				case 8169:
				case 8170: {
					this.unsetFullMorph();
					break;
				}
				}
				this.deleteItem(obj.getGuid());
			} else {
				SocketManager.GAME_SEND_UPDATE_ITEM(this, obj);
			}
		}
		if (this.getObjetByPos(21) != null) {
			obj = this.getObjetByPos(21);
			obj.getStats().addOneStat(811, -1);
			if (obj.getStats().getEffect(811) <= 0) {
				this._gfxID = this.getClasse() * 10 + this.getSexe();
				SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
				this.deleteItem(obj.getGuid());
			} else {
				SocketManager.GAME_SEND_UPDATE_ITEM(this, obj);
			}
		}
	}

	public boolean isSpec() {
		return this._spec;
	}

	public void setSpec(final boolean s) {
		this._spec = s;
	}

	public String getAllTitle() {
		return this._allTitle = Database.getStatique().getPlayerData().loadTitles(this.getId());
	}

	public void setTimeDeblo(final long timeDeblo) {
		this.restriction.timeDeblo = timeDeblo;
		Database.getStatique().getPlayerData().updateTimeDeblo(this.getId(), timeDeblo);
	}

	public void setAllTitle(String title) {
		this.getAllTitle();
		boolean erreur = false;
		if (title.equals("")) {
			title = "0";
		}
		if (this._allTitle != null) {
			String[] split;
			for (int length = (split = this._allTitle.split(",")).length, j = 0; j < length; ++j) {
				final String i = split[j];
				if (i.equals(title)) {
					erreur = true;
				}
			}
		}
		if (this._allTitle == null && !erreur) {
			this._allTitle = title;
		} else if (!erreur) {
			this._allTitle = String.valueOf(this._allTitle) + "," + title;
		}
		Database.getStatique().getPlayerData().updateTitles(this.getId(), this._allTitle);
	}

	public static Player CREATE_PERSONNAGE(final String name, final int sexe, final int classe, final int color1,
			final int color2, final int color3, final Account compte) {
		String z = "";
		if (Config.getInstance().allZaap) {
			for (final Map.Entry<Integer, Integer> i : Constant.ZAAPS.entrySet()) {
				if (z.length() != 0) {
					z = String.valueOf(z) + ",";
				}
				z = String.valueOf(z) + i.getKey();
			}
		}
		if (classe > 12 || classe < 1) {
			return null;
		}
		if (sexe < 0 || sexe > 1) {
			return null;
		}
		final Player perso = new Player(Database.getStatique().getPlayerData().getNextId(), name, -1, sexe, classe,
				color1, color2, color3, Main.startKamas, (Main.startLevel - 1) * 1, (Main.startLevel - 1) * 5, 10000,
				Main.startLevel, World.getPersoXpMin(Main.startLevel), 100,
				Integer.parseInt(String.valueOf(classe) + sexe), (byte) 0, compte.getGuid(),
				new TreeMap<Integer, Integer>(), (byte) 1, (byte) 0, (byte) 0,
				"*#%!pi$:?", (Config
						.getInstance().startMap != 0)
								? ((short) Config.getInstance().startMap)
								: Constant
										.getStartMap(
												classe),
				(Config.getInstance().startCell != 0) ? ((short) Config.getInstance().startCell)
						: Constant.getStartCell(classe),
				"", "", 100, "",
				String.valueOf((Config.getInstance().startMap != 0) ? ((short) Config.getInstance().startMap)
						: Constant.getStartMap(classe))
						+ ","
						+ ((Config.getInstance().startCell != 0) ? ((short) Config.getInstance().startCell)
								: Constant.getStartCell(classe)),
				"", 0, -1, 0, 0, 0, z, (byte) 0, 0, "0;0", "",
				Config.getInstance().allEmote ? "0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21" : "0", 0L,
				true, "118,0;119,0;123,0;124,0;125,0;126,0", 0L, false);
		perso.staticEmote.put(1, 1);
		perso._sorts = Constant.getStartSorts(classe);
		for (int a = 1; a <= perso.getLevel(); ++a) {
			Constant.onLevelUpSpells(perso, a);
		}
		perso._sortsPlaces = Constant.getStartSortsPlaces(classe);
		SocketManager.GAME_SEND_WELCOME(perso);
		if (!Database.getStatique().getPlayerData().add(perso)) {
			return null;
		}
		World.addPersonnage(perso);
		if (Main.key.equals("jiva")) {
			for (final ObjectTemplate t : World.getItemSet(5).getItemTemplates()) {
				final org.aestia.object.Object obj = t.createNewItem(1, true);
				if (perso.addObjet(obj, true)) {
					World.addObjet(obj, true);
				}
			}
		}
		return perso;
	}

	public void setSpells(final Map<Integer, Spell.SortStats> spells) {
		this._sorts.clear();
		this._sortsPlaces.clear();
		this._sorts = spells;
		this._sortsPlaces = Constant.getStartSortsPlaces(this.getClasse());
	}

	public void teleportOldMap() {
		this.teleport(this.oldMap, this.oldCell);
	}

	public short getOldMap() {
		return this.oldMap;
	}

	public void setOldMap(final short i) {
		this.oldMap = i;
	}

	public int getOldCell() {
		return this.oldCell;
	}

	public void setOldCell(final int i) {
		this.oldCell = i;
	}

	public void set_Online(final boolean d) {
		this._isOnline = d;
	}

	public boolean isOnline() {
		return this._isOnline;
	}

	public void setGroup(final Group g) {
		this._group = g;
	}

	public Group getGroup() {
		return this._group;
	}

	public String parseSpellToDB() {
		final StringBuilder sorts = new StringBuilder();
		if (this._morphMode) {
			if (this._saveSorts.isEmpty()) {
				return "";
			}
			for (final int key : this._saveSorts.keySet()) {
				final Spell.SortStats SS = this._saveSorts.get(key);
				if (SS == null) {
					continue;
				}
				sorts.append(SS.getSpellID()).append(";").append(SS.getLevel()).append(";");
				if (this._saveSortsPlaces.get(key) != null) {
					sorts.append(this._saveSortsPlaces.get(key));
				} else {
					sorts.append("_");
				}
				sorts.append(",");
			}
		} else {
			if (this._sorts.isEmpty()) {
				return "";
			}
			for (final int key : this._sorts.keySet()) {
				final Spell.SortStats SS = this._sorts.get(key);
				if (SS == null) {
					continue;
				}
				sorts.append(SS.getSpellID()).append(";").append(SS.getLevel()).append(";");
				if (this._sortsPlaces.get(key) != null) {
					sorts.append(this._sortsPlaces.get(key));
				} else {
					sorts.append("_");
				}
				sorts.append(",");
			}
		}
		return sorts.substring(0, sorts.length() - 1).toString();
	}

	private void parseSpells(final String str) {
		if (!str.equalsIgnoreCase("")) {
			if (this._morphMode) {
				final String[] spells = str.split(",");
				this._saveSorts.clear();
				this._saveSortsPlaces.clear();
				String[] array;
				for (int length = (array = spells).length, i = 0; i < length; ++i) {
					final String e = array[i];
					try {
						final int id = Integer.parseInt(e.split(";")[0]);
						final int lvl = Integer.parseInt(e.split(";")[1]);
						final char place = e.split(";")[2].charAt(0);
						this.learnSpell(id, lvl);
						this._saveSortsPlaces.put(id, place);
					} catch (NumberFormatException e2) {
						e2.printStackTrace();
					}
				}
			} else {
				final String[] spells = str.split(",");
				this._sorts.clear();
				this._sortsPlaces.clear();
				String[] array2;
				for (int length2 = (array2 = spells).length, j = 0; j < length2; ++j) {
					final String e = array2[j];
					try {
						final int id = Integer.parseInt(e.split(";")[0]);
						final int lvl = Integer.parseInt(e.split(";")[1]);
						final char place = e.split(";")[2].charAt(0);
						if (!this._morphMode) {
							this.learnSpell(id, lvl, false, false, false);
						} else {
							this.learnSpell(id, lvl, false, true, false);
						}
						this._sortsPlaces.put(id, place);
					} catch (NumberFormatException e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	private void parseSpellsFullMorph(final String str) {
		final String[] spells = str.split(",");
		this._sorts.clear();
		this._sortsPlaces.clear();
		String[] array;
		for (int length = (array = spells).length, i = 0; i < length; ++i) {
			final String e = array[i];
			try {
				final int id = Integer.parseInt(e.split(";")[0]);
				final int lvl = Integer.parseInt(e.split(";")[1]);
				final char place = e.split(";")[2].charAt(0);
				if (!this._morphMode) {
					this.learnSpell(id, lvl, false, false, false);
				} else {
					this.learnSpell(id, lvl, false, true, false);
				}
				this._sortsPlaces.put(id, place);
			} catch (NumberFormatException e2) {
				e2.printStackTrace();
			}
		}
	}

	public String get_savePos() {
		return this._savePos;
	}

	public void set_savePos(final String savePos) {
		this._savePos = savePos;
	}

	public int get_isTradingWith() {
		return this._isTradingWith;
	}

	public void set_isTradingWith(final int tradingWith) {
		this._isTradingWith = tradingWith;
	}

	public ArrayList<Integer> getIsCraftingType() {
		return this.isCraftingType;
	}

	public void setIsCraftingType(final ArrayList<Integer> isCraftingType) {
		this.isCraftingType = isCraftingType;
	}

	public int get_isTalkingWith() {
		return this._isTalkingWith;
	}

	public void set_isTalkingWith(final int talkingWith) {
		this._isTalkingWith = talkingWith;
	}

	public int getIsOnDialogAction() {
		return this.action;
	}

	public void setIsOnDialogAction(final int action) {
		this.action = action;
	}

	public long get_kamas() {
		return this._kamas;
	}

	public Map<Integer, SpellEffect> get_buff() {
		return this._buffs;
	}

	public void set_kamas(final long l) {
		this._kamas = l;
	}

	public Account getAccount() {
		return this._compte;
	}

	public int get_spellPts() {
		if (this._morphMode) {
			return this._saveSpellPts;
		}
		return this._spellPts;
	}

	public void set_spellPts(final int pts) {
		if (this._morphMode) {
			this._saveSpellPts = pts;
		} else {
			this._spellPts = pts;
		}
	}

	public Guild get_guild() {
		if (this._guildMember == null) {
			return null;
		}
		return this._guildMember.getGuild();
	}

	public void setGuildMember(final Guild.GuildMember _guild) {
		this._guildMember = _guild;
	}

	public boolean is_ready() {
		return this._ready;
	}

	public void set_ready(final boolean _ready) {
		this._ready = _ready;
	}

	public int get_duelID() {
		return this._duelID;
	}

	public Fight get_fight() {
		return this._fight;
	}

	public void set_duelID(final int _duelid) {
		this._duelID = _duelid;
	}

	public boolean is_showFriendConnection() {
		return this._showFriendConnection;
	}

	public boolean is_showWings() {
		return this._showWings;
	}

	public boolean isShowSeller() {
		return this._seeSeller;
	}

	public void setShowSeller(final boolean is) {
		this._seeSeller = is;
	}

	public String get_canaux() {
		return this._canaux;
	}

	public Case getCurCell() {
		return this.curCell;
	}

	public void setCurCell(final Case cell) {
		this.curCell = cell;
	}

	public int get_size() {
		return this._size;
	}

	public void set_size(final int _size) {
		this._size = _size;
	}

	public void set_fight(final Fight fight) {
		this.refreshLife(false);
		if (fight == null) {
			SocketManager.send(this, "ILS2000");
		} else {
			SocketManager.send(this, "ILF0");
		}
		this.sitted = false;
		this._fight = fight;
	}

	public int get_gfxID() {
		return this._gfxID;
	}

	public void set_gfxID(final int _gfxid) {
		this._gfxID = _gfxid;
	}

	public boolean isMorphMercenaire() {
		return this._gfxID == 8009 || this._gfxID == 8006;
	}

	public org.aestia.map.Map getCurMap() {
		return this.curMap;
	}

	public boolean is_away() {
		return this._away;
	}

	public void set_away(final boolean _away) {
		this._away = _away;
	}

	public boolean isSitted() {
		return this.sitted;
	}

	public int get_capital() {
		return this._capital;
	}

	public boolean learnSpell(final int spellID, final int level, final boolean save, final boolean send,
			final boolean learn) {
		if (World.getSort(spellID).getStatsByLevel(level) == null) {
			GameServer.addToLog("[ERROR]Sort " + spellID + " lvl " + level + " non trouve.");
			return false;
		}
		if (spellID == 366 && this._sorts.containsKey(spellID)) {
			return false;
		}
		if (this._sorts.containsKey(spellID) && learn) {
			SocketManager.GAME_SEND_MESSAGE(this, "Tu poss\u00e8de d\u00e9j\u00e0 ce sort.");
			return false;
		}
		this._sorts.put(spellID, World.getSort(spellID).getStatsByLevel(level));
		if (send) {
			SocketManager.GAME_SEND_SPELL_LIST(this);
			SocketManager.GAME_SEND_Im_PACKET(this, "03;" + spellID);
		}
		if (save) {
			Database.getStatique().getPlayerData().update(this, false);
		}
		return true;
	}

	public boolean learnSpell(final int spellID, final int level) {
		if (World.getSort(spellID).getStatsByLevel(level) == null) {
			GameServer.addToLog("[ERROR]Sort " + spellID + " lvl " + level + " non trouve.");
			return false;
		}
		if (spellID == 366 && this._saveSorts.containsKey(spellID)) {
			return false;
		}
		if (this._saveSorts.containsKey(spellID)) {
			return false;
		}
		this._saveSorts.put(spellID, World.getSort(spellID).getStatsByLevel(level));
		return true;
	}

	public boolean unlearnSpell(final Player perso, final int spellID, final int level, final int ancLevel,
			final boolean save, final boolean send) {
		int spellPoint = 1;
		if (ancLevel == 2) {
			spellPoint = 1;
		}
		if (ancLevel == 3) {
			spellPoint = 3;
		}
		if (ancLevel == 4) {
			spellPoint = 6;
		}
		if (ancLevel == 5) {
			spellPoint = 10;
		}
		if (ancLevel == 6) {
			spellPoint = 15;
		}
		if (World.getSort(spellID).getStatsByLevel(level) == null) {
			GameServer.addToLog("[ERROR]Sort " + spellID + " lvl " + level + " non trouve.");
			return false;
		}
		if (spellID == 366 && this._sorts.containsKey(spellID)) {
			return false;
		}
		this._sorts.put(spellID, World.getSort(spellID).getStatsByLevel(level));
		if (send) {
			SocketManager.GAME_SEND_SPELL_LIST(this);
			SocketManager.GAME_SEND_Im_PACKET(this, "0154;<b>" + ancLevel + "</b>" + "~" + "<b>" + spellPoint + "</b>");
			this.addSpellPoint(spellPoint);
			SocketManager.GAME_SEND_STATS_PACKET(perso);
		}
		if (save) {
			Database.getStatique().getPlayerData().update(this, false);
		}
		return true;
	}

	public boolean boostSpell(final int spellID) {
		if (this.getSortStatBySortIfHas(spellID) == null) {
			return false;
		}
		final int AncLevel = this.getSortStatBySortIfHas(spellID).getLevel();
		if (AncLevel == 6) {
			return false;
		}
		if (this._spellPts < AncLevel
				|| World.getSort(spellID).getStatsByLevel(AncLevel + 1).getReqLevel() > this.getLevel()) {
			return (this._spellPts >= AncLevel
					|| World.getSort(spellID).getStatsByLevel(AncLevel + 1).getReqLevel() <= this.getLevel())
					&& this._away;
		}
		if (this.learnSpell(spellID, AncLevel + 1, true, false, false)) {
			this._spellPts -= AncLevel;
			Database.getStatique().getPlayerData().update(this, false);
			return true;
		}
		return false;
	}

	public void boostSpellIncarnation() {
		for (final Map.Entry<Integer, Spell.SortStats> i : this._sorts.entrySet()) {
			if (this.getSortStatBySortIfHas(i.getValue().getSpell().getSpellID()) == null) {
				continue;
			}
			if (!this.learnSpell(i.getValue().getSpell().getSpellID(), i.getValue().getLevel() + 1, true, false,
					false)) {
				continue;
			}
			Database.getStatique().getPlayerData().update(this, false);
		}
	}

	public boolean forgetSpell(final int spellID) {
		if (this.getSortStatBySortIfHas(spellID) == null) {
			return false;
		}
		final int AncLevel = this.getSortStatBySortIfHas(spellID).getLevel();
		if (AncLevel <= 1) {
			return false;
		}
		if (this.learnSpell(spellID, 1, true, false, false)) {
			this._spellPts += Formulas.spellCost(AncLevel);
			Database.getStatique().getPlayerData().update(this, false);
			return true;
		}
		return false;
	}

	public void demorph() {
		if (this.getMorphMode()) {
			final int morphID = this.getClasse() * 10 + this.getSexe();
			this.set_gfxID(morphID);
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getCurMap(), this.getId());
			SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.getCurMap(), this);
		}
	}

	public boolean getMorphMode() {
		return this._morphMode;
	}

	public int getMorphId() {
		return this._morphId;
	}

	public void setMorphId(final int id) {
		this._morphId = id;
	}

	public void setFullMorph(final int morphid, final boolean isLoad, final boolean join) {
		if (this._morphMode && !join) {
			this.unsetFullMorph();
		}
		if (this.isGhost) {
			SocketManager.send(this, "Im1185");
			return;
		}
		if (!join) {
			if (!this._morphMode) {
				this._saveSpellPts = this._spellPts;
				this._saveSorts.putAll(this._sorts);
				this._saveSortsPlaces.putAll(this._sortsPlaces);
			}
			if (isLoad) {
				this._saveSpellPts = this._spellPts;
				this._saveSorts.putAll(this._sorts);
				this._saveSortsPlaces.putAll(this._sortsPlaces);
			}
		}
		this._morphMode = true;
		this._sorts.clear();
		this._sortsPlaces.clear();
		this._spellPts = 0;
		final Map<String, String> fullMorph = World.getFullMorph(morphid);
		this.set_gfxID(Integer.parseInt(fullMorph.get("gfxid")));
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
		this.parseSpellsFullMorph(fullMorph.get("spells"));
		this.setMorphId(morphid);
		if (this.getObjetByPos(1) != null
				&& Constant.isIncarnationWeapon(this.getObjetByPos(1).getTemplate().getId())) {
			for (int i = 0; i <= this.getObjetByPos(1).getSoulStat().get(962); ++i) {
				if (i == 10 || i == 20 || i == 30 || i == 40 || i == 50) {
					this.boostSpellIncarnation();
				}
			}
		}
		SocketManager.GAME_SEND_ASK(this.getGameClient(), this);
		SocketManager.GAME_SEND_SPELL_LIST(this);
		if (fullMorph.get("vie") != null) {
			this.maxPdv = Integer.parseInt(fullMorph.get("vie"));
			this.setPdv(this.getMaxPdv());
			this.pa = Integer.parseInt(fullMorph.get("pa"));
			this.pm = Integer.parseInt(fullMorph.get("pm"));
			this.vitalite = Integer.parseInt(fullMorph.get("vitalite"));
			this.sagesse = Integer.parseInt(fullMorph.get("sagesse"));
			this.terre = Integer.parseInt(fullMorph.get("terre"));
			this.feu = Integer.parseInt(fullMorph.get("feu"));
			this.eau = Integer.parseInt(fullMorph.get("eau"));
			this.air = Integer.parseInt(fullMorph.get("air"));
			this.initiative = Integer.parseInt(String.valueOf(fullMorph.get("initiative")) + this.sagesse + this.terre
					+ this.feu + this.eau + this.air);
			this.useStats = fullMorph.get("stats").equals("1");
			this.donjon = fullMorph.get("donjon").equals("1");
			this.useCac = false;
		}
		SocketManager.GAME_SEND_STATS_PACKET(this);
		if (!join) {
			Database.getStatique().getPlayerData().update(this, true);
		}
	}

	public boolean isMorph() {
		return this._gfxID != this.getClasse() * 10 + this.getSexe();
	}

	public boolean canCac() {
		return this.useCac;
	}

	public void unsetMorph() {
		this.set_gfxID(this.getClasse() * 10 + this.getSexe());
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.curMap, this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void unsetFullMorph() {
		if (!this._morphMode) {
			return;
		}
		final int morphID = this.getClasse() * 10 + this.getSexe();
		this.set_gfxID(morphID);
		this.useStats = false;
		this.donjon = false;
		this._morphMode = false;
		this.useCac = true;
		this._sorts.clear();
		this._sortsPlaces.clear();
		this._spellPts = this._saveSpellPts;
		this._sorts.putAll(this._saveSorts);
		this._sortsPlaces.putAll(this._saveSortsPlaces);
		this.parseSpells(this.parseSpellToDB());
		this.setMorphId(0);
		SocketManager.GAME_SEND_SPELL_LIST(this);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.curMap, this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public String parseSpellList() {
		final StringBuilder packet = new StringBuilder();
		packet.append("SL");
		for (final Spell.SortStats SS : this._sorts.values()) {
			packet.append(SS.getSpellID()).append("~").append(SS.getLevel()).append("~")
					.append(this._sortsPlaces.get(SS.getSpellID())).append(";");
		}
		return packet.toString();
	}

	public void set_SpellPlace(final int SpellID, final char Place) {
		this.replace_SpellInBook(Place);
		this._sortsPlaces.remove(SpellID);
		this._sortsPlaces.put(SpellID, Place);
		Database.getStatique().getPlayerData().update(this, false);
	}

	private void replace_SpellInBook(final char Place) {
		for (final int key : this._sorts.keySet()) {
			if (this._sortsPlaces.get(key) != null && this._sortsPlaces.get(key).equals(Place)) {
				this._sortsPlaces.remove(key);
			}
		}
	}

	public Spell.SortStats getSortStatBySortIfHas(final int spellID) {
		return this._sorts.get(spellID);
	}

	public String parseALK() {
		final StringBuilder perso = new StringBuilder();
		perso.append("|");
		perso.append(this.getId()).append(";");
		perso.append(this.getName()).append(";");
		perso.append(this.getLevel()).append(";");
		int gfx = this._gfxID;
		if (this.getObjetByPos(21) != null && this.getObjetByPos(21).getTemplate().getId() == 10681) {
			gfx = 8037;
		}
		perso.append(gfx).append(";");
		int color1 = this.getColor1();
		int color2 = this.getColor2();
		int color3 = this.getColor3();
		if (this.getObjetByPos(22) != null && this.getObjetByPos(22).getTemplate().getId() == 10838) {
			color1 = 16342021;
			color2 = 16342021;
			color3 = 16342021;
		}
		perso.append((color1 != -1) ? Integer.toHexString(color1) : "-1").append(";");
		perso.append((color2 != -1) ? Integer.toHexString(color2) : "-1").append(";");
		perso.append((color3 != -1) ? Integer.toHexString(color3) : "-1").append(";");
		perso.append(this.getGMStuffString()).append(";");
		perso.append(this.isShowSeller() ? 1 : 0).append(";");
		perso.append(Main.serverId).append(";");
		perso.append("0;");
		perso.append("0;");
		return perso.toString();
	}

	public void remove() {
		Database.getStatique().getPlayerData().delete(this);
	}

	public void OnJoinGame() {
		this._compte.setCurPerso(this);
		this.set_Online(true);
		if (this._compte.getGameClient() == null) {
			return;
		}
		final GameClient out = this._compte.getGameClient();
		if (this.isShowSeller()) {
			this.setShowSeller(false);
			World.removeSeller(this.getId(), this.getCurMap().getId());
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
		}
		if (this._mount != null) {
			SocketManager.GAME_SEND_Re_PACKET(this, "+", this._mount);
		}
		SocketManager.GAME_SEND_Rx_PACKET(this);
		SocketManager.GAME_SEND_ASK(out, this);
		for (int a = 1; a < World.getItemSetNumber(); ++a) {
			if (this.getNumbEquipedItemOfPanoplie(a) != 0) {
				SocketManager.GAME_SEND_OS_PACKET(this, a);
			}
		}
		if (this._fight != null) {
			SocketManager.send(this, "ILF0");
		} else {
			SocketManager.send(this, "ILS2000");
		}
		if (this._metiers.size() > 0) {
			final ArrayList<JobStat> list = new ArrayList<JobStat>();
			list.addAll(this._metiers.values());
			SocketManager.GAME_SEND_JS_PACKET(this, list);
			SocketManager.GAME_SEND_JX_PACKET(this, list);
			SocketManager.GAME_SEND_JO_PACKET(this, list);
			final org.aestia.object.Object obj = this.getObjetByPos(1);
			if (obj != null) {
				for (final JobStat sm : list) {
					if (sm.getTemplate().isValidTool(obj.getTemplate().getId())) {
						SocketManager.GAME_SEND_OT_PACKET(this._compte.getGameClient(), sm.getTemplate().getId());
					}
				}
			}
		}
		SocketManager.GAME_SEND_ALIGNEMENT(out, this._align);
		SocketManager.GAME_SEND_ADD_CANAL(out,
				String.valueOf(this._canaux) + "^" + ((this.getGroupe() != null) ? "@" : ""));
		if (this._guildMember != null) {
			SocketManager.GAME_SEND_gS_PACKET(this, this._guildMember);
		}
		SocketManager.GAME_SEND_ZONE_ALLIGN_STATUT(out);
		SocketManager.GAME_SEND_EMOTE_LIST(this, getCompiledEmote(this.staticEmote, this.dynamicEmote));
		SocketManager.GAME_SEND_RESTRICTIONS(out);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_SEE_FRIEND_CONNEXION(out, this._showFriendConnection);
		SocketManager.GAME_SEND_SPELL_LIST(this);
		this._compte.sendOnline();
		SocketManager.GAME_SEND_Im_PACKET(this, "189");
		if (!this._compte.getLastConnectionDate().equals("") && !this._compte.getLastIP().equals("")) {
			SocketManager.GAME_SEND_Im_PACKET(this,
					"0152;" + this._compte.getLastConnectionDate() + "~" + this._compte.getLastIP());
		}
		SocketManager.GAME_SEND_Im_PACKET(this, "0153;" + this._compte.getCurIP());
		this._compte.setLastIP(this._compte.getCurIP());
		final Date actDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd");
		final String jour = dateFormat.format(actDate);
		dateFormat = new SimpleDateFormat("MM");
		final String mois = dateFormat.format(actDate);
		dateFormat = new SimpleDateFormat("yyyy");
		final String annee = dateFormat.format(actDate);
		dateFormat = new SimpleDateFormat("HH");
		final String heure = dateFormat.format(actDate);
		dateFormat = new SimpleDateFormat("mm");
		final String min = dateFormat.format(actDate);
		this._compte.setLastConnectionDate(String.valueOf(annee) + "~" + mois + "~" + jour + "~" + heure + "~" + min);
		if (this._guildMember != null) {
			this._guildMember.setLastCo(String.valueOf(annee) + "~" + mois + "~" + jour + "~" + heure + "~" + min);
		}
		World.showPrismes(this);
		Database.getStatique().getAccountData().updateLastConnection(this._compte);
		if (!Config.getInstance().startMessage.equals("")) {
			SocketManager.GAME_SEND_MESSAGE(this, Config.getInstance().startMessage);
		}
		synchronized (this._items) {
			for (final org.aestia.object.Object object : this._items.values()) {
				if (object.getTemplate().getType() == 18) {
					final PetEntry p = World.getPetsEntry(object.getGuid());
					final Pet pets = World.getPets(object.getTemplate().getId());
					if (p == null || pets == null) {
						if (p == null || p.getPdv() <= 0) {
							continue;
						}
						SocketManager.GAME_SEND_Im_PACKET(this, "025");
					} else {
						if (pets.getType() == 0) {
							continue;
						}
						if (pets.getType() == 1) {
							continue;
						}
						p.updatePets(this, Integer.parseInt(pets.getGap().split(",")[1]));
					}
				} else {
					if (object.getTemplate().getId() != 10207) {
						continue;
					}
					String date = object.getTxtStat().get(805);
					if (date == null) {
						continue;
					}
					if (date.contains("#")) {
						date = date.split("#")[3];
					}
					if (System.currentTimeMillis() - Long.parseLong(date) <= 604800000L) {
						continue;
					}
					object.getTxtStat().clear();
					object.getTxtStat().putAll(Dopeul.generateStatsTrousseau());
					Database.getStatique().getItemData().save(object, false);
					SocketManager.GAME_SEND_UPDATE_ITEM(this, object);
				}
			}
		}
		// monitorexit(this._items)
		if (this._morphMode) {
			this.setFullMorph(this._morphId, true, true);
		}
		if (Config.getInstance().autoReboot) {
			this.send(Reboot.getInstance().toString());
		}
		String a2 = " ";
		for (final int i : this.itemLost) {
			a2 = String.valueOf(a2) + i + " ";
		}
		if (!this.itemLost.isEmpty()) {
			SocketManager.GAME_SEND_MESSAGE(this,
					"Les objets " + a2 + "sont perdu, merci de contacter le staff pour les r\u00e9cup\u00e9r\u00e9s.");
		}
		final long lastConnect = this.getAccount().getLastConnectDay();
		final long now = System.currentTimeMillis();
		if (lastConnect + 86400000L <= now || lastConnect == 0L) {
			this.getAccount().setLastConnectDay(now);
			this.getAccount().setPoints(this.getAccount().getPoints() + 20);
			SocketManager.GAME_SEND_MESSAGE(this,
					"Vous venez de gagner 20 points boutiques pour votre premi\u00e8re connexion de la journ\u00e9e.");
		}
		this.PointsTimer();
		this.checkVote();
		Console.println("Le joueur " + this.getName() + " vient de se connecter.", Console.Color.GREEN);
		if (this.getCurMap().getSubArea().getId() == 319 || this.getCurMap().getSubArea().getId() == 210) {
			this.getWaiter().addNow(new Runnable() {
				@Override
				public void run() {
					Toror.sendPacketMap(Player.this);
				}
			}, 3500L);
		} else if (this.getCurMap().getSubArea().getId() == 200) {
			this.getWaiter().addNow(new Runnable() {
				@Override
				public void run() {
					Dc.sendPacketMap(Player.this);
				}
			}, 3500L);
		}
	}

	public void PointsTimer() {
		(this.pointsTimer = new Manageable() {
			private Player player;

			@Override
			public void launch() {
				this.player = Player.this.getThis();
				GlobalManager.worldSheduler.schedule(this, 60L, TimeUnit.MINUTES);
			}

			@Override
			public void run() {
				try {
					if (Player.this.pointsTimer == null) {
						return;
					}
					if (this.player == null) {
						Player.this.pointsTimer = null;
						return;
					}
					if (!this.player.isOnline()) {
						Player.this.pointsTimer = null;
						return;
					}
					this.player.getAccount().setPoints(Player.this.getAccount().getPoints() + 2);
					SocketManager.GAME_SEND_MESSAGE(this.player.getThis(),
							"Vous venez de gagner 2 points boutiques pour votre activit\u00e9 sur le serveur.");
				} catch (Exception e) {
					e.printStackTrace();
					Console.println("Erreur PointsTimer du joueur " + this.player.getName() + " : " + e.getMessage(),
							Console.Color.ERROR);
				}
			}
		}).launch();
	}

	public void checkVote() {
		(this.checkVote = new Manageable() {
			private Player player;

			@Override
			public void launch() {
				this.player = Player.this.getThis();
				GlobalManager.worldSheduler.schedule(this, 15L, TimeUnit.MINUTES);
			}

			@Override
			public void run() {
				try {
					if (Player.this.checkVote == null) {
						return;
					}
					if (this.player == null) {
						return;
					}
					if (!this.player.isOnline()) {
						Player.this.checkVote = null;
						return;
					}
					final String IP = this.player.getAccount().getLastIP();
					final long now = System.currentTimeMillis() / 1000L;
					boolean vote = true;
					for (final Map.Entry<Integer, Account> entry : World.getAccounts().entrySet()) {
						final Account a = entry.getValue();
						if (a == null) {
							continue;
						}
						if (a.getLastVoteIP() == null) {
							continue;
						}
						if (a.getLastVoteIP().equalsIgnoreCase("")) {
							continue;
						}
						if (!a.getLastVoteIP().equalsIgnoreCase(IP)) {
							continue;
						}
						final long h = a.getHeureVote();
						if (h + 10800L > now) {
							vote = false;
							break;
						}
					}
					if (vote) {
						SocketManager.GAME_SEND_MESSAGE(this.player,
								"<b>Vous pouvez de nouveau voter sur le <a href='http://aestia.fr/'>site</a>.</b>");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Console.println("Erreur checkVote du joueur " + this.player.getName() + " : " + e.getMessage(),
							Console.Color.ERROR);
				}
			}
		}).launch();
	}

	public Player getThis() {
		return this;
	}

	public void SetSeeFriendOnline(final boolean bool) {
		this._showFriendConnection = bool;
	}

	public void sendGameCreate() {
		this.set_Online(true);
		this._compte.setCurPerso(this);
		if (this._compte.getGameClient() == null) {
			return;
		}
		final GameClient out = this._compte.getGameClient();
		SocketManager.GAME_SEND_GAME_CREATE(out, this.getName());
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getAccountData().setLogged(this.getAccID(), 1);
		Database.getStatique().getPlayerData().updateLogged(this.id, 1);
		this.verifEquiped();
		SocketManager.GAME_SEND_MAPDATA(out, this.curMap.getId(), this.curMap.getDate(), this.curMap.getKey());
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT(out, this.getCurMap());
		this.curMap.addPlayer(this);
	}

	public String parseToOa() {
		final StringBuilder packetOa = new StringBuilder();
		packetOa.append("Oa").append(this.getId()).append("|").append(this.getGMStuffString());
		return packetOa.toString();
	}

	public String parseToGM() {
		final StringBuilder str = new StringBuilder();
		if (this._fight == null) {
			str.append(this.curCell.getId()).append(";").append(this._orientation).append(";");
			str.append("0").append(";");
			str.append(this.getId()).append(";").append(this.getName()).append(";").append(this.getClasse());
			str.append((this.get_title() > 0) ? ("," + this.get_title() + ";") : ";");
			int gfx = this._gfxID;
			if (this.getObjetByPos(21) != null && this.getObjetByPos(21).getTemplate().getId() == 10681) {
				gfx = 8037;
			}
			str.append(gfx).append("^").append(this._size);
			if (this.getObjetByPos(24) != null) {
				str.append(",").append(Constant.getItemIdByMascotteId(this.getObjetByPos(24).getTemplate().getId()))
						.append("^100");
			}
			str.append(";").append(this.getSexe()).append(";");
			str.append(this._align).append(",");
			str.append("0").append(",");
			str.append(this._showWings ? this.getGrade() : "0").append(",");
			str.append(this.getLevel() + this.getId());
			if (this._showWings && this._deshonor > 0) {
				str.append(",");
				str.append((this._deshonor > 0) ? 1 : 0).append(';');
			} else {
				str.append(";");
			}
			int color1 = this.getColor1();
			int color2 = this.getColor2();
			int color3 = this.getColor3();
			if (this.getObjetByPos(22) != null && this.getObjetByPos(22).getTemplate().getId() == 10838) {
				color1 = 16342021;
				color2 = 16342021;
				color3 = 16342021;
			}
			str.append((color1 == -1) ? "-1" : Integer.toHexString(color1)).append(";");
			str.append((color2 == -1) ? "-1" : Integer.toHexString(color2)).append(";");
			str.append((color3 == -1) ? "-1" : Integer.toHexString(color3)).append(";");
			str.append(this.getGMStuffString()).append(";");
			if (this.hasEquiped(10054) || this.hasEquiped(10055) || this.hasEquiped(10056) || this.hasEquiped(10058)
					|| this.hasEquiped(10061) || this.hasEquiped(10102)) {
				str.append(3).append(";");
				this.set_title(2);
			} else {
				if (this.get_title() == 2) {
					this.set_title(0);
				}
				final Groupes g = this.getGroupe();
				int level = this.getLevel();
				if (g != null && (!g.isPlayer() || this.get_size() <= 0)) {
					level = 1;
				}
				str.append((level > 99) ? ((level > 199) ? 2 : 1) : 0).append(";");
			}
			str.append(";");
			str.append(";");
			if (this._guildMember != null && this._guildMember.getGuild().haveTenMembers()) {
				str.append(this._guildMember.getGuild().getName()).append(";")
						.append(this._guildMember.getGuild().getEmblem()).append(";");
			} else {
				str.append(";;");
			}
			if (this.isDead == 1 && !this.isGhost) {
				str.append("-1");
			}
			str.append(this.getSpeed()).append(";");
			str.append(
					(this._onMount && this._mount != null) ? this._mount.getStringColor(this.parsecolortomount()) : "")
					.append(";");
			str.append(String.valueOf(this.isDead()) + ";");
		}
		return str.toString();
	}

	public String parseToMerchant() {
		final StringBuilder str = new StringBuilder();
		str.append(this.curCell.getId()).append(";");
		str.append(this._orientation).append(";");
		str.append("0").append(";");
		str.append(this.getId()).append(";");
		str.append(this.getName()).append(";");
		str.append("-5").append(";");
		str.append(this._gfxID).append("^").append(this._size).append(";");
		int color1 = this.getColor1();
		int color2 = this.getColor2();
		int color3 = this.getColor3();
		if (this.getObjetByPos(22) != null && this.getObjetByPos(22).getTemplate().getId() == 10838) {
			color1 = 16342021;
			color2 = 16342021;
			color3 = 16342021;
		}
		str.append((color1 == -1) ? "-1" : Integer.toHexString(color1)).append(";");
		str.append((color2 == -1) ? "-1" : Integer.toHexString(color2)).append(";");
		str.append((color3 == -1) ? "-1" : Integer.toHexString(color3)).append(";");
		str.append(this.getGMStuffString()).append(";");
		str.append((this._guildMember != null) ? this._guildMember.getGuild().getName() : "").append(";");
		str.append((this._guildMember != null) ? this._guildMember.getGuild().getEmblem() : "").append(";");
		str.append("0;");
		return str.toString();
	}

	public String getGMStuffString() {
		final StringBuilder str = new StringBuilder();
		org.aestia.object.Object object = this.getObjetByPos(1);
		if (object != null) {
			str.append(Integer.toHexString(object.getTemplate().getId()));
		}
		str.append(",");
		object = this.getObjetByPos(6);
		if (object != null) {
			object.parseStatsString();
			final Integer obvi = object.getStats().getMap().get(970);
			if (obvi == null) {
				str.append(Integer.toHexString(object.getTemplate().getId()));
			} else {
				str.append(String.valueOf(Integer.toHexString(obvi)) + "~16~").append(object.getObvijevanLook());
			}
		}
		str.append(",");
		object = this.getObjetByPos(7);
		if (object != null) {
			object.parseStatsString();
			final Integer obvi = object.getStats().getMap().get(970);
			if (obvi == null) {
				str.append(Integer.toHexString(object.getTemplate().getId()));
			} else {
				str.append(String.valueOf(Integer.toHexString(obvi)) + "~17~").append(object.getObvijevanLook());
			}
		}
		str.append(",");
		object = this.getObjetByPos(8);
		if (object != null) {
			str.append(Integer.toHexString(object.getTemplate().getId()));
		}
		str.append(",");
		object = this.getObjetByPos(15);
		if (object != null) {
			str.append(Integer.toHexString(object.getTemplate().getId()));
		}
		return str.toString();
	}

	public String getAsPacket() {
		this.refreshStats();
		this.refreshLife(true);
		final StringBuilder ASData = new StringBuilder();
		ASData.append("As").append(this.xpString(",")).append("|");
		ASData.append(this._kamas).append("|").append(this._capital).append("|").append(this._spellPts).append("|");
		ASData.append(this._align).append("~").append(this._align).append(",").append(this._aLvl).append(",")
				.append(this.getGrade()).append(",").append(this._honor).append(",")
				.append(String.valueOf(this._deshonor) + ",").append(this._showWings ? "1" : "0").append("|");
		int pdv = this.curPdv;
		int pdvMax = this.maxPdv;
		if (this._fight != null) {
			final Fighter f = this._fight.getFighterByPerso(this);
			if (f != null) {
				pdv = f.getPdv();
				pdvMax = f.getPdvMax();
			}
		}
		final Stats stats = Stats.cumulStat(statsParcho, this.getStats());
		ASData.append(pdv).append(",").append(pdvMax).append("|");
		ASData.append(this.getEnergy()).append(",10000|");
		ASData.append(this.getInitiative()).append("|");
		ASData.append(stats.getEffect(176) + this.getStuffStats().getEffect(176)
				+ (int) Math.ceil(this.getTotalStats().getEffect(123) / 10) + this.getBuffsStats().getEffect(176))
				.append("|");
		ASData.append(stats.getEffect(111)).append(",").append(this.getStuffStats().getEffect(111)).append(",")
				.append(this.getDonsStats().getEffect(111)).append(",").append(this.getBuffsStats().getEffect(111))
				.append(",").append(this.getTotalStats().getEffect(111)).append("|");
		ASData.append(stats.getEffect(128)).append(",").append(this.getStuffStats().getEffect(128)).append(",")
				.append(this.getDonsStats().getEffect(128)).append(",").append(this.getBuffsStats().getEffect(128))
				.append(",").append(this.getTotalStats().getEffect(128)).append("|");
		ASData.append(stats.getEffect(118)).append(",").append(this.getStuffStats().getEffect(118)).append(",")
				.append(this.getDonsStats().getEffect(118)).append(",").append(this.getBuffsStats().getEffect(118))
				.append("|");
		ASData.append(stats.getEffect(125)).append(",").append(this.getStuffStats().getEffect(125)).append(",")
				.append(this.getDonsStats().getEffect(125)).append(",").append(this.getBuffsStats().getEffect(125))
				.append("|");
		ASData.append(stats.getEffect(124)).append(",").append(this.getStuffStats().getEffect(124)).append(",")
				.append(this.getDonsStats().getEffect(124)).append(",").append(this.getBuffsStats().getEffect(124))
				.append("|");
		ASData.append(stats.getEffect(123)).append(",").append(this.getStuffStats().getEffect(123)).append(",")
				.append(this.getDonsStats().getEffect(123)).append(",").append(this.getBuffsStats().getEffect(123))
				.append("|");
		ASData.append(stats.getEffect(119)).append(",").append(this.getStuffStats().getEffect(119)).append(",")
				.append(this.getDonsStats().getEffect(119)).append(",").append(this.getBuffsStats().getEffect(119))
				.append("|");
		ASData.append(stats.getEffect(126)).append(",").append(this.getStuffStats().getEffect(126)).append(",")
				.append(this.getDonsStats().getEffect(126)).append(",").append(this.getBuffsStats().getEffect(126))
				.append("|");
		ASData.append(stats.getEffect(117)).append(",").append(this.getStuffStats().getEffect(117)).append(",")
				.append(this.getDonsStats().getEffect(117)).append(",").append(this.getBuffsStats().getEffect(117))
				.append("|");
		ASData.append(stats.getEffect(182)).append(",").append(this.getStuffStats().getEffect(182)).append(",")
				.append(this.getDonsStats().getEffect(182)).append(",").append(this.getBuffsStats().getEffect(182))
				.append("|");
		ASData.append(stats.getEffect(112)).append(",").append(this.getStuffStats().getEffect(112)).append(",")
				.append(this.getDonsStats().getEffect(112)).append(",").append(this.getBuffsStats().getEffect(112))
				.append("|");
		ASData.append(stats.getEffect(142)).append(",").append(this.getStuffStats().getEffect(142)).append(",")
				.append(this.getDonsStats().getEffect(142)).append(",").append(this.getBuffsStats().getEffect(142))
				.append("|");
		ASData.append(stats.getEffect(165)).append(",").append(this.getStuffStats().getEffect(165)).append(",")
				.append(this.getDonsStats().getEffect(165)).append(",").append(this.getBuffsStats().getEffect(165))
				.append("|");
		ASData.append(stats.getEffect(138)).append(",").append(this.getStuffStats().getEffect(138))
				.append("," + this.getDonsStats().getEffect(138)).append(",")
				.append(this.getBuffsStats().getEffect(138)).append("|");
		ASData.append(stats.getEffect(178)).append(",").append(this.getStuffStats().getEffect(178)).append(",")
				.append(this.getDonsStats().getEffect(178)).append(",").append(this.getBuffsStats().getEffect(178))
				.append("|");
		ASData.append(stats.getEffect(225)).append(",").append(this.getStuffStats().getEffect(225)).append(",")
				.append(this.getDonsStats().getEffect(225)).append(",").append(this.getBuffsStats().getEffect(225))
				.append("|");
		ASData.append(stats.getEffect(226)).append(",").append(this.getStuffStats().getEffect(226)).append(",")
				.append(this.getDonsStats().getEffect(226)).append(",").append(this.getBuffsStats().getEffect(226))
				.append("|");
		ASData.append(stats.getEffect(220)).append(",").append(this.getStuffStats().getEffect(220)).append(",")
				.append(this.getDonsStats().getEffect(220)).append(",").append(this.getBuffsStats().getEffect(220))
				.append("|");
		ASData.append(stats.getEffect(115)).append(",").append(this.getStuffStats().getEffect(115)).append(",")
				.append(this.getDonsStats().getEffect(115)).append(",").append(this.getBuffsStats().getEffect(115))
				.append("|");
		ASData.append(stats.getEffect(122)).append(",").append(this.getStuffStats().getEffect(122)).append(",")
				.append(this.getDonsStats().getEffect(122)).append(",").append(this.getBuffsStats().getEffect(122))
				.append("|");
		ASData.append(stats.getEffect(160)).append(",").append(this.getStuffStats().getEffect(160)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(160)).append(",")
				.append(this.getBuffsStats().getEffect(160)).append("|");
		ASData.append(stats.getEffect(161)).append(",").append(this.getStuffStats().getEffect(161)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(161)).append(",")
				.append(this.getBuffsStats().getEffect(161)).append("|");
		ASData.append(stats.getEffect(241)).append(",").append(this.getStuffStats().getEffect(241)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(241)).append(",")
				.append(this.getBuffsStats().getEffect(241)).append("|");
		ASData.append(stats.getEffect(214)).append(",").append(this.getStuffStats().getEffect(214)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(214)).append(",")
				.append(this.getBuffsStats().getEffect(214)).append("|");
		ASData.append(stats.getEffect(264)).append(",").append(this.getStuffStats().getEffect(264)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(264)).append(",")
				.append(this.getBuffsStats().getEffect(264)).append("|");
		ASData.append(stats.getEffect(254)).append(",").append(this.getStuffStats().getEffect(254)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(254)).append(",")
				.append(this.getBuffsStats().getEffect(254)).append("|");
		ASData.append(stats.getEffect(242)).append(",").append(this.getStuffStats().getEffect(242)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(242)).append(",")
				.append(this.getBuffsStats().getEffect(242)).append("|");
		ASData.append(stats.getEffect(210)).append(",").append(this.getStuffStats().getEffect(210)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(210)).append(",")
				.append(this.getBuffsStats().getEffect(210)).append("|");
		ASData.append(stats.getEffect(260)).append(",").append(this.getStuffStats().getEffect(260)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(260)).append(",")
				.append(this.getBuffsStats().getEffect(260)).append("|");
		ASData.append(stats.getEffect(250)).append(",").append(this.getStuffStats().getEffect(250)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(250)).append(",")
				.append(this.getBuffsStats().getEffect(250)).append("|");
		ASData.append(stats.getEffect(243)).append(",").append(this.getStuffStats().getEffect(243)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(243)).append(",")
				.append(this.getBuffsStats().getEffect(243)).append("|");
		ASData.append(stats.getEffect(211)).append(",").append(this.getStuffStats().getEffect(211)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(211)).append(",")
				.append(this.getBuffsStats().getEffect(211)).append("|");
		ASData.append(stats.getEffect(261)).append(",").append(this.getStuffStats().getEffect(261)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(261)).append(",")
				.append(this.getBuffsStats().getEffect(261)).append("|");
		ASData.append(stats.getEffect(251)).append(",").append(this.getStuffStats().getEffect(251)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(251)).append(",")
				.append(this.getBuffsStats().getEffect(251)).append("|");
		ASData.append(stats.getEffect(244)).append(",").append(this.getStuffStats().getEffect(244)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(244)).append(",")
				.append(this.getBuffsStats().getEffect(244)).append("|");
		ASData.append(stats.getEffect(212)).append(",").append(this.getStuffStats().getEffect(212)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(212)).append(",")
				.append(this.getBuffsStats().getEffect(212)).append("|");
		ASData.append(stats.getEffect(262)).append(",").append(this.getStuffStats().getEffect(262)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(262)).append(",")
				.append(this.getBuffsStats().getEffect(262)).append("|");
		ASData.append(stats.getEffect(252)).append(",").append(this.getStuffStats().getEffect(252)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(252)).append(",")
				.append(this.getBuffsStats().getEffect(252)).append("|");
		ASData.append(stats.getEffect(240)).append(",").append(this.getStuffStats().getEffect(240)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(240)).append(",")
				.append(this.getBuffsStats().getEffect(240)).append("|");
		ASData.append(stats.getEffect(213)).append(",").append(this.getStuffStats().getEffect(213)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(213)).append(",")
				.append(this.getBuffsStats().getEffect(213)).append("|");
		ASData.append(stats.getEffect(263)).append(",").append(this.getStuffStats().getEffect(263)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(263)).append(",")
				.append(this.getBuffsStats().getEffect(263)).append("|");
		ASData.append(stats.getEffect(253)).append(",").append(this.getStuffStats().getEffect(253)).append(",")
				.append(0).append(",").append(this.getBuffsStats().getEffect(253)).append(",")
				.append(this.getBuffsStats().getEffect(253)).append("|");
		return ASData.toString();
	}

	public int getGrade() {
		if (this._align == -1) {
			return 0;
		}
		if (this._honor >= 17500) {
			return 10;
		}
		for (int n = 1; n <= 10; ++n) {
			if (this._honor < World.getExpLevel(n).pvp) {
				return n - 1;
			}
		}
		return 0;
	}

	public String xpString(final String c) {
		if (!this._morphMode) {
			return String.valueOf(this.getExp()) + c + World.getPersoXpMin(this.getLevel()) + c
					+ World.getPersoXpMax(this.getLevel());
		}
		if (this.getObjetByPos(1) != null && Constant.isIncarnationWeapon(this.getObjetByPos(1).getTemplate().getId())
				&& this.getObjetByPos(1).getSoulStat().get(1000) != null) {
			return this.getObjetByPos(1).getSoulStat().get(1000) + c
					+ World.getBanditsXpMin(this.getObjetByPos(1).getSoulStat().get(962)) + c
					+ World.getBanditsXpMax(this.getObjetByPos(1).getSoulStat().get(962));
		}
		return String.valueOf(1) + c + 1 + c + 1;
	}

	public int emoteActive() {
		return this._emoteActive;
	}

	public void setEmoteActive(final int emoteActive) {
		this._emoteActive = emoteActive;
	}

	public Stats getStuffStats() {
		if (this.useStats) {
			return new Stats();
		}
		Stats stats = new Stats(false, null);
		final ArrayList<Integer> itemSetApplied = new ArrayList<Integer>();
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final int ObjType = entry.getValue().getTemplate().getType();
				if (entry.getValue().getPosition() != -1 && ObjType != 42 && ObjType != 33 && ObjType != 37
						&& ObjType != 69 && ObjType != 49 && ObjType != 13) {
					if (ObjType == 12) {
						continue;
					}
					stats = Stats.cumulStat(stats, entry.getValue().getStats());
					final int panID = entry.getValue().getTemplate().getPanoId();
					if (panID <= 0 || itemSetApplied.contains(panID)) {
						continue;
					}
					itemSetApplied.add(panID);
					final ObjectSet IS = World.getItemSet(panID);
					if (IS == null) {
						continue;
					}
					stats = Stats.cumulStat(stats, IS.getBonusStatByItemNumb(this.getNumbEquipedItemOfPanoplie(panID)));
				}
			}
		}
		// monitorexit(this._items)
		if (this._onMount && this._mount != null) {
			stats = Stats.cumulStat(stats, this._mount.getStats());
		}
		final org.aestia.object.Object obj = this.getObjetByPos(25);
		if (obj != null && obj.getTemplate() != null) {
			stats = Stats.cumulStat(stats,
					obj.getTemplate().generateNewStatsFromTemplate(obj.getTemplate().getStrTemplate(), true));
		}
		return stats;
	}

	public Stats getBuffsStats() {
		final Stats stats = new Stats(false, null);
		for (final Map.Entry<Integer, SpellEffect> entry : this._buffs.entrySet()) {
			stats.addOneStat(entry.getValue().getEffectID(), entry.getValue().getValue());
		}
		return stats;
	}

	public int get_orientation() {
		return this._orientation;
	}

	public void set_orientation(final int _orientation) {
		this._orientation = _orientation;
	}

	public int getInitiative() {
		if (!this.useStats) {
			int fact = 4;
			final int maxPdv = this.maxPdv - 55;
			final int curPdv = this.curPdv - 55;
			if (this.getClasse() == 11) {
				fact = 8;
			}
			double coef = maxPdv / fact;
			coef += this.getStuffStats().getEffect(174);
			coef += this.getTotalStats().getEffect(119);
			coef += this.getTotalStats().getEffect(123);
			coef += this.getTotalStats().getEffect(126);
			coef += this.getTotalStats().getEffect(118);
			int init = 1;
			if (maxPdv != 0) {
				init = (int) (coef * (curPdv / maxPdv));
			}
			if (init < 0) {
				init = 0;
			}
			return init;
		}
		return this.initiative;
	}

	public Stats getTotalStats() {
		Stats total = new Stats(false, null);
		if (!this.useStats) {
			total = Stats.cumulStat(total, this.getStats());
			total = Stats.cumulStat(total, this.getStuffStats());
			total = Stats.cumulStat(total, this.getDonsStats());
			total = Stats.cumulStat(total, this.getStatsParcho());
			if (this._fight == null) {
				total = Stats.cumulStat(total, this.getBuffsStats());
			}
			return total;
		}
		return this.newStatsMorph();
	}

	public Stats getDonsStats() {
		final Stats stats = new Stats(false, null);
		return stats;
	}

	public Stats newStatsMorph() {
		final Stats stats = new Stats();
		stats.addOneStat(111, this.pa);
		stats.addOneStat(128, this.pm);
		stats.addOneStat(125, this.vitalite);
		stats.addOneStat(124, this.sagesse);
		stats.addOneStat(118, this.terre);
		stats.addOneStat(126, this.feu);
		stats.addOneStat(123, this.eau);
		stats.addOneStat(119, this.air);
		stats.addOneStat(174, this.initiative);
		stats.addOneStat(176, 100);
		this.useCac = false;
		return stats;
	}

	public int getPodUsed() {
		int pod = 0;
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				pod += entry.getValue().getTemplate().getPod() * entry.getValue().getQuantity();
			}
		}
		// monitorexit(this._items)
		pod += this.parseStoreItemsListPods();
		return pod;
	}

	public int getMaxPod() {
		Stats total = new Stats(false, null);
		total = Stats.cumulStat(total, this.getStats());
		total = Stats.cumulStat(total, this.getStuffStats());
		total = Stats.cumulStat(total, this.getDonsStats());
		int pods = total.getEffect(158);
		pods += total.getEffect(118) * 5;
		for (final JobStat SM : this._metiers.values()) {
			pods += SM.get_lvl() * 5;
			if (SM.get_lvl() == 100) {
				pods += 1000;
			}
		}
		if (pods < 1000) {
			pods = 1000;
		}
		return pods;
	}

	public void setSitted(final boolean sitted) {
		if (this.sitted == sitted) {
			return;
		}
		this.sitted = sitted;
		this.refreshLife(false);
		this.regenRate = (sitted ? 1000 : 2000);
		SocketManager.send(this, "ILS" + this.regenRate);
	}

	public void refreshLife(final boolean refresh) {
		if (this.get_isClone()) {
			return;
		}
		final long time = System.currentTimeMillis() - this.regenTime;
		this.regenTime = System.currentTimeMillis();
		if (this._fight != null) {
			return;
		}
		if (this.regenRate == 0) {
			return;
		}
		if (this.curPdv > this.maxPdv) {
			this.curPdv = this.maxPdv - 1;
			if (!refresh) {
				SocketManager.GAME_SEND_STATS_PACKET(this);
			}
			return;
		}
		final int diff = (int) time / this.regenRate;
		if (diff >= 10 && this.curPdv < this.maxPdv && this.regenRate == 2000) {
			SocketManager.send(this, "ILF" + diff);
		}
		this.setPdv(this.curPdv + diff);
	}

	public byte get_align() {
		return this._align;
	}

	public int get_pdvper() {
		this.refreshLife(false);
		int pdvper = 100;
		pdvper = 100 * this.curPdv / this.maxPdv;
		if (pdvper > 100) {
			return 100;
		}
		return pdvper;
	}

	public void useSmiley(final String str) {
		try {
			final int id = Integer.parseInt(str);
			final org.aestia.map.Map map = this.curMap;
			if (this._fight == null) {
				SocketManager.GAME_SEND_EMOTICONE_TO_MAP(map, this.getId(), id);
			} else {
				SocketManager.GAME_SEND_EMOTICONE_TO_FIGHT(this._fight, 7, this.getId(), id);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void boostStat(final int stat, final boolean capital) {
		int value = 0;
		switch (stat) {
		case 10: {
			value = this.getStats().getEffect(118);
			break;
		}
		case 13: {
			value = this.getStats().getEffect(123);
			break;
		}
		case 14: {
			value = this.getStats().getEffect(119);
			break;
		}
		case 15: {
			value = this.getStats().getEffect(126);
			break;
		}
		}
		int cout = Constant.getReqPtsToBoostStatsByClass(this.getClasse(), stat, value);
		if (!capital) {
			cout = 0;
		}
		if (cout <= this._capital) {
			switch (stat) {
			case 11: {
				if (this.getClasse() != 11) {
					this.getStats().addOneStat(125, 1);
					break;
				}
				this.getStats().addOneStat(125, 2);
				break;
			}
			case 12: {
				this.getStats().addOneStat(124, 1);
				break;
			}
			case 10: {
				this.getStats().addOneStat(118, 1);
				break;
			}
			case 13: {
				this.getStats().addOneStat(123, 1);
				break;
			}
			case 14: {
				this.getStats().addOneStat(119, 1);
				break;
			}
			case 15: {
				this.getStats().addOneStat(126, 1);
				break;
			}
			default: {
				return;
			}
			}
			this._capital -= cout;
			SocketManager.GAME_SEND_STATS_PACKET(this);
			Database.getStatique().getPlayerData().update(this, false);
		}
	}

	public void boostStatFixedCount(final int stat, final int countVal) {
		for (int i = 0; i < countVal; ++i) {
			int value = 0;
			switch (stat) {
			case 10: {
				value = this.getStats().getEffect(118);
				break;
			}
			case 13: {
				value = this.getStats().getEffect(123);
				break;
			}
			case 14: {
				value = this.getStats().getEffect(119);
				break;
			}
			case 15: {
				value = this.getStats().getEffect(126);
				break;
			}
			}
			final int cout = Constant.getReqPtsToBoostStatsByClass(this.getClasse(), stat, value);
			if (cout <= this._capital) {
				switch (stat) {
				case 11: {
					if (this.getClasse() != 11) {
						this.getStats().addOneStat(125, 1);
						break;
					}
					this.getStats().addOneStat(125, 2);
					break;
				}
				case 12: {
					this.getStats().addOneStat(124, 1);
					break;
				}
				case 10: {
					this.getStats().addOneStat(118, 1);
					break;
				}
				case 13: {
					this.getStats().addOneStat(123, 1);
					break;
				}
				case 14: {
					this.getStats().addOneStat(119, 1);
					break;
				}
				case 15: {
					this.getStats().addOneStat(126, 1);
					break;
				}
				default: {
					return;
				}
				}
				this._capital -= cout;
			}
		}
		SocketManager.GAME_SEND_STATS_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void setCraft(final boolean action) {
		this._isCraft = action;
	}

	public boolean isCraft() {
		return this._isCraft;
	}

	public boolean isMuted() {
		return this._compte.isMuted();
	}

	public void setCurMap(final org.aestia.map.Map Map) {
		this.curMap = Map;
	}

	public String parseObjetsToDB() {
		final StringBuilder str = new StringBuilder();
		synchronized (this._items) {
			if (this._items.isEmpty()) {
				// monitorexit(this._items)
				return "";
			}
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				if (obj == null) {
					continue;
				}
				str.append(obj.getGuid()).append("|");
			}
		}
		// monitorexit(this._items)
		return str.toString();
	}

	public boolean addObjet(final org.aestia.object.Object newObj, final boolean stackIfSimilar) {
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				if (ConditionParser.stackIfSimilar(obj, newObj, stackIfSimilar)) {
					obj.setQuantity(obj.getQuantity() + newObj.getQuantity());
					Database.getStatique().getItemData().save(obj, false);
					if (this._isOnline) {
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, obj);
					}
					// monitorexit(this._items)
					return false;
				}
			}
			this._items.put(newObj.getGuid(), newObj);
		}
		// monitorexit(this._items)
		SocketManager.GAME_SEND_OAKO_PACKET(this, newObj);
		return true;
	}

	public void addObjet(final org.aestia.object.Object newObj) {
		synchronized (this._items) {
			this._items.put(newObj.getGuid(), newObj);
		}
		// monitorexit(this._items)
		SocketManager.GAME_SEND_OAKO_PACKET(this, newObj);
	}

	public Map<Integer, org.aestia.object.Object> getItems() {
		return this._items;
	}

	public String parseItemToASK() {
		final StringBuilder str = new StringBuilder();
		synchronized (this._items) {
			if (this._items.isEmpty()) {
				// monitorexit(this._items)
				return "";
			}
			for (final org.aestia.object.Object obj : this._items.values()) {
				str.append(obj.parseItem());
			}
		}
		// monitorexit(this._items)
		return str.toString();
	}

	public String getBankItemsIDSplitByChar(final String splitter) {
		final StringBuilder str = new StringBuilder();
		if (this._compte.getBank().isEmpty()) {
			return "";
		}
		for (final int entry : this._compte.getBank().keySet()) {
			str.append(entry).append(splitter);
		}
		return str.toString();
	}

	public String getItemsIDSplitByChar(final String splitter) {
		final StringBuilder str = new StringBuilder();
		synchronized (this._items) {
			if (this._items.isEmpty()) {
				// monitorexit(this._items)
				return "";
			}
			for (final int entry : this._items.keySet()) {
				if (str.length() != 0) {
					str.append(splitter);
				}
				str.append(entry);
			}
		}
		// monitorexit(this._items)
		return str.toString();
	}

	public String getStoreItemsIDSplitByChar(final String splitter) {
		final StringBuilder str = new StringBuilder();
		synchronized (this._storeItems) {
			if (this._storeItems.isEmpty()) {
				// monitorexit(this._storeItems)
				return "";
			}
			for (final int entry : this._storeItems.keySet()) {
				if (str.length() != 0) {
					str.append(splitter);
				}
				str.append(entry);
			}
		}
		// monitorexit(this._storeItems)
		return str.toString();
	}

	public boolean hasItemGuid(final int guid) {
		synchronized (this._items) {
			// monitorexit(this._items)
			return this._items.get(guid) != null && this._items.get(guid).getQuantity() > 0;
		}
	}

	public void sellItem(final int guid, int qua) {
		if (qua <= 0) {
			return;
		}
		synchronized (this._items) {
			if (this._items.get(guid).getQuantity() < qua) {
				qua = this._items.get(guid).getQuantity();
			}
			final int prix = qua * (this._items.get(guid).getTemplate().getPrice() / 10);
			final int newQua = this._items.get(guid).getQuantity() - qua;
			if (newQua <= 0) {
				this._items.remove(guid);
				World.removeItem(guid);
				Database.getStatique().getItemData().delete(guid);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			} else {
				this._items.get(guid).setQuantity(newQua);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, this._items.get(guid));
			}
			this._kamas += prix;
		}
		// monitorexit(this._items)
		SocketManager.GAME_SEND_STATS_PACKET(this);
		SocketManager.GAME_SEND_Ow_PACKET(this);
		SocketManager.GAME_SEND_ESK_PACKEt(this);
	}

	public void removeItem(final int guid) {
		synchronized (this._items) {
			this._items.remove(guid);
		}
		// monitorexit(this._items)
	}

	public void removeItem(final int guid, int nombre, final boolean send, final boolean deleteFromWorld) {
		synchronized (this._items) {
			final org.aestia.object.Object obj = this._items.get(guid);
			if (nombre > obj.getQuantity()) {
				nombre = obj.getQuantity();
			}
			if (obj.getQuantity() >= nombre) {
				final int newQua = obj.getQuantity() - nombre;
				if (newQua > 0) {
					obj.setQuantity(newQua);
					if (send && this._isOnline) {
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, obj);
					}
				} else {
					this._items.remove(obj.getGuid());
					if (deleteFromWorld) {
						World.removeItem(obj.getGuid());
					}
					if (send && this._isOnline) {
						SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
					}
				}
			}
		}
		// monitorexit(this._items)
		SocketManager.GAME_SEND_Ow_PACKET(this);
	}

	public void deleteItem(final int guid) {
		synchronized (this._items) {
			this._items.remove(guid);
		}
		// monitorexit(this._items)
		World.removeItem(guid);
	}

	public org.aestia.object.Object getObjetByPos(final int pos) {
		if (pos == -1) {
			return null;
		}
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				if (obj.getPosition() == pos && pos == 8) {
					if (obj.getTxtStat().isEmpty()) {
						// monitorexit(this._items)
						return null;
					}
					if (World.getPetsEntry(obj.getGuid()) == null) {
						// monitorexit(this._items)
						return null;
					}
				}
				if (obj.getPosition() == pos) {
					// monitorexit(this._items)
					return obj;
				}
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public org.aestia.object.Object getObjetByPos2(final int pos) {
		if (pos == -1) {
			return null;
		}
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				if (obj.getPosition() == pos) {
					// monitorexit(this._items)
					return obj;
				}
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public void refreshStats() {
		final double actPdvPer = 100.0 * this.curPdv / this.maxPdv;
		if (!this.useStats) {
			this.maxPdv = (this.getLevel() - 1) * 5 + 50 + this.getTotalStats().getEffect(125);
		}
		this.curPdv = (int) Math.round(this.maxPdv * actPdvPer / 100.0);
	}

	public boolean levelUp(final boolean send, final boolean addXp) {
		if (this.getLevel() == World.getExpLevelSize()) {
			return false;
		}
		++this.level;
		this._capital += 5;
		++this._spellPts;
		this.maxPdv += 5;
		this.setPdv(this.getMaxPdv());
		SocketManager.GAME_SEND_STATS_PACKET(this);
		if (this.getLevel() == 100) {
			this.getStats().addOneStat(111, 1);
		}
		Constant.onLevelUpSpells(this, this.getLevel());
		if (addXp) {
			this.exp = World.getExpLevel(this.getLevel()).perso;
		}
		if (this.get_guild() != null) {
			Database.getGame().getGuild_memberData().update(this);
			this.getGuildMember().setLevel(this.getLevel());
		}
		if (send && this._isOnline) {
			SocketManager.GAME_SEND_NEW_LVL_PACKET(this._compte.getGameClient(), this.getLevel());
			SocketManager.GAME_SEND_STATS_PACKET(this);
			SocketManager.GAME_SEND_SPELL_LIST(this);
		}
		return true;
	}

	public boolean levelDown(final boolean send, final boolean addXp) {
		if (this.getLevel() == 1) {
			return false;
		}
		--this.level;
		if (this.getLevel() == 99) {
			this.getStats().addOneStat(111, -1);
		}
		if (addXp) {
			this.exp = World.getExpLevel(this.getLevel()).perso;
		}
		if (this.get_guild() != null) {
			Database.getGame().getGuild_memberData().update(this);
			this.getGuildMember().setLevel(this.getLevel());
		}
		if (send && this._isOnline) {
			SocketManager.GAME_SEND_NEW_LVL_PACKET(this._compte.getGameClient(), this.getLevel());
			SocketManager.GAME_SEND_STATS_PACKET(this);
			SocketManager.GAME_SEND_SPELL_LIST(this);
		}
		return true;
	}

	public boolean addXp(final long winxp) {
		boolean up = false;
		this.exp += winxp;
		final int exLevel = this.getLevel();
		while (this.getExp() >= World.getPersoXpMax(this.getLevel()) && this.getLevel() < World.getExpLevelSize()) {
			up = this.levelUp(true, false);
		}
		if (this._isOnline) {
			if (exLevel < this.getLevel()) {
				SocketManager.GAME_SEND_NEW_LVL_PACKET(this._compte.getGameClient(), this.getLevel());
			}
			SocketManager.GAME_SEND_STATS_PACKET(this);
		}
		return up;
	}

	public boolean levelUpIncarnations(final boolean send, final boolean addXp) {
		int level = this.getObjetByPos(1).getSoulStat().get(962);
		if (level == 50) {
			return false;
		}
		++level;
		this.setPdv(this.getMaxPdv());
		SocketManager.GAME_SEND_STATS_PACKET(this);
		switch (level) {
		case 10:
		case 20:
		case 30:
		case 40:
		case 50: {
			this.boostSpellIncarnation();
			break;
		}
		}
		if (send && this._isOnline) {
			SocketManager.GAME_SEND_STATS_PACKET(this);
			SocketManager.GAME_SEND_SPELL_LIST(this);
		}
		this.getObjetByPos(1).getSoulStat().clear();
		this.getObjetByPos(1).getSoulStat().put(962, level);
		this.getObjetByPos(1);
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(this, this.getObjetByPos(1));
		return true;
	}

	public boolean addXpIncarnations(final long winxp) {
		boolean up = false;
		int level = this.getObjetByPos(1).getSoulStat().get(962);
		long exp = this.getObjetByPos(1).getSoulStat().get(1000);
		exp += winxp;
		if (Constant.isBanditsWeapon(this.getObjetByPos(1).getTemplate().getId())) {
			while (exp >= World.getBanditsXpMax(level)) {
				if (level >= 50) {
					break;
				}
				up = this.levelUpIncarnations(true, false);
				level = this.getObjetByPos(1).getSoulStat().get(962);
			}
		} else if (Constant.isTourmenteurWeapon(this.getObjetByPos(1).getTemplate().getId())) {
			while (exp >= World.getTourmenteursXpMax(level) && level < 50) {
				up = this.levelUpIncarnations(true, false);
				level = this.getObjetByPos(1).getSoulStat().get(962);
			}
		}
		if (this._isOnline) {
			SocketManager.GAME_SEND_STATS_PACKET(this);
		}
		level = this.getObjetByPos(1).getSoulStat().get(962);
		this.getObjetByPos(1).getSoulStat().clear();
		this.getObjetByPos(1).getSoulStat().put(962, level);
		this.getObjetByPos(1).getSoulStat().put(1000, (int) exp);
		Database.getStatique().getItemData().save(this.getObjetByPos(1), false);
		return up;
	}

	public void addKamas(final long l) {
		this._kamas += l;
	}

	public org.aestia.object.Object getSimilarItem(final org.aestia.object.Object exObj) {
		if (exObj.getTemplate().getId() == 8378) {
			return null;
		}
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				if (obj.getTemplate().getId() == exObj.getTemplate().getId()
						&& obj.getStats().isSameStats(exObj.getStats()) && obj.getGuid() != exObj.getGuid()
						&& !Constant.isIncarnationWeapon(exObj.getTemplate().getId())
						&& exObj.getTemplate().getType() != 77 && exObj.getTemplate().getType() != 85
						&& obj.getTemplate().getType() != 93 && obj.getTemplate().getType() != 97
						&& (exObj.getTemplate().getType() != 24 || Constant.isFlacGelee(obj.getTemplate().getId()))
						&& !Constant.isCertificatDopeuls(obj.getTemplate().getId()) && obj.getTemplate().getType() != 18
						&& obj.getTemplate().getType() != 113 && obj.getPosition() == -1) {
					// monitorexit(this._items)
					return obj;
				}
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public void setCurExchange(final PlayerExchange echg) {
		this.curExchange = echg;
	}

	public PlayerExchange getCurExchange() {
		return this.curExchange;
	}

	public PlayerExchange.NpcExchange getCurNpcExchange() {
		return this.curNpcExchange;
	}

	public void setCurNpcExchange(final PlayerExchange.NpcExchange echg) {
		this.curNpcExchange = echg;
	}

	public PlayerExchange.NpcExchangePets getCurNpcExchangePets() {
		return this.curNpcExchangePets;
	}

	public void setCurNpcExchangePets(final PlayerExchange.NpcExchangePets echg) {
		this.curNpcExchangePets = echg;
	}

	public PlayerExchange.NpcRessurectPets getCurNpcRessurectPets() {
		return this.curNpcRessurectPets;
	}

	public void setCurNpcRessurectPets(final PlayerExchange.NpcRessurectPets echg) {
		this.curNpcRessurectPets = echg;
	}

	public int learnJob(final Job m) {
		for (final Map.Entry<Integer, JobStat> entry : this._metiers.entrySet()) {
			if (entry.getValue().getTemplate().getId() == m.getId()) {
				return -1;
			}
		}
		final int Msize = this._metiers.size();
		if (Msize == 6) {
			return -1;
		}
		int pos = 0;
		if (JobConstant.isMageJob(m.getId())) {
			if (this._metiers.get(5) == null) {
				pos = 5;
			}
			if (this._metiers.get(4) == null) {
				pos = 4;
			}
			if (this._metiers.get(3) == null) {
				pos = 3;
			}
		} else {
			if (this._metiers.get(2) == null) {
				pos = 2;
			}
			if (this._metiers.get(1) == null) {
				pos = 1;
			}
			if (this._metiers.get(0) == null) {
				pos = 0;
			}
		}
		final JobStat sm = new JobStat(pos, m, 1, 0L, this);
		this._metiers.put(pos, sm);
		if (this._isOnline) {
			final ArrayList<JobStat> list = new ArrayList<JobStat>();
			list.add(sm);
			SocketManager.GAME_SEND_Im_PACKET(this, "02;" + m.getId());
			SocketManager.GAME_SEND_JS_PACKET(this, list);
			SocketManager.GAME_SEND_JX_PACKET(this, list);
			SocketManager.GAME_SEND_JO_PACKET(this, list);
			final org.aestia.object.Object obj = this.getObjetByPos(1);
			if (obj != null && sm.getTemplate().isValidTool(obj.getTemplate().getId())) {
				SocketManager.GAME_SEND_OT_PACKET(this._compte.getGameClient(), m.getId());
			}
		}
		return pos;
	}

	public void unlearnJob(final int m) {
		this._metiers.remove(m);
	}

	public void unequipedObjet(final org.aestia.object.Object o) {
		o.setPosition(-1);
		final ObjectTemplate oTpl = o.getTemplate();
		final int idSetExObj = oTpl.getPanoId();
		if ((idSetExObj >= 81 && idSetExObj <= 92) || (idSetExObj >= 201 && idSetExObj <= 212)) {
			final String[] stats = oTpl.getStrTemplate().split(",");
			String[] array;
			for (int length = (array = stats).length, i = 0; i < length; ++i) {
				final String stat = array[i];
				final String[] val = stat.split("#");
				final String modifi = String.valueOf(Integer.parseInt(val[0], 16)) + ";" + Integer.parseInt(val[1], 16)
						+ ";0";
				SocketManager.SEND_SB_SPELL_BOOST(this, modifi);
				this.removeItemClasseSpell(Integer.parseInt(val[1], 16));
			}
			this.removeItemClasse(oTpl.getId());
		}
		SocketManager.GAME_SEND_OBJET_MOVE_PACKET(this, o);
		if (oTpl.getPanoId() > 0) {
			SocketManager.GAME_SEND_OS_PACKET(this, oTpl.getPanoId());
		}
	}

	public void verifEquiped() {
		if (this.getMorphMode()) {
			return;
		}
		final org.aestia.object.Object arme = this.getObjetByPos(1);
		final org.aestia.object.Object bouclier = this.getObjetByPos(15);
		if (arme != null) {
			if (arme.getTemplate().isTwoHanded() && bouclier != null) {
				this.unequipedObjet(arme);
				SocketManager.GAME_SEND_Im_PACKET(this, "119|44");
			} else if (!arme.getTemplate().getConditions().equalsIgnoreCase("")
					&& !ConditionParser.validConditions(this, arme.getTemplate().getConditions())) {
				this.unequipedObjet(arme);
				SocketManager.GAME_SEND_Im_PACKET(this, "119|44");
			}
		}
		if (bouclier != null && !bouclier.getTemplate().getConditions().equalsIgnoreCase("")
				&& !ConditionParser.validConditions(this, bouclier.getTemplate().getConditions())) {
			this.unequipedObjet(bouclier);
			SocketManager.GAME_SEND_Im_PACKET(this, "119|44");
		}
	}

	public boolean hasEquiped(final int id) {
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				if (entry.getValue().getTemplate().getId() == id && entry.getValue().getPosition() != -1) {
					// monitorexit(this._items)
					return true;
				}
			}
		}
		// monitorexit(this._items)
		return false;
	}

	public void setInvitation(final int target) {
		this._inviting = target;
	}

	public int getInvitation() {
		return this._inviting;
	}

	public String parseToPM() {
		final StringBuilder str = new StringBuilder();
		str.append(this.getId()).append(";");
		str.append(this.getName()).append(";");
		str.append(this._gfxID).append(";");
		int color1 = this.getColor1();
		int color2 = this.getColor2();
		int color3 = this.getColor3();
		if (this.getObjetByPos(22) != null && this.getObjetByPos(22).getTemplate().getId() == 10838) {
			color1 = 16342021;
			color2 = 16342021;
			color3 = 16342021;
		}
		str.append(color1).append(";");
		str.append(color2).append(";");
		str.append(color3).append(";");
		str.append(this.getGMStuffString()).append(";");
		str.append(this.curPdv).append(",").append(this.maxPdv).append(";");
		str.append(this.getLevel()).append(";");
		str.append(this.getInitiative()).append(";");
		str.append(this.getTotalStats().getEffect(176) + (int) Math.ceil(this.getTotalStats().getEffect(123) / 10))
				.append(";");
		str.append("0");
		return str.toString();
	}

	public int getNumbEquipedItemOfPanoplie(final int panID) {
		int nb = 0;
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> i : this._items.entrySet()) {
				if (i.getValue().getPosition() == -1) {
					continue;
				}
				if (i.getValue().getTemplate().getPanoId() != panID) {
					continue;
				}
				++nb;
			}
		}
		// monitorexit(this._items)
		return nb;
	}

	public void startActionOnCell(final GameAction GA) {
		int cellID = -1;
		int action = -1;
		try {
			cellID = Integer.parseInt(GA.args.split(";")[0]);
			action = Integer.parseInt(GA.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cellID == -1 || action == -1) {
			return;
		}
		if (!this.curMap.getCase(cellID).canDoAction(action)) {
			return;
		}
		this.curMap.getCase(cellID).startAction(this, GA);
	}

	public void finishActionOnCell(final GameAction GA) {
		int cellID = -1;
		try {
			cellID = Integer.parseInt(GA.args.split(";")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cellID == -1) {
			return;
		}
		this.curMap.getCase(cellID).finishAction(this, GA);
	}

	public void teleportD(final short newMapID, final int newCellID) {
		this.curMap = World.getMap(newMapID);
		this.curCell = World.getMap(newMapID).getCase(newCellID);
		Database.getStatique().getPlayerData().update(this, false);
	}

	public void teleportLaby(final short newMapID, final int newCellID) {
		final GameClient client = this.getGameClient();
		if (client == null) {
			return;
		}
		if (World.getMap(newMapID) == null) {
			return;
		}
		if (World.getMap(newMapID).getCase(newCellID) == null) {
			return;
		}
		SocketManager.GAME_SEND_GA2_PACKET(client, this.getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.curMap, this.getId());
		if (this.getMount() != null && this.getMount().getFatigue() >= 220) {
			this.getMount().setEnergie(this.getMount().getEnergie() - 1);
		}
		if (this.curCell.getCharacters().containsKey(this.getId())) {
			this.curCell.removeCharacter(this.getId());
		}
		this.curMap = World.getMap(newMapID);
		this.curCell = this.curMap.getCase(newCellID);
		SocketManager.GAME_SEND_MAPDATA(client, newMapID, this.curMap.getDate(), this.curMap.getKey());
		this.curMap.addPlayer(this);
		if (!this._Follower.isEmpty()) {
			synchronized (this._Follower) {
				for (final Player t : this._Follower.values()) {
					if (t.isOnline()) {
						SocketManager.GAME_SEND_FLAG_PACKET(t, this);
					} else {
						this._Follower.remove(t.getId());
					}
				}
			}
			// monitorexit(this._Follower)
		}
	}

	public void teleport(final short newMapID, final int newCellID) {
		final GameClient client = this.getGameClient();
		if (client == null) {
			return;
		}
		final org.aestia.map.Map map = World.getMap(newMapID);
		if (map == null) {
			GameServer.addToLog("Game: INVALID MAP : " + newMapID);
			return;
		}
		if (map.getCase(newCellID) == null) {
			GameServer.addToLog("Game: INVALID CELL : " + newCellID + " ON MAP : " + newMapID);
			return;
		}
		if (newMapID == this.curMap.getId()) {
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.curMap, this.getId());
			this.curCell.removeCharacter(this.getId());
			this.curCell = this.curMap.getCase(newCellID);
			this.curMap.addPlayer(this);
			SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.curMap, this);
			return;
		}
		boolean fullmorph = false;
		if (Constant.isInMorphDonjon(this.curMap.getId()) && !Constant.isInMorphDonjon(newMapID)) {
			fullmorph = true;
		}
		SocketManager.GAME_SEND_GA2_PACKET(client, this.getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.curMap, this.getId());
		if (this.getMount() != null && this.getMount().getFatigue() >= 220) {
			this.getMount().setEnergie(this.getMount().getEnergie() - 1);
		}
		if (this.curCell.getCharacters().containsKey(this.getId())) {
			this.curCell.removeCharacter(this.getId());
		}
		this.curMap = map;
		this.curCell = this.curMap.getCase(newCellID);
		if (this.curMap.getMountPark() != null && this.curMap.getMountPark().getOwner() > 0
				&& this.curMap.getMountPark().getGuild().getId() != -1
				&& World.getGuild(this.curMap.getMountPark().getGuild().getId()) == null) {
			GameServer.addToLog("[MountPark] Suppression d'un MountPark a Guild invalide. GuildID : "
					+ this.curMap.getMountPark().getGuild().getId());
		}
		final Collector col = Collector.getCollectorByMapId(this.curMap.getId());
		if (col != null && World.getGuild(col.getGuildId()) == null) {
			GameServer
					.addToLog("[Collector] Suppression d'un Collector a Guild invalide. GuildID : " + col.getGuildId());
			Collector.removeCollector(col.getGuildId());
		}
		if (this.isInAreaNotSubscribe()) {
			if (!this.isInPrivateArea) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.getGameClient(), 'S');
			}
			this.isInPrivateArea = true;
		} else {
			this.isInPrivateArea = false;
		}
		SocketManager.GAME_SEND_MAPDATA(client, newMapID, this.curMap.getDate(), this.curMap.getKey());
		this.curMap.addPlayer(this);
		if (fullmorph) {
			this.unsetFullMorph();
		}
		if (this._Follower != null && !this._Follower.isEmpty()) {
			synchronized (this._Follower) {
				for (final Player t : this._Follower.values()) {
					if (t.isOnline()) {
						SocketManager.GAME_SEND_FLAG_PACKET(t, this);
					} else {
						this._Follower.remove(t.getId());
					}
				}
			}
			// monitorexit(this._Follower)
		}
		if (map.getSubArea() != null) {
			if (map.getSubArea().getId() == 200) {
				this.getWaiter().addNow(new Runnable() {
					@Override
					public void run() {
						Dc.sendPacketMap(Player.this);
					}
				}, 1000L);
			} else if (map.getSubArea().getId() == 210 || map.getSubArea().getId() == 319) {
				this.getWaiter().addNow(new Runnable() {
					@Override
					public void run() {
						Toror.sendPacketMap(Player.this);
					}
				}, 1000L);
			}
		}
	}

	public void teleport(final org.aestia.map.Map map, final int cell) {
		GameClient PW = null;
		if (this._compte.getGameClient() != null) {
			PW = this._compte.getGameClient();
		}
		if (map == null) {
			GameServer.addToLog("Game: INVALID MAP in teleport fonction !");
			return;
		}
		if (map.getCase(cell) == null) {
			GameServer.addToLog("Game: INVALID CELL : " + cell + " ON MAP : " + map.getId());
			return;
		}
		if (!this.isInPrison() && !this.cantTP() && this.getCurMap().getSubArea() != null && map.getSubArea() != null
				&& this.getCurMap().getSubArea().getId() == 165 && map.getSubArea().getId() == 165) {
			if (!this.hasItemTemplate(997, 1)) {
				SocketManager.GAME_SEND_Im_PACKET(this, "14");
				return;
			}
			this.removeByTemplateID(997, 1);
		}
		boolean fullmorph = false;
		if (Constant.isInMorphDonjon(this.curMap.getId()) && !Constant.isInMorphDonjon(map.getId())) {
			fullmorph = true;
		}
		if (map.getId() == this.curMap.getId()) {
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.curMap, this.getId());
			this.curCell.removeCharacter(this.getId());
			this.curCell = this.curMap.getCase(cell);
			this.curMap.addPlayer(this);
			SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.curMap, this);
			if (fullmorph) {
				this.unsetFullMorph();
			}
			return;
		}
		if (PW != null) {
			SocketManager.GAME_SEND_GA2_PACKET(PW, this.getId());
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.curMap, this.getId());
		}
		if (this.getMount() != null && this.getMount().getFatigue() >= 220) {
			this.getMount().setEnergie(this.getMount().getEnergie() - 1);
		}
		this.curCell.removeCharacter(this.getId());
		this.curMap = map;
		this.curCell = this.curMap.getCase(cell);
		if (this.curMap.getMountPark() != null && this.curMap.getMountPark().getOwner() > 0
				&& this.curMap.getMountPark().getGuild().getId() != -1
				&& World.getGuild(this.curMap.getMountPark().getGuild().getId()) == null) {
			GameServer.addToLog("[MountPark] Suppression d'un MountPark a Guild invalide. GuildID : "
					+ this.curMap.getMountPark().getGuild().getId());
		}
		if (Collector.getCollectorByMapId(this.curMap.getId()) != null
				&& World.getGuild(Collector.getCollectorByMapId(this.curMap.getId()).getGuildId()) == null) {
			GameServer.addToLog("[Collector] Suppression d'un Collector a Guild invalide. GuildID : "
					+ Collector.getCollectorByMapId(this.curMap.getId()).getGuildId());
			Collector.removeCollector(Collector.getCollectorByMapId(this.curMap.getId()).getGuildId());
		}
		if (PW != null) {
			SocketManager.GAME_SEND_MAPDATA(PW, map.getId(), this.curMap.getDate(), this.curMap.getKey());
			this.curMap.addPlayer(this);
			if (fullmorph) {
				this.unsetFullMorph();
			}
		}
		if (!this._Follower.isEmpty()) {
			for (final Player t : this._Follower.values()) {
				if (t.isOnline()) {
					SocketManager.GAME_SEND_FLAG_PACKET(t, this);
				} else {
					this._Follower.remove(t.getId());
				}
			}
		}
	}

	public void disconnectInFight() {
		if (this.getCurExchange() != null) {
			this.getCurExchange().cancel();
		}
		if (this.getGroup() != null) {
			this.getGroup().leave(this);
		}
		this.resetVars();
		Database.getStatique().getPlayerData().update(this, true);
		this.set_isClone(true);
		World.unloadPerso(this.getId());
	}

	public int getBankCost() {
		return this._compte.getBank().size();
	}

	public String getStringVar(final String str) {
		switch (str) {
		case "bankCost": {
			return new StringBuilder(String.valueOf(this.getBankCost())).toString();
		}
		case "nbrOnline": {
			return new StringBuilder(String.valueOf(Main.gameServer.getPlayerNumber())).toString();
		}
		case "points": {
			return new StringBuilder(String.valueOf(this.getAccount().getPoints())).toString();
		}
		case "name": {
			return this.getName();
		}
		case "align": {
			return World.getStatOfAlign();
		}
		default:
			break;
		}
		return "";
	}

	public void refreshMapAfterFight() {
		SocketManager.send(this, "ILS2000");
		this.regenRate = 2000;
		this.curMap.addPlayer(this);
		if (this._compte.getGameClient() != null && this._compte.getGameClient() != null) {
			SocketManager.GAME_SEND_STATS_PACKET(this);
		}
		this._fight = null;
		this._away = false;
	}

	public void setBankKamas(final long i) {
		this._compte.setBankKamas(i);
	}

	public long getBankKamas() {
		return this._compte.getBankKamas();
	}

	public void setInBank(final boolean b) {
		this._isInBank = b;
	}

	public boolean isInBank() {
		return this._isInBank;
	}

	public String parseBankPacket() {
		final StringBuilder packet = new StringBuilder();
		for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._compte.getBank().entrySet()) {
			packet.append("O").append(entry.getValue().parseItem()).append(";");
		}
		if (this.getBankKamas() != 0L) {
			packet.append("G").append(this.getBankKamas());
		}
		return packet.toString();
	}

	public void addCapital(final int pts) {
		this._capital += pts;
	}

	public void setCapital(final int capital) {
		this._capital = capital;
	}

	public void addSpellPoint(final int pts) {
		if (this._morphMode) {
			this._saveSpellPts += pts;
		} else {
			this._spellPts += pts;
		}
	}

	public void addInBank(final int guid, final int qua) {
		if (qua <= 0) {
			return;
		}
		final org.aestia.object.Object PersoObj = World.getObjet(guid);
		synchronized (this._items) {
			if (this._items.get(guid) == null) {
				// monitorexit(this._items)
				return;
			}
		}
		// monitorexit(this._items)
		if (PersoObj.getPosition() != -1) {
			return;
		}
		org.aestia.object.Object BankObj = this.getSimilarBankItem(PersoObj);
		final int newQua = PersoObj.getQuantity() - qua;
		if (BankObj == null) {
			if (newQua <= 0) {
				this.removeItem(PersoObj.getGuid());
				this._compte.getBank().put(PersoObj.getGuid(), PersoObj);
				final String str = "O+" + PersoObj.getGuid() + "|" + PersoObj.getQuantity() + "|"
						+ PersoObj.getTemplate().getId() + "|" + PersoObj.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(this, str);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			} else {
				PersoObj.setQuantity(newQua);
				BankObj = org.aestia.object.Object.getCloneObjet(PersoObj, qua);
				World.addObjet(BankObj, true);
				this._compte.getBank().put(BankObj.getGuid(), BankObj);
				final String str = "O+" + BankObj.getGuid() + "|" + BankObj.getQuantity() + "|"
						+ BankObj.getTemplate().getId() + "|" + BankObj.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(this, str);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
				Database.getStatique().getItemData().update(BankObj);
				Database.getStatique().getItemData().update(PersoObj);
			}
		} else if (newQua <= 0) {
			this.removeItem(PersoObj.getGuid());
			World.removeItem(PersoObj.getGuid());
			BankObj.setQuantity(BankObj.getQuantity() + PersoObj.getQuantity());
			final String str = "O+" + BankObj.getGuid() + "|" + BankObj.getQuantity() + "|"
					+ BankObj.getTemplate().getId() + "|" + BankObj.parseStatsString();
			SocketManager.GAME_SEND_EsK_PACKET(this, str);
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, guid);
			Database.getStatique().getItemData().update(BankObj);
		} else {
			PersoObj.setQuantity(newQua);
			BankObj.setQuantity(BankObj.getQuantity() + qua);
			final String str = "O+" + BankObj.getGuid() + "|" + BankObj.getQuantity() + "|"
					+ BankObj.getTemplate().getId() + "|" + BankObj.parseStatsString();
			SocketManager.GAME_SEND_EsK_PACKET(this, str);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
			Database.getStatique().getItemData().update(BankObj);
			Database.getStatique().getItemData().update(PersoObj);
		}
		SocketManager.GAME_SEND_Ow_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
		Database.getStatique().getAccountData().updateBank(this._compte);
	}

	private org.aestia.object.Object getSimilarBankItem(final org.aestia.object.Object obj) {
		for (final org.aestia.object.Object value : this._compte.getBank().values()) {
			if (value.getTemplate().getId() == obj.getTemplate().getId() && value.getStats().isSameStats(obj.getStats())
					&& obj.getTemplate().getType() != 77 && !Constant.isIncarnationWeapon(obj.getTemplate().getId())
					&& obj.getTemplate().getType() != 85 && obj.getTemplate().getType() != 93
					&& obj.getTemplate().getType() != 97 && obj.getTemplate().getType() != 113
					&& (obj.getTemplate().getType() != 24 || Constant.isFlacGelee(value.getTemplate().getId()))
					&& value.getPosition() == -1 && obj.getTemplate().getType() != 18) {
				return value;
			}
		}
		return null;
	}

	public void removeFromBank(final int guid, final int qua) {
		if (qua <= 0) {
			return;
		}
		final org.aestia.object.Object BankObj = World.getObjet(guid);
		if (this._compte.getBank().get(guid) == null) {
			return;
		}
		org.aestia.object.Object PersoObj = this.getSimilarItem(BankObj);
		final int newQua = BankObj.getQuantity() - qua;
		if (PersoObj == null) {
			if (newQua <= 0) {
				this._compte.getBank().remove(guid);
				synchronized (this._items) {
					this._items.put(guid, BankObj);
				}
				// monitorexit(this._items)
				SocketManager.GAME_SEND_OAKO_PACKET(this, BankObj);
				final String str = "O-" + guid;
				SocketManager.GAME_SEND_EsK_PACKET(this, str);
			} else {
				PersoObj = org.aestia.object.Object.getCloneObjet(BankObj, qua);
				World.addObjet(PersoObj, true);
				BankObj.setQuantity(newQua);
				synchronized (this._items) {
					this._items.put(PersoObj.getGuid(), PersoObj);
				}
				// monitorexit(this._items)
				SocketManager.GAME_SEND_OAKO_PACKET(this, PersoObj);
				final String str = "O+" + BankObj.getGuid() + "|" + BankObj.getQuantity() + "|"
						+ BankObj.getTemplate().getId() + "|" + BankObj.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(this, str);
				Database.getStatique().getItemData().update(BankObj);
				Database.getStatique().getItemData().update(PersoObj);
			}
		} else if (newQua <= 0) {
			this._compte.getBank().remove(BankObj.getGuid());
			World.removeItem(BankObj.getGuid());
			PersoObj.setQuantity(PersoObj.getQuantity() + BankObj.getQuantity());
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
			final String str = "O-" + guid;
			SocketManager.GAME_SEND_EsK_PACKET(this, str);
			Database.getStatique().getItemData().update(PersoObj);
		} else {
			BankObj.setQuantity(newQua);
			PersoObj.setQuantity(PersoObj.getQuantity() + qua);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
			final String str = "O+" + BankObj.getGuid() + "|" + BankObj.getQuantity() + "|"
					+ BankObj.getTemplate().getId() + "|" + BankObj.parseStatsString();
			SocketManager.GAME_SEND_EsK_PACKET(this, str);
			Database.getStatique().getItemData().update(BankObj);
			Database.getStatique().getItemData().update(PersoObj);
		}
		SocketManager.GAME_SEND_Ow_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
		Database.getStatique().getAccountData().updateBank(this._compte);
	}

	public void openMountPark() {
		if (this.getDeshonor() >= 5) {
			SocketManager.GAME_SEND_Im_PACKET(this, "183");
			return;
		}
		if (this.getGuildMember() != null && this.curMap.getMountPark().getGuild() != null
				&& this.curMap.getMountPark().getGuild().getId() == this.getGuildMember().getGuild().getId()
				&& !this.getGuildMember().canDo(Constant.G_USEENCLOS)) {
			SocketManager.GAME_SEND_Im_PACKET(this, "1101");
			return;
		}
		this.inMountPark = this.curMap.getMountPark();
		this._away = true;
		final String str = this.getListDrago();
		SocketManager.GAME_SEND_ECK_PACKET(this, 16, str);
	}

	private String getListDrago() {
		String packet = "";
		this.inMountPark = this.curMap.getMountPark();
		boolean first = false;
		if (this.inMountPark.getEtable().size() > 0) {
			for (final Dragodinde DD : this.inMountPark.getEtable()) {
				if (first) {
					packet = String.valueOf(packet) + ";";
				}
				if (DD.getPerso() == this.getId()) {
					packet = String.valueOf(packet) + DD.parseToDinde();
					first = true;
				}
			}
		}
		packet = String.valueOf(packet) + "~";
		if (this.inMountPark.getListOfRaising().size() > 0) {
			boolean first2 = false;
			for (final Integer id : this.inMountPark.getListOfRaising()) {
				final Dragodinde dd = World.getDragoByID(id);
				if (dd.getPerso() == this.getId()) {
					if (first2) {
						packet = String.valueOf(packet) + ";";
					}
					packet = String.valueOf(packet) + dd.parseToDinde();
					first2 = true;
				} else {
					if (this.getGuildMember() == null || !this.getGuildMember().canDo(Constant.G_OTHDINDE)
							|| this.inMountPark.getOwner() == -1 || this.inMountPark.getGuild() == null
							|| this.inMountPark.getGuild().getId() != this.get_guild().getId()) {
						continue;
					}
					if (first2) {
						packet = String.valueOf(packet) + ";";
					}
					packet = String.valueOf(packet) + dd.parseToDinde();
					first2 = true;
				}
			}
		}
		return packet;
	}

	public void leftMountPark() {
		if (this.inMountPark == null) {
			return;
		}
		this.inMountPark = null;
	}

	public MountPark getInMountPark() {
		return this.inMountPark;
	}

	public void fullPDV() {
		this.setPdv(this.getMaxPdv());
		SocketManager.GAME_SEND_STATS_PACKET(this);
	}

	public void warpToSavePos() {
		try {
			final String[] infos = this._savePos.split(",");
			this.teleport(Short.parseShort(infos[0]), Integer.parseInt(infos[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeByTemplateID(final int tID, final int count) {
		final ArrayList<org.aestia.object.Object> list = new ArrayList<org.aestia.object.Object>();
		synchronized (this._items) {
			list.addAll(this._items.values());
		}
		// monitorexit(this._items)
		final ArrayList<org.aestia.object.Object> remove = new ArrayList<org.aestia.object.Object>();
		int tempCount = count;
		for (final org.aestia.object.Object obj : list) {
			if (obj.getTemplate().getId() != tID) {
				continue;
			}
			if (obj.getQuantity() >= count) {
				final int newQua = obj.getQuantity() - count;
				if (newQua > 0) {
					obj.setQuantity(newQua);
					if (this._isOnline) {
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, obj);
					}
				} else {
					synchronized (this._items) {
						this._items.remove(obj.getGuid());
					}
					// monitorexit(this._items)
					World.removeItem(obj.getGuid());
					if (this._isOnline) {
						SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, obj.getGuid());
					}
				}
				return;
			}
			if (obj.getQuantity() >= tempCount) {
				final int newQua = obj.getQuantity() - tempCount;
				if (newQua > 0) {
					obj.setQuantity(newQua);
					if (this._isOnline) {
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, obj);
					}
				} else {
					remove.add(obj);
				}
				for (final org.aestia.object.Object o : remove) {
					synchronized (this._items) {
						this._items.remove(o.getGuid());
					}
					// monitorexit(this._items)
					World.removeItem(o.getGuid());
					if (this._isOnline) {
						SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, o.getGuid());
					}
				}
			} else {
				tempCount -= obj.getQuantity();
				remove.add(obj);
			}
		}
	}

	public ArrayList<Job> getJobs() {
		final ArrayList<Job> list = new ArrayList<Job>();
		for (final JobStat js : this._metiers.values()) {
			if (js.getTemplate() != null) {
				list.add(js.getTemplate());
			}
		}
		return list.isEmpty() ? null : list;
	}

	public Map<Integer, JobStat> getMetiers() {
		return this._metiers;
	}

	public void doJobAction(final int actionID, final InteractiveObject object, final GameAction GA, final Case cell) {
		final JobStat SM = this.getMetierBySkill(actionID);
		if (SM != null) {
			SM.startAction(actionID, this, object, GA, cell);
			return;
		}
		switch (actionID) {
		case 151: {
			new JobAction(151, 4, 0, true, 100, 0).startAction(this, object, GA, cell);
		}
		case 110: {
			new JobAction(110, 2, 0, true, 100, 0).startAction(this, object, GA, cell);
		}
		default: {
			SocketManager.GAME_SEND_MESSAGE(this, "Erreur stats job null.");
		}
		}
	}

	public void finishJobAction(final int actionID, final InteractiveObject object, final GameAction GA,
			final Case cell) {
		final JobStat SM = this.getMetierBySkill(actionID);
		if (SM == null) {
			return;
		}
		SM.endAction(actionID, this, object, GA, cell);
	}

	public String parseJobData() {
		final StringBuilder str = new StringBuilder();
		if (this._metiers.isEmpty()) {
			return "";
		}
		for (final JobStat SM : this._metiers.values()) {
			if (SM == null) {
				continue;
			}
			if (str.length() > 0) {
				str.append(";");
			}
			str.append(SM.getTemplate().getId()).append(",").append(SM.getXp());
		}
		return str.toString();
	}

	public int totalJobBasic() {
		int i = 0;
		for (final JobStat SM : this._metiers.values()) {
			if (SM.getTemplate().getId() == 2 || SM.getTemplate().getId() == 11 || SM.getTemplate().getId() == 13
					|| SM.getTemplate().getId() == 14 || SM.getTemplate().getId() == 15
					|| SM.getTemplate().getId() == 16 || SM.getTemplate().getId() == 17
					|| SM.getTemplate().getId() == 18 || SM.getTemplate().getId() == 19
					|| SM.getTemplate().getId() == 20 || SM.getTemplate().getId() == 24
					|| SM.getTemplate().getId() == 25 || SM.getTemplate().getId() == 26
					|| SM.getTemplate().getId() == 27 || SM.getTemplate().getId() == 28
					|| SM.getTemplate().getId() == 31 || SM.getTemplate().getId() == 36
					|| SM.getTemplate().getId() == 41 || SM.getTemplate().getId() == 56
					|| SM.getTemplate().getId() == 58 || SM.getTemplate().getId() == 60
					|| SM.getTemplate().getId() == 65) {
				++i;
			}
		}
		return i;
	}

	public int totalJobFM() {
		int i = 0;
		for (final JobStat SM : this._metiers.values()) {
			if (SM.getTemplate().getId() == 43 || SM.getTemplate().getId() == 44 || SM.getTemplate().getId() == 45
					|| SM.getTemplate().getId() == 46 || SM.getTemplate().getId() == 47
					|| SM.getTemplate().getId() == 48 || SM.getTemplate().getId() == 49
					|| SM.getTemplate().getId() == 50 || SM.getTemplate().getId() == 62
					|| SM.getTemplate().getId() == 63 || SM.getTemplate().getId() == 64) {
				++i;
			}
		}
		return i;
	}

	public boolean canAggro() {
		return this.canAggro;
	}

	public void setCanAggro(final boolean canAggro) {
		this.canAggro = canAggro;
	}

	public void setCurJobAction(final JobAction JA) {
		this._curJobAction = JA;
	}

	public JobAction getCurJobAction() {
		return this._curJobAction;
	}

	public boolean changeName() {
		return this._changeName;
	}

	public void changeName(final boolean l) {
		this._changeName = l;
	}

	public JobStat getMetierBySkill(final int skID) {
		for (final JobStat SM : this._metiers.values()) {
			if (SM.isValidMapAction(skID)) {
				return SM;
			}
		}
		return null;
	}

	public String parseToFriendList(final int guid) {
		final StringBuilder str = new StringBuilder();
		str.append(";");
		str.append("?;");
		str.append(this.getName()).append(";");
		if (this._compte.isFriendWith(guid)) {
			str.append(this.getLevel()).append(";");
			str.append(this._align).append(";");
		} else {
			str.append("?;");
			str.append("-1;");
		}
		str.append(this.getClasse()).append(";");
		str.append(this.getSexe()).append(";");
		str.append(this._gfxID);
		return str.toString();
	}

	public String parseToEnemyList(final int guid) {
		final StringBuilder str = new StringBuilder();
		str.append(";");
		str.append("?;");
		str.append(this.getName()).append(";");
		if (this._compte.isFriendWith(guid)) {
			str.append(this.getLevel()).append(";");
			str.append(this._align).append(";");
		} else {
			str.append("?;");
			str.append("-1;");
		}
		str.append(this.getClasse()).append(";");
		str.append(this.getSexe()).append(";");
		str.append(this._gfxID);
		return str.toString();
	}

	public JobStat getMetierByID(final int job) {
		for (final JobStat SM : this._metiers.values()) {
			if (SM.getTemplate().getId() == job) {
				return SM;
			}
		}
		return null;
	}

	public boolean isOnMount() {
		return this._onMount;
	}

	public void toogleOnMount() {
		if (this._mount == null) {
			return;
		}
		if (Main.useSubscribe) {
			SocketManager.GAME_SEND_Im_PACKET(this, "1115");
			return;
		}
		if (this.getInHouse() != null) {
			SocketManager.GAME_SEND_Im_PACKET(this, "1117");
			return;
		}
		if (!this._onMount && this._mount.isMontable() == 0) {
			SocketManager.GAME_SEND_Re_PACKET(this, "Er", null);
			return;
		}
		if (this._mount.getEnergie() < Formulas.calculEnergieLooseForToogleMount(this._mount.getFatigue())) {
			SocketManager.GAME_SEND_Im_PACKET(this, "1113");
			return;
		}
		if (!this._onMount) {
			final int EnergyoLose = this._mount.getEnergie()
					- Formulas.calculEnergieLooseForToogleMount(this._mount.getFatigue());
			this._mount.setEnergie(EnergyoLose);
		}
		this._onMount = !this._onMount;
		final org.aestia.object.Object obj = this.getObjetByPos(8);
		if (this._onMount && obj != null) {
			obj.setPosition(-1);
			SocketManager.GAME_SEND_OBJET_MOVE_PACKET(this, obj);
		}
		if (this._mount.getEnergie() <= 0) {
			this._mount.setEnergie(0);
			SocketManager.GAME_SEND_Im_PACKET(this, "1114");
			return;
		}
		if (this.get_fight() != null && this.get_fight().getState() == 2) {
			SocketManager.GAME_SEND_ALTER_FIGHTER_MOUNT(this.get_fight(), this.get_fight().getFighterByPerso(this),
					this.getId(), this.get_fight().getTeamId(this.getId()),
					this.get_fight().getOtherTeamId(this.getId()));
		} else {
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.curMap, this);
		}
		SocketManager.GAME_SEND_Re_PACKET(this, "+", this._mount);
		SocketManager.GAME_SEND_Rr_PACKET(this, this._onMount ? "+" : "-");
		SocketManager.GAME_SEND_STATS_PACKET(this);
	}

	public int getMountXpGive() {
		return this._mountXpGive;
	}

	public Dragodinde getMount() {
		return this._mount;
	}

	public void setMount(final Dragodinde DD) {
		this._mount = DD;
	}

	public void setMountGiveXp(final int parseInt) {
		this._mountXpGive = parseInt;
	}

	public void resetVars() {
		if (this._curJobAction != null && this._curJobAction.getJobCraft() != null) {
			this._curJobAction.getJobCraft().jobAction.broke = true;
		}
		this.doAction = false;
		this._curJobAction = null;
		this.setGameAction(null);
		this._isTradingWith = 0;
		this._isTalkingWith = 0;
		this._away = false;
		this._emoteActive = 0;
		this._fight = null;
		this._duelID = 0;
		this._ready = false;
		this.curExchange = null;
		this._group = null;
		this._isInBank = false;
		this._inviting = 0;
		this.sitted = false;
		this._isZaaping = false;
		this.inMountPark = null;
		this._onMount = false;
		this._isOnCollectorID = 0;
		this._isClone = false;
		this._isForgetingSpell = false;
		this._isAbsent = false;
		this._isInvisible = false;
		this._Follower.clear();
		this._Follows = null;
		this._curTrunk = null;
		this._curHouse = null;
		this.isGhost = false;
		this._livreArti = false;
		this.concasseur = null;
		this._inDD = false;
		this._spec = false;
		this.setNeededEndFight(-1, null);
	}

	public void setInDD(final boolean inDD) {
		this._inDD = inDD;
	}

	public boolean getInDD() {
		return this._inDD;
	}

	public void addChanel(final String chan) {
		if (this._canaux.indexOf(chan) >= 0) {
			return;
		}
		this._canaux = String.valueOf(this._canaux) + chan;
		SocketManager.GAME_SEND_cC_PACKET(this, '+', chan);
	}

	public void removeChanel(final String chan) {
		this._canaux = this._canaux.replace(chan, "");
		SocketManager.GAME_SEND_cC_PACKET(this, '-', chan);
	}

	public void modifAlignement(final int i) {
		this._honor = 0;
		this._deshonor = 0;
		this._align = (byte) i;
		this._aLvl = 1;
		SocketManager.GAME_SEND_ZC_PACKET(this, i);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		if (this.get_guild() != null) {
			Database.getGame().getGuild_memberData().update(this);
			this.getGuildMember().setAlign((byte) i);
		}
	}

	public void setDeshonor(final int deshonor) {
		this._deshonor = deshonor;
	}

	public int getDeshonor() {
		return this._deshonor;
	}

	public void setShowWings(final boolean showWings) {
		this._showWings = showWings;
	}

	public int get_honor() {
		return this._honor;
	}

	public void set_honor(final int honor) {
		this._honor = honor;
	}

	public void setALvl(final int a) {
		this._aLvl = a;
	}

	public int getALvl() {
		return this._aLvl;
	}

	public void toggleWings(final char c) {
		if (this._align == -1) {
			return;
		}
		final int hloose = this._honor * 5 / 100;
		switch (c) {
		case '*': {
			SocketManager.GAME_SEND_GIP_PACKET(this, hloose);
			return;
		}
		case '+': {
			this.setShowWings(true);
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.curMap, this);
			Database.getStatique().getPlayerData().update(this, false);
			break;
		}
		case '-': {
			this.setShowWings(false);
			this._honor -= hloose;
			SocketManager.GAME_SEND_ALTER_GM_PACKET(this.curMap, this);
			Database.getStatique().getPlayerData().update(this, false);
			break;
		}
		}
		SocketManager.GAME_SEND_STATS_PACKET(this);
	}

	public void addHonor(final int winH) {
		if (this._align == 0) {
			return;
		}
		final int curGrade = this.getGrade();
		this._honor += winH;
		SocketManager.GAME_SEND_Im_PACKET(this, "080;" + winH);
		if (this.getGrade() != curGrade) {
			SocketManager.GAME_SEND_Im_PACKET(this, "082;" + this.getGrade());
		}
	}

	public void remHonor(final int losePH) {
		if (this._align == 0) {
			return;
		}
		final int curGrade = this.getGrade();
		this._honor -= losePH;
		SocketManager.GAME_SEND_Im_PACKET(this, "081;" + losePH);
		if (this.getGrade() != curGrade) {
			SocketManager.GAME_SEND_Im_PACKET(this, "083;" + this.getGrade());
		}
	}

	public Guild.GuildMember getGuildMember() {
		return this._guildMember;
	}

	public int getAccID() {
		return this._accID;
	}

	public void setAccount(final Account c) {
		this._compte = c;
	}

	public String parseZaapList() {
		String map = new StringBuilder(String.valueOf(this.curMap.getId())).toString();
		try {
			map = this._savePos.split(",")[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		final StringBuilder str = new StringBuilder();
		str.append(map);
		final int SubAreaID = this.curMap.getSubArea().getArea().get_superArea().get_id();
		for (final short i : this._zaaps) {
			if (World.getMap(i) == null) {
				continue;
			}
			if (World.getMap(i).getSubArea().getArea().get_superArea().get_id() != SubAreaID) {
				continue;
			}
			int cost = Formulas.calculZaapCost(this.curMap, World.getMap(i));
			if (i == this.curMap.getId()) {
				cost = 0;
			}
			str.append("|").append(i).append(";").append(cost);
		}
		return str.toString();
	}

	public String parsePrismesList() {
		final String map = new StringBuilder(String.valueOf(this.curMap.getId())).toString();
		String str = new StringBuilder(String.valueOf(map)).toString();
		final int SubAreaID = this.curMap.getSubArea().getArea().get_superArea().get_id();
		for (final Prism Prisme : World.AllPrisme()) {
			if (Prisme.getAlignement() != this._align) {
				continue;
			}
			final short MapID = Prisme.getMap();
			if (World.getMap(MapID) == null) {
				continue;
			}
			if (World.getMap(MapID).getSubArea().getArea().get_superArea().get_id() != SubAreaID) {
				continue;
			}
			if (Prisme.getInFight() == 0 || Prisme.getInFight() == -2) {
				str = String.valueOf(str) + "|" + MapID + ";*";
			} else {
				int costo = Formulas.calculZaapCost(this.curMap, World.getMap(MapID));
				if (MapID == this.curMap.getId()) {
					costo = 0;
				}
				str = String.valueOf(str) + "|" + MapID + ";" + costo;
			}
		}
		return str;
	}

	public void openZaapMenu() {
		if (this._fight == null) {
			if (!this.verifOtomaiZaap()) {
				return;
			}
			if (this.getDeshonor() >= 3) {
				SocketManager.GAME_SEND_Im_PACKET(this, "183");
				return;
			}
			this._isZaaping = true;
			this.verifAndAddZaap(this.curMap.getId());
			SocketManager.GAME_SEND_WC_PACKET(this);
		}
	}

	public void verifAndAddZaap(final short mapId) {
		if (!this.verifOtomaiZaap()) {
			return;
		}
		if (!this._zaaps.contains(mapId)) {
			this._zaaps.add(mapId);
			SocketManager.GAME_SEND_Im_PACKET(this, "024");
			Database.getStatique().getPlayerData().update(this, false);
		}
	}

	public boolean verifOtomaiZaap() {
		return (this.getCurMap().getId() != 10643 && this.getCurMap().getId() != 11210)
				|| (ConditionParser.validConditions(this, "QT=231") && ConditionParser.validConditions(this, "QT=232"));
	}

	public void openPrismeMenu() {
		if (this._fight == null) {
			if (this.getDeshonor() >= 3) {
				SocketManager.GAME_SEND_Im_PACKET(this, "183");
				return;
			}
			this._isZaaping = true;
			SocketManager.SEND_Wp_MENU_Prisme(this);
		}
	}

	public void useZaap(final short id) {
		if (!this._isZaaping) {
			return;
		}
		if (this._fight != null) {
			return;
		}
		if (!this._zaaps.contains(id)) {
			return;
		}
		final int cost = Formulas.calculZaapCost(this.curMap, World.getMap(id));
		if (this._kamas < cost) {
			return;
		}
		final int SubAreaID = this.curMap.getSubArea().getArea().get_superArea().get_id();
		final int cellID = World.getZaapCellIdByMapId(id);
		if (World.getMap(id) == null) {
			GameServer.addToLog("La map " + id + " n'est pas implantee, Zaap refuse");
			SocketManager.GAME_SEND_WUE_PACKET(this);
			return;
		}
		if (World.getMap(id).getCase(cellID) == null) {
			GameServer.addToLog("La cellule associee au zaap " + id + " n'est pas implantee, Zaap refuse");
			SocketManager.GAME_SEND_WUE_PACKET(this);
			return;
		}
		if (!World.getMap(id).getCase(cellID).isWalkable(true)) {
			GameServer.addToLog("La cellule associee au zaap " + id + " n'est pas 'walkable', Zaap refuse");
			SocketManager.GAME_SEND_WUE_PACKET(this);
			return;
		}
		if (World.getMap(id).getSubArea().getArea().get_superArea().get_id() != SubAreaID) {
			SocketManager.GAME_SEND_WUE_PACKET(this);
			return;
		}
		if (id == 4263 && this.get_align() == 2) {
			return;
		}
		if (id == 5295 && this.get_align() == 1) {
			return;
		}
		this._kamas -= cost;
		this.teleport(id, cellID);
		SocketManager.GAME_SEND_STATS_PACKET(this);
		SocketManager.GAME_SEND_WV_PACKET(this);
		this._isZaaping = false;
	}

	public void usePrisme(final String packet) {
		int celdaID = 340;
		short MapID = 7411;
		for (final Prism Prisme : World.AllPrisme()) {
			if (Prisme.getMap() == Short.valueOf(packet.substring(2))) {
				celdaID = Prisme.getCell();
				MapID = Prisme.getMap();
				break;
			}
		}
		int costo = Formulas.calculZaapCost(this.curMap, World.getMap(MapID));
		if (MapID == this.curMap.getId()) {
			costo = 0;
		}
		if (this._kamas < costo) {
			SocketManager.GAME_SEND_MESSAGE(this,
					"Vous n'avez pas sufisamment de Kamas pour r\u00e9aliser cette action.");
			return;
		}
		this._kamas -= costo;
		SocketManager.GAME_SEND_STATS_PACKET(this);
		this.teleport(Short.valueOf(packet.substring(2)), celdaID);
		SocketManager.SEND_Ww_CLOSE_Prisme(this);
	}

	public String parseZaaps() {
		final StringBuilder str = new StringBuilder();
		boolean first = true;
		if (this._zaaps.isEmpty()) {
			return "";
		}
		for (final int i : this._zaaps) {
			if (!first) {
				str.append(",");
			}
			first = false;
			str.append(i);
		}
		return str.toString();
	}

	public String parsePrisme() {
		String str = "";
		final Prism Prisme = World.getPrisme(this.curMap.getSubArea().getPrismId());
		if (Prisme == null) {
			str = "-3";
		} else if (Prisme.getInFight() == 0) {
			str = "0;" + Prisme.getTurnTime() + ";45000;7";
		} else {
			str = new StringBuilder(String.valueOf(Prisme.getInFight())).toString();
		}
		return str;
	}

	public void stopZaaping() {
		if (!this._isZaaping) {
			return;
		}
		this._isZaaping = false;
		SocketManager.GAME_SEND_WV_PACKET(this);
	}

	public void Zaapi_close() {
		if (!this._isZaaping) {
			return;
		}
		this._isZaaping = false;
		SocketManager.GAME_SEND_CLOSE_ZAAPI_PACKET(this);
	}

	public void Prisme_close() {
		if (!this._isZaaping) {
			return;
		}
		this._isZaaping = false;
		SocketManager.SEND_Ww_CLOSE_Prisme(this);
	}

	public void Zaapi_use(final String packet) {
		final org.aestia.map.Map map = World.getMap(Short.valueOf(packet.substring(2)));
		short idcelula = 100;
		if (map != null) {
			for (final Map.Entry<Integer, Case> entry : map.getCases().entrySet()) {
				final InteractiveObject obj = entry.getValue().getObject();
				if (obj != null && (obj.getId() == 7031 || obj.getId() == 7030)) {
					idcelula = (short) (entry.getValue().getId() + 18);
				}
			}
		}
		if (map.getSubArea().getArea().get_id() == 7 || map.getSubArea().getArea().get_id() == 11) {
			int price = 20;
			if (this.get_align() == 1 || this.get_align() == 2) {
				price = 10;
			}
			this._kamas -= price;
			SocketManager.GAME_SEND_STATS_PACKET(this);
			if ((map.getSubArea().getArea().get_id() == 7 && this.getCurMap().getSubArea().getArea().get_id() == 7)
					|| (map.getSubArea().getArea().get_id() == 11
							&& this.getCurMap().getSubArea().getArea().get_id() == 11)) {
				this.teleport(Short.valueOf(packet.substring(2)), idcelula);
			}
			SocketManager.GAME_SEND_CLOSE_ZAAPI_PACKET(this);
		}
	}

	public boolean hasItemTemplate(final int i, final int q) {
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				if (obj.getPosition() != -1) {
					continue;
				}
				if (obj.getTemplate().getId() != i) {
					continue;
				}
				if (obj.getQuantity() >= q) {
					// monitorexit(this._items)
					return true;
				}
			}
		}
		// monitorexit(this._items)
		return false;
	}

	public boolean hasItemType(final int type) {
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				if (obj.getPosition() != -1) {
					continue;
				}
				if (obj.getTemplate().getType() == type) {
					// monitorexit(this._items)
					return true;
				}
			}
		}
		// monitorexit(this._items)
		return false;
	}

	public static String getCompiledEmote(final Map<Integer, Integer> i, final Map<Integer, Integer> y) {
		int i2 = 0;
		int y2 = 0;
		for (final Integer b : i.values()) {
			i2 += 2 << b - 2;
		}
		if (y != null) {
			for (final Integer b : y.values()) {
				y2 += 2 << b - 2;
			}
		}
		return String.valueOf(i2) + "|" + y2;
	}

	public org.aestia.object.Object getItemTemplate(final int i, final int q) {
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				if (obj.getPosition() != -1) {
					continue;
				}
				if (obj.getTemplate().getId() != i) {
					continue;
				}
				if (obj.getQuantity() >= q) {
					// monitorexit(this._items)
					return obj;
				}
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public org.aestia.object.Object getItemTemplate(final int i) {
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				if (obj.getTemplate().getId() != i) {
					continue;
				}
				return obj;
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public int getNbItemTemplate(final int i) {
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				if (obj.getTemplate().getId() != i) {
					continue;
				}
				return obj.getQuantity();
			}
		}
		// monitorexit(this._items)
		return -1;
	}

	public void SetZaaping(final boolean zaaping) {
		this._isZaaping = zaaping;
	}

	public void setisForgetingSpell(final boolean isForgetingSpell) {
		this._isForgetingSpell = isForgetingSpell;
	}

	public boolean isForgetingSpell() {
		return this._isForgetingSpell;
	}

	public boolean isDispo(final Player sender) {
		return !this._isAbsent && (!this._isInvisible || this._compte.isFriendWith(sender.getAccount().getGuid()));
	}

	public boolean get_isClone() {
		return this._isClone;
	}

	public void set_isClone(final boolean isClone) {
		this._isClone = isClone;
	}

	public int get_isOnCollectorID() {
		return this._isOnCollectorID;
	}

	public void set_isOnCollectorID(final int isOnCollectorID) {
		this._isOnCollectorID = isOnCollectorID;
	}

	public void set_title(final int i) {
		this._title = (byte) i;
	}

	public byte get_title() {
		return this._title;
	}

	public long getLastPacketTime() {
		return this.getGameClient().getSession().getLastIoTime();
	}

	public static Player ClonePerso(final Player P, final int id, final int pdv) {
		final TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
		stats.put(125, pdv);
		stats.put(118, P.getStats().getEffect(118));
		stats.put(124, P.getStats().getEffect(124));
		stats.put(126, P.getStats().getEffect(126));
		stats.put(123, P.getStats().getEffect(123));
		stats.put(119, P.getStats().getEffect(119));
		stats.put(111, P.getStats().getEffect(111));
		stats.put(128, P.getStats().getEffect(128));
		stats.put(214, P.getStats().getEffect(214));
		stats.put(210, P.getStats().getEffect(210));
		stats.put(213, P.getStats().getEffect(213));
		stats.put(211, P.getStats().getEffect(211));
		stats.put(212, P.getStats().getEffect(212));
		stats.put(160, P.getStats().getEffect(160));
		stats.put(161, P.getStats().getEffect(161));
		byte showWings = 0;
		int alvl = 0;
		if (P.get_align() != 0 && P._showWings) {
			showWings = 1;
			alvl = P.getGrade();
		}
		int mountID = -1;
		if (P.getMount() != null) {
			mountID = P.getMount().getId();
		}
		final Player Clone = new Player(id, P.getName(), (P.getGroupe() != null) ? P.getGroupe().getId() : -1,
				P.getSexe(), P.getClasse(), P.getColor1(), P.getColor2(), P.getColor3(), P.getLevel(), 100,
				P.get_gfxID(), stats, P.parseObjetsToDB(), 100, showWings, mountID, alvl, P.get_align());
		Clone.set_isClone(true);
		if (P._onMount) {
			Clone._onMount = true;
		}
		return Clone;
	}

	public void VerifAndChangeItemPlace() {
		boolean isFirstAM = true;
		boolean isFirstAN = true;
		boolean isFirstANb = true;
		boolean isFirstAR = true;
		boolean isFirstBO = true;
		boolean isFirstBOb = true;
		boolean isFirstCA = true;
		boolean isFirstCE = true;
		boolean isFirstCO = true;
		boolean isFirstDa = true;
		boolean isFirstDb = true;
		boolean isFirstDc = true;
		boolean isFirstDd = true;
		boolean isFirstDe = true;
		boolean isFirstDf = true;
		boolean isFirstFA = true;
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				if (obj.getPosition() == -1) {
					continue;
				}
				if (obj.getPosition() == 0) {
					if (isFirstAM) {
						isFirstAM = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 2) {
					if (isFirstAN) {
						isFirstAN = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 4) {
					if (isFirstANb) {
						isFirstANb = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 1) {
					if (isFirstAR) {
						isFirstAR = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 5) {
					if (isFirstBO) {
						isFirstBO = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 15) {
					if (isFirstBOb) {
						isFirstBOb = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 7) {
					if (isFirstCA) {
						isFirstCA = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 3) {
					if (isFirstCE) {
						isFirstCE = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 6) {
					if (isFirstCO) {
						isFirstCO = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 9) {
					if (isFirstDa) {
						isFirstDa = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 10) {
					if (isFirstDb) {
						isFirstDb = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 11) {
					if (isFirstDc) {
						isFirstDc = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 12) {
					if (isFirstDd) {
						isFirstDd = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 13) {
					if (isFirstDe) {
						isFirstDe = false;
					} else {
						obj.setPosition(-1);
					}
				} else if (obj.getPosition() == 14) {
					if (isFirstDf) {
						isFirstDf = false;
					} else {
						obj.setPosition(-1);
					}
				} else {
					if (obj.getPosition() != 8) {
						continue;
					}
					if (isFirstFA) {
						isFirstFA = false;
					} else {
						obj.setPosition(-1);
					}
				}
			}
		}
		// monitorexit(this._items)
	}

	public Stalk get_traque() {
		return this._traqued;
	}

	public void set_traque(final Stalk traq) {
		this._traqued = traq;
	}

	public void MarryTo(final Player wife) {
		this._wife = wife.getId();
		Database.getStatique().getPlayerData().update(this, true);
	}

	public String get_wife_friendlist() {
		final Player wife = World.getPersonnage(this._wife);
		final StringBuilder str = new StringBuilder();
		if (wife != null) {
			int color1 = wife.getColor1();
			int color2 = wife.getColor2();
			int color3 = wife.getColor3();
			if (wife.getObjetByPos(22) != null && wife.getObjetByPos(22).getTemplate().getId() == 10838) {
				color1 = 16342021;
				color2 = 16342021;
				color3 = 16342021;
			}
			str.append(wife.getName()).append("|").append(wife.getClasse() + wife.getSexe()).append("|").append(color1)
					.append("|").append(color2).append("|").append(color3).append("|");
			if (!wife.isOnline()) {
				str.append("|");
			} else {
				str.append(wife.parse_towife()).append("|");
			}
		} else {
			str.append("|");
		}
		return str.toString();
	}

	public String parse_towife() {
		int f = 0;
		if (this._fight != null) {
			f = 1;
		}
		return String.valueOf(this.curMap.getId()) + "|" + this.getLevel() + "|" + f;
	}

	public void meetWife(final Player p) {
		if (p == null) {
			return;
		}
		if (this.getPodUsed() >= this.getMaxPod()) {
			SocketManager.GAME_SEND_Im_PACKET(this, "170");
			return;
		}
		final int dist = (this.curMap.getX() - p.getCurMap().getX()) * (this.curMap.getX() - p.getCurMap().getX())
				+ (this.curMap.getY() - p.getCurMap().getY()) * (this.curMap.getY() - p.getCurMap().getY());
		if (dist > 100 || p.getCurMap().getId() == this.getCurMap().getId()) {
			if (p.getSexe() == 0) {
				SocketManager.GAME_SEND_Im_PACKET(this, "178");
			} else {
				SocketManager.GAME_SEND_Im_PACKET(this, "179");
			}
			return;
		}
		final int cellPositiontoadd = Constant.getNearCellidUnused(p);
		if (cellPositiontoadd == -1) {
			if (p.getSexe() == 0) {
				SocketManager.GAME_SEND_Im_PACKET(this, "141");
			} else {
				SocketManager.GAME_SEND_Im_PACKET(this, "142");
			}
			return;
		}
		this.teleport(p.getCurMap().getId(), cellPositiontoadd);
	}

	public void Divorce() {
		if (this.isOnline()) {
			SocketManager.GAME_SEND_Im_PACKET(this, "047;" + World.getPersonnage(this._wife).getName());
		}
		this._wife = 0;
		Database.getStatique().getPlayerData().update(this, true);
	}

	public int getWife() {
		return this._wife;
	}

	public int setisOK(final int ok) {
		return this._isOK = ok;
	}

	public int getisOK() {
		return this._isOK;
	}

	public void changeOrientation(final int toOrientation) {
		if (this.get_orientation() == 0 || this.get_orientation() == 2 || this.get_orientation() == 4
				|| this.get_orientation() == 6) {
			this.set_orientation(toOrientation);
			SocketManager.GAME_SEND_eD_PACKET_TO_MAP(this.getCurMap(), this.getId(), toOrientation);
		}
	}

	public void set_Ghosts() {
		if (this.isOnMount()) {
			this.toogleOnMount();
		}
		this.isDead = 0;
		this.isGhost = true;
		this.setEnergy(0);
		this.set_gfxID(8004);
		this.setCanAggro(false);
		this.set_away(true);
		this.setSpeed(-40);
		this.regenRate = 0;
		SocketManager.send(this,
				"IH-11;-54|2;-12|-41;-17|5;-9|25;-4|36;5|12;12|10;19|-10;13|-14;31|-43;0|-60;-3|-58;18|24;-43|27;-33");
		final int idArea = this.getCurMap().getSubArea().getArea().get_id();
		Constant.tpCim(idArea, this);
	}

	public void set_Alive() {
		if (!this.isGhost) {
			return;
		}
		this.isGhost = false;
		this.isDead = 0;
		this.setEnergy(1000);
		this.setPdv(1);
		this.set_gfxID(Integer.parseInt(String.valueOf(this.getClasse()) + this.getSexe()));
		this.setCanAggro(true);
		this.set_away(false);
		this.setSpeed(0);
		SocketManager.GAME_SEND_MESSAGE(this, "Tu as gagn\u00e9 <b>1000</b> points d'\u00e9nergie.", "009900");
		SocketManager.GAME_SEND_STATS_PACKET(this);
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.curMap, this);
		SocketManager.send(this, "IH");
	}

	public boolean isGhost() {
		return this.isGhost;
	}

	public void setDead(final int isDead) {
		this.isDead = isDead;
	}

	public int isDead() {
		return this.isDead;
	}

	public void setFuneral() {
		this.isDead = 1;
		if (this.isOnMount()) {
			this.toogleOnMount();
		}
		if (this.get_orientation() == 2) {
			this.set_orientation(1);
			SocketManager.GAME_SEND_eD_PACKET_TO_MAP(this.getCurMap(), this.getId(), 1);
		}
		this.set_gfxID(Integer.parseInt(String.valueOf(this.getClasse()) + "3"));
		SocketManager.send(this, "AR3K");
		SocketManager.send(this, "M112");
		SocketManager.GAME_SEND_ALTER_GM_PACKET(this.getCurMap(), this);
	}

	public void setInTrunk(final Trunk t) {
		this._curTrunk = t;
	}

	public Trunk getInTrunk() {
		return this._curTrunk;
	}

	public void setInHouse(final House h) {
		this._curHouse = h;
	}

	public House getInHouse() {
		return this._curHouse;
	}

	public Map<Integer, Integer> getStoreItems() {
		return this._storeItems;
	}

	public int needEndFight() {
		return this.hasEndFight;
	}

	public Monster.MobGroup hasMobGroup() {
		return this.hasMobGroup;
	}

	public void setNeededEndFight(final int hasEndFight, final Monster.MobGroup group) {
		this.hasEndFight = hasEndFight;
		this.hasMobGroup = group;
	}

	public String parseStoreItemsList() {
		final StringBuilder list = new StringBuilder();
		synchronized (this._storeItems) {
			if (this._storeItems.isEmpty()) {
				// monitorexit(this._storeItems)
				return "";
			}
			for (final Map.Entry<Integer, Integer> obj : this._storeItems.entrySet()) {
				final org.aestia.object.Object O = World.getObjet(obj.getKey());
				if (O == null) {
					continue;
				}
				list.append(O.getGuid()).append(";").append(O.getQuantity()).append(";").append(O.getTemplate().getId())
						.append(";").append(O.parseStatsString()).append(";").append(obj.getValue()).append("|");
			}
		}
		// monitorexit(this._storeItems)
		return (list.length() > 0) ? list.toString().substring(0, list.length() - 1) : list.toString();
	}

	public int parseStoreItemsListPods() {
		synchronized (this._storeItems) {
			if (this._storeItems.isEmpty()) {
				// monitorexit(this._storeItems)
				return 0;
			}
			int total = 0;
			for (final Map.Entry<Integer, Integer> obj : this._storeItems.entrySet()) {
				final org.aestia.object.Object O = World.getObjet(obj.getKey());
				if (O != null) {
					final int qua = O.getQuantity();
					final int poidBase1 = O.getTemplate().getPod() * qua;
					total += poidBase1;
				}
			}
			// monitorexit(this._storeItems)
			return total;
		}
	}

	public String parseStoreItemstoBD() {
		final StringBuilder str = new StringBuilder();
		synchronized (this._storeItems) {
			for (final Map.Entry<Integer, Integer> _storeObjets : this._storeItems.entrySet()) {
				str.append(_storeObjets.getKey()).append(",").append(_storeObjets.getValue()).append("|");
			}
		}
		// monitorexit(this._storeItems)
		return str.toString();
	}

	public void addinStore(final int ObjID, final int price, final int qua) {
		final org.aestia.object.Object PersoObj = World.getObjet(ObjID);
		synchronized (this._storeItems) {
			if (this._storeItems.get(ObjID) != null) {
				this._storeItems.remove(ObjID);
				this._storeItems.put(ObjID, price);
				SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
				// monitorexit(this._storeItems)
				return;
			}
		}
		// monitorexit(this._storeItems)
		synchronized (this._items) {
			if (this._items.get(ObjID) == null) {
				GameServer.addToLog(
						"Le joueur " + this.getName() + " a tenter d'ajouter un objet au store qu'il n'avait pas.");
				// monitorexit(this._items)
				return;
			}
		}
		// monitorexit(this._items)
		if (PersoObj.getPosition() != -1) {
			return;
		}
		org.aestia.object.Object SimilarObj = this.getSimilarStoreItem(PersoObj);
		final int newQua = PersoObj.getQuantity() - qua;
		if (SimilarObj == null) {
			if (newQua <= 0) {
				this.removeItem(PersoObj.getGuid());
				synchronized (this._storeItems) {
					this._storeItems.put(PersoObj.getGuid(), price);
				}
				// monitorexit(this._storeItems)
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, PersoObj.getGuid());
				SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
			} else {
				PersoObj.setQuantity(newQua);
				SimilarObj = org.aestia.object.Object.getCloneObjet(PersoObj, qua);
				World.addObjet(SimilarObj, true);
				synchronized (this._storeItems) {
					this._storeItems.put(SimilarObj.getGuid(), price);
				}
				// monitorexit(this._storeItems)
				SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
			}
		} else if (newQua <= 0) {
			this.removeItem(PersoObj.getGuid());
			World.removeItem(PersoObj.getGuid());
			SimilarObj.setQuantity(SimilarObj.getQuantity() + PersoObj.getQuantity());
			synchronized (this._storeItems) {
				this._storeItems.remove(SimilarObj.getGuid());
				this._storeItems.put(SimilarObj.getGuid(), price);
			}
			// monitorexit(this._storeItems)
			SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this, PersoObj.getGuid());
		} else {
			PersoObj.setQuantity(newQua);
			SimilarObj.setQuantity(SimilarObj.getQuantity() + qua);
			synchronized (this._storeItems) {
				this._storeItems.remove(SimilarObj.getGuid());
				this._storeItems.put(SimilarObj.getGuid(), price);
			}
			// monitorexit(this._storeItems)
			SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
		}
		SocketManager.GAME_SEND_Ow_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	private org.aestia.object.Object getSimilarStoreItem(final org.aestia.object.Object obj) {
		synchronized (this._storeItems) {
			for (final Map.Entry<Integer, Integer> value : this._storeItems.entrySet()) {
				final org.aestia.object.Object obj2 = World.getObjet(value.getKey());
				if (obj2.getTemplate().getId() == obj.getTemplate().getId()
						&& obj2.getStats().isSameStats(obj.getStats()) && obj.getTemplate().getType() != 77
						&& !Constant.isIncarnationWeapon(obj.getTemplate().getId()) && obj.getTemplate().getType() != 85
						&& obj.getTemplate().getType() != 93 && obj.getTemplate().getType() != 97
						&& obj.getTemplate().getType() != 113
						&& (obj.getTemplate().getType() != 24 || Constant.isFlacGelee(obj2.getTemplate().getId()))
						&& obj2.getPosition() == -1 && obj.getTemplate().getType() != 18) {
					// monitorexit(this._storeItems)
					return obj2;
				}
			}
		}
		// monitorexit(this._storeItems)
		return null;
	}

	public void removeFromStore(final int guid, final int qua) {
		final org.aestia.object.Object SimilarObj = World.getObjet(guid);
		synchronized (this._storeItems) {
			if (this._storeItems.get(guid) == null) {
				GameServer.addToLog(
						"Le joueur " + this.getName() + " a tenter de retirer un objet du store qu'il n'avait pas.");
				// monitorexit(this._storeItems)
				return;
			}
		}
		// monitorexit(this._storeItems)
		final org.aestia.object.Object PersoObj = this.getSimilarItem(SimilarObj);
		final int newQua = SimilarObj.getQuantity() - qua;
		if (PersoObj == null) {
			if (newQua <= 0) {
				synchronized (this._storeItems) {
					this._storeItems.remove(guid);
				}
				// monitorexit(this._storeItems)
				synchronized (this._items) {
					this._items.put(guid, SimilarObj);
				}
				// monitorexit(this._items)
				SocketManager.GAME_SEND_OAKO_PACKET(this, SimilarObj);
				SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
			}
		} else if (newQua <= 0) {
			synchronized (this._storeItems) {
				this._storeItems.remove(SimilarObj.getGuid());
			}
			// monitorexit(this._storeItems)
			World.removeItem(SimilarObj.getGuid());
			PersoObj.setQuantity(PersoObj.getQuantity() + SimilarObj.getQuantity());
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, PersoObj);
			SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this, this);
		}
		SocketManager.GAME_SEND_Ow_PACKET(this);
		Database.getStatique().getPlayerData().update(this, true);
	}

	public void removeStoreItem(final int guid) {
		synchronized (this._storeItems) {
			this._storeItems.remove(guid);
		}
		// monitorexit(this._storeItems)
	}

	public void addStoreItem(final int guid, final int price) {
		synchronized (this._storeItems) {
			this._storeItems.put(guid, price);
		}
		// monitorexit(this._storeItems)
	}

	public void setSpeed(final int _Speed) {
		this._Speed = _Speed;
	}

	public int getSpeed() {
		return this._Speed;
	}

	public int get_savestat() {
		return this.savestat;
	}

	public void set_savestat(final int stat) {
		this.savestat = stat;
	}

	public void setMetierPublic(final boolean b) {
		this._metierPublic = b;
	}

	public boolean getMetierPublic() {
		return this._metierPublic;
	}

	public void setLivreArtisant(final boolean b) {
		this._livreArti = b;
	}

	public boolean getLivreArtisant() {
		return this._livreArti;
	}

	public void setConcasseur(final Concasseur concasseur) {
		this.concasseur = concasseur;
	}

	public Concasseur getConcasseur() {
		return this.concasseur;
	}

	public boolean hasSpell(final int spellID) {
		return this.getSortStatBySortIfHas(spellID) != null;
	}

	public void leaveEnnemyFaction() {
		if (!this.isInEnnemyFaction) {
			return;
		}
		final int pGrade = this.getGrade();
		final long compar = System.currentTimeMillis() - (this.enteredOnEnnemyFaction + 60000 * pGrade);
		switch (pGrade) {
		case 1: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 1 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 2: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 2 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 3: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 3 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 4: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 4 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 5: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 5 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 6: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 6 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 7: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 7 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 8: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 8 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 9: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 9 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		case 10: {
			if (compar >= 0L) {
				this.leaveFaction();
				SocketManager.PACKET_POPUP_DEPART(this,
						"Vous venez d'\u00eatre lib\u00e9r\u00e9 de prison apr\u00e8s 10 minutes d'attente.");
				break;
			}
			long restant = -compar;
			if (restant <= 1000L) {
				restant = 1000L;
			}
			SocketManager.PACKET_POPUP_DEPART(this,
					"Vous devez attendre encore " + restant / 1000L + " secondes en prison.");
			break;
		}
		}
		Database.getStatique().getPlayerData().update(this, false);
	}

	public void leaveEnnemyFactionAndPay(final Player perso) {
		if (!this.isInEnnemyFaction) {
			return;
		}
		final int pGrade = perso.getGrade();
		final long curKamas = perso.get_kamas();
		switch (pGrade) {
		case 1: {
			if (curKamas < 1000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 1000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 2: {
			if (curKamas < 2000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 2000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 3: {
			if (curKamas < 3000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 3000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 4: {
			if (curKamas < 4000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 4000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 5: {
			if (curKamas < 5000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 5000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 6: {
			if (curKamas < 7000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 7000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 7: {
			if (curKamas < 9000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 9000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 8: {
			if (curKamas < 12000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 12000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 9: {
			if (curKamas < 16000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 16000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		case 10: {
			if (curKamas < 25000L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Tu ne poss\u00e8des que " + curKamas + "Kamas. Tu n'as pas assez d'argent pour sortir !",
						"009900");
				break;
			}
			final int countKamas = 25000;
			long newKamas = curKamas - countKamas;
			if (newKamas < 0L) {
				newKamas = 0L;
			}
			perso.set_kamas(newKamas);
			this.leaveFaction();
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens de payer " + countKamas
					+ "Kamas pour sortir. Il te reste maintenant " + newKamas + "Kamas.", "009900");
			break;
		}
		}
		Database.getStatique().getPlayerData().update(this, false);
		SocketManager.GAME_SEND_STATS_PACKET(perso);
	}

	public void leaveFaction() {
		try {
			this.isInEnnemyFaction = false;
			this.enteredOnEnnemyFaction = 0L;
			this.warpToSavePos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void teleportWithoutBlocked(final short newMapID, final int newCellID) {
		GameClient PW = null;
		if (this._compte.getGameClient() != null) {
			PW = this._compte.getGameClient();
		}
		if (World.getMap(newMapID) == null) {
			GameServer.addToLog("Game: INVALID MAP : " + newMapID);
			return;
		}
		if (World.getMap(newMapID).getCase(newCellID) == null) {
			GameServer.addToLog("Game: INVALID CELL : " + newCellID + " ON MAP : " + newMapID);
			return;
		}
		if (PW != null) {
			SocketManager.GAME_SEND_GA2_PACKET(PW, this.getId());
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.curMap, this.getId());
		}
		this.curCell.removeCharacter(this.getId());
		this.curMap = World.getMap(newMapID);
		this.curCell = this.curMap.getCase(newCellID);
		if (this.curMap.getMountPark() != null && this.curMap.getMountPark().getOwner() > 0
				&& this.curMap.getMountPark().getGuild().getId() != -1
				&& World.getGuild(this.curMap.getMountPark().getGuild().getId()) == null) {
			GameServer.addToLog("[MountPark] Suppression d'un MountPark a Guild invalide. GuildID : "
					+ this.curMap.getMountPark().getGuild().getId());
			org.aestia.map.Map.removeMountPark(this.curMap.getMountPark().getGuild().getId());
		}
		if (Collector.getCollectorByMapId(this.curMap.getId()) != null
				&& World.getGuild(Collector.getCollectorByMapId(this.curMap.getId()).getGuildId()) == null) {
			GameServer.addToLog("[Collector] Suppression d'un Collector a Guild invalide. GuildID : "
					+ Collector.getCollectorByMapId(this.curMap.getId()).getGuildId());
			Collector.removeCollector(Collector.getCollectorByMapId(this.curMap.getId()).getGuildId());
		}
		if (PW != null) {
			SocketManager.GAME_SEND_MAPDATA(PW, newMapID, this.curMap.getDate(), this.curMap.getKey());
			this.curMap.addPlayer(this);
		}
		if (!this._Follower.isEmpty()) {
			for (final Player t : this._Follower.values()) {
				if (t.isOnline()) {
					SocketManager.GAME_SEND_FLAG_PACKET(t, this);
				} else {
					this._Follower.remove(t.getId());
				}
			}
		}
	}

	public void teleportFaction(final int factionEnnemy) {
		short mapID = 0;
		int cellID = 0;
		this.enteredOnEnnemyFaction = System.currentTimeMillis();
		this.isInEnnemyFaction = true;
		switch (factionEnnemy) {
		case 1: {
			mapID = 6164;
			cellID = 236;
			break;
		}
		case 2: {
			mapID = 6171;
			cellID = 397;
			break;
		}
		case 3: {
			mapID = 1002;
			cellID = 326;
			break;
		}
		default: {
			mapID = 8534;
			cellID = 297;
			break;
		}
		}
		SocketManager.PACKET_POPUP_DEPART(this,
				"Vous \u00eates en prison !<br />\nVous devrez donc patientez quelques Minutes avant de pouvoir sortir.<br/>\nParlez au gardien de prison pour obtenir plus d'information.");
		if (this.getEnergy() <= 0) {
			if (this.isOnMount()) {
				this.toogleOnMount();
			}
			this.isGhost = true;
			this.set_gfxID(8004);
			this.setCanAggro(false);
			this.set_away(true);
			this.setSpeed(-40);
		}
		this.teleportWithoutBlocked(mapID, cellID);
		Database.getStatique().getPlayerData().update(this, false);
	}

	public org.aestia.object.Object getItemGuid(final int IDtemplate) {
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> value : this._items.entrySet()) {
				final org.aestia.object.Object obj2 = World.getObjet(value.getKey());
				if (obj2.getTemplate().getId() == IDtemplate) {
					// monitorexit(this._items)
					return obj2;
				}
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public String parsecolortomount() {
		int color1 = this.getColor1();
		int color2 = this.getColor2();
		int color3 = this.getColor3();
		if (this.getObjetByPos(22) != null && this.getObjetByPos(22).getTemplate().getId() == 10838) {
			color1 = 16342021;
			color2 = 16342021;
			color3 = 16342021;
		}
		return String.valueOf((color1 == -1) ? "" : Integer.toHexString(color1)) + ","
				+ ((color2 == -1) ? "" : Integer.toHexString(color2)) + ","
				+ ((color3 == -1) ? "" : Integer.toHexString(color3));
	}

	public boolean addObjetWithOAKO(final org.aestia.object.Object objet, final boolean Similer) {
		synchronized (this._items) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				if (obj.getTemplate().getId() == objet.getTemplate().getId()
						&& obj.getStats().isSameStats(objet.getStats()) && Similer
						&& objet.getTemplate().getType() != 85 && obj.getPosition() == -1) {
					obj.setQuantity(obj.getQuantity() + objet.getQuantity());
					Database.getStatique().getItemData().save(obj, false);
					if (this._isOnline) {
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, obj);
					}
					// monitorexit(this._items)
					return false;
				}
			}
			this._items.put(objet.getGuid(), objet);
			SocketManager.GAME_SEND_OAKO_PACKET(this, objet);
		}
		// monitorexit(this._items)
		return true;
	}

	public boolean addObjetSimiler(final org.aestia.object.Object objet, final boolean hasSimiler, final int oldID) {
		final ObjectTemplate objModelo = objet.getTemplate();
		if (objModelo.getType() == 85 || objModelo.getType() == 18) {
			return false;
		}
		if (hasSimiler) {
			synchronized (this._items) {
				for (final Map.Entry<Integer, org.aestia.object.Object> entry : this._items.entrySet()) {
					final org.aestia.object.Object obj = entry.getValue();
					if (obj.getPosition() == -1 && obj.getGuid() != oldID
							&& obj.getTemplate().getId() == objModelo.getId()
							&& obj.getStats().isSameStats(objet.getStats())) {
						obj.setQuantity(obj.getQuantity() + objet.getQuantity());
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this, obj);
						// monitorexit(this._items)
						return true;
					}
				}
			}
			// monitorexit(this._items)
		}
		return false;
	}

	public Map<Integer, World.Couple<Integer, Integer>> getItemClasseSpell() {
		return this._itemClasseSpell;
	}

	public void removeItemClasseSpell(final int spell) {
		if (this._itemClasseSpell.containsKey(spell)) {
			this._itemClasseSpell.remove(spell);
		}
	}

	public void addItemClasseSpell(final int spell, final int effect, final int modif) {
		if (!this._itemClasseSpell.containsKey(spell)) {
			this._itemClasseSpell.put(spell, new World.Couple<Integer, Integer>(effect, modif));
		}
	}

	public ArrayList<Integer> getItemClasse() {
		return this._itemClasse;
	}

	public void setItemClasse(final ArrayList<Integer> ItemClasse) {
		this._itemClasse = ItemClasse;
	}

	public void addItemClasse(final int item) {
		if (!this._itemClasse.contains(item)) {
			this._itemClasse.add(item);
		}
	}

	public void removeItemClasse(final int item) {
		if (this._itemClasse.contains(item)) {
			final int index = this._itemClasse.indexOf(item);
			this._itemClasse.remove(index);
		}
	}

	public void refreshItemClasse() {
		for (int j = 2; j < 8; ++j) {
			if (this.getObjetByPos(j) != null) {
				final org.aestia.object.Object obj = this.getObjetByPos(j);
				final int template = obj.getTemplate().getId();
				final int pano = obj.getTemplate().getPanoId();
				if ((pano >= 81 && pano <= 92) || (pano >= 201 && pano <= 212)) {
					final String[] stats = obj.getTemplate().getStrTemplate().split(",");
					String[] array;
					for (int length = (array = stats).length, i = 0; i < length; ++i) {
						final String stat = array[i];
						final String[] val = stat.split("#");
						final int effect = Integer.parseInt(val[0], 16);
						final int spell = Integer.parseInt(val[1], 16);
						final int modif = Integer.parseInt(val[3], 16);
						final String modifi = String.valueOf(effect) + ";" + spell + ";" + modif;
						SocketManager.SEND_SB_SPELL_BOOST(this, modifi);
						this.addItemClasseSpell(spell, effect, modif);
					}
					if (!this._itemClasse.contains(template)) {
						this._itemClasse.add(template);
					}
				}
			}
		}
	}

	public int getItemClasseModif(final int spell, final int effect) {
		int modif = 0;
		if (this._bendHechizo == spell && this._bendEfecto == effect) {
			modif += this._bendModif;
		}
		if (this._itemClasseSpell.containsKey(spell) && this._itemClasseSpell.get(spell).first == effect) {
			modif += this._itemClasseSpell.get(spell).second;
			return modif;
		}
		return modif;
	}

	public int storeAllBuy() {
		int total = 0;
		synchronized (this._storeItems) {
			for (final Map.Entry<Integer, Integer> value : this._storeItems.entrySet()) {
				final org.aestia.object.Object O = World.getObjet(value.getKey());
				final int multiple = O.getQuantity();
				final int add = value.getValue() * multiple;
				total += add;
			}
		}
		// monitorexit(this._storeItems)
		return total;
	}

	public void DialogTimer() {
		(this.dialogTimer = new Manageable() {
			@Override
			public void launch() {
				GlobalManager.worldSheduler.schedule(this, 15L, TimeUnit.MINUTES);
			}

			@Override
			public void run() {
				if (Player.this.dialogTimer == null) {
					return;
				}
				if (Player.this.get_isTradingWith() == 0 && Player.this.getCurExchange() == null
						&& Player.this.getCurJobAction() == null && Player.this.getInMountPark() == null
						&& !Player.this.isInBank() && Player.this.get_isOnCollectorID() == 0
						&& Player.this.getInTrunk() == null) {
					return;
				}
				if (Player.this.get_isOnCollectorID() != 0) {
					final Collector Collector = World.getCollector(Player.this.get_isOnCollectorID());
					if (Collector == null) {
						return;
					}
					Collector.reloadTimer();
					for (final Player z : World.getGuild(Collector.getGuildId()).getMembers()) {
						if (z == null) {
							continue;
						}
						if (!z.isOnline()) {
							continue;
						}
						SocketManager.GAME_SEND_gITM_PACKET(z,
								org.aestia.entity.Collector.parseToGuild(z.get_guild().getId()));
						String str = "";
						str = String.valueOf(str) + "G" + Collector.getN1() + "," + Collector.getN2();
						str = String.valueOf(str) + "|.|" + World.getMap(Collector.getMap()).getX() + "|"
								+ World.getMap(Collector.getMap()).getY() + "|";
						str = String.valueOf(str) + Player.this.getName() + "|";
						str = String.valueOf(str) + Collector.getXp() + ";";
						if (!Collector.getLogObjects().equals("")) {
							str = String.valueOf(str) + Collector.getLogObjects();
						}
						Player.this.getGuildMember().giveXpToGuild(Collector.getXp());
						SocketManager.GAME_SEND_gT_PACKET(z, str);
					}
					Player.this.getCurMap().RemoveNpc(Collector.getId());
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(Player.this.getCurMap(), Collector.getId());
					Collector.delCollector(Collector.getId());
					Database.getGame().getPercepteurData().delete(Collector.getId());
					Player.this.set_isOnCollectorID(0);
				}
				Database.getStatique().getPlayerData().update(Player.this.getAccount().getCurPerso(), true);
				SocketManager.GAME_SEND_EV_PACKET(Player.this.getGameClient());
				Player.this.set_isTradingWith(0);
				Player.this.set_away(false);
				Player.this.setInBank(false);
				Player.this.setInTrunk(null);
			}
		}).launch();
	}

	public void set_timeInTaverne() {
		this._timeInTaverne = System.currentTimeMillis();
	}

	public void set_timeInTaverne(final long num) {
		this._timeInTaverne = num;
	}

	public long get_timeInTaverne() {
		return this._timeInTaverne;
	}

	public GameAction getGameAction() {
		return this._gameAction;
	}

	public void setGameAction(final GameAction Action) {
		this._gameAction = Action;
	}

	public int getAlignMap() {
		if (this.getCurMap().getSubArea() == null) {
			return -1;
		}
		if (this.getCurMap().getSubArea().getAlignement() == 0) {
			return 1;
		}
		if (this.getCurMap().getSubArea().getAlignement() == this.get_align()) {
			return 1;
		}
		return -1;
	}

	public Map<Integer, Integer> getStaticEmote() {
		return this.staticEmote;
	}

	public void setStaticEmote(final Map<Integer, Integer> staticEmote) {
		this.staticEmote = staticEmote;
	}

	public void addStaticEmote(final int emote) {
		if (this.staticEmote.containsKey(emote)) {
			return;
		}
		this.staticEmote.put(emote, emote);
		if (!this.isOnline()) {
			return;
		}
		SocketManager.GAME_SEND_EMOTE_LIST(this, getCompiledEmote(this.getStaticEmote(), this.getDynamicEmote()));
		SocketManager.GAME_SEND_STATS_PACKET(this);
		SocketManager.send(this, "eA" + emote);
	}

	public void deleteStaticEmote(final int emote) {
		if (!this.staticEmote.containsKey(emote)) {
			return;
		}
		this.staticEmote.remove(emote);
	}

	public Map<Integer, Integer> getDynamicEmote() {
		return this.dynamicEmote;
	}

	public void setDynamicEmote(final Map<Integer, Integer> dynamicEmote) {
		this.dynamicEmote = dynamicEmote;
	}

	public void addDynamicEmote(final int emote) {
		if (this.dynamicEmote.containsKey(emote)) {
			return;
		}
		this.dynamicEmote.put(emote, emote);
	}

	public void deleteDynamicEmote(final int emote) {
		if (!this.dynamicEmote.containsKey(emote)) {
			return;
		}
		this.dynamicEmote.remove(emote);
	}

	public String parseEmoteToDB() {
		final StringBuilder str = new StringBuilder();
		boolean isFirst = true;
		for (final int i : this.staticEmote.keySet()) {
			if (isFirst) {
				str.append(new StringBuilder(String.valueOf(i)).toString());
			} else {
				str.append(";" + i);
			}
			isFirst = false;
		}
		return str.toString();
	}

	public void setBlockMovement(final boolean b) {
		this.isBlocked = b;
	}

	public boolean getBlockMovement() {
		return this.isBlocked;
	}

	public GameClient getGameClient() {
		return this.getAccount().getGameClient();
	}

	public void send(final String packet) {
		SocketManager.send(this, packet);
	}

	public void sendMessage(final String msg) {
		SocketManager.GAME_SEND_MESSAGE(this, msg);
	}

	public boolean isSubscribe() {
		return !Main.useSubscribe || this.getAccount().isSubscribe();
	}

	public boolean isInAreaNotSubscribe() {
		boolean ok = Main.useSubscribe;
		if (this.curMap == null) {
			return false;
		}
		switch (this.curMap.getId()) {
		case 6824:
		case 6825:
		case 6826: {
			return false;
		}
		default: {
			if (this.curMap.getSubArea() == null) {
				return false;
			}
			if (this.curMap.getSubArea().getArea() == null) {
				return false;
			}
			if (this.curMap.getSubArea().getArea().get_superArea().get_id() == 3
					|| this.curMap.getSubArea().getArea().get_superArea().get_id() == 4
					|| this.curMap.getSubArea().getArea().get_id() == 18) {
				ok = false;
			}
			return ok;
		}
		}
	}

	public boolean cantDefie() {
		return this.getCurMap().noDefie;
	}

	public boolean cantAgro() {
		return this.getCurMap().noAgro;
	}

	public boolean cantCanal() {
		return this.getCurMap().noCanal;
	}

	public boolean cantTP() {
		return !this.isInPrison() && this.getCurMap().noTP;
	}

	public boolean isInPrison() {
		if (this.curMap == null) {
			return false;
		}
		switch (this.curMap.getId()) {
		case 666:
		case 8726: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public Quest_Etape checkMonster(final int monsterId) {
		for (final Quest.Quest_Perso questP : this.getQuestPerso().values()) {
			final Quest quest = questP.getQuest();
			for (final Quest_Etape qEtape : quest.getQuestEtapeList()) {
				if (!questP.isQuestEtapeIsValidate(qEtape) && (qEtape.getType() == 0 || qEtape.getType() == 6)
						&& qEtape.getMonsterId() == monsterId) {
					return qEtape;
				}
			}
		}
		return null;
	}

	public void setShootingMonsterInList(final int aId) {
		this.shootingMonster.add(aId);
	}

	public boolean haveShootingMonster(final int aId) {
		for (final Integer i : this.shootingMonster) {
			if (i == aId) {
				return true;
			}
		}
		return false;
	}

	public void removeShootingMonster(final int aId) {
		this.shootingMonster.remove(this.shootingMonster.indexOf(aId));
	}

	public ArrayList<Integer> getShootingMonster() {
		return this.shootingMonster;
	}

	public void addQuestPerso(final Quest.Quest_Perso qPerso) {
		this.questList.put(qPerso.getId(), qPerso);
	}

	public void delQuestPerso(final int key) {
		this.questList.remove(key);
	}

	public Map<Integer, Quest.Quest_Perso> getQuestPerso() {
		return this.questList;
	}

	public Quest.Quest_Perso getQuestPersoByQuest(final Quest quest) {
		for (final Quest.Quest_Perso qPerso : this.questList.values()) {
			if (qPerso.getQuest().getId() == quest.getId()) {
				return qPerso;
			}
		}
		return null;
	}

	public Quest.Quest_Perso getQuestPersoByQuestId(final int id) {
		for (final Quest.Quest_Perso qPerso : this.questList.values()) {
			if (qPerso.getQuest().getId() == id) {
				return qPerso;
			}
		}
		return null;
	}

	public String getQuestGmPacket() {
		final StringBuilder packet = new StringBuilder();
		int nb = 0;
		packet.append("+");
		for (final Quest.Quest_Perso qPerso : this.questList.values()) {
			packet.append(qPerso.getQuest().getId()).append(";");
			packet.append(qPerso.isFinish() ? 1 : 0);
			if (nb < this.questList.size() - 1) {
				packet.append("|");
			}
			++nb;
		}
		return packet.toString();
	}

	public TimerWaiter getWaiter() {
		if (this.getGameClient() == null) {
			return null;
		}
		return this.getGameClient().getWaiter();
	}
}
