// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.object;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.PetEntry;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;
import org.aestia.kernel.Constant;
import org.aestia.object.entity.SoulStone;
import org.aestia.other.Dopeul;

public class ObjectTemplate {
	public int boutique = 0;

	private int id;
	private String strTemplate;
	private String name;
	private int type;
	private int level;
	private int pod;
	private int price;
	private int panoId;
	private String conditions;
	private int PACost;
	private int POmin;
	private int POmax;
	private int tauxCC;
	private int tauxEC;
	private int bonusCC;
	private boolean isTwoHanded;
	private long sold;
	private int avgPrice;
	private int points;
	private ArrayList<ObjectAction> onUseActions;

	public ObjectTemplate(final int id, final String strTemplate, final String name, final int type, final int level,
			final int pod, final int price, final int panoId, final String conditions, final String armesInfos,
			final int sold, final int avgPrice, final int points, int boutique) {
		this.onUseActions = new ArrayList<ObjectAction>();
		this.id = id;
		this.strTemplate = strTemplate;
		this.name = name;
		this.type = type;
		this.level = level;
		this.pod = pod;
		this.price = price;
		this.panoId = panoId;
		this.conditions = conditions;
		this.PACost = -1;
		this.POmin = 1;
		this.POmax = 1;
		this.tauxCC = 100;
		this.tauxEC = 2;
		this.bonusCC = 0;
		this.sold = sold;
		this.avgPrice = avgPrice;
		this.points = points;
		try {
			final String[] infos = armesInfos.split(";");
			this.PACost = Integer.parseInt(infos[0]);
			this.POmin = Integer.parseInt(infos[1]);
			this.POmax = Integer.parseInt(infos[2]);
			this.tauxCC = Integer.parseInt(infos[3]);
			this.tauxEC = Integer.parseInt(infos[4]);
			this.bonusCC = Integer.parseInt(infos[5]);
			this.isTwoHanded = infos[6].equals("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.boutique = boutique;
	}

	public String parseinfo() {
		final String str = String.valueOf(this.PACost) + ";" + this.POmin + ";" + this.POmax + ";" + this.tauxCC + ";"
				+ this.tauxEC + ";" + this.bonusCC + ";";
		return str;
	}

	public void setInfos(final String strTemplate, final String name, final int type, final int level, final int pod,
			final int price, final int panoId, final String conditions, final String armesInfos, final int sold,
			final int avgPrice, final int points) {
		this.strTemplate = strTemplate;
		this.name = name;
		this.type = type;
		this.level = level;
		this.pod = pod;
		this.price = price;
		this.panoId = panoId;
		this.conditions = conditions;
		this.PACost = -1;
		this.POmin = 1;
		this.POmax = 1;
		this.tauxCC = 100;
		this.tauxEC = 2;
		this.bonusCC = 0;
		this.sold = sold;
		this.avgPrice = avgPrice;
		this.points = points;
		try {
			final String[] infos = armesInfos.split(";");
			this.PACost = Integer.parseInt(infos[0]);
			this.POmin = Integer.parseInt(infos[1]);
			this.POmax = Integer.parseInt(infos[2]);
			this.tauxCC = Integer.parseInt(infos[3]);
			this.tauxEC = Integer.parseInt(infos[4]);
			this.bonusCC = Integer.parseInt(infos[5]);
			this.isTwoHanded = infos[6].equals("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getStrTemplate() {
		return this.strTemplate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getType() {
		return this.type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public int getPod() {
		return this.pod;
	}

	public void setPod(final int pod) {
		this.pod = pod;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	public int getPanoId() {
		return this.panoId;
	}

	public void setPanoId(final int panoId) {
		this.panoId = panoId;
	}

	public String getConditions() {
		return this.conditions;
	}

	public void setConditions(final String conditions) {
		this.conditions = conditions;
	}

	public int getPACost() {
		return this.PACost;
	}

	public void setPACost(final int pACost) {
		this.PACost = pACost;
	}

	public int getPOmin() {
		return this.POmin;
	}

	public void setPOmin(final int pOmin) {
		this.POmin = pOmin;
	}

	public int getPOmax() {
		return this.POmax;
	}

	public void setPOmax(final int pOmax) {
		this.POmax = pOmax;
	}

	public int getTauxCC() {
		return this.tauxCC;
	}

	public void setTauxCC(final int tauxCC) {
		this.tauxCC = tauxCC;
	}

	public int getTauxEC() {
		return this.tauxEC;
	}

	public void setTauxEC(final int tauxEC) {
		this.tauxEC = tauxEC;
	}

	public int getBonusCC() {
		return this.bonusCC;
	}

	public void setBonusCC(final int bonusCC) {
		this.bonusCC = bonusCC;
	}

	public boolean isTwoHanded() {
		return this.isTwoHanded;
	}

	public void setTwoHanded(final boolean isTwoHanded) {
		this.isTwoHanded = isTwoHanded;
	}

	public int getAvgPrice() {
		return this.avgPrice;
	}

	public long getSold() {
		return this.sold;
	}

	public int getPoints() {
		return this.points;
	}

	public void addAction(final ObjectAction A) {
		this.onUseActions.add(A);
	}

	public ArrayList<ObjectAction> getOnUseActions() {
		return this.onUseActions;
	}

	public org.aestia.object.Object createNewCertificat(final org.aestia.object.Object obj) {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		if (this.getType() == 77) {
			final PetEntry myPets = World.getPetsEntry(obj.getGuid());
			final Map<Integer, String> txtStat = new TreeMap<Integer, String>();
			Map<Integer, String> actualStat = new TreeMap<Integer, String>();
			actualStat = obj.getTxtStat();
			if (actualStat.containsKey(800)) {
				txtStat.put(800, actualStat.get(800));
			}
			if (actualStat.containsKey(808)) {
				txtStat.put(808, new StringBuilder(String.valueOf(myPets.getLastEatDate())).toString());
			}
			if (actualStat.containsKey(806)) {
				txtStat.put(806, actualStat.get(806));
			}
			if (actualStat.containsKey(940)) {
				txtStat.put(940, actualStat.get(940));
			}
			if (actualStat.containsKey(807)) {
				txtStat.put(807, actualStat.get(807));
			}
			item = new org.aestia.object.Object(id, this.getId(), 1, -1, obj.getStats(), new ArrayList<SpellEffect>(),
					new TreeMap<Integer, Integer>(), txtStat, 0);
			World.removePetsEntry(obj.getGuid());
			Database.getStatique().getPets_dataData().delete(obj.getGuid());
		}
		return item;
	}

	public org.aestia.object.Object createNewFamilier(final org.aestia.object.Object obj) {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		final Map<Integer, String> stats = new TreeMap<Integer, String>();
		stats.putAll(obj.getTxtStat());
		item = new org.aestia.object.Object(id, this.getId(), 1, -1, obj.getStats(), new ArrayList<SpellEffect>(),
				new TreeMap<Integer, Integer>(), stats, 0);
		final long time = System.currentTimeMillis();
		World.addPetsEntry(new PetEntry(id, this.getId(), time, 0, Integer.parseInt(stats.get(800), 16),
				Integer.parseInt(stats.get(806), 16), !stats.containsKey(940)));
		Database.getStatique().getPets_dataData().add(id, time, this.getId());
		return item;
	}

	public org.aestia.object.Object createNewBenediction(final int turn) {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		final Stats stats = this.generateNewStatsFromTemplate(this.getStrTemplate(), true);
		stats.addOneStat(811, turn);
		item = new org.aestia.object.Object(id, this.getId(), 1, 23, stats, new ArrayList<SpellEffect>(),
				new TreeMap<Integer, Integer>(), new TreeMap<Integer, String>(), 0);
		return item;
	}

	public org.aestia.object.Object createNewMalediction() {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		final Stats stats = this.generateNewStatsFromTemplate(this.getStrTemplate(), true);
		stats.addOneStat(811, 1);
		item = new org.aestia.object.Object(id, this.getId(), 1, 22, stats, new ArrayList<SpellEffect>(),
				new TreeMap<Integer, Integer>(), new TreeMap<Integer, String>(), 0);
		return item;
	}

	public org.aestia.object.Object createNewRoleplayBuff() {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		final Stats stats = this.generateNewStatsFromTemplate(this.getStrTemplate(), true);
		stats.addOneStat(811, 1);
		item = new org.aestia.object.Object(id, this.getId(), 1, 21, stats, new ArrayList<SpellEffect>(),
				new TreeMap<Integer, Integer>(), new TreeMap<Integer, String>(), 0);
		return item;
	}

	public org.aestia.object.Object createNewCandy(final int turn) {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		final Stats stats = this.generateNewStatsFromTemplate(this.getStrTemplate(), true);
		stats.addOneStat(811, turn);
		item = new org.aestia.object.Object(id, this.getId(), 1, 25, stats, new ArrayList<SpellEffect>(),
				new TreeMap<Integer, Integer>(), new TreeMap<Integer, String>(), 0);
		return item;
	}

	public org.aestia.object.Object createNewFollowPnj(final int turn) {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item = null;
		final Stats stats = this.generateNewStatsFromTemplate(this.getStrTemplate(), true);
		stats.addOneStat(811, turn);
		stats.addOneStat(148, 0);
		item = new org.aestia.object.Object(id, this.getId(), 1, 24, stats, new ArrayList<SpellEffect>(),
				new TreeMap<Integer, Integer>(), new TreeMap<Integer, String>(), 0);
		return item;
	}

	public org.aestia.object.Object createNewItem(final int qua, final boolean useMax) {
		final int id = World.getNewItemGuid();
		org.aestia.object.Object item;
		if (this.getType() == 24 && (Constant.isCertificatDopeuls(this.getId()) || this.getId() == 6653)) {
			final Map<Integer, String> txtStat = new TreeMap<Integer, String>();
			txtStat.put(805, new StringBuilder(String.valueOf(System.currentTimeMillis())).toString());
			item = new org.aestia.object.Object(id, this.getId(), qua, -1, new Stats(false, null),
					new ArrayList<SpellEffect>(), new TreeMap<Integer, Integer>(), txtStat, 0);
		} else if (this.getId() == 10207) {
			item = new org.aestia.object.Object(id, this.getId(), qua, -1, new Stats(false, null),
					new ArrayList<SpellEffect>(), new TreeMap<Integer, Integer>(), Dopeul.generateStatsTrousseau(), 0);
		} else if (this.getType() == 18) {
			item = new org.aestia.object.Object(id, this.getId(), qua, -1, new Stats(false, null),
					new ArrayList<SpellEffect>(), new TreeMap<Integer, Integer>(),
					World.getPets(this.getId()).generateNewtxtStatsForPets(), 0);
			if (useMax) {
				for (int statsID : World.getPets(this.getId()).getStats()) {
					item.getStats().addOneStat(statsID, Integer.parseInt(World.getPets(this.getId()).getStatsMax()));
				}
			}
			final long time = System.currentTimeMillis();
			World.addPetsEntry(new PetEntry(id, this.getId(), time, 0, 10, 0, false));
			Database.getStatique().getPets_dataData().add(id, time, this.getId());
		} else if (this.getType() == 93) {
			item = new org.aestia.object.Object(id, this.getId(), qua, -1, new Stats(false, null),
					new ArrayList<SpellEffect>(), new TreeMap<Integer, Integer>(),
					this.getStringResistance(this.getStrTemplate()), 0);
		} else if (Constant.isIncarnationWeapon(this.getId())) {
			final Map<Integer, Integer> Stats = new TreeMap<Integer, Integer>();
			Stats.put(1000, 0);
			Stats.put(962, 1);
			item = new org.aestia.object.Object(id, this.getId(), qua, -1,
					this.generateNewStatsFromTemplate(this.getStrTemplate(), useMax),
					this.getEffectTemplate(this.getStrTemplate()), Stats, new TreeMap<Integer, String>(), 0);
		} else {
			final Map<Integer, String> Stat = new TreeMap<Integer, String>();
			switch (this.getType()) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8: {
				final String[] splitted = this.getStrTemplate().split(",");
				String[] array;
				for (int length = (array = splitted).length, i = 0; i < length; ++i) {
					final String s = array[i];
					final String[] stats = s.split("#");
					final int statID = Integer.parseInt(stats[0], 16);
					if (statID == 812) {
						final String ResistanceIni = stats[1];
						Stat.put(statID, ResistanceIni);
					}
				}
				break;
			}
			}
			item = new org.aestia.object.Object(id, this.getId(), qua, -1,
					this.generateNewStatsFromTemplate(this.getStrTemplate(), useMax),
					this.getEffectTemplate(this.getStrTemplate()), new TreeMap<Integer, Integer>(), Stat, 0);
		}
		return item;
	}

	public int getObviType() {
		try {
			String[] split;
			for (int length = (split = this.getStrTemplate().split(",")).length, i = 0; i < length; ++i) {
				final String sts = split[i];
				final String[] stats = sts.split("#");
				final int statID = Integer.parseInt(stats[0], 16);
				if (statID == 973) {
					return Integer.parseInt(stats[3], 16);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			GameServer.addToLog(e.getMessage());
			return 113;
		}
		return 113;
	}

	private Map<Integer, String> getStringResistance(final String statsTemplate) {
		final Map<Integer, String> Stat = new TreeMap<Integer, String>();
		final String[] splitted = statsTemplate.split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			final String ResistanceIni = stats[1];
			Stat.put(statID, ResistanceIni);
		}
		return Stat;
	}

	public Stats generateNewStatsFromTemplate(final String statsTemplate, final boolean useMax) {
		final Stats itemStats = new Stats(false, null);
		if (statsTemplate.equals("") || statsTemplate == null) {
			return itemStats;
		}
		final String[] splitted = statsTemplate.split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			boolean follow = true;
			int[] armes_EFFECT_IDS;
			for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
				final int a = armes_EFFECT_IDS[j];
				if (a == statID) {
					follow = false;
				}
			}
			if (follow) {
				if (statID != 812) {
					boolean isStatsInvalid = false;
					switch (statID) {
					case 110:
					case 139:
					case 605:
					case 614: {
						isStatsInvalid = true;
						break;
					}
					}
					if (!isStatsInvalid) {
						String jet = "";
						int value = 1;
						try {
							jet = stats[4];
							value = Formulas.getRandomJet(jet);
							if (useMax) {
								try {
									final int min = Integer.parseInt(stats[1], 16);
									final int max = Integer.parseInt(stats[2], 16);
									value = min;
									if (max != 0) {
										value = max;
									}
								} catch (Exception e) {
									e.printStackTrace();
									value = Formulas.getRandomJet(jet);
								}
							}
						} catch (Exception ex) {
						}
						itemStats.addOneStat(statID, value);
					}
				}
			}
		}
		return itemStats;
	}

	private ArrayList<SpellEffect> getEffectTemplate(final String statsTemplate) {
		final ArrayList<SpellEffect> Effets = new ArrayList<SpellEffect>();
		if (statsTemplate.equals("") || statsTemplate == null) {
			return Effets;
		}
		final String[] splitted = statsTemplate.split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			int[] armes_EFFECT_IDS;
			for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
				final int a = armes_EFFECT_IDS[j];
				if (a == statID) {
					final int id = statID;
					final String min = stats[1];
					final String max = stats[2];
					final String jet = stats[4];
					final String args = String.valueOf(min) + ";" + max + ";-1;-1;0;" + jet;
					Effets.add(new SpellEffect(id, args, 0, -1));
				}
			}
			switch (statID) {
			case 110:
			case 139:
			case 605:
			case 614: {
				final String min2 = stats[1];
				final String max2 = stats[2];
				final String jet2 = stats[4];
				final String args2 = String.valueOf(min2) + ";" + max2 + ";-1;-1;0;" + jet2;
				Effets.add(new SpellEffect(statID, args2, 0, -1));
				break;
			}
			}
		}
		return Effets;
	}

	public String parseItemTemplateStats() {
		return String.valueOf(this.getId()) + ";" + this.getStrTemplate();
	}

	public String parseItemTemplateStatsBoutique() {
		return String.valueOf(this.getId()) + ";" + this.getStrTemplate();
	}

	public void applyAction(final Player perso, final Player target, final int objID, final short cellid) {
		if (World.getObjet(objID) == null) {
			return;
		}
		if (World.getObjet(objID).getTemplate().getType() == 85) {
			boolean isArena = false;
			for (final int id : Config.arenaMap) {
				if (id == perso.getCurMap().getId()) {
					isArena = true;
				}
			}
			if (!isArena) {
				return;
			}
			final SoulStone pierrePleine = (SoulStone) World.getObjet(objID);
			perso.getCurMap().spawnNewGroup(true, perso.getCurCell().getId(), pierrePleine.parseGroupData(),
					"MiS=" + perso.getId());
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + World.getObjet(objID).getTemplate().getId());
			perso.removeItem(objID, 1, true, true);
		} else {
			for (final ObjectAction a : this.getOnUseActions()) {
				a.apply(perso, target, objID, cellid);
			}
		}
	}

	public synchronized void newSold(final int amount, final int price) {
		final long oldSold = this.getSold();
		this.sold += amount;
		this.avgPrice = (int) ((this.getAvgPrice() * oldSold + price) / this.getSold());
	}
}
