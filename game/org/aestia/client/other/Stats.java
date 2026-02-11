// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.client.other;

import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;

public class Stats {
	private Map<Integer, Integer> effects;

	public Stats(final boolean addBases, final Player perso) {
		this.effects = new TreeMap<Integer, Integer>();
		this.effects = new TreeMap<Integer, Integer>();
		if (!addBases) {
			return;
		}
		this.effects.put(111, (perso.getLevel() < 100) ? 6 : 7);
		this.effects.put(128, 3);
		this.effects.put(176, (perso.getClasse() == 3) ? 120 : 100);
		this.effects.put(158, 1000);
		this.effects.put(182, 1);
		this.effects.put(174, 1);
	}

	public Stats(final Map<Integer, Integer> stats, final boolean addBases, final Player perso) {
		this.effects = new TreeMap<Integer, Integer>();
		this.effects = stats;
		if (!addBases) {
			return;
		}
		this.effects.put(111, (perso.getLevel() < 100) ? 6 : 7);
		this.effects.put(128, 3);
		this.effects.put(176, (perso.getClasse() == 3) ? 120 : 100);
		this.effects.put(158, 1000);
		this.effects.put(182, 1);
		this.effects.put(174, 1);
	}

	public Stats(final boolean a) {
		(this.effects = new TreeMap<Integer, Integer>()).put(125, 0);
		this.effects.put(124, 0);
		this.effects.put(126, 0);
		this.effects.put(118, 0);
		this.effects.put(123, 0);
		this.effects.put(119, 0);
	}

	public Stats(final Map<Integer, Integer> stats) {
		this.effects = new TreeMap<Integer, Integer>();
		this.effects = stats;
	}

	public Stats() {
		this.effects = new TreeMap<Integer, Integer>();
		this.effects = new TreeMap<Integer, Integer>();
	}

	public Map<Integer, Integer> getMap() {
		return this.effects;
	}

	public int addOneStat(final int id, final int val) {
		if (this.effects.get(id) == null || this.effects.get(id) == 0) {
			this.effects.put(id, val);
		} else {
			final int newVal = this.effects.get(id) + val;
			this.effects.put(id, newVal);
		}
		return this.effects.get(id);
	}

	public boolean isSameStats(final Stats other) {
		for (final Map.Entry<Integer, Integer> entry : this.effects.entrySet()) {
			if (other.getMap().get(entry.getKey()) == null) {
				return false;
			}
			if (other.getMap().get(entry.getKey()).compareTo(entry.getValue()) != 0) {
				return false;
			}
		}
		for (final Map.Entry<Integer, Integer> entry : other.getMap().entrySet()) {
			if (this.effects.get(entry.getKey()) == null) {
				return false;
			}
			if (this.effects.get(entry.getKey()).compareTo(entry.getValue()) != 0) {
				return false;
			}
		}
		return true;
	}

	public static Stats cumulStat(final Stats s1, final Stats s2) {
		final TreeMap<Integer, Integer> effets = new TreeMap<Integer, Integer>();
		for (int a = 0; a <= 1500; ++a) {
			if (s1.effects.get(a) == null || s1.effects.get(a) == 0) {
				if (s2.effects.get(a) == null) {
					continue;
				}
				if (s2.effects.get(a) == 0) {
					continue;
				}
			}
			int som = 0;
			if (s1.effects.get(a) != null) {
				som += s1.effects.get(a);
			}
			if (s2.effects.get(a) != null) {
				som += s2.effects.get(a);
			}
			effets.put(a, som);
		}
		return new Stats(effets, false, null);
	}

	public static Stats cumulStatFight(final Stats s1, final Stats s2) {
		final TreeMap<Integer, Integer> effets = new TreeMap<Integer, Integer>();
		for (int a = 0; a <= 1500; ++a) {
			if (s1.effects.get(a) == null || s1.effects.get(a) == 0) {
				if (s2.effects.get(a) == null) {
					continue;
				}
				if (s2.effects.get(a) == 0) {
					continue;
				}
			}
			int som = 0;
			if (s1.effects.get(a) != null) {
				som += s1.effects.get(a);
			}
			if (s2.effects.get(a) != null) {
				som += s2.effects.get(a);
			}
			effets.put(a, som);
		}
		return new Stats(effets, false, null);
	}

