// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.client.other;

import java.util.ArrayList;
import java.util.List;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;

public class Group {
	private List<Player> persos;
	private Player chief;

	public Group(final Player p1, final Player p2) {
		this.persos = new ArrayList<Player>();
		this.chief = p1;
		this.persos.add(p1);
		this.persos.add(p2);
	}

	public List<Player> getPersos() {
		return this.persos;
	}

	public Player getChief() {
		return this.chief;
	}

	public boolean isChief(final int i) {
		return this.chief.getId() == i;
	}

	public void addPerso(final Player p) {
		this.persos.add(p);
	}

	public int getPersosNumber() {
		return this.persos.size();
	}

	public int getGroupLevel() {
		int lvls = 0;
		for (final Player p : this.persos) {
			lvls += p.getLevel();
		}
		return lvls;
	}

	public void leave(final Player i) {
		if (!this.persos.contains(i)) {
			return;
		}
		i.setGroup(null);
		this.persos.remove(i);
		if (this.persos.size() == 1) {
			this.persos.get(0).setGroup(null);
			if (this.persos.get(0).getAccount() == null || this.persos.get(0).getGameClient() == null) {
				return;
			}
			SocketManager.GAME_SEND_PV_PACKET(this.persos.get(0).getGameClient(), "");
		} else {
			SocketManager.GAME_SEND_PM_DEL_PACKET_TO_GROUP(this, i.getId());
		}
	}
}
