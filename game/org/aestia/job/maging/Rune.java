// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job.maging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Rune {
	public static List<Rune> runes;
	private int characteristic;
	private float weight;
	private Map<Integer, Integer> bonus;

	static {
		Rune.runes = new ArrayList<Rune>();
	}

	public Rune(final int characteristic, final float weight, final String bonus) {
		this.bonus = new TreeMap<Integer, Integer>(Collections.reverseOrder());
		this.characteristic = characteristic;
		this.weight = weight;
		String[] split2;
		for (int length = (split2 = bonus.split("\\,")).length, i = 0; i < length; ++i) {
			final String arg0 = split2[i];
			final String[] split = arg0.split("\\|");
			this.bonus.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
		Rune.runes.add(this);
	}

	public int getCharacteristic() {
		return this.characteristic;
	}

	public float getWeight() {
		return this.weight;
	}

	public Map<Integer, Integer> getBonus() {
		return this.bonus;
	}

	public double getBase(final int bonus) {
		double base = 0.0;
		switch (this.characteristic) {
		case 111:
		case 128: {
			base = 0.1;
			break;
		}
		case 112:
		case 115:
		case 117:
		case 182:
		case 220: {
			base = 0.3;
			break;
		}
		case 118:
		case 119:
		case 123:
		case 124:
		case 126: {
			switch (bonus) {
			case 1: {
				base = 0.9;
				break;
			}
			case 3: {
				base = 0.7;
				break;
			}
			case 10: {
				base = 0.5;
				break;
			}
			}
			break;
		}
		case 125: {
			switch (bonus) {
			case 3: {
				base = 0.9;
				break;
			}
			case 10: {
				base = 0.7;
				break;
			}
			case 30: {
				base = 0.5;
				break;
			}
			}
			break;
		}
		case 158:
		case 174: {
			switch (bonus) {
			case 10: {
				base = 0.9;
				break;
			}
			case 30: {
				base = 0.7;
				break;
			}
			case 100: {
				base = 0.5;
				break;
			}
			}
			break;
		}
		default: {
			base = 0.9;
			break;
		}
		}
		return base;
	}
}
