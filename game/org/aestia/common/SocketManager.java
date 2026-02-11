// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.aestia.client.Player;
import org.aestia.client.other.Group;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.Prism;
import org.aestia.entity.monster.Monster;
import org.aestia.entity.npc.Npc;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.game.GameClient;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.hdv.Hdv;
import org.aestia.hdv.HdvEntry;
import org.aestia.job.JobStat;
import org.aestia.kernel.Config;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.Case;
import org.aestia.map.InteractiveObject;
import org.aestia.map.MountPark;
import org.aestia.object.ObjectSet;
import org.aestia.object.ObjectTemplate;
import org.aestia.other.Guild;
import org.aestia.other.Trunk;
import org.aestia.quest.Quest;

public class SocketManager {
	public static void send(final Player p, final String packet) {
		if (p == null || p.getAccount() == null) {
			return;
		}
		send(p.getGameClient(), packet);
	}

	public static void send(final GameClient out, final String packet) {
		if (out != null && !out.getSession().isClosing() && out.getSession().isConnected()) {
			out.getSession().write(packet);
		}
	}

	public static void GAME_SEND_UPDATE_ITEM(final Player P, final org.aestia.object.Object obj) {
		final String packet = "OC|" + obj.parseItem();
		send(P, packet);
	}

	public static void GAME_SEND_BONBON_DATA(final Player p, final int id, final int turn) {
		final String a = "OAKO" + Integer.toHexString(1) + "~" + Integer.toHexString(id) + "~" + Integer.toHexString(1)
				+ "~" + Integer.toHexString(25) + "~" + Constant.getStatsOfCandy(id, turn) + ";";
		send(p, a);
	}

	public static void GAME_SEND_MASCOTTE_DATA(final Player p, final int id) {
		final String packet = "OAKO" + Integer.toHexString(1) + "~" + Integer.toHexString(id) + "~"
				+ Integer.toHexString(1) + "~" + Integer.toHexString(24) + "~" + Constant.getStatsOfMascotte() + ";";
		send(p, packet);
	}

