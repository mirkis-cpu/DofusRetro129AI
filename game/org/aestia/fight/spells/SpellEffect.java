// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.spells;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.common.CryptManager;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.entity.monster.Monster;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.traps.Glyph;
import org.aestia.fight.traps.Trap;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.Case;

public class SpellEffect {
	private int effectID;
	private int turns;
	private String jet;
	private int chance;
	private String args;
	private int value;
	private Fighter caster;
	private int spell;
	private int spellLvl;
	private boolean debuffable;
	private boolean startTurn;
	private int duration;
	private final int durationFixed;
	private Case cell;

	public SpellEffect(final int aID, final String aArgs, final int aSpell, final int aSpellLevel) {
		this.turns = 0;
		this.jet = "0d0+0";
		this.chance = 100;
		this.value = 0;
		this.caster = null;
		this.spell = 0;
		this.spellLvl = 1;
		this.debuffable = true;
		this.startTurn = true;
		this.duration = 0;
		this.cell = null;
		this.effectID = aID;
		this.args = aArgs;
		this.spell = aSpell;
		this.spellLvl = aSpellLevel;
		this.durationFixed = 0;
		try {
			this.value = Integer.parseInt(this.args.split(";")[0]);
			this.turns = Integer.parseInt(this.args.split(";")[3]);
			this.chance = Integer.parseInt(this.args.split(";")[4]);
			this.jet = this.args.split(";")[5];
		} catch (Exception ex) {
		}
	}

	public SpellEffect(final int id, final int value2, final int aduration, final int turns2, final boolean debuff,
			final Fighter aCaster, final String args2, final int aspell, final boolean start) {
		this.turns = 0;
		this.jet = "0d0+0";
		this.chance = 100;
		this.value = 0;
		this.caster = null;
		this.spell = 0;
		this.spellLvl = 1;
		this.debuffable = true;
		this.startTurn = true;
		this.duration = 0;
		this.cell = null;
		this.effectID = id;
		this.value = value2;
		this.turns = turns2;
		this.debuffable = debuff;
		this.startTurn = start;
		this.caster = aCaster;
		this.duration = aduration;
		this.durationFixed = this.duration;
		this.args = args2;
		this.spell = aspell;
		try {
			this.jet = this.args.split(";")[5];
		} catch (Exception ex) {
		}
	}

	public boolean isStart() {
		return this.startTurn;
	}

	public boolean getSpell2(final int id) {
		return this.spell == id;
	}

	public int getDuration() {
		return this.duration;
	}

	public int getTurn() {
		return this.turns;
	}

	public boolean isDebuffabe() {
		return this.debuffable;
	}

	public void setTurn(final int turn) {
		this.turns = turn;
	}

	public int getEffectID() {
		return this.effectID;
	}

	public String getJet() {
		return this.jet;
	}

	public int getValue() {
		return this.value;
	}

	public int getChance() {
		return this.chance;
	}

	public String getArgs() {
		return this.args;
	}

	public static ArrayList<Fighter> getTargets(final SpellEffect SE, final Fight fight, final ArrayList<Case> cells) {
		final ArrayList<Fighter> cibles = new ArrayList<Fighter>();
		for (final Case aCell : cells) {
			if (aCell == null) {
				continue;
			}
			final Fighter f = aCell.getFirstFighter();
			if (f == null) {
				continue;
			}
			cibles.add(f);
		}
		return cibles;
	}

	public int getMaxMinSpell(final Fighter fighter, int value) {
		final int val = value;
		if (fighter.hasBuff(782)) {
			int max = Integer.parseInt(this.args.split(";")[1]);
			if (max == -1) {
				max = Integer.parseInt(this.args.split(";")[0]);
			}
			value = max;
		}
		if (fighter.hasBuff(781)) {
			value = Integer.parseInt(this.args.split(";")[0]);
		}
		return val;
	}

	public void setValue(final int i) {
		this.value = i;
	}

	public int decrementDuration() {
		return --this.duration;
	}

	public void applyBeginingBuff(final Fight _fight, final Fighter fighter) {
		final ArrayList<Fighter> cible = new ArrayList<Fighter>();
		cible.add(fighter);
		this.turns = -1;
		this.applyToFight(_fight, this.caster, cible, false);
	}

	public void applyToFight(final Fight fight, final Fighter perso, final Case Cell, final ArrayList<Fighter> cibles) {
		this.cell = Cell;
		this.applyToFight(fight, perso, cibles, false);
	}

