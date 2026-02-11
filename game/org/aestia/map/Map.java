// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.aestia.client.Player;
import org.aestia.common.ConditionParser;
import org.aestia.common.CryptManager;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.Prism;
import org.aestia.entity.monster.Monster;
import org.aestia.entity.npc.Npc;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.game.GameServer;
import org.aestia.game.scheduler.GlobalManager;
import org.aestia.game.scheduler.Manageable;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.other.Action;

public class Map {
	private short id;
	private String date;
	private byte w;
	private byte h;
	private String key;
	private String placesStr;
	private java.util.Map<Integer, Case> cases;
	private java.util.Map<Integer, Fight> fights;
	private ArrayList<Monster.MobGrade> mobPossibles;
	private java.util.Map<Integer, Monster.MobGroup> mobGroups;
	private java.util.Map<Integer, Monster.MobGroup> fixMobGroups;
	private java.util.Map<Integer, Npc> npcs;
	public java.util.Map<Integer, Integer> requiredCell;
	private java.util.Map<Integer, ArrayList<Action>> endFightAction;
	private java.util.Map<Integer, Integer> mobExtras;
	public int nextObjectId;
	private byte X;
	private byte Y;
	private World.SubArea subArea;
	private MountPark mountPark;
	private byte maxGroup;
	private byte maxSize;
	private byte minSize;
	private byte fixSize;
	private int maxTeam0;
	private int maxTeam1;
	private boolean isMute;
	private String forbiddenCellSpawn;
	public boolean noMarchand;
	public boolean noCollector;
	public boolean noPrism;
	public boolean noTP;
	public boolean noDefie;
	public boolean noAgro;
	public boolean noCanal;
	private Manageable.ManageableRespawn doorRespawn;

