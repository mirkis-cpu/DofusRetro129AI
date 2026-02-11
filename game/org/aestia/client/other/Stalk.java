// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.client.other;

import org.aestia.client.Player;

public class Stalk {
	private long time;
	private Player tracked;

	public Stalk(final long time, final Player p) {
		this.time = time;
		this.tracked = p;
	}

	public void setTraque(final Player p) {
		this.tracked = p;
	}

	public Player getTraque() {
		return this.tracked;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(final long t) {
		this.time = t;
	}
}
