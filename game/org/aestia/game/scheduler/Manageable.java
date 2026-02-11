// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.concurrent.ScheduledFuture;

public abstract class Manageable implements Runnable {
	public abstract void launch();

	@Override
	public abstract void run();

	public abstract static class ManageableRespawn extends Manageable {
		public ScheduledFuture<?> task;

		public void cancel() {
			this.task.cancel(true);
			this.cancel();
		}
	}
}
