// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.entity.Collector;
import org.aestia.entity.Prism;
import org.aestia.entity.monster.Monster;
import org.aestia.fight.spells.LaunchedSpell;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.Case;
import org.aestia.other.Guild;

public class Fighter implements Comparable<Fighter> {
	private int id;
	private boolean canPlay;
	private Fight fight;
	private int type;
	private Monster.MobGrade mob;
	private Player perso;
	private Player _double;
	private Collector collector;
	private Prism prism;
	private int team;
	private Case cell;
	private int pdvMax;
	private int pdv;
	private boolean isDead;
	private boolean hasLeft;
	private int gfxId;
	private Fighter isHolding;
	private Fighter holdedBy;
	private Fighter oldCible;
	private Fighter invocator;
	private boolean levelUp;
	private boolean isDeconnected;
	private int turnRemaining;
	private int nbrDisconnection;
	private boolean isTraqued;
	public int nbrInvoc;
	private Map<Integer, Integer> state;
	private ArrayList<SpellEffect> fightBuffs;
	private Map<Integer, Integer> chatiValue;
	private ArrayList<LaunchedSpell> launchedSpell;
	private int cellLaunchFight;
	public boolean inLancer;
	public boolean isStatique;

	public Fighter(final Fight f, final Monster.MobGrade mob) {
		this.id = 0;
		this.canPlay = false;
		this.type = 0;
		this.mob = null;
		this.perso = null;
		this._double = null;
		this.collector = null;
		this.prism = null;
		this.team = -2;
		this.oldCible = null;
		this.levelUp = false;
		this.isDeconnected = false;
		this.turnRemaining = 0;
		this.nbrDisconnection = 0;
		this.isTraqued = false;
		this.state = new TreeMap<Integer, Integer>();
		this.fightBuffs = new ArrayList<SpellEffect>();
		this.chatiValue = new TreeMap<Integer, Integer>();
		this.launchedSpell = new ArrayList<LaunchedSpell>();
		this.cellLaunchFight = 0;
		this.inLancer = false;
		this.isStatique = false;
		this.fight = f;
		this.type = 2;
		this.mob = mob;
		this.setId(mob.getInFightID());
		this.pdvMax = mob.getPdvMax();
		this.pdv = mob.getPdv();
		this.gfxId = this.getDefaultGfx();
	}

	public Fighter(final Fight f, final Player perso) {
		this.id = 0;
		this.canPlay = false;
		this.type = 0;
		this.mob = null;
		this.perso = null;
		this._double = null;
		this.collector = null;
		this.prism = null;
		this.team = -2;
		this.oldCible = null;
		this.levelUp = false;
		this.isDeconnected = false;
		this.turnRemaining = 0;
		this.nbrDisconnection = 0;
		this.isTraqued = false;
		this.state = new TreeMap<Integer, Integer>();
		this.fightBuffs = new ArrayList<SpellEffect>();
		this.chatiValue = new TreeMap<Integer, Integer>();
		this.launchedSpell = new ArrayList<LaunchedSpell>();
		this.cellLaunchFight = 0;
		this.inLancer = false;
		this.isStatique = false;
		this.fight = f;
		if (perso._isClone) {
			this.type = 10;
			this.setDouble(perso);
		} else {
			this.type = 1;
			this.perso = perso;
		}
		this.setId(perso.getId());
		this.pdvMax = perso.getMaxPdv();
		this.pdv = perso.getCurPdv();
		this.gfxId = this.getDefaultGfx();
	}

	public Fighter(final Fight f, final Collector Perco) {
		this.id = 0;
		this.canPlay = false;
		this.type = 0;
		this.mob = null;
		this.perso = null;
		this._double = null;
		this.collector = null;
		this.prism = null;
		this.team = -2;
		this.oldCible = null;
		this.levelUp = false;
		this.isDeconnected = false;
		this.turnRemaining = 0;
		this.nbrDisconnection = 0;
		this.isTraqued = false;
		this.state = new TreeMap<Integer, Integer>();
		this.fightBuffs = new ArrayList<SpellEffect>();
		this.chatiValue = new TreeMap<Integer, Integer>();
		this.launchedSpell = new ArrayList<LaunchedSpell>();
		this.cellLaunchFight = 0;
		this.inLancer = false;
		this.isStatique = false;
		this.fight = f;
		this.type = 5;
		this.setCollector(Perco);
		this.setId(-1);
		this.pdvMax = World.getGuild(Perco.getGuildId()).getLvl() * 100;
		this.pdv = World.getGuild(Perco.getGuildId()).getLvl() * 100;
		this.gfxId = 6000;
	}