	public static void MULTI_SEND_Af_PACKET(final GameClient out, final int position, final int totalAbo,
			final int totalNonAbo, final String subscribe, final int queueID) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Af").append(position).append("|").append(totalAbo).append("|").append(totalNonAbo).append("|")
				.append(subscribe).append("|").append(queueID);
		send(out, packet.toString());
	}

	public static void GAME_SEND_HELLOGAME_PACKET(final GameClient out) {
		final String packet = "HG";
		send(out, packet);
	}

	public static void GAME_SEND_ATTRIBUTE_FAILED(final GameClient out) {
		final String packet = "ATE";
		send(out, packet);
	}

	public static void GAME_SEND_ATTRIBUTE_SUCCESS(final GameClient out) {
		final String packet = "ATK0";
		send(out, packet);
	}

	public static void GAME_SEND_AV0(final GameClient out) {
		final String packet = "AV0";
		send(out, packet);
	}

	public static void GAME_SEND_HIDE_GENERATE_NAME(final GameClient out) {
		final String packet = "APE2";
		send(out, packet);
	}

	public static void GAME_SEND_PERSO_LIST(final GameClient out, final Map<Integer, Player> persos,
			final long subscriber) {
		final StringBuilder packet = new StringBuilder();
		packet.append("ALK");
		if (Main.useSubscribe) {
			packet.append(subscriber);
		} else {
			packet.append("86400000");
		}
		packet.append("|").append(persos.size());
		for (final Map.Entry<Integer, Player> entry : persos.entrySet()) {
			packet.append(entry.getValue().parseALK());
		}
		send(out, packet.toString());
	}

	public static void GAME_SEND_NAME_ALREADY_EXIST(final GameClient out) {
		final String packet = "AAEa";
		send(out, packet);
	}

	public static void GAME_SEND_CREATE_PERSO_FULL(final GameClient out) {
		final String packet = "AAEf";
		send(out, packet);
	}

	public static void GAME_SEND_CREATE_OK(final GameClient out) {
		final String packet = "AAK";
		send(out, packet);
	}

	public static void GAME_SEND_DELETE_PERSO_FAILED(final GameClient out) {
		final String packet = "ADE";
		send(out, packet);
	}

	public static void GAME_SEND_CREATE_FAILED(final GameClient out) {
		final String packet = "AAEF";
		send(out, packet);
	}

	public static void GAME_SEND_PERSO_SELECTION_FAILED(final GameClient out) {
		final String packet = "ASE";
		send(out, packet);
	}

	public static void GAME_SEND_STATS_PACKET(final Player perso) {
		final String packet = perso.getAsPacket();
		GAME_SEND_Ow_PACKET(perso);
		send(perso, packet);
	}

	public static void GAME_SEND_Rx_PACKET(final Player out) {
		final String packet = "Rx" + out.getMountXpGive();
		send(out, packet);
	}

	public static void GAME_SEND_Rn_PACKET(final Player out, final String name) {
		final String packet = "Rn" + name;
		send(out, packet);
	}

	public static void GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(final Player perso, final org.aestia.object.Object item) {
		final StringBuilder packet = new StringBuilder();
		packet.append("OCO").append(item.parseItem());
		send(perso, packet.toString());
	}

	public static void GAME_SEND_Re_PACKET(final Player out, final String sign, final Dragodinde DD) {
		String packet = "Re" + sign;
		if (sign.equals("+")) {
			packet = String.valueOf(packet) + DD.parseToDinde();
		}
		send(out, packet);
	}

	public static void GAME_SEND_ASK(final GameClient out, final Player perso) {
		final StringBuilder packet = new StringBuilder();
		int color1 = perso.getColor1();
		int color2 = perso.getColor2();
		int color3 = perso.getColor3();
		if (perso.getObjetByPos(22) != null && perso.getObjetByPos(22).getTemplate().getId() == 10838) {
			color1 = 16342021;
			color2 = 16342021;
			color3 = 16342021;
		}
		packet.append("ASK|").append(perso.getId()).append("|").append(perso.getName()).append("|");
		packet.append(perso.getLevel()).append("|").append(perso.getMorphMode() ? -1 : perso.getClasse()).append("|")
				.append(perso.getSexe());
		packet.append("|").append(perso.get_gfxID()).append("|")
				.append((color1 == -1) ? "-1" : Integer.toHexString(color1));
		packet.append("|").append((color2 == -1) ? "-1" : Integer.toHexString(color2)).append("|");
		packet.append((color3 == -1) ? "-1" : Integer.toHexString(color3)).append("|");
		packet.append(perso.parseItemToASK());
		send(out, packet.toString());
	}

	public static void GAME_SEND_ALIGNEMENT(final GameClient out, final int alliID) {
		final String packet = "ZS" + alliID;
		send(out, packet);
	}

	public static void GAME_SEND_ADD_CANAL(final GameClient out, final String chans) {
		final String packet = "cC+" + chans;
		send(out, packet);
	}

	public static void GAME_SEND_ZONE_ALLIGN_STATUT(final GameClient out) {
		final String packet = "al|" + World.getSousZoneStateString();
		send(out, packet);
	}

	public static void GAME_SEND_SEESPELL_OPTION(final GameClient out, final boolean spells) {
		final String packet = "SLo" + (spells ? "+" : "-");
		send(out, packet);
	}

	public static void GAME_SEND_RESTRICTIONS(final GameClient out) {
		final String packet = "AR6bk";
		send(out, packet);
	}

	public static void GAME_SEND_Ow_PACKET(final Player perso) {
		final String packet = "Ow" + perso.getPodUsed() + "|" + perso.getMaxPod();
		send(perso, packet);
	}

	public static void GAME_SEND_OT_PACKET(final GameClient out, final int id) {
		String packet = "OT";
		if (id > 0) {
			packet = String.valueOf(packet) + id;
		}
		send(out, packet);
	}

	public static void GAME_SEND_SEE_FRIEND_CONNEXION(final GameClient out, final boolean see) {
		final String packet = "FO" + (see ? "+" : "-");
		send(out, packet);
	}

	public static void GAME_SEND_GAME_CREATE(final GameClient out, final String _name) {
		final String packet = "GCK|1|" + _name;
		send(out, packet);
	}

	public static void GAME_SEND_SERVER_HOUR(final GameClient out) {
		final String packet = GameServer.getServerTime();
		send(out, packet);
	}

	public static void GAME_SEND_SERVER_DATE(final GameClient out) {
		final String packet = GameServer.getServerDate();
		send(out, packet);
	}

	public static void GAME_SEND_MAPDATA(final GameClient out, final int id, final String date, final String key) {
		final String packet = "GDM|" + id + "|" + date + "|" + key;
		send(out, packet);
	}

	public static void GAME_SEND_GDK_PACKET(final GameClient out) {
		final String packet = "GDK";
		send(out, packet);
	}

	public static void GAME_SEND_MAP_MOBS_GMS_PACKETS(final GameClient out, final org.aestia.map.Map Map) {
		final String packet = Map.getMobGroupGMsPackets();
		if (packet.equals("")) {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_MAP_OBJECTS_GDS_PACKETS(final GameClient out, final org.aestia.map.Map Map) {
		final String packet = Map.getObjectsGDsPackets();
		if (packet.equals("")) {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_MAP_NPCS_GMS_PACKETS(final GameClient out, final org.aestia.map.Map Map) {
		final String packet = Map.getNpcsGMsPackets(out.getPersonnage());
		if (packet.equals("") && packet.length() < 4) {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_MAP_PERCO_GMS_PACKETS(final GameClient out, final org.aestia.map.Map Map) {
		final String packet = Collector.parseGM(Map);
		if (packet.length() < 5) {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_MAP_GMS_PACKETS(final GameClient out, final org.aestia.map.Map Map) {
		final String packet = Map.getGMsPackets(out.getPersonnage());
		send(out, packet);
	}

	public static void GAME_SEND_ERASE_ON_MAP_TO_MAP(final org.aestia.map.Map map, final int guid) {
		if (map == null) {
			return;
		}
		final String packet = "GM|-" + guid;
		for (final Player z : map.getPersos()) {
			if (z.getGameClient() == null) {
				continue;
			}
			send(z.getGameClient(), packet);
		}
	}

	public static void GAME_SEND_ON_FIGHTER_KICK(final Fight f, final int guid, final int team) {
		final String packet = "GM|-" + guid;
		for (final Fighter F : f.getFighters(team)) {
			if (F.getPersonnage() != null && F.getPersonnage().getGameClient() != null) {
				if (F.getPersonnage().getId() == guid) {
					continue;
				}
				send(F.getPersonnage().getGameClient(), packet);
			}
		}
	}

	public static void GAME_SEND_ALTER_FIGHTER_MOUNT(final Fight fight, final Fighter fighter, final int guid,
			final int team, final int otherteam) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GM|-").append(guid).append('\0').append(fighter.getGmPacket('+'));
		for (final Fighter F : fight.getFighters(team)) {
			if (F.getPersonnage() != null && F.getPersonnage().getGameClient() != null) {
				if (!F.getPersonnage().isOnline()) {
					continue;
				}
				send(F.getPersonnage().getGameClient(), packet.toString());
			}
		}
		if (otherteam > -1) {
			for (final Fighter F : fight.getFighters(otherteam)) {
				if (F.getPersonnage() != null && F.getPersonnage().getGameClient() != null) {
					if (!F.getPersonnage().isOnline()) {
						continue;
					}
					send(F.getPersonnage().getGameClient(), packet.toString());
				}
			}
		}
	}

	public static void GAME_SEND_ADD_PLAYER_TO_MAP(final org.aestia.map.Map map, final Player perso) {
		final String packet = "GM|+" + perso.parseToGM();
		for (final Player z : map.getPersos()) {
			if (perso.get_size() > 0) {
				send(z, packet);
			} else {
				if (z.getGroupe() == null) {
					continue;
				}
				send(z, packet);
			}
		}
	}

	public static void GAME_SEND_DUEL_Y_AWAY(final GameClient out, final int guid) {
		final String packet = "GA;903;" + guid + ";o";
		send(out, packet);
	}

	public static void GAME_SEND_DUEL_E_AWAY(final GameClient out, final int guid) {
		final String packet = "GA;903;" + guid + ";z";
		send(out, packet);
	}

	public static void GAME_SEND_MAP_NEW_DUEL_TO_MAP(final org.aestia.map.Map map, final int guid, final int guid2) {
		final String packet = "GA;900;" + guid + ";" + guid2;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_CANCEL_DUEL_TO_MAP(final org.aestia.map.Map map, final int guid, final int guid2) {
		final String packet = "GA;902;" + guid + ";" + guid2;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_MAP_START_DUEL_TO_MAP(final org.aestia.map.Map map, final int guid, final int guid2) {
		final String packet = "GA;901;" + guid + ";" + guid2;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_MAP_FIGHT_COUNT(final GameClient out, final org.aestia.map.Map map) {
		final String packet = "fC" + map.getNbrFight();
		send(out, packet);
	}

	public static void GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(final Fight fight, final int teams, final int state,
			final int cancelBtn, final int duel, final int spec, final int time, final int type) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GJK").append(state).append("|").append(cancelBtn).append("|").append(duel).append("|")
				.append(spec).append("|").append(time).append("|").append(type);
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(final Fight fight, final int teams, final String places,
			final int team) {
		final String packet = "GP" + places + "|" + team;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_MAP_FIGHT_COUNT_TO_MAP(final org.aestia.map.Map map) {
		final String packet = "fC" + map.getNbrFight();
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(final org.aestia.map.Map map, final int arg1,
			final int guid1, final int guid2, final int cell1, final String str1, final int cell2, final String str2) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gc+").append(guid1).append(";").append(arg1).append("|").append(guid1).append(";").append(cell1)
				.append(";").append(str1).append("|").append(guid2).append(";").append(cell2).append(";").append(str2);
		for (final Player z : map.getPersos()) {
			send(z, packet.toString());
		}
	}

	public static void GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(final Player p, final org.aestia.map.Map map,
			final int arg1, final int guid1, final int guid2, final int cell1, final String str1, final int cell2,
			final String str2) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gc+").append(guid1).append(";").append(arg1).append("|").append(guid1).append(";").append(cell1)
				.append(";").append(str1).append("|").append(guid2).append(";").append(cell2).append(";").append(str2);
		send(p, packet.toString());
	}

	public static void GAME_SEND_GAME_REMFLAG_PACKET_TO_MAP(final org.aestia.map.Map map, final int guid) {
		final String packet = "Gc-" + guid;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_ADD_IN_TEAM_PACKET_TO_MAP(final org.aestia.map.Map map, final int teamID,
			final Fighter perso) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gt").append(teamID).append("|+").append(perso.getId()).append(";").append(perso.getPacketsName())
				.append(";").append(perso.getLvl());
		for (final Player z : map.getPersos()) {
			send(z, packet.toString());
		}
	}

	public static void GAME_SEND_ADD_IN_TEAM_PACKET_TO_PLAYER(final Player p, final org.aestia.map.Map map,
			final int teamID, final Fighter perso) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gt").append(teamID).append("|+").append(perso.getId()).append(";").append(perso.getPacketsName())
				.append(";").append(perso.getLvl());
		send(p, packet.toString());
	}

	public static void GAME_SEND_REMOVE_IN_TEAM_PACKET_TO_MAP(final org.aestia.map.Map map, final int teamID,
			final Fighter perso) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gt").append(teamID).append("|-").append(perso.getId()).append(";").append(perso.getPacketsName())
				.append(";").append(perso.getLvl());
		for (final Player z : map.getPersos()) {
			send(z, packet.toString());
		}
	}

	public static void GAME_SEND_MAP_MOBS_GMS_PACKETS_TO_MAP(final org.aestia.map.Map map) {
		final String packet = map.getMobGroupGMsPackets();
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_MAP_MOBS_GMS_PACKETS_TO_MAP(final org.aestia.map.Map map, final Player perso) {
		final String packet = map.getMobGroupGMsPackets();
		send(perso, packet);
	}

	public static void GAME_SEND_MAP_MOBS_GM_PACKET(final org.aestia.map.Map map, final Monster.MobGroup current_Mobs) {
		String packet = "GM|";
		packet = String.valueOf(packet) + current_Mobs.parseGM();
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_MAP_GMS_PACKETS(final org.aestia.map.Map map, final Player _perso) {
		final String packet = (_perso.get_fight() != null) ? map.getFightersGMsPackets(_perso.get_fight())
				: map.getGMsPackets(_perso);
		send(_perso, packet);
	}

	public static void GAME_SEND_ON_EQUIP_ITEM(final org.aestia.map.Map map, final Player _perso) {
		final String packet = _perso.parseToOa();
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_ON_EQUIP_ITEM_FIGHT(final Player _perso, final Fighter f, final Fight F) {
		final String packet = _perso.parseToOa();
		for (final Fighter z : F.getFighters(f.getTeam2())) {
			if (z.getPersonnage() == null) {
				continue;
			}
			send(z.getPersonnage(), packet);
		}
		for (final Fighter z : F.getFighters(f.getOtherTeam())) {
			if (z.getPersonnage() == null) {
				continue;
			}
			send(z.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_FIGHT_CHANGE_PLACE_PACKET_TO_FIGHT(final Fight fight, final int teams,
			final org.aestia.map.Map map, final int guid, final int cell) {
		final String packet = "GIC|" + guid + ";" + cell + ";1";
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_Ew_PACKET(final Player perso, final int pods, final int podsMax) {
		final String packet = "Ew" + pods + ";" + podsMax;
		send(perso, packet);
	}

	public static void GAME_SEND_EL_MOUNT_PACKET(final Player out, final Dragodinde drago) {
		final String packet = "EL" + drago.parseToDindeItem();
		send(out, packet);
	}

	public static void GAME_SEND_GM_MOUNT_TO_MAP(final org.aestia.map.Map map, final Dragodinde dd) {
		final String packet = dd.getRaisingMount(map.getMountPark());
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_GDO_OBJECT_TO_MAP(final GameClient out, final org.aestia.map.Map map) {
		final String packet = map.getObjects();
		if (packet.equals("")) {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_GM_MOUNT(final GameClient out, final org.aestia.map.Map map) {
		final String packet = map.getGMOfMount();
		if (packet == "") {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_Ef_MOUNT_TO_ETABLE(final Player perso, final char c, final String s) {
		final String packet = "Ef" + c + s;
		send(perso, packet);
	}

	public static void GAME_SEND_GA_ACTION_TO_MAP(final org.aestia.map.Map mapa, final String idUnique,
			final int idAction, final String s1, final String s2) {
		String packet = "GA" + idUnique + ";" + idAction + ";" + s1;
		if (!s2.equals("")) {
			packet = String.valueOf(packet) + ";" + s2;
		}
		for (final Player z : mapa.getPersos()) {
			send(z, packet);
		}
	}

	public static void SEND_GDO_PUT_OBJECT_MOUNT(final org.aestia.map.Map map, final String str) {
		final String packet = "GDO+" + str;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void SEND_GDE_FRAME_OBJECT_EXTERNAL(final org.aestia.map.Map map, final String str) {
		final String packet = "GDE|" + str;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void SEND_GDE_FRAME_OBJECT_EXTERNAL(final Player perso, final String str) {
		final String packet = "GDE|" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(final org.aestia.map.Map map, final char s,
			final char option, final int guid) {
		final String packet = "Go" + s + option + guid;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_FIGHT_PLAYER_READY_TO_FIGHT(final Fight fight, final int teams, final int guid,
			final boolean b) {
		final String packet = "GR" + (b ? "1" : "0") + guid;
		if (fight.getState() != 2) {
			return;
		}
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.getPersonnage() != null) {
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				if (f.hasLeft()) {
					continue;
				}
				send(f.getPersonnage(), packet);
			}
		}
	}

	public static void GAME_SEND_GJK_PACKET(final Player out, final int state, final int cancelBtn, final int duel,
			final int spec, final int time, final int unknown) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GJK").append(state).append("|").append(cancelBtn).append("|").append(duel).append("|")
				.append(spec).append("|").append(time).append("|").append(unknown);
		send(out, packet.toString());
	}

	public static void GAME_SEND_FIGHT_PLACES_PACKET(final GameClient out, final String places, final int team) {
		final String packet = "GP" + places + "|" + team;
		send(out, packet);
	}

	public static void GAME_SEND_Im_PACKET_TO_ALL(final String str) {
		final String packet = "Im" + str;
		for (final Player perso : World.getOnlinePersos()) {
			send(perso, packet);
		}
	}

	public static void GAME_SEND_Im_PACKET(final Player out, final String str) {
		final String packet = "Im" + str;
		send(out, packet);
	}

	public static void GAME_SEND_ILS_PACKET(final Player out, final int i) {
		final String packet = "ILS" + i;
		send(out, packet);
	}

	public static void GAME_SEND_ILF_PACKET(final Player P, final int i) {
		final String packet = "ILF" + i;
		send(P, packet);
	}

	public static void GAME_SEND_Im_PACKET_TO_MAP(final org.aestia.map.Map map, final String id) {
		final String packet = "Im" + id;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_Im_PACKET_TO_PLAYER(final Player p, final String id) {
		final String packet = "Im" + id;
		send(p, packet);
	}

	public static void GAME_SEND_eUK_PACKET_TO_MAP(final org.aestia.map.Map map, final int guid, final int emote) {
		final String packet = "eUK" + guid + "|" + emote;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_Im_PACKET_TO_FIGHT(final Fight fight, final int teams, final String id) {
		final String packet = "Im" + id;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_MESSAGE(final Player out, final String mess, final String color) {
		final String packet = "cs<font color='#" + color + "'>" + mess + "</font>";
		send(out, packet);
	}

	public static void GAME_SEND_MESSAGE(final Player out, final String mess) {
		final String packet = "cs<font color='#" + Config.getInstance().colorMessage + "'>" + mess + "</font>";
		send(out, packet);
	}

	public static void GAME_SEND_MESSAGE_TO_MAP(final org.aestia.map.Map map, final String mess, final String color) {
		final String packet = "cs<font color='#" + color + "'>" + mess + "</font>";
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_GA903_ERROR_PACKET(final GameClient out, final char c, final int guid) {
		final String packet = "GA;903;" + guid + ";" + c;
		send(out, packet);
	}

	public static void GAME_SEND_GIC_PACKETS_TO_FIGHT(final Fight fight, final int teams) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GIC|");
		for (final Fighter p : fight.getFighters(3)) {
			if (p.getCell() == null) {
				continue;
			}
			packet.append(p.getId()).append(";").append(p.getCell().getId()).append(";1|");
		}
		for (final Fighter perso : fight.getFighters(teams)) {
			if (perso.hasLeft()) {
				continue;
			}
			if (perso.getPersonnage() == null) {
				continue;
			}
			if (!perso.getPersonnage().isOnline()) {
				continue;
			}
			send(perso.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_GIC_PACKET_TO_FIGHT(final Fight fight, final int teams, final Fighter f) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GIC|").append(f.getId()).append(";").append(f.getCell().getId()).append(";1|");
		for (final Fighter perso : fight.getFighters(teams)) {
			if (perso.hasLeft()) {
				continue;
			}
			if (perso.getPersonnage() == null) {
				continue;
			}
			if (!perso.getPersonnage().isOnline()) {
				continue;
			}
			send(perso.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_GIC_PACKETS(final Fight fight, final Player out) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GIC|");
		for (final Fighter p : fight.getFighters(3)) {
			if (p.getCell() == null) {
				continue;
			}
			packet.append(p.getId()).append(";").append(p.getCell().getId()).append(";1|");
		}
		send(out, packet.toString());
	}

	public static void GAME_SEND_GS_PACKET_TO_FIGHT(final Fight fight, final int teams) {
		final String packet = "GS";
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			f.initBuffStats();
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GS_PACKET(final Player out) {
		final String packet = "GS";
		send(out, packet);
	}

	public static void GAME_SEND_GTL_PACKET_TO_FIGHT(final Fight fight, final int teams) {
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), fight.getGTL());
		}
	}

	public static void GAME_SEND_GTL_PACKET(final Player out, final Fight fight) {
		final String packet = fight.getGTL();
		send(out, packet);
	}

	public static void GAME_SEND_GTM_PACKET_TO_FIGHT(final Fight fight, final int teams) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GTM");
		for (final Fighter f : fight.getFighters(3)) {
			packet.append("|").append(f.getId()).append(";");
			if (f.isDead()) {
				packet.append("1");
			} else {
				packet.append("0;").append(String.valueOf(f.getPdv()) + ";").append(String.valueOf(f.getPa()) + ";")
						.append(String.valueOf(f.getPm()) + ";");
				packet.append(f.isHide() ? "-1" : f.getCell().getId()).append(";");
				packet.append(";");
				packet.append(f.getPdvMax());
			}
		}
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_GTM_PACKET(final Player out, final Fight fight) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GTM");
		for (final Fighter f : fight.getFighters(3)) {
			packet.append("|").append(f.getId()).append(";");
			if (f.isDead()) {
				packet.append("1");
			} else {
				packet.append("0;").append(String.valueOf(f.getPdv()) + ";").append(String.valueOf(f.getPa()) + ";")
						.append(String.valueOf(f.getPm()) + ";");
				packet.append(f.isHide() ? "-1" : f.getCell().getId()).append(";");
				packet.append(";");
				packet.append(f.getPdvMax());
			}
		}
		send(out, packet.toString());
	}

	public static void GAME_SEND_GAMETURNSTART_PACKET_TO_FIGHT(final Fight fight, final int teams, final int guid,
			final int time) {
		final String packet = "GTS" + guid + "|" + time;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GAMETURNSTART_PACKET(final Player P, final int guid, final int time) {
		final String packet = "GTS" + guid + "|" + time;
		send(P, packet);
	}

	public static void GAME_SEND_GV_PACKET(final Player P) {
		final String packet = "GV";
		send(P, packet);
	}

	public static void GAME_SEND_PONG(final GameClient out) {
		final String packet = "pong";
		send(out, packet);
	}

	public static void GAME_SEND_QPONG(final GameClient out) {
		final String packet = "qpong";
		send(out, packet);
	}

	public static void GAME_SEND_GAS_PACKET_TO_FIGHT(final Fight fight, final int teams, final int guid) {
		final String packet = "GAS" + guid;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GA_PACKET_TO_FIGHT(final Fight fight, final int teams, final int actionID,
			final String s1, final String s2) {
		String packet = "GA;" + actionID + ";" + s1;
		if (!s2.equals("")) {
			packet = String.valueOf(packet) + ";" + s2;
		}
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GA_PACKET(final Fight fight, final Player perso, final int actionID, final String s1,
			final String s2) {
		String packet = "GA;" + actionID + ";" + s1;
		if (!s2.equals("")) {
			packet = String.valueOf(packet) + ";" + s2;
		}
		send(perso, packet);
	}

	public static void SEND_SB_SPELL_BOOST(final Player perso, final String modif) {
		final String packet = "SB" + modif;
		send(perso, packet);
	}

	public static void GAME_SEND_GA_PACKET(final GameClient out, final String actionID, final String s0,
			final String s1, final String s2) {
		String packet = "GA" + actionID + ";" + s0;
		if (!s1.equals("")) {
			packet = String.valueOf(packet) + ";" + s1;
		}
		if (!s2.equals("")) {
			packet = String.valueOf(packet) + ";" + s2;
		}
		send(out, packet);
	}

	public static void GAME_SEND_GA_PACKET_TO_FIGHT(final Fight fight, final int teams, final int gameActionID,
			final String s1, final String s2, final String s3) {
		final String packet = "GA" + gameActionID + ";" + s1 + ";" + s2 + ";" + s3;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GAMEACTION_TO_FIGHT(final Fight fight, final int teams, final String packet) {
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GAF_PACKET_TO_FIGHT(final Fight fight, final int teams, final int i1, final int guid) {
		final String packet = "GAF" + i1 + "|" + guid;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.getPersonnage() != null) {
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				send(f.getPersonnage(), packet);
			}
		}
	}

	public static void GAME_SEND_BN(final Player out) {
		final String packet = "BN";
		send(out, packet);
	}

	public static void GAME_SEND_BN(final GameClient out) {
		final String packet = "BN";
		send(out, packet);
	}

	public static void GAME_SEND_GAMETURNSTOP_PACKET_TO_FIGHT(final Fight fight, final int teams, final int guid) {
		final String packet = "GTF" + guid;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GTR_PACKET_TO_FIGHT(final Fight fight, final int teams, final int guid) {
		final String packet = "GTR" + guid;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_EMOTICONE_TO_MAP(final org.aestia.map.Map map, final int guid, final int id) {
		final String packet = "cS" + guid + "|" + id;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_SPELL_UPGRADE_FAILED(final GameClient _out) {
		final String packet = "SUE";
		send(_out, packet);
	}

	public static void GAME_SEND_SPELL_UPGRADE_SUCCED(final GameClient _out, final int spellID, final int level) {
		final String packet = "SUK" + spellID + "~" + level;
		send(_out, packet);
	}

	public static void GAME_SEND_SPELL_LIST(final Player perso) {
		final String packet = perso.parseSpellList();
		send(perso, packet);
	}

	public static void GAME_SEND_FIGHT_PLAYER_DIE_TO_FIGHT(final Fight fight, final int teams, final int guid) {
		final String packet = "GA;103;" + guid + ";" + guid;
		for (final Fighter f : fight.getFighters(teams)) {
			if (!f.hasLeft()) {
				if (f.getPersonnage() == null) {
					continue;
				}
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				send(f.getPersonnage(), packet);
			}
		}
	}

	public static void GAME_SEND_FIGHT_GE_PACKET_TO_FIGHT(final Fight fight, final int teams, final int win) {
		final String packet = fight.getGE(win);
		for (final Fighter f : fight.getFighters(teams)) {
			if (!f.hasLeft()) {
				if (f.getPersonnage() == null) {
					continue;
				}
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				send(f.getPersonnage(), packet);
			}
		}
	}

	public static void GAME_SEND_FIGHT_GE_PACKET(final GameClient out, final Fight fight, final int win) {
		final String packet = fight.getGE(win);
		send(out, packet);
	}

	public static void GAME_SEND_FIGHT_GIE_TO_FIGHT(final Fight fight, final int teams, final int mType,
			final int cible, final int value, final String mParam2, final String mParam3, final String mParam4,
			final int turn, final int spellID) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GIE").append(mType).append(";").append(cible).append(";").append(value).append(";")
				.append(mParam2).append(";").append(mParam3).append(";").append(mParam4).append(";").append(turn)
				.append(";").append(spellID);
		for (final Fighter f : fight.getFighters(teams)) {
			if (!f.hasLeft()) {
				if (f.getPersonnage() == null) {
					continue;
				}
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				send(f.getPersonnage(), packet.toString());
			}
		}
	}

	public static void GAME_SEND_MAP_FIGHT_GMS_PACKETS_TO_FIGHT(final Fight fight, final int teams,
			final org.aestia.map.Map map) {
		final String packet = map.getFightersGMsPackets(fight);
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_MAP_FIGHT_GMS_PACKETS(final Fight fight, final org.aestia.map.Map map,
			final Player _perso) {
		final String packet = map.getFightersGMsPackets(fight);
		send(_perso, packet);
	}

	public static void GAME_SEND_FIGHT_PLAYER_JOIN(final Fight fight, final int teams, final Fighter _fighter) {
		final String packet = _fighter.getGmPacket('+');
		for (final Fighter f : fight.getFighters(teams)) {
			if (f != _fighter && f.getPersonnage() != null) {
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				if (f.getPersonnage() == null || f.getPersonnage().getGameClient() == null) {
					continue;
				}
				send(f.getPersonnage(), packet);
			}
		}
	}

	public static void GAME_SEND_cMK_PACKET(final Player perso, final String suffix, final int guid, final String name,
			final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		send(perso, packet);
	}

	public static void GAME_SEND_FIGHT_LIST_PACKET(final GameClient out, final org.aestia.map.Map map) {
		final StringBuilder packet = new StringBuilder();
		packet.append("fL");
		for (final Map.Entry<Integer, Fight> entry : map.getFights().entrySet()) {
			if (packet.length() > 2) {
				packet.append("|");
			}
			packet.append(entry.getValue().parseFightInfos());
		}
		send(out, packet.toString());
	}

	public static void GAME_SEND_cMK_PACKET_TO_MAP(final org.aestia.map.Map map, final String suffix, final int guid,
			final String name, final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_cMK_PACKET_TO_GUILD(final Guild g, final String suffix, final int guid,
			final String name, final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		for (final Player perso : g.getMembers()) {
			if (perso != null) {
				if (!perso.isOnline()) {
					continue;
				}
				send(perso, packet);
			}
		}
	}

	public static void GAME_SEND_cMK_PACKET_TO_ALL(final Player perso, final String suffix, final int guid,
			final String name, final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		if (perso.getLevel() < 6) {
			GAME_SEND_MESSAGE(perso, "Ce canal n'est accessible qu'\u00e0 partir du niveau <b>6</b>.");
			GAME_SEND_BN(perso);
			return;
		}
		for (final Player perso2 : World.getOnlinePersos()) {
			send(perso2, packet);
		}
	}

	public static void GAME_SEND_cMK_PACKET_TO_ALIGN(final String suffix, final int guid, final String name,
			final String msg, final Player _perso) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		for (final Player perso : World.getOnlinePersos()) {
			if (perso.get_align() == _perso.get_align()) {
				send(perso, packet);
			}
		}
	}

	public static void GAME_SEND_cMK_PACKET_TO_ADMIN(final String suffix, final int guid, final String name,
			final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		for (final Player perso : World.getOnlinePersos()) {
			if (perso.isOnline() && perso.getAccount() != null && perso.getGroupe() != null) {
				send(perso, packet);
			}
		}
	}

	public static void GAME_SEND_cMK_PACKET_TO_FIGHT(final Fight fight, final int teams, final String suffix,
			final int guid, final String name, final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GDZ_PACKET_TO_FIGHT(final Fight fight, final int teams, final String suffix,
			final int cell, final int size, final int unk) {
		final String packet = "GDZ" + suffix + cell + ";" + size + ";" + unk;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GDC_PACKET_TO_FIGHT(final Fight fight, final int teams, final int cell) {
		final String packet = "GDC" + cell;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_GA2_PACKET(final GameClient out, final int guid) {
		final String packet = "GA;2;" + guid + ";";
		send(out, packet);
	}

	public static void GAME_SEND_CHAT_ERROR_PACKET(final GameClient out, final String name) {
		final String packet = "cMEf" + name;
		send(out, packet);
	}

	public static void GAME_SEND_eD_PACKET_TO_MAP(final org.aestia.map.Map map, final int guid, final int dir) {
		final String packet = "eD" + guid + "|" + dir;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_ECK_PACKET(final Player out, final int type, final String str) {
		String packet = "ECK" + type;
		if (!str.equals("")) {
			packet = String.valueOf(packet) + "|" + str;
		}
		send(out, packet);
	}

	public static void GAME_SEND_ECK_PACKET(final GameClient out, final int type, final String str) {
		String packet = "ECK" + type;
		if (!str.equals("")) {
			packet = String.valueOf(packet) + "|" + str;
		}
		send(out, packet);
	}

	public static void GAME_SEND_ITEM_VENDOR_LIST_PACKET(final GameClient out, final Npc npc) {
		final String packet = "EL" + npc.getTemplate().getItemVendorList();
		send(out, packet);
	}

	public static void GAME_SEND_ITEM_LIST_PACKET_PERCEPTEUR(final GameClient out, final Collector perco) {
		final String packet = "EL" + perco.getItemCollectorList();
		send(out, packet);
	}

	public static void GAME_SEND_ITEM_LIST_PACKET_SELLER(final Player p, final Player out) {
		final String packet = "EL" + p.parseStoreItemsList();
		send(out, packet);
	}

	public static void GAME_SEND_EV_PACKET(final GameClient out) {
		final String packet = "EV";
		send(out, packet);
	}

	public static void GAME_SEND_DCK_PACKET(final GameClient out, final int id) {
		final String packet = "DCK" + id;
		send(out, packet);
	}

	public static void GAME_SEND_QUESTION_PACKET(final GameClient out, final String str) {
		final String packet = "DQ" + str;
		send(out, packet);
	}

	public static void GAME_SEND_END_DIALOG_PACKET(final GameClient out) {
		final String packet = "DV";
		send(out, packet);
	}

	public static void GAME_SEND_CONSOLE_MESSAGE_PACKET(final GameClient out, final String mess) {
		final String packet = "BAT2" + mess;
		send(out, packet);
	}

	public static void GAME_SEND_BUY_ERROR_PACKET(final GameClient out) {
		final String packet = "EBE";
		send(out, packet);
	}

	public static void GAME_SEND_SELL_ERROR_PACKET(final GameClient out) {
		final String packet = "ESE";
		send(out, packet);
	}

	public static void GAME_SEND_BUY_OK_PACKET(final GameClient out) {
		final String packet = "EBK";
		send(out, packet);
	}

	public static void GAME_SEND_OBJECT_QUANTITY_PACKET(final Player out, final org.aestia.object.Object obj) {
		final String packet = "OQ" + obj.getGuid() + "|" + obj.getQuantity();
		send(out, packet);
	}

	public static void GAME_SEND_OAKO_PACKET(final Player out, final org.aestia.object.Object obj) {
		final String packet = "OAKO" + obj.parseItem();
		send(out, packet);
	}

	public static void SEND_OAKO_PACKET(final GameClient out, final org.aestia.object.Object obj) {
		final String packet = "OAKO" + obj.parseItem();
		send(out, packet);
	}

	public static void GAME_SEND_ESK_PACKEt(final Player out) {
		final String packet = "ESK";
		send(out, packet);
	}

	public static void GAME_SEND_REMOVE_ITEM_PACKET(final Player out, final int guid) {
		final String packet = "OR" + guid;
		send(out, packet);
	}

	public static void GAME_SEND_DELETE_OBJECT_FAILED_PACKET(final GameClient out) {
		final String packet = "OdE";
		send(out, packet);
	}

	public static void GAME_SEND_OBJET_MOVE_PACKET(final Player out, final org.aestia.object.Object obj) {
		String packet = "OM" + obj.getGuid() + "|";
		if (obj.getPosition() != -1) {
			packet = String.valueOf(packet) + obj.getPosition();
		}
		send(out, packet);
	}

	public static void GAME_SEND_DELETE_STATS_ITEM_FM(final Player perso, final int id) {
		final String packet = "OR" + id;
		send(perso, packet);
	}

	public static void GAME_SEND_EMOTICONE_TO_FIGHT(final Fight fight, final int teams, final int guid, final int id) {
		final String packet = "cS" + guid + "|" + id;
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_OAEL_PACKET(final GameClient out) {
		final String packet = "OAEL";
		send(out, packet);
	}

	public static void GAME_SEND_NEW_LVL_PACKET(final GameClient out, final int lvl) {
		final String packet = "AN" + lvl;
		send(out, packet);
	}

	public static void GAME_SEND_MESSAGE_TO_ALL(final String msg, final String color) {
		final String packet = "cs<font color='#" + color + "'>" + msg + "</font>";
		for (final Player P : World.getOnlinePersos()) {
			send(P, packet);
		}
	}

	public static void GAME_SEND_EXCHANGE_REQUEST_OK(final GameClient out, final int guid, final int guidT,
			final int msgID) {
		final String packet = "ERK" + guid + "|" + guidT + "|" + msgID;
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_REQUEST_ERROR(final GameClient out, final char c) {
		final String packet = "ERE" + c;
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_CONFIRM_OK(final GameClient out, final int type) {
		final String packet = "ECK" + type;
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_MOVE_OK(final Player out, final char type, final String signe,
			final String s1) {
		String packet = "EMK" + type + signe;
		if (!s1.equals("")) {
			packet = String.valueOf(packet) + s1;
		}
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_MOVE_OK_FM(final Player out, final char type, final String signe,
			final String s1) {
		String packet = "EmK" + type + signe;
		if (!s1.equals("")) {
			packet = String.valueOf(packet) + s1;
		}
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_OTHER_MOVE_OK(final GameClient out, final char type, final String signe,
			final String s1) {
		String packet = "EmK" + type + signe;
		if (!s1.equals("")) {
			packet = String.valueOf(packet) + s1;
		}
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_OTHER_MOVE_OK_FM(final GameClient out, final char type, final String signe,
			final String s1) {
		String packet = "EMK" + type + signe;
		if (!s1.equals("")) {
			packet = String.valueOf(packet) + s1;
		}
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_OK(final GameClient out, final boolean ok, final int guid) {
		final String packet = "EK" + (ok ? "1" : "0") + guid;
		send(out, packet);
	}

	public static void GAME_SEND_EXCHANGE_OK(final GameClient out, final boolean ok) {
		final String str = "EK" + (ok ? "1" : "0");
		send(out, str);
	}

	public static void GAME_SEND_EXCHANGE_VALID(final GameClient out, final char c) {
		final String packet = "EV" + c;
		send(out, packet);
	}

	public static void GAME_SEND_GROUP_INVITATION_ERROR(final GameClient out, final String s) {
		final String packet = "PIE" + s;
		send(out, packet);
	}

	public static void GAME_SEND_GROUP_INVITATION(final GameClient out, final String n1, final String n2) {
		final String packet = "PIK" + n1 + "|" + n2;
		send(out, packet);
	}

	public static void GAME_SEND_GROUP_CREATE(final GameClient out, final Group g) {
		final String packet = "PCK" + g.getChief().getName();
		send(out, packet);
	}

	public static void GAME_SEND_PL_PACKET(final GameClient out, final Group g) {
		final String packet = "PL" + g.getChief().getId();
		send(out, packet);
	}

	public static void GAME_SEND_PR_PACKET(final Player out) {
		final String packet = "PR";
		send(out, packet);
	}

	public static void GAME_SEND_PV_PACKET(final GameClient out, final String s) {
		final String packet = "PV" + s;
		send(out, packet);
	}

	public static void GAME_SEND_ALL_PM_ADD_PACKET(final GameClient out, final Group g) {
		final StringBuilder packet = new StringBuilder();
		packet.append("PM+");
		boolean first = true;
		for (final Player p : g.getPersos()) {
			if (!first) {
				packet.append("|");
			}
			packet.append(p.parseToPM());
			first = false;
		}
		send(out, packet.toString());
	}

	public static void GAME_SEND_PM_ADD_PACKET_TO_GROUP(final Group g, final Player p) {
		final String packet = "PM+" + p.parseToPM();
		for (final Player P : g.getPersos()) {
			send(P, packet);
		}
	}

	public static void GAME_SEND_PM_MOD_PACKET_TO_GROUP(final Group g, final Player p) {
		final String packet = "PM~" + p.parseToPM();
		for (final Player P : g.getPersos()) {
			send(P, packet);
		}
	}

	public static void GAME_SEND_PM_DEL_PACKET_TO_GROUP(final Group group, final int guid) {
		final String packet = "PM-" + guid;
		for (final Player P : group.getPersos()) {
			send(P, packet);
		}
	}

	public static void GAME_SEND_cMK_PACKET_TO_GROUP(final Group g, final String s, final int guid, final String name,
			final String msg) {
		final String packet = "cMK" + s + "|" + guid + "|" + name + "|" + msg + "|";
		for (final Player P : g.getPersos()) {
			send(P, packet);
		}
	}

	public static void GAME_SEND_FIGHT_DETAILS(final GameClient out, final Fight fight) {
		if (fight == null) {
			return;
		}
		final StringBuilder packet = new StringBuilder();
		packet.append("fD").append(fight.getId()).append("|");
		for (final Fighter f : fight.getFighters(1)) {
			packet.append(f.getPacketsName()).append("~").append(f.getLvl()).append(";");
		}
		packet.append("|");
		for (final Fighter f : fight.getFighters(2)) {
			packet.append(f.getPacketsName()).append("~").append(f.getLvl()).append(";");
		}
		send(out, packet.toString());
	}

	public static void GAME_SEND_IQ_PACKET(final Player perso, final int guid, final int qua) {
		final String packet = "IQ" + guid + "|" + qua;
		send(perso, packet);
	}

	public static void GAME_SEND_JN_PACKET(final Player perso, final int jobID, final int lvl) {
		final String packet = "JN" + jobID + "|" + lvl;
		send(perso, packet);
	}

	public static void GAME_SEND_GDF_PACKET_TO_MAP(final org.aestia.map.Map map, final Case cell) {
		final int cellID = cell.getId();
		final InteractiveObject object = cell.getObject();
		final String packet = "GDF|" + cellID + ";" + object.getState() + ";" + (object.isInteractive() ? "1" : "0");
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_GDF_PACKET_TO_FIGHT(final Player player, final Collection<Case> collection) {
		String packet = "GDF|";
		for (final Case cell : collection) {
			if (cell.getObject() == null) {
				continue;
			}
			if (cell.getObject().getTemplate() == null) {
				continue;
			}
			switch (cell.getObject().getTemplate().getId()) {
			case 7500:
			case 7501:
			case 7502:
			case 7503:
			case 7504:
			case 7505:
			case 7506:
			case 7507:
			case 7508:
			case 7509:
			case 7511:
			case 7512:
			case 7513:
			case 7515:
			case 7516:
			case 7517:
			case 7518:
			case 7533:
			case 7534:
			case 7535:
			case 7536:
			case 7541:
			case 7542:
			case 7550:
			case 7551:
			case 7552:
			case 7553:
			case 7554:
			case 7557: {
				cell.getObject().setState(4);
				packet = String.valueOf(packet) + cell.getId() + ";" + cell.getObject().getState() + ";"
						+ (cell.getObject().isInteractive() ? "1" : "0") + "|";
			}
			default: {
				continue;
			}
			}
		}
		send(player, packet);
	}

	public static void GAME_SEND_GA_PACKET_TO_MAP(final org.aestia.map.Map map, final String gameActionID,
			final int actionID, final String s1, final String s2) {
		String packet = "GA" + gameActionID + ";" + actionID + ";" + s1;
		if (!s2.equals("")) {
			packet = String.valueOf(packet) + ";" + s2;
		}
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_EL_BANK_PACKET(final Player perso) {
		final String packet = "EL" + perso.parseBankPacket();
		send(perso, packet);
	}

	public static void GAME_SEND_EL_TRUNK_PACKET(final Player perso, final Trunk t) {
		final String packet = "EL" + t.parseToTrunkPacket();
		send(perso, packet);
	}

	public static void GAME_SEND_JX_PACKET(final Player perso, final ArrayList<JobStat> SMs) {
		final StringBuilder packet = new StringBuilder();
		packet.append("JX");
		for (final JobStat sm : SMs) {
			packet.append("|").append(sm.getTemplate().getId()).append(";").append(sm.get_lvl()).append(";")
					.append(sm.getXpString(";")).append(";");
		}
		send(perso, packet.toString());
	}

	public static void GAME_SEND_JO_PACKET(final Player perso, final ArrayList<JobStat> JobStats) {
		for (final JobStat SM : JobStats) {
			final String packet = "JO" + SM.getPosition() + "|" + SM.getOptBinValue() + "|" + SM.getSlotsPublic();
			send(perso, packet);
		}
	}

	public static void GAME_SEND_JO_PACKET(final Player perso, final JobStat SM) {
		final String packet = "JO" + SM.getPosition() + "|" + SM.getOptBinValue() + "|" + SM.getSlotsPublic();
		send(perso, packet);
	}

	public static void GAME_SEND_JS_PACKET(final Player perso, final ArrayList<JobStat> SMs) {
		String packet = "JS";
		for (final JobStat sm : SMs) {
			packet = String.valueOf(packet) + sm.parseJS();
		}
		send(perso, packet);
	}

	public static void GAME_SEND_EsK_PACKET(final Player perso, final String str) {
		final String packet = "EsK" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_FIGHT_SHOW_CASE(final ArrayList<GameClient> PWs, final int guid, final int cellID) {
		final String packet = "Gf" + guid + "|" + cellID;
		for (final GameClient PW : PWs) {
			send(PW, packet);
		}
	}

	public static void GAME_SEND_Ea_PACKET(final Player perso, final String str) {
		final String packet = "Ea" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_EA_PACKET(final Player perso, final String str) {
		final String packet = "EA" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_Ec_PACKET(final Player perso, final String str) {
		final String packet = "Ec" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_Em_PACKET(final Player perso, final String str) {
		final String packet = "Em" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_IO_PACKET_TO_MAP(final org.aestia.map.Map map, final int guid, final String str) {
		final String packet = "IO" + guid + "|" + str;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_FRIENDLIST_PACKET(final Player perso) {
		final String packet = "FL" + perso.getAccount().parseFriendList();
		send(perso, packet);
		if (perso.getWife() != 0) {
			final String packet2 = "FS" + perso.get_wife_friendlist();
			send(perso, packet2);
		}
	}

	public static void GAME_SEND_FRIEND_ONLINE(final Player friend, final Player perso) {
		final String packet = "Im0143;" + friend.getAccount().getPseudo()
				+ " (<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + friend.getName() + "'>" + friend.getName()
				+ "</a></b>)";
		send(perso, packet);
	}

	public static void GAME_SEND_FA_PACKET(final Player perso, final String str) {
		final String packet = "FA" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_FD_PACKET(final Player perso, final String str) {
		final String packet = "FD" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_Rp_PACKET(final Player perso, final MountPark MP) {
		final StringBuilder packet = new StringBuilder();
		if (MP == null) {
			return;
		}
		packet.append("Rp").append(MP.getOwner()).append(";").append(MP.getPrice()).append(";").append(MP.getSize())
				.append(";").append(MP.getMaxObject()).append(";");
		final Guild G = MP.getGuild();
		if (G != null) {
			packet.append(G.getName()).append(";").append(G.getEmblem());
		} else {
			packet.append(";");
		}
		send(perso, packet.toString());
	}

	public static void GAME_SEND_OS_PACKET(final Player perso, final int pano) {
		final StringBuilder packet = new StringBuilder();
		packet.append("OS");
		final int num = perso.getNumbEquipedItemOfPanoplie(pano);
		if (num <= 0) {
			packet.append("-").append(pano);
		} else {
			packet.append("+").append(pano).append("|");
			final ObjectSet IS = World.getItemSet(pano);
			if (IS != null) {
				final StringBuilder items = new StringBuilder();
				for (final ObjectTemplate OT : IS.getItemTemplates()) {
					if (perso.hasEquiped(OT.getId())) {
						if (items.length() > 0) {
							items.append(";");
						}
						items.append(OT.getId());
					}
				}
				packet.append(items.toString()).append("|")
						.append(IS.getBonusStatByItemNumb(num).parseToItemSetStats());
			}
		}
		send(perso, packet.toString());
	}

	public static void GAME_SEND_MOUNT_DESCRIPTION_PACKET(final Player perso, final Dragodinde DD) {
		final String packet = "Rd" + DD.parseToDinde();
		send(perso, packet);
	}

	public static void GAME_SEND_Rr_PACKET(final Player perso, final String str) {
		final String packet = "Rr" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_ALTER_GM_PACKET(final org.aestia.map.Map map, final Player perso) {
		final String packet = "GM|~" + perso.parseToGM();
		for (final Player z : map.getPersos()) {
			if (perso.get_size() > 0) {
				send(z, packet);
			} else {
				if (z.getGroupe() == null) {
					continue;
				}
				send(z, packet);
			}
		}
	}

	public static void GAME_SEND_Ee_PACKET(final Player perso, final char c, final String s) {
		final String packet = "Ee" + c + s;
		send(perso, packet);
	}

	public static void GAME_SEND_cC_PACKET(final Player perso, final char c, final String s) {
		final String packet = "cC" + c + s;
		send(perso, packet);
	}

	public static void GAME_SEND_ADD_NPC_TO_MAP(final org.aestia.map.Map map, final Npc npc) {
		for (final Player z : map.getPersos()) {
			send(z, "GM|" + npc.parse(false, z));
		}
	}

	public static void GAME_SEND_ADD_PERCO_TO_MAP(final org.aestia.map.Map map) {
		final String packet = "GM|" + Collector.parseGM(map);
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_GDO_PACKET_TO_MAP(final org.aestia.map.Map map, final char c, final int cell,
			final int itm, final int i) {
		final String packet = "GDO" + c + cell + ";" + itm + ";" + i;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_GDO_PACKET(final Player p, final char c, final int cell, final int itm, final int i) {
		final String packet = "GDO" + c + cell + ";" + itm + ";" + i;
		send(p, packet);
	}

	public static void GAME_SEND_ZC_PACKET(final Player p, final int a) {
		final String packet = "ZC" + a;
		send(p, packet);
	}

	public static void PACKET_POPUP_DEPART(final Player perso, final String str) {
		final String packet = "BAIO" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_GIP_PACKET(final Player p, final int a) {
		final String packet = "GIP" + a;
		send(p, packet);
	}

	public static void GAME_SEND_gn_PACKET(final Player p) {
		final String packet = "gn";
		send(p, packet);
	}

	public static void GAME_SEND_gC_PACKET(final Player p, final String s) {
		final String packet = "gC" + s;
		send(p, packet);
	}

	public static void GAME_SEND_gV_PACKET(final Player p) {
		final String packet = "gV";
		send(p, packet);
	}

	public static void GAME_SEND_gIM_PACKET(final Player p, final Guild g, final char c) {
		String packet = "gIM" + c;
		switch (c) {
		case '+': {
			packet = String.valueOf(packet) + g.parseMembersToGM();
			break;
		}
		}
		send(p, packet);
	}

	public static void GAME_SEND_gIB_PACKET(final Player p, final String infos) {
		final String packet = "gIB" + infos;
		send(p, packet);
	}

	public static void GAME_SEND_gIH_PACKET(final Player p, final String infos) {
		final String packet = "gIH" + infos;
		send(p, packet);
	}

	public static void GAME_SEND_gS_PACKET(final Player p, final Guild.GuildMember gm) {
		final StringBuilder packet = new StringBuilder();
		packet.append("gS").append(gm.getGuild().getName()).append("|")
				.append(gm.getGuild().getEmblem().replace(',', '|')).append("|").append(gm.parseRights());
		send(p, packet.toString());
	}

	public static void GAME_SEND_gJ_PACKET(final Player p, final String str) {
		final String packet = "gJ" + str;
		send(p, packet);
	}

	public static void GAME_SEND_gK_PACKET(final Player p, final String str) {
		final String packet = "gK" + str;
		send(p, packet);
	}

	public static void GAME_SEND_gIG_PACKET(final Player p, final Guild g) {
		final long xpMin = World.getExpLevel(g.getLvl()).guilde;
		long xpMax;
		if (World.getExpLevel(g.getLvl() + 1) == null) {
			xpMax = -1L;
		} else {
			xpMax = World.getExpLevel(g.getLvl() + 1).guilde;
		}
		final StringBuilder packet = new StringBuilder();
		packet.append("gIG").append(g.haveTenMembers() ? 1 : 0).append("|").append(g.getLvl()).append("|").append(xpMin)
				.append("|").append(g.getXp()).append("|").append(xpMax);
		send(p, packet.toString());
	}

	public static void REALM_SEND_MESSAGE(final GameClient out, final String args) {
		final String packet = "M" + args;
		send(out, packet);
	}

	public static void GAME_SEND_WC_PACKET(final Player perso) {
		final String packet = "WC" + perso.parseZaapList();
		send(perso.getGameClient(), packet);
	}

	public static void GAME_SEND_WV_PACKET(final Player out) {
		final String packet = "WV";
		send(out, packet);
	}

	public static void GAME_SEND_ZAAPI_PACKET(final Player perso, final String list) {
		final String packet = "Wc" + perso.getCurMap().getId() + "|" + list;
		send(perso, packet);
	}

	public static void GAME_SEND_CLOSE_ZAAPI_PACKET(final Player out) {
		final String packet = "Wv";
		send(out, packet);
	}

	public static void GAME_SEND_WUE_PACKET(final Player out) {
		final String packet = "WUE";
		send(out, packet);
	}

	public static void GAME_SEND_EMOTE_LIST(final Player perso, final String s) {
		send(perso, "eL" + s);
	}

	public static void GAME_SEND_NO_EMOTE(final Player out) {
		final String packet = "eUE";
		send(out, packet);
	}

	public static void REALM_SEND_TOO_MANY_PLAYER_ERROR(final GameClient out) {
		final String packet = "AlEw";
		send(out, packet);
	}

	public static void REALM_SEND_REQUIRED_APK(final GameClient out) {
		String pass = "";
		final String noms = "fantasy;mr;beau;fort;dark;knight;sword;big;boss;chuck;norris;wood;rick;roll;food;play;volt;rick;ven;bana;sam;ron;fou;pui;to;fu;lo;rien;bank;cap;chap;fort;dou;soleil;gentil;mechant;bad;killer;fight;gra;evil;dark;jerry;fatal;haut;bas;arc;epe;cac;ec;mai;invo;tro;com;koi;bou;let;top;fun;fai;sony;kani;meulou;faur;asus;choa;chau;cho;miel;beur;pain;cry;big;sma;to;day;bi;cih;geni;bou;che;scania;dave;swi;cas;que;chi;er;de;nul;do;a;b;c;d;e;f;g;h;i;j;k;l;m;n;o;p;q;r;s;t;u;v;w;x;y;z;a;e;i;o;u;y";
		final String[] str = noms.split(";");
		String rep = "";
		int tiree = 0;
		for (int maxi = (int) Math.floor(Math.random() * 4.0) + 2, x = 0; x < maxi; ++x) {
			rep = String.valueOf(rep) + str[(int) Math.floor(Math.random() * str.length)];
			if (maxi >= 3 && x == 0 && tiree == 0 && (int) Math.floor(Math.random() * 2.0) == 1) {
				rep = String.valueOf(rep) + "-";
				tiree = 1;
			}
		}
		rep = (pass = String.valueOf(rep.substring(0, 1).toUpperCase()) + rep.substring(1));
		final String packet = "APK" + pass;
		send(out, packet);
	}

	public static void GAME_SEND_ADD_ENEMY(final Player out, final Player pr) {
		final String packet = "iAK" + pr.getAccount().getName() + ";2;" + pr.getName() + ";36;10;0;100.FL.";
		send(out, packet);
	}

	public static void GAME_SEND_iAEA_PACKET(final Player out) {
		final String packet = "iAEA.";
		send(out, packet);
	}

	public static void GAME_SEND_ENEMY_LIST(final Player perso) {
		final String packet = "iL" + perso.getAccount().parseEnemyList();
		send(perso, packet);
	}

	public static void GAME_SEND_iD_COMMANDE(final Player perso, final String str) {
		final String packet = "iD" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_BWK(final Player perso, final String str) {
		final String packet = "BWK" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_KODE(final Player perso, final String str) {
		final String packet = "K" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_hOUSE(final Player perso, final String str) {
		final String packet = "h" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_FORGETSPELL_INTERFACE(final char sign, final Player perso) {
		final String packet = "SF" + sign;
		send(perso, packet);
	}

	public static void GAME_SEND_R_PACKET(final Player perso, final String str) {
		final String packet = "R" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_gIF_PACKET(final Player perso, final String str) {
		final String packet = "gIF" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_gITM_PACKET(final Player perso, final String str) {
		final String packet = "gITM" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_gITp_PACKET(final Player perso, final String str) {
		final String packet = "gITp" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_gITP_PACKET(final Player perso, final String str) {
		final String packet = "gITP" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_IH_PACKET(final Player perso, final String str) {
		final String packet = "IH" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_FLAG_PACKET(final Player perso, final Player cible) {
		final String packet = "IC" + cible.getCurMap().getX() + "|" + cible.getCurMap().getY();
		send(perso, packet);
	}

	public static void GAME_SEND_FLAG_PACKET(final Player perso, final org.aestia.map.Map CurMap) {
		final String packet = "IC" + CurMap.getX() + "|" + CurMap.getY();
		send(perso, packet);
	}

	public static void GAME_SEND_DELETE_FLAG_PACKET(final Player perso) {
		final String packet = "IC|";
		send(perso, packet);
	}

	public static void GAME_SEND_gT_PACKET(final Player perso, final String str) {
		final String packet = "gT" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_GUILDHOUSE_PACKET(final Player perso) {
		final String packet = "gUT";
		send(perso, packet);
	}

	public static void GAME_SEND_GUILDENCLO_PACKET(final Player perso) {
		final String packet = "gUF";
		send(perso, packet);
	}

	public static void GAME_SEND_EHm_PACKET(final Player out, final String sign, final String str) {
		final String packet = "EHm" + sign + str;
		send(out, packet);
	}

	public static void GAME_SEND_EHM_PACKET(final Player out, final String sign, final String str) {
		final String packet = "EHM" + sign + str;
		send(out, packet);
	}

	public static void GAME_SEND_EHP_PACKET(final Player out, final int templateID) {
		final String packet = "EHP" + templateID + "|" + World.getObjTemplate(templateID).getAvgPrice();
		send(out, packet);
	}

	public static void GAME_SEND_EHl(final Player out, final Hdv seller, final int templateID) {
		final String packet = "EHl" + seller.parseToEHl(templateID);
		send(out, packet);
	}

	public static void GAME_SEND_EHL_PACKET(final Player out, final int categ, final String templates) {
		final String packet = "EHL" + categ + "|" + templates;
		send(out, packet);
	}

	public static void GAME_SEND_EHL_PACKET(final Player out, final String items) {
		final String packet = "EHL" + items;
		send(out, packet);
	}

	public static void GAME_SEND_HDVITEM_SELLING(final Player perso) {
		String packet = "EL";
		final HdvEntry[] entries = perso.getAccount().getHdvItems(Math.abs(perso.get_isTradingWith()));
		boolean isFirst = true;
		HdvEntry[] array;
		for (int length = (array = entries).length, i = 0; i < length; ++i) {
			final HdvEntry curEntry = array[i];
			if (curEntry == null) {
				break;
			}
			if (!curEntry.buy) {
				if (!isFirst) {
					packet = String.valueOf(packet) + "|";
				}
				packet = String.valueOf(packet) + curEntry.parseToEL();
				isFirst = false;
			}
		}
		send(perso, packet);
	}

	public static void GAME_SEND_WEDDING(final org.aestia.map.Map c, final int action, final int homme, final int femme,
			final int parlant) {
		final String packet = "GA;" + action + ";" + homme + ";" + homme + "," + femme + "," + parlant;
		final Player Homme = World.getPersonnage(homme);
		send(Homme, packet);
	}

	public static void GAME_SEND_PF(final Player perso, final String str) {
		final String packet = "PF" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_MERCHANT_LIST(final Player P, final short mapID) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GM|");
		if (World.getSeller(P.getCurMap().getId()) == null) {
			return;
		}
		for (final Integer pID : World.getSeller(P.getCurMap().getId())) {
			if (!World.getPersonnage(pID).isOnline() && World.getPersonnage(pID).isShowSeller()) {
				packet.append("~").append(World.getPersonnage(pID).parseToMerchant()).append("|");
			}
		}
		if (packet.length() < 5) {
			return;
		}
		send(P, packet.toString());
	}

	public static void GAME_SEND_PACKET_TO_FIGHT(final Fight fight, final int i, final String packet) {
		for (final Fighter f : fight.getFighters(i)) {
			if (f.hasLeft()) {
				continue;
			}
			if (f.getPersonnage() == null) {
				continue;
			}
			if (!f.getPersonnage().isOnline()) {
				continue;
			}
			send(f.getPersonnage(), packet);
		}
	}

	public static void GAME_SEND_FIGHT_GJK_PACKET_TO_FIGHT(final Fight fight, final int teams, final int state,
			final int cancelBtn, final int duel, final int spec, final long time, final int type) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GJK").append(state).append("|");
		packet.append(cancelBtn).append("|").append(duel).append("|");
		packet.append(spec).append("|").append(time).append("|").append(type);
		for (final Fighter f : fight.getFighters(teams)) {
			if (f.hasLeft()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_GJK_PACKET(final Player out, final int state, final int cancelBtn, final int duel,
			final int spec, final long time, final int unknown) {
		final StringBuilder packet = new StringBuilder();
		packet.append("GJK").append(state).append("|").append(cancelBtn).append("|").append(duel).append("|")
				.append(spec).append("|").append(time).append("|").append(unknown);
		send(out, packet.toString());
	}

	public static void GAME_SEND_cMK_PACKET_INCARNAM_CHAT(final Player perso, final String suffix, final int guid,
			final String name, final String msg) {
		final String packet = "cMK" + suffix + "|" + guid + "|" + name + "|" + msg;
		if (perso.getLevel() > 15 && perso.getGroupe() == null) {
			GAME_SEND_BN(perso);
			return;
		}
		for (final Player perso2 : World.getOnlinePersos()) {
			if (perso2.getCurMap().getSubArea().getArea().get_id() == 45) {
				send(perso2, packet);
			}
		}
	}

	public static void GAME_SEND_Ag_PACKET(final GameClient out, final int idObjet, final String codObjet) {
		final String packet = "Ag1|" + idObjet + "|Cadeau Dofus| Voil\u00e0 un joli cadeau pour vous ! "
				+ "Un jeune aventurier comme vous sera sans servir de la meilleur fa\u00e7on ! "
				+ "Bonne continuation avec ceci ! |DOFUS|" + codObjet;
		send(out, packet);
	}

	public static void SEND_Ej_LIVRE(final Player pj, final String str) {
		final String packet = "Ej" + str;
		send(pj, packet);
	}

	public static void SEND_EW_METIER_PUBLIC(final Player pj, final String str) {
		final String packet = "EW" + str;
		send(pj, packet);
	}

	public static void SEND_EJ_LIVRE(final Player pj, final String str) {
		final String packet = "EJ" + str;
		send(pj, packet);
	}

	public static void SEND_GDF_PERSO(final Player perso, final int celda, final int frame, final int esInteractivo) {
		final String packet = "GDF|" + celda + ";" + frame + ";" + esInteractivo;
		send(perso, packet);
	}

	public static void SEND_EMK_MOVE_ITEM(final GameClient out, final char tipoOG, final String signo,
			final String s1) {
		String packet = "EMK" + tipoOG + signo;
		if (!s1.equals("")) {
			packet = String.valueOf(packet) + s1;
		}
		send(out, packet);
	}

	public static void SEND_OR_DELETE_ITEM(final GameClient out, final int id) {
		final String packet = "OR" + id;
		send(out, packet);
	}

	public static void GAME_SEND_CHALLENGE_FIGHT(final Fight fight, final int team, final String str) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gd").append(str);
		for (final Fighter fighter : fight.getFighters(team)) {
			if (fighter.hasLeft()) {
				continue;
			}
			if (fighter.getPersonnage() == null) {
				continue;
			}
			if (!fighter.getPersonnage().isOnline()) {
				continue;
			}
			send(fighter.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_CHALLENGE_PERSO(final Player p, final String str) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Gd").append(str);
		send(p, packet.toString());
	}

	public static void GAME_SEND_Im_PACKET_TO_CHALLENGE(final Fight fight, final int challenge, final String str) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Im").append(str);
		for (final Fighter fighter : fight.getFighters(challenge)) {
			if (fighter.hasLeft()) {
				continue;
			}
			if (fighter.getPersonnage() == null) {
				continue;
			}
			if (!fighter.getPersonnage().isOnline()) {
				continue;
			}
			send(fighter.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_Im_PACKET_TO_CHALLENGE_PERSO(final Player player, final String str) {
		final StringBuilder packet = new StringBuilder();
		packet.append("Im").append(str);
		send(player, packet.toString());
	}

	public static void GAME_SEND_MESSAGE_SERVER(final Player out, final String args) {
		final String packet = "M1" + args;
		send(out, packet);
	}

	public static void GAME_SEND_WELCOME(final Player perso) {
		final StringBuilder packet = new StringBuilder();
		packet.append("TB");
		send(perso, packet.toString());
	}

	public static void GAME_SEND_Eq_PACKET(final Player Personnage, final long Prix) {
		send(Personnage, "Eq1|1|" + Prix);
	}

	public static void GAME_SEND_INFO_HIGHLIGHT_PACKET(final Player perso, final String args) {
		final String packet = "IH" + args;
		send(perso, packet);
	}

	public static void GAME_SEND_GA_CLEAR_PACKET_TO_FIGHT(final Fight fight, final int teams) {
		final String packet = "GA;0";
		for (final Fighter f : fight.getFighters(teams)) {
			if (!f.hasLeft() && f.getPersonnage() != null) {
				if (!f.getPersonnage().isOnline()) {
					continue;
				}
				send(f.getPersonnage(), packet);
			}
		}
	}

	public static void SEND_MESSAGE_DECO(final Player P, final int MSG_ID, final String args) {
		final String packet = "M0" + MSG_ID + "|" + args;
		send(P, packet);
	}

	public static void SEND_MESSAGE_DECO_ALL(final int MSG_ID, final String args) {
		final String packet = "M0" + MSG_ID + "|" + args;
		for (final Player perso : World.getOnlinePersos()) {
			send(perso, packet);
		}
	}

	public static void PACKET_POPUP_ALL(final String msg) {
		final String packet = "BAIO" + msg;
		for (final Player P : World.getOnlinePersos()) {
			send(P, packet);
		}
	}

	public static void PACKET_POPUP(final Player perso, final String str) {
		final String packet = "BAIO" + str;
		send(perso, packet);
	}

	public static void SEND_gA_PERCEPTEUR(final Player perso, final String str) {
		final String packet = "gA" + str;
		send(perso, packet);
	}

	public static void SEND_Im1223_ALL(final String str) {
		final String packet = "Im1223;" + str;
		for (final Player perso : World.getOnlinePersos()) {
			send(perso, packet);
		}
	}

	public static void GAME_SEND_PERCO_INFOS_PACKET(final Player perso, final Collector perco, final String car) {
		final StringBuilder str = new StringBuilder();
		str.append("gA").append(car).append(perco.getN1()).append(",").append(perco.getN2()).append("|");
		str.append("-1").append("|");
		str.append(World.getMap(perco.getMap()).getX()).append("|").append(World.getMap(perco.getMap()).getY());
		send(perso, str.toString());
	}

	public static void SEND_Wp_MENU_Prisme(final Player perso) {
		final String packet = "Wp" + perso.parsePrismesList();
		send(perso.getGameClient(), packet);
	}

	public static void SEND_Ww_CLOSE_Prisme(final Player out) {
		final String packet = "Ww";
		send(out, packet);
	}

	public static void GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(final Player p, final String str) {
		final String packet = "am" + str;
		if (p == null || p.getAccount() == null) {
			return;
		}
		send(p, packet);
	}

	public static void SEND_CB_BONUS_CONQUETE(final Player pj, final String str) {
		final String packet = "CB" + str;
		send(pj, packet);
	}

	public static void SEND_Cb_BALANCE_CONQUETE(final Player pj, final String str) {
		final String packet = "Cb" + str;
		send(pj, packet);
	}

	public static void SEND_GM_PRISME_TO_MAP(final GameClient out, final org.aestia.map.Map Map) {
		final String packet = Map.getPrismeGMPacket();
		if (packet == "" || packet.isEmpty()) {
			return;
		}
		send(out, packet);
	}

	public static void GAME_SEND_PRISME_TO_MAP(final org.aestia.map.Map Map, final Prism Prisme) {
		final String packet = Prisme.getGMPrisme();
		for (final Player z : Map.getPersos()) {
			send(z, packet);
		}
	}

	public static void SEND_CP_INFO_DEFENSEURS_PRISME(final Player perso, final String str) {
		final String packet = "CP" + str;
		send(perso, packet);
	}

	public static void SEND_Cp_INFO_ATTAQUANT_PRISME(final Player perso, final String str) {
		final String packet = "Cp" + str;
		send(perso, packet);
	}

	public static void SEND_CIV_CLOSE_INFO_CONQUETE(final Player pj) {
		final String packet = "CIV";
		send(pj, packet);
	}

	public static void SEND_CW_INFO_WORLD_CONQUETE(final Player pj, final String str) {
		final String packet = "CW" + str;
		send(pj, packet);
	}

	public static void SEND_CIJ_INFO_JOIN_PRISME(final Player pj, final String str) {
		final String packet = "CIJ" + str;
		send(pj, packet);
	}

	public static void GAME_SEND_aM_ALIGN_PACKET_TO_AREA(final Player perso, final String str) {
		final String packet = "aM" + str;
		send(perso, packet);
	}

	public static void SEND_GA_ACTION_TO_Map(final org.aestia.map.Map Map, final String gameActionID,
			final int actionID, final String s1, final String s2) {
		String packet = "GA" + gameActionID + ";" + actionID + ";" + s1;
		if (!s2.equals("")) {
			packet = String.valueOf(packet) + ";" + s2;
		}
		for (final Player z : Map.getPersos()) {
			send(z, packet);
		}
	}

	public static void SEND_CS_SURVIVRE_MESSAGE_PRISME(final Player perso, final String str) {
		final String packet = "CS" + str;
		send(perso, packet);
	}

	public static void SEND_CD_MORT_MESSAGE_PRISME(final Player perso, final String str) {
		final String packet = "CD" + str;
		send(perso, packet);
	}

	public static void SEND_CA_ATTAQUE_MESSAGE_PRISME(final Player perso, final String str) {
		final String packet = "CA" + str;
		send(perso, packet);
	}

	public static void GAME_SEND_ACTION_TO_DOOR(final org.aestia.map.Map map, final int args, final boolean open) {
		String packet = "";
		if (open) {
			packet = "GDF|" + args + ";2";
		} else if (!open) {
			packet = "GDF|" + args + ";4";
		}
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_ACTION_TO_DOOR(final Player p, final int args, final boolean open) {
		String packet = "";
		if (open) {
			packet = "GDF|" + args + ";2";
		} else if (!open) {
			packet = "GDF|" + args + ";4";
		}
		send(p, packet);
	}

	public static void GAME_UPDATE_CELL(final org.aestia.map.Map map, final String args) {
		final String packet = "GDC" + args;
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_UPDATE_CELL(final Player p, final String args) {
		send(p, "GDC" + args);
	}

	public static void GAME_SEND_ACTION_TO_DOOR_FAST(final Player perso, final int args, final boolean open) {
		String packet = "";
		if (open) {
			packet = "GDF|" + args + ";3";
		} else if (!open) {
			packet = "GDF|" + args + ";1";
		}
		send(perso, packet);
	}

	public static void GAME_UPDATE_CELL_FAST(final Player perso, final String args) {
		final String packet = "GDC" + args;
		send(perso, packet);
	}

	public static void GAME_SEND_ACTION_TO_DOOR_PEUR(final org.aestia.map.Map map, final boolean open) {
		String packet = "";
		if (open) {
			packet = "GDF|294;2|309;2|324;2|339;2|323;2|338;2|353;2|337;2|352;2|367;2|336;2|351;2|366;2|381;2|365;2|380;2|395;2|379;2|394;2|409;2";
		} else if (!open) {
			packet = "GDF|294;4;0|336;4;0|309;4;0|324;4;0|339;4;0|323;4;0|338;4;0|353;4;0|337;4;0|352;4;0|367;4;0|351;4;0|366;4;0|381;4;0|365;4;0|380;4;0|395;4;0|379;4;0|394;4;0|409;4;0";
		}
		for (final Player z : map.getPersos()) {
			send(z, packet);
		}
	}

	public static void GAME_SEND_ACTION_TO_DOOR_FAST_PEUR(final Player perso, final boolean open) {
		String packet = "";
		if (open) {
			packet = "GDF|294;3|309;3|324;3|339;3|323;3|338;3|353;3|337;3|352;3|367;3|336;3|351;3|366;3|381;3|365;3|380;3|395;3|379;3|394;3|409;3";
		} else if (!open) {
			packet = "GDF|294;1;0|336;1;0|309;1;0|324;1;0|339;1;0|323;1;0|338;1;0|353;1;0|337;1;0|352;1;0|367;1;0|351;1;0|366;1;0|381;1;0|365;1;0|380;1;0|395;1;0|379;1;0|394;1;0|409;1;0";
		}
		send(perso, packet);
	}

	public static void GAME_SEND_ALE_PACKET(final GameClient out, final String caract) {
		final String packet = "AlE" + caract;
		send(out, packet);
	}

	public static void GAME_SEND_FIGHT_PLAYER_JOIN(final Player perso, final Fighter f) {
		final String packet = f.getGmPacket('+');
		send(perso, packet);
	}

	public static void GAME_SEND_FIGHT_OBJECTS_GDS_PACKETS(final org.aestia.map.Map map, final Fight fight) {
		final String packet = map.getObjectsGDsPacketsInFight(true);
		if (packet.equals("")) {
			return;
		}
		for (final Fighter f : fight.getFighters(1)) {
			if (f.hasLeft()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
		for (final Fighter f : fight.getFighters(2)) {
			if (f.hasLeft()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
	}

	public static void GAME_SEND_FIGHT_OBJECTS_GDS_END_PACKETS(final org.aestia.map.Map map, final Fight fight) {
		final String packet = map.getObjectsGDsPacketsInFight(false);
		if (packet.equals("")) {
			return;
		}
		for (final Fighter f : fight.getFighters(1)) {
			if (f.hasLeft()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
		for (final Fighter f : fight.getFighters(2)) {
			if (f.hasLeft()) {
				continue;
			}
			send(f.getPersonnage(), packet.toString());
		}
	}

	public static void QuestList(final GameClient out, final Player perso) {
		final String packet = "QL" + perso.getQuestGmPacket();
		send(out, packet);
	}

	public static void QuestGep(final GameClient out, final Quest quest, final Player perso) {
		final String packet = "QS" + quest.getGmQuestDataPacket(perso);
		send(out, packet);
	}

	public static void sendPacketToMap(final org.aestia.map.Map map, final String packet) {
		for (final Player perso : map.getPersos()) {
			send(perso, packet);
		}
	}

	public static void sendPacketToMapGM(final org.aestia.map.Map map, final Npc npc) {
		for (final Player perso : map.getPersos()) {
			send(perso, "GM|" + npc.parse(true, perso));
		}
	}
}
