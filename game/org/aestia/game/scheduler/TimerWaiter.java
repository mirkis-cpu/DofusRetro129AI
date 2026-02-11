// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import org.aestia.kernel.Console;

public class TimerWaiter implements Runnable {
	private Deque<Instance> waitingList;

	public TimerWaiter() {
		this.waitingList = new ConcurrentLinkedDeque<Instance>();
	}

	private void scheduleNextTask() {
		try {
			final Long time = this.waitingList.getFirst().getTime();
			this.launch(this, time);
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	@Override
	public void run() {
		try {
			if (this.waitingList.isEmpty()) {
				this.sendError(new Exception("WaitingList est vide"));
				return;
			}
			final Instance instance = this.waitingList.pop();
			if (!this.waitingList.isEmpty()) {
				this.scheduleNextTask();
			}
			instance.getRunnable().run();
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	public void addNext(final Runnable run, final long put, final TimeUnit unit) {
		try {
			final long time = TimeUnit.MILLISECONDS.convert(put, unit);
			this.waitingList.addLast(new Instance(run, time));
			if (this.waitingList.size() == 1) {
				this.scheduleNextTask();
			}
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	public void addNext(final Runnable run, final long time) {
		try {
			this.addNext(run, time, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	public void addNow(final Runnable run, final long put, final TimeUnit unit) {
		try {
			final long time = TimeUnit.MILLISECONDS.convert(put, unit);
			this.launch(run, time);
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	public void addNow(final Runnable run, final long put) {
		try {
			this.addNow(run, put, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	private synchronized void launch(final Runnable run, final long time) {
		try {
			GlobalManager.worldSheduler.schedule(run, time, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	private void sendError(final Exception e) {
		e.printStackTrace();
		Console.println("Erreur TimerWaiter : " + e.getMessage(), Console.Color.ERROR);
	}
}