	public Fighter(final Fight Fight, final Prism Prisme) {
		this.id = 0;
		this.canPlay = false;
		this.type = 0;
		this.mob = null;
		this.perso = null;
		this._double = null;
		this.collector = null;
		this.prism = null;
		this.team = -2;
		this.oldCible = null;
		this.levelUp = false;
		this.isDeconnected = false;
		this.turnRemaining = 0;
		this.nbrDisconnection = 0;
		this.isTraqued = false;
		this.state = new TreeMap<Integer, Integer>();
		this.fightBuffs = new ArrayList<SpellEffect>();
		this.chatiValue = new TreeMap<Integer, Integer>();
		this.launchedSpell = new ArrayList<LaunchedSpell>();
		this.cellLaunchFight = 0;
		this.inLancer = false;
		this.isStatique = false;
		this.fight = Fight;
		this.type = 7;
		this.setPrism(Prisme);
		this.setId(-1);
		this.pdvMax = Prisme.getLevel() * 10000;
		this.pdv = Prisme.getLevel() * 10000;
		this.gfxId = ((Prisme.getAlignement() == 1) ? 8101 : 8100);
		Prisme.refreshStats();
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public boolean canPlay() {
		return this.canPlay;
	}

	public void setCanPlay(final boolean canPlay) {
		this.canPlay = canPlay;
	}

	public Fight getFight() {
		return this.fight;
	}

	public int getType() {
		return this.type;
	}

	public Monster.MobGrade getMob() {
		if (this.type == 2) {
			return this.mob;
		}
		return null;
	}

	public boolean isMob() {
		return this.mob != null;
	}

	public Player getPersonnage() {
		if (this.type == 1) {
			return this.perso;
		}
		return null;
	}

	public Player getDouble() {
		return this._double;
	}

	public boolean isDouble() {
		return this._double != null;
	}

	public void setDouble(final Player _double) {
		this._double = _double;
	}

	public Collector getCollector() {
		if (this.type == 5) {
			return this.collector;
		}
		return null;
	}

	public void setCollector(final Collector collector) {
		this.collector = collector;
	}

	public boolean isCollector() {
		return this.collector != null;
	}

	public Prism getPrism() {
		if (this.type == 7) {
			return this.prism;
		}
		return null;
	}

	public boolean isPrisme() {
		return this.prism != null;
	}

	public void setPrism(final Prism prism) {
		this.prism = prism;
	}

	public int getTeam() {
		return this.team;
	}

	public void setTeam(final int i) {
		this.team = i;
	}

	public int getTeam2() {
		return this.fight.getTeamId(this.getId());
	}

	public int getOtherTeam() {
		return this.fight.getOtherTeamId(this.getId());
	}

	public Case getCell() {
		return this.cell;
	}

	public void setCell(final Case cell) {
		this.cell = cell;
	}

	public int getPdvMax() {
		return this.pdvMax + this.getBuffValue(125);
	}

	public void removePdvMax(final int pdv) {
		this.pdvMax -= pdv;
		if (this.pdv > this.pdvMax) {
			this.pdv = this.pdvMax;
		}
	}

	public int getPdv() {
		return this.pdv + this.getBuffValue(125);
	}

	public void addPdv(final int pdv) {
		this.pdvMax += pdv;
		this.pdv += pdv;
	}

	public void removePdv(final Fighter caster, final int pdv) {
		if (pdv > 0) {
			for (final Map.Entry<Integer, Challenge> c : this.getFight().getAllChallenges().entrySet()) {
				if (c.getValue() == null) {
					continue;
				}
				c.getValue().onFighterAttacked(caster, this);
			}
		}
		this.pdv -= pdv;
	}

	public void fullPdv() {
		this.pdv = this.pdvMax;
	}

	public boolean isFullPdv() {
		return this.pdv == this.pdvMax;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public void setIsDead(final boolean isDead) {
		this.isDead = isDead;
	}

	public boolean hasLeft() {
		return this.hasLeft;
	}

	public void setLeft(final boolean hasLeft) {
		this.hasLeft = hasLeft;
	}

	public int getGfxId() {
		return this.gfxId;
	}

	public void setGfxId(final int gfxId) {
		this.gfxId = gfxId;
	}

	public Fighter getIsHolding() {
		return this.isHolding;
	}

	public void setIsHolding(final Fighter isHolding) {
		this.isHolding = isHolding;
	}

	public Fighter getHoldedBy() {
		return this.holdedBy;
	}

	public void setHoldedBy(final Fighter holdedBy) {
		this.holdedBy = holdedBy;
	}

	public Fighter getOldCible() {
		return this.oldCible;
	}

	public void setOldCible(final Fighter cible) {
		this.oldCible = cible;
	}

	public Fighter getInvocator() {
		return this.invocator;
	}

	public boolean haveInvocation() {
		for (final Map.Entry<Integer, Fighter> entry : this.getFight().getTeam(this.getTeam2()).entrySet()) {
			final Fighter f = entry.getValue();
			if (f.isInvocation() && f.getInvocator() == this) {
				return true;
			}
		}
		return false;
	}

	public int nbInvocation() {
		int i = 0;
		for (final Map.Entry<Integer, Fighter> entry : this.getFight().getTeam(this.getTeam2()).entrySet()) {
			final Fighter f = entry.getValue();
			if (f.isInvocation() && !f.isStatique && f.getInvocator() == this) {
				++i;
			}
		}
		return i;
	}

	public boolean isInvocation() {
		return this.invocator != null;
	}

	public void setInvocator(final Fighter invocator) {
		this.invocator = invocator;
	}

	public boolean getLevelUp() {
		return this.levelUp;
	}

	public void setLevelUp(final boolean levelUp) {
		this.levelUp = levelUp;
	}

	public void Disconnect() {
		if (this.isDeconnected) {
			return;
		}
		this.isDeconnected = true;
		this.turnRemaining = 20;
		++this.nbrDisconnection;
	}

	public void Reconnect() {
		this.isDeconnected = false;
		this.turnRemaining = 0;
	}

	public boolean isDeconnected() {
		return !this.hasLeft && this.isDeconnected;
	}

	public int getTurnRemaining() {
		return this.turnRemaining;
	}

	public void setTurnRemaining() {
		--this.turnRemaining;
	}

	public int getNbrDisconnection() {
		return this.nbrDisconnection;
	}

	public boolean getTraqued() {
		return this.isTraqued;
	}

	public void setTraqued(final boolean isTraqued) {
		this.isTraqued = isTraqued;
	}

	public void setState(final int id, final int t) {
		this.state.remove(id);
		if (t != 0) {
			this.state.put(id, t);
		}
	}

	public void setLaunchCell(final int cellLaunchFight) {
		this.cellLaunchFight = cellLaunchFight;
	}

	public int getLaunchCell() {
		return this.cellLaunchFight;
	}

	public void sendState(final Player p) {
		if (p.getAccount() == null || p.getGameClient() == null) {
			return;
		}
		for (final Map.Entry<Integer, Integer> state : this.state.entrySet()) {
			SocketManager.GAME_SEND_GA_PACKET(p.getGameClient(), "7", "950",
					new StringBuilder(String.valueOf(this.getId())).toString(),
					String.valueOf(this.getId()) + "," + state.getKey() + ",1");
		}
	}

	public void decrementStates() {
		final ArrayList<Map.Entry<Integer, Integer>> entries = new ArrayList<Map.Entry<Integer, Integer>>();
		entries.addAll(this.state.entrySet());
		for (final Map.Entry<Integer, Integer> e : entries) {
			if (e.getKey() < 0) {
				continue;
			}
			this.state.remove(e.getKey());
			final int nVal = e.getValue() - 1;
			if (nVal == 0) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 950,
						new StringBuilder(String.valueOf(this.getId())).toString(),
						String.valueOf(this.getId()) + "," + e.getKey() + ",0");
			} else {
				this.state.put(e.getKey(), nVal);
			}
		}
	}

