// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job;

import java.util.ArrayList;

import org.aestia.common.Formulas;

public class JobConstant {
	public static final int JOB_BASE = 1;
	public static final int JOB_BUCHERON = 2;
	public static final int JOB_F_EPEE = 11;
	public static final int JOB_S_ARC = 13;
	public static final int JOB_F_MARTEAU = 14;
	public static final int JOB_CORDONIER = 15;
	public static final int JOB_BIJOUTIER = 16;
	public static final int JOB_F_DAGUE = 17;
	public static final int JOB_S_BATON = 18;
	public static final int JOB_S_BAGUETTE = 19;
	public static final int JOB_F_PELLE = 20;
	public static final int JOB_MINEUR = 24;
	public static final int JOB_BOULANGER = 25;
	public static final int JOB_ALCHIMISTE = 26;
	public static final int JOB_TAILLEUR = 27;
	public static final int JOB_PAYSAN = 28;
	public static final int JOB_F_HACHES = 31;
	public static final int JOB_PECHEUR = 36;
	public static final int JOB_CHASSEUR = 41;
	public static final int JOB_FM_DAGUE = 43;
	public static final int JOB_FM_EPEE = 44;
	public static final int JOB_FM_MARTEAU = 45;
	public static final int JOB_FM_PELLE = 46;
	public static final int JOB_FM_HACHES = 47;
	public static final int JOB_SM_ARC = 48;
	public static final int JOB_SM_BAGUETTE = 49;
	public static final int JOB_SM_BATON = 50;
	public static final int JOB_BOUCHER = 56;
	public static final int JOB_POISSONNIER = 58;
	public static final int JOB_F_BOUCLIER = 60;
	public static final int JOB_CORDOMAGE = 62;
	public static final int JOB_JOAILLOMAGE = 63;
	public static final int JOB_COSTUMAGE = 64;
	public static final int JOB_BRICOLEUR = 65;
	public static final int JOB_JOAILLER = 66;
	public static final int JOB_BIJOUTIER2 = 67;
	public static final int IOBJECT_STATE_FULL = 1;
	public static final int IOBJECT_STATE_EMPTYING = 2;
	public static final int IOBJECT_STATE_EMPTY = 3;
	public static final int IOBJECT_STATE_EMPTY2 = 4;
	public static final int IOBJECT_STATE_FULLING = 5;
	public static final int[][] JOB_ACTION;
	public static final int[][] JOB_PROTECTORS;

