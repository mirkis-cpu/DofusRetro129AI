// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.spells;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.fight.Challenge;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.map.Case;

public class Spell {
	private String nombre;
	private int spellID;
	private int spriteID;
	private String spriteInfos;
	private Map<Integer, SortStats> sortStats;
	private ArrayList<Integer> effectTargets;
	private ArrayList<Integer> CCeffectTargets;
	private int type;

	public Spell(final int aspellID, final String aNombre, final int aspriteID, final String aspriteInfos,
			final String ET, final int type) {
		this.sortStats = new TreeMap<Integer, SortStats>();
		this.effectTargets = new ArrayList<Integer>();
		this.CCeffectTargets = new ArrayList<Integer>();
		this.spellID = aspellID;
		this.nombre = aNombre;
		this.spriteID = aspriteID;
		this.spriteInfos = aspriteInfos;
		if (ET.equalsIgnoreCase("0")) {
			this.effectTargets.add(0);
			this.CCeffectTargets.add(0);
		} else {
			final String nET = ET.split(":")[0];
			String ccET = "";
			if (ET.split(":").length > 1) {
				ccET = ET.split(":")[1];
			}
			String[] split;
			for (int length = (split = nET.split(";")).length, i = 0; i < length; ++i) {
				final String num = split[i];
				try {
					this.effectTargets.add(Integer.parseInt(num));
				} catch (Exception e) {
					this.effectTargets.add(0);
				}
			}
			String[] split2;
			for (int length2 = (split2 = ccET.split(";")).length, j = 0; j < length2; ++j) {
				final String num = split2[j];
				try {
					this.CCeffectTargets.add(Integer.parseInt(num));
				} catch (Exception e) {
					this.CCeffectTargets.add(0);
				}
			}
		}
		this.type = type;
	}

	public void setInfos(final int aspriteID, final String aspriteInfos, final String ET, final int type) {
		this.spriteID = aspriteID;
		this.spriteInfos = aspriteInfos;
		final String nET = ET.split(":")[0];
		String ccET = "";
		this.type = type;
		if (ET.split(":").length > 1) {
			ccET = ET.split(":")[1];
		}
		String[] split;
		for (int length = (split = nET.split(";")).length, i = 0; i < length; ++i) {
			final String num = split[i];
			try {
				this.effectTargets.add(Integer.parseInt(num));
			} catch (Exception e) {
				this.effectTargets.add(0);
			}
		}
		String[] split2;
		for (int length2 = (split2 = ccET.split(";")).length, j = 0; j < length2; ++j) {
			final String num = split2[j];
			try {
				this.CCeffectTargets.add(Integer.parseInt(num));
			} catch (Exception e) {
				this.CCeffectTargets.add(0);
			}
		}
	}

	public ArrayList<Integer> getEffectTargets() {
		return this.effectTargets;
	}

	public int getSpriteID() {
		return this.spriteID;
	}

	public String getSpriteInfos() {
		return this.spriteInfos;
	}

	public int getSpellID() {
		return this.spellID;
	}

	public SortStats getStatsByLevel(final int lvl) {
		return this.sortStats.get(lvl);
	}

	public String getNombre() {
		return this.nombre;
	}

	public Map<Integer, SortStats> getSortsStats() {
		return this.sortStats;
	}

	public void addSortStats(final Integer lvl, final SortStats stats) {
		if (this.sortStats.get(lvl) != null) {
			this.sortStats.remove(lvl);
		}
		this.sortStats.put(lvl, stats);
	}

