// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.exchange;

import java.util.ArrayList;

import org.aestia.client.Player;
import org.aestia.game.world.World;

public abstract class Exchange {
	protected final Player player1;
	protected final Player player2;
	protected long kamas1;
	protected long kamas2;
	protected ArrayList<World.Couple<Integer, Integer>> items1;
	protected ArrayList<World.Couple<Integer, Integer>> items2;
	protected boolean ok1;
	protected boolean ok2;

	public Exchange(final Player player1, final Player player2) {
		this.kamas1 = 0L;
		this.kamas2 = 0L;
		this.items1 = new ArrayList<World.Couple<Integer, Integer>>();
		this.items2 = new ArrayList<World.Couple<Integer, Integer>>();
		this.player1 = player1;
		this.player2 = player2;
	}

	public abstract void apply();

	public abstract void cancel();

	public static World.Couple<Integer, Integer> getCoupleInList(final ArrayList<World.Couple<Integer, Integer>> items,
			final int id) {
		for (final World.Couple<Integer, Integer> couple : items) {
			if (couple.first == id) {
				return couple;
			}
		}
		return null;
	}
}