	public boolean haveState(final int id) {
		return this.state.get(id) != null && this.state.get(id) != 0;
	}

	public ArrayList<SpellEffect> getFightBuff() {
		return this.fightBuffs;
	}

	private Stats getFightBuffStats() {
		final Stats stats = new Stats();
		synchronized (this.fightBuffs) {
			for (final SpellEffect entry : this.fightBuffs) {
				stats.addOneStat(entry.getEffectID(), entry.getValue());
			}
		}
		// monitorexit(this.fightBuffs)
		return stats;
	}

	public int getBuffValue(final int id) {
		int value = 0;
		synchronized (this.fightBuffs) {
			for (final SpellEffect entry : this.fightBuffs) {
				if (entry.getEffectID() == id) {
					value += entry.getValue();
				}
			}
		}
		// monitorexit(this.fightBuffs)
		return value;
	}

	public SpellEffect getBuff(final int id) {
		synchronized (this.fightBuffs) {
			for (final SpellEffect entry : this.fightBuffs) {
				if (entry.getEffectID() == id && entry.getDuration() > 0) {
					// monitorexit(this.fightBuffs)
					return entry;
				}
			}
		}
		// monitorexit(this.fightBuffs)
		return null;
	}

	public ArrayList<SpellEffect> getBuffsByEffectID(final int effectID) {
		final ArrayList<SpellEffect> buffs = new ArrayList<SpellEffect>();
		synchronized (this.fightBuffs) {
			for (final SpellEffect buff : this.fightBuffs) {
				if (buff.getEffectID() == effectID) {
					buffs.add(buff);
				}
			}
		}
		// monitorexit(this.fightBuffs)
		return buffs;
	}

	public Stats getTotalStatsLessBuff() {
		Stats stats = new Stats(new TreeMap<Integer, Integer>());
		if (this.type == 1) {
			stats = this.perso.getTotalStats();
		}
		if (this.type == 2) {
			stats = this.mob.getStats();
		}
		if (this.type == 5) {
			stats = World.getGuild(this.getCollector().getGuildId()).getStatsFight();
		}
		if (this.type == 7) {
			stats = this.getPrism().getStats();
		}
		if (this.type == 10) {
			stats = this.getDouble().getTotalStats();
		}
		return stats;
	}

	public boolean hasBuff(final int id) {
		synchronized (this.fightBuffs) {
			for (final SpellEffect entry : this.fightBuffs) {
				if (entry.getEffectID() == id && entry.getDuration() > 0) {
					// monitorexit(this.fightBuffs)
					return true;
				}
			}
		}
		// monitorexit(this.fightBuffs)
		return false;
	}

