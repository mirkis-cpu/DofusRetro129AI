// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.aestia.game.world.World;
import org.aestia.object.ObjectTemplate;

public class Job {
	private int id;
	private ArrayList<Integer> tools;
	private Map<Integer, ArrayList<Integer>> crafts;
	private Map<Integer, ArrayList<Integer>> skills;
	private Map<Integer, Integer> ap;

	public Job(final int id, final String tools, final String crafts, final String skills, final String AP) {
		this.tools = new ArrayList<Integer>();
		this.crafts = new HashMap<Integer, ArrayList<Integer>>();
		this.skills = new HashMap<Integer, ArrayList<Integer>>();
		this.ap = new HashMap<Integer, Integer>();
		this.id = id;
		if (!tools.equals("")) {
			String[] split;
			for (int length = (split = tools.split(",")).length, i = 0; i < length; ++i) {
				final String str = split[i];
				try {
					this.tools.add(Integer.parseInt(str));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!crafts.equals("")) {
			String[] split2;
			for (int length2 = (split2 = crafts.split("\\|")).length, j = 0; j < length2; ++j) {
				final String str = split2[j];
				try {
					final int skID = Integer.parseInt(str.split(";")[0]);
					final ArrayList<Integer> list = new ArrayList<Integer>();
					String[] split3;
					for (int length3 = (split3 = str.split(";")[1].split(",")).length, k = 0; k < length3; ++k) {
						final String str2 = split3[k];
						list.add(Integer.parseInt(str2));
					}
					this.crafts.put(skID, list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!skills.isEmpty() || !skills.equals("")) {
			String[] split4;
			for (int length4 = (split4 = skills.split("\\|")).length, l = 0; l < length4; ++l) {
				final String arg0 = split4[l];
				final String io = arg0.split(";")[0];
				final String skill = arg0.split(";")[1];
				final ArrayList<Integer> list2 = new ArrayList<Integer>();
				String[] split5;
				for (int length5 = (split5 = skill.split(",")).length, n = 0; n < length5; ++n) {
					final String arg2 = split5[n];
					list2.add(Integer.parseInt(arg2));
				}
				String[] split6;
				for (int length6 = (split6 = io.split(",")).length, n2 = 0; n2 < length6; ++n2) {
					final String arg2 = split6[n2];
					this.skills.put(Integer.parseInt(arg2), list2);
				}
			}
		}
		if (!AP.equalsIgnoreCase("") && !AP.isEmpty() && AP.length() > 7) {
			final String[] str3 = AP.split(",");
			if (str3.length > 3) {
				this.ap.put(25, Integer.parseInt(str3[0]));
				this.ap.put(50, Integer.parseInt(str3[1]));
				this.ap.put(75, Integer.parseInt(str3[2]));
				this.ap.put(100, Integer.parseInt(str3[3]));
			}
		}
	}

	public int getAP(final int level) {
		if (this.ap.isEmpty()) {
			return 0;
		}
		if (this.ap.containsKey(level)) {
			return this.ap.get(level);
		}
		return 0;
	}

	public int getId() {
		return this.id;
	}

	public Map<Integer, ArrayList<Integer>> getSkills() {
		return this.skills;
	}

	public boolean isValidTool(final int id1) {
		for (final int id2 : this.tools) {
			if (id2 == id1) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Integer> getListBySkill(final int skill) {
		return this.crafts.get(skill);
	}

	public boolean canCraft(final int skill, final int template) {
		if (this.crafts.get(skill) != null) {
			for (final int id : this.crafts.get(skill)) {
				if (id == template) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isMaging() {
		switch (this.id) {
		case 43:
		case 44:
		case 45:
		case 46:
		case 47:
		case 48:
		case 49:
		case 50:
		case 62:
		case 63:
		case 64: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public static int getBaseMaxJet(final int templateID, final String statsModif) {
		final ObjectTemplate t = World.getObjTemplate(templateID);
		final String[] splitted = t.getStrTemplate().split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			if (stats[0].compareTo(statsModif) <= 0) {
				if (stats[0].compareTo(statsModif) == 0) {
					int max = Integer.parseInt(stats[2], 16);
					if (max == 0) {
						max = Integer.parseInt(stats[1], 16);
					}
					return max;
				}
			}
		}
		return 0;
	}

	public static int getActualJet(final org.aestia.object.Object obj, final String statsModif) {
		for (final Map.Entry<Integer, Integer> entry : obj.getStats().getMap().entrySet()) {
			if (Integer.toHexString(entry.getKey()).compareTo(statsModif) > 0) {
				continue;
			}
			if (Integer.toHexString(entry.getKey()).compareTo(statsModif) == 0) {
				final int JetActual = entry.getValue();
				return JetActual;
			}
		}
		return 0;
	}

	public static byte viewActualStatsItem(final org.aestia.object.Object obj, final String stats) {
		if (!obj.parseStatsString().isEmpty()) {
			for (final Map.Entry<Integer, Integer> entry : obj.getStats().getMap().entrySet()) {
				if (Integer.toHexString(entry.getKey()).compareTo(stats) > 0) {
					if (Integer.toHexString(entry.getKey()).compareTo("98") == 0 && stats.compareTo("7b") == 0) {
						return 2;
					}
					if (Integer.toHexString(entry.getKey()).compareTo("9a") == 0 && stats.compareTo("77") == 0) {
						return 2;
					}
					if (Integer.toHexString(entry.getKey()).compareTo("9b") == 0 && stats.compareTo("7e") == 0) {
						return 2;
					}
					if (Integer.toHexString(entry.getKey()).compareTo("9d") == 0 && stats.compareTo("76") == 0) {
						return 2;
					}
					if (Integer.toHexString(entry.getKey()).compareTo("74") == 0 && stats.compareTo("75") == 0) {
						return 2;
					}
					if (Integer.toHexString(entry.getKey()).compareTo("99") == 0 && stats.compareTo("7d") == 0) {
						return 2;
					}
					continue;
				} else {
					if (Integer.toHexString(entry.getKey()).compareTo(stats) == 0) {
						return 1;
					}
					continue;
				}
			}
			return 0;
		}
		return 0;
	}

	public static byte viewBaseStatsItem(final org.aestia.object.Object obj, final String ItemStats) {
		final String[] splitted = obj.getTemplate().getStrTemplate().split(",");
		String[] array;
		for (int length = (array = splitted).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			if (stats[0].compareTo(ItemStats) > 0) {
				if (stats[0].compareTo("98") == 0 && ItemStats.compareTo("7b") == 0) {
					return 2;
				}
				if (stats[0].compareTo("9a") == 0 && ItemStats.compareTo("77") == 0) {
					return 2;
				}
				if (stats[0].compareTo("9b") == 0 && ItemStats.compareTo("7e") == 0) {
					return 2;
				}
				if (stats[0].compareTo("9d") == 0 && ItemStats.compareTo("76") == 0) {
					return 2;
				}
				if (stats[0].compareTo("74") == 0 && ItemStats.compareTo("75") == 0) {
					return 2;
				}
				if (stats[0].compareTo("99") == 0 && ItemStats.compareTo("7d") == 0) {
					return 2;
				}
			} else if (stats[0].compareTo(ItemStats) == 0) {
				return 1;
			}
		}
		return 0;
	}
}
