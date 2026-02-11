// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.object.entity;

import java.util.ArrayList;

import org.aestia.game.world.World;
import org.aestia.object.Object;

public class SoulStone extends Object {
	private ArrayList<World.Couple<Integer, Integer>> monsters;

	public SoulStone(final int Guid, final int qua, final int template, final int pos, final String strStats) {
		super(pos, pos, pos, pos, strStats, 0);
		this.guid = Guid;
		this.template = World.getObjTemplate(template);
		this.quantity = 1;
		this.position = -1;
		this.monsters = new ArrayList<World.Couple<Integer, Integer>>();
		this.parseStringToStats(strStats);
	}

	@Override
	public void parseStringToStats(final String m) {
		if (!m.equalsIgnoreCase("")) {
			if (this.monsters == null) {
				this.monsters = new ArrayList<World.Couple<Integer, Integer>>();
			}
			final String[] split = m.split("\\|");
			String[] array;
			for (int length = (array = split).length, i = 0; i < length; ++i) {
				final String s = array[i];
				try {
					final int monstre = Integer.parseInt(s.split(",")[0]);
					final int level = Integer.parseInt(s.split(",")[1]);
					final World.Couple<Integer, Integer> couple = new World.Couple<Integer, Integer>(monstre, level);
					this.monsters.add(couple);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String parseStatsString() {
		final StringBuilder stats = new StringBuilder();
		boolean isFirst = true;
		for (final World.Couple<Integer, Integer> coupl : this.monsters) {
			if (!isFirst) {
				stats.append(",");
			}
			try {
				stats.append("26f#0#0#").append(Integer.toHexString(coupl.first));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			isFirst = false;
		}
		return stats.toString();
	}

	public String parseGroupData() {
		final StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		for (final World.Couple<Integer, Integer> curMob : this.monsters) {
			if (!isFirst) {
				toReturn.append(";");
			}
			toReturn.append(curMob.first).append(",").append(curMob.second).append(",").append(curMob.second);
			isFirst = false;
		}
		return toReturn.toString();
	}

	@Override
	public String parseToSave() {
		final StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		for (final World.Couple<Integer, Integer> curMob : this.monsters) {
			if (!isFirst) {
				toReturn.append("|");
			}
			toReturn.append(curMob.first).append(",").append(curMob.second);
			isFirst = false;
		}
		return toReturn.toString();
	}
}
