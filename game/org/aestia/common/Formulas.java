// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.aestia.client.Player;
import org.aestia.entity.Collector;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.map.Map;
import org.aestia.other.Guild;

public class Formulas {
	public static int countCell(int i) {
		if (i > 64) {
			i = 64;
		}
		return 2 * i * (i + 1);
	}

	public static int getRandomValue(final int i1, final int i2) {
		if (i2 < i1) {
			return 0;
		}
		final Random rand = new Random();
		return rand.nextInt(i2 - i1 + 1) + i1;
	}

	public static int getMaxJet(final String jet) {
		int num = 0;
		try {
			final int des = Integer.parseInt(jet.split("d")[0]);
			final int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			final int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			for (int a = 0; a < des; ++a) {
				num += faces;
			}
			num += add;
			return num;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int getRandomJet(final String jet) {
		try {
			int num = 0;
			final int des = Integer.parseInt(jet.split("d")[0]);
			final int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			final int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			if (faces == 0 && add == 0) {
				num = getRandomValue(0, des);
			} else {
				for (int a = 0; a < des; ++a) {
					num += getRandomValue(1, faces);
				}
			}
			num += add;
			return num;
		} catch (NumberFormatException e) {
			Console.println("!A REPORTER! Erreur de jet '" + jet + "'", Console.Color.ERROR);
			e.printStackTrace();
			return -1;
		}
	}

	public static int getMiddleJet(final String jet) {
		try {
			int num = 0;
			final int des = Integer.parseInt(jet.split("d")[0]);
			final int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			final int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			num += (1 + faces) / 2 * des;
			num += add;
			return num;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int getTacleChance(final Fighter fight, final Fighter fighter) {
		final int agiTacleur = fight.getTotalStats().getEffect(119);
		final int agiEnemi = fighter.getTotalStats().getEffect(119);
		int div = agiTacleur + agiEnemi + 50;
		if (div == 0) {
			div = 1;
		}
		final int esquive = 300 * (agiTacleur + 25) / div - 100;
		return esquive;
	}

	public static int calculFinalHeal(final Player caster, final int jet) {
		int statC = caster.getTotalStats().getEffect(126);
		final int soins = caster.getTotalStats().getEffect(178);
		if (statC < 0) {
			statC = 0;
		}
		return jet * (100 + statC) / 100 + soins;
	}

	public static int calculFinalHealCac(final Fighter healer, final int rank, final boolean isCac) {
		int intel = healer.getTotalStats().getEffect(126);
		final int heals = healer.getTotalStats().getEffect(178);
		if (intel < 0) {
			intel = 0;
		}
		float adic = 100.0f;
		if (isCac) {
			adic = 105.0f;
		}
		return (int) (rank * ((100.0 + intel) / adic) + heals / 2);
	}

	public static int calculXpWinCraft(final int lvl, final int numCase) {
		if (lvl == 100) {
			return 0;
		}
		switch (numCase) {
		case 1: {
			if (lvl < 40) {
				return 1;
			}
			return 0;
		}
		case 2: {
			if (lvl < 60) {
				return 10;
			}
			return 0;
		}
		case 3: {
			if (lvl > 9 && lvl < 80) {
				return 25;
			}
			return 0;
		}
		case 4: {
			if (lvl > 19) {
				return 50;
			}
			return 0;
		}
		case 5: {
			if (lvl > 39) {
				return 100;
			}
			return 0;
		}
		case 6: {
			if (lvl > 59) {
				return 250;
			}
			return 0;
		}
		case 7: {
			if (lvl > 79) {
				return 500;
			}
			return 0;
		}
		case 8: {
			if (lvl > 99) {
				return 1000;
			}
			return 0;
		}
		default: {
			return 0;
		}
		}
	}

	public static int calculXpWinFm(final int lvl, final int poid) {
		if (lvl <= 1) {
			if (poid <= 10) {
				return 10;
			}
			if (poid <= 50) {
				return 25;
			}
			return 50;
		} else if (lvl <= 25) {
			if (poid <= 10) {
				return 10;
			}
			return 50;
		} else if (lvl <= 50) {
			if (poid <= 1) {
				return 10;
			}
			if (poid <= 10) {
				return 25;
			}
			if (poid <= 50) {
				return 50;
			}
			return 100;
		} else if (lvl <= 75) {
			if (poid <= 3) {
				return 25;
			}
			if (poid <= 10) {
				return 50;
			}
			if (poid <= 50) {
				return 100;
			}
			return 250;
		} else if (lvl <= 100) {
			if (poid <= 3) {
				return 50;
			}
			if (poid <= 10) {
				return 100;
			}
			if (poid <= 50) {
				return 250;
			}
			return 500;
		} else if (lvl <= 125) {
			if (poid <= 3) {
				return 100;
			}
			if (poid <= 10) {
				return 250;
			}
			if (poid <= 50) {
				return 500;
			}
			return 1000;
		} else if (lvl <= 150) {
			if (poid <= 10) {
				return 250;
			}
			return 1000;
		} else if (lvl <= 175) {
			if (poid <= 1) {
				return 250;
			}
			if (poid <= 10) {
				return 500;
			}
			return 1000;
		} else {
			if (poid <= 1) {
				return 500;
			}
			return 1000;
		}
	}

	public static int calculXpLooseCraft(final int lvl, final int numCase) {
		if (lvl == 100) {
			return 0;
		}
		switch (numCase) {
		case 1: {
			if (lvl < 40) {
				return 1;
			}
			return 0;
		}
		case 2: {
			if (lvl < 60) {
				return 5;
			}
			return 0;
		}
		case 3: {
			if (lvl > 9 && lvl < 80) {
				return 12;
			}
			return 0;
		}
		case 4: {
			if (lvl > 19) {
				return 25;
			}
			return 0;
		}
		case 5: {
			if (lvl > 39) {
				return 50;
			}
			return 0;
		}
		case 6: {
			if (lvl > 59) {
				return 125;
			}
			return 0;
		}
		case 7: {
			if (lvl > 79) {
				return 250;
			}
			return 0;
		}
		case 8: {
			if (lvl > 99) {
				return 500;
			}
			return 0;
		}
		default: {
			return 0;
		}
		}
	}

	public static int calculHonorWin(final ArrayList<Fighter> winner, final ArrayList<Fighter> looser,
			final Fighter F) {
		float totalGradeWin = 0.0f;
		float totalLevelWin = 0.0f;
		float totalGradeLoose = 0.0f;
		float totalLevelLoose = 0.0f;
		boolean Prisme = false;
		int fighters = 0;
		for (final Fighter f : winner) {
			if (f.getPersonnage() == null && f.getPrism() == null) {
				continue;
			}
			if (f.getPersonnage() != null) {
				totalLevelWin += f.getLvl();
				totalGradeWin += f.getPersonnage().getGrade();
			} else {
				Prisme = true;
				totalLevelWin += f.getPrism().getLevel() * 15 + 80;
				totalGradeWin += f.getPrism().getLevel();
			}
		}
		for (final Fighter f : looser) {
			if (f.getPersonnage() == null && f.getPrism() == null) {
				continue;
			}
			if (f.getPersonnage() != null) {
				totalLevelLoose += f.getLvl();
				totalGradeLoose += f.getPersonnage().getGrade();
				++fighters;
			} else {
				Prisme = true;
				totalLevelLoose += f.getPrism().getLevel() * 15 + 80;
				totalGradeLoose += f.getPrism().getLevel();
			}
		}
		if (!Prisme && totalLevelWin - totalLevelLoose > 15 * fighters) {
			return 0;
		}
		int base = (int) (100.0f * (totalGradeLoose * totalLevelLoose / (totalGradeWin * totalLevelWin)))
				/ winner.size();
		if (Prisme && base <= 0) {
			return 100;
		}
		if (looser.contains(F)) {
			base = -base;
		}
		return base * Config.getInstance().rateHonor;
	}

	public static int calculFinalDommage(final Fight fight, final Fighter caster, final Fighter target,
			final int statID, final int jet, final boolean isHeal, final boolean isCaC, final int spellid) {
		float i = 0.0f;
		final float j = 100.0f;
		float a = 1.0f;
		float num = 0.0f;
		float statC = 0.0f;
		float domC = 0.0f;
		float perdomC = 0.0f;
		float resfT = 0.0f;
		float respT = 0.0f;
		float mulT = 1.0f;
		int multiplier = 0;
		if (!isHeal) {
			domC = caster.getTotalStats().getEffect(112);
			perdomC = caster.getTotalStats().getEffect(138);
			multiplier = caster.getTotalStats().getEffect(114);
			if (caster.hasBuff(114)) {
				mulT = caster.getBuffValue(114);
			}
		} else {
			domC = caster.getTotalStats().getEffect(178);
		}
		switch (statID) {
		case -1: {
			statC = 0.0f;
			resfT = 0.0f;
			respT = 0.0f;
			respT = 0.0f;
			mulT = 1.0f;
			break;
		}
		case 0: {
			statC = caster.getTotalStats().getEffect(118);
			resfT = target.getTotalStats().getEffect(241);
			respT = target.getTotalStats().getEffect(214);
			if (caster.getPersonnage() != null) {
				respT += target.getTotalStats().getEffect(254);
				resfT += target.getTotalStats().getEffect(264);
			}
			domC += caster.getTotalStats().getEffect(142);
			resfT = target.getTotalStats().getEffect(184);
			break;
		}
		case 1: {
			statC = caster.getTotalStats().getEffect(118);
			resfT = target.getTotalStats().getEffect(242);
			respT = target.getTotalStats().getEffect(210);
			if (caster.getPersonnage() != null) {
				respT += target.getTotalStats().getEffect(250);
				resfT += target.getTotalStats().getEffect(260);
			}
			domC += caster.getTotalStats().getEffect(142);
			resfT = target.getTotalStats().getEffect(184);
			break;
		}
		case 2: {
			statC = caster.getTotalStats().getEffect(123);
			resfT = target.getTotalStats().getEffect(243);
			respT = target.getTotalStats().getEffect(211);
			if (caster.getPersonnage() != null) {
				respT += target.getTotalStats().getEffect(251);
				resfT += target.getTotalStats().getEffect(261);
			}
			resfT = target.getTotalStats().getEffect(183);
			break;
		}
		case 3: {
			statC = caster.getTotalStats().getEffect(126);
			resfT = target.getTotalStats().getEffect(240);
			respT = target.getTotalStats().getEffect(213);
			if (caster.getPersonnage() != null) {
				respT += target.getTotalStats().getEffect(253);
				resfT += target.getTotalStats().getEffect(263);
			}
			resfT = target.getTotalStats().getEffect(183);
			break;
		}
		case 4: {
			statC = caster.getTotalStats().getEffect(119);
			resfT = target.getTotalStats().getEffect(244);
			respT = target.getTotalStats().getEffect(212);
			if (caster.getPersonnage() != null) {
				respT += target.getTotalStats().getEffect(252);
				resfT += target.getTotalStats().getEffect(262);
			}
			resfT = target.getTotalStats().getEffect(183);
			break;
		}
		}
		if (target.getMob() == null && respT > 50.0f) {
			respT = 50.0f;
		}
		if (statC < 0.0f) {
			statC = 0.0f;
		}
		if (caster.getPersonnage() != null && isCaC) {
			final int ArmeType = caster.getPersonnage().getObjetByPos(1).getTemplate().getType();
			if (caster.getSpellValueBool(392) && ArmeType == 2) {
				i = caster.getMaitriseDmg(392);
			}
			if (caster.getSpellValueBool(390) && ArmeType == 4) {
				i = caster.getMaitriseDmg(390);
			}
			if (caster.getSpellValueBool(391) && ArmeType == 6) {
				i = caster.getMaitriseDmg(391);
			}
			if (caster.getSpellValueBool(393) && ArmeType == 7) {
				i = caster.getMaitriseDmg(393);
			}
			if (caster.getSpellValueBool(394) && ArmeType == 3) {
				i = caster.getMaitriseDmg(394);
			}
			if (caster.getSpellValueBool(395) && ArmeType == 5) {
				i = caster.getMaitriseDmg(395);
			}
			if (caster.getSpellValueBool(396) && ArmeType == 8) {
				i = caster.getMaitriseDmg(396);
			}
			if (caster.getSpellValueBool(397) && ArmeType == 19) {
				i = caster.getMaitriseDmg(397);
			}
			a = (100.0f + i) / 100.0f * (j / 100.0f);
		}
		num = a * mulT * (jet * ((100.0f + statC + perdomC + multiplier * 100) / 100.0f)) + domC;
		if (spellid != -1) {
			switch (spellid) {
			case 66: {
				statC = caster.getTotalStats().getEffect(119);
				num = jet * ((100.0f + statC + perdomC + multiplier * 100) / 100.0f) + domC;
				if (target.hasBuff(105)) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + target.getBuff(105).getValue());
					return 0;
				}
				if (target.hasBuff(184)) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + target.getBuff(184).getValue());
					return 0;
				}
				return (int) num;
			}
			case 71:
			case 196:
			case 219: {
				statC = caster.getTotalStats().getEffect(118);
				num = jet * ((100.0f + statC + perdomC + multiplier * 100) / 100.0f) + domC;
				if (target.hasBuff(105)) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + target.getBuff(105).getValue());
					return 0;
				}
				if (target.hasBuff(184)) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + target.getBuff(184).getValue());
					return 0;
				}
				return (int) num;
			}
			case 181:
			case 200: {
				statC = caster.getTotalStats().getEffect(126);
				num = jet * ((100.0f + statC + perdomC + multiplier * 100) / 100.0f) + domC;
				if (target.hasBuff(105)) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + target.getBuff(105).getValue());
					return 0;
				}
				if (target.hasBuff(184)) {
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
							new StringBuilder(String.valueOf(caster.getId())).toString(),
							String.valueOf(target.getId()) + "," + target.getBuff(184).getValue());
					return 0;
				}
				return (int) num;
			}
			}
		}
		if (caster.getId() != target.getId()) {
			int renvoie = target.getTotalStatsLessBuff().getEffect(220);
			if (renvoie > 0 && !isHeal) {
				if (renvoie > num) {
					renvoie = (int) num;
				}
				num -= renvoie;
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 107, "-1",
						String.valueOf(target.getId()) + "," + renvoie);
				if (renvoie > caster.getPdv()) {
					renvoie = caster.getPdv();
				}
				if (num < 1.0f) {
					num = 0.0f;
				}
				if (caster.getPdv() <= renvoie) {
					caster.removePdv(caster, renvoie);
					fight.onFighterDie(caster, caster);
				} else {
					caster.removePdv(caster, renvoie);
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100,
						new StringBuilder(String.valueOf(caster.getId())).toString(),
						String.valueOf(caster.getId()) + ",-" + renvoie);
			}
		}
		if (!isHeal) {
			num -= resfT;
		}
		final int reduc = (int) (num / 100.0f * respT);
		if (!isHeal) {
			num -= reduc;
		}
		final int armor = getArmorResist(target, statID);
		if (!isHeal) {
			num -= armor;
		}
		if (!isHeal && armor > 0) {
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105,
					new StringBuilder(String.valueOf(caster.getId())).toString(),
					String.valueOf(target.getId()) + "," + armor);
		}
		if (num < 1.0f) {
			num = 0.0f;
		}
		if (caster.getPersonnage() != null || caster.isCollector()) {
			if (target.getPersonnage() != null) {
				target.removePdvMax((int) Math.floor(num / 10.0f));
			}
			return (int) num;
		}
		if (caster.getMob().getTemplate().getId() == 116) {
			return (int) (num / 25.0f * caster.getPdvMax());
		}
		final int niveauMob = caster.getLvl();
		final double CalculCoef = niveauMob * 0.5 / 100.0;
		final int Multiplicateur = (int) Math.ceil(CalculCoef);
		return (int) num * Multiplicateur;
	}

	public static int calculZaapCost(final Map map1, final Map map2) {
		return 10 * (Math.abs(map2.getX() - map1.getX()) + Math.abs(map2.getY() - map1.getY()) - 1);
	}

	private static int getArmorResist(final Fighter target, final int statID) {
		int armor = 0;
		for (final SpellEffect SE : target.getBuffsByEffectID(265)) {
			Fighter fighter = null;
			switch (SE.getSpell()) {
			case 1: {
				if (statID != 3) {
					continue;
				}
				fighter = SE.getCaster();
				break;
			}
			case 6: {
				if (statID != 1 && statID != 0) {
					continue;
				}
				fighter = SE.getCaster();
				break;
			}
			case 14: {
				if (statID != 4) {
					continue;
				}
				fighter = SE.getCaster();
				break;
			}
			case 18: {
				if (statID != 2) {
					continue;
				}
				fighter = SE.getCaster();
				break;
			}
			default: {
				fighter = target;
				break;
			}
			}
			final int intell = fighter.getTotalStats().getEffect(126);
			int carac = 0;
			switch (statID) {
			case 4: {
				carac = fighter.getTotalStats().getEffect(119);
				break;
			}
			case 3: {
				carac = fighter.getTotalStats().getEffect(126);
				break;
			}
			case 2: {
				carac = fighter.getTotalStats().getEffect(123);
				break;
			}
			case 0:
			case 1: {
				carac = fighter.getTotalStats().getEffect(118);
				break;
			}
			}
			final int value = SE.getValue();
			final int a = value * (100 + intell / 2 + carac / 2) / 100;
			armor += a;
		}
		for (final SpellEffect SE : target.getBuffsByEffectID(105)) {
			final int intell2 = target.getTotalStats().getEffect(126);
			int carac2 = 0;
			switch (statID) {
			case 4: {
				carac2 = target.getTotalStats().getEffect(119);
				break;
			}
			case 3: {
				carac2 = target.getTotalStats().getEffect(126);
				break;
			}
			case 2: {
				carac2 = target.getTotalStats().getEffect(123);
				break;
			}
			case 0:
			case 1: {
				carac2 = target.getTotalStats().getEffect(118);
				break;
			}
			}
			final int value2 = SE.getValue();
			final int a2 = value2 * (100 + intell2 / 2 + carac2 / 2) / 100;
			armor += a2;
		}
		return armor;
	}

	public static int getPointsLost(final char z, final int value, final Fighter caster, final Fighter target) {
		float esquiveC = (z == 'a') ? caster.getTotalStats().getEffect(160) : caster.getTotalStats().getEffect(161);
		float esquiveT = (z == 'a') ? target.getTotalStats().getEffect(160) : target.getTotalStats().getEffect(161);
		float ptsMax = (z == 'a') ? target.getTotalStatsLessBuff().getEffect(111)
				: target.getTotalStatsLessBuff().getEffect(128);
		int retrait = 0;
		for (int i = 0; i < value; ++i) {
			if (ptsMax == 0.0f && target.getMob() != null) {
				ptsMax = ((z == 'a') ? target.getMob().getPa() : target.getMob().getPm());
			}
			final float pts = (z == 'a') ? target.getPa() : target.getPm();
			final float ptsAct = pts - retrait;
			if (esquiveT <= 0.0f) {
				esquiveT = 1.0f;
			}
			if (esquiveC <= 0.0f) {
				esquiveC = 1.0f;
			}
			final float a = esquiveC / esquiveT;
			final float b = ptsAct / ptsMax;
			final float pourcentage = a * b * 50.0f;
			int chance = (int) Math.ceil(pourcentage);
			if (chance < 0) {
				chance = 0;
			}
			if (chance > 100) {
				chance = 100;
			}
			final int jet = getRandomValue(0, 99);
			if (jet < chance) {
				++retrait;
			}
		}
		return retrait;
	}

	public static long getXpWinPerco(final Collector perco, final ArrayList<Fighter> winners,
			final ArrayList<Fighter> loosers, final long groupXP) {
		final Guild G = World.getGuild(perco.getGuildId());
		final float sag = G.getStats(124);
		final float coef = (sag + 100.0f) / 100.0f;
		final int taux = Config.getInstance().rateXp;
		long xpWin = 0L;
		int lvlmax = 0;
		for (final Fighter entry : winners) {
			if (entry.getLvl() > lvlmax) {
				lvlmax = entry.getLvl();
			}
		}
		int nbbonus = 0;
		for (final Fighter entry2 : winners) {
			if (entry2.getLvl() > lvlmax / 3) {
				++nbbonus;
			}
		}
		double bonus = 1.0;
		if (nbbonus == 2) {
			bonus = 1.1;
		}
		if (nbbonus == 3) {
			bonus = 1.3;
		}
		if (nbbonus == 4) {
			bonus = 2.2;
		}
		if (nbbonus == 5) {
			bonus = 2.5;
		}
		if (nbbonus == 6) {
			bonus = 2.8;
		}
		if (nbbonus == 7) {
			bonus = 3.1;
		}
		if (nbbonus >= 8) {
			bonus = 3.5;
		}
		int lvlLoosers = 0;
		for (final Fighter entry3 : loosers) {
			lvlLoosers += entry3.getLvl();
		}
		int lvlWinners = 0;
		for (final Fighter entry4 : winners) {
			lvlWinners += entry4.getLvl();
		}
		double rapport = 1.0 + lvlLoosers / lvlWinners;
		if (rapport <= 1.3) {
			rapport = 1.3;
		}
		final int lvl = G.getLvl();
		final double rapport2 = 1.0 + lvl / lvlWinners;
		xpWin = (long) (groupXP * rapport * bonus * taux * coef * rapport2);
		return xpWin;
	}

	public static long getXpWinPvm(Fighter perso, ArrayList<Fighter> winners, ArrayList<Fighter> loosers, long groupXP,
			int star) {
		if (perso.getPersonnage() == null)
			return 0;
		if (winners.contains(perso))// Si winner
		{
			float sag = perso.getTotalStats().getEffect(Constant.STATS_ADD_SAGE);
			float coef = (sag + 100) / 100;
			int taux = Config.singleton.rateXp;
			long xpWin = 0;
			int lvlmax = 0;
			for (Fighter entry : winners) {
				if (entry.getLvl() > lvlmax)
					lvlmax = entry.getLvl();
			}
			int nbbonus = 0;
			for (Fighter entry : winners) {
				if (entry.getLvl() > (lvlmax / 3))
					nbbonus += 1;
			}

			double bonus = 1;
			if (nbbonus == 2)
				bonus = 1.1;
			if (nbbonus == 3)
				bonus = 1.3;
			if (nbbonus == 4)
				bonus = 2.2;
			if (nbbonus == 5)
				bonus = 2.5;
			if (nbbonus == 6)
				bonus = 2.8;
			if (nbbonus == 7)
				bonus = 3.1;
			if (nbbonus >= 8)
				bonus = 3.5;

			int lvlLoosers = 0;
			for (Fighter entry : loosers)
				lvlLoosers += entry.getLvl();
			int lvlWinners = 0;
			for (Fighter entry : winners)
				lvlWinners += entry.getLvl();
			double rapport = 1 + ((double) lvlLoosers / (double) lvlWinners);
			if (rapport <= 1.3)
				rapport = 1.3;

			int lvl = perso.getLvl();
			double rapport2 = 1 + ((double) lvl / (double) lvlWinners);

			xpWin = (long) (groupXP * rapport * bonus * taux * coef * rapport2);
			if (star > 0)
				xpWin = xpWin + xpWin * (star / 100);
			return xpWin;
		}
		return 0;
	}

	public static long getGuildXpWin(final Fighter perso, final AtomicReference<Long> xpWin) {
		if (perso.getPersonnage() == null) {
			return 0L;
		}
		if (perso.getPersonnage().getGuildMember() == null) {
			return 0L;
		}
		final Guild.GuildMember gm = perso.getPersonnage().getGuildMember();
		final double xp = xpWin.get();
		final double Lvl = perso.getLvl();
		final double LvlGuild = perso.getPersonnage().get_guild().getLvl();
		final double pXpGive = gm.getPXpGive() / 100.0;
		final double maxP = xp * pXpGive * 0.1;
		final double diff = Math.abs(Lvl - LvlGuild);
		double toGuild;
		if (diff >= 70.0) {
			toGuild = maxP * 0.1;
		} else if (diff >= 31.0 && diff <= 69.0) {
			toGuild = maxP - maxP * 0.1 * Math.floor((diff + 30.0) / 10.0);
		} else if (diff >= 10.0 && diff <= 30.0) {
			toGuild = maxP - maxP * 0.2 * Math.floor(diff / 10.0);
		} else {
			toGuild = maxP;
		}
		xpWin.set((long) (xp - xp * pXpGive));
		return Math.round(toGuild);
	}

	public static long getMountXpWin(final Fighter perso, final AtomicReference<Long> xpWin) {
		if (perso.getPersonnage() == null) {
			return 0L;
		}
		if (perso.getPersonnage().getMount() == null) {
			return 0L;
		}
		final int diff = Math.abs(perso.getLvl() - perso.getPersonnage().getMount().getLvl());
		double coeff = 0.0;
		final double xp = xpWin.get();
		final double pToMount = perso.getPersonnage().getMountXpGive() / 100.0 + 0.2;
		if (diff >= 0 && diff <= 9) {
			coeff = 0.1;
		} else if (diff >= 10 && diff <= 19) {
			coeff = 0.08;
		} else if (diff >= 20 && diff <= 29) {
			coeff = 0.06;
		} else if (diff >= 30 && diff <= 39) {
			coeff = 0.04;
		} else if (diff >= 40 && diff <= 49) {
			coeff = 0.03;
		} else if (diff >= 50 && diff <= 59) {
			coeff = 0.02;
		} else if (diff >= 60 && diff <= 69) {
			coeff = 0.015;
		} else {
			coeff = 0.01;
		}
		if (pToMount > 0.2) {
			xpWin.set((long) (xp - xp * (pToMount - 0.2)));
		}
		return Math.round(xp * pToMount * coeff);
	}

	public static int getKamasWin(final Fighter i, final ArrayList<Fighter> winners, int maxk, final int mink) {
		++maxk;
		final int rkamas = (int) (Math.random() * (maxk - mink)) + mink;
		return rkamas * Config.getInstance().rateKamas;
	}

	public static int getKamasWinPerco(int maxk, final int mink) {
		++maxk;
		final int rkamas = (int) (Math.random() * (maxk - mink)) + mink;
		return rkamas * Config.getInstance().rateKamas;
	}

	public static int calculElementChangeChance(final int lvlM, final int lvlA, final int lvlP) {
		int K = 350;
		if (lvlP == 1) {
			K = 100;
		} else if (lvlP == 25) {
			K = 175;
		} else if (lvlP == 50) {
			K = 350;
		}
		return lvlM * 100 / (K + lvlA);
	}

	public static World.Couple<Integer, Integer> decompPierreAme(final org.aestia.object.Object toDecomp) {
		final String[] stats = toDecomp.parseStatsString().split("#");
		final int lvlMax = Integer.parseInt(stats[3], 16);
		final int chance = Integer.parseInt(stats[1], 16);
		final World.Couple<Integer, Integer> toReturn = new World.Couple<Integer, Integer>(chance, lvlMax);
		return toReturn;
	}

	public static int totalCaptChance(final int pierreChance, final Player p) {
		int sortChance = 0;
		switch (p.getSortStatBySortIfHas(413).getLevel()) {
		case 1: {
			sortChance = 1;
			break;
		}
		case 2: {
			sortChance = 3;
			break;
		}
		case 3: {
			sortChance = 6;
			break;
		}
		case 4: {
			sortChance = 10;
			break;
		}
		case 5: {
			sortChance = 15;
			break;
		}
		case 6: {
			sortChance = 25;
			break;
		}
		}
		return sortChance + pierreChance;
	}

	public static String parseReponse(final String reponse) {
		final StringBuilder toReturn = new StringBuilder("");
		final String[] cut = reponse.split("[%]");
		if (cut.length == 1) {
			return reponse;
		}
		toReturn.append(cut[0]);
		for (int i = 1; i < cut.length; ++i) {
			final char charact = (char) Integer.parseInt(cut[i].substring(0, 2), 16);
			toReturn.append(charact).append(cut[i].substring(2));
		}
		return toReturn.toString();
	}

	public static int spellCost(final int nb) {
		int total = 0;
		for (int i = 1; i < nb; ++i) {
			total += i;
		}
		return total;
	}

	public static int getLoosEnergy(final int lvl, final boolean isAgression, final boolean isPerco) {
		int returned = 5 * lvl;
		if (isAgression) {
			returned *= 1;
		}
		if (isPerco) {
			returned *= 1;
		}
		return returned;
	}

	public static int getRandomAbi(final int i1, final int i2, final int i3) {
		int Luck = 0;
		final int Random = getRandomValue(1, 10);
		switch (Random) {
		case 1: {
			Luck = i1;
			break;
		}
		case 2: {
			Luck = i2;
			break;
		}
		case 3: {
			Luck = i3;
			break;
		}
		case 4: {
			Luck = 0;
			break;
		}
		case 5: {
			Luck = 0;
			break;
		}
		case 6: {
			Luck = 0;
			break;
		}
		case 7: {
			Luck = 0;
			break;
		}
		case 8: {
			Luck = 0;
			break;
		}
		case 9: {
			Luck = 0;
			break;
		}
		case 10: {
			Luck = 0;
			break;
		}
		}
		return Luck;
	}

	public static int totalAppriChance(final boolean Amande, final boolean Rousse, final boolean Doree,
			final Player p) {
		int sortChance = 0;
		int ddChance = 0;
		switch (p.getSortStatBySortIfHas(414).getLevel()) {
		case 1: {
			sortChance = 15;
			break;
		}
		case 2: {
			sortChance = 20;
			break;
		}
		case 3: {
			sortChance = 25;
			break;
		}
		case 4: {
			sortChance = 30;
			break;
		}
		case 5: {
			sortChance = 35;
			break;
		}
		case 6: {
			sortChance = 45;
			break;
		}
		}
		if (Amande || Rousse) {
			ddChance = 15;
		}
		if (Doree) {
			ddChance = 5;
		}
		return sortChance + ddChance;
	}

	public static int getCouleur(final boolean Amande, final boolean Rousse, final boolean Doree) {
		final int Couleur = 0;
		if (Amande && !Rousse && !Doree) {
			return 20;
		}
		if (Rousse && !Amande && !Doree) {
			return 10;
		}
		if (Doree && !Amande && !Rousse) {
			return 18;
		}
		if (Amande && Rousse && !Doree) {
			final int Chance = getRandomValue(1, 2);
			if (Chance == 1) {
				return 20;
			}
			if (Chance == 2) {
				return 10;
			}
		}
		if (Amande && !Rousse && Doree) {
			final int Chance = getRandomValue(1, 2);
			if (Chance == 1) {
				return 20;
			}
			if (Chance == 2) {
				return 18;
			}
		}
		if (!Amande && Rousse && Doree) {
			final int Chance = getRandomValue(1, 2);
			if (Chance == 1) {
				return 18;
			}
			if (Chance == 2) {
				return 10;
			}
		}
		if (Amande && Rousse && Doree) {
			final int Chance = getRandomValue(1, 3);
			if (Chance == 1) {
				return 20;
			}
			if (Chance == 2) {
				return 10;
			}
			if (Chance == 3) {
				return 18;
			}
		}
		return Couleur;
	}

	public static int calculEnergieLooseForToogleMount(final int pts) {
		if (pts <= 170) {
			return 4;
		}
		if (pts >= 171 && pts < 180) {
			return 5;
		}
		if (pts >= 180 && pts < 200) {
			return 6;
		}
		if (pts >= 200 && pts < 210) {
			return 7;
		}
		if (pts >= 210 && pts < 220) {
			return 8;
		}
		if (pts >= 220 && pts < 230) {
			return 10;
		}
		if (pts >= 230 && pts <= 240) {
			return 12;
		}
		return 10;
	}

	public static int getLvlDopeuls(final int lvl) {
		if (lvl < 20) {
			return 20;
		}
		if (lvl > 19 && lvl < 40) {
			return 40;
		}
		if (lvl > 39 && lvl < 60) {
			return 60;
		}
		if (lvl > 59 && lvl < 80) {
			return 80;
		}
		if (lvl > 79 && lvl < 100) {
			return 100;
		}
		if (lvl > 99 && lvl < 120) {
			return 120;
		}
		if (lvl > 119 && lvl < 140) {
			return 140;
		}
		if (lvl > 139 && lvl < 160) {
			return 160;
		}
		if (lvl > 159 && lvl < 180) {
			return 180;
		}
		if (lvl > 180) {
			return 200;
		}
		return 200;
	}

	public static int calculChanceByElement(final int lvlJob, final int lvlObject, final int lvlRune) {
		int K = 1;
		if (lvlRune == 1) {
			K = 100;
		} else if (lvlRune == 25) {
			K = 175;
		} else if (lvlRune == 50) {
			K = 350;
		}
		return lvlJob * 100 / (K + lvlObject);
	}

	public static int ChanceFM(final int poidItemBase, final int poidItemActual, final int poidBaseJet,
			final int poidActualJet, final double poidRune, final int Puis, final double Coef) {
		int Chance = 0;
		final int a = poidItemBase + poidBaseJet + Puis * Config.getInstance().rateFm;
		int b = (int) Math.sqrt(poidItemActual + poidActualJet + poidRune);
		if (b <= 0) {
			b = 1;
		}
		Chance = (int) Math.floor(a / b * Coef / 10.0);
		return Chance;
	}

	public static ArrayList<Integer> chanceFM(final int WeightTotalBase, final int WeightTotalBaseMin,
			final int currentWeithTotal, final int currentWeightStats, final int weight, final int diff,
			final float coef, final int maxStat, final int minStat, final int actualStat, final float x,
			final boolean bonusRune, final int statsAdd) {
		final ArrayList<Integer> chances = new ArrayList<Integer>();
		float c = 1.0f;
		final float m1 = maxStat - (actualStat + statsAdd);
		final float m2 = maxStat - minStat;
		if (1.0f - m1 / m2 > 1.0) {
			c = (1.0f - (1.0f - m1 / m2) / 2.0f) / 2.0f;
		} else if (1.0f - m1 / m2 > 0.8) {
			c = 1.0f - (1.0f - m1 / m2) / 2.0f;
		}
		if (c < 0.0f) {
			c = 0.0f;
		}
		final int moyenne = (int) Math.floor(WeightTotalBase - (WeightTotalBase - WeightTotalBaseMin) / 2);
		float mStat = moyenne / currentWeithTotal;
		if (mStat > 1.2) {
			mStat = 1.2f;
		}
		final float a = (WeightTotalBase + diff) * coef * mStat * c * x * Config.getInstance().rateFm;
		float b = (float) (Math.sqrt(currentWeithTotal + currentWeightStats) + weight);
		if (b < 1.0) {
			b = 1.0f;
		}
		int p1 = (int) Math.floor(a / b);
		int p2 = 0;
		int p3 = 0;
		if (bonusRune) {
			p1 += 20;
		}
		if (p1 < 1) {
			p1 = 1;
			p2 = 0;
			p3 = 99;
		} else if (p1 > 100) {
			p1 = 66;
			p2 = 34;
		} else if (p1 > 66) {
			p1 = 66;
		}
		if (p2 == 0 && p3 == 0) {
			p2 = (int) Math.floor(a / Math.sqrt(currentWeithTotal + currentWeightStats));
			if (p2 > 100 - p1) {
				p2 = 100 - p1;
			}
			if (p2 > 50) {
				p2 = 50;
			}
		}
		chances.add(0, p1);
		chances.add(1, p2);
		chances.add(2, p3);
		return chances;
	}

	public static String convertToDate(final long time) {
		String hexDate = "#";
		final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String date = formatter.format(time);
		final String[] split = date.split("\\s");
		final String[] split2 = split[0].split("-");
		hexDate = String.valueOf(hexDate) + Integer.toHexString(Integer.parseInt(split2[0])) + "#";
		final int mois = Integer.parseInt(split2[1]) - 1;
		final int jour = Integer.parseInt(split2[2]);
		hexDate = String.valueOf(hexDate) + Integer.toHexString(Integer.parseInt(new StringBuilder()
				.append((mois < 10) ? ("0" + mois) : mois).append((jour < 10) ? ("0" + jour) : jour).toString())) + "#";
		final String[] split3 = split[1].split(":");
		final String heure = String.valueOf(split3[0]) + split3[1];
		hexDate = String.valueOf(hexDate) + Integer.toHexString(Integer.parseInt(heure));
		return hexDate;
	}

	public static int getXpStalk(final int lvl) {
		switch (lvl) {
		case 50:
		case 51:
		case 52:
		case 53:
		case 54:
		case 55:
		case 56:
		case 57:
		case 58:
		case 59: {
			return 65000;
		}
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 65:
		case 66:
		case 67:
		case 68:
		case 69: {
			return 90000;
		}
		case 70:
		case 71:
		case 72:
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 78:
		case 79: {
			return 120000;
		}
		case 80:
		case 81:
		case 82:
		case 83:
		case 84:
		case 85:
		case 86:
		case 87:
		case 88:
		case 89: {
			return 160000;
		}
		case 90:
		case 91:
		case 92:
		case 93:
		case 94:
		case 95:
		case 96:
		case 97:
		case 98:
		case 99: {
			return 210000;
		}
		case 100:
		case 101:
		case 102:
		case 103:
		case 104:
		case 105:
		case 106:
		case 107:
		case 108:
		case 109: {
			return 270000;
		}
		case 110:
		case 111:
		case 112:
		case 113:
		case 114:
		case 115:
		case 116:
		case 117:
		case 118:
		case 119: {
			return 350000;
		}
		case 120:
		case 121:
		case 122:
		case 123:
		case 124:
		case 125:
		case 126:
		case 127:
		case 128:
		case 129: {
			return 440000;
		}
		case 130:
		case 131:
		case 132:
		case 133:
		case 134:
		case 135:
		case 136:
		case 137:
		case 138:
		case 139: {
			return 540000;
		}
		case 140:
		case 141:
		case 142:
		case 143:
		case 144:
		case 145:
		case 146:
		case 147:
		case 148:
		case 149: {
			return 650000;
		}
		case 150:
		case 151:
		case 152:
		case 153:
		case 154: {
			return 760000;
		}
		case 155:
		case 156:
		case 157:
		case 158:
		case 159: {
			return 880000;
		}
		case 160:
		case 161:
		case 162:
		case 163:
		case 164: {
			return 1000000;
		}
		case 165:
		case 166:
		case 167:
		case 168:
		case 169: {
			return 1130000;
		}
		case 170:
		case 171:
		case 172:
		case 173:
		case 174: {
			return 1300000;
		}
		case 175:
		case 176:
		case 177:
		case 178:
		case 179: {
			return 1500000;
		}
		case 180:
		case 181:
		case 182:
		case 183:
		case 184: {
			return 1700000;
		}
		case 185:
		case 186:
		case 187:
		case 188:
		case 189: {
			return 2000000;
		}
		case 190:
		case 191:
		case 192:
		case 193:
		case 194: {
			return 2500000;
		}
		case 195:
		case 196:
		case 197:
		case 198:
		case 199:
		case 200: {
			return 3000000;
		}
		default: {
			return 65000;
		}
		}
	}

	public static String translateMsg(String msg) {
		String alpha = "a b c d e f g h i j k l n o p q r s t u v w x y z";
		String[] split;
		for (int length = (split = alpha.split(" ")).length, j = 0; j < length; ++j) {
			final String i = split[j];
			msg = msg.replace(i, "m");
		}
		alpha = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
		String[] split2;
		for (int length2 = (split2 = alpha.split(" ")).length, k = 0; k < length2; ++k) {
			final String i = split2[k];
			msg = msg.replace(i, "H");
		}
		return msg;
	}
}
