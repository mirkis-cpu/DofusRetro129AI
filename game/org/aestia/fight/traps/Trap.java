// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.traps;

import java.util.ArrayList;

import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.Spell;
import org.aestia.kernel.Constant;
import org.aestia.map.Case;

public class Trap {
	private Fighter caster;
	private Case cell;
	private byte size;
	private int spell;
	private Spell.SortStats trapSpell;
	private Fight fight;
	private int color;
	private boolean isUnHide;
	private int teamUnHide;

	public Trap(final Fight fight, final Fighter caster, final Case cell, final byte size,
			final Spell.SortStats trapSpell, final int spell) {
		this.isUnHide = true;
		this.teamUnHide = -1;
		this.fight = fight;
		this.caster = caster;
		this.cell = cell;
		this.spell = spell;
		this.size = size;
		this.trapSpell = trapSpell;
		this.color = Constant.getTrapsColor(spell);
	}

	public Case getCell() {
		return this.cell;
	}

	public byte getSize() {
		return this.size;
	}

	public Fighter getCaster() {
		return this.caster;
	}

	public void setIsUnHide(final Fighter f) {
		this.isUnHide = true;
		this.teamUnHide = f.getTeam();
	}

	public boolean getIsUnHide() {
		return this.isUnHide;
	}

	public int getColor() {
		return this.color;
	}

	public void desappear() {
		final StringBuilder str = new StringBuilder();
		final StringBuilder str2 = new StringBuilder();
		final StringBuilder str3 = new StringBuilder();
		final StringBuilder str4 = new StringBuilder();
		final int team = this.caster.getTeam() + 1;
		str.append("GDZ-").append(this.cell.getId()).append(";").append(this.size).append(";").append(this.color);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, team, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str.toString());
		str2.append("GDC" + this.cell.getId());
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, team, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str2.toString());
		if (this.isUnHide) {
			final int team2 = this.teamUnHide + 1;
			str3.append("GDZ-").append(this.cell.getId()).append(";").append(this.size).append(";").append(this.color);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, team2, 999,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(), str3.toString());
			str4.append("GDC").append(this.cell.getId());
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, team2, 999,
					new StringBuilder(String.valueOf(this.caster.getId())).toString(), str4.toString());
		}
	}

	public void appear(final Fighter f) {
		final StringBuilder str = new StringBuilder();
		final StringBuilder str2 = new StringBuilder();
		final int team = f.getTeam() + 1;
		str.append("GDZ+").append(this.cell.getId()).append(";").append(this.size).append(";").append(this.color);
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, team, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str.toString());
		str2.append("GDC").append(this.cell.getId()).append(";Haaaaaaaaz3005;");
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, team, 999,
				new StringBuilder(String.valueOf(this.caster.getId())).toString(), str2.toString());
	}

	public void onTraped(final Fighter target) {
		if (target.isDead()) {
			return;
		}
		this.fight.getAllTraps().remove(this);
		this.desappear();
		final String str = String.valueOf(this.spell) + "," + this.cell.getId() + ",0,1,1," + this.caster.getId();
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 307,
				new StringBuilder(String.valueOf(target.getId())).toString(), str);
		final ArrayList<Case> cells = new ArrayList<Case>();
		cells.add(this.cell);
		for (int a = 0; a < this.size; ++a) {
			final char[] dirs = { 'b', 'd', 'f', 'h' };
			final ArrayList<Case> cases2 = new ArrayList<Case>();
			cases2.addAll(cells);
			for (final Case aCell : cases2) {
				char[] array;
				for (int length = (array = dirs).length, i = 0; i < length; ++i) {
					final char d = array[i];
					final Case cell = this.fight.getMap()
							.getCase(Pathfinding.GetCaseIDFromDirrection(aCell.getId(), d, this.fight.getMap(), true));
					if (cell != null) {
						if (!cells.contains(cell)) {
							cells.add(cell);
						}
					}
				}
			}
		}
		Fighter fakeCaster;
		if (this.caster.getPersonnage() == null) {
			fakeCaster = new Fighter(this.fight, this.caster.getMob());
		} else {
			fakeCaster = new Fighter(this.fight, this.caster.getPersonnage());
		}
		fakeCaster.setCell(this.cell);
		this.trapSpell.applySpellEffectToFight(this.fight, fakeCaster, target.getCell(), cells, false);
		this.fight.verifIfTeamAllDead();
	}
}
