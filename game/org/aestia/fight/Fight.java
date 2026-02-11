// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.aestia.client.Player;
import org.aestia.client.other.Group;
import org.aestia.common.ConditionParser;
import org.aestia.common.CryptManager;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.dynamic.FormuleOfficiel;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.PetEntry;
import org.aestia.entity.Prism;
import org.aestia.entity.monster.Monster;
import org.aestia.entity.monster.boss.Bandit;
import org.aestia.fight.ia.IAThread;
import org.aestia.fight.spells.LaunchedSpell;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.fight.traps.Glyph;
import org.aestia.fight.traps.Trap;
import org.aestia.fight.turn.Turn;
import org.aestia.game.GameAction;
import org.aestia.game.GameClient;
import org.aestia.game.GameServer;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.Case;
import org.aestia.map.laby.Toror;
import org.aestia.object.ObjectTemplate;
import org.aestia.object.entity.SoulStone;
import org.aestia.other.Guild;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

public class Fight {
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	private int id;
	private int state;
	private int guildId;
	private int type;
	private int st1;
	private int st2;
	private int curPlayer;
	private int captWinner;
	private int curFighterPa;
	private int curFighterPm;
	private int curFighterUsedPa;
	private int curFighterUsedPm;
	private Map<Integer, Fighter> team0;
	private Map<Integer, Fighter> team1;
	private Map<Integer, Fighter> deadList;
	private Map<Integer, Player> viewer;
	private ArrayList<Case> start0;
	private ArrayList<Case> start1;
	private Map<Integer, Challenge> allChallenges;
	private Map<Integer, Case> rholBack;
	private List<Glyph> allGlyphs;
	private List<Trap> allTraps;
	private List<Fighter> orderPlaying;
	private ArrayList<Fighter> capturer;
	private ArrayList<Fighter> trainer;
	private long launchTime;
	private long startTime;
	private boolean locked0;
	private boolean locked1;
	private boolean onlyGroup0;
	private boolean onlyGroup1;
	private boolean help0;
	private boolean help1;
	private boolean viewerOk;
	private boolean haveKnight;
	private boolean isBegin;
	private boolean checkTimer;
	private boolean isCapturable;
	public boolean finish;
	private String curAction;
	private Monster.MobGroup mobGroup;
	private Collector collector;
	private Prism prism;
	private org.aestia.map.Map map;
	private org.aestia.map.Map mapOld;
	private Fighter init0;
	private Fighter init1;
	private SoulStone fullSoul;
	private Turn turn;
	private String defenders;
	private int trainerWinner;
	private int nextId;
	private TimerWaiter waiter;
	public boolean waiterLancer;
	private ScheduledFuture<?> timerTask;
	boolean collectorProtect;

