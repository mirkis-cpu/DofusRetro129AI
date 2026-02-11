// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.object.entity;

import java.util.ArrayList;

import org.aestia.game.world.World;
import org.aestia.object.Object;

public class Fragment extends Object {
	private ArrayList<World.Couple<Integer, Integer>> runes;

	public Fragment(final int Guid, final String runes) {
		super(Guid);
		this.runes = new ArrayList<World.Couple<Integer, Integer>>();
		if (runes.isEmpty()) {
			return;
		}
		String[] split2;
		for (int length = (split2 = runes.split("\\;")).length, i = 0; i < length; ++i) {
			final String rune = split2[i];
			final String[] split = rune.split("\\:");
			this.runes.add(new World.Couple<Integer, Integer>(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
		}
	}

	public ArrayList<World.Couple<Integer, Integer>> getRunes() {
		return this.runes;
	}

	public void addRune(final int id, final int quantity) {
		final World.Couple<Integer, Integer> rune = this.search(id);
		if (rune == null) {
			this.runes.add(new World.Couple<Integer, Integer>(id, quantity));
		} else {
			final World.Couple<Integer, Integer> couple = rune;
			couple.second += quantity;
		}
	}

	public World.Couple<Integer, Integer> search(final int id) {
		for (final World.Couple<Integer, Integer> couple : this.runes) {
			if (couple.first == id) {
				return couple;
			}
		}
		return null;
	}
}
