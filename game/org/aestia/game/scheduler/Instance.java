// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

public class Instance {
	private Runnable runnable;
	private Long time;

	public Instance(final Runnable runnable, final Long time) {
		this.runnable = runnable;
		this.time = time;
	}

	public Runnable getRunnable() {
		return this.runnable;
	}

	public Long getTime() {
		return this.time;
	}
}