	public void addBuff(final int id, int val, final int duration, final int turns, boolean debuff, final int spellID,
			final String args, final Fighter caster, final boolean start) {
		if (this.mob != null) {
			int[] static_INVOCATIONS;
			for (int length = (static_INVOCATIONS = Constant.STATIC_INVOCATIONS).length, i = 0; i < length; ++i) {
				final int id2 = static_INVOCATIONS[i];
				if (id2 == this.mob.getTemplate().getId()) {
					return;
				}
			}
		}
		if (spellID == 99 || spellID == 5 || spellID == 20 || spellID == 127 || spellID == 89 || spellID == 126
				|| spellID == 115 || spellID == 192 || spellID == 4 || spellID == 1 || spellID == 6 || spellID == 14
				|| spellID == 18 || spellID == 7 || spellID == 284 || spellID == 197 || spellID == 704 || spellID == 168
				|| spellID == 45 || spellID == 159 || spellID == 171 || spellID == 167) {
			debuff = true;
		}
		if (spellID == 431 || spellID == 433 || spellID == 437 || spellID == 443) {
			debuff = false;
		}
		synchronized (this.fightBuffs) {
			this.fightBuffs.add(new SpellEffect(id, val, duration, turns, debuff, caster, args, spellID, start));
		}
		// monitorexit(this.fightBuffs)
		if (Main.modDebug) {
			Console.println("Ajout du Buff " + id + " sur le personnage fighter " + this.getId() + ". Val: " + val
					+ " duration: " + duration + " turns: " + turns + " debuff: " + debuff + " spellid: " + spellID
					+ " args: " + args + " !", Console.Color.INFORMATION);
		}
		switch (id) {
		case 106: {
			SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(this.fight, 7, id, this.getId(), -1,
					new StringBuilder(String.valueOf(val)).toString(), "10", "", duration, spellID);
			break;
		}
		case 79: {
			val = Integer.parseInt(args.split(";")[0]);
			final String valMax = args.split(";")[1];
			final String chance = args.split(";")[2];
			SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(this.fight, 7, id, this.getId(), val, valMax, chance, "",
					duration, spellID);
			break;
		}
		case 788: {
			val = Integer.parseInt(args.split(";")[1]);
			final String valMax2 = args.split(";")[2];
			if (Integer.parseInt(args.split(";")[0]) == 108) {
				return;
			}
			SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(this.fight, 7, id, this.getId(), val,
					new StringBuilder().append(val).toString(), new StringBuilder().append(valMax2).toString(), "",
					duration, spellID);
			break;
		}
		case 91:
		case 92:
		case 93:
		case 94:
		case 95:
		case 96:
		case 97:
		case 98:
		case 99:
		case 100:
		case 107:
		case 108:
		case 114:
		case 165:
		case 781:
		case 782: {
			val = Integer.parseInt(args.split(";")[0]);
			final String valMax3 = args.split(";")[1];
			if (valMax3.compareTo("-1") == 0 || spellID == 82 || spellID == 94) {
				SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(this.fight, 7, id, this.getId(), val, "", "", "", duration,
						spellID);
				break;
			}
			if (valMax3.compareTo("-1") != 0) {
				SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(this.fight, 7, id, this.getId(), val, valMax3, "", "",
						duration, spellID);
				break;
			}
			break;
		}
		default: {
			SocketManager.GAME_SEND_FIGHT_GIE_TO_FIGHT(this.fight, 7, id, this.getId(), val, "", "", "", duration,
					spellID);
			break;
		}
		}
		if (this.perso != null) {
			SocketManager.GAME_SEND_STATS_PACKET(this.perso);
		}
	}

