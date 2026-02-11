// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job;

import java.util.ArrayList;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.game.GameAction;
import org.aestia.game.world.World;
import org.aestia.map.Case;
import org.aestia.map.InteractiveObject;

public class JobStat {
	private int id;
	private Job template;
	private int lvl;
	private long xp;
	private ArrayList<JobAction> posActions;
	private boolean isCheap;
	private boolean freeOnFails;
	private boolean noRessource;
	private JobAction curAction;
	private int maxCase;
	private int slotsPublic;
	private int position;
	private boolean isPossible;
	private Player player;

	public JobStat(final int id, final Job tp, final int lvl, final long xp, final Player player) {
		this.posActions = new ArrayList<JobAction>();
		this.isCheap = false;
		this.freeOnFails = false;
		this.noRessource = false;
		this.maxCase = 2;
		this.id = id;
		this.template = tp;
		this.lvl = lvl;
		this.xp = xp;
		this.posActions = JobConstant.getPosActionsToJob(tp.getId(), lvl);
		this.player = player;
	}

	public int getId() {
		return this.id;
	}

	public Job getTemplate() {
		return this.template;
	}

	public int get_lvl() {
		return this.lvl;
	}

	public long getXp() {
		return this.xp;
	}

	public boolean isCheap() {
		return this.isCheap;
	}

	public void setIsCheap(final boolean isCheap) {
		this.isCheap = isCheap;
	}

	public boolean isFreeOnFails() {
		return this.freeOnFails;
	}

	public void setFreeOnFails(final boolean freeOnFails) {
		this.freeOnFails = freeOnFails;
	}

	public boolean isNoRessource() {
		return this.noRessource;
	}

	public void setNoRessource(final boolean noRessource) {
		this.noRessource = noRessource;
	}

	public int getMaxcase() {
		return this.maxCase;
	}

	public void setSlotsPublic(final int slots) {
		this.slotsPublic = slots;
	}

	public int getSlotsPublic() {
		return this.slotsPublic;
	}

	public int getPosition() {
		return this.position;
	}

	public void setIsPossibleToActiveBook(final boolean isPossible) {
		this.isPossible = isPossible;
	}

	public boolean getIsPossibleToActiveBook() {
		return this.isPossible;
	}

	public JobAction getJobActionBySkill(final int skill) {
		for (final JobAction JA : this.posActions) {
			if (JA.getId() == skill) {
				return JA;
			}
		}
		return null;
	}

	public void startAction(final int id, final Player P, final InteractiveObject IO, final GameAction GA,
			final Case cell) {
		for (final JobAction JA : this.posActions) {
			if (JA.getId() == id) {
				(this.curAction = JA).startAction(P, IO, GA, cell, this);
			}
		}
	}

	public void endAction(final int id, final Player P, final InteractiveObject IO, final GameAction GA,
			final Case cell) {
		if (this.curAction == null) {
			SocketManager.GAME_SEND_MESSAGE(P, "Erreur action en cours null.");
			return;
		}
		this.curAction.endAction(P, IO, GA, cell);
		this.curAction = null;
		final ArrayList<JobStat> list = new ArrayList<JobStat>();
		list.add(this);
		SocketManager.GAME_SEND_JX_PACKET(P, list);
	}

	public void addXp(final Player P, final long xp, final boolean points) {
		if (this.lvl > 99) {
			return;
		}
		final int exLvl = this.lvl;
		this.xp += xp;
		while (this.xp >= World.getExpLevel(this.lvl + 1).metier && this.lvl < 100) {
			this.levelUp(P, false, points);
		}
		if (this.lvl > exLvl && P.isOnline()) {
			final ArrayList<JobStat> list = new ArrayList<JobStat>();
			list.add(this);
			SocketManager.GAME_SEND_JS_PACKET(P, list);
			SocketManager.GAME_SEND_JN_PACKET(P, this.template.getId(), this.lvl);
			SocketManager.GAME_SEND_STATS_PACKET(P);
			SocketManager.GAME_SEND_Ow_PACKET(P);
			SocketManager.GAME_SEND_JO_PACKET(P, list);
		}
	}

	public String getXpString(final String s) {
		final StringBuilder str = new StringBuilder();
		str.append(World.getExpLevel(this.lvl).metier).append(s);
		str.append(this.xp).append(s);
		str.append(World.getExpLevel((this.lvl < 100) ? (this.lvl + 1) : this.lvl).metier);
		return str.toString();
	}

	public void levelUp(final Player P, final boolean send, final boolean level) {
		++this.lvl;
		this.posActions = JobConstant.getPosActionsToJob(this.template.getId(), this.lvl);
		if (send) {
			final ArrayList<JobStat> list = new ArrayList<JobStat>();
			list.add(this);
			SocketManager.GAME_SEND_JS_PACKET(P, list);
			SocketManager.GAME_SEND_STATS_PACKET(P);
			SocketManager.GAME_SEND_Ow_PACKET(P);
			SocketManager.GAME_SEND_JN_PACKET(P, this.template.getId(), this.lvl);
			SocketManager.GAME_SEND_JO_PACKET(P, list);
		}
		if (level) {
			switch (this.lvl) {
			case 25:
			case 50:
			case 75:
			case 100: {
				this.player.getAccount()
						.setPoints(this.player.getAccount().getPoints() + this.template.getAP(this.lvl));
				SocketManager.GAME_SEND_MESSAGE(this.player, "Vous avez gagn\u00e9 " + this.template.getAP(this.lvl)
						+ " points boutiques pour avoir mont\u00e9 votre m\u00e9tier niveau " + this.lvl + ".");
				break;
			}
			}
		}
	}

	public String parseJS() {
		final StringBuilder str = new StringBuilder();
		str.append("|").append(this.template.getId()).append(";");
		boolean first = true;
		for (final JobAction JA : this.posActions) {
			if (!first) {
				str.append(",");
			} else {
				first = false;
			}
			str.append(JA.getId()).append("~").append(JA.getMin()).append("~");
			if (JA.isCraft()) {
				str.append("0~0~").append(JA.getChance());
			} else {
				str.append(JA.getMax()).append("~0~").append(JA.getTime());
			}
		}
		return str.toString();
	}

	public int getOptBinValue() {
		int nbr = 0;
		nbr += (this.isCheap ? 1 : 0);
		nbr += (this.freeOnFails ? 2 : 0);
		nbr += (this.noRessource ? 4 : 0);
		return nbr;
	}

	public void setOptBinValue(final int bin) {
		this.isCheap = false;
		this.freeOnFails = false;
		this.noRessource = false;
		this.noRessource = ((bin & 0x4) == 0x4);
		this.freeOnFails = ((bin & 0x2) == 0x2);
		this.isCheap = ((bin & 0x1) == 0x1);
	}

	public boolean isValidMapAction(final int id) {
		for (final JobAction JA : this.posActions) {
			if (JA.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public void set_o(final String[] pp) {
		this.setOptBinValue(Integer.parseInt(pp[1]));
		final int cm = JobConstant.getTotalCaseByJobLevel(this.lvl);
		if (cm <= Integer.parseInt(pp[2])) {
			this.maxCase = cm;
		}
		this.maxCase = Integer.parseInt(pp[2]);
	}
}
