// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.other.Stats;
import org.aestia.common.Formulas;
import org.aestia.entity.monster.boss.MaitreCorbac;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.map.Case;

public class Monster {
	private int id;
	private int gfxId;
	private int align;
	private String colors;
	private int ia;
	private int minKamas;
	private int maxKamas;
	private Map<Integer, MobGrade> grades;
	private ArrayList<World.Drop> drops;
	private boolean isCapturable;
	private int aggroDistance;

	public Monster(final int id, final int gfxId, final int align, final String colors, final String thisGrades,
			final String thisSpells, final String thisStats, final String thisStatsInfos, final String thisPdvs,
			final String thisPoints, final String thisInit, final int minKamas, final int maxKamas, final String thisXp,
			final int ia, final boolean capturable, final int aggroDistance) {
		this.ia = 0;
		this.grades = new TreeMap<Integer, MobGrade>();
		this.drops = new ArrayList<World.Drop>();
		this.aggroDistance = 0;
		this.id = id;
		this.gfxId = gfxId;
		this.align = align;
		this.colors = colors;
		this.minKamas = minKamas;
		this.maxKamas = maxKamas;
		this.ia = ia;
		this.isCapturable = capturable;
		this.aggroDistance = aggroDistance;
		int G = 1;
		for (int n = 0; n < 12; ++n) {
			try {
				final String grade = thisGrades.split("\\|")[n];
				final String[] infos = grade.split("@");
				final int level = Integer.parseInt(infos[0]);
				final String resists = infos[1];
				final String stats = thisStats.split("\\|")[n];
				String spells = "";
				if (!thisSpells.equalsIgnoreCase("||||") && !thisSpells.equalsIgnoreCase("")
						&& !thisSpells.equalsIgnoreCase("-1")) {
					spells = thisSpells.split("\\|")[n];
					if (spells.equals("-1")) {
						spells = "";
					}
				}
				int pdvmax = 1;
				int init = 1;
				try {
					pdvmax = Integer.parseInt(thisPdvs.split("\\|")[n]);
					init = Integer.parseInt(thisInit.split("\\|")[n]);
				} catch (Exception e) {
					e.printStackTrace();
					Console.println("#1# Erreur lors du chargement du monstre (template) : " + id, Console.Color.ERROR);
				}
				int PA = 3;
				int PM = 3;
				int xp = 10;
				try {
					final String[] pts = thisPoints.split("\\|")[n].split(";");
					try {
						PA = Integer.parseInt(pts[0]);
						PM = Integer.parseInt(pts[1]);
						xp = Integer.parseInt(thisXp.split("\\|")[n]);
					} catch (Exception e2) {
						Console.println("#2# Erreur lors du chargement du monstre (template) : " + id,
								Console.Color.ERROR);
						e2.printStackTrace();
					}
				} catch (Exception e3) {
					Console.println("#3# Erreur lors du chargement du monstre (template) : " + id, Console.Color.ERROR);
					e3.printStackTrace();
				}
				this.grades.put(G, new MobGrade(this, G, level, PA, PM, resists, stats, thisStatsInfos, spells, pdvmax,
						init, xp, n));
				++G;
			} catch (Exception ex) {
			}
		}
	}

