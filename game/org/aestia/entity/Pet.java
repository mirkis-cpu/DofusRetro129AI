// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Pet {
	private int templateId;
	private int type;
	private String gap;
	private String statsUp;
	private int max;
	private int gain;
	private int deadtemplate;
	private int epo;
	private Map<Integer, ArrayList<Integer>> categ;
	private Map<Integer, ArrayList<Integer>> template;
	private Map<Integer, ArrayList<Map<Integer, Integer>>> monster;
	private String statsMax;

	public Pet(final int Tid, final int type, final String gap, final String statsUp, final String statsMax,
			final int max, final int gain, final int Dtemplate, final int epo) {
		this.categ = new TreeMap<Integer, ArrayList<Integer>>();
		this.template = new TreeMap<Integer, ArrayList<Integer>>();
		this.monster = new HashMap<Integer, ArrayList<Map<Integer, Integer>>>();
		this.templateId = Tid;
		this.type = type;
		this.gap = gap;
		this.statsUp = statsUp;
		this.statsMax = statsMax;
		this.decompileStatsUpItem();
		this.max = max;
		this.gain = gain;
		this.deadtemplate = Dtemplate;
		this.epo = epo;
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public int getType() {
		return this.type;
	}

	public String getGap() {
		return this.gap;
	}

	public String getStatsUp() {
		return this.statsUp;
	}

	public int getMax() {
		return this.max;
	}

	public int getGain() {
		return this.gain;
	}

	public int getDeadTemplate() {
		return this.deadtemplate;
	}

	public int getEpo() {
		return this.epo;
	}

	public Map<Integer, ArrayList<Map<Integer, Integer>>> getMonsters() {
		return this.monster;
	}

	public String getStatsMax() {
		return this.statsMax;
	}

	public int getNumbMonster(final int StatID, final int monsterID) {
		for (final Map.Entry<Integer, ArrayList<Map<Integer, Integer>>> ID : this.monster.entrySet()) {
			if (ID.getKey() == StatID) {
				for (final Map<Integer, Integer> entry : ID.getValue()) {
					for (final Map.Entry<Integer, Integer> monsterEntry : entry.entrySet()) {
						if (monsterEntry.getKey() == monsterID) {
							return monsterEntry.getValue();
						}
					}
				}
			}
		}
		return 0;
	}

	public void decompileStatsUpItem() {
		if (this.type == 3 || this.type == 2) {
			if (this.statsUp.contains(";")) {
				String[] split;
				for (int length = (split = this.statsUp.split(";")).length, i = 0; i < length; ++i) {
					final String cut = split[i];
					final String[] cut2 = cut.split("\\|");
					final int statsID = Integer.parseInt(cut2[0], 16);
					final ArrayList<Integer> ar = new ArrayList<Integer>();
					String[] split2;
					for (int length2 = (split2 = cut2[1].split("#")).length, j = 0; j < length2; ++j) {
						final String categ = split2[j];
						final int categID = Integer.parseInt(categ);
						ar.add(categID);
					}
					if (this.type == 3) {
						this.categ.put(statsID, ar);
					}
					if (this.type == 2) {
						this.template.put(statsID, ar);
					}
				}
			} else {
				final String[] cut3 = this.statsUp.split("\\|");
				final int statsID2 = Integer.parseInt(cut3[0], 16);
				final ArrayList<Integer> ar2 = new ArrayList<Integer>();
				String[] split3;
				for (int length3 = (split3 = cut3[1].split("#")).length, k = 0; k < length3; ++k) {
					final String categ2 = split3[k];
					final int categID2 = Integer.parseInt(categ2);
					ar2.add(categID2);
				}
				if (this.type == 3) {
					this.categ.put(statsID2, ar2);
				}
				if (this.type == 2) {
					this.template.put(statsID2, ar2);
				}
			}
		} else if (this.type == 1) {
			if (this.statsUp.contains(";")) {
				String[] split4;
				for (int length4 = (split4 = this.statsUp.split(";")).length, l = 0; l < length4; ++l) {
					final String cut = split4[l];
					final String[] cut2 = cut.split("\\|");
					final int statsID = Integer.parseInt(cut2[0], 16);
					final ArrayList<Map<Integer, Integer>> ar3 = new ArrayList<Map<Integer, Integer>>();
					String[] split5;
					for (int length5 = (split5 = cut2[1].split("#")).length, n = 0; n < length5; ++n) {
						final String soustotal = split5[n];
						int monsterID = 0;
						int qua = 0;
						String[] split6;
						for (int length6 = (split6 = soustotal.split(",")).length, n2 = 0; n2 < length6; ++n2) {
							final String Iqua = split6[n2];
							if (monsterID == 0) {
								monsterID = Integer.parseInt(Iqua);
							} else {
								qua = Integer.parseInt(Iqua);
								final Map<Integer, Integer> Mqua = new TreeMap<Integer, Integer>();
								Mqua.put(monsterID, qua);
								ar3.add(Mqua);
								this.monster.put(statsID, ar3);
								monsterID = 0;
							}
						}
					}
				}
			} else {
				final String[] cut3 = this.statsUp.split("\\|");
				final int statsID2 = Integer.parseInt(cut3[0], 16);
				final ArrayList<Map<Integer, Integer>> ar4 = new ArrayList<Map<Integer, Integer>>();
				String[] split7;
				for (int length7 = (split7 = cut3[1].split("#")).length, n3 = 0; n3 < length7; ++n3) {
					final String categ2 = split7[n3];
					int monsterID2 = 0;
					int qua2 = 0;
					String[] split8;
					for (int length8 = (split8 = categ2.split(",")).length, n4 = 0; n4 < length8; ++n4) {
						final String Iqua2 = split8[n4];
						if (monsterID2 == 0) {
							monsterID2 = Integer.parseInt(Iqua2);
						} else {
							qua2 = Integer.parseInt(Iqua2);
							final Map<Integer, Integer> Mqua2 = new TreeMap<Integer, Integer>();
							Mqua2.put(monsterID2, qua2);
							ar4.add(Mqua2);
							this.monster.put(statsID2, ar4);
						}
					}
				}
			}
		}
	}

	public boolean canEat(final int Tid, final int categID, final int monsterId) {
		if (this.type == 1) {
			for (final Map.Entry<Integer, ArrayList<Map<Integer, Integer>>> ID : this.monster.entrySet()) {
				for (final Map<Integer, Integer> entry : ID.getValue()) {
					for (final Map.Entry<Integer, Integer> monsterEntry : entry.entrySet()) {
						if (monsterEntry.getKey() == monsterId) {
							return true;
						}
					}
				}
			}
			return false;
		}
		if (this.type == 2) {
			for (final Map.Entry<Integer, ArrayList<Integer>> ID2 : this.template.entrySet()) {
				if (ID2.getValue().contains(Tid)) {
					return true;
				}
			}
			return false;
		}
		if (this.type == 3) {
			for (final Map.Entry<Integer, ArrayList<Integer>> ID2 : this.categ.entrySet()) {
				if (ID2.getValue().contains(categID)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	public ArrayList<Integer> getStats() {
		ArrayList<Integer> data = new ArrayList<>();
		for(String value : this.statsUp.split("\\;")) 
			data.add(Integer.parseInt(value.substring(0,2),16));
		return data;
	}

	public int statsIdByEat(final int Tid, final int categID, final int monsterId) {
		if (this.type == 1) {
			for (final Map.Entry<Integer, ArrayList<Map<Integer, Integer>>> ID : this.monster.entrySet()) {
				for (final Map<Integer, Integer> entry : ID.getValue()) {
					for (final Map.Entry<Integer, Integer> monsterEntry : entry.entrySet()) {
						if (monsterEntry.getKey() == monsterId) {
							return ID.getKey();
						}
					}
				}
			}
			return 0;
		}
		if (this.type == 2) {
			for (final Map.Entry<Integer, ArrayList<Integer>> ID2 : this.template.entrySet()) {
				if (ID2.getValue().contains(Tid)) {
					return ID2.getKey();
				}
			}
			return 0;
		}
		if (this.type == 3) {
			for (final Map.Entry<Integer, ArrayList<Integer>> ID2 : this.categ.entrySet()) {
				if (ID2.getValue().contains(categID)) {
					return ID2.getKey();
				}
			}
			return 0;
		}
		return 0;
	}

	public Map<Integer, String> generateNewtxtStatsForPets() {
		final Map<Integer, String> txtStat = new TreeMap<Integer, String>();
		txtStat.put(800, "a");
		txtStat.put(808, "0");
		txtStat.put(806, "0");
		return txtStat;
	}
}