	public static int applyOnHitBuffs(int finalDommage, final Fighter target, final Fighter caster, final Fight fight,
			final int elementId) {
		int[] on_HIT_BUFFS;
		for (int length = (on_HIT_BUFFS = Constant.ON_HIT_BUFFS).length, j = 0; j < length; ++j) {
			final int id = on_HIT_BUFFS[j];
			for (final SpellEffect buff : target.getBuffsByEffectID(id)) {
				switch (id) {
				case 138: {
					if (buff.getSpell() == 1039) {
						int stats = 0;
						if (elementId == 4) {
							stats = 217;
						} else if (elementId == 2) {
							stats = 216;
						} else if (elementId == 3) {
							stats = 218;
						} else if (elementId == 0) {
							stats = 219;
						} else if (elementId == 1) {
							stats = 215;
						}
						final int val = target.getBuff(stats).getValue();
						final int turns = target.getBuff(stats).getTurn();
						final int duration = target.getBuff(stats).getDurationFixed();
						final String args = target.getBuff(stats).getArgs();
						GameServer.addToLog(
								"val : " + val + " turns : " + turns + " duration: " + duration + " args : " + args);
						GameServer.addToLog(new StringBuilder().append(Constant.getOppositeStats(stats)).toString());
						int[] oppositeStats;
						for (int length2 = (oppositeStats = Constant
								.getOppositeStats(stats)).length, k = 0; k < length2; ++k) {
							final int i = oppositeStats[k];
							target.addBuff(i, val, turns, duration, true, buff.getSpell(), args, caster, false);
						}
						target.addBuff(stats, val, duration, turns, true, buff.getSpell(), args, caster, false);
						continue;
					}
					continue;
				}
				case 9: {
					final int d = Pathfinding.getDistanceBetween(fight.getMap(), target.getCell().getId(),
							caster.getCell().getId());
					if (d > 1) {
						continue;
					}
					final int chan = buff.getValue();
					final int c = Formulas.getRandomValue(0, 99);
					if (c + 1 >= chan) {
						continue;
					}
					int nbrCase = 0;
					try {
						nbrCase = Integer.parseInt(buff.getArgs().split(";")[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (nbrCase == 0) {
						continue;
					}
					final int exCase = target.getCell().getId();
					int newCellID = Pathfinding.newCaseAfterPush(fight, caster.getCell(), target.getCell(), nbrCase);
					if (newCellID <= 0) {
						int a = -newCellID;
						a = nbrCase - a;
						newCellID = Pathfinding.newCaseAfterPush(fight, caster.getCell(), target.getCell(), a);
						if (newCellID <= 0 || fight.getMap().getCase(newCellID) == null) {
							newCellID = target.getCell().getId();
						}
					}
					target.getCell().getFighters().clear();
					target.setCell(fight.getMap().getCase(newCellID));
					target.getCell().addFighter(target);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 5,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + "," + newCellID);
					final ArrayList<Trap> P = new ArrayList<Trap>();
					P.addAll(fight.getAllTraps());
					for (final Trap p : P) {
						final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(),
								target.getCell().getId());
						if (dist <= p.getSize()) {
							p.onTraped(target);
						}
					}
					if (exCase != newCellID) {
						finalDommage = 0;
						continue;
					}
					continue;
				}
				case 79: {
					try {
						final String[] infos = buff.getArgs().split(";");
						final int coefDom = Integer.parseInt(infos[0]);
						final int coefHeal = Integer.parseInt(infos[1]);
						final int chance = Integer.parseInt(infos[2]);
						final int jet = Formulas.getRandomValue(0, 99);
						if (jet < chance) {
							finalDommage = -(finalDommage * coefHeal);
							if (-finalDommage <= target.getPdvMax() - target.getPdv()) {
								continue;
							}
							finalDommage = -(target.getPdvMax() - target.getPdv());
						} else {
							finalDommage *= coefDom;
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					continue;
				}
				case 107: {
					if (target.getId() == caster.getId()) {
						continue;
					}
					final String[] args2 = buff.getArgs().split(";");
					final float coef = 1 + target.getTotalStats().getEffect(124) / 100;
					int renvoie = 0;
					try {
						if (Integer.parseInt(args2[1]) != -1) {
							renvoie = (int) (coef
									* Formulas.getRandomValue(Integer.parseInt(args2[0]), Integer.parseInt(args2[1])));
						} else {
							renvoie = (int) (coef * Integer.parseInt(args2[0]));
						}
					} catch (Exception e3) {
						e3.printStackTrace();
						return finalDommage;
					}
					if (renvoie > finalDommage) {
						renvoie = finalDommage;
					}
					finalDommage -= renvoie;
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 107, "-1",
							String.valueOf(target.getId()) + "," + renvoie);
					if (renvoie > caster.getPdv()) {
						renvoie = caster.getPdv();
					}
					if (finalDommage < 0) {
						finalDommage = 0;
					}
					if (caster.getPdv() <= renvoie) {
						caster.removePdv(caster, renvoie);
						fight.onFighterDie(caster, caster);
					} else {
						caster.removePdv(caster, renvoie);
					}
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(caster.getId()) + ",-" + renvoie);
					continue;
				}
				case 788: {
					final int taux = (caster.getPersonnage() == null) ? 1 : 2;
					int gain = finalDommage / taux;
					final int stat = buff.getValue();
					int max = 0;
					try {
						max = Integer.parseInt(buff.getArgs().split(";")[1]);
					} catch (Exception e4) {
						e4.printStackTrace();
					}
					if (max == 0) {
						continue;
					}
					if (stat == 108) {
						target.addBuff(stat, max, 5, 1, false, buff.getSpell(), buff.getArgs(), caster, false);
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, stat,
								new StringBuilder(String.valueOf(caster.getId())).toString(),
								String.valueOf(target.getId()) + "," + max + "," + 5);
						target.addPdv(max);
						target.getChatiValue().put(stat, max);
						continue;
					}
					final int a2 = (target.getChatiValue().get(stat) == null) ? 0 : target.getChatiValue().get(stat);
					max -= a2;
					if (gain > max) {
						gain = max;
					}
					target.addBuff(stat, gain, 5, 1, false, buff.getSpell(), buff.getArgs(), caster, false);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, stat,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + gain + "," + 5);
					final int value = a2 + gain;
					target.getChatiValue().put(stat, value);
					continue;
				}
				default: {
					GameServer.addToLog("Effect id " + id
							+ " definie comme ON_HIT_BUFF mais n'a pas d'effet definie dans ce gestionnaire.");
					continue;
				}
				}
			}
		}
		return finalDommage;
	}

	private int getDurationFixed() {
		return this.durationFixed;
	}

	public Fighter getCaster() {
		return this.caster;
	}

	public int getSpell() {
		return this.spell;
	}

	private ArrayList<Fighter> trierCibles(final ArrayList<Fighter> cibles, final Fight fight) {
		final ArrayList<Fighter> _c = new ArrayList<Fighter>();
		int max = -1;
		int distance = -1;
		for (final Fighter f : cibles) {
			distance = Pathfinding.getDistanceBetween(fight.getMap(), this.cell.getId(), f.getCell().getId());
			if (distance > max) {
				max = distance;
			}
		}
		for (int i = max; i >= 0; --i) {
			final Iterator<Fighter> it = cibles.iterator();
			while (it.hasNext()) {
				final Fighter f2 = it.next();
				distance = Pathfinding.getDistanceBetween(fight.getMap(), this.cell.getId(), f2.getCell().getId());
				if (distance == i) {
					_c.add(f2);
					it.remove();
				}
			}
		}
		return _c;
	}

	public void applyToFight(final Fight fight, final Fighter acaster, final ArrayList<Fighter> cibles,
			final boolean isCaC) {
		GameServer.addToLog("Effet id: " + this.effectID + " Args: " + this.args + " turns: " + this.turns + " cibles: "
				+ cibles.size() + " chance: " + this.chance);
		try {
			if (this.turns != -1) {
				this.turns = Integer.parseInt(this.args.split(";")[3]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		this.caster = acaster;
		try {
			this.jet = this.args.split(";")[5];
		} catch (Exception ex) {
		}
		if (this.caster.getPersonnage() != null) {
			final Player perso = this.caster.getPersonnage();
			if (perso.getItemClasseSpell().containsKey(this.spell)) {
				int modi = 0;
				if (this.effectID == 108) {
					modi = perso.getItemClasseModif(this.spell, 284);
				} else if (this.effectID >= 91 && this.effectID <= 100) {
					modi = perso.getItemClasseModif(this.spell, 283);
				}
				final String jeta = this.jet.split("\\+")[0];
				final int bonus = Integer.parseInt(this.jet.split("\\+")[1]) + modi;
				this.jet = String.valueOf(jeta) + "+" + bonus;
			}
		}
		switch (this.effectID) {
		case 4: {
			this.applyEffect_4(fight, cibles);
			break;
		}
		case 5: {
			this.applyEffect_5(cibles, fight);
			break;
		}
		case 6: {
			this.applyEffect_6(cibles, fight);
			break;
		}
		case 7: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 8: {
			this.applyEffect_8(cibles, fight);
			break;
		}
		case 9: {
			this.applyEffect_9(cibles, fight);
			break;
		}
		case 10: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 13: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 34: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 50: {
			this.applyEffect_50(fight);
			break;
		}
		case 51: {
			this.applyEffect_51(fight);
			break;
		}
		case 77: {
			this.applyEffect_77(cibles, fight);
			break;
		}
		case 78: {
			this.applyEffect_78(cibles, fight);
			break;
		}
		case 79: {
			this.applyEffect_79(cibles, fight);
			break;
		}
		case 81: {
			this.applyEffect_81(cibles, fight);
			break;
		}
		case 82: {
			this.applyEffect_82(cibles, fight);
			break;
		}
		case 84: {
			this.applyEffect_84(cibles, fight);
			break;
		}
		case 85: {
			this.applyEffect_85(cibles, fight);
			break;
		}
		case 86: {
			this.applyEffect_86(cibles, fight);
			break;
		}
		case 87: {
			this.applyEffect_87(cibles, fight);
			break;
		}
		case 88: {
			this.applyEffect_88(cibles, fight);
			break;
		}
		case 89: {
			this.applyEffect_89(cibles, fight);
			break;
		}
		case 90: {
			this.applyEffect_90(cibles, fight);
			break;
		}
		case 91: {
			this.applyEffect_91(cibles, fight, isCaC);
			break;
		}
		case 92: {
			this.applyEffect_92(cibles, fight, isCaC);
			break;
		}
		case 93: {
			this.applyEffect_93(cibles, fight, isCaC);
			break;
		}
		case 94: {
			this.applyEffect_94(cibles, fight, isCaC);
			break;
		}
		case 95: {
			this.applyEffect_95(cibles, fight, isCaC);
			break;
		}
		case 96: {
			this.applyEffect_96(cibles, fight, isCaC);
			break;
		}
		case 97: {
			this.applyEffect_97(cibles, fight, isCaC);
			break;
		}
		case 98: {
			this.applyEffect_98(cibles, fight, isCaC);
			break;
		}
		case 99: {
			this.applyEffect_99(cibles, fight, isCaC);
			break;
		}
		case 100: {
			this.applyEffect_100(cibles, fight, isCaC);
			break;
		}
		case 101: {
			this.applyEffect_101(cibles, fight);
			break;
		}
		case 105: {
			this.applyEffect_105(cibles, fight);
			break;
		}
		case 106: {
			this.applyEffect_106(cibles, fight);
			break;
		}
		case 107: {
			this.applyEffect_107(cibles, fight);
			break;
		}
		case 108: {
			this.applyEffect_108(cibles, fight, isCaC);
			break;
		}
		case 109: {
			this.applyEffect_109(fight);
			break;
		}
		case 110: {
			this.applyEffect_110(cibles, fight);
			break;
		}
		case 111: {
			this.applyEffect_111(cibles, fight);
			break;
		}
		case 112: {
			this.applyEffect_112(cibles, fight);
			break;
		}
		case 113: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 114: {
			this.applyEffect_114(cibles, fight);
			break;
		}
		case 115: {
			this.applyEffect_115(cibles, fight);
			break;
		}
		case 116: {
			this.applyEffect_116(cibles, fight);
			break;
		}
		case 117: {
			this.applyEffect_117(cibles, fight);
			break;
		}
		case 118: {
			this.applyEffect_118(cibles, fight);
			break;
		}
		case 119: {
			this.applyEffect_119(cibles, fight);
			break;
		}
		case 120: {
			this.applyEffect_120(cibles, fight);
			break;
		}
		case 121: {
			this.applyEffect_121(cibles, fight);
			break;
		}
		case 122: {
			this.applyEffect_122(cibles, fight);
			break;
		}
		case 123: {
			this.applyEffect_123(cibles, fight);
			break;
		}
		case 124: {
			this.applyEffect_124(cibles, fight);
			break;
		}
		case 125: {
			this.applyEffect_125(cibles, fight);
			break;
		}
		case 126: {
			this.applyEffect_126(cibles, fight);
			break;
		}
		case 127: {
			this.applyEffect_127(cibles, fight);
			break;
		}
		case 128: {
			this.applyEffect_128(cibles, fight);
			break;
		}
		case 130: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 131: {
			this.applyEffect_131(cibles, fight);
			break;
		}
		case 132: {
			this.applyEffect_132(cibles, fight);
			break;
		}
		case 133: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 134: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 135: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 136: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 137: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 138: {
			this.applyEffect_138(cibles, fight);
			break;
		}
		case 139: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 140: {
			this.applyEffect_140(cibles, fight);
			break;
		}
		case 141: {
			this.applyEffect_141(fight, cibles);
			break;
		}
		case 142: {
			this.applyEffect_142(fight, cibles);
			break;
		}
		case 143: {
			this.applyEffect_143(cibles, fight);
			break;
		}
		case 144: {
			this.applyEffect_144(fight, cibles);
			break;
		}
		case 145: {
			this.applyEffect_145(fight, cibles);
			break;
		}
		case 146: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 147: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 148: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 149: {
			this.applyEffect_149(fight, cibles);
			break;
		}
		case 150: {
			this.applyEffect_150(fight, cibles);
			break;
		}
		case 152: {
			this.applyEffect_152(fight, cibles);
			break;
		}
		case 153: {
			this.applyEffect_153(fight, cibles);
			break;
		}
		case 154: {
			this.applyEffect_154(fight, cibles);
			break;
		}
		case 155: {
			this.applyEffect_155(fight, cibles);
			break;
		}
		case 156: {
			this.applyEffect_156(fight, cibles);
			break;
		}
		case 157: {
			this.applyEffect_157(fight, cibles);
			break;
		}
		case 158: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 159: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 160: {
			this.applyEffect_160(fight, cibles);
			break;
		}
		case 161: {
			this.applyEffect_161(fight, cibles);
			break;
		}
		case 162: {
			this.applyEffect_162(fight, cibles);
			break;
		}
		case 163: {
			this.applyEffect_163(fight, cibles);
			break;
		}
		case 164: {
			this.applyEffect_164(cibles, fight);
			break;
		}
		case 165: {
			this.applyEffect_165(fight, cibles);
			break;
		}
		case 166: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 168: {
			this.applyEffect_168(cibles, fight);
			break;
		}
		case 169: {
			this.applyEffect_169(cibles, fight);
			break;
		}
		case 171: {
			this.applyEffect_171(fight, cibles);
			break;
		}
		case 172: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 173: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 174: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 175: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 176: {
			this.applyEffect_176(cibles, fight);
			break;
		}
		case 177: {
			this.applyEffect_177(cibles, fight);
			break;
		}
		case 178: {
			this.applyEffect_178(cibles, fight);
			break;
		}
		case 179: {
			this.applyEffect_179(cibles, fight);
			break;
		}
		case 180: {
			this.applyEffect_180(fight);
			break;
		}
		case 181: {
			this.applyEffect_181(fight);
			break;
		}
		case 182: {
			this.applyEffect_182(fight, cibles);
			break;
		}
		case 183: {
			this.applyEffect_183(fight, cibles);
			break;
		}
		case 184: {
			this.applyEffect_184(fight, cibles);
			break;
		}
		case 185: {
			this.applyEffect_185(fight);
			break;
		}
		case 186: {
			this.applyEffect_186(fight, cibles);
			break;
		}
		case 188: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 194: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 197: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 201: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 202: {
			this.applyEffect_202(fight, cibles);
			break;
		}
		case 206: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 210: {
			this.applyEffect_210(fight, cibles);
			break;
		}
		case 211: {
			this.applyEffect_211(fight, cibles);
			break;
		}
		case 212: {
			this.applyEffect_212(fight, cibles);
			break;
		}
		case 213: {
			this.applyEffect_213(fight, cibles);
			break;
		}
		case 214: {
			this.applyEffect_214(fight, cibles);
			break;
		}
		case 215: {
			this.applyEffect_215(fight, cibles);
			break;
		}
		case 216: {
			this.applyEffect_216(fight, cibles);
			break;
		}
		case 217: {
			this.applyEffect_217(fight, cibles);
			break;
		}
		case 218: {
			this.applyEffect_218(fight, cibles);
			break;
		}
		case 219: {
			this.applyEffect_219(fight, cibles);
			break;
		}
		case 220: {
			this.applyEffect_220(cibles, fight);
			break;
		}
		case 221: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 222: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 225: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 226: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 229: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 230: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 240: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 241: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 242: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 243: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 244: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 245: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 246: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 247: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 248: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 249: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 250: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 251: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 252: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 253: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 254: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 255: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 256: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 257: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 258: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 259: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 260: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 261: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 262: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 263: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 264: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 265: {
			this.applyEffect_265(fight, cibles);
			break;
		}
		case 266: {
			this.applyEffect_266(fight, cibles);
			break;
		}
		case 267: {
			this.applyEffect_267(fight, cibles);
			break;
		}
		case 268: {
			this.applyEffect_268(fight, cibles);
			break;
		}
		case 269: {
			this.applyEffect_269(fight, cibles);
			break;
		}
		case 270: {
			this.applyEffect_270(fight, cibles);
			break;
		}
		case 271: {
			this.applyEffect_271(fight, cibles);
			break;
		}
		case 275: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 276: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 277: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 278: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 279: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 281: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 282: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 283: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 284: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 285: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 286: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 287: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 288: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 289: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 290: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 291: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 292: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 293: {
			this.applyEffect_293(fight);
			break;
		}
		case 294: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 310: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 320: {
			this.applyEffect_320(fight, cibles);
			break;
		}
		case 333: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 335: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 400: {
			this.applyEffect_400(fight);
			break;
		}
		case 401: {
			this.applyEffect_401(fight);
			break;
		}
		case 402: {
			this.applyEffect_402(fight);
			break;
		}
		case 405: {
			this.applyEffect_141(fight, cibles);
			break;
		}
		case 406:
		case 513:
		case 600:
		case 601:
		case 602:
		case 603:
		case 604:
		case 605:
		case 606:
		case 607:
		case 608:
		case 609:
		case 610:
		case 611:
		case 612:
		case 613:
		case 614:
		case 615:
		case 616:
		case 620:
		case 621:
		case 622:
		case 623:
		case 624:
		case 625:
		case 626:
		case 627:
		case 628:
		case 631:
		case 640:
		case 641:
		case 642:
		case 643:
		case 645:
		case 646:
		case 647:
		case 648:
		case 649:
		case 654: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 666: {
			break;
		}
		case 669: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 670: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 671: {
			this.applyEffect_671(cibles, fight);
			break;
		}
		case 672: {
			this.applyEffect_672(cibles, fight);
			break;
		}
		case 699:
		case 700:
		case 701:
		case 702:
		case 705:
		case 706:
		case 710:
		case 715:
		case 716:
		case 717:
		case 720:
		case 724:
		case 725:
		case 730:
		case 731:
		case 732:
		case 740:
		case 741:
		case 742:
		case 750:
		case 751:
		case 752:
		case 753:
		case 760: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 765: {
			this.applyEffect_765(cibles, fight);
			break;
		}
		case 770:
		case 771:
		case 772:
		case 773:
		case 774:
		case 775: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 776: {
			this.applyEffect_776(cibles, fight);
			break;
		}
		case 780: {
			this.applyEffect_780(fight);
			break;
		}
		case 781: {
			this.applyEffect_781(cibles, fight);
			break;
		}
		case 782: {
			this.applyEffect_782(cibles, fight);
			break;
		}
		case 783: {
			this.applyEffect_783(cibles, fight);
			break;
		}
		case 784: {
			this.applyEffect_784(cibles, fight);
			break;
		}
		case 785: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 786: {
			this.applyEffect_786(cibles, fight);
			break;
		}
		case 787: {
			this.applyEffect_787(cibles, fight);
			break;
		}
		case 788: {
			this.applyEffect_788(cibles, fight);
			break;
		}
		case 789:
		case 790:
		case 791:
		case 795:
		case 800:
		case 805:
		case 806:
		case 807:
		case 808:
		case 810:
		case 811:
		case 812:
		case 813:
		case 814:
		case 815:
		case 816:
		case 825:
		case 905:
		case 930:
		case 931:
		case 932:
		case 933:
		case 934:
		case 935:
		case 936:
		case 937:
		case 939:
		case 940:
		case 946:
		case 947:
		case 948:
		case 949: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		case 950: {
			this.applyEffect_950(fight, cibles);
			break;
		}
		case 951: {
			this.applyEffect_951(fight, cibles);
			break;
		}
		case 952:
		case 960:
		case 961:
		case 962:
		case 963:
		case 964:
		case 970:
		case 971:
		case 972:
		case 973:
		case 974:
		case 983:
		case 984:
		case 985:
		case 986:
		case 987:
		case 988:
		case 989:
		case 990:
		case 994:
		case 995:
		case 996:
		case 997:
		case 998:
		case 999: {
			if (Main.modDebug) {
				Console.println("Effet " + this.effectID + " non implante.", Console.Color.ERROR);
				break;
			}
			break;
		}
		default: {
			Console.println("L'effet " + this.effectID + " n existe pas !", Console.Color.ERROR);
			GameServer.addToLog("effet non implante : " + this.effectID + " args: " + this.args);
			break;
		}
		}
	}

	private void applyEffect_202(final Fight fight, final ArrayList<Fighter> cibles) {
		if (this.spell == 113 || this.spell == 64) {
			for (final Fighter target : cibles) {
				if (target.isHide() && target != this.caster) {
					target.unHide(this.spell);
				}
			}
			for (final Trap p : fight.getAllTraps()) {
				p.setIsUnHide(this.caster);
				p.appear(this.caster);
			}
		}
	}

	private void applyEffect_781(final ArrayList<Fighter> cibles, final Fight fight) {
		this.caster.addBuff(this.effectID, this.value, this.turns, 1, this.debuffable, this.spell, this.args,
				this.caster, true);
	}

	private void applyEffect_782(final ArrayList<Fighter> cibles, final Fight fight) {
		this.caster.addBuff(this.effectID, this.value, this.turns, 1, this.debuffable, this.spell, this.args,
				this.caster, true);
	}

	private void applyEffect_165(final Fight fight, final ArrayList<Fighter> cibles) {
		int value = -1;
		try {
			value = Integer.parseInt(this.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (value == -1) {
			return;
		}
		this.caster.addBuff(this.effectID, value, this.turns, 1, true, this.spell, this.args, this.caster, true);
	}

	private void applyEffect_787(final ArrayList<Fighter> objetivos, final Fight pelea) {
		int hechizoID = -1;
		int hechizoNivel = -1;
		try {
			hechizoID = Integer.parseInt(this.args.split(";")[0]);
			hechizoNivel = Integer.parseInt(this.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final Spell hechizo = World.getSort(hechizoID);
		final ArrayList<SpellEffect> EH = hechizo.getStatsByLevel(hechizoNivel).getEffects();
		for (final SpellEffect eh : EH) {
			for (final Fighter objetivo : objetivos) {
				objetivo.addBuff(eh.effectID, eh.value, 1, 1, true, eh.spell, eh.args, this.caster, true);
			}
		}
	}

	private void applyEffect_51(final Fight fight) {
		if (!this.cell.isWalkable(true) || this.cell.getFighters().size() > 0) {
			return;
		}
		final Fighter target = this.caster.getIsHolding();
		if (target == null) {
			return;
		}
		final int distance = Pathfinding.getDistanceBetween(fight.getMap(), target.getCell().getId(),
				this.cell.getId());
		target.setCell(this.cell);
		target.getCell().addFighter(target);
		target.setState(8, 0);
		this.caster.setState(3, 0);
		target.setHoldedBy(null);
		this.caster.setIsHolding(null);
		this.caster.inLancer = true;
		fight.waiterLancer = true;
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 51,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				new StringBuilder(String.valueOf(this.cell.getId())).toString());
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
				new StringBuilder(String.valueOf(target.getId())).toString(),
				String.valueOf(target.getId()) + "," + 8 + ",0");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + 3 + ",0");
		this.caster.getPersonnage().getWaiter().addNow(new Runnable() {
			@Override
			public void run() {
				SpellEffect.this.caster.inLancer = false;
				fight.waiterLancer = false;
			}
		}, 2400 + distance * 150);
	}

	private void applyEffect_950(final Fight fight, final ArrayList<Fighter> cibles) {
		int id = -1;
		try {
			id = Integer.parseInt(this.args.split(";")[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (id == -1) {
			return;
		}
		for (final Fighter target : cibles) {
			if (this.spell == 139 && target.getTeam() != this.caster.getTeam()) {
				continue;
			}
			if (this.turns <= 0) {
				target.setState(id, this.turns);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + id + ",1");
			} else {
				target.setState(id, this.turns);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + id + ",1");
				target.addBuff(this.effectID, this.value, this.turns, 1, false, this.spell, this.args, target, true);
			}
			if (this.spell != 686) {
				continue;
			}
			target.unHide(686);
		}
	}

	private void applyEffect_951(final Fight fight, final ArrayList<Fighter> cibles) {
		int id = -1;
		try {
			id = Integer.parseInt(this.args.split(";")[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (id == -1) {
			return;
		}
		for (final Fighter target : cibles) {
			if (!target.haveState(id)) {
				continue;
			}
			target.setState(id, 0);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + id + ",0");
		}
	}

	private void applyEffect_50(final Fight fight) {
		final Fighter target = this.cell.getFirstFighter();
		if (target == null) {
			return;
		}
		if (target.getMob() != null) {
			int[] static_INVOCATIONS;
			for (int length = (static_INVOCATIONS = Constant.STATIC_INVOCATIONS).length, j = 0; j < length; ++j) {
				final int i = static_INVOCATIONS[j];
				if (i == target.getMob().getTemplate().getId()) {
				}
			}
		}
		if (target.haveState(6)) {
			return;
		}
		target.getCell().getFighters().clear();
		target.setCell(this.caster.getCell());
		target.setState(8, -1);
		this.caster.setState(3, -1);
		target.setHoldedBy(this.caster);
		this.caster.setIsHolding(target);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
				new StringBuilder(String.valueOf(target.getId())).toString(),
				String.valueOf(target.getId()) + "," + 8 + ",1");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 950,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + 3 + ",1");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 50,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				new StringBuilder().append(target.getId()).toString());
	}

	private void applyEffect_788(final ArrayList<Fighter> cibles, final Fight fight) {
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, this.value, this.turns, 1, false, this.spell, this.args, target, true);
		}
	}

	private void applyEffect_131(final ArrayList<Fighter> cibles, final Fight fight) {
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, this.value, this.turns, 1, true, this.spell, this.args, this.caster, false);
		}
	}

	private void applyEffect_185(final Fight fight) {
		final int cellID = this.cell.getId();
		int mobID = -1;
		int level = -1;
		try {
			mobID = Integer.parseInt(this.args.split(";")[0]);
			level = Integer.parseInt(this.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Monster.MobGrade MG = null;
		try {
			MG = World.getMonstre(mobID).getGradeByLevel(level).getCopy();
		} catch (Exception e2) {
			GameServer.addToLog("Erreur sur le monstre id:" + mobID);
			return;
		}
		if (mobID == -1 || level == -1 || MG == null) {
			return;
		}
		final int id = fight.getNextLowerFighterGuid();
		MG.setInFightID(id);
		if (this.caster.getPersonnage() != null) {
			MG.modifStatByInvocator(this.caster);
		}
		final Fighter F = new Fighter(fight, MG);
		F.isStatique = true;
		F.setTeam(this.caster.getTeam());
		F.setInvocator(this.caster);
		fight.getMap().getCase(cellID).addFighter(F);
		F.setCell(fight.getMap().getCase(cellID));
		fight.addFighterInTeam(F, this.caster.getTeam());
		final String gm = F.getGmPacket('+').substring(3);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 181,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), gm);
	}

	private void applyEffect_293(final Fight fight) {
		this.caster.addBuff(this.effectID, this.value, this.turns, 1, false, this.spell, this.args, this.caster, false);
	}

	private void applyEffect_672(final ArrayList<Fighter> cibles, final Fight fight) {
		final double val = Formulas.getRandomJet(this.jet) / 100.0;
		final int pdvMax = this.caster.getPdvMaxOutFight();
		final double pVie = this.caster.getPdv() / this.caster.getPdvMax();
		final double rad = 6.283185307179586 * (pVie - 0.5);
		final double cos = Math.cos(rad);
		final double taux = Math.pow(cos + 1.0, 2.0) / 4.0;
		final double dgtMax = val * pdvMax;
		final int dgt = (int) (taux * dgtMax);
		for (Fighter target : cibles) {
			if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(target.getId()) + ",1");
				target = this.caster;
			}
			if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
				this.applyEffect_765B(fight, target);
				target = target.getBuff(765).getCaster();
			}
			int finalDommage = applyOnHitBuffs(dgt, target, this.caster, fight, 0);
			if (finalDommage > target.getPdv()) {
				finalDommage = target.getPdv();
			}
			target.removePdv(this.caster, finalDommage);
			finalDommage = -finalDommage;
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + finalDommage);
			if (target.getPdv() <= 0) {
				fight.onFighterDie(target, target);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		}
	}

	private void applyEffect_783(final ArrayList<Fighter> cibles, final Fight fight) {
		final Case ccase = this.caster.getCell();
		final char dir = Pathfinding.getDirBetweenTwoCase(ccase.getId(), this.cell.getId(), fight.getMap(), true);
		final int tcellID = Pathfinding.GetCaseIDFromDirrection(ccase.getId(), dir, fight.getMap(), true);
		final Case tcase = fight.getMap().getCase(tcellID);
		if (tcase == null) {
			return;
		}
		if (tcase.getFighters().isEmpty()) {
			return;
		}
		final Fighter target = tcase.getFirstFighter();
		int c1 = tcellID;
		int limite = 0;
		if (target.getMob() != null) {
			int[] static_INVOCATIONS;
			for (int length = (static_INVOCATIONS = Constant.STATIC_INVOCATIONS).length, j = 0; j < length; ++j) {
				final int i = static_INVOCATIONS[j];
				if (i == target.getMob().getTemplate().getId()) {
				}
			}
		}
		while (Pathfinding.GetCaseIDFromDirrection(c1, dir, fight.getMap(), true) != this.cell.getId()) {
			if (Pathfinding.GetCaseIDFromDirrection(c1, dir, fight.getMap(), true) == -1) {
				return;
			}
			c1 = Pathfinding.GetCaseIDFromDirrection(c1, dir, fight.getMap(), true);
			if (++limite > 50) {
				return;
			}
		}
		final Case newCell = Pathfinding.checkIfCanPushEntity(fight, ccase.getId(), this.cell.getId(), dir);
		if (newCell != null) {
			this.cell = newCell;
		}
		target.getCell().getFighters().clear();
		target.setCell(this.cell);
		target.getCell().addFighter(target);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 5,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(target.getId()) + "," + this.cell.getId());
		final ArrayList<Trap> traps = new ArrayList<Trap>();
		traps.addAll(fight.getAllTraps());
		for (final Trap trap : traps) {
			if (Pathfinding.getDistanceBetween(fight.getMap(), trap.getCell().getId(), this.cell.getId()) <= trap
					.getSize()) {
				trap.onTraped(target);
			}
		}
	}

	private void applyEffect_784(final ArrayList<Fighter> cibles, final Fight fight) {
		Map<Integer, Case> origPos = new TreeMap<Integer, Case>();
		origPos = fight.getRholBack();
		ArrayList<Fighter> list = new ArrayList<Fighter>();
		list = fight.getFighters(3);
		for (int i = 1; i < list.size(); ++i) {
			if (!list.isEmpty()) {
				for (final Fighter F : list) {
					if (F != null && !F.isDead()) {
						if (!origPos.containsKey(F.getId())) {
							continue;
						}
						if (F.getCell().getId() == origPos.get(F.getId()).getId()) {
							continue;
						}
						if (origPos.get(F.getId()).getFirstFighter() != null) {
							continue;
						}
						F.getCell().getFighters().clear();
						F.setCell(origPos.get(F.getId()));
						F.getCell().addFighter(F);
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 4,
								new StringBuilder(String.valueOf(F.getId())).toString(),
								String.valueOf(F.getId()) + "," + F.getCell().getId());
					}
				}
			}
		}
	}

	private void applyEffect_9(final ArrayList<Fighter> cibles, final Fight fight) {
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, this.value, this.turns, 1, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_8(final ArrayList<Fighter> cibles, final Fight fight) {
		if (cibles.isEmpty()) {
			return;
		}
		final Fighter target = cibles.get(0);
		if (target == null) {
			return;
		}
		if (target.getMob() != null) {
			int[] static_INVOCATIONS;
			for (int length = (static_INVOCATIONS = Constant.STATIC_INVOCATIONS).length, j = 0; j < length; ++j) {
				final int i = static_INVOCATIONS[j];
				if (i == target.getMob().getTemplate().getId()) {
					return;
				}
			}
		}
		if (target.haveState(6)) {
			return;
		}
		switch (this.spell) {
		case 438: {
			if (target.getTeam() != this.caster.getTeam()) {
				return;
			}
			break;
		}
		case 445: {
			if (target.getTeam() == this.caster.getTeam()) {
				return;
			}
			break;
		}
		}
		target.getCell().getFighters().clear();
		this.caster.getCell().getFighters().clear();
		final Case exTarget = target.getCell();
		final Case exCaster = this.caster.getCell();
		target.setCell(exCaster);
		this.caster.setCell(exTarget);
		target.getCell().addFighter(target);
		this.caster.getCell().addFighter(this.caster);
		final ArrayList<Trap> P = new ArrayList<Trap>();
		P.addAll(fight.getAllTraps());
		for (final Trap p : P) {
			final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(),
					target.getCell().getId());
			final int dist2 = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(),
					this.caster.getCell().getId());
			if (dist <= p.getSize()) {
				p.onTraped(target);
			} else {
				if (dist2 > p.getSize()) {
					continue;
				}
				p.onTraped(this.caster);
			}
		}
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 4,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(target.getId()) + "," + exCaster.getId());
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 4,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + exTarget.getId());
	}

	private void applyEffect_266(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		int vol = 0;
		for (final Fighter target : cibles) {
			target.addBuff(152, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 152,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			vol += val;
		}
		if (vol == 0) {
			return;
		}
		this.caster.addBuff(123, vol, this.turns, 1, true, this.spell, this.args, this.caster, false);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 123,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + vol + "," + this.turns);
	}

	private void applyEffect_267(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		int vol = 0;
		for (final Fighter target : cibles) {
			target.addBuff(153, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 153,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			vol += val;
		}
		if (vol == 0) {
			return;
		}
		this.caster.addBuff(125, vol, this.turns, 1, true, this.spell, this.args, this.caster, false);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 125,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + vol + "," + this.turns);
	}

	private void applyEffect_268(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		int vol = 0;
		for (final Fighter target : cibles) {
			target.addBuff(154, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 154,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			vol += val;
		}
		if (vol == 0) {
			return;
		}
		this.caster.addBuff(119, vol, this.turns, 1, true, this.spell, this.args, this.caster, false);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 119,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + vol + "," + this.turns);
	}

	private void applyEffect_269(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		int vol = 0;
		for (final Fighter target : cibles) {
			target.addBuff(155, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 155,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			vol += val;
		}
		if (vol == 0) {
			return;
		}
		this.caster.addBuff(126, vol, this.turns, 1, true, this.spell, this.args, this.caster, false);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 126,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + vol + "," + this.turns);
	}

	private void applyEffect_270(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		int vol = 0;
		for (final Fighter target : cibles) {
			target.addBuff(156, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 156,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			vol += val;
		}
		if (vol == 0) {
			return;
		}
		this.caster.addBuff(124, vol, this.turns, 1, true, this.spell, this.args, this.caster, false);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 124,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + vol + "," + this.turns);
	}

	private void applyEffect_271(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		int vol = 0;
		for (final Fighter target : cibles) {
			target.addBuff(157, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 157,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			vol += val;
		}
		if (vol == 0) {
			return;
		}
		this.caster.addBuff(118, vol, this.turns, 1, true, this.spell, this.args, this.caster, false);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 118,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + vol + "," + this.turns);
	}

	private void applyEffect_210(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_211(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_212(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_213(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_214(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_215(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_216(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_217(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_218(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_219(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_106(final ArrayList<Fighter> cibles, final Fight fight) {
		int val = -1;
		try {
			val = Integer.parseInt(this.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (val == -1) {
			return;
		}
		this.duration = this.turns;
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_105(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_265(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_155(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_163(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		if (cibles.isEmpty() && this.spell == 310 && this.caster.getOldCible() != null) {
			this.caster.getOldCible().addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args,
					this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getOldCible().getId())).toString(),
					String.valueOf(this.caster.getOldCible().getId()) + "," + this.turns);
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_162(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_161(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_160(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_149(final Fight fight, final ArrayList<Fighter> cibles) {
		int id = -1;
		try {
			id = Integer.parseInt(this.args.split(";")[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (final Fighter target : cibles) {
			if (target.isDead()) {
				continue;
			}
			if (this.spell == 686 && ((target.getPersonnage() != null && target.getPersonnage().getSexe() == 1)
					|| (target.getMob() != null && target.getMob().getTemplate().getId() == 547))) {
				id = 8011;
			}
			if (id == -1) {
				id = target.getDefaultGfx();
			}
			target.addBuff(this.effectID, id, this.turns, 1, true, this.spell, this.args, this.caster, true);
			final int defaut = target.getDefaultGfx();
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(), String.valueOf(target.getId())
							+ "," + defaut + "," + id + "," + (target.canPlay() ? (this.turns + 1) : this.turns));
		}
	}

	private void applyEffect_182(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_184(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_183(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_145(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_171(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_176(final ArrayList<Fighter> objetivos, final Fight pelea) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			return;
		}
		for (final Fighter objetivo : objetivos) {
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, 176,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_142(final Fight fight, final ArrayList<Fighter> cibles) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_150(final Fight fight, final ArrayList<Fighter> cibles) {
		if (this.turns == 0) {
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + this.turns);
		}
	}

	private void applyEffect_402(final Fight fight) {
		if (!this.cell.isWalkable(true)) {
			return;
		}
		final String[] infos = this.args.split(";");
		final int spellID = Short.parseShort(infos[0]);
		final int level = Byte.parseByte(infos[1]);
		final byte duration = Byte.parseByte(infos[3]);
		final String po = World.getSort(this.spell).getStatsByLevel(this.spellLvl).getPorteeType();
		final byte size = (byte) CryptManager.getIntByHashedValue(po.charAt(1));
		final Spell.SortStats TS = World.getSort(spellID).getStatsByLevel(level);
		final Glyph g = new Glyph(fight, this.caster, this.cell, size, TS, duration, this.spell);
		fight.getAllGlyphs().add(g);
		final int unk = g.getColor();
		String str = "GDZ+" + this.cell.getId() + ";" + size + ";" + unk;
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str);
		str = "GDC" + this.cell.getId() + ";Haaaaaaaaa3005;";
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str);
	}

	private void applyEffect_401(final Fight fight) {
		if (!this.cell.isWalkable(true)) {
			return;
		}
		if (this.cell.getFirstFighter() != null) {
			return;
		}
		final String[] infos = this.args.split(";");
		final int spellID = Short.parseShort(infos[0]);
		final int level = Byte.parseByte(infos[1]);
		final byte duration = Byte.parseByte(infos[3]);
		final String po = World.getSort(this.spell).getStatsByLevel(this.spellLvl).getPorteeType();
		final byte size = (byte) CryptManager.getIntByHashedValue(po.charAt(1));
		final Spell.SortStats TS = World.getSort(spellID).getStatsByLevel(level);
		final Glyph g = new Glyph(fight, this.caster, this.cell, size, TS, duration, this.spell);
		fight.getAllGlyphs().add(g);
		final int unk = g.getColor();
		String str = "GDZ+" + this.cell.getId() + ";" + size + ";" + unk;
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str);
		str = "GDC" + this.cell.getId() + ";Haaaaaaaaa3005;";
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str);
	}

	private void applyEffect_400(final Fight fight) {
		if (!this.cell.isWalkable(true)) {
			return;
		}
		if (this.cell.getFirstFighter() != null) {
			return;
		}
		for (final Trap p : fight.getAllTraps()) {
			if (p.getCell().getId() == this.cell.getId()) {
				return;
			}
		}
		final String[] infos = this.args.split(";");
		final int spellID = Short.parseShort(infos[0]);
		final int level = Byte.parseByte(infos[1]);
		final String po = World.getSort(this.spell).getStatsByLevel(this.spellLvl).getPorteeType();
		final byte size = (byte) CryptManager.getIntByHashedValue(po.charAt(1));
		final Spell.SortStats TS = World.getSort(spellID).getStatsByLevel(level);
		final Trap g = new Trap(fight, this.caster, this.cell, size, TS, this.spell);
		fight.getAllTraps().add(g);
		final int unk = g.getColor();
		final int team = this.caster.getTeam() + 1;
		String str = "GDZ+" + this.cell.getId() + ";" + size + ";" + unk;
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, team, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str);
		str = "GDC" + this.cell.getId() + ";Haaaaaaaaz3005;";
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, team, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str);
	}

	private void applyEffect_116(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_117(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
			if (target.canPlay() && target == this.caster) {
				target.getTotalStats().addOneStat(117, val);
			}
		}
	}

	private void applyEffect_118(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_119(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_120(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		this.caster.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
		this.caster.setCurPa(fight, val);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(),
				String.valueOf(this.caster.getId()) + "," + val + "," + this.turns);
	}

	private void applyEffect_78(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_180(final Fight fight) {
		final int cell = this.cell.getId();
		if (!this.cell.getFighters().isEmpty()) {
			return;
		}
		final int id = fight.getNextLowerFighterGuid();
		final Player Clone = Player.ClonePerso(this.caster.getPersonnage(), id, this.caster.getPdvMax());
		Clone.set_fight(fight);
		final Fighter F = new Fighter(fight, Clone);
		F.setTeam(this.caster.getTeam());
		F.setInvocator(this.caster);
		fight.getMap().getCase(cell).addFighter(F);
		F.setCell(fight.getMap().getCase(cell));
		fight.getOrderPlaying().add(fight.getOrderPlaying().indexOf(this.caster) + 1, F);
		fight.addFighterInTeam(F, this.caster.getTeam());
		final String gm = F.getGmPacket('+').substring(3);
		final String gtl = fight.getGTL();
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 180,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), gm);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), gtl);
		final ArrayList<Trap> P = new ArrayList<Trap>();
		P.addAll(fight.getAllTraps());
		for (final Trap p : P) {
			final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(), F.getCell().getId());
			if (dist <= p.getSize()) {
				p.onTraped(F);
			}
		}
	}

	private void applyEffect_181(final Fight fight) {
		if (this.caster.nbrInvoc >= this.caster.getCreaInvo()) {
			return;
		}
		final int cell = this.cell.getId();
		if (!this.cell.getFighters().isEmpty()) {
			return;
		}
		int mobID = -1;
		int level = -1;
		try {
			mobID = Integer.parseInt(this.args.split(";")[0]);
			level = Integer.parseInt(this.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Monster.MobGrade MG = null;
		try {
			MG = World.getMonstre(mobID).getGradeByLevel(level).getCopy();
		} catch (Exception e3) {
			try {
				MG = World.getMonstre(mobID).getRandomGrade().getCopy();
			} catch (Exception e2) {
				new Exception("Erreur d'invocation du monstre " + mobID + ".").printStackTrace();
				e2.printStackTrace();
				GameServer.addToLog("Erreur d'invocation sur le monstre id:" + mobID);
				return;
			}
		}
		if (mobID == -1 || level == -1 || MG == null) {
			return;
		}
		final int id = fight.getNextLowerFighterGuid();
		MG.setInFightID(id);
		if (this.caster.getPersonnage() != null) {
			MG.modifStatByInvocator(this.caster);
		}
		final Fighter F = new Fighter(fight, MG);
		F.setTeam(this.caster.getTeam());
		F.setInvocator(this.caster);
		fight.getMap().getCase(cell).addFighter(F);
		F.setCell(fight.getMap().getCase(cell));
		fight.getOrderPlaying().add(fight.getOrderPlaying().indexOf(this.caster) + 1, F);
		fight.addFighterInTeam(F, this.caster.getTeam());
		final String gm = F.getGmPacket('+').substring(3);
		final String gtl = fight.getGTL();
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 181,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), gm);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), gtl);
		final Fighter caster = this.caster;
		++caster.nbrInvoc;
		final ArrayList<Trap> P = new ArrayList<Trap>();
		P.addAll(fight.getAllTraps());
		for (final Trap p : P) {
			final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(), F.getCell().getId());
			if (dist <= p.getSize()) {
				p.onTraped(F);
			}
		}
	}

	private void applyEffect_110(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_111(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			if (this.spell == 89 && target.getTeam() != this.caster.getTeam()) {
				continue;
			}
			if (this.spell == 101 && target != this.caster) {
				continue;
			}
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			if (target.canPlay() && target == this.caster) {
				target.setCurPa(fight, val);
			}
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_112(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_121(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_122(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_123(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_124(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_125(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_126(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_128(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			if (target.canPlay() && target == this.caster) {
				target.setCurPm(fight, val);
			}
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_138(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_152(final Fight pelea, final ArrayList<Fighter> objetivos) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_153(final Fight pelea, final ArrayList<Fighter> objetivos) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_154(final Fight pelea, final ArrayList<Fighter> objetivos) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_156(final Fight pelea, final ArrayList<Fighter> objetivos) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_157(final Fight pelea, final ArrayList<Fighter> objetivos) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_164(final ArrayList<Fighter> objetivos, final Fight pelea) {
		final int val = this.value;
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		for (final Fighter objetivo : objetivos) {
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
		}
	}

	private void applyEffect_186(final Fight fight, final ArrayList<Fighter> cibles) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur sur getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		final int val2 = val;
		for (final Fighter f : cibles) {
			val = this.getMaxMinSpell(f, val);
			f.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(f.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_220(final ArrayList<Fighter> objetivos, final Fight pelea) {
		if (this.turns < 1) {
			return;
		}
		for (final Fighter objetivo : objetivos) {
			objetivo.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_786(final ArrayList<Fighter> objetivos, final Fight pelea) {
		for (final Fighter objetivo : objetivos) {
			objetivo.addBuff(this.effectID, this.value, this.turns, 1, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_671(final ArrayList<Fighter> objetivos, final Fight pelea) {
		if (this.turns <= 0) {
			Fighter objetivo = objetivos.get(0);
			if (objetivo.hasBuff(106) && objetivo.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, 106,
						new StringBuilder(String.valueOf(objetivo.getId())).toString(),
						String.valueOf(objetivo.getId()) + ",1");
				objetivo = this.caster;
			}
			if (objetivo.hasBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getCaster().isDead()) {
				this.applyEffect_765B(pelea, objetivo);
				objetivo = objetivo.getBuff(765).getCaster();
			}
			int resP = objetivo.getTotalStats().getEffect(214);
			int resF = objetivo.getTotalStats().getEffect(241);
			if (objetivo.getPersonnage() != null) {
				resP += objetivo.getTotalStats().getEffect(254);
				resF += objetivo.getTotalStats().getEffect(264);
			}
			int da\u00f1o = Formulas.getRandomJet(this.args.split(";")[5]);
			da\u00f1o = this.getMaxMinSpell(objetivo, da\u00f1o);
			int val = this.caster.getPdv() / 100 * da\u00f1o;
			val -= resF;
			final int reduc = (int) (val / 100.0f) * resP;
			val -= reduc;
			if (val < 0) {
				val = 0;
			}
			val = applyOnHitBuffs(val, objetivo, this.caster, pelea, -1);
			if (val > objetivo.getPdv()) {
				val = objetivo.getPdv();
			}
			objetivo.removePdv(this.caster, val);
			int cura = val;
			if (objetivo.hasBuff(786) && objetivo.getBuff(786) != null) {
				if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
					cura = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -cura);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, 100,
						new StringBuilder(String.valueOf(objetivo.getId())).toString(),
						String.valueOf(this.caster.getId()) + ",+" + cura);
			}
			val = -val;
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, 100,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val);
			if (objetivo.getPdv() <= 0) {
				pelea.getDeadList().remove(objetivo);
			}
		} else {
			this.caster.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
		}
	}

	private void applyEffect_776(final ArrayList<Fighter> objetivos, final Fight pelea) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		for (final Fighter objetivo : objetivos) {
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_143(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			final String[] jet = this.args.split(";");
			int heal = 0;
			if (jet.length < 6) {
				heal = 1;
			} else {
				heal = Formulas.getRandomJet(jet[5]);
			}
			final int dmg2 = heal;
			for (final Fighter cible : cibles) {
				if (cible.isDead()) {
					continue;
				}
				heal = this.getMaxMinSpell(cible, heal);
				int healFinal = Formulas.calculFinalHealCac(this.caster, heal, false);
				if (this.spell == 450) {
					healFinal = heal;
				}
				if (healFinal + cible.getPdv() > cible.getPdvMax()) {
					healFinal = cible.getPdvMax() - cible.getPdv();
				}
				if (healFinal < 1) {
					healFinal = 0;
				}
				cible.removePdv(this.caster, -healFinal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 108,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(cible.getId()) + "," + healFinal);
				heal = dmg2;
			}
		} else {
			for (final Fighter cible2 : cibles) {
				if (cible2.isDead()) {
					continue;
				}
				cible2.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_177(final ArrayList<Fighter> objetivos, final Fight pelea) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		for (final Fighter objetivo : objetivos) {
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, 177,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_178(final ArrayList<Fighter> objetivos, final Fight pelea) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_179(final ArrayList<Fighter> objetivos, final Fight pelea) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_144(final Fight pelea, final ArrayList<Fighter> objetivos) {
		int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			Console.println("Erreur de valeur pour le jet aleatoire : " + this.effectID, Console.Color.ERROR);
			return;
		}
		final int val2 = val;
		for (final Fighter objetivo : objetivos) {
			val = this.getMaxMinSpell(objetivo, val);
			objetivo.addBuff(145, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(pelea, 7, 145,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(objetivo.getId()) + "," + val + "," + this.turns);
			val = val2;
		}
	}

	private void applyEffect_114(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_" + this.effectID + ")");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, true);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_115(final ArrayList<Fighter> cibles, final Fight fight) {
		final int val = Formulas.getRandomJet(this.jet);
		if (val == -1) {
			GameServer.addToLog("Erreur de valeur pour getRandomJet (applyEffect_115)");
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, val, this.turns, 1, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, this.effectID,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + val + "," + this.turns);
		}
	}

	private void applyEffect_77(final ArrayList<Fighter> cibles, final Fight fight) {
		int value = 1;
		try {
			value = Integer.parseInt(this.args.split(";")[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int num = 0;
		for (final Fighter target : cibles) {
			final int val = Formulas.getPointsLost('m', value, this.caster, target);
			if (val < value) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 309,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + (value - val));
			}
			if (val < 1) {
				continue;
			}
			target.addBuff(127, val, 1, 0, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 127,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + ",-" + val + "," + this.turns);
			num += val;
		}
		if (num != 0) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 128,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(this.caster.getId()) + "," + num + "," + this.turns);
			this.caster.addBuff(128, num, this.turns, 1, true, this.spell, this.args, this.caster, false);
			if (this.caster.canPlay()) {
				this.caster.setCurPm(fight, num);
			}
		}
	}

	private void applyEffect_84(final ArrayList<Fighter> cibles, final Fight fight) {
		int value = 1;
		try {
			value = Integer.parseInt(this.args.split(";")[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int num = 0;
		for (final Fighter target : cibles) {
			final int val = Formulas.getPointsLost('m', value, this.caster, target);
			if (val < value) {
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 308,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + (value - val));
			}
			if (val < 1) {
				continue;
			}
			if (this.spell == 95) {
				target.addBuff(101, val, 1, 1, true, this.spell, this.args, this.caster, false);
			} else {
				target.addBuff(101, val, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 101,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + ",-" + val + "," + this.turns);
			num += val;
		}
		if (num != 0) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 111,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(this.caster.getId()) + "," + num + "," + this.turns);
			this.caster.addBuff(111, num, 0, 0, true, this.spell, this.args, this.caster, false);
			if (this.caster.canPlay()) {
				this.caster.setCurPa(fight, num);
			}
		}
	}

	private void applyEffect_168(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (final Fighter cible : cibles) {
				if (cible.isDead()) {
					continue;
				}
				cible.addBuff(this.effectID, this.value, 1, 1, true, this.spell, this.args, this.caster, false);
				if (this.turns <= 1 || this.duration <= 1) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 168,
							new StringBuilder(String.valueOf(cible.getId())).toString(),
							String.valueOf(cible.getId()) + ",-" + this.value);
				}
				if (fight.getFighterByOrdreJeu() != cible) {
					continue;
				}
				fight.setCurFighterPa(fight.getCurFighterPa() - this.value);
			}
		} else {
			boolean repetibles = false;
			for (final Fighter cible2 : cibles) {
				if (cible2.isDead()) {
					continue;
				}
				if (this.spell == 197 || this.spell == 112) {
					cible2.addBuff(this.effectID, this.value, this.turns, this.turns, true, this.spell, this.args,
							this.caster, false);
				} else if (this.spell == 115) {
					if (!repetibles) {
						final int lostPA = Formulas.getRandomJet(this.jet);
						if (lostPA == -1) {
							continue;
						}
						this.value = lostPA;
					}
					cible2.addBuff(this.effectID, this.value, this.turns, this.turns, true, this.spell, this.args,
							this.caster, false);
					repetibles = true;
				} else {
					cible2.addBuff(this.effectID, this.value, 1, 1, true, this.spell, this.args, this.caster, false);
				}
				if (this.turns <= 1 || this.duration <= 1) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 168,
							new StringBuilder(String.valueOf(cible2.getId())).toString(),
							String.valueOf(cible2.getId()) + ",-" + this.value);
				}
				if (fight.getFighterByOrdreJeu() != cible2) {
					continue;
				}
				fight.setCurFighterPa(fight.getCurFighterPa() - this.value);
			}
		}
	}

	private void applyEffect_169(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (final Fighter cible : cibles) {
				if (cible.isDead()) {
					continue;
				}
				cible.addBuff(this.effectID, this.value, 1, 1, true, this.spell, this.args, this.caster, false);
				if (this.turns > 1 && this.duration > 1) {
					continue;
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 169,
						new StringBuilder(String.valueOf(cible.getId())).toString(),
						String.valueOf(cible.getId()) + ",-" + this.value);
			}
		} else {
			if (cibles.isEmpty() && this.spell == 120 && this.caster.getOldCible() != null) {
				this.caster.getOldCible().addBuff(this.effectID, this.value, this.turns, this.turns, false, this.spell,
						this.args, this.caster, false);
				if (this.turns <= 1 || this.duration <= 1) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 169,
							new StringBuilder(String.valueOf(this.caster.getOldCible().getId())).toString(),
							String.valueOf(this.caster.getOldCible().getId()) + ",-" + this.value);
				}
			}
			for (final Fighter cible : cibles) {
				boolean repetibles = false;
				if (cible.isDead()) {
					continue;
				}
				if (this.spell == 192) {
					cible.addBuff(this.effectID, this.value, this.turns, 0, true, this.spell, this.args, this.caster,
							false);
				} else if (this.spell == 115) {
					if (!repetibles) {
						final int lostPM = Formulas.getRandomJet(this.jet);
						if (lostPM == -1) {
							continue;
						}
						this.value = lostPM;
					}
					cible.addBuff(this.effectID, this.value, this.turns, this.turns, true, this.spell, this.args,
							this.caster, false);
					repetibles = true;
				} else if (this.spell == 197) {
					cible.addBuff(this.effectID, this.value, this.turns, this.turns, true, this.spell, this.args,
							this.caster, false);
				} else {
					cible.addBuff(this.effectID, this.value, 1, 1, true, this.spell, this.args, this.caster, false);
				}
				if (this.turns > 1 && this.duration > 1) {
					continue;
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 169,
						new StringBuilder(String.valueOf(cible.getId())).toString(),
						String.valueOf(cible.getId()) + ",-" + this.value);
			}
		}
	}

	private void applyEffect_101(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (final Fighter target : cibles) {
				if (target.hasBuff(788) && target.getBuff(788).getValue() == 101) {
					target.addBuff(111, this.value, this.duration, this.turns, true, target.getBuff(788).getSpell(),
							this.args, target, false);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 101,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",+" + this.value);
				}
				final int retrait = Formulas.getPointsLost('a', this.value, this.caster, target);
				if (this.value - retrait > 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 308,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + (this.value - retrait));
				}
				if (retrait > 0) {
					target.addBuff(101, retrait, 1, 1, false, this.spell, this.args, this.caster, false);
					if (this.turns <= 1 || this.duration <= 1) {
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 101,
								new StringBuilder(String.valueOf(target.getId())).toString(),
								String.valueOf(target.getId()) + ",-" + retrait);
					}
				}
				if (fight.getFighterByOrdreJeu() == target) {
					fight.setCurFighterPa(fight.getCurFighterPa() - retrait);
				}
			}
		} else {
			for (final Fighter target : cibles) {
				if (target.hasBuff(788) && target.getBuff(788).getValue() == 101) {
					final SpellEffect SE = target.getBuff(788);
					target.addBuff(111, this.value, SE.getDurationFixed(), this.turns, true,
							target.getBuff(788).getSpell(), this.args, target, false);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 101,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",+" + this.value);
				}
				final int retrait = Formulas.getPointsLost('a', this.value, this.caster, target);
				if (this.value - retrait > 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 308,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + (this.value - retrait));
				}
				if (retrait > 0) {
					target.addBuff(this.effectID, retrait, 1, 1, false, this.spell, this.args, this.caster, false);
					if (this.turns <= 1 || this.duration <= 1) {
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 101,
								new StringBuilder(String.valueOf(target.getId())).toString(),
								String.valueOf(target.getId()) + ",-" + retrait);
					}
				}
				if (fight.getFighterByOrdreJeu() == target) {
					fight.setCurFighterPa(fight.getCurFighterPa() - retrait);
				}
			}
		}
	}

	private void applyEffect_127(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.spell == 907) {
			for (final Fighter target : cibles) {
				final int retrait = Formulas.getPointsLost('m', this.value, this.caster, target);
				target.setCurPm(fight, -retrait);
			}
		} else if (this.turns <= 0) {
			for (final Fighter target : cibles) {
				final int retrait = Formulas.getPointsLost('m', this.value, this.caster, target);
				if (this.value - retrait > 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 309,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + (this.value - retrait));
				}
				if (retrait > 0) {
					target.addBuff(127, retrait, 1, 1, false, this.spell, this.args, this.caster, false);
					if (this.turns > 1 && this.duration > 1) {
						continue;
					}
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 127,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",-" + retrait);
				}
			}
		} else {
			for (final Fighter target : cibles) {
				final int retrait = Formulas.getPointsLost('m', this.value, this.caster, target);
				if (this.value - retrait > 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 309,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + (this.value - retrait));
				}
				if (retrait > 0) {
					if (this.spell == 136) {
						target.addBuff(this.effectID, retrait, this.turns, this.turns, false, this.spell, this.args,
								this.caster, false);
					} else {
						target.addBuff(this.effectID, retrait, 1, 1, false, this.spell, this.args, this.caster, false);
					}
					if (this.turns > 1 && this.duration > 1) {
						continue;
					}
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 127,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",-" + retrait);
				}
			}
		}
	}

	private void applyEffect_107(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns < 1) {
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_79(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns < 1) {
			return;
		}
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, -1, this.turns, 0, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_4(final Fight fight, final ArrayList<Fighter> cibles) {
		if (this.turns > 1) {
			return;
		}
		if (this.cell.isWalkable(true) && !fight.isOccuped(this.cell.getId())) {
			this.caster.getCell().getFighters().clear();
			this.caster.setCell(this.cell);
			this.caster.getCell().addFighter(this.caster);
			final ArrayList<Trap> P = new ArrayList<Trap>();
			P.addAll(fight.getAllTraps());
			for (final Trap p : P) {
				final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(),
						this.caster.getCell().getId());
				if (dist <= p.getSize()) {
					p.onTraped(this.caster);
				}
			}
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 4,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(this.caster.getId()) + "," + this.cell.getId());
		} else {
			GameServer.addToLog("Tentative de teleportation echouee : case non libre:");
			GameServer.addToLog("IsOccuped: " + fight.isOccuped(this.cell.getId()));
			GameServer.addToLog("Walkable: " + this.cell.isWalkable(true));
		}
	}

	private void applyEffect_765B(final Fight fight, final Fighter target) {
		final Fighter sacrified = target.getBuff(765).getCaster();
		final Case cell1 = sacrified.getCell();
		final Case cell2 = target.getCell();
		sacrified.getCell().getFighters().clear();
		target.getCell().getFighters().clear();
		sacrified.setCell(cell2);
		sacrified.getCell().addFighter(sacrified);
		target.setCell(cell1);
		target.getCell().addFighter(target);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 4,
				new StringBuilder(String.valueOf(target.getId())).toString(),
				String.valueOf(target.getId()) + "," + cell1.getId());
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 4,
				new StringBuilder(String.valueOf(sacrified.getId())).toString(),
				String.valueOf(sacrified.getId()) + "," + cell2.getId());
	}

	private void applyEffect_109(final Fight fight) {
		if (this.turns <= 0) {
			final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
			int finalDommage = Formulas.calculFinalDommage(fight, this.caster, this.caster, -1, dmg, false, false,
					this.spell);
			finalDommage = applyOnHitBuffs(finalDommage, this.caster, this.caster, fight, -1);
			if (finalDommage > this.caster.getPdv()) {
				finalDommage = this.caster.getPdv();
			}
			this.caster.removePdv(this.caster, finalDommage);
			finalDommage = -finalDommage;
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(this.caster.getId()) + "," + finalDommage);
			if (this.caster.getPdv() <= 0) {
				fight.onFighterDie(this.caster, this.caster);
			}
		} else {
			this.caster.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
		}
	}

	private void applyEffect_82(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				int finalDommage = dmg;
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, -1);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, target);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, true);
			}
		}
	}

	private void applyEffect_6(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (final Fighter target : cibles) {
				if (target.getMob() != null) {
					int[] static_INVOCATIONS;
					for (int length = (static_INVOCATIONS = Constant.STATIC_INVOCATIONS).length, j = 0; j < length; ++j) {
						final int i = static_INVOCATIONS[j];
						if (i == target.getMob().getTemplate().getId()) {
						}
					}
				}
				if (target.haveState(6)) {
					continue;
				}
				Case eCell = this.cell;
				if (target.getCell().getId() == this.cell.getId()) {
					eCell = this.caster.getCell();
				}
				int newCellID = Pathfinding.newCaseAfterPush(fight, eCell, target.getCell(), -this.value);
				if (newCellID == 0) {
					return;
				}
				if (newCellID < 0) {
					final int a = -(this.value + newCellID);
					newCellID = Pathfinding.newCaseAfterPush(fight, this.caster.getCell(), target.getCell(), a);
					if (newCellID == 0) {
						return;
					}
					if (fight.getMap().getCase(newCellID) == null) {
						return;
					}
				}
				target.getCell().getFighters().clear();
				target.setCell(fight.getMap().getCase(newCellID));
				target.getCell().addFighter(target);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 5,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + newCellID);
				final ArrayList<Trap> P = new ArrayList<Trap>();
				P.addAll(fight.getAllTraps());
				for (final Trap p : P) {
					final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(),
							target.getCell().getId());
					if (dist <= p.getSize()) {
						p.onTraped(target);
					}
				}
			}
		}
	}

	private void applyEffect_5(ArrayList<Fighter> cibles, final Fight fight) {
		if (((cibles.size() == 1 && this.spell == 120) || this.spell == 310) && !cibles.get(0).isDead()) {
			this.caster.setOldCible(cibles.get(0));
		}
		if (this.turns <= 0) {
			cibles = this.trierCibles(cibles, fight);
			for (final Fighter target : cibles) {
				if (target.getMob() != null) {
					int[] static_INVOCATIONS;
					for (int length = (static_INVOCATIONS = Constant.STATIC_INVOCATIONS).length, j = 0; j < length; ++j) {
						final int i = static_INVOCATIONS[j];
						if (i == target.getMob().getTemplate().getId()) {
						}
					}
				}
				if (target.haveState(6)) {
					continue;
				}
				Case eCell = this.cell;
				if (Pathfinding.getDistanceBetween(fight.getMap(), this.caster.getCell().getId(), this.cell.getId()) > 1
						&& target.getCell().getId() == this.cell.getId() && this.spell != 165) {
					continue;
				}
				if (target.getCell().getId() == this.cell.getId()) {
					eCell = this.caster.getCell();
				}
				int newCellID = Pathfinding.newCaseAfterPush(fight, eCell, target.getCell(), this.value);
				if (newCellID == 0) {
					continue;
				}
				if (newCellID < 0) {
					int a = -newCellID;
					final int coef = Formulas.getRandomJet("1d7+1");
					double b = 0.0;
					if (this.caster.isInvocation()) {
						b = this.caster.getInvocator().getLvl() / 50.0;
					} else {
						b = this.caster.getLvl() / 50.0;
					}
					if (b < 0.1) {
						b = 0.1;
					}
					final double c = b * a;
					int finalDommage = (int) (8.0 + coef * c);
					if (finalDommage < 1) {
						finalDommage = 1;
					}
					if (finalDommage > target.getPdv()) {
						finalDommage = target.getPdv();
					}
					if (target.hasBuff(184)) {
						finalDommage -= target.getBuff(184).getValue();
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
								new StringBuilder(String.valueOf(this.caster.getId())).toString(),
								String.valueOf(target.getId()) + "," + target.getBuff(184).getValue());
					}
					if (finalDommage > 0) {
						target.removePdv(this.caster, finalDommage);
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
								new StringBuilder(String.valueOf(this.caster.getId())).toString(),
								String.valueOf(target.getId()) + ",-" + finalDommage);
						if (target.getPdv() <= 0) {
							fight.onFighterDie(target, this.caster);
							if (target.canPlay() && target.getPersonnage() != null) {
								fight.endTurn(false);
								continue;
							}
							if (target.canPlay()) {
								target.setCanPlay(false);
								continue;
							}
							continue;
						}
					}
					a = this.value - a;
					newCellID = Pathfinding.newCaseAfterPush(fight, this.caster.getCell(), target.getCell(), a);
					if (newCellID == 0) {
						continue;
					}
				}
				if (fight.getMap().getCase(newCellID) == null) {
					continue;
				}
				target.getCell().getFighters().clear();
				target.setCell(fight.getMap().getCase(newCellID));
				target.getCell().addFighter(target);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 5,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + newCellID);
				final ArrayList<Trap> P = new ArrayList<Trap>();
				P.addAll(fight.getAllTraps());
				for (final Trap p : P) {
					final int dist = Pathfinding.getDistanceBetween(fight.getMap(), p.getCell().getId(),
							target.getCell().getId());
					if (dist <= p.getSize()) {
						p.onTraped(target);
					}
				}
			}
		}
	}

	private void applyEffect_91(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (isCaC) {
			for (Fighter target : cibles) {
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 2, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 2);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else if (this.turns <= 0) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 2, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 2);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, target);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_92(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.caster.isHide()) {
			this.caster.unHide(this.spell);
		}
		if (isCaC) {
			for (Fighter target : cibles) {
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 1, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 1);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 1, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 1);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, target);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_93(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.caster.isHide()) {
			this.caster.unHide(this.spell);
		}
		if (isCaC) {
			for (Fighter target : cibles) {
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 4, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 4);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 4, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 4);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, target);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_94(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.caster.isHide()) {
			this.caster.unHide(this.spell);
		}
		if (isCaC) {
			for (Fighter target : cibles) {
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 3, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 3);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 3, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 3);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, target);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_95(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.caster.isHide()) {
			this.caster.unHide(this.spell);
		}
		if (isCaC) {
			for (Fighter target : cibles) {
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 0, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 0);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 0, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 0);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				int heal = -finalDommage / 2;
				if (this.caster.getPdv() + heal > this.caster.getPdvMax()) {
					heal = this.caster.getPdvMax() - this.caster.getPdv();
				}
				this.caster.removePdv(this.caster, -heal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(target.getId())).toString(),
						String.valueOf(this.caster.getId()) + "," + heal);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, target);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_85(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int resP = target.getTotalStats().getEffect(211);
				int resF = target.getTotalStats().getEffect(243);
				if (target.getPersonnage() != null) {
					resP += target.getTotalStats().getEffect(251);
					resF += target.getTotalStats().getEffect(261);
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int val = this.caster.getPdv() / 100 * dmg;
				val -= resF;
				final int reduc = (int) (val / 100.0f) * resP;
				val -= reduc;
				if (val < 0) {
					val = 0;
				}
				val = applyOnHitBuffs(val, target, this.caster, fight, -1);
				if (val > target.getPdv()) {
					val = target.getPdv();
				}
				target.removePdv(this.caster, val);
				int cura = val;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				val = -val;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + val);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_86(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int resP = target.getTotalStats().getEffect(210);
				int resF = target.getTotalStats().getEffect(242);
				if (target.getPersonnage() != null) {
					resP += target.getTotalStats().getEffect(250);
					resF += target.getTotalStats().getEffect(260);
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int val = this.caster.getPdv() / 100 * dmg;
				val -= resF;
				final int reduc = (int) (val / 100.0f) * resP;
				val -= reduc;
				if (val < 0) {
					val = 0;
				}
				val = applyOnHitBuffs(val, target, this.caster, fight, -1);
				if (val > target.getPdv()) {
					val = target.getPdv();
				}
				target.removePdv(this.caster, val);
				int cura = val;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				val = -val;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + val);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_87(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int resP = target.getTotalStats().getEffect(212);
				int resF = target.getTotalStats().getEffect(244);
				if (target.getPersonnage() != null) {
					resP += target.getTotalStats().getEffect(252);
					resF += target.getTotalStats().getEffect(262);
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int val = this.caster.getPdv() / 100 * dmg;
				val -= resF;
				final int reduc = (int) (val / 100.0f) * resP;
				val -= reduc;
				if (val < 0) {
					val = 0;
				}
				val = applyOnHitBuffs(val, target, this.caster, fight, -1);
				if (val > target.getPdv()) {
					val = target.getPdv();
				}
				target.removePdv(this.caster, val);
				int cura = val;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				val = -val;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + val);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_88(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int resP = target.getTotalStats().getEffect(213);
				int resF = target.getTotalStats().getEffect(240);
				if (target.getPersonnage() != null) {
					resP += target.getTotalStats().getEffect(253);
					resF += target.getTotalStats().getEffect(263);
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int val = this.caster.getPdv() / 100 * dmg;
				val -= resF;
				final int reduc = (int) (val / 100.0f) * resP;
				val -= reduc;
				if (val < 0) {
					val = 0;
				}
				val = applyOnHitBuffs(val, target, this.caster, fight, -1);
				if (val > target.getPdv()) {
					val = target.getPdv();
				}
				target.removePdv(this.caster, val);
				int cura = val;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				val = -val;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + val);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_89(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int resP = target.getTotalStats().getEffect(214);
				int resF = target.getTotalStats().getEffect(241);
				if (target.getPersonnage() != null) {
					resP += target.getTotalStats().getEffect(254);
					resF += target.getTotalStats().getEffect(264);
				}
				final int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				int val = this.caster.getPdv() / 100 * dmg;
				val -= resF;
				final int reduc = (int) (val / 100.0f) * resP;
				val -= reduc;
				int armor = 0;
				for (final SpellEffect SE : target.getBuffsByEffectID(105)) {
					final int intell = target.getTotalStats().getEffect(126);
					final int carac = target.getTotalStats().getEffect(118);
					final int value = SE.getValue();
					final int a = value * (100 + intell / 2 + carac / 2) / 100;
					armor += a;
				}
				if (armor > 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + armor);
					val -= armor;
				}
				if (val < 0) {
					val = 0;
				}
				val = applyOnHitBuffs(val, target, this.caster, fight, -1);
				if (val > target.getPdv()) {
					val = target.getPdv();
				}
				target.removePdv(this.caster, val);
				int cura = val;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				val = -val;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + val);
				if (target.getPdv() <= 0) {
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_96(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (isCaC) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 2, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 2);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else if (this.turns <= 0) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 2, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 2);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_97(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (isCaC) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 1, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 1);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else if (this.turns <= 0) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				if (this.spell == 160 && target == this.caster) {
					continue;
				}
				if (this.chance > 0 && this.spell == 108) {
					int fDommage = Formulas.calculFinalDommage(fight, this.caster, this.caster, 1, dmg, false, false,
							this.spell);
					fDommage = applyOnHitBuffs(fDommage, this.caster, this.caster, fight, 1);
					if (fDommage > this.caster.getPdv()) {
						fDommage = this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, fDommage);
					fDommage = -fDommage;
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(this.caster.getId()) + "," + fDommage);
					if (this.caster.getPdv() > 0) {
						continue;
					}
					fight.onFighterDie(this.caster, target);
				} else {
					int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 1, dmg, false, false,
							this.spell);
					finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 1);
					if (finalDommage > target.getPdv()) {
						finalDommage = target.getPdv();
					}
					target.removePdv(this.caster, finalDommage);
					int cura = finalDommage;
					if (target.hasBuff(786)) {
						if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
							cura = this.caster.getPdvMax() - this.caster.getPdv();
						}
						this.caster.removePdv(this.caster, -cura);
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
								new StringBuilder(String.valueOf(target.getId())).toString(),
								String.valueOf(this.caster.getId()) + ",+" + cura);
					}
					finalDommage = -finalDommage;
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(this.caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + finalDommage);
					if (target.getPdv() > 0) {
						continue;
					}
					fight.onFighterDie(target, this.caster);
					if (target.canPlay() && target.getPersonnage() != null) {
						fight.endTurn(false);
					} else {
						if (!target.canPlay()) {
							continue;
						}
						target.setCanPlay(false);
					}
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_98(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (isCaC) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 4, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 4);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else if (this.turns <= 0) {
			if (this.caster.isHide()) {
				this.caster.unHide(this.spell);
			}
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 4, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 4);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_99(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.caster.isHide()) {
			this.caster.unHide(this.spell);
		}
		if (isCaC) {
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 3, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 3);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (this.spell == 36 && target == this.caster) {
					continue;
				}
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 3, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 3);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_100(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.caster.isHide()) {
			this.caster.unHide(this.spell);
		}
		if (isCaC) {
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 0, dmg, false, true,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 0);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else if (this.turns <= 0) {
			for (Fighter target : cibles) {
				if (this.caster.isMob() && this.caster.getTeam2() == target.getTeam2() && !this.caster.isInvocation()) {
					continue;
				}
				if (target.hasBuff(106) && target.getBuffValue(106) >= this.spellLvl && this.spell != 0) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 106,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(target.getId()) + ",1");
					target = this.caster;
				}
				if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
					this.applyEffect_765B(fight, target);
					target = target.getBuff(765).getCaster();
				}
				int dmg = Formulas.getRandomJet(this.args.split(";")[5]);
				for (final SpellEffect SE : this.caster.getBuffsByEffectID(293)) {
					if (SE.getValue() == this.spell) {
						int add = -1;
						try {
							add = Integer.parseInt(SE.getArgs().split(";")[2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (add <= 0) {
							continue;
						}
						dmg += add;
					}
				}
				int finalDommage = Formulas.calculFinalDommage(fight, this.caster, target, 0, dmg, false, false,
						this.spell);
				finalDommage = applyOnHitBuffs(finalDommage, target, this.caster, fight, 0);
				if (finalDommage > target.getPdv()) {
					finalDommage = target.getPdv();
				}
				target.removePdv(this.caster, finalDommage);
				int cura = finalDommage;
				if (target.hasBuff(786)) {
					if (cura + this.caster.getPdv() > this.caster.getPdvMax()) {
						cura = this.caster.getPdvMax() - this.caster.getPdv();
					}
					this.caster.removePdv(this.caster, -cura);
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
							new StringBuilder(String.valueOf(target.getId())).toString(),
							String.valueOf(this.caster.getId()) + ",+" + cura);
				}
				finalDommage = -finalDommage;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + "," + finalDommage);
				if (target.getPdv() > 0) {
					continue;
				}
				fight.onFighterDie(target, this.caster);
				if (target.canPlay() && target.getPersonnage() != null) {
					fight.endTurn(false);
				} else {
					if (!target.canPlay()) {
						continue;
					}
					target.setCanPlay(false);
				}
			}
		} else {
			for (final Fighter target : cibles) {
				target.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_132(final ArrayList<Fighter> cibles, final Fight fight) {
		for (final Fighter target : cibles) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 132,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					new StringBuilder(String.valueOf(target.getId())).toString());
			fight.getWaiter().addNow(new Runnable() {
				@Override
				public void run() {
					target.debuff();
					if (target.isHide()) {
						target.unHide(SpellEffect.this.spell);
					}
				}
			}, 3000L);
		}
	}

	private void applyEffect_140(final ArrayList<Fighter> cibles, final Fight fight) {
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, 0, 1, 0, true, this.spell, this.args, this.caster, false);
		}
	}

	private void applyEffect_765(final ArrayList<Fighter> cibles, final Fight fight) {
		for (final Fighter target : cibles) {
			target.addBuff(this.effectID, 0, this.turns, 1, true, this.spell, this.args, this.caster, true);
		}
	}

	private void applyEffect_81(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			final String[] jet = this.args.split(";");
			int heal = 0;
			if (jet.length < 6) {
				heal = 1;
			} else {
				heal = Formulas.getRandomJet(jet[5]);
			}
			final int heal2 = heal;
			for (final Fighter cible : cibles) {
				if (cible.isDead()) {
					continue;
				}
				heal = this.getMaxMinSpell(cible, heal);
				final int pdvMax = cible.getPdvMax();
				int healFinal = Formulas.calculFinalHealCac(this.caster, heal, false);
				if (healFinal + cible.getPdv() > pdvMax) {
					healFinal = pdvMax - cible.getPdv();
				}
				if (healFinal < 1) {
					healFinal = 0;
				}
				cible.removePdv(this.caster, -healFinal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 108,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(cible.getId()) + "," + healFinal);
				heal = heal2;
			}
		} else {
			for (final Fighter cible2 : cibles) {
				if (cible2.isDead()) {
					continue;
				}
				cible2.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_90(final ArrayList<Fighter> cibles, final Fight fight) {
		if (this.turns <= 0) {
			final int pAge = Formulas.getRandomJet(this.args.split(";")[5]);
			int val = pAge * (this.caster.getPdv() / 100);
			int finalDommage = applyOnHitBuffs(val, this.caster, this.caster, fight, -1);
			if (finalDommage > this.caster.getPdv()) {
				finalDommage = this.caster.getPdv();
			}
			this.caster.removePdv(this.caster, finalDommage);
			finalDommage = -finalDommage;
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(this.caster.getId()) + "," + finalDommage);
			for (final Fighter target : cibles) {
				if (val + target.getPdv() > target.getPdvMax()) {
					val = target.getPdvMax() - target.getPdv();
				}
				target.removePdv(this.caster, -val);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(target.getId()) + ",+" + val);
			}
			if (this.caster.getPdv() <= 0) {
				fight.onFighterDie(this.caster, this.caster);
			}
		} else {
			for (final Fighter target2 : cibles) {
				target2.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_108(final ArrayList<Fighter> cibles, final Fight fight, final boolean isCaC) {
		if (this.spell == 441) {
			return;
		}
		if (isCaC) {
			if (this.caster.isHide()) {
				this.caster.unHide(-1);
			}
			final String[] jet = this.args.split(";");
			int heal = 0;
			if (jet.length < 6) {
				heal = 1;
			} else {
				heal = Formulas.getRandomJet(jet[5]);
			}
			final int heal2 = heal;
			for (final Fighter cible : cibles) {
				if (cible.isDead()) {
					continue;
				}
				heal = this.getMaxMinSpell(cible, heal);
				final int pdvMax = cible.getPdvMax();
				int healFinal = Formulas.calculFinalHealCac(this.caster, heal, isCaC);
				if (healFinal + cible.getPdv() > pdvMax) {
					healFinal = pdvMax - cible.getPdv();
				}
				if (healFinal < 1) {
					healFinal = 0;
				}
				cible.removePdv(this.caster, -healFinal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 108,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(cible.getId()) + "," + healFinal);
				heal = heal2;
			}
		} else if (this.turns <= 0) {
			final String[] jet = this.args.split(";");
			int heal = 0;
			if (jet.length < 6) {
				heal = 1;
			} else {
				heal = Formulas.getRandomJet(jet[5]);
			}
			final int heal2 = heal;
			for (final Fighter cible : cibles) {
				if (cible.isDead()) {
					continue;
				}
				heal = this.getMaxMinSpell(cible, heal);
				final int pdvMax = cible.getPdvMax();
				int healFinal = Formulas.calculFinalHealCac(this.caster, heal, isCaC);
				if (healFinal + cible.getPdv() > pdvMax) {
					healFinal = pdvMax - cible.getPdv();
				}
				if (healFinal < 1) {
					healFinal = 0;
				}
				cible.removePdv(this.caster, -healFinal);
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 108,
						new StringBuilder(String.valueOf(this.caster.getId())).toString(),
						String.valueOf(cible.getId()) + "," + healFinal);
				heal = heal2;
			}
		} else {
			for (final Fighter cible2 : cibles) {
				if (cible2.isDead()) {
					continue;
				}
				cible2.addBuff(this.effectID, 0, this.turns, 0, true, this.spell, this.args, this.caster, false);
			}
		}
	}

	private void applyEffect_141(final Fight fight, final ArrayList<Fighter> cibles) {
		for (Fighter target : cibles) {
			if (target.hasBuff(765) && target.getBuff(765) != null && !target.getBuff(765).getCaster().isDead()) {
				this.applyEffect_765B(fight, target);
				target = target.getBuff(765).getCaster();
			}
			fight.onFighterDie(target, target);
		}
	}

	private void applyEffect_320(final Fight fight, final ArrayList<Fighter> cibles) {
		int value = 1;
		try {
			value = Integer.parseInt(this.args.split(";")[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int num = 0;
		for (final Fighter target : cibles) {
			target.addBuff(116, value, this.turns, 0, true, this.spell, this.args, this.caster, false);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 116,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + value + "," + this.turns);
			num += value;
		}
		if (num != 0) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 117,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(),
					String.valueOf(this.caster.getId()) + "," + num + "," + this.turns);
			this.caster.addBuff(117, num, 1, 0, true, this.spell, this.args, this.caster, false);
			if (this.caster.canPlay()) {
				this.caster.getTotalStats().addOneStat(117, num);
			}
		}
	}

	private void applyEffect_780(final Fight fight) {
		final Map<Integer, Fighter> deads = fight.getDeadList();
		Fighter target = null;
		for (final Map.Entry<Integer, Fighter> entry : deads.entrySet()) {
			if (entry.getValue().hasLeft()) {
				continue;
			}
			if (entry.getValue().getTeam() == this.caster.getTeam()) {
				target = entry.getValue();
			}
			if (!entry.getValue().isInvocation() || entry.getValue().getInvocator().isDead()) {
			}
		}
		if (target == null) {
			return;
		}
		fight.addFighterInTeam(target, target.getTeam());
		target.setIsDead(false);
		target.getFightBuff().clear();
		if (target.isInvocation()) {
			fight.getOrderPlaying().add(fight.getOrderPlaying().indexOf(target.getInvocator()) + 1, target);
		}
		target.setCell(this.cell);
		target.getCell().addFighter(target);
		target.fullPdv();
		final int percent = (100 - this.value) * target.getPdvMax() / 100;
		target.removePdv(this.caster, percent);
		final String gm = target.getGmPacket('+').substring(3);
		final String gtl = fight.getGTL();
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 181,
				new StringBuilder(String.valueOf(target.getId())).toString(), gm);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 999,
				new StringBuilder(String.valueOf(target.getId())).toString(), gtl);
		if (!target.isInvocation()) {
			SocketManager.GAME_SEND_STATS_PACKET(target.getPersonnage());
		}
		fight.removeDead(target);
	}

	public void setArgs(final String newArgs) {
		this.args = newArgs;
	}

	public void setEffectID(final int id) {
		this.effectID = id;
	}
}
