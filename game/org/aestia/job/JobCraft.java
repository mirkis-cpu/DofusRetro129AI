// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.game.scheduler.TimerWaiter;

public class JobCraft {
	public Player perso1;
	public JobAction jobAction;
	private Player perso2;
	private int time;
	private boolean itsOk;
	public TimerWaiter waiter;

	public JobCraft(final JobAction JA, final Player perso) {
		this.time = 0;
		this.itsOk = true;
		this.waiter = new TimerWaiter();
		this.jobAction = JA;
		this.perso1 = perso;
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				if (JobCraft.this.itsOk) {
					JobCraft.this.jobAction.craft(false, -1);
				}
			}
		}, 1000L);
		this.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				if (!JobCraft.this.itsOk) {
					JobCraft.this.repeat(JobCraft.this.time, JobCraft.this.time, JobCraft.this.perso2);
				}
			}
		}, 1000L);
	}

	public void setAction(final int time, final Player perso2) {
		this.time = time;
		this.jobAction.broken = false;
		this.perso2 = perso2;
		this.itsOk = false;
	}

	public void repeat(final int time, final int _time, final Player P) {
		final int j = time - _time;
		this.jobAction.player = P;
		this.jobAction.isRepeat = true;
		if (this.jobAction.broke || this.jobAction.broken || P.getCurJobAction() == null || !P.isOnline()) {
			if (P.getCurJobAction() == null) {
				this.jobAction.broken = true;
			}
			if (P.isOnline()) {
				SocketManager.GAME_SEND_Ea_PACKET(this.jobAction.player, this.jobAction.broken ? "2" : "4");
			}
			this.end();
			return;
		}
		this.jobAction.ingredients.putAll(this.jobAction.lastCraft);
		SocketManager.GAME_SEND_EA_PACKET(this.jobAction.player, new StringBuilder(String.valueOf(_time)).toString());
		this.jobAction.craft(this.jobAction.isRepeat, j);
		if (_time <= 0) {
			this.end();
			return;
		}
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				JobCraft.this.repeat(time, _time - 1, P);
			}
		}, 1000L);
	}

	public void end() {
		SocketManager.GAME_SEND_Ea_PACKET(this.jobAction.player, "1");
		if (!this.jobAction.data.isEmpty()) {
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK_FM(this.jobAction.player, 'O', "+", this.jobAction.data);
		}
		this.jobAction.ingredients.clear();
		this.jobAction.lastCraft.clear();
		this.jobAction.isRepeat = false;
	}
}
