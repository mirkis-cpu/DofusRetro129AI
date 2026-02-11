// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.object;

import java.util.ArrayList;

import org.aestia.client.other.Stats;
import org.aestia.game.world.World;

public class ObjectSet {
	private int id;
	private ArrayList<ObjectTemplate> itemTemplates;
	private ArrayList<Stats> effects;

	public ObjectSet(final int id, final String items, final String bonuses) {
		this.itemTemplates = new ArrayList<ObjectTemplate>();
		this.effects = new ArrayList<Stats>();
		this.id = id;
		String[] split;
		for (int length = (split = items.split(",")).length, i = 0; i < length; ++i) {
			final String str = split[i];
			try {
				final ObjectTemplate obj = World.getObjTemplate(Integer.parseInt(str.trim()));
				if (obj != null) {
					this.itemTemplates.add(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.effects.add(new Stats());
		String[] split2;
		for (int length2 = (split2 = bonuses.split(";")).length, j = 0; j < length2; ++j) {
			final String str = split2[j];
			final Stats S = new Stats();
			String[] split3;
			for (int length3 = (split3 = str.split(",")).length, k = 0; k < length3; ++k) {
				final String str2 = split3[k];
				if (!str2.equalsIgnoreCase("")) {
					try {
						final String[] infos = str2.split(":");
						final int stat = Integer.parseInt(infos[0]);
						final int value = Integer.parseInt(infos[1]);
						S.addOneStat(stat, value);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			this.effects.add(S);
		}
	}

	public int getId() {
		return this.id;
	}

	public Stats getBonusStatByItemNumb(final int numb) {
		if (numb > this.effects.size()) {
			return new Stats();
		}
		return this.effects.get(numb - 1);
	}

	public ArrayList<ObjectTemplate> getItemTemplates() {
		return this.itemTemplates;
	}
}