	static {
		JOB_ACTION = new int[][] { { 101 }, { 6, 303 }, { 39, 473 }, { 40, 476 }, { 10, 460 }, { 141, 2357 },
				{ 139, 2358 }, { 37, 471 }, { 154, 7013 }, { 33, 461 }, { 41, 474 }, { 34, 449 }, { 174, 7925 },
				{ 155, 7016 }, { 38, 472 }, { 35, 470 }, { 158, 7014 }, { 48 }, { 32 }, { 24, 312 }, { 25, 441 },
				{ 26, 442 }, { 28, 443 }, { 56, 445 }, { 162, 7032 }, { 55, 444 }, { 29, 350 }, { 31, 446 },
				{ 30, 313 }, { 161, 7033 }, { 133 }, { 124, 1782, 1844, 603 }, { 125, 1844, 603, 1847, 1794 },
				{ 126, 603, 1847, 1794, 1779 }, { 127, 1847, 1794, 1779, 1801 }, { 128, 598, 1757, 1750 },
				{ 129, 1757, 1805, 600 }, { 130, 1805, 1750, 1784, 600 }, { 131, 600, 1805, 602, 1784 }, { 136, 2187 },
				{ 140, 1759 }, { 140, 1799 }, { 23 }, { 68, 421 }, { 69, 428 }, { 71, 395 }, { 72, 380 }, { 73, 593 },
				{ 74, 594 }, { 160, 7059 }, { 122 }, { 47 }, { 45, 289 }, { 53, 400 }, { 57, 533 }, { 46, 401 },
				{ 50, 423 }, { 52, 532 }, { 159, 7018 }, { 58, 405 }, { 54, 425 }, { 109 }, { 27 }, { 135 }, { 134 },
				{ 132 }, { 64 }, { 123 }, { 63 }, { 11 }, { 12 }, { 13 }, { 14 }, { 145 }, { 20 }, { 144 }, { 19 },
				{ 142 }, { 18 }, { 146 }, { 21 }, { 65 }, { 143 }, { 115 }, { 1 }, { 116 }, { 113 }, { 117 }, { 120 },
				{ 119 }, { 118 }, { 165 }, { 166 }, { 167 }, { 163 }, { 164 }, { 169 }, { 168 }, { 171 }, { 182 },
				{ 15 }, { 149 }, { 17 }, { 147 }, { 16 }, { 148 }, { 156 }, { 151 }, { 110 } };
		JOB_PROTECTORS = new int[][] { { 684, 289 }, { 684, 2018 }, { 685, 400 }, { 685, 2032 }, { 686, 533 },
				{ 686, 1671 }, { 687, 401 }, { 687, 2021 }, { 688, 423 }, { 688, 2026 }, { 689, 532 }, { 689, 2029 },
				{ 690, 7018 }, { 691, 405 }, { 692, 425 }, { 692, 2035 }, { 693, 312 }, { 694, 441 }, { 695, 442 },
				{ 696, 443 }, { 697, 445 }, { 698, 444 }, { 699, 7032 }, { 700, 350 }, { 701, 446 }, { 702, 313 },
				{ 703, 7033 }, { 704, 421 }, { 705, 428 }, { 706, 395 }, { 707, 380 }, { 708, 593 }, { 709, 594 },
				{ 710, 7059 }, { 711, 303 }, { 712, 473 }, { 713, 476 }, { 714, 460 }, { 715, 2358 }, { 716, 2357 },
				{ 717, 471 }, { 718, 461 }, { 719, 7013 }, { 720, 7925 }, { 721, 474 }, { 722, 449 }, { 723, 7016 },
				{ 724, 470 }, { 725, 7014 }, { 726, 1782 }, { 726, 1790 }, { 727, 607 }, { 727, 1844 }, { 727, 1846 },
				{ 728, 603 }, { 729, 598 }, { 730, 1757 }, { 730, 1759 }, { 731, 1750 }, { 732, 1847 }, { 732, 1749 },
				{ 733, 1794 }, { 733, 1796 }, { 734, 1805 }, { 734, 1807 }, { 735, 600 }, { 735, 1799 }, { 736, 1779 },
				{ 736, 1792 }, { 737, 1784 }, { 737, 1788 }, { 738, 1801 }, { 738, 1803 }, { 739, 602 },
				{ 739, 1853 } };
	}

	public static int getTotalCaseByJobLevel(final int lvl) {
		if (lvl < 10) {
			return 2;
		}
		if (lvl == 100) {
			return 9;
		}
		return lvl / 20 + 3;
	}

	public static int getChanceForMaxCase(final int lvl) {
		if (lvl < 10) {
			return 50;
		}
		return 54 + (lvl / 10 - 1) * 5;
	}

	public static boolean isJobAction(final int a) {
		for (int v = 0; v < JobConstant.JOB_ACTION.length; ++v) {
			if (JobConstant.JOB_ACTION[v][0] == a) {
				return true;
			}
		}
		return false;
	}

	public static int getObjectByJobSkill(final int skID) {
		for (int v = 0; v < JobConstant.JOB_ACTION.length; ++v) {
			if (JobConstant.JOB_ACTION[v][0] == skID) {
				if (JobConstant.JOB_ACTION[v].length > 2) {
					return JobConstant.JOB_ACTION[v][Formulas.getRandomValue(1, JobConstant.JOB_ACTION[v].length - 1)];
				}
				if (JobConstant.JOB_ACTION[v].length > 1) {
					return JobConstant.JOB_ACTION[v][1];
				}
			}
		}
		return -1;
	}

