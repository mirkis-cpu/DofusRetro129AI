// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.traps;

import org.aestia.common.SocketManager;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.Spell;
import org.aestia.kernel.Constant;
import org.aestia.map.Case;

public class Glyph {
	private Fighter caster;
	private Case cell;
	private byte size;
	private int spell;
	private Spell.SortStats trapSpell;
	private byte duration;
	private Fight fight;
	private int color;

	public Glyph(final Fight fight, final Fighter caster, final Case cell, final byte size,
			final Spell.SortStats trapSpell, final byte duration, final int spell) {
		this.fight = fight;
		this.caster = caster;
		this.cell = cell;
		this.spell = spell;
		this.size = size;
		this.trapSpell = trapSpell;
		this.duration = duration;
		this.color = Constant.getGlyphColor(spell);
	}

	public Fighter getCaster() {
		return this.caster;
	}

	public Case getCell() {
		return this.cell;
	}

	public byte getSize() {
		return this.size;
	}

	public int getSpell() {
		return this.spell;
	}

	public byte getDuration() {
		return this.duration;
	}

	public int decrementDuration() {
		return --this.duration;
	}

	public int getColor() {
		return this.color;
	}

	public void onTraped(final Fighter target) {
		final String str = String.valueOf(this.spell) + "," + this.cell.getId() + ", 0, 1, 1," + this.caster.getId();
		SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 307,
				new StringBuilder(String.valueOf(target.getId())).toString(), str);
		this.trapSpell.applySpellEffectToFight(this.fight, this.caster, target.getCell(), false, true);
		this.fight.verifIfTeamAllDead();
	}

	public void desapear() {
		SocketManager.GAME_SEND_GDZ_PACKET_TO_FIGHT(this.fight, 7, "-", this.cell.getId(), this.size, this.color);
		SocketManager.GAME_SEND_GDC_PACKET_TO_FIGHT(this.fight, 7, this.cell.getId());
	}
}
