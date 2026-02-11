// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.turn;

import java.util.Timer;

import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;

public class Turn extends Timer {
	private final TurnTimer timer;

	public Turn(final Fight fight, final Fighter fighter) {
		this.schedule(this.timer = new TurnTimer(fight, fighter), 30000L);
	}

	public TurnTimer getTimer() {
		return this.timer;
	}

	public void stop() {
		try {
			this.getTimer().stop();
			this.cancel();
			this.purge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
