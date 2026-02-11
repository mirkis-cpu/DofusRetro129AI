// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.common;

import java.util.ArrayList;
import java.util.Map;

import org.aestia.client.Player;
import org.aestia.game.world.World;
import org.aestia.job.JobStat;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.other.Action;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;

public class ConditionParser {
	public static boolean validConditions(final Player perso, String req) {
		if (req == null || req.equals("")) {
			return true;
		}
		if (req.contains("BI")) {
			return false;
		}
		if (perso == null) {
			return false;
		}
		final Jep jep = new Jep();
		req = req.replace("&", "&&").replace("=", "==").replace("|", "||").replace("!", "!=").replace("~", "==");
		if (req.contains("Sc")) {
			return true;
		}
		if (req.contains("Pg")) {
			return false;
		}
		if (req.contains("RO")) {
			return haveRO(req, perso);
		}
		if (req.contains("Mph")) {
			return haveMorph(req, perso);
		}
		if (req.contains("PO")) {
			req = havePO(req, perso);
		}
		if (req.contains("PN")) {
			req = canPN(req, perso);
		}
		if (req.contains("PJ")) {
			req = canPJ(req, perso);
		}
		if (req.contains("JOB")) {
			req = haveJOB(req, perso);
		}
		if (req.contains("NPC")) {
			return haveNPC(req, perso);
		}
		if (req.contains("QEt")) {
			return haveQEt(req, perso);
		}
		if (req.contains("QE")) {
			return haveQE(req, perso);
		}
		if (req.contains("QT")) {
			return haveQT(req, perso);
		}
		if (req.contains("Ce")) {
			return haveCe(req, perso);
		}
		if (req.contains("TiT")) {
			return haveTiT(req, perso);
		}
		if (req.contains("Ti")) {
			return haveTi(req, perso);
		}
		if (req.contains("Qa")) {
			return haveQa(req, perso);
		}
		if (req.contains("Pj")) {
			return havePj(req, perso);
		}
		if (req.contains("AM")) {
			return haveMetier(req, perso);
		}
		try {
			jep.addVariable("CI", perso.getTotalStats().getEffect(126));
			jep.addVariable("CV", perso.getTotalStats().getEffect(125));
			jep.addVariable("CA", perso.getTotalStats().getEffect(119));
			jep.addVariable("CW", perso.getTotalStats().getEffect(124));
			jep.addVariable("CC", perso.getTotalStats().getEffect(123));
			jep.addVariable("CS", perso.getTotalStats().getEffect(118));
			jep.addVariable("CM", perso.getStats().getEffect(128));
			jep.addVariable("Ci", perso.getStats().getEffect(126));
			jep.addVariable("Cs", perso.getStats().getEffect(118));
			jep.addVariable("Cv", perso.getStats().getEffect(125));
			jep.addVariable("Ca", perso.getStats().getEffect(119));
			jep.addVariable("Cw", perso.getStats().getEffect(124));
			jep.addVariable("Cc", perso.getStats().getEffect(123));
			jep.addVariable("Ps", perso.get_align());
			jep.addVariable("Pa", perso.getALvl());
			jep.addVariable("PP", perso.getGrade());
			jep.addVariable("PL", perso.getLevel());
			jep.addVariable("PK", perso.get_kamas());
			jep.addVariable("PG", perso.getClasse());
			jep.addVariable("PS", perso.getSexe());
			jep.addVariable("PZ", 1.0);
			jep.addVariable("PX", perso.getGroupe() != null);
			jep.addVariable("PW", perso.getMaxPod());
			if (perso.getCurMap().getSubArea() != null) {
				jep.addVariable("PB", perso.getCurMap().getSubArea().getId());
			}
			jep.addVariable("PR", (perso.getWife() > 0) ? 1 : 0);
			jep.addVariable("SI", perso.getCurMap().getId());
			jep.addVariable("MiS", perso.getId());
			jep.addVariable("MA", perso.getAlignMap());
			jep.addVariable("PSB", perso.getAccount().getPoints());
			jep.addVariable("CF",
					(perso.getObjetByPos(24) == null) ? -1 : perso.getObjetByPos(24).getTemplate().getId());
			jep.parse(req);
			final Object result = jep.evaluate();
			boolean ok = false;
			if (result != null) {
				ok = Boolean.valueOf(result.toString());
			}
			return ok;
		} catch (JepException e) {
			e.printStackTrace();
			Console.println("Jep exception : " + e.getMessage(), Console.Color.ERROR);
			return false;
		}
	}