	public String parseToItemSetStats() {
		final StringBuilder str = new StringBuilder();
		if (this.effects.isEmpty()) {
			return "";
		}
		for (final Map.Entry<Integer, Integer> entry : this.effects.entrySet()) {
			if (str.length() > 0) {
				str.append(",");
			}
			str.append(Integer.toHexString(entry.getKey())).append("#").append(Integer.toHexString(entry.getValue()))
					.append("#0#0");
		}
		return str.toString();
	}

	public int getEffect(final int id) {
		int val;
		if (this.effects.get(id) == null) {
			val = 0;
		} else {
			val = this.effects.get(id);
		}
		switch (id) {
		case 160: {
			if (this.effects.get(162) != null) {
				val -= this.getEffect(162);
			}
			if (this.effects.get(124) != null) {
				val += this.getEffect(124) / 4;
				break;
			}
			break;
		}
		case 161: {
			if (this.effects.get(163) != null) {
				val -= this.getEffect(163);
			}
			if (this.effects.get(124) != null) {
				val += this.getEffect(124) / 4;
				break;
			}
			break;
		}
		case 174: {
			if (this.effects.get(175) != null) {
				val -= this.effects.get(175);
				break;
			}
			break;
		}
		case 119: {
			if (this.effects.get(154) != null) {
				val -= this.effects.get(154);
				break;
			}
			break;
		}
		case 118: {
			if (this.effects.get(157) != null) {
				val -= this.effects.get(157);
				break;
			}
			break;
		}
		case 123: {
			if (this.effects.get(152) != null) {
				val -= this.effects.get(152);
				break;
			}
			break;
		}
		case 126: {
			if (this.effects.get(155) != null) {
				val -= this.effects.get(155);
				break;
			}
			break;
		}
		case 111: {
			if (this.effects.get(120) != null) {
				val += this.effects.get(120);
			}
			if (this.effects.get(101) != null) {
				val -= this.effects.get(101);
			}
			if (this.effects.get(168) != null) {
				val -= this.effects.get(168);
				break;
			}
			break;
		}
		case 128: {
			if (this.effects.get(78) != null) {
				val += this.effects.get(78);
			}
			if (this.effects.get(127) != null) {
				val -= this.effects.get(127);
			}
			if (this.effects.get(169) != null) {
				val -= this.effects.get(169);
				break;
			}
			break;
		}
		case 117: {
			if (this.effects.get(116) != null) {
				val -= this.effects.get(116);
				break;
			}
			break;
		}
		case 125: {
			if (this.effects.get(153) != null) {
				val -= this.effects.get(153);
				break;
			}
			break;
		}
		case 110: {
			val = 110;
			break;
		}
		case 112: {
			if (this.effects.get(145) != null) {
				val -= this.effects.get(145);
				break;
			}
			break;
		}
		case 158: {
			if (this.effects.get(159) != null) {
				val -= this.effects.get(159);
				break;
			}
			break;
		}
		case 176: {
			if (this.effects.get(177) != null) {
				val -= this.effects.get(177);
				break;
			}
			break;
		}
		case 242: {
			if (this.effects.get(247) != null) {
				val -= this.effects.get(247);
				break;
			}
			break;
		}
		case 243: {
			if (this.effects.get(248) != null) {
				val -= this.effects.get(248);
				break;
			}
			break;
		}
		case 244: {
			if (this.effects.get(249) != null) {
				val -= this.effects.get(249);
				break;
			}
			break;
		}
		case 240: {
			if (this.effects.get(245) != null) {
				val -= this.effects.get(245);
				break;
			}
			break;
		}
		case 241: {
			if (this.effects.get(246) != null) {
				val -= this.effects.get(246);
				break;
			}
			break;
		}
		case 210: {
			if (this.effects.get(215) != null) {
				val -= this.effects.get(215);
				break;
			}
			break;
		}
		case 211: {
			if (this.effects.get(216) != null) {
				val -= this.effects.get(216);
				break;
			}
			break;
		}
		case 212: {
			if (this.effects.get(217) != null) {
				val -= this.effects.get(217);
				break;
			}
			break;
		}
		case 213: {
			if (this.effects.get(218) != null) {
				val -= this.effects.get(218);
				break;
			}
			break;
		}
		case 214: {
			if (this.effects.get(219) != null) {
				val -= this.effects.get(219);
				break;
			}
			break;
		}
		case 165: {
			if (this.effects.get(165) != null) {
				val = this.effects.get(165);
				break;
			}
			break;
		}
		}
		return val;
	}
}