	public int getType() {
		return this.type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public static class SortStats {
		private int spellID;
		private int level;
		private int PACost;
		private int minPO;
		private int maxPO;
		private int TauxCC;
		private int TauxEC;
		private boolean isLineLaunch;
		private boolean hasLDV;
		private boolean isEmptyCell;
		private boolean isModifPO;
		private int maxLaunchbyTurn;
		private int maxLaunchbyByTarget;
		private int coolDown;
		private int reqLevel;
		private boolean isEcEndTurn;
		private ArrayList<SpellEffect> effects;
		private ArrayList<SpellEffect> CCeffects;
		private String porteeType;

		public SortStats(final int AspellID, final int Alevel, final int cost, final int minPO, final int maxPO,
				final int tauxCC, final int tauxEC, final boolean isLineLaunch, final boolean hasLDV,
				final boolean isEmptyCell, final boolean isModifPO, final int maxLaunchbyTurn,
				final int maxLaunchbyByTarget, final int coolDown, final int reqLevel, final boolean isEcEndTurn,
				final String effects, final String ceffects, final String typePortee) {
			this.spellID = AspellID;
			this.level = Alevel;
			this.PACost = cost;
			this.minPO = minPO;
			this.maxPO = maxPO;
			this.TauxCC = tauxCC;
			this.TauxEC = tauxEC;
			this.isLineLaunch = isLineLaunch;
			this.hasLDV = hasLDV;
			this.isEmptyCell = isEmptyCell;
			this.isModifPO = isModifPO;
			this.maxLaunchbyTurn = maxLaunchbyTurn;
			this.maxLaunchbyByTarget = maxLaunchbyByTarget;
			this.coolDown = coolDown;
			this.reqLevel = reqLevel;
			this.isEcEndTurn = isEcEndTurn;
			this.effects = this.parseEffect(effects);
			this.CCeffects = this.parseEffect(ceffects);
			this.porteeType = typePortee;
		}

		private ArrayList<SpellEffect> parseEffect(final String e) {
			final ArrayList<SpellEffect> effets = new ArrayList<SpellEffect>();
			final String[] splt = e.split("\\|");
			String[] array;
			for (int length = (array = splt).length, i = 0; i < length; ++i) {
				final String a = array[i];
				try {
					if (!e.equals("-1")) {
						final int id = Integer.parseInt(a.split(";", 2)[0]);
						final String args = a.split(";", 2)[1];
						effets.add(new SpellEffect(id, args, this.spellID, this.level));
					}
				} catch (Exception f) {
					f.printStackTrace();
					Console.println("Erreur du sort : " + a + ". Exception : " + f.getMessage(), Console.Color.ERROR);
					System.exit(1);
				}
			}
			return effets;
		}

		public int getSpellID() {
			return this.spellID;
		}

		public Spell getSpell() {
			return World.getSort(this.spellID);
		}

		public int getSpriteID() {
			return this.getSpell().getSpriteID();
		}

		public String getSpriteInfos() {
			return this.getSpell().getSpriteInfos();
		}

		public int getLevel() {
			return this.level;
		}

		public int getPACost() {
			return this.PACost;
		}

		public int getMinPO() {
			return this.minPO;
		}

		public int getMaxPO() {
			return this.maxPO;
		}

		public int getTauxCC() {
			return this.TauxCC;
		}

		public int getTauxEC() {
			return this.TauxEC;
		}

		public boolean isLineLaunch() {
			return this.isLineLaunch;
		}

		public boolean hasLDV() {
			return this.hasLDV;
		}

		public boolean isEmptyCell() {
			return this.isEmptyCell;
		}

		public boolean isModifPO() {
			return this.isModifPO;
		}

		public int getMaxLaunchbyTurn() {
			return this.maxLaunchbyTurn;
		}

		public int getMaxLaunchByTarget() {
			return this.maxLaunchbyByTarget;
		}

		public int getCoolDown() {
			return this.coolDown;
		}

		public int getReqLevel() {
			return this.reqLevel;
		}

		public boolean isEcEndTurn() {
			return this.isEcEndTurn;
		}

		public ArrayList<SpellEffect> getEffects() {
			return this.effects;
		}

		public ArrayList<SpellEffect> getCCeffects() {
			return this.CCeffects;
		}

		public String getPorteeType() {
			return this.porteeType;
		}

		public void applySpellEffectToFight(final Fight fight, final Fighter perso, final Case cell,
				final ArrayList<Case> cells, final boolean isCC) {
			ArrayList<SpellEffect> effets;
			if (isCC) {
				effets = this.CCeffects;
			} else {
				effets = this.effects;
			}
			GameServer.addToLog("Nombre d'effets: " + effets.size());
			final int jetChance = Formulas.getRandomValue(0, 99);
			int curMin = 0;
			for (final SpellEffect SE : effets) {
				if (SE.getChance() != 0 && SE.getChance() != 100) {
					if (jetChance <= curMin || jetChance >= SE.getChance() + curMin) {
						curMin += SE.getChance();
						continue;
					}
					curMin += SE.getChance();
				}
				final ArrayList<Fighter> cibles = SpellEffect.getTargets(SE, fight, cells);
				if (fight.getType() != 0 && fight.getAllChallenges().size() > 0) {
					for (final Map.Entry<Integer, Challenge> c : fight.getAllChallenges().entrySet()) {
						if (c.getValue() == null) {
							continue;
						}
						c.getValue().onFightersAttacked(cibles, perso, SE, this.getSpellID(), true);
					}
				}
				SE.applyToFight(fight, perso, cell, cibles);
			}
		}

		public void applySpellEffectToFight(final Fight fight, final Fighter perso, final Case cell, final boolean isCC,
				final boolean isTrap) {
			ArrayList<SpellEffect> effets;
			if (isCC) {
				effets = this.CCeffects;
			} else {
				effets = this.effects;
			}
			GameServer.addToLog("Nombre d'effets: " + effets.size());
			int jetChance = 0;
			if (this.getSpell().getSpellID() == 101) {
				jetChance = Formulas.getRandomValue(0, 75);
				if (jetChance % 2 == 0) {
					++jetChance;
				}
			} else if (this.getSpell().getSpellID() == 574) {
				jetChance = Formulas.getRandomValue(0, 96);
			} else if (this.getSpell().getSpellID() == 574) {
				jetChance = Formulas.getRandomValue(0, 95);
			} else {
				jetChance = Formulas.getRandomValue(0, 99);
			}
			int curMin = 0;
			int num = 0;
			for (final SpellEffect SE : effets) {
				if (fight.getState() >= 4) {
					return;
				}
				if (SE.getChance() != 0 && SE.getChance() != 100) {
					if (jetChance <= curMin || jetChance >= SE.getChance() + curMin) {
						curMin += SE.getChance();
						++num;
						continue;
					}
					curMin += SE.getChance();
				}
				int POnum = num * 2;
				if (isCC) {
					POnum += this.effects.size() * 2;
				}
				final ArrayList<Case> cells = Pathfinding.getCellListFromAreaString(fight.getMap(), cell.getId(),
						perso.getCell().getId(), this.porteeType, POnum, isCC);
				final ArrayList<Case> finalCells = new ArrayList<Case>();
				int TE = 0;
				final Spell S = World.getSort(this.spellID);
				if (S != null && S.getEffectTargets().size() > num) {
					TE = S.getEffectTargets().get(num);
				}
				for (final Case C : cells) {
					if (C == null) {
						continue;
					}
					final Fighter F = C.getFirstFighter();
					if (F == null) {
						continue;
					}
					if ((TE & 0x1) == 0x1 && F.getTeam() == perso.getTeam()) {
						continue;
					}
					if ((TE >> 1 & 0x1) == 0x1 && F.getId() == perso.getId()) {
						continue;
					}
					if ((TE >> 2 & 0x1) == 0x1 && F.getTeam() != perso.getTeam()) {
						continue;
					}
					if ((TE >> 3 & 0x1) == 0x1 && !F.isInvocation()) {
						continue;
					}
					if ((TE >> 4 & 0x1) == 0x1 && F.isInvocation()) {
						continue;
					}
					if ((TE >> 5 & 0x1) == 0x1 && F.getId() != perso.getId()) {
						continue;
					}
					if ((TE >> 10 & 0x1) == 0x1) {
						continue;
					}
					finalCells.add(C);
				}
				if ((TE >> 5 & 0x1) == 0x1 && !finalCells.contains(perso.getCell())) {
					finalCells.add(perso.getCell());
				}
				final ArrayList<Fighter> cibles = SpellEffect.getTargets(SE, fight, finalCells);
				if (fight.getType() != 0 && fight.getAllChallenges().size() > 0) {
					for (final Map.Entry<Integer, Challenge> c : fight.getAllChallenges().entrySet()) {
						if (c.getValue() == null) {
							continue;
						}
						c.getValue().onFightersAttacked(cibles, perso, SE, this.getSpellID(), isTrap);
					}
				}
				SE.applyToFight(fight, perso, cell, cibles);
				++num;
			}
		}
	}
}
