// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity;

import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.common.SocketManager;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.game.world.World;

public class Prism {
	private int id;
	private int alignement;
	private int level;
	private short Map;
	private int cell;
	private int dir;
	private int name;
	private int gfx;
	private int inFight;
	private int fightId;
	private int turnTime;
	private int honor;
	private int area;
	private Fight fight;
	private Map<Integer, Integer> stats;

	public Prism(final int id, final int alignement, final int level, final short Map, final int cell, final int honor,
			final int area) {
		this.turnTime = 45000;
		this.honor = 0;
		this.area = -1;
		this.stats = new TreeMap<Integer, Integer>();
		this.id = id;
		this.alignement = alignement;
		this.level = level;
		this.Map = Map;
		this.cell = cell;
		this.dir = 1;
		if (alignement == 1) {
			this.name = 1111;
			this.gfx = 8101;
		} else {
			this.name = 1112;
			this.gfx = 8100;
		}
		this.inFight = -1;
		this.fightId = -1;
		this.honor = honor;
		this.area = area;
		this.fight = null;
	}

	public int getId() {
		return this.id;
	}

	public int getAlignement() {
		return this.alignement;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(final int i) {
		this.level = i;
	}

	public short getMap() {
		return this.Map;
	}

	public int getCell() {
		return this.cell;
	}

	public void setCell(final int i) {
		this.cell = i;
	}

	public int getInFight() {
		return this.inFight;
	}

	public void setInFight(final int i) {
		this.inFight = i;
	}

	public int getFightId() {
		return this.fightId;
	}

	public void setFightId(final int i) {
		this.fightId = i;
	}

	public int getTurnTime() {
		return this.turnTime;
	}

	public void setTurnTime(final int i) {
		this.turnTime = i;
	}

	public void discountingTime(final int i) {
		this.turnTime -= i;
	}

	public int getHonor() {
		return this.honor;
	}

	public void addHonor(final int i) {
		this.honor += i;
	}

	public int getGrade() {
		int g = 1;
		for (int n = 1; n <= 10; ++n) {
			if (this.honor < World.getExpLevel(n).pvp) {
				g = n - 1;
				break;
			}
		}
		return g;
	}

	public int getConquestArea() {
		return this.area;
	}

	public void setConquestArea(final int i) {
		this.area = i;
	}

	public Fight getFight() {
		return this.fight;
	}

	public Stats getStats() {
		return new Stats(this.stats);
	}

	public void refreshStats() {
		final int feu = 1000 + 500 * this.level;
		final int intel = 1000 + 500 * this.level;
		final int agi = 1000 + 500 * this.level;
		final int sagesse = 1000 + 500 * this.level;
		final int chance = 1000 + 500 * this.level;
		final int resistance = 9 * this.level;
		this.stats.clear();
		this.stats.put(118, feu);
		this.stats.put(126, intel);
		this.stats.put(119, agi);
		this.stats.put(124, sagesse);
		this.stats.put(123, chance);
		this.stats.put(214, resistance);
		this.stats.put(213, resistance);
		this.stats.put(211, resistance);
		this.stats.put(212, resistance);
		this.stats.put(210, resistance);
		this.stats.put(160, resistance);
		this.stats.put(161, resistance);
		this.stats.put(111, 6);
		this.stats.put(128, 0);
	}

	public int getX() {
		final org.aestia.map.Map Map = World.getMap(this.Map);
		return Map.getX();
	}

	public int getY() {
		final org.aestia.map.Map Map = World.getMap(this.Map);
		return Map.getY();
	}

	public World.SubArea getSubArea() {
		final org.aestia.map.Map Map = World.getMap(this.Map);
		return Map.getSubArea();
	}

	public World.Area getArea() {
		final org.aestia.map.Map Map = World.getMap(this.Map);
		return Map.getSubArea().getArea();
	}

	public int getAlignSubArea() {
		final org.aestia.map.Map Map = World.getMap(this.Map);
		return Map.getSubArea().getAlignement();
	}

	public int getAlignArea() {
		final org.aestia.map.Map Map = World.getMap(this.Map);
		return Map.getSubArea().getAlignement();
	}

	public String getGMPrisme() {
		if (this.inFight != -1) {
			return "";
		}
		String str = "GM|+";
		str = String.valueOf(str) + this.cell + ";";
		str = String.valueOf(str) + this.dir + ";0;" + this.id + ";" + this.name + ";-10;" + this.gfx + "^100;"
				+ this.level + ";" + this.getGrade() + ";" + this.alignement;
		return str;
	}

	public static void parseAttack(final Player perso) {
		for (final Prism Prisme : World.AllPrisme()) {
			if ((Prisme.inFight == 0 || Prisme.inFight == -2) && perso.get_align() == Prisme.getAlignement()) {
				SocketManager.SEND_Cp_INFO_ATTAQUANT_PRISME(perso,
						attackerOfPrisme(Prisme.id, Prisme.Map, Prisme.fightId));
			}
		}
	}

	public static void parseDefense(final Player perso) {
		for (final Prism Prisme : World.AllPrisme()) {
			if ((Prisme.inFight == 0 || Prisme.inFight == -2) && perso.get_align() == Prisme.getAlignement()) {
				SocketManager.SEND_CP_INFO_DEFENSEURS_PRISME(perso,
						defenderOfPrisme(Prisme.id, Prisme.Map, Prisme.fightId));
			}
		}
	}

	public static String attackerOfPrisme(final int id, final short MapId, final int FightId) {
		String str = "+";
		str = String.valueOf(str) + Integer.toString(id, 36);
		for (final Map.Entry<Integer, Fight> Fight : World.getMap(MapId).getFights().entrySet()) {
			if (Fight.getValue().getId() == FightId) {
				for (final Fighter fighter : Fight.getValue().getFighters(1)) {
					if (fighter.getPersonnage() == null) {
						continue;
					}
					str = String.valueOf(str) + "|";
					str = String.valueOf(str) + Integer.toString(fighter.getPersonnage().getId(), 36) + ";";
					str = String.valueOf(str) + fighter.getPersonnage().getName() + ";";
					str = String.valueOf(str) + fighter.getPersonnage().getLevel() + ";";
					str = String.valueOf(str) + "0;";
				}
			}
		}
		return str;
	}

	public static String defenderOfPrisme(final int id, final short MapId, final int FightId) {
		String str = "+";
		String stra = "";
		str = String.valueOf(str) + Integer.toString(id, 36);
		for (final Map.Entry<Integer, Fight> Fight : World.getMap(MapId).getFights().entrySet()) {
			if (Fight.getValue().getId() == FightId) {
				for (final Fighter fighter : Fight.getValue().getFighters(2)) {
					if (fighter.getPersonnage() == null) {
						continue;
					}
					str = String.valueOf(str) + "|";
					str = String.valueOf(str) + Integer.toString(fighter.getPersonnage().getId(), 36) + ";";
					str = String.valueOf(str) + fighter.getPersonnage().getName() + ";";
					str = String.valueOf(str) + fighter.getPersonnage().get_gfxID() + ";";
					str = String.valueOf(str) + fighter.getPersonnage().getLevel() + ";";
					str = String.valueOf(str) + Integer.toString(fighter.getPersonnage().getColor1(), 36) + ";";
					str = String.valueOf(str) + Integer.toString(fighter.getPersonnage().getColor2(), 36) + ";";
					str = String.valueOf(str) + Integer.toString(fighter.getPersonnage().getColor3(), 36) + ";";
					if (Fight.getValue().getFighters(2).size() > 7) {
						str = String.valueOf(str) + "1;";
					} else {
						str = String.valueOf(str) + "0;";
					}
				}
				stra = str.substring(1);
				stra = "-" + stra;
				Fight.getValue().setDefenders(stra);
			}
		}
		return str;
	}
}