	private static boolean haveMorph(final String c, final Player p) {
		if (c.equalsIgnoreCase("")) {
			return false;
		}
		int morph = -1;
		try {
			morph = Integer.parseInt(c.contains("==") ? c.split("==")[1] : c.split("!=")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (p.getMorphId() == morph) {
			return c.contains("==");
		}
		return !c.contains("==");
	}

	private static boolean haveMetier(final String c, final Player p) {
		if (p.getMetiers() == null || p.getMetiers().isEmpty()) {
			return false;
		}
		for (final Map.Entry<Integer, JobStat> entry : p.getMetiers().entrySet()) {
			if (entry.getValue() != null) {
				return true;
			}
		}
		return false;
	}

	private static boolean havePj(final String c, final Player p) {
		if (c.equalsIgnoreCase("")) {
			return false;
		}
		String[] split;
		for (int length = (split = c.split("\\|\\|")).length, i = 0; i < length; ++i) {
			final String s = split[i];
			final String[] k = s.split("==");
			int id = -1;
			try {
				id = Integer.parseInt(k[1]);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			if (p.getMetierByID(id) != null) {
				return true;
			}
		}
		return false;
	}

	private static boolean haveQa(final String req, final Player player) {
		final int id = Integer.parseInt(req.contains("==") ? req.split("==")[1] : req.split("!=")[1]);
		final Quest q = Quest.getQuestById(id);
		if (q == null) {
			return !req.contains("==");
		}
		final Quest.Quest_Perso qp = player.getQuestPersoByQuest(q);
		if (qp == null) {
			return !req.contains("==");
		}
		return !qp.isFinish() || !req.contains("==");
	}

	private static boolean haveQEt(final String req, final Player player) {
		final int id = Integer.parseInt(req.contains("==") ? req.split("==")[1] : req.split("!=")[1]);
		final Quest_Etape qe = Quest_Etape.getQuestEtapeById(id);
		if (qe != null) {
			final Quest q = qe.getQuestData();
			if (q != null) {
				final Quest.Quest_Perso qp = player.getQuestPersoByQuest(q);
				if (qp != null) {
					final Quest_Etape current = q.getQuestEtapeCurrent(qp);
					if (current == null) {
						return false;
					}
					if (current.getId() == qe.getId()) {
						return req.contains("==");
					}
				}
			}
		}
		return false;
	}

	private static boolean haveTiT(final String req, final Player player) {
		if (req.contains("==")) {
			final String split = req.split("==")[1];
			if (split.contains("&&")) {
				final int item = Integer.parseInt(split.split("&&")[0]);
				final int time = Integer.parseInt(split.split("&&")[1]);
				final int item2 = Integer.parseInt(split.split("&&")[2]);
				if (player.hasItemTemplate(item2, 1) && player.hasItemTemplate(item, 1)) {
					final long timeStamp = Long.parseLong(player.getItemTemplate(item, 1).getTxtStat().get(805));
					if (System.currentTimeMillis() - timeStamp <= time) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean haveTi(final String req, final Player player) {
		if (req.contains("==")) {
			final String split = req.split("==")[1];
			if (split.contains(",")) {
				final String[] split2 = split.split(",");
				final int item = Integer.parseInt(split2[0]);
				final int time = Integer.parseInt(split2[1]) * 60 * 1000;
				if (player.hasItemTemplate(item, 1)) {
					final long timeStamp = Long.parseLong(player.getItemTemplate(item, 1).getTxtStat().get(805));
					if (System.currentTimeMillis() - timeStamp > time) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean haveCe(final String req, final Player player) {
		final Map<Integer, World.Couple<Integer, Integer>> dopeuls = Action.getDopeul();
		final org.aestia.map.Map map = player.getCurMap();
		if (!dopeuls.containsKey((int) map.getId())) {
			return false;
		}
		final World.Couple<Integer, Integer> couple = dopeuls.get((int) map.getId());
		if (couple == null) {
			return false;
		}
		final int IDmob = couple.first;
		final int certificat = Constant.getCertificatByDopeuls(IDmob);
		if (certificat == -1) {
			return false;
		}
		if (player.hasItemTemplate(certificat, 1)) {
			String txt = player.getItemTemplate(certificat, 1).getTxtStat().get(805);
			if (txt.contains("#")) {
				txt = txt.split("#")[3];
			}
			final long timeStamp = Long.parseLong(txt);
			return System.currentTimeMillis() - timeStamp > 86400000L;
		}
		return true;
	}

	private static boolean haveQE(final String req, final Player player) {
		if (player == null) {
			return false;
		}
		final int id = Integer.parseInt(req.contains("==") ? req.split("==")[1] : req.split("!=")[1]);
		final Quest.Quest_Perso qp = player.getQuestPersoByQuestId(id);
		if (req.contains("==")) {
			return qp != null && !qp.isFinish();
		}
		return qp == null || qp.isFinish();
	}

	private static boolean haveQT(final String req, final Player player) {
		final int id = Integer.parseInt(req.contains("==") ? req.split("==")[1] : req.split("!=")[1]);
		final Quest.Quest_Perso quest = player.getQuestPersoByQuestId(id);
		if (req.contains("==")) {
			return quest != null && quest.isFinish();
		}
		return quest == null || !quest.isFinish();
	}

	private static boolean haveNPC(final String req, final Player perso) {
		switch (perso.getCurMap().getId()) {
		case 9052: {
			if (perso.getCurCell().getId() == 268 && perso.get_orientation() == 7) {
				return true;
			}
		}
		case 8905: {
			final ArrayList<Integer> cell = new ArrayList<Integer>();
			String[] split;
			for (int length = (split = "168,197,212,227,242,183,213,214,229,244,245,259"
					.split("\\,")).length, j = 0; j < length; ++j) {
				final String i = split[j];
				cell.add(Integer.parseInt(i));
			}
			if (cell.contains(perso.getCurCell().getId())) {
				return true;
			}
			break;
		}
		}
		return false;
	}

	public static boolean haveRO(final String condition, final Player player) {
		try {
			final String[] split2;
			if ((split2 = condition.split("&&")).length != 0) {
				final String cond = split2[0];
				final String[] split = cond.split("\\==")[1].split("\\,");
				final int id = Integer.parseInt(split[0]);
				final int qua = Integer.parseInt(split[1]);
				if (player.hasItemTemplate(id, qua)) {
					player.removeByTemplateID(id, qua);
					return true;
				}
				SocketManager.GAME_SEND_Im_PACKET(player, "14");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String havePO(final String cond, final Player perso) {
		boolean Jump = false;
		boolean ContainsPO = false;
		boolean CutFinalLenght = true;
		String copyCond = "";
		int finalLength = 0;
		if (cond.contains("&&")) {
			String[] split;
			for (int length = (split = cond.split("&&")).length, i = 0; i < length; ++i) {
				final String cur = split[i];
				if (cond.contains("==")) {
					String[] split2;
					for (int length2 = (split2 = cur.split("==")).length, j = 0; j < length2; ++j) {
						final String cur2 = split2[j];
						if (cur2.contains("PO")) {
							ContainsPO = true;
						} else if (Jump) {
							copyCond = String.valueOf(copyCond) + cur2;
							Jump = false;
						} else if (!cur2.contains("PO") && !ContainsPO) {
							copyCond = String.valueOf(copyCond) + cur2 + "==";
							Jump = true;
						} else if (!cur2.contains("!=")) {
							ContainsPO = false;
							if (perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "=="
										+ Integer.parseInt(cur2);
							} else {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "==" + 0;
							}
						}
					}
				}
				if (cond.contains("!=")) {
					String[] split3;
					for (int length3 = (split3 = cur.split("!=")).length, k = 0; k < length3; ++k) {
						final String cur2 = split3[k];
						if (cur2.contains("PO")) {
							ContainsPO = true;
						} else if (Jump) {
							copyCond = String.valueOf(copyCond) + cur2;
							Jump = false;
						} else if (!cur2.contains("PO") && !ContainsPO) {
							copyCond = String.valueOf(copyCond) + cur2 + "!=";
							Jump = true;
						} else if (!cur2.contains("==")) {
							ContainsPO = false;
							if (perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "!="
										+ Integer.parseInt(cur2);
							} else {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "!=" + 0;
							}
						}
					}
				}
				copyCond = String.valueOf(copyCond) + "&&";
			}
		} else if (cond.contains("||")) {
			String[] split4;
			for (int length4 = (split4 = cond.split("\\|\\|")).length, l = 0; l < length4; ++l) {
				final String cur = split4[l];
				if (cond.contains("==")) {
					String[] split5;
					for (int length5 = (split5 = cur.split("==")).length, n = 0; n < length5; ++n) {
						final String cur2 = split5[n];
						if (cur2.contains("PO")) {
							ContainsPO = true;
						} else if (Jump) {
							copyCond = String.valueOf(copyCond) + cur2;
							Jump = false;
						} else if (!cur2.contains("PO") && !ContainsPO) {
							copyCond = String.valueOf(copyCond) + cur2 + "==";
							Jump = true;
						} else if (!cur2.contains("!=")) {
							ContainsPO = false;
							if (perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "=="
										+ Integer.parseInt(cur2);
							} else {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "==" + 0;
							}
						}
					}
				}
				if (cond.contains("!=")) {
					String[] split6;
					for (int length6 = (split6 = cur.split("!=")).length, n2 = 0; n2 < length6; ++n2) {
						final String cur2 = split6[n2];
						if (cur2.contains("PO")) {
							ContainsPO = true;
						} else if (Jump) {
							copyCond = String.valueOf(copyCond) + cur2;
							Jump = false;
						} else if (!cur2.contains("PO") && !ContainsPO) {
							copyCond = String.valueOf(copyCond) + cur2 + "!=";
							Jump = true;
						} else if (!cur2.contains("==")) {
							ContainsPO = false;
							if (perso.hasItemTemplate(Integer.parseInt(cur2), 1)) {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "!="
										+ Integer.parseInt(cur2);
							} else {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur2) + "!=" + 0;
							}
						}
					}
				}
				copyCond = String.valueOf(copyCond) + "||";
			}
		} else {
			CutFinalLenght = false;
			if (cond.contains("==")) {
				String[] split7;
				for (int length7 = (split7 = cond.split("==")).length, n3 = 0; n3 < length7; ++n3) {
					final String cur = split7[n3];
					if (!cur.contains("PO")) {
						if (!cur.contains("!=")) {
							if (perso.hasItemTemplate(Integer.parseInt(cur), 1)) {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur) + "=="
										+ Integer.parseInt(cur);
							} else {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur) + "==" + 0;
							}
						}
					}
				}
			}
			if (cond.contains("!=")) {
				String[] split8;
				for (int length8 = (split8 = cond.split("!=")).length, n4 = 0; n4 < length8; ++n4) {
					final String cur = split8[n4];
					if (!cur.contains("PO")) {
						if (!cur.contains("==")) {
							if (perso.hasItemTemplate(Integer.parseInt(cur), 1)) {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur) + "!="
										+ Integer.parseInt(cur);
							} else {
								copyCond = String.valueOf(copyCond) + Integer.parseInt(cur) + "!=" + 0;
							}
						}
					}
				}
			}
		}
		if (CutFinalLenght) {
			finalLength = copyCond.length() - 2;
			copyCond = copyCond.substring(0, finalLength);
		}
		return copyCond;
	}

	public static String canPN(final String cond, final Player perso) {
		String copyCond = "";
		String[] split;
		for (int length = (split = cond.split("==")).length, i = 0; i < length; ++i) {
			final String cur = split[i];
			if (cur.contains("PN")) {
				copyCond = String.valueOf(copyCond) + "1==";
			} else if (perso.getName().toLowerCase().compareTo(cur) == 0) {
				copyCond = String.valueOf(copyCond) + "1";
			} else {
				copyCond = String.valueOf(copyCond) + "0";
			}
		}
		return copyCond;
	}

	public static String canPJ(final String cond, final Player perso) {
		String copyCond = "";
		if (cond.contains("==")) {
			final String[] cur = cond.split("==");
			if (perso.getMetierByID(Integer.parseInt(cur[1])) != null) {
				copyCond = "1==1";
			} else {
				copyCond = "1==0";
			}
		} else if (cond.contains(">")) {
			if (cond.contains("||")) {
				String[] split;
				for (int length = (split = cond.split("\\|\\|")).length, j = 0; j < length; ++j) {
					final String cur2 = split[j];
					if (cur2.contains(">")) {
						final String[] _cur = cur2.split(">");
						if (_cur[1].contains(",")) {
							final String[] m = _cur[1].split(",");
							final JobStat js = perso.getMetierByID(Integer.parseInt(m[0]));
							if (!copyCond.equalsIgnoreCase("")) {
								copyCond = String.valueOf(copyCond) + "||";
							}
							if (js != null) {
								copyCond = String.valueOf(copyCond) + js.get_lvl() + ">" + m[1];
							} else {
								copyCond = String.valueOf(copyCond) + "1==0";
							}
						}
					}
				}
			} else {
				final String[] cur = cond.split(">");
				final String[] i = cur[1].split(",");
				final JobStat js2 = perso.getMetierByID(Integer.parseInt(i[0]));
				if (js2 != null) {
					copyCond = String.valueOf(js2.get_lvl()) + ">" + i[1];
				} else {
					copyCond = "1==0";
				}
			}
		}
		return copyCond;
	}

	public static String haveJOB(final String cond, final Player perso) {
		String copyCond = "";
		if (perso.getMetierByID(Integer.parseInt(cond.split("==")[1])) != null) {
			copyCond = "1==1";
		} else {
			copyCond = "0==1";
		}
		return copyCond;
	}

	public static boolean stackIfSimilar(final org.aestia.object.Object obj, final org.aestia.object.Object newObj,
			final boolean stackIfSimilar) {
		return obj.getTemplate().getId() == newObj.getTemplate().getId() && stackIfSimilar
				&& obj.getStats().isSameStats(newObj.getStats()) && newObj.getTemplate().getType() != 77
				&& !Constant.isIncarnationWeapon(newObj.getTemplate().getId()) && newObj.getTemplate().getType() != 85
				&& newObj.getTemplate().getType() != 93 && newObj.getTemplate().getType() != 97
				&& newObj.getTemplate().getType() != 113
				&& (newObj.getTemplate().getType() != 24 || Constant.isFlacGelee(obj.getTemplate().getId())
						|| Constant.isDoplon(obj.getTemplate().getId()))
				&& obj.getPosition() == -1 && newObj.getTemplate().getType() != 18;
	}
}
