// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.npc;

import java.util.Map;

import org.aestia.client.Player;
import org.aestia.common.ConditionParser;
import org.aestia.game.world.World;
import org.aestia.job.JobStat;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.other.Action;
import org.aestia.other.Dopeul;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

public class NpcQuestion {
	private int id;
	private String answers;
	private String args;
	private String condition;
	private String falseQuestion;

	public NpcQuestion(final int id, final String answers, final String args, final String condition,
			final String falseQuestion) {
		this.id = id;
		this.answers = answers;
		this.args = args;
		this.condition = condition;
		this.falseQuestion = falseQuestion;
	}

	public int getId() {
		return this.id;
	}

	public String getAnwsers() {
		return this.answers;
	}

	public String getArgs() {
		return this.args;
	}

	public void setArgs(final String args) {
		this.args = args;
	}

	public String conditionsReponse(final Player player) {
		String _str = "";
		try {
			final String[] split = this.answers.split(";");
			boolean first = true;
			if (split != null && split.length > 0) {
				String[] array;
				for (int length = (array = split).length, i = 0; i < length; ++i) {
					final String loc1 = array[i];
					if (!loc1.equalsIgnoreCase("")) {
						final Integer ans = Integer.parseInt(loc1);
						final NpcAnswer answer = World.getNpcAnswer().get(ans);
						if (answer != null) {
							boolean ok = true;
							for (final Action action : answer.getActions()) {
								switch (action.getId()) {
								case 15: {
									final String args = action.getArgs();
									final int clef = Integer.parseInt(args.split(",")[2]);
									if (!player.hasItemTemplate(clef, 1)) {
										ok = false;
										continue;
									}
									continue;
								}
								case 16: {
									final String args = action.getArgs();
									final int clef = Integer.parseInt(args.split(",")[2]);
									if (!player.hasItemTemplate(clef, 1)) {
										ok = false;
										continue;
									}
									continue;
								}
								case 6: {
									final int mId = Integer.parseInt(action.getArgs().split(",")[0]);
									final int cId = Integer.parseInt(action.getArgs().split(",")[1]);
									if (player.getCurMap().getId() != (short) cId) {
										ok = false;
										continue;
									}
									if (player.totalJobBasic() > 2) {
										ok = false;
										continue;
									}
									if (player.getMetierByID(mId) != null) {
										ok = false;
										continue;
									}
									continue;
								}
								case 40: {
									if (!player.getQuestPerso().isEmpty()) {
										for (final Quest.Quest_Perso QP : player.getQuestPerso().values()) {
											if (QP.getQuest().getId() == Integer.parseInt(action.getArgs())) {
												ok = false;
											}
										}
										continue;
									}
									continue;
								}
								case 997: {
									final int mId2 = Integer.parseInt(action.getArgs().split(",")[0]);
									final int cId2 = Integer.parseInt(action.getArgs().split(",")[1]);
									if (player.getCurMap().getId() != (short) cId2) {
										ok = false;
										continue;
									}
									if (player.getMetierByID(mId2) != null) {
										ok = false;
										continue;
									}
									if (player.totalJobFM() > 2) {
										ok = false;
										continue;
									}
									if (!World.getMetier(mId2).isMaging()) {
										continue;
									}
									final JobStat metier = player.getMetierByID(World.getMetierByMaging(mId2));
									if (metier == null) {
										ok = false;
										continue;
									}
									if (metier.get_lvl() < 65) {
										ok = false;
										continue;
									}
									continue;
								}
								default: {
									continue;
								}
								}
							}
							if (!player.getQuestPerso().isEmpty() && ok) {
								for (final Quest.Quest_Perso QP2 : player.getQuestPerso().values()) {
									if (!QP2.isFinish()) {
										if (QP2.getQuest() == null) {
											continue;
										}
										for (final Quest_Etape q : QP2.getQuest().getQuestEtapeList()) {
											if (q == null) {
												continue;
											}
											if (QP2.isQuestEtapeIsValidate(q)) {
												continue;
											}
											if (q.getValidationType() != ans) {
												continue;
											}
											switch (q.getType()) {
											case 3: {
												for (final Map.Entry<Integer, Integer> _entry : q.getItemNecessaryList()
														.entrySet()) {
													if (!player.hasItemTemplate(_entry.getKey(), _entry.getValue())) {
														ok = false;
													}
												}
											}
											default: {
												continue;
											}
											}
										}
									}
								}
							}
							if (ok) {
								final String[][] s = Constant.HUNTING_QUESTS;
								for (int v = 0; v < s.length; ++v) {
									if (Integer.parseInt(s[v][6]) == answer.getId()) {
										for (final Quest.Quest_Perso QP3 : player.getQuestPerso().values()) {
											boolean k = true;
											if (QP3.getQuest().getId() == Integer.parseInt(s[v][5])) {
												k = false;
												final org.aestia.object.Object suiveur = player.getObjetByPos(24);
												if (suiveur != null) {
													ok = (suiveur.getTemplate().getId() == Integer.parseInt(s[v][4]));
													break;
												}
												ok = false;
											}
											if (k) {
												ok = false;
											}
										}
									}
								}
							}
							if (ok) {
								final org.aestia.map.Map mapActuel = player.getCurMap();
								Integer IDmob = null;
								int certificat = -1;
								final Map<Integer, World.Couple<Integer, Integer>> dopeuls = Action.getDopeul();
								switch (answer.getId()) {
								case 4643: {
									if (player.getALvl() > 10) {
										ok = false;
										break;
									}
									break;
								}
								case 4644: {
									if (player.getALvl() <= 10 || player.getALvl() > 20) {
										ok = false;
										break;
									}
									break;
								}
								case 4645: {
									if (player.getALvl() <= 20 || player.getALvl() > 30) {
										ok = false;
										break;
									}
									break;
								}
								case 4646: {
									if (player.getALvl() <= 30 || player.getALvl() > 40) {
										ok = false;
										break;
									}
									break;
								}
								case 4647: {
									if (player.getALvl() <= 40 || player.getALvl() > 50) {
										ok = false;
										break;
									}
									break;
								}
								case 4648: {
									if (player.getALvl() <= 50 || player.getALvl() > 60) {
										ok = false;
										break;
									}
									break;
								}
								case 4649: {
									if (player.getALvl() <= 60 || player.getALvl() > 70) {
										ok = false;
										break;
									}
									break;
								}
								case 4650: {
									if (player.getALvl() <= 70 || player.getALvl() > 80) {
										ok = false;
										break;
									}
									break;
								}
								case 4651: {
									if (player.getALvl() <= 80 || player.getALvl() > 90) {
										ok = false;
										break;
									}
									break;
								}
								case 4652: {
									if (player.getALvl() <= 90) {
										ok = false;
										break;
									}
									break;
								}
								case 4639: {
									if (player.get_align() != 2) {
										ok = false;
										break;
									}
									break;
								}
								case 4637: {
									if (player.get_align() != 2) {
										ok = false;
										break;
									}
									break;
								}
								case 4641: {
									if (player.get_align() != 1) {
										ok = false;
										break;
									}
									break;
								}
								case 4638: {
									if (player.get_align() != 1) {
										ok = false;
										break;
									}
									break;
								}
								case 4653: {
									if (!player.hasItemTemplate(9811, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 4654: {
									if (!player.hasItemTemplate(9812, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 4655: {
									if (!player.hasItemTemplate(9811, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 4656: {
									if (!player.hasItemTemplate(9812, 1) || player.get_align() != 2) {
										ok = false;
										break;
									}
									break;
								}
								case 4657: {
									if (!player.hasItemTemplate(9812, 1) || player.get_align() != 1) {
										ok = false;
										break;
									}
									break;
								}
								case 7453: {
									if (!player.hasItemTemplate(10563, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 2769: {
									if (!player.hasItemTemplate(8077, 10) || !player.hasItemTemplate(8076, 10)
											|| !player.hasItemTemplate(8075, 10) || !player.hasItemTemplate(8064, 10)) {
										ok = false;
										break;
									}
									break;
								}
								case 2754: {
									if (player.getCurMap().getId() != 9717) {
										ok = false;
										break;
									}
									if (player.hasSpell(414)) {
										ok = false;
										break;
									}
									if (!player.hasItemTemplate(7904, 50) || !player.hasItemTemplate(7903, 50)) {
										ok = false;
										break;
									}
									break;
								}
								case 2962: {
									if (player.getCurMap().getId() != 10199) {
										ok = false;
										break;
									}
									break;
								}
								case 2963: {
									if (player.getCurMap().getId() != 10213) {
										ok = false;
										break;
									}
									break;
								}
								case 3355: {
									final Quest q2 = Quest.getQuestById(198);
									if (q2 != null && player.getQuestPersoByQuest(q2) != null) {
										ok = false;
										break;
									}
									break;
								}
								case 528: {
									if (player.hasItemTemplate(1469, 1)) {
										ok = false;
										break;
									}
									if (player.getMetierByID(26) != null) {
										ok = false;
										break;
									}
									break;
								}
								case 530: {
									if (!player.hasItemTemplate(1469, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 531: {
									if (!player.hasItemTemplate(1470, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 532: {
									if (!player.hasItemTemplate(1471, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 534: {
									if (!player.hasItemTemplate(1472, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 2047: {
									boolean metier2 = true;
									for (final Map.Entry<Integer, JobStat> entry : player.getMetiers().entrySet()) {
										if (entry.getValue().get_lvl() < 30) {
											metier2 = false;
										}
									}
									if (player.hasItemTemplate(2107, 1)) {
										ok = false;
										break;
									}
									if (!player.hasItemTemplate(2106, 1)) {
										ok = false;
										break;
									}
									if (player.getMetierByID(36) != null) {
										ok = false;
										break;
									}
									if (!metier2) {
										ok = false;
										break;
									}
									break;
								}
								case 2037: {
									if (player.hasItemTemplate(2106, 1)) {
										ok = false;
										break;
									}
									if (!player.hasItemTemplate(2107, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 2013: {
									if (player.hasItemTemplate(2106, 1)) {
										ok = false;
										break;
									}
									if (player.hasItemTemplate(2107, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 1968: {
									if (!player.hasItemTemplate(2039, 1)) {
										ok = false;
										break;
									}
									if (!player.hasItemTemplate(2041, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 1962: {
									if (player.hasItemTemplate(2039, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 1967: {
									if (!player.hasItemTemplate(2039, 1) || player.hasItemTemplate(2041, 1)) {
										ok = false;
										break;
									}
									break;
								}
								case 1509: {
									if (!dopeuls.containsKey((int) mapActuel.getId())) {
										break;
									}
									IDmob = dopeuls.get((int) mapActuel.getId()).first;
									certificat = Constant.getCertificatByDopeuls(IDmob);
									if (certificat == -1) {
										break;
									}
									if (!player.hasItemTemplate(certificat, 1)) {
										break;
									}
									final String date = player.getItemTemplate(certificat, 1).getTxtStat().get(805);
									final long timeStamp = Long.parseLong(date.split("#")[3]);
									if (System.currentTimeMillis() - timeStamp <= 86400000L) {
										ok = false;
										break;
									}
									break;
								}
								case 1419: {
									if (!dopeuls.containsKey((int) mapActuel.getId())) {
										break;
									}
									IDmob = dopeuls.get((int) mapActuel.getId()).first;
									certificat = Constant.getCertificatByDopeuls(IDmob);
									if (certificat == -1) {
										break;
									}
									if (!player.hasItemTemplate(certificat, 1)) {
										break;
									}
									final String date = player.getItemTemplate(certificat, 1).getTxtStat().get(805);
									long timeStamp = System.currentTimeMillis();
									try {
										timeStamp = Long.parseLong(date.split("#")[3]);
									} catch (Exception e2) {
										Console.println(
												"!A REPPORTER! Erreur de date avec l'item : "
														+ player.getItemTemplate(certificat, 1).getGuid() + ".",
												Console.Color.ERROR);
										return "";
									}
									if (System.currentTimeMillis() - timeStamp <= 86400000L) {
										ok = false;
										break;
									}
									break;
								}
								case 6772: {
									if (!player.getQuestPerso().isEmpty()) {
										for (final Quest.Quest_Perso QP : player.getQuestPerso().values()) {
											if (QP.getQuest().getId() == 470) {
												ok = false;
											}
										}
										break;
									}
									break;
								}
								case 3627: {
									if (!player.getQuestPerso().isEmpty()) {
										for (final Quest.Quest_Perso QP : player.getQuestPerso().values()) {
											if (QP.getQuest().getId() == 232) {
												ok = false;
											}
										}
										ok = !ok;
										break;
									}
									ok = false;
									break;
								}
								case 6701: {
									if (player.hasItemTemplate(10207, 1)) {
										ok = false;
										break;
									}
									if (Dopeul.hasOneDoplon(player) == -1) {
										ok = false;
										break;
									}
									break;
								}
								case 6699: {
									final org.aestia.map.Map curMap = player.getCurMap();
									final int idMap = World.getTempleByClasse(player.getClasse());
									ok = (curMap.getId() == (short) idMap
											&& player.hasItemTemplate(Dopeul.getDoplonByClasse(player.getClasse()), 1)
											&& !player.hasSpell(Constant.getSpecialSpellByClasse(player.getClasse())));
									break;
								}
								case 7326: {
									final org.aestia.map.Map curMap2 = player.getCurMap();
									final int idMap2 = World.getTempleByClasse(player.getClasse());
									if (curMap2.getId() != (short) idMap2) {
										ok = false;
										break;
									}
									if (!player.hasItemTemplate(Dopeul.getDoplonByClasse(player.getClasse()), 1)) {
										ok = false;
									}
									if (player.hasItemTemplate(10601, 1)) {
										ok = false;
										break;
									}
									break;
								}
								}
							}
							if (ok) {
								if (!first) {
									_str = String.valueOf(_str) + ";";
								}
								_str = String.valueOf(_str) + answer.getId();
								first = false;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _str;
	}

	public String parse(final Player player) {
		if (this.condition.equals("") || ConditionParser.validConditions(player, this.condition)) {
			String str = String.valueOf(this.id);
			if (!this.args.equals("")) {
				str = String.valueOf(str) + ";" + this.parseArgs(this.args, player);
			}
			if (!this.answers.equals("")) {
				final String arg = this.conditionsReponse(player);
				if (!arg.isEmpty()) {
					str = String.valueOf(str) + "|" + arg;
				}
			}
			if (player.getItemTemplate(10207) != null) {
				String[] split;
				for (int length = (split = player.getItemTemplate(10207).getTxtStat().values().toString()
						.split("\\,")).length, j = 0; j < length; ++j) {
					final String i = split[j];
					final org.aestia.map.Map map = player.getCurMap();
					if (map != null) {
						final Npc npc = map.getNpc(player.get_isTalkingWith());
						if (npc != null) {
							final NpcTemplate npcT = npc.getTemplate();
							if (npcT != null) {
								if (Dopeul.parseConditionTrousseau(i.replace(" ", ""), npcT.get_id(), map.getId())) {
									if (!str.contains("|")) {
										break;
									}
									final String _reponses = str.split("\\|")[1];
									if (_reponses.contains(";")) {
										final String[] reponses = _reponses.split(";");
										String[] array;
										for (int length2 = (array = reponses).length, k = 0; k < length2; ++k) {
											final String rep = array[k];
											final NpcAnswer ans = World.getNpcAnswer(Integer.parseInt(rep));
											if (ans != null) {
												boolean entreeDonjon = false;
												for (final Action a : ans.getActions()) {
													if (a.getId() == 15) {
														entreeDonjon = true;
														break;
													}
												}
												if (entreeDonjon) {
													str = String.valueOf(str)
															+ (this.answers.equals("") ? "|6605" : ";6605");
													break;
												}
											}
										}
									} else {
										final NpcAnswer ans2 = World.getNpcAnswer(Integer.parseInt(_reponses));
										if (ans2 != null) {
											boolean entreeDonjon2 = false;
											for (final Action a2 : ans2.getActions()) {
												if (a2.getId() == 15) {
													entreeDonjon2 = true;
													break;
												}
											}
											if (entreeDonjon2) {
												str = String.valueOf(str)
														+ (this.answers.equals("") ? "|6605" : ";6605");
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return str;
		}
		if (this.falseQuestion.contains("|")) {
			return World.getNPCQuestion(Integer.parseInt(this.falseQuestion.split("|")[0])).parse(player);
		}
		return World.getNPCQuestion(Integer.parseInt(this.falseQuestion)).parse(player);
	}

	private String parseArgs(final String args, final Player player) {
		String arg = args.replace("[name]", player.getStringVar("name"));
		arg = arg.replace("[bankCost]", player.getStringVar("bankCost"));
		arg = arg.replace("[points]", player.getStringVar("points"));
		arg = arg.replace("[pointsVote]", player.getStringVar("pointsVote"));
		arg = arg.replace("[nbrOnline]", player.getStringVar("nbrOnline"));
		arg = arg.replace("[align]", player.getStringVar("align"));
		return arg;
	}
}