	public void debuff() {
		synchronized (this.fightBuffs) {
			final Iterator<SpellEffect> it = this.fightBuffs.iterator();
			while (it.hasNext()) {
				final SpellEffect SE = it.next();
				if (SE.isDebuffabe()) {
					it.remove();
				}
				switch (SE.getEffectID()) {
				default: {
					continue;
				}
				case 111:
				case 120: {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 101,
							new StringBuilder(String.valueOf(this.getId())).toString(),
							String.valueOf(this.getId()) + ",-" + SE.getValue());
					continue;
				}
				case 78:
				case 128: {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 127,
							new StringBuilder(String.valueOf(this.getId())).toString(),
							String.valueOf(this.getId()) + ",-" + SE.getValue());
					continue;
				}
				}
			}
			final ArrayList<SpellEffect> clone = new ArrayList<SpellEffect>(this.fightBuffs);
			if (!clone.isEmpty()) {
				this.fightBuffs.clear();
				for (final SpellEffect SE2 : clone) {
					this.addBuff(SE2.getEffectID(), SE2.getValue(), SE2.getDuration(), SE2.getTurn(), SE2.isDebuffabe(),
							SE2.getSpell(), SE2.getArgs(), this, SE2.isStart());
				}
			}
		}
		// monitorexit(this.fightBuffs)
		if (this.perso != null && !this.hasLeft) {
			SocketManager.GAME_SEND_STATS_PACKET(this.perso);
		}
	}

	public void refreshStartfightBuff() {
		final ArrayList<Fighter> fighters = this.getFight().getFighters(3);
		for (final Fighter f : fighters) {
			final ArrayList<SpellEffect> fightbuffs = f.getFightBuff();
			synchronized (fightbuffs) {
				final Iterator<SpellEffect> it = fightbuffs.iterator();
				while (it.hasNext()) {
					final SpellEffect entry = it.next();
					if (entry == null) {
						continue;
					}
					if (!entry.isStart()) {
						continue;
					}
					if (entry.getCaster().getId() != this.getId()) {
						continue;
					}
					if (entry.decrementDuration() != 0) {
						continue;
					}
					it.remove();
					switch (entry.getEffectID()) {
					case 108: {
						if (entry.getSpell() == 441) {
							f.pdvMax -= entry.getValue();
							int pdv = 0;
							if (f.pdv - entry.getValue() <= 0) {
								pdv = 0;
								f.fight.onFighterDie(f, f.holdedBy);
								f.fight.verifIfTeamAllDead();
							} else {
								pdv = f.pdv - entry.getValue();
							}
							f.pdv = pdv;
							continue;
						}
						continue;
					}
					case 950: {
						final String args = entry.getArgs();
						int id = -1;
						try {
							id = Integer.parseInt(args.split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (id == -1) {
							// monitorexit(fightbuffs)
							return;
						}
						this.setState(id, 0);
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(f.fight, 7, 950,
								new StringBuilder(String.valueOf(entry.getCaster().getId())).toString(),
								String.valueOf(entry.getCaster().getId()) + "," + id + ",0");
						continue;
					}
					default: {
						continue;
					}
					case 150: {
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(f.fight, 7, 150,
								new StringBuilder(String.valueOf(entry.getCaster().getId())).toString(),
								String.valueOf(this.getId()) + ",0");
						continue;
					}
					}
				}
			}
			// monitorexit(fightbuffs)
		}
	}

	public void refreshEndfightBuff() {
		synchronized (this.fightBuffs) {
			final Iterator<SpellEffect> it = this.fightBuffs.iterator();
			while (it.hasNext()) {
				final SpellEffect entry = it.next();
				if (entry == null) {
					continue;
				}
				if (entry.isStart() && !entry.getCaster().isDead) {
					continue;
				}
				if (entry.decrementDuration() != 0) {
					continue;
				}
				it.remove();
				switch (entry.getEffectID()) {
				case 108: {
					if (entry.getSpell() == 441) {
						this.pdvMax -= entry.getValue();
						int pdv = 0;
						if (this.pdv - entry.getValue() <= 0) {
							pdv = 0;
							this.fight.onFighterDie(this, this.holdedBy);
							this.fight.verifIfTeamAllDead();
						} else {
							pdv = this.pdv - entry.getValue();
						}
						this.pdv = pdv;
						continue;
					}
					continue;
				}
				case 950: {
					final String args = entry.getArgs();
					int id = -1;
					try {
						id = Integer.parseInt(args.split(";")[2]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (id == -1) {
						// monitorexit(this.fightBuffs)
						return;
					}
					this.setState(id, 0);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 950,
							new StringBuilder(String.valueOf(entry.getCaster().getId())).toString(),
							String.valueOf(entry.getCaster().getId()) + "," + id + ",0");
					continue;
				}
				default: {
					continue;
				}
				case 150: {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 150,
							new StringBuilder(String.valueOf(entry.getCaster().getId())).toString(),
							String.valueOf(this.getId()) + ",0");
					continue;
				}
				}
			}
		}
		// monitorexit(this.fightBuffs)
	}

	public void initBuffStats() {
		synchronized (this.fightBuffs) {
			if (this.type == 1) {
				for (final Map.Entry<Integer, SpellEffect> entry : this.perso.get_buff().entrySet()) {
					this.fightBuffs.add(entry.getValue());
				}
			}
		}
		// monitorexit(this.fightBuffs)
	}

	public void applyBeginningTurnBuff(final Fight fight) {
		synchronized (this.fightBuffs) {
			int[] begin_TURN_BUFF;
			for (int length = (begin_TURN_BUFF = Constant.BEGIN_TURN_BUFF).length, i = 0; i < length; ++i) {
				final int effectID = begin_TURN_BUFF[i];
				final ArrayList<SpellEffect> buffs = new ArrayList<SpellEffect>();
				buffs.addAll(this.fightBuffs);
				for (final SpellEffect entry : buffs) {
					if (entry.getEffectID() == effectID) {
						if (Main.modDebug) {
							Console.println("Effet en debut de tour : " + effectID + " !", Console.Color.INFORMATION);
						}
						entry.applyBeginingBuff(fight, this);
					}
				}
			}
		}
		// monitorexit(this.fightBuffs)
	}

	public ArrayList<LaunchedSpell> getLaunchedSorts() {
		return this.launchedSpell;
	}

	public void refreshLaunchedSort() {
		final ArrayList<LaunchedSpell> copie = new ArrayList<LaunchedSpell>();
		copie.addAll(this.launchedSpell);
		int i = 0;
		for (final LaunchedSpell S : copie) {
			S.actuCooldown();
			if (S.getCooldown() <= 0) {
				this.launchedSpell.remove(i);
				--i;
			}
			++i;
		}
	}

	public void addLaunchedSort(final Fighter target, final Spell.SortStats sort, final Fighter fighter) {
		final LaunchedSpell launched = new LaunchedSpell(target, sort, fighter);
		this.launchedSpell.add(launched);
	}

	public Stats getTotalStats() {
		Stats stats = new Stats(new TreeMap<Integer, Integer>());
		if (this.type == 1) {
			stats = this.perso.getTotalStats();
		}
		if (this.type == 2) {
			stats = this.mob.getStats();
		}
		if (this.type == 5) {
			stats = World.getGuild(this.getCollector().getGuildId()).getStatsFight();
		}
		if (this.type == 7) {
			stats = this.getPrism().getStats();
		}
		if (this.type == 10) {
			stats = this.getDouble().getTotalStats();
		}
		synchronized (this.fightBuffs) {
			stats = Stats.cumulStatFight(stats, this.getFightBuffStats());
		}
		// monitorexit(this.fightBuffs)
		return stats;
	}

	public int getMaitriseDmg(final int id) {
		int value = 0;
		synchronized (this.fightBuffs) {
			for (final SpellEffect entry : this.fightBuffs) {
				if (entry.getSpell() == id) {
					value += entry.getValue();
				}
			}
		}
		// monitorexit(this.fightBuffs)
		return value;
	}

	public boolean getSpellValueBool(final int id) {
		synchronized (this.fightBuffs) {
			for (final SpellEffect entry : this.fightBuffs) {
				if (entry.getSpell() == id) {
					// monitorexit(this.fightBuffs)
					return true;
				}
			}
		}
		// monitorexit(this.fightBuffs)
		return false;
	}

	public boolean testIfCC(int tauxCC) {
		if (tauxCC < 2) {
			return false;
		}
		int agi = this.getTotalStats().getEffect(119);
		if (agi < 0) {
			agi = 0;
		}
		tauxCC -= this.getTotalStats().getEffect(115);
		tauxCC = (int) (tauxCC * 2.9901 / Math.log(agi + 12));
		if (tauxCC < 2) {
			tauxCC = 2;
		}
		final int jet = Formulas.getRandomValue(1, tauxCC);
		return jet == tauxCC;
	}

	public boolean testIfCC(int porcCC, final Spell.SortStats sSort, final Fighter fighter) {
		final Player perso = fighter.getPersonnage();
		if (porcCC < 2) {
			return false;
		}
		int agi = this.getTotalStats().getEffect(119);
		if (agi < 0) {
			agi = 0;
		}
		porcCC -= this.getTotalStats().getEffect(115);
		if (fighter.getType() == 1 && perso.getItemClasseSpell().containsKey(sSort.getSpellID())) {
			final int modi = perso.getItemClasseModif(sSort.getSpellID(), 287);
			porcCC -= modi;
		}
		porcCC = (int) (porcCC * 2.9901 / Math.log(agi + 12));
		if (porcCC < 2) {
			porcCC = 2;
		}
		final int jet = Formulas.getRandomValue(1, porcCC);
		return jet == porcCC;
	}

	public int getInitiative() {
		if (this.type == 1) {
			return this.perso.getInitiative();
		}
		if (this.type == 2) {
			return this.mob.getInit();
		}
		if (this.type == 5) {
			return World.getGuild(this.getCollector().getGuildId()).getLvl();
		}
		if (this.type == 7) {
			return 0;
		}
		if (this.type == 10) {
			return this.getDouble().getInitiative();
		}
		return 0;
	}

	public int getPa() {
		switch (this.type) {
		case 1: {
			return this.getTotalStats().getEffect(111);
		}
		case 2: {
			return this.getTotalStats().getEffect(111) + this.mob.getPa();
		}
		case 5: {
			return this.getTotalStats().getEffect(128) + 6;
		}
		case 7: {
			return this.getTotalStats().getEffect(128) + 6;
		}
		case 10: {
			return this.getTotalStats().getEffect(111);
		}
		default: {
			return 0;
		}
		}
	}

	public int getPm() {
		switch (this.type) {
		case 1: {
			return this.getTotalStats().getEffect(128);
		}
		case 2: {
			return this.getTotalStats().getEffect(128) + this.mob.getPm();
		}
		case 5: {
			return this.getTotalStats().getEffect(128) + 4;
		}
		case 7: {
			return this.getTotalStats().getEffect(128);
		}
		case 10: {
			return this.getTotalStats().getEffect(128);
		}
		default: {
			return 0;
		}
		}
	}

	public int getPros() {
		switch (this.type) {
		case 1: {
			return this.getTotalStats().getEffect(176) + Math.round(this.getBuffValue(123) / 10);
		}
		case 2: {
			if (this.isInvocation()) {
				return this.getTotalStats().getEffect(176) + 1000 * (1 + this.getInvocator().getLvl() / 100) / 10;
			}
			return this.getTotalStats().getEffect(176) + Math.round(this.getBuffValue(123) / 10);
		}
		default: {
			return 0;
		}
		}
	}

	public int getCreaInvo() {
		return this.getTotalStats().getEffect(182);
	}

	public int getCurPa(final Fight fight) {
		return fight.getCurFighterPa();
	}

	public void setCurPa(final Fight fight, final int pa) {
		fight.setCurFighterPa(fight.getCurFighterPa() + pa);
	}

	public int getCurPm(final Fight fight) {
		return fight.getCurFighterPm();
	}

	public void setCurPm(final Fight fight, final int pm) {
		fight.setCurFighterPm(fight.getCurFighterPm() + pm);
	}

	public boolean canLaunchSpell(final int spellID) {
		return this.getPersonnage().hasSpell(spellID) && LaunchedSpell.cooldownGood(this, spellID);
	}

	public void unHide(final int spellid) {
		if (spellid != -1) {
			switch (spellid) {
			case 66:
			case 71:
			case 181:
			case 196:
			case 200:
			case 219: {
				return;
			}
			}
		}
		final ArrayList<SpellEffect> buffs = new ArrayList<SpellEffect>();
		buffs.addAll(this.getFightBuff());
		for (final SpellEffect SE : buffs) {
			if (SE.getEffectID() == 150) {
				this.getFightBuff().remove(SE);
			}
		}
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 150,
				new StringBuilder(String.valueOf(this.getId())).toString(), String.valueOf(this.getId()) + ",0");
		SocketManager.GAME_SEND_GIC_PACKET_TO_FIGHT(this.fight, 7, this);
	}

	public boolean isHide() {
		return this.hasBuff(150);
	}

	public int getPdvMaxOutFight() {
		if (this.perso != null) {
			return this.perso.getMaxPdv();
		}
		if (this.mob != null) {
			return this.mob.getPdvMax();
		}
		return 0;
	}

	public Map<Integer, Integer> getChatiValue() {
		return this.chatiValue;
	}

	public int getDefaultGfx() {
		if (this.perso != null) {
			return this.perso.get_gfxID();
		}
		if (this.mob != null) {
			return this.mob.getTemplate().getGfxId();
		}
		return 0;
	}

	public int getLvl() {
		if (this.type == 1) {
			return this.perso.getLevel();
		}
		if (this.type == 2) {
			return this.mob.getLevel();
		}
		if (this.type == 5) {
			return World.getGuild(this.getCollector().getGuildId()).getLvl();
		}
		if (this.type == 7) {
			return this.getPrism().getLevel();
		}
		if (this.type == 10) {
			return this.getDouble().getLevel();
		}
		return 0;
	}

	public String xpString(final String str) {
		if (this.perso != null) {
			int max = this.perso.getLevel() + 1;
			if (max > World.getExpLevelSize()) {
				max = World.getExpLevelSize();
			}
			return String.valueOf(World.getExpLevel(this.perso.getLevel()).perso) + str + this.perso.getExp() + str
					+ World.getExpLevel(max).perso;
		}
		return "0" + str + "0" + str + "0";
	}

	public long getXpGive() {
		if (this.mob != null) {
			return this.mob.getBaseXp();
		}
		return 0L;
	}

	public String getPacketsName() {
		if (this.type == 1) {
			return this.perso.getName();
		}
		if (this.type == 2) {
			return new StringBuilder(String.valueOf(this.mob.getTemplate().getId())).toString();
		}
		if (this.type == 5) {
			return String.valueOf(this.getCollector().getN1()) + "," + this.getCollector().getN2();
		}
		if (this.type == 7) {
			return new StringBuilder(String.valueOf((this.getPrism().getAlignement() == 1) ? 1111 : 1112)).toString();
		}
		if (this.type == 10) {
			return this.getDouble().getName();
		}
		return "";
	}

	public String getGmPacket(final char c) {
		final StringBuilder str = new StringBuilder();
		str.append("GM|").append(c);
		str.append(this.getCell().getId()).append(";");
		str.append("1;0;");
		str.append(this.getId()).append(";");
		str.append(this.getPacketsName()).append(";");
		switch (this.type) {
		case 1: {
			str.append(this.perso.getClasse()).append(";");
			str.append(this.perso.get_gfxID()).append("^").append(this.perso.get_size()).append(";");
			str.append(this.perso.getSexe()).append(";");
			str.append(this.perso.getLevel()).append(";");
			str.append(this.perso.get_align()).append(",");
			str.append("0").append(",");
			str.append(this.perso.is_showWings() ? this.perso.getGrade() : "0").append(",");
			str.append(this.perso.getLevel() + this.perso.getId());
			if (this.perso.is_showWings() && this.perso.getDeshonor() > 0) {
				str.append(",");
				str.append((this.perso.getDeshonor() > 0) ? 1 : 0).append(';');
			} else {
				str.append(";");
			}
			int color1 = this.perso.getColor1();
			int color2 = this.perso.getColor2();
			int color3 = this.perso.getColor3();
			if (this.perso.getObjetByPos(22) != null && this.perso.getObjetByPos(22).getTemplate().getId() == 10838) {
				color1 = 16342021;
				color2 = 16342021;
				color3 = 16342021;
			}
			str.append((color1 == -1) ? "-1" : Integer.toHexString(color1)).append(";");
			str.append((color2 == -1) ? "-1" : Integer.toHexString(color2)).append(";");
			str.append((color3 == -1) ? "-1" : Integer.toHexString(color3)).append(";");
			str.append(this.perso.getGMStuffString()).append(";");
			str.append(this.getPdv()).append(";");
			str.append(this.getTotalStats().getEffect(111)).append(";");
			str.append(this.getTotalStats().getEffect(128)).append(";");
			str.append(this.getTotalStats().getEffect(214)).append(";");
			str.append(this.getTotalStats().getEffect(210)).append(";");
			str.append(this.getTotalStats().getEffect(213)).append(";");
			str.append(this.getTotalStats().getEffect(211)).append(";");
			str.append(this.getTotalStats().getEffect(212)).append(";");
			str.append(this.getTotalStats().getEffect(160)).append(";");
			str.append(this.getTotalStats().getEffect(161)).append(";");
			str.append(this.team).append(";");
			if (this.perso.isOnMount() && this.perso.getMount() != null) {
				str.append(this.perso.getMount().getStringColor(this.perso.parsecolortomount()));
			}
			str.append(";");
			break;
		}
		case 2: {
			str.append("-2;");
			str.append(this.mob.getTemplate().getGfxId()).append("^" + this.mob.getSize() + ";");
			str.append(this.mob.getGrade()).append(";");
			str.append(this.mob.getTemplate().getColors().replace(",", ";")).append(";");
			str.append("0,0,0,0;");
			str.append(this.getPdvMax()).append(";");
			str.append(this.mob.getPa()).append(";");
			str.append(this.mob.getPm()).append(";");
			str.append(this.team);
			break;
		}
		case 5: {
			str.append("-6;");
			str.append("6000^100;");
			final Guild G = World.getGuild(this.collector.getGuildId());
			str.append(G.getLvl()).append(";");
			str.append("1;");
			str.append("2;4;");
			str.append((int) Math.floor(G.getLvl() / 2)).append(";").append((int) Math.floor(G.getLvl() / 2))
					.append(";").append((int) Math.floor(G.getLvl() / 2)).append(";")
					.append((int) Math.floor(G.getLvl() / 2)).append(";").append((int) Math.floor(G.getLvl() / 2))
					.append(";").append((int) Math.floor(G.getLvl() / 2)).append(";")
					.append((int) Math.floor(G.getLvl() / 2)).append(";");
			str.append(this.team);
			break;
		}
		case 7: {
			str.append("-2;");
			str.append(String.valueOf((this.getPrism().getAlignement() == 1) ? 8101 : 8100) + "^100;");
			str.append(String.valueOf(this.getPrism().getLevel()) + ";");
			str.append("-1;-1;-1;");
			str.append("0,0,0,0;");
			str.append(this.getPdvMax()).append(";");
			str.append(this.getTotalStats().getEffect(111)).append(";");
			str.append(this.getTotalStats().getEffect(128)).append(";");
			str.append(String.valueOf(this.getTotalStats().getEffect(214)) + ";");
			str.append(String.valueOf(this.getTotalStats().getEffect(210)) + ";");
			str.append(String.valueOf(this.getTotalStats().getEffect(213)) + ";");
			str.append(String.valueOf(this.getTotalStats().getEffect(211)) + ";");
			str.append(String.valueOf(this.getTotalStats().getEffect(212)) + ";");
			str.append(String.valueOf(this.getTotalStats().getEffect(160)) + ";");
			str.append(String.valueOf(this.getTotalStats().getEffect(161)) + ";");
			str.append(this.team);
			break;
		}
		case 10: {
			str.append(this.getDouble().getClasse()).append(";");
			str.append(this.getDouble().get_gfxID()).append("^").append(this.getDouble().get_size()).append(";");
			str.append(this.getDouble().getSexe()).append(";");
			str.append(this.getDouble().getLevel()).append(";");
			str.append(this.getDouble().get_align()).append(",");
			str.append("1,");
			str.append(this.getDouble().is_showWings() ? this.getDouble().getALvl() : "0").append(",");
			str.append(this.getDouble().getId()).append(";");
			str.append((this.getDouble().getColor1() == -1) ? "-1" : Integer.toHexString(this.getDouble().getColor1()))
					.append(";");
			str.append((this.getDouble().getColor2() == -1) ? "-1" : Integer.toHexString(this.getDouble().getColor2()))
					.append(";");
			str.append((this.getDouble().getColor3() == -1) ? "-1" : Integer.toHexString(this.getDouble().getColor3()))
					.append(";");
			str.append(this.getDouble().getGMStuffString()).append(";");
			str.append(this.getPdv()).append(";");
			str.append(this.getTotalStats().getEffect(111)).append(";");
			str.append(this.getTotalStats().getEffect(128)).append(";");
			str.append(this.getTotalStats().getEffect(214)).append(";");
			str.append(this.getTotalStats().getEffect(210)).append(";");
			str.append(this.getTotalStats().getEffect(213)).append(";");
			str.append(this.getTotalStats().getEffect(211)).append(";");
			str.append(this.getTotalStats().getEffect(212)).append(";");
			str.append(this.getTotalStats().getEffect(160)).append(";");
			str.append(this.getTotalStats().getEffect(161)).append(";");
			str.append(this.team).append(";");
			if (this.getDouble().isOnMount() && this.getDouble().getMount() != null) {
				str.append(this.getDouble().getMount().getStringColor(this.getDouble().parsecolortomount()));
			}
			str.append(";");
			break;
		}
		}
		return str.toString();
	}

	@Override
	public int compareTo(final Fighter t) {
		return (this.getPros() > t.getPros() && !this.isInvocation()) ? 1 : 0;
	}
}
