// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job.maging;

import java.util.ArrayList;

import org.aestia.game.world.World;

public class Concasseur {
	private ArrayList<World.Couple<Integer, Integer>> objects;

	public Concasseur() {
		this.objects = new ArrayList<World.Couple<Integer, Integer>>();
	}

	public ArrayList<World.Couple<Integer, Integer>> getObjects() {
		return this.objects;
	}

	public int addObject(final int id, final int qua) {
		final World.Couple<Integer, Integer> couple = this.search(id);
		if (couple == null) {
			this.objects.add(new World.Couple<Integer, Integer>(id, qua));
			return qua;
		}
		final World.Couple<Integer, Integer> couple2 = couple;
		couple2.second += qua;
		return couple.second;
	}

	public int removeObject(final int id, final int qua) {
		final World.Couple<Integer, Integer> couple = this.search(id);
		if (couple == null) {
			return 0;
		}
		if (qua >= couple.second) {
			this.objects.remove(couple);
			return qua;
		}
		final World.Couple<Integer, Integer> couple2 = couple;
		couple2.second -= qua;
		if (couple.second <= 0) {
			this.objects.remove(couple);
			return 0;
		}
		return couple.second;
	}

	public World.Couple<Integer, Integer> search(final int id) {
		for (final World.Couple<Integer, Integer> couple : this.objects) {
			if (couple.first == id) {
				return couple;
			}
		}
		return null;
	}
}
