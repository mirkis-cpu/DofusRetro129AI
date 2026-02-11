// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.npc;

import java.util.ArrayList;

import org.aestia.client.Player;
import org.aestia.other.Action;
import org.aestia.quest.Quest;

public class NpcAnswer {
	private int id;
	private ArrayList<Action> actions;
	private Quest quest;

	public NpcAnswer(final int id) {
		this.actions = new ArrayList<Action>();
		this.quest = null;
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public ArrayList<Action> getActions() {
		return this.actions;
	}

	public void setActions(final ArrayList<Action> actions) {
		this.actions = actions;
	}

	public void addAction(final Action action0) {
		final ArrayList<Action> actions = new ArrayList<Action>();
		actions.addAll(this.actions);
		for (final Action action : actions) {
			if (action.getId() == action0.getId()) {
				this.getActions().remove(action);
			}
		}
		this.actions.add(action0);
	}

	public boolean apply(final Player player) {
		boolean leave = true;
		for (final Action action : this.getActions()) {
			leave = action.apply(player, null, -1, -1);
		}
		return leave;
	}

	public boolean isAnotherDialog() {
		for (final Action action : this.getActions()) {
			if (action.getId() == 1) {
				return true;
			}
		}
		return false;
	}

	public Quest getQuest() {
		return this.quest;
	}

	public void setQuest(final Quest quest) {
		this.quest = quest;
	}
}
