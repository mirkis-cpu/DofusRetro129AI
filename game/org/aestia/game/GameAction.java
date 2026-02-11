// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game;

public class GameAction {
	public int id;
	public int actionId;
	public String packet;
	public String args;
	public boolean tp;

	public GameAction(final int aId, final int aActionId, final String aPacket) {
		this.tp = false;
		this.id = aId;
		this.actionId = aActionId;
		this.packet = aPacket;
	}
}
