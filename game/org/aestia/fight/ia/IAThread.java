// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.ia;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.aestia.common.CryptManager;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.LaunchedSpell;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.GameAction;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;
import org.aestia.map.Case;

public class IAThread {
	public static final ScheduledExecutorService sheduler = Executors.newSingleThreadScheduledExecutor();

	private Fight _fight;
	private Fighter _fighter;
	private boolean stop;
	private boolean haveAttacted;
	private boolean haveInvok;

	public IAThread(final Fighter fighter, final Fight fight) {
		this.stop = false;
		this.haveAttacted = false;
		this.haveInvok = false;
		this._fighter = fighter;
		this._fight = fight;
		this.run();
	}

	public void run() {
		this.stop = false;
		this.haveInvok = false;
		if (this._fighter.getMob() == null) {
			if (this._fighter.isDouble()) {
				this.apply_type5(this._fighter, this._fight, 5);
			} else if (this._fighter.isCollector()) {
				this.apply_typePerco(this._fighter, this._fight, 6);
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.endTurn();
				}
			}, 2000L, TimeUnit.MILLISECONDS);
		} else if (this._fighter.getMob().getTemplate() == null) {
			this.stop = true;
			this.endTurn();
		} else {
			switch (this._fighter.getMob().getTemplate().getIa()) {
			case 0: {
				this.apply_type0(this._fighter, this._fight);
				break;
			}
			case 1: {
				this.apply_type1(this._fighter, this._fight, 5);
				break;
			}
			case 2: {
				this.apply_type2(this._fighter, this._fight, 5);
				break;
			}
			case 3: {
				this.apply_type3(this._fighter, this._fight, 5);
				break;
			}
			case 4: {
				this.apply_type4(this._fighter, this._fight, 5);
				break;
			}
			case 5: {
				this.apply_type5(this._fighter, this._fight, 5);
				break;
			}
			case 6: {
				this.apply_type6(this._fighter, this._fight, 5);
				break;
			}
			case 7: {
				this.apply_type7(this._fighter, this._fight, 4);
				break;
			}
			case 8: {
				this.apply_type8(this._fighter, this._fight, 5);
				break;
			}
			case 9: {
				this.apply_type9(this._fighter, this._fight, 8);
				break;
			}
			case 10: {
				this.apply_type10(this._fighter, this._fight, 8);
				break;
			}
			case 11: {
				this.apply_type11(this._fighter, this._fight);
				break;
			}
			case 12: {
				this.apply_type12(this._fighter, this._fight, 5);
				break;
			}
			case 13: {
				this.apply_type13(this._fighter, this._fight, 5);
				break;
			}
			case 14: {
				this.apply_type14(this._fighter, this._fight, 5);
				break;
			}
			case 15: {
				this.apply_type15(this._fighter, this._fight, 5);
				break;
			}
			case 16: {
				this.apply_type16(this._fighter, this._fight, 8);
				break;
			}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.endTurn();
				}
			}, 1000L, TimeUnit.MILLISECONDS);
		}
	}

	private void endTurn() {
		if (this.stop && !this._fighter.isDead()) {
			if (this.haveInvok) {
				sheduler.schedule(new Runnable() {
					@Override
					public void run() {
						IAThread.this._fight.endTurn(false, IAThread.this._fighter);
					}
				}, 1000L, TimeUnit.MILLISECONDS);
			} else {
				this._fight.endTurn(false, this._fighter);
			}
		} else {
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.endTurn();
				}
			}, 500L, TimeUnit.MILLISECONDS);
		}
	}

	private void apply_type0(final Fighter F, final Fight fight) {
		this.stop = true;
	}

	private void apply_type1(final Fighter F, final Fight fight, final int count) {
		long timePM = 0L;
		if (F.canPlay() && count > 0 && !this.stop) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			final Fighter T2 = this.getNearestFriend(fight, F);
			if (T == null) {
				this.stop = true;
				return;
			}
			if (!this.HealIfPossible(fight, F, true)) {
				final int val = this.moveToAttackIfPossible(fight, F);
				final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
				final int cellID = val - val / 1000 * 1000;
				if (cellID != -1) {
					if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
						final int path = Pathfinding
								.getShortestPathBetween(fight.getMap(), F.getCell().getId(), cellID, F.getCurPm(fight))
								.size();
						if (path > 0) {
							timePM = 600 + path * 100;
						}
						fight.tryCastSpell(F, SS, cellID);
					} else {
						timePM = this._apply_type1(F, fight, T, T2);
					}
				} else {
					timePM = this._apply_type1(F, fight, T, T2);
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type1(F, fight, count - 1);
				}
			}, 200L + timePM, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private long _apply_type1(final Fighter F, final Fight fight, final Fighter T, final Fighter T2) {
		long timePM = 0L;
		if (!this.buffIfPossible(fight, F, F) && !this.HealIfPossible(fight, F, false)
				&& !this.buffIfPossible(fight, F, T2) && !this.invocIfPossible(fight, F)) {
			final int usePM = F.getCurPm(fight);
			if (usePM > 0) {
				timePM = 600 + usePM * 100;
			}
			if (!this.moveNearIfPossible(fight, F, T)) {
				timePM = 0L;
				this.stop = true;
			}
		}
		return timePM;
	}

	private void apply_type2(final Fighter F, final Fight fight, final int count) {
		if (!this.stop && F.canPlay() && count > 0) {
			Fighter T = this.getNearestFriend(fight, F);
			if (!this.HealIfPossible(fight, F, false) && !this.buffIfPossible(fight, F, T)
					&& !this.moveNearIfPossible(fight, F, T) && !this.HealIfPossible(fight, F, true)
					&& !this.buffIfPossible(fight, F, F) && !this.invocIfPossible(fight, F)) {
				T = this.getNearestEnnemy(fight, F);
				final int attack = this.attackIfPossible(fight, F);
				if (attack != 0 && this.moveToAttackIfPossible(fight, F) != -1) {
					if (attack == 5) {
						this.stop = true;
					}
					if (!this.moveFarIfPossible(fight, F)) {
						this.stop = true;
					}
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type2(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type3(final Fighter F, final Fight fight, final int count) {
		if (!this.stop && F.canPlay() && count > 0) {
			final Fighter T = this.getNearestFriendNoInvok(fight, F);
			if (!this.moveNearIfPossible(fight, F, T) && !this.buffIfPossible(fight, F, T)
					&& !this.HealIfPossible(fight, F, false) && !this.HealIfPossible(fight, F, true)
					&& !this.invocIfPossible(fight, F) && !this.buffIfPossible(fight, F, F)) {
				this.stop = true;
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type3(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type4(final Fighter F, final Fight fight, final int count) {
		long timePM = 0L;
		if (!this.stop && F.canPlay() && count > 0) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			final Fighter T2 = this.getNearestFriend(fight, F);
			if (T == null) {
				return;
			}
			final int attack = this.attackIfPossible(fight, F);
			if (attack != 0) {
				if (!this.HealIfPossible(fight, F, false) && !this.buffIfPossible(fight, F, T2)
						&& !this.HealIfPossible(fight, F, true) && !this.invocIfPossible(fight, F)
						&& !this.buffIfPossible(fight, F, F)) {
					final int usePM = F.getCurPm(fight);
					if (usePM > 0) {
						timePM = 600 + usePM * 100;
					}
					if (this.haveAttacted) {
						if (!this.moveFarIfPossible(fight, F)) {
							timePM = 0L;
							this.stop = true;
						}
					} else if (!this.moveNearIfPossible(fight, F, T)) {
						timePM = 0L;
						this.stop = true;
					}
				}
			} else {
				this.haveAttacted = true;
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type4(F, fight, count - 1);
				}
			}, 200L + timePM, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type5(final Fighter F, final Fight fight, final int count) {
		if (!this.stop && F.canPlay() && count > 0) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			if (T == null) {
				return;
			}
			if (!this.moveNearIfPossible(fight, F, T)) {
				this.stop = true;
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type5(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type6(final Fighter F, final Fight fight, final int count) {
		if (!this.stop && F.canPlay() && count > 0) {
			if (!this.invocIfPossible(fight, F)) {
				final Fighter T = this.getNearestFriend(fight, F);
				if (!this.HealIfPossible(fight, F, false) && !this.buffIfPossible(fight, F, T)
						&& !this.buffIfPossible(fight, F, F) && !this.HealIfPossible(fight, F, true)) {
					final int attack = this.attackIfPossible(fight, F);
					if (attack != 0) {
						if (attack == 5) {
							this.stop = true;
						}
						if (!this.moveFarIfPossible(fight, F)) {
							this.stop = true;
						}
					}
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type6(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void setAttacted() {
		this.haveAttacted = true;
	}

	private boolean getAttacted() {
		return this.haveAttacted;
	}

	private void apply_type7(final Fighter fighter, final Fight fight, final int count) {
		if (!this.buffIfPossible(fight, fighter, fighter)
				&& !this.buffIfPossible(fight, fighter, this.getNearestFriend(fight, fighter))
				&& !this.invocIfPossible(fight, fighter)) {
			if (count != 4) {
				sheduler.schedule(new Runnable() {
					@Override
					public void run() {
						IAThread.this.apply_type7_2(fighter, fight, 4);
					}
				}, 200L, TimeUnit.MILLISECONDS);
				return;
			}
			this.apply_type7_2(fighter, fight, 4);
		}
		sheduler.schedule(new Runnable() {
			@Override
			public void run() {
				IAThread.this.apply_type7(fighter, fight, count - 1);
			}
		}, 200L, TimeUnit.MILLISECONDS);
	}

	private void apply_type7_2(final Fighter fighter, final Fight fight, final int count) {
		final int val = this.moveToAttackIfPossible(fight, fighter);
		final Spell.SortStats SS = fighter.getMob().getSpells().get(val / 1000);
		final int cellID = val - val / 1000 * 1000;
		if (cellID != -1) {
			if (fight.canCastSpell1(fighter, SS, fighter.getCell(), cellID)) {
				final int path = Pathfinding.getShortestPathBetween(fight.getMap(), fighter.getCell().getId(), cellID,
						fighter.getCurPm(fight)).size();
				long timePM = 0L;
				if (path > 0) {
					timePM = 600 + path * 100;
				}
				this.setAttacted();
				fight.tryCastSpell(fighter, SS, cellID);
				sheduler.schedule(new Runnable() {
					@Override
					public void run() {
						IAThread.this.apply_type7_2(fighter, fight, count - 1);
					}
				}, 200L + timePM, TimeUnit.MILLISECONDS);
			} else if (count == 4) {
				this.apply_type7_3(fighter, fight);
			} else {
				sheduler.schedule(new Runnable() {
					@Override
					public void run() {
						IAThread.this.apply_type7_3(fighter, fight);
					}
				}, 200L, TimeUnit.MILLISECONDS);
			}
		} else if (count == 4) {
			this.apply_type7_3(fighter, fight);
		} else {
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type7_3(fighter, fight);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		}
	}

	private void apply_type7_3(final Fighter fighter, final Fight fight) {
		final int usePM = fighter.getCurPm(fight);
		long timePM = 0L;
		if (usePM > 0) {
			timePM = 600 + usePM * 100;
		}
		if (!this.getAttacted()) {
			this.moveNearIfPossible(fight, fighter, this.getNearestEnnemy(fight, fighter));
		} else {
			this.moveFarIfPossible(fight, fighter);
		}
		sheduler.schedule(new Runnable() {
			@Override
			public void run() {
				IAThread.access$12(IAThread.this, true);
			}
		}, timePM, TimeUnit.MILLISECONDS);
	}

	private void apply_type8(final Fighter F, final Fight fight, final int count) {
		long timePM = 0L;
		if (!this.stop && F.canPlay() && count > 0) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			final Fighter T2 = this.getNearestFriendInvoc(fight, F);
			if (T == null) {
				return;
			}
			if (!this.invocIfPossible(fight, F) && !this.buffIfPossible(fight, F, T2)) {
				final int usePM = F.getCurPm(fight);
				if (usePM > 0) {
					timePM = 600 + usePM * 100;
				}
				if (!this.moveFarIfPossible(fight, F)) {
					timePM = 0L;
					this.stop = true;
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type8(F, fight, count - 1);
				}
			}, 200L + timePM, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type9(final Fighter F, final Fight fight, final int count) {
		if (count > 0 && F.canPlay() && !this.stop) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			if (T == null) {
				return;
			}
			final int val = this.moveToAttackIfPossible(fight, F);
			final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
			final int cellID = val - val / 1000 * 1000;
			if (cellID != -1) {
				if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
					fight.tryCastSpell(F, SS, cellID);
				}
			} else if (!this.moveFarIfPossible(fight, F)) {
				this.stop = true;
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type9(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type10(final Fighter F, final Fight fight, final int count) {
		if (count > 0 && F.canPlay() && !this.stop) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			if (T == null) {
				return;
			}
			final int val = this.moveToAttackIfPossible(fight, F);
			final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
			final int cellID = val - val / 1000 * 1000;
			if (cellID != -1) {
				if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
					fight.tryCastSpell(F, SS, cellID);
				}
			} else if (F.haveState(8)) {
				if (!this.HealIfPossible(fight, F, true) && !this.HealIfPossible(fight, F, false)) {
					this.stop = true;
				}
			} else {
				this.stop = true;
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type10(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type11(final Fighter F, final Fight fight) {
		final Fighter T = this.getNearestEnnemy(fight, F);
		int val = this.moveToAttackIfPossible(fight, F);
		final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
		final int cellID = val - val / 1000 * 1000;
		if (cellID != -1) {
			final int cell = cellID;
			final Spell.SortStats sort = SS;
			if (fight.canCastSpell1(F, sort, F.getCell(), cell)) {
				sheduler.schedule(new Runnable() {
					@Override
					public void run() {
						fight.tryCastSpell(F, sort, cell);
					}
				}, 200L, TimeUnit.MILLISECONDS);
			}
		} else {
			val = this.moveToAttackIfPossibleAll(fight, F);
			final Spell.SortStats sort2 = F.getMob().getSpells().get(val / 1000);
			final int cell2 = val - val / 1000 * 1000;
			if (cell2 != -1 && fight.canCastSpell1(F, sort2, F.getCell(), cell2)) {
				sheduler.schedule(new Runnable() {
					@Override
					public void run() {
						fight.tryCastSpell(F, sort2, cell2);
					}
				}, 200L, TimeUnit.MILLISECONDS);
			}
		}
		final int usePM = F.getCurPm(fight);
		long timePM = 0L;
		if (usePM > 0) {
			timePM = 600 + usePM * 100;
		}
		sheduler.schedule(new Runnable() {
			@Override
			public void run() {
				IAThread.this.moveNearIfPossible(fight, F, T);
			}
		}, 200L, TimeUnit.MILLISECONDS);
		sheduler.schedule(new Runnable() {
			@Override
			public void run() {
				IAThread.access$12(IAThread.this, true);
			}
		}, timePM, TimeUnit.MILLISECONDS);
	}

	private void apply_type12(final Fighter fighter, final Fight fight, final int count) {
		int cellID = 1;
		if (fighter.getPa() > 4 && cellID != -1 && fighter.canPlay() && !this.stop && count > 0) {
			final int val = this.moveToAttackIfPossible(fight, fighter);
			final Spell.SortStats SS = fighter.getMob().getSpells().get(val / 1000);
			cellID = val - val / 1000 * 1000;
			if (cellID != -1 && fight.canCastSpell1(fighter, SS, fighter.getCell(), cellID)) {
				fight.tryCastSpell(fighter, SS, cellID);
				this.setAttacted();
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type12(fighter, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			final int usePM = fighter.getCurPm(fight);
			long timePM = 0L;
			if (usePM > 0) {
				timePM = 600 + usePM * 100;
			}
			if (this.getAttacted()) {
				this.moveFarIfPossible(fight, fighter);
			} else {
				this.moveNearIfPossible(fight, fighter, this.getNearestEnnemy(fight, fighter));
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.access$12(IAThread.this, true);
				}
			}, timePM, TimeUnit.MILLISECONDS);
		}
	}

	private void apply_type13(final Fighter F, final Fight fight, final int count) {
		boolean attaque = true;
		if (count > 0 && F.canPlay() && !this.stop) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			if (T == null) {
				return;
			}
			if (!this.buffIfPossible(fight, F, F)) {
				final int val = this.moveToAttackIfPossibleRandom(fight, F);
				final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
				final int cellID = val - val / 1000 * 1000;
				if (cellID != -1 && attaque) {
					if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
						attaque = true;
						fight.tryCastSpell(F, SS, cellID);
					} else {
						attaque = false;
					}
				} else if (!this.moveNearIfPossible(fight, F, T)) {
					this.stop = true;
				}
				if (!attaque) {
					attaque = true;
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type13(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type14(final Fighter F, final Fight fight, final int count) {
		if (count > 0 && F.canPlay() && !this.stop) {
			if (!F.haveInvocation()) {
				if (!this.invocIfPossible(fight, F)) {
					final Fighter T = this.getNearestEnnemy(fight, F);
					final int val = this.moveToAttackIfPossible(fight, F);
					final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
					final int cellID = val - val / 1000 * 1000;
					if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
						fight.tryCastSpell(F, SS, cellID);
					} else {
						this.moveNearIfPossible(fight, F, T);
					}
				}
			} else {
				final Fighter T = this.getNearestEnnemy(fight, F);
				final int val = this.moveToAttackIfPossible(fight, F);
				final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
				final int cellID = val - val / 1000 * 1000;
				if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
					fight.tryCastSpell(F, SS, cellID);
				} else {
					this.moveNearIfPossible(fight, F, T);
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type14(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type15(final Fighter F, final Fight fight, final int count) {
		boolean attaque = true;
		if (count > 0 && F.canPlay() && !this.stop) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			final Fighter Fi = this.getNearestFriend(fight, F);
			if (T == null) {
				return;
			}
			if (!this.buffIfPossible(fight, F, F) && !this.buffIfPossible(fight, F, Fi)) {
				final int val = this.moveToAttackIfPossible(fight, F);
				final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
				final int cellID = val - val / 1000 * 1000;
				if (cellID != -1 && attaque) {
					if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
						attaque = true;
						fight.tryCastSpell(F, SS, cellID);
					} else {
						attaque = false;
						if (!this.moveNearIfPossible(fight, F, T)) {
							this.stop = true;
						}
					}
				} else if (!this.moveNearIfPossible(fight, F, T)) {
					this.stop = true;
				}
				if (!attaque) {
					attaque = true;
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type15(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_type16(final Fighter F, final Fight fight, final int count) {
		boolean attaque = true;
		if (count > 0 && F.canPlay() && !this.stop) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			if (T == null) {
				return;
			}
			final int val = this.moveToAttackIfPossible(fight, F);
			final Spell.SortStats SS = F.getMob().getSpells().get(val / 1000);
			final int cellID = val - val / 1000 * 1000;
			if (cellID != -1 && attaque) {
				if (fight.canCastSpell1(F, SS, F.getCell(), cellID)) {
					attaque = true;
					fight.tryCastSpell(F, SS, cellID);
				} else {
					attaque = false;
					if (!this.moveNearIfPossible(fight, F, T) && !this.invocIfPossible(fight, F)) {
						this.stop = true;
					}
				}
			} else if (!this.moveNearIfPossible(fight, F, T) && !this.invocIfPossible(fight, F)) {
				this.stop = true;
			}
			if (!attaque) {
				attaque = true;
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_type16(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private void apply_typePerco(final Fighter F, final Fight fight, final int count) {
		if (!this.stop && F.canPlay() && count > 0) {
			final Fighter T = this.getNearestEnnemy(fight, F);
			if (T == null) {
				return;
			}
			final int attack = this.attackIfPossible(fight, F);
			if (attack != 0) {
				if (attack == 5) {
					this.stop = true;
				}
				if (!this.moveFarIfPossible(fight, F) && !this.HealIfPossible(fight, F, false)
						&& !this.buffIfPossible(fight, F, T) && !this.HealIfPossible(fight, F, true)
						&& !this.buffIfPossible(fight, F, F)) {
					this.stop = true;
				}
			}
			sheduler.schedule(new Runnable() {
				@Override
				public void run() {
					IAThread.this.apply_typePerco(F, fight, count - 1);
				}
			}, 200L, TimeUnit.MILLISECONDS);
		} else {
			this.stop = true;
		}
	}

	private boolean moveFarIfPossible(final Fight fight, final Fighter F) {
		if (fight == null || F == null) {
			return false;
		}
		if (fight.getMap() == null) {
			return false;
		}
		final int[] dist = { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 };
		final int[] cell = new int[10];
		for (int i = 0; i < 10; ++i) {
			for (final Fighter f : fight.getFighters(3)) {
				if (f.isDead()) {
					continue;
				}
				if (f == F) {
					continue;
				}
				if (f.getTeam() == F.getTeam()) {
					continue;
				}
				final int cellf = f.getCell().getId();
				if (cellf == cell[0] || cellf == cell[1] || cellf == cell[2] || cellf == cell[3] || cellf == cell[4]
						|| cellf == cell[5] || cellf == cell[6] || cellf == cell[7] || cellf == cell[8]) {
					continue;
				}
				if (cellf == cell[9]) {
					continue;
				}
				int d = 0;
				d = Pathfinding.getDistanceBetween(fight.getMap(), F.getCell().getId(), f.getCell().getId());
				if (d < dist[i]) {
					dist[i] = d;
					cell[i] = cellf;
				}
				if (dist[i] != 1000) {
					continue;
				}
				dist[i] = 0;
				cell[i] = F.getCell().getId();
			}
		}
		final int[] dist2 = new int[10];
		final int PM = F.getCurPm(fight);
		int caseDepart = F.getCell().getId();
		int destCase = F.getCell().getId();
		final ArrayList<Integer> caseUse = new ArrayList<Integer>();
		caseUse.add(caseDepart);
		for (int j = 0; j <= PM; ++j) {
			if (destCase > 0) {
				caseDepart = destCase;
			}
			int curCase = caseDepart;
			curCase += 15;
			int infl = 0;
			int inflF = 0;
			for (int a = 0; a < 10 && dist[a] != 0; ++a) {
				dist2[a] = Pathfinding.getDistanceBetween(fight.getMap(), curCase, cell[a]);
				if (dist2[a] > dist[a]) {
					++infl;
				}
			}
			if (infl > inflF && curCase >= 15 && curCase <= 463 && this.testCotes(destCase, curCase)
					&& fight.getMap().getCase(curCase).isWalkable(false, true, -1)
					&& fight.getMap().getCase(curCase).getFighters().isEmpty() && !caseUse.contains(curCase)) {
				inflF = infl;
				destCase = curCase;
			}
			curCase = caseDepart + 14;
			infl = 0;
			for (int a = 0; a < 10 && dist[a] != 0; ++a) {
				dist2[a] = Pathfinding.getDistanceBetween(fight.getMap(), curCase, cell[a]);
				if (dist2[a] > dist[a]) {
					++infl;
				}
			}
			if (infl > inflF && curCase >= 15 && curCase <= 463 && this.testCotes(destCase, curCase)
					&& fight.getMap().getCase(curCase).isWalkable(false, true, -1)
					&& fight.getMap().getCase(curCase).getFighters().isEmpty() && !caseUse.contains(curCase)) {
				inflF = infl;
				destCase = curCase;
			}
			curCase = caseDepart - 15;
			infl = 0;
			for (int a = 0; a < 10 && dist[a] != 0; ++a) {
				dist2[a] = Pathfinding.getDistanceBetween(fight.getMap(), curCase, cell[a]);
				if (dist2[a] > dist[a]) {
					++infl;
				}
			}
			if (infl > inflF && curCase >= 15 && curCase <= 463 && this.testCotes(destCase, curCase)
					&& fight.getMap().getCase(curCase).isWalkable(false, true, -1)
					&& fight.getMap().getCase(curCase).getFighters().isEmpty() && !caseUse.contains(curCase)) {
				inflF = infl;
				destCase = curCase;
			}
			curCase = caseDepart - 14;
			infl = 0;
			for (int a = 0; a < 10 && dist[a] != 0; ++a) {
				dist2[a] = Pathfinding.getDistanceBetween(fight.getMap(), curCase, cell[a]);
				if (dist2[a] > dist[a]) {
					++infl;
				}
			}
			if (infl > inflF && curCase >= 15 && curCase <= 463 && this.testCotes(destCase, curCase)
					&& fight.getMap().getCase(curCase).isWalkable(false, true, -1)
					&& fight.getMap().getCase(curCase).getFighters().isEmpty() && !caseUse.contains(curCase)) {
				inflF = infl;
				destCase = curCase;
			}
			caseUse.add(destCase);
		}
		if (destCase < 15 || destCase > 463 || destCase == F.getCell().getId()
				|| !fight.getMap().getCase(destCase).isWalkable(false, true, -1)) {
			return false;
		}
		if (F.getPm() <= 0) {
			return false;
		}
		final ArrayList<Case> path = new AstarPathfinding(fight.getMap(), fight, F.getCell().getId(), destCase)
				.getShortestPath(-1);
		if (path == null) {
			return false;
		}
		final ArrayList<Case> finalPath = new ArrayList<Case>();
		for (int a2 = 0; a2 < F.getCurPm(fight) && path.size() != a2; ++a2) {
			finalPath.add(path.get(a2));
		}
		String pathstr = "";
		try {
			int curCaseID = F.getCell().getId();
			final int curDir = 0;
			for (final Case c : finalPath) {
				final char d2 = Pathfinding.getDirBetweenTwoCase(curCaseID, c.getId(), fight.getMap(), true);
				if (d2 == '\0') {
					return false;
				}
				if (curDir != d2) {
					if (finalPath.indexOf(c) != 0) {
						pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
					}
					pathstr = String.valueOf(pathstr) + d2;
				}
				curCaseID = c.getId();
			}
			if (curCaseID != F.getCell().getId()) {
				pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final GameAction GA = new GameAction(0, 1, "");
		GA.args = pathstr;
		final boolean result = fight.onFighterDeplace(F, GA);
		return result;
	}

	private boolean testCotes(final int cellWeAre, final int cellWego) {
		return ((cellWeAre != 15 && cellWeAre != 44 && cellWeAre != 73 && cellWeAre != 102 && cellWeAre != 131
				&& cellWeAre != 160 && cellWeAre != 189 && cellWeAre != 218 && cellWeAre != 247 && cellWeAre != 276
				&& cellWeAre != 305 && cellWeAre != 334 && cellWeAre != 363 && cellWeAre != 392 && cellWeAre != 421
				&& cellWeAre != 450) || (cellWego != cellWeAre + 14 && cellWego != cellWeAre - 15))
				&& ((cellWeAre != 28 && cellWeAre != 57 && cellWeAre != 86 && cellWeAre != 115 && cellWeAre != 144
						&& cellWeAre != 173 && cellWeAre != 202 && cellWeAre != 231 && cellWeAre != 260
						&& cellWeAre != 289 && cellWeAre != 318 && cellWeAre != 347 && cellWeAre != 376
						&& cellWeAre != 405 && cellWeAre != 434 && cellWeAre != 463)
						|| (cellWego != cellWeAre + 15 && cellWego != cellWeAre - 14))
				&& (cellWeAre < 451 || cellWeAre > 462 || (cellWego != cellWeAre + 15 && cellWego != cellWeAre + 14))
				&& (cellWeAre < 16 || cellWeAre > 27 || (cellWego != cellWeAre - 15 && cellWego != cellWeAre - 14));
	}

	private boolean invocIfPossible(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return false;
		}
		if (fighter.nbInvocation() >= fighter.getTotalStats().getEffect(182)) {
			return false;
		}
		final Fighter nearest = this.getNearestEnnemy(fight, fighter);
		if (nearest == null) {
			return false;
		}
		int nearestCell = fighter.getCell().getId();
		final int limit = 30;
		int _loc0_ = 0;
		Spell.SortStats spell = null;
		while ((spell = this.getInvocSpell(fight, fighter, nearestCell)) == null && _loc0_++ < limit) {
			nearestCell = Pathfinding.getNearestCellAround(fight.getMap(), nearestCell, nearest.getCell().getId(),
					null);
		}
		if (nearestCell == -1) {
			return false;
		}
		if (spell == null) {
			return false;
		}
		final int invoc = fight.tryCastSpell(fighter, spell, nearestCell);
		return invoc == 0 && (this.haveInvok = true);
	}

	private Spell.SortStats getInvocSpell(final Fight fight, final Fighter fighter, final int nearestCell) {
		if (fight == null || fighter == null) {
			return null;
		}
		if (fighter.getMob() == null) {
			return null;
		}
		if (fight.getMap() == null) {
			return null;
		}
		if (fight.getMap().getCase(nearestCell) == null) {
			return null;
		}
		for (final Map.Entry<Integer, Spell.SortStats> SS : fighter.getMob().getSpells().entrySet()) {
			if (!fight.canCastSpell1(fighter, SS.getValue(), fight.getMap().getCase(nearestCell), -1)) {
				continue;
			}
			for (final SpellEffect SE : SS.getValue().getEffects()) {
				if (SE.getEffectID() == 181) {
					return SS.getValue();
				}
			}
		}
		return null;
	}

	private boolean HealIfPossible(final Fight fight, final Fighter f, final boolean autoSoin) {
		if (fight == null || f == null) {
			return false;
		}
		if (f.isDead()) {
			return false;
		}
		if (autoSoin && f.getPdv() * 100 / f.getPdvMax() > 95) {
			return false;
		}
		Fighter target = null;
		Spell.SortStats SS = null;
		if (autoSoin) {
			target = f;
			SS = this.getHealSpell(fight, f, target);
		} else {
			Fighter curF = null;
			int PDVPERmin = 100;
			Spell.SortStats curSS = null;
			for (final Fighter F : fight.getFighters(3)) {
				if (f.isDead()) {
					continue;
				}
				if (F == f) {
					continue;
				}
				if (F.isDead()) {
					continue;
				}
				if (F.getTeam() != f.getTeam()) {
					continue;
				}
				final int PDVPER = F.getPdv() * 100 / F.getPdvMax();
				if (PDVPER >= PDVPERmin || PDVPER >= 95) {
					continue;
				}
				int infl = 0;
				if (f.isCollector()) {
					for (final Map.Entry<Integer, Spell.SortStats> ss : World.getGuild(f.getCollector().getGuildId())
							.getSpells().entrySet()) {
						if (ss.getValue() == null) {
							continue;
						}
						if (infl >= this.calculInfluenceHeal(ss.getValue())
								|| this.calculInfluenceHeal(ss.getValue()) == 0
								|| !fight.canCastSpell1(f, ss.getValue(), F.getCell(), -1)) {
							continue;
						}
						infl = this.calculInfluenceHeal(ss.getValue());
						curSS = ss.getValue();
					}
				} else {
					for (final Map.Entry<Integer, Spell.SortStats> ss : f.getMob().getSpells().entrySet()) {
						if (infl < this.calculInfluenceHeal(ss.getValue())
								&& this.calculInfluenceHeal(ss.getValue()) != 0
								&& fight.canCastSpell1(f, ss.getValue(), F.getCell(), -1)) {
							infl = this.calculInfluenceHeal(ss.getValue());
							curSS = ss.getValue();
						}
					}
				}
				if (curSS == SS || curSS == null) {
					continue;
				}
				curF = F;
				SS = curSS;
				PDVPERmin = PDVPER;
			}
			target = curF;
		}
		if (target == null) {
			return false;
		}
		if (target.isFullPdv()) {
			return false;
		}
		if (SS == null) {
			return false;
		}
		final int heal = fight.tryCastSpell(f, SS, target.getCell().getId());
		return heal == 0;
	}

	private boolean buffIfPossible(final Fight fight, final Fighter fighter, final Fighter target) {
		if (fight == null || fighter == null) {
			return false;
		}
		if (target == null) {
			return false;
		}
		final Spell.SortStats SS = this.getBuffSpell(fight, fighter, target);
		if (SS == null) {
			return false;
		}
		final int buff = fight.tryCastSpell(fighter, SS, target.getCell().getId());
		return buff == 0;
	}

	private Spell.SortStats getBuffSpell(final Fight fight, final Fighter F, final Fighter T) {
		if (fight == null || F == null) {
			return null;
		}
		int infl = -1500000;
		Spell.SortStats ss = null;
		if (F.isCollector()) {
			for (final Map.Entry<Integer, Spell.SortStats> SS : World.getGuild(F.getCollector().getGuildId())
					.getSpells().entrySet()) {
				if (SS.getValue() == null) {
					continue;
				}
				if (infl >= this.calculInfluence(SS.getValue(), F, T) || this.calculInfluence(SS.getValue(), F, T) <= 0
						|| !fight.canCastSpell1(F, SS.getValue(), T.getCell(), -1)) {
					continue;
				}
				infl = this.calculInfluence(SS.getValue(), F, T);
				ss = SS.getValue();
			}
		} else {
			for (final Map.Entry<Integer, Spell.SortStats> SS : F.getMob().getSpells().entrySet()) {
				final int inf = this.calculInfluence(SS.getValue(), F, T);
				if (infl < inf && SS.getValue().getSpell().getType() == 1
						&& fight.canCastSpell1(F, SS.getValue(), T.getCell(), -1)) {
					infl = this.calculInfluence(SS.getValue(), F, T);
					ss = SS.getValue();
				}
			}
		}
		return ss;
	}

	private Spell.SortStats getHealSpell(final Fight fight, final Fighter F, final Fighter T) {
		if (fight == null || F == null) {
			return null;
		}
		int infl = 0;
		Spell.SortStats ss = null;
		if (F.isCollector()) {
			for (final Map.Entry<Integer, Spell.SortStats> SS : World.getGuild(F.getCollector().getGuildId())
					.getSpells().entrySet()) {
				if (SS.getValue() == null) {
					continue;
				}
				if (infl >= this.calculInfluenceHeal(SS.getValue()) || this.calculInfluenceHeal(SS.getValue()) == 0
						|| !fight.canCastSpell1(F, SS.getValue(), T.getCell(), -1)) {
					continue;
				}
				infl = this.calculInfluenceHeal(SS.getValue());
				ss = SS.getValue();
			}
		} else {
			for (final Map.Entry<Integer, Spell.SortStats> SS : F.getMob().getSpells().entrySet()) {
				if (SS.getValue() == null) {
					continue;
				}
				if (infl >= this.calculInfluenceHeal(SS.getValue()) || this.calculInfluenceHeal(SS.getValue()) == 0
						|| !fight.canCastSpell1(F, SS.getValue(), T.getCell(), -1)) {
					continue;
				}
				infl = this.calculInfluenceHeal(SS.getValue());
				ss = SS.getValue();
			}
		}
		return ss;
	}

	private boolean moveNearIfPossible(final Fight fight, final Fighter F, final Fighter T) {
		if (fight == null) {
			return false;
		}
		if (F == null) {
			return false;
		}
		if (T == null) {
			return false;
		}
		if (F.getCurPm(fight) <= 0) {
			return false;
		}
		final org.aestia.map.Map map = fight.getMap();
		if (map == null) {
			return false;
		}
		final Case cell = F.getCell();
		if (cell == null) {
			return false;
		}
		final Case cell2 = T.getCell();
		if (cell2 == null) {
			return false;
		}
		if (Pathfinding.isNextTo(map, cell.getId(), cell2.getId())) {
			return false;
		}
		int cellID = Pathfinding.getNearestCellAround(map, cell2.getId(), cell.getId(), null);
		if (cellID == -1) {
			final Map<Integer, Fighter> ennemys = this.getLowHpEnnemyList(fight, F);
			for (final Map.Entry<Integer, Fighter> target : ennemys.entrySet()) {
				final int cellID2 = Pathfinding.getNearestCellAround(map, target.getValue().getCell().getId(),
						cell.getId(), null);
				if (cellID2 != -1) {
					cellID = cellID2;
					break;
				}
			}
		}
		final ArrayList<Case> path = new AstarPathfinding(fight.getMapOld(), fight, cell.getId(), cell2.getId())
				.getShortestPath(-1);
		if (path == null || path.isEmpty()) {
			return false;
		}
		if (Main.modDebug) {
			Console.println("DEBUG PATHFINDING:", Console.Color.INFORMATION);
			Console.println("startCell:" + cell.getId(), Console.Color.INFORMATION);
			Console.println("destinationCell:" + cellID, Console.Color.INFORMATION);
			for (final Case c : path) {
				Console.println("Passage par cellID: " + c.getId() + " walk: " + c.isWalkable(true),
						Console.Color.INFORMATION);
			}
		}
		final ArrayList<Case> finalPath = new ArrayList<Case>();
		for (int a = 0; a < F.getCurPm(fight) && path.size() != a; ++a) {
			finalPath.add(path.get(a));
		}
		String pathstr = "";
		try {
			int curCaseID = cell.getId();
			final int curDir = 0;
			for (final Case c2 : finalPath) {
				final char d = Pathfinding.getDirBetweenTwoCase(curCaseID, c2.getId(), map, true);
				if (d == '\0') {
					return false;
				}
				if (curDir != d) {
					if (finalPath.indexOf(c2) != 0) {
						pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
					}
					pathstr = String.valueOf(pathstr) + d;
				}
				curCaseID = c2.getId();
			}
			if (curCaseID != cell.getId()) {
				pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final GameAction GA = new GameAction(0, 1, "");
		GA.args = pathstr;
		final boolean result = fight.onFighterDeplace(F, GA);
		return result;
	}

	private Fighter getNearestFriendInvoc(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return null;
		}
		int dist = 1000;
		Fighter curF = null;
		for (final Fighter f : fight.getFighters(3)) {
			if (f.isDead()) {
				continue;
			}
			if (f == fighter) {
				continue;
			}
			if (f.getTeam2() != fighter.getTeam2() || !f.isInvocation()) {
				continue;
			}
			final int d = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
					f.getCell().getId());
			if (d >= dist) {
				continue;
			}
			dist = d;
			curF = f;
		}
		return curF;
	}

	private Fighter getNearestFriendNoInvok(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return null;
		}
		int dist = 1000;
		Fighter curF = null;
		for (final Fighter f : fight.getFighters(3)) {
			if (f.isDead()) {
				continue;
			}
			if (f == fighter) {
				continue;
			}
			if (f.getTeam2() != fighter.getTeam2() || f.isInvocation()) {
				continue;
			}
			final int d = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
					f.getCell().getId());
			if (d >= dist) {
				continue;
			}
			dist = d;
			curF = f;
		}
		return curF;
	}

	private Fighter getNearest(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return null;
		}
		int dist = 1000;
		Fighter curF = null;
		for (final Fighter f : fight.getFighters(3)) {
			if (f.isDead()) {
				continue;
			}
			if (f == fighter) {
				continue;
			}
			final int d = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
					f.getCell().getId());
			if (d >= dist) {
				continue;
			}
			dist = d;
			curF = f;
		}
		return curF;
	}

	private Fighter getNearestFriend(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return null;
		}
		int dist = 1000;
		Fighter curF = null;
		for (final Fighter f : fight.getFighters(3)) {
			if (f.isDead()) {
				continue;
			}
			if (f == fighter) {
				continue;
			}
			if (f.getTeam2() != fighter.getTeam2()) {
				continue;
			}
			final int d = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
					f.getCell().getId());
			if (d >= dist) {
				continue;
			}
			dist = d;
			curF = f;
		}
		return curF;
	}

	private Fighter getNearestEnnemy(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return null;
		}
		int dist = 1000;
		Fighter curF = null;
		for (final Fighter f : fight.getFighters(3)) {
			if (f.isDead()) {
				continue;
			}
			if (f.getTeam2() == fighter.getTeam2()) {
				continue;
			}
			final int d = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
					f.getCell().getId());
			if (d >= dist) {
				continue;
			}
			dist = d;
			curF = f;
		}
		return curF;
	}

	private Map<Integer, Fighter> getLowHpEnnemyList(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return null;
		}
		final Map<Integer, Fighter> list = new TreeMap<Integer, Fighter>();
		final Map<Integer, Fighter> ennemy = new TreeMap<Integer, Fighter>();
		for (final Fighter f : fight.getFighters(3)) {
			if (f.isDead()) {
				continue;
			}
			if (f == fighter) {
				continue;
			}
			if (f.getTeam2() == fighter.getTeam2()) {
				continue;
			}
			ennemy.put(f.getId(), f);
		}
		int i = 0;
		final int i2 = ennemy.size();
		int curHP = 10000;
		Fighter curEnnemy = null;
		while (i < i2) {
			curHP = 200000;
			curEnnemy = null;
			for (final Map.Entry<Integer, Fighter> t : ennemy.entrySet()) {
				if (t.getValue().getPdv() < curHP) {
					curHP = t.getValue().getPdv();
					curEnnemy = t.getValue();
				}
			}
			list.put(curEnnemy.getId(), curEnnemy);
			ennemy.remove(curEnnemy.getId());
			++i;
		}
		return list;
	}

	private int attackIfPossible(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return 0;
		}
		final Map<Integer, Fighter> ennemyList = this.getLowHpEnnemyList(fight, fighter);
		Spell.SortStats SS = null;
		Fighter target = null;
		for (final Map.Entry<Integer, Fighter> t : ennemyList.entrySet()) {
			SS = this.getBestSpellForTarget(fight, fighter, t.getValue(), fighter.getCell().getId());
			if (SS != null) {
				target = t.getValue();
				break;
			}
		}
		int curTarget = 0;
		int cell = 0;
		Spell.SortStats SS2 = null;
		if (fighter.isCollector()) {
			for (final Map.Entry<Integer, Spell.SortStats> S : World.getGuild(fighter.getCollector().getGuildId())
					.getSpells().entrySet()) {
				if (S.getValue() == null) {
					continue;
				}
				final int targetVal = this.getBestTargetZone(fight, fighter, S.getValue(), fighter.getCell().getId());
				if (targetVal == -1) {
					continue;
				}
				if (targetVal == 0) {
					continue;
				}
				final int nbTarget = targetVal / 1000;
				final int cellID = targetVal - nbTarget * 1000;
				if (nbTarget <= curTarget) {
					continue;
				}
				curTarget = nbTarget;
				cell = cellID;
				SS2 = S.getValue();
			}
		} else {
			for (final Map.Entry<Integer, Spell.SortStats> S : fighter.getMob().getSpells().entrySet()) {
				final int targetVal = this.getBestTargetZone(fight, fighter, S.getValue(), fighter.getCell().getId());
				if (targetVal != -1) {
					if (targetVal == 0) {
						continue;
					}
					final int nbTarget = targetVal / 1000;
					final int cellID = targetVal - nbTarget * 1000;
					if (nbTarget <= curTarget) {
						continue;
					}
					curTarget = nbTarget;
					cell = cellID;
					SS2 = S.getValue();
				}
			}
		}
		if (curTarget > 0 && cell >= 15 && cell <= 463 && SS2 != null) {
			final int attack = fight.tryCastSpell(fighter, SS2, cell);
			if (attack != 0) {
				return attack;
			}
		} else {
			if (target == null || SS == null) {
				return 666;
			}
			final int attack = fight.tryCastSpell(fighter, SS, target.getCell().getId());
			if (attack != 0) {
				return attack;
			}
		}
		return 0;
	}

	public int moveToAction(final Fight fight, final Fighter current, Fighter target, final short action,
			final ArrayList<Integer> noSpell, final int index) {
		if (fight == null || current == null) {
			return 0;
		}
		final Map<Integer, Fighter> ennemyList = this.getLowHpEnnemyList(fight, current);
		if (current.getCurPm(fight) <= 0) {
			return 2;
		}
		boolean canAttack = false;
		final ArrayList<Case> path = new AstarPathfinding(fight.getMapOld(), fight, current.getCell().getId(),
				this.getNearestEnnemy(fight, current).getCell().getId()).getShortestPath(-1);
		int caseLaunch = -1;
		int newCase = -1;
		int bestNbTarget = 0;
		int _loc1_ = 1;
		int targetVal = 0;
		int nbTarget = 0;
		int cellID = -1;
		Case newCell = current.getCell();
		Spell.SortStats bestSort = null;
		do {
			if (newCell != null) {
				for (final Map.Entry<Integer, Fighter> t : ennemyList.entrySet()) {
					bestSort = this.getBestSpellForTarget(fight, current, t.getValue(), current.getCell().getId());
					if (bestSort != null) {
						target = t.getValue();
						break;
					}
				}
				if (target == null) {
					continue;
				}
				for (final Spell.SortStats SS : current.getMob().getSpells().values()) {
					targetVal = this.getBestTargetZone(fight, current, SS, newCell.getId());
					if (targetVal != 0) {
						nbTarget = targetVal / 1000;
						cellID = targetVal - nbTarget * 1000;
					} else {
						cellID = target.getCell().getId();
						nbTarget = 1;
					}
					if (fight.canCastSpell1(current, SS, fight.getMapOld().getCase(cellID), newCell.getId())
							&& nbTarget > bestNbTarget) {
						bestSort = SS;
						caseLaunch = cellID;
						bestNbTarget = nbTarget;
						newCase = newCell.getId();
					}
				}
			}
			newCell = path.get(_loc1_ - 1);
		} while (_loc1_++ < path.size() && _loc1_ <= current.getCurPm(fight) && !canAttack);
		if (caseLaunch != -1) {
			canAttack = true;
		} else if (newCase == -1 && index == 1) {
			return 3;
		}
		boolean result = true;
		if (newCase != current.getCell().getId()) {
			String pathstr = "";
			try {
				int curCaseID = current.getCell().getId();
				final int curDir = 0;
				for (final Case c : path) {
					if (curCaseID == c.getId()) {
						continue;
					}
					final char d = Pathfinding.getDirBetweenTwoCase(curCaseID, c.getId(), fight.getMap(), true);
					if (d == '\0') {
						return 0;
					}
					if (curDir != d) {
						if (path.indexOf(c) != 0) {
							pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
						}
						pathstr = String.valueOf(pathstr) + d;
					}
					curCaseID = c.getId();
					if (c.getId() == newCase) {
						break;
					}
				}
				if (curCaseID != current.getCell().getId()) {
					pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			final GameAction GA = new GameAction(0, 1, "");
			GA.args = pathstr;
			result = fight.onFighterDeplace(current, GA);
		}
		if (result && canAttack) {
			if (fight.canCastSpell1(current, bestSort, fight.getMapOld().getCase(caseLaunch),
					current.getCell().getId())) {
				fight.tryCastSpell(current, bestSort, caseLaunch);
				return 1;
			}
		} else if (result && !canAttack) {
			return 1;
		}
		return 3;
	}

	private int moveToAttackIfPossibleRandom(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return -1;
		}
		Fighter target = null;
		if (Math.random() < 0.67) {
			target = this.getNearestEnnemy(fight, fighter);
		} else {
			target = this.getNearestFriend(fight, fighter);
		}
		if (target == null) {
			return -1;
		}
		final int distMin = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
				target.getCell().getId());
		final ArrayList<Spell.SortStats> sorts = this.getLaunchableSort(fighter, fight, distMin);
		if (sorts == null) {
			return -1;
		}
		final ArrayList<Integer> cells = Pathfinding.getListCaseFromFighter(fight, fighter, fighter.getCell().getId(),
				sorts);
		if (cells == null) {
			return -1;
		}
		int CellDest = 0;
		Spell.SortStats bestSS = null;
		final int[] bestInvok = { 1000, 0, 0, 0, -1 };
		final int[] bestFighter = { 1000, 0, 0, 0, -1 };
		int targetCell = -1;
		for (final int i : cells) {
			for (final Spell.SortStats S : sorts) {
				if (fight.canCastSpell1(fighter, S, target.getCell(), i)) {
					final int dist = Pathfinding.getDistanceBetween(fight.getMapOld(), fighter.getCell().getId(),
							target.getCell().getId());
					if (!Pathfinding.isNextTo(fighter.getFight().getMap(), fighter.getCell().getId(),
							target.getCell().getId())) {
						if (target.isInvocation()) {
							if (dist >= bestInvok[0]) {
								continue;
							}
							bestInvok[0] = dist;
							bestInvok[1] = i;
							bestInvok[3] = (bestInvok[2] = 1);
							bestInvok[4] = target.getCell().getId();
							bestSS = S;
						} else {
							if (dist >= bestFighter[0]) {
								continue;
							}
							bestFighter[0] = dist;
							bestFighter[1] = i;
							bestFighter[2] = 1;
							bestFighter[3] = 0;
							bestFighter[4] = target.getCell().getId();
							bestSS = S;
						}
					} else {
						if (dist >= bestFighter[0]) {
							continue;
						}
						bestFighter[0] = dist;
						bestFighter[1] = i;
						bestFighter[2] = 1;
						bestFighter[3] = 0;
						bestFighter[4] = target.getCell().getId();
						bestSS = S;
					}
				}
			}
		}
		if (bestFighter[1] != 0) {
			CellDest = bestFighter[1];
			targetCell = bestFighter[4];
		} else {
			if (bestInvok[1] == 0) {
				return -1;
			}
			CellDest = bestInvok[1];
			targetCell = bestInvok[4];
		}
		if (CellDest == 0) {
			return -1;
		}
		if (CellDest == fighter.getCell().getId()) {
			return targetCell + bestSS.getSpellID() * 1000;
		}
		final ArrayList<Case> path = new AstarPathfinding(fight.getMapOld(), fight, fighter.getCell().getId(), CellDest)
				.getShortestPath(-1);
		if (path == null) {
			return -1;
		}
		String pathstr = "";
		try {
			int curCaseID = fighter.getCell().getId();
			final int curDir = 0;
			path.add(fight.getMapOld().getCase(CellDest));
			for (final Case c : path) {
				if (curCaseID == c.getId()) {
					continue;
				}
				final char d = Pathfinding.getDirBetweenTwoCase(curCaseID, c.getId(), fight.getMap(), true);
				if (d == '\0') {
					return -1;
				}
				if (curDir != d) {
					if (path.indexOf(c) != 0) {
						pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
					}
					pathstr = String.valueOf(pathstr) + d;
				}
				curCaseID = c.getId();
			}
			if (curCaseID != fighter.getCell().getId()) {
				pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final GameAction GA = new GameAction(0, 1, "");
		GA.args = pathstr;
		fight.onFighterDeplace(fighter, GA);
		return targetCell + bestSS.getSpellID() * 1000;
	}

	private int moveToAttackIfPossibleAll(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return -1;
		}
		final Fighter target = this.getNearest(fight, fighter);
		final int distMin = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
				target.getCell().getId());
		final ArrayList<Spell.SortStats> sorts = this.getLaunchableSort(fighter, fight, distMin);
		if (sorts == null) {
			return -1;
		}
		final ArrayList<Integer> cells = Pathfinding.getListCaseFromFighter(fight, fighter, fighter.getCell().getId(),
				sorts);
		if (cells == null) {
			return -1;
		}
		int CellDest = 0;
		Spell.SortStats bestSS = null;
		final int[] bestInvok = { 1000, 0, 0, 0, -1 };
		final int[] bestFighter = { 1000, 0, 0, 0, -1 };
		int targetCell = -1;
		for (final int i : cells) {
			for (final Spell.SortStats S : sorts) {
				if (fight.canCastSpell1(fighter, S, target.getCell(), i)) {
					final int dist = Pathfinding.getDistanceBetween(fight.getMapOld(), fighter.getCell().getId(),
							target.getCell().getId());
					if (!Pathfinding.isNextTo(fighter.getFight().getMap(), fighter.getCell().getId(),
							target.getCell().getId())) {
						if (target.isInvocation()) {
							if (dist >= bestInvok[0]) {
								continue;
							}
							bestInvok[0] = dist;
							bestInvok[1] = i;
							bestInvok[3] = (bestInvok[2] = 1);
							bestInvok[4] = target.getCell().getId();
							bestSS = S;
						} else {
							if (dist >= bestFighter[0]) {
								continue;
							}
							bestFighter[0] = dist;
							bestFighter[1] = i;
							bestFighter[2] = 1;
							bestFighter[3] = 0;
							bestFighter[4] = target.getCell().getId();
							bestSS = S;
						}
					} else {
						if (dist >= bestFighter[0]) {
							continue;
						}
						bestFighter[0] = dist;
						bestFighter[1] = i;
						bestFighter[2] = 1;
						bestFighter[3] = 0;
						bestFighter[4] = target.getCell().getId();
						bestSS = S;
					}
				}
			}
		}
		if (bestFighter[1] != 0) {
			CellDest = bestFighter[1];
			targetCell = bestFighter[4];
		} else {
			if (bestInvok[1] == 0) {
				return -1;
			}
			CellDest = bestInvok[1];
			targetCell = bestInvok[4];
		}
		if (CellDest == 0) {
			return -1;
		}
		if (CellDest == fighter.getCell().getId()) {
			return targetCell + bestSS.getSpellID() * 1000;
		}
		final ArrayList<Case> path = new AstarPathfinding(fight.getMapOld(), fight, fighter.getCell().getId(), CellDest)
				.getShortestPath(-1);
		if (path == null) {
			return -1;
		}
		String pathstr = "";
		try {
			int curCaseID = fighter.getCell().getId();
			final int curDir = 0;
			path.add(fight.getMapOld().getCase(CellDest));
			for (final Case c : path) {
				if (curCaseID == c.getId()) {
					continue;
				}
				final char d = Pathfinding.getDirBetweenTwoCase(curCaseID, c.getId(), fight.getMap(), true);
				if (d == '\0') {
					return -1;
				}
				if (curDir != d) {
					if (path.indexOf(c) != 0) {
						pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
					}
					pathstr = String.valueOf(pathstr) + d;
				}
				curCaseID = c.getId();
			}
			if (curCaseID != fighter.getCell().getId()) {
				pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final GameAction GA = new GameAction(0, 1, "");
		GA.args = pathstr;
		fight.onFighterDeplace(fighter, GA);
		return targetCell + bestSS.getSpellID() * 1000;
	}

	private int moveToAttackIfPossible(final Fight fight, final Fighter fighter) {
		if (fight == null || fighter == null) {
			return -1;
		}
		final org.aestia.map.Map m = fight.getMap();
		if (m == null) {
			return -1;
		}
		final Case _c = fighter.getCell();
		if (_c == null) {
			return -1;
		}
		final Fighter ennemy = this.getNearestEnnemy(fight, fighter);
		if (ennemy == null) {
			return -1;
		}
		final int distMin = Pathfinding.getDistanceBetween(m, _c.getId(), ennemy.getCell().getId());
		final ArrayList<Spell.SortStats> sorts = this.getLaunchableSort(fighter, fight, distMin);
		if (sorts == null) {
			return -1;
		}
		final ArrayList<Integer> cells = Pathfinding.getListCaseFromFighter(fight, fighter, fighter.getCell().getId(),
				sorts);
		if (cells == null) {
			return -1;
		}
		final ArrayList<Fighter> targets = this.getPotentialTarget(fight, fighter, sorts);
		if (targets == null) {
			return -1;
		}
		int CellDest = 0;
		Spell.SortStats bestSS = null;
		final int[] bestInvok = { 1000, 0, 0, 0, -1 };
		final int[] bestFighter = { 1000, 0, 0, 0, -1 };
		int targetCell = -1;
		for (final int i : cells) {
			for (final Spell.SortStats S : sorts) {
				final int targetVal = this.getBestTargetZone(fight, fighter, S, i);
				if (targetVal > 0) {
					final int nbTarget = targetVal / 1000;
					final int cellID = targetVal - nbTarget * 1000;
					if (fight.getMapOld().getCase(cellID) == null || nbTarget <= 0
							|| !fight.canCastSpell1(fighter, S, fight.getMapOld().getCase(cellID), i)) {
						continue;
					}
					final int dist = Pathfinding.getDistanceBetween(fight.getMapOld(), fighter.getCell().getId(), i);
					if (dist >= bestFighter[0] && bestFighter[2] >= nbTarget) {
						continue;
					}
					bestFighter[0] = dist;
					bestFighter[1] = i;
					bestFighter[2] = nbTarget;
					bestFighter[4] = cellID;
					bestSS = S;
				} else {
					for (final Fighter T : targets) {
						if (fight.canCastSpell1(fighter, S, T.getCell(), i)) {
							final int dist = Pathfinding.getDistanceBetween(fight.getMapOld(),
									fighter.getCell().getId(), T.getCell().getId());
							if (!Pathfinding.isCACwithEnnemy(fighter, targets)) {
								if (T.isInvocation()) {
									if (dist >= bestInvok[0]) {
										continue;
									}
									bestInvok[0] = dist;
									bestInvok[1] = i;
									bestInvok[3] = (bestInvok[2] = 1);
									bestInvok[4] = T.getCell().getId();
									bestSS = S;
								} else {
									if (dist >= bestFighter[0]) {
										continue;
									}
									bestFighter[0] = dist;
									bestFighter[1] = i;
									bestFighter[2] = 1;
									bestFighter[3] = 0;
									bestFighter[4] = T.getCell().getId();
									bestSS = S;
								}
							} else {
								if (dist >= bestFighter[0]) {
									continue;
								}
								bestFighter[0] = dist;
								bestFighter[1] = i;
								bestFighter[2] = 1;
								bestFighter[3] = 0;
								bestFighter[4] = T.getCell().getId();
								bestSS = S;
							}
						}
					}
				}
			}
		}
		if (bestFighter[1] != 0) {
			CellDest = bestFighter[1];
			targetCell = bestFighter[4];
		} else {
			if (bestInvok[1] == 0) {
				return -1;
			}
			CellDest = bestInvok[1];
			targetCell = bestInvok[4];
		}
		if (CellDest == 0) {
			return -1;
		}
		if (CellDest == fighter.getCell().getId()) {
			return targetCell + bestSS.getSpellID() * 1000;
		}
		final ArrayList<Case> path = new AstarPathfinding(fight.getMapOld(), fight, fighter.getCell().getId(), CellDest)
				.getShortestPath(-1);
		if (path == null) {
			return -1;
		}
		String pathstr = "";
		try {
			int curCaseID = fighter.getCell().getId();
			final int curDir = 0;
			path.add(fight.getMapOld().getCase(CellDest));
			for (final Case c : path) {
				if (curCaseID == c.getId()) {
					continue;
				}
				final char d = Pathfinding.getDirBetweenTwoCase(curCaseID, c.getId(), m, true);
				if (d == '\0') {
					return -1;
				}
				if (curDir != d) {
					if (path.indexOf(c) != 0) {
						pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
					}
					pathstr = String.valueOf(pathstr) + d;
				}
				curCaseID = c.getId();
			}
			if (curCaseID != fighter.getCell().getId()) {
				pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final GameAction GA = new GameAction(0, 1, "");
		GA.args = pathstr;
		fight.onFighterDeplace(fighter, GA);
		return targetCell + bestSS.getSpellID() * 1000;
	}

	private ArrayList<Spell.SortStats> getLaunchableSort(final Fighter fighter, final Fight fight, final int distMin) {
		if (fight == null || fighter == null) {
			return null;
		}
		final ArrayList<Spell.SortStats> sorts = new ArrayList<Spell.SortStats>();
		if (fighter.getMob() == null) {
			return null;
		}
		for (final Map.Entry<Integer, Spell.SortStats> S : fighter.getMob().getSpells().entrySet()) {
			if (S.getValue().getSpellID() == 479) {
				continue;
			}
			if (S.getValue().getPACost() > fighter.getCurPa(fight)) {
				continue;
			}
			if (!LaunchedSpell.cooldownGood(fighter, S.getValue().getSpellID())) {
				continue;
			}
			if (S.getValue().getMaxLaunchbyTurn() - LaunchedSpell.getNbLaunch(fighter, S.getValue().getSpellID()) <= 0
					&& S.getValue().getMaxLaunchbyTurn() > 0) {
				continue;
			}
			if (S.getValue().getSpell().getType() != 0) {
				continue;
			}
			sorts.add(S.getValue());
		}
		final ArrayList<Spell.SortStats> finalS = this.TriInfluenceSorts(fighter, sorts, fight);
		return finalS;
	}

	private ArrayList<Spell.SortStats> TriInfluenceSorts(final Fighter fighter, final ArrayList<Spell.SortStats> sorts,
			final Fight fight) {
		if (fight == null || fighter == null) {
			return null;
		}
		if (sorts == null) {
			return null;
		}
		final ArrayList<Spell.SortStats> finalSorts = new ArrayList<Spell.SortStats>();
		final Map<Integer, Spell.SortStats> copie = new TreeMap<Integer, Spell.SortStats>();
		for (final Spell.SortStats S : sorts) {
			copie.put(S.getSpellID(), S);
		}
		int curInfl = 0;
		int curID = 0;
		while (copie.size() > 0) {
			curInfl = -1;
			curID = 0;
			for (final Map.Entry<Integer, Spell.SortStats> S2 : copie.entrySet()) {
				final int infl = this.getInfl(fight, S2.getValue());
				if (infl > curInfl) {
					curID = S2.getValue().getSpellID();
					curInfl = infl;
				}
			}
			finalSorts.add(copie.get(curID));
			copie.remove(curID);
		}
		return finalSorts;
	}

	private ArrayList<Fighter> getPotentialTarget(final Fight fight, final Fighter fighter,
			final ArrayList<Spell.SortStats> sorts) {
		if (fight == null || fighter == null) {
			return null;
		}
		final ArrayList<Fighter> targets = new ArrayList<Fighter>();
		int distMax = 0;
		for (final Spell.SortStats S : sorts) {
			if (S.getMaxPO() > distMax) {
				distMax = S.getMaxPO();
			}
		}
		distMax += fighter.getCurPm(fight) + 3;
		final Map<Integer, Fighter> potentialsT = this.getLowHpEnnemyList(fight, fighter);
		for (final Map.Entry<Integer, Fighter> T : potentialsT.entrySet()) {
			final int dist = Pathfinding.getDistanceBetween(fight.getMap(), fighter.getCell().getId(),
					T.getValue().getCell().getId());
			if (dist < distMax) {
				targets.add(T.getValue());
			}
		}
		return targets;
	}

	public int getInfl(final Fight fight, final Spell.SortStats SS) {
		if (fight == null) {
			return 0;
		}
		int inf = 0;
		for (final SpellEffect SE : SS.getEffects()) {
			switch (SE.getEffectID()) {
			case 96:
			case 97:
			case 98:
			case 99: {
				inf += 500 * Formulas.getMiddleJet(SE.getJet());
				continue;
			}
			default: {
				inf += Formulas.getMiddleJet(SE.getJet());
				continue;
			}
			}
		}
		return inf;
	}

	private Spell.SortStats getBestSpellForTarget(final Fight fight, final Fighter F, final Fighter T,
			final int launch) {
		if (fight == null || F == null) {
			return null;
		}
		int inflMax = 0;
		Spell.SortStats ss = null;
		if (F.isCollector()) {
			for (final Map.Entry<Integer, Spell.SortStats> SS : World.getGuild(F.getCollector().getGuildId())
					.getSpells().entrySet()) {
				if (SS.getValue() == null) {
					continue;
				}
				int curInfl = 0;
				int Infl1 = 0;
				int Infl2 = 0;
				final int PA = 6;
				final int[] usedPA = new int[2];
				if (!fight.canCastSpell1(F, SS.getValue(), F.getCell(), T.getCell().getId())) {
					continue;
				}
				curInfl = this.calculInfluence(SS.getValue(), F, T);
				if (curInfl == 0) {
					continue;
				}
				if (curInfl > inflMax) {
					ss = SS.getValue();
					usedPA[0] = ss.getPACost();
					Infl1 = (inflMax = curInfl);
				}
				for (final Map.Entry<Integer, Spell.SortStats> SS2 : World.getGuild(F.getCollector().getGuildId())
						.getSpells().entrySet()) {
					if (SS2.getValue() == null) {
						continue;
					}
					if (PA - usedPA[0] < SS2.getValue().getPACost()) {
						continue;
					}
					if (!fight.canCastSpell1(F, SS2.getValue(), F.getCell(), T.getCell().getId())) {
						continue;
					}
					curInfl = this.calculInfluence(SS2.getValue(), F, T);
					if (curInfl == 0) {
						continue;
					}
					if (Infl1 + curInfl > inflMax) {
						ss = SS.getValue();
						usedPA[1] = SS2.getValue().getPACost();
						Infl2 = curInfl;
						inflMax = Infl1 + Infl2;
					}
					for (final Map.Entry<Integer, Spell.SortStats> SS3 : World.getGuild(F.getCollector().getGuildId())
							.getSpells().entrySet()) {
						if (SS3.getValue() == null) {
							continue;
						}
						if (PA - usedPA[0] - usedPA[1] < SS3.getValue().getPACost()) {
							continue;
						}
						if (!fight.canCastSpell1(F, SS3.getValue(), F.getCell(), T.getCell().getId())) {
							continue;
						}
						curInfl = this.calculInfluence(SS3.getValue(), F, T);
						if (curInfl == 0) {
							continue;
						}
						if (curInfl + Infl1 + Infl2 <= inflMax) {
							continue;
						}
						ss = SS.getValue();
						inflMax = curInfl + Infl1 + Infl2;
					}
				}
			}
		} else {
			for (final Map.Entry<Integer, Spell.SortStats> SS : F.getMob().getSpells().entrySet()) {
				if (SS.getValue().getSpell().getType() != 0) {
					continue;
				}
				int curInfl = 0;
				int Infl1 = 0;
				int Infl2 = 0;
				final int PA = F.getMob().getPa();
				final int[] usedPA = new int[2];
				if (!fight.canCastSpell1(F, SS.getValue(), T.getCell(), launch)) {
					continue;
				}
				curInfl = this.getInfl(fight, SS.getValue());
				if (curInfl > inflMax) {
					ss = SS.getValue();
					usedPA[0] = ss.getPACost();
					Infl1 = (inflMax = curInfl);
				}
				for (final Map.Entry<Integer, Spell.SortStats> SS2 : F.getMob().getSpells().entrySet()) {
					if (SS2.getValue().getSpell().getType() != 0) {
						continue;
					}
					if (PA - usedPA[0] < SS2.getValue().getPACost()) {
						continue;
					}
					if (!fight.canCastSpell1(F, SS2.getValue(), T.getCell(), launch)) {
						continue;
					}
					curInfl = this.getInfl(fight, SS2.getValue());
					if (Infl1 + curInfl > inflMax) {
						ss = SS.getValue();
						usedPA[1] = SS2.getValue().getPACost();
						Infl2 = curInfl;
						inflMax = Infl1 + Infl2;
					}
					for (final Map.Entry<Integer, Spell.SortStats> SS3 : F.getMob().getSpells().entrySet()) {
						if (SS3.getValue().getSpell().getType() != 0) {
							continue;
						}
						if (PA - usedPA[0] - usedPA[1] < SS3.getValue().getPACost()) {
							continue;
						}
						if (!fight.canCastSpell1(F, SS3.getValue(), T.getCell(), launch)) {
							continue;
						}
						curInfl = this.getInfl(fight, SS3.getValue());
						if (curInfl + Infl1 + Infl2 <= inflMax) {
							continue;
						}
						ss = SS.getValue();
						inflMax = curInfl + Infl1 + Infl2;
					}
				}
			}
		}
		return ss;
	}

	private int getBestTargetZone(final Fight fight, final Fighter fighter, final Spell.SortStats spell,
			final int launchCell) {
		if (fight == null || fighter == null) {
			return 0;
		}
		if (spell.getPorteeType().isEmpty()
				|| (spell.getPorteeType().charAt(0) == 'P' && spell.getPorteeType().charAt(1) == 'a')
				|| spell.isLineLaunch()) {
			return 0;
		}
		ArrayList<Case> possibleLaunch = new ArrayList<Case>();
		int CellF = -1;
		if (spell.getMaxPO() != 0) {
			final char arg1 = 'C';
			final char[] table = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
					'r', 's', 't', 'u', 'v' };
			char arg2 = 'a';
			if (spell.getMaxPO() > 20) {
				arg2 = 'u';
			} else {
				arg2 = table[spell.getMaxPO()];
			}
			final String args = String.valueOf(Character.toString(arg1)) + Character.toString(arg2);
			possibleLaunch = Pathfinding.getCellListFromAreaString(fight.getMap(), launchCell, launchCell, args, 0,
					false);
		} else {
			possibleLaunch.add(fight.getMap().getCase(launchCell));
		}
		if (possibleLaunch == null) {
			return -1;
		}
		int nbTarget = 0;
		for (final Case cell : possibleLaunch) {
			try {
				if (!fight.canCastSpell1(fighter, spell, cell, launchCell)) {
					continue;
				}
				int curTarget = 0;
				final ArrayList<Case> cells = Pathfinding.getCellListFromAreaString(fight.getMap(), cell.getId(),
						launchCell, spell.getPorteeType(), 0, false);
				for (final Case c : cells) {
					if (c == null) {
						continue;
					}
					if (c.getFirstFighter() == null) {
						continue;
					}
					if (c.getFirstFighter().getTeam2() == fighter.getTeam2()) {
						continue;
					}
					++curTarget;
				}
				if (curTarget <= nbTarget) {
					continue;
				}
				nbTarget = curTarget;
				CellF = cell.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (nbTarget > 0 && CellF != -1) {
			return CellF + nbTarget * 1000;
		}
		return 0;
	}

	private int calculInfluenceHeal(final Spell.SortStats ss) {
		int inf = 0;
		for (final SpellEffect SE : ss.getEffects()) {
			if (SE.getEffectID() != 108) {
				return 0;
			}
			inf += 100 * Formulas.getMiddleJet(SE.getJet());
		}
		return inf;
	}

	private int calculInfluence(final Spell.SortStats ss, final Fighter C, final Fighter T) {
		int infTot = 0;
		for (final SpellEffect SE : ss.getEffects()) {
			int inf = 0;
			switch (SE.getEffectID()) {
			case 5: {
				inf = 500 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 89: {
				inf = 200 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 91: {
				inf = 150 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 92: {
				inf = 150 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 93: {
				inf = 150 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 94: {
				inf = 150 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 95: {
				inf = 150 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 96: {
				inf = 100 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 97: {
				inf = 100 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 98: {
				inf = 100 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 99: {
				inf = 100 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 100: {
				inf = 100 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 101: {
				inf = 1000 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 127: {
				inf = 1000 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 84: {
				inf = 1500 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 77: {
				inf = 1500 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 111: {
				inf = -1000 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 128: {
				inf = -1000 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 121: {
				inf = -100 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 131: {
				inf = 300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 132: {
				inf = 2000;
				break;
			}
			case 138: {
				inf = -50 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 150: {
				inf = -2000;
				break;
			}
			case 168: {
				inf = 1000 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 169: {
				inf = 1000 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 210: {
				inf = -300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 211: {
				inf = -300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 212: {
				inf = -300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 213: {
				inf = -300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 214: {
				inf = -300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 215: {
				inf = 300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 216: {
				inf = 300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 217: {
				inf = 300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 218: {
				inf = 300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 219: {
				inf = 300 * Formulas.getMiddleJet(SE.getJet());
				break;
			}
			case 265: {
				inf = -250 * Formulas.getMiddleJet(SE.getJet());
			}
			case 765: {
				inf = -1000;
				break;
			}
			case 786: {
				inf = -1000;
				break;
			}
			case 106: {
				inf = -900;
				break;
			}
			}
			if (C.getTeam() == T.getTeam()) {
				infTot -= inf;
			} else {
				infTot += inf;
			}
		}
		return infTot;
	}

	public int getBonusForMonster(final Fighter Monster, final Fighter target, final SpellEffect SS,
			final Fight Fight) {
		switch (Monster.getMob().getTemplate().getId()) {
		case 103: {
			if (SS.getEffectID() == 99) {
				return 5;
			}
			break;
		}
		}
		return 1;
	}

	static /* synthetic */ void access$12(final IAThread iaThread, final boolean stop) {
		iaThread.stop = stop;
	}
}