	public Map(final short id, final String date, final byte w, final byte h, final String key, final String places,
			final String dData, final String cellsData, final String monsters, final String mapPos, final byte maxGroup,
			final byte fixSize, final byte minSize, final byte maxSize, final String cases, final String forbidden) {
		this.cases = new TreeMap<Integer, Case>();
		this.fights = new TreeMap<Integer, Fight>();
		this.mobPossibles = new ArrayList<Monster.MobGrade>();
		this.mobGroups = new TreeMap<Integer, Monster.MobGroup>();
		this.fixMobGroups = new TreeMap<Integer, Monster.MobGroup>();
		this.npcs = new TreeMap<Integer, Npc>();
		this.requiredCell = new TreeMap<Integer, Integer>();
		this.endFightAction = new TreeMap<Integer, ArrayList<Action>>();
		this.mobExtras = new TreeMap<Integer, Integer>();
		this.nextObjectId = -1;
		this.X = 0;
		this.Y = 0;
		this.maxGroup = 3;
		this.maxTeam0 = 0;
		this.maxTeam1 = 0;
		this.isMute = false;
		this.noMarchand = false;
		this.noCollector = false;
		this.noPrism = false;
		this.noTP = false;
		this.noDefie = false;
		this.noAgro = false;
		this.noCanal = false;
		this.id = id;
		this.date = date;
		this.w = w;
		this.h = h;
		this.key = key;
		this.placesStr = places;
		this.maxGroup = maxGroup;
		this.maxSize = maxSize;
		this.minSize = minSize;
		this.fixSize = fixSize;
		this.forbiddenCellSpawn = cases;
		try {
			if (!places.equalsIgnoreCase("") && !places.equalsIgnoreCase("|")) {
				final String[] split = places.split("\\|");
				this.maxTeam0 = split[0].length() / 2;
				this.maxTeam1 = split[1].length() / 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			final String[] mapInfos = mapPos.split(",");
			this.X = Byte.parseByte(mapInfos[0]);
			this.Y = Byte.parseByte(mapInfos[1]);
			final int subArea = Integer.parseInt(mapInfos[2]);
			if (subArea == 0 && id == 32) {
				this.subArea = World.getSubArea(subArea);
				if (this.subArea != null) {
					this.subArea.addMap(this);
				}
			} else if (subArea != 0) {
				this.subArea = World.getSubArea(subArea);
				if (this.subArea != null) {
					this.subArea.addMap(this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Main.stop();
		}
		try {
			final String[] split = forbidden.split(";");
			this.noMarchand = split[0].equals("1");
			this.noCollector = split[1].equals("1");
			this.noPrism = split[2].equals("1");
			this.noTP = split[3].equals("1");
			this.noDefie = split[4].equals("1");
			this.noAgro = split[5].equals("1");
			this.noCanal = split[6].equals("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!dData.isEmpty()) {
			this.cases = CryptManager.decompileMapData(this, dData);
		} else {
			final String[] cellsDataArray = cellsData.split("\\|");
			String[] array;
			for (int length = (array = cellsDataArray).length, j = 0; j < length; ++j) {
				final String o = array[j];
				boolean Walkable = true;
				boolean LineOfSight = true;
				int Number = -1;
				int obj = -1;
				final String[] cellInfos = o.split(",");
				try {
					Walkable = cellInfos[2].equals("1");
					LineOfSight = cellInfos[1].equals("1");
					Number = Integer.parseInt(cellInfos[0]);
					if (!cellInfos[3].trim().equals("")) {
						obj = Integer.parseInt(cellInfos[3]);
					}
				} catch (Exception d) {
					d.printStackTrace();
				}
				if (Number != -1) {
					this.cases.put(Number, new Case(this, Number, Walkable, LineOfSight, obj));
				}
			}
		}
		String[] split2;
		for (int length2 = (split2 = monsters.split("\\|")).length, k = 0; k < length2; ++k) {
			final String mob = split2[k];
			if (!mob.equals("")) {
				int id2 = 0;
				int lvl = 0;
				try {
					id2 = Integer.parseInt(mob.split(",")[0]);
					lvl = Integer.parseInt(mob.split(",")[1]);
				} catch (NumberFormatException e2) {
					e2.printStackTrace();
					continue;
				}
				if (id2 != 0) {
					if (lvl != 0) {
						if (World.getMonstre(id2) != null) {
							if (World.getMonstre(id2).getGradeByLevel(lvl) != null) {
								if (Config.getInstance().HALLOWEEN) {
									switch (id2) {
									case 98: {
										if (World.getMonstre(794) != null
												&& World.getMonstre(794).getGradeByLevel(lvl) != null) {
											id2 = 794;
											break;
										}
										break;
									}
									case 101: {
										if (World.getMonstre(793) != null
												&& World.getMonstre(793).getGradeByLevel(lvl) != null) {
											id2 = 793;
											break;
										}
										break;
									}
									}
								}
								this.mobPossibles.add(World.getMonstre(id2).getGradeByLevel(lvl));
							}
						}
					}
				}
			}
		}
		if (!cases.isEmpty()) {
			for (final Case c : this.cases.values()) {
				c.setWalkableInFight(false);
			}
			try {
				String[] split3;
				for (int length3 = (split3 = cases.split(",")).length, l = 0; l < length3; ++l) {
					final String i = split3[l];
					this.cases.get(Integer.parseInt(i)).setWalkableInFight(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Console.println("## Probleme de map cases : " + this.id, Console.Color.ERROR);
			}
		}
	}

	public Map(final short id, final String date, final byte w, final byte h, final String key, final String places) {
		this.cases = new TreeMap<Integer, Case>();
		this.fights = new TreeMap<Integer, Fight>();
		this.mobPossibles = new ArrayList<Monster.MobGrade>();
		this.mobGroups = new TreeMap<Integer, Monster.MobGroup>();
		this.fixMobGroups = new TreeMap<Integer, Monster.MobGroup>();
		this.npcs = new TreeMap<Integer, Npc>();
		this.requiredCell = new TreeMap<Integer, Integer>();
		this.endFightAction = new TreeMap<Integer, ArrayList<Action>>();
		this.mobExtras = new TreeMap<Integer, Integer>();
		this.nextObjectId = -1;
		this.X = 0;
		this.Y = 0;
		this.maxGroup = 3;
		this.maxTeam0 = 0;
		this.maxTeam1 = 0;
		this.isMute = false;
		this.noMarchand = false;
		this.noCollector = false;
		this.noPrism = false;
		this.noTP = false;
		this.noDefie = false;
		this.noAgro = false;
		this.noCanal = false;
		this.id = id;
		this.date = date;
		this.w = w;
		this.h = h;
		this.key = key;
		this.placesStr = places;
		this.cases = new TreeMap<Integer, Case>();
	}

	public Map(final short id, final String date, final byte w, final byte h, final String key, final String places,
			final byte x, final byte y, final byte maxGroup, final byte fixSize, final byte minSize,
			final byte maxSize) {
		this.cases = new TreeMap<Integer, Case>();
		this.fights = new TreeMap<Integer, Fight>();
		this.mobPossibles = new ArrayList<Monster.MobGrade>();
		this.mobGroups = new TreeMap<Integer, Monster.MobGroup>();
		this.fixMobGroups = new TreeMap<Integer, Monster.MobGroup>();
		this.npcs = new TreeMap<Integer, Npc>();
		this.requiredCell = new TreeMap<Integer, Integer>();
		this.endFightAction = new TreeMap<Integer, ArrayList<Action>>();
		this.mobExtras = new TreeMap<Integer, Integer>();
		this.nextObjectId = -1;
		this.X = 0;
		this.Y = 0;
		this.maxGroup = 3;
		this.maxTeam0 = 0;
		this.maxTeam1 = 0;
		this.isMute = false;
		this.noMarchand = false;
		this.noCollector = false;
		this.noPrism = false;
		this.noTP = false;
		this.noDefie = false;
		this.noAgro = false;
		this.noCanal = false;
		this.id = id;
		this.date = date;
		this.w = w;
		this.h = h;
		this.key = key;
		this.placesStr = places;
		this.X = x;
		this.Y = y;
		this.maxGroup = maxGroup;
		this.maxSize = maxSize;
		this.minSize = minSize;
		this.fixSize = fixSize;
	}

	public void setInfos(final String date, final String monsters, final String mapPos, final byte maxGroup,
			final byte fixSize, final byte minSize, final byte maxSize, final String cases, final String forbidden) {
		this.date = date;
		this.mobPossibles.clear();
		try {
			final String[] split = forbidden.split(";");
			this.noMarchand = split[0].equals("1");
			this.noCollector = split[1].equals("1");
			this.noPrism = split[2].equals("1");
			this.noTP = split[3].equals("1");
			this.noDefie = split[4].equals("1");
			this.noAgro = split[5].equals("1");
			this.noCanal = split[6].equals("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] split2;
		for (int length = (split2 = monsters.split("\\|")).length, j = 0; j < length; ++j) {
			final String mob = split2[j];
			if (!mob.equals("")) {
				int id1 = 0;
				int lvl = 0;
				try {
					id1 = Integer.parseInt(mob.split(",")[0]);
					lvl = Integer.parseInt(mob.split(",")[1]);
				} catch (NumberFormatException e2) {
					e2.printStackTrace();
					continue;
				}
				if (id1 != 0) {
					if (lvl != 0) {
						if (World.getMonstre(id1) != null) {
							if (World.getMonstre(id1).getGradeByLevel(lvl) != null) {
								this.mobPossibles.add(World.getMonstre(id1).getGradeByLevel(lvl));
							}
						}
					}
				}
			}
		}
		try {
			final String[] mapInfos = mapPos.split(",");
			this.X = Byte.parseByte(mapInfos[0]);
			this.Y = Byte.parseByte(mapInfos[1]);
			final int subArea = Integer.parseInt(mapInfos[2]);
			if (subArea == 0 && this.id == 32) {
				this.subArea = World.getSubArea(subArea);
				if (this.subArea != null) {
					this.subArea.addMap(this);
				}
			} else if (subArea != 0) {
				this.subArea = World.getSubArea(subArea);
				if (this.subArea != null) {
					this.subArea.addMap(this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Main.stop();
		}
		this.maxGroup = maxGroup;
		this.maxSize = maxSize;
		this.minSize = minSize;
		this.fixSize = fixSize;
		if (!cases.isEmpty()) {
			for (final Case c : this.cases.values()) {
				c.setWalkableInFight(false);
			}
			String[] split3;
			for (int length2 = (split3 = cases.split(",")).length, k = 0; k < length2; ++k) {
				final String i = split3[k];
				this.cases.get(Integer.parseInt(i)).setWalkableInFight(true);
			}
		}
	}

	public void addMobExtra(final Integer id, final Integer chances) {
		this.mobExtras.put(id, chances);
	}

	public void setGs(final byte maxGroup, final byte minSize, final byte fixSize, final byte maxSize) {
		this.maxGroup = maxGroup;
		this.maxSize = maxSize;
		this.minSize = minSize;
		this.fixSize = fixSize;
	}

	public ArrayList<Monster.MobGrade> getMobPossibles() {
		return this.mobPossibles;
	}

	public byte getMaxSize() {
		return this.maxSize;
	}

	public byte getMinSize() {
		return this.minSize;
	}

	public byte getFixSize() {
		return this.fixSize;
	}

	public void setMobPossibles(final String monsters) {
		if (monsters == null || monsters == "") {
			return;
		}
		this.mobPossibles = new ArrayList<Monster.MobGrade>();
		String[] split;
		for (int length = (split = monsters.split("\\|")).length, i = 0; i < length; ++i) {
			final String mob = split[i];
			if (!mob.equals("")) {
				int id1 = 0;
				int lvl = 0;
				try {
					id1 = Integer.parseInt(mob.split(",")[0]);
					lvl = Integer.parseInt(mob.split(",")[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
				if (id1 != 0) {
					if (lvl != 0) {
						if (World.getMonstre(id1) != null) {
							if (World.getMonstre(id1).getGradeByLevel(lvl) != null) {
								if (Config.getInstance().HALLOWEEN) {
									switch (id1) {
									case 98: {
										if (World.getMonstre(794) != null
												&& World.getMonstre(794).getGradeByLevel(lvl) != null) {
											id1 = 794;
											break;
										}
										break;
									}
									case 101: {
										if (World.getMonstre(793) != null
												&& World.getMonstre(793).getGradeByLevel(lvl) != null) {
											id1 = 793;
											break;
										}
										break;
									}
									}
								}
								this.mobPossibles.add(World.getMonstre(id1).getGradeByLevel(lvl));
							}
						}
					}
				}
			}
		}
	}

	public short getId() {
		return this.id;
	}

	public String getDate() {
		return this.date;
	}

	public byte getW() {
		return this.w;
	}

	public byte getH() {
		return this.h;
	}

	public String getKey() {
		return this.key;
	}

	public String getPlaces() {
		return this.placesStr;
	}

	public void setPlaces(final String place) {
		this.placesStr = place;
	}

	public java.util.Map<Integer, Case> getCases() {
		return this.cases;
	}

	public Case getCase(final int id) {
		if (this.cases.containsKey(id)) {
			return this.cases.get(id);
		}
		return null;
	}

	private void setCases(final java.util.Map<Integer, Case> cases) {
		this.cases = cases;
	}

	public Fight newFight(final Player init1, final Player init2, final int type) {
		if (init1.get_fight() != null || init2.get_fight() != null) {
			return null;
		}
		int id = 1;
		if (!this.fights.isEmpty()) {
			id = (int) this.fights.keySet().toArray()[this.fights.size() - 1] + 1;
		}
		final Fight f = new Fight(type, id, this, init1, init2);
		this.fights.put(id, f);
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(this);
		return f;
	}

	public void removeFight(final int id) {
		this.fights.remove(id);
	}

	public int getNbrFight() {
		return this.fights.size();
	}

	public Fight getFight(final int id) {
		return this.fights.get(id);
	}

	public java.util.Map<Integer, Fight> getFights() {
		return this.fights;
	}

	public java.util.Map<Integer, Monster.MobGroup> getMobGroups() {
		return this.mobGroups;
	}

	public void removeNpcOrMobGroup(final int id) {
		this.npcs.remove(id);
		this.mobGroups.remove(id);
	}

	public Npc addNpc(final int npcID, final int cellID, final int dir, final boolean isMovable) {
		final NpcTemplate temp = World.getNPCTemplate(npcID);
		if (temp == null) {
			return null;
		}
		if (this.getCase(cellID) == null) {
			return null;
		}
		final Npc npc = new Npc(this.nextObjectId, cellID, (byte) dir, temp, isMovable);
		this.npcs.put(this.nextObjectId, npc);
		--this.nextObjectId;
		return npc;
	}

	public java.util.Map<Integer, Npc> getNpcs() {
		return this.npcs;
	}

	public Npc getNpc(final int id) {
		return this.npcs.get(id);
	}

	public Npc RemoveNpc(final int id) {
		return this.npcs.remove(id);
	}

	public void applyEndFightAction(final Player perso) {
		if (this.endFightAction.get(perso.needEndFight()) == null) {
			return;
		}
		if (this.id == 8545) {
			if (perso.getCurCell().getId() <= 193 && perso.getCurCell().getId() != 186
					&& perso.getCurCell().getId() != 187 && perso.getCurCell().getId() != 173
					&& perso.getCurCell().getId() != 172 && perso.getCurCell().getId() != 144
					&& perso.getCurCell().getId() != 158) {
				for (final Action A : this.endFightAction.get(perso.needEndFight())) {
					A.apply(perso, null, -1, -1);
				}
			} else {
				for (final Action A : this.endFightAction.get(perso.needEndFight())) {
					A.setArgs("8547,214");
					A.apply(perso, null, -1, -1);
				}
			}
		} else {
			for (final Action A : this.endFightAction.get(perso.needEndFight())) {
				A.apply(perso, null, -1, -1);
			}
		}
		perso.setNeededEndFight(-1, null);
	}

	public boolean hasEndFightAction() {
		return !this.endFightAction.isEmpty();
	}

	public boolean hasEndFightAction(final int type) {
		return this.endFightAction.get(type) != null;
	}

	public void addEndFightAction(final int type, final Action A) {
		if (this.endFightAction.get(type) == null) {
			this.endFightAction.put(type, new ArrayList<Action>());
		}
		this.delEndFightAction(type, A.getId());
		this.endFightAction.get(type).add(A);
	}

	public void delEndFightAction(final int type, final int aType) {
		if (this.endFightAction.get(type) == null) {
			return;
		}
		final ArrayList<Action> copy = new ArrayList<Action>();
		copy.addAll(this.endFightAction.get(type));
		for (final Action A : copy) {
			if (A.getId() == aType) {
				this.endFightAction.get(type).remove(A);
			}
		}
	}

	public void delAllEndFightAction() {
		this.endFightAction.clear();
	}

	public int getX() {
		return this.X;
	}

	public int getY() {
		return this.Y;
	}

	public World.SubArea getSubArea() {
		return this.subArea;
	}

	public MountPark getMountPark() {
		return this.mountPark;
	}

	public void setMountPark(final MountPark mountPark) {
		this.mountPark = mountPark;
	}

	public static void removeMountPark(final int id) {
		try {
			for (final java.util.Map.Entry<Short, MountPark> mp : World.getMountPark().entrySet()) {
				if (mp.getValue().getGuild().getId() == id) {
					if (!mp.getValue().getListOfRaising().isEmpty()) {
						for (final Integer dindeId : mp.getValue().getListOfRaising()) {
							if (World.getDragoByID(dindeId) == null) {
								mp.getValue().delRaising(dindeId);
							} else {
								World.removeDragodinde(dindeId);
								Database.getStatique().getMounts_dataData().delete(dindeId);
							}
						}
						mp.getValue().getListOfRaising().clear();
					}
					if (!mp.getValue().getEtable().isEmpty()) {
						for (final Dragodinde dd : mp.getValue().getEtable()) {
							if (dd == null) {
								mp.getValue().getEtable().remove(dd);
							} else {
								World.removeDragodinde(dd.getId());
								Database.getStatique().getMounts_dataData().delete(dd.getId());
							}
						}
						mp.getValue().getEtable().clear();
					}
					mp.getValue().setOwner(0);
					mp.getValue().setGuild(null);
					mp.getValue().setPrice(3000000);
					Database.getGame().getMountpark_dataData().update(mp.getValue());
					for (final Player p : mp.getValue().getMap().getPersos()) {
						SocketManager.GAME_SEND_Rp_PACKET(p, mp.getValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public InteractiveObject getMountParkDoor() {
		for (final Case c : this.cases.values()) {
			if (c.getObject() == null) {
				continue;
			}
			if (c.getObject().getId() == 6763 || c.getObject().getId() == 6766 || c.getObject().getId() == 6767
					|| c.getObject().getId() == 6772) {
				return c.getObject();
			}
		}
		return null;
	}

	public int getMaxGroupNumb() {
		return this.maxGroup;
	}

	public void setMaxGroup(final byte id) {
		this.maxGroup = id;
	}

	public int getMaxTeam0() {
		return this.maxTeam0;
	}

	public int getMaxTeam1() {
		return this.maxTeam1;
	}

	public boolean containsForbiddenCellSpawn(final int id) {
		return this.forbiddenCellSpawn.contains(String.valueOf(id));
	}

	public Map getMapCopy() {
		final java.util.Map<Integer, Case> cases = new TreeMap<Integer, Case>();
		final Map map = new Map(this.id, this.date, this.w, this.h, this.key, this.placesStr);
		for (final java.util.Map.Entry<Integer, Case> entry : this.cases.entrySet()) {
			if (map.getId() == 8279) {
				switch (entry.getKey()) {
				case 86:
				case 100:
				case 114:
				case 128:
				case 142:
				case 156:
				case 170:
				case 187: {
					continue;
				}
				}
			}
			cases.put(entry.getKey(),
					new Case(map, entry.getValue().getId(), entry.getValue().isWalkableInFight(),
							entry.getValue().isLoS(),
							(entry.getValue().getObject() == null) ? -1 : entry.getValue().getObject().getId()));
		}
		map.setCases(cases);
		return map;
	}

	public Map getMapCopyIdentic() {
		final Map map = new Map(this.id, this.date, this.w, this.h, this.key, this.placesStr, this.X, this.Y,
				this.maxGroup, this.fixSize, this.minSize, this.maxSize);
		final java.util.Map<Integer, Case> cases = new TreeMap<Integer, Case>();
		for (final java.util.Map.Entry<Integer, Case> entry : this.cases.entrySet()) {
			cases.put(entry.getKey(),
					new Case(map, entry.getValue().getId(), entry.getValue().isWalkable(false),
							entry.getValue().isLoS(),
							(entry.getValue().getObject() == null) ? -1 : entry.getValue().getObject().getId()));
		}
		map.setCases(cases);
		return map;
	}

	public void addPlayer(final Player perso) {
		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this, perso);
		perso.getCurCell().addCharacter(perso);
		if (perso.getEnergy() < 0) {
			if (perso.getEnergy() >= 10000) {
				return;
			}
			if (Constant.isTaverne(this)) {
				if (perso.get_timeInTaverne() == 0L) {
					perso.set_timeInTaverne();
				}
			} else if (perso.get_timeInTaverne() != 0L) {
				final int gain = (int) ((System.currentTimeMillis() - perso.get_timeInTaverne()) / 1000L);
				perso.setEnergy(perso.getEnergy() + gain);
				if (perso.getEnergy() >= 10000) {
					perso.setEnergy(10000);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "092;" + gain);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				perso.set_timeInTaverne(0L);
			}
		}
	}

	public ArrayList<Player> getPersos() {
		final ArrayList<Player> persos = new ArrayList<Player>();
		for (final Case c : this.cases.values()) {
			for (final Player entry : c.getCharacters().values()) {
				persos.add(entry);
			}
		}
		return persos;
	}

	public void sendFloorItems(final Player perso) {
		for (final Case c : this.cases.values()) {
			if (c.getDroppedItem(false) != null) {
				SocketManager.GAME_SEND_GDO_PACKET(perso, '+', c.getId(), c.getDroppedItem(false).getTemplate().getId(),
						0);
			}
		}
	}

	public void delAllDropItem() {
		for (final java.util.Map.Entry<Integer, Case> cell : this.cases.entrySet()) {
			SocketManager.GAME_SEND_GDO_PACKET_TO_MAP(this, '-', cell.getValue().getId(), 0, 0);
			cell.getValue().clearDroppedItem();
		}
	}

	public int getStoreCount() {
		if (World.getSeller(this.getId()) == null) {
			return 0;
		}
		return World.getSeller(this.getId()).size();
	}

	public boolean haveMobFix() {
		return this.fixMobGroups.size() > 0;
	}

	public boolean isPossibleToPutMonster() {
		return !this.cases.isEmpty() && this.maxGroup > 0 && this.mobPossibles.size() > 0;
	}

	public boolean loadExtraMonsterOnMap(final int idMob) {
		if (World.getMonstre(idMob) == null) {
			return false;
		}
		final Monster.MobGrade grade = World.getMonstre(idMob).getRandomGrade();
		final int cell = this.getRandomFreeCellId(false);
		final Monster.MobGroup group = new Monster.MobGroup(this.nextObjectId, -1, this.mobPossibles, this, cell,
				this.fixSize, this.maxSize, this.maxSize, grade);
		if (group.getMobs().isEmpty()) {
			return false;
		}
		this.mobGroups.put(this.nextObjectId, group);
		--this.nextObjectId;
		return true;
	}

	public void loadMonsterOnMap() {
		if (this.maxGroup == 0) {
			return;
		}
		this.spawnGroup(-1, this.maxGroup, false, -1);
		this.spawnGroup(1, 1, false, -1);
		this.spawnGroup(2, 1, false, -1);
	}

	public void mute() {
		this.isMute = !this.isMute;
	}

	public boolean isMute() {
		return this.isMute;
	}

	public short ultimeCaseId() {
		final short c = (short) (this.w * this.h * 2 - (this.h + this.w));
		return c;
	}

	public boolean isAggroByMob(final Player perso, final int cell) {
		if (this.placesStr.equalsIgnoreCase("|")) {
			return false;
		}
		if (perso.getCurMap().getId() != this.id || !perso.canAggro()) {
			return false;
		}
		for (final Monster.MobGroup group : this.mobGroups.values()) {
			if (perso.get_align() == 0 && group.getAlignement() > 0) {
				continue;
			}
			if (perso.get_align() == 1 && group.getAlignement() == 1) {
				continue;
			}
			if (perso.get_align() == 2 && group.getAlignement() == 2) {
				continue;
			}
			if (this.subArea != null) {
				group.setSubArea(this.subArea.getId());
				group.changeAgro();
			}
			if (Pathfinding.getDistanceBetween(this, cell, group.getCellId()) <= group.getAggroDistance()
					&& group.getAggroDistance() > 0 && ConditionParser.validConditions(perso, group.getCondition())) {
				return true;
			}
		}
		return false;
	}

	public void spawnAfterTimeGroup(final int i, final int j, final boolean b, final int k) {
		new Manageable() {
			@Override
			public void launch() {
				GlobalManager.worldSheduler.schedule(this, 5L, TimeUnit.MINUTES);
			}

			@Override
			public void run() {
				Map.this.spawnGroup(-1, 1, true, -1);
			}
		}.launch();
	}

	public void spawnAfterTimeGroupFix(final int cell) {
		final java.util.Map<String, String> mobData = World.getGroupFix(this.getId(), cell);
		new Manageable() {
			@Override
			public void launch() {
				GlobalManager.worldSheduler.schedule(this, Integer.parseInt(mobData.get("timer")) / 1000,
						TimeUnit.SECONDS);
			}

			@Override
			public void run() {
				Map.this.addStaticGroup(cell, mobData.get("groupData"), true);
			}
		}.launch();
	}

	public void spawnGroup(final int align, final int nbr, final boolean log, final int cellID) {
		if (nbr < 1) {
			return;
		}
		if (this.mobGroups.size() - this.fixMobGroups.size() >= this.maxGroup) {
			return;
		}
		for (int a = 1; a <= nbr; ++a) {
			final ArrayList<Monster.MobGrade> mobPoss = new ArrayList<Monster.MobGrade>(this.mobPossibles);
			if (!this.mobExtras.isEmpty()) {
				for (final java.util.Map.Entry<Integer, Integer> entry : this.mobExtras.entrySet()) {
					if (entry.getKey() == 499 && !Config.getInstance().NOEL) {
						continue;
					}
					int random = Formulas.getRandomValue(0, 99);
					while (entry.getValue() > random) {
						final Monster mob = World.getMonstre(entry.getKey());
						if (mob == null) {
							continue;
						}
						final Monster.MobGrade mobG = mob.getRandomGrade();
						if (mobG == null) {
							continue;
						}
						mobPoss.add(mobG);
						if (entry.getKey() == 422) {
							break;
						}
						if (entry.getKey() == 499) {
							break;
						}
						random = Formulas.getRandomValue(0, 99);
					}
				}
			}
			final Monster.MobGroup group = new Monster.MobGroup(this.nextObjectId, align, mobPoss, this, cellID,
					this.fixSize, this.minSize, this.maxSize, null);
			if (!group.getMobs().isEmpty()) {
				this.mobGroups.put(this.nextObjectId, group);
				if (log) {
					GameServer.addToLog("- Un groupe de monstre viens d'\u00eatre ajout\u00e9 sur la Map (" + this.id
							+ ") d'alignement " + align + " avec comme ID : " + this.nextObjectId + " !");
					SocketManager.GAME_SEND_MAP_MOBS_GM_PACKET(this, group);
				}
				--this.nextObjectId;
			}
		}
	}

	public void spawnGroupWith(final Monster m) {
		Monster.MobGrade _m;
		for (_m = null; _m == null; _m = m.getRandomGrade()) {
		}
		int cell;
		for (cell = this.getRandomFreeCellId(false); this
				.containsForbiddenCellSpawn(cell); cell = this.getRandomFreeCellId(false)) {
		}
		final Monster.MobGroup group = new Monster.MobGroup(this.nextObjectId, -1, this.mobPossibles, this, cell,
				this.fixSize, this.minSize, this.maxSize, _m);
		group.setIsFix(false);
		this.mobGroups.put(this.nextObjectId, group);
		SocketManager.GAME_SEND_MAP_MOBS_GM_PACKET(this, group);
		--this.nextObjectId;
	}

	public void spawnNewGroup(final boolean timer, int cellID, final String groupData, final String condition) {
		while (this.containsForbiddenCellSpawn(cellID)) {
			cellID = this.getRandomFreeCellId(false);
		}
		final Monster.MobGroup group = new Monster.MobGroup(this.nextObjectId, cellID, groupData);
		if (group.getMobs().isEmpty()) {
			return;
		}
		this.mobGroups.put(this.nextObjectId, group);
		group.setCondition(condition);
		group.setIsFix(false);
		SocketManager.GAME_SEND_MAP_MOBS_GM_PACKET(this, group);
		--this.nextObjectId;
	}

	public void spawnGroupOnCommand(final int cellID, final String groupData, final boolean send) {
		final Monster.MobGroup group = new Monster.MobGroup(this.nextObjectId, cellID, groupData);
		if (group.getMobs().isEmpty()) {
			return;
		}
		this.mobGroups.put(this.nextObjectId, group);
		group.setIsFix(false);
		if (send) {
			SocketManager.GAME_SEND_MAP_MOBS_GM_PACKET(this, group);
		}
		--this.nextObjectId;
	}

	public void addStaticGroup(final int cellID, final String groupData, final boolean b) {
		final Monster.MobGroup group = new Monster.MobGroup(this.nextObjectId, cellID, groupData);
		if (group.getMobs().isEmpty()) {
			return;
		}
		this.mobGroups.put(this.nextObjectId, group);
		--this.nextObjectId;
		this.fixMobGroups.put(-1000 + this.nextObjectId, group);
		if (b) {
			SocketManager.GAME_SEND_MAP_MOBS_GM_PACKET(this, group);
		}
	}

	public void refreshSpawns() {
		for (final int id : this.mobGroups.keySet()) {
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this, id);
		}
		this.mobGroups.clear();
		this.mobGroups.putAll(this.fixMobGroups);
		for (final Monster.MobGroup mg : this.fixMobGroups.values()) {
			SocketManager.GAME_SEND_MAP_MOBS_GM_PACKET(this, mg);
		}
		this.spawnGroup(-1, this.maxGroup, true, -1);
		this.spawnGroup(1, 1, true, -1);
		this.spawnGroup(2, 1, true, -1);
	}

	public String getGMsPackets(final Player player) {
		final StringBuilder packet = new StringBuilder();
		for (final Case cell : this.cases.values()) {
			if (cell == null) {
				continue;
			}
			final java.util.Map<Integer, Player> characters = new HashMap<Integer, Player>(cell.getCharacters());
			for (final Player perso : characters.values()) {
				if (perso == null) {
					continue;
				}
				if (player.getGroupe() != null) {
					packet.append("GM|+").append(perso.parseToGM()).append('\0');
				} else {
					if (perso.get_size() <= 0) {
						continue;
					}
					packet.append("GM|+").append(perso.parseToGM()).append('\0');
				}
			}
		}
		return packet.toString();
	}

	public String getFightersGMsPackets(final Fight fight) {
		final StringBuilder packet = new StringBuilder();
		for (final java.util.Map.Entry<Integer, Case> cell : this.cases.entrySet()) {
			for (final Fighter f : cell.getValue().getFighters().values()) {
				if (f.getFight() != fight) {
					continue;
				}
				packet.append(f.getGmPacket('+')).append('\0');
			}
		}
		return packet.toString();
	}

	public String getMobGroupGMsPackets() {
		if (this.mobGroups.isEmpty()) {
			return "";
		}
		final StringBuilder packet = new StringBuilder();
		packet.append("GM|");
		boolean isFirst = true;
		for (final Monster.MobGroup entry : this.mobGroups.values()) {
			final String GM = entry.parseGM();
			if (GM.equals("")) {
				continue;
			}
			if (!isFirst) {
				packet.append("|");
			}
			packet.append(GM);
			isFirst = false;
		}
		return packet.toString();
	}

	public String getPrismeGMPacket() {
		if (World.AllPrisme() == null) {
			return "";
		}
		String str = "";
		for (final Prism Prisme : World.AllPrisme()) {
			if (Prisme.getMap() == this.id) {
				str = Prisme.getGMPrisme();
				break;
			}
		}
		return str;
	}

	public String getNpcsGMsPackets(final Player p) {
		if (this.npcs.isEmpty()) {
			return "";
		}
		final StringBuilder packet = new StringBuilder();
		packet.append("GM|");
		boolean isFirst = true;
		for (final java.util.Map.Entry<Integer, Npc> entry : this.npcs.entrySet()) {
			final String GM = entry.getValue().parse(false, p);
			if (GM.equals("")) {
				continue;
			}
			if (!isFirst) {
				packet.append("|");
			}
			packet.append(GM);
			isFirst = false;
		}
		return packet.toString();
	}

	public String getObjectsGDsPackets() {
		final StringBuilder toreturn = new StringBuilder();
		boolean first = true;
		for (final java.util.Map.Entry<Integer, Case> entry : this.cases.entrySet()) {
			if (entry.getValue().getObject() != null) {
				if (!first) {
					toreturn.append('\0');
				}
				first = false;
				final int cellID = entry.getValue().getId();
				final InteractiveObject object = entry.getValue().getObject();
				toreturn.append("GDF|").append(cellID).append(";").append(object.getState()).append(";")
						.append(object.isInteractive() ? "1" : "0");
			}
		}
		return toreturn.toString();
	}

	public String getObjectsGDsPacketsInFight(final boolean what) {
		final StringBuilder toreturn = new StringBuilder();
		boolean first = true;
		if (what) {
			for (final java.util.Map.Entry<Integer, Case> entry : this.cases.entrySet()) {
				if (entry.getValue().getObject() != null) {
					if (!first) {
						toreturn.append('\0');
					}
					first = false;
					final int cellId = entry.getValue().getId();
					toreturn.append("GDF|").append(cellId).append(";2;0");
				}
			}
		} else {
			for (final java.util.Map.Entry<Integer, Case> entry : this.cases.entrySet()) {
				if (entry.getValue().getObject() != null) {
					if (!first) {
						toreturn.append('\0');
					}
					first = false;
					final int cellId = entry.getValue().getId();
					final InteractiveObject object = entry.getValue().getObject();
					toreturn.append("GDF|").append(cellId).append(";").append(object.getState()).append(";")
							.append(object.isInteractive() ? "1" : "0");
				}
			}
		}
		return toreturn.toString();
	}

	public void startFightVersusMonstres(final Player perso, final Monster.MobGroup group) {
		if (perso.get_fight() != null) {
			return;
		}
		if (perso.isInAreaNotSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(perso.getGameClient(), 'S');
			return;
		}
		if (this.placesStr.isEmpty() || this.placesStr.equals("|")) {
			perso.sendMessage(
					"Poste sur le forum dans la cat\u00e9gorie ad\u00e9quat avec l'id de la map (/mapid dans le tchat) afin de pouvoir y mettre les cellules de combat. Merci.");
			return;
		}
		if (Main.fightAsBlocked) {
			return;
		}
		if (perso.isDead() == 1) {
			return;
		}
		if (perso.get_align() == 0 && group.getAlignement() > 0) {
			return;
		}
		if (perso.get_align() == 1 && group.getAlignement() == 1) {
			return;
		}
		if (perso.get_align() == 2 && group.getAlignement() == 2) {
			return;
		}
		if (!perso.canAggro()) {
			return;
		}
		if (!group.getCondition().equals("") && !ConditionParser.validConditions(perso, group.getCondition())) {
			SocketManager.GAME_SEND_Im_PACKET(perso, "119");
			return;
		}
		int id = 1;
		if (!this.fights.isEmpty()) {
			id = (int) this.fights.keySet().toArray()[this.fights.size() - 1] + 1;
		}
		this.mobGroups.remove(group.getId());
		this.fights.put(id, new Fight(id, this, perso, group));
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(this);
	}

	public void startFightVersusProtectors(final Player perso, final Monster.MobGroup group) {
		if (perso.get_fight() != null) {
			return;
		}
		if (perso.isInAreaNotSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(perso.getGameClient(), 'S');
			return;
		}
		if (Main.fightAsBlocked) {
			return;
		}
		if (perso.isDead() == 1) {
			return;
		}
		if (!perso.canAggro()) {
			return;
		}
		int id = 1;
		if (this.placesStr.isEmpty() || this.placesStr.equals("|")) {
			perso.sendMessage(
					"Poste sur le forum dans la cat\u00e9gorie ad\u00e9quat avec l'id de la map (/mapid dans le tchat) afin de pouvoir y mettre les cellules de combat. Merci.");
			return;
		}
		if (!this.fights.isEmpty()) {
			id = (int) this.fights.keySet().toArray()[this.fights.size() - 1] + 1;
		}
		this.fights.put(id, new Fight(id, this, perso, group, 4));
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(this);
	}

	public void startFigthVersusDopeuls(final Player perso, final Monster.MobGroup group) {
		if (perso.get_fight() != null) {
			return;
		}
		if (perso.isInAreaNotSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(perso.getGameClient(), 'S');
			return;
		}
		int id = 1;
		if (perso.isDead() == 1) {
			return;
		}
		if (!perso.canAggro()) {
			return;
		}
		if (!this.fights.isEmpty()) {
			id = (int) this.fights.keySet().toArray()[this.fights.size() - 1] + 1;
		}
		this.fights.put(id, new Fight(id, this, perso, group, 3));
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(this);
	}

	public void startFightVersusPercepteur(final Player perso, final Collector perco) {
		if (perso.get_fight() != null) {
			return;
		}
		if (perso.isInAreaNotSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(perso.getGameClient(), 'S');
			return;
		}
		if (Main.fightAsBlocked) {
			return;
		}
		if (perso.isDead() == 1) {
			return;
		}
		if (!perso.canAggro()) {
			return;
		}
		int id = 1;
		if (!this.fights.isEmpty()) {
			id = (int) this.fights.keySet().toArray()[this.fights.size() - 1] + 1;
		}
		this.fights.put(id, new Fight(id, this, perso, perco));
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(this);
	}

	public void startFightVersusPrisme(final Player perso, final Prism Prisme) {
		if (perso.get_fight() != null) {
			return;
		}
		if (perso.isInAreaNotSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(perso.getGameClient(), 'S');
			return;
		}
		if (Main.fightAsBlocked) {
			return;
		}
		if (perso.isDead() == 1) {
			return;
		}
		if (!perso.canAggro()) {
			return;
		}
		int id = 1;
		if (!this.fights.isEmpty()) {
			id = (int) this.fights.keySet().toArray()[this.fights.size() - 1] + 1;
		}
		this.fights.put(id, new Fight(id, this, perso, Prisme));
		SocketManager.GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(this);
	}

	public int getRandomFreeCellId(final boolean TP) {
		final ArrayList<Integer> freecell = new ArrayList<Integer>();
		for (final java.util.Map.Entry<Integer, Case> entry : this.cases.entrySet()) {
			if (this.getCase(entry.getKey()) == null) {
				continue;
			}
			if (!entry.getValue().isWalkable(true)) {
				continue;
			}
			if (entry.getValue().getObject() != null) {
				continue;
			}
			if (this.mountPark != null && this.mountPark.getCellOfObject().contains(entry.getKey())) {
				continue;
			}
			boolean ok = true;
			if (this.mobGroups != null) {
				for (final Monster.MobGroup mg : this.mobGroups.values()) {
					if (mg != null && mg.getCellId() == entry.getKey()) {
						ok = false;
					}
				}
			}
			if (this.npcs != null) {
				for (final Npc npc : this.npcs.values()) {
					if (npc != null && npc.getCellid() == entry.getKey()) {
						ok = false;
					}
				}
			}
			if (!ok) {
				continue;
			}
			if (!entry.getValue().getCharacters().isEmpty()) {
				continue;
			}
			freecell.add(entry.getKey());
		}
		if (freecell.isEmpty()) {
			return -1;
		}
		return freecell.get(Formulas.getRandomValue(0, freecell.size() - 1));
	}

	public int getRandomNearFreeCellId(final int cellid) {
		final ArrayList<Integer> freecell = new ArrayList<Integer>();
		final ArrayList<Integer> cases = new ArrayList<Integer>();
		cases.add(cellid + 1);
		cases.add(cellid - 1);
		cases.add(cellid + 2);
		cases.add(cellid - 2);
		cases.add(cellid + 14);
		cases.add(cellid - 14);
		cases.add(cellid + 15);
		cases.add(cellid - 15);
		cases.add(cellid + 16);
		cases.add(cellid - 16);
		cases.add(cellid + 27);
		cases.add(cellid - 27);
		cases.add(cellid + 28);
		cases.add(cellid - 28);
		cases.add(cellid + 29);
		cases.add(cellid - 29);
		cases.add(cellid + 30);
		cases.add(cellid - 30);
		cases.add(cellid + 31);
		cases.add(cellid - 31);
		cases.add(cellid + 42);
		cases.add(cellid - 42);
		cases.add(cellid + 43);
		cases.add(cellid - 43);
		cases.add(cellid + 44);
		cases.add(cellid - 44);
		cases.add(cellid + 45);
		cases.add(cellid - 45);
		cases.add(cellid + 57);
		cases.add(cellid - 57);
		cases.add(cellid + 58);
		cases.add(cellid - 58);
		cases.add(cellid + 59);
		cases.add(cellid - 59);
		for (final Integer entry : cases) {
			if (this.cases.get(entry) == null) {
				continue;
			}
			if (!this.cases.get(entry).isWalkable(true)) {
				continue;
			}
			boolean ok = true;
			for (final java.util.Map.Entry<Integer, Monster.MobGroup> mgEntry : this.mobGroups.entrySet()) {
				if (mgEntry.getValue().getCellId() == this.cases.get(entry).getId()) {
					ok = false;
				}
			}
			if (!ok) {
				continue;
			}
			ok = true;
			for (final java.util.Map.Entry<Integer, Npc> npcEntry : this.npcs.entrySet()) {
				if (npcEntry.getValue().getCellid() == this.cases.get(entry).getId()) {
					ok = false;
				}
			}
			if (!ok) {
				continue;
			}
			if (!this.cases.get(entry).getCharacters().isEmpty()) {
				continue;
			}
			freecell.add(this.cases.get(entry).getId());
		}
		if (freecell.isEmpty()) {
			return -1;
		}
		final int rand = Formulas.getRandomValue(0, freecell.size() - 1);
		return freecell.get(rand);
	}

	public void onMapNpcDisplacement(final boolean force) {
		for (final java.util.Map.Entry<Integer, Npc> entry : this.getNpcs().entrySet()) {
			if (!force) {
				final int rand = Formulas.getRandomValue(1, 100);
				if (rand < 75) {
					continue;
				}
			}
			final Npc npc = entry.getValue();
			if (npc == null) {
				continue;
			}
			if (!npc.isMovable()) {
				continue;
			}
			final int cell = this.getRandomNearFreeCellId(npc.getCellid());
			String pathstr;
			try {
				pathstr = Pathfinding.getShortestStringPathBetween(this, npc.getCellid(), cell, 0);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			if (pathstr == null) {
				continue;
			}
			npc.setCellid(cell);
			for (final Player z : this.getPersos()) {
				SocketManager.GAME_SEND_GA_PACKET(z.getGameClient(), "0", "1",
						new StringBuilder(String.valueOf(npc.getId())).toString(), pathstr);
			}
			break;
		}
	}

	public void onMapMonstersDisplacement() {
		if (this.getMobGroups().size() == 0) {
			return;
		}
		final int RandNumb = Formulas.getRandomValue(1, this.getMobGroups().size());
		int i = 0;
		for (final Monster.MobGroup group : this.getMobGroups().values()) {
			switch (this.id) {
			case 8279: {
				final int cell1 = group.getCellId();
				final Case cell2 = this.getCase(cell1 - 15);
				final Case cell3 = this.getCase(cell1 - 15 + 1);
				final Case cell4 = this.getCase(cell1 + 15 - 1);
				final Case cell5 = this.getCase(cell1 + 15);
				final boolean case2 = cell2 != null && cell2.isWalkable(true) && cell2.getCharacters().isEmpty();
				final boolean case3 = cell3 != null && cell3.isWalkable(true) && cell3.getCharacters().isEmpty();
				final boolean case4 = cell4 != null && cell4.isWalkable(true) && cell4.getCharacters().isEmpty();
				final boolean case5 = cell5 != null && cell5.isWalkable(true) && cell5.getCharacters().isEmpty();
				final ArrayList<Boolean> array = new ArrayList<Boolean>();
				array.add(case2);
				array.add(case3);
				array.add(case4);
				array.add(case5);
				int count = 0;
				for (final boolean bo : array) {
					if (bo) {
						++count;
					}
				}
				if (count == 0) {
					return;
				}
				if (count == 1) {
					final Case newCell = case2 ? cell2 : (case3 ? cell3 : (case4 ? cell4 : cell5));
					Case nextCell = null;
					if (newCell == null) {
						return;
					}
					if (newCell.equals(cell2)) {
						if (this.checkCell(newCell.getId() - 15)) {
							nextCell = this.getCase(newCell.getId() - 15);
							if (this.checkCell(nextCell.getId() - 15)) {
								nextCell = this.getCase(nextCell.getId() - 15);
							}
						}
					} else if (newCell.equals(cell3)) {
						if (this.checkCell(newCell.getId() - 15 + 1)) {
							nextCell = this.getCase(newCell.getId() - 15 + 1);
							if (this.getCase(nextCell.getId() - 15 + 1) != null) {
								nextCell = this.getCase(nextCell.getId() - 15 + 1);
							}
						}
					} else if (newCell.equals(cell4)) {
						if (this.checkCell(newCell.getId() + 15 - 1)) {
							nextCell = this.getCase(newCell.getId() + 15 - 1);
							if (this.checkCell(nextCell.getId() + 15 - 1)) {
								nextCell = this.getCase(nextCell.getId() + 15 - 1);
							}
						}
					} else if (newCell.equals(cell5) && this.checkCell(newCell.getId() + 15)) {
						nextCell = this.getCase(newCell.getId() + 15);
						if (this.checkCell(nextCell.getId() + 15)) {
							nextCell = this.getCase(nextCell.getId() + 15);
						}
					}
					String pathstr;
					try {
						pathstr = Pathfinding.getShortestStringPathBetween(this, group.getCellId(), nextCell.getId(),
								0);
					} catch (Exception e2) {
						return;
					}
					if (pathstr == null) {
						return;
					}
					group.setCellId(nextCell.getId());
					for (final Player z : this.getPersos()) {
						SocketManager.GAME_SEND_GA_PACKET(z.getGameClient(), "0", "1",
								new StringBuilder(String.valueOf(group.getId())).toString(), pathstr);
					}
					continue;
				} else {
					if (group.isFix()) {
						continue;
					}
					if (++i != RandNumb) {
						continue;
					}
					int cell6;
					for (cell6 = -1; cell6 == -1 || cell6 == 383 || cell6 == 384 || cell6 == 398
							|| cell6 == 369; cell6 = this.getRandomNearFreeCellId(group.getCellId())) {
					}
					String pathstr2;
					try {
						pathstr2 = Pathfinding.getShortestStringPathBetween(this, group.getCellId(), cell6, 0);
					} catch (Exception e) {
						return;
					}
					if (pathstr2 == null) {
						return;
					}
					group.setCellId(cell6);
					for (final Player z2 : this.getPersos()) {
						SocketManager.GAME_SEND_GA_PACKET(z2.getGameClient(), "0", "1",
								new StringBuilder(String.valueOf(group.getId())).toString(), pathstr2);
					}
					continue;
				}
			}
			default: {
				if (group.isFix()) {
					continue;
				}
				if (++i != RandNumb) {
					continue;
				}
				final int cell6 = this.getRandomNearFreeCellId(group.getCellId());
				String pathstr2;
				try {
					pathstr2 = Pathfinding.getShortestStringPathBetween(this, group.getCellId(), cell6, 0);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (pathstr2 == null) {
					return;
				}
				group.setCellId(cell6);
				for (final Player z2 : this.getPersos()) {
					SocketManager.GAME_SEND_GA_PACKET(z2.getGameClient(), "0", "1",
							new StringBuilder(String.valueOf(group.getId())).toString(), pathstr2);
				}
				continue;
			}
			}
		}
	}

	public boolean checkCell(final int id) {
		return this.getCase(id - 15) != null && this.getCase(id - 15).isWalkable(true);
	}

	public String getObjects() {
		if (this.mountPark == null || this.mountPark.getObject().size() == 0) {
			return "";
		}
		String packets = "GDO+";
		boolean first = true;
		for (final java.util.Map.Entry<Integer, java.util.Map<Integer, Integer>> entry : this.mountPark.getObject()
				.entrySet()) {
			for (final java.util.Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				if (!first) {
					packets = String.valueOf(packets) + "|";
				}
				final int cellidDurab = entry.getKey();
				packets = String.valueOf(packets) + entry.getKey() + ";" + entry2.getKey() + ";1;"
						+ this.getObjDurable(cellidDurab);
				first = false;
			}
		}
		return packets;
	}

	public String getObjDurable(final int CellID) {
		String packets = "";
		for (final java.util.Map.Entry<Integer, java.util.Map<Integer, Integer>> entry : this.mountPark.getObjDurab()
				.entrySet()) {
			for (final java.util.Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				if (CellID == entry.getKey()) {
					packets = String.valueOf(packets) + entry2.getValue() + ";" + entry2.getKey();
				}
			}
		}
		return packets;
	}

	public static int getObjResist(final Player perso, final int cellid, final int itemID) {
		final MountPark MP = perso.getCurMap().getMountPark();
		String packets = "";
		if (MP == null || MP.getObject().size() == 0) {
			return 0;
		}
		for (final java.util.Map.Entry<Integer, java.util.Map<Integer, Integer>> entry : MP.getObjDurab().entrySet()) {
			for (final java.util.Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				if (cellid == entry.getKey()) {
					packets = String.valueOf(packets) + entry.getKey() + ";" + entry2.getValue() + ";"
							+ entry2.getKey();
				}
			}
		}
		int cell = 0;
		int durability = 0;
		int durabilityMax = 0;
		try {
			final String[] infos = packets.split(";");
			cell = Integer.parseInt(infos[0]);
			if (itemID == 7798 || itemID == 7605 || itemID == 7606 || itemID == 7625 || itemID == 7628
					|| itemID == 7634) {
				durability = Integer.parseInt(infos[1]);
			} else {
				durability = Integer.parseInt(infos[1]) - 1;
			}
			durabilityMax = Integer.parseInt(infos[2]);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		if (durability <= 0) {
			if (MP.delObject(cell)) {
				SocketManager.SEND_GDO_PUT_OBJECT_MOUNT(perso.getCurMap(), String.valueOf(cell) + ";0;0");
			}
		} else {
			final java.util.Map<Integer, Integer> InDurab = new TreeMap<Integer, Integer>();
			InDurab.put(durabilityMax, durability);
			MP.getObjDurab().put(cell, InDurab);
			SocketManager.SEND_GDO_PUT_OBJECT_MOUNT(perso.getCurMap(),
					String.valueOf(cell) + ";" + itemID + ";1;" + durability + ";" + durabilityMax);
		}
		return durabilityMax;
	}

	public static int getObjResist(final MountPark MP, final int cellid, final int itemID) {
		String packets = "";
		if (MP == null || MP.getObject().size() == 0) {
			return 0;
		}
		for (final java.util.Map.Entry<Integer, java.util.Map<Integer, Integer>> entry : MP.getObjDurab().entrySet()) {
			for (final java.util.Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				if (cellid == entry.getKey()) {
					packets = String.valueOf(packets) + entry.getKey() + ";" + entry2.getValue() + ";"
							+ entry2.getKey();
				}
			}
		}
		final String[] infos = packets.split(";");
		final int cell = Integer.parseInt(infos[0]);
		int durability = 0;
		if (itemID == 7798 || itemID == 7605 || itemID == 7606 || itemID == 7625 || itemID == 7628 || itemID == 7634) {
			durability = Integer.parseInt(infos[1]);
		} else {
			durability = Integer.parseInt(infos[1]) - 1;
		}
		final int durabilityMax = Integer.parseInt(infos[2]);
		if (durability <= 0) {
			if (MP.delObject(cell)) {
				SocketManager.SEND_GDO_PUT_OBJECT_MOUNT(MP.getMap(), String.valueOf(cell) + ";0;0");
			}
		} else {
			final java.util.Map<Integer, Integer> InDurab = new TreeMap<Integer, Integer>();
			InDurab.put(durabilityMax, durability);
			MP.getObjDurab().put(cell, InDurab);
			SocketManager.SEND_GDO_PUT_OBJECT_MOUNT(MP.getMap(),
					String.valueOf(cell) + ";" + itemID + ";1;" + durability + ";" + durabilityMax);
		}
		return durabilityMax;
	}

	public boolean cellSideLeft(final int cell) {
		int ladoIzq = this.w;
		for (int i = 0; i < this.w; ++i) {
			if (cell == ladoIzq) {
				return true;
			}
			ladoIzq = ladoIzq + this.w * 2 - 1;
		}
		return false;
	}

	public boolean cellSideRight(final int cell) {
		int ladoDer = 2 * (this.w - 1);
		for (int i = 0; i < this.w; ++i) {
			if (cell == ladoDer) {
				return true;
			}
			ladoDer = ladoDer + this.w * 2 - 1;
		}
		return false;
	}

	public boolean cellSide(final int cell1, final int cell2) {
		return (this.cellSideLeft(cell1) && (cell2 == cell1 + (this.w - 1) || cell2 == cell1 - this.w))
				|| (this.cellSideRight(cell1) && (cell2 == cell1 + this.w || cell2 == cell1 - (this.w - 1)));
	}

	public String getGMOfMount() {
		if (this.mountPark == null || this.mountPark.getListOfRaising().size() == 0) {
			return "";
		}
		final StringBuilder packets = new StringBuilder();
		packets.append("GM|+");
		boolean first = true;
		for (final Integer id : this.mountPark.getListOfRaising()) {
			final String GM = World.getDragoByID(id).getRaisingMount(this.mountPark);
			if (!GM.equals("")) {
				if (GM.equals(null)) {
					continue;
				}
				if (!first) {
					packets.append("|+");
				}
				packets.append(GM);
				first = false;
			}
		}
		return packets.toString();
	}

	public void onPlayerArriveOnCell(final Player perso, final int caseID) {
		final Case _case = this.cases.get(caseID);
		if (_case == null) {
			return;
		}
		synchronized (_case) {
			final org.aestia.object.Object obj = _case.getDroppedItem(true);
			if (obj != null && !Main.mapAsBlocked) {
				if (perso.addObjet(obj, true)) {
					World.addObjet(obj, true);
				}
				SocketManager.GAME_SEND_GDO_PACKET_TO_MAP(this, '-', caseID, 0, 0);
				SocketManager.GAME_SEND_Ow_PACKET(perso);
			}
			if (obj != null && Main.mapAsBlocked) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"L'Administrateur \u00e0 bloqu\u00e9 temporairement l'acc\u00e8s de r\u00e9colte des objets aux sols.");
			}
		}
		final boolean cond = false;
		switch (this.getId()) {
		case 917: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 243) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 10352: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& (caseID == 299 || caseID == 327 || caseID == 355)) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 3) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 1186: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 437) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 1663: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 450) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 1213: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 274) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 736: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 259) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 8538: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 88) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 666: {
			final java.util.Map<Integer, Integer> requiredCell2 = new TreeMap<Integer, Integer>();
			final java.util.Map<Integer, Integer> requiredCell3 = new TreeMap<Integer, Integer>();
			final java.util.Map<Integer, Integer> requiredCell4 = new TreeMap<Integer, Integer>();
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 196) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoorAlkatraz(196);
				this.requiredCell.clear();
			}
			if (!requiredCell2.containsKey(caseID) && !requiredCell2.containsValue(perso.getId()) && caseID == 195) {
				requiredCell2.put(caseID, perso.getId());
			}
			if (requiredCell2.size() == 1) {
				this.openDoorAlkatraz(195);
				requiredCell2.clear();
			}
			if (!requiredCell3.containsKey(caseID) && !requiredCell3.containsValue(perso.getId()) && caseID == 340) {
				requiredCell3.put(caseID, perso.getId());
			}
			if (requiredCell3.size() == 1) {
				this.openDoorAlkatraz(340);
				requiredCell3.clear();
			}
			if (!requiredCell4.containsKey(caseID) && !requiredCell4.containsValue(perso.getId()) && caseID == 341) {
				requiredCell4.put(caseID, perso.getId());
			}
			if (requiredCell4.size() == 1) {
				this.openDoorAlkatraz(341);
				requiredCell4.clear();
				break;
			}
			break;
		}
		case 1884: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& (caseID == 378 || caseID == 295)) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 8269: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& (caseID == 378 || caseID == 295)) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 1) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 6692: {
			if ((!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& caseID == 150) || caseID == 300) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() > 2) {
				this.requiredCell.clear();
				break;
			}
			break;
		}
		case 8308: {
			if (!this.requiredCell.containsKey(caseID) && !this.requiredCell.containsValue(perso.getId())
					&& (caseID == 372 || caseID == 206 || caseID == 183)) {
				this.requiredCell.put(caseID, perso.getId());
			}
			if (this.requiredCell.size() == 3) {
				this.openDoor();
				this.requiredCell.clear();
				break;
			}
			break;
		}
		}
		if (cond) {
			return;
		}
		this.cases.get(caseID).applyOnCellStopActions(perso);
		if (this.placesStr.equalsIgnoreCase("|")) {
			return;
		}
		if (perso.getCurMap().getId() != this.id || !perso.canAggro()) {
			return;
		}
		for (final Monster.MobGroup group : this.mobGroups.values()) {
			if (Pathfinding.getDistanceBetween(this, caseID, group.getCellId()) <= group.getAggroDistance()) {
				this.startFightVersusMonstres(perso, group);
			}
		}
	}

	public void verifDoor(final Player perso) {
		switch (this.id) {
		case 8279: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "212;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 212, true);
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "198;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 198, true);
				break;
			}
			break;
		}
		case 917: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "242;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 242, true);
				break;
			}
			break;
		}
		case 10352: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "98;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 98, true);
				break;
			}
			break;
		}
		case 1186: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "295;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 295, true);
				break;
			}
			break;
		}
		case 1663: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "153;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 153, true);
				break;
			}
			break;
		}
		case 1213: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "409;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 409, true);
				break;
			}
			break;
		}
		case 736: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "224;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 224, true);
				break;
			}
			break;
		}
		case 8538: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "125;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 125, true);
				break;
			}
			break;
		}
		case 8308: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "326;aaaaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "344;aaGaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "251;aaaaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "178;aaGaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "239;aaaaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "155;aaGaaaaaaa801;1");
				this.send("GDF|326;1|344;3|251;1|178;3|239;1|155;3");
				return;
			}
			SocketManager.GAME_UPDATE_CELL_FAST(perso, "326;aaGaaaaaaa801;1");
			SocketManager.GAME_UPDATE_CELL_FAST(perso, "344;aaaaaaaaaa801;1");
			SocketManager.GAME_UPDATE_CELL_FAST(perso, "251;aaGaaaaaaa801;1");
			SocketManager.GAME_UPDATE_CELL_FAST(perso, "178;aaaaaaaaaa801;1");
			SocketManager.GAME_UPDATE_CELL_FAST(perso, "239;aaGaaaaaaa801;1");
			SocketManager.GAME_UPDATE_CELL_FAST(perso, "155;aaaaaaaaaa801;1");
			this.send("GDF|326;3|344;1|251;3|178;1|239;3|155;1");
			break;
		}
		case 6692: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 172, true);
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 52, true);
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "187;aaGaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "67;aaGaaaaaaa801;1");
				break;
			}
			break;
		}
		case 6904: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 172, true);
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 52, true);
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "187;aaGaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "67;aaGaaaaaaa801;1");
				break;
			}
			break;
		}
		case 6164: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "327;aaGaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "341;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 327, true);
				break;
			}
			break;
		}
		case 6171: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "312;aaGaaaaaaa801;1");
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "327;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 312, true);
				break;
			}
			break;
		}
		case 2034: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "192;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 192, true);
				break;
			}
			break;
		}
		case 2029: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "266;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 266, true);
				break;
			}
			break;
		}
		case 8269: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "192;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 192, true);
				break;
			}
			break;
		}
		case 7288: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "266;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 266, true);
				break;
			}
			break;
		}
		case 2032: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "192;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 192, true);
				break;
			}
			break;
		}
		case 2027: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "266;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 266, true);
				break;
			}
			break;
		}
		case 2017: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "192;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 192, true);
				break;
			}
			break;
		}
		case 2018: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "266;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 266, true);
				break;
			}
			break;
		}
		case 8529: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso, "250;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST(perso, 250, true);
				break;
			}
			break;
		}
		case 1884: {
			if (this.doorRespawn != null) {
				SocketManager.GAME_UPDATE_CELL_FAST(perso,
						"294;aaGaaaaaaa801;1|309;aaGaaaaaaa801;1|324;aaGaaaaaaa801;1|339;aaGaaaaaaa801;1|323;aaGaaaaaa a801;1|338;aaGaa aaaaa801;1|353;aaGaaaaaaa801;1|337;aaGaaaaaaa801 ;1|352;aaGaaaaaaa801;1|367;aaGaaaaaaa801;1|336;aaGaaaaaaa801;1|351;aaGaaaaaaa801;1|366;aaGaaaaaaa801;1|381;aaGaaaaaaa801;1|365;aaGaaaaaaa801;1|380;aaGaaaaaaa801;1|395;aaGaaaaaaa801;1|379;aaGaaaaaaa801;1|394;aaGaaaaaaa801;1|409;aaGaaaaaaa801;1");
				SocketManager.GAME_SEND_ACTION_TO_DOOR_FAST_PEUR(perso, true);
				break;
			}
			break;
		}
		}
	}

	public void openDoor() {
		switch (this.id) {
		case 8279: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "212;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 212, true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "198;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 198, true);
					Map.this.getCases().remove(212);
					Map.this.getCases().put(212, new Case(Map.this, 212, true, true, -1));
					Map.this.getCases().remove(198);
					Map.this.getCases().put(198, new Case(Map.this, 198, true, true, -1));
					Map.this.getCases().remove(184);
					Map.this.getCases().put(184, new Case(Map.this, 184, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "184;aaGaaaaaaa801;1");
					Map.this.getCases().remove(170);
					Map.this.getCases().put(170, new Case(Map.this, 170, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "170;aaGaaaaaaa801;1");
					Map.this.getCases().remove(156);
					Map.this.getCases().put(156, new Case(Map.this, 156, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "156;aaGaaaaaaa801;1");
					Map.this.getCases().remove(142);
					Map.this.getCases().put(142, new Case(Map.this, 142, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "142;aaGaaaaaaa801;1");
					Map.this.getCases().remove(128);
					Map.this.getCases().put(128, new Case(Map.this, 128, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "128;aaGaaaaaaa801;1");
					Map.this.getCases().remove(114);
					Map.this.getCases().put(114, new Case(Map.this, 100, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "100;aaGaaaaaaa801;1");
					Map.this.getCases().remove(86);
					Map.this.getCases().put(86, new Case(Map.this, 86, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "86;aaGaaaaaaa801;1");
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "212;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 212, false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "198;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 198, false);
					Map.access$0(Map.this, null);
					Map.this.getCases().remove(212);
					Map.this.getCases().put(212, new Case(Map.this, 212, false, false, -1));
					Map.this.getCases().remove(198);
					Map.this.getCases().put(198, new Case(Map.this, 198, false, false, -1));
					Map.this.getCases().remove(184);
					Map.this.getCases().put(184, new Case(Map.this, 184, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "184;aaaaaaaaaa801;1");
					Map.this.getCases().remove(170);
					Map.this.getCases().put(170, new Case(Map.this, 170, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "170;aaaaaaaaaa801;1");
					Map.this.getCases().remove(156);
					Map.this.getCases().put(156, new Case(Map.this, 156, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "156;aaaaaaaaaa801;1");
					Map.this.getCases().remove(142);
					Map.this.getCases().put(142, new Case(Map.this, 142, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "142;aaaaaaaaaa801;1");
					Map.this.getCases().remove(128);
					Map.this.getCases().put(128, new Case(Map.this, 128, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "128;aaaaaaaaaa801;1");
					Map.this.getCases().remove(114);
					Map.this.getCases().put(114, new Case(Map.this, 100, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "100;aaaaaaaaaa801;1");
					Map.this.getCases().remove(86);
					Map.this.getCases().put(86, new Case(Map.this, 86, false, false, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "86;aaaaaaaaaa801;1");
				}
			}).launch();
			break;
		}
		case 917: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "242;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 242, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "242;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 242, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 10352: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "98;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 98, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "98;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 98, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 1186: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.cases.get(295).setWalkable(true);
					Map.this.getCase(295).addOnCellStopAction(0, "2109,133", "-1", null);
					SocketManager.GAME_UPDATE_CELL(Map.this, "295;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 295, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "295;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 295, false);
					Map.this.cases.get(295).setWalkable(false);
					Map.this.cases.get(295).clearOnCellAction();
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 1663: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.cases.remove(153);
					Map.this.cases.put(153, new Case(Map.this, 153, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "153;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 153, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "153;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 153, false);
					Map.this.cases.remove(153);
					Map.this.cases.put(153, new Case(Map.this, 153, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 1213: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.cases.remove(409);
					Map.this.cases.put(409, new Case(Map.this, 409, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "409;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 409, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "409;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 409, false);
					Map.this.cases.remove(409);
					Map.this.cases.put(409, new Case(Map.this, 409, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 736: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.cases.remove(224);
					Map.this.cases.put(224, new Case(Map.this, 224, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this, "224;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 224, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "224;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 224, false);
					Map.this.cases.remove(224);
					Map.this.cases.put(224, new Case(Map.this, 224, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 8538: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.cases.remove(125);
					Map.this.cases.put(125, new Case(Map.this, 125, true, true, -1));
					World.getMap((short) 8538).getCase(125).addOnCellStopAction(0, "8540,309", "-1", null);
					SocketManager.GAME_UPDATE_CELL(Map.this, "125;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 125, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "125;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 125, false);
					Map.this.cases.remove(125);
					Map.this.cases.put(125, new Case(Map.this, 125, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 8308: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.send("GDF|326;4|344;2|251;4|178;2|239;4|155;2");
					SocketManager.GAME_UPDATE_CELL(Map.this, "326;aaaaaaaaaa801;1");
					Map.this.getCase(326).setWalkable(false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "344;aaGaaaaaaa801;1");
					Map.this.getCase(344).setWalkable(true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "251;aaaaaaaaaa801;1");
					Map.this.getCase(251).setWalkable(false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "178;aaGaaaaaaa801;1");
					Map.this.getCase(178).setWalkable(true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "239;aaaaaaaaaa801;1");
					Map.this.getCase(239).setWalkable(false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "155;aaGaaaaaaa801;1");
					Map.this.getCase(155).setWalkable(true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					Map.this.send("GDF|326;2|344;4|251;2|178;4|239;2|155;4");
					SocketManager.GAME_UPDATE_CELL(Map.this, "326;aaGaaaaaaa801;1");
					Map.this.getCase(326).setWalkable(true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "344;aaaaaaaaaa801;1");
					Map.this.getCase(344).setWalkable(false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "251;aaGaaaaaaa801;1");
					Map.this.getCase(251).setWalkable(true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "178;aaaaaaaaaa801;1");
					Map.this.getCase(178).setWalkable(false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "239;aaGaaaaaaa801;1");
					Map.this.getCase(239).setWalkable(true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "155;aaaaaaaaaa801;1");
					Map.this.getCase(155).setWalkable(false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 6692: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 52, true);
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 172, true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "187;aaGaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "67;aaGaaaaaaa801;1");
					Map.this.cases.remove(187);
					Map.this.cases.put(187, new Case(Map.this, 187, true, true, -1));
					Map.this.cases.remove(67);
					Map.this.cases.put(67, new Case(Map.this, 67, true, true, -1));
					World.getMap((short) 6692).getCase(187).addOnCellStopAction(0, "6710,426", "-1", null);
					World.getMap((short) 6692).getCase(67).addOnCellStopAction(0, "6710,335", "-1", null);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 52, false);
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 172, false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "187;aaaaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "67;aaaaaaaaaa801;1");
					Map.this.cases.remove(187);
					Map.this.cases.put(187, new Case(Map.this, 187, false, false, -1));
					Map.this.cases.remove(67);
					Map.this.cases.put(67, new Case(Map.this, 67, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 6904: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 172, true);
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 52, true);
					SocketManager.GAME_UPDATE_CELL(Map.this, "187;aaGaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "67;aaGaaaaaaa801;1");
					Map.this.cases.remove(187);
					Map.this.cases.put(187, new Case(Map.this, 187, true, true, -1));
					Map.this.cases.remove(67);
					Map.this.cases.put(67, new Case(Map.this, 67, true, true, -1));
					World.getMap((short) 6904).getCase(187).addOnCellStopAction(0, "6723,411", "-1", null);
					World.getMap((short) 6904).getCase(67).addOnCellStopAction(0, "6723,321", "-1", null);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 52, false);
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 172, false);
					SocketManager.GAME_UPDATE_CELL(Map.this, "187;aaaaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "67;aaaaaaaaaa801;1");
					Map.this.cases.remove(187);
					Map.this.cases.put(187, new Case(Map.this, 187, false, false, -1));
					Map.this.cases.remove(67);
					Map.this.cases.put(67, new Case(Map.this, 67, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 6164: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "327;aaGaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "341;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 327, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "327;aaaaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "341;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 327, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 6171: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "312;aaGaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "327;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 312, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "312;aaaaaaaaaa801;1");
					SocketManager.GAME_UPDATE_CELL(Map.this, "327;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 312, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 2034: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 2029: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 8269: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 7288: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 2032: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 2027: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 2017: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "192;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 192, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 2018: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "266;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 266, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 8529: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "250;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 250, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					SocketManager.GAME_UPDATE_CELL(Map.this, "250;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR(Map.this, 250, false);
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		case 1884: {
			if (this.doorRespawn != null) {
				break;
			}
			(this.doorRespawn = new Manageable.ManageableRespawn() {
				@Override
				public void launch() {
					Map.this.cases.remove(294);
					Map.this.cases.put(294, new Case(Map.this, 294, true, true, -1));
					Map.this.cases.remove(323);
					Map.this.cases.put(323, new Case(Map.this, 323, true, true, -1));
					Map.this.cases.remove(338);
					Map.this.cases.put(338, new Case(Map.this, 338, true, true, -1));
					Map.this.cases.remove(309);
					Map.this.cases.put(309, new Case(Map.this, 309, true, true, -1));
					Map.this.cases.remove(324);
					Map.this.cases.put(324, new Case(Map.this, 324, true, true, -1));
					Map.this.cases.remove(339);
					Map.this.cases.put(339, new Case(Map.this, 339, true, true, -1));
					Map.this.cases.remove(323);
					Map.this.cases.put(323, new Case(Map.this, 323, true, true, -1));
					Map.this.cases.remove(338);
					Map.this.cases.put(338, new Case(Map.this, 338, true, true, -1));
					Map.this.cases.remove(353);
					Map.this.cases.put(353, new Case(Map.this, 353, true, true, -1));
					Map.this.cases.remove(337);
					Map.this.cases.put(337, new Case(Map.this, 337, true, true, -1));
					Map.this.cases.remove(352);
					Map.this.cases.put(352, new Case(Map.this, 352, true, true, -1));
					Map.this.cases.remove(367);
					Map.this.cases.put(367, new Case(Map.this, 367, true, true, -1));
					Map.this.cases.remove(336);
					Map.this.cases.put(336, new Case(Map.this, 336, true, true, -1));
					Map.this.cases.remove(351);
					Map.this.cases.put(351, new Case(Map.this, 351, true, true, -1));
					Map.this.cases.remove(366);
					Map.this.cases.put(366, new Case(Map.this, 366, true, true, -1));
					Map.this.cases.remove(381);
					Map.this.cases.put(381, new Case(Map.this, 381, true, true, -1));
					Map.this.cases.remove(365);
					Map.this.cases.put(365, new Case(Map.this, 365, true, true, -1));
					Map.this.cases.remove(380);
					Map.this.cases.put(380, new Case(Map.this, 380, true, true, -1));
					Map.this.cases.remove(395);
					Map.this.cases.put(395, new Case(Map.this, 395, true, true, -1));
					Map.this.cases.remove(379);
					Map.this.cases.put(379, new Case(Map.this, 379, true, true, -1));
					Map.this.cases.remove(394);
					Map.this.cases.put(394, new Case(Map.this, 394, true, true, -1));
					Map.this.cases.remove(409);
					Map.this.cases.put(409, new Case(Map.this, 409, true, true, -1));
					SocketManager.GAME_UPDATE_CELL(Map.this,
							"294;aaGaaaaaaa801;1|323;aaGaaaaaaa801;1|338;aaGaaaaaaa801;1|309;aaGaaaaaaa801;1|324;aaGaaaaaaa801;1|339;aaGaaaaaaa801;1|323;aaGaaaaaa a801;1|338;aaGaa aaaaa801;1|353;aaGaaaaaaa801;1|337;aaGaaaaaaa801 ;1|352;aaGaaaaaaa801;1|367;aaGaaaaaaa801;1|336;aaGaaaaaaa801;1|351;aaGaaaaaaa801;1|366;aaGaaaaaaa801;1|381;aaGaaaaaaa801;1|365;aaGaaaaaaa801;1|380;aaGaaaaaaa801;1|395;aaGaaaaaaa801;1|379;aaGaaaaaaa801;1|394;aaGaaaaaaa801;1|409;aaGaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR_PEUR(Map.this, true);
					GlobalManager.worldSheduler.schedule(this, 30L, TimeUnit.SECONDS);
				}

				@Override
				public void run() {
					for (final Player player : Map.this.getPersos()) {
						final int cell = player.getCurCell().getId();
						if (cell == 294 || cell == 309 || cell == 324 || cell == 339 || cell == 323 || cell == 338
								|| cell == 353 || cell == 337 || cell == 352 || cell == 367 || cell == 336
								|| cell == 351 || cell == 366 || cell == 381 || cell == 365 || cell == 380
								|| cell == 395 || cell == 379 || cell == 394 || cell == 409) {
							SocketManager.GAME_SEND_Im_PACKET(player, "148");
							player.setFuneral();
						}
					}
					SocketManager.GAME_UPDATE_CELL(Map.this,
							"294;aaaaaaaaaa801;1|323;aaaaaaaaaa801;1|338;aaaaaaaaaa801;1|336;aaaaaaaaaa801;1|309;aaaaaaaaaa801;1|324;aaaaaaaaaa801;1|339;aaaaaaaaaa801;1|323;aaaaaaaaaa801;1|338;aaaaaaaaaa801;1|353;aaaaaaaaaa801;1|337;aaaaaaaaaa801;1|352;aaaaaaaaaa801;1|367;aaaaaaaaaa801;1|351;aaaaaaaaaa801;1|366;aaaaaaaaaa801;1|381;aaaaaaaaaa801;1|365;aaaaaaaaaa801;1|380;aaaaaaaaaa801;1|395;aaaaaaaaaa801;1|379;aaaaaaaaaa801;1|394;aaaaaaaaaa801;1|409;aaaaaaaaaa801;1");
					SocketManager.GAME_SEND_ACTION_TO_DOOR_PEUR(Map.this, false);
					Map.this.cases.remove(294);
					Map.this.cases.put(294, new Case(Map.this, 294, false, false, -1));
					Map.this.cases.remove(323);
					Map.this.cases.put(323, new Case(Map.this, 323, false, false, -1));
					Map.this.cases.remove(338);
					Map.this.cases.put(338, new Case(Map.this, 338, false, false, -1));
					Map.this.cases.remove(309);
					Map.this.cases.put(309, new Case(Map.this, 309, false, false, -1));
					Map.this.cases.remove(324);
					Map.this.cases.put(324, new Case(Map.this, 324, false, false, -1));
					Map.this.cases.remove(339);
					Map.this.cases.put(339, new Case(Map.this, 339, false, false, -1));
					Map.this.cases.remove(323);
					Map.this.cases.put(323, new Case(Map.this, 323, false, false, -1));
					Map.this.cases.remove(338);
					Map.this.cases.put(338, new Case(Map.this, 338, false, false, -1));
					Map.this.cases.remove(353);
					Map.this.cases.put(353, new Case(Map.this, 353, false, false, -1));
					Map.this.cases.remove(337);
					Map.this.cases.put(337, new Case(Map.this, 337, false, false, -1));
					Map.this.cases.remove(352);
					Map.this.cases.put(352, new Case(Map.this, 352, false, false, -1));
					Map.this.cases.remove(367);
					Map.this.cases.put(367, new Case(Map.this, 367, false, false, -1));
					Map.this.cases.remove(336);
					Map.this.cases.put(336, new Case(Map.this, 336, false, false, -1));
					Map.this.cases.remove(351);
					Map.this.cases.put(351, new Case(Map.this, 351, false, false, -1));
					Map.this.cases.remove(366);
					Map.this.cases.put(366, new Case(Map.this, 366, false, false, -1));
					Map.this.cases.remove(381);
					Map.this.cases.put(381, new Case(Map.this, 381, false, false, -1));
					Map.this.cases.remove(365);
					Map.this.cases.put(365, new Case(Map.this, 365, false, false, -1));
					Map.this.cases.remove(380);
					Map.this.cases.put(380, new Case(Map.this, 380, false, false, -1));
					Map.this.cases.remove(395);
					Map.this.cases.put(395, new Case(Map.this, 395, false, false, -1));
					Map.this.cases.remove(379);
					Map.this.cases.put(379, new Case(Map.this, 379, false, false, -1));
					Map.this.cases.remove(394);
					Map.this.cases.put(394, new Case(Map.this, 394, false, false, -1));
					Map.this.cases.remove(409);
					Map.this.cases.put(409, new Case(Map.this, 409, false, false, -1));
					Map.access$0(Map.this, null);
				}
			}).launch();
			break;
		}
		}
	}

	public void openDoorAlkatraz(final int cell) {
	}

	public void send(final String packet) {
		for (final Player player : this.getPersos()) {
			if (player != null) {
				player.send(packet);
			}
		}
	}

	static /* synthetic */ void access$0(final Map map, final Manageable.ManageableRespawn doorRespawn) {
		map.doorRespawn = doorRespawn;
	}
}
