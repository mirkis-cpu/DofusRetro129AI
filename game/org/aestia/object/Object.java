// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.other.Stats;
import org.aestia.common.Formulas;
import org.aestia.db.Database;
import org.aestia.entity.PetEntry;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.world.World;
import org.aestia.job.Job;
import org.aestia.job.JobConstant;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.object.entity.Fragment;

public class Object {
	protected ObjectTemplate template;
	protected int quantity;
	protected int position;
	protected int guid;
	protected int obvijevanId;
	protected int obvijevanPos;
	protected int obvijevanLook;
	protected int kamas;
	protected int puit;
	private Stats Stats;
	private ArrayList<SpellEffect> Effects;
	private ArrayList<String> SortStats;
	private Map<Integer, String> txtStats;
	private Map<Integer, Integer> SoulStats;

	public Object(final int Guid, final int template, final int qua, final int pos, final String strStats,
			final int puit) {
		this.quantity = 1;
		this.position = -1;
		this.Stats = new Stats();
		this.Effects = new ArrayList<SpellEffect>();
		this.SortStats = new ArrayList<String>();
		this.txtStats = new TreeMap<Integer, String>();
		this.SoulStats = new TreeMap<Integer, Integer>();
		this.guid = Guid;
		this.template = World.getObjTemplate(template);
		this.quantity = qua;
		this.position = pos;
		this.puit = puit;
		this.Stats = new Stats();
		this.parseStringToStats(strStats);
	}

	public Object(final int Guid) {
		this.quantity = 1;
		this.position = -1;
		this.Stats = new Stats();
		this.Effects = new ArrayList<SpellEffect>();
		this.SortStats = new ArrayList<String>();
		this.txtStats = new TreeMap<Integer, String>();
		this.SoulStats = new TreeMap<Integer, Integer>();
		this.guid = Guid;
		this.template = World.getObjTemplate(8378);
		this.quantity = 1;
		this.position = -1;
		this.puit = 0;
	}

	public int getPuit() {
		return this.puit;
	}

	public void setPuit(final int puit) {
		this.puit = puit;
	}

	public int getObvijevanId() {
		return this.obvijevanId;
	}

	public void setObvijevanId(final int id) {
		this.obvijevanId = id;
	}

	public int getObvijevanPos() {
		return this.obvijevanPos;
	}

	public void setObvijevanPos(final int pos) {
		this.obvijevanPos = pos;
	}

	public int getObvijevanLook() {
		return this.obvijevanLook;
	}

	public void setObvijevanLook(final int look) {
		this.obvijevanLook = look;
	}

	public int getKamas() {
		return this.kamas;
	}

	public void setKamas(final int k) {
		this.kamas = k;
	}