	public void setInfos(final int gfxId, final int align, final String colors, final String thisGrades,
			final String thisSpells, final String thisStats, final String thisStatsInfos, final String thisPdvs,
			final String thisPoints, final String thisInit, final int minKamas, final int maxKamas, final String thisXp,
			final int ia, final boolean capturable, final int aggroDistance) {
		this.gfxId = gfxId;
		this.align = align;
		this.colors = colors;
		this.minKamas = minKamas;
		this.maxKamas = maxKamas;
		this.ia = ia;
		this.isCapturable = capturable;
		this.aggroDistance = aggroDistance;
		int G = 1;
		this.grades.clear();
		for (int n = 0; n < 12; ++n) {
			try {
				final String grade = thisGrades.split("\\|")[n];
				final String[] infos = grade.split("@");
				final int level = Integer.parseInt(infos[0]);
				final String resists = infos[1];
				final String stats = thisStats.split("\\|")[n];
				String spells = thisSpells.split("\\|")[n];
				if (spells.equals("-1")) {
					spells = "";
				}
				int pdvmax = 1;
				int init = 1;
				try {
					pdvmax = Integer.parseInt(thisPdvs.split("\\|")[n]);
					init = Integer.parseInt(thisInit.split("\\|")[n]);
				} catch (Exception e) {
					e.printStackTrace();
					Console.println("#4# Erreur lors du chargement du monstre (template) : " + this.id,
							Console.Color.ERROR);
				}
				int PA = 3;
				int PM = 3;
				int xp = 10;
				try {
					final String[] pts = thisPoints.split("\\|")[n].split(";");
					try {
						PA = Integer.parseInt(pts[0]);
						PM = Integer.parseInt(pts[1]);
						xp = Integer.parseInt(thisXp.split("\\|")[n]);
					} catch (Exception e2) {
						Console.println("#5# Erreur lors du chargement du monstre (template) : " + this.id,
								Console.Color.ERROR);
						e2.printStackTrace();
					}
				} catch (Exception e3) {
					Console.println("#6# Erreur lors du chargement du monstre (template) : " + this.id,
							Console.Color.ERROR);
					e3.printStackTrace();
				}
				this.grades.put(G, new MobGrade(this, G, level, PA, PM, resists, stats, thisStatsInfos, spells, pdvmax,
						init, xp, n));
				++G;
			} catch (Exception ex) {
			}
		}
	}

	public int getId() {
		return this.id;
	}

	public int getGfxId() {
		return this.gfxId;
	}

	public int getAlign() {
		return this.align;
	}

	public String getColors() {
		return this.colors;
	}

	public int getIa() {
		return this.ia;
	}

	public int getMinKamas() {
		return this.minKamas;
	}

	public int getMaxKamas() {
		return this.maxKamas;
	}

	public Map<Integer, MobGrade> getGrades() {
		return this.grades;
	}

	public void addDrop(final World.Drop D) {
		this.drops.add(D);
	}

	public ArrayList<World.Drop> getDrops() {
		return this.drops;
	}

	public boolean isCapturable() {
		return this.isCapturable;
	}

	public int getAggroDistance() {
		return this.aggroDistance;
	}

	public MobGrade getGradeByLevel(final int lvl) {
		for (final Map.Entry<Integer, MobGrade> grade : this.getGrades().entrySet()) {
			if (grade.getValue().getLevel() == lvl) {
				return grade.getValue();
			}
		}
		return null;
	}

	public MobGrade getRandomGrade() {
		final int randomgrade = (int) (Math.random() * 5.0) + 1;
		int graderandom = 1;
		for (final Map.Entry<Integer, MobGrade> grade : this.getGrades().entrySet()) {
			if (graderandom == randomgrade) {
				return grade.getValue();
			}
			++graderandom;
		}
		return null;
	}

	public static class MobGroup {
		public static final MaitreCorbac MAITRE_CORBAC;
		private int id;
		private int cellId;
		private int orientation;
		private int align;
		private int starBonus;
		private int aggroDistance;
		private int subarea;
		private boolean changeAgro;
		private boolean isFix;
		private boolean isExtraGroup;
		private Map<Integer, MobGrade> mobs;
		private String condition;

		static {
			MAITRE_CORBAC = new MaitreCorbac();
		}

