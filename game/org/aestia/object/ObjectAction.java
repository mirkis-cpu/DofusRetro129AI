// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.object;

import org.aestia.client.Player;
import org.aestia.common.ConditionParser;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.dynamic.Noel;
import org.aestia.entity.PetEntry;
import org.aestia.entity.Prism;
import org.aestia.game.world.World;
import org.aestia.job.JobStat;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.Map;
import org.aestia.map.MountPark;
import org.aestia.object.entity.Fragment;
import org.aestia.object.entity.SoulStone;
import org.aestia.other.Action;
import org.aestia.other.Animation;
import org.aestia.other.House;

public class ObjectAction {
	private String type;
	private String args;
	private String cond;
	private boolean send;

	public ObjectAction(final String type, final String args, final String cond) {
		this.send = true;
		this.type = type;
		this.args = args;
		this.cond = cond;
	}

	public void apply(final Player player0, final Player target, final int objet, final int cellid) {
		if (player0 == null) {
			return;
		}
		if (!player0.isOnline()) {
			return;
		}
		if (player0.get_fight() != null) {
			return;
		}
		if (player0.getGameClient() == null) {
			return;
		}
		if (!this.cond.equalsIgnoreCase("") && !this.cond.equalsIgnoreCase("-1")
				&& !ConditionParser.validConditions(player0, this.cond)) {
			SocketManager.GAME_SEND_Im_PACKET(player0, "119");
			return;
		}
		if (player0.getLevel() < World.getObjet(objet).getTemplate().getLevel()) {
			SocketManager.GAME_SEND_Im_PACKET(player0, "119");
			return;
		}
		Player player = player0;
		if (target != null) {
			player = target;
		}
		if (World.getObjet(objet) == null) {
			SocketManager.GAME_SEND_MESSAGE(player,
					"Error object null. Merci de pr\u00e9venir un administrateur est d'indiquer le message.");
			return;
		}
		boolean isOk = true;
		int turn = 0;
		String arg = "";
		try {
			String[] split;
			for (int length = (split = this.type.split("\\;")).length, n = 0; n < length; ++n) {
				final String type = split[n];
				if (!this.args.isEmpty()) {
					arg = this.args.split("\\|", 2)[turn];
				}
				switch (Integer.parseInt(type)) {
				case -1: {
					isOk = true;
					this.send = false;
					break;
				}
				case 0: {
					final short mapId = Short.parseShort(arg.split(",", 2)[0]);
					final int cellId = Integer.parseInt(arg.split(",", 2)[1]);
					if (!player.isInPrison() && !player.cantTP()) {
						player.teleport(mapId, cellId);
						break;
					}
					if (player.getCurCell().getId() == 268) {
						player.teleport(mapId, cellId);
						break;
					}
					break;
				}
				case 1: {
					if (!player.isInPrison() && !player.cantTP()) {
						player.warpToSavePos();
						break;
					}
					break;
				}
				case 2: {
					final int count = Integer.parseInt(arg);
					final long curKamas = player.get_kamas();
					long newKamas = curKamas + count;
					if (newKamas < 0L) {
						newKamas = 0L;
					}
					player.set_kamas(newKamas);
					if (player.isOnline()) {
						SocketManager.GAME_SEND_STATS_PACKET(player);
						break;
					}
					break;
				}
				case 3: {
					boolean isOk2 = true;
					boolean isOk3 = true;
					String[] split2;
					for (int length2 = (split2 = arg.split(",")).length, n2 = 0; n2 < length2; ++n2) {
						final String arg2 = split2[n2];
						int statId1;
						int val;
						if (arg.contains(";")) {
							statId1 = Integer.parseInt(arg.split(";")[0]);
							val = World.getObjet(objet).getRandomValue(World.getObjet(objet).parseStatsString(),
									Integer.parseInt(arg.split(";")[0]));
						} else {
							statId1 = Integer.parseInt(arg2);
							val = World.getObjet(objet).getRandomValue(World.getObjet(objet).parseStatsString(),
									Integer.parseInt(arg2));
						}
						switch (statId1) {
						case 110: {
							if (player.getCurPdv() == player.getMaxPdv()) {
								isOk2 = false;
								break;
							}
							if (player.getCurPdv() + val > player.getMaxPdv()) {
								val = player.getMaxPdv() - player.getCurPdv();
							}
							player.setPdv(player.getCurPdv() + val);
							SocketManager.GAME_SEND_STATS_PACKET(player);
							SocketManager.GAME_SEND_Im_PACKET(player, "01;" + val);
							break;
						}
						case 139: {
							if (player.getEnergy() == 10000) {
								isOk3 = false;
								break;
							}
							if (player.getEnergy() + val > 10000) {
								val = 10000 - player.getEnergy();
							}
							player.setEnergy(player.getEnergy() + val);
							SocketManager.GAME_SEND_STATS_PACKET(player);
							SocketManager.GAME_SEND_Im_PACKET(player, "07;" + val);
							break;
						}
						case 605: {
							player.addXp(val);
							SocketManager.GAME_SEND_STATS_PACKET(player);
							SocketManager.GAME_SEND_Im_PACKET(player, "08;" + val);
							break;
						}
						case 614: {
							final JobStat job = player.getMetierByID(Integer.parseInt(arg2.split(";")[1]));
							if (job == null) {
								isOk2 = false;
								isOk3 = false;
								break;
							}
							job.addXp(player, val, true);
							SocketManager.GAME_SEND_Im_PACKET(player,
									"017;" + val + "~" + Integer.parseInt(arg2.split(";")[1]));
							break;
						}
						}
					}
					if (arg.split(",").length == 1) {
						if (!isOk2 || !isOk3) {
							isOk = false;
						} else if (!isOk2 && !isOk3) {
							isOk = false;
						}
					}
					this.send = false;
					break;
				}
				case 4: {
					String[] split3;
					for (int length3 = (split3 = arg.split(",")).length, n3 = 0; n3 < length3; ++n3) {
						final String arg2 = split3[n3];
						final int statId2 = Integer.parseInt(arg2.split(";")[0]);
						final int val2 = Integer.parseInt(arg2.split(";")[1]);
						switch (statId2) {
						case 1: {
							for (int i = 0; i < val2; ++i) {
								player.boostStat(11, false);
								player.getStatsParcho().addOneStat(125, 1);
							}
							break;
						}
						case 2: {
							for (int i = 0; i < val2; ++i) {
								player.getStatsParcho().addOneStat(124, 1);
								player.boostStat(12, false);
							}
							break;
						}
						case 3: {
							for (int i = 0; i < val2; ++i) {
								player.boostStat(10, false);
								player.getStatsParcho().addOneStat(118, 1);
							}
							break;
						}
						case 4: {
							for (int i = 0; i < val2; ++i) {
								player.boostStat(15, false);
								player.getStatsParcho().addOneStat(126, 1);
							}
							break;
						}
						case 5: {
							for (int i = 0; i < val2; ++i) {
								player.boostStat(13, false);
								player.getStatsParcho().addOneStat(123, 1);
							}
							break;
						}
						case 6: {
							for (int i = 0; i < val2; ++i) {
								player.boostStat(14, false);
								player.getStatsParcho().addOneStat(119, 1);
							}
							break;
						}
						case 7: {
							player.set_spellPts(player.get_spellPts() + val2);
							break;
						}
						}
					}
					SocketManager.GAME_SEND_STATS_PACKET(player);
					break;
				}
				case 5: {
					final int id0 = Integer.parseInt(arg);
					final Animation anim = World.getAnimation(id0);
					if (player.get_fight() != null) {
						return;
					}
					player.changeOrientation(1);
					SocketManager.GAME_SEND_GA_PACKET_TO_MAP(player.getCurMap(), "0", 228,
							String.valueOf(player.getId()) + ";" + cellid + "," + Animation.PrepareToGA(anim), "");
					break;
				}
				case 6: {
					final int id0 = Integer.parseInt(arg);
					if (World.getSort(id0) == null) {
						return;
					}
					if (!player.learnSpell(id0, 1, true, true, true)) {
						return;
					}
					this.send = false;
					break;
				}
				case 7: {
					final int id0 = Integer.parseInt(arg);
					final int oldLevel = player.getSortStatBySortIfHas(id0).getLevel();
					if (player.getSortStatBySortIfHas(id0) == null) {
						return;
					}
					if (oldLevel <= 1) {
						return;
					}
					player.unlearnSpell(player, id0, 1, oldLevel, true, true);
					break;
				}
				case 8: {
					isOk = false;
					this.send = false;
					break;
				}
				case 9: {
					final int job2 = Integer.parseInt(arg);
					if (job2 < 1) {
						return;
					}
					final JobStat jobStats = player.getMetierByID(job2);
					if (jobStats == null) {
						return;
					}
					player.unlearnJob(jobStats.getId());
					SocketManager.GAME_SEND_STATS_PACKET(player);
					Database.getStatique().getPlayerData().update(player, false);
					break;
				}
				case 10: {
					final org.aestia.object.Object obj = World.getObjet(objet);
					if (obj == null) {
						return;
					}
					final org.aestia.object.Object pets = player.getObjetByPos(8);
					if (pets == null) {
						return;
					}
					final PetEntry MyPets = World.getPetsEntry(pets.getGuid());
					if (MyPets == null) {
						return;
					}
					if (obj.getTemplate().getConditions()
							.contains(new StringBuilder(String.valueOf(pets.getTemplate().getId())).toString())) {
						MyPets.giveEpo(player);
						break;
					}
					break;
				}
				case 11: {
					if (player.getSexe() == 0) {
						player.setSexe(1);
					} else {
						player.setSexe(0);
					}
					SocketManager.GAME_SEND_ALTER_GM_PACKET(player.getCurMap(), player);
					break;
				}
				case 12: {
					isOk = false;
					this.send = false;
					break;
				}
				case 13: {
					final int emote = Integer.parseInt(arg);
					if (player.getStaticEmote().containsKey(emote)) {
						SocketManager.GAME_SEND_MESSAGE(player, "Tu connais d\u00e9j\u00e0 cet aptitude !");
						return;
					}
					player.addStaticEmote(emote);
					break;
				}
				case 14: {
					final int job2 = Integer.parseInt(arg);
					if (World.getMetier(job2) == null) {
						return;
					}
					if (player.getMetierByID(job2) != null) {
						SocketManager.GAME_SEND_Im_PACKET(player, "111");
						return;
					}
					if ((player.getMetierByID(2) != null && player.getMetierByID(2).get_lvl() < 30)
							|| (player.getMetierByID(11) != null && player.getMetierByID(11).get_lvl() < 30)
							|| (player.getMetierByID(13) != null && player.getMetierByID(13).get_lvl() < 30)
							|| (player.getMetierByID(14) != null && player.getMetierByID(14).get_lvl() < 30)
							|| (player.getMetierByID(15) != null && player.getMetierByID(15).get_lvl() < 30)
							|| (player.getMetierByID(16) != null && player.getMetierByID(16).get_lvl() < 30)
							|| (player.getMetierByID(17) != null && player.getMetierByID(17).get_lvl() < 30)
							|| (player.getMetierByID(18) != null && player.getMetierByID(18).get_lvl() < 30)
							|| (player.getMetierByID(19) != null && player.getMetierByID(19).get_lvl() < 30)
							|| (player.getMetierByID(20) != null && player.getMetierByID(20).get_lvl() < 30)
							|| (player.getMetierByID(24) != null && player.getMetierByID(24).get_lvl() < 30)
							|| (player.getMetierByID(25) != null && player.getMetierByID(25).get_lvl() < 30)
							|| (player.getMetierByID(26) != null && player.getMetierByID(26).get_lvl() < 30)
							|| (player.getMetierByID(27) != null && player.getMetierByID(27).get_lvl() < 30)
							|| (player.getMetierByID(28) != null && player.getMetierByID(28).get_lvl() < 30)
							|| (player.getMetierByID(31) != null && player.getMetierByID(31).get_lvl() < 30)
							|| (player.getMetierByID(36) != null && player.getMetierByID(36).get_lvl() < 30)
							|| (player.getMetierByID(41) != null && player.getMetierByID(41).get_lvl() < 30)
							|| (player.getMetierByID(56) != null && player.getMetierByID(56).get_lvl() < 30)
							|| (player.getMetierByID(58) != null && player.getMetierByID(58).get_lvl() < 30)
							|| (player.getMetierByID(60) != null && player.getMetierByID(60).get_lvl() < 30)
							|| (player.getMetierByID(65) != null && player.getMetierByID(65).get_lvl() < 30)) {
						SocketManager.GAME_SEND_Im_PACKET(player, "18;30");
						return;
					}
					if (player.totalJobBasic() > 2) {
						SocketManager.GAME_SEND_Im_PACKET(player, "19");
						return;
					}
					if (job2 != 27) {
						player.learnJob(World.getMetier(job2));
						break;
					}
					if (!player.hasItemTemplate(966, 1)) {
						return;
					}
					SocketManager.GAME_SEND_Im_PACKET(player, "022;966~1");
					player.learnJob(World.getMetier(job2));
					break;
				}
				case 15: {
					for (final House j : World.getHouses().values()) {
						if (j.getOwnerId() == player.getId()) {
							player.teleport((short) j.getHouseMapId(), j.getHouseCellId());
							break;
						}
					}
					break;
				}
				case 16: {
					player.setMascotte(Integer.parseInt(this.args));
					break;
				}
				case 17: {
					player.setBenediction(World.getObjet(objet).getTemplate().getId());
					break;
				}
				case 18: {
					player.setMalediction(World.getObjet(objet).getTemplate().getId());
					break;
				}
				case 19: {
					player.setRoleplayBuff(World.getObjet(objet).getTemplate().getId());
					break;
				}
				case 20: {
					player.setCandy(World.getObjet(objet).getTemplate().getId());
					break;
				}
				case 21: {
					final Map map0 = player.getCurMap();
					final int id0 = World.getObjet(objet).getTemplate().getId();
					final int resist = World.getObjet(objet).getResistance(World.getObjet(objet).parseStatsString());
					final int resistMax = World.getObjet(objet)
							.getResistanceMax(World.getObjet(objet).getTemplate().getStrTemplate());
					if (map0.getMountPark() == null) {
						return;
					}
					final MountPark MP = map0.getMountPark();
					if (player.get_guild() == null) {
						SocketManager.GAME_SEND_BN(player);
						return;
					}
					if (!player.getGuildMember().canDo(Constant.G_AMENCLOS)) {
						SocketManager.GAME_SEND_Im_PACKET(player, "193");
						return;
					}
					if (MP.getCellOfObject().size() == 0 || !MP.getCellOfObject().contains(cellid)) {
						SocketManager.GAME_SEND_BN(player);
						return;
					}
					if (MP.getObject().size() < MP.getMaxObject()) {
						MP.addObject(cellid, id0, player.getId(), resistMax, resist);
						SocketManager.SEND_GDO_PUT_OBJECT_MOUNT(map0,
								String.valueOf(cellid) + ";" + id0 + ";1;" + resist + ";" + resistMax);
						break;
					}
					SocketManager.GAME_SEND_Im_PACKET(player, "1107");
					return;
				}
				case 22: {
					final Map map0 = player.getCurMap();
					final int cellId2 = player.getCurCell().getId();
					final World.SubArea subArea = map0.getSubArea();
					final World.Area area = subArea.getArea();
					final int alignement = player.get_align();
					if (cellId2 <= 0) {
						return;
					}
					if (alignement == 0 || alignement == 3) {
						SocketManager.GAME_SEND_MESSAGE(player,
								"Vous ne possedez pas l'alignement n\u00e9cessaire pour poser un prisme.");
						return;
					}
					if (!player.is_showWings()) {
						SocketManager.GAME_SEND_MESSAGE(player,
								"Vos ailes doivent \u00eatre activ\u00e9 afin de poser un prisme.");
						return;
					}
					if (map0.noPrism) {
						SocketManager.GAME_SEND_MESSAGE(player, "Vous ne pouvez pas poser un prisme sur cette map.");
						return;
					}
					if (subArea.getAlignement() != 0 || !subArea.getConquistable()) {
						SocketManager.GAME_SEND_MESSAGE(player,
								"L'alignement de cette sous-zone est en conqu\u00e8te ou n'est pas neutre !");
						return;
					}
					final Prism Prisme = new Prism(World.getNextIDPrisme(), alignement, 1, map0.getId(), cellId2,
							player.get_honor(), -1);
					subArea.setAlignement(alignement);
					subArea.setPrismId(Prisme.getId());
					for (final Player z : World.getOnlinePersos()) {
						if (z == null) {
							continue;
						}
						if (z.get_align() == 0) {
							SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z,
									String.valueOf(subArea.getId()) + "|" + alignement + "|1");
							if (area.getalignement() != 0) {
								continue;
							}
							SocketManager.GAME_SEND_aM_ALIGN_PACKET_TO_AREA(z,
									String.valueOf(area.get_id()) + "|" + alignement);
						} else {
							SocketManager.GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(z,
									String.valueOf(subArea.getId()) + "|" + alignement + "|0");
							if (area.getalignement() != 0) {
								continue;
							}
							SocketManager.GAME_SEND_aM_ALIGN_PACKET_TO_AREA(z,
									String.valueOf(area.get_id()) + "|" + alignement);
						}
					}
					if (area.getalignement() == 0) {
						area.setPrismeID(Prisme.getId());
						area.setalignement(alignement);
						Prisme.setConquestArea(area.get_id());
					}
					World.addPrisme(Prisme);
					Database.getGame().getPrismeData().add(Prisme);
					player.getCurMap().getSubArea().setAlignement(player.get_align());
					Database.getGame().getSubarea_dataData().update(player.getCurMap().getSubArea());
					SocketManager.GAME_SEND_PRISME_TO_MAP(map0, Prisme);
					break;
				}
				case 23: {
					int dist = 99999;
					int alea = 0;
					short mapId = 0;
					int cellId = 0;
					for (final Prism k : World.AllPrisme()) {
						if (k.getAlignement() != player.get_align()) {
							continue;
						}
						alea = (World.getMap(k.getMap()).getX() - player.getCurMap().getX())
								* (World.getMap(k.getMap()).getX() - player.getCurMap().getX())
								+ (World.getMap(k.getMap()).getY() - player.getCurMap().getY())
										* (World.getMap(k.getMap()).getY() - player.getCurMap().getY());
						if (alea >= dist) {
							continue;
						}
						dist = alea;
						mapId = k.getMap();
						cellId = k.getCell();
					}
					if (mapId != 0) {
						player.teleport(mapId, cellId);
						break;
					}
					break;
				}
				case 24: {
					final short mapId = (short) Integer.parseInt(arg.split(",")[0]);
					final int cellId = Integer.parseInt(arg.split(",")[1]);
					if (World.getMap(mapId).getSubArea().getAlignement() == player.get_align()) {
						player.teleport(mapId, cellId);
						break;
					}
					break;
				}
				case 25: {
					final boolean inArena = arg.split(";")[0].equals("true");
					String groupData = "";
					if (inArena && !Config.contains(Config.arenaMap, player.getCurMap().getId())) {
						return;
					}
					if (arg.split(";")[1].equals("1")) {
						groupData = arg.split(";")[2];
					} else {
						final SoulStone pierrePleine = (SoulStone) World.getObjet(objet);
						groupData = pierrePleine.parseGroupData();
					}
					final String condition = "MiS = " + player.getId();
					player.getCurMap().spawnNewGroup(true, player.getCurCell().getId(), groupData, condition);
					break;
				}
				case 26: {
					String[] split4;
					for (int length4 = (split4 = arg.split(";")).length, n4 = 0; n4 < length4; ++n4) {
						final String l = split4[n4];
						final org.aestia.object.Object obj = World.getObjTemplate(Integer.parseInt(l.split(",")[0]))
								.createNewItem(Integer.parseInt(l.split(",")[1]), false);
						if (player.addObjet(obj, true)) {
							World.addObjet(obj, true);
						}
					}
					SocketManager.GAME_SEND_Ow_PACKET(player);
					break;
				}
				case 27: {
					player.setAllTitle(arg);
					break;
				}
				case 28: {
					player.verifAndAddZaap((short) Integer.parseInt(arg));
					break;
				}
				case 29: {
					player.setisForgetingSpell(true);
					SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', player);
					break;
				}
				case 30: {
					SocketManager.GAME_SEND_MESSAGE(player,
							"Vous vous \u00eates fait enculer. Cordialement, le staff.");
					break;
				}
				case 31: {
					new Action(511, "", "", null).apply(player, null, objet, -1);
					break;
				}
				case 32: {
					final String traque = World.getObjet(objet).getTraquedName();
					if (traque == null) {
						break;
					}
					final Player cible = World.getPersoByName(traque);
					if (cible == null) {
						break;
					}
					if (!cible.isOnline()) {
						SocketManager.GAME_SEND_Im_PACKET(player, "1198");
						break;
					}
					SocketManager.GAME_SEND_FLAG_PACKET(player, cible);
					break;
				}
				case 33: {
					player.getAccount().setPoints(player.getAccount().getPoints() + Integer.parseInt(arg));
					break;
				}
				default: {
					if (Main.modDebug) {
						Console.println("Action id " + type + " non implante dans le systeme.",
								Console.Color.INFORMATION);
						break;
					}
					break;
				}
				}
				++turn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean effect = this.haveEffect(World.getObjet(objet).getTemplate().getId(), World.getObjet(objet), player);
		if (effect) {
			isOk = true;
		}
		if (isOk) {
			effect = true;
		}
		if (this.type.split("\\;").length > 1) {
			isOk = true;
		}
		if (objet != -1) {
			if (this.send) {
				SocketManager.GAME_SEND_Im_PACKET(player, "022;1~" + World.getObjet(objet).getTemplate().getId());
			}
			if (isOk && effect && World.getObjet(objet).getTemplate().getId() != 7799
					&& World.getObjet(objet) != null) {
				player0.removeItem(objet, 1, true, true);
			}
		}
	}

	private boolean haveEffect(final int id, final org.aestia.object.Object object, final Player player) {
		switch (id) {
		case 8378: {
			for (final World.Couple<Integer, Integer> couple : ((Fragment) object).getRunes()) {
				final ObjectTemplate objectTemplate = World.getObjTemplate(couple.first);
				if (objectTemplate == null) {
					continue;
				}
				final org.aestia.object.Object newObject = objectTemplate.createNewItem(couple.second, true);
				if (newObject == null) {
					continue;
				}
				if (player.addObjetSimiler(newObject, true, -1)) {
					continue;
				}
				World.addObjet(newObject, true);
				player.addObjet(newObject);
			}
			return this.send = true;
		}
		case 7799: {
			player.toogleOnMount();
			return this.send = false;
		}
		case 10832: {
			player.getCurMap().spawnNewGroup(true, player.getCurCell().getId(), "483,1,1000", "MiS=" + player.getId());
			return true;
		}
		case 10664: {
			player.getCurMap().spawnNewGroup(true, player.getCurCell().getId(), "47,1,1000", "MiS=" + player.getId());
			return true;
		}
		case 10665: {
			player.setCandy(10688);
			return true;
		}
		case 10670: {
			player.setBenediction(10682);
			return true;
		}
		case 8435: {
			SocketManager.sendPacketToMap(player.getCurMap(),
					"GA;208;" + player.getId() + ";" + player.getCurCell().getId() + ",2906,11,8,1");
			return true;
		}
		case 8624: {
			SocketManager.sendPacketToMap(player.getCurMap(),
					"GA;208;" + player.getId() + ";" + player.getCurCell().getId() + ",2907,11,8,1");
			return true;
		}
		case 8625: {
			SocketManager.sendPacketToMap(player.getCurMap(),
					"GA;208;" + player.getId() + ";" + player.getCurCell().getId() + ",2908,11,8,1");
			return true;
		}
		case 8430: {
			SocketManager.sendPacketToMap(player.getCurMap(),
					"GA;208;" + player.getId() + ";" + player.getCurCell().getId() + ",2909,11,8,1");
			return true;
		}
		case 8621: {
			player.set_gfxID(1109);
			SocketManager.GAME_SEND_ALTER_GM_PACKET(player.getCurMap(), player);
			return true;
		}
		case 8626: {
			player.set_gfxID(1046);
			SocketManager.GAME_SEND_ALTER_GM_PACKET(player.getCurMap(), player);
			return true;
		}
		case 10833: {
			player.set_gfxID(9001);
			SocketManager.GAME_SEND_ALTER_GM_PACKET(player.getCurMap(), player);
			return true;
		}
		case 10839: {
			player.getCurMap().spawnNewGroup(true, player.getCurCell().getId(), "2787,1,1000", "MiS=" + player.getId());
			return true;
		}
		case 8335: {
			Noel.getRandomObjectOne(player);
			return true;
		}
		case 8336: {
			Noel.getRandomObjectTwo(player);
			return true;
		}
		case 8337: {
			Noel.getRandomObjectTree(player);
			return true;
		}
		case 8339: {
			Noel.getRandomObjectFour(player);
			return true;
		}
		case 8340: {
			Noel.getRandomObjectFive(player);
			return true;
		}
		case 10912: {
			return false;
		}
		case 10913: {
			return false;
		}
		case 10914: {
			return false;
		}
		default: {
			return false;
		}
		}
	}
}
