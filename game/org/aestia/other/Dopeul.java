// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.world.World;
import org.aestia.kernel.Constant;
import org.aestia.object.ObjectTemplate;

public class Dopeul {
	private static Map<Integer, World.Couple<Integer, Integer>> donjons;

	static {
		Dopeul.donjons = new TreeMap<Integer, World.Couple<Integer, Integer>>();
	}

	public static Map<Integer, World.Couple<Integer, Integer>> getDonjons() {
		return Dopeul.donjons;
	}

	public static void getReward(final Player perso, final int type) {
		final org.aestia.map.Map curMap = perso.getCurMap();
		final int idMap = World.getTempleByClasse(perso.getClasse());
		switch (type) {
		case 1: {
			if (!perso.hasItemTemplate(getDoplonByClasse(perso.getClasse()), 1)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "14");
				return;
			}
			if (curMap.getId() != (short) idMap) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu n'es pas dans ton temple de classe !");
				return;
			}
			if (perso.hasSpell(Constant.getSpecialSpellByClasse(perso.getClasse()))) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu as d\u00e9j\u00e0 appris le sort !");
				return;
			}
			perso.learnSpell(Constant.getSpecialSpellByClasse(perso.getClasse()), 1, true, true, true);
			removeObject(perso, getDoplonByClasse(perso.getClasse()), 1);
			break;
		}
		case 2: {
			if (perso.hasItemTemplate(10207, 1)) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu poss\u00e8de d\u00e9j\u00e0 un Trousseau de clef !");
				return;
			}
			final int doplon = hasOneDoplon(perso);
			if (doplon == -1) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "14");
				return;
			}
			final org.aestia.object.Object obj = World.getObjTemplate(10207).createNewItem(1, true);
			if (perso.addObjet(obj, false)) {
				World.addObjet(obj, true);
			}
			removeObject(perso, doplon, 1);
			break;
		}
		case 3: {
			ArrayList<Integer> doplons = new ArrayList<Integer>();
			final int doplon = hasQuaDoplon(perso, 7);
			if (doplon == -1) {
				if (hasAllDoplon(perso) == null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "14");
					return;
				}
				doplons = hasAllDoplon(perso);
				for (final int id : doplons) {
					removeObject(perso, id, 1);
				}
			} else {
				removeObject(perso, doplon, 7);
			}
			perso.setisForgetingSpell(true);
			SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', perso);
			break;
		}
		case 4: {
			if (!perso.hasItemTemplate(getDoplonByClasse(perso.getClasse()), 1)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "14");
				return;
			}
			if (curMap.getId() != (short) idMap) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu n'es pas dans ton temple de classe !");
				return;
			}
			if (perso.hasItemTemplate(10601, 1)) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Tu ne peux pas te reconstituer plusieurs fois !");
				return;
			}
			perso.getStats().addOneStat(125, -perso.getStats().getEffect(125));
			perso.getStats().addOneStat(124, -perso.getStats().getEffect(124));
			perso.getStats().addOneStat(118, -perso.getStats().getEffect(118));
			perso.getStats().addOneStat(123, -perso.getStats().getEffect(123));
			perso.getStats().addOneStat(119, -perso.getStats().getEffect(119));
			perso.getStats().addOneStat(126, -perso.getStats().getEffect(126));
			perso.addCapital((perso.getLevel() - 1) * 5 - perso.get_capital());
			final ObjectTemplate OT = World.getObjTemplate(10601);
			final org.aestia.object.Object obj2 = OT.createNewItem(1, false);
			if (perso.addObjet(obj2, true)) {
				World.addObjet(obj2, true);
			}
			obj2.refreshStatsObjet("325" + System.currentTimeMillis());
			Database.getStatique().getItemData().save(obj2, false);
			Database.getStatique().getPlayerData().update(perso, true);
			SocketManager.GAME_SEND_STATS_PACKET(perso);
			removeObject(perso, getDoplonByClasse(perso.getClasse()), 1);
			break;
		}
		case 5: {
			final ArrayList<Integer> doplons = hasAllDoplon(perso);
			if (doplons == null) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "14");
				return;
			}
			final org.aestia.object.Object obj = World.getObjTemplate(1575).createNewItem(1, true);
			if (perso.addObjet(obj, false)) {
				World.addObjet(obj, true);
			}
			for (final int id2 : doplons) {
				removeObject(perso, id2, 1);
			}
			break;
		}
		case 6: {
			SocketManager.GAME_SEND_MESSAGE(perso, "Prochainement..");
			break;
		}
		}
		SocketManager.GAME_SEND_Ow_PACKET(perso);
	}

	public static int getDoplonByClasse(final int classe) {
		switch (classe) {
		case 1: {
			return 10306;
		}
		case 2: {
			return 10308;
		}
		case 3: {
			return 10305;
		}
		case 4: {
			return 10312;
		}
		case 5: {
			return 10313;
		}
		case 6: {
			return 10303;
		}
		case 7: {
			return 10304;
		}
		case 8: {
			return 10307;
		}
		case 9: {
			return 10302;
		}
		case 10: {
			return 10311;
		}
		case 11: {
			return 10310;
		}
		case 12: {
			return 10309;
		}
		default: {
			return -1;
		}
		}
	}

	private static ArrayList<Integer> hasAllDoplon(final Player perso) {
		final ArrayList<Integer> doplons = new ArrayList<Integer>();
		if (perso.hasItemTemplate(10306, 1)) {
			doplons.add(10306);
		} else if (perso.hasItemTemplate(10308, 1)) {
			doplons.add(10308);
		} else if (perso.hasItemTemplate(10305, 1)) {
			doplons.add(10305);
		} else if (perso.hasItemTemplate(10312, 1)) {
			doplons.add(10312);
		} else if (perso.hasItemTemplate(10313, 1)) {
			doplons.add(10313);
		} else if (perso.hasItemTemplate(10303, 1)) {
			doplons.add(10303);
		} else if (perso.hasItemTemplate(10304, 1)) {
			doplons.add(10304);
		} else if (perso.hasItemTemplate(10307, 1)) {
			doplons.add(10307);
		} else if (perso.hasItemTemplate(10302, 1)) {
			doplons.add(10302);
		} else if (perso.hasItemTemplate(10311, 1)) {
			doplons.add(10311);
		} else if (perso.hasItemTemplate(10310, 1)) {
			doplons.add(10310);
		} else if (perso.hasItemTemplate(10309, 1)) {
			doplons.add(10309);
		}
		if (doplons.size() == 12) {
			return doplons;
		}
		return null;
	}

	public static int hasOneDoplon(final Player perso) {
		if (perso.hasItemTemplate(10306, 1)) {
			return 10306;
		}
		if (perso.hasItemTemplate(10308, 1)) {
			return 10308;
		}
		if (perso.hasItemTemplate(10305, 1)) {
			return 10305;
		}
		if (perso.hasItemTemplate(10312, 1)) {
			return 10312;
		}
		if (perso.hasItemTemplate(10313, 1)) {
			return 10313;
		}
		if (perso.hasItemTemplate(10303, 1)) {
			return 10303;
		}
		if (perso.hasItemTemplate(10304, 1)) {
			return 10304;
		}
		if (perso.hasItemTemplate(10307, 1)) {
			return 10307;
		}
		if (perso.hasItemTemplate(10302, 1)) {
			return 10302;
		}
		if (perso.hasItemTemplate(10311, 1)) {
			return 10311;
		}
		if (perso.hasItemTemplate(10310, 1)) {
			return 10310;
		}
		if (perso.hasItemTemplate(10309, 1)) {
			return 10309;
		}
		return -1;
	}

	private static int hasQuaDoplon(final Player perso, final int qua) {
		if (perso.hasItemTemplate(10306, qua)) {
			return 10306;
		}
		if (perso.hasItemTemplate(10308, qua)) {
			return 10308;
		}
		if (perso.hasItemTemplate(10305, qua)) {
			return 10305;
		}
		if (perso.hasItemTemplate(10312, qua)) {
			return 10312;
		}
		if (perso.hasItemTemplate(10313, qua)) {
			return 10313;
		}
		if (perso.hasItemTemplate(10303, qua)) {
			return 10303;
		}
		if (perso.hasItemTemplate(10304, qua)) {
			return 10304;
		}
		if (perso.hasItemTemplate(10307, qua)) {
			return 10307;
		}
		if (perso.hasItemTemplate(10302, qua)) {
			return 10302;
		}
		if (perso.hasItemTemplate(10311, qua)) {
			return 10311;
		}
		if (perso.hasItemTemplate(10310, qua)) {
			return 10310;
		}
		if (perso.hasItemTemplate(10309, qua)) {
			return 10309;
		}
		return -1;
	}

	private static void removeObject(final Player perso, final int id, final int qua) {
		perso.removeByTemplateID(id, qua);
		SocketManager.GAME_SEND_Ow_PACKET(perso);
		SocketManager.GAME_SEND_Im_PACKET(perso, "022;" + qua + "~" + id);
	}

	public static boolean parseConditionTrousseau(final String stats, final int npc, final int map) {
		final World.Couple<Integer, Integer> couple = Dopeul.donjons.get(map);
		return couple != null && couple.first == npc && Integer.toHexString(couple.second).equals(stats);
	}

	public static String generateStats() {
		final StringBuilder stats = new StringBuilder();
		for (final World.Couple<Integer, Integer> couple : Dopeul.donjons.values()) {
			if (!stats.toString().isEmpty()) {
				stats.append(",");
			}
			stats.append(Integer.toHexString(couple.second));
		}
		return stats.toString();
	}

	public static Map<Integer, String> generateStatsTrousseau() {
		final Map<Integer, String> txtStat = new TreeMap<Integer, String>();
		txtStat.put(814, generateStats());
		txtStat.put(805, String.valueOf(System.currentTimeMillis()));
		return txtStat;
	}
}
