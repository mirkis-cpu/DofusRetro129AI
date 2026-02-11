// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.dynamic;

import java.util.ArrayList;

import org.aestia.entity.Collector;
import org.aestia.fight.Fighter;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;

public class FormuleOfficiel {
	public static long getXp(final Object object, final ArrayList<Fighter> winners, final long groupXp, byte nbonus,
			final int star, final int challenge, final int lvlMax, final int lvlMin, final int lvlLoosers,
			final int lvlWinners) {
		if (lvlMin <= 0) {
			return 0L;
		}
		if (object instanceof Fighter) {
			final Fighter fighter = (Fighter) object;
			if (winners.contains(fighter)) {
				if (lvlWinners <= 0) {
					return 0L;
				}
				final double sagesse = fighter.getLvl() * 0.5 + fighter.getPersonnage().getTotalStats().getEffect(124);
				double nvGrpMonster = lvlMax / lvlMin;
				double bonus = 1.0;
				double rapport = lvlLoosers / lvlWinners;
				if (winners.size() == 1) {
					rapport = 0.6;
				} else {
					if (rapport == 0.0) {
						return 0L;
					}
					if (rapport <= 1.1 && rapport >= 0.9) {
						rapport = 1.0;
					} else {
						if (rapport > 1.0) {
							rapport = 1.0 / rapport;
						}
						if (rapport < 0.01) {
							rapport = 0.01;
						}
					}
				}
				int sizeGroupe = 0;
				for (final Fighter f : winners) {
					if (f.getPersonnage() != null && !f.isInvocation() && !f.isMob() && !f.isCollector()
							&& !f.isDouble()) {
						++sizeGroupe;
					}
				}
				if (sizeGroupe < 1) {
					return 0L;
				}
				if (sizeGroupe > 8) {
					sizeGroupe = 8;
				}
				if (nbonus > 8) {
					nbonus = 8;
				}
				switch (nbonus) {
				case 0: {
					bonus = 0.5;
					break;
				}
				case 1: {
					bonus = 0.5;
					break;
				}
				case 2: {
					bonus = 2.1;
					break;
				}
				case 3: {
					bonus = 3.2;
					break;
				}
				case 4: {
					bonus = 4.3;
					break;
				}
				case 5: {
					bonus = 5.4;
					break;
				}
				case 6: {
					bonus = 6.5;
					break;
				}
				case 7: {
					bonus = 7.8;
					break;
				}
				case 8: {
					bonus = 9.0;
					break;
				}
				}
				if (nvGrpMonster == 0.0) {
					return 0L;
				}
				if (nvGrpMonster < 3.0) {
					nvGrpMonster = 1.0;
				} else {
					nvGrpMonster = 1.0 / nvGrpMonster;
				}
				if (nvGrpMonster < 0.0) {
					nvGrpMonster = 0.0;
				} else if (nvGrpMonster > 1.0) {
					nvGrpMonster = 1.0;
				}
				return (long) ((1.0 + (sagesse + star + challenge) / 100.0) * (bonus + rapport) * nvGrpMonster
						* (groupXp / sizeGroupe)) * Config.getInstance().rateXp;
			}
		} else if (object instanceof Collector) {
			final Collector collector = (Collector) object;
			if (World.getGuild(collector.getGuildId()) == null) {
				return 0L;
			}
			if (lvlWinners <= 0) {
				return 0L;
			}
			final double sagesse = World.getGuild(collector.getGuildId()).getLvl() * 0.5
					+ World.getGuild(collector.getGuildId()).getStats(124);
			double nvGrpMonster = lvlMax / lvlMin;
			double bonus = 1.0;
			double rapport = lvlLoosers / lvlWinners;
			if (winners.size() == 1) {
				rapport = 0.6;
			} else {
				if (rapport == 0.0) {
					return 0L;
				}
				if (rapport <= 1.1 && rapport >= 0.9) {
					rapport = 1.0;
				} else {
					if (rapport > 1.0) {
						rapport = 1.0 / rapport;
					}
					if (rapport < 0.01) {
						rapport = 0.01;
					}
				}
			}
			int sizeGroupe = 0;
			for (final Fighter f : winners) {
				if (f.getPersonnage() != null && !f.isInvocation() && !f.isMob() && !f.isCollector() && !f.isDouble()) {
					++sizeGroupe;
				}
			}
			if (sizeGroupe < 1) {
				return 0L;
			}
			if (sizeGroupe > 8) {
				sizeGroupe = 8;
			}
			if (nbonus > 8) {
				nbonus = 8;
			}
			switch (nbonus) {
			case 0: {
				bonus = 0.5;
				break;
			}
			case 1: {
				bonus = 0.5;
				break;
			}
			case 2: {
				bonus = 2.1;
				break;
			}
			case 3: {
				bonus = 3.2;
				break;
			}
			case 4: {
				bonus = 4.3;
				break;
			}
			case 5: {
				bonus = 5.4;
				break;
			}
			case 6: {
				bonus = 6.5;
				break;
			}
			case 7: {
				bonus = 7.8;
				break;
			}
			case 8: {
				bonus = 9.0;
				break;
			}
			}
			if (nvGrpMonster == 0.0) {
				return 0L;
			}
			if (nvGrpMonster < 3.0) {
				nvGrpMonster = 1.0;
			} else {
				nvGrpMonster = 1.0 / nvGrpMonster;
			}
			if (nvGrpMonster < 0.0) {
				nvGrpMonster = 0.0;
			} else if (nvGrpMonster > 1.0) {
				nvGrpMonster = 1.0;
			}
			return (long) ((1.0 + (sagesse + star + challenge) / 100.0) * (bonus + rapport) * nvGrpMonster
					* (groupXp / sizeGroupe) * Config.getInstance().rateXp);
		}
		return 0L;
	}
}