	public static int getChanceByNbrCaseByLvl(final int lvl, final int nbr) {
		return 100;
	}

	public static boolean isMageJob(final int id) {
		return (id > 42 && id < 51) || (id > 61 && id < 65);
	}

	public static String actionMetier(final int oficio) {
		switch (oficio) {
		case 62: {
			return "163;164";
		}
		case 63: {
			return "169;168";
		}
		case 64: {
			return "165;166;167";
		}
		case 45: {
			return "116";
		}
		case 46: {
			return "117";
		}
		case 67: {
			return "115";
		}
		case 43: {
			return "1";
		}
		case 44: {
			return "113";
		}
		case 48: {
			return "118";
		}
		case 49: {
			return "119";
		}
		case 50: {
			return "120";
		}
		default: {
			return "";
		}
		}
	}

	public static int getStatIDRune(final int statID) {
		int multi = 1;
		if (statID == 118 || statID == 126 || statID == 125 || statID == 119 || statID == 123 || statID == 158
				|| statID == 174) {
			multi = 1;
		} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
			multi = 2;
		} else if (statID == 124 || statID == 176) {
			multi = 3;
		} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
			multi = 4;
		} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
			multi = 5;
		} else if (statID == 225) {
			multi = 6;
		} else if (statID == 178 || statID == 112) {
			multi = 7;
		} else if (statID == 115 || statID == 182) {
			multi = 8;
		} else if (statID == 117) {
			multi = 9;
		} else if (statID == 128) {
			multi = 10;
		} else if (statID == 111) {
			multi = 10;
		}
		return multi;
	}

	public static int getProtectorLvl(final int lvl) {
		if (lvl < 40) {
			return 10;
		}
		if (lvl < 80) {
			return 20;
		}
		if (lvl < 120) {
			return 30;
		}
		if (lvl < 160) {
			return 40;
		}
		if (lvl < 200) {
			return 50;
		}
		return 50;
	}

	public static ArrayList<JobAction> getPosActionsToJob(final int tID, final int lvl) {
		final ArrayList<JobAction> list = new ArrayList<JobAction>();
		final int timeWin = lvl * 100;
		final int dropWin = lvl / 5;
		switch (tID) {
		case 16: {
			list.add(new JobAction(11, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(12, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 27: {
			list.add(new JobAction(64, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(123, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(63, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 60: {
			list.add(new JobAction(156, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 65: {
			list.add(new JobAction(171, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(182, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 15: {
			list.add(new JobAction(13, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(14, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 13: {
			list.add(new JobAction(15, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(149, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 18: {
			list.add(new JobAction(17, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(147, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 19: {
			list.add(new JobAction(16, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(148, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 62: {
			list.add(new JobAction(163, 3, 0, true, lvl, 0));
			list.add(new JobAction(164, 3, 0, true, lvl, 0));
			break;
		}
		case 63: {
			list.add(new JobAction(169, 3, 0, true, lvl, 0));
			list.add(new JobAction(168, 3, 0, true, lvl, 0));
			break;
		}
		case 64: {
			list.add(new JobAction(165, 3, 0, true, lvl, 0));
			list.add(new JobAction(167, 3, 0, true, lvl, 0));
			list.add(new JobAction(166, 3, 0, true, lvl, 0));
			break;
		}
		case 11: {
			list.add(new JobAction(20, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(145, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 17: {
			list.add(new JobAction(142, 3, 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(18, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 14: {
			list.add(new JobAction(19, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(144, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 20: {
			list.add(new JobAction(21, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(146, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 31: {
			list.add(new JobAction(65, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(143, 3, 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 47: {
			list.add(new JobAction(115, 3, 0, true, lvl, 0));
			break;
		}
		case 43: {
			list.add(new JobAction(1, 3, 0, true, lvl, 0));
			break;
		}
		case 44: {
			list.add(new JobAction(113, 3, 0, true, lvl, 0));
			break;
		}
		case 45: {
			list.add(new JobAction(116, 3, 0, true, lvl, 0));
			break;
		}
		case 46: {
			list.add(new JobAction(117, 3, 0, true, lvl, 0));
			break;
		}
		case 48: {
			list.add(new JobAction(118, 3, 0, true, lvl, 0));
			break;
		}
		case 50: {
			list.add(new JobAction(120, 3, 0, true, lvl, 0));
			break;
		}
		case 49: {
			list.add(new JobAction(119, 3, 0, true, lvl, 0));
			break;
		}
		case 41: {
			list.add(new JobAction(132, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 56: {
			list.add(new JobAction(134, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 58: {
			list.add(new JobAction(135, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 25: {
			list.add(new JobAction(27, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(109, 3, 0, true, 100, -1));
			break;
		}
		case 24: {
			if (lvl > 99) {
				list.add(new JobAction(161, -19 + dropWin, -18 + dropWin, false, 12000 - timeWin, 60));
			}
			if (lvl > 79) {
				list.add(new JobAction(30, -15 + dropWin, -14 + dropWin, false, 12000 - timeWin, 55));
			}
			if (lvl > 69) {
				list.add(new JobAction(31, -13 + dropWin, -12 + dropWin, false, 12000 - timeWin, 50));
			}
			if (lvl > 59) {
				list.add(new JobAction(29, -11 + dropWin, -10 + dropWin, false, 12000 - timeWin, 40));
			}
			if (lvl > 49) {
				list.add(new JobAction(55, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 35));
				list.add(new JobAction(162, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 35));
			}
			if (lvl > 39) {
				list.add(new JobAction(56, -7 + dropWin, -6 + dropWin, false, 12000 - timeWin, 30));
			}
			if (lvl > 29) {
				list.add(new JobAction(28, -5 + dropWin, -4 + dropWin, false, 12000 - timeWin, 25));
			}
			if (lvl > 19) {
				list.add(new JobAction(26, -3 + dropWin, -2 + dropWin, false, 12000 - timeWin, 20));
			}
			if (lvl > 9) {
				list.add(new JobAction(25, -1 + dropWin, 0 + dropWin, false, 12000 - timeWin, 15));
			}
			list.add(new JobAction(24, 1 + dropWin, 2 + dropWin, false, 12000 - timeWin, 10));
			list.add(new JobAction(32, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(48, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 36: {
			if (lvl > 74) {
				list.add(new JobAction(131, 0, 1, false, 12000 - timeWin, 35));
			}
			if (lvl > 69) {
				list.add(new JobAction(127, 0, 1, false, 12000 - timeWin, 35));
			}
			if (lvl > 49) {
				list.add(new JobAction(130, 0, 1, false, 12000 - timeWin, 30));
			}
			if (lvl > 39) {
				list.add(new JobAction(126, 0, 1, false, 12000 - timeWin, 25));
			}
			if (lvl > 19) {
				list.add(new JobAction(129, 0, 1, false, 12000 - timeWin, 20));
			}
			if (lvl > 9) {
				list.add(new JobAction(125, 0, 1, false, 12000 - timeWin, 15));
			}
			list.add(new JobAction(140, 0, 1, false, 12000 - timeWin, 50));
			list.add(new JobAction(136, 1, 1, false, 12000 - timeWin, 5));
			list.add(new JobAction(124, 0, 1, false, 12000 - timeWin, 10));
			list.add(new JobAction(128, 0, 1, false, 12000 - timeWin, 10));
			list.add(new JobAction(133, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 26: {
			if (lvl > 49) {
				list.add(new JobAction(160, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 35));
				list.add(new JobAction(74, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 35));
			}
			if (lvl > 39) {
				list.add(new JobAction(73, -7 + dropWin, -6 + dropWin, false, 12000 - timeWin, 30));
			}
			if (lvl > 29) {
				list.add(new JobAction(72, -5 + dropWin, -4 + dropWin, false, 12000 - timeWin, 25));
			}
			if (lvl > 19) {
				list.add(new JobAction(71, -3 + dropWin, -2 + dropWin, false, 12000 - timeWin, 20));
			}
			if (lvl > 9) {
				list.add(new JobAction(69, -1 + dropWin, 0 + dropWin, false, 12000 - timeWin, 15));
			}
			list.add(new JobAction(68, 1 + dropWin, 2 + dropWin, false, 12000 - timeWin, 10));
			list.add(new JobAction(23, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 2: {
			if (lvl > 99) {
				list.add(new JobAction(158, -19 + dropWin, -18 + dropWin, false, 12000 - timeWin, 75));
			}
			if (lvl > 89) {
				list.add(new JobAction(35, -17 + dropWin, -16 + dropWin, false, 12000 - timeWin, 70));
			}
			if (lvl > 79) {
				list.add(new JobAction(38, -15 + dropWin, -14 + dropWin, false, 12000 - timeWin, 65));
				list.add(new JobAction(155, -15 + dropWin, -14 + dropWin, false, 12000 - timeWin, 65));
			}
			if (lvl > 74) {
				list.add(new JobAction(174, -14 + dropWin, -13 + dropWin, false, 12000 - timeWin, 55));
			}
			if (lvl > 69) {
				list.add(new JobAction(34, -13 + dropWin, -12 + dropWin, false, 12000 - timeWin, 50));
			}
			if (lvl > 59) {
				list.add(new JobAction(41, -11 + dropWin, -10 + dropWin, false, 12000 - timeWin, 45));
			}
			if (lvl > 49) {
				list.add(new JobAction(33, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 40));
				list.add(new JobAction(154, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 40));
			}
			if (lvl > 39) {
				list.add(new JobAction(37, -7 + dropWin, -6 + dropWin, false, 12000 - timeWin, 35));
			}
			if (lvl > 34) {
				list.add(new JobAction(139, -6 + dropWin, -5 + dropWin, false, 12000 - timeWin, 30));
				list.add(new JobAction(141, -6 + dropWin, -5 + dropWin, false, 12000 - timeWin, 30));
			}
			if (lvl > 29) {
				list.add(new JobAction(10, -5 + dropWin, -4 + dropWin, false, 12000 - timeWin, 25));
			}
			if (lvl > 19) {
				list.add(new JobAction(40, -3 + dropWin, -2 + dropWin, false, 12000 - timeWin, 20));
			}
			if (lvl > 9) {
				list.add(new JobAction(39, -1 + dropWin, 0 + dropWin, false, 12000 - timeWin, 15));
			}
			list.add(new JobAction(6, 1 + dropWin, 2 + dropWin, false, 12000 - timeWin, 10));
			list.add(new JobAction(101, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			break;
		}
		case 28: {
			if (lvl > 69) {
				list.add(new JobAction(54, -13 + dropWin, -12 + dropWin, false, 12000 - timeWin, 45));
			}
			if (lvl > 59) {
				list.add(new JobAction(58, -11 + dropWin, -10 + dropWin, false, 12000 - timeWin, 40));
			}
			if (lvl > 49) {
				list.add(new JobAction(159, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 35));
				list.add(new JobAction(52, -9 + dropWin, -8 + dropWin, false, 12000 - timeWin, 35));
			}
			if (lvl > 39) {
				list.add(new JobAction(50, -7 + dropWin, -6 + dropWin, false, 12000 - timeWin, 30));
			}
			if (lvl > 29) {
				list.add(new JobAction(46, -5 + dropWin, -4 + dropWin, false, 12000 - timeWin, 25));
			}
			if (lvl > 19) {
				list.add(new JobAction(57, -3 + dropWin, -2 + dropWin, false, 12000 - timeWin, 20));
			}
			if (lvl > 9) {
				list.add(new JobAction(53, -1 + dropWin, 0 + dropWin, false, 12000 - timeWin, 15));
			}
			list.add(new JobAction(45, 1 + dropWin, 2 + dropWin, false, 12000 - timeWin, 10));
			list.add(new JobAction(47, getTotalCaseByJobLevel(lvl), 0, true, getChanceForMaxCase(lvl), -1));
			list.add(new JobAction(122, 1, 0, true, 100, 10));
			break;
		}
		}
		return list;
	}

	public static int statRune(final int stat, final int cant) {
		switch (stat) {
		case 111: {
			return 1557;
		}
		case 112: {
			return 7435;
		}
		case 115: {
			return 7433;
		}
		case 117: {
			return 7438;
		}
		case 118: {
			if (cant > 70) {
				return 1551;
			}
			if (cant <= 70 && cant > 20) {
				return 1545;
			}
			return 1519;
		}
		case 119: {
			if (cant > 70) {
				return 1555;
			}
			if (cant <= 70 && cant > 20) {
				return 1549;
			}
			return 1524;
		}
		case 123: {
			if (cant > 70) {
				return 1556;
			}
			if (cant <= 70 && cant > 20) {
				return 1550;
			}
			return 1525;
		}
		case 124: {
			if (cant > 30) {
				return 1552;
			}
			if (cant <= 30 && cant > 10) {
				return 1546;
			}
			return 1521;
		}
		case 125: {
			if (cant > 230) {
				return 1554;
			}
			if (cant <= 230 && cant > 60) {
				return 1548;
			}
			return 1523;
		}
		case 126: {
			if (cant > 70) {
				return 1553;
			}
			if (cant <= 70 && cant > 20) {
				return 1547;
			}
			return 1522;
		}
		case 128: {
			return 1558;
		}
		case 138: {
			return 7436;
		}
		case 158: {
			if (cant > 300) {
				return 7445;
			}
			if (cant <= 300 && cant > 100) {
				return 7444;
			}
			return 7443;
		}
		case 174: {
			if (cant > 300) {
				return 7450;
			}
			if (cant <= 300 && cant > 100) {
				return 7449;
			}
			return 7448;
		}
		case 176: {
			if (cant > 5) {
				return 10662;
			}
			return 7451;
		}
		case 178: {
			return 7434;
		}
		case 182: {
			return 7442;
		}
		case 220: {
			return 7437;
		}
		case 225: {
			return 7446;
		}
		case 226: {
			return 7447;
		}
		case 240: {
			return 7452;
		}
		case 241: {
			return 7453;
		}
		case 242: {
			return 7454;
		}
		case 243: {
			return 7455;
		}
		case 244: {
			return 7456;
		}
		case 210: {
			return 7457;
		}
		case 211: {
			return 7458;
		}
		case 212: {
			return 7560;
		}
		case 213: {
			return 7459;
		}
		case 214: {
			return 7460;
		}
		default: {
			return 0;
		}
		}
	}

	public static int getDistCanne(final int temp) {
		switch (temp) {
		case 596:
		case 6661:
		case 8541: {
			return 2;
		}
		case 1866: {
			return 3;
		}
		case 1864:
		case 1865: {
			return 4;
		}
		case 1867:
		case 2188: {
			return 5;
		}
		case 1862:
		case 1863: {
			return 6;
		}
		case 1868: {
			return 7;
		}
		case 1860:
		case 1861: {
			return 8;
		}
		case 2366: {
			return 9;
		}
		default: {
			return 0;
		}
		}
	}

	public static int getPoissonRare(final int tID) {
		switch (tID) {
		case 598: {
			return 1786;
		}
		case 600: {
			return 1799;
		}
		case 602: {
			return 1853;
		}
		case 603: {
			return 1762;
		}
		case 1750: {
			return 1754;
		}
		case 1757: {
			return 1759;
		}
		case 1779: {
			return 1779;
		}
		case 1785: {
			return 1790;
		}
		case 1784: {
			return 1788;
		}
		case 1794: {
			return 1796;
		}
		case 1801: {
			return 1803;
		}
		case 1805: {
			return 1807;
		}
		case 1844: {
			return 1846;
		}
		case 1847: {
			return 1849;
		}
		default: {
			return -1;
		}
		}
	}
}