	public TimerWaiter getWaiter() {
		return this.waiter;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getState() {
		return this.state;
	}

	public void setState(final int state) {
		this.state = state;
	}

	public int getGuildId() {
		return this.guildId;
	}

	public void setGuildId(final int guildId) {
		this.guildId = guildId;
	}

	public int getType() {
		return this.type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public int getSt1() {
		return this.st1;
	}

	public void setSt1(final int st1) {
		this.st1 = st1;
	}

	public int getSt2() {
		return this.st2;
	}

	public void setSt2(final int st2) {
		this.st2 = st2;
	}

	public int getCurPlayer() {
		return this.curPlayer;
	}

	public void setCurPlayer(final int curPlayer) {
		this.curPlayer = curPlayer;
	}

	public int getCaptWinner() {
		return this.captWinner;
	}

	public void setCaptWinner(final int captWinner) {
		this.captWinner = captWinner;
	}

	public int getCurFighterPa() {
		return this.curFighterPa;
	}

	public void setCurFighterPa(final int curFighterPa) {
		this.curFighterPa = curFighterPa;
	}

	public int getCurFighterPm() {
		return this.curFighterPm;
	}

	public void setCurFighterPm(final int curFighterPm) {
		this.curFighterPm = curFighterPm;
	}

	public int getCurFighterUsedPa() {
		return this.curFighterUsedPa;
	}

	public void setCurFighterUsedPa(final int curFighterUsedPa) {
		this.curFighterUsedPa = curFighterUsedPa;
	}

	public int getCurFighterUsedPm() {
		return this.curFighterUsedPm;
	}

	public void setCurFighterUsedPm(final int curFighterUsedPm) {
		this.curFighterUsedPm = curFighterUsedPm;
	}

	public Map<Integer, Fighter> getTeam(final int team) {
		switch (team) {
		case 1: {
			return this.team0;
		}
		case 2: {
			return this.team1;
		}
		default: {
			return this.team0;
		}
		}
	}

	public Map<Integer, Fighter> getTeam0() {
		return this.team0;
	}

	public Map<Integer, Fighter> getTeam1() {
		return this.team1;
	}

	public Map<Integer, Fighter> getDeadList() {
		return this.deadList;
	}

	public boolean removeDead(final Fighter target) {
		return this.deadList.remove(target.getId(), target);
	}

	public Map<Integer, Player> getViewer() {
		return this.viewer;
	}

	public ArrayList<Case> getStart0() {
		return this.start0;
	}

	public ArrayList<Case> getStart1() {
		return this.start1;
	}

	public Map<Integer, Challenge> getAllChallenges() {
		return this.allChallenges;
	}

	public Map<Integer, Case> getRholBack() {
		return this.rholBack;
	}

	public List<Glyph> getAllGlyphs() {
		return this.allGlyphs;
	}

	public List<Trap> getAllTraps() {
		return this.allTraps;
	}

	public ArrayList<Fighter> getCapturer() {
		return this.capturer;
	}

	public ArrayList<Fighter> getTrainer() {
		return this.trainer;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	public long getLaunchTime() {
		return this.launchTime;
	}

	public void setLaunchTime(final long launchTime) {
		this.launchTime = launchTime;
	}

	public boolean isLocked0() {
		return this.locked0;
	}

	public void setLocked0(final boolean locked0) {
		this.locked0 = locked0;
	}

	public boolean isLocked1() {
		return this.locked1;
	}

	public void setLocked1(final boolean locked1) {
		this.locked1 = locked1;
	}

	public boolean isOnlyGroup0() {
		return this.onlyGroup0;
	}

	public void setOnlyGroup0(final boolean onlyGroup0) {
		this.onlyGroup0 = onlyGroup0;
	}

	public boolean isOnlyGroup1() {
		return this.onlyGroup1;
	}

	public void setOnlyGroup1(final boolean onlyGroup1) {
		this.onlyGroup1 = onlyGroup1;
	}

	public boolean isHelp0() {
		return this.help0;
	}

	public void setHelp0(final boolean help0) {
		this.help0 = help0;
	}

	public boolean isHelp1() {
		return this.help1;
	}

	public void setHelp1(final boolean help1) {
		this.help1 = help1;
	}

	public boolean isViewerOk() {
		return this.viewerOk;
	}

	public void setViewerOk(final boolean viewerOk) {
		this.viewerOk = viewerOk;
	}

	public boolean isHaveKnight() {
		return this.haveKnight;
	}

	public void setHaveKnight(final boolean haveKnight) {
		this.haveKnight = haveKnight;
	}

	public boolean isBegin() {
		return this.isBegin;
	}

	public void setBegin(final boolean isBegin) {
		this.isBegin = isBegin;
	}

	public boolean isCheckTimer() {
		return this.checkTimer;
	}

	public void setCheckTimer(final boolean checkTimer) {
		this.checkTimer = checkTimer;
	}

	public boolean isCapturable() {
		return this.isCapturable;
	}

	public void setCapturable(final boolean isCapturable) {
		this.isCapturable = isCapturable;
	}

	public String getCurAction() {
		return this.curAction;
	}

	public void setCurAction(final String curAction) {
		this.curAction = curAction;
	}

	public Monster.MobGroup getMobGroup() {
		return this.mobGroup;
	}

	public void setMobGroup(final Monster.MobGroup mobGroup) {
		this.mobGroup = mobGroup;
	}

	public Collector getCollector() {
		return this.collector;
	}

	public void setCollector(final Collector collector) {
		this.collector = collector;
	}

	public Prism getPrism() {
		return this.prism;
	}

	public void setPrism(final Prism prism) {
		this.prism = prism;
	}

	public org.aestia.map.Map getMap() {
		return this.map;
	}

	public void setMap(final org.aestia.map.Map map) {
		this.map = map;
	}

	public org.aestia.map.Map getMapOld() {
		return this.mapOld;
	}

	public void setMapOld(final org.aestia.map.Map mapOld) {
		this.mapOld = mapOld;
	}

	public Fighter getInit0() {
		return this.init0;
	}

	public void setInit0(final Fighter init0) {
		this.init0 = init0;
	}

	public Fighter getInit1() {
		return this.init1;
	}

	public void setInit1(final Fighter init1) {
		this.init1 = init1;
	}

	public SoulStone getFullSoul() {
		return this.fullSoul;
	}

	public void setFullSoul(final SoulStone fullSoul) {
		this.fullSoul = fullSoul;
	}

	public String getDefenders() {
		return this.defenders;
	}

	public void setDefenders(final String defenders) {
		this.defenders = defenders;
	}

	public int getTrainerWinner() {
		return this.trainerWinner;
	}

	public void setTrainerWinner(final int trainerWinner) {
		this.trainerWinner = trainerWinner;
	}

	public int getTeamId(final int guid) {
		if (this.getTeam0().containsKey(guid)) {
			return 1;
		}
		if (this.getTeam1().containsKey(guid)) {
			return 2;
		}
		if (this.getViewer().containsKey(guid)) {
			return 4;
		}
		return -1;
	}

	public int getOtherTeamId(final int guid) {
		if (this.getTeam0().containsKey(guid)) {
			return 2;
		}
		if (this.getTeam1().containsKey(guid)) {
			return 1;
		}
		return -1;
	}

	public static int getFightIdByFighter(final org.aestia.map.Map map, final int guid) {
		for (final Map.Entry<Integer, Fight> fight : map.getFights().entrySet()) {
			for (final Map.Entry<Integer, Fighter> F : fight.getValue().getTeam0().entrySet()) {
				if (F.getValue().getPersonnage() != null && F.getValue().getId() == guid) {
					return fight.getValue().getId();
				}
			}
		}
		return 0;
	}

	public void scheduleTimer(final int time) {
		final Runnable run = new Runnable() {
			@Override
			public void run() {
				if (Fight.this.collector != null && !Fight.this.collectorProtect) {
					Fight.this.collector.removeTimeTurn(1000);
				}
				if (Fight.this.getState() != 3) {
					Fight.this.startFight();
				} else if (Fight.this.collector != null && !Fight.this.collectorProtect) {
					Fight.this.collector.setTimeTurn(60000L);
				}
			}
		};
		this.timerTask = Fight.executor.schedule(run, time, TimeUnit.SECONDS);
	}

	public void closeSheduler() {
		if (this.timerTask != null && !this.timerTask.isCancelled()) {
			this.timerTask.cancel(true);
		}
		this.timerTask = null;
	}

	public Fight(final int type, final int id, final org.aestia.map.Map map, final Player perso, final Player init2) {
		this.state = 0;
		this.guildId = -1;
		this.type = -1;
		this.captWinner = -1;
		this.team0 = new TreeMap<Integer, Fighter>();
		this.team1 = new TreeMap<Integer, Fighter>();
		this.deadList = new TreeMap<Integer, Fighter>();
		this.viewer = new TreeMap<Integer, Player>();
		this.start0 = new ArrayList<Case>();
		this.start1 = new ArrayList<Case>();
		this.allChallenges = new TreeMap<Integer, Challenge>();
		this.rholBack = new TreeMap<Integer, Case>();
		this.allGlyphs = new ArrayList<Glyph>();
		this.allTraps = new ArrayList<Trap>();
		this.orderPlaying = new ArrayList<Fighter>();
		this.capturer = new ArrayList<Fighter>(8);
		this.trainer = new ArrayList<Fighter>(8);
		this.launchTime = 0L;
		this.startTime = 0L;
		this.locked0 = false;
		this.locked1 = false;
		this.onlyGroup0 = false;
		this.onlyGroup1 = false;
		this.help0 = false;
		this.help1 = false;
		this.viewerOk = true;
		this.haveKnight = false;
		this.isBegin = false;
		this.checkTimer = false;
		this.isCapturable = false;
		this.finish = false;
		this.curAction = "";
		this.defenders = "";
		this.trainerWinner = -1;
		this.nextId = -100;
		this.waiter = new TimerWaiter();
		this.waiterLancer = false;
		this.collectorProtect = false;
		this.launchTime = System.currentTimeMillis();
		this.setType(type);
		this.setId(id);
		this.setMap(map.getMapCopy());
		this.setMapOld(map);
		this.setInit0(new Fighter(this, perso));
		this.setInit1(new Fighter(this, init2));
		this.getTeam0().put(perso.getId(), this.getInit0());
		this.getTeam1().put(init2.getId(), this.getInit1());
		SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, this.getMap().getCases().values());
		if (this.getType() != 0) {
			this.scheduleTimer(45);
		}
		final int cancelBtn = (this.getType() == 0) ? 1 : 0;
		final long time = (this.getType() == 0) ? 0 : 45000;
		SocketManager.GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(this, 7, 2, cancelBtn, 1, 0, time, this.getType());
		if (init2.get_align() == 0) {
			this.setHaveKnight(true);
		}
		final int morph = perso.get_gfxID();
		if (morph == 1109 || morph == 1046 || morph == 9001) {
			perso.unsetFullMorph();
			SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.getCurMap(), perso);
		}
		this.start0 = CryptManager.parseStartCell(this.getMap(), 0);
		this.start1 = CryptManager.parseStartCell(this.getMap(), 1);
		SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 0);
		SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 2, this.getMap().getPlaces(), 1);
		this.setSt1(0);
		this.setSt2(1);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 3 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(init2.getId())).toString(),
				String.valueOf(init2.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(init2.getId())).toString(),
				String.valueOf(init2.getId()) + "," + 3 + ",0");
		this.getInit0().setCell(this.getRandomCell(this.start0));
		this.getInit1().setCell(this.getRandomCell(this.start1));
		this.getInit0().getPersonnage().getCurCell().removeCharacter(this.getInit0().getId());
		this.getInit1().getPersonnage().getCurCell().removeCharacter(this.getInit1().getId());
		this.getInit0().getCell().addFighter(this.getInit0());
		this.getInit1().getCell().addFighter(this.getInit1());
		this.getInit0().getPersonnage().set_fight(this);
		this.getInit0().setTeam(0);
		this.getInit1().getPersonnage().set_fight(this);
		this.getInit1().setTeam(1);
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit1().getPersonnage().getCurMap(),
				this.getInit1().getId());
		if (this.getType() == 1) {
			SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(), 0,
					this.getInit0().getId(), this.getInit1().getId(),
					this.getInit0().getPersonnage().getCurCell().getId(),
					"0;" + this.getInit0().getPersonnage().get_align(),
					this.getInit1().getPersonnage().getCurCell().getId(),
					"0;" + this.getInit1().getPersonnage().get_align());
		} else {
			SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(), 0,
					this.getInit0().getId(), this.getInit1().getId(),
					this.getInit0().getPersonnage().getCurCell().getId(), "0;-1",
					this.getInit1().getPersonnage().getCurCell().getId(), "0;-1");
		}
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId(), this.getInit0());
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit1().getId(), this.getInit1());
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(this, 7, this.getMap());
		this.setState(2);
	}

	public Fight(final int id, final org.aestia.map.Map map, final Player perso, final Monster.MobGroup group) {
		System.out.println("Player vita = " + perso.getCurPdv());
		this.state = 0;
		this.guildId = -1;
		this.type = -1;
		this.captWinner = -1;
		this.team0 = new TreeMap<Integer, Fighter>();
		this.team1 = new TreeMap<Integer, Fighter>();
		this.deadList = new TreeMap<Integer, Fighter>();
		this.viewer = new TreeMap<Integer, Player>();
		this.start0 = new ArrayList<Case>();
		this.start1 = new ArrayList<Case>();
		this.allChallenges = new TreeMap<Integer, Challenge>();
		this.rholBack = new TreeMap<Integer, Case>();
		this.allGlyphs = new ArrayList<Glyph>();
		this.allTraps = new ArrayList<Trap>();
		this.orderPlaying = new ArrayList<Fighter>();
		this.capturer = new ArrayList<Fighter>(8);
		this.trainer = new ArrayList<Fighter>(8);
		this.launchTime = 0L;
		this.startTime = 0L;
		this.locked0 = false;
		this.locked1 = false;
		this.onlyGroup0 = false;
		this.onlyGroup1 = false;
		this.help0 = false;
		this.help1 = false;
		this.viewerOk = true;
		this.haveKnight = false;
		this.isBegin = false;
		this.checkTimer = false;
		this.isCapturable = false;
		this.finish = false;
		this.curAction = "";
		this.defenders = "";
		this.trainerWinner = -1;
		this.nextId = -100;
		this.waiter = new TimerWaiter();
		this.waiterLancer = false;
		this.collectorProtect = false;
		this.launchTime = System.currentTimeMillis();
		this.setCheckTimer(true);
		this.setMobGroup(group);
		this.demorph(perso);
		this.setType(4);
		this.setId(id);
		this.setMap(map.getMapCopy());
		this.setMapOld(map);
		this.setInit0(new Fighter(this, perso));
		this.getInit0().setLaunchCell(group.getCellId());
		this.getTeam0().put(perso.getId(), this.getInit0());
		for (final Map.Entry<Integer, Monster.MobGrade> entry : group.getMobs().entrySet()) {
			entry.getValue().setInFightID(entry.getKey());
			final Fighter mob = new Fighter(this, entry.getValue());
			this.getTeam1().put(entry.getKey(), mob);
			if (entry.getValue().getTemplate().getId() == 832) {
				Toror.demi();
			} else {
				if (entry.getValue().getTemplate().getId() != 831) {
					continue;
				}
				Toror.momi();
			}
		}
		SocketManager.GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(this, 1, 2, 0, 1, 0, 45000, this.getType());
		System.err.println("STAPE 1");
		SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, this.getMap().getCases().values());
		System.err.println("STAPE 2");
		this.scheduleTimer(45);
		System.err.println("STAPE 3");
		this.start0 = CryptManager.parseStartCell(this.getMap(), 0);
		System.err.println("STAPE 4");
		this.start1 = CryptManager.parseStartCell(this.getMap(), 1);
		System.err.println("STAPE 5");
		SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 0);
		this.setSt1(0);
		this.setSt2(1);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 3 + ",0");
		final List<Map.Entry<Integer, Fighter>> e = new ArrayList<Map.Entry<Integer, Fighter>>();
		e.addAll(this.getTeam1().entrySet());
		for (final Map.Entry<Integer, Fighter> entry2 : e) {
			final Fighter f = entry2.getValue();
			final Case cell = this.getRandomCell(this.getStart1());
			if (cell == null) {
				this.getTeam1().remove(f.getId());
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 3 + ",0");
				f.setCell(cell);
				f.getCell().addFighter(f);
				f.setTeam(1);
				f.fullPdv();
			}
		}
		this.getInit0().setCell(this.getRandomCell(this.getStart0()));
		this.getInit0().getPersonnage().getCurCell().removeCharacter(this.getInit0().getPersonnage().getId());
		this.getInit0().getCell().addFighter(this.getInit0());
		this.getInit0().getPersonnage().set_fight(this);
		this.getInit0().setTeam(0);
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(), group.getId());
		int c = Pathfinding.getNearestCellAround(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getPersonnage().getCurCell().getId(), group.getCellId(), new ArrayList<Case>());
		if (c < 0) {
			c = this.getInit0().getPersonnage().getCurCell().getId();
		}
		SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(), 4,
				this.getInit0().getId(), group.getId(), c, "0;-1", group.getCellId(), "1;-1");
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId(), this.getInit0());
		for (final Fighter f2 : this.getTeam1().values()) {
			SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					group.getId(), f2);
		}
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(this, 7, this.getMap());
		this.setState(2);
	}

	public Fight(final int id, final org.aestia.map.Map map, final Player perso, final Monster.MobGroup group,
			final int type) {
		this.state = 0;
		this.guildId = -1;
		this.type = -1;
		this.captWinner = -1;
		this.team0 = new TreeMap<Integer, Fighter>();
		this.team1 = new TreeMap<Integer, Fighter>();
		this.deadList = new TreeMap<Integer, Fighter>();
		this.viewer = new TreeMap<Integer, Player>();
		this.start0 = new ArrayList<Case>();
		this.start1 = new ArrayList<Case>();
		this.allChallenges = new TreeMap<Integer, Challenge>();
		this.rholBack = new TreeMap<Integer, Case>();
		this.allGlyphs = new ArrayList<Glyph>();
		this.allTraps = new ArrayList<Trap>();
		this.orderPlaying = new ArrayList<Fighter>();
		this.capturer = new ArrayList<Fighter>(8);
		this.trainer = new ArrayList<Fighter>(8);
		this.launchTime = 0L;
		this.startTime = 0L;
		this.locked0 = false;
		this.locked1 = false;
		this.onlyGroup0 = false;
		this.onlyGroup1 = false;
		this.help0 = false;
		this.help1 = false;
		this.viewerOk = true;
		this.haveKnight = false;
		this.isBegin = false;
		this.checkTimer = false;
		this.isCapturable = false;
		this.finish = false;
		this.curAction = "";
		this.defenders = "";
		this.trainerWinner = -1;
		this.nextId = -100;
		this.waiter = new TimerWaiter();
		this.waiterLancer = false;
		this.collectorProtect = false;
		this.launchTime = System.currentTimeMillis();
		this.setMobGroup(group);
		this.setType(type);
		this.setId(id);
		this.setMap(map.getMapCopy());
		this.setMapOld(map);
		this.demorph(perso);
		this.setInit0(new Fighter(this, perso));
		this.getTeam0().put(perso.getId(), this.getInit0());
		for (final Map.Entry<Integer, Monster.MobGrade> entry : group.getMobs().entrySet()) {
			entry.getValue().setInFightID(entry.getKey());
			final Fighter mob = new Fighter(this, entry.getValue());
			this.getTeam1().put(entry.getKey(), mob);
			if (entry.getValue().getTemplate().getId() == 832) {
				Toror.demi();
			} else {
				if (entry.getValue().getTemplate().getId() != 831) {
					continue;
				}
				Toror.momi();
			}
		}
		if (perso != null && perso.getCurPdv() >= perso.getMaxPdv()) {
			final int pdvMax = perso.getMaxPdv();
			perso.setPdv(pdvMax);
		}
		SocketManager.GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(this, 1, 2, 0, 1, 0, 45000, this.getType());
		SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, this.getMap().getCases().values());
		this.scheduleTimer(45);
		this.start0 = CryptManager.parseStartCell(this.getMap(), 0);
		this.start1 = CryptManager.parseStartCell(this.getMap(), 1);
		SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 0);
		this.setSt1(0);
		this.setSt2(1);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 3 + ",0");
		final List<Map.Entry<Integer, Fighter>> e = new ArrayList<Map.Entry<Integer, Fighter>>();
		e.addAll(this.getTeam1().entrySet());
		for (final Map.Entry<Integer, Fighter> entry2 : e) {
			final Fighter f = entry2.getValue();
			final Case cell = this.getRandomCell(this.getStart1());
			if (cell == null) {
				this.getTeam1().remove(f.getId());
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 3 + ",0");
				f.setCell(cell);
				f.getCell().addFighter(f);
				f.setTeam(1);
				f.fullPdv();
			}
		}
		this.getInit0().setCell(this.getRandomCell(this.getStart0()));
		this.getInit0().getPersonnage().getCurCell().removeCharacter(this.getInit0().getPersonnage().getId());
		this.getInit0().getCell().addFighter(this.getInit0());
		this.getInit0().getPersonnage().set_fight(this);
		this.getInit0().setTeam(0);
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(), group.getId());
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId(), this.getInit0());
		for (final Fighter f2 : this.getTeam1().values()) {
			SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					group.getId(), f2);
		}
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(this, 7, this.getMap());
		this.setState(2);
	}

	public Fight(final int id, final org.aestia.map.Map map, final Player perso, final Collector perco) {
		this.state = 0;
		this.guildId = -1;
		this.type = -1;
		this.captWinner = -1;
		this.team0 = new TreeMap<Integer, Fighter>();
		this.team1 = new TreeMap<Integer, Fighter>();
		this.deadList = new TreeMap<Integer, Fighter>();
		this.viewer = new TreeMap<Integer, Player>();
		this.start0 = new ArrayList<Case>();
		this.start1 = new ArrayList<Case>();
		this.allChallenges = new TreeMap<Integer, Challenge>();
		this.rholBack = new TreeMap<Integer, Case>();
		this.allGlyphs = new ArrayList<Glyph>();
		this.allTraps = new ArrayList<Trap>();
		this.orderPlaying = new ArrayList<Fighter>();
		this.capturer = new ArrayList<Fighter>(8);
		this.trainer = new ArrayList<Fighter>(8);
		this.launchTime = 0L;
		this.startTime = 0L;
		this.locked0 = false;
		this.locked1 = false;
		this.onlyGroup0 = false;
		this.onlyGroup1 = false;
		this.help0 = false;
		this.help1 = false;
		this.viewerOk = true;
		this.haveKnight = false;
		this.isBegin = false;
		this.checkTimer = false;
		this.isCapturable = false;
		this.finish = false;
		this.curAction = "";
		this.defenders = "";
		this.trainerWinner = -1;
		this.nextId = -100;
		this.waiter = new TimerWaiter();
		this.waiterLancer = false;
		this.collectorProtect = false;
		if (perso.get_fight() != null) {
			return;
		}
		this.launchTime = System.currentTimeMillis();
		this.setGuildId(perco.getGuildId());
		perco.setInFight((byte) 1);
		perco.set_inFightID((byte) id);
		this.demorph(perso);
		this.setType(5);
		this.setId(id);
		this.setMap(map.getMapCopy());
		this.setMapOld(map);
		this.setInit0(new Fighter(this, perso));
		this.setCollector(perco);
		this.getTeam0().put(perso.getId(), this.getInit0());
		final Fighter percoF = new Fighter(this, perco);
		this.getTeam1().put(-1, percoF);
		SocketManager.GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(this, 1, 2, 0, 1, 0, 45000, this.getType());
		SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, this.getMap().getCases().values());
		this.scheduleTimer(60);
		final Random teams = new Random();
		if (teams.nextBoolean()) {
			this.start0 = CryptManager.parseStartCell(this.getMap(), 0);
			this.start1 = CryptManager.parseStartCell(this.getMap(), 1);
			SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 0);
			this.setSt1(0);
			this.setSt2(1);
		} else {
			this.start0 = CryptManager.parseStartCell(this.getMap(), 1);
			this.start1 = CryptManager.parseStartCell(this.getMap(), 0);
			this.setSt1(1);
			this.setSt2(0);
			SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 1);
		}
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 3 + ",0");
		final List<Map.Entry<Integer, Fighter>> e = new ArrayList<Map.Entry<Integer, Fighter>>();
		e.addAll(this.getTeam1().entrySet());
		for (final Map.Entry<Integer, Fighter> entry : e) {
			final Fighter f = entry.getValue();
			final Case cell = this.getRandomCell(this.start1);
			if (cell == null) {
				this.getTeam1().remove(f.getId());
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 3 + ",0");
				f.setCell(cell);
				f.getCell().addFighter(f);
				f.setTeam(1);
				f.fullPdv();
			}
		}
		this.getInit0().setCell(this.getRandomCell(this.start0));
		this.getInit0().getPersonnage().getCurCell().removeCharacter(this.getInit0().getPersonnage().getId());
		this.getInit0().getCell().addFighter(this.getInit0());
		this.getInit0().getPersonnage().set_fight(this);
		this.getInit0().setTeam(0);
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(), perco.getId());
		int c = Pathfinding.getNearestCellAround(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getPersonnage().getCurCell().getId(), perco.getCell(), new ArrayList<Case>());
		if (c < 0) {
			c = this.getInit0().getPersonnage().getCurCell().getId();
		}
		SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(), 5,
				this.getInit0().getId(), perco.getId(), c, "0;-1", perco.getCell(), "3;-1");
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId(), this.getInit0());
		for (final Fighter f2 : this.getTeam1().values()) {
			SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					perco.getId(), f2);
		}
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(this, 7, this.getMap());
		this.setState(2);
		String str = "";
		if (this.getCollector() != null) {
			str = "A" + this.getCollector().getN1() + "," + this.getCollector().getN2() + "|.|"
					+ World.getMap(this.getCollector().getMap()).getX() + "|"
					+ World.getMap(this.getCollector().getMap()).getY();
		}
		for (final Player z : World.getGuild(this.getGuildId()).getMembers()) {
			if (z == null) {
				continue;
			}
			if (!z.isOnline()) {
				continue;
			}
			SocketManager.GAME_SEND_gITM_PACKET(z, Collector.parseToGuild(z.get_guild().getId()));
			Collector.parseAttaque(z, this.getGuildId());
			Collector.parseDefense(z, this.getGuildId());
			SocketManager.SEND_gA_PERCEPTEUR(z, str);
		}
	}

	public Fight(final int id, final org.aestia.map.Map Map, final Player perso, final Prism Prisme) {
		this.state = 0;
		this.guildId = -1;
		this.type = -1;
		this.captWinner = -1;
		this.team0 = new TreeMap<Integer, Fighter>();
		this.team1 = new TreeMap<Integer, Fighter>();
		this.deadList = new TreeMap<Integer, Fighter>();
		this.viewer = new TreeMap<Integer, Player>();
		this.start0 = new ArrayList<Case>();
		this.start1 = new ArrayList<Case>();
		this.allChallenges = new TreeMap<Integer, Challenge>();
		this.rholBack = new TreeMap<Integer, Case>();
		this.allGlyphs = new ArrayList<Glyph>();
		this.allTraps = new ArrayList<Trap>();
		this.orderPlaying = new ArrayList<Fighter>();
		this.capturer = new ArrayList<Fighter>(8);
		this.trainer = new ArrayList<Fighter>(8);
		this.launchTime = 0L;
		this.startTime = 0L;
		this.locked0 = false;
		this.locked1 = false;
		this.onlyGroup0 = false;
		this.onlyGroup1 = false;
		this.help0 = false;
		this.help1 = false;
		this.viewerOk = true;
		this.haveKnight = false;
		this.isBegin = false;
		this.checkTimer = false;
		this.isCapturable = false;
		this.finish = false;
		this.curAction = "";
		this.defenders = "";
		this.trainerWinner = -1;
		this.nextId = -100;
		this.waiter = new TimerWaiter();
		this.waiterLancer = false;
		this.collectorProtect = false;
		this.launchTime = System.currentTimeMillis();
		Prisme.setInFight(0);
		Prisme.setFightId(id);
		this.demorph(perso);
		this.setType(2);
		this.setId(id);
		this.setMap(Map.getMapCopy());
		this.setMapOld(Map);
		this.setInit0(new Fighter(this, perso));
		this.setPrism(Prisme);
		this.getTeam0().put(perso.getId(), this.getInit0());
		final Fighter lPrisme = new Fighter(this, Prisme);
		this.getTeam1().put(-1, lPrisme);
		SocketManager.GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(this, 1, 2, 0, 1, 0, 45000, this.getType());
		SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, this.getMap().getCases().values());
		this.scheduleTimer(60);
		final Random teams = new Random();
		if (teams.nextBoolean()) {
			this.start0 = CryptManager.parseStartCell(this.getMap(), 0);
			this.start1 = CryptManager.parseStartCell(this.getMap(), 1);
			SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 0);
			this.setSt1(0);
			this.setSt2(1);
		} else {
			this.start0 = CryptManager.parseStartCell(this.getMap(), 1);
			this.start1 = CryptManager.parseStartCell(this.getMap(), 0);
			this.setSt1(1);
			this.setSt2(0);
			SocketManager.GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(this, 1, this.getMap().getPlaces(), 1);
		}
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
				new StringBuilder(String.valueOf(perso.getId())).toString(),
				String.valueOf(perso.getId()) + "," + 3 + ",0");
		final List<Map.Entry<Integer, Fighter>> e = new ArrayList<Map.Entry<Integer, Fighter>>();
		e.addAll(this.getTeam1().entrySet());
		for (final Map.Entry<Integer, Fighter> entry : e) {
			final Fighter f = entry.getValue();
			final Case cell = this.getRandomCell(this.getStart1());
			if (cell == null) {
				this.getTeam1().remove(f.getId());
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 3 + ",0");
				f.setCell(cell);
				f.getCell().addFighter(f);
				f.setTeam(1);
				f.fullPdv();
			}
		}
		this.getInit0().setCell(this.getRandomCell(this.getStart0()));
		this.getInit0().getPersonnage().getCurCell().removeCharacter(this.getInit0().getPersonnage().getId());
		this.getInit0().getCell().addFighter(this.getInit0());
		this.getInit0().getPersonnage().set_fight(this);
		this.getInit0().setTeam(0);
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId());
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.getInit0().getPersonnage().getCurMap(), Prisme.getId());
		int c = Pathfinding.getNearestCellAround(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getPersonnage().getCurCell().getId(), Prisme.getCell(), new ArrayList<Case>());
		if (c < 0) {
			c = this.getInit0().getPersonnage().getCurCell().getId();
		}
		SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(), 0,
				this.getInit0().getId(), Prisme.getId(), c, "0;" + this.getInit0().getPersonnage().get_align(),
				Prisme.getCell(), "0;" + Prisme.getAlignement());
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId(), this.getInit0());
		for (final Fighter f2 : this.getTeam1().values()) {
			SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					Prisme.getId(), f2);
		}
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(this, 7, this.getMap());
		this.setState(2);
		String str = "";
		if (this.getPrism() != null) {
			str = String.valueOf(Prisme.getMap()) + "|" + Prisme.getX() + "|" + Prisme.getY();
		}
		for (final Player z : World.getOnlinePersos()) {
			if (z == null) {
				continue;
			}
			if (z.get_align() != Prisme.getAlignement()) {
				continue;
			}
			SocketManager.SEND_CA_ATTAQUE_MESSAGE_PRISME(z, str);
		}
	}

	public void demorph(final Player p) {
		if (!p.getMorphMode() && p.isMorph() && p.getGroupe() == null && p.getMorphId() != 8006
				&& p.getMorphId() != 8007 && p.getMorphId() != 8009) {
			p.unsetMorph();
		}
	}

	public void startFight() {
		this.launchTime = -1L;
		this.startTime = System.currentTimeMillis();
		if (this.collector != null && !this.collectorProtect) {
			final ArrayList<Player> protectors = new ArrayList<Player>(this.collector.getDefenseFight().values());
			for (final Player player : protectors) {
				if (player.get_fight() == null && !player.is_away()) {
					player.setOldMap(player.getCurMap().getId());
					player.setOldCell(player.getCurCell().getId());
					if (player.getCurMap().getId() != this.getMapOld().getId()) {
						player.teleport(this.getMapOld().getId(), this.collector.getCell());
					}
					final Player _p = player;
					this.waiter.addNow(new Runnable() {
						@Override
						public void run() {
							Fight.this.joinCollectorFight(_p, _p.getId(), Fight.this.collector.getId());
						}
					}, 1000L);
				} else {
					SocketManager.GAME_SEND_MESSAGE(player,
							"Vous n'avez pas pu rejoindre le combat du percepteur suite \u00e0 votre indisponibilit\u00e9.");
					this.collector.delDefenseFight(player);
				}
			}
			this.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					Fight.this.collectorProtect = true;
					Fight.this.scheduleTimer(10);
				}
			}, 100L);
			return;
		}
		if (this.getState() >= 3) {
			return;
		}
		if (this.getType() == 4) {
			if (this.getMobGroup().isFix() && this.isCheckTimer() && this.getMap().getId() != 6826
					&& this.getMap().getId() != 10332 && this.getMap().getId() != 7388) {
				World.getMap(this.getMap().getId()).spawnAfterTimeGroupFix(this.getMobGroup().getCellId());
			}
			if (!this.getMobGroup().isFix() && this.isCheckTimer()) {
				World.getMap(this.getMap().getId()).spawnAfterTimeGroup(-1, 1, true, -1);
			}
		}
		if (this.getType() == 2) {
			this.getPrism().setInFight(-2);
			for (final Player z : World.getOnlinePersos()) {
				if (z == null) {
					continue;
				}
				if (z.get_align() != this.getPrism().getAlignement()) {
					continue;
				}
				Prism.parseAttack(z);
				Prism.parseDefense(z);
			}
		}
		this.closeSheduler();
		if (this.getType() == 5) {
			this.getCollector().setInFight((byte) 2);
		}
		this.setState(3);
		this.setStartTime(System.currentTimeMillis());
		SocketManager.GAME_SEND_GAME_REMFLAG_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
				this.getInit0().getId());
		if (this.isHaveKnight() && this.getType() == 1) {
			this.addChevalier();
		}
		this.setCheckTimer(false);
		SocketManager.GAME_SEND_GIC_PACKETS_TO_FIGHT(this, 7);
		SocketManager.GAME_SEND_GS_PACKET_TO_FIGHT(this, 7);
		this.initOrderPlaying();
		this.setCurPlayer(-1);
		SocketManager.GAME_SEND_GTL_PACKET_TO_FIGHT(this, 7);
		SocketManager.GAME_SEND_GTM_PACKET_TO_FIGHT(this, 7);
		if (Main.modDebug) {
			Console.println("D\u00e9but d'un combat de type" + this.getType() + " et d'id " + this.getId(),
					Console.Color.INFORMATION);
		}
		if (this.getType() == 4 || this.getType() == 3) {
			boolean hasMale = false;
			boolean hasFemale = false;
			boolean hasDisciple = false;
			boolean hasCawotte = false;
			boolean hasChafer = false;
			boolean hasRoulette = false;
			boolean hasArakne = false;
			boolean hasArround = false;
			boolean ecartLvlPlayer = false;
			int hasBoss = -1;
			if (this.getTeam0().size() > 1) {
				int lowLvl1 = 201;
				int lowLvl2 = 201;
				for (final Fighter fighter : this.getTeam0().values()) {
					if (fighter.getLvl() < lowLvl1) {
						lowLvl1 = fighter.getLvl();
					}
				}
				for (final Fighter fighter : this.getTeam0().values()) {
					if (fighter.getLvl() < lowLvl2 && fighter.getLvl() > lowLvl1) {
						lowLvl2 = fighter.getLvl();
					}
				}
				if (lowLvl2 - lowLvl1 > 10) {
					ecartLvlPlayer = true;
				}
			}
			for (final Fighter f : this.getTeam0().values()) {
				final Player player2 = f.getPersonnage();
				if (f.getPersonnage() != null) {
					switch (player2.getClasse()) {
					case 1:
					case 2:
					case 4:
					case 5:
					case 10: {
						hasDisciple = true;
						break;
					}
					}
					player2.setOldMap(player2.getCurMap().getId());
					player2.setOldCell(player2.getCurCell().getId());
					if (player2.hasSpell(367)) {
						hasCawotte = true;
					}
					if (player2.hasSpell(373)) {
						hasChafer = true;
					}
					if (player2.hasSpell(101)) {
						hasRoulette = true;
					}
					if (player2.hasSpell(370)) {
						hasArakne = true;
					}
					if (player2.getSexe() == 0) {
						hasMale = true;
					}
					if (player2.getSexe() != 1) {
						continue;
					}
					hasFemale = true;
				}
			}
			final String boss = "58 85 86 107 113 121 147 173 180 225 226 230 232 251 252 257 289 295 374 375 377 382 404 423 430 457 478 568 605 612 669 670 673 675 677 681 780 792 797 799 800 827 854 926 939 940 943 1015 1027 1045 1051 1071 1072 1085 1086 1087 1159 1184 1185 1186 1187 1188";
			for (final Fighter fighter2 : this.getTeam1().values()) {
				if (fighter2.getMob() != null && fighter2.getMob().getTemplate() != null) {
					if (boss.contains(String.valueOf(fighter2.getMob().getTemplate().getId()))) {
						hasBoss = fighter2.getMob().getTemplate().getId();
					}
					for (final Fighter fighter3 : this.getTeam0().values()) {
						if (Pathfinding.getDistanceBetween(this.getMap(), fighter3.getCell().getId(),
								fighter2.getCell().getId()) >= 5) {
							hasArround = true;
						}
					}
				}
			}
			for (final Fighter fighter2 : this.getTeam1().values()) {
				if (fighter2.getMob() != null && fighter2.getMob().getTemplate() != null) {
					switch (fighter2.getMob().getTemplate().getId()) {
					case 98:
					case 111:
					case 120:
					case 171:
					case 200:
					case 382:
					case 473:
					case 582:
					case 666:
					case 794:
					case 796:
					case 800:
					case 801:
					case 803:
					case 805:
					case 806:
					case 807:
					case 808:
					case 841:
					case 847:
					case 868:
					case 970: {
						hasArround = false;
					}
					default: {
						continue;
					}
					}
				}
			}
			final boolean severalEnnemies = this.getTeam1().size() >= 2;
			final boolean severalAllies = this.getTeam0().size() >= 2;
			final boolean bothSexes = hasMale && hasFemale;
			final boolean EvenEnnemies = this.getTeam1().size() % 2 == 0;
			final boolean MoreEnnemies = this.getTeam1().size() >= this.getTeam0().size();
			final String challenges = World.getChallengeFromConditions(severalEnnemies, severalAllies, bothSexes,
					EvenEnnemies, MoreEnnemies, hasCawotte, hasChafer, hasRoulette, hasArakne, hasBoss, ecartLvlPlayer,
					hasArround, hasDisciple, this.getTeam0().size() != 1);
			final int challengeNumber = (this.getMapOld().hasEndFightAction(this.getType())
					|| Config.contains(Config.arenaMap, this.getMapOld().getId())) ? 2 : 1;
			for (final String chalInfos : World.getRandomChallenge(challengeNumber, challenges)) {
				final String[] chalInfo = chalInfos.split(",");
				final int challengeID = Integer.parseInt(chalInfo[0]);
				final int challengeXP = Integer.parseInt(chalInfo[1]);
				final int challengeDP = Integer.parseInt(chalInfo[2]);
				int bonusGroupe = Integer.parseInt(chalInfo[3]);
				bonusGroupe *= this.getTeam1().size();
				this.getAllChallenges().put(challengeID,
						new Challenge(this, challengeID, challengeXP + bonusGroupe, challengeDP + bonusGroupe));
			}
			for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				c.getValue().fightStart();
				SocketManager.GAME_SEND_CHALLENGE_FIGHT(this, 1, c.getValue().parseToPacket());
			}
		}
		for (final Fighter F : this.getFighters(3)) {
			final Player perso = F.getPersonnage();
			if (perso == null) {
				continue;
			}
			if (!perso.isOnMount()) {
				continue;
			}
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
					new StringBuilder(String.valueOf(perso.getId())).toString(),
					String.valueOf(perso.getId()) + "," + 11 + ",1");
		}
		this.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				Fight.this.startTurn();
				for (final Fighter F : Fight.this.getFighters(3)) {
					if (F == null) {
						continue;
					}
					Fight.this.getRholBack().put(F.getId(), F.getCell());
				}
				Fight.this.setBegin(true);
			}
		}, 1000L);
	}

	public void leftFight(final Player perso, final Player target) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		if (perso == null) {
			return;
		}
		final Fighter F = this.getFighterByPerso(perso);
		Fighter _t = null;
		if (target != null) {
			_t = this.getFighterByPerso(target);
		}
		final Fighter T = _t;
		if (target != null && T != null) {
			GameServer
					.addToLog("- Le personnage " + perso.getName() + " expulse " + T.getPersonnage().getName() + " !");
		} else {
			GameServer.addToLog("- Le personnage " + perso.getName() + " a quitt\u00e9 le combat !");
		}
		if (F != null) {
			switch (this.getType()) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5: {
				if (this.getState() >= 3) {
					this.onFighterDie(F, F);
					F.setLeft(true);
					if (current.getId() == F.getId()) {
						this.endTurn(false, F);
					}
					final Player P = F.getPersonnage();
					P.set_duelID(-1);
					P.set_ready(false);
					P.set_fight(null);
					P.set_away(false);
					this.verifIfTeamAllDead();
					break;
				}
				if (this.getState() == 2) {
					boolean isValid1 = false;
					if (T != null) {
						if (this.getInit0() != null && this.getInit0().getPersonnage() != null
								&& F.getPersonnage().getId() == this.getInit0().getPersonnage().getId()) {
							isValid1 = true;
						}
						if (this.getInit1() != null && this.getInit1().getPersonnage() != null
								&& F.getPersonnage().getId() == this.getInit1().getPersonnage().getId()) {
							isValid1 = true;
						}
					}
					if (isValid1) {
						if (T.getTeam() == F.getTeam() && T.getId() != F.getId()) {
							SocketManager.GAME_SEND_ON_FIGHTER_KICK(this, T.getPersonnage().getId(),
									this.getTeamId(T.getId()));
							if (this.getType() == 1 || this.getType() == 0 || this.getType() == 5 || this.getType() == 2
									|| this.getType() == 3) {
								SocketManager.GAME_SEND_ON_FIGHTER_KICK(this, T.getPersonnage().getId(),
										this.getOtherTeamId(T.getId()));
							}
							final Player P2 = T.getPersonnage();
							P2.set_duelID(-1);
							P2.set_ready(false);
							P2.set_fight(null);
							P2.set_away(false);
							if (P2.isOnline()) {
								P2.getWaiter().addNow(new Runnable() {
									@Override
									public void run() {
										SocketManager.GAME_SEND_GV_PACKET(P2);
									}
								}, 200L);
							}
							if (this.getTeam0().containsKey(T.getId())) {
								T.getCell().removeFighter(T);
								this.getTeam0().remove(T.getId());
							} else if (this.getTeam1().containsKey(T.getId())) {
								T.getCell().removeFighter(T);
								this.getTeam1().remove(T.getId());
							}
							for (final Player z : this.getMapOld().getPersos()) {
								FightStateAddFlag(this.getMapOld(), z);
							}
							break;
						}
						break;
					} else {
						if (T != null) {
							break;
						}
						boolean isValid2 = false;
						if (this.getInit0() != null && this.getInit0().getPersonnage() != null
								&& F.getPersonnage().getId() == this.getInit0().getPersonnage().getId()) {
							isValid2 = true;
						}
						if (this.getInit1() != null && this.getInit1().getPersonnage() != null
								&& F.getPersonnage().getId() == this.getInit1().getPersonnage().getId()) {
							isValid2 = true;
						}
						if (isValid2) {
							for (final Fighter f : this.getFighters(F.getTeam2())) {
								final Player P3 = f.getPersonnage();
								P3.set_duelID(-1);
								P3.set_ready(false);
								P3.set_fight(null);
								P3.set_away(false);
								f.setLeft(true);
								if (F.getPersonnage().getId() != f.getPersonnage().getId()) {
									if (!P3.isOnline()) {
										continue;
									}
									P3.getWaiter().addNow(new Runnable() {
										@Override
										public void run() {
											SocketManager.GAME_SEND_GV_PACKET(P3);
										}
									}, 200L);
								} else {
									if (this.getType() == 1 || this.getType() == 4 || this.getType() == 5
											|| this.getType() == 2) {
										final int EnergyLoos = Formulas.getLoosEnergy(P3.getLevel(),
												this.getType() == 1, this.getType() == 5);
										int Energy = P3.getEnergy() - EnergyLoos;
										if (Energy < 0) {
											Energy = 0;
										}
										P3.setEnergy(Energy);
										f.setLeft(true);
										if (P3.isOnline()) {
											SocketManager.GAME_SEND_Im_PACKET(P3, "034;" + EnergyLoos);
										}
										P3.setMascotte(0);
										if (F.getPersonnage().getObjetByPos(8) != null) {
											final org.aestia.object.Object obj = F.getPersonnage().getObjetByPos(8);
											if (obj != null) {
												final PetEntry pets = World.getPetsEntry(obj.getGuid());
												if (pets != null) {
													pets.looseFight(F.getPersonnage());
												}
											}
										}
										if (this.getType() == 1 || this.getType() == 2) {
											int honor = P3.get_honor() - 500;
											if (honor < 0) {
												honor = 0;
											}
											P3.set_honor(honor);
											if (P3.isOnline()) {
												SocketManager.GAME_SEND_Im_PACKET(P3, "076;" + honor);
											}
										}
										final int _energy = Energy;
										this.waiter.addNow(new Runnable() {
											@Override
											public void run() {
												if (_energy == 0) {
													if (Fight.this.getType() == 1) {
														int enemyFaction = 0;
														if (Fight.this.getTeam1().containsValue(F)) {
															for (final Fighter ennemy : Fight.this.getTeam0()
																	.values()) {
																if (ennemy.getPersonnage() != null
																		&& ennemy.getPersonnage().get_traque()
																				.getTraque() == F.getPersonnage()) {
																	enemyFaction = ennemy.getPersonnage().get_align();
																	break;
																}
															}
														} else {
															for (final Fighter ennemy : Fight.this.getTeam1()
																	.values()) {
																if (ennemy.getPersonnage() != null
																		&& ennemy.getPersonnage().get_traque()
																				.getTraque() == F.getPersonnage()) {
																	enemyFaction = ennemy.getPersonnage().get_align();
																	break;
																}
															}
														}
														P3.teleportFaction(enemyFaction);
													} else {
														P3.setFuneral();
													}
												} else {
													if (Fight.this.getType() == 1) {
														int enemyFaction = 0;
														if (Fight.this.getTeam1().containsValue(F)) {
															for (final Fighter ennemy : Fight.this.getTeam0()
																	.values()) {
																if (ennemy.getPersonnage() != null
																		&& ennemy.getPersonnage().get_traque()
																				.getTraque() == F.getPersonnage()) {
																	enemyFaction = ennemy.getPersonnage().get_align();
																	break;
																}
															}
														} else {
															for (final Fighter ennemy : Fight.this.getTeam1()
																	.values()) {
																if (ennemy.getPersonnage() != null
																		&& ennemy.getPersonnage().get_traque()
																				.getTraque() == F.getPersonnage()) {
																	enemyFaction = ennemy.getPersonnage().get_align();
																	break;
																}
															}
														}
														P3.teleportFaction(enemyFaction);
													} else {
														P3.warpToSavePos();
													}
													P3.setPdv(1);
												}
											}
										}, 1000L);
									}
									if (!P3.isOnline()) {
										continue;
									}
									P3.getWaiter().addNow(new Runnable() {
										@Override
										public void run() {
											SocketManager.GAME_SEND_GV_PACKET(P3);
										}
									}, 200L);
								}
							}
							if (this.getType() == 1 || this.getType() == 0 || this.getType() == 5
									|| this.getType() == 2) {
								for (final Fighter f : this.getFighters(F.getOtherTeam())) {
									if (f.getPersonnage() == null) {
										continue;
									}
									final Player P3 = f.getPersonnage();
									P3.set_duelID(-1);
									P3.set_ready(false);
									P3.set_fight(null);
									P3.set_away(false);
									f.hasLeft();
									if (!P3.isOnline()) {
										continue;
									}
									P3.getWaiter().addNow(new Runnable() {
										@Override
										public void run() {
											SocketManager.GAME_SEND_GV_PACKET(P3);
										}
									}, 200L);
								}
							}
							this.setState(4);
							World.getMap(this.getMap().getId()).removeFight(this.getId());
							SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(World.getMap(this.getMap().getId()));
							SocketManager.GAME_SEND_GAME_REMFLAG_PACKET_TO_MAP(this.getMapOld(),
									this.getInit0().getId());
							if (this.getType() == 5) {
								for (final Player z : World.getGuild(this.getGuildId()).getMembers()) {
									if (z == null) {
										continue;
									}
									if (!z.isOnline()) {
										continue;
									}
									SocketManager.GAME_SEND_gITM_PACKET(z,
											Collector.parseToGuild(z.get_guild().getId()));
									SocketManager.GAME_SEND_MESSAGE(z, "Votre percepteur remporte la victioire.");
								}
								this.getCollector().setInFight((byte) 0);
								this.getCollector().set_inFightID(-1);
								for (final Player z : World.getMap(this.getCollector().getMap()).getPersos()) {
									if (z != null) {
										SocketManager.GAME_SEND_MAP_PERCO_GMS_PACKETS(z.getGameClient(), z.getCurMap());
									}
								}
							}
							this.setMap(null);
							this.orderPlaying = null;
							break;
						}
						SocketManager.GAME_SEND_ON_FIGHTER_KICK(this, F.getPersonnage().getId(),
								this.getTeamId(F.getId()));
						if (this.getType() == 1 || this.getType() == 0 || this.getType() == 5 || this.getType() == 2) {
							SocketManager.GAME_SEND_ON_FIGHTER_KICK(this, F.getPersonnage().getId(),
									this.getOtherTeamId(F.getId()));
						}
						final Player P4 = F.getPersonnage();
						P4.set_duelID(-1);
						P4.set_ready(false);
						P4.set_fight(null);
						P4.set_away(false);
						F.setLeft(true);
						F.hasLeft();
						if (this.getType() == 1 || this.getType() == 4 || this.getType() == 5 || this.getType() == 2
								|| this.getType() == 3) {
							final int EnergyLoos2 = Formulas.getLoosEnergy(P4.getLevel(), this.getType() == 1,
									this.getType() == 5);
							int Energy2 = P4.getEnergy() - EnergyLoos2;
							if (Energy2 < 0) {
								Energy2 = 0;
							}
							P4.setEnergy(Energy2);
							if (P4.isOnline()) {
								SocketManager.GAME_SEND_Im_PACKET(P4, "034;" + EnergyLoos2);
							}
							if (F.getPersonnage().getObjetByPos(8) != null) {
								final org.aestia.object.Object obj2 = F.getPersonnage().getObjetByPos(8);
								if (obj2 != null) {
									final PetEntry pets2 = World.getPetsEntry(obj2.getGuid());
									if (pets2 != null) {
										pets2.looseFight(F.getPersonnage());
									}
								}
							}
							P4.setMascotte(0);
							if (this.getType() == 1 || this.getType() == 2) {
								int honor2 = P4.get_honor() - 500;
								if (honor2 < 0) {
									honor2 = 0;
								}
								P4.set_honor(honor2);
								if (P4.isOnline()) {
									SocketManager.GAME_SEND_Im_PACKET(P4, "076;" + honor2);
								}
							}
							final int _energy2 = Energy2;
							this.waiter.addNow(new Runnable() {
								@Override
								public void run() {
									if (_energy2 == 0) {
										if (Fight.this.getType() == 1) {
											int enemyFaction = 0;
											if (Fight.this.getTeam1().containsValue(F)) {
												for (final Fighter ennemy : Fight.this.getTeam0().values()) {
													if (ennemy.getPersonnage() != null && ennemy.getPersonnage()
															.get_traque().getTraque() == F.getPersonnage()) {
														enemyFaction = ennemy.getPersonnage().get_align();
														break;
													}
												}
											} else {
												for (final Fighter ennemy : Fight.this.getTeam1().values()) {
													if (ennemy.getPersonnage() != null && ennemy.getPersonnage()
															.get_traque().getTraque() == F.getPersonnage()) {
														enemyFaction = ennemy.getPersonnage().get_align();
														break;
													}
												}
											}
											P4.teleportFaction(enemyFaction);
										} else {
											P4.setFuneral();
										}
									} else {
										if (Fight.this.getType() == 1) {
											int enemyFaction = 0;
											if (Fight.this.getTeam1().containsValue(F)) {
												for (final Fighter ennemy : Fight.this.getTeam0().values()) {
													if (ennemy.getPersonnage() != null && ennemy.getPersonnage()
															.get_traque().getTraque() == F.getPersonnage()) {
														enemyFaction = ennemy.getPersonnage().get_align();
														break;
													}
												}
											} else {
												for (final Fighter ennemy : Fight.this.getTeam1().values()) {
													if (ennemy.getPersonnage() != null && ennemy.getPersonnage()
															.get_traque().getTraque() == F.getPersonnage()) {
														enemyFaction = ennemy.getPersonnage().get_align();
														break;
													}
												}
											}
											P4.teleportFaction(enemyFaction);
										} else if (Fight.this.getType() != 5) {
											P4.warpToSavePos();
										} else if (!P4.getCurMap().hasEndFightAction(0)) {
											P4.teleportOldMap();
										}
										P4.setPdv(1);
									}
								}
							}, 1000L);
						}
						if (P4.isOnline()) {
							P4.getWaiter().addNow(new Runnable() {
								@Override
								public void run() {
									SocketManager.GAME_SEND_GV_PACKET(P4);
								}
							}, 200L);
						}
						if (this.getTeam0().containsKey(F.getId())) {
							F.getCell().removeFighter(F);
							this.getTeam0().remove(F.getId());
						} else if (this.getTeam1().containsKey(F.getId())) {
							F.getCell().removeFighter(F);
							this.getTeam1().remove(F.getId());
						}
						for (final Player z2 : this.getMapOld().getPersos()) {
							FightStateAddFlag(this.getMapOld(), z2);
						}
						break;
					}
				} else {
					if (Main.modDebug) {
						GameServer.addToLog("- Phase de combat non g\u00e9r\u00e9e sur le type : " + this.getType()
								+ " T:" + T + " F:" + F);
						break;
					}
					break;
				}
			}
			default: {
				if (Main.modDebug) {
					GameServer.addToLog("- Phase de combat non g\u00e9r\u00e9e sur le type : " + this.getType() + " T:"
							+ T + " F:" + F);
					break;
				}
				break;
			}
			}
			if (T == null) {
				if (F.getPersonnage().getMorphMode() && F.getPersonnage().donjon) {
					F.getPersonnage().unsetFullMorph();
				}
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						if (Fight.this.getTeam0().containsKey(F.getId())) {
							F.getCell().removeFighter(F);
							Fight.this.getTeam0().remove(F.getId());
						} else if (Fight.this.getTeam1().containsKey(F.getId())) {
							F.getCell().removeFighter(F);
							Fight.this.getTeam1().remove(F.getId());
						}
						SocketManager.GAME_SEND_GV_PACKET(F.getPersonnage());
					}
				}, 3000L);
			}
			if (T != null) {
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						if (Fight.this.getTeam0().containsKey(T.getId())) {
							T.getCell().removeFighter(T);
							Fight.this.getTeam0().remove(T.getId());
						} else if (Fight.this.getTeam1().containsKey(T.getId())) {
							T.getCell().removeFighter(T);
							Fight.this.getTeam1().remove(T.getId());
						}
					}
				}, 1000L);
			}
		} else {
			SocketManager.GAME_SEND_GV_PACKET(perso);
			this.getViewer().remove(perso.getId());
			perso.set_fight(null);
			perso.set_away(false);
		}
	}

	public void endFight(final boolean b) {
		if (this.launchTime > 1L) {
			return;
		}
		if (b) {
			for (final Fighter F : this.getTeam1().values()) {
				try {
					if (F == null) {
						continue;
					}
					F.setIsDead(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.verifIfTeamAllDead();
		} else if (!b) {
			for (final Fighter F : this.getTeam0().values()) {
				try {
					if (F == null) {
						continue;
					}
					F.setIsDead(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.verifIfTeamAllDead();
		}
	}

	public void startTurn() {
		if (!this.verifyStillInFight()) {
			this.verifIfTeamAllDead();
		}
		if (this.getState() >= 4) {
			return;
		}
		this.setCurPlayer(this.getCurPlayer() + 1);
		this.setCurAction("");
		if (this.getCurPlayer() >= this.getOrderPlayingSize()) {
			this.setCurPlayer(0);
		}
		Fighter current = this.getFighterByOrdreJeu();
		this.setCurFighterPa(current.getPa());
		this.setCurFighterPm(current.getPm());
		this.setCurFighterUsedPa(0);
		this.setCurFighterUsedPm(0);
		current.refreshStartfightBuff();
		if (current.isDeconnected()) {
			current.setTurnRemaining();
			if (current.getTurnRemaining() > 0) {
				SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 7,
						"0162;" + current.getPacketsName() + "~" + current.getTurnRemaining());
				final Fighter c = current;
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						Fight.this.endTurn(false, c);
					}
				}, 400L);
				return;
			}
			if (current.getPersonnage() != null) {
				this.leftFight(current.getPersonnage(), null);
				current.getPersonnage().disconnectInFight();
			} else {
				this.onFighterDie(current, current);
				current.setLeft(true);
			}
		}
		if (current.hasLeft() || current.isDead()) {
			if (Main.modDebug) {
				Console.println("Le fighter " + this.getCurPlayer() + " est mort dans le combat " + this.getId()
						+ " de type " + this.getType() + " !", Console.Color.INFORMATION);
			}
			final Fighter c = current;
			this.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					Fight.this.endTurn(false, c);
				}
			}, 400L);
			return;
		}
		current = this.getFighterByOrdreJeu();
		current.applyBeginningTurnBuff(this);
		current = this.getFighterByOrdreJeu();
		if (this.getState() == 4) {
			return;
		}
		if (current.getPdv() <= 0) {
			this.onFighterDie(current, this.getInit0());
			this.endTurn(false, current);
			return;
		}
		current.refreshLaunchedSort();
		current.getChatiValue().clear();
		current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		if (current.isDead() && !current.isInvocation()) {
			if (Main.modDebug) {
				Console.println("Le fighter " + this.getCurPlayer() + " est mort dans le combat " + this.getId()
						+ " de type " + this.getType() + " !", Console.Color.INFORMATION);
			}
			this.endTurn(false, current);
			return;
		}
		if (current.getPersonnage() != null) {
			SocketManager.GAME_SEND_STATS_PACKET(current.getPersonnage());
		}
		if (current.hasBuff(140)) {
			if (Main.modDebug) {
				Console.println("Le fighter " + this.getCurPlayer() + " passe son tour dans le combat " + this.getId()
						+ " de type " + this.getType() + " !", Console.Color.INFORMATION);
			}
			this.endTurn(false, current);
			return;
		}
		if (Main.modDebug) {
			Console.println("Le fighter " + this.getCurPlayer() + " d\u00e9bute son tour dans le combat " + this.getId()
					+ " de type " + this.getType() + " !", Console.Color.INFORMATION);
		}
		SocketManager.GAME_SEND_GAMETURNSTART_PACKET_TO_FIGHT(this, 7, current.getId(), 30000);
		final Fighter _current = current;
		this.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				_current.setCanPlay(true);
				Fight.access$1(Fight.this, new Turn(Fight.this, _current));
				final ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
				glyphs.addAll(Fight.this.getAllGlyphs());
				for (final Glyph g : glyphs) {
					if (Fight.this.getState() >= 4) {
						return;
					}
					if (g.getCaster().getId() == _current.getId() && g.decrementDuration() == 0) {
						Fight.this.getAllGlyphs().remove(g);
						g.desapear();
					} else {
						final int dist = Pathfinding.getDistanceBetween(Fight.this.getMap(), _current.getCell().getId(),
								g.getCell().getId());
						if (dist > g.getSize() || g.getSpell() == 476) {
							continue;
						}
						g.onTraped(_current);
					}
				}
				if ((Fight.this.getType() == 4 && Fight.this.getAllChallenges().size() > 0 && !_current.isInvocation()
						&& !_current.isDouble() && !_current.isCollector())
						|| (Fight.this.getType() == 3 && Fight.this.getAllChallenges().size() > 0
								&& !_current.isInvocation() && !_current.isDouble() && !_current.isCollector())) {
					for (final Map.Entry<Integer, Challenge> c : Fight.this.getAllChallenges().entrySet()) {
						if (c.getValue() == null) {
							continue;
						}
						c.getValue().onPlayerStartTurn(_current);
					}
				}
				if (_current.getPersonnage() == null || _current.getDouble() != null
						|| _current.getCollector() != null) {
					new IAThread(_current, Fight.this);
				}
			}
		}, 500L);
	}

	public synchronized void endTurn(final boolean onAction, final Fighter f) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		if (f == current) {
			this.endTurn(onAction);
		}
	}

	public synchronized void endTurn(final boolean onAction) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		if (this.waiterLancer) {
			return;
		}
		try {
			if (this.getState() >= 4) {
				return;
			}
			if (current.hasLeft() || current.isDead()) {
				this.startTurn();
				return;
			}
			if (!this.getCurAction().equals("") && current.getPersonnage() != null) {
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						Fight.this.endTurn(onAction, current);
					}
				}, 100L);
				return;
			}
			if (this.turn != null) {
				this.turn.stop();
			}
			SocketManager.GAME_SEND_GAMETURNSTOP_PACKET_TO_FIGHT(this, 7, current.getId());
			current.setCanPlay(false);
			this.setCurAction("");
			if (onAction) {
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						for (final SpellEffect SE : current.getBuffsByEffectID(131)) {
							final int pas = SE.getValue();
							int val = -1;
							try {
								val = Integer.parseInt(SE.getArgs().split(";")[1]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (val == -1) {
								continue;
							}
							final int nbr = (int) Math.floor(Fight.this.getCurFighterUsedPa() / pas);
							int dgt = val * nbr;
							if (SE.getSpell() == 200) {
								int inte = SE.getCaster().getTotalStats().getEffect(126);
								if (inte < 0) {
									inte = 0;
								}
								int pdom = SE.getCaster().getTotalStats().getEffect(138);
								if (pdom < 0) {
									pdom = 0;
								}
								dgt *= (100 + inte + pdom) / 100;
							}
							if (current.hasBuff(184)) {
								SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 7, 105,
										new StringBuilder(String.valueOf(current.getId())).toString(),
										String.valueOf(current.getId()) + "," + current.getBuff(184).getValue());
								dgt -= current.getBuff(184).getValue();
							}
							if (current.hasBuff(105)) {
								SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 7, 105,
										new StringBuilder(String.valueOf(current.getId())).toString(),
										String.valueOf(current.getId()) + "," + current.getBuff(105).getValue());
								dgt -= current.getBuff(105).getValue();
							}
							if (dgt <= 0) {
								continue;
							}
							if (dgt > current.getPdv()) {
								dgt = current.getPdv();
							}
							current.removePdv(current, dgt);
							dgt = -dgt;
							SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 7, 100,
									new StringBuilder(String.valueOf(SE.getCaster().getId())).toString(),
									String.valueOf(current.getId()) + "," + dgt);
						}
						final ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
						glyphs.addAll(Fight.this.getAllGlyphs());
						for (final Glyph g : glyphs) {
							if (Fight.this.getState() >= 4) {
								return;
							}
							final int dist = Pathfinding.getDistanceBetween(Fight.this.getMap(),
									current.getCell().getId(), g.getCell().getId());
							if (dist > g.getSize() || g.getSpell() != 476) {
								continue;
							}
							g.onTraped(current);
						}
						if (current.getPdv() <= 0) {
							Fight.this.onFighterDie(current, Fight.this.getInit0());
						}
						if ((Fight.this.getType() == 4 && Fight.this.getAllChallenges().size() > 0
								&& !current.isInvocation() && !current.isDouble() && !current.isCollector()
								&& current.getTeam() == 0)
								|| (Fight.this.getType() == 3 && Fight.this.getAllChallenges().size() > 0
										&& !current.isInvocation() && !current.isDouble() && !current.isCollector()
										&& current.getTeam() == 0)) {
							for (final Map.Entry<Integer, Challenge> c : Fight.this.getAllChallenges().entrySet()) {
								if (c.getValue() == null) {
									continue;
								}
								c.getValue().onPlayerEndTurn(current);
							}
						}
						Fight.this.setCurFighterUsedPa(0);
						Fight.this.setCurFighterUsedPm(0);
						Fight.this.setCurFighterPa(current.getTotalStats().getEffect(111));
						Fight.this.setCurFighterPm(current.getTotalStats().getEffect(128));
						current.refreshEndfightBuff();
						if (current.getPersonnage() != null && current.getPersonnage().isOnline()) {
							SocketManager.GAME_SEND_STATS_PACKET(current.getPersonnage());
						}
						SocketManager.GAME_SEND_GTM_PACKET_TO_FIGHT(Fight.this, 7);
						SocketManager.GAME_SEND_GTR_PACKET_TO_FIGHT(Fight.this, 7, current.getId());
						if (Main.modDebug) {
							Console.println(
									"Le fighter " + Fight.this.getCurPlayer() + " met fin a son tour dans le combat "
											+ Fight.this.getId() + " de type " + Fight.this.getType() + " !",
									Console.Color.INFORMATION);
						}
					}
				}, 2100L);
			} else {
				for (final SpellEffect SE : current.getBuffsByEffectID(131)) {
					final int pas = SE.getValue();
					int val = -1;
					try {
						val = Integer.parseInt(SE.getArgs().split(";")[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (val == -1) {
						continue;
					}
					final int nbr = (int) Math.floor(this.getCurFighterUsedPa() / pas);
					int dgt = val * nbr;
					if (SE.getSpell() == 200) {
						int inte = SE.getCaster().getTotalStats().getEffect(126);
						if (inte < 0) {
							inte = 0;
						}
						int pdom = SE.getCaster().getTotalStats().getEffect(138);
						if (pdom < 0) {
							pdom = 0;
						}
						dgt *= (100 + inte + pdom) / 100;
					}
					if (current.hasBuff(184)) {
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 105,
								new StringBuilder(String.valueOf(current.getId())).toString(),
								String.valueOf(current.getId()) + "," + current.getBuff(184).getValue());
						dgt -= current.getBuff(184).getValue();
					}
					if (current.hasBuff(105)) {
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 105,
								new StringBuilder(String.valueOf(current.getId())).toString(),
								String.valueOf(current.getId()) + "," + current.getBuff(105).getValue());
						dgt -= current.getBuff(105).getValue();
					}
					if (dgt <= 0) {
						continue;
					}
					if (dgt > current.getPdv()) {
						dgt = current.getPdv();
					}
					current.removePdv(current, dgt);
					dgt = -dgt;
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 100,
							new StringBuilder(String.valueOf(SE.getCaster().getId())).toString(),
							String.valueOf(current.getId()) + "," + dgt);
				}
				final ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
				glyphs.addAll(this.getAllGlyphs());
				for (final Glyph g : glyphs) {
					if (this.getState() >= 4) {
						return;
					}
					final int dist = Pathfinding.getDistanceBetween(this.getMap(), current.getCell().getId(),
							g.getCell().getId());
					if (dist > g.getSize() || g.getSpell() != 476) {
						continue;
					}
					g.onTraped(current);
				}
				if (current.getPdv() <= 0) {
					this.onFighterDie(current, this.getInit0());
				}
				if ((this.getType() == 4 && this.getAllChallenges().size() > 0 && !current.isInvocation()
						&& !current.isDouble() && !current.isCollector() && current.getTeam() == 0)
						|| (this.getType() == 3 && this.getAllChallenges().size() > 0 && !current.isInvocation()
								&& !current.isDouble() && !current.isCollector() && current.getTeam() == 0)) {
					for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
						if (c.getValue() == null) {
							continue;
						}
						c.getValue().onPlayerEndTurn(current);
					}
				}
				this.setCurFighterUsedPa(0);
				this.setCurFighterUsedPm(0);
				this.setCurFighterPa(current.getTotalStats().getEffect(111));
				this.setCurFighterPm(current.getTotalStats().getEffect(128));
				current.refreshEndfightBuff();
				if (current.getPersonnage() != null && current.getPersonnage().isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(current.getPersonnage());
				}
				SocketManager.GAME_SEND_GTM_PACKET_TO_FIGHT(this, 7);
				SocketManager.GAME_SEND_GTR_PACKET_TO_FIGHT(this, 7, current.getId());
				if (Main.modDebug) {
					Console.println("Le fighter " + this.getCurPlayer() + " met fin a son tour dans le combat "
							+ this.getId() + " de type " + this.getType() + " !", Console.Color.INFORMATION);
				}
			}
			this.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					Fight.this.startTurn();
				}
			}, 1000L);
		} catch (NullPointerException e2) {
			e2.printStackTrace();
			Console.println("Erreur de fin de tour : " + e2.getMessage(), Console.Color.ERROR);
			try {
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						Fight.this.endTurn(false);
					}
				}, 1000L);
			} catch (Exception e3) {
				e3.printStackTrace();
				this.verifIfTeamAllDead();
			}
		}
	}

	public void playerPass(final Player perso) {
		final Fighter f = this.getFighterByPerso(perso);
		if (f == null) {
			return;
		}
		if (!f.canPlay()) {
			return;
		}
		if (!this.getCurAction().equalsIgnoreCase("")) {
			return;
		}
		this.endTurn(false, f);
	}

	public void joinFight(final Player perso, final int guid) {
		final long timeRestant = 45000L - (System.currentTimeMillis() - this.launchTime);
		Fighter currentJoin = null;
		if (perso.isDead() == 1) {
			return;
		}
		if (this.isBegin()) {
			return;
		}
		if (perso.get_fight() != null) {
			return;
		}
		if (this.getTeam0().containsKey(guid)) {
			final Case cell = this.getRandomCell(this.getStart0());
			if (cell == null) {
				return;
			}
			if (this.getType() == 1) {
				boolean multiIp = false;
				for (final Fighter f : this.getTeam0().values()) {
					if (perso.getAccount().getCurIP().compareTo(f.getPersonnage().getAccount().getCurIP()) == 0) {
						multiIp = true;
					}
				}
				if (multiIp) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Impossible de rejoindre ce combat, vous \u00eates d\u00e9j\u00e0 dans le combat avec une m\u00eame IP !");
					return;
				}
			}
			if (this.isOnlyGroup0()) {
				final Group g = this.getInit0().getPersonnage().getGroup();
				if (g != null && !g.getPersos().contains(perso)) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
					return;
				}
			}
			if (this.getType() == 1) {
				if (perso.get_align() == -1) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
					return;
				}
				if (this.getInit0().getPersonnage().get_align() != perso.get_align()) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
					return;
				}
			}
			if (this.getType() == 2) {
				if (perso.get_align() == -1) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'a', guid);
					return;
				}
				if (this.getInit0().getPrism().getAlignement() != perso.get_align()) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'a', guid);
					return;
				}
				perso.toggleWings('+');
			}
			if (this.getGuildId() > -1 && perso.get_guild() != null && this.getGuildId() == perso.get_guild().getId()) {
				SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
				return;
			}
			if (this.isLocked0()) {
				SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
				return;
			}
			if (this.getTeam0().size() >= 8) {
				return;
			}
			if (this.getType() == 0) {
				SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 1, 1, 0, timeRestant, this.getType());
			} else {
				SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 0, 1, 0, timeRestant, this.getType());
			}
			SocketManager.GAME_SEND_FIGHT_PLACES_PACKET(perso.getGameClient(), this.getMap().getPlaces(),
					this.getSt1());
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
					new StringBuilder(String.valueOf(perso.getId())).toString(),
					String.valueOf(perso.getId()) + "," + 8 + ",0");
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
					new StringBuilder(String.valueOf(perso.getId())).toString(),
					String.valueOf(perso.getId()) + "," + 3 + ",0");
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
			SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, this.getMap().getCases().values());
			final Fighter f2 = currentJoin = new Fighter(this, perso);
			f2.setTeam(0);
			this.getTeam0().put(perso.getId(), f2);
			perso.set_fight(this);
			f2.setCell(cell);
			f2.getCell().addFighter(f2);
		} else if (this.getTeam1().containsKey(guid)) {
			final Case cell = this.getRandomCell(this.getStart1());
			if (cell == null) {
				return;
			}
			if (this.getType() == 1) {
				boolean multiIp = false;
				for (final Fighter f : this.getTeam1().values()) {
					if (perso.getAccount().getCurIP().compareTo(f.getPersonnage().getAccount().getCurIP()) == 0) {
						multiIp = true;
					}
				}
				if (multiIp) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Impossible de rejoindre ce combat, vous \u00eates d\u00e9j\u00e0 dans le combat avec une m\u00eame IP !");
					return;
				}
			}
			if (this.isOnlyGroup1()) {
				final Group g = this.getInit1().getPersonnage().getGroup();
				if (g != null && !g.getPersos().contains(perso)) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
					return;
				}
			}
			if (this.getType() == 1) {
				if (perso.get_align() == -1) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
					return;
				}
				if (this.getInit1().getPersonnage().get_align() != perso.get_align()) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
					return;
				}
			}
			if (this.getType() == 2) {
				if (perso.get_align() == -1) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'a', guid);
					return;
				}
				if (this.getInit1().getPrism().getAlignement() != perso.get_align()) {
					SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'a', guid);
					return;
				}
				perso.toggleWings('+');
			}
			if (this.getGuildId() > -1 && perso.get_guild() != null && this.getGuildId() == perso.get_guild().getId()) {
				SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
				return;
			}
			if (this.isLocked1()) {
				SocketManager.GAME_SEND_GA903_ERROR_PACKET(perso.getGameClient(), 'f', guid);
				return;
			}
			if (this.getTeam1().size() >= 8) {
				return;
			}
			if (this.getType() == 0) {
				SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 1, 1, 0, 0, this.getType());
			} else {
				SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 0, 1, 0, 0, this.getType());
			}
			SocketManager.GAME_SEND_FIGHT_PLACES_PACKET(perso.getGameClient(), this.getMap().getPlaces(),
					this.getSt2());
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
					new StringBuilder(String.valueOf(perso.getId())).toString(),
					String.valueOf(perso.getId()) + "," + 8 + ",0");
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
					new StringBuilder(String.valueOf(perso.getId())).toString(),
					String.valueOf(perso.getId()) + "," + 3 + ",0");
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
			final Fighter f2 = currentJoin = new Fighter(this, perso);
			f2.setTeam(1);
			this.getTeam1().put(perso.getId(), f2);
			perso.set_fight(this);
			f2.setCell(cell);
			f2.getCell().addFighter(f2);
		}
		this.demorph(perso);
		perso.getCurCell().removeCharacter(perso.getId());
		SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(perso.getCurMap(),
				((currentJoin.getTeam() == 0) ? this.getInit0() : this.getInit1()).getId(), currentJoin);
		SocketManager.GAME_SEND_FIGHT_PLAYER_JOIN(this, 7, currentJoin);
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS(this, this.getMap(), perso);
		if (this.getCollector() != null) {
			for (final Player z : World.getGuild(this.getGuildId()).getMembers()) {
				if (z.isOnline()) {
					Collector.parseAttaque(z, this.getGuildId());
					Collector.parseDefense(z, this.getGuildId());
				}
			}
		}
		if (this.getPrism() != null) {
			for (final Player z : World.getOnlinePersos()) {
				if (z == null) {
					continue;
				}
				if (z.get_align() != this.getPrism().getAlignement()) {
					continue;
				}
				Prism.parseAttack(perso);
			}
		}
	}

	public void joinCollectorFight(final Player perso, final int guid, final int percoID) {
		final Case cell = this.getRandomCell(this.getStart1());
		if (cell == null) {
			return;
		}
		perso.getWaiter().addNow(new Runnable() {
			@Override
			public void run() {
				SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 0, 1, 0, 0, Fight.this.getType());
				SocketManager.GAME_SEND_FIGHT_PLACES_PACKET(perso.getGameClient(), Fight.this.getMap().getPlaces(),
						Fight.this.getSt2());
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 3, 950,
						new StringBuilder(String.valueOf(perso.getId())).toString(),
						String.valueOf(perso.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 3, 950,
						new StringBuilder(String.valueOf(perso.getId())).toString(),
						String.valueOf(perso.getId()) + "," + 3 + ",0");
				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
				final Fighter f = new Fighter(Fight.this, perso);
				f.setTeam(1);
				Fight.this.getTeam1().put(perso.getId(), f);
				perso.set_fight(Fight.this);
				f.setCell(cell);
				f.getCell().addFighter(f);
				perso.getCurCell().removeCharacter(perso.getId());
				SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(perso.getCurMap(), percoID, f);
				SocketManager.GAME_SEND_FIGHT_PLAYER_JOIN(Fight.this, 7, f);
				SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS(Fight.this, Fight.this.getMap(), perso);
				SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, Fight.this.getMap().getCases().values());
			}
		}, 700L);
	}

	public void joinPrismFight(final Player perso, final int id, final int PrismeID) {
		final Case cell = this.getRandomCell(this.start1);
		if (cell == null) {
			return;
		}
		perso.getWaiter().addNow(new Runnable() {
			@Override
			public void run() {
				SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 0, 1, 0, 0, Fight.this.getType());
				SocketManager.GAME_SEND_FIGHT_PLACES_PACKET(perso.getGameClient(), Fight.this.getMap().getPlaces(),
						Fight.this.getSt2());
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 3, 950,
						new StringBuilder(String.valueOf(perso.getId())).toString(),
						String.valueOf(perso.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 3, 950,
						new StringBuilder(String.valueOf(perso.getId())).toString(),
						String.valueOf(perso.getId()) + "," + 3 + ",0");
				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
				final Fighter f = new Fighter(Fight.this, perso);
				f.setTeam(1);
				Fight.this.getTeam1().put(perso.getId(), f);
				perso.set_fight(Fight.this);
				f.setCell(cell);
				Fight.this.demorph(perso);
				f.getCell().addFighter(f);
				perso.getCurCell().removeCharacter(perso.getId());
				SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(perso.getCurMap(), PrismeID, f);
				SocketManager.GAME_SEND_FIGHT_PLAYER_JOIN(Fight.this, 7, f);
				SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS(Fight.this, Fight.this.getMap(), perso);
				SocketManager.GAME_SEND_GDF_PACKET_TO_FIGHT(perso, Fight.this.getMap().getCases().values());
			}
		}, 700L);
	}

	public void joinAsSpect(final Player p) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		if (!this.isBegin() || p.get_fight() != null) {
			SocketManager.GAME_SEND_Im_PACKET(p, "157");
			return;
		}
		if (p.getGroupe() == null && (!this.isViewerOk() || this.getState() != 3)) {
			SocketManager.GAME_SEND_Im_PACKET(p, "157");
			return;
		}
		this.demorph(p);
		p.getCurCell().removeCharacter(p.getId());
		SocketManager.GAME_SEND_GJK_PACKET(p, this.getState(), 0, 0, 1, 0, this.getType());
		SocketManager.GAME_SEND_GS_PACKET(p);
		SocketManager.GAME_SEND_GTL_PACKET(p, this);
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(p.getCurMap(), p.getId());
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS(this, this.getMap(), p);
		SocketManager.GAME_SEND_GAMETURNSTART_PACKET(p, current.getId(), 30000);
		this.getViewer().put(p.getId(), p);
		p.setSpec(true);
		p.set_fight(this);
		final ArrayList<Fighter> all = new ArrayList<Fighter>();
		all.addAll(this.getTeam0().values());
		all.addAll(this.getTeam1().values());
		for (final Fighter f : all) {
			if (f.isHide()) {
				SocketManager.GAME_SEND_GA_PACKET(this, p, 150, new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + ",4");
			}
		}
		if (p.getGroupe() == null) {
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 7, "036;" + p.getName());
		}
		if ((this.getType() == 4 && this.getAllChallenges().size() > 0)
				|| (this.getType() == 3 && this.getAllChallenges().size() > 0)) {
			for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				SocketManager.GAME_SEND_CHALLENGE_PERSO(p, c.getValue().parseToPacket());
				if (c.getValue().loose()) {
					continue;
				}
				c.getValue().challengeSpecLoose(p);
			}
		}
	}

	public void toggleLockTeam(final int guid) {
		if (this.getInit0() != null && this.getInit0().getId() == guid) {
			this.setLocked0(!this.isLocked0());
			SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					this.isLocked0() ? '+' : '-', 'A', guid);
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 1, this.isLocked0() ? "095" : "096");
		} else if (this.getInit1() != null && this.getInit1().getId() == guid) {
			this.setLocked1(!this.isLocked1());
			SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit1().getPersonnage().getCurMap(),
					this.isLocked1() ? '+' : '-', 'A', guid);
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 2, this.isLocked1() ? "095" : "096");
		}
	}

	public void toggleLockSpec(final int guid) {
		if ((this.getInit0() != null && this.getInit0().getId() == guid)
				|| (this.getInit1() != null && this.getInit1().getId() == guid)) {
			this.setViewerOk(!this.isViewerOk());
			if (!this.isViewerOk()) {
				for (final Map.Entry<Integer, Player> spectateur : this.getViewer().entrySet()) {
					final Player perso = spectateur.getValue();
					if (perso.getGroupe() == null) {
						SocketManager.GAME_SEND_GV_PACKET(perso);
						this.getViewer().remove(perso.getId());
						perso.set_fight(null);
						perso.set_away(false);
						perso.setSpec(false);
					}
				}
				SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
						this.isViewerOk() ? '+' : '-', 'S', this.getInit0().getId());
				if (this.getInit1() != null) {
					SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(
							this.getInit0().getPersonnage().getCurMap(), this.isViewerOk() ? '+' : '-', 'S',
							this.getInit1().getId());
				}
				SocketManager.GAME_SEND_Im_PACKET_TO_MAP(this.getMap(), this.isViewerOk() ? "039" : "040");
			}
		}
	}

	public void toggleOnlyGroup(final int guid) {
		if (this.getInit0() != null && this.getInit0().getId() == guid) {
			this.setOnlyGroup0(!this.isOnlyGroup0());
			SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					this.isOnlyGroup0() ? '+' : '-', 'P', guid);
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 1, this.isOnlyGroup0() ? "093" : "094");
		} else if (this.getInit1() != null && this.getInit1().getId() == guid) {
			this.setOnlyGroup1(!this.isOnlyGroup1());
			SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit1().getPersonnage().getCurMap(),
					this.isOnlyGroup1() ? '+' : '-', 'P', guid);
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 2, this.isOnlyGroup1() ? "095" : "096");
		}
	}

	public void toggleHelp(final int guid) {
		if (this.getInit0() != null && this.getInit0().getId() == guid) {
			this.setHelp0(!this.isHelp0());
			SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
					this.isHelp0() ? '+' : '-', 'H', guid);
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 1, this.isHelp0() ? "0103" : "0104");
		} else if (this.getInit1() != null && this.getInit1().getId() == guid) {
			this.setHelp1(!this.isHelp1());
			SocketManager.GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(this.getInit1().getPersonnage().getCurMap(),
					this.isHelp1() ? '+' : '-', 'H', guid);
			SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 2, this.isHelp1() ? "0103" : "0104");
		}
	}

	public void showCaseToTeam(final int guid, final int cellID) {
		final int teams = this.getTeamId(guid) - 1;
		if (teams == 4) {
			return;
		}
		final ArrayList<GameClient> PWs = new ArrayList<GameClient>();
		if (teams == 0) {
			for (final Map.Entry<Integer, Fighter> e : this.getTeam0().entrySet()) {
				if (e.getValue().getPersonnage() != null && e.getValue().getPersonnage().getGameClient() != null) {
					PWs.add(e.getValue().getPersonnage().getGameClient());
				}
			}
		} else if (teams == 1) {
			for (final Map.Entry<Integer, Fighter> e : this.getTeam1().entrySet()) {
				if (e.getValue().getPersonnage() != null && e.getValue().getPersonnage().getGameClient() != null) {
					PWs.add(e.getValue().getPersonnage().getGameClient());
				}
			}
		}
		SocketManager.GAME_SEND_FIGHT_SHOW_CASE(PWs, guid, cellID);
	}

	public void showCaseToAll(final int guid, final int cellID) {
		final ArrayList<GameClient> PWs = new ArrayList<GameClient>();
		for (final Map.Entry<Integer, Fighter> e : this.getTeam0().entrySet()) {
			if (e.getValue().getPersonnage() != null && e.getValue().getPersonnage().getGameClient() != null) {
				PWs.add(e.getValue().getPersonnage().getGameClient());
			}
		}
		for (final Map.Entry<Integer, Fighter> e : this.getTeam1().entrySet()) {
			if (e.getValue().getPersonnage() != null && e.getValue().getPersonnage().getGameClient() != null) {
				PWs.add(e.getValue().getPersonnage().getGameClient());
			}
		}
		for (final Map.Entry<Integer, Player> e2 : this.getViewer().entrySet()) {
			PWs.add(e2.getValue().getGameClient());
		}
		SocketManager.GAME_SEND_FIGHT_SHOW_CASE(PWs, guid, cellID);
	}

	private void initOrderPlaying() {
		int j = 0;
		int k = 0;
		int start0 = 0;
		int start2 = 0;
		int curMaxIni0 = 0;
		int curMaxIni2 = 0;
		Fighter curMax0 = null;
		Fighter curMax2 = null;
		boolean team1_ready = false;
		boolean team2_ready = false;
		do {
			if (!team1_ready) {
				team1_ready = true;
				final Map<Integer, Fighter> team = this.getTeam0();
				for (final Map.Entry<Integer, Fighter> entry : team.entrySet()) {
					if (this.haveFighterInOrdreJeu(entry.getValue())) {
						continue;
					}
					team1_ready = false;
					if (entry.getValue().getInitiative() >= curMaxIni0) {
						curMaxIni0 = entry.getValue().getInitiative();
						curMax0 = entry.getValue();
					}
					if (curMaxIni0 <= start0) {
						continue;
					}
					start0 = curMaxIni0;
				}
			}
			if (!team2_ready) {
				team2_ready = true;
				for (final Map.Entry<Integer, Fighter> entry2 : this.getTeam1().entrySet()) {
					if (this.haveFighterInOrdreJeu(entry2.getValue())) {
						continue;
					}
					team2_ready = false;
					if (entry2.getValue().getInitiative() >= curMaxIni2) {
						curMaxIni2 = entry2.getValue().getInitiative();
						curMax2 = entry2.getValue();
					}
					if (curMaxIni2 <= start2) {
						continue;
					}
					start2 = curMaxIni2;
				}
			}
			if (curMax2 == null && curMax0 == null) {
				return;
			}
			if (start0 > start2) {
				if (this.getFighters(1).size() > j) {
					this.orderPlaying.add(curMax0);
					++j;
				}
				if (this.getFighters(2).size() > k) {
					this.orderPlaying.add(curMax2);
					++k;
				}
			} else {
				if (this.getFighters(2).size() > j) {
					this.orderPlaying.add(curMax2);
					++j;
				}
				if (this.getFighters(1).size() > k) {
					this.orderPlaying.add(curMax0);
					++k;
				}
			}
			curMaxIni0 = 0;
			curMaxIni2 = 0;
			curMax0 = null;
			curMax2 = null;
		} while (this.getOrderPlayingSize() != this.getFighters(3).size());
	}

	public void tryCaC(final Player perso, final int cellID) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		final Fighter caster = this.getFighterByPerso(perso);
		if (caster == null) {
			return;
		}
		if (current.getId() != caster.getId()) {
			return;
		}
		if (!perso.canCac()) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 305,
					new StringBuilder(String.valueOf(perso.getId())).toString(), "");
			SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 0, perso.getId());
			this.endTurn(false, current);
			return;
		}
		if ((this.getType() == 4 && this.getAllChallenges().size() > 0)
				|| (this.getType() == 3 && this.getAllChallenges().size() > 0)) {
			for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				c.getValue().onPlayerCac(current);
			}
		}
		if (perso.getObjetByPos(1) == null) {
			this.tryCastSpell(caster, World.getSort(0).getStatsByLevel(1), cellID);
		} else {
			final org.aestia.object.Object arme = perso.getObjetByPos(1);
			if (arme.getTemplate().getType() == 83) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 305,
						new StringBuilder(String.valueOf(perso.getId())).toString(), "");
				SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 0, perso.getId());
				this.waiter.addNow(new Runnable() {
					@Override
					public void run() {
						Fight.this.endTurn(false, current);
					}
				}, 300L);
				return;
			}
			final int PACost = arme.getTemplate().getPACost();
			if (this.getCurFighterPa() < PACost) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1170;" + this.getCurFighterPa() + "~" + PACost);
				return;
			}
			final int dist = Pathfinding.getDistanceBetween(this.getMap(), caster.getCell().getId(), cellID);
			final int MaxPO = arme.getTemplate().getPOmax();
			final int MinPO = arme.getTemplate().getPOmin();
			if (dist < MinPO || dist > MaxPO) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1171;" + MinPO + "~" + MaxPO + "~" + dist);
				return;
			}
			SocketManager.GAME_SEND_GAS_PACKET_TO_FIGHT(this, 7, perso.getId());
			final boolean isEc = arme.getTemplate().getTauxEC() != 0
					&& Formulas.getRandomValue(1, arme.getTemplate().getTauxEC()) == arme.getTemplate().getTauxEC();
			if (isEc) {
				if (Main.modDebug) {
					Console.println("Le personnage " + perso.getName() + " fait un echec critique au Cac !",
							Console.Color.INFORMATION);
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 305,
						new StringBuilder(String.valueOf(perso.getId())).toString(), "");
				SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 0, perso.getId());
				this.endTurn(false, current);
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 303,
						new StringBuilder(String.valueOf(perso.getId())).toString(),
						new StringBuilder(String.valueOf(cellID)).toString());
				final boolean isCC = caster.testIfCC(arme.getTemplate().getTauxCC());
				if (isCC) {
					if (Main.modDebug) {
						Console.println("Le personnage " + perso.getName() + " fait un coup critique au CaC !",
								Console.Color.INFORMATION);
					}
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 301,
							new StringBuilder(String.valueOf(perso.getId())).toString(), "0");
				}
				if (caster.isHide()) {
					caster.unHide(-1);
				}
				ArrayList<SpellEffect> effets = null;
				if (isCC) {
					effets = arme.getCritEffects();
				} else {
					effets = arme.getEffects();
				}
				for (final SpellEffect SE : effets) {
					if (this.getState() != 3) {
						break;
					}
					final ArrayList<Fighter> cibles = Pathfinding.getCiblesByZoneByWeapon(this,
							arme.getTemplate().getType(), this.getMap().getCase(cellID), caster.getCell().getId());
					SE.setTurn(0);
					if (this.getType() != 0 && this.getAllChallenges().size() > 0) {
						for (final Map.Entry<Integer, Challenge> c2 : this.getAllChallenges().entrySet()) {
							if (c2.getValue() == null) {
								continue;
							}
							c2.getValue().onFightersAttacked(cibles, caster, SE, -1, false);
						}
					}
					SE.applyToFight(this, caster, cibles, true);
				}
				final int idArme = arme.getTemplate().getId();
				int basePdvSoin = 1;
				int pdvSoin = -1;
				if (idArme == 7172 || idArme == 7156 || idArme == 1355 || idArme == 7182 || idArme == 7040
						|| idArme == 6539 || idArme == 6519 || idArme == 8118) {
					pdvSoin = Constant.getArmeSoin(idArme);
					if (pdvSoin != -1) {
						if (isCC) {
							basePdvSoin += arme.getTemplate().getBonusCC();
							pdvSoin += arme.getTemplate().getBonusCC();
						}
						final int intel = perso.getStats().getEffect(126) + perso.getStuffStats().getEffect(126)
								+ perso.getDonsStats().getEffect(126) + perso.getBuffsStats().getEffect(126);
						final int soins = perso.getStats().getEffect(178) + perso.getStuffStats().getEffect(178)
								+ perso.getDonsStats().getEffect(178) + perso.getBuffsStats().getEffect(178);
						final int minSoin = basePdvSoin * (100 + intel) / 100 + soins;
						final int maxSoin = pdvSoin * (100 + intel) / 100 + soins;
						int finalSoin = Formulas.getRandomValue(minSoin, maxSoin);
						final Fighter target = this.getMap().getCase(cellID).getFirstFighter();
						if (finalSoin + target.getPdv() > target.getPdvMax()) {
							finalSoin = target.getPdvMax() - target.getPdv();
						}
						target.removePdv(target, -finalSoin);
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 100,
								new StringBuilder(String.valueOf(target.getId())).toString(),
								String.valueOf(target.getId()) + ",+" + finalSoin);
					}
				}
				this.setCurFighterPa(this.getCurFighterPa() - PACost);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 102,
						new StringBuilder(String.valueOf(perso.getId())).toString(),
						String.valueOf(perso.getId()) + ",-" + PACost);
				SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 0, perso.getId());
				this.verifIfTeamAllDead();
			}
		}
	}

	public int tryCastSpell(final Fighter fighter, final Spell.SortStats Spell, final int caseID) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return 10;
		}
		if (!this.getCurAction().equals("")) {
			return 10;
		}
		if (Spell == null) {
			return 10;
		}
		final Player perso = fighter.getPersonnage();
		final Case Cell = this.getMap().getCase(caseID);
		this.setCurAction("casting");
		if (this.canCastSpell1(fighter, Spell, Cell, -1)) {
			if (fighter.getPersonnage() != null) {
				SocketManager.GAME_SEND_STATS_PACKET(fighter.getPersonnage());
			}
			if (Main.modDebug) {
				Console.println("Le fighter " + fighter.getPacketsName() + " fait une tentative de lancer le sort "
						+ Spell.getSpellID() + " sur la case " + caseID + " !", Console.Color.INFORMATION);
			}
			if (fighter.getType() == 1 && perso.getItemClasseSpell().containsKey(Spell.getSpellID())) {
				final int modi = perso.getItemClasseModif(Spell.getSpellID(), 285);
				this.setCurFighterPa(this.getCurFighterPa() - (Spell.getPACost() - modi));
				this.curFighterUsedPa += Spell.getPACost() - modi;
			} else {
				this.setCurFighterPa(this.getCurFighterPa() - Spell.getPACost());
				this.curFighterUsedPa += Spell.getPACost();
			}
			SocketManager.GAME_SEND_GAS_PACKET_TO_FIGHT(this, 7, fighter.getId());
			final boolean isEc = Spell.getTauxEC() != 0
					&& Formulas.getRandomValue(1, Spell.getTauxEC()) == Spell.getTauxEC();
			if (isEc) {
				if (Main.modDebug) {
					Console.println("Le fighter " + fighter.getPacketsName() + " fait un echec critique sur le sort "
							+ Spell.getSpellID() + " !", Console.Color.INFORMATION);
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 302,
						new StringBuilder(String.valueOf(fighter.getId())).toString(),
						new StringBuilder(String.valueOf(Spell.getSpellID())).toString());
			} else {
				if (this.getType() == 4 && this.getAllChallenges().size() > 0 && !current.isInvocation()
						&& !current.isDouble() && !current.isCollector()) {
					for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
						if (c.getValue() == null) {
							continue;
						}
						c.getValue().onPlayerAction(current, Spell.getSpellID());
						if (Spell.getSpell().getSpellID() == 0) {
							continue;
						}
						c.getValue().onPlayerSpell(current);
					}
				}
				final boolean isCC = fighter.testIfCC(Spell.getTauxCC(), Spell, fighter);
				final String sort = String.valueOf(Spell.getSpellID()) + "," + caseID + "," + Spell.getSpriteID() + ","
						+ Spell.getLevel() + "," + Spell.getSpriteInfos();
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 300,
						new StringBuilder(String.valueOf(fighter.getId())).toString(), sort);
				if (isCC) {
					if (Main.modDebug) {
						Console.println("Le fighter " + fighter.getPacketsName() + " fait un coup critique sur le sort "
								+ Spell.getSpellID() + " !", Console.Color.INFORMATION);
					}
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 301,
							new StringBuilder(String.valueOf(fighter.getId())).toString(), sort);
				}
				if (fighter.isHide()) {
					if (Spell.getSpellID() == 0) {
						fighter.unHide(caseID);
					} else {
						this.showCaseToAll(fighter.getId(), fighter.getCell().getId());
					}
				}
				Spell.applySpellEffectToFight(this, fighter, Cell, isCC, false);
			}
			if (fighter.getType() == 1 && perso.getItemClasseSpell().containsKey(Spell.getSpellID())) {
				final int modi2 = perso.getItemClasseModif(Spell.getSpellID(), 285);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 102,
						new StringBuilder(String.valueOf(fighter.getId())).toString(),
						String.valueOf(fighter.getId()) + ",-" + (Spell.getPACost() - modi2));
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 102,
						new StringBuilder(String.valueOf(fighter.getId())).toString(),
						String.valueOf(fighter.getId()) + ",-" + Spell.getPACost());
			}
			SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 0, fighter.getId());
			if (!isEc) {
				fighter.addLaunchedSort(Cell.getFirstFighter(), Spell, fighter);
			}
			if (isEc && Spell.isEcEndTurn()) {
				this.setCurAction("");
				if (fighter.getMob() != null || fighter.isInvocation()) {
					return 5;
				}
				this.endTurn(false, current);
				return 5;
			}
		} else if (fighter.getMob() != null || fighter.isInvocation()) {
			return 10;
		}
		if (fighter.getPersonnage() != null) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 102,
					new StringBuilder(String.valueOf(fighter.getId())).toString(),
					String.valueOf(fighter.getId()) + ",-0");
		}
		this.setCurAction("");
		this.verifIfTeamAllDead();
		return 0;
	}

	public boolean canCastSpell1(final Fighter caster, final Spell.SortStats SS, final Case cell,
			final int targetCell) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return false;
		}
		int casterCell;
		if (targetCell <= -1) {
			casterCell = caster.getCell().getId();
		} else {
			casterCell = targetCell;
		}
		final Player perso = caster.getPersonnage();
		if (SS == null) {
			if (perso != null) {
				SocketManager.GAME_SEND_GA_CLEAR_PACKET_TO_FIGHT(this, 7);
				SocketManager.GAME_SEND_Im_PACKET(perso, "1169");
				SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 0, perso.getId());
			}
			return false;
		}
		if (current == null || current.getId() != caster.getId()) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1175");
			}
			return false;
		}
		int usedPA = 0;
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			final int modi = perso.getItemClasseModif(SS.getSpellID(), 285);
			usedPA = SS.getPACost() - modi;
		} else {
			usedPA = SS.getPACost();
		}
		if (this.getCurFighterPa() < usedPA) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1170;" + this.getCurFighterPa() + "~" + SS.getPACost());
			}
			return false;
		}
		if (cell == null) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1172");
			}
			return false;
		}
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			final int modi = perso.getItemClasseModif(SS.getSpellID(), 288);
			final boolean modif = modi == 1;
			if (SS.isLineLaunch() && !modif
					&& !Pathfinding.casesAreInSameLine(this.getMap(), casterCell, cell.getId(), 'z')) {
				if (perso != null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "1173");
				}
				return false;
			}
		} else if (SS.isLineLaunch() && !Pathfinding.casesAreInSameLine(this.getMap(), casterCell, cell.getId(), 'z')) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1173");
			}
			return false;
		}
		final char dir = Pathfinding.getDirBetweenTwoCase(casterCell, cell.getId(), this.getMap(), true);
		if (SS.getSpellID() == 67 && !Pathfinding.checkLoS(this.getMap(),
				Pathfinding.GetCaseIDFromDirrection(casterCell, dir, this.getMap(), true), cell.getId(), null, true)) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1174");
			}
			return false;
		}
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			final int modi2 = perso.getItemClasseModif(SS.getSpellID(), 289);
			final boolean modif2 = modi2 == 1;
			if (SS.hasLDV() && !Pathfinding.checkLoS(this.getMap(), casterCell, cell.getId(), caster) && !modif2) {
				if (perso != null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "1174");
				}
				return false;
			}
		} else if (SS.hasLDV() && !Pathfinding.checkLoS(this.getMap(), casterCell, cell.getId(), caster)) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1174");
			}
			return false;
		}
		final int dist = Pathfinding.getDistanceBetween(this.getMap(), casterCell, cell.getId());
		int maxAlc = SS.getMaxPO();
		final int minAlc = SS.getMinPO();
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			final int modi3 = perso.getItemClasseModif(SS.getSpellID(), 281);
			maxAlc += modi3;
		}
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			final int modi3 = perso.getItemClasseModif(SS.getSpellID(), 282);
			final boolean modif3 = modi3 == 1;
			if (SS.isModifPO() || modif3) {
				maxAlc += caster.getTotalStats().getEffect(117);
				if (maxAlc <= minAlc) {
					maxAlc = minAlc + 1;
				}
			}
		} else if (SS.isModifPO()) {
			maxAlc += caster.getTotalStats().getEffect(117);
			if (maxAlc <= minAlc) {
				maxAlc = minAlc + 1;
			}
		}
		if (maxAlc < minAlc) {
			maxAlc = minAlc;
		}
		if (dist < minAlc || dist > maxAlc) {
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1171;" + minAlc + "~" + maxAlc + "~" + dist);
			}
			return false;
		}
		if (!LaunchedSpell.cooldownGood(caster, SS.getSpellID())) {
			return false;
		}
		int numLunch = SS.getMaxLaunchbyTurn();
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			numLunch += perso.getItemClasseModif(SS.getSpellID(), 290);
		}
		if (numLunch - LaunchedSpell.getNbLaunch(caster, SS.getSpellID()) <= 0 && numLunch > 0) {
			return false;
		}
		final Fighter t = cell.getFirstFighter();
		int numLunchT = SS.getMaxLaunchByTarget();
		if (caster.getType() == 1 && perso.getItemClasseSpell().containsKey(SS.getSpellID())) {
			numLunchT += perso.getItemClasseModif(SS.getSpellID(), 291);
		}
		return numLunchT - LaunchedSpell.getNbLaunchTarget(caster, t, SS.getSpellID()) > 0 || numLunchT <= 0;
	}

	public boolean canCastSpell2(final Fighter fighter, final Spell.SortStats spell, final Case cell,
			final int launchCase) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return false;
		}
		if (spell == null) {
			if (Main.modDebug) {
				Console.println("Le sort est inexistant !", Console.Color.INFORMATION);
			}
			return false;
		}
		if (current.getId() != fighter.getId()) {
			if (Main.modDebug) {
				Console.println("Ce n est pas le tour du joueur", Console.Color.INFORMATION);
			}
			return false;
		}
		if (this.getCurFighterPa() < spell.getPACost()) {
			if (Main.modDebug) {
				Console.println("Le joueur ne possede pas assez de PA (" + this.getCurFighterPa() + "/"
						+ spell.getPACost() + ") !", Console.Color.INFORMATION);
			}
			return false;
		}
		if (cell == null) {
			if (Main.modDebug) {
				Console.println("La cellule visee est inexistante !", Console.Color.INFORMATION);
			}
			return false;
		}
		if (spell.isLineLaunch() && !Pathfinding.casesAreInSameLine(this.getMap(), launchCase, cell.getId(), 'z')) {
			if (Main.modDebug) {
				Console.println("Le sort demande un lancer en ligne", Console.Color.INFORMATION);
			}
			return false;
		}
		if (spell.hasLDV() && !Pathfinding.checkLoS(this.getMap(), launchCase, cell.getId(), fighter)) {
			if (Main.modDebug) {
				Console.println("Le sort demande une ligne de vue (IA)", Console.Color.INFORMATION);
			}
			return false;
		}
		final int dist = Pathfinding.getDistanceBetween(this.getMap(), launchCase, cell.getId());
		int MaxPO = spell.getMaxPO();
		if (spell.isModifPO()) {
			MaxPO += fighter.getTotalStats().getEffect(117);
		}
		if (dist < spell.getMinPO() || dist > MaxPO) {
			if (Main.modDebug) {
				Console.println("La case est trop proche ou trop eloignee (IA) Min: " + spell.getMinPO() + " & Max: "
						+ spell.getMaxPO() + " & Dist: " + dist + " !", Console.Color.INFORMATION);
			}
			return false;
		}
		if (!LaunchedSpell.cooldownGood(fighter, spell.getSpellID())) {
			return false;
		}
		final int nbLancer = spell.getMaxLaunchbyTurn();
		if (nbLancer - LaunchedSpell.getNbLaunch(fighter, spell.getSpellID()) <= 0 && nbLancer > 0) {
			return false;
		}
		final Fighter target = cell.getFirstFighter();
		final int nbLancerT = spell.getMaxLaunchByTarget();
		return nbLancerT - LaunchedSpell.getNbLaunchTarget(fighter, target, spell.getSpellID()) > 0 || nbLancerT <= 0;
	}

	public boolean canCastSpell3(final Fighter fighter, final Spell.SortStats spell, final Case cell) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return false;
		}
		final Player perso = fighter.getPersonnage();
		if (spell == null) {
			if (Main.modDebug) {
				Console.println("Le sort est inexistant", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1169");
			}
			return false;
		}
		if (current.getId() != fighter.getId()) {
			if (Main.modDebug) {
				Console.println("Ce n est pas le tour du joueur !", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1175");
			}
			return false;
		}
		if (this.getCurFighterPa() < spell.getPACost()) {
			if (Main.modDebug) {
				Console.println("Le joueur ne possede pas assez de PA (" + this.getCurFighterPa() + "/"
						+ spell.getPACost() + ") !", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1170;" + this.getCurFighterPa() + "~" + spell.getPACost());
			}
			return false;
		}
		if (cell == null) {
			if (Main.modDebug) {
				Console.println("La cellule visee est inexistante !", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1172");
			}
			return false;
		}
		if (spell.isLineLaunch()
				&& !Pathfinding.casesAreInSameLine(this.getMap(), fighter.getCell().getId(), cell.getId(), 'z')) {
			if (Main.modDebug) {
				Console.println("Le sort demande un lancer en ligne !", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1173");
			}
			return false;
		}
		if (spell.hasLDV() && !Pathfinding.checkLoS(this.getMap(), fighter.getCell().getId(), cell.getId(), fighter)) {
			if (Main.modDebug) {
				Console.println("Le sort demande une ligne de vue !", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "1174");
			}
			return false;
		}
		final int dist = Pathfinding.getDistanceBetween(this.getMap(), fighter.getCell().getId(), cell.getId());
		int MaxPO = spell.getMaxPO();
		if (spell.isModifPO()) {
			MaxPO += fighter.getTotalStats().getEffect(117);
		}
		if (MaxPO < spell.getMinPO()) {
			MaxPO = spell.getMinPO();
		}
		if (dist < spell.getMinPO() || dist > MaxPO) {
			if (Main.modDebug) {
				Console.println("La case est trop proche ou trop eloignee (IA) Min: " + spell.getMinPO() + " & Max: "
						+ spell.getMaxPO() + " & Dist: " + dist + " !", Console.Color.INFORMATION);
			}
			if (perso != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso,
						"1171;" + spell.getMinPO() + "~" + spell.getMaxPO() + "~" + dist);
			}
			return false;
		}
		if (!LaunchedSpell.cooldownGood(fighter, spell.getSpellID())) {
			return false;
		}
		final int nbLancer = spell.getMaxLaunchbyTurn();
		if (nbLancer - LaunchedSpell.getNbLaunch(fighter, spell.getSpellID()) <= 0 && nbLancer > 0) {
			return false;
		}
		final Fighter target = cell.getFirstFighter();
		final int nbLancerT = spell.getMaxLaunchByTarget();
		return nbLancerT - LaunchedSpell.getNbLaunchTarget(fighter, target, spell.getSpellID()) > 0 || nbLancerT <= 0;
	}

	public boolean onFighterDeplace(final Fighter f, final GameAction GA) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return false;
		}
		final String path = GA.args;
		if (path.equals("")) {
			if (Main.modDebug) {
				Console.println("Le fighter " + f.getPacketsName() + " demande un deplacement vide !",
						Console.Color.INFORMATION);
			}
			return false;
		}
		if (this.getOrderPlayingSize() <= this.getCurPlayer()) {
			return false;
		}
		if (Main.modDebug) {
			Console.println("Le fighter " + f.getPacketsName() + " tente de se deplacer a partir de la cellule "
					+ f.getCell().getId() + " !", Console.Color.INFORMATION);
		}
		if (Main.modDebug) {
			Console.println("Value du path pour le fighter " + f.getPacketsName() + " : " + path,
					Console.Color.INFORMATION);
		}
		if (!this.getCurAction().equals("") || current.getId() != f.getId() || this.getState() != 3) {
			if (!this.getCurAction().equals("") && Main.modDebug) {
				Console.println(
						"Le fighter " + f.getPacketsName()
								+ " effectue un deplacement qui echoue du a une action deja en cours !",
						Console.Color.INFORMATION);
			}
			if (current.getId() != f.getId() && Main.modDebug) {
				Console.println(
						"Le fighter " + f.getPacketsName() + " effectue un deplacement qui echoue du au tour de jeu !",
						Console.Color.INFORMATION);
			}
			if (this.getState() != 3 && Main.modDebug) {
				Console.println(
						"Le fighter " + f.getPacketsName()
								+ " effectue un deplacement qui echoue du au combat non commence !",
						Console.Color.INFORMATION);
			}
			return false;
		}
		final Fighter targetTacle = Pathfinding.getEnemyAround(f.getCell().getId(), this.getMap(), this);
		if (targetTacle != null && !f.haveState(6) && !f.haveState(8)) {
			final int esquive = Formulas.getTacleChance(f, targetTacle);
			final int rand = Formulas.getRandomValue(0, 99);
			if (rand > esquive) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, GA.id, "104", String.valueOf(f.getId()) + ";", "");
				int pierdePA = this.getCurFighterPa() * esquive / 100;
				if (pierdePA < 0) {
					pierdePA = -pierdePA;
				}
				if (this.getCurFighterPm() < 0) {
					this.setCurFighterPm(0);
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, GA.id, "129",
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + ",-" + this.getCurFighterPm());
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, GA.id, "102",
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + ",-" + pierdePA);
				this.setCurFighterPm(0);
				this.setCurFighterPa(this.getCurFighterPa() - pierdePA);
				return false;
			}
		}
		final AtomicReference<String> pathRef = new AtomicReference<String>(path);
		int nStep = Pathfinding.isValidPath(this.getMap(), f.getCell().getId(), pathRef, this, null, -1);
		final String newPath = pathRef.get();
		if (nStep > this.getCurFighterPm() || nStep == -1000) {
			if (Main.modDebug) {
				Console.println(
						"Le fighter " + f.getPacketsName() + " demande un chemin inaccessible ou trop eloigne !",
						Console.Color.INFORMATION);
			}
			if (f.getPersonnage() != null) {
				SocketManager.GAME_SEND_GA_PACKET(f.getPersonnage().getGameClient(), "", "0", "", "");
			}
			return false;
		}
		this.setCurFighterPm(this.getCurFighterPm() - nStep);
		this.curFighterUsedPm += nStep;
		final int nextCellID = CryptManager.cellCode_To_ID(newPath.substring(newPath.length() - 2));
		if (current.getPersonnage() != null) {
			SocketManager.GAME_SEND_GAS_PACKET_TO_FIGHT(this, 7, current.getId());
		}
		if (!current.isHide()) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, GA.id, "1",
					new StringBuilder(String.valueOf(current.getId())).toString(),
					"a" + CryptManager.cellID_To_Code(f.getCell().getId()) + newPath);
		} else if (current.getPersonnage() != null) {
			final GameClient out = current.getPersonnage().getGameClient();
			SocketManager.GAME_SEND_GA_PACKET(out, new StringBuilder(String.valueOf(GA.id)).toString(), "1",
					new StringBuilder(String.valueOf(current.getId())).toString(),
					"a" + CryptManager.cellID_To_Code(f.getCell().getId()) + newPath);
		}
		Fighter po = current.getHoldedBy();
		if (po != null && current.haveState(8) && po.haveState(3) && nextCellID != po.getCell().getId()) {
			po.setState(3, 0);
			current.setState(8, 0);
			po.setIsHolding(null);
			current.setHoldedBy(null);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 950,
					new StringBuilder(String.valueOf(po.getId())).toString(),
					String.valueOf(po.getId()) + "," + 3 + ",0");
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 950,
					new StringBuilder(String.valueOf(current.getId())).toString(),
					String.valueOf(current.getId()) + "," + 8 + ",0");
		}
		current.getCell().getFighters().clear();
		if (Main.modDebug) {
			Console.println("Le fighter " + f.getPacketsName() + " se deplace de la case " + current.getCell().getId()
					+ " vers " + CryptManager.cellCode_To_ID(newPath.substring(newPath.length() - 2))
					+ " avec succes !", Console.Color.INFORMATION);
		}
		current.setCell(this.getMap().getCase(nextCellID));
		current.getCell().addFighter(current);
		if (po != null) {
			po.getCell().addFighter(po);
		}
		if (nStep < 0) {
			if (Main.modDebug) {
				Console.println(
						"Bug nStep sur le fighter " + f.getPacketsName() + ", nStep negatives, reconversion en cours !",
						Console.Color.INFORMATION);
			}
			nStep *= -1;
		}
		this.setCurAction("GA;129;" + current.getId() + ";" + current.getId() + ",-" + nStep);
		po = current.getIsHolding();
		if (po != null && current.haveState(3) && po.haveState(8)) {
			po.setCell(current.getCell());
			if (Main.modDebug) {
				Console.println(
						"Le fighter " + po.getPacketsName() + " se deplace vers la cellule " + nextCellID + " !",
						Console.Color.INFORMATION);
			}
		}
		if (f.getPersonnage() == null) {
			final String _curAction = this.getCurAction();
			try {
				Thread.sleep(900 + 100 * nStep);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SocketManager.GAME_SEND_GAMEACTION_TO_FIGHT(this, 7, _curAction);
			this.setCurAction("");
			final ArrayList<Trap> P = new ArrayList<Trap>();
			P.addAll(this.getAllTraps());
			for (final Trap p : P) {
				if (p == null) {
					continue;
				}
				final int dist = Pathfinding.getDistanceBetween(this.getMap(), p.getCell().getId(),
						current.getCell().getId());
				if (dist > p.getSize()) {
					continue;
				}
				p.onTraped(current);
			}
			return true;
		}
		if ((this.getType() == 4 && this.getAllChallenges().size() > 0 && !current.isInvocation() && !current.isDouble()
				&& !current.isCollector())
				|| (this.getType() == 3 && this.getAllChallenges().size() > 0 && !current.isInvocation()
						&& !current.isDouble() && !current.isCollector())) {
			for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				c.getValue().onPlayerMove(f);
			}
		}
		f.getPersonnage().getGameClient().addAction(GA);
		return true;
	}

	public void onFighterDie(final Fighter target, final Fighter caster) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		target.setIsDead(true);
		if (!target.hasLeft()) {
			this.getDeadList().put(target.getId(), target);
		}
		SocketManager.GAME_SEND_FIGHT_PLAYER_DIE_TO_FIGHT(this, 7, target.getId());
		target.getCell().getFighters().clear();
		if (target.haveState(3)) {
			final Fighter f = target.getIsHolding();
			f.setCell(f.getCell());
			f.getCell().addFighter(f);
			f.setState(8, 0);
			target.setState(3, 0);
			f.setHoldedBy(null);
			target.setIsHolding(null);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 950,
					new StringBuilder(String.valueOf(f.getId())).toString(),
					String.valueOf(f.getId()) + "," + 8 + ",0");
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 950,
					new StringBuilder(String.valueOf(target.getId())).toString(),
					String.valueOf(target.getId()) + "," + 3 + ",0");
		}
		if ((this.getType() == 4 && this.getAllChallenges().size() > 0)
				|| (this.getType() == 3 && this.getAllChallenges().size() > 0)) {
			for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				c.getValue().onFighterDie(target);
			}
		}
		if (target.getTeam() == 0) {
			final TreeMap<Integer, Fighter> team = new TreeMap<Integer, Fighter>();
			team.putAll(this.getTeam0());
			for (final Map.Entry<Integer, Fighter> entry : team.entrySet()) {
				if (entry.getValue().getInvocator() == null) {
					continue;
				}
				if (entry.getValue().getPdv() == 0) {
					continue;
				}
				if (entry.getValue().isDead()) {
					continue;
				}
				if (entry.getValue().getInvocator().getId() != target.getId()) {
					continue;
				}
				this.onFighterDie(entry.getValue(), caster);
				try {
					final int index = this.getOrderPlaying().indexOf(entry.getValue());
					if (index != -1) {
						this.getOrderPlaying().remove(index);
					}
					if (this.getTeam0().containsKey(entry.getValue().getId())) {
						this.getTeam0().remove(entry.getValue().getId());
					} else if (this.getTeam1().containsKey(entry.getValue().getId())) {
						this.getTeam1().remove(entry.getValue().getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 999,
						new StringBuilder(String.valueOf(target.getId())).toString(), this.getGTL());
			}
		} else if (target.getTeam() == 1) {
			final TreeMap<Integer, Fighter> team = new TreeMap<Integer, Fighter>();
			team.putAll(this.getTeam1());
			for (final Map.Entry<Integer, Fighter> entry : team.entrySet()) {
				if (entry.getValue().getInvocator() == null) {
					continue;
				}
				if (entry.getValue().getPdv() == 0) {
					continue;
				}
				if (entry.getValue().isDead()) {
					continue;
				}
				if (entry.getValue().getInvocator().getId() != target.getId()) {
					continue;
				}
				this.onFighterDie(entry.getValue(), caster);
				if (this.getOrderPlaying() != null && !this.getOrderPlaying().isEmpty()) {
					try {
						final int index = this.getOrderPlaying().indexOf(entry.getValue());
						if (index != -1) {
							this.getOrderPlaying().remove(index);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (this.getTeam0().containsKey(entry.getValue().getId())) {
					this.getTeam0().remove(entry.getValue().getId());
				} else if (this.getTeam1().containsKey(entry.getValue().getId())) {
					this.getTeam1().remove(entry.getValue().getId());
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 999,
						new StringBuilder(String.valueOf(target.getId())).toString(), this.getGTL());
			}
		}
		if (target.getMob() != null) {
			try {
				if (target.isInvocation() && !target.isStatique) {
					final Fighter invocator = target.getInvocator();
					--invocator.nbrInvoc;
					if (current != null) {
						if (!target.canPlay() && current.getId() == target.getId()) {
							this.setCurPlayer(this.getCurPlayer() - 1);
							this.endTurn(false, current);
						}
						if (target.canPlay() && current.getId() == target.getId()) {
							this.setCurAction("");
							this.endTurn(false, current);
						}
					}
					if (this.getOrderPlaying() != null && !this.getOrderPlaying().isEmpty()) {
						final int index2 = this.getOrderPlaying().indexOf(target);
						if (index2 != -1) {
							if (this.getCurPlayer() > index2 && this.getCurPlayer() > 0) {
								this.setCurPlayer(this.getCurPlayer() - 1);
							}
							this.getOrderPlaying().remove(index2);
						}
						if (this.getCurPlayer() < 0) {
							return;
						}
						if (this.getTeam0().containsKey(target.getId())) {
							this.getTeam0().remove(target.getId());
						} else if (this.getTeam1().containsKey(target.getId())) {
							this.getTeam1().remove(target.getId());
						}
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 7, 999,
								new StringBuilder(String.valueOf(target.getId())).toString(), this.getGTL());
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if ((this.getType() == 4 || this.getType() == 3) && this.getAllChallenges().size() > 0) {
			for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				c.getValue().onMobDie(target, caster);
			}
		}
		final ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
		glyphs.addAll(this.getAllGlyphs());
		for (final Glyph g : glyphs) {
			if (g.getCaster().getId() == target.getId()) {
				SocketManager.GAME_SEND_GDZ_PACKET_TO_FIGHT(this, 7, "-", g.getCell().getId(), g.getSize(), 4);
				SocketManager.GAME_SEND_GDC_PACKET_TO_FIGHT(this, 7, g.getCell().getId());
				this.getAllGlyphs().remove(g);
			}
		}
		final ArrayList<Trap> Ps = new ArrayList<Trap>();
		Ps.addAll(this.getAllTraps());
		for (final Trap p : Ps) {
			if (p.getCaster().getId() == target.getId()) {
				p.desappear();
				this.getAllTraps().remove(p);
			}
		}
		if (target.canPlay() && current.getId() == target.getId() && !current.hasLeft()) {
			this.endTurn(false, current);
		}
		if (target.isCollector()) {
			for (final Fighter f2 : this.getFighters(target.getTeam2())) {
				if (f2.isDead()) {
					continue;
				}
				this.onFighterDie(f2, target);
				this.verifIfTeamAllDead();
			}
		}
		if (target.isPrisme()) {
			for (final Fighter f2 : this.getFighters(target.getTeam2())) {
				if (f2.isDead()) {
					continue;
				}
				this.onFighterDie(f2, target);
				this.verifIfTeamAllDead();
			}
		}
		for (final Fighter fighter : this.getFighters(3)) {
			final ArrayList<SpellEffect> newBuffs = new ArrayList<SpellEffect>();
			for (final SpellEffect entry2 : fighter.getFightBuff()) {
				final int casterId = entry2.getCaster().getId();
				if (casterId == target.getId()) {
					continue;
				}
				newBuffs.add(entry2);
			}
			fighter.getFightBuff().clear();
			fighter.getFightBuff().addAll(newBuffs);
		}
		this.verifIfTeamAllDead();
	}

	public ArrayList<Player> getAllFighters() {
		final ArrayList<Player> fighters = new ArrayList<Player>();
		for (final Map.Entry<Integer, Fighter> entry : this.getTeam1().entrySet()) {
			if (entry.getValue().getPersonnage() != null) {
				fighters.add(entry.getValue().getPersonnage());
			}
		}
		for (final Map.Entry<Integer, Fighter> entry : this.getTeam0().entrySet()) {
			if (entry.getValue().getPersonnage() != null) {
				fighters.add(entry.getValue().getPersonnage());
			}
		}
		return fighters;
	}

	public ArrayList<Fighter> getFighters(int teams) {
		final ArrayList<Fighter> fighters = new ArrayList<Fighter>();
		if (teams - 4 >= 0) {
			for (final Map.Entry<Integer, Player> entry : this.getViewer().entrySet()) {
				fighters.add(new Fighter(this, entry.getValue()));
			}
			teams -= 4;
		}
		if (teams - 2 >= 0) {
			for (final Map.Entry<Integer, Fighter> entry2 : this.getTeam1().entrySet()) {
				fighters.add(entry2.getValue());
			}
			teams -= 2;
		}
		if (teams - 1 >= 0) {
			for (final Map.Entry<Integer, Fighter> entry2 : this.getTeam0().entrySet()) {
				fighters.add(entry2.getValue());
			}
		}
		return fighters;
	}

	public ArrayList<Fighter> getFighters2(final int teams) {
		final ArrayList<Fighter> fighters = new ArrayList<Fighter>();
		if (teams == 0) {
			for (final Map.Entry<Integer, Player> entry : this.getViewer().entrySet()) {
				fighters.add(new Fighter(this, entry.getValue()));
			}
		}
		if (teams == 2) {
			for (final Map.Entry<Integer, Fighter> entry2 : this.getTeam1().entrySet()) {
				fighters.add(entry2.getValue());
			}
		}
		if (teams == 1) {
			for (final Map.Entry<Integer, Fighter> entry2 : this.getTeam0().entrySet()) {
				fighters.add(entry2.getValue());
			}
		}
		return fighters;
	}

	public ArrayList<Fighter> getEnnemy(final Fighter F) {
		final ArrayList<Fighter> ennemy = new ArrayList<Fighter>();
		for (final Fighter f : this.getFighters(3)) {
			if (f.getOtherTeam() == F.getOtherTeam()) {
				continue;
			}
			if (f == F) {
				continue;
			}
			ennemy.add(f);
		}
		return ennemy;
	}

	public Map<Integer, Fighter> getEnnemy2(final Fighter F) {
		final Map<Integer, Fighter> ennemy = new TreeMap<Integer, Fighter>();
		for (final Fighter f : this.getFighters(3)) {
			if (f.getOtherTeam() == F.getOtherTeam()) {
				continue;
			}
			if (f == F) {
				continue;
			}
			ennemy.put(f.getId(), f);
		}
		return ennemy;
	}

	public Fighter getFighterByPerso(final Player perso) {
		Fighter fighter = null;
		if (this.getTeam0().get(perso.getId()) != null) {
			fighter = this.getTeam0().get(perso.getId());
		}
		if (this.getTeam1().get(perso.getId()) != null) {
			fighter = this.getTeam1().get(perso.getId());
		}
		return fighter;
	}

	public void refreshCurPlayerInfos() {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		this.setCurFighterPa(current.getTotalStats().getEffect(111) - this.getCurFighterUsedPa());
		this.setCurFighterPm(current.getTotalStats().getEffect(128) - this.getCurFighterUsedPm());
	}

	private Case getRandomCell(final List<Case> cells) {
		final Random rand = new Random();
		if (cells.isEmpty()) {
			return null;
		}
		int limit = 0;
		Case cell;
		do {
			final int id = rand.nextInt(cells.size());
			cell = cells.get(id);
			++limit;
		} while ((cell == null || !cell.getFighters().isEmpty()) && limit < 80);
		if (limit == 80) {
			if (Main.modDebug) {
				Console.println("Cellule de combat aleatoire non trouve pour le combat " + this.getId() + " sur la map "
						+ this.getMapOld().getId() + " !", Console.Color.INFORMATION);
			}
			return null;
		}
		return cell;
	}

	public synchronized void exchangePlace(final Player perso, final int cell) {
		final Fighter fighter = this.getFighterByPerso(perso);
		final int team = this.getTeamId(perso.getId()) - 1;
		if (this.collector != null && this.collectorProtect && !this.collector.getDefenseFight().containsValue(perso)) {
			return;
		}
		if (fighter == null) {
			return;
		}
		boolean valid1 = false;
		boolean valid2 = false;
		for (int a = 0; a < this.getStart0().size(); ++a) {
			if (this.getStart0().get(a).getId() == cell) {
				valid1 = true;
			}
		}
		for (int a = 0; a < this.getStart1().size(); ++a) {
			if (this.getStart1().get(a).getId() == cell) {
				valid2 = true;
			}
		}
		if (this.getState() != 2 || this.isOccuped(cell) || perso.is_ready() || (team == 0 && !valid1)
				|| (team == 1 && !valid2)) {
			return;
		}
		fighter.getCell().getFighters().clear();
		fighter.setCell(this.getMap().getCase(cell));
		this.getMap().getCase(cell).addFighter(fighter);
		SocketManager.GAME_SEND_FIGHT_CHANGE_PLACE_PACKET_TO_FIGHT(this, 3, this.getMap(), perso.getId(), cell);
	}

	public boolean isOccuped(final int cell) {
		return this.getMap().getCase(cell) == null || this.getMap().getCase(cell).getFighters().size() > 0;
	}

	public int getNextLowerFighterGuid() {
		return this.nextId--;
	}

	public static void FightStateAddFlag(final org.aestia.map.Map map, final Player P) {
		for (final Map.Entry<Integer, Fight> fight : map.getFights().entrySet()) {
			if (fight.getValue().state == 2) {
				if (fight.getValue().type == 0) {
					SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), 0, fight.getValue().init0.getId(),
							fight.getValue().init1.getId(), fight.getValue().init0.getPersonnage().getCurCell().getId(),
							"0;-1", fight.getValue().init1.getPersonnage().getCurCell().getId(), "0;-1");
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team0.entrySet()) {
						if (Main.modDebug) {
							Console.println(F.getValue().getPersonnage().getName(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init0.getPersonnage().getCurMap(), fight.getValue().init0.getId(),
								fight.getValue().init0);
					}
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team1.entrySet()) {
						if (Main.modDebug) {
							Console.println(F.getValue().getPersonnage().getName(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init1.getPersonnage().getCurMap(), fight.getValue().init1.getId(),
								fight.getValue().init1);
					}
				} else if (fight.getValue().type == 1) {
					SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), 0, fight.getValue().init0.getId(),
							fight.getValue().init1.getId(), fight.getValue().init0.getPersonnage().getCurCell().getId(),
							"0;" + fight.getValue().init0.getPersonnage().get_align(),
							fight.getValue().init1.getPersonnage().getCurCell().getId(),
							"0;" + fight.getValue().init1.getPersonnage().get_align());
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team0.entrySet()) {
						if (Main.modDebug) {
							Console.println(F.getValue().getPersonnage().getName(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init0.getPersonnage().getCurMap(), fight.getValue().init0.getId(),
								fight.getValue().init0);
					}
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team1.entrySet()) {
						if (Main.modDebug) {
							Console.println(F.getValue().getPersonnage().getName(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init1.getPersonnage().getCurMap(), fight.getValue().init1.getId(),
								fight.getValue().init1);
					}
				} else if (fight.getValue().type == 4) {
					SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), 4, fight.getValue().init0.getId(),
							fight.getValue().mobGroup.getId(),
							fight.getValue().init0.getPersonnage().getCurCell().getId() + 1, "0;-1",
							fight.getValue().mobGroup.getCellId(), "1;-1");
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team0.entrySet()) {
						if (Main.modDebug) {
							Console.println("PVM1: " + F.getValue().getPersonnage().getName(),
									Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init0.getPersonnage().getCurMap(), fight.getValue().init0.getId(),
								fight.getValue().init0);
					}
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team1.entrySet()) {
						if (Main.modDebug) {
							Console.println("PVM2: " + F.getValue(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P, fight.getValue().map,
								fight.getValue().getMobGroup().getId(), F.getValue());
					}
				} else if (fight.getValue().type == 3) {
					SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), 4, fight.getValue().init0.getId(),
							fight.getValue().mobGroup.getId(),
							fight.getValue().init0.getPersonnage().getCurCell().getId() + 1, "0;-1",
							fight.getValue().mobGroup.getCellId(), "1;-1");
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team0.entrySet()) {
						if (Main.modDebug) {
							Console.println("PVM1: " + F.getValue().getPersonnage().getName(),
									Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init0.getPersonnage().getCurMap(), fight.getValue().init0.getId(),
								fight.getValue().init0);
					}
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team1.entrySet()) {
						if (Main.modDebug) {
							Console.println("PVM2: " + F.getValue(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P, fight.getValue().map,
								fight.getValue().getMobGroup().getId(), F.getValue());
					}
				} else if (fight.getValue().type == 5) {
					SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), 5, fight.getValue().init0.getId(),
							fight.getValue().collector.getId(),
							fight.getValue().init0.getPersonnage().getCurCell().getId() + 1, "0;-1",
							fight.getValue().collector.getCell(), "3;-1");
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team0.entrySet()) {
						if (Main.modDebug) {
							Console.println("PVT1: " + F.getValue().getPersonnage().getName(),
									Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
								fight.getValue().init0.getPersonnage().getCurMap(), fight.getValue().init0.getId(),
								fight.getValue().init0);
					}
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team1.entrySet()) {
						if (Main.modDebug) {
							Console.println("PVT2: " + F.getValue(), Console.Color.INFORMATION);
						}
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P, fight.getValue().map,
								fight.getValue().getCollector().getId(), F.getValue());
					}
				} else {
					if (fight.getValue().type != 2) {
						continue;
					}
					SocketManager.GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), 0, fight.getValue().init0.getId(),
							fight.getValue().prism.getId(), fight.getValue().init0.getPersonnage().getCurCell().getId(),
							"0;" + fight.getValue().init0.getPersonnage().get_align(), fight.getValue().prism.getCell(),
							"0;" + fight.getValue().prism.getAlignement());
					SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P,
							fight.getValue().init0.getPersonnage().getCurMap(), fight.getValue().init0.getId(),
							fight.getValue().init0);
					for (final Map.Entry<Integer, Fighter> F : fight.getValue().team1.entrySet()) {
						SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(P, fight.getValue().map,
								fight.getValue().getPrism().getId(), F.getValue());
					}
				}
			}
		}
	}

	public void addFighterInTeam(final Fighter f, final int team) {
		if (team == 0) {
			this.getTeam0().put(f.getId(), f);
		} else if (team == 1) {
			this.getTeam1().put(f.getId(), f);
		}
	}

	public void addChevalier() {
		String groupData = "";
		int a = 0;
		for (final Fighter F : this.getTeam0().values()) {
			if (F.getPersonnage() == null) {
				continue;
			}
			if (this.getTeam1().size() > this.getTeam0().size()) {
				continue;
			}
			groupData = String.valueOf(groupData) + "394," + Constant.getLevelForChevalier(F.getPersonnage()) + ","
					+ Constant.getLevelForChevalier(F.getPersonnage());
			if (a < this.getTeam0().size() - 1) {
				groupData = String.valueOf(groupData) + ";";
			}
			++a;
		}
		this.setMobGroup(new Monster.MobGroup(this.getMapOld().nextObjectId,
				this.getInit0().getPersonnage().getCurCell().getId(), groupData));
		for (final Map.Entry<Integer, Monster.MobGrade> entry : this.getMobGroup().getMobs().entrySet()) {
			entry.getValue().setInFightID(entry.getKey());
			this.getTeam1().put(entry.getKey(), new Fighter(this, entry.getValue()));
		}
		final List<Map.Entry<Integer, Fighter>> e = new ArrayList<Map.Entry<Integer, Fighter>>();
		e.addAll(this.getTeam1().entrySet());
		for (final Map.Entry<Integer, Fighter> entry2 : e) {
			if (entry2.getValue().getPersonnage() != null) {
				continue;
			}
			final Fighter f = entry2.getValue();
			final Case cell = this.getRandomCell(this.getStart1());
			if (cell == null) {
				this.getTeam1().remove(f.getId());
			} else {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 8 + ",0");
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this, 3, 950,
						new StringBuilder(String.valueOf(f.getId())).toString(),
						String.valueOf(f.getId()) + "," + 3 + ",0");
				f.setCell(cell);
				f.getCell().addFighter(f);
				f.setTeam(1);
				SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit0().getPersonnage().getCurMap(),
						this.getMobGroup().getId(), entry2.getValue());
				SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(this.getInit1().getPersonnage().getCurMap(),
						this.getMobGroup().getId(), entry2.getValue());
			}
		}
		SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(this, 7, this.getMap());
	}

	public boolean playerDisconnect(final Player perso, final boolean verif) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return false;
		}
		final Fighter f = this.getFighterByPerso(perso);
		if (f == null) {
			return false;
		}
		if (this.getState() == 1 || this.getState() == 4) {
			if (!verif) {
				this.leftFight(perso, null);
			}
			return false;
		}
		if (f.getNbrDisconnection() >= 5) {
			if (!verif) {
				this.leftFight(perso, null);
				for (final Fighter e : this.getFighters(7)) {
					if (e.getPersonnage() != null) {
						if (!e.getPersonnage().isOnline()) {
							continue;
						}
						SocketManager.GAME_SEND_MESSAGE(e.getPersonnage(),
								String.valueOf(f.getPacketsName())
										+ " s'est d\u00e9connect\u00e9 plus de 5 fois dans le m\u00eame combat, nous avons d\u00e9cid\u00e9 de lui faire abandonner.",
								"A00000");
					}
				}
			}
			return false;
		}
		if (!verif && !this.isBegin()) {
			perso.set_ready(true);
			perso.get_fight().verifIfAllReady();
			SocketManager.GAME_SEND_FIGHT_PLAYER_READY_TO_FIGHT(perso.get_fight(), 3, perso.getId(), true);
		}
		if (!verif) {
			if (!perso.get_fight().getFighterByPerso(perso).isDeconnected()) {
				SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 7, "1182;" + f.getPacketsName() + "~20");
			}
			f.Disconnect();
		}
		if (current.getId() == f.getId()) {
			this.endTurn(false, current);
		}
		return true;
	}

	public boolean playerReconnect(final Player perso) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return false;
		}
		final Fighter f = this.getFighterByPerso(perso);
		if (f == null) {
			return false;
		}
		if (this.getState() == 1) {
			return false;
		}
		f.Reconnect();
		if (this.getState() == 4) {
			return false;
		}
		final ArrayList<Fighter> all = new ArrayList<Fighter>();
		all.addAll(this.getTeam0().values());
		all.addAll(this.getTeam1().values());
		for (final Fighter f2 : all) {
			if (f2.isHide()) {
				SocketManager.GAME_SEND_GA_PACKET(this, perso, 150,
						new StringBuilder(String.valueOf(f2.getId())).toString(), String.valueOf(f2.getId()) + ",4");
			}
		}
		SocketManager.GAME_SEND_Im_PACKET_TO_FIGHT(this, 7, "1184;" + f.getPacketsName());
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				if (perso == null) {
					return;
				}
				if (Fight.this.getState() == 3) {
					SocketManager.GAME_SEND_GJK_PACKET(perso, Fight.this.getState(), 0, 0, 0, 0, Fight.this.getType());
				} else if (Fight.this.getType() == 0) {
					SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 1, 1, 0, 0, Fight.this.getType());
				} else {
					SocketManager.GAME_SEND_GJK_PACKET(perso, 2, 0, 1, 0, 0, Fight.this.getType());
				}
			}
		}, 200L);
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				if (perso == null) {
					return;
				}
				SocketManager.GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(perso, Fight.this.getMap(),
						((f.getTeam() == 0) ? Fight.this.getInit0() : Fight.this.getInit1()).getId(), f);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
			}
		}, 200L);
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				if (perso == null) {
					return;
				}
				SocketManager.GAME_SEND_MAP_FIGHT_GMS_PACKETS(Fight.this, Fight.this.getMap(), perso);
			}
		}, 1500L);
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				if (perso == null) {
					return;
				}
				if (Fight.this.getState() == 2) {
					SocketManager.GAME_SEND_FIGHT_PLACES_PACKET(perso.getGameClient(), Fight.this.getMap().getPlaces(),
							Fight.this.getSt1());
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 3, 950,
							new StringBuilder(String.valueOf(perso.getId())).toString(),
							String.valueOf(perso.getId()) + "," + 8 + ",0");
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(Fight.this, 3, 950,
							new StringBuilder(String.valueOf(perso.getId())).toString(),
							String.valueOf(perso.getId()) + "," + 3 + ",0");
				} else {
					SocketManager.GAME_SEND_GS_PACKET(perso);
					SocketManager.GAME_SEND_GTL_PACKET(perso, Fight.this);
					SocketManager.GAME_SEND_GAMETURNSTART_PACKET(perso, current.getId(),
							(int) (System.currentTimeMillis() - Fight.this.launchTime));
					if ((Fight.this.getType() == 4 || Fight.this.getType() == 3)
							&& Fight.this.getAllChallenges().size() > 0) {
						for (final Map.Entry<Integer, Challenge> c : Fight.this.getAllChallenges().entrySet()) {
							if (c.getValue() == null) {
								continue;
							}
							SocketManager.GAME_SEND_CHALLENGE_PERSO(perso, c.getValue().parseToPacket());
							if (c.getValue().loose()) {
								continue;
							}
							c.getValue().challengeSpecLoose(perso);
						}
					}
					for (final Fighter f1 : Fight.this.getFighters(3)) {
						f1.sendState(perso);
					}
				}
			}
		}, 1500L);
		return true;
	}

	public void verifIfAllReady() {
		boolean val = true;
		if (this.getType() == 3) {
			for (final Fighter f : this.getTeam0().values()) {
				if (f != null) {
					if (f.getPersonnage() == null) {
						continue;
					}
					final Player perso = f.getPersonnage();
					if (perso.is_ready()) {
						continue;
					}
					val = false;
				}
			}
			if (val) {
				this.startFight();
			}
			return;
		}
		for (int a = 0; a < this.getTeam0().size(); ++a) {
			if (!this.getTeam0().get(this.getTeam0().keySet().toArray()[a]).getPersonnage().is_ready()) {
				val = false;
			}
		}
		if (this.getType() != 4 && this.getType() != 5 && this.getType() != 7 && this.getType() != 2) {
			for (int a = 0; a < this.getTeam1().size(); ++a) {
				if (!this.getTeam1().get(this.getTeam1().keySet().toArray()[a]).getPersonnage().is_ready()) {
					val = false;
				}
			}
		}
		if (this.getType() == 5 || this.getType() == 2) {
			val = false;
		}
		if (val) {
			this.startFight();
		}
	}

	public boolean verifyStillInFight() {
		for (final Fighter f : this.getTeam0().values()) {
			if (f.isCollector()) {
				return true;
			}
			if (f.isInvocation() || f.isDead() || f.getPersonnage() == null || f.getMob() != null
					|| f.getDouble() != null) {
				continue;
			}
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() != null && f.getPersonnage().get_fight() != null
					&& f.getPersonnage().get_fight().getId() == this.getId()) {
				return true;
			}
		}
		for (final Fighter f : this.getTeam1().values()) {
			if (f.isCollector()) {
				return true;
			}
			if (f.isInvocation() || f.isDead() || f.getPersonnage() == null || f.getMob() != null
					|| f.getDouble() != null) {
				continue;
			}
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() != null && f.getPersonnage().get_fight() != null
					&& f.getPersonnage().get_fight().getId() == this.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean verifyStillInFightTeam(final int guid) {
		if (this.getTeam0().containsKey(guid)) {
			for (final Fighter f : this.getTeam0().values()) {
				if (f.isCollector()) {
					return true;
				}
				if (f.isInvocation() || f.isDead() || f.getPersonnage() == null || f.getMob() != null
						|| f.getDouble() != null) {
					continue;
				}
				if (f.hasLeft()) {
					continue;
				}
				if (f.getPersonnage() != null && f.getPersonnage().get_fight() != null
						&& f.getPersonnage().get_fight().getId() == this.getId()) {
					return true;
				}
			}
		} else if (this.getTeam1().containsKey(guid)) {
			for (final Fighter f : this.getTeam1().values()) {
				if (f.isCollector()) {
					return true;
				}
				if (!f.isInvocation() || f.isDead() || f.getPersonnage() == null || f.getMob() != null
						|| f.getDouble() != null) {
					continue;
				}
				if (f.hasLeft()) {
					continue;
				}
				if (f.getPersonnage() != null && f.getPersonnage().get_fight() != null
						&& f.getPersonnage().get_fight().getId() == this.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean verifIfTeamIsDead() {
		boolean finish = true;
		for (final Map.Entry<Integer, Fighter> entry : this.getTeam1().entrySet()) {
			if (entry.getValue().isInvocation()) {
				continue;
			}
			if (!entry.getValue().isDead()) {
				finish = false;
				break;
			}
		}
		return finish;
	}

	public boolean verifIfTeamAllDead() {
		if (this.getState() >= 4) {
			return false;
		}
		boolean team0 = true;
		boolean team2 = true;
		for (final Fighter fighter : this.getTeam0().values()) {
			if (fighter.isInvocation()) {
				continue;
			}
			if (!fighter.isDead()) {
				team0 = false;
				break;
			}
		}
		for (final Fighter fighter : this.getTeam1().values()) {
			if (fighter.isInvocation()) {
				continue;
			}
			if (!fighter.isDead()) {
				team2 = false;
				break;
			}
		}
		if ((team0 || team2 || !this.verifyStillInFight()) && !this.finish) {
			this.finish = true;
			final Map<Integer, Fighter> __team0 = new HashMap<Integer, Fighter>();
			final Map<Integer, Fighter> __team2 = new HashMap<Integer, Fighter>();
			for (final Map.Entry<Integer, Fighter> entry : this.getTeam0().entrySet()) {
				if (entry.getValue().getMob() != null && entry.getValue().getMob().getTemplate().getId() == 375) {
					Bandit.getBandits().setPop(false);
				}
				__team0.put(entry.getKey(), entry.getValue());
			}
			for (final Map.Entry<Integer, Fighter> entry : this.getTeam1().entrySet()) {
				if (entry.getValue().getMob() != null && entry.getValue().getMob().getTemplate().getId() == 375) {
					Bandit.getBandits().setPop(false);
				}
				__team2.put(entry.getKey(), entry.getValue());
			}
			final boolean _team0 = team0;
			this.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					final ArrayList<Fighter> fighters = new ArrayList<Fighter>();
					fighters.addAll(__team0.values());
					fighters.addAll(__team2.values());
					Fight.this.turn.stop();
					Fight.access$1(Fight.this, null);
					try {
						String challenges = "";
						if ((Fight.this.getType() == 4 && Fight.this.getAllChallenges().size() > 0)
								|| (Fight.this.getType() == 3 && Fight.this.getAllChallenges().size() > 0)) {
							for (final Challenge challenge : Fight.this.getAllChallenges().values()) {
								if (challenge != null) {
									challenge.fightEnd();
									challenges = String.valueOf(challenges) + (challenges.isEmpty()
											? challenge.getPacketEndFight() : ("," + challenge.getPacketEndFight()));
								}
							}
						}
						Fight.this.setState(4);
						Fight.this.closeSheduler();
						final ArrayList<Fighter> winTeam = new ArrayList<Fighter>();
						final ArrayList<Fighter> looseTeam = new ArrayList<Fighter>();
						if (_team0) {
							looseTeam.addAll(__team0.values());
							winTeam.addAll(__team2.values());
						} else {
							winTeam.addAll(__team0.values());
							looseTeam.addAll(__team2.values());
						}
						Fight.this.getInit0().getPersonnage().getCurMap().removeFight(Fight.this.getId());
						if (4 == Fight.this.getType()) {
							for (final Fighter fighter : winTeam) {
								if (fighter.getPersonnage() == null) {
									continue;
								}
								fighter.getPersonnage().set_fight(null);
								if (fighter.isDeconnected()) {
									fighter.getPersonnage().setNeededEndFight(Fight.this.getType(),
											Fight.this.getMobGroup());
									fighter.getPersonnage().getCurMap().applyEndFightAction(fighter.getPersonnage());
									fighter.getPersonnage().setNeededEndFight(-1, null);
								} else {
									fighter.getPersonnage().setNeededEndFight(Fight.this.getType(),
											Fight.this.getMobGroup());
								}
							}
						}
						final String packet = Fight.this.getGE(_team0 ? 2 : 1);

						for (final Fighter fighter2 : fighters) {
							final Player player = fighter2.getPersonnage();
							if (player != null) {
								SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(Fight.this.getMap(), fighter2.getId());
							}
						}
						for (final Fighter fighter2 : fighters) {
							final Player player = fighter2.getPersonnage();
							if (player != null && Fight.this.isBegin()) {
								if (player.hasItemTemplate(7373, 1) && player.hasItemTemplate(7374, 1)
										&& player.hasItemTemplate(7375, 1) && player.hasItemTemplate(7376, 1)
										&& player.hasItemTemplate(7377, 1) && player.hasItemTemplate(7378, 1)) {
									player.removeByTemplateID(7373, 1);
									player.removeByTemplateID(7374, 1);
									player.removeByTemplateID(7375, 1);
									player.removeByTemplateID(7376, 1);
									player.removeByTemplateID(7377, 1);
									player.removeByTemplateID(7378, 1);
								}
								player.send(packet);
							}
						}
						for (final Player player2 : Fight.this.getViewer().values()) {
							player2.refreshMapAfterFight();
							player2.setSpec(false);
							SocketManager.send(player2, packet);
							if (player2.getAccount().isBanned()) {
								player2.getGameClient().kick();
							}
						}
						Fight.this.setCurPlayer(-1);
						switch (Fight.this.getType()) {
						case 0:
						case 1:
						case 2: {
							for (final Fighter fighter2 : __team2.values()) {
								final Player player = fighter2.getPersonnage();
								if (player == null) {
									continue;
								}
								player.set_duelID(-1);
								player.set_ready(false);
							}
							break;
						}
						}
						for (final Fighter fighter2 : Fight.this.getTeam0().values()) {
							final Player player = fighter2.getPersonnage();
							if (player == null) {
								continue;
							}
							player.set_duelID(-1);
							player.set_ready(false);
						}
						for (final Fighter fighter2 : Fight.this.getFighters(3)) {
							fighter2.getFightBuff().clear();
						}
						World.getMap(Fight.this.getMap().getId()).removeFight(Fight.this.getId());
						SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(World.getMap(Fight.this.getMap().getId()));
						final String str = (Fight.this.getPrism() != null)
								? (String.valueOf(Fight.this.getPrism().getMap()) + "|" + Fight.this.getPrism().getX()
										+ "|" + Fight.this.getPrism().getY())
								: "";
						Fight.this.setMap(null);
						Fight.access$4(Fight.this, null);
						Fight.this.waiter.addNow(new Runnable() {
							@Override
							public void run() {
								for (final Fighter fighter : winTeam) {
									if (fighter.getCollector() != null) {
										for (final Player z : World.getGuild(Fight.this.getGuildId()).getMembers()) {
											if (z != null && z.isOnline()) {
												SocketManager.GAME_SEND_gITM_PACKET(z,
														Collector.parseToGuild(z.get_guild().getId()));
												SocketManager.GAME_SEND_PERCO_INFOS_PACKET(z, fighter.getCollector(),
														"S");
											}
										}
										fighter.getCollector().setInFight((byte) 0);
										fighter.getCollector().set_inFightID(-1);
										fighter.getCollector().clearDefenseFight();
										for (final Player z : World.getMap(fighter.getCollector().getMap())
												.getPersos()) {
											if (z == null) {
												continue;
											}
											SocketManager.GAME_SEND_MAP_PERCO_GMS_PACKETS(z.getGameClient(),
													z.getCurMap());
										}
									}
									if (fighter.getPrism() != null) {
										for (final Player z : World.getOnlinePersos()) {
											if (z == null) {
												continue;
											}
											if (z.get_align() != Fight.this.getPrism().getAlignement()) {
												continue;
											}
											SocketManager.SEND_CS_SURVIVRE_MESSAGE_PRISME(z, str);
										}
										fighter.getPrism().setInFight(-1);
										fighter.getPrism().setFightId(-1);
										for (final Player z : World.getMap(fighter.getPrism().getMap()).getPersos()) {
											if (z == null) {
												continue;
											}
											SocketManager.SEND_GM_PRISME_TO_MAP(z.getGameClient(), z.getCurMap());
										}
									}
									if (fighter.isInvocation()) {
										continue;
									}
									if (fighter.hasLeft()) {
										continue;
									}
									Fight.this.onPlayerWin(fighter, looseTeam);
								}
								for (final Fighter fighter : looseTeam) {
									if (fighter.getCollector() != null) {
										for (final Player z : World.getGuild(Fight.this.getGuildId()).getMembers()) {
											if (z == null) {
												continue;
											}
											if (!z.isOnline()) {
												continue;
											}
											SocketManager.GAME_SEND_gITM_PACKET(z,
													Collector.parseToGuild(z.get_guild().getId()));
											SocketManager.GAME_SEND_PERCO_INFOS_PACKET(z, fighter.getCollector(), "D");
										}
										Fight.this.getMapOld().RemoveNpc(fighter.getCollector().getId());
										SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(Fight.this.getMapOld(),
												fighter.getCollector().getId());
										fighter.getCollector().reloadTimer();
										Fight.this.getCollector().delCollector(fighter.getCollector().getId());
										Database.getGame().getPercepteurData().delete(fighter.getCollector().getId());
									}
									if (fighter.getPrism() != null) {
										final World.SubArea subarea = Fight.this.getMapOld().getSubArea();
										for (final Player z2 : World.getOnlinePersos()) {
											if (z2 == null) {
												continue;
											}
											if (z2.get_align() == 0) {
												SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z2,
														String.valueOf(subarea.getId()) + "|-1|1");
											} else {
												if (z2.get_align() == Fight.this.getPrism().getAlignement()) {
													SocketManager.SEND_CD_MORT_MESSAGE_PRISME(z2, str);
												}
												SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z2,
														String.valueOf(subarea.getId()) + "|-1|0");
												if (Fight.this.getPrism().getConquestArea() != -1) {
													SocketManager.GAME_SEND_aM_ALIGN_PACKET_TO_AREA(z2,
															String.valueOf(subarea.getArea().get_id()) + "|-1");
													subarea.getArea().setPrismeID(0);
													subarea.getArea().setalignement(0);
												}
												SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z2,
														String.valueOf(subarea.getId()) + "|0|1");
											}
										}
										final int id = fighter.getPrism().getId();
										subarea.setPrismId(0);
										subarea.setAlignement(0);
										Fight.this.getMapOld().RemoveNpc(id);
										SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(Fight.this.getMapOld(), id);
										World.removePrisme(id);
										Database.getGame().getPrismeData().delete(id);
									}
									if (fighter.getMob() != null) {
										continue;
									}
									if (fighter.isInvocation()) {
										continue;
									}
									Fight.this.onPlayerLoose(fighter);
								}
							}
						}, 1000);
					} catch (Exception e) {
						e.printStackTrace();
						for (final Fighter fighter3 : fighters) {
							final Player player3 = fighter3.getPersonnage();
							if (player3 != null) {
								player3.set_duelID(-1);
								player3.set_ready(false);
								player3.set_fight(null);
								SocketManager.GAME_SEND_GV_PACKET(player3);
							}
						}
						Console.println("Error: verifIfAllDead : " + e.getMessage(), Console.Color.ERROR);
					}
					for (final Fighter fighter4 : fighters) {
						final Player player4 = fighter4.getPersonnage();
						if (player4 == null) {
							continue;
						}
						System.err.println(player4.getCurPdv() + " / " + player4.getMaxPdv());
						if (player4.get_fight() != null) {
							player4.set_fight(null);
							if (!player4.getCurMap().hasEndFightAction(4) && Fight.this.getType() == 4) {
								player4.refreshMapAfterFight();
							}
						}
						System.err.println(player4.getCurPdv() + " (1)/ " + player4.getMaxPdv());
						System.err.println(player4.get_fight());
						if (fighter4.getLevelUp()) {
							player4.fullPDV();
						} else if (fighter4.isDead() && Fight.this.getType() != 0) {
							player4.setPdv(1);
						} else
							player4.refreshLife(false);
						System.err.println(player4.getCurPdv() + " (2)/ " + player4.getMaxPdv());
						System.err.println(player4.get_fight());
						if (!player4.getCurCell().isWalkable(true)) {
							player4.teleport(player4.getCurMap(), player4.getCurMap().getRandomFreeCellId(false));
						}
						System.err.println(player4.getCurPdv() + " (3)/ " + player4.getMaxPdv());
						System.err.println(player4.get_fight());
						if (player4.getAccount().isBanned()) {
							player4.getGameClient().kick();
						}
						if (!fighter4.isDeconnected()) {
							continue;
						}
						player4.getAccount().disconnect(player4);
					}
				}
			}, 100);
		}
		return false;
	}

	public void onPlayerWin(final Fighter fighter, final ArrayList<Fighter> looseTeam) {
		final Player player = fighter.getPersonnage();
		if (player == null) {
			return;
		}
		final org.aestia.object.Object arme = player.getObjetByPos(1);
		if (arme != null && arme.getTxtStat().containsKey(812)) {
			final int statNew = Integer.parseInt(arme.getTxtStat().get(812), 16) - 1;
			if (statNew <= 0) {
				SocketManager.send(player, "Im160");
				player.removeItem(arme.getGuid(), 1, true, true);
			} else {
				arme.getTxtStat().remove(812);
				arme.addTxtStat(812, Integer.toHexString(statNew));
				SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(player, arme);
			}
		}
		if (this.getType() == 2 && player != null && this.getPrism() != null) {
			SocketManager.SEND_CP_INFO_DEFENSEURS_PRISME(player, this.getDefenders());
		}
		if (this.getType() != 0) {
			if (fighter.getPdv() <= 0) {
				player.setPdv(1);
			} else {
				if (player.getCurPdv() != player.getMaxPdv())
					player.setPdv(fighter.getPdv());
			}
		}
		if (this.getType() == 5 && player.getGuildMember() != null
				&& this.getCollector().getGuildId() == player.getGuildMember().getGuild().getId()) {
			player.teleportOldMap();
		}
		if (this.getType() == 4) {
			final org.aestia.object.Object obj = player.getObjetByPos(8);
			if (obj != null) {
				final Map<Integer, Integer> souls = new TreeMap<Integer, Integer>();
				for (final Fighter f : looseTeam) {
					if (f.getMob() == null) {
						continue;
					}
					final int id = f.getMob().getTemplate().getId();
					if (!souls.isEmpty() && souls.containsKey(id)) {
						souls.put(id, souls.get(id) + 1);
					} else {
						souls.put(id, 1);
					}
				}
				if (!souls.isEmpty()) {
					final PetEntry pet = World.getPetsEntry(obj.getGuid());
					if (pet != null) {
						pet.eatSouls(player, souls);
					}
				}
			}
		}
	}

	public void onPlayerLoose(final Fighter fighter) {
		final Player player = fighter.getPersonnage();
		if (player == null) {
			return;
		}
		if (player.getMorphMode() && player.donjon) {
			player.unsetFullMorph();
		}
		final org.aestia.object.Object arme = player.getObjetByPos(1);
		if (arme != null && arme.getTxtStat().containsKey(812)) {
			final int statNew = Integer.parseInt(arme.getTxtStat().get(812), 16) - 1;
			if (statNew <= 0) {
				SocketManager.send(player, "Im160");
				player.removeItem(arme.getGuid(), 1, true, true);
			} else {
				arme.getTxtStat().remove(812);
				arme.addTxtStat(812, Integer.toHexString(statNew));
				SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(player, arme);
			}
		}
		if (player.getObjetByPos(8) != null && this.getType() != 0) {
			final org.aestia.object.Object obj = player.getObjetByPos(8);
			if (obj != null) {
				final PetEntry pets = World.getPetsEntry(obj.getGuid());
				if (pets != null) {
					pets.looseFight(player);
				}
			}
		}
		if (player.getObjetByPos(24) != null) {
			player.setMascotte(0);
		}
		if (this.getType() == 2 && player != null && this.getPrism() != null) {
			SocketManager.SEND_CP_INFO_DEFENSEURS_PRISME(player, this.getDefenders());
		}
		if (this.getType() == 1 || this.getType() == 2) {
			int honor = player.get_honor() - 500;
			if (honor < 0) {
				honor = 0;
			}
			player.set_honor(honor);
			if (player.isOnline()) {
				SocketManager.GAME_SEND_Im_PACKET(player, "076;" + honor);
			}
		}
		if (this.getType() != 0) {
			final int loose = Formulas.getLoosEnergy(player.getLevel(), this.getType() == 1, this.getType() == 5);
			final int energy = player.getEnergy() - loose;
			player.setEnergy((energy < 0) ? 0 : energy);
			if (player.isOnline()) {
				SocketManager.GAME_SEND_Im_PACKET(player, "034;" + loose);
			}
			if (energy <= 0) {
				if (this.getType() == 1 && fighter.getTraqued()) {
					if (this.getTeam1().containsValue(fighter)) {
						player.teleportFaction(this.getAlignementOfTraquer(this.getTeam0().values(), player));
					} else {
						player.teleportFaction(this.getAlignementOfTraquer(this.getTeam1().values(), player));
					}
					player.setEnergy(1);
				} else {
					player.setFuneral();
				}
			} else if (this.getType() == 1 && fighter.getTraqued()) {
				if (this.getTeam1().containsValue(fighter)) {
					player.teleportFaction(this.getAlignementOfTraquer(this.getTeam0().values(), player));
				} else {
					player.teleportFaction(this.getAlignementOfTraquer(this.getTeam1().values(), player));
				}
			} else if (player.getCurMap().getSubArea().getId() == 319
					|| player.getCurMap().getSubArea().getId() == 210) {
				player.teleportLaby((short) 9558, 224);
				player.getWaiter().addNow(new Runnable() {
					@Override
					public void run() {
						Toror.sendPacketMap(player);
					}
				}, 3500L);
				player.setPdv(1);
			} else {
				player.warpToSavePos();
				player.setPdv(1);
			}
		}
	}

	public int getAlignementOfTraquer(final Collection<Fighter> fighters, final Player player) {
		for (final Fighter fighter : fighters) {
			if (fighter.getPersonnage() != null && fighter.getPersonnage().get_traque().getTraque() == player) {
				return fighter.getPersonnage().get_align();
			}
		}
		return 0;
	}

	public void onGK(final Player perso) {
		final Fighter current = this.getFighterByOrdreJeu();
		if (current == null) {
			return;
		}
		if (this.getCurAction().equals("") || current.getId() != perso.getId() || this.getState() != 3) {
			return;
		}
		if (Main.modDebug) {
			Console.println("Le fighter " + this.getCurPlayer() + " vient de terminer son deplacement dans le combat "
					+ this.getId() + " de type " + this.getType() + " !", Console.Color.INFORMATION);
		}
		SocketManager.GAME_SEND_GAMEACTION_TO_FIGHT(this, 7, this.getCurAction());
		SocketManager.GAME_SEND_GAF_PACKET_TO_FIGHT(this, 7, 2, current.getId());
		final ArrayList<Trap> P = new ArrayList<Trap>();
		P.addAll(this.getAllTraps());
		for (final Trap p : P) {
			final Fighter F = this.getFighterByPerso(perso);
			final int dist = Pathfinding.getDistanceBetween(this.getMap(), p.getCell().getId(), F.getCell().getId());
			if (dist <= p.getSize()) {
				p.onTraped(F);
			}
			if (this.getState() == 4) {
				break;
			}
		}
		this.setCurAction("");
	}

	public String getGE(final int win) {
		try {
			final long time = System.currentTimeMillis() - this.getStartTime();
			final int initGUID = this.getInit0().getId();
			int type = 0;
			if (this.getType() == 1 || this.getType() == 2) {
				type = 1;
			}
			if (this.getType() == 5) {
				type = 0;
			}
			final StringBuilder Packet = new StringBuilder();
			Packet.append("GE").append(time);
			if (this.getType() == 4 && this.getMobGroup() != null) {
				Packet.append(';').append(this.getMobGroup().getStarBonus());
			}
			Packet.append("|").append(initGUID).append("|").append(type).append("|");
			ArrayList<Fighter> TEAM1 = new ArrayList<Fighter>();
			final ArrayList<Fighter> TEAM2 = new ArrayList<Fighter>();
			if (win == 1) {
				TEAM1.addAll(this.getTeam0().values());
				TEAM2.addAll(this.getTeam1().values());
			} else {
				TEAM1.addAll(this.getTeam1().values());
				TEAM2.addAll(this.getTeam0().values());
			}
			boolean traquePVP = false;
			Player curp = null;
			for (final Fighter F : TEAM1) {
				if (F.isInvocation()) {
					continue;
				}
				if (TEAM1.size() != 1) {
					continue;
				}
				curp = F.getPersonnage();
			}
			for (final Fighter F : TEAM2) {
				if (F.isInvocation()) {
					continue;
				}
				if (curp == null || curp.get_traque() == null || curp.get_traque().getTraque() != F.getPersonnage()) {
					continue;
				}
				SocketManager.GAME_SEND_MESSAGE(curp,
						"Thomas Sacre : Contrat fini, reviens me voir pour r\u00e9cuperer ta r\u00e9compense.",
						"000000");
				curp.get_traque().setTime(-2L);
				traquePVP = true;
				F.setTraqued(true);
			}
			int groupPP = 0;
			int minkamas = 0;
			int maxkamas = 0;
			for (final Fighter F2 : TEAM1) {
				if (!F2.isInvocation() || (F2.getMob() != null && F2.getMob().getTemplate().getId() == 285)) {
					groupPP += F2.getTotalStats().getEffect(176);
				}
			}
			if (groupPP < 0) {
				groupPP = 0;
			}
			double factChalDrop = 100.0;
			if (this.getType() == 4 && this.getAllChallenges().size() > 0) {
				for (final Map.Entry<Integer, Challenge> c : this.getAllChallenges().entrySet()) {
					if (c.getValue() != null) {
						if (!c.getValue().getWin()) {
							continue;
						}
						factChalDrop += c.getValue().getDrop();
					}
				}
				factChalDrop += this.getMobGroup().getStarBonus();
			}
			factChalDrop /= 100.0;
			final ArrayList<World.Drop> possibleDrops = new ArrayList<World.Drop>();
			final ArrayList<World.Drop> possibleMeats = new ArrayList<World.Drop>();
			Collection<org.aestia.object.Object> collectorDrops = null;
			if (this.getType() == 5 && win == 1) {
				minkamas = (maxkamas = (int) Math.ceil(this.collector.getKamas() / TEAM1.size()));
				collectorDrops = this.getCollector().getDrops();
			} else {
				for (final Fighter F3 : TEAM2) {
					if (!F3.isInvocation()) {
						if (F3.getMob() == null) {
							continue;
						}
						minkamas += F3.getMob().getTemplate().getMinKamas();
						maxkamas += F3.getMob().getTemplate().getMaxKamas();
						for (final World.Drop D : F3.getMob().getTemplate().getDrops()) {
							if (D.getProspection() <= groupPP && D.getAction() != 1) {
								final float taux = (float) (factChalDrop * D.getTaux() * Config.getInstance().rateDrop);
								boolean b = true;
								for (final World.Drop D2 : possibleDrops) {
									if (D2.getTemplateId() == D.getTemplateId() && D2.getTaux() == taux) {
										D2.setMax(D2.getMax() + D.getMax());
										D2.setMax_combat(D2.getMax_combat() + D.getMax_combat());
										b = false;
									}
								}
								if (!b) {
									continue;
								}
								possibleDrops.add(new World.Drop(D.getTemplateId(), 0, taux, D.getMax(),
										D.getMax_combat(), D.getLevel(), D.getAction(), D.getCondition()));
							} else {
								if (D.getAction() != 1) {
									continue;
								}
								possibleMeats.add(new World.Drop(D.getTemplateId(), 0, D.getTaux(), D.getMax(),
										D.getMax_combat(), D.getLevel(), D.getAction(), D.getCondition()));
							}
						}
					}
				}
			}
			final ArrayList<World.Drop> possibleDropsCollector = new ArrayList<World.Drop>(possibleDrops);
			final ArrayList<Fighter> Temp = new ArrayList<Fighter>();
			Fighter curMax = null;
			while (Temp.size() < TEAM1.size()) {
				int curPP = -1;
				for (final Fighter F4 : TEAM1) {
					if (F4.getTotalStats().getEffect(176) > curPP && !Temp.contains(F4)) {
						curMax = F4;
						curPP = F4.getTotalStats().getEffect(176);
					}
				}
				Temp.add(curMax);
			}
			TEAM1.clear();
			TEAM1.addAll(Temp);
			long totalXP = 0L;
			for (final Fighter F5 : TEAM2) {
				if (F5.getMob() != null) {
					totalXP += F5.getMob().getBaseXp();
				}
			}
			boolean mobCapturable = true;
			for (final Fighter F6 : TEAM2) {
				try {
					mobCapturable &= F6.getMob().getTemplate().isCapturable();
				} catch (Exception e3) {
					mobCapturable = false;
					break;
				}
			}
			this.isCapturable |= mobCapturable;
			if (this.isCapturable() && !Config.contains(Config.arenaMap, this.getMapOld().getId())) {
				boolean isFirst = true;
				int maxLvl = 0;
				String pierreStats = "";
				for (final Fighter F7 : TEAM2) {
					pierreStats = String.valueOf(pierreStats) + (isFirst ? "" : "|") + F7.getMob().getTemplate().getId()
							+ "," + F7.getLvl();
					isFirst = false;
					if (F7.getLvl() > maxLvl) {
						maxLvl = F7.getLvl();
					}
				}
				this.setFullSoul(new SoulStone(World.getNewItemGuid(), 1, 7010, -1, pierreStats));
				for (final Fighter F7 : TEAM1) {
					if (!F7.isInvocation() && F7.haveState(2)) {
						this.getCapturer().add(F7);
					}
				}
				if (this.getCapturer().size() > 0 && !Config.contains(Config.arenaMap, this.getMapOld().getId())) {
					for (int i = 0; i < this.getCapturer().size(); ++i) {
						try {
							final Fighter f = this.getCapturer()
									.get(Formulas.getRandomValue(0, this.getCapturer().size() - 1));
							if (f.getPersonnage().getObjetByPos(1).getTemplate().getType() != 83) {
								this.getCapturer().remove(f);
							} else {
								final World.Couple<Integer, Integer> pierreJoueur = Formulas
										.decompPierreAme(f.getPersonnage().getObjetByPos(1));
								if (pierreJoueur.second < maxLvl) {
									this.getCapturer().remove(f);
								} else if (Formulas.getRandomValue(1, 100) <= Formulas
										.totalCaptChance(pierreJoueur.first, f.getPersonnage())) {
									final int pierreVide = f.getPersonnage().getObjetByPos(1).getGuid();
									f.getPersonnage().deleteItem(pierreVide);
									SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(f.getPersonnage(), pierreVide);
									this.setCaptWinner(f.getId());
									break;
								}
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (this.getType() == 4 || this.getType() == 3) {
				for (final Fighter F6 : TEAM1) {
					if (F6.getPersonnage() == null) {
						continue;
					}
					if (F6.getPersonnage().getQuestPerso().isEmpty()) {
						continue;
					}
					for (final Fighter ennemy : TEAM2) {
						if (ennemy.getMob() == null) {
							continue;
						}
						final Player p = F6.getPersonnage();
						for (final Quest.Quest_Perso questP : p.getQuestPerso().values()) {
							final Quest quest = questP.getQuest();
							for (final Quest_Etape qEtape : quest.getQuestEtapeList()) {
								if (!questP.isQuestEtapeIsValidate(qEtape)
										&& (qEtape.getType() == 0 || qEtape.getType() == 6)
										&& qEtape.getMonsterId() == ennemy.getMob().getTemplate().getId()) {
									p.getQuestPersoByQuest(qEtape.getQuestData()).getMonsterKill()
											.put(ennemy.getMob().getTemplate().getId(), (short) 1);
									qEtape.getQuestData().updateQuestData(F6.getPersonnage(), false, 2);
								}
							}
						}
					}
				}
			}
			boolean Amande = false;
			boolean Rousse = false;
			boolean Doree = false;
			for (final Fighter F7 : TEAM2) {
				try {
					if (F7.getMob().getTemplate().getId() == 171) {
						Amande = true;
					}
					if (F7.getMob().getTemplate().getId() == 200) {
						Rousse = true;
					}
					if (F7.getMob().getTemplate().getId() != 666) {
						continue;
					}
					Doree = true;
				} catch (Exception e4) {
					Amande = false;
					Rousse = false;
					Doree = false;
					break;
				}
			}
			if (Amande || Rousse || Doree) {
				for (final Fighter F7 : TEAM1) {
					if (!F7.isInvocation() && F7.haveState(10)) {
						this.getTrainer().add(F7);
					}
				}
				if (this.getTrainer().size() > 0) {
					for (int i = 0; i < this.getTrainer().size(); ++i) {
						try {
							final Fighter f = this.getTrainer()
									.get(Formulas.getRandomValue(0, this.getTrainer().size() - 1));
							if (f.getPersonnage().getObjetByPos(1).getTemplate().getType() != 99) {
								this.getTrainer().remove(f);
							} else {
								final int chanc = Formulas.getRandomValue(1, 100);
								final int appriChance = Formulas.totalAppriChance(Amande, Rousse, Doree,
										f.getPersonnage());
								if (chanc <= appriChance) {
									final int Filet = f.getPersonnage().getObjetByPos(1).getGuid();
									f.getPersonnage().deleteItem(Filet);
									SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(f.getPersonnage(), Filet);
									this.setTrainerWinner(f.getId());
									break;
								}
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				}
			}
			int memberGuild = 0;
			if (this.getType() == 5 && win == 1) {
				for (final Fighter j : TEAM1) {
					if (j.getPersonnage() != null && j.getPersonnage().getGuildMember() != null) {
						++memberGuild;
					}
				}
			}
			int lvlLoosers = 0;
			int lvlWinners = 0;
			int lvlMaxLooser = 0;
			int lvlMax = 0;
			int lvlMin = 0;
			int challXp = 0;
			byte nbbonus = 0;
			for (final Challenge c2 : this.getAllChallenges().values()) {
				if (c2 != null && c2.getWin()) {
					challXp += c2.getXp();
				}
			}
			for (final Fighter entry : TEAM2) {
				lvlLoosers += entry.getLvl();
			}
			for (final Fighter entry : TEAM1) {
				lvlWinners += entry.getLvl();
				if (entry.getLvl() > lvlMaxLooser && entry.getPersonnage() != null) {
					lvlMaxLooser = entry.getLvl();
				}
			}
			if (lvlLoosers > lvlWinners) {
				lvlMax = lvlLoosers;
				lvlMin = lvlWinners;
			} else {
				lvlMax = lvlWinners;
				lvlMin = lvlLoosers;
			}
			for (final Fighter entry : TEAM1) {
				if (entry.getLvl() > lvlMaxLooser / 3 && entry.getPersonnage() != null) {
					++nbbonus;
				}
			}
			if (lvlWinners <= 0) {
				lvlWinners = 1;
			}
			double rapport = 1 + lvlLoosers / lvlWinners;
			if (rapport <= 1.3) {
				rapport = 1.3;
			}
			Collections.sort(TEAM1);
			final Map<Integer, StringBuilder> gains = new HashMap<Integer, StringBuilder>();
			for (final Fighter k : TEAM1) {
				if (k.isInvocation() && k.getMob() != null && k.getMob().getTemplate().getId() != 285) {
					continue;
				}
				if (k.getDouble() != null) {
					continue;
				}
				if (k.getPersonnage() != null && this.getType() != 0) {
					k.getPersonnage().calculTurnCandy();
				}
				if (this.getType() == 5 || this.getType() == 4 || this.getType() == 0 || this.getType() == 3) {
					String drops = "";
					long winxp = 0L;
					long guildxp = 0L;
					long mountxp = 0L;
					int winKamas = 0;
					final AtomicReference<Long> XP = new AtomicReference<Long>();
					if (k.getPersonnage() != null) {
						winxp = Formulas.getXpWinPvm(k, TEAM1, TEAM2, totalXP,
								(this.getMobGroup() != null) ? this.getMobGroup().getStarBonus() : 0);
					}
					XP.set(winxp);
					if (this.getType() == 5 && win == 1) {
						final Player player = k.getPersonnage();
						if (player != null && memberGuild != 0 && player.getGuildMember() != null) {
							guildxp = (int) Math.floor(this.getCollector().getXp() / memberGuild);
						}
					} else {
						guildxp = Formulas.getGuildXpWin(k, XP);
					}
					if (k.getPersonnage() != null && k.getPersonnage().isOnMount()) {
						mountxp = Formulas.getMountXpWin(k, XP);
						k.getPersonnage().getMount().addXp(mountxp);
						SocketManager.GAME_SEND_Re_PACKET(k.getPersonnage(), "+", k.getPersonnage().getMount());
					}
					if (this.getType() == 5 && win == 1) {
						winKamas = (int) Math.floor(this.getCollector().getKamas() / TEAM1.size());
					} else {
						winKamas = Formulas.getKamasWin(k, TEAM1, minkamas, maxkamas);
					}
					if (k.getPersonnage() != null) {
						final Map<Integer, Integer> mobs = new HashMap<Integer, Integer>();
						for (final Fighter mob : TEAM2) {
							if (mob.getMob() != null) {
								if (mobs.get(mob.getMob().getTemplate().getId()) != null) {
									mobs.put(mob.getMob().getTemplate().getId(),
											mobs.get(mob.getMob().getTemplate().getId()) + 1);
								} else {
									mobs.put(mob.getMob().getTemplate().getId(), 1);
								}
							}
						}
					}
					final Map<Integer, Integer> itemWon = new TreeMap<Integer, Integer>();
					final Map<Integer, Integer> itemWon2 = new TreeMap<Integer, Integer>();
					if (this.getType() == 5 && win == 1) {
						final int ItemWonPerPerso = (int) Math.floor(collectorDrops.size() / TEAM1.size());
						int l = 0;
						final ArrayList<org.aestia.object.Object> temp = new ArrayList<org.aestia.object.Object>();
						temp.addAll(collectorDrops);
						Collections.shuffle(temp);
						for (final org.aestia.object.Object object : temp) {
							if (l > ItemWonPerPerso) {
								continue;
							}
							itemWon.put(object.getTemplate().getId(), object.getQuantity());
							collectorDrops.remove(object);
							World.removeItem(object.getGuid());
							++l;
						}
					} else {
						final ArrayList<World.Drop> temp2 = new ArrayList<World.Drop>();
						temp2.addAll(possibleDrops);
						Collections.shuffle(temp2);
						for (final World.Drop D3 : temp2) {
							int m = D3.getMax();
							while (m > 0) {
								final int t = (int) (D3.getTaux() * (k.getPros() / 100.0));
								final int jet = Formulas.getRandomValue(0, 1000000);
								boolean ok = false;
								switch (D3.getAction()) {
								case 4: {
									if (ConditionParser.validConditions(k.getPersonnage(), "QE=" + D3.getCondition())) {
										ok = true;
										break;
									}
									break;
								}
								}
								if (jet < t || ok) {
									final ObjectTemplate OT = World.getObjTemplate(D3.getTemplateId());
									if (OT == null) {
										continue;
									}
									boolean itsOk = false;
									boolean setMax = true;
									boolean dontPut = false;
									Label_4414: {
										switch (D3.getAction()) {
										case -1: {
											itsOk = true;
											setMax = true;
											break;
										}
										case 1: {
											break;
										}
										case 2: {
											String[] split;
											for (int length = (split = D3.getCondition()
													.split(",")).length, n = 0; n < length; ++n) {
												final String id = split[n];
												if (id.equals(String.valueOf(this.getMap().getId()))) {
													itsOk = true;
													setMax = false;
													dontPut = true;
												}
											}
											break;
										}
										case 3: {
											if (this.getMapOld().getSubArea() == null) {
												break;
											}
											final String condition;
											switch (condition = D3.getCondition()) {
											case "0": {
												if (this.getMapOld().getSubArea().getAlignement() == 2) {
													itsOk = true;
													break Label_4414;
												}
												break Label_4414;
											}
											case "1": {
												if (this.getMapOld().getSubArea().getAlignement() == 1) {
													itsOk = true;
													break Label_4414;
												}
												break Label_4414;
											}
											case "2": {
												if (this.getMapOld().getSubArea().getAlignement() == 2) {
													itsOk = true;
													break Label_4414;
												}
												break Label_4414;
											}
											case "3": {
												if (this.getMapOld().getSubArea().getAlignement() == 3) {
													itsOk = true;
													break Label_4414;
												}
												break Label_4414;
											}
											default:
												break;
											}
											itsOk = true;
											break;
										}
										case 4: {
											if (ConditionParser.validConditions(k.getPersonnage(),
													"QE=" + D3.getCondition())) {
												itsOk = true;
												dontPut = true;
												setMax = false;
												break;
											}
											break;
										}
										case 5: {
											if (k.getPersonnage() == null) {
												break;
											}
											if (k.getPersonnage().getNbItemTemplate(OT.getId()) > 0) {
												break;
											}
											itsOk = true;
											dontPut = true;
											break;
										}
										case 6: {
											final int item = Integer.parseInt(D3.getCondition());
											if (item == 2039) {
												if (this.getMap().getId() != 7388) {
													itsOk = false;
													break;
												}
												if (k.getPersonnage().hasItemTemplate(item, 1)) {
													itsOk = true;
													break;
												}
												break;
											} else {
												if (k.getPersonnage().hasItemTemplate(item, 1)) {
													itsOk = true;
													break;
												}
												break;
											}
										}
										case 7: {
											if (k.getPersonnage() == null) {
												break;
											}
											if (k.getPersonnage().hasItemTemplate(OT.getId(), 1)) {
												break;
											}
											String[] split2;
											for (int length2 = (split2 = D3.getCondition()
													.split(",")).length, n2 = 0; n2 < length2; ++n2) {
												final String id2 = split2[n2];
												if (id2.equals(String.valueOf(this.getMap().getId()))) {
													itsOk = true;
													setMax = false;
													dontPut = true;
												}
											}
											break;
										}
										case 999: {
											itsOk = true;
											dontPut = true;
											break;
										}
										default: {
											itsOk = true;
											setMax = true;
											break;
										}
										}
									}
									if (!itsOk) {
										--m;
										continue;
									}
									itemWon.put(OT.getId(),
											((itemWon.get(OT.getId()) == null) ? 0 : itemWon.get(OT.getId())) + 1);
									if (!setMax) {
										--m;
										continue;
									}
									D3.setMax_combat(D3.getMax_combat() - 1);
									if (D3.getMax_combat() == 0) {
										possibleDrops.remove(D3);
									}
									if (dontPut) {
										--m;
										continue;
									}
								}
								--m;
							}
						}
						if (k.getPersonnage() != null) {
							final ArrayList<World.Drop> temp3 = new ArrayList<World.Drop>();
							temp3.addAll(possibleMeats);
							Collections.shuffle(temp3);
							int k2 = 0;
							final int ItemWonPerPerso2 = (int) Math.floor(temp3.size() / TEAM1.size());
							for (final World.Drop D4 : temp3) {
								if (k2 > ItemWonPerPerso2) {
									continue;
								}
								final int t2 = (int) (D4.getTaux() * (k.getPros() / 100.0));
								final int jet2 = Formulas.getRandomValue(0, 1000000);
								if (jet2 < t2) {
									if (k.getPersonnage().getMetierByID(41) == null
											&& !k.getPersonnage().hasEquiped(1934) && D4.getAction() == -1) {
										continue;
									}
									final ObjectTemplate OT2 = World.getObjTemplate(D4.getTemplateId());
									if (OT2 == null) {
										continue;
									}
									if (D4.getAction() == 1 && k.getPersonnage().hasEquiped(1934)
											&& k.getPersonnage().getMetierByID(41) != null
											&& k.getPersonnage().getMetierByID(41).get_lvl() >= D4.getLevel()) {
										itemWon2.put(OT2.getId(),
												((itemWon2.get(OT2.getId()) == null) ? 0 : itemWon2.get(OT2.getId()))
														+ 1);
									}
									D4.setMax(D4.getMax() - 1);
									if (D4.getMax() == 0) {
										possibleMeats.remove(D4);
									}
								}
								++k2;
							}
						}
					}
					if (k.getPersonnage() != null || (k.getMob() != null && k.getMob().getTemplate().getId() == 285)) {
						if (k.getPersonnage() != null) {
							if (k.getId() == this.getCaptWinner() && this.getFullSoul() != null) {
								if (drops.length() > 0) {
									drops = String.valueOf(drops) + ",";
								}
								drops = String.valueOf(drops) + this.getFullSoul().getTemplate().getId() + "~" + 1;
								if (k.getPersonnage().addObjet(this.getFullSoul(), false)) {
									World.addObjet(this.getFullSoul(), true);
								}
							}
							if (this.getTrainerWinner() != -1 && k.getId() == this.getTrainerWinner()
									&& k.getPersonnage().getMount() == null) {
								final int Couleur = Formulas.getCouleur(Amande, Rousse, Doree);
								final Dragodinde DD = new Dragodinde(Couleur, k.getId(), true);
								k.getPersonnage().setMount(DD);
								SocketManager.GAME_SEND_Re_PACKET(k.getPersonnage(), "+", DD);
								SocketManager.GAME_SEND_Rx_PACKET(k.getPersonnage());
								SocketManager.GAME_SEND_STATS_PACKET(k.getPersonnage());
								switch (Couleur) {
								case 20: {
									drops = String.valueOf(drops) + "7807~1,";
									break;
								}
								case 10: {
									drops = String.valueOf(drops) + "7809~1,";
									break;
								}
								case 18: {
									drops = String.valueOf(drops) + "7864~1,";
									break;
								}
								}
							}
						}
						for (final Map.Entry<Integer, Integer> entry2 : itemWon.entrySet()) {
							final ObjectTemplate OT3 = World.getObjTemplate(entry2.getKey());
							if (OT3 == null) {
								continue;
							}
							if (drops.length() > 0) {
								drops = String.valueOf(drops) + ",";
							}
							drops = String.valueOf(drops) + entry2.getKey() + "~" + entry2.getValue();
							if (OT3.getType() == 32 && k.getPersonnage() != null) {
								k.getPersonnage().setMascotte(entry2.getKey());
							} else {
								final org.aestia.object.Object obj = OT3.createNewItem(entry2.getValue(), false);
								if (k.getPersonnage() != null && k.getPersonnage().addObjet(obj, true)) {
									World.addObjet(obj, true);
								} else {
									if (!k.isInvocation() || k.getMob().getTemplate().getId() != 285
											|| !k.getInvocator().getPersonnage().addObjet(obj, true)) {
										continue;
									}
									World.addObjet(obj, true);
								}
							}
						}
						for (final Map.Entry<Integer, Integer> entry2 : itemWon2.entrySet()) {
							final ObjectTemplate OT3 = World.getObjTemplate(entry2.getKey());
							if (OT3 == null) {
								continue;
							}
							if (drops.length() > 0) {
								drops = String.valueOf(drops) + ",";
							}
							drops = String.valueOf(drops) + entry2.getKey() + "~" + entry2.getValue();
							final org.aestia.object.Object obj = OT3.createNewItem(entry2.getValue(), false);
							if (k.getPersonnage() != null && k.getPersonnage().addObjet(obj, true)) {
								World.addObjet(obj, true);
							} else {
								if (!k.isInvocation() || k.getMob().getTemplate().getId() != 285
										|| !k.getInvocator().getPersonnage().addObjet(obj, true)) {
									continue;
								}
								World.addObjet(obj, true);
							}
						}
						if (this.getType() == 3) {
							for (final Fighter F8 : TEAM2) {
								final Monster.MobGrade mob2 = F8.getMob();
								final Monster m2 = mob2.getTemplate();
								if (m2 == null) {
									Console.println("Erreur sur un grade/template de monstre de type dopeul.",
											Console.Color.ERROR);
								} else {
									final int IDmob = m2.getId();
									if (drops.length() > 0) {
										drops = String.valueOf(drops) + ",";
									}
									drops = String.valueOf(drops) + Constant.getCertificatByDopeuls(IDmob) + "~1";
									final ObjectTemplate OT4 = World
											.getObjTemplate(Constant.getCertificatByDopeuls(IDmob));
									final org.aestia.object.Object obj2 = OT4.createNewItem(1, false);
									if (k.getPersonnage().addObjet(obj2, true)) {
										World.addObjet(obj2, true);
									}
									Database.getStatique().getItemData().save(obj2, false);
									obj2.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
									Database.getStatique().getPlayerData().update(k.getPersonnage(), true);
									SocketManager.GAME_SEND_Ow_PACKET(k.getPersonnage());
								}
							}
						}
						if (this.getType() == 4 && k.getPersonnage() != null) {
							final Player player2 = k.getPersonnage();
							int bouftou = 0;
							int tofu = 0;
							for (final Monster.MobGrade mob3 : this.getMobGroup().getMobs().values()) {
								switch (mob3.getTemplate().getId()) {
								case 289: {
									if (player2.getCurMap().getSubArea().getId() != 211) {
										continue;
									}
									Monster.MobGroup.MAITRE_CORBAC.repop(player2.getCurMap().getId());
									continue;
								}
								default: {
									continue;
								}
								case 793: {
									++bouftou;
									continue;
								}
								case 794: {
									++tofu;
									continue;
								}
								}
							}
							if (Config.getInstance().HALLOWEEN && (bouftou > 0 || tofu > 0)
									&& !player2.hasEquiped(976)) {
								if (bouftou > tofu) {
									player2.setMalediction(8169);
									player2.setFullMorph(Formulas.getRandomValue(16, 20), false, false);
								} else if (tofu > bouftou) {
									player2.setMalediction(8170);
									player2.setFullMorph(Formulas.getRandomValue(21, 25), false, false);
								} else {
									switch (Formulas.getRandomValue(1, 2)) {
									case 1: {
										player2.setMalediction(8169);
										player2.setFullMorph(Formulas.getRandomValue(16, 20), false, false);
										break;
									}
									case 2: {
										player2.setMalediction(8170);
										player2.setFullMorph(Formulas.getRandomValue(21, 25), false, false);
										break;
									}
									}
								}
							}
							switch (k.getPersonnage().getCurMap().getId()) {
							case 8984: {
								final org.aestia.object.Object obj = World.getObjTemplate(8012).createNewItem(1, false);
								if (k.getPersonnage().addObjet(obj, true)) {
									World.addObjet(obj, true);
								}
								drops = String.valueOf(drops) + ((drops.length() > 0) ? "," : "") + "8012~1";
								break;
							}
							}
						}
						winxp = XP.get();
						if (winxp != 0L && k.getPersonnage() != null) {
							if (k.getPersonnage().getMorphMode()) {
								final org.aestia.object.Object obj3 = k.getPersonnage().getObjetByPos(1);
								if (obj3 != null && Constant.isIncarnationWeapon(obj3.getTemplate().getId())
										&& k.getPersonnage().addXpIncarnations(winxp)) {
									k.setLevelUp(true);
								}
							} else if (k.getPersonnage().addXp(winxp)) {
								k.setLevelUp(true);
							}
						}
						if (winKamas != 0 && k.getPersonnage() != null) {
							k.getPersonnage().addKamas(winKamas);
						}
						if (winKamas != 0 && k.isInvocation() && k.getInvocator().getPersonnage() != null) {
							k.getInvocator().getPersonnage().addKamas(winKamas);
						}
						if (guildxp > 0L && k.getPersonnage().getGuildMember() != null) {
							k.getPersonnage().getGuildMember().giveXpToGuild(guildxp);
						}
					}
					final StringBuilder p2 = new StringBuilder();
					p2.append("2;");
					p2.append(k.getId()).append(";");
					p2.append(k.getPacketsName()).append(";");
					p2.append(k.getLvl()).append(";");
					p2.append(k.isDead() ? "1" : "0").append(";");
					p2.append(k.xpString(";")).append(";");
					p2.append((winxp == 0L) ? "" : winxp).append(";");
					p2.append((guildxp == 0L) ? "" : guildxp).append(";");
					p2.append((mountxp == 0L) ? "" : mountxp).append(";");
					p2.append(drops).append(";");
					p2.append((winKamas == 0) ? "" : winKamas).append("|");
					gains.put(k.getId(), p2);
				} else {
					int winH = 0;
					int winD = 0;
					if (type == 1) {
						if (k.isInvocation()) {
							continue;
						}
						if (k.isPrisme()) {
							continue;
						}
						if (k.isMob()) {
							continue;
						}
						if (this.getType() == 1) {
							if (this.getInit1().getPersonnage().get_align() != 0
									&& this.getInit0().getPersonnage().get_align() != 0) {
								if (this.getInit1().getPersonnage().getAccount().getCurIP()
										.compareTo(this.getInit0().getPersonnage().getAccount().getCurIP()) != 0
										|| Main.allowMulePvp) {
									winH = Formulas.calculHonorWin(TEAM1, TEAM2, k);
								}
								if (k.getPersonnage().getDeshonor() > 0) {
									winD = -1;
								}
							}
						} else if (this.getType() == 2) {
							winH = Formulas.calculHonorWin(TEAM1, TEAM2, k);
						}
						final Player P = k.getPersonnage();
						if (P.get_align() != 0) {
							if (P.get_honor() + winH < 0) {
								winH = -P.get_honor();
							}
							P.addHonor(winH);
							P.setDeshonor(P.getDeshonor() + winD);
						}
						final StringBuilder p3 = new StringBuilder();
						p3.append("2;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
								.append(k.getLvl()).append(";").append(k.isDead() ? "1" : "0").append(";");
						p3.append((P.get_align() != -1) ? World.getExpLevel(P.getGrade()).pvp : 0).append(";");
						p3.append(P.get_honor()).append(";");
						int maxHonor = World.getExpLevel(P.getGrade() + 1).pvp;
						if (maxHonor == -1) {
							maxHonor = World.getExpLevel(P.getGrade()).pvp;
						}
						p3.append((P.get_align() != -1) ? maxHonor : 0).append(";");
						p3.append(winH).append(";");
						p3.append(P.getGrade()).append(";");
						p3.append(P.getDeshonor()).append(";");
						p3.append(winD);
						p3.append(";");
						if (traquePVP) {
							p3.append("10275~2");
						}
						p3.append(";0;0;0;0;0|");
						p3.append(";;0;0;0;0;0|");
						gains.put(k.getId(), p3);
					} else {
						if (this.getType() != 2) {
							continue;
						}
						final Player P = k.getPersonnage();
						if (P != null) {
							if (P.get_honor() + winH < 0) {
								winH = -P.get_honor();
							}
							P.addHonor(winH);
							if (P.getDeshonor() - winD < 0) {
								winD = 0;
							}
							P.setDeshonor(P.getDeshonor() - winD);
							final StringBuilder p3 = new StringBuilder();
							p3.append("2;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
									.append(k.getLvl()).append(";").append(k.isDead() ? "1" : "0").append(";");
							p3.append((P.get_align() != -1) ? World.getExpLevel(P.getGrade()).pvp : 0).append(";");
							p3.append(P.get_honor()).append(";");
							int maxHonor = World.getExpLevel(P.getGrade() + 1).pvp;
							if (maxHonor == -1) {
								maxHonor = World.getExpLevel(P.getGrade()).pvp;
							}
							p3.append((P.get_align() != -1) ? maxHonor : 0).append(";");
							p3.append(winH).append(";");
							p3.append(P.getGrade()).append(";");
							p3.append(P.getDeshonor()).append(";");
							p3.append(winD);
							p3.append(";;0;0;0;0;0|");
							gains.put(k.getId(), p3);
						} else {
							final Prism prisme = k.getPrism();
							winH *= 5;
							if (prisme.getHonor() + winH < 0) {
								winH = -prisme.getHonor();
							}
							winH *= 3;
							prisme.addHonor(winH);
							final StringBuilder p4 = new StringBuilder();
							p4.append("2;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
									.append(k.getLvl()).append(";").append(k.isDead() ? "1" : "0").append(";");
							p4.append(World.getExpLevel(prisme.getLevel()).pvp).append(";");
							p4.append(prisme.getHonor()).append(";");
							int maxHonor2 = World.getExpLevel(prisme.getLevel() + 1).pvp;
							if (maxHonor2 == -1) {
								maxHonor2 = World.getExpLevel(prisme.getLevel()).pvp;
							}
							p4.append(maxHonor2).append(";");
							p4.append(winH).append(";");
							p4.append(prisme.getLevel()).append(";");
							p4.append("0;0;;0;0;0;0;0|");
							gains.put(k.getId(), p4);
						}
					}
				}
			}
			if (this.getType() == 4 && win == 1) {
				Collections.shuffle(TEAM1);
				final Map<Integer, Integer> invoks = new HashMap<Integer, Integer>();
				for (final Fighter i2 : TEAM1) {
					if (i2.isInvocation() && i2.getMob() != null && i2.getMob().getTemplate().getId() == 285) {
						invoks.put(i2.getId(), i2.getInvocator().getId());
					}
				}
				if (invoks != null && invoks.size() > 0) {
					for (final Map.Entry<Integer, Integer> entry3 : invoks.entrySet()) {
						TEAM1 = this.deplace(TEAM1, entry3.getValue(), entry3.getKey());
					}
				}
				for (final Fighter i2 : TEAM1) {
					if (i2.isInvocation() && i2.getMob() != null && i2.getMob().getTemplate().getId() != 285) {
						continue;
					}
					if (i2.getDouble() != null) {
						continue;
					}
					Packet.append(gains.get(i2.getId()));
				}
			} else {
				for (final Fighter k : TEAM1) {
					Packet.append(gains.get(k.getId()));
				}
			}
			for (final Fighter k : TEAM2) {
				if (k.isInvocation() && k.getMob() != null && k.getMob().getTemplate().getId() != 285) {
					continue;
				}
				if (k.getDouble() != null) {
					continue;
				}
				if (k.getPersonnage() != null && this.getType() != 0) {
					k.getPersonnage().calculTurnCandy();
				}
				if (this.getType() != 1 && this.getType() != 2) {
					final StringBuilder p5 = new StringBuilder();
					if (k.getPdv() == 0 || k.hasLeft() || k.isDead()) {
						p5.append("0;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
								.append(k.getLvl()).append(";1").append(";").append(k.xpString(";")).append(";;;;|");
					} else {
						p5.append("0;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
								.append(k.getLvl()).append(";0").append(";").append(k.xpString(";")).append(";;;;|");
					}
					Packet.append(p5);
				} else {
					int winH = 0;
					int winD = 0;
					if (this.getType() == 1) {
						if (this.getInit1().getPersonnage().get_align() != 0
								&& this.getInit0().getPersonnage().get_align() != 0
								&& (this.getInit1().getPersonnage().getAccount().getCurIP()
										.compareTo(this.getInit0().getPersonnage().getAccount().getCurIP()) != 0
										|| Main.allowMulePvp)) {
							winH = Formulas.calculHonorWin(TEAM1, TEAM2, k);
						}
						final Player P = k.getPersonnage();
						if (P == null) {
							continue;
						}
						if (P.get_align() != 0) {
							P.remHonor((P.get_honor() + winH < 0) ? (-P.get_honor()) : (-winH));
							if (P.getDeshonor() - winD < 0) {
								winD = 0;
							}
							P.setDeshonor(P.getDeshonor() - winD);
						}
						Packet.append("0;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
								.append(k.getLvl()).append(";").append(k.isDead() ? "1" : "0").append(";");
						Packet.append((P.get_align() != -1) ? World.getExpLevel(P.getGrade()).pvp : 0).append(";");
						Packet.append(P.get_honor()).append(";");
						int maxHonor3 = World.getExpLevel(P.getGrade() + 1).pvp;
						if (maxHonor3 == -1) {
							maxHonor3 = World.getExpLevel(P.getGrade()).pvp;
						}
						Packet.append((P.get_align() != -1) ? maxHonor3 : 0).append(";");
						Packet.append(winH).append(";");
						Packet.append(P.getGrade()).append(";");
						Packet.append(P.getDeshonor()).append(";");
						Packet.append(winD);
						Packet.append(";;0;0;0;0;0|");
					} else {
						if (this.getType() != 2) {
							continue;
						}
						winH = Formulas.calculHonorWin(TEAM1, TEAM2, k);
						final Player P = k.getPersonnage();
						if (P != null) {
							if (P.get_honor() + winH < 0) {
								winH = -P.get_honor();
							}
							winH = 0;
							if (P.getDeshonor() - winD < 0) {
								winD = 0;
							}
							P.setDeshonor(P.getDeshonor() - winD);
							Packet.append("0;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
									.append(k.getLvl()).append(";").append(k.isDead() ? "1" : "0").append(";");
							Packet.append((P.get_align() != -1) ? World.getExpLevel(P.getGrade()).pvp : 0).append(";");
							Packet.append(P.get_honor()).append(";");
							int maxHonor3 = World.getExpLevel(P.getGrade() + 1).pvp;
							if (maxHonor3 == -1) {
								maxHonor3 = World.getExpLevel(P.getGrade()).pvp;
							}
							Packet.append((P.get_align() != -1) ? maxHonor3 : 0).append(";");
							Packet.append(winH).append(";");
							Packet.append(P.getGrade()).append(";");
							Packet.append(P.getDeshonor()).append(";");
							Packet.append(winD);
							Packet.append(";;0;0;0;0;0|");
						} else {
							final Prism Prisme = k.getPrism();
							if (Prisme.getHonor() + winH < 0) {
								winH = -Prisme.getHonor();
							}
							Prisme.addHonor(winH);
							Packet.append("0;").append(k.getId()).append(";").append(k.getPacketsName()).append(";")
									.append(k.getLvl()).append(";").append(k.isDead() ? "1" : "0").append(";");
							Packet.append(World.getExpLevel(Prisme.getLevel()).pvp).append(";");
							Packet.append(Prisme.getHonor()).append(";");
							int maxHonor = World.getExpLevel(Prisme.getLevel() + 1).pvp;
							if (maxHonor == -1) {
								maxHonor = World.getExpLevel(Prisme.getLevel()).pvp;
							}
							Packet.append(maxHonor).append(";");
							Packet.append(winH).append(";");
							Packet.append(Prisme.getLevel()).append(";");
							Packet.append("0;0;;0;0;0;0;0|");
						}
					}
				}
			}
			if (Collector.getCollectorByMapId(this.getMap().getId()) != null && this.getType() == 4) {
				final Collector p6 = Collector.getCollectorByMapId(this.getMap().getId());
				final long winxp2 = FormuleOfficiel.getXp(p6, TEAM1, totalXP, nbbonus,
						(this.getMobGroup() != null) ? this.getMobGroup().getStarBonus() : 0, challXp, lvlMax, lvlMin,
						lvlLoosers, lvlWinners) / 10L;
				final long winkamas = (int) Math.floor(Formulas.getKamasWinPerco(minkamas, maxkamas));
				p6.setXp(p6.getXp() + winxp2);
				p6.setKamas(p6.getKamas() + winkamas);
				Packet.append("5;").append(p6.getId()).append(";").append(p6.getN1()).append(",").append(p6.getN2())
						.append(";").append(World.getGuild(p6.getGuildId()).getLvl()).append(";0;");
				final Guild G = World.getGuild(p6.getGuildId());
				Packet.append(G.getLvl()).append(";");
				Packet.append(G.getXp()).append(";");
				Packet.append(World.getGuildXpMax(G.getLvl())).append(";");
				Packet.append(";");
				Packet.append(winxp2).append(";");
				Packet.append(";");
				String drops2 = "";
				final ArrayList<World.Drop> temp4 = new ArrayList<World.Drop>();
				temp4.addAll(possibleDropsCollector);
				final Map<Integer, Integer> itemWon3 = new TreeMap<Integer, Integer>();
				if (p6.getPodsTotal() < p6.getMaxPod()) {
					for (final World.Drop D5 : temp4) {
						int j2 = D5.getMax();
						while (j2 > 0) {
							final int t3 = (int) (D5.getTaux()
									* (World.getGuild(p6.getGuildId()).getStats(176) / 100.0));
							final int jet3 = Formulas.getRandomValue(0, 1000000);
							if (jet3 < t3) {
								final ObjectTemplate OT5 = World.getObjTemplate(D5.getTemplateId());
								if (OT5 == null) {
									continue;
								}
								boolean itsOk2 = false;
								boolean setMax2 = true;
								boolean dontPut2 = false;
								Label_10616: {
									switch (D5.getAction()) {
									case -1: {
										itsOk2 = true;
										setMax2 = true;
										break;
									}
									case 1: {
										break;
									}
									case 2: {
										String[] split3;
										for (int length3 = (split3 = D5.getCondition()
												.split(",")).length, n3 = 0; n3 < length3; ++n3) {
											final String id3 = split3[n3];
											if (id3.equals(new StringBuilder(String.valueOf(this.getMap().getId()))
													.toString())) {
												itsOk2 = true;
												setMax2 = false;
												dontPut2 = true;
											}
										}
										break;
									}
									case 3: {
										if (this.getMapOld().getSubArea() == null) {
											break;
										}
										final String condition2;
										switch (condition2 = D5.getCondition()) {
										case "0": {
											if (this.getMapOld().getSubArea().getAlignement() == 2) {
												itsOk2 = true;
												break Label_10616;
											}
											break Label_10616;
										}
										case "1": {
											if (this.getMapOld().getSubArea().getAlignement() == 1) {
												itsOk2 = true;
												break Label_10616;
											}
											break Label_10616;
										}
										case "2": {
											if (this.getMapOld().getSubArea().getAlignement() == 2) {
												itsOk2 = true;
												break Label_10616;
											}
											break Label_10616;
										}
										case "3": {
											if (this.getMapOld().getSubArea().getAlignement() == 3) {
												itsOk2 = true;
												break Label_10616;
											}
											break Label_10616;
										}
										default:
											break;
										}
										itsOk2 = true;
										break;
									}
									case 4: {
										if (OT5.getId() == 2553) {
											itsOk2 = true;
											setMax2 = true;
											break;
										}
										break;
									}
									case 5: {
										itsOk2 = false;
										break;
									}
									case 6: {
										break;
									}
									case 7: {
										break;
									}
									default: {
										itsOk2 = true;
										setMax2 = true;
										break;
									}
									}
								}
								if (!itsOk2) {
									--j2;
									continue;
								}
								itemWon3.put(OT5.getId(),
										((itemWon3.get(OT5.getId()) == null) ? 0 : itemWon3.get(OT5.getId())) + 1);
								if (!setMax2) {
									--j2;
									continue;
								}
								D5.setMax_combat(D5.getMax_combat() - 1);
								if (D5.getMax_combat() == 0) {
									possibleDropsCollector.remove(D5);
								}
								if (dontPut2) {
									--j2;
									continue;
								}
							}
							--j2;
						}
					}
					for (final Map.Entry<Integer, Integer> entry4 : itemWon3.entrySet()) {
						final ObjectTemplate OT6 = World.getObjTemplate(entry4.getKey());
						if (OT6 == null) {
							continue;
						}
						if (p6.getPodsTotal() + OT6.getPod() * entry4.getValue() >= p6.getMaxPod()) {
							continue;
						}
						if (drops2.length() > 0) {
							drops2 = String.valueOf(drops2) + ",";
						}
						drops2 = String.valueOf(drops2) + entry4.getKey() + "~" + entry4.getValue();
						final org.aestia.object.Object obj4 = OT6.createNewItem(entry4.getValue(), false);
						if (!p6.addObjet(obj4)) {
							continue;
						}
						World.addObjet(obj4, true);
					}
				}
				Packet.append(drops2).append(";");
				Packet.append(winkamas).append("|");
				Database.getGame().getPercepteurData().update(p6);
			}
			return Packet.toString();
		} catch (Exception e2) {
			e2.printStackTrace();
			return "";
		}
	}

	public ArrayList<Fighter> deplace(final ArrayList<Fighter> TEAM1, final Integer Invocator,
			final Integer Invocation) {
		int k = 0;
		int p = 0;
		int j = 0;
		final int s = TEAM1.size() - 1;
		boolean b = true;
		Fighter invok = null;
		for (final Fighter i : TEAM1) {
			if (i.getId() == Invocation) {
				invok = i;
				b = false;
			}
			if (!b && invok != i) {
				TEAM1.set(k - 1, i);
			}
			++k;
		}
		TEAM1.set(s, invok);
		k = 0;
		b = true;
		for (final Fighter i : TEAM1) {
			if (i.getId() == Invocator) {
				p = k;
				b = false;
			}
			if (!b && i.getId() != Invocator) {
				++j;
				if (k < s) {
					TEAM1.set(s - j + 1, TEAM1.get(s - j));
				}
			}
			++k;
		}
		TEAM1.set(p + 1, invok);
		return TEAM1;
	}

	public String getGTL() {
		String packet = "GTL";
		if (this.orderPlaying != null) {
			for (final Fighter f : this.orderPlaying) {
				packet = String.valueOf(packet) + "|" + f.getId();
			}
		}
		return String.valueOf(packet) + '\0';
	}

	public String parseFightInfos() {
		final StringBuilder infos = new StringBuilder();
		infos.append(this.getId()).append(";");
		final long time = this.startTime + TimeZone.getDefault().getRawOffset();
		infos.append((this.getStartTime() == 0L) ? "-1" : time).append(";");
		infos.append("0,");
		switch (this.getType()) {
		case 0: {
			infos.append("0,");
			infos.append(this.getTeam0().size()).append(";");
			infos.append("0,");
			infos.append("0,");
			infos.append(this.getTeam1().size()).append(";");
			break;
		}
		case 1: {
			infos.append(this.getInit0().getPersonnage().get_align()).append(",");
			infos.append(this.getTeam0().size()).append(";");
			infos.append("0,");
			infos.append(this.getInit1().getPersonnage().get_align()).append(",");
			infos.append(this.getTeam1().size()).append(";");
			break;
		}
		case 2: {
			infos.append(this.getInit0().getPersonnage().get_align()).append(",");
			infos.append(this.getTeam0().size()).append(";");
			infos.append("0,");
			infos.append(this.getPrism().getAlignement()).append(",");
			infos.append(this.getTeam1().size()).append(";");
			break;
		}
		case 4: {
			infos.append("0,");
			infos.append(this.getTeam0().size()).append(";");
			infos.append("1,");
			if (this.getTeam0().isEmpty()) {
				infos.append("0,");
			} else {
				infos.append(
						this.getTeam1().get(this.getTeam1().keySet().toArray()[0]).getMob().getTemplate().getAlign())
						.append(",");
			}
			infos.append(this.getTeam1().size()).append(";");
			break;
		}
		case 3: {
			infos.append("0,");
			infos.append(this.getTeam0().size()).append(";");
			infos.append("1,");
			if (this.getTeam0().isEmpty()) {
				infos.append("0,");
			} else {
				infos.append(
						this.getTeam1().get(this.getTeam1().keySet().toArray()[0]).getMob().getTemplate().getAlign())
						.append(",");
			}
			infos.append(this.getTeam1().size()).append(";");
			break;
		}
		case 5: {
			infos.append("0,");
			infos.append(this.getTeam0().size()).append(";");
			infos.append("4,");
			infos.append("0,");
			infos.append(this.getTeam1().size()).append(";");
			break;
		}
		}
		return infos.toString();
	}

	public Fighter getFighterByOrdreJeu() {
		if (this.orderPlaying == null) {
			return null;
		}
		if (this.curPlayer >= this.orderPlaying.size()) {
			this.curPlayer = this.orderPlaying.size() - 1;
		}
		if (this.curPlayer < 0) {
			this.curPlayer = 0;
		}
		if (this.orderPlaying.size() <= 0) {
			return null;
		}
		Fighter current = null;
		try {
			current = this.orderPlaying.get(this.curPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			Console.println("Erreur de recuperation de CurrentFighter en combat : " + e.getMessage(),
					Console.Color.ERROR);
		}
		return current;
	}

	public int getOrderPlayingSize() {
		if (this.orderPlaying == null) {
			return 0;
		}
		if (this.orderPlaying.size() <= 0) {
			return 0;
		}
		return this.orderPlaying.size();
	}

	public boolean haveFighterInOrdreJeu(final Fighter f) {
		return this.orderPlaying != null && f != null && this.orderPlaying.contains(f);
	}

	public List<Fighter> getOrderPlaying() {
		return this.orderPlaying;
	}

	static /* synthetic */ void access$1(final Fight fight, final Turn turn) {
		fight.turn = turn;
	}

	static /* synthetic */ void access$4(final Fight fight, final List orderPlaying) {
		fight.orderPlaying = orderPlaying;
	}
}