	public void parseStringToStats(final String strStats) {
		String dj1 = "";
		if (!strStats.equalsIgnoreCase("")) {
			final String[] split = strStats.split(",");
			String[] array;
			for (int length = (array = split).length, i = 0; i < length; ++i) {
				final String s = array[i];
				try {
					if (!s.equalsIgnoreCase("")) {
						if (s.substring(0, 3).equalsIgnoreCase("325")
								&& (this.getTemplate().getId() == 10207 || this.getTemplate().getId() == 10601)) {
							this.txtStats.put(805, new StringBuilder(String.valueOf(s.substring(3))).toString());
						} else if (s.substring(0, 3).equalsIgnoreCase("3dc")) {
							this.txtStats.put(988, s.split("#")[4]);
						} else if (s.substring(0, 3).equalsIgnoreCase("3d9")) {
							this.txtStats.put(985, s.split("#")[4]);
						} else {
							final String spell = s;
							final String[] stats = s.split("#");
							final int id = Integer.parseInt(stats[0], 16);
							if (id == 808 && this.getTemplate().getType() == 77) {
								this.txtStats.put(id, s.substring(3));
							} else if (id == 985 || id == 989) {
								this.txtStats.put(id, stats[4]);
							} else if (id == 717) {
								this.SoulStats.put(Integer.parseInt(stats[1], 16), Integer.parseInt(stats[3], 16));
							} else if (id == 814) {
								dj1 = String.valueOf(dj1) + (dj1.isEmpty() ? "" : ",") + stats[3];
								this.txtStats.put(814, dj1);
							} else if (id == 997 || id == 996) {
								this.txtStats.put(id, stats[4]);
							} else if (this.template.getId() == 77 && id == 808) {
								this.txtStats.put(id, s.substring(3));
							} else if (id == 805) {
								this.txtStats.put(id, stats[3]);
							} else if (id != 814 && id != 812
									&& !stats[3].equals("") && (!stats[3].equals("0") || id == 808 || id == 800
											|| id == 806 || id == 940 || id == 807)
									&& (this.getTemplate().getType() != 77 || id != 808)) {
								this.txtStats.put(id, stats[3]);
							} else if (id == 812 && this.getTemplate().getType() == 93) {
								this.txtStats.put(id, stats[4]);
							} else if (id == 812) {
								this.txtStats.put(id, stats[4]);
							} else if (id >= 281 && id <= 294) {
								this.SortStats.add(spell);
							} else {
								boolean follow1 = true;
								switch (id) {
								case 110:
								case 139:
								case 605:
								case 614: {
									final String min = stats[1];
									final String max = stats[2];
									final String jet = stats[4];
									final String args = String.valueOf(min) + ";" + max + ";-1;-1;0;" + jet;
									this.Effects.add(new SpellEffect(id, args, 0, -1));
									follow1 = false;
									break;
								}
								}
								if (follow1) {
									boolean follow2 = true;
									int[] armes_EFFECT_IDS;
									for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
										final int a = armes_EFFECT_IDS[j];
										if (a == id) {
											this.Effects.add(new SpellEffect(id,
													String.valueOf(stats[1]) + ";" + stats[2] + ";-1;-1;0;" + stats[4],
													0, -1));
											follow2 = false;
										}
									}
									if (follow2) {
										this.Stats.addOneStat(id, Integer.parseInt(stats[1], 16));
									}
								}
							}
						}
					}
				} catch (Exception e) {
					Console.println("error id " + this.guid + " -> " + e.getMessage(), Console.Color.ERROR);
					e.printStackTrace();
				}
			}
		}
	}

	public void addTxtStat(final int i, final String s) {
		this.txtStats.put(i, s);
	}

	public String getTraquedName() {
		for (final Map.Entry<Integer, String> entry : this.txtStats.entrySet()) {
			if (Integer.toHexString(entry.getKey()).compareTo("3dd") == 0) {
				return entry.getValue();
			}
		}
		return null;
	}

	public Object(final int Guid, final int template, final int qua, final int pos, final Stats stats,
			final ArrayList<SpellEffect> effects, final Map<Integer, Integer> _SoulStat,
			final Map<Integer, String> _txtStats, final int puit) {
		this.quantity = 1;
		this.position = -1;
		this.Stats = new Stats();
		this.Effects = new ArrayList<SpellEffect>();
		this.SortStats = new ArrayList<String>();
		this.txtStats = new TreeMap<Integer, String>();
		this.SoulStats = new TreeMap<Integer, Integer>();
		this.guid = Guid;
		this.template = World.getObjTemplate(template);
		this.quantity = qua;
		this.position = pos;
		this.Stats = stats;
		this.Effects = effects;
		this.SoulStats = _SoulStat;
		this.txtStats = _txtStats;
		this.obvijevanPos = 0;
		this.obvijevanLook = 0;
		this.puit = puit;
	}

	public Stats getStats() {
		return this.Stats;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity <= 0) {
			quantity = 0;
		}
		this.quantity = quantity;
		Database.getStatique().getItemData().update(this);
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public ObjectTemplate getTemplate() {
		return this.template;
	}

	public int getGuid() {
		return this.guid;
	}

	public Map<Integer, Integer> getSoulStat() {
		return this.SoulStats;
	}

	public Map<Integer, String> getTxtStat() {
		return this.txtStats;
	}

	public String parseItem() {
		final String posi = (this.position == -1) ? "" : Integer.toHexString(this.position);
		return String.valueOf(Integer.toHexString(this.guid)) + "~" + Integer.toHexString(this.template.getId()) + "~"
				+ Integer.toHexString(this.quantity) + "~" + posi + "~" + this.parseStatsString() + ";";
	}

	public String parseStatsString() {
		if (this.getTemplate().getType() == 83) {
			return this.getTemplate().getStrTemplate();
		}
		final StringBuilder stats = new StringBuilder();
		boolean isFirst = true;
		if ((this.getTemplate().getPanoId() >= 81 && this.getTemplate().getPanoId() <= 92)
				|| (this.getTemplate().getPanoId() >= 201 && this.getTemplate().getPanoId() <= 212)) {
			for (final String spell : this.SortStats) {
				if (!isFirst) {
					stats.append(",");
				}
				final String[] sort = spell.split("#");
				final int spellid = Integer.parseInt(sort[1], 16);
				stats.append(sort[0]).append("#").append(sort[1]).append("#0#").append(sort[3]).append("#")
						.append(World.getSort(spellid));
				isFirst = false;
			}
		}
		for (final SpellEffect SE : this.Effects) {
			if (!isFirst) {
				stats.append(",");
			}
			final String[] infos = SE.getArgs().split(";");
			try {
				switch (SE.getEffectID()) {
				case 614: {
					stats.append(Integer.toHexString(SE.getEffectID())).append("#0#0#").append(infos[0]).append("#")
							.append(infos[5]);
					break;
				}
				default: {
					stats.append(Integer.toHexString(SE.getEffectID())).append("#").append(infos[0]).append("#")
							.append(infos[1]).append("#").append(infos[1]).append("#").append(infos[5]);
					break;
				}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, String> entry : this.txtStats.entrySet()) {
			if (!isFirst) {
				stats.append(",");
			}
			if (this.template.getType() == 77 || this.template.getType() == 90) {
				if (entry.getKey() == 800) {
					stats.append(Integer.toHexString(entry.getKey())).append("#").append(entry.getValue()).append("#0#")
							.append(entry.getValue());
				}
				if (entry.getKey() == 940) {
					stats.append(Integer.toHexString(entry.getKey())).append("#").append(entry.getValue()).append("#0#")
							.append(entry.getValue());
				}
				if (entry.getKey() == 807) {
					stats.append(Integer.toHexString(entry.getKey())).append("#").append(entry.getValue()).append("#0#")
							.append(entry.getValue());
				}
				if (entry.getKey() == 806) {
					int corpu = 0;
					int corpulence = 0;
					final String c = entry.getValue();
					if (c != null && !c.equalsIgnoreCase("")) {
						try {
							corpulence = Integer.parseInt(c);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
					if (corpulence > 0 || corpulence < 0) {
						corpu = 7;
					}
					stats.append(Integer.toHexString(entry.getKey())).append("#").append(Integer.toHexString(corpu))
							.append("#" + ((corpulence > 0) ? corpu : 0) + "#").append(Integer.toHexString(corpu));
				}
				if (entry.getKey() == 808 && this.template.getType() == 77) {
					if (entry.getValue().contains("#")) {
						stats.append(Integer.toHexString(entry.getKey())).append(entry.getValue());
					} else {
						stats.append(Integer.toHexString(entry.getKey()))
								.append(Formulas.convertToDate(Long.parseLong(entry.getValue())));
					}
				}
			} else if (entry.getKey() == 985 || entry.getKey() == 989) {
				stats.append(Integer.toHexString(entry.getKey())).append("#0#0#0#").append(entry.getValue());
			} else if (entry.getKey() == 814) {
				if (entry.getValue().equals("0d0+0")) {
					continue;
				}
				String[] split;
				for (int length = (split = entry.getValue().split(",")).length, k = 0; k < length; ++k) {
					final String i = split[k];
					stats.append(",").append(Integer.toHexString(entry.getKey())).append("#0#0#").append(i);
				}
				continue;
			} else if (entry.getKey() == 805) {
				final String item = entry.getValue();
				if (item.contains("#")) {
					final String date = item.split("#")[3];
					if (date != null && !date.equalsIgnoreCase("")) {
						stats.append(Integer.toHexString(entry.getKey()))
								.append(Formulas.convertToDate(Long.parseLong(date)));
					}
				} else {
					stats.append(Integer.toHexString(entry.getKey()))
							.append(Formulas.convertToDate(Long.parseLong(item)));
				}
			} else if (entry.getKey() == 623) {
				stats.append(Integer.toHexString(entry.getKey())).append("#0#0#").append(entry.getValue());
			} else if (entry.getKey() == 800 || entry.getKey() == 806 || entry.getKey() == 808
					|| entry.getKey() == 807) {
				final PetEntry p = World.getPetsEntry(this.getGuid());
				if (p == null) {
					if (entry.getKey() == 800) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("a").append("#0#a");
					}
					if (entry.getKey() == 806) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("0").append("#0#0");
					}
					if (entry.getKey() == 808) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("0").append("#0#0");
					}
					if (entry.getKey() == 807) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("0").append("#0#0");
					}
				} else {
					if (entry.getKey() == 800) {
						stats.append(Integer.toHexString(entry.getKey())).append("#")
								.append(Integer.toHexString(p.getPdv())).append("#0#")
								.append(Integer.toHexString(p.getPdv()));
					}
					if (entry.getKey() == 806) {
						stats.append(Integer.toHexString(entry.getKey())).append("#")
								.append(Integer.toString(p.parseCorpulence()))
								.append("#" + ((p.getCorpulence() > 0) ? p.parseCorpulence() : 0) + "#")
								.append(Integer.toString(p.parseCorpulence()));
					}
					if (entry.getKey() == 808) {
						stats.append(Integer.toHexString(entry.getKey())).append(p.parseLastEatDate());
					}
					if (entry.getKey() == 807) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append(entry.getValue())
								.append("#0#").append(entry.getValue());
					}
					if (p.getIsEupeoh() && entry.getKey() == 940) {
						stats.append(Integer.toHexString(entry.getKey())).append("#")
								.append(Integer.toHexString(p.getIsEupeoh() ? 1 : 0)).append("#0#")
								.append(Integer.toHexString(p.getIsEupeoh() ? 1 : 0));
					}
				}
			} else if (entry.getKey() == 812 && this.getTemplate().getType() == 93) {
				stats.append(Integer.toHexString(entry.getKey())).append("#")
						.append(Integer.toHexString(this.getResistanceMax(this.getTemplate().getStrTemplate())))
						.append("#").append(entry.getValue()).append("#")
						.append(Integer.toHexString(this.getResistanceMax(this.getTemplate().getStrTemplate())));
			} else if (entry.getKey() == 812) {
				stats.append(Integer.toHexString(entry.getKey())).append("#")
						.append(Integer.toHexString(this.getResistanceMax(this.getTemplate().getStrTemplate())))
						.append("#").append(entry.getValue()).append("#")
						.append(Integer.toHexString(this.getResistanceMax(this.getTemplate().getStrTemplate())));
			} else {
				stats.append(Integer.toHexString(entry.getKey())).append("#0#0#0#").append(entry.getValue());
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, Integer> entry2 : this.SoulStats.entrySet()) {
			if (!isFirst) {
				stats.append(",");
			}
			if (this.getTemplate().getType() == 18) {
				stats.append(Integer.toHexString(717)).append("#").append(Integer.toHexString(entry2.getKey()))
						.append("#").append("0").append("#").append(Integer.toHexString(entry2.getValue()));
			}
			if (entry2.getKey() == 962) {
				stats.append(Integer.toHexString(962)).append("#").append(Integer.toHexString(entry2.getKey()))
						.append("#").append("0").append("#").append(Integer.toHexString(entry2.getValue()));
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, Integer> entry2 : this.Stats.getMap().entrySet()) {
			final int statID = entry2.getKey();
			if ((this.getTemplate().getPanoId() >= 81 && this.getTemplate().getPanoId() <= 92)
					|| (this.getTemplate().getPanoId() >= 201 && this.getTemplate().getPanoId() <= 212)) {
				final String[] modificable = this.template.getStrTemplate().split(",");
				for (int cantMod = modificable.length, j = 0; j < cantMod; ++j) {
					final String[] mod = modificable[j].split("#");
					if (Integer.parseInt(mod[0], 16) == statID) {
						final String jet = "0d0+" + Integer.parseInt(mod[1], 16);
						if (!isFirst) {
							stats.append(",");
						}
						stats.append(mod[0]).append("#").append(mod[1]).append("#0#").append(mod[3]).append("#")
								.append(jet);
						isFirst = false;
					}
				}
			} else {
				if (!isFirst) {
					stats.append(",");
				}
				if (statID == 970 || statID == 971 || statID == 972 || statID == 973 || statID == 974) {
					final int jet2 = entry2.getValue();
					if (statID == 974 || statID == 972 || statID == 970) {
						stats.append(Integer.toHexString(statID)).append("#0#0#").append(Integer.toHexString(jet2));
					} else {
						stats.append(Integer.toHexString(statID)).append("#0#0#").append(jet2);
					}
					if (statID == 973) {
						this.setObvijevanPos(jet2);
					}
					if (statID == 972) {
						this.setObvijevanLook(jet2);
					}
				} else if (statID == 811) {
					final String jet3 = "0d0+" + entry2.getValue();
					stats.append(Integer.toHexString(statID)).append("#");
					stats.append("0#0#").append(Integer.toHexString(entry2.getValue())).append("#").append(jet3);
				} else {
					final String jet3 = "0d0+" + entry2.getValue();
					stats.append(Integer.toHexString(statID)).append("#");
					stats.append(Integer.toHexString(entry2.getValue())).append("#0#0#").append(jet3);
				}
				isFirst = false;
			}
		}
		return stats.toString();
	}

	public String parseStatsStringSansUserObvi() {
		if (this.getTemplate().getType() == 83) {
			return this.getTemplate().getStrTemplate();
		}
		final StringBuilder stats = new StringBuilder();
		boolean isFirst = true;
		if (this instanceof Fragment) {
			final Fragment fragment = (Fragment) this;
			for (final World.Couple<Integer, Integer> couple : fragment.getRunes()) {
				stats.append(stats.toString().isEmpty() ? ((Integer) couple.first) : (";" + couple.first)).append(":")
						.append(couple.second);
			}
			return stats.toString();
		}
		for (final Map.Entry<Integer, String> entry : this.txtStats.entrySet()) {
			if (!isFirst) {
				stats.append(",");
			}
			if (this.template.getType() == 77) {
				if (entry.getKey() == 800) {
					stats.append(Integer.toHexString(entry.getKey())).append("#").append(entry.getValue()).append("#0#")
							.append(entry.getValue());
				}
				if (entry.getKey() == 806) {
					stats.append(Integer.toHexString(entry.getKey())).append("#").append(entry.getValue())
							.append("#" + entry.getValue() + "#").append(entry.getValue());
				}
				if (entry.getKey() == 808) {
					if (entry.getValue().contains("#")) {
						stats.append(Integer.toHexString(entry.getKey())).append(entry.getValue());
					} else {
						stats.append(Integer.toHexString(entry.getKey()))
								.append(Formulas.convertToDate(Long.parseLong(entry.getValue())));
					}
				}
			} else if (entry.getKey() == 805) {
				if (entry.getValue().contains("#")) {
					stats.append(Integer.toHexString(entry.getKey())).append(entry.getValue());
				} else {
					stats.append(Integer.toHexString(entry.getKey())).append("#0#0#")
							.append(Long.parseLong(entry.getValue()));
				}
			} else if (entry.getKey() == 985 || entry.getKey() == 989) {
				stats.append(Integer.toHexString(entry.getKey())).append("#0#0#0#").append(entry.getValue());
			} else if (entry.getKey() == 814) {
				String[] split;
				for (int length = (split = entry.getValue().split(",")).length, j = 0; j < length; ++j) {
					final String i = split[j];
					stats.append(",").append(Integer.toHexString(entry.getKey())).append("#0#0#").append(i);
				}
			} else if (entry.getKey() == 623) {
				stats.append(Integer.toHexString(entry.getKey())).append("#0#0#").append(entry.getValue());
			} else if (entry.getKey() == 800 || entry.getKey() == 806 || entry.getKey() == 808) {
				final PetEntry p = World.getPetsEntry(this.getGuid());
				if (p == null) {
					if (entry.getKey() == 800) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("a").append("#0#a");
					}
					if (entry.getKey() == 806) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("0").append("#0#0");
					}
					if (entry.getKey() == 808) {
						stats.append(Integer.toHexString(entry.getKey())).append("#").append("0").append("#0#0");
					}
				} else {
					if (entry.getKey() == 800) {
						stats.append(Integer.toHexString(entry.getKey())).append("#")
								.append(Integer.toHexString(p.getPdv())).append("#0#")
								.append(Integer.toHexString(p.getPdv()));
					}
					if (entry.getKey() == 806) {
						stats.append(Integer.toHexString(entry.getKey())).append("#")
								.append(Integer.toString(p.parseCorpulence()))
								.append("#" + ((p.getCorpulence() > 0) ? p.parseCorpulence() : 0) + "#")
								.append(Integer.toString(p.parseCorpulence()));
					}
					if (entry.getKey() == 808) {
						stats.append(Integer.toHexString(entry.getKey())).append(p.parseLastEatDate());
					}
					if (p.getIsEupeoh() && entry.getKey() == 940) {
						stats.append(Integer.toHexString(entry.getKey())).append("#")
								.append(Integer.toHexString(p.getIsEupeoh() ? 1 : 0)).append("#0#")
								.append(Integer.toHexString(p.getIsEupeoh() ? 1 : 0));
					}
				}
			} else {
				stats.append(Integer.toHexString(entry.getKey())).append("#0#0#0#").append(entry.getValue());
			}
			isFirst = false;
		}
		for (final SpellEffect SE : this.Effects) {
			if (!isFirst) {
				stats.append(",");
			}
			final String[] infos = SE.getArgs().split(";");
			try {
				stats.append(Integer.toHexString(SE.getEffectID())).append("#").append(infos[0]).append("#")
						.append(infos[1]).append("#0#").append(infos[5]);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, Integer> entry2 : this.SoulStats.entrySet()) {
			if (!isFirst) {
				stats.append(",");
			}
			stats.append(Integer.toHexString(717)).append("#").append(Integer.toHexString(entry2.getKey())).append("#")
					.append("0").append("#").append(Integer.toHexString(entry2.getValue()));
			isFirst = false;
		}
		for (final Map.Entry<Integer, Integer> entry2 : this.Stats.getMap().entrySet()) {
			if (!isFirst) {
				stats.append(",");
			}
			final String jet = "0d0+" + entry2.getValue();
			stats.append(Integer.toHexString(entry2.getKey())).append("#")
					.append(Integer.toHexString(entry2.getValue()));
			stats.append("#0#0#").append(jet);
			isFirst = false;
		}
		return stats.toString();
	}

	public String parseToSave() {
		return this.parseStatsStringSansUserObvi();
	}

	public String obvijevanOCO_Packet(final int pos) {
		String strPos = String.valueOf(pos);
		if (pos == -1) {
			strPos = "";
		}
		String upPacket = "OCO";
		upPacket = String.valueOf(upPacket) + Integer.toHexString(this.getGuid()) + "~";
		upPacket = String.valueOf(upPacket) + Integer.toHexString(this.getTemplate().getId()) + "~";
		upPacket = String.valueOf(upPacket) + Integer.toHexString(this.getQuantity()) + "~";
		upPacket = String.valueOf(upPacket) + strPos + "~";
		upPacket = String.valueOf(upPacket) + this.parseStatsString();
		return upPacket;
	}

	public void obvijevanNourir(final Object obj) {
		if (obj == null) {
			return;
		}
		for (final Map.Entry<Integer, Integer> entry : this.Stats.getMap().entrySet()) {
			if (entry.getKey() != 974) {
				continue;
			}
			if (entry.getValue() > 500) {
				return;
			}
			entry.setValue(entry.getValue() + obj.getTemplate().getLevel() / 3);
		}
	}

	public void obvijevanChangeStat(final int statID, final int val) {
		for (final Map.Entry<Integer, Integer> entry : this.Stats.getMap().entrySet()) {
			if (entry.getKey() != statID) {
				continue;
			}
			entry.setValue(val);
		}
	}

	public void removeAllObvijevanStats() {
		this.setObvijevanPos(0);
		final Stats StatsSansObvi = new Stats();
		for (final Map.Entry<Integer, Integer> entry : this.Stats.getMap().entrySet()) {
			final int statID = entry.getKey();
			if (statID != 970 && statID != 971 && statID != 972 && statID != 973) {
				if (statID == 974) {
					continue;
				}
				StatsSansObvi.addOneStat(statID, entry.getValue());
			}
		}
		this.Stats = StatsSansObvi;
	}

	public void removeAll_ExepteObvijevanStats() {
		this.setObvijevanPos(0);
		final Stats StatsSansObvi = new Stats();
		for (final Map.Entry<Integer, Integer> entry : this.Stats.getMap().entrySet()) {
			final int statID = entry.getKey();
			if (statID != 971 && statID != 972 && statID != 973 && statID != 974) {
				continue;
			}
			StatsSansObvi.addOneStat(statID, entry.getValue());
		}
		this.Stats = StatsSansObvi;
	}

	public String parseFMStatsString(final String statsstr, final Object obj, final int add, final boolean negatif) {
		String stats = "";
		boolean isFirst = true;
		for (final SpellEffect SE : obj.Effects) {
			if (!isFirst) {
				stats = String.valueOf(stats) + ",";
			}
			final String[] infos = SE.getArgs().split(";");
			try {
				stats = String.valueOf(stats) + Integer.toHexString(SE.getEffectID()) + "#" + infos[0] + "#" + infos[1]
						+ "#0#" + infos[5];
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, Integer> entry : obj.Stats.getMap().entrySet()) {
			if (!isFirst) {
				stats = String.valueOf(stats) + ",";
			}
			if (Integer.toHexString(entry.getKey()).compareTo(statsstr) == 0) {
				int newstats = 0;
				if (negatif) {
					newstats = entry.getValue() - add;
					if (newstats < 1) {
						continue;
					}
				} else {
					newstats = entry.getValue() + add;
				}
				final String jet = "0d0+" + newstats;
				stats = String.valueOf(stats) + Integer.toHexString(entry.getKey()) + "#"
						+ Integer.toHexString(entry.getValue() + add) + "#0#0#" + jet;
			} else {
				final String jet2 = "0d0+" + entry.getValue();
				stats = String.valueOf(stats) + Integer.toHexString(entry.getKey()) + "#"
						+ Integer.toHexString(entry.getValue()) + "#0#0#" + jet2;
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, String> entry2 : obj.txtStats.entrySet()) {
			if (!isFirst) {
				stats = String.valueOf(stats) + ",";
			}
			stats = String.valueOf(stats) + Integer.toHexString(entry2.getKey()) + "#0#0#0#" + entry2.getValue();
			isFirst = false;
		}
		return stats;
	}

	public String getObvijevanStatsOnly() {
		final Object obj = getCloneObjet(this, 1);
		obj.removeAll_ExepteObvijevanStats();
		return obj.parseStatsStringSansUserObvi();
	}

	public String parseFMEchecStatsString(final Object obj, final double poid) {
		String stats = "";
		boolean isFirst = true;
		for (final SpellEffect SE : obj.Effects) {
			if (!isFirst) {
				stats = String.valueOf(stats) + ",";
			}
			final String[] infos = SE.getArgs().split(";");
			try {
				stats = String.valueOf(stats) + Integer.toHexString(SE.getEffectID()) + "#" + infos[0] + "#" + infos[1]
						+ "#0#" + infos[5];
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			isFirst = false;
		}
		for (final Map.Entry<Integer, Integer> entry : obj.Stats.getMap().entrySet()) {
			int newstats = 0;
			if (entry.getKey() == 152 || entry.getKey() == 154 || entry.getKey() == 155 || entry.getKey() == 157
					|| entry.getKey() == 116 || entry.getKey() == 153) {
				float a = (float) (entry.getValue() * poid / 100.0);
				if (a < 1.0f) {
					a = 1.0f;
				}
				final float chute = entry.getValue() + a;
				newstats = (int) Math.floor(chute);
				if (newstats > Job.getBaseMaxJet(obj.getTemplate().getId(), Integer.toHexString(entry.getKey()))) {
					newstats = Job.getBaseMaxJet(obj.getTemplate().getId(), Integer.toHexString(entry.getKey()));
				}
			} else {
				if (entry.getKey() == 127) {
					continue;
				}
				if (entry.getKey() == 101) {
					continue;
				}
				final float chute2 = (float) (entry.getValue() - entry.getValue() * poid / 100.0);
				newstats = (int) Math.floor(chute2);
			}
			if (newstats < 1) {
				continue;
			}
			final String jet = "0d0+" + newstats;
			if (!isFirst) {
				stats = String.valueOf(stats) + ",";
			}
			stats = String.valueOf(stats) + Integer.toHexString(entry.getKey()) + "#" + Integer.toHexString(newstats)
					+ "#0#0#" + jet;
			isFirst = false;
		}
		for (final Map.Entry<Integer, String> entry2 : obj.txtStats.entrySet()) {
			if (!isFirst) {
				stats = String.valueOf(stats) + ",";
			}
			stats = String.valueOf(stats) + Integer.toHexString(entry2.getKey()) + "#0#0#0#" + entry2.getValue();
			isFirst = false;
		}
		return stats;
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
				} catch (Exception e) {
					e.printStackTrace();
				}
				itemStats.addOneStat(statID, value);
			}
		}
		return itemStats;
	}

	public void setStats(final Stats SS) {
		this.Stats = SS;
	}

	public static int getPoidOfActualItem(final String statsTemplate) {
		int poid = 0;
		int somme = 0;
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
				String jet = "";
				int value = 1;
				try {
					jet = stats[4];
					value = Formulas.getRandomJet(jet);
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
				} catch (Exception e) {
					e.printStackTrace();
				}
				int multi = 1;
				if (statID == 118 || statID == 126 || statID == 125 || statID == 119 || statID == 123 || statID == 158
						|| statID == 174) {
					multi = 1;
				} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
					multi = 2;
				} else if (statID == 124 || statID == 176) {
					multi = 3;
				} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
					multi = 4;
				} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
					multi = 5;
				} else if (statID == 225) {
					multi = 15;
				} else if (statID == 178 || statID == 112) {
					multi = 20;
				} else if (statID == 115 || statID == 182) {
					multi = 30;
				} else if (statID == 117) {
					multi = 50;
				} else if (statID == 128) {
					multi = 90;
				} else if (statID == 111) {
					multi = 100;
				}
				poid = value * multi;
				somme += poid;
			}
		}
		return somme;
	}

	public static int getPoidOfBaseItem(final int i) {
		int poid = 0;
		int somme = 0;
		final String NaturalStatsItem = Database.getStatique().getItem_templateData().getStatsTemplate(i);
		if (NaturalStatsItem == null || NaturalStatsItem.isEmpty()) {
			return 0;
		}
		final String[] splitted = NaturalStatsItem.split(",");
		String[] array;
		for (int length = (array = splitted).length, j = 0; j < length; ++j) {
			final String s = array[j];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			boolean follow = true;
			int[] armes_EFFECT_IDS;
			for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, k = 0; k < length2; ++k) {
				final int a = armes_EFFECT_IDS[k];
				if (a == statID) {
					follow = false;
				}
			}
			if (follow) {
				String jet = "";
				int value = 1;
				try {
					jet = stats[4];
					value = Formulas.getRandomJet(jet);
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
				} catch (Exception e) {
					e.printStackTrace();
				}
				int multi = 1;
				if (statID == 118 || statID == 126 || statID == 125 || statID == 119 || statID == 123 || statID == 158
						|| statID == 174) {
					multi = 1;
				} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
					multi = 2;
				} else if (statID == 124 || statID == 176) {
					multi = 3;
				} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
					multi = 4;
				} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
					multi = 5;
				} else if (statID == 225) {
					multi = 15;
				} else if (statID == 178 || statID == 112) {
					multi = 20;
				} else if (statID == 115 || statID == 182) {
					multi = 30;
				} else if (statID == 117) {
					multi = 50;
				} else if (statID == 128) {
					multi = 90;
				} else if (statID == 111) {
					multi = 100;
				}
				poid = value * multi;
				somme += poid;
			}
		}
		return somme;
	}

	public void setTemplate(final int Tid) {
		this.template = World.getObjTemplate(Tid);
	}

	public ArrayList<SpellEffect> getEffects() {
		return this.Effects;
	}

	public ArrayList<SpellEffect> getCritEffects() {
		final ArrayList<SpellEffect> effets = new ArrayList<SpellEffect>();
		for (final SpellEffect SE : this.Effects) {
			try {
				boolean boost = true;
				int[] no_BOOST_CC_IDS;
				for (int length = (no_BOOST_CC_IDS = Constant.NO_BOOST_CC_IDS).length, j = 0; j < length; ++j) {
					final int i = no_BOOST_CC_IDS[j];
					if (i == SE.getEffectID()) {
						boost = false;
					}
				}
				final String[] infos = SE.getArgs().split(";");
				if (!boost) {
					effets.add(SE);
				} else {
					final int min = Integer.parseInt(infos[0], 16) + (boost ? this.template.getBonusCC() : 0);
					final int max = Integer.parseInt(infos[1], 16) + (boost ? this.template.getBonusCC() : 0);
					final String jet = "1d" + (max - min + 1) + "+" + (min - 1);
					final String newArgs = "0;0;0;-1;0;" + jet;
					effets.add(new SpellEffect(SE.getEffectID(), newArgs, 0, -1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return effets;
	}

	public static Object getCloneObjet(final Object obj, final int qua) {
		final Map<Integer, Integer> maps = new TreeMap<Integer, Integer>();
		maps.putAll(obj.getStats().getMap());
		final Stats newStats = new Stats(maps);
		final Object ob = new Object(World.getNewItemGuid(), obj.getTemplate().getId(), qua, -1, newStats,
				obj.getEffects(), obj.getSoulStat(), obj.getTxtStat(), obj.getPuit());
		return ob;
	}

	public static String getRunes(final Object Obj) {
		String rune = "";
		final String statsTemplate = Obj.parseStatsString();
		boolean isFirst = true;
		final String[] Splitted = statsTemplate.split(",");
		final int random = Formulas.getRandomValue(1, 10);
		final int quantity = 11 - random;
		String[] array;
		for (int length = (array = Splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] Stats = s.split("#");
			if (!Stats[0].isEmpty()) {
				final int statID = Integer.parseInt(Stats[0], 16);
				int numero = 0;
				try {
					numero = Integer.parseInt(Stats[4].replaceAll("0d0\\+", ""));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if (numero > 0) {
					if (random >= JobConstant.getStatIDRune(statID)) {
						boolean isSecond = true;
						int[] armes_EFFECT_IDS;
						for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
							final int a = armes_EFFECT_IDS[j];
							if (a == statID) {
								isSecond = false;
							}
						}
						if (isSecond) {
							if (!isFirst) {
								rune = String.valueOf(rune) + ";";
							}
							rune = String.valueOf(rune) + JobConstant.statRune(statID, numero) + "," + quantity;
							isFirst = false;
						}
					}
				}
			}
		}
		return rune;
	}

	public void clearStats() {
		this.Stats = new Stats();
		this.Effects.clear();
		this.txtStats.clear();
		this.SortStats.clear();
		this.SoulStats.clear();
	}

	public void refreshStatsObjet(final String newsStats) {
		this.parseStringToStats(newsStats);
	}

	public int getResistance(final String statsTemplate) {
		int Resistance = 0;
		final String[] splitted = statsTemplate.split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			if (Integer.parseInt(stats[0], 16) == 812) {
				Resistance = Integer.parseInt(stats[2], 16);
			}
		}
		return Resistance;
	}

	public String getResistance(final int CellID) {
		final StringBuilder Resistance = new StringBuilder();
		for (final Map.Entry<Integer, String> entry : this.txtStats.entrySet()) {
			if (entry.getKey() == CellID) {
				Resistance.append(entry.getValue());
			}
		}
		return Resistance.toString();
	}

	public int getResistanceMax(final String statsTemplate) {
		int ResistanceMax = 0;
		final String[] splitted = statsTemplate.split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			if (Integer.parseInt(stats[0], 16) == 812) {
				ResistanceMax = Integer.parseInt(stats[1], 16);
			}
		}
		return ResistanceMax;
	}

	public boolean isOverFm(final int stat, final int val) {
		boolean trouve = false;
		String statsTemplate = "";
		statsTemplate = this.template.getStrTemplate();
		if (statsTemplate == null || statsTemplate.isEmpty()) {
			return false;
		}
		final String[] split = statsTemplate.split(",");
		String[] array;
		for (int length = (array = split).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			if (statID == stat) {
				trouve = true;
				boolean sig = true;
				int[] armes_EFFECT_IDS;
				for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
					final int a = armes_EFFECT_IDS[j];
					if (a == statID) {
						sig = false;
					}
				}
				if (sig) {
					String jet = "";
					int value = 1;
					try {
						jet = stats[4];
						value = Formulas.getRandomJet(jet);
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
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (val > value) {
						return true;
					}
				}
			}
		}
		return !trouve;
	}

	public String parseStringStatsEC_FM(final Object obj, final double poid, final int carac) {
		String stats = "";
		boolean first = false;
		double perte = 0.0;
		for (final SpellEffect EH : obj.Effects) {
			if (first) {
				stats = String.valueOf(stats) + ",";
			}
			final String[] infos = EH.getArgs().split(";");
			try {
				stats = String.valueOf(stats) + Integer.toHexString(EH.getEffectID()) + "#" + infos[0] + "#" + infos[1]
						+ "#0#" + infos[5];
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			first = true;
		}
		final Map<Integer, Integer> statsObj = new HashMap<Integer, Integer>(obj.Stats.getMap());
		final ArrayList<Integer> keys = new ArrayList<Integer>(obj.Stats.getMap().keySet());
		Collections.shuffle(keys);
		int p = 0;
		int key = 0;
		if (keys.size() > 1) {
			for (final Integer i : keys) {
				final int value = statsObj.get(i);
				if (this.isOverFm(i, value)) {
					key = i;
					break;
				}
				++p;
			}
			if (key > 0) {
				keys.remove(p);
				keys.add(p, keys.get(0));
				keys.remove(0);
				keys.add(0, key);
			}
		}
		for (final Integer i : keys) {
			int newstats = 0;
			final int statID = i;
			final int value2 = statsObj.get(i);
			if (perte > poid || statID == carac) {
				newstats = value2;
			} else if (statID == 152 || statID == 154 || statID == 155 || statID == 157 || statID == 116
					|| statID == 153) {
				float a = (float) (value2 * poid / 100.0);
				if (a < 1.0f) {
					a = 1.0f;
				}
				final float chute = value2 + a;
				newstats = (int) Math.floor(chute);
				if (newstats > Job.getBaseMaxJet(obj.getTemplate().getId(), Integer.toHexString(i))) {
					newstats = Job.getBaseMaxJet(obj.getTemplate().getId(), Integer.toHexString(i));
				}
			} else {
				if (statID == 127) {
					continue;
				}
				if (statID == 101) {
					continue;
				}
				float chute2;
				if (this.isOverFm(statID, value2)) {
					chute2 = (float) (value2 - value2 * (poid - (int) Math.floor(perte)) * 2.0 / 100.0);
				} else {
					chute2 = (float) (value2 - value2 * (poid - (int) Math.floor(perte)) / 100.0);
				}
				if (chute2 / value2 < 0.75) {
					chute2 = value2 * 0.75f;
				}
				final double chutePwr = (value2 - chute2) * World.getPwrPerEffet(statID);
				perte += chutePwr;
				newstats = (int) Math.floor(chute2);
			}
			if (newstats < 1) {
				continue;
			}
			final String jet = "0d0+" + newstats;
			if (first) {
				stats = String.valueOf(stats) + ",";
			}
			stats = String.valueOf(stats) + Integer.toHexString(statID) + "#" + Integer.toHexString(newstats) + "#0#0#"
					+ jet;
			first = true;
		}
		for (final Map.Entry<Integer, String> entry : obj.txtStats.entrySet()) {
			if (first) {
				stats = String.valueOf(stats) + ",";
			}
			stats = String.valueOf(stats) + Integer.toHexString(entry.getKey()) + "#0#0#0#" + entry.getValue();
			first = true;
		}
		return stats;
	}

	public int getRandomValue(final String statsTemplate, final int statsId) {
		if (statsTemplate.equals("") || statsTemplate == null) {
			return 0;
		}
		final String[] splitted = statsTemplate.split(",");
		int value = 0;
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			if (statID == statsId) {
				String jet = "";
				try {
					jet = stats[4];
					value = Formulas.getRandomJet(jet);
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		}
		return value;
	}
}
