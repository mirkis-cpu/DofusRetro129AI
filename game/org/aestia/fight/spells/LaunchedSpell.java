// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.spells;

import org.aestia.fight.Fighter;

public class LaunchedSpell {
	private Fighter target;
	private int spellId;
	private int cooldown;

	public LaunchedSpell(final Fighter t, final Spell.SortStats SS, final Fighter caster) {
		this.target = null;
		this.spellId = 0;
		this.cooldown = 0;
		this.target = t;
		this.spellId = SS.getSpellID();
		if (caster.getType() == 1 && caster.getPersonnage().getItemClasseSpell().containsKey(SS.getSpellID())) {
			final int modi = caster.getPersonnage().getItemClasseModif(SS.getSpellID(), 286);
			this.cooldown = SS.getCoolDown() - modi;
		} else {
			this.cooldown = SS.getCoolDown();
		}
	}

	public Fighter getTarget() {
		return this.target;
	}

	public int getSpellId() {
		return this.spellId;
	}

	public int getCooldown() {
		return this.cooldown;
	}

	public void actuCooldown() {
		--this.cooldown;
	}

	public static boolean cooldownGood(final Fighter fighter, final int id) {
		for (final LaunchedSpell S : fighter.getLaunchedSorts()) {
			if (S.spellId == id && S.getCooldown() > 0) {
				return false;
			}
		}
		return true;
	}

	public static int getNbLaunch(final Fighter fighter, final int id) {
		int nb = 0;
		for (final LaunchedSpell S : fighter.getLaunchedSorts()) {
			if (S.spellId == id) {
				++nb;
			}
		}
		return nb;
	}

	public static int getNbLaunchTarget(final Fighter fighter, final Fighter target, final int id) {
		if (target == null) {
			return 0;
		}
		int nb = 0;
		for (final LaunchedSpell S : fighter.getLaunchedSorts()) {
			if (S.target != null && S.spellId == id && S.target.getId() == target.getId()) {
				++nb;
			}
		}
		return nb;
	}
}