		public MobGroup(final int Aid, final int Aalign, final ArrayList<MobGrade> possibles,
				final org.aestia.map.Map Map, final int cell, final int fixSize, final int minSize, final int maxSize,
				final MobGrade extra) {
			this.orientation = 2;
			this.align = -1;
			this.aggroDistance = 0;
			this.subarea = -1;
			this.changeAgro = false;
			this.isFix = false;
			this.isExtraGroup = false;
			this.mobs = new TreeMap<Integer, MobGrade>();
			this.condition = "";
			this.id = Aid;
			this.align = Aalign;
			int rand = 0;
			int nbr = 0;
			Label_1600: {
				if (fixSize > 0 && fixSize < 9) {
					nbr = fixSize;
				} else if (minSize != -1 && maxSize != -1 && maxSize != 0 && minSize < maxSize) {
					if (minSize == 3 && maxSize == 8) {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 25) {
							nbr = 3;
						} else if (rand < 48) {
							nbr = 4;
						} else if (rand < 51) {
							nbr = 5;
						} else if (rand < 85) {
							nbr = 6;
						} else if (rand < 95) {
							nbr = 7;
						} else {
							nbr = 8;
						}
					} else if (minSize == 1 && maxSize == 3) {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 40) {
							nbr = 1;
						} else if (rand < 75) {
							nbr = 2;
						} else {
							nbr = 3;
						}
					} else if (minSize == 1 && maxSize == 5) {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 30) {
							nbr = 1;
						} else if (rand < 53) {
							nbr = 2;
						} else if (rand < 73) {
							nbr = 3;
						} else if (rand < 90) {
							nbr = 4;
						} else {
							nbr = 5;
						}
					} else if (minSize == 1 && maxSize == 4) {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 35) {
							nbr = 1;
						} else if (rand < 61) {
							nbr = 2;
						} else if (rand < 82) {
							nbr = 3;
						} else {
							nbr = 4;
						}
					} else {
						nbr = Formulas.getRandomValue(minSize, maxSize);
					}
				} else if (minSize == -1) {
					switch (maxSize) {
					case 0: {
						return;
					}
					case 1: {
						nbr = 1;
						break;
					}
					case 2: {
						nbr = Formulas.getRandomValue(1, 2);
						break;
					}
					case 3: {
						nbr = Formulas.getRandomValue(1, 3);
						break;
					}
					case 4: {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 22) {
							nbr = 1;
							break;
						}
						if (rand < 48) {
							nbr = 2;
							break;
						}
						if (rand < 74) {
							nbr = 3;
							break;
						}
						nbr = 4;
						break;
					}
					case 5: {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 15) {
							nbr = 1;
							break;
						}
						if (rand < 35) {
							nbr = 2;
							break;
						}
						if (rand < 60) {
							nbr = 3;
							break;
						}
						if (rand < 85) {
							nbr = 4;
							break;
						}
						nbr = 5;
						break;
					}
					case 6: {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 10) {
							nbr = 1;
							break;
						}
						if (rand < 25) {
							nbr = 2;
							break;
						}
						if (rand < 45) {
							nbr = 3;
							break;
						}
						if (rand < 65) {
							nbr = 4;
							break;
						}
						if (rand < 85) {
							nbr = 5;
							break;
						}
						nbr = 6;
						break;
					}
					case 7: {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 9) {
							nbr = 1;
							break;
						}
						if (rand < 20) {
							nbr = 2;
							break;
						}
						if (rand < 35) {
							nbr = 3;
							break;
						}
						if (rand < 55) {
							nbr = 4;
							break;
						}
						if (rand < 75) {
							nbr = 5;
							break;
						}
						if (rand < 91) {
							nbr = 6;
							break;
						}
						nbr = 7;
						break;
					}
					default: {
						rand = Formulas.getRandomValue(0, 99);
						if (rand < 9) {
							nbr = 1;
							break;
						}
						if (rand < 20) {
							nbr = 2;
							break;
						}
						if (rand < 33) {
							nbr = 3;
							break;
						}
						if (rand < 50) {
							nbr = 4;
							break;
						}
						if (rand < 67) {
							nbr = 5;
							break;
						}
						if (rand < 80) {
							nbr = 6;
							break;
						}
						if (rand < 91) {
							nbr = 7;
							break;
						}
						nbr = 8;
						break;
					}
					}
				} else {
					switch (minSize) {
					case 1: {
						rand = Formulas.getRandomValue(1, 8);
						switch (rand) {
						case 1: {
							nbr = 1;
							break;
						}
						case 2: {
							nbr = 2;
							break;
						}
						case 3: {
							nbr = 3;
							break;
						}
						case 4: {
							nbr = 4;
							break;
						}
						case 5: {
							nbr = 5;
							break;
						}
						case 6: {
							nbr = 6;
							break;
						}
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 2: {
						rand = Formulas.getRandomValue(2, 8);
						switch (rand) {
						case 2: {
							nbr = 2;
							break;
						}
						case 3: {
							nbr = 3;
							break;
						}
						case 4: {
							nbr = 4;
							break;
						}
						case 5: {
							nbr = 5;
							break;
						}
						case 6: {
							nbr = 6;
							break;
						}
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 3: {
						rand = Formulas.getRandomValue(3, 8);
						switch (rand) {
						case 3: {
							nbr = 3;
							break;
						}
						case 4: {
							nbr = 4;
							break;
						}
						case 5: {
							nbr = 5;
							break;
						}
						case 6: {
							nbr = 6;
							break;
						}
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 4: {
						rand = Formulas.getRandomValue(4, 8);
						switch (rand) {
						case 4: {
							nbr = 4;
							break;
						}
						case 5: {
							nbr = 5;
							break;
						}
						case 6: {
							nbr = 6;
							break;
						}
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 5: {
						rand = Formulas.getRandomValue(5, 8);
						switch (rand) {
						case 5: {
							nbr = 5;
							break;
						}
						case 6: {
							nbr = 6;
							break;
						}
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 6: {
						rand = Formulas.getRandomValue(6, 8);
						switch (rand) {
						case 6: {
							nbr = 6;
							break;
						}
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 7: {
						rand = Formulas.getRandomValue(7, 8);
						switch (rand) {
						case 7: {
							nbr = 7;
							break;
						}
						case 8: {
							nbr = 8;
							break;
						}
						}
						break;
					}
					case 8: {
						nbr = 8;
						break;
					}
					default: {
						rand = Formulas.getRandomValue(1, 8);
						switch (rand) {
						case 1: {
							nbr = 1;
							break Label_1600;
						}
						case 2: {
							nbr = 2;
							break Label_1600;
						}
						case 3: {
							nbr = 3;
							break Label_1600;
						}
						case 4: {
							nbr = 4;
							break Label_1600;
						}
						case 5: {
							nbr = 5;
							break Label_1600;
						}
						case 6: {
							nbr = 6;
							break Label_1600;
						}
						case 7: {
							nbr = 7;
							break Label_1600;
						}
						case 8: {
							nbr = 8;
							break Label_1600;
						}
						}
						break;
					}
					}
				}
			}
			int guid = -1;
			boolean haveSameAlign = false;
			if (extra != null) {
				this.isExtraGroup = true;
				--nbr;
				this.mobs.put(guid, extra);
				--guid;
			}
			for (final MobGrade mob : possibles) {
				if (mob.getTemplate().getAlign() == this.align) {
					haveSameAlign = true;
				}
			}
			if (!haveSameAlign) {
				return;
			}
			for (int a = 0; a < nbr; ++a) {
				MobGrade Mob = null;
				do {
					final int random = Formulas.getRandomValue(0, possibles.size() - 1);
					Mob = possibles.get(random).getCopy();
				} while (Mob.getTemplate().getAlign() != this.align);
				this.mobs.put(guid, Mob);
				if (Mob.getTemplate().getAggroDistance() > this.aggroDistance) {
					this.aggroDistance = Mob.getTemplate().getAggroDistance();
				}
				--guid;
			}
			this.cellId = ((cell == -1) ? Map.getRandomFreeCellId(false) : cell);
			while (Map.containsForbiddenCellSpawn(this.cellId)) {
				this.cellId = Map.getRandomFreeCellId(false);
			}
			if (this.cellId == 0) {
				return;
			}
			this.orientation = Formulas.getRandomValue(0, 3) * 2 + 1;
			this.isFix = false;
			this.starBonus = Constant.getStarAlea();
		}

		public MobGroup(final int id, final int cellId, final String groupData) {
			this.orientation = 2;
			this.align = -1;
			this.aggroDistance = 0;
			this.subarea = -1;
			this.changeAgro = false;
			this.isFix = false;
			this.isExtraGroup = false;
			this.mobs = new TreeMap<Integer, MobGrade>();
			this.condition = "";
			this.id = id;
			this.align = -1;
			this.cellId = cellId;
			this.isFix = true;
			int guid = -1;
			boolean star = false;
			String[] split;
			for (int length = (split = groupData.split(";")).length, i = 0; i < length; ++i) {
				final String data = split[i];
				if (!data.equalsIgnoreCase("")) {
					final String[] infos = data.split(",");
					try {
						final int idMonster = Integer.parseInt(infos[0]);
						final int min = Integer.parseInt(infos[1]);
						final int max = Integer.parseInt(infos[2]);
						final Monster m = World.getMonstre(idMonster);
						final List<MobGrade> mgs = new ArrayList<MobGrade>();
						for (final MobGrade MG : m.getGrades().values()) {
							if (MG.getBaseXp() != 0) {
								star = true;
							}
							if (MG.level >= min && MG.level <= max) {
								mgs.add(MG);
							}
						}
						if (!mgs.isEmpty()) {
							this.mobs.put(guid, mgs.get(Formulas.getRandomValue(0, mgs.size() - 1)));
							if (m.getAggroDistance() > this.aggroDistance) {
								this.aggroDistance = m.getAggroDistance();
							}
							--guid;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			this.orientation = Formulas.getRandomValue(0, 3) * 2 + 1;
			this.starBonus = (star ? Constant.getStarAlea() : 0);
		}

		public void setSubArea(final int sa) {
			this.subarea = sa;
		}

		public void changeAgro() {
			if (!this.changeAgro && this.haveMineur() && this.subarea != 29 && this.subarea != 96
					&& this.subarea != 31) {
				this.removeAgro(118);
			}
			this.changeAgro = true;
		}

		public void removeAgro(final int id) {
			this.aggroDistance = 0;
			for (final Map.Entry<Integer, MobGrade> e : this.mobs.entrySet()) {
				final MobGrade mb = e.getValue();
				if (mb.template.getId() != id && mb.template.getAggroDistance() > this.aggroDistance) {
					this.aggroDistance = mb.template.getAggroDistance();
				}
			}
		}

		public boolean haveMineur() {
			for (final Map.Entry<Integer, MobGrade> e : this.mobs.entrySet()) {
				final MobGrade mb = e.getValue();
				if (mb.template.getId() == 118) {
					return true;
				}
			}
			return false;
		}

		public int getId() {
			return this.id;
		}

		public int getCellId() {
			return this.cellId;
		}

		public void setCellId(final int cellId) {
			this.cellId = cellId;
		}

		public int getOrientation() {
			return this.orientation;
		}

		public void setOrientation(final int orientation) {
			this.orientation = orientation;
		}

		public int getAlignement() {
			return this.align;
		}

		public int getStarBonus() {
			return this.starBonus;
		}

		public int getAggroDistance() {
			return this.aggroDistance;
		}

		public boolean isFix() {
			return this.isFix;
		}

		public void setIsFix(final boolean isFix) {
			this.isFix = isFix;
		}

		public boolean getIsExtraGroup() {
			return this.isExtraGroup;
		}

		public Map<Integer, MobGrade> getMobs() {
			return this.mobs;
		}

		public MobGrade getMobGradeById(final int id) {
			return this.mobs.get(id);
		}

		public String getCondition() {
			return this.condition;
		}

		public void setCondition(final String condition) {
			this.condition = condition;
		}

		public String parseGM() {
			final StringBuilder mobIDs = new StringBuilder();
			final StringBuilder mobGFX = new StringBuilder();
			final StringBuilder mobLevels = new StringBuilder();
			final StringBuilder colors = new StringBuilder();
			final StringBuilder toreturn = new StringBuilder();
			boolean isFirst = true;
			if (this.mobs.isEmpty()) {
				return "";
			}
			for (final Map.Entry<Integer, MobGrade> entry : this.mobs.entrySet()) {
				if (!isFirst) {
					mobIDs.append(",");
					mobGFX.append(",");
					mobLevels.append(",");
				}
				mobIDs.append(entry.getValue().getTemplate().getId());
				mobGFX.append(entry.getValue().getTemplate().getGfxId()).append("^" + entry.getValue().getSize());
				mobLevels.append(entry.getValue().getLevel());
				colors.append(entry.getValue().getTemplate().getColors()).append(";0,0,0,0;");
				isFirst = false;
			}
			toreturn.append("+").append(this.cellId).append(";").append(this.orientation).append(";");
			toreturn.append(this.getStarBonus());
			toreturn.append(";").append(this.id).append(";").append(mobIDs).append(";-3;").append(mobGFX).append(";")
					.append(mobLevels).append(";").append(colors);
			return toreturn.toString();
		}

		static /* synthetic */ void access$0(final MobGroup mobGroup, final String condition) {
			mobGroup.condition = condition;
		}
	}

	public static class MobGrade {
		private Monster template;
		private int grade;
		private int level;
		private int pdv;
		private int pdvMax;
		private int inFightId;
		private int init;
		private int pa;
		private int pm;
		private int size;
		private int baseXp;
		private static int pSize;
		private Case fightCell;
		private ArrayList<SpellEffect> fightBuffs;
		private Map<Integer, Integer> stats;
		private Map<Integer, Spell.SortStats> spells;
		private ArrayList<Integer> statsInfos;

		static {
			MobGrade.pSize = 2;
		}

		public MobGrade(final Monster template, final int grade, final int level, final int pa, final int pm,
				final String resists, final String stats, final String statsInfos, final String spells,
				final int pdvMax, final int aInit, final int xp, final int n) {
			this.baseXp = 10;
			this.fightBuffs = new ArrayList<SpellEffect>();
			this.stats = new TreeMap<Integer, Integer>();
			this.spells = new TreeMap<Integer, Spell.SortStats>();
			this.statsInfos = new ArrayList<Integer>();
			this.size = 100 + n * MobGrade.pSize;
			this.template = template;
			this.grade = grade;
			this.level = level;
			this.pdvMax = pdvMax;
			this.pdv = pdvMax;
			this.pa = pa;
			this.pm = pm;
			this.baseXp = xp;
			this.init = aInit;
			final String[] resist = resists.split(";");
			final String[] stat = stats.split(",");
			final String[] statInfos = statsInfos.split(";");
			String[] array;
			for (int length = (array = statInfos).length, i = 0; i < length; ++i) {
				final String str = array[i];
				this.statsInfos.add(Integer.parseInt(str));
			}
			int RN = 0;
			int RF = 0;
			int RE = 0;
			int RA = 0;
			int RT = 0;
			int AF = 0;
			int MF = 0;
			int force = 0;
			int intell = 0;
			int sagesse = 0;
			int chance = 0;
			int agilite = 0;
			int dommage = 0;
			int perDommage = 0;
			int soins = 0;
			int creaInv = 0;
			try {
				RN = Integer.parseInt(resist[0]);
				RT = Integer.parseInt(resist[1]);
				RF = Integer.parseInt(resist[2]);
				RE = Integer.parseInt(resist[3]);
				RA = Integer.parseInt(resist[4]);
				AF = Integer.parseInt(resist[5]);
				MF = Integer.parseInt(resist[6]);
				force = Integer.parseInt(stat[0]);
				sagesse = Integer.parseInt(stat[1]);
				intell = Integer.parseInt(stat[2]);
				chance = Integer.parseInt(stat[3]);
				agilite = Integer.parseInt(stat[4]);
				dommage = Integer.parseInt(statInfos[0]);
				perDommage = Integer.parseInt(statInfos[1]);
				soins = Integer.parseInt(statInfos[2]);
				creaInv = Integer.parseInt(statInfos[3]);
			} catch (Exception e) {
				Console.println("#1# Erreur lors du chargement du grade du monstre (template) : " + template.getId(),
						Console.Color.ERROR);
				e.printStackTrace();
			}
			this.stats.clear();
			this.stats.put(118, force);
			this.stats.put(124, sagesse);
			this.stats.put(126, intell);
			this.stats.put(123, chance);
			this.stats.put(119, agilite);
			this.stats.put(214, RN);
			this.stats.put(213, RF);
			this.stats.put(211, RE);
			this.stats.put(212, RA);
			this.stats.put(210, RT);
			this.stats.put(160, AF);
			this.stats.put(161, MF);
			this.stats.put(112, dommage);
			this.stats.put(138, perDommage);
			this.stats.put(178, soins);
			this.stats.put(182, creaInv);
			this.spells.clear();
			if (!spells.equalsIgnoreCase("")) {
				final String[] spell = spells.split(";");
				String[] array2;
				for (int length2 = (array2 = spell).length, j = 0; j < length2; ++j) {
					final String str2 = array2[j];
					if (!str2.equals("")) {
						final String[] spellInfo = str2.split("@");
						int spellID = 0;
						int spellLvl = 0;
						try {
							spellID = Integer.parseInt(spellInfo[0]);
							spellLvl = Integer.parseInt(spellInfo[1]);
						} catch (Exception e2) {
							e2.printStackTrace();
							continue;
						}
						if (spellID != 0) {
							if (spellLvl != 0) {
								final Spell sort = World.getSort(spellID);
								if (sort != null) {
									final Spell.SortStats SpellStats = sort.getStatsByLevel(spellLvl);
									if (SpellStats != null) {
										this.spells.put(spellID, SpellStats);
									}
								}
							}
						}
					}
				}
			}
		}

		private MobGrade(final Monster template, final int grade, final int level, final int pdv, final int pdvMax,
				final int pa, final int pm, final Map<Integer, Integer> stats, final ArrayList<Integer> statsInfos,
				final Map<Integer, Spell.SortStats> spells, final int xp, final int n) {
			this.baseXp = 10;
			this.fightBuffs = new ArrayList<SpellEffect>();
			this.stats = new TreeMap<Integer, Integer>();
			this.spells = new TreeMap<Integer, Spell.SortStats>();
			this.statsInfos = new ArrayList<Integer>();
			this.size = 100 + n * MobGrade.pSize;
			this.template = template;
			this.grade = grade;
			this.level = level;
			this.pdv = pdv;
			this.pdvMax = pdvMax;
			this.pa = pa;
			this.pm = pm;
			this.stats = stats;
			this.statsInfos = statsInfos;
			this.spells = spells;
			this.inFightId = -1;
			this.baseXp = xp;
		}

		public MobGrade getCopy() {
			final Map<Integer, Integer> newStats = new TreeMap<Integer, Integer>();
			newStats.putAll(this.stats);
			final int n = (this.size - 100) / MobGrade.pSize;
			return new MobGrade(this.template, this.grade, this.level, this.pdv, this.pdvMax, this.pa, this.pm,
					newStats, this.statsInfos, this.spells, this.baseXp, n);
		}

		public void refresh() {
			if (this.spells.isEmpty()) {
				return;
			}
			String spells = "";
			for (final Map.Entry<Integer, Spell.SortStats> entry : this.spells.entrySet()) {
				spells = String.valueOf(spells)
						+ (spells.isEmpty() ? (entry.getKey() + "," + entry.getValue().getLevel())
								: (";" + entry.getKey() + "," + entry.getValue().getLevel()));
			}
			this.spells.clear();
			if (!spells.equalsIgnoreCase("")) {
				String[] split2;
				for (int length = (split2 = spells.split("\\;")).length, i = 0; i < length; ++i) {
					final String split = split2[i];
					final int id = Integer.parseInt(split.split("\\,")[0]);
					this.spells.put(id, World.getSort(id).getStatsByLevel(Integer.parseInt(split.split("\\,")[1])));
				}
			}
		}

		public int getSize() {
			return this.size;
		}

		public Monster getTemplate() {
			return this.template;
		}

		public int getGrade() {
			return this.grade;
		}

		public int getLevel() {
			return this.level;
		}

		public int getPdv() {
			return this.pdv;
		}

		public void setPdv(final int pdv) {
			this.pdv = pdv;
		}

		public int getPdvMax() {
			return this.pdvMax;
		}

		public int getInFightID() {
			return this.inFightId;
		}

		public void setInFightID(final int i) {
			this.inFightId = i;
		}

		public int getInit() {
			return this.init;
		}

		public int getPa() {
			return this.pa;
		}

		public int getPm() {
			return this.pm;
		}

		public int getBaseXp() {
			return this.baseXp;
		}

		public Case getFightCell() {
			return this.fightCell;
		}

		public void setFightCell(final Case cell) {
			this.fightCell = cell;
		}

		public ArrayList<SpellEffect> getBuffs() {
			return this.fightBuffs;
		}

		public Stats getStats() {
			return new Stats(this.stats);
		}

		public Map<Integer, Spell.SortStats> getSpells() {
			return this.spells;
		}

		public void modifStatByInvocator(final Fighter caster) {
			final float taux = 1.0f + caster.getLvl() / 100.0f;
			final int life = Math.round(this.pdvMax * taux);
			this.pdv = life;
			this.pdvMax = this.pdv;
			final int force = this.stats.get(118) * (int) taux;
			final int intel = this.stats.get(126) * (int) taux;
			final int agili = this.stats.get(119) * (int) taux;
			final int sages = this.stats.get(124) * (int) taux;
			final int chanc = this.stats.get(123) * (int) taux;
			this.stats.put(118, force);
			this.stats.put(126, intel);
			this.stats.put(119, agili);
			this.stats.put(124, sages);
			this.stats.put(123, chanc);
		}
	}
}
