// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.aestia.client.Player;
import org.aestia.client.other.Stalk;
import org.aestia.client.other.Stats;
import org.aestia.common.ConditionParser;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.PetEntry;
import org.aestia.entity.monster.Monster;
import org.aestia.entity.npc.Npc;
import org.aestia.entity.npc.NpcQuestion;
import org.aestia.game.GameClient;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.job.Job;
import org.aestia.job.JobStat;
import org.aestia.kernel.Config;
import org.aestia.kernel.Constant;
import org.aestia.map.Case;
import org.aestia.map.Map;
import org.aestia.map.laby.Dc;
import org.aestia.object.ObjectTemplate;
import org.aestia.object.entity.SoulStone;
import org.aestia.quest.Quest;

public class Action {
	private int id;
	private String args;
	private String cond;
	private Map map;

	public Action(final int id, final String args, final String cond, final Map map) {
		this.setId(id);
		this.setArgs(args);
		this.setCond(cond);
		this.setMap(map);
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getArgs() {
		return this.args;
	}

	public void setArgs(final String args) {
		this.args = args;
	}

	public String getCond() {
		return this.cond;
	}

	public void setCond(final String cond) {
		this.cond = cond;
	}

	public Map getMap() {
		return this.map;
	}

	public void setMap(final Map map) {
		this.map = map;
	}

	public static java.util.Map<Integer, World.Couple<Integer, Integer>> getDopeul() {
		final java.util.Map<Integer, World.Couple<Integer, Integer>> changeDopeul = new HashMap<Integer, World.Couple<Integer, Integer>>();
		changeDopeul.put(1549, Couple(167, 460));
		changeDopeul.put(1466, Couple(169, 465));
		changeDopeul.put(1558, Couple(168, 458));
		changeDopeul.put(1470, Couple(162, 464));
		changeDopeul.put(1469, Couple(164, 468));
		changeDopeul.put(1546, Couple(161, 461));
		changeDopeul.put(1554, Couple(160, 469));
		changeDopeul.put(6928, Couple(166, 462));
		changeDopeul.put(8490, Couple(2691, 466));
		changeDopeul.put(6926, Couple(163, 467));
		changeDopeul.put(1544, Couple(165, 459));
		changeDopeul.put(6949, Couple(455, 463));
		return changeDopeul;
	}

	private static World.Couple<Integer, Integer> Couple(final int i, final int j) {
		return new World.Couple<Integer, Integer>(i, j);
	}

	public boolean apply(final Player perso, Player target, final int itemID, int cellid) {
		if (perso == null) {
			return true;
		}
		if (!perso.isOnline()) {
			return true;
		}
		if (perso.get_fight() != null) {
			SocketManager.GAME_SEND_MESSAGE(perso, "<b>Action impossible,</b> vous \u00eates en combat !", "000000");
			return true;
		}
		if (!this.cond.equalsIgnoreCase("") && !this.cond.equalsIgnoreCase("-1")
				&& !ConditionParser.validConditions(perso, this.cond)) {
			SocketManager.GAME_SEND_Im_PACKET(perso, "119");
			return true;
		}
		if (perso.getGameClient() == null) {
			return true;
		}
		GameClient out = perso.getGameClient();
		switch (this.id) {
		case -22: {
			if (perso.getObjetByPos(24) == null) {
				break;
			}
			final int skinFollower = perso.getObjetByPos(24).getTemplate().getId();
			final int questId = Constant.getQuestByMobSkin(skinFollower);
			if (questId != -1) {
				perso.setMascotte(1);
				final int itemFollow = Constant.getItemByMobSkin(skinFollower);
				perso.removeByTemplateID(itemFollow, 1);
				break;
			}
			break;
		}
		case -11: {
			perso.teleport(Constant.getStartMap(perso.getClasse()), Constant.getStartCell(perso.getClasse()));
			SocketManager.GAME_SEND_WELCOME(perso);
			break;
		}
		case -10: {
			if (perso.get_align() == 1 || perso.get_align() == 2 || perso.get_align() == 3) {
				return true;
			}
			int ange = 0;
			int demon = 0;
			int total = 0;
			for (final Player i : World.getPlayers().values()) {
				if (i == null) {
					continue;
				}
				if (i.get_align() == 1) {
					++ange;
				}
				if (i.get_align() == 2) {
					++demon;
				}
				++total;
			}
			ange /= total;
			demon /= total;
			if (ange > demon) {
				perso.modifAlignement(2);
				break;
			}
			if (demon > ange) {
				perso.modifAlignement(1);
				break;
			}
			if (demon == ange) {
				perso.modifAlignement(Formulas.getRandomValue(1, 2));
				break;
			}
			break;
		}
		case -9: {
			perso.setAllTitle(this.args);
			break;
		}
		case -8: {
			perso.verifAndAddZaap(Short.parseShort(this.args));
			break;
		}
		case -7: {
			Dopeul.getReward(perso, Integer.parseInt(this.args));
			break;
		}
		case -6: {
			final Map mapActuel = perso.getCurMap();
			final java.util.Map<Integer, World.Couple<Integer, Integer>> dopeuls = getDopeul();
			Integer IDmob = null;
			if (!dopeuls.containsKey((int) mapActuel.getId())) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Erreur de dopeul, veuillez nous avertir sur le forum.");
				return true;
			}
			IDmob = dopeuls.get((int) mapActuel.getId()).first;
			final int LVLmob = Formulas.getLvlDopeuls(perso.getLevel());
			if (perso.getLevel() < 11) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Il faut \u00eatre niveau 11 minimum pour combattre les dopeuls des temples.");
				return true;
			}
			final int certificat = Constant.getCertificatByDopeuls(IDmob);
			if (certificat == -1) {
				return true;
			}
			if (perso.hasItemTemplate(certificat, 1)) {
				final String date = perso.getItemTemplate(certificat, 1).getTxtStat().get(805);
				final long timeStamp = Long.parseLong(date.split("#")[3]);
				if (System.currentTimeMillis() - timeStamp <= 86400000L) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Il faut que tu attende 24 heures avant de pouvoir combattre ce dopeul.");
					return true;
				}
				perso.removeByTemplateID(certificat, 1);
			}
			boolean b = true;
			if (perso.getQuestPerso() != null && !perso.getQuestPerso().isEmpty()) {
				for (final java.util.Map.Entry<Integer, Quest.Quest_Perso> entry : perso.getQuestPerso().entrySet()) {
					final Quest.Quest_Perso qa = entry.getValue();
					if (qa.getQuest().getId() == dopeuls.get((int) mapActuel.getId()).second) {
						b = false;
						if (!qa.isFinish()) {
							continue;
						}
						perso.delQuestPerso(entry.getKey());
						if (!qa.deleteQuestPerso()) {
							continue;
						}
						final Quest q = Quest.getQuestById(dopeuls.get((int) mapActuel.getId()).second);
						q.applyQuest(perso);
					}
				}
			}
			if (b) {
				final Quest q2 = Quest.getQuestById(dopeuls.get((int) mapActuel.getId()).second);
				q2.applyQuest(perso);
			}
			final String grp = IDmob + "," + LVLmob + "," + LVLmob + ";";
			final Monster.MobGroup MG = new Monster.MobGroup(perso.getCurMap().nextObjectId, perso.getCurCell().getId(),
					grp);
			perso.getCurMap().startFigthVersusDopeuls(perso, MG);
			break;
		}
		case -5: {
			try {
				final int sID = Integer.parseInt(this.args);
				if (World.getSort(sID) == null) {
					return true;
				}
				perso.learnSpell(sID, 1, true, true, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case -4: {
			switch (Short.parseShort(this.args)) {
			case 1: {
				perso.leaveEnnemyFactionAndPay(perso);
				break;
			}
			case 2: {
				perso.leaveEnnemyFaction();
				break;
			}
			}
			break;
		}
		case -3: {
			final int idMascotte = Integer.parseInt(this.args);
			if (perso.hasItemTemplate(itemID, 1)) {
				perso.removeByTemplateID(itemID, 1);
				perso.setMascotte(idMascotte);
				Database.getStatique().getPlayerData().update(perso, true);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + itemID);
				SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.getCurMap(), perso);
				break;
			}
			break;
		}
		case -2: {
			if (perso.is_away()) {
				return true;
			}
			if (perso.get_guild() != null || perso.getGuildMember() != null) {
				SocketManager.GAME_SEND_gC_PACKET(perso, "Ea");
				return true;
			}
			if (perso.hasItemTemplate(1575, 1)) {
				SocketManager.GAME_SEND_gn_PACKET(perso);
				perso.removeByTemplateID(1575, -1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;-1~1575");
				break;
			}
			SocketManager.GAME_SEND_MESSAGE(perso,
					"Pour pouvoir cr\u00e9er une guilde, il faut poss\u00e8der une Guildalogemme.");
			break;
		}
		case -1: {
			boolean ok = false;
			for (final Npc npc : perso.getCurMap().getNpcs().values()) {
				if (npc.getTemplate().get_gfxID() == 9048) {
					ok = true;
				}
			}
			if (!ok) {
				break;
			}
			Database.getStatique().getPlayerData().update(perso, true);
			if (perso.getDeshonor() >= 1) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "183");
				return true;
			}
			final int cost = perso.getBankCost();
			if (cost > 0) {
				final long playerKamas = perso.get_kamas();
				final long kamasRemaining = playerKamas - cost;
				final long bankKamas = perso.getAccount().getBankKamas();
				final long totalKamas = bankKamas + playerKamas;
				if (kamasRemaining < 0L) {
					if (bankKamas >= cost) {
						perso.setBankKamas(bankKamas - cost);
					} else {
						if (totalKamas < cost) {
							SocketManager.GAME_SEND_MESSAGE_SERVER(perso, "10|" + cost);
							return true;
						}
						perso.set_kamas(0L);
						perso.setBankKamas(totalKamas - cost);
						SocketManager.GAME_SEND_STATS_PACKET(perso);
						SocketManager.GAME_SEND_Im_PACKET(perso, "020;" + playerKamas);
					}
				} else {
					perso.set_kamas(kamasRemaining);
					SocketManager.GAME_SEND_STATS_PACKET(perso);
					SocketManager.GAME_SEND_Im_PACKET(perso, "020;" + cost);
				}
			}
			SocketManager.GAME_SEND_ECK_PACKET(perso.getGameClient(), 5, "");
			SocketManager.GAME_SEND_EL_BANK_PACKET(perso);
			perso.set_away(true);
			perso.setInBank(true);
			break;
		}
		case 0: {
			try {
				final short newMapID = Short.parseShort(this.args.split(",", 2)[0]);
				final int newCellID = Integer.parseInt(this.args.split(",", 2)[1]);
				if (!perso.isInPrison()) {
					perso.teleport(newMapID, newCellID);
					break;
				}
				if (perso.getCurCell().getId() == 268) {
					perso.teleport(newMapID, newCellID);
					break;
				}
				break;
			} catch (Exception e4) {
				return true;
			}
		}
		case 1: {
			out = perso.getGameClient();
			if (this.args.equalsIgnoreCase("DV")) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				break;
			}
			int qID = -1;
			try {
				qID = Integer.parseInt(this.args);
			} catch (NumberFormatException e2) {
				e2.printStackTrace();
			}
			final NpcQuestion quest = World.getNPCQuestion(qID);
			if (quest == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			try {
				SocketManager.GAME_SEND_QUESTION_PACKET(out, quest.parse(perso));
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			break;
		}
		case 2: {
			try {
				final short newMapID = Short.parseShort(this.args.split(",")[0]);
				final int newCellID = Integer.parseInt(this.args.split(",")[1]);
				final int verifMapID = Integer.parseInt(this.args.split(",")[2]);
				if (perso.getCurMap().getId() == verifMapID) {
					perso.teleport(newMapID, newCellID);
					break;
				}
				break;
			} catch (Exception e4) {
				e4.printStackTrace();
				return true;
			}
		}
		case 4: {
			try {
				final int count = Integer.parseInt(this.args);
				final long curKamas = perso.get_kamas();
				final long newKamas = curKamas + count;
				if (newKamas < 0L) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "084;1");
					return true;
				}
				perso.set_kamas(newKamas);
				SocketManager.GAME_SEND_Im_PACKET(perso, "046;" + count);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 5: {
			try {
				final int tID = Integer.parseInt(this.args.split(",")[0]);
				final int count2 = Integer.parseInt(this.args.split(",")[1]);
				boolean send = true;
				if (this.args.split(",").length > 2) {
					send = this.args.split(",")[2].equals("1");
				}
				if (count2 > 0) {
					final ObjectTemplate T = World.getObjTemplate(tID);
					if (T == null) {
						return true;
					}
					final org.aestia.object.Object O = T.createNewItem(count2, false);
					if (perso.addObjet(O, true)) {
						World.addObjet(O, true);
					}
				} else {
					perso.removeByTemplateID(tID, -count2);
				}
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_Ow_PACKET(perso);
					if (send) {
						if (count2 >= 0) {
							SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + count2 + "~" + tID);
						} else if (count2 < 0) {
							SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + -count2 + "~" + tID);
						}
					}
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 6: {
			try {
				perso.setIsOnDialogAction(1);
				final int mID = Integer.parseInt(this.args.split(",")[0]);
				final int mapId = Integer.parseInt(this.args.split(",")[1]);
				final int sucess = Integer.parseInt(this.args.split(",")[2]);
				final int fail = Integer.parseInt(this.args.split(",")[3]);
				if (World.getMetier(mID) == null) {
					return true;
				}
				if (mID == 2 || mID == 11 || mID == 13 || mID == 14 || mID == 15 || mID == 16 || mID == 17 || mID == 18
						|| mID == 19 || mID == 20 || mID == 24 || mID == 25 || mID == 26 || mID == 27 || mID == 28
						|| mID == 31 || mID == 36 || mID == 41 || mID == 56 || mID == 58 || mID == 60 || mID == 65) {
					if (perso.getMetierByID(mID) != null) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "111");
						SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
						perso.set_isTalkingWith(0);
						perso.setIsOnDialogAction(-1);
						return true;
					}
					if ((perso.getMetierByID(2) != null && perso.getMetierByID(2).get_lvl() < 30)
							|| (perso.getMetierByID(11) != null && perso.getMetierByID(11).get_lvl() < 30)
							|| (perso.getMetierByID(13) != null && perso.getMetierByID(13).get_lvl() < 30)
							|| (perso.getMetierByID(14) != null && perso.getMetierByID(14).get_lvl() < 30)
							|| (perso.getMetierByID(15) != null && perso.getMetierByID(15).get_lvl() < 30)
							|| (perso.getMetierByID(16) != null && perso.getMetierByID(16).get_lvl() < 30)
							|| (perso.getMetierByID(17) != null && perso.getMetierByID(17).get_lvl() < 30)
							|| (perso.getMetierByID(18) != null && perso.getMetierByID(18).get_lvl() < 30)
							|| (perso.getMetierByID(19) != null && perso.getMetierByID(19).get_lvl() < 30)
							|| (perso.getMetierByID(20) != null && perso.getMetierByID(20).get_lvl() < 30)
							|| (perso.getMetierByID(24) != null && perso.getMetierByID(24).get_lvl() < 30)
							|| (perso.getMetierByID(25) != null && perso.getMetierByID(25).get_lvl() < 30)
							|| (perso.getMetierByID(26) != null && perso.getMetierByID(26).get_lvl() < 30)
							|| (perso.getMetierByID(27) != null && perso.getMetierByID(27).get_lvl() < 30)
							|| (perso.getMetierByID(28) != null && perso.getMetierByID(28).get_lvl() < 30)
							|| (perso.getMetierByID(31) != null && perso.getMetierByID(31).get_lvl() < 30)
							|| (perso.getMetierByID(36) != null && perso.getMetierByID(36).get_lvl() < 30)
							|| (perso.getMetierByID(41) != null && perso.getMetierByID(41).get_lvl() < 30)
							|| (perso.getMetierByID(56) != null && perso.getMetierByID(56).get_lvl() < 30)
							|| (perso.getMetierByID(58) != null && perso.getMetierByID(58).get_lvl() < 30)
							|| (perso.getMetierByID(60) != null && perso.getMetierByID(60).get_lvl() < 30)
							|| (perso.getMetierByID(65) != null && perso.getMetierByID(65).get_lvl() < 30)) {
						if (sucess == -1 || fail == -1) {
							SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
							perso.set_isTalkingWith(0);
							perso.setIsOnDialogAction(-1);
							SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
						} else {
							SocketManager.send(out, "DQ" + fail + "|4840");
						}
						return true;
					}
					if (perso.totalJobBasic() > 2) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "19");
						SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
						perso.set_isTalkingWith(0);
						perso.setIsOnDialogAction(-1);
						return true;
					}
					if (mID == 27) {
						if (!perso.hasItemTemplate(966, 1)) {
							return true;
						}
						perso.removeByTemplateID(966, 1);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;966~1");
						perso.learnJob(World.getMetier(mID));
					} else {
						if (perso.getCurMap().getId() != mapId) {
							return true;
						}
						perso.learnJob(World.getMetier(mID));
						if (sucess == -1 || fail == -1) {
							SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
							perso.set_isTalkingWith(0);
							perso.setIsOnDialogAction(-1);
						} else {
							SocketManager.send(out, "DQ" + sucess + "|4840");
						}
					}
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 7: {
			if (!perso.isInPrison()) {
				perso.warpToSavePos();
				break;
			}
			break;
		}
		case 8: {
			try {
				final int statID = Integer.parseInt(this.args.split(",", 2)[0]);
				final int number = Integer.parseInt(this.args.split(",", 2)[1]);
				perso.getStats().addOneStat(statID, number);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				int messID = 0;
				switch (statID) {
				case 126: {
					messID = 14;
					break;
				}
				}
				if (messID > 0) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "0" + messID + ";" + number);
					break;
				}
				break;
			} catch (Exception e4) {
				e4.printStackTrace();
				return true;
			}
		}
		case 9: {
			try {
				final int sID2 = Integer.parseInt(this.args.split(",", 2)[0]);
				final int mapId = Integer.parseInt(this.args.split(",", 2)[1]);
				if (World.getSort(sID2) == null) {
					return true;
				}
				if (perso.getCurMap().getId() != mapId) {
					return true;
				}
				perso.learnSpell(sID2, 1, true, true, true);
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 10: {
			try {
				final int min = Integer.parseInt(this.args.split(",", 2)[0]);
				int max = Integer.parseInt(this.args.split(",", 2)[1]);
				if (max == 0) {
					max = min;
				}
				int val = Formulas.getRandomValue(min, max);
				if (target != null) {
					if (target.getCurPdv() + val > target.getMaxPdv()) {
						val = target.getMaxPdv() - target.getCurPdv();
					}
					target.setPdv(target.getCurPdv() + val);
					SocketManager.GAME_SEND_STATS_PACKET(target);
				} else {
					if (perso.getCurPdv() + val > perso.getMaxPdv()) {
						val = perso.getMaxPdv() - perso.getCurPdv();
					}
					perso.setPdv(perso.getCurPdv() + val);
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 11: {
			try {
				final byte newAlign = Byte.parseByte(this.args.split(",", 2)[0]);
				final boolean replace = Integer.parseInt(this.args.split(",", 2)[1]) == 1;
				if (perso.get_align() != -1 && !replace) {
					return true;
				}
				perso.modifAlignement(newAlign);
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 12: {
			try {
				final boolean delObj = this.args.split(",")[0].equals("true");
				final boolean inArena = this.args.split(",")[1].equals("true");
				if (inArena && !Config.contains(Config.arenaMap, perso.getCurMap().getId())) {
					return true;
				}
				final SoulStone pierrePleine = (SoulStone) World.getObjet(itemID);
				final String groupData = pierrePleine.parseGroupData();
				final String condition = "MiS = " + perso.getId();
				perso.getCurMap().spawnNewGroup(true, perso.getCurCell().getId(), groupData, condition);
				if (delObj) {
					perso.removeItem(itemID, 1, true, true);
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 13: {
			if (perso.getLevel() <= 30 && perso.getCurMap().getId() == 10354) {
				try {
					perso.getStats().addOneStat(125, -perso.getStats().getEffect(125));
					perso.getStats().addOneStat(124, -perso.getStats().getEffect(124));
					perso.getStats().addOneStat(118, -perso.getStats().getEffect(118));
					perso.getStats().addOneStat(123, -perso.getStats().getEffect(123));
					perso.getStats().addOneStat(119, -perso.getStats().getEffect(119));
					perso.getStats().addOneStat(126, -perso.getStats().getEffect(126));
					perso.addCapital((perso.getLevel() - 1) * 5 - perso.get_capital());
					perso.getStatsParcho().getMap().clear();
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				} catch (Exception e4) {
					e4.printStackTrace();
					GameServer.addToLog(e4.getMessage());
				}
				break;
			}
			SocketManager.GAME_SEND_MESSAGE(perso,
					"Ton niveau est sup\u00e9rieur \u00e0 30. Tu ne peux donc pas te restaur\u00e9 !");
			break;
		}
		case 14: {
			if (perso.getLevel() <= 30 && perso.getCurMap().getId() == 10354) {
				perso.setisForgetingSpell(true);
				SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', perso);
				break;
			}
			SocketManager.GAME_SEND_MESSAGE(perso,
					"Ton niveau est sup\u00e9rieur \u00e0 30. Tu ne peux donc pas te restaur\u00e9 !");
			break;
		}
		case 15: {
			try {
				final short newMapID = Short.parseShort(this.args.split(",")[0]);
				final int newCellID = Integer.parseInt(this.args.split(",")[1]);
				final int ObjetNeed = Integer.parseInt(this.args.split(",")[2]);
				final int MapNeed = Integer.parseInt(this.args.split(",")[3]);
				if (ObjetNeed == 0) {
					perso.teleport(newMapID, newCellID);
				} else if (ObjetNeed > 0) {
					if (MapNeed == 0) {
						perso.teleport(newMapID, newCellID);
					} else if (MapNeed > 0) {
						if (perso.hasItemTemplate(ObjetNeed, 1) && perso.getCurMap().getId() == MapNeed) {
							perso.teleport(newMapID, newCellID);
							perso.removeByTemplateID(ObjetNeed, 1);
							SocketManager.GAME_SEND_Ow_PACKET(perso);
						} else if (perso.getCurMap().getId() != MapNeed) {
							SocketManager.GAME_SEND_MESSAGE(perso,
									"Vous n'\u00eates pas sur la bonne map du donjon pour \u00eatre t\u00e9l\u00e9porter.",
									"009900");
						} else {
							SocketManager.GAME_SEND_MESSAGE(perso, "Vous ne possedez pas la clef n\u00e9cessaire.",
									"009900");
						}
					}
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 16: {
			try {
				final short newMapID = Short.parseShort(this.args.split(",")[0]);
				final int newCellID = Integer.parseInt(this.args.split(",")[1]);
				final int ObjetNeed = Integer.parseInt(this.args.split(",")[2]);
				final int MapNeed = Integer.parseInt(this.args.split(",")[3]);
				if (ObjetNeed == 0) {
					perso.teleport(newMapID, newCellID);
				} else if (ObjetNeed > 0) {
					if (MapNeed == 0) {
						perso.teleport(newMapID, newCellID);
					} else if (MapNeed > 0) {
						if (perso.hasItemTemplate(ObjetNeed, 1) && perso.getCurMap().getId() == MapNeed) {
							perso.teleport(newMapID, newCellID);
							SocketManager.GAME_SEND_Ow_PACKET(perso);
						} else if (perso.getCurMap().getId() != MapNeed) {
							SocketManager.GAME_SEND_MESSAGE(perso,
									"Vous n'\u00eates pas sur la bonne map du donjon pour \u00eatre t\u00e9l\u00e9porter.",
									"009900");
						} else {
							SocketManager.GAME_SEND_MESSAGE(perso, "Vous ne possedez pas la clef n\u00e9cessaire.",
									"009900");
						}
					}
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 17: {
			try {
				final int JobID = Integer.parseInt(this.args.split(",")[0]);
				final int XpValue = Integer.parseInt(this.args.split(",")[1]);
				if (perso.getMetierByID(JobID) != null) {
					perso.getMetierByID(JobID).addXp(perso, XpValue, true);
				}
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 18: {
			if (!House.alreadyHaveHouse(perso)) {
				break;
			}
			final org.aestia.object.Object obj2 = World.getObjet(itemID);
			if (!perso.hasItemTemplate(obj2.getTemplate().getId(), 1)) {
				break;
			}
			perso.removeByTemplateID(obj2.getTemplate().getId(), 1);
			final House h = House.getHouseByPerso(perso);
			if (h == null) {
				return true;
			}
			perso.teleport((short) h.getHouseMapId(), h.getHouseCellId());
			break;
		}
		case 19: {
			SocketManager.GAME_SEND_GUILDHOUSE_PACKET(perso);
			break;
		}
		case 20: {
			try {
				final int pts = Integer.parseInt(this.args);
				if (pts < 1) {
					return true;
				}
				perso.addSpellPoint(pts);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 21: {
			try {
				final int energyMin = Integer.parseInt(this.args.split(",", 2)[0]);
				int energyMax = Integer.parseInt(this.args.split(",", 2)[1]);
				if (energyMax == 0) {
					energyMax = energyMin;
				}
				final int val = Formulas.getRandomValue(energyMin, energyMax);
				int EnergyTotal = perso.getEnergy() + val;
				if (EnergyTotal > 10000) {
					EnergyTotal = 10000;
				}
				perso.setEnergy(EnergyTotal);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 22: {
			try {
				final long XpAdd = Integer.parseInt(this.args);
				if (XpAdd < 1L) {
					return true;
				}
				final long TotalXp = perso.getExp() + XpAdd;
				perso.setExp(TotalXp);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
			} catch (Exception e4) {
				e4.printStackTrace();
				GameServer.addToLog(e4.getMessage());
			}
			break;
		}
		case 23: {
			final int Job = Integer.parseInt(this.args.split(",", 2)[0]);
			final int mapId = Integer.parseInt(this.args.split(",", 2)[1]);
			if (perso.getCurMap().getId() != mapId) {
				return true;
			}
			if (Job < 1) {
				return true;
			}
			final JobStat m2 = perso.getMetierByID(Job);
			if (m2 == null) {
				return true;
			}
			perso.unlearnJob(m2.getId());
			SocketManager.GAME_SEND_STATS_PACKET(perso);
			Database.getStatique().getPlayerData().update(perso, false);
			break;
		}
		case 24: {
			try {
				final int morphID = Integer.parseInt(this.args);
				if (morphID < 0) {
					return true;
				}
				perso.set_gfxID(morphID);
				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
				SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso);
			} catch (Exception e5) {
				e5.printStackTrace();
				GameServer.addToLog(e5.getMessage());
			}
			break;
		}
		case 25: {
			final int UnMorphID = perso.getClasse() * 10 + perso.getSexe();
			perso.set_gfxID(UnMorphID);
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
			SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso);
			break;
		}
		case 26: {
			SocketManager.GAME_SEND_GUILDENCLO_PACKET(perso);
			break;
		}
		case 27: {
			String ValidMobGroup = "";
			if (perso.get_fight() != null) {
				return true;
			}
			try {
				final int mapId2 = Integer.parseInt(this.args.split(":", 2)[1]);
				if (perso.getCurMap().getId() != mapId2) {
					return true;
				}
				String[] split2;
				for (int length = (split2 = this.args.split(":", 2)[0].split("\\|")).length, l = 0; l < length; ++l) {
					final String MobAndLevel = split2[l];
					int monsterID = -1;
					int monsterLevel = -1;
					final String[] MobOrLevel = MobAndLevel.split(",");
					monsterID = Integer.parseInt(MobOrLevel[0]);
					monsterLevel = Integer.parseInt(MobOrLevel[1]);
					if (World.getMonstre(monsterID) != null) {
						if (World.getMonstre(monsterID).getGradeByLevel(monsterLevel) != null) {
							ValidMobGroup = String.valueOf(ValidMobGroup) + monsterID + "," + monsterLevel + ","
									+ monsterLevel + ";";
						}
					}
				}
				if (ValidMobGroup.isEmpty()) {
					return true;
				}
				final Monster.MobGroup group = new Monster.MobGroup(perso.getCurMap().nextObjectId,
						perso.getCurCell().getId(), ValidMobGroup);
				perso.getCurMap().startFightVersusMonstres(perso, group);
			} catch (Exception e6) {
				e6.printStackTrace();
				GameServer.addToLog(e6.getMessage());
			}
			break;
		}
		case 28: {
			try {
				final int sID3 = Integer.parseInt(this.args);
				final int AncLevel = perso.getSortStatBySortIfHas(sID3).getLevel();
				if (perso.getSortStatBySortIfHas(sID3) == null) {
					return true;
				}
				if (AncLevel <= 1) {
					return true;
				}
				perso.unlearnSpell(perso, sID3, 1, AncLevel, true, true);
			} catch (Exception e6) {
				e6.printStackTrace();
				GameServer.addToLog(e6.getMessage());
			}
			break;
		}
		case 29: {
			final long pKamas3 = perso.get_kamas();
			final int payKamas = perso.getLevel() * perso.getLevel() * 25;
			if (pKamas3 < payKamas) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu n'as pas assez de kamas pour effectu\u00e9 cette action.");
				return true;
			}
			long pNewKamas3 = pKamas3 - payKamas;
			if (pNewKamas3 < 0L) {
				pNewKamas3 = 0L;
			}
			final int sID4 = Integer.parseInt(this.args);
			final int AncLevel2 = perso.getSortStatBySortIfHas(sID4).getLevel();
			if (perso.getSortStatBySortIfHas(sID4) == null) {
				return true;
			}
			if (AncLevel2 <= 1) {
				return true;
			}
			perso.unlearnSpell(perso, sID4, 1, AncLevel2, true, true);
			SocketManager.GAME_SEND_MESSAGE(perso, "Tu as perdu " + payKamas + "Kamas.");
			break;
		}
		case 30: {
			final int size = Integer.parseInt(this.args);
			perso.set_size(size);
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
			SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso.getCurMap(), perso);
			break;
		}
		case 31: {
			SocketManager.GAME_SEND_MESSAGE(perso, "Vous vous \u00eates fait enculer. Cordialement, le staff.");
			break;
		}
		case 33: {
			final int posItem = Integer.parseInt(this.args);
			final org.aestia.object.Object itemPos = perso.getObjetByPos(posItem);
			if (itemPos != null) {
				itemPos.clearStats();
				final Stats maxStats = itemPos.generateNewStatsFromTemplate(itemPos.getTemplate().getStrTemplate(),
						true);
				itemPos.setStats(maxStats);
				final int idObjPos = itemPos.getGuid();
				perso.removeItem(itemID, 1, true, true);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(perso, idObjPos);
				SocketManager.GAME_SEND_OAKO_PACKET(perso, itemPos);
				Database.getStatique().getItemData().save(itemPos, false);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "Il n'y a aucune item dans la pos choisi.");
			break;
		}
		case 34: {
			final int idLot = Integer.parseInt(this.args.split(",", 2)[0]);
			Loterie.startLoterie(perso, idLot);
			break;
		}
		case 35: {
			try {
				if (perso.getCurMap().getId() != 741 || !perso.hasItemTemplate(10563, 1)) {
					return true;
				}
				perso.removeByTemplateID(10563, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;10563~1");
				perso.getStats().addOneStat(125, -perso.getStats().getEffect(125));
				perso.getStats().addOneStat(124, -perso.getStats().getEffect(124));
				perso.getStats().addOneStat(118, -perso.getStats().getEffect(118));
				perso.getStats().addOneStat(123, -perso.getStats().getEffect(123));
				perso.getStats().addOneStat(119, -perso.getStats().getEffect(119));
				perso.getStats().addOneStat(126, -perso.getStats().getEffect(126));
				perso.addCapital((perso.getLevel() - 1) * 5 - perso.get_capital());
				SocketManager.GAME_SEND_STATS_PACKET(perso);
			} catch (Exception e7) {
				e7.printStackTrace();
				GameServer.addToLog(e7.getMessage());
			}
			break;
		}
		case 36: {
			try {
				final long price = Integer.parseInt(this.args.split(";")[0]);
				int tutorial = Integer.parseInt(this.args.split(";")[1]);
				if (tutorial == 30) {
					final int random = Formulas.getRandomValue(1, 200);
					if (random == 100) {
						tutorial = 31;
					} else {
						Database.getStatique().getNpc_questionData().updateLot();
					}
				}
				final Tutorial tuto = World.getTutorial(tutorial);
				if (tuto == null) {
					return true;
				}
				if (perso.get_kamas() >= price) {
					if (price != 0L) {
						perso.set_kamas(perso.get_kamas() - price);
						if (perso.isOnline()) {
							SocketManager.GAME_SEND_STATS_PACKET(perso);
						}
						SocketManager.GAME_SEND_Im_PACKET(perso, "046;" + price);
					}
					try {
						tuto.getStart().apply(perso, null, -1, -1);
					} catch (Exception e8) {
						e8.printStackTrace();
					}
					final int _tutorial = tutorial;
					perso.getWaiter().addNext(new Runnable() {
						@Override
						public void run() {
							SocketManager.send(perso, "TC" + _tutorial + "|7001010000");
							perso.setTutorial(tuto);
							perso.set_away(true);
						}
					}, 1500L);
					return true;
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "182");
			} catch (Exception e9) {
				e9.printStackTrace();
			}
			break;
		}
		case 37: {
			Loterie.startLoteriePioute(perso);
			break;
		}
		case 38: {
			perso.addStaticEmote(Integer.parseInt(this.args));
			break;
		}
		case 40: {
			final int QuestID = Integer.parseInt(this.args);
			boolean problem = false;
			final Quest quest2 = Quest.getQuestById(QuestID);
			if (quest2 == null) {
				SocketManager.GAME_SEND_MESSAGE(perso, "La qu\u00eate est introuvable.");
				problem = true;
				break;
			}
			for (final Quest.Quest_Perso qPerso : perso.getQuestPerso().values()) {
				if (qPerso.getQuest().getId() == QuestID) {
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous connaissez d\u00e9j\u00e0 cette qu\u00eate.");
					problem = true;
					break;
				}
			}
			if (!problem) {
				quest2.applyQuest(perso);
				break;
			}
			break;
		}
		case 41: {
		}
		case 43: {
			final String[] split = this.args.split(";");
			final int mapid = Integer.parseInt(split[0].split(",")[0]);
			cellid = Integer.parseInt(split[0].split(",")[1]);
			final int mapsecu = Integer.parseInt(split[1]);
			final int questId2 = Integer.parseInt(split[2]);
			if (perso.getCurMap().getId() != mapsecu) {
				return true;
			}
			if (!perso.getQuestPersoByQuestId(questId2).isFinish()) {
				return true;
			}
			perso.teleport((short) mapid, cellid);
			break;
		}
		case 44: {
			final int count3 = Integer.parseInt(this.args);
			if (perso.getLevel() >= count3) {
				break;
			}
			while (perso.getLevel() < count3) {
				perso.levelUp(false, true);
			}
			if (perso.isOnline()) {
				SocketManager.GAME_SEND_SPELL_LIST(perso);
				SocketManager.GAME_SEND_NEW_LVL_PACKET(perso.getGameClient(), perso.getLevel());
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				break;
			}
			break;
		}
		case 50: {
			if (perso.get_align() == 0 || perso.get_align() == 3) {
				return true;
			}
			if (perso.get_traque() == null) {
				final Stalk t = new Stalk(0L, null);
				perso.set_traque(t);
			}
			if (perso.get_traque().getTime() >= System.currentTimeMillis() - 600000L
					&& perso.get_traque().getTime() != 0L) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Vous venez juste de signer un contrat, vous devez vous reposez !", "000000");
				break;
			}
			Player tempP = null;
			final ArrayList<Player> victimes = new ArrayList<Player>();
			for (final Player victime : World.getOnlinePersos()) {
				if (victime != null) {
					if (victime == perso) {
						continue;
					}
					if (victime.get_align() == perso.get_align() || victime.get_align() == 0
							|| victime.get_align() == 3) {
						continue;
					}
					if (!victime.is_showWings()) {
						continue;
					}
					if (perso.getLevel() + 20 < victime.getLevel() || perso.getLevel() - 20 > victime.getLevel()) {
						continue;
					}
					victimes.add(victime);
				}
			}
			if (victimes.size() == 0) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Nous n'avons pas trouver de cible \u00e0 ta hauteur, reviens plus tard.", "000000");
				perso.set_traque(null);
				return true;
			}
			if (victimes.size() == 1) {
				tempP = victimes.get(0);
			} else {
				tempP = victimes.get(Formulas.getRandomValue(0, victimes.size()));
			}
			SocketManager.GAME_SEND_MESSAGE(perso, "Vous \u00eates d\u00e9sormais en chasse de : " + tempP.getName(),
					"000000");
			perso.get_traque().setTraque(tempP);
			perso.get_traque().setTime(System.currentTimeMillis());
			final ObjectTemplate T2 = World.getObjTemplate(10085);
			final org.aestia.object.Object newObj = T2.createNewItem(20, false);
			newObj.addTxtStat(989, tempP.getName());
			if (perso.addObjet(newObj, true)) {
				World.addObjet(newObj, true);
				break;
			}
			perso.removeByTemplateID(T2.getId(), 20);
			break;
		}
		case 52: {
			if (perso.get_traque() != null && perso.get_traque().getTime() == -2L) {
				final long xp = Formulas.getXpStalk(perso.getLevel());
				perso.addXp(xp);
				perso.set_traque(null);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				SocketManager.GAME_SEND_MESSAGE(perso, "Vous venez de recevoir " + xp + " points d'experiences.",
						"000000");
				break;
			}
			SocketManager.GAME_SEND_MESSAGE(perso, "Thomas Sacre : Reviens me voir quand tu aura abatu un ennemi.",
					"000000");
			break;
		}
		case 53: {
			if (this.args == null) {
				break;
			}
			if (World.getMap(Short.parseShort(this.args)) == null) {
				break;
			}
			final Map CurMap = World.getMap(Short.parseShort(this.args));
			if (perso.get_fight() == null) {
				SocketManager.GAME_SEND_FLAG_PACKET(perso, CurMap);
				break;
			}
			break;
		}
		case 60: {
			String ValidMobGroup2 = "";
			if (perso.get_fight() != null) {
				return true;
			}
			try {
				String[] split3;
				for (int length2 = (split3 = this.args.split("\\|")).length, n = 0; n < length2; ++n) {
					final String MobAndLevel2 = split3[n];
					int monsterID2 = -1;
					int lvlMin = -1;
					int lvlMax = -1;
					final String[] MobOrLevel2 = MobAndLevel2.split(",");
					monsterID2 = Integer.parseInt(MobOrLevel2[0]);
					lvlMin = Integer.parseInt(MobOrLevel2[1]);
					lvlMax = Integer.parseInt(MobOrLevel2[2]);
					if (World.getMonstre(monsterID2) != null
							&& World.getMonstre(monsterID2).getGradeByLevel(lvlMin) != null) {
						if (World.getMonstre(monsterID2).getGradeByLevel(lvlMax) != null) {
							ValidMobGroup2 = String.valueOf(ValidMobGroup2) + monsterID2 + "," + lvlMin + "," + lvlMax
									+ ";";
						}
					}
				}
				if (ValidMobGroup2.isEmpty()) {
					return true;
				}
				final Monster.MobGroup group2 = new Monster.MobGroup(perso.getCurMap().nextObjectId,
						perso.getCurCell().getId(), ValidMobGroup2);
				perso.getCurMap().startFightVersusProtectors(perso, group2);
			} catch (Exception e10) {
				e10.printStackTrace();
				GameServer.addToLog(e10.getMessage());
			}
			break;
		}
		case 100: {
			if (!perso.hasItemTemplate(361, 100)) {
				break;
			}
			perso.removeByTemplateID(361, 100);
			final org.aestia.object.Object newObjAdded = World.getObjTemplate(9201).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded, true, -1)) {
				World.addObjet(newObjAdded, true);
				perso.addObjet(newObjAdded);
				break;
			}
			break;
		}
		case 101: {
			if (perso.getCurCell().getId() == 282) {
				World.AddMarried(0, perso);
				break;
			}
			if (perso.getCurCell().getId() == 297) {
				World.AddMarried(1, perso);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "1102");
			break;
		}
		case 102: {
			World.PriestRequest(perso, perso.getCurMap(), perso.get_isTalkingWith());
			break;
		}
		case 103: {
			if (perso.get_kamas() < 50000L) {
				return true;
			}
			perso.set_kamas(perso.get_kamas() - 50000L);
			if (perso.isOnline()) {
				SocketManager.GAME_SEND_STATS_PACKET(perso);
			}
			final Player wife = World.getPersonnage(perso.getWife());
			wife.Divorce();
			perso.Divorce();
			break;
		}
		case 104: {
			if (perso.getCurMap().getId() != 10257) {
				return true;
			}
			final ArrayList<World.Couple<Short, Integer>> arrays = new ArrayList<World.Couple<Short, Integer>>();
			String[] split4;
			for (int length3 = (split4 = this.args.split("\\;")).length, n2 = 0; n2 < length3; ++n2) {
				final String j = split4[n2];
				arrays.add(new World.Couple<Short, Integer>(Short.parseShort(j.split("\\,")[0]),
						Integer.parseInt(j.split("\\,")[1])));
			}
			final World.Couple<Short, Integer> couple = arrays.get(new Random().nextInt(arrays.size()));
			SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
					new StringBuilder(String.valueOf(perso.getId())).toString(), "6");
			perso.teleport(couple.first, couple.second);
			break;
		}
		case 105: {
			if (!perso.hasItemTemplate(10563, 1)) {
				perso.sendMessage("Tu ne poss\u00e8de pas l'orbe reconstituant.");
				return true;
			}
			perso.removeByTemplateID(10563, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;10563~1");
			if (perso.getStatsParcho().getEffect(125) != 0 || perso.getStatsParcho().getEffect(124) != 0
					|| perso.getStatsParcho().getEffect(118) != 0 || perso.getStatsParcho().getEffect(119) != 0
					|| perso.getStatsParcho().getEffect(126) != 0 || perso.getStats().getEffect(123) != 0) {
				perso.getStats().addOneStat(125,
						-perso.getStats().getEffect(125) + perso.getStatsParcho().getEffect(125));
				perso.getStats().addOneStat(124,
						-perso.getStats().getEffect(124) + perso.getStatsParcho().getEffect(124));
				perso.getStats().addOneStat(118,
						-perso.getStats().getEffect(118) + perso.getStatsParcho().getEffect(118));
				perso.getStats().addOneStat(123,
						-perso.getStats().getEffect(123) + perso.getStatsParcho().getEffect(123));
				perso.getStats().addOneStat(119,
						-perso.getStats().getEffect(119) + perso.getStatsParcho().getEffect(119));
				perso.getStats().addOneStat(126,
						-perso.getStats().getEffect(126) + perso.getStatsParcho().getEffect(126));
				perso.addCapital((perso.getLevel() - 1) * 5 - perso.get_capital());
			} else {
				if (perso.getStats().getEffect(125) != 101 || perso.getStats().getEffect(124) != 101
						|| perso.getStats().getEffect(118) != 101 || perso.getStats().getEffect(123) != 101
						|| perso.getStats().getEffect(119) != 101 || perso.getStats().getEffect(126) != 101) {
					perso.sendMessage(
							"Tu ne peux pas restaurer t'es caract\u00e9ristiques avec ce pouvoir. Tu ne poss\u00e8de pas de parchotage.");
					return true;
				}
				perso.getStats().addOneStat(125, -perso.getStats().getEffect(125) + 101);
				perso.getStats().addOneStat(124, -perso.getStats().getEffect(124) + 101);
				perso.getStats().addOneStat(118, -perso.getStats().getEffect(118) + 101);
				perso.getStats().addOneStat(123, -perso.getStats().getEffect(123) + 101);
				perso.getStats().addOneStat(119, -perso.getStats().getEffect(119) + 101);
				perso.getStats().addOneStat(126, -perso.getStats().getEffect(126) + 101);
				perso.getStatsParcho().addOneStat(125, 101);
				perso.getStatsParcho().addOneStat(124, 101);
				perso.getStatsParcho().addOneStat(118, 101);
				perso.getStatsParcho().addOneStat(123, 101);
				perso.getStatsParcho().addOneStat(119, 101);
				perso.getStatsParcho().addOneStat(126, 101);
				perso.addCapital((perso.getLevel() - 1) * 5 - perso.get_capital());
			}
			SocketManager.GAME_SEND_STATS_PACKET(perso);
			perso.sendMessage("Tu viens de restaurer tes caract\u00e9ristiques.");
			break;
		}
		case 116: {
			final org.aestia.object.Object EPO = World.getObjet(itemID);
			if (EPO == null) {
				return true;
			}
			final org.aestia.object.Object pets = perso.getObjetByPos(8);
			if (pets == null) {
				return true;
			}
			final PetEntry MyPets = World.getPetsEntry(pets.getGuid());
			if (MyPets == null) {
				return true;
			}
			if (EPO.getTemplate().getConditions()
					.contains(new StringBuilder(String.valueOf(pets.getTemplate().getId())).toString())) {
				MyPets.giveEpo(perso);
				break;
			}
			break;
		}
		case 170: {
			try {
				final byte title1 = (byte) Integer.parseInt(this.args);
				target = World.getPersoByName(perso.getName());
				target.set_title(title1);
				SocketManager.GAME_SEND_MESSAGE(perso, "Vous avez d\u00e9sormais un nouveau titre.");
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				Database.getStatique().getPlayerData().update(perso, false);
				if (target.get_fight() == null) {
					SocketManager.GAME_SEND_ALTER_GM_PACKET(perso.getCurMap(), perso);
				}
			} catch (Exception e11) {
				e11.printStackTrace();
				GameServer.addToLog(e11.getMessage());
			}
			break;
		}
		case 171: {
			final short type2 = (short) Integer.parseInt(this.args.split(",")[0]);
			final int mapId3 = Integer.parseInt(this.args.split(",")[1]);
			if (perso.get_align() > 0) {
				return true;
			}
			if (type2 == 1 && perso.getCurMap().getId() == mapId3 && perso.hasItemTemplate(42, 10)) {
				perso.removeByTemplateID(42, 10);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;42~10");
				perso.modifAlignement(1);
			}
			if (type2 == 2 && perso.getCurMap().getId() == mapId3 && perso.hasItemTemplate(95, 10)) {
				perso.removeByTemplateID(95, 10);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;95~10");
				perso.modifAlignement(2);
				break;
			}
			break;
		}
		case 172: {
			final int mapId4 = Integer.parseInt(this.args);
			if (perso.getCurMap().getId() != mapId4) {
				return true;
			}
			if (perso.totalJobBasic() > 2) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "19");
				return true;
			}
			if (perso.hasItemTemplate(459, 20) && perso.hasItemTemplate(7657, 15)) {
				perso.removeByTemplateID(459, 20);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;459~20");
				perso.removeByTemplateID(7657, 15);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;7657~15");
				perso.learnJob(World.getMetier(65));
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			return true;
		}
		case 200: {
			final long pKamas4 = perso.get_kamas();
			if (pKamas4 >= 100L && perso.getCurMap().getId() == 9520) {
				long pNewKamas4 = pKamas4 - 100L;
				if (pNewKamas4 < 0L) {
					pNewKamas4 = 0L;
				}
				perso.set_kamas(pNewKamas4);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "046;100");
				SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
						new StringBuilder(String.valueOf(perso.getId())).toString(), "2");
				perso.teleport((short) 9541, 407);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "182");
			break;
		}
		case 219: {
			if (perso.getCurMap().getId() != 1780) {
				return true;
			}
			final int type3 = Integer.parseInt(this.args);
			if (type3 == 1) {
				final org.aestia.object.Object newObjAdded2 = World.getObjTemplate(970).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded2, true, -1)) {
					World.addObjet(newObjAdded2, true);
					perso.addObjet(newObjAdded2);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~970");
				perso.teleport((short) 844, 212);
				break;
			}
			if (type3 == 2) {
				final org.aestia.object.Object newObjAdded2 = World.getObjTemplate(969).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded2, true, -1)) {
					World.addObjet(newObjAdded2, true);
					perso.addObjet(newObjAdded2);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~969");
				perso.teleport((short) 844, 212);
				break;
			}
			if (type3 == 3) {
				final org.aestia.object.Object newObjAdded2 = World.getObjTemplate(971).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded2, true, -1)) {
					World.addObjet(newObjAdded2, true);
					perso.addObjet(newObjAdded2);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~971");
				perso.teleport((short) 844, 212);
				break;
			}
			break;
		}
		case 220: {
			try {
				final String remove0 = this.args.split(";")[0];
				final String add0 = this.args.split(";")[1];
				final String add2 = this.args.split(";")[4];
				final int obj3 = Integer.parseInt(remove0.split(",")[0]);
				final int qua0 = Integer.parseInt(remove0.split(",")[1]);
				final int newObj2 = Integer.parseInt(add0.split(",")[0]);
				final int newQua1 = Integer.parseInt(add0.split(",")[1]);
				final int newObj3 = Integer.parseInt(add2.split(",")[0]);
				final int newQua2 = Integer.parseInt(add2.split(",")[1]);
				if (perso.hasItemTemplate(obj3, qua0)) {
					perso.removeByTemplateID(obj3, qua0);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua0 + "~" + obj3);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + newQua1 + "~" + newObj2);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + newQua2 + "~" + newObj3);
					final org.aestia.object.Object newObjAdded3 = World.getObjTemplate(newObj2).createNewItem(newQua1,
							false);
					if (!perso.addObjetSimiler(newObjAdded3, true, -1)) {
						World.addObjet(newObjAdded3, true);
						perso.addObjet(newObjAdded3);
					}
					final org.aestia.object.Object newObjAdded4 = World.getObjTemplate(newObj3).createNewItem(newQua2,
							false);
					if (!perso.addObjetSimiler(newObjAdded4, true, -1)) {
						World.addObjet(newObjAdded4, true);
						perso.addObjet(newObjAdded4);
					}
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e12) {
				e12.printStackTrace();
				GameServer.addToLog(e12.getMessage());
			}
			return true;
		}
		case 221: {
			try {
				final String remove0 = this.args.split(";")[0];
				final String remove2 = this.args.split(";")[1];
				final String add3 = this.args.split(";")[4];
				final int obj3 = Integer.parseInt(remove0.split(",")[0]);
				final int qua0 = Integer.parseInt(remove0.split(",")[1]);
				final int obj4 = Integer.parseInt(remove2.split(",")[0]);
				final int qua2 = Integer.parseInt(remove2.split(",")[1]);
				final int newObj4 = Integer.parseInt(add3.split(",")[0]);
				final int newQua3 = Integer.parseInt(add3.split(",")[1]);
				if (perso.hasItemTemplate(obj3, qua0) && perso.hasItemTemplate(obj4, qua2)) {
					perso.removeByTemplateID(obj3, qua0);
					perso.removeByTemplateID(obj4, qua2);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua0 + "~" + obj3);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua2 + "~" + obj4);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + newQua3 + "~" + newObj4);
					final org.aestia.object.Object newObjAdded3 = World.getObjTemplate(newObj4).createNewItem(newQua3,
							false);
					if (!perso.addObjetSimiler(newObjAdded3, true, -1)) {
						World.addObjet(newObjAdded3, true);
						perso.addObjet(newObjAdded3);
					}
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e12) {
				e12.printStackTrace();
				GameServer.addToLog(e12.getMessage());
			}
			return true;
		}
		case 222: {
			try {
				final String remove0 = this.args.split(";")[0];
				final String remove2 = this.args.split(";")[1];
				final String remove3 = this.args.split(";")[2];
				final String remove4 = this.args.split(";")[3];
				final String add4 = this.args.split(";")[4];
				final int verifMapId = Integer.parseInt(this.args.split(";")[5]);
				final int obj5 = Integer.parseInt(remove0.split(",")[0]);
				final int qua3 = Integer.parseInt(remove0.split(",")[1]);
				final int obj6 = Integer.parseInt(remove2.split(",")[0]);
				final int qua4 = Integer.parseInt(remove2.split(",")[1]);
				final int obj7 = Integer.parseInt(remove3.split(",")[0]);
				final int qua5 = Integer.parseInt(remove3.split(",")[1]);
				final int obj8 = Integer.parseInt(remove4.split(",")[0]);
				final int qua6 = Integer.parseInt(remove4.split(",")[1]);
				final int mapID = Integer.parseInt(add4.split(",")[0]);
				final int cellID = Integer.parseInt(add4.split(",")[1]);
				if (perso.hasItemTemplate(obj5, qua3) && perso.hasItemTemplate(obj6, qua4)
						&& perso.hasItemTemplate(obj7, qua5) && perso.hasItemTemplate(obj8, qua6)) {
					perso.removeByTemplateID(obj5, qua3);
					perso.removeByTemplateID(obj6, qua4);
					perso.removeByTemplateID(obj7, qua5);
					perso.removeByTemplateID(obj8, qua6);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua3 + "~" + obj5);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua4 + "~" + obj6);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua5 + "~" + obj7);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua6 + "~" + obj8);
					if (perso.get_fight() != null || perso.getCurMap().getId() != verifMapId) {
						return true;
					}
					perso.teleport((short) mapID, cellID);
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e12) {
				e12.printStackTrace();
				GameServer.addToLog(e12.getMessage());
			}
			return true;
		}
		case 223: {
			try {
				final String remove0 = this.args.split(";")[0];
				final String remove2 = this.args.split(";")[1];
				final String remove3 = this.args.split(";")[2];
				final String remove4 = this.args.split(";")[3];
				final String remove5 = this.args.split(";")[4];
				final String add5 = this.args.split(";")[5];
				final int obj5 = Integer.parseInt(remove0.split(",")[0]);
				final int qua3 = Integer.parseInt(remove0.split(",")[1]);
				final int obj6 = Integer.parseInt(remove2.split(",")[0]);
				final int qua4 = Integer.parseInt(remove2.split(",")[1]);
				final int obj7 = Integer.parseInt(remove3.split(",")[0]);
				final int qua5 = Integer.parseInt(remove3.split(",")[1]);
				final int obj8 = Integer.parseInt(remove4.split(",")[0]);
				final int qua6 = Integer.parseInt(remove4.split(",")[1]);
				final int obj9 = Integer.parseInt(remove5.split(",")[0]);
				final int qua7 = Integer.parseInt(remove5.split(",")[1]);
				final int newItem = Integer.parseInt(add5.split(",")[0]);
				final int quaNewItem = Integer.parseInt(add5.split(",")[1]);
				if (perso.hasItemTemplate(obj5, qua3) && perso.hasItemTemplate(obj6, qua4)
						&& perso.hasItemTemplate(obj7, qua5) && perso.hasItemTemplate(obj8, qua6)
						&& perso.hasItemTemplate(obj9, qua7)) {
					perso.removeByTemplateID(obj5, qua3);
					perso.removeByTemplateID(obj6, qua4);
					perso.removeByTemplateID(obj7, qua5);
					perso.removeByTemplateID(obj8, qua6);
					perso.removeByTemplateID(obj9, qua7);
					final org.aestia.object.Object newObjAdded5 = World.getObjTemplate(newItem)
							.createNewItem(quaNewItem, false);
					if (!perso.addObjetSimiler(newObjAdded5, true, -1)) {
						World.addObjet(newObjAdded5, true);
						perso.addObjet(newObjAdded5);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua3 + "~" + obj5);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua4 + "~" + obj6);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua5 + "~" + obj7);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua6 + "~" + obj8);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua7 + "~" + obj9);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + quaNewItem + "~" + newItem);
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e12) {
				e12.printStackTrace();
				GameServer.addToLog(e12.getMessage());
			}
			return true;
		}
		case 224: {
			try {
				final String remove0 = this.args.split(";")[0];
				final String remove2 = this.args.split(";")[1];
				final String remove3 = this.args.split(";")[2];
				final String remove4 = this.args.split(";")[3];
				final String add4 = this.args.split(";")[4];
				final int obj10 = Integer.parseInt(remove0.split(",")[0]);
				final int qua8 = Integer.parseInt(remove0.split(",")[1]);
				final int obj11 = Integer.parseInt(remove2.split(",")[0]);
				final int qua9 = Integer.parseInt(remove2.split(",")[1]);
				final int obj12 = Integer.parseInt(remove3.split(",")[0]);
				final int qua10 = Integer.parseInt(remove3.split(",")[1]);
				final int obj13 = Integer.parseInt(remove4.split(",")[0]);
				final int qua11 = Integer.parseInt(remove4.split(",")[1]);
				final int newItem2 = Integer.parseInt(add4.split(",")[0]);
				final int quaNewItem2 = Integer.parseInt(add4.split(",")[1]);
				if (perso.hasItemTemplate(obj10, qua8) && perso.hasItemTemplate(obj11, qua9)
						&& perso.hasItemTemplate(obj12, qua10) && perso.hasItemTemplate(obj13, qua11)) {
					perso.removeByTemplateID(obj10, qua8);
					perso.removeByTemplateID(obj11, qua9);
					perso.removeByTemplateID(obj12, qua10);
					perso.removeByTemplateID(obj13, qua11);
					final org.aestia.object.Object newObjAdded6 = World.getObjTemplate(newItem2)
							.createNewItem(quaNewItem2, false);
					if (!perso.addObjetSimiler(newObjAdded6, true, -1)) {
						World.addObjet(newObjAdded6, true);
						perso.addObjet(newObjAdded6);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua8 + "~" + obj10);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua9 + "~" + obj11);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua10 + "~" + obj12);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua11 + "~" + obj13);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + quaNewItem2 + "~" + newItem2);
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e12) {
				e12.printStackTrace();
				GameServer.addToLog(e12.getMessage());
			}
			return true;
		}
		case 225: {
			try {
				final String remove0 = this.args.split(";")[0];
				final String remove2 = this.args.split(";")[1];
				final String remove3 = this.args.split(";")[2];
				final String add6 = this.args.split(";")[3];
				final int obj14 = Integer.parseInt(remove0.split(",")[0]);
				final int qua12 = Integer.parseInt(remove0.split(",")[1]);
				final int obj15 = Integer.parseInt(remove2.split(",")[0]);
				final int qua13 = Integer.parseInt(remove2.split(",")[1]);
				final int obj16 = Integer.parseInt(remove3.split(",")[0]);
				final int qua14 = Integer.parseInt(remove3.split(",")[1]);
				final int newItem3 = Integer.parseInt(add6.split(",")[0]);
				final int quaNewItem3 = Integer.parseInt(add6.split(",")[1]);
				if (perso.hasItemTemplate(obj14, qua12) && perso.hasItemTemplate(obj15, qua13)
						&& perso.hasItemTemplate(obj16, qua14)) {
					perso.removeByTemplateID(obj14, qua12);
					perso.removeByTemplateID(obj15, qua13);
					perso.removeByTemplateID(obj16, qua14);
					final org.aestia.object.Object newObjAdded7 = World.getObjTemplate(newItem3)
							.createNewItem(quaNewItem3, false);
					if (!perso.addObjetSimiler(newObjAdded7, true, -1)) {
						World.addObjet(newObjAdded7, true);
						perso.addObjet(newObjAdded7);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua12 + "~" + obj14);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua13 + "~" + obj15);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua14 + "~" + obj16);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + quaNewItem3 + "~" + newItem3);
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e12) {
				e12.printStackTrace();
				GameServer.addToLog(e12.getMessage());
			}
			break;
		}
		case 226: {
			if (perso.hasItemTemplate(1089, 1) && perso.hasEquiped(1021) && perso.hasEquiped(1019)
					&& perso.getCurMap().getId() == 1014) {
				perso.removeByTemplateID(1019, 1);
				perso.removeByTemplateID(1021, 1);
				perso.removeByTemplateID(1089, 1);
				final org.aestia.object.Object newObj5 = World.getObjTemplate(1020).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObj5, true, -1)) {
					World.addObjet(newObj5, true);
					perso.addObjet(newObj5);
				}
				final org.aestia.object.Object newObj6 = World.getObjTemplate(1022).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObj6, true, -1)) {
					World.addObjet(newObj6, true);
					perso.addObjet(newObj6);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1089");
				SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
						new StringBuilder(String.valueOf(perso.getId())).toString(), "1");
				perso.teleport((short) 437, 411);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			return true;
		}
		case 227: {
			final long pKamas5 = perso.get_kamas();
			if (pKamas5 >= 500L && perso.getCurMap().getId() == 167) {
				long pNewKamas5 = pKamas5 - 500L;
				if (pNewKamas5 < 0L) {
					pNewKamas5 = 0L;
				}
				perso.set_kamas(pNewKamas5);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "046;500");
				SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
						new StringBuilder(String.valueOf(perso.getId())).toString(), "2");
				perso.teleport((short) 833, 141);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "182");
			break;
		}
		case 228: {
			try {
				final int AnimationId = Integer.parseInt(this.args);
				final Animation animation = World.getAnimation(AnimationId);
				if (perso.get_fight() != null) {
					return true;
				}
				perso.changeOrientation(1);
				SocketManager.GAME_SEND_GA_PACKET_TO_MAP(perso.getCurMap(), "0", 228,
						String.valueOf(perso.getId()) + ";" + cellid + "," + Animation.PrepareToGA(animation), "");
			} catch (Exception e13) {
				e13.printStackTrace();
				GameServer.addToLog(e13.getMessage());
			}
			break;
		}
		case 229: {
			final short map = Constant.getClassStatueMap(perso.getClasse());
			final int cell = Constant.getClassStatueCell(perso.getClasse());
			SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
					new StringBuilder(String.valueOf(perso.getId())).toString(), "7");
			perso.teleport(map, cell);
			perso.set_savePos(String.valueOf(map) + "," + cell);
			SocketManager.GAME_SEND_Im_PACKET(perso, "06");
			break;
		}
		case 230: {
			try {
				final int pts2 = Integer.parseInt(this.args);
				int ptsTotal = perso.getAccount().getPoints() + pts2;
				if (ptsTotal < 0) {
					ptsTotal = 0;
				}
				if (ptsTotal > 50000) {
					ptsTotal = 50000;
				}
				perso.getAccount().setPoints(ptsTotal);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu viens d'acqu\u00e9rir " + pts2
						+ " Point(s). Tu poss\u00e8des donc " + ptsTotal + "Point(s).");
				return true;
			} catch (Exception e14) {
				e14.printStackTrace();
				GameServer.addToLog(e14.getMessage());
				break;
			}
		}
		case 231: {
			try {
				final String remove6 = this.args.split(";")[0];
				final String add5 = this.args.split(";")[1];
				final int obj17 = Integer.parseInt(remove6.split(",")[0]);
				final int qua15 = Integer.parseInt(remove6.split(",")[1]);
				final int newItem4 = Integer.parseInt(add5.split(",")[0]);
				final int quaNewItem4 = Integer.parseInt(add5.split(",")[1]);
				if (perso.hasItemTemplate(obj17, qua15)) {
					perso.removeByTemplateID(obj17, qua15);
					final org.aestia.object.Object newObjAdded8 = World.getObjTemplate(newItem4)
							.createNewItem(quaNewItem4, false);
					if (!perso.addObjetSimiler(newObjAdded8, true, -1)) {
						World.addObjet(newObjAdded8, true);
						perso.addObjet(newObjAdded8);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua15 + "~" + obj17);
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + quaNewItem4 + "~" + newItem4);
				} else {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				}
			} catch (Exception e14) {
				e14.printStackTrace();
				GameServer.addToLog(e14.getMessage());
			}
			break;
		}
		case 232: {
			if (perso.get_fight() != null) {
				return true;
			}
			String ValidMobGroup3 = "";
			final int pMap = perso.getCurMap().getId();
			if (pMap != 10131 && pMap != 10132 && pMap != 10133 && pMap != 10134 && pMap != 10135 && pMap != 10136
					&& pMap != 10137) {
				if (pMap != 10138) {
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous ne pouvez pas vous battre ici. Allez en ar\u00e9ne !");
					break;
				}
			}
			try {
				String[] split5;
				for (int length4 = (split5 = this.args.split("\\|")).length, n3 = 0; n3 < length4; ++n3) {
					final String MobAndLevel3 = split5[n3];
					int monsterID3 = -1;
					int monsterLevel2 = -1;
					final String[] MobOrLevel3 = MobAndLevel3.split(",");
					monsterID3 = Integer.parseInt(MobOrLevel3[0]);
					monsterLevel2 = Integer.parseInt(MobOrLevel3[1]);
					if (World.getMonstre(monsterID3) != null) {
						if (World.getMonstre(monsterID3).getGradeByLevel(monsterLevel2) != null) {
							ValidMobGroup3 = String.valueOf(ValidMobGroup3) + monsterID3 + "," + monsterLevel2 + ","
									+ monsterLevel2 + ";";
						}
					}
				}
				if (ValidMobGroup3.isEmpty()) {
					return true;
				}
				final Monster.MobGroup group3 = new Monster.MobGroup(perso.getCurMap().nextObjectId,
						perso.getCurCell().getId(), ValidMobGroup3);
				perso.getCurMap().startFightVersusMonstres(perso, group3);
			} catch (Exception e15) {
				e15.printStackTrace();
				GameServer.addToLog(e15.getMessage());
			}
			break;
		}
		case 233: {
			try {
				final int tID2 = Integer.parseInt(this.args.split(",")[0]);
				final int count3 = Integer.parseInt(this.args.split(",")[1]);
				boolean send2 = true;
				if (this.args.split(",").length > 2) {
					send2 = this.args.split(",")[2].equals("1");
				}
				final int pMap2 = perso.getCurMap().getId();
				if (pMap2 == 10131 || pMap2 == 10132 || pMap2 == 10133 || pMap2 == 10134 || pMap2 == 10135
						|| pMap2 == 10136 || pMap2 == 10137 || pMap2 == 10138) {
					if (count3 > 0) {
						final ObjectTemplate T3 = World.getObjTemplate(tID2);
						if (T3 == null) {
							return true;
						}
						final org.aestia.object.Object O2 = T3.createNewItem(count3, false);
						if (perso.addObjet(O2, true)) {
							World.addObjet(O2, true);
						}
					} else {
						perso.removeByTemplateID(tID2, -count3);
					}
					if (perso.isOnline()) {
						SocketManager.GAME_SEND_Ow_PACKET(perso);
						if (send2) {
							if (count3 >= 0) {
								SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + count3 + "~" + tID2);
							} else if (count3 < 0) {
								SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + -count3 + "~" + tID2);
							}
						}
					}
				}
			} catch (Exception e15) {
				e15.printStackTrace();
				GameServer.addToLog(e15.getMessage());
			}
			break;
		}
		case 234: {
			final int IdObj = Short.parseShort(this.args.split(";")[0]);
			final int MapId = Integer.parseInt(this.args.split(";")[1]);
			if (perso.getCurMap().getId() != MapId) {
				return true;
			}
			if (perso.hasItemTemplate(IdObj, 1)) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu poss\u00e8de d\u00e9j\u00e0 l'objet.");
				break;
			}
			final org.aestia.object.Object newObjAdded9 = World.getObjTemplate(IdObj).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded9, true, -1)) {
				World.addObjet(newObjAdded9, true);
				perso.addObjet(newObjAdded9);
				break;
			}
			break;
		}
		case 235: {
			if (perso.getCurMap().getId() != 713) {
				break;
			}
			if (perso.hasItemTemplate(757, 1) && perso.hasItemTemplate(368, 1) && perso.hasItemTemplate(369, 1)
					&& !perso.hasItemTemplate(960, 1)) {
				perso.removeByTemplateID(757, 1);
				perso.removeByTemplateID(368, 1);
				perso.removeByTemplateID(369, 1);
				final org.aestia.object.Object newObjAdded9 = World.getObjTemplate(960).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded9, true, -1)) {
					World.addObjet(newObjAdded9, true);
					perso.addObjet(newObjAdded9);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~757");
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~368");
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~369");
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~960");
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 299: {
			try {
				SocketManager.GAME_SEND_ALE_PACKET(out, "r");
				perso.changeName(true);
			} catch (Exception e16) {
				e16.printStackTrace();
			}
			break;
		}
		case 300: {
			if (perso.getCurMap().getId() == 1559 && perso.hasItemTemplate(973, 1)) {
				perso.removeByTemplateID(973, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~973");
				perso.learnSpell(370, 1, true, true, true);
				break;
			}
			break;
		}
		case 239: {
			perso.setisForgetingSpell(true);
			SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', perso);
			break;
		}
		case 241: {
			if (perso.get_kamas() >= 10L && perso.getCurMap().getId() == 6863) {
				if (perso.hasItemTemplate(6653, 1)) {
					final String date2 = perso.getItemTemplate(6653, 1).getTxtStat().get(805);
					final long timeStamp2 = Long.parseLong(date2.split("#")[3]);
					if (System.currentTimeMillis() - timeStamp2 <= 86400000L) {
						SocketManager.GAME_SEND_MESSAGE(perso, "Ton ticket est bon.");
						return true;
					}
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Ton ticket est d\u00e9pass\u00e9, il faut que tu en rach\u00e9te un.");
					perso.removeByTemplateID(6653, 1);
				}
				long rK = perso.get_kamas() - 10L;
				if (rK < 0L) {
					rK = 0L;
				}
				perso.set_kamas(rK);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				final ObjectTemplate OT = World.getObjTemplate(6653);
				final org.aestia.object.Object obj18 = OT.createNewItem(1, false);
				if (perso.addObjet(obj18, true)) {
					World.addObjet(obj18, true);
				}
				Database.getStatique().getItemData().save(obj18, false);
				obj18.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
				Database.getStatique().getPlayerData().update(perso, true);
				SocketManager.GAME_SEND_Ow_PACKET(perso);
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~6653");
				break;
			}
			break;
		}
		case 450: {
			if (perso.getCurMap().getId() == 1844 && perso.get_kamas() >= 5000L && perso.hasItemTemplate(363, 5)) {
				perso.removeByTemplateID(363, 5);
				long rK = perso.get_kamas() - 5000L;
				if (rK < 0L) {
					rK = 0L;
				}
				perso.set_kamas(rK);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				final org.aestia.object.Object newObjAdded8 = World.getObjTemplate(998).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded8, true, -1)) {
					World.addObjet(newObjAdded8, true);
					perso.addObjet(newObjAdded8);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~998");
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;5~363");
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 451: {
			if (perso.get_kamas() >= 200L && perso.getCurMap().getId() == 436) {
				long rK = perso.get_kamas() - 200L;
				if (rK < 0L) {
					rK = 0L;
				}
				perso.set_kamas(rK);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				final org.aestia.object.Object newObjAdded8 = World.getObjTemplate(1004).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded8, true, -1)) {
					World.addObjet(newObjAdded8, true);
					perso.addObjet(newObjAdded8);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~1004");
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 452: {
			if (perso.hasItemTemplate(1000, 6) && perso.hasItemTemplate(1003, 1) && perso.hasItemTemplate(1018, 10)
					&& perso.hasItemTemplate(998, 1) && perso.hasItemTemplate(1002, 1) && perso.hasItemTemplate(999, 1)
					&& perso.hasItemTemplate(1004, 4) && perso.hasItemTemplate(1001, 2)
					&& perso.getCurMap().getId() == 437) {
				perso.removeByTemplateID(1000, 6);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;6~1000");
				perso.removeByTemplateID(1003, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1003");
				perso.removeByTemplateID(1018, 10);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;10~1018");
				perso.removeByTemplateID(998, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~998");
				perso.removeByTemplateID(1002, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1002");
				perso.removeByTemplateID(999, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~99");
				perso.removeByTemplateID(1004, 4);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;4~1004");
				perso.removeByTemplateID(1001, 2);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;2~1001");
				final org.aestia.object.Object newObjAdded9 = World.getObjTemplate(6716).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded9, true, -1)) {
					World.addObjet(newObjAdded9, true);
					perso.addObjet(newObjAdded9);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~6716");
				perso.teleport((short) 1701, 247);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 453: {
			if (perso.hasItemTemplate(1010, 1) && perso.hasItemTemplate(1011, 1) && perso.hasItemTemplate(1012, 1)
					&& perso.hasItemTemplate(1013, 1) && perso.getCurMap().getId() == 1714) {
				perso.removeByTemplateID(1010, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1010");
				perso.removeByTemplateID(1011, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1011");
				perso.removeByTemplateID(1012, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1012");
				perso.removeByTemplateID(1013, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1013");
				perso.teleport((short) 1766, 332);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 454: {
			if (perso.hasEquiped(1088) && perso.getCurMap().getId() == 1764) {
				perso.teleport((short) 1765, 226);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 455: {
			if (perso.hasItemTemplate(1006, 1) && perso.hasItemTemplate(1007, 1) && perso.hasItemTemplate(1008, 1)
					&& perso.hasItemTemplate(1009, 1) && perso.getCurMap().getId() == 1838) {
				perso.removeByTemplateID(1006, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1006");
				perso.removeByTemplateID(1007, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1007");
				perso.removeByTemplateID(1008, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1008");
				perso.removeByTemplateID(1009, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1009");
				final org.aestia.object.Object newObjAdded9 = World.getObjTemplate(1086).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded9, true, -1)) {
					World.addObjet(newObjAdded9, true);
					perso.addObjet(newObjAdded9);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~1086");
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
			break;
		}
		case 456: {
			if (!perso.hasItemTemplate(1014, 1) || !perso.hasItemTemplate(1015, 1) || !perso.hasItemTemplate(1016, 1)
					|| !perso.hasItemTemplate(1017, 1) || !perso.hasItemTemplate(1086, 1)
					|| perso.getCurMap().getId() != 425) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "14|43");
				break;
			}
			perso.removeByTemplateID(1014, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1014");
			perso.removeByTemplateID(1015, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1015");
			perso.removeByTemplateID(1016, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1016");
			perso.removeByTemplateID(1017, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1017");
			perso.removeByTemplateID(1086, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1086");
			final org.aestia.object.Object newObjAdded9 = World.getObjTemplate(1088).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded9, true, -1)) {
				World.addObjet(newObjAdded9, true);
				perso.addObjet(newObjAdded9);
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~1088");
			final NpcQuestion quest3 = World.getNPCQuestion(577);
			if (quest3 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(perso.getGameClient());
				perso.set_isTalkingWith(0);
				return true;
			}
			try {
				SocketManager.GAME_SEND_QUESTION_PACKET(perso.getGameClient(), quest3.parse(perso));
			} catch (Exception e17) {
				e17.printStackTrace();
			}
			break;
		}
		case 457: {
			if (perso.get_kamas() >= 1000L && perso.getCurMap().getId() == 1014) {
				perso.set_kamas(perso.get_kamas() - 1000L);
				final org.aestia.object.Object newObjAdded10 = World.getObjTemplate(1089).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded10, true, -1)) {
					World.addObjet(newObjAdded10, true);
					perso.addObjet(newObjAdded10);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~1089");
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				break;
			}
			break;
		}
		case 500: {
			if (perso.getCurMap().getId() != 2084) {
				return true;
			}
			perso.teleport((short) 1856, 226);
			final org.aestia.object.Object newObjAdded9 = World.getObjTemplate(1728).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded9, true, -1)) {
				World.addObjet(newObjAdded9, true);
				perso.addObjet(newObjAdded9);
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~1728");
			break;
		}
		case 501: {
			if (perso.getCurMap().getId() != 9767) {
				return true;
			}
			perso.teleport((short) 9470, 198);
			final org.aestia.object.Object newObjAdded11 = World.getObjTemplate(8000).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded11, true, -1)) {
				World.addObjet(newObjAdded11, true);
				perso.addObjet(newObjAdded11);
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~8000");
			break;
		}
		case 502: {
			if (perso.hasEquiped(969) && perso.hasEquiped(970) && perso.hasEquiped(971)
					&& perso.getCurMap().getId() == 1781) {
				perso.teleport((short) 1783, 114);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "114;");
			break;
		}
		case 503: {
			if (perso.getCurMap().getId() != 1795) {
				return true;
			}
			if (perso.hasItemTemplate(969, 1) && perso.hasItemTemplate(970, 1) && perso.hasItemTemplate(971, 1)) {
				perso.removeByTemplateID(969, 1);
				perso.removeByTemplateID(970, 1);
				perso.removeByTemplateID(971, 1);
				final org.aestia.object.Object newObjAdded12 = World.getObjTemplate(972).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded12, true, -1)) {
					World.addObjet(newObjAdded12, true);
					perso.addObjet(newObjAdded12);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~972");
				perso.teleport((short) 1781, 227);
				break;
			}
			break;
		}
		case 504: {
			if (perso.getCurMap().getId() == 9717) {
				int type4 = 0;
				try {
					type4 = Integer.parseInt(this.args);
				} catch (Exception e18) {
					e18.printStackTrace();
				}
				if (type4 == 1) {
					final org.aestia.object.Object newObjAdded13 = World.getObjTemplate(7890).createNewItem(1, false);
					if (!perso.addObjetSimiler(newObjAdded13, true, -1)) {
						World.addObjet(newObjAdded13, true);
						perso.addObjet(newObjAdded13);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~7890");
				}
				if (type4 == 2) {
					final org.aestia.object.Object newObjAdded13 = World.getObjTemplate(7889).createNewItem(1, false);
					if (!perso.addObjetSimiler(newObjAdded13, true, -1)) {
						World.addObjet(newObjAdded13, true);
						perso.addObjet(newObjAdded13);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~7889");
				}
				if (type4 == 3) {
					final org.aestia.object.Object newObjAdded13 = World.getObjTemplate(7888).createNewItem(1, false);
					if (!perso.addObjetSimiler(newObjAdded13, true, -1)) {
						World.addObjet(newObjAdded13, true);
						perso.addObjet(newObjAdded13);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~7888");
				}
				if (type4 == 4) {
					final org.aestia.object.Object newObjAdded13 = World.getObjTemplate(7887).createNewItem(1, false);
					if (!perso.addObjetSimiler(newObjAdded13, true, -1)) {
						World.addObjet(newObjAdded13, true);
						perso.addObjet(newObjAdded13);
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~7887");
				}
				perso.teleport((short) 8905, 431);
				break;
			}
			break;
		}
		case 505: {
			if (perso.getCurMap().getId() == 9717 && perso.hasItemTemplate(7904, 50)
					&& perso.hasItemTemplate(7903, 50)) {
				perso.removeByTemplateID(7904, 50);
				perso.removeByTemplateID(7903, 50);
				perso.learnSpell(414, 1, true, true, true);
				break;
			}
			break;
		}
		case 506: {
			if (perso.getCurMap().getId() != 8905 || perso.getCurCell().getId() != 213) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'\u00eates pas devant le PNJ.");
				break;
			}
			if (perso.hasItemTemplate(7908, 1)) {
				perso.removeByTemplateID(7908, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~7908");
				perso.teleport((short) 8950, 408);
				break;
			}
			SocketManager.GAME_SEND_MESSAGE(perso, "Vous ne poss\u00e8dez pas la clef necc\u00e9ssaire.");
			break;
		}
		case 507: {
			int type5 = 0;
			if (perso.getCurMap().getId() == 6823) {
				try {
					type5 = Integer.parseInt(this.args);
					if (type5 < 6) {
						if (perso.hasItemTemplate(2433, 15) && perso.hasItemTemplate(2432, 15)
								&& perso.hasItemTemplate(2431, 15) && perso.hasItemTemplate(2430, 15)) {
							type5 = 6;
						} else if (perso.hasItemTemplate(2433, 10) && perso.hasItemTemplate(2432, 10)
								&& perso.hasItemTemplate(2431, 10) && perso.hasItemTemplate(2430, 10)) {
							type5 = 5;
						}
					}
				} catch (Exception e18) {
					e18.printStackTrace();
				}
				switch (type5) {
				case 1: {
					if (perso.hasItemTemplate(2433, 10)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2433) + "~" + 2433);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2432) + "~" + 2432);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2431) + "~" + 2431);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2430) + "~" + 2430);
						perso.removeByTemplateID(2430, perso.getNbItemTemplate(2430));
						perso.removeByTemplateID(2431, perso.getNbItemTemplate(2431));
						perso.removeByTemplateID(2432, perso.getNbItemTemplate(2432));
						perso.removeByTemplateID(2433, perso.getNbItemTemplate(2433));
						perso.teleport((short) 6834, 422);
						break;
					}
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez pas le bon nombre de flaque de Menthe.");
					break;
				}
				case 2: {
					if (perso.hasItemTemplate(2432, 10)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2433) + "~" + 2433);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2432) + "~" + 2432);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2431) + "~" + 2431);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2430) + "~" + 2430);
						perso.removeByTemplateID(2430, perso.getNbItemTemplate(2430));
						perso.removeByTemplateID(2431, perso.getNbItemTemplate(2431));
						perso.removeByTemplateID(2432, perso.getNbItemTemplate(2432));
						perso.removeByTemplateID(2433, perso.getNbItemTemplate(2433));
						perso.teleport((short) 6833, 422);
						break;
					}
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez pas le bon nombre de flaque de Fraise.");
					break;
				}
				case 3: {
					if (perso.hasItemTemplate(2431, 10)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2433) + "~" + 2433);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2432) + "~" + 2432);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2431) + "~" + 2431);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2430) + "~" + 2430);
						perso.removeByTemplateID(2430, perso.getNbItemTemplate(2430));
						perso.removeByTemplateID(2431, perso.getNbItemTemplate(2431));
						perso.removeByTemplateID(2432, perso.getNbItemTemplate(2432));
						perso.removeByTemplateID(2433, perso.getNbItemTemplate(2433));
						perso.teleport((short) 6832, 422);
						break;
					}
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez pas le bon nombre de flaque de Citron.");
					break;
				}
				case 4: {
					if (perso.hasItemTemplate(2430, 10)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2433) + "~" + 2433);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2432) + "~" + 2432);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2431) + "~" + 2431);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2430) + "~" + 2430);
						perso.removeByTemplateID(2430, perso.getNbItemTemplate(2430));
						perso.removeByTemplateID(2431, perso.getNbItemTemplate(2431));
						perso.removeByTemplateID(2432, perso.getNbItemTemplate(2432));
						perso.removeByTemplateID(2433, perso.getNbItemTemplate(2433));
						perso.teleport((short) 6831, 422);
						break;
					}
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez pas le bon nombre de flaque de Bleue.");
					break;
				}
				case 5: {
					if (perso.hasItemTemplate(2433, 10) && perso.hasItemTemplate(2432, 10)
							&& perso.hasItemTemplate(2431, 10) && perso.hasItemTemplate(2430, 10)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2433) + "~" + 2433);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2432) + "~" + 2432);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2431) + "~" + 2431);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2430) + "~" + 2430);
						perso.removeByTemplateID(2430, perso.getNbItemTemplate(2430));
						perso.removeByTemplateID(2431, perso.getNbItemTemplate(2431));
						perso.removeByTemplateID(2432, perso.getNbItemTemplate(2432));
						perso.removeByTemplateID(2433, perso.getNbItemTemplate(2433));
						perso.teleport((short) 6835, 422);
						break;
					}
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous n'avez pas le bon nombre de flaque pour combattre 2 gel\u00e9es royales.");
					break;
				}
				case 6: {
					if (perso.hasItemTemplate(2433, 15) && perso.hasItemTemplate(2432, 15)
							&& perso.hasItemTemplate(2431, 15) && perso.hasItemTemplate(2430, 15)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2433) + "~" + 2433);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2432) + "~" + 2432);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2431) + "~" + 2431);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + perso.getNbItemTemplate(2430) + "~" + 2430);
						perso.removeByTemplateID(2430, perso.getNbItemTemplate(2430));
						perso.removeByTemplateID(2431, perso.getNbItemTemplate(2431));
						perso.removeByTemplateID(2432, perso.getNbItemTemplate(2432));
						perso.removeByTemplateID(2433, perso.getNbItemTemplate(2433));
						perso.teleport((short) 6836, 422);
						break;
					}
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous n'avez pas le bon nombre de flaque pour combattre 4 gel\u00e9es royales.");
					break;
				}
				}
				break;
			}
			break;
		}
		case 508: {
			if (perso.getCurMap().getId() == 8317) {
				perso.teleport((short) 8236, 370);
				final org.aestia.object.Object newObjAdded13 = World.getObjTemplate(7415).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded13, true, -1)) {
					World.addObjet(newObjAdded13, true);
					perso.addObjet(newObjAdded13);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~7415");
				break;
			}
			break;
		}
		case 509: {
			perso.teleport((short) 4786, 300);
			final org.aestia.object.Object newObjAdded13 = World.getObjTemplate(6885).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded13, true, -1)) {
				World.addObjet(newObjAdded13, true);
				perso.addObjet(newObjAdded13);
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~6885");
			final org.aestia.object.Object newObjAdded14 = World.getObjTemplate(8388).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded14, true, -1)) {
				World.addObjet(newObjAdded14, true);
				perso.addObjet(newObjAdded14);
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~8388");
			break;
		}
		case 510: {
			if (perso.getCurMap().getId() == 3373 && perso.hasItemTemplate(6885, 1)) {
				perso.removeByTemplateID(6885, 1);
				final org.aestia.object.Object newObjAdded15 = World.getObjTemplate(6887).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded15, true, -1)) {
					World.addObjet(newObjAdded15, true);
					perso.addObjet(newObjAdded15);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~6885");
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~6887");
				break;
			}
			break;
		}
		case 511: {
			final int cadeau = Loterie.getCadeauBworker();
			final org.aestia.object.Object newObjAdded16 = World.getObjTemplate(cadeau).createNewItem(1, false);
			if (!perso.addObjetSimiler(newObjAdded16, true, -1)) {
				World.addObjet(newObjAdded16, true);
				perso.addObjet(newObjAdded16);
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + cadeau);
			break;
		}
		case 512: {
			if (perso.getCurMap().getId() == 10213) {
				perso.teleport((short) 6536, 273);
				final org.aestia.object.Object newObjAdded17 = World.getObjTemplate(8476).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded17, true, -1)) {
					World.addObjet(newObjAdded17, true);
					perso.addObjet(newObjAdded17);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~8476");
				break;
			}
			break;
		}
		case 513: {
			if (perso.getCurMap().getId() == 10199) {
				perso.teleport((short) 6738, 213);
				final org.aestia.object.Object newObjAdded17 = World.getObjTemplate(8477).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded17, true, -1)) {
					World.addObjet(newObjAdded17, true);
					perso.addObjet(newObjAdded17);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~8477");
				break;
			}
			break;
		}
		case 514: {
			if (perso.getCurMap().getId() == 9638 && perso.hasItemTemplate(8476, 1) && perso.hasItemTemplate(8477, 1)) {
				perso.teleport((short) 10141, 448);
				perso.removeByTemplateID(8476, 1);
				perso.removeByTemplateID(8477, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8476");
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8477");
				break;
			}
			break;
		}
		case 515: {
			if (perso.getCurMap().getId() == 8497) {
				perso.teleport((short) 8167, 252);
				final org.aestia.object.Object newObjAdded17 = World.getObjTemplate(7414).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded17, true, -1)) {
					World.addObjet(newObjAdded17, true);
					perso.addObjet(newObjAdded17);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~7414");
				break;
			}
			break;
		}
		case 516: {
			if (perso.getCurMap().getId() == 1140 && perso.get_kamas() >= 1000L) {
				final org.aestia.object.Object newObjAdded17 = World.getObjTemplate(2239).createNewItem(1, false);
				if (!perso.addObjetSimiler(newObjAdded17, true, -1)) {
					World.addObjet(newObjAdded17, true);
					perso.addObjet(newObjAdded17);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~2239");
				break;
			}
			break;
		}
		case 517: {
			final int mapId = Integer.parseInt(this.args.split(";")[0].split(",")[0]);
			final int cellId = Integer.parseInt(this.args.split(";")[0].split(",")[1]);
			final short mapSecu = Short.parseShort(this.args.split(";")[1]);
			final int id = Integer.parseInt(this.args.split(";")[2]);
			if (perso.getCurMap().getId() != mapSecu) {
				return true;
			}
			if (perso.getCurMap().getId() == 9052) {
				if (perso.getCurCell().getId() != 268 || perso.get_orientation() != 7) {
					return true;
				}
				if (!perso.hasItemType(90)) {
					SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'avez pas de fant\u00f4me de familier.");
					return true;
				}
			}
			perso.teleport((short) mapId, cellId);
			perso.setFullMorph(id, false, false);
			break;
		}
		case 518: {
			final int mapId = Integer.parseInt(this.args.split(";")[0].split(",")[0]);
			final int cellId = Integer.parseInt(this.args.split(";")[0].split(",")[1]);
			final short mapSecu = Short.parseShort(this.args.split(";")[1]);
			if (perso.getCurMap().getId() != mapSecu) {
				return true;
			}
			perso.unsetFullMorph();
			perso.teleport((short) mapId, cellId);
			break;
		}
		case 519: {
			final int mapId = Integer.parseInt(this.args.split(";")[0].split(",")[0]);
			final int cellId = Integer.parseInt(this.args.split(";")[0].split(",")[1]);
			final short mapSecu = Short.parseShort(this.args.split(";")[1]);
			if (perso.getCurMap().getId() != mapSecu) {
				return true;
			}
			org.aestia.object.Object obj19 = World.getObjTemplate(Integer.parseInt(this.args.split(";")[2]))
					.createNewItem(1, false);
			if (obj19 != null && perso.addObjet(obj19, true)) {
				World.addObjet(obj19, true);
			}
			perso.send("Im021;1~" + this.args.split(";")[2]);
			obj19 = World.getObjTemplate(Integer.parseInt(this.args.split(";")[3])).createNewItem(1, false);
			if (obj19 != null && perso.addObjet(obj19, true)) {
				World.addObjet(obj19, true);
			}
			perso.send("Im021;1~" + this.args.split(";")[3]);
			perso.teleport((short) mapId, cellId);
			break;
		}
		case 520: {
			if (perso.getCurMap().getId() != 8497) {
				return true;
			}
			org.aestia.object.Object obj19 = World.getObjTemplate(7414).createNewItem(1, false);
			if (perso.addObjet(obj19, true)) {
				World.addObjet(obj19, true);
			}
			perso.send("Im021;1~7414");
			if (!perso.getStaticEmote().containsKey(15)) {
				obj19 = World.getObjTemplate(7413).createNewItem(1, false);
				if (perso.addObjet(obj19, true)) {
					World.addObjet(obj19, true);
				}
				perso.send("Im021;1~7413");
			}
			perso.teleport((short) 8167, 252);
			break;
		}
		case 521: {
			if (perso.getCurMap().getId() != 9248) {
				return true;
			}
			if (perso.hasItemTemplate(7887, 1) && perso.hasItemTemplate(7888, 1) && perso.hasItemTemplate(7889, 1)
					&& perso.hasItemTemplate(7890, 1)) {
				perso.removeByTemplateID(7887, 1);
				perso.removeByTemplateID(7888, 1);
				perso.removeByTemplateID(7889, 1);
				perso.removeByTemplateID(7890, 1);
				perso.send("Im022;1~7887");
				perso.send("Im022;1~7888");
				perso.send("Im022;1~7889");
				perso.send("Im022;1~7890");
				final org.aestia.object.Object obj19 = World.getObjTemplate(8073).createNewItem(1, false);
				if (perso.addObjet(obj19, true)) {
					World.addObjet(obj19, true);
				}
				perso.send("Im021;1~8073");
				break;
			}
			perso.send("Im119|45");
			break;
		}
		case 522: {
			if (perso.getCurMap().getId() != 8349) {
				return true;
			}
			final org.aestia.object.Object obj19 = World.getObjTemplate(6978).createNewItem(1, false);
			if (perso.addObjet(obj19, true)) {
				World.addObjet(obj19, true);
			}
			perso.send("Im021;1~6978");
			perso.teleport((short) 8467, 227);
			break;
		}
		case 523: {
			if (perso.getCurMap().getId() != 1779) {
				return true;
			}
			if (!perso.hasItemTemplate(361, 100)) {
				perso.send("Im14");
				break;
			}
			perso.removeByTemplateID(361, 100);
			perso.send("Im022;100~361");
			perso.learnSpell(367, 1, true, true, true);
			final NpcQuestion quest4 = World.getNPCQuestion(473);
			if (quest4 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			SocketManager.GAME_SEND_QUESTION_PACKET(out, quest4.parse(perso));
			return false;
		}
		case 524: {
			final int qID2 = Monster.MobGroup.MAITRE_CORBAC.check();
			final NpcQuestion quest5 = World.getNPCQuestion(qID2);
			if (quest5 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			SocketManager.GAME_SEND_QUESTION_PACKET(out, quest5.parse(perso));
			return false;
		}
		case 525: {
			final Monster.MobGroup group4 = perso.hasMobGroup();
			final String[] split = this.args.split(";");
			for (final Monster.MobGrade mb : group4.getMobs().values()) {
				switch (mb.getTemplate().getId()) {
				case 289: {
					perso.teleport((short) 9604, 403);
					return true;
				}
				case 819: {
					perso.teleport(Short.parseShort(split[0].split(",")[0]), Integer.parseInt(split[0].split(",")[1]));
					return true;
				}
				case 820: {
					perso.teleport(Short.parseShort(split[1].split(",")[0]), Integer.parseInt(split[1].split(",")[1]));
					return true;
				}
				default: {
					continue;
				}
				}
			}
			break;
		}
		case 526: {
			if (perso.getCurMap().getId() != 9604) {
				return true;
			}
			final org.aestia.object.Object obj19 = World.getObjTemplate(7703).createNewItem(1, false);
			if (perso.addObjet(obj19, true)) {
				World.addObjet(obj19, true);
			}
			perso.send("Im021;1~7703");
			perso.teleport((short) 2985, 279);
			break;
		}
		case 527: {
			if (perso.getCurMap().getId() != 10165) {
				return true;
			}
			perso.addStaticEmote(19);
			perso.teleport((short) 10155, 210);
			break;
		}
		case 964: {
			if (perso.getCurMap().getId() != 10255) {
				return true;
			}
			if (perso.get_align() != 1 && perso.get_align() != 2) {
				return true;
			}
			if (perso.hasItemTemplate(9487, 1)) {
				final String date3 = perso.getItemTemplate(9487, 1).getTxtStat().get(805);
				final long timeStamp3 = Long.parseLong(date3);
				if (System.currentTimeMillis() - timeStamp3 <= 1209600000L) {
					return true;
				}
				perso.removeByTemplateID(9487, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~9487");
			}
			if (perso.hasItemTemplate(9811, 1)) {
				perso.removeByTemplateID(9811, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~9811");
				perso.modifAlignement(0);
			} else if (perso.hasItemTemplate(9812, 1)) {
				if (perso.hasItemTemplate(9488, 1)) {
					perso.removeByTemplateID(9488, 1);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~9488");
					perso.modifAlignement(1);
				} else if (perso.hasItemTemplate(9489, 1)) {
					perso.removeByTemplateID(9489, 1);
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~9489");
					perso.modifAlignement(2);
				}
				perso.removeByTemplateID(9812, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~9812");
			}
			final ObjectTemplate t2 = World.getObjTemplate(9487);
			final org.aestia.object.Object obj20 = t2.createNewItem(1, false);
			obj20.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
			if (perso.addObjet(obj20, false)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj20.getTemplate().getId());
				World.addObjet(obj20, true);
			}
			final NpcQuestion quest5 = World.getNPCQuestion(Integer.parseInt(this.args));
			if (quest5 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			try {
				SocketManager.GAME_SEND_QUESTION_PACKET(out, quest5.parse(perso));
				return false;
			} catch (Exception e19) {
				e19.printStackTrace();
				break;
			}
		}
		case 965: {
			if (perso.getCurMap().getId() != 10255) {
				return true;
			}
			if (perso.get_align() != 1 && perso.get_align() != 2) {
				return true;
			}
			if (perso.hasItemTemplate(9487, 1)) {
				final String date4 = perso.getItemTemplate(9487, 1).getTxtStat().get(805);
				final long timeStamp4 = Long.parseLong(date4);
				if (System.currentTimeMillis() - timeStamp4 <= 1209600000L) {
					return true;
				}
			}
			boolean next = false;
			if (perso.hasItemTemplate(9811, 1)) {
				next = true;
			} else if (perso.hasItemTemplate(9812, 1)) {
				int idTemp = -1;
				if (perso.get_align() == 2) {
					idTemp = 9488;
				} else {
					idTemp = 9489;
				}
				final ObjectTemplate t3 = World.getObjTemplate(idTemp);
				final org.aestia.object.Object obj21 = t3.createNewItem(1, false);
				obj21.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
				if (perso.addObjet(obj21, false)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj21.getTemplate().getId());
					World.addObjet(obj21, true);
				}
				next = true;
			}
			if (!next) {
				break;
			}
			final NpcQuestion quest5 = World.getNPCQuestion(Integer.parseInt(this.args));
			if (quest5 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			try {
				SocketManager.GAME_SEND_QUESTION_PACKET(out, quest5.parse(perso));
				return false;
			} catch (Exception e20) {
				e20.printStackTrace();
				break;
			}
		}
		case 963: {
			if (perso.getCurMap().getId() != 10255) {
				return true;
			}
			if (perso.get_align() != 1 && perso.get_align() != 2) {
				return true;
			}
			if (perso.hasItemTemplate(9487, 1)) {
				final String date5 = perso.getItemTemplate(9487, 1).getTxtStat().get(805);
				final long timeStamp5 = Long.parseLong(date5);
				if (System.currentTimeMillis() - timeStamp5 <= 1209600000L) {
					return true;
				}
			}
			final ObjectTemplate t2 = World.getObjTemplate(9812);
			final org.aestia.object.Object obj20 = t2.createNewItem(1, false);
			obj20.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
			if (perso.addObjet(obj20, false)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj20.getTemplate().getId());
				World.addObjet(obj20, true);
			}
			final NpcQuestion quest5 = World.getNPCQuestion(Integer.parseInt(this.args));
			if (quest5 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			try {
				SocketManager.GAME_SEND_QUESTION_PACKET(out, quest5.parse(perso));
				return false;
			} catch (Exception e20) {
				e20.printStackTrace();
				break;
			}
		}
		case 966: {
			if (perso.getCurMap().getId() != 10255) {
				return true;
			}
			if (perso.get_align() != 1 && perso.get_align() != 2) {
				return true;
			}
			if (perso.hasItemTemplate(9487, 1)) {
				final String date5 = perso.getItemTemplate(9487, 1).getTxtStat().get(805);
				final long timeStamp5 = Long.parseLong(date5);
				if (System.currentTimeMillis() - timeStamp5 <= 1209600000L) {
					return true;
				}
			}
			int kamas = 256000;
			if (perso.getALvl() <= 10) {
				kamas = 500;
			} else if (perso.getALvl() <= 20) {
				kamas = 1000;
			} else if (perso.getALvl() <= 30) {
				kamas = 2000;
			} else if (perso.getALvl() <= 40) {
				kamas = 4000;
			} else if (perso.getALvl() <= 50) {
				kamas = 8000;
			} else if (perso.getALvl() <= 60) {
				kamas = 16000;
			} else if (perso.getALvl() <= 70) {
				kamas = 32000;
			} else if (perso.getALvl() <= 80) {
				kamas = 64000;
			} else if (perso.getALvl() <= 90) {
				kamas = 128000;
			} else if (perso.getALvl() <= 100) {
				kamas = 256000;
			}
			if (perso.get_kamas() < kamas) {
				SocketManager.GAME_SEND_MESSAGE_SERVER(perso, "10|" + kamas);
				return true;
			}
			perso.set_kamas(perso.get_kamas() - kamas);
			SocketManager.GAME_SEND_STATS_PACKET(perso);
			SocketManager.GAME_SEND_Im_PACKET(perso, "046;" + kamas);
			if (perso.hasItemTemplate(9811, 1)) {
				perso.removeByTemplateID(9811, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~9811");
			}
			final ObjectTemplate t3 = World.getObjTemplate(9811);
			final org.aestia.object.Object obj21 = t3.createNewItem(1, false);
			obj21.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
			if (perso.addObjet(obj21, false)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj21.getTemplate().getId());
				World.addObjet(obj21, true);
			}
			final NpcQuestion quest5 = World.getNPCQuestion(Integer.parseInt(this.args));
			if (quest5 == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				return true;
			}
			try {
				SocketManager.GAME_SEND_QUESTION_PACKET(out, quest5.parse(perso));
				return false;
			} catch (Exception e21) {
				e21.printStackTrace();
				break;
			}
		}
		case 967: {
			if (perso.getCurMap().getId() != 8736 && perso.getCurMap().getId() != 8737) {
				return true;
			}
			final Job brico = World.getMetier(65);
			if (brico == null) {
				return true;
			}
			if (perso.getMetierByID(brico.getId()) != null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "111");
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				perso.setIsOnDialogAction(-1);
				return true;
			}
			if ((perso.getMetierByID(2) != null && perso.getMetierByID(2).get_lvl() < 30)
					|| (perso.getMetierByID(11) != null && perso.getMetierByID(11).get_lvl() < 30)
					|| (perso.getMetierByID(13) != null && perso.getMetierByID(13).get_lvl() < 30)
					|| (perso.getMetierByID(14) != null && perso.getMetierByID(14).get_lvl() < 30)
					|| (perso.getMetierByID(15) != null && perso.getMetierByID(15).get_lvl() < 30)
					|| (perso.getMetierByID(16) != null && perso.getMetierByID(16).get_lvl() < 30)
					|| (perso.getMetierByID(17) != null && perso.getMetierByID(17).get_lvl() < 30)
					|| (perso.getMetierByID(18) != null && perso.getMetierByID(18).get_lvl() < 30)
					|| (perso.getMetierByID(19) != null && perso.getMetierByID(19).get_lvl() < 30)
					|| (perso.getMetierByID(20) != null && perso.getMetierByID(20).get_lvl() < 30)
					|| (perso.getMetierByID(24) != null && perso.getMetierByID(24).get_lvl() < 30)
					|| (perso.getMetierByID(25) != null && perso.getMetierByID(25).get_lvl() < 30)
					|| (perso.getMetierByID(26) != null && perso.getMetierByID(26).get_lvl() < 30)
					|| (perso.getMetierByID(27) != null && perso.getMetierByID(27).get_lvl() < 30)
					|| (perso.getMetierByID(28) != null && perso.getMetierByID(28).get_lvl() < 30)
					|| (perso.getMetierByID(31) != null && perso.getMetierByID(31).get_lvl() < 30)
					|| (perso.getMetierByID(36) != null && perso.getMetierByID(36).get_lvl() < 30)
					|| (perso.getMetierByID(41) != null && perso.getMetierByID(41).get_lvl() < 30)
					|| (perso.getMetierByID(56) != null && perso.getMetierByID(56).get_lvl() < 30)
					|| (perso.getMetierByID(58) != null && perso.getMetierByID(58).get_lvl() < 30)
					|| (perso.getMetierByID(60) != null && perso.getMetierByID(60).get_lvl() < 30)) {
				SocketManager.send(out, "DQ336|4840");
				return true;
			}
			if (perso.totalJobBasic() > 2) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "19");
				SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
				perso.set_isTalkingWith(0);
				perso.setIsOnDialogAction(-1);
				return true;
			}
			if (perso.hasItemTemplate(459, 20) && perso.hasItemTemplate(7657, 15)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;20~459");
				perso.removeByTemplateID(459, 20);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;15~7657");
				perso.removeByTemplateID(7657, 15);
				perso.learnJob(brico);
				SocketManager.send(out, "DQ3153|4840");
				break;
			}
			SocketManager.send(out, "DQ3151|4840");
			return true;
		}
		case 968: {
			if (perso.getCurMap().getId() == 9877 || perso.getCurMap().getId() == 9881) {
				perso.teleport((short) 9538, 186);
				break;
			}
			break;
		}
		case 969: {
			if (perso.getCurMap().getId() == 8715) {
				perso.teleport((short) 8716, 366);
				perso.setFullMorph(11, false, false);
				break;
			}
			if (perso.getCurMap().getId() == 9120) {
				perso.teleport((short) 9121, 69);
				perso.setFullMorph(11, false, false);
				break;
			}
			break;
		}
		case 970: {
			if (perso.getCurMap().getId() == 8719) {
				perso.unsetFullMorph();
				perso.teleport((short) 10154, 335);
				break;
			}
			if (perso.getCurMap().getId() == 9123) {
				perso.unsetFullMorph();
				perso.teleport((short) 9125, 71);
				break;
			}
			break;
		}
		case 971: {
			if (perso.getCurMap().getId() != 9788) {
				return true;
			}
			if (!perso.hasItemTemplate(8342, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(8343, 1)) {
				return true;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8342");
			perso.removeByTemplateID(8342, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8343");
			perso.removeByTemplateID(8343, 1);
			perso.teleport((short) 10098, 407);
			break;
		}
		case 972: {
			if (perso.getCurMap().getId() != 8978) {
				return true;
			}
			if (!perso.hasItemTemplate(7935, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7936, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7937, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7938, 1)) {
				return true;
			}
			break;
		}
		case 973: {
			if (perso.getCurMap().getId() != 8978) {
				return true;
			}
			if (!perso.hasItemTemplate(7935, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7936, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7937, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7938, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(8073, 1)) {
				return true;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~7935");
			perso.removeByTemplateID(7935, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~7936");
			perso.removeByTemplateID(7936, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~7937");
			perso.removeByTemplateID(7937, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~7938");
			perso.removeByTemplateID(7938, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8073");
			perso.removeByTemplateID(8073, 1);
			final ObjectTemplate dofus = World.getObjTemplate(8072);
			final org.aestia.object.Object obj22 = dofus.createNewItem(1, false);
			if (perso.addObjet(obj22, false)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj22.getTemplate().getId());
				World.addObjet(obj22, true);
			}
			perso.teleport((short) 9503, 357);
			break;
		}
		case 974: {
			if (perso.getCurMap().getId() != 8978) {
				return true;
			}
			if (!perso.hasItemTemplate(8075, 10)) {
				return true;
			}
			if (!perso.hasItemTemplate(8076, 10)) {
				return true;
			}
			if (!perso.hasItemTemplate(8077, 10)) {
				return true;
			}
			if (!perso.hasItemTemplate(8064, 10)) {
				return true;
			}
			if (perso.hasSpell(364)) {
				return true;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;10~8075");
			perso.removeByTemplateID(8075, 10);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;10~8076");
			perso.removeByTemplateID(8076, 10);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;10~8077");
			perso.removeByTemplateID(8077, 10);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;10~8064");
			perso.removeByTemplateID(8064, 10);
			perso.learnSpell(364, 1, true, true, true);
			break;
		}
		case 975: {
			if (perso.getCurMap().getId() != 8973) {
				return true;
			}
			if (!perso.hasItemTemplate(7935, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7936, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7937, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(7938, 1)) {
				return true;
			}
			if (!perso.hasItemTemplate(8073, 1)) {
				return true;
			}
			perso.teleport((short) 8977, 448);
			break;
		}
		case 976: {
			try {
				if (perso.getCurMap().getId() != 9557) {
					return true;
				}
				if (!perso.hasItemTemplate(8305, 1)) {
					return true;
				}
				if (!perso.hasItemTemplate(8306, 1)) {
					return true;
				}
				if (!perso.hasItemTemplate(7924, 1)) {
					return true;
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8305");
				perso.removeByTemplateID(8305, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~8306");
				perso.removeByTemplateID(8306, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~7924");
				perso.removeByTemplateID(7924, 1);
				perso.teleport((short) 9880, 399);
				break;
			} catch (Exception e22) {
				return true;
			}
		}
		case 977: {
			try {
				switch (perso.getCurMap().getId()) {
				case 9553:
				case 9554:
				case 9555:
				case 9556:
				case 9557:
				case 9558:
				case 9559:
				case 9560:
				case 9561:
				case 9562:
				case 9563:
				case 9564:
				case 9565:
				case 9566:
				case 9567:
				case 9568:
				case 9569:
				case 9570:
				case 9571:
				case 9572:
				case 9573:
				case 9574:
				case 9575:
				case 9576:
				case 9577: {
					perso.teleport((short) 9876, 287);
					break;
				}
				}
				break;
			} catch (Exception e22) {
				return true;
			}
		}
		case 978: {
			try {
				switch (perso.getCurMap().getId()) {
				case 9371:
				case 9372:
				case 9373:
				case 9374:
				case 9375:
				case 9376:
				case 9377:
				case 9378:
				case 9379:
				case 9380:
				case 9381:
				case 9382:
				case 9383:
				case 9384:
				case 9385:
				case 9386:
				case 9387:
				case 9388:
				case 9389:
				case 9390:
				case 9391:
				case 9392:
				case 9393:
				case 9394: {
					perso.teleport((short) 9396, 387);
					break;
				}
				}
				break;
			} catch (Exception e22) {
				return true;
			}
		}
		case 979: {
			try {
				final short newMapID2 = Short.parseShort(this.args.split(",", 2)[0]);
				final Map newMap = World.getMap(newMapID2);
				final int newCellID2 = Integer.parseInt(this.args.split(",", 2)[1]);
				final Case curCase = perso.getCurCell();
				final Map curMap = perso.getCurMap();
				final int idCurCase = curCase.getId();
				if (idCurCase < 52 || idCurCase > 412) {
					perso.teleportLaby(newMapID2, newCellID2);
					perso.getWaiter().addNext(new Runnable() {
						@Override
						public void run() {
							Dc.fermer(curMap, curCase);
							Dc.fermer(newMap, Dc.getCaseBas(newMap));
							Dc.fermer(newMap, Dc.getCaseHaut(newMap));
							Dc.ouvrir(newMap, Dc.getCaseDroite(newMap));
							Dc.ouvrir(newMap, Dc.getCaseGauche(newMap));
						}
					}, 1000L);
					break;
				}
				if (idCurCase == 262 || idCurCase == 320 || idCurCase == 144 || idCurCase == 216 || idCurCase == 231
						|| idCurCase == 274) {
					perso.teleportLaby(newMapID2, newCellID2);
					perso.getWaiter().addNext(new Runnable() {
						@Override
						public void run() {
							Dc.fermer(curMap, curCase);
							Dc.fermer(newMap, Dc.getCaseGauche(newMap));
							Dc.fermer(newMap, Dc.getCaseDroite(newMap));
							Dc.ouvrir(newMap, Dc.getCaseHaut(newMap));
							Dc.ouvrir(newMap, Dc.getCaseBas(newMap));
						}
					}, 1000L);
					break;
				}
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Cette porte n'est pas fonctionnelle. Veuillez reporter la map et la porte sur le forum.");
				return true;
			} catch (Exception e22) {
				return true;
			}
		}
		case 980: {
			try {
				final int mapId = Integer.parseInt(this.args.split(",")[0]);
				final int cellId = Integer.parseInt(this.args.split(",")[1]);
				final int item = Integer.parseInt(this.args.split(",")[2]);
				final int item2 = Integer.parseInt(this.args.split(",")[3]);
				final short mapSecu = Short.parseShort(this.args.split(",")[4]);
				if (perso.getCurMap().getId() != mapSecu) {
					return true;
				}
				if (!perso.hasItemTemplate(item, 1) && !perso.hasItemTemplate(item2, 1)) {
					return true;
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item);
				perso.removeByTemplateID(item, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item2);
				perso.removeByTemplateID(item2, 1);
				perso.teleport((short) mapId, cellId);
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 981: {
			try {
				final int mapId = Integer.parseInt(this.args.split(",")[0]);
				final int cellId = Integer.parseInt(this.args.split(",")[1]);
				final int item = Integer.parseInt(this.args.split(",")[2]);
				final short mapSecu = Short.parseShort(this.args.split(",")[3]);
				if (perso.getCurMap().getId() != mapSecu) {
					return true;
				}
				if (!perso.hasItemTemplate(item, 1)) {
					return true;
				}
				perso.teleport((short) mapId, cellId);
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 982: {
			try {
				perso.setFuneral();
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 983: {
			try {
				final Quest q3 = Quest.getQuestById(193);
				if (q3 == null) {
					return true;
				}
				final Map curMap2 = perso.getCurMap();
				if (curMap2.getId() != 10332) {
					return true;
				}
				if (perso.getQuestPersoByQuest(q3) == null) {
					q3.applyQuest(perso);
				} else if (q3.getQuestEtapeCurrent(perso.getQuestPersoByQuest(q3)).getId() != 793) {
					return true;
				}
				final Monster petitChef = World.getMonstre(984);
				if (petitChef == null) {
					return true;
				}
				final Monster.MobGrade mg = petitChef.getGradeByLevel(10);
				if (mg == null) {
					return true;
				}
				final Monster.MobGroup _mg = new Monster.MobGroup(perso.getCurMap().nextObjectId,
						perso.getCurCell().getId(),
						String.valueOf(petitChef.getId()) + "," + mg.getLevel() + "," + mg.getLevel() + ";");
				perso.getCurMap().startFightVersusMonstres(perso, _mg);
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 984: {
			try {
				final int xp2 = Integer.parseInt(this.args.split(",")[0]);
				final int mapCurId = Integer.parseInt(this.args.split(",")[1]);
				final int idQuest = Integer.parseInt(this.args.split(",")[2]);
				if (perso.getCurMap().getId() != (short) mapCurId) {
					return true;
				}
				final Quest.Quest_Perso qp = perso.getQuestPersoByQuestId(idQuest);
				if (qp == null) {
					return true;
				}
				if (qp.isFinish()) {
					return true;
				}
				perso.addXp(xp2);
				SocketManager.GAME_SEND_Im_PACKET(perso, "08;" + xp2);
				qp.setFinish(true);
				SocketManager.GAME_SEND_Im_PACKET(perso, "055;" + idQuest);
				SocketManager.GAME_SEND_Im_PACKET(perso, "056;" + idQuest);
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 985: {
			try {
				final int item = Integer.parseInt(this.args.split(",")[0]);
				final int item2 = Integer.parseInt(this.args.split(",")[1]);
				final int mapCurId2 = Integer.parseInt(this.args.split(",")[2]);
				final int metierId = Integer.parseInt(this.args.split(",")[3]);
				if (perso.getCurMap().getId() != (short) mapCurId2) {
					return true;
				}
				final Job metierArgs = World.getMetier(metierId);
				if (metierArgs == null) {
					return true;
				}
				if (perso.getMetierByID(metierId) != null) {
					SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
					perso.set_isTalkingWith(0);
					perso.setIsOnDialogAction(-1);
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				final ObjectTemplate t4 = World.getObjTemplate(item2);
				if (t4 == null) {
					return true;
				}
				if (perso.hasItemTemplate(item, 1)) {
					for (final java.util.Map.Entry<Integer, JobStat> entry2 : perso.getMetiers().entrySet()) {
						if (entry2.getValue().get_lvl() < 30 && !entry2.getValue().getTemplate().isMaging()) {
							SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
							perso.set_isTalkingWith(0);
							perso.setIsOnDialogAction(-1);
							SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
							return true;
						}
					}
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item);
					perso.removeByTemplateID(item, 1);
					final org.aestia.object.Object obj22 = t4.createNewItem(1, false);
					obj22.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
					if (perso.addObjet(obj22, false)) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj22.getTemplate().getId());
						World.addObjet(obj22, true);
					}
					perso.learnJob(World.getMetier(metierId));
					Database.getStatique().getPlayerData().update(perso, true);
					SocketManager.GAME_SEND_Ow_PACKET(perso);
				}
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 986: {
			try {
				final int mapCurId3 = Integer.parseInt(this.args.split(",")[0]);
				final int item3 = Integer.parseInt(this.args.split(",")[1]);
				final int item4 = Integer.parseInt(this.args.split(",")[2]);
				final int metierId = Integer.parseInt(this.args.split(",")[3]);
				if (perso.getCurMap().getId() != (short) mapCurId3) {
					return true;
				}
				final Job metierArgs = World.getMetier(metierId);
				if (metierArgs == null) {
					return true;
				}
				if (perso.getMetierByID(metierId) != null) {
					SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
					perso.set_isTalkingWith(0);
					perso.setIsOnDialogAction(-1);
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				if (!perso.hasItemTemplate(item3, 1)) {
					break;
				}
				perso.removeByTemplateID(item3, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item3);
				final ObjectTemplate t4 = World.getObjTemplate(item4);
				if (t4 == null) {
					break;
				}
				final org.aestia.object.Object obj22 = t4.createNewItem(1, false);
				obj22.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
				if (perso.addObjet(obj22, false)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj22.getTemplate().getId());
					World.addObjet(obj22, true);
					Database.getStatique().getPlayerData().update(perso, true);
					SocketManager.GAME_SEND_Ow_PACKET(perso);
					return false;
				}
				break;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 987: {
			try {
				final int item = Integer.parseInt(this.args.split(",")[0]);
				final int item2 = Integer.parseInt(this.args.split(",")[1]);
				final int item5 = Integer.parseInt(this.args.split(",")[2]);
				final int mapCurId4 = Integer.parseInt(this.args.split(",")[3]);
				final int metierId2 = Integer.parseInt(this.args.split(",")[4]);
				if (perso.getCurMap().getId() != (short) mapCurId4) {
					return true;
				}
				final Job metierArgs2 = World.getMetier(metierId2);
				if (metierArgs2 == null) {
					return true;
				}
				if (perso.getMetierByID(metierId2) != null) {
					SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
					perso.set_isTalkingWith(0);
					perso.setIsOnDialogAction(-1);
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				if (!perso.hasItemTemplate(item, 1) || !perso.hasItemTemplate(item2, 1)) {
					break;
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item);
				perso.removeByTemplateID(item, 1);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item2);
				perso.removeByTemplateID(item2, 1);
				final ObjectTemplate t5 = World.getObjTemplate(item5);
				if (t5 == null) {
					break;
				}
				final org.aestia.object.Object obj22 = t5.createNewItem(1, false);
				if (perso.addObjet(obj22, false)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj22.getTemplate().getId());
					World.addObjet(obj22, true);
					Database.getStatique().getPlayerData().update(perso, true);
					SocketManager.GAME_SEND_Ow_PACKET(perso);
					return false;
				}
				break;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 988: {
			try {
				if (!perso.hasItemTemplate(2107, 1)) {
					break;
				}
				final long timeStamp6 = Long.parseLong(perso.getItemTemplate(2107, 1).getTxtStat().get(805));
				final boolean success = System.currentTimeMillis() - timeStamp6 <= 120000L;
				final NpcQuestion qQuest = World.getNPCQuestion(success ? 1171 : 1172);
				SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~2107");
				perso.removeByTemplateID(2107, 1);
				if (qQuest == null) {
					SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
					perso.set_isTalkingWith(0);
					return true;
				}
				if (success) {
					final Job metierArgs = World.getMetier(36);
					if (metierArgs == null) {
						return true;
					}
					if (perso.getMetierByID(36) != null) {
						SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
						perso.set_isTalkingWith(0);
						perso.setIsOnDialogAction(-1);
						SocketManager.GAME_SEND_Im_PACKET(perso, "111");
						return true;
					}
					for (final java.util.Map.Entry<Integer, JobStat> entry3 : perso.getMetiers().entrySet()) {
						if (entry3.getValue().get_lvl() < 30 && !entry3.getValue().getTemplate().isMaging()) {
							SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
							perso.set_isTalkingWith(0);
							perso.setIsOnDialogAction(-1);
							SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
							return true;
						}
					}
					perso.learnJob(World.getMetier(36));
					Database.getStatique().getPlayerData().update(perso, true);
					SocketManager.GAME_SEND_Ow_PACKET(perso);
				}
				SocketManager.GAME_SEND_QUESTION_PACKET(out, qQuest.parse(perso));
				return false;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 989: {
			try {
				final int mapCurId3 = Integer.parseInt(this.args.split(",")[0]);
				final int item3 = Integer.parseInt(this.args.split(",")[1]);
				final int item4 = Integer.parseInt(this.args.split(",")[2]);
				final int metierId = Integer.parseInt(this.args.split(",")[3]);
				if (perso.getCurMap().getId() != (short) mapCurId3) {
					return true;
				}
				final Job metierArgs = World.getMetier(metierId);
				if (metierArgs == null) {
					return true;
				}
				if (perso.getMetierByID(metierId) != null) {
					SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
					perso.set_isTalkingWith(0);
					perso.setIsOnDialogAction(-1);
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				if (!perso.hasItemTemplate(item3, 1)) {
					break;
				}
				final ObjectTemplate t4 = World.getObjTemplate(item4);
				if (t4 == null) {
					break;
				}
				final org.aestia.object.Object obj22 = t4.createNewItem(1, false);
				if (perso.addObjet(obj22, false)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj22.getTemplate().getId());
					World.addObjet(obj22, true);
					Database.getStatique().getPlayerData().update(perso, true);
					SocketManager.GAME_SEND_Ow_PACKET(perso);
					return false;
				}
				break;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 990: {
			try {
				if (perso.getCurMap().getId() == 7388) {
					if (perso.hasItemTemplate(2039, 1) && perso.hasItemTemplate(2041, 1)) {
						final long timeStamp6 = Long.parseLong(perso.getItemTemplate(2039, 1).getTxtStat().get(805));
						final boolean success = System.currentTimeMillis() - timeStamp6 <= 120000L;
						final NpcQuestion qQuest = World.getNPCQuestion(success ? 2364 : 1175);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~2039");
						perso.removeByTemplateID(2039, 1);
						SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~2041");
						perso.removeByTemplateID(2041, 1);
						if (qQuest == null) {
							SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
							perso.set_isTalkingWith(0);
							return true;
						}
						if (success) {
							final Job metierArgs = World.getMetier(41);
							if (metierArgs == null) {
								return true;
							}
							if (perso.getMetierByID(41) != null) {
								SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
								perso.set_isTalkingWith(0);
								perso.setIsOnDialogAction(-1);
								SocketManager.GAME_SEND_Im_PACKET(perso, "111");
								return true;
							}
							for (final java.util.Map.Entry<Integer, JobStat> entry3 : perso.getMetiers().entrySet()) {
								if (entry3.getValue().get_lvl() < 30 && !entry3.getValue().getTemplate().isMaging()) {
									SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
									perso.set_isTalkingWith(0);
									perso.setIsOnDialogAction(-1);
									SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
									return true;
								}
							}
							perso.learnJob(World.getMetier(41));
							Database.getStatique().getPlayerData().update(perso, true);
							SocketManager.GAME_SEND_Ow_PACKET(perso);
						}
						SocketManager.GAME_SEND_QUESTION_PACKET(out, qQuest.parse(perso));
						return false;
					} else {
						perso.send("Im14");
					}
				}
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 991: {
			try {
				final int mapCurId3 = Integer.parseInt(this.args.split(",")[0]);
				final int item3 = Integer.parseInt(this.args.split(",")[1]);
				final int monstre = Integer.parseInt(this.args.split(",")[2]);
				final int grade = Integer.parseInt(this.args.split(",")[3]);
				if (perso.getCurMap().getId() == (short) mapCurId3 && perso.hasItemTemplate(item3, 1)) {
					final String groupe = String.valueOf(monstre) + "," + grade + "," + grade + ";";
					final Monster.MobGroup Mgroupe = new Monster.MobGroup(perso.getCurMap().nextObjectId,
							perso.getCurCell().getId(), groupe);
					perso.getCurMap().startFightVersusMonstres(perso, Mgroupe);
				}
			} catch (Exception e22) {
				e22.printStackTrace();
			}
			break;
		}
		case 992: {
			try {
				final int item6 = Integer.parseInt(this.args.split(",")[0]);
				final int item2 = Integer.parseInt(this.args.split(",")[1]);
				final int mapCurId2 = Integer.parseInt(this.args.split(",")[2]);
				final int mId = Integer.parseInt(this.args.split(",")[3]);
				if (perso.getCurMap().getId() != (short) mapCurId2) {
					break;
				}
				if (perso.hasItemTemplate(item6, 1)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item6);
					perso.removeByTemplateID(item6, 1);
				}
				if (perso.hasItemTemplate(item2, 1)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item2);
					perso.removeByTemplateID(item2, 1);
				}
				final Job metierArgs = World.getMetier(mId);
				if (metierArgs == null) {
					return true;
				}
				if (perso.getMetierByID(mId) != null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				for (final java.util.Map.Entry<Integer, JobStat> entry3 : perso.getMetiers().entrySet()) {
					if (entry3.getValue().get_lvl() < 30 && !entry3.getValue().getTemplate().isMaging()) {
						SocketManager.GAME_SEND_END_DIALOG_PACKET(out);
						perso.set_isTalkingWith(0);
						perso.setIsOnDialogAction(-1);
						SocketManager.GAME_SEND_Im_PACKET(perso, "18;30");
						return true;
					}
				}
				perso.learnJob(World.getMetier(mId));
				Database.getStatique().getPlayerData().update(perso, true);
				SocketManager.GAME_SEND_Ow_PACKET(perso);
				return true;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 993: {
			try {
				final int item6 = Integer.parseInt(this.args.split(",")[0]);
				final int item2 = Integer.parseInt(this.args.split(",")[1]);
				if (perso.hasItemTemplate(item6, 1)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item6);
					perso.removeByTemplateID(item6, 1);
				}
				if (perso.hasItemTemplate(item2, 1)) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~" + item2);
					perso.removeByTemplateID(item2, 1);
				}
				Database.getStatique().getPlayerData().update(perso, true);
				SocketManager.GAME_SEND_Ow_PACKET(perso);
				return true;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 994: {
			try {
				final int mapID2 = Integer.parseInt(this.args.split(",")[0]);
				final int item3 = Integer.parseInt(this.args.split(",")[1]);
				final int metierId3 = Integer.parseInt(this.args.split(",")[2]);
				final Job metierArgs3 = World.getMetier(metierId3);
				if (metierArgs3 == null) {
					return true;
				}
				if (perso.getMetierByID(metierId3) != null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				final Map curMapP = perso.getCurMap();
				if (curMapP.getId() == (short) mapID2 && !perso.hasItemTemplate(item3, 1)) {
					if (perso.getMetierByID(41) != null) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "182");
						return true;
					}
					final ObjectTemplate t4 = World.getObjTemplate(item3);
					if (t4 != null) {
						final org.aestia.object.Object obj22 = t4.createNewItem(1, false);
						obj22.refreshStatsObjet("325#0#0#" + System.currentTimeMillis());
						if (perso.addObjet(obj22, false)) {
							SocketManager.GAME_SEND_Im_PACKET(perso, "021;1~" + obj22.getTemplate().getId());
							World.addObjet(obj22, true);
							Database.getStatique().getPlayerData().update(perso, true);
							SocketManager.GAME_SEND_Ow_PACKET(perso);
							return true;
						}
					}
				}
				return false;
			} catch (Exception e22) {
				e22.printStackTrace();
				break;
			}
		}
		case 995: {
			final Map curMap3 = perso.getCurMap();
			if (perso.isInPrison()) {
				break;
			}
			if (curMap3.getId() == 11866) {
				SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
						new StringBuilder(String.valueOf(perso.getId())).toString(), "6");
				perso.teleport((short) 11862, 253);
				break;
			}
			if (curMap3.getId() == 11862) {
				SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
						new StringBuilder(String.valueOf(perso.getId())).toString(), "6");
				perso.teleport((short) 11866, 344);
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "182");
			return true;
		}
		case 996: {
			final Map curMap2 = perso.getCurMap();
			final ArrayList<Integer> mapSecure = new ArrayList<Integer>();
			String[] split6;
			for (int length5 = (split6 = this.args.split("\\,")).length, n4 = 0; n4 < length5; ++n4) {
				final String k = split6[n4];
				mapSecure.add(Integer.parseInt(k));
			}
			if (!mapSecure.contains((int) curMap2.getId())) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "182");
				return true;
			}
			final long pKamas6 = perso.get_kamas();
			if (pKamas6 < 50L) {
				perso.teleport((short) 11862, 253);
				return true;
			}
			if (!perso.isInPrison()) {
				SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
						new StringBuilder(String.valueOf(perso.getId())).toString(), "6");
				long pNewKamas6 = pKamas6 - 50L;
				if (pNewKamas6 < 0L) {
					pNewKamas6 = 0L;
				}
				perso.set_kamas(pNewKamas6);
				if (perso.isOnline()) {
					SocketManager.GAME_SEND_STATS_PACKET(perso);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "046;50");
				perso.teleport((short) 10256, 211);
				break;
			}
			break;
		}
		case 997: {
			try {
				final int metierID = Integer.parseInt(this.args.split(",")[0]);
				final int mapIdargs = Integer.parseInt(this.args.split(",")[1]);
				final Job metierArgs4 = World.getMetier(metierID);
				if (metierArgs4 == null) {
					return true;
				}
				if (perso.getMetierByID(metierID) != null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "111");
					return true;
				}
				final Map curMapPerso = perso.getCurMap();
				if (curMapPerso.getId() != (short) mapIdargs) {
					return true;
				}
				if (metierArgs4.isMaging()) {
					final JobStat metierBase = perso.getMetierByID(World.getMetierByMaging(metierID));
					if (metierBase == null) {
						return true;
					}
					if (metierBase.get_lvl() < 65) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "111");
						return true;
					}
					if (perso.totalJobFM() > 2) {
						SocketManager.GAME_SEND_Im_PACKET(perso, "19");
						return true;
					}
					perso.learnJob(World.getMetier(metierID));
				}
			} catch (Exception e23) {
				e23.printStackTrace();
				GameServer.addToLog(e23.getMessage());
			}
			break;
		}
		case 998: {
			if (perso.getCurMap().getId() == 10154 && perso.getCurCell().getId() == 142) {
				perso.teleport((short) 8721, 395);
				break;
			}
			SocketManager.GAME_SEND_MESSAGE(perso, "Vous n'\u00eates pas devant le PNJ.");
			break;
		}
		case 999: {
			perso.teleport(this.map, Integer.parseInt(this.args));
			break;
		}
		}
		return true;
	}
}
