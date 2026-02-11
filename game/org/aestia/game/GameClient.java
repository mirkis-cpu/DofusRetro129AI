// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.client.other.Group;
import org.aestia.command.Admin;
import org.aestia.command.Command;
import org.aestia.command.CommandPlayer;
import org.aestia.command.server.Groupes;
import org.aestia.common.ConditionParser;
import org.aestia.common.CryptManager;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.dynamic.Migration;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.Pet;
import org.aestia.entity.PetEntry;
import org.aestia.entity.Prism;
import org.aestia.entity.exchange.CraftSecure;
import org.aestia.entity.exchange.PlayerExchange;
import org.aestia.entity.npc.Npc;
import org.aestia.entity.npc.NpcAnswer;
import org.aestia.entity.npc.NpcQuestion;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.Spell;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.hdv.Hdv;
import org.aestia.hdv.HdvEntry;
import org.aestia.hdv.HdvLine;
import org.aestia.job.Job;
import org.aestia.job.JobConstant;
import org.aestia.job.JobStat;
import org.aestia.job.maging.Rune;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.Case;
import org.aestia.map.InteractiveObject;
import org.aestia.map.MountPark;
import org.aestia.object.ObjectTemplate;
import org.aestia.object.entity.Fragment;
import org.aestia.other.Action;
import org.aestia.other.Dopeul;
import org.aestia.other.Guild;
import org.aestia.other.House;
import org.aestia.other.Trunk;
import org.aestia.other.Tutorial;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;
import org.apache.mina.core.session.IoSession;

public class GameClient {
	private IoSession session;
	private Account compte;
	private Player perso;
	private Map<Integer, GameAction> actions;
	private long timeLastTradeMsg;
	private long timeLastRecrutmentMsg;
	private long timeLastAlignMsg;
	private long timeLastChatMsg;
	private long timeLastIncarnamMsg;
	private boolean walk;
	private Command command;
	private TimerWaiter waiter;

	public GameClient(final IoSession session) {
		this.actions = new TreeMap<Integer, GameAction>();
		this.timeLastTradeMsg = 0L;
		this.timeLastRecrutmentMsg = 0L;
		this.timeLastAlignMsg = 0L;
		this.timeLastChatMsg = 0L;
		this.timeLastIncarnamMsg = 0L;
		this.walk = false;
		this.waiter = new TimerWaiter();
		this.session = session;
		SocketManager.GAME_SEND_HELLOGAME_PACKET(this);
		Main.gameServer.newClient();
	}

	public void parsePacket(final String packet) throws InterruptedException {
		if (packet.length() > 3 && packet.substring(0, 4).equalsIgnoreCase("ping")) {
			SocketManager.GAME_SEND_PONG(this);
			return;
		}
		switch (packet.charAt(0)) {
		case 'A': {
			this.parseAccountPacket(packet);
			break;
		}
		case 'B': {
			this.parseBasicsPacket(packet);
			break;
		}
		case 'C': {
			this.parseConquestPacket(packet);
			break;
		}
		case 'c': {
			this.parseChanelPacket(packet);
			break;
		}
		case 'D': {
			this.parseDialogPacket(packet);
			break;
		}
		case 'd': {
			this.parseDocumentPacket(packet);
			break;
		}
		case 'E': {
			this.parseExchangePacket(packet);
			break;
		}
		case 'e': {
			this.parseEnvironementPacket(packet);
			break;
		}
		case 'F': {
			this.parseFrienDDacket(packet);
			break;
		}
		case 'f': {
			this.parseFightPacket(packet);
			break;
		}
		case 'G': {
			this.parseGamePacket(packet);
			break;
		}
		case 'g': {
			this.parseGuildPacket(packet);
			break;
		}
		case 'h': {
			this.parseHousePacket(packet);
			break;
		}
		case 'i': {
			this.parseEnemyPacket(packet);
			break;
		}
		case 'J': {
			this.parseJobOption(packet);
			break;
		}
		case 'K': {
			this.parseHouseKodePacket(packet);
			break;
		}
		case 'O': {
			this.parseObjectPacket(packet);
			break;
		}
		case 'P': {
			this.parseGroupPacket(packet);
			break;
		}
		case 'R': {
			this.parseMountPacket(packet);
			break;
		}
		case 'Q': {
			this.parseQuestData(packet);
			break;
		}
		case 'S': {
			this.parseSpellPacket(packet);
			break;
		}
		case 'T': {
			this.parseFoireTroll(packet);
			break;
		}
		case 'W': {
			this.parseWaypointPacket(packet);
			break;
		}
		}
	}

	private void parseAccountPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			this.addCharacter(packet);
			break;
		}
		case 'B': {
			this.boost(packet);
			break;
		}
		case 'D': {
			this.deleteCharacter(packet);
			break;
		}
		case 'f': {
			this.getQueuePosition(packet);
			break;
		}
		case 'g': {
			this.getGifts();
			break;
		}
		case 'G': {
			this.attributeGiftToCharacter(packet.substring(2));
			break;
		}
		case 'i': {
			this.sendIdentity(packet);
			break;
		}
		case 'L': {
			this.getCharacters(packet.length() == 2);
			break;
		}
		case 'M': {
			this.parseMigration(packet);
			break;
		}
		case 'S': {
			this.setCharacter(packet);
			break;
		}
		case 'T': {
			this.sendTicket(packet);
			break;
		}
		case 'V': {
			this.requestRegionalVersion();
			break;
		}
		case 'P': {
			SocketManager.REALM_SEND_REQUIRED_APK(this);
			break;
		}
		}
	}

	private void parseMigration(final String packet) {
		switch (packet.charAt(2)) {
		case '-': {
			try {
				final Migration migration = Migration.migrations.get(this.compte.getGuid());
				if (migration == null) {
					return;
				}
				final int player = Integer.parseInt(packet.substring(3));
				final int server = migration.search(player);
				Main.exchangeClient.send("MD" + player + "|" + server);
				this.waiter.addNext(new Runnable() {
					@Override
					public void run() {
						migration.getPlayers().clear();
						Main.exchangeClient.send("MP" + GameClient.this.compte.getGuid());
					}
				}, 1500L);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		default: {
			try {
				final Migration migration = Migration.migrations.get(this.compte.getGuid());
				if (migration == null) {
					return;
				}
				final String[] split = packet.substring(2).split("\\;");
				final String name = split[1];
				if (Database.getStatique().getPlayerData().exist(name)) {
					SocketManager.GAME_SEND_NAME_ALREADY_EXIST(this);
					return;
				}
				boolean isValid = true;
				int tiretCount = 0;
				char exLetterA = ' ';
				char exLetterB = ' ';
				char[] charArray;
				for (int length = (charArray = name.toCharArray()).length, i = 0; i < length; ++i) {
					final char curLetter = charArray[i];
					if ((curLetter < 'a' || curLetter > 'z') && curLetter != '-') {
						isValid = false;
						break;
					}
					if (curLetter == exLetterA && curLetter == exLetterB) {
						isValid = false;
						break;
					}
					if (curLetter >= 'a' && curLetter <= 'z') {
						exLetterA = exLetterB;
						exLetterB = curLetter;
					}
					if (curLetter == '-') {
						if (tiretCount >= 1) {
							isValid = false;
							break;
						}
						++tiretCount;
					}
				}
				if (!isValid) {
					SocketManager.GAME_SEND_NAME_ALREADY_EXIST(this);
					return;
				}
				final int server2 = migration.search(Integer.parseInt(split[0]));
				Main.exchangeClient.send("MO" + split[0] + "|" + server2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		}
	}

	private void addCharacter(final String packet) {
		final String[] infos = packet.substring(2).split("\\|");
		if (Database.getStatique().getPlayerData().exist(infos[0])) {
			SocketManager.GAME_SEND_NAME_ALREADY_EXIST(this);
			return;
		}
		boolean isValid = true;
		final String name = infos[0].toLowerCase();
		if (name.length() > 20 || name.length() < 3 || name.contains("modo") || name.contains("admin")
				|| name.contains("putain") || name.contains("administrateur") || name.contains("puta")) {
			isValid = false;
		}
		if (isValid) {
			int tiretCount = 0;
			char exLetterA = ' ';
			char exLetterB = ' ';
			char[] charArray;
			for (int length = (charArray = name.toCharArray()).length, i = 0; i < length; ++i) {
				final char curLetter = charArray[i];
				if ((curLetter < 'a' || curLetter > 'z') && curLetter != '-') {
					isValid = false;
					break;
				}
				if (curLetter == exLetterA && curLetter == exLetterB) {
					isValid = false;
					break;
				}
				if (curLetter >= 'a' && curLetter <= 'z') {
					exLetterA = exLetterB;
					exLetterB = curLetter;
				}
				if (curLetter == '-') {
					if (tiretCount >= 1) {
						isValid = false;
						break;
					}
					++tiretCount;
				}
			}
		}
		if (!isValid) {
			SocketManager.GAME_SEND_NAME_ALREADY_EXIST(this);
			return;
		}
		if (this.compte.getNumberOfPersos() >= 5) {
			SocketManager.GAME_SEND_CREATE_PERSO_FULL(this);
			return;
		}
		if (this.compte.createPerso(infos[0], Integer.parseInt(infos[2]), Integer.parseInt(infos[1]),
				Integer.parseInt(infos[3]), Integer.parseInt(infos[4]), Integer.parseInt(infos[5]))) {
			SocketManager.GAME_SEND_CREATE_OK(this);
			SocketManager.GAME_SEND_PERSO_LIST(this, this.compte.getPersos(), this.compte.getSubscribeRemaining());
			SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(World.getMap((short) 10114), "", -1, "Guide de bienvenue",
					"Bienvenue \u00e0 " + infos[0] + " qui viens de nous rejoindre !");
		} else {
			SocketManager.GAME_SEND_CREATE_FAILED(this);
		}
	}

	private void boost(final String packet) {
		try {
			int stat = -1;
			if (this.perso.getMorphMode()) {
				this.perso.sendMessage(
						"Vous \u00eates incarn\u00e9, vous ne pouvez donc pas vous ajout\u00e9 de point de caract\u00e9ristique !");
				return;
			}
			if (packet.substring(2).contains(";")) {
				stat = Integer.parseInt(packet.substring(2).split(";")[0]);
				if (stat > 0) {
					int code = 0;
					code = Integer.parseInt(packet.substring(2).split(";")[1]);
					if (code < 0) {
						return;
					}
					if (this.perso.get_capital() < code) {
						code = this.perso.get_capital();
					}
					this.perso.boostStatFixedCount(stat, code);
				}
			} else {
				stat = Integer.parseInt(packet.substring(2).split("/u000A")[0]);
				this.perso.boostStat(stat, true);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void deleteCharacter(final String packet) {
		final String[] split = packet.substring(2).split("\\|");
		final int GUID = Integer.parseInt(split[0]);
		final String reponse = (split.length > 1) ? split[1] : "";
		if (this.compte.getPersos().containsKey(GUID)) {
			if (this.compte.getPersos().get(GUID).getLevel() < 20 || (this.compte.getPersos().get(GUID).getLevel() >= 20
					&& reponse.equals(this.compte.getAwnser().replace(" ", "%20")))) {
				this.compte.deletePerso(GUID);
				SocketManager.GAME_SEND_PERSO_LIST(this, this.compte.getPersos(), this.compte.getSubscribeRemaining());
			} else {
				SocketManager.GAME_SEND_DELETE_PERSO_FAILED(this);
			}
		} else {
			SocketManager.GAME_SEND_DELETE_PERSO_FAILED(this);
		}
	}

	private void getQueuePosition(final String packet) {
		final int queueID = 1;
		final int position = 1;
		SocketManager.MULTI_SEND_Af_PACKET(this, position, 1, 1, "1", queueID);
	}

	private void getGifts() {
		final String gifts = Database.getStatique().getGiftData().getByAccount(this.compte.getGuid());
		if (gifts == null) {
			return;
		}
		if (!gifts.isEmpty()) {
			String data = "";
			int item = -1;
			String[] split;
			for (int length = (split = gifts.split("\\;")).length, i = 0; i < length; ++i) {
				final String object = split[i];
				final int id = Integer.parseInt(object.split("\\,")[0]);
				final int qua = Integer.parseInt(object.split("\\,")[1]);
				if (data.isEmpty()) {
					data = "1~" + Integer.toString(id, 16) + "~" + Integer.toString(qua, 16) + "~~"
							+ World.getObjTemplate(id).getStrTemplate();
				} else {
					data = String.valueOf(data) + ";1~" + Integer.toString(id, 16) + "~" + Integer.toString(qua, 16)
							+ "~~" + World.getObjTemplate(id).getStrTemplate();
				}
				if (item == -1) {
					item = id;
				}
			}
			SocketManager.GAME_SEND_Ag_PACKET(this, item, data);
		}
	}

	private void attributeGiftToCharacter(final String packet) {
		final String[] infos = packet.split("\\|");
		final int template = Integer.parseInt(infos[0]);
		final Player player = World.getPersonnage(Integer.parseInt(infos[1]));
		if (player == null) {
			return;
		}
		String gifts = Database.getStatique().getGiftData().getByAccount(this.compte.getGuid());
		if (gifts.isEmpty()) {
			return;
		}
		String[] split2;
		for (int length = (split2 = gifts.split("\\;")).length, i = 0; i < length; ++i) {
			final String data = split2[i];
			final String[] split = data.split("\\,");
			final int id = Integer.parseInt(split[0]);
			if (id == template) {
				final int qua = Integer.parseInt(split[1]);
				final int jp = Integer.parseInt(split[2]);
				org.aestia.object.Object obj = null;
				if (qua == 1) {
					obj = World.getObjTemplate(template).createNewItem(qua, jp == 1);
					if (player.addObjet(obj, true)) {
						World.addObjet(obj, true);
					}
					final String str1 = String.valueOf(id) + "," + qua + "," + jp;
					final String str2 = String.valueOf(id) + "," + qua + "," + jp + ";";
					final String str3 = ";" + id + "," + qua + "," + jp;
					gifts = gifts.replace(str2, "").replace(str3, "").replace(str1, "");
				} else {
					obj = World.getObjTemplate(template).createNewItem(1, jp == 1);
					if (player.addObjet(obj, true)) {
						World.addObjet(obj, true);
					}
					final String str1 = String.valueOf(id) + "," + qua + "," + jp;
					final String str2 = String.valueOf(id) + "," + qua + "," + jp + ";";
					final String str3 = ";" + id + "," + qua + "," + jp;
					final String cstr1 = String.valueOf(id) + "," + (qua - 1) + "," + jp;
					final String cstr2 = String.valueOf(id) + "," + (qua - 1) + "," + jp + ";";
					final String cstr3 = ";" + id + "," + (qua - 1) + "," + jp;
					gifts = gifts.replace(str2, cstr2).replace(str3, cstr3).replace(str1, cstr1);
				}
				Database.getStatique().getGiftData().update(player.getAccID(), gifts);
			}
		}
		Database.getStatique().getPlayerData().update(player, true);
		if (gifts.isEmpty()) {
			player.send("AG");
		} else {
			this.getGifts();
			player.send("AG");
		}
	}

	private void sendIdentity(final String packet) {
		this.compte.setKey(packet.substring(2));
	}

	private void getCharacters(final boolean force) {
		this.compte.setGameClient(this);
		for (final Player p : this.compte.getPersos().values()) {
			if (p != null && p.get_fight() != null && p.get_fight().getFighterByPerso(p) != null) {
				this.perso = p;
				if (this.perso != null) {
					this.perso.OnJoinGame();
					return;
				}
				continue;
			}
		}
		SocketManager.GAME_SEND_PERSO_LIST(this, this.compte.getPersos(), this.compte.getSubscribeRemaining());
	}

	private void setCharacter(final String packet) {
		final int charID = Integer.parseInt(packet.substring(2));
		if (this.compte.getPersos().get(charID) != null) {
			this.compte.setGameClient(this);
			this.perso = this.compte.getPersos().get(charID);
			if (this.perso != null) {
				this.perso.OnJoinGame();
				return;
			}
		}
		SocketManager.GAME_SEND_PERSO_SELECTION_FAILED(this);
	}

	private void sendTicket(final String packet) {
		try {
			final int id = Integer.parseInt(packet.substring(2));
			this.compte = Main.gameServer.getWaitingCompte(id);
			if (this.compte == null) {
				SocketManager.GAME_SEND_ATTRIBUTE_FAILED(this);
				this.kick();
			} else {
				final String ip = this.session.getRemoteAddress().toString().substring(1).split("\\:")[0];
				this.compte.setGameClient(this);
				this.compte.setCurIP(ip);
				Main.gameServer.delWaitingCompte(this.compte);
				Database.getStatique().getPlayerData().loadByAccountId(this.compte.getGuid());
				SocketManager.GAME_SEND_ATTRIBUTE_SUCCESS(this);
			}
		} catch (Exception e) {
			this.kick();
		}
	}

	private void requestRegionalVersion() {
		SocketManager.GAME_SEND_AV0(this);
	}

	private void parseBasicsPacket(final String packet) throws InterruptedException {
		switch (packet.charAt(1)) {
		case 'A': {
			this.autorisedCommand(packet);
			break;
		}
		case 'D': {
			this.getDate();
			break;
		}
		case 'M': {
			if (packet.charAt(2) == '*') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == '#') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == '$') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == '%') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == '!') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == '?') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == ':') {
				this.send(packet);
				break;
			}
			if (packet.charAt(2) == '�') {
				if (this.perso.getGroupe() == null) {
					this.send(packet);
					break;
				}
				if (!packet.contains("|")) {
					break;
				}
				String suffix = this.replaceString(packet.replace('|', ' ').substring(4));
				if (!suffix.startsWith("MAP ")) {
					return;
				}
				suffix = suffix.substring(4);
				if (suffix.contains("°0 ")) {
					return;
				}
				final String prefix = "<i>Map</i> - <b><a href='asfunction:onHref,ShowPlayerPopupMenu,"
						+ this.getPersonnage().getName() + "'>[" + this.perso.getGroupe().getNom() + "] "
						+ this.getPersonnage().getName() + "</a></b>~";
				SocketManager.GAME_SEND_Im_PACKET_TO_MAP(this.perso.getCurMap(), "116;" + prefix + suffix);
				break;
			} else {
				if (packet.charAt(2) == '@') {
					this.send(packet);
					break;
				}
				final Groupes g = this.perso.getGroupe();
				if (g == null) {
					this.send(packet);
					break;
				}
				if (g.isPlayer()) {
					this.send(packet);
					break;
				}
				if (!packet.contains("|")) {
					break;
				}
				String pseudo = "";
				String message = "";
				try {
					pseudo = packet.substring(2).split("\\|")[0];
					message = packet.substring(3 + pseudo.length());
					message = this.replaceString(message.substring(0, message.length() - 1));
					if (message.contains("°0|")) {
						SocketManager.GAME_SEND_MESSAGE(this.getPersonnage(),
								"N'envoyez pas d'objet en message priv\u00e9e !");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (pseudo.equalsIgnoreCase("") || message.equalsIgnoreCase("")) {
					return;
				}
				final Player P = World.getPersoByName(pseudo);
				if (P == null || !P.isOnline()) {
					SocketManager.GAME_SEND_CHAT_ERROR_PACKET(this, pseudo);
					return;
				}
				String prefix2 = "";
				if (this.getPersonnage().getGroupe() != null) {
					prefix2 = "<i>de</i> <b><a href='asfunction:onHref,ShowPlayerPopupMenu,"
							+ this.getPersonnage().getName() + "'>[" + this.perso.getGroupe().getNom() + "] "
							+ this.getPersonnage().getName() + "</a></b>";
				} else {
					prefix2 = "<i>de</i> <b><a href='asfunction:onHref,ShowPlayerPopupMenu,"
							+ this.getPersonnage().getName() + "'>" + this.getPersonnage().getName() + "</a></b>";
				}
				SocketManager.GAME_SEND_Im_PACKET_TO_PLAYER(P, "116;" + prefix2 + "~" + message);
				if (P.getGroupe() != null) {
					prefix2 = "<i>\u00e0</i> <b><a href='asfunction:onHref,ShowPlayerPopupMenu," + pseudo + "'>["
							+ P.getGroupe().getNom() + "] " + pseudo + "</a></b>";
				} else {
					prefix2 = "<i>\u00e0</i> <b><a href='asfunction:onHref,ShowPlayerPopupMenu," + pseudo + "'>"
							+ pseudo + "</a></b>";
				}
				SocketManager.GAME_SEND_Im_PACKET_TO_PLAYER(this.getPersonnage(), "116;" + prefix2 + "~" + message);
				break;
			}
		}
		case 'W': {
			this.whoIs(packet);
			break;
		}
		case 'S': {
			this.perso.useSmiley(packet.substring(2));
			break;
		}
		case 'Y': {
			this.chooseState(packet);
			break;
		}
		case 'a': {
			if (packet.charAt(2) == 'M') {
				this.goToMap(packet);
				break;
			}
			break;
		}
		}
	}

	private String replaceString(String packet) {
		if (packet.contains("&lt;") && (!packet.contains("&gt;") || !packet.contains("&lt;/"))) {
			packet = packet.replace("&lt;", "").replace("&gt;", "");
		}
		if (packet.contains("&lt;") && packet.contains("&gt;") && !packet.contains("&lt;/")) {
			packet = packet.replace("&lt;", "").replace("&gt;", "");
		}
		return packet.replace("&lt;", "<").replace("&gt;", ">");
	}

	private void autorisedCommand(final String packet) throws InterruptedException {
		final Player perso = this.getPersonnage();
		if (this.command == null) {
			this.command = Admin.get(perso);
		}
		if (perso.getGroupe() == null || this.getPersonnage() == null) {
			this.getAccount().getGameClient().kick();
			return;
		}
		this.command.apply(packet);
	}

	private void getDate() {
		SocketManager.GAME_SEND_SERVER_HOUR(this);
	}

	private void send(String packet) {
		String msg = "";
		String lastMsg = "";
		if (this.perso.isMuted()) {
			if (this.perso.getAccount() != null) {
				this.perso.getAccount().sendMutedIm();
			}
			return;
		}
		if (this.perso.getCurMap() != null && this.perso.getCurMap().isMute() && this.perso.getGroupe() == null) {
			SocketManager.PACKET_POPUP(this.perso, "Shh..<br/>\nLa Map o\u00f9 tu te trouve est temporairement mute !");
			return;
		}
		packet = packet.replace("<", "");
		packet = packet.replace(">", "");
		if (packet.length() < 6) {
			return;
		}
		switch (packet.charAt(2)) {
		case '*': {
			long c;
			if ((c = System.currentTimeMillis() - this.timeLastChatMsg) < 500L) {
				c = (500L - c) / 1000L;
				SocketManager.PACKET_POPUP(this.perso, "Shh..<br/>\nNe fais pas de spam!");
				return;
			}
			this.timeLastChatMsg = System.currentTimeMillis();
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			msg = packet.split("\\|", 2)[1];
			if (CommandPlayer.analyse(this.perso, msg)) {
				return;
			}
			if (msg == lastMsg) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "184");
				return;
			}
			if (this.perso.isSpec() && this.perso.get_fight() != null) {
				final int team = this.perso.get_fight().getTeamId(this.perso.getId());
				if (team == -1) {
					return;
				}
				SocketManager.GAME_SEND_cMK_PACKET_TO_FIGHT(this.perso.get_fight(), team, "#", this.perso.getId(),
						this.perso.getName(), msg);
				return;
			} else {
				if (this.perso.getObjetByPos(21) != null
						&& this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
					msg = Formulas.translateMsg(msg);
				}
				if (this.perso.get_fight() == null) {
					SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(this.perso.getCurMap(), "", this.perso.getId(),
							this.perso.getName(), msg);
					break;
				}
				SocketManager.GAME_SEND_cMK_PACKET_TO_FIGHT(this.perso.get_fight(), 7, "", this.perso.getId(),
						this.perso.getName(), msg);
				break;
			}
		}
		case '^': {
			msg = packet.split("\\|", 2)[1];
			long x;
			if ((x = System.currentTimeMillis() - this.timeLastIncarnamMsg) < 30000L) {
				x = (30000L - x) / 1000L;
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "0115;" + ((int) Math.ceil(x) + 1));
				return;
			}
			if (msg == lastMsg) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "184");
				return;
			}
			if (this.perso.getLevel() > 150) {
				return;
			}
			this.timeLastIncarnamMsg = System.currentTimeMillis();
			msg = (lastMsg = packet.split("\\|", 2)[1]);
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_INCARNAM_CHAT(this.perso, "^", this.perso.getId(), this.perso.getName(),
					msg);
			break;
		}
		case '#': {
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			if (this.perso.get_fight() == null) {
				break;
			}
			msg = packet.split("\\|", 2)[1];
			final int team2 = this.perso.get_fight().getTeamId(this.perso.getId());
			if (team2 == -1) {
				return;
			}
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_TO_FIGHT(this.perso.get_fight(), team2, "#", this.perso.getId(),
					this.perso.getName(), msg);
			break;
		}
		case '$': {
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			if (this.perso.getGroup() == null) {
				break;
			}
			msg = packet.split("\\|", 2)[1];
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_TO_GROUP(this.perso.getGroup(), "$", this.perso.getId(),
					this.perso.getName(), msg);
			break;
		}
		case ':': {
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			if (this.perso.cantCanal()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso, "Vous n'avez pas la permission de parler dans ce canal !",
						"B9121B");
				break;
			}
			if (this.perso.isInPrison()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Vous \u00eates en prison, impossible de parler dans ce canal !", "B9121B");
				break;
			}
			long l;
			if (this.perso.getGroupe() == null && (l = System.currentTimeMillis() - this.timeLastTradeMsg) < 50000L) {
				l = (50000L - l) / 1000L;
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "0115;" + ((int) Math.ceil(l) + 1));
				return;
			}
			this.timeLastTradeMsg = System.currentTimeMillis();
			msg = packet.split("\\|", 2)[1];
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_TO_ALL(this.perso, ":", this.perso.getId(), this.perso.getName(), msg);
			break;
		}
		case '@': {
			if (this.perso.getGroupe() == null) {
				return;
			}
			msg = packet.split("\\|", 2)[1];
			SocketManager.GAME_SEND_cMK_PACKET_TO_ADMIN("@", this.perso.getId(), this.perso.getName(), msg);
			break;
		}
		case '?': {
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			if (this.perso.cantCanal()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso, "Vous n'avez pas la permission de parler dans ce canal !",
						"B9121B");
				break;
			}
			if (this.perso.isInPrison()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Vous \u00eates en prison, impossible de parler dans ce canal !", "B9121B");
				break;
			}
			long j;
			if (this.perso.getGroupe() == null
					&& (j = System.currentTimeMillis() - this.timeLastRecrutmentMsg) < 40000L) {
				j = (40000L - j) / 1000L;
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "0115;" + ((int) Math.ceil(j) + 1));
				return;
			}
			this.timeLastRecrutmentMsg = System.currentTimeMillis();
			msg = packet.split("\\|", 2)[1];
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_TO_ALL(this.perso, "?", this.perso.getId(), this.perso.getName(), msg);
			break;
		}
		case '%': {
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			if (this.perso.get_guild() == null) {
				return;
			}
			msg = packet.split("\\|", 2)[1];
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_TO_GUILD(this.perso.get_guild(), "%", this.perso.getId(),
					this.perso.getName(), msg);
			break;
		}
		case '!': {
			if (!this.perso.get_canaux().contains(new StringBuilder(String.valueOf(packet.charAt(2))).toString())) {
				return;
			}
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			if (this.perso.get_align() == 0) {
				return;
			}
			if (this.perso.getDeshonor() >= 1) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "183");
				return;
			}
			long k;
			if ((k = System.currentTimeMillis() - this.timeLastAlignMsg) < 30000L) {
				k = (30000L - k) / 1000L;
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "0115;" + ((int) Math.ceil(k) + 1));
				return;
			}
			this.timeLastAlignMsg = System.currentTimeMillis();
			msg = packet.split("\\|", 2)[1];
			if (this.perso.getObjetByPos(21) != null && this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
				msg = Formulas.translateMsg(msg);
			}
			SocketManager.GAME_SEND_cMK_PACKET_TO_ALIGN("!", this.perso.getId(), this.perso.getName(), msg, this.perso);
			break;
		}
		default: {
			final String nom = packet.substring(2).split("\\|")[0];
			msg = packet.split("\\|", 2)[1];
			if (nom.length() <= 1) {
				GameServer.addToLog("ChatHandler: Chanel non gere : " + nom);
				break;
			}
			final Player target = World.getPersoByName(nom);
			if (target == null) {
				if (this.perso.getGroupe() != null) {
					SocketManager.GAME_SEND_MESSAGE(this.perso, "Target : " + target);
				}
				SocketManager.GAME_SEND_CHAT_ERROR_PACKET(this, nom);
				return;
			}
			if (target.getAccount() == null) {
				if (this.perso.getGroupe() != null) {
					SocketManager.GAME_SEND_MESSAGE(this.perso, "Account : " + target.getAccount());
				}
				SocketManager.GAME_SEND_CHAT_ERROR_PACKET(this, nom);
				return;
			}
			if (target.getGameClient() == null) {
				SocketManager.GAME_SEND_CHAT_ERROR_PACKET(this, nom);
				return;
			}
			if (target.getAccount().isEnemyWith(this.perso.getAccount().getGuid()) || !target.isDispo(this.perso)) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "114;" + target.getName());
				return;
			}
			if (msg == lastMsg) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "184");
				return;
			}
			if (this.perso.getGroupe() == null && target.isInvisible()) {
				SocketManager.GAME_SEND_CHAT_ERROR_PACKET(this, nom);
				return;
			}
			if (!target.mpToTp) {
				if (this.perso.getObjetByPos(21) != null
						&& this.perso.getObjetByPos(21).getTemplate().getId() == 10844) {
					msg = Formulas.translateMsg(msg);
				}
				SocketManager.GAME_SEND_cMK_PACKET(target, "F", this.perso.getId(), this.perso.getName(), msg);
				SocketManager.GAME_SEND_cMK_PACKET(this.perso, "T", target.getId(), target.getName(), msg);
				break;
			}
			if (this.perso.get_fight() != null) {
				return;
			}
			this.perso.thatMap = this.perso.getCurMap().getId();
			this.perso.thatCell = this.perso.getCurCell().getId();
			this.perso.teleport(target.getCurMap().getId(), target.getCurCell().getId());
		}
		}
	}

	private void whoIs(String packet) {
		packet = packet.substring(2);
		final Player player = World.getPersoByName(packet);
		if (player == null) {
			if (packet.isEmpty()) {
				SocketManager.GAME_SEND_BWK(this.perso,
						String.valueOf(this.perso.getAccount().getPseudo()) + "|1|" + this.perso.getName() + "|"
								+ ((this.perso.getCurMap().getSubArea() != null)
										? this.perso.getCurMap().getSubArea().getArea().get_id() : "-1"));
			} else {
				this.perso.send("PIEn" + packet);
			}
		} else {
			if (!player.isOnline()) {
				this.perso.send("PIEn" + player.getName());
				return;
			}
			if (this.perso.getAccount().isFriendWith(player.getId())) {
				SocketManager.GAME_SEND_BWK(this.perso,
						String.valueOf(player.getAccount().getPseudo()) + "|1|" + player.getName() + "|"
								+ ((player.getCurMap().getSubArea() != null)
										? player.getCurMap().getSubArea().getArea().get_id() : "-1"));
			} else if (player == this.perso) {
				SocketManager.GAME_SEND_BWK(this.perso,
						String.valueOf(this.perso.getAccount().getPseudo()) + "|1|" + this.perso.getName() + "|"
								+ ((this.perso.getCurMap().getSubArea() != null)
										? this.perso.getCurMap().getSubArea().getArea().get_id() : "-1"));
			} else {
				SocketManager.GAME_SEND_BWK(this.perso,
						String.valueOf(player.getAccount().getPseudo()) + "|1|" + player.getName() + "|-1");
			}
		}
	}

	public void chooseState(final String packet) {
		switch (packet.charAt(2)) {
		case 'A': {
			if (this.perso._isAbsent) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "038");
				this.perso._isAbsent = false;
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "037");
			this.perso._isAbsent = true;
			break;
		}
		case 'I': {
			if (this.perso._isInvisible) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "051");
				this.perso._isInvisible = false;
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "050");
			this.perso._isInvisible = true;
			break;
		}
		}
	}

	public void goToMap(final String packet) {
		if (this.perso.getGroupe() == null) {
			return;
		}
		if (this.perso.getGroupe().isPlayer()) {
			return;
		}
		final String datas = packet.substring(3);
		if (datas.isEmpty()) {
			return;
		}
		final int MapX = Integer.parseInt(datas.split(",")[0]);
		final int MapY = Integer.parseInt(datas.split(",")[1]);
		final ArrayList<org.aestia.map.Map> i = World.getMapByPosInArrayPlayer(MapX, MapY, this.perso);
		org.aestia.map.Map map = null;
		if (i.size() <= 0) {
			return;
		}
		if (i.size() > 1) {
			map = i.get(Formulas.getRandomValue(0, i.size() - 1));
		} else if (i.size() == 1) {
			map = i.get(0);
		}
		if (map == null) {
			return;
		}
		final int CellId = map.getRandomFreeCellId(true);
		if (map.getCase(CellId) == null) {
			return;
		}
		if (this.perso.get_fight() != null) {
			return;
		}
		this.perso.teleport(map.getId(), CellId);
	}

	private void parseConquestPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'b': {
			this.requestBalance();
			break;
		}
		case 'B': {
			this.getAlignedBonus();
			break;
		}
		case 'W': {
			this.worldInfos(packet);
			break;
		}
		case 'I': {
			this.prismInfos(packet);
			break;
		}
		case 'F': {
			this.prismFight(packet);
			break;
		}
		}
	}

	public void requestBalance() {
		SocketManager.SEND_Cb_BALANCE_CONQUETE(this.perso, String.valueOf(World.getBalanceWorld(this.perso.get_align()))
				+ ";" + World.getBalanceArea(this.perso.getCurMap().getSubArea().getArea(), this.perso.get_align()));
	}

	public void getAlignedBonus() {
		final double porc = World.getBalanceWorld(this.perso.get_align());
		final double porcN = Math.rint(this.perso.getGrade() / 2.5 + 1.0);
		SocketManager.SEND_CB_BONUS_CONQUETE(this.perso, String.valueOf(porc) + "," + porc + "," + porc + ";" + porcN
				+ "," + porcN + "," + porcN + ";" + porc + "," + porc + "," + porc);
	}

	private void worldInfos(final String packet) {
		switch (packet.charAt(2)) {
		case 'J': {
			SocketManager.SEND_CW_INFO_WORLD_CONQUETE(this.perso, World.PrismesGeoposition(1));
			SocketManager.SEND_CW_INFO_WORLD_CONQUETE(this.perso, World.PrismesGeoposition(2));
			break;
		}
		}
	}

	private void prismInfos(final String packet) {
		if (packet.charAt(2) == 'J' || packet.charAt(2) == 'V') {
			switch (packet.charAt(2)) {
			case 'J': {
				final Prism Prismes = World.getPrisme(this.perso.getCurMap().getSubArea().getPrismId());
				if (Prismes != null) {
					Prism.parseAttack(this.perso);
					Prism.parseDefense(this.perso);
				}
				SocketManager.SEND_CIJ_INFO_JOIN_PRISME(this.perso, this.perso.parsePrisme());
				break;
			}
			}
		}
	}

	private void prismFight(final String packet) {
		switch (packet.charAt(2)) {
		case 'J': {
			if (this.perso.isInPrison()) {
				return;
			}
			final int PrismeID = this.perso.getCurMap().getSubArea().getPrismId();
			final Prism Prismes = World.getPrisme(PrismeID);
			if (Prismes == null) {
				return;
			}
			int FightID = -1;
			try {
				FightID = Prismes.getFightId();
			} catch (Exception e) {
				e.printStackTrace();
			}
			short MapID = -1;
			try {
				MapID = Prismes.getMap();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			int cellID = -1;
			try {
				cellID = Prismes.getCell();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			if (PrismeID == -1 || FightID == -1 || MapID == -1 || cellID == -1) {
				return;
			}
			if (Prismes.getAlignement() != this.perso.get_align()) {
				return;
			}
			if (this.perso.get_fight() != null) {
				return;
			}
			if (this.perso.isDead() == 1) {
				SocketManager.GAME_SEND_BN(this.perso);
				return;
			}
			final short _MapID = MapID;
			final int _cellID = cellID;
			final int _fightID = FightID;
			if (this.perso.getCurMap().getId() != MapID) {
				this.perso.setCurMap(this.perso.getCurMap());
				this.perso.setCurCell(this.perso.getCurCell());
				this.waiter.addNext(new Runnable() {
					@Override
					public void run() {
						GameClient.this.perso.teleport(_MapID, _cellID);
					}
				}, 200L);
			}
			this.waiter.addNext(new Runnable() {
				@Override
				public void run() {
					World.getMap(_MapID).getFight(_fightID).joinPrismFight(GameClient.this.perso,
							GameClient.this.perso.getId(), PrismeID);
					for (final Player z : World.getOnlinePersos()) {
						if (z == null) {
							continue;
						}
						if (z.get_align() != GameClient.this.perso.get_align()) {
							continue;
						}
						Prism.parseDefense(z);
					}
				}
			}, 200L);
			break;
		}
		}
	}

	private void parseChanelPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'C': {
			this.subscribeChannels(packet);
			break;
		}
		}
	}

	private void subscribeChannels(final String packet) {
		final String chan = new StringBuilder(String.valueOf(packet.charAt(3))).toString();
		switch (packet.charAt(2)) {
		case '+': {
			this.perso.addChanel(chan);
			break;
		}
		case '-': {
			this.perso.removeChanel(chan);
			break;
		}
		}
		Database.getStatique().getPlayerData().update(this.perso, false);
	}

	private void parseDialogPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'C': {
			this.create(packet);
			break;
		}
		case 'R': {
			this.response(packet);
			break;
		}
		case 'V': {
			this.leave();
			break;
		}
		}
	}

	private void create(final String packet) {
		try {
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			final int npcID = Integer.parseInt(packet.substring(2).split("\n")[0]);
			final Collector taxCollector = World.getCollector(npcID);
			if (taxCollector != null && taxCollector.getMap() == this.perso.getCurMap().getId()) {
				SocketManager.GAME_SEND_DCK_PACKET(this, npcID);
				SocketManager.GAME_SEND_QUESTION_PACKET(this,
						World.getGuild(taxCollector.getGuildId()).parseQuestionTaxCollector());
				return;
			}
			final Npc npc = this.perso.getCurMap().getNpc(npcID);
			if (npc == null) {
				return;
			}
			SocketManager.GAME_SEND_DCK_PACKET(this, npcID);
			final int qID = npc.getTemplate().getInitQuestionId(this.perso.getCurMap().getId());
			NpcQuestion quest = World.getNPCQuestion(qID);
			if (quest == null) {
				SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
				return;
			}
			if (npc.getTemplate().get_id() == 870) {
				final Quest q = Quest.getQuestById(185);
				if (q != null) {
					final Quest.Quest_Perso qp = this.perso.getQuestPersoByQuest(q);
					if (qp != null && qp.isFinish()) {
						SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
						return;
					}
				}
			} else if (npc.getTemplate().get_id() == 891) {
				final Quest q = Quest.getQuestById(200);
				if (q != null) {
					final Quest.Quest_Perso qp = this.perso.getQuestPersoByQuest(q);
					if (qp == null) {
						q.applyQuest(this.perso);
					}
				}
			} else if (npc.getTemplate().get_id() == 925 && this.perso.getCurMap().getId() == 9402) {
				final Quest q = Quest.getQuestById(231);
				if (q != null) {
					final Quest.Quest_Perso qp = this.perso.getQuestPersoByQuest(q);
					if (qp != null && qp.isFinish()) {
						quest = World.getNPCQuestion(4127);
						if (quest == null) {
							SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
							return;
						}
					}
				}
			} else if (npc.getTemplate().get_id() == 577 && this.perso.getCurMap().getId() == 7596) {
				if (this.perso.hasItemTemplate(2106, 1)) {
					quest = World.getNPCQuestion(2407);
				}
			} else if (npc.getTemplate().get_id() == 1041 && this.perso.getCurMap().getId() == 10255 && qID == 5516) {
				if (this.perso.get_align() == 1) {
					if (this.perso.getSexe() == 0) {
						quest = World.getNPCQuestion(5519);
					} else {
						quest = World.getNPCQuestion(5520);
					}
				} else if (this.perso.get_align() == 2) {
					if (this.perso.getSexe() == 0) {
						quest = World.getNPCQuestion(5517);
					} else {
						quest = World.getNPCQuestion(5518);
					}
				} else {
					quest = World.getNPCQuestion(5516);
				}
			}
			this.perso.set_isTalkingWith(npcID);
			final String dialog = quest.parse(this.perso);
			SocketManager.GAME_SEND_QUESTION_PACKET(this, dialog);
			for (final Quest.Quest_Perso qPerso : this.perso.getQuestPerso().values()) {
				boolean loc1 = false;
				for (final Quest_Etape qEtape : qPerso.getQuest().getQuestEtapeList()) {
					if (qEtape.getNpc() == null) {
						continue;
					}
					if (qEtape.getNpc().get_id() != this.perso.getCurMap().getNpc(this.perso.get_isTalkingWith())
							.getTemplate().get_id()) {
						continue;
					}
					loc1 = true;
				}
				final Quest q2 = qPerso.getQuest();
				if (q2 == null) {
					continue;
				}
				if (qPerso.isFinish()) {
					continue;
				}
				final NpcTemplate NPC = q2.getNpc_Tmpl();
				if (NPC == null && !loc1) {
					continue;
				}
				q2.updateQuestData(this.perso, false, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void response(final String packet) {
		final String[] infos = packet.substring(2).split("\\|");
		try {
			if (this.perso.get_isTalkingWith() == 0) {
				return;
			}
			if (this.perso.getCurMap().getNpc(this.perso.get_isTalkingWith()) == null) {
				return;
			}
			final int qID = Integer.parseInt(infos[0]);
			final int rID = Integer.parseInt(infos[1]);
			final NpcQuestion quest = World.getNPCQuestion(qID);
			final NpcAnswer rep = World.getNpcAnswer(rID);
			if (quest == null || rep == null) {
				this.perso.setIsOnDialogAction(-1);
				SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
				return;
			}
			try {
				if (!this.perso.getQuestPerso().isEmpty()) {
					for (final Quest.Quest_Perso QP : this.perso.getQuestPerso().values()) {
						if (!QP.isFinish() && QP.getQuest() != null) {
							if (QP.getQuest().getNpc_Tmpl() == null) {
								continue;
							}
							final ArrayList<Quest_Etape> QEs = QP.getQuest().getQuestEtapeList();
							for (final Quest_Etape qe : QEs) {
								if (qe == null) {
									continue;
								}
								if (QP.isQuestEtapeIsValidate(qe)) {
									continue;
								}
								final NpcTemplate npc = qe.getNpc();
								final NpcTemplate curNpc = this.perso.getCurMap().getNpc(this.perso.get_isTalkingWith())
										.getTemplate();
								if (npc == null) {
									continue;
								}
								if (curNpc == null) {
									continue;
								}
								if (npc.get_id() != curNpc.get_id()) {
									continue;
								}
								QP.getQuest().updateQuestData(this.perso, false, rID);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (rID == 6605) {
				String stats = "";
				String statsReplace = "";
				if (this.perso.hasItemTemplate(10207, 1)) {
					stats = this.perso.getItemTemplate(10207).getTxtStat().get(814).toString();
				}
				try {
					for (final Action a : World.getNpcAnswer(Integer.parseInt(quest.getAnwsers())).getActions()) {
						if (a.getId() == 15 && this.perso.hasItemTemplate(10207, 1)) {
							stats = this.perso.getItemTemplate(10207).getTxtStat().get(814).toString();
							String[] split;
							for (int length = (split = this.perso.getItemTemplate(10207).getTxtStat().get(814)
									.toString().split("\\,")).length, k = 0; k < length; ++k) {
								final String i = split[k];
								final org.aestia.map.Map map = this.perso.getCurMap();
								if (map != null) {
									final Npc npc2 = map.getNpc(this.perso.get_isTalkingWith());
									if (npc2 != null) {
										final NpcTemplate npcT = npc2.getTemplate();
										if (npcT != null) {
											if (Dopeul.parseConditionTrousseau(i.replace(" ", ""), npcT.get_id(),
													map.getId())) {
												this.perso.teleport(Short.parseShort(a.getArgs().split("\\,")[0]),
														Integer.parseInt(a.getArgs().split("\\,")[1]));
												statsReplace = i;
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				if (!statsReplace.isEmpty()) {
					String newStats = "";
					String[] split2;
					for (int length2 = (split2 = stats.split("\\,")).length, l = 0; l < length2; ++l) {
						final String j = split2[l];
						if (!j.equals(statsReplace)) {
							newStats = String.valueOf(newStats) + (newStats.isEmpty() ? j : ("," + j));
						}
					}
					this.perso.getItemTemplate(10207).getTxtStat().remove(814);
					this.perso.getItemTemplate(10207).getTxtStat().put(814, newStats);
				}
				SocketManager.GAME_SEND_UPDATE_ITEM(this.perso, this.perso.getItemTemplate(10207));
			} else if (rID == 4628) {
				Action a2 = null;
				if (this.perso.hasItemTemplate(9487, 1)) {
					final String date = this.perso.getItemTemplate(9487, 1).getTxtStat().get(805);
					final long timeStamp = Long.parseLong(date);
					if (System.currentTimeMillis() - timeStamp <= 1209600000L) {
						a2 = new Action(1, "5522", "", World.getMap((short) 10255));
						a2.apply(this.perso, null, -1, -1);
						return;
					}
				}
				a2 = new Action(1, "5521", "", World.getMap((short) 10255));
				a2.apply(this.perso, null, -1, -1);
				return;
			}
			final boolean leave = rep.apply(this.perso);
			if (!rep.isAnotherDialog()) {
				if (this.perso.getIsOnDialogAction() == 1) {
					this.perso.setIsOnDialogAction(-1);
				} else if (leave) {
					SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
					this.perso.set_isTalkingWith(0);
				}
			}
		} catch (Exception e3) {
			e3.printStackTrace();
			this.perso.setIsOnDialogAction(-1);
			SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
		}
	}

	private void leave() {
		SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
		if (this.perso.get_isTalkingWith() != 0) {
			this.perso.set_isTalkingWith(0);
		}
	}

	private void parseDocumentPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'V': {
			SocketManager.send(this.perso, "dV");
			break;
		}
		}
	}

	private synchronized void parseExchangePacket(final String packet) {
		if (this.perso.isDead() == 1) {
			return;
		}
		switch (packet.charAt(1)) {
		case 'A': {
			this.accept();
			break;
		}
		case 'B': {
			this.buy(packet);
			break;
		}
		case 'H': {
			this.bigStore(packet);
			break;
		}
		case 'K': {
			this.ready();
			break;
		}
		case 'L': {
			this.replayCraft();
			break;
		}
		case 'M': {
			this.movementItemOrKamas(packet);
			break;
		}
		case 'P': {
			this.movementItemOrKamasDons(packet.substring(2));
			break;
		}
		case 'q': {
			this.askOfflineExchange();
			break;
		}
		case 'Q': {
			this.offlineExchange();
			break;
		}
		case 'r': {
			this.putInInventory(packet);
			break;
		}
		case 'f': {
			this.putInMountPark(packet);
			break;
		}
		case 'R': {
			this.request(packet);
			break;
		}
		case 'S': {
			this.sell(packet);
			break;
		}
		case 'J': {
			this.bookOfArtisant(packet);
			break;
		}
		case 'W': {
			this.setPublicMode(packet);
			break;
		}
		case 'V': {
			this.leaveExchange();
			break;
		}
		}
	}

	private void accept() {
		if (Main.tradeAsBlocked || this.perso.get_isTradingWith() == 0) {
			return;
		}
		final Player target = World.getPersonnage(this.perso.get_isTradingWith());
		if (target == null || target.isDead() == 1) {
			return;
		}
		final int type = this.perso.getIsCraftingType().get(0);
		switch (type) {
		case 1: {
			SocketManager.GAME_SEND_EXCHANGE_CONFIRM_OK(this, 1);
			SocketManager.GAME_SEND_EXCHANGE_CONFIRM_OK(target.getGameClient(), 1);
			final PlayerExchange echg = new PlayerExchange(target, this.perso);
			this.perso.setCurExchange(echg);
			this.perso.set_isTradingWith(target.getId());
			this.perso.getIsCraftingType().clear();
			target.getIsCraftingType().clear();
			target.setCurExchange(echg);
			target.set_isTradingWith(this.perso.getId());
			break;
		}
		case 12:
		case 13: {
			final Player player1 = (target.getIsCraftingType().get(0) == 12) ? target : this.perso;
			final Player player2 = (target.getIsCraftingType().get(0) == 13) ? target : this.perso;
			final int max_case = JobConstant
					.getTotalCaseByJobLevel(player1.getMetierBySkill(player1.getIsCraftingType().get(1)).get_lvl());
			SocketManager.GAME_SEND_ECK_PACKET(this, type,
					String.valueOf(max_case) + ";" + this.perso.getIsCraftingType().get(1));
			SocketManager.GAME_SEND_ECK_PACKET(target.getGameClient(), target.getIsCraftingType().get(0),
					String.valueOf(max_case) + ";" + this.perso.getIsCraftingType().get(1));
			final PlayerExchange echg = new CraftSecure(player1, player2);
			this.perso.setCurExchange(echg);
			this.perso.set_isTradingWith(target.getId());
			target.set_isTradingWith(this.perso.getId());
			target.setCurExchange(echg);
			this.perso.setCurExchange(echg);
			break;
		}
		}
	}

	private void buy(final String packet) {
		final String[] infos = packet.substring(2).split("\\|");

		if (perso.boutique) {
			try {
				int quantity = Integer.parseInt(infos[1]);
				ObjectTemplate template = World.getObjTemplate(Integer.parseInt(infos[0]));

				int value = template.getPoints() * quantity;
				int points = compte.getPoints() - value;

				if (points <= 0)// Si le joueur n'a pas assez de point
				{
					int diferencia = value - compte.getPoints();
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous n'avez pas assez de points pour acheter cet article, vous avez actuellement "
									+ compte.getPoints() + " points et il vous manque " + diferencia
									+ " points pour pouvoir l'acheter.",
							"FF0000");
					SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
					return;
				}
				compte.setPoints(points);
				org.aestia.object.Object newObj = null;
				newObj = template.createNewItem(quantity, true);
				
				if (perso.addObjet(newObj, true))// Return TRUE si c'est un
					World.addObjet(newObj, true);
				SocketManager.GAME_SEND_BUY_OK_PACKET(this);
				SocketManager.GAME_SEND_STATS_PACKET(perso);
				SocketManager.GAME_SEND_Ow_PACKET(perso);
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Vous venez d'acheter " + quantity + (quantity > 1 ? " items" : " item") + " : <b>"
								+ template.getName() + "</b> au prix de <b>" + value + "</b> points boutique !",
						"000000");
			} catch (Exception e) {
				e.printStackTrace();
				SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
				return;
			}
			return;
		}

		if (this.perso.get_isTradingWith() > 0) {
			final Player seller = World.getPersonnage(this.perso.get_isTradingWith());
			if (seller != null && seller != this.perso) {
				int itemID = 0;
				int qua = 0;
				int price = 0;
				try {
					itemID = Integer.valueOf(infos[0]);
					qua = Integer.valueOf(infos[1]);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				synchronized (seller.getStoreItems()) {
					if (!seller.getStoreItems().containsKey(itemID) || qua <= 0) {
						SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
						// monitorexit(seller.getStoreItems())
						return;
					}
					price = seller.getStoreItems().get(itemID) * qua;
					final int price2 = seller.getStoreItems().get(itemID);
					final org.aestia.object.Object itemStore = World.getObjet(itemID);
					if (itemStore == null) {
						// monitorexit(seller.getStoreItems())
						return;
					}
					if (price > this.perso.get_kamas()) {
						// monitorexit(seller.getStoreItems())
						return;
					}
					if (qua <= 0 || qua > 100000) {
						// monitorexit(seller.getStoreItems())
						return;
					}
					if (qua > itemStore.getQuantity()) {
						qua = itemStore.getQuantity();
					}
					if (qua == itemStore.getQuantity()) {
						seller.getStoreItems().remove(itemStore.getGuid());
						this.perso.addObjet(itemStore, true);
					} else {
						if (itemStore.getQuantity() <= qua) {
							SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
							// monitorexit(seller.getStoreItems())
							return;
						}
						seller.getStoreItems().remove(itemStore.getGuid());
						itemStore.setQuantity(itemStore.getQuantity() - qua);
						Database.getStatique().getItemData().save(itemStore, false);
						seller.addStoreItem(itemStore.getGuid(), price2);
						final org.aestia.object.Object clone = org.aestia.object.Object.getCloneObjet(itemStore, qua);
						if (this.perso.addObjet(clone, true)) {
							World.addObjet(clone, false);
						}
						Database.getStatique().getItemData().saveNew(clone);
					}
					this.perso.addKamas(-price);
					seller.addKamas(price);
					Database.getStatique().getPlayerData().update(seller, true);
					SocketManager.GAME_SEND_STATS_PACKET(this.perso);
					SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(seller, this.perso);
					SocketManager.GAME_SEND_BUY_OK_PACKET(this);
					if (seller.getStoreItems().isEmpty() && World.getSeller(seller.getCurMap().getId()) != null
							&& World.getSeller(seller.getCurMap().getId()).contains(seller.getId())) {
						World.removeSeller(seller.getId(), seller.getCurMap().getId());
						SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(seller.getCurMap(), seller.getId());
						this.leaveExchange();
					}
				}
				// monitorexit(seller.getStoreItems())
			}
			return;
		}
		try {
			final int tempID = Integer.parseInt(infos[0]);
			final int qua2 = Integer.parseInt(infos[1]);
			if (qua2 <= 0 || qua2 > 100000) {
				return;
			}
			final ObjectTemplate template = World.getObjTemplate(tempID);
			if (template == null) {
				SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
				return;
			}
			if (template.getType() == 18 && qua2 > 1) {
				this.perso.sendMessage("Merci de n'acheter qu'un seul familier \u00e0 la fois !");
				return;
			}
			if (this.perso.getCurMap().getNpc(this.perso.get_isTradingWith()) == null) {
				return;
			}
			if (!this.perso.getCurMap().getNpc(this.perso.get_isTradingWith()).getTemplate().haveItem(tempID)) {
				SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
				return;
			}
			if (template.getPoints() > 0) {
				final int value = template.getPoints() * qua2;
				final int points = this.compte.getPoints() - value;
				if (this.compte.getPoints() < value) {
					final int diff = value - this.compte.getPoints();
					SocketManager.GAME_SEND_MESSAGE(this.perso,
							"Vous n'avez pas assez de points pour acheter cet article, vous avez actuellement "
									+ this.compte.getPoints() + "  points boutique et vous manquent " + diff
									+ " points pour pouvoir l'acheter.");
					SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
					return;
				}
				this.compte.setPoints(points);
				final org.aestia.object.Object newObj = template.createNewItem(qua2, true);
				if (this.perso.addObjet(newObj, true)) {
					World.addObjet(newObj, true);
				}
				SocketManager.GAME_SEND_BUY_OK_PACKET(this);
				SocketManager.GAME_SEND_STATS_PACKET(this.perso);
				SocketManager.GAME_SEND_Ow_PACKET(this.perso);
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Il te reste : " + this.compte.getPoints() + " points boutique !");
			} else if (template.getPoints() == 0) {
				final int prix = template.getPrice() * qua2;
				if (prix < 0) {
					return;
				}
				if (this.perso.get_kamas() < prix) {
					SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
					return;
				}
				final org.aestia.object.Object newObj2 = template.createNewItem(qua2, false);
				final long newKamas = this.perso.get_kamas() - prix;
				this.perso.set_kamas(newKamas);
				if (this.perso.addObjet(newObj2, true)) {
					World.addObjet(newObj2, true);
				}
				SocketManager.GAME_SEND_BUY_OK_PACKET(this);
				SocketManager.GAME_SEND_STATS_PACKET(this.perso);
				SocketManager.GAME_SEND_Ow_PACKET(this.perso);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			SocketManager.GAME_SEND_BUY_ERROR_PACKET(this);
		}
	}

	private void bigStore(final String packet) {
		if (this.perso.get_isTradingWith() > 0 || this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		switch (packet.charAt(2)) {
		case 'B': {
			final String[] info = packet.substring(3).split("\\|");
			final Hdv curHdv = World.getHdv(Math.abs(this.perso.get_isTradingWith()));
			final int ligneID = Integer.parseInt(info[0]);
			final byte amount = Byte.parseByte(info[1]);
			final HdvLine hL = curHdv.getLine(ligneID);
			if (hL == null) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"[1] Une erreur est survenue lors de la confirmation d'achat. Veuillez contactacter un administrateur.");
				return;
			}
			final HdvEntry hE = hL.doYouHave(amount, Integer.parseInt(info[2]));
			if (hE == null) {
				SocketManager.GAME_SEND_MESSAGE(this.perso, "[2 - Template '" + hL.getTemplateId()
						+ "'] Une erreur est survenue lors de la confirmation d'achat. Veuillez contactacter un administrateur.");
				return;
			}
			final Integer owner = hE.getOwner();
			if (owner == null) {
				SocketManager.GAME_SEND_MESSAGE(this.perso, "[3 - Template '" + hL.getTemplateId()
						+ "'] Cet objet n'a pas de propri\u00e9taire. Contactez un administrateur.");
				return;
			}
			if (owner == this.perso.getAccount().getGuid()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso, "Tu ne peux pas acheter ton propre objet.");
				return;
			}
			if (curHdv.buyItem(ligneID, amount, Integer.parseInt(info[2]), this.perso)) {
				SocketManager.GAME_SEND_EHm_PACKET(this.perso, "-",
						new StringBuilder(String.valueOf(ligneID)).toString());
				if (curHdv.getLine(ligneID) != null && !curHdv.getLine(ligneID).isEmpty()) {
					SocketManager.GAME_SEND_EHm_PACKET(this.perso, "+", curHdv.getLine(ligneID).parseToEHm());
				}
				this.perso.refreshStats();
				SocketManager.GAME_SEND_Ow_PACKET(this.perso);
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "068");
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "172");
			break;
		}
		case 'l': {
			final int templateID = Integer.parseInt(packet.substring(3));
			try {
				SocketManager.GAME_SEND_EHl(this.perso, World.getHdv(Math.abs(this.perso.get_isTradingWith())),
						templateID);
			} catch (NullPointerException e) {
				e.printStackTrace();
				SocketManager.GAME_SEND_EHM_PACKET(this.perso, "-",
						new StringBuilder(String.valueOf(templateID)).toString());
			}
			break;
		}
		case 'P': {
			final int templateID = Integer.parseInt(packet.substring(3));
			SocketManager.GAME_SEND_EHP_PACKET(this.perso, templateID);
			break;
		}
		case 'T': {
			final int categ = Integer.parseInt(packet.substring(3));
			final String allTemplate = World.getHdv(Math.abs(this.perso.get_isTradingWith())).parseTemplate(categ);
			SocketManager.GAME_SEND_EHL_PACKET(this.perso, categ, allTemplate);
			break;
		}
		}
	}

	private void ready() {
		if (this.perso.getCurJobAction() != null && this.perso.getCurJobAction().isCraft()) {
			this.perso.getCurJobAction().startCraft(this.perso);
		}
		if (this.perso.getCurNpcExchange() != null) {
			this.perso.getCurNpcExchange().toogleOK(false);
		}
		if (this.perso.getCurNpcExchangePets() != null) {
			this.perso.getCurNpcExchangePets().toogleOK(false);
		}
		if (this.perso.getCurNpcRessurectPets() != null) {
			this.perso.getCurNpcRessurectPets().toogleOK(false);
		}
		if (this.perso.getCurExchange() != null && this.perso.getCurExchange().toogleOk(this.perso.getId())) {
			this.perso.getCurExchange().apply();
		}
		if (this.perso.getConcasseur() != null) {
			if (this.perso.getConcasseur().getObjects().isEmpty()) {
				return;
			}
			final Fragment fragment = new Fragment(World.getNewItemGuid(), "");
			for (final World.Couple<Integer, Integer> couple : this.perso.getConcasseur().getObjects()) {
				final org.aestia.object.Object object = World.getObjet(couple.first);
				if (object != null) {
					if (couple.second < 1) {
						continue;
					}
					for (int k = couple.second; k > 0; --k) {
						if (object.getTemplate().getType() != 78) {
							for (final Map.Entry<Integer, Integer> entry1 : object.getStats().getMap().entrySet()) {
								final int jet = entry1.getValue();
								for (final Rune rune : Rune.runes) {
									if (entry1.getKey() == rune.getCharacteristic()) {
										for (final Map.Entry<Integer, Integer> entry2 : rune.getBonus().entrySet()) {
											if (entry2.getValue() == 1557 || entry2.getValue() == 1558
													|| entry2.getValue() == 7438) {
												final double puissanceExtractible = 1.5
														* (Math.pow(object.getTemplate().getLevel(), 2.0)
																/ Math.pow(rune.getWeight(), 1.25))
														+ (jet - 1) / rune.getWeight()
																* (66.66 - 1.5 * (Math
																		.pow(object.getTemplate().getLevel(), 2.0)
																		/ Math.pow(rune.getWeight(), 13.75)));
												int p = (int) Math.ceil(puissanceExtractible);
												if (p > 66) {
													p = 66;
												} else if (p <= 0) {
													p = 1;
												}
												if (Formulas.getRandomValue(1, 100) > p) {
													continue;
												}
												fragment.addRune(entry2.getValue(), 1);
											} else {
												double val = entry2.getKey();
												if (entry2.getValue() == 7451 || entry2.getValue() == 10662) {
													val *= 3.0;
												}
												final double tauxObtentionIntermediaire = World
														.getTauxObtentionIntermediaire(val, true, val != 30.0);
												final double tauxObtentionMaximum = tauxObtentionIntermediaire
														/ 0.6666666666666666 / 0.9;
												int tMax = (int) Math.ceil(tauxObtentionMaximum);
												final int tOT = (int) Math.ceil(tauxObtentionIntermediaire);
												final int tMin = 2 * (tMax - tOT) - 2;
												if (entry2.getValue() == 7433 || entry2.getValue() == 7434
														|| entry2.getValue() == 7435 || entry2.getValue() == 7441) {
													++tMax;
												}
												if (jet < tMin) {
													continue;
												}
												for (int i = jet; i > 0; i -= tMax) {
													int j = 0;
													if (i > tMax) {
														j = tMax;
													} else {
														j = i;
													}
													if (j == tMax) {
														fragment.addRune(entry2.getValue(), 1);
													} else if (Formulas.getRandomValue(1, 100) < 100 * (tMax - j)
															/ (tMax - tMin)) {
														fragment.addRune(entry2.getValue(), 1);
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if (couple.second == object.getQuantity()) {
						this.perso.deleteItem(object.getGuid());
						World.removeItem(object.getGuid());
						SocketManager.SEND_OR_DELETE_ITEM(this, object.getGuid());
					} else {
						object.setQuantity(object.getQuantity() - couple.second);
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, object);
					}
				}
			}
			World.addObjet(fragment, true);
			this.perso.addObjet(fragment);
			SocketManager.GAME_SEND_Ec_PACKET(this.perso, "K;8378");
			SocketManager.GAME_SEND_Ow_PACKET(this.perso);
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.perso.getCurMap(), this.perso.getId(), "+8378");
			SocketManager.GAME_SEND_EV_PACKET(this);
			this.perso.startActionOnCell(this.perso.getGameAction());
		}
	}

	private void replayCraft() {
		if (this.perso.getCurJobAction() != null) {
			this.perso.getCurJobAction().putLastCraftIngredients();
		}
	}

	private synchronized void movementItemOrKamas(final String packet) {
		if (this.perso.get_isTradingWith() == this.perso.getId()) {
			switch (packet.charAt(2)) {
			case 'O': {
				if (packet.charAt(3) == '+') {
					final String[] infos = packet.substring(4).split("\\|");
					try {
						final int guid = Integer.parseInt(infos[0]);
						int qua = Integer.parseInt(infos[1]);
						final int price = Integer.parseInt(infos[2]);
						final org.aestia.object.Object obj = World.getObjet(guid);
						if (obj == null) {
							return;
						}
						if (qua <= 0) {
							return;
						}
						if (price <= 0) {
							return;
						}
						if (qua > obj.getQuantity()) {
							qua = obj.getQuantity();
						}
						this.perso.addinStore(obj.getGuid(), price, qua);
						break;
					} catch (NumberFormatException e) {
						Console.println("Error Echange Store '" + packet + "' => " + e.getMessage(),
								Console.Color.ERROR);
						e.printStackTrace();
						return;
					}
				}
				final String[] infos = packet.substring(4).split("\\|");
				try {
					final int guid = Integer.parseInt(infos[0]);
					int qua = Integer.parseInt(infos[1]);
					if (qua <= 0) {
						return;
					}
					final org.aestia.object.Object obj2 = World.getObjet(guid);
					if (obj2 == null) {
						return;
					}
					if (qua < 0) {
						return;
					}
					if (qua > obj2.getQuantity()) {
						return;
					}
					if (qua < obj2.getQuantity()) {
						qua = obj2.getQuantity();
					}
					this.perso.removeFromStore(obj2.getGuid(), qua);
				} catch (NumberFormatException e) {
					Console.println("Error Echange Store '" + packet + "' => " + e.getMessage(), Console.Color.ERROR);
					e.printStackTrace();
					return;
				}
				break;
			}
			}
			return;
		}
		if (this.perso.get_isOnCollectorID() != 0) {
			final Collector Collector = World.getCollector(this.perso.get_isOnCollectorID());
			if (Collector == null || Collector.getInFight() > 0) {
				return;
			}
			switch (packet.charAt(2)) {
			case 'G': {
				if (packet.charAt(3) != '-') {
					break;
				}
				long P_Kamas = -1L;
				try {
					P_Kamas = Integer.parseInt(packet.substring(4));
				} catch (NumberFormatException e2) {
					e2.printStackTrace();
					Console.println("Error Echange CC '" + packet + "' => " + e2.getMessage(), Console.Color.ERROR);
				}
				if (P_Kamas < 0L) {
					return;
				}
				if (Collector.getKamas() >= P_Kamas) {
					long P_Retrait = Collector.getKamas() - P_Kamas;
					Collector.setKamas(Collector.getKamas() - P_Kamas);
					if (P_Retrait < 0L) {
						P_Retrait = 0L;
						P_Kamas = Collector.getKamas();
					}
					Collector.setKamas(P_Retrait);
					this.perso.addKamas(P_Kamas);
					SocketManager.GAME_SEND_STATS_PACKET(this.perso);
					SocketManager.GAME_SEND_EsK_PACKET(this.perso, "G" + Collector.getKamas());
					break;
				}
				break;
			}
			case 'O': {
				if (packet.charAt(3) != '-') {
					break;
				}
				final String[] infos2 = packet.substring(4).split("\\|");
				int guid2 = 0;
				int qua2 = 0;
				try {
					guid2 = Integer.parseInt(infos2[0]);
					qua2 = Integer.parseInt(infos2[1]);
				} catch (NumberFormatException e8) {
					return;
				}
				if (guid2 <= 0 || qua2 <= 0) {
					return;
				}
				final org.aestia.object.Object obj = World.getObjet(guid2);
				if (obj == null) {
					return;
				}
				if (Collector.haveObjects(guid2)) {
					Collector.removeFromCollector(this.perso, guid2, qua2);
				}
				Collector.addLogObjects(guid2, obj);
				break;
			}
			}
			Database.getGame().getGuildData().update(this.perso.get_guild());
		} else {
			if (this.perso.getConcasseur() != null) {
				if (packet.charAt(2) == 'O') {
					if (packet.charAt(3) == '+') {
						if (this.perso.getConcasseur().getObjects().size() >= 8) {
							return;
						}
						final String[] infos = packet.substring(4).split("\\|");
						try {
							final int id = Integer.parseInt(infos[0]);
							int qua = Integer.parseInt(infos[1]);
							if (!this.perso.hasItemGuid(id)) {
								return;
							}
							final org.aestia.object.Object object = World.getObjet(id);
							if (object == null) {
								return;
							}
							if (qua < 1) {
								return;
							}
							if (qua > object.getQuantity()) {
								qua = object.getQuantity();
							}
							if (object.getTemplate().getType() == 18) {
								return;
							}
							SocketManager.SEND_EMK_MOVE_ITEM(this, 'O', "+",
									String.valueOf(id) + "|" + this.perso.getConcasseur().addObject(id, qua));
							return;
						} catch (NumberFormatException e) {
							Console.println("Error Echange CC '" + packet + "' => " + e.getMessage(),
									Console.Color.ERROR);
							e.printStackTrace();
							return;
						}
					}
					if (packet.charAt(3) == '-') {
						final String[] infos = packet.substring(4).split("\\|");
						try {
							final int id = Integer.parseInt(infos[0]);
							final int qua = Integer.parseInt(infos[1]);
							final org.aestia.object.Object object = World.getObjet(id);
							if (object == null) {
								return;
							}
							if (qua < 1) {
								return;
							}
							final int quantity = this.perso.getConcasseur().removeObject(id, qua);
							if (quantity <= 0) {
								SocketManager.SEND_EMK_MOVE_ITEM(this, 'O', "-",
										new StringBuilder(String.valueOf(id)).toString());
							} else {
								SocketManager.SEND_EMK_MOVE_ITEM(this, 'O', "+", String.valueOf(id) + "|" + quantity);
							}
						} catch (NumberFormatException e) {
							Console.println("Error Echange CC '" + packet + "' => " + e.getMessage(),
									Console.Color.ERROR);
							e.printStackTrace();
						}
					}
				}
				return;
			}
			if (this.perso.getInDD()) {
				final Dragodinde drago = this.perso.getMount();
				if (drago == null) {
					return;
				}
				Label_1316: {
					switch (packet.charAt(2)) {
					case 'O': {
						int id = 0;
						int cant = 0;
						try {
							id = Integer.parseInt(packet.substring(4).split("\\|")[0]);
							cant = Integer.parseInt(packet.substring(4).split("\\|")[1]);
						} catch (Exception e3) {
							Console.println("Error Echange DD '" + packet + "' => " + e3.getMessage(),
									Console.Color.ERROR);
							e3.printStackTrace();
							return;
						}
						if (id == 0 || cant <= 0) {
							return;
						}
						if (World.getObjet(id) == null) {
							SocketManager.GAME_SEND_MESSAGE(this.perso,
									"Erreur 1 d'inventaire de dragodinde : l'objet n'existe pas !");
							return;
						}
						switch (packet.charAt(3)) {
						case '+': {
							drago.addInDinde(id, cant, this.perso);
							break Label_1316;
						}
						case '-': {
							drago.removeFromDinde(id, cant, this.perso);
							break Label_1316;
						}
						}
						break;
					}
					}
				}
			} else {
				if (this.perso.getCurNpcExchange() != null) {
					if (this.perso.get_fight() != null) {
						return;
					}
					switch (packet.charAt(2)) {
					case 'O': {
						if (packet.charAt(3) == '+') {
							final String[] infos = packet.substring(4).split("\\|");
							try {
								int guid = Integer.parseInt(infos[0]);
								int qua = Integer.parseInt(infos[1]);
								final int quaInExch = this.perso.getCurNpcExchange().getQuaItem(guid, false);
								if (!this.perso.hasItemGuid(guid)) {
									return;
								}
								final org.aestia.object.Object obj = World.getObjet(guid);
								if (obj == null) {
									return;
								}
								if (qua > obj.getQuantity() - quaInExch) {
									qua = obj.getQuantity() - quaInExch;
								}
								if (qua <= 0) {
									return;
								}
								if (obj.getQuantity() > qua) {
									final ObjectTemplate T = obj.getTemplate();
									if (T == null) {
										return;
									}
									final org.aestia.object.Object newObj = T.createNewItem(qua, false);
									World.addObjet(newObj, true);
									obj.setQuantity(obj.getQuantity() - qua);
									Database.getStatique().getItemData().save(obj, false);
									guid = newObj.getGuid();
								}
								this.perso.getCurNpcExchange().addItem(guid, qua);
								break;
							} catch (NumberFormatException e) {
								Console.println("Error Echange NPC '" + packet + "' => " + e.getMessage(),
										Console.Color.ERROR);
								e.printStackTrace();
								return;
							}
						}
						final String[] infos = packet.substring(4).split("\\|");
						try {
							final int guid = Integer.parseInt(infos[0]);
							final int qua = Integer.parseInt(infos[1]);
							if (qua <= 0) {
								return;
							}
							if (!this.perso.hasItemGuid(guid)) {
								return;
							}
							final org.aestia.object.Object obj2 = World.getObjet(guid);
							if (obj2 == null) {
								return;
							}
							if (qua > this.perso.getCurNpcExchange().getQuaItem(guid, false)) {
								return;
							}
							this.perso.getCurNpcExchange().removeItem(guid, qua);
							break;
						} catch (NumberFormatException e) {
							Console.println("Error Echange NPC '" + packet + "' => " + e.getMessage(),
									Console.Color.ERROR);
							e.printStackTrace();
							return;
						}
					}
					case 'G': {
						try {
							long numb = Integer.parseInt(packet.substring(3));
							if (this.perso.get_kamas() < numb) {
								numb = this.perso.get_kamas();
							}
							this.perso.getCurNpcExchange().setKamas(false, numb);
						} catch (NumberFormatException e4) {
							Console.println("Error Echange NPC '" + packet + "' => " + e4.getMessage(),
									Console.Color.ERROR);
							e4.printStackTrace();
							return;
						}
						break;
					}
					}
				} else if (this.perso.getCurNpcExchangePets() != null) {
					if (this.perso.get_fight() != null) {
						return;
					}
					switch (packet.charAt(2)) {
					case 'O': {
						if (packet.charAt(3) == '+') {
							final String[] infos = packet.substring(4).split("\\|");
							try {
								final int guid = Integer.parseInt(infos[0]);
								int qua = Integer.parseInt(infos[1]);
								final int quaInExch = this.perso.getCurNpcExchangePets().getQuaItem(guid, false);
								if (!this.perso.hasItemGuid(guid)) {
									return;
								}
								final org.aestia.object.Object obj = World.getObjet(guid);
								if (obj == null) {
									return;
								}
								if (qua > obj.getQuantity() - quaInExch) {
									qua = obj.getQuantity() - quaInExch;
								}
								if (qua <= 0) {
									return;
								}
								this.perso.getCurNpcExchangePets().addItem(guid, qua);
								break;
							} catch (NumberFormatException e) {
								Console.println("Error Echange Pets '" + packet + "' => " + e.getMessage(),
										Console.Color.ERROR);
								e.printStackTrace();
								return;
							}
						}
						final String[] infos = packet.substring(4).split("\\|");
						try {
							final int guid = Integer.parseInt(infos[0]);
							final int qua = Integer.parseInt(infos[1]);
							if (qua <= 0) {
								return;
							}
							if (!this.perso.hasItemGuid(guid)) {
								return;
							}
							final org.aestia.object.Object obj2 = World.getObjet(guid);
							if (obj2 == null) {
								return;
							}
							if (qua > this.perso.getCurNpcExchangePets().getQuaItem(guid, false)) {
								return;
							}
							this.perso.getCurNpcExchangePets().removeItem(guid, qua);
							break;
						} catch (NumberFormatException e) {
							Console.println("Error Echange Pets '" + packet + "' => " + e.getMessage(),
									Console.Color.ERROR);
							e.printStackTrace();
							return;
						}
					}
					case 'G': {
						try {
							long numb = Integer.parseInt(packet.substring(3));
							if (numb < 0L) {
								return;
							}
							if (this.perso.get_kamas() < numb) {
								numb = this.perso.get_kamas();
							}
							this.perso.getCurNpcExchange().setKamas(false, numb);
						} catch (NumberFormatException e4) {
							Console.println("Error Echange Pets '" + packet + "' => " + e4.getMessage(),
									Console.Color.ERROR);
							e4.printStackTrace();
							return;
						}
						break;
					}
					}
				} else if (this.perso.getCurNpcRessurectPets() != null) {
					if (this.perso.get_fight() != null) {
						return;
					}
					switch (packet.charAt(2)) {
					case 'O': {
						if (packet.charAt(3) == '+') {
							final String[] infos = packet.substring(4).split("\\|");
							try {
								final int guid = Integer.parseInt(infos[0]);
								int qua = Integer.parseInt(infos[1]);
								final int quaInExch = this.perso.getCurNpcRessurectPets().getQuaItem(guid, false);
								if (!this.perso.hasItemGuid(guid)) {
									return;
								}
								final org.aestia.object.Object obj = World.getObjet(guid);
								if (obj == null) {
									return;
								}
								if (qua > obj.getQuantity() - quaInExch) {
									qua = obj.getQuantity() - quaInExch;
								}
								if (qua <= 0) {
									return;
								}
								this.perso.getCurNpcRessurectPets().addItem(guid, qua);
								break;
							} catch (NumberFormatException e) {
								Console.println("Error Echange RPets '" + packet + "' => " + e.getMessage(),
										Console.Color.ERROR);
								e.printStackTrace();
								return;
							}
						}
						final String[] infos = packet.substring(4).split("\\|");
						try {
							final int guid = Integer.parseInt(infos[0]);
							final int qua = Integer.parseInt(infos[1]);
							if (qua <= 0) {
								return;
							}
							if (!this.perso.hasItemGuid(guid)) {
								return;
							}
							final org.aestia.object.Object obj2 = World.getObjet(guid);
							if (obj2 == null) {
								return;
							}
							if (qua > this.perso.getCurNpcRessurectPets().getQuaItem(guid, false)) {
								return;
							}
							this.perso.getCurNpcRessurectPets().removeItem(guid, qua);
							break;
						} catch (NumberFormatException e) {
							Console.println("Error Echange RPets '" + packet + "' => " + e.getMessage(),
									Console.Color.ERROR);
							e.printStackTrace();
							return;
						}
					}
					case 'G': {
						try {
							long numb = Integer.parseInt(packet.substring(3));
							if (numb < 0L) {
								return;
							}
							if (this.perso.get_kamas() < numb) {
								numb = this.perso.get_kamas();
							}
							this.perso.getCurNpcRessurectPets().setKamas(false, numb);
						} catch (NumberFormatException e4) {
							e4.printStackTrace();
							return;
						}
						break;
					}
					}
				} else {
					if (this.perso.get_isTradingWith() < 0 && this.perso.getConcasseur() == null) {
						switch (packet.charAt(3)) {
						case '-': {
							int count = 0;
							int cheapestID = 0;
							try {
								cheapestID = Integer.parseInt(packet.substring(4).split("\\|")[0]);
								count = Integer.parseInt(packet.substring(4).split("\\|")[1]);
							} catch (Exception e5) {
								Console.println("Error Echange HDV '" + packet + "' => " + e5.getMessage(),
										Console.Color.ERROR);
								e5.printStackTrace();
								return;
							}
							if (count <= 0) {
								return;
							}
							this.perso.getAccount().recoverItem(cheapestID, count);
							SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this, '-', "",
									new StringBuilder(String.valueOf(cheapestID)).toString());
							break;
						}
						case '+': {
							if (Integer.parseInt(packet.substring(4).split("\\|")[1]) > 127) {
								SocketManager.GAME_SEND_MESSAGE(this.perso,
										"Vous avez atteins la limite maximum du nombre d'objet.");
								return;
							}
							int price = 0;
							byte amount = 0;
							int itmID;
							try {
								itmID = Integer.parseInt(packet.substring(4).split("\\|")[0]);
								amount = Byte.parseByte(packet.substring(4).split("\\|")[1]);
								price = Integer.parseInt(packet.substring(4).split("\\|")[2]);
							} catch (Exception e6) {
								Console.println("Error Echange HDV '" + packet + "' => " + e6.getMessage(),
										Console.Color.ERROR);
								e6.printStackTrace();
								SocketManager.GAME_SEND_MESSAGE(this.perso,
										"Une erreur s'est produite lors de la mise en vente de votre objet. Veuillez vous reconnectez pour corriger l'erreur. Personnage "
												+ this.getPersonnage().getName() + " et paquet " + packet + ".");
								return;
							}
							if (amount <= 0 || price <= 0) {
								return;
							}
							if (packet.substring(1).split("\\|")[2] == "0" || packet.substring(2).split("\\|")[2] == "0"
									|| packet.substring(3).split("\\|")[2] == "0") {
								return;
							}
							final Hdv curHdv = World.getHdv(Math.abs(this.perso.get_isTradingWith()));
							curHdv.getHdvId();
							final int taxe = (int) (price * (curHdv.getTaxe() / 100.0f));
							if (taxe < 0) {
								return;
							}
							if (!this.perso.hasItemGuid(itmID)) {
								return;
							}
							if (this.perso.getAccount().countHdvItems(curHdv.getHdvId()) >= curHdv
									.getMaxAccountItem()) {
								SocketManager.GAME_SEND_Im_PACKET(this.perso, "058");
								return;
							}
							if (this.perso.get_kamas() < taxe) {
								SocketManager.GAME_SEND_Im_PACKET(this.perso, "176");
								return;
							}
							this.perso.addKamas(taxe * -1);
							SocketManager.GAME_SEND_STATS_PACKET(this.perso);
							org.aestia.object.Object obj3 = World.getObjet(itmID);
							final int qua3 = (amount == 1) ? 1 : ((amount == 2) ? 10 : 100);
							if (qua3 > obj3.getQuantity()) {
								return;
							}
							final int rAmount = (int) (Math.pow(10.0, amount) / 10.0);
							final int newQua = obj3.getQuantity() - rAmount;
							if (newQua <= 0) {
								this.perso.removeItem(itmID);
								SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, itmID);
							} else {
								obj3.setQuantity(obj3.getQuantity() - rAmount);
								Database.getStatique().getItemData().save(obj3, false);
								SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj3);
								final org.aestia.object.Object newObj2 = org.aestia.object.Object.getCloneObjet(obj3,
										rAmount);
								World.addObjet(newObj2, true);
								obj3 = newObj2;
								Database.getStatique().getItemData().save(newObj2, false);
							}
							final HdvEntry toAdd = new HdvEntry(World.getNextHdvItemID(), price, amount,
									this.perso.getAccount().getGuid(), obj3);
							curHdv.addEntry(toAdd, false);
							SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this, '+', "", toAdd.parseToEmK());
							SocketManager.GAME_SEND_HDVITEM_SELLING(this.perso);
							Database.getStatique().getPlayerData().update(this.perso, true);
							break;
						}
						}
						return;
					}
					if (this.perso.getCurJobAction() != null) {
						if (!this.perso.getCurJobAction().isCraft()) {
							return;
						}
						if (packet.charAt(2) == 'O') {
							if (packet.charAt(3) == '+') {
								final String[] infos = packet.substring(4).split("\\|");
								try {
									final int guid = Integer.parseInt(infos[0]);
									int qua = Integer.parseInt(infos[1]);
									if (qua <= 0) {
										return;
									}
									if (!this.perso.hasItemGuid(guid)) {
										return;
									}
									final org.aestia.object.Object obj2 = World.getObjet(guid);
									if (obj2 == null) {
										return;
									}
									if (qua < 0) {
										return;
									}
									if (obj2.getQuantity() < qua) {
										qua = obj2.getQuantity();
									}
									this.perso.getCurJobAction().modifIngredient(this.perso, guid, qua);
									return;
								} catch (NumberFormatException e) {
									Console.println("Error Echange Metier '" + packet + "' => " + e.getMessage(),
											Console.Color.ERROR);
									e.printStackTrace();
									return;
								}
							}
							final String[] infos = packet.substring(4).split("\\|");
							try {
								final int guid = Integer.parseInt(infos[0]);
								final int qua = Integer.parseInt(infos[1]);
								if (qua <= 0) {
									return;
								}
								final org.aestia.object.Object obj2 = World.getObjet(guid);
								if (obj2 == null) {
									return;
								}
								this.perso.getCurJobAction().modifIngredient(this.perso, guid, -qua);
								return;
							} catch (NumberFormatException e) {
								Console.println("Error Echange Metier '" + packet + "' => " + e.getMessage(),
										Console.Color.ERROR);
								e.printStackTrace();
								return;
							}
						}
						if (packet.charAt(2) == 'R') {
							int c = 1;
							try {
								c = Integer.parseInt(packet.substring(3));
							} catch (Exception ex) {
							}
							this.perso.getCurJobAction().getJobCraft().setAction(c, this.perso);
						} else if (packet.charAt(2) == 'r' && this.perso.getCurJobAction() != null
								&& this.perso.getCurJobAction().getJobCraft() != null) {
							this.perso.getCurJobAction().broken = true;
						}
						return;
					}
				}
				if (this.perso.isInBank()) {
					if (this.perso.getCurExchange() != null) {
						return;
					}
					Label_4349: {
						switch (packet.charAt(2)) {
						case 'G': {
							if (Main.tradeAsBlocked) {
								return;
							}
							long kamas = 0L;
							try {
								kamas = Integer.parseInt(packet.substring(3));
							} catch (Exception e5) {
								Console.println("Error Echange Banque '" + packet + "' => " + e5.getMessage(),
										Console.Color.ERROR);
								e5.printStackTrace();
								return;
							}
							if (kamas == 0L) {
								return;
							}
							if (kamas > 0L) {
								if (this.perso.get_kamas() < kamas) {
									kamas = this.perso.get_kamas();
								}
								this.perso.setBankKamas(this.perso.getBankKamas() + kamas);
								this.perso.set_kamas(this.perso.get_kamas() - kamas);
								SocketManager.GAME_SEND_STATS_PACKET(this.perso);
								SocketManager.GAME_SEND_EsK_PACKET(this.perso, "G" + this.perso.getBankKamas());
								break;
							}
							kamas = -kamas;
							if (this.perso.getBankKamas() < kamas) {
								kamas = this.perso.getBankKamas();
							}
							this.perso.setBankKamas(this.perso.getBankKamas() - kamas);
							this.perso.set_kamas(this.perso.get_kamas() + kamas);
							SocketManager.GAME_SEND_STATS_PACKET(this.perso);
							SocketManager.GAME_SEND_EsK_PACKET(this.perso, "G" + this.perso.getBankKamas());
							break;
						}
						case 'O': {
							if (Main.tradeAsBlocked) {
								return;
							}
							int guid2 = 0;
							int qua2 = 0;
							try {
								guid2 = Integer.parseInt(packet.substring(4).split("\\|")[0]);
								qua2 = Integer.parseInt(packet.substring(4).split("\\|")[1]);
							} catch (Exception e7) {
								Console.println("Error Echange Banque '" + packet + "' => " + e7.getMessage(),
										Console.Color.ERROR);
								e7.printStackTrace();
								return;
							}
							if (guid2 == 0 || qua2 <= 0) {
								return;
							}
							switch (packet.charAt(3)) {
							case '+': {
								this.perso.addInBank(guid2, qua2);
								break Label_4349;
							}
							case '-': {
								this.perso.removeFromBank(guid2, qua2);
								break Label_4349;
							}
							}
							break;
						}
						}
					}
				} else if (this.perso.getInTrunk() != null) {
					if (Main.tradeAsBlocked) {
						return;
					}
					if (this.perso.getCurExchange() != null) {
						return;
					}
					final Trunk t = this.perso.getInTrunk();
					if (t == null) {
						return;
					}
					Label_4867: {
						switch (packet.charAt(2)) {
						case 'G': {
							long kamas2 = 0L;
							try {
								kamas2 = Integer.parseInt(packet.substring(3));
							} catch (Exception e3) {
								Console.println("Error Echange Coffre '" + packet + "' => " + e3.getMessage(),
										Console.Color.ERROR);
								e3.printStackTrace();
								return;
							}
							if (kamas2 == 0L) {
								return;
							}
							if (kamas2 > 0L) {
								if (this.perso.get_kamas() < kamas2) {
									kamas2 = this.perso.get_kamas();
								}
								t.setKamas(t.getKamas() + kamas2);
								this.perso.set_kamas(this.perso.get_kamas() - kamas2);
								SocketManager.GAME_SEND_STATS_PACKET(this.perso);
							} else {
								kamas2 = -kamas2;
								if (t.getKamas() < kamas2) {
									kamas2 = t.getKamas();
								}
								t.setKamas(t.getKamas() - kamas2);
								this.perso.set_kamas(this.perso.get_kamas() + kamas2);
								SocketManager.GAME_SEND_STATS_PACKET(this.perso);
							}
							for (final Player P : World.getOnlinePersos()) {
								if (P.getInTrunk() != null
										&& this.perso.getInTrunk().getId() == P.getInTrunk().getId()) {
									SocketManager.GAME_SEND_EsK_PACKET(P, "G" + t.getKamas());
								}
							}
							Database.getGame().getCoffreData().update(t);
							break;
						}
						case 'O': {
							int guid3 = 0;
							int qua4 = 0;
							try {
								guid3 = Integer.parseInt(packet.substring(4).split("\\|")[0]);
								qua4 = Integer.parseInt(packet.substring(4).split("\\|")[1]);
							} catch (Exception e6) {
								Console.println("Error Echange Coffre '" + packet + "' => " + e6.getMessage(),
										Console.Color.ERROR);
								e6.printStackTrace();
								return;
							}
							if (guid3 == 0 || qua4 <= 0) {
								return;
							}
							switch (packet.charAt(3)) {
							case '+': {
								t.addInTrunk(guid3, qua4, this.perso);
								break Label_4867;
							}
							case '-': {
								t.removeFromTrunk(guid3, qua4, this.perso);
								break Label_4867;
							}
							}
							break;
						}
						}
					}
				} else {
					if (this.perso.getCurExchange() == null) {
						return;
					}
					switch (packet.charAt(2)) {
					case 'O': {
						if (packet.charAt(3) == '+') {
							final String[] infos = packet.substring(4).split("\\|");
							try {
								final int guid = Integer.parseInt(infos[0]);
								int qua = Integer.parseInt(infos[1]);
								final int quaInExch = this.perso.getCurExchange().getQuaItem(guid, this.perso.getId());
								if (!this.perso.hasItemGuid(guid)) {
									return;
								}
								final org.aestia.object.Object obj = World.getObjet(guid);
								if (obj == null) {
									return;
								}
								if (qua > obj.getQuantity() - quaInExch) {
									qua = obj.getQuantity() - quaInExch;
								}
								if (qua <= 0) {
									return;
								}
								this.perso.getCurExchange().addItem(guid, qua, this.perso.getId());
								break;
							} catch (NumberFormatException e) {
								Console.println("Error Echange PvP '" + packet + "' => " + e.getMessage(),
										Console.Color.ERROR);
								e.printStackTrace();
								return;
							}
						}
						final String[] infos = packet.substring(4).split("\\|");
						try {
							final int guid = Integer.parseInt(infos[0]);
							final int qua = Integer.parseInt(infos[1]);
							if (qua <= 0) {
								return;
							}
							if (!this.perso.hasItemGuid(guid)) {
								return;
							}
							final org.aestia.object.Object obj2 = World.getObjet(guid);
							if (obj2 == null) {
								return;
							}
							if (qua > this.perso.getCurExchange().getQuaItem(guid, this.perso.getId())) {
								return;
							}
							this.perso.getCurExchange().removeItem(guid, qua, this.perso.getId());
							break;
						} catch (NumberFormatException e) {
							Console.println("Error Echange PvP '" + packet + "' => " + e.getMessage(),
									Console.Color.ERROR);
							e.printStackTrace();
						}
					}
					case 'G': {
						try {
							long numb = Integer.parseInt(packet.substring(3));
							if (this.perso.get_kamas() < numb) {
								numb = this.perso.get_kamas();
							}
							if (numb < 0L) {
								return;
							}
							this.perso.getCurExchange().setKamas(this.perso.getId(), numb);
						} catch (NumberFormatException e4) {
							Console.println("Error Echange PvP '" + packet + "' => " + e4.getMessage(),
									Console.Color.ERROR);
							e4.printStackTrace();
							return;
						}
						break;
					}
					}
				}
			}
		}
	}

	private synchronized void movementItemOrKamasDons(final String packet) {
		if (this.perso.getCurExchange() instanceof CraftSecure
				&& ((CraftSecure) this.perso.getCurExchange()).getNeeder() == this.perso) {
			final byte type = Byte.parseByte(String.valueOf(packet.charAt(0)));
			switch (packet.charAt(1)) {
			case 'O': {
				final String[] split = packet.substring(3).split("\\|");
				final boolean adding = packet.charAt(2) == '+';
				final int guid = Integer.parseInt(split[0]);
				final int quantity = Integer.parseInt(split[1]);
				((CraftSecure) this.perso.getCurExchange()).setPayItems(type, adding, guid, quantity);
				break;
			}
			case 'G': {
				((CraftSecure) this.perso.getCurExchange()).setPayKamas(type, Integer.parseInt(packet.substring(2)));
				break;
			}
			}
		}
	}

	private void askOfflineExchange() {
		if (this.perso.get_isTradingWith() > 0 || this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		if (this.perso.parseStoreItemsList().isEmpty()) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "123");
			return;
		}
		if (Config.contains(Config.arenaMap, this.perso.getCurMap().getId()) || this.perso.getCurMap().noMarchand) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "113");
			return;
		}
		if (this.perso.getCurMap().getId() == 33 || this.perso.getCurMap().getId() == 38
				|| this.perso.getCurMap().getId() == 4601 || this.perso.getCurMap().getId() == 4259
				|| this.perso.getCurMap().getId() == 8036 || this.perso.getCurMap().getId() == 10301) {
			if (this.perso.getCurMap().getStoreCount() >= 25) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "125;25");
				return;
			}
		} else if (this.perso.getCurMap().getStoreCount() >= 6) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "125;6");
			return;
		}
		synchronized (this.perso.getStoreItems()) {
			for (final Map.Entry<Integer, Integer> entry : this.perso.getStoreItems().entrySet()) {
				if (entry.getValue() <= 0) {
					this.kick();
					// monitorexit(this.perso.getStoreItems())
					return;
				}
			}
		}
		// monitorexit(this.perso.getStoreItems())
		final long Apayer = this.perso.storeAllBuy() / 1000;
		if (Apayer < 0L) {
			this.kick();
			return;
		}
		SocketManager.GAME_SEND_Eq_PACKET(this.perso, Apayer);
	}

	private void offlineExchange() {
		if (Config.contains(Config.arenaMap, this.perso.getCurMap().getId()) || this.perso.getCurMap().noMarchand) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "113");
			return;
		}
		if (this.perso.getCurMap().getId() == 33 || this.perso.getCurMap().getId() == 38
				|| this.perso.getCurMap().getId() == 4601 || this.perso.getCurMap().getId() == 4259
				|| this.perso.getCurMap().getId() == 8036 || this.perso.getCurMap().getId() == 10301) {
			if (this.perso.getCurMap().getStoreCount() >= 25) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "125;25");
				return;
			}
		} else if (this.perso.getCurMap().getStoreCount() >= 6) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "125;6");
			return;
		}
		final long Apayer2 = this.perso.storeAllBuy() / 1000;
		if (this.perso.get_kamas() < Apayer2) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "176");
			return;
		}
		if (Apayer2 < 0L) {
			SocketManager.GAME_SEND_MESSAGE(this.perso, "Erreur de mode marchand, la somme est n\u00e9gatif.");
			return;
		}
		final int orientation = Formulas.getRandomValue(1, 3);
		this.perso.set_kamas(this.perso.get_kamas() - Apayer2);
		this.perso.set_orientation(orientation);
		final org.aestia.map.Map map = this.perso.getCurMap();
		this.perso.setShowSeller(true);
		World.addSeller(this.perso);
		this.kick();
		for (final Player z : map.getPersos()) {
			if (z != null && z.isOnline()) {
				SocketManager.GAME_SEND_MERCHANT_LIST(z, z.getCurMap().getId());
			}
		}
	}

	private synchronized void putInInventory(String packet) {
		if (this.perso.getInMountPark() != null) {
			final char c = packet.charAt(2);
			packet = packet.substring(3);
			int guid = -1;
			final MountPark MP = this.perso.getCurMap().getMountPark();
			try {
				guid = Integer.parseInt(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch (c) {
			case 'C': {
				if (guid == -1 || !this.perso.hasItemGuid(guid)) {
					return;
				}
				final org.aestia.object.Object obj = World.getObjet(guid);
				final int DDid = obj.getStats().getEffect(995);
				Dragodinde DD = World.getDragoByID(-DDid);
				if (DD == null) {
					final int color = Constant.getMountColorByParchoTemplate(obj.getTemplate().getId());
					if (color < 1) {
						return;
					}
					DD = new Dragodinde(color, this.perso.getId(), false);
				}
				DD.setPerso(this.perso.getId());
				this.perso.removeItem(guid);
				World.removeItem(guid);
				if (!MP.getEtable().contains(DD)) {
					MP.getEtable().add(DD);
				}
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, obj.getGuid());
				if (DD.getFecundatedAgo() >= DD.minCalving() && DD.getFecundatedAgo() <= 1440) {
					int crias = Formulas.getRandomValue(1, 2);
					if (DD.getCapacites().contains(3)) {
						crias *= 2;
					}
					if (DD.getReprod() + crias > 20) {
						crias = 20 - DD.getReprod();
					}
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "1111;" + crias);
					final Dragodinde DragoPadre = World.getDragoByID(DD.getCouple());
					for (int i = 0; i < crias; ++i) {
						int color2;
						if (DragoPadre != null) {
							color2 = Constant.colorToEtable(DD.getColor(), DragoPadre.getColor());
						} else {
							color2 = Constant.colorToEtable(DD.getColor(), DD.getColor());
						}
						final Dragodinde Drago = new Dragodinde(color2, DD, DragoPadre);
						Database.getStatique().getMounts_dataData().add(Drago);
						SocketManager.GAME_SEND_Ee_PACKET(this.perso, '~', Drago.parseToDinde());
						DD.aumReprodution();
						MP.getEtable().add(Drago);
					}
					DD.resAmour(7500);
					DD.resResistence(7500);
					DD.setFecundadaHace(-1);
				} else if (DD.getFecundatedAgo() > 1440) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "1112");
					DD.aumReprodution();
					DD.resAmour(7500);
					DD.resResistence(7500);
					DD.setFecundadaHace(-1);
				}
				Database.getStatique().getMounts_dataData().update(DD, false);
				SocketManager.GAME_SEND_Ee_PACKET(this.perso, '+', DD.parseToDinde());
				break;
			}
			case 'c': {
				final Dragodinde DD2 = World.getDragoByID(guid);
				if (!MP.getEtable().contains(DD2) || DD2 == null) {
					return;
				}
				MP.getEtable().remove(DD2);
				DD2.setPerso(this.perso.getId());
				final ObjectTemplate OM = Constant.getParchoTemplateByMountColor(DD2.getColor());
				final org.aestia.object.Object obj2 = OM.createNewItem(1, false);
				World.addObjet(obj2, true);
				obj2.clearStats();
				obj2.getStats().addOneStat(995, -DD2.getId());
				obj2.getTxtStat().put(996, this.perso.getName());
				obj2.getTxtStat().put(997, DD2.getName());
				this.perso.addObjet(obj2);
				Database.getStatique().getMounts_dataData().update(DD2, false);
				SocketManager.GAME_SEND_Ow_PACKET(this.perso);
				SocketManager.GAME_SEND_Ee_PACKET(this.perso, '-',
						new StringBuilder(String.valueOf(DD2.getId())).toString());
				break;
			}
			case 'g': {
				final Dragodinde DD3 = World.getDragoByID(guid);
				if (!MP.getEtable().contains(DD3) || DD3 == null) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "1104");
					return;
				}
				if (this.perso.getMount() != null) {
					SocketManager.GAME_SEND_BN(this);
					return;
				}
				DD3.setPerso(this.perso.getId());
				MP.getEtable().remove(DD3);
				this.perso.setMount(DD3);
				Database.getStatique().getMounts_dataData().update(DD3, false);
				SocketManager.GAME_SEND_Re_PACKET(this.perso, "+", DD3);
				SocketManager.GAME_SEND_Ee_PACKET(this.perso, '-',
						new StringBuilder(String.valueOf(DD3.getId())).toString());
				SocketManager.GAME_SEND_Rx_PACKET(this.perso);
				break;
			}
			case 'p': {
				if (this.perso.getMount() == null || this.perso.getMount().getId() != guid) {
					break;
				}
				final Dragodinde DD4 = this.perso.getMount();
				int size = 0;
				synchronized (DD4.getItems()) {
					size = DD4.getItems().size();
				}
				// monitorexit(DD4.getItems())
				if (size == 0) {
					if (this.perso.isOnMount()) {
						this.perso.toogleOnMount();
					}
					if (!MP.getEtable().contains(DD4)) {
						MP.getEtable().add(DD4);
					}
					DD4.setPerso(this.perso.getId());
					this.perso.setMount(null);
					if (DD4.getFecundatedAgo() >= DD4.minCalving() && DD4.getFecundatedAgo() <= 1440) {
						int crias2 = Formulas.getRandomValue(1, 2);
						if (DD4.getCapacites().contains(3)) {
							crias2 *= 2;
						}
						if (DD4.getReprod() + crias2 > 20) {
							crias2 = 20 - DD4.getReprod();
						}
						SocketManager.GAME_SEND_Im_PACKET(this.perso, "1111;" + crias2);
						final Dragodinde DragoPadre2 = World.getDragoByID(DD4.getCouple());
						for (int j = 0; j < crias2; ++j) {
							int color3;
							if (DragoPadre2 != null) {
								color3 = Constant.colorToEtable(DD4.getColor(), DragoPadre2.getColor());
							} else {
								color3 = Constant.colorToEtable(DD4.getColor(), DD4.getColor());
							}
							final Dragodinde Drago2 = new Dragodinde(color3, DD4, DragoPadre2);
							Database.getStatique().getMounts_dataData().add(Drago2);
							SocketManager.GAME_SEND_Ee_PACKET(this.perso, '~', Drago2.parseToDinde());
							DD4.aumReprodution();
							MP.getEtable().add(Drago2);
						}
						DD4.resAmour(7500);
						DD4.resResistence(7500);
						DD4.setFecundadaHace(-1);
					} else if (DD4.getFecundatedAgo() > 1440) {
						SocketManager.GAME_SEND_Im_PACKET(this.perso, "1112");
						DD4.aumReprodution();
						DD4.resAmour(7500);
						DD4.resResistence(7500);
						DD4.setFecundadaHace(-1);
					}
					Database.getStatique().getMounts_dataData().update(DD4, false);
					SocketManager.GAME_SEND_Ee_PACKET(this.perso, '+', DD4.parseToDinde());
					SocketManager.GAME_SEND_Re_PACKET(this.perso, "-", null);
					SocketManager.GAME_SEND_Rx_PACKET(this.perso);
					break;
				}
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "1106");
				break;
			}
			}
		}
	}

	private synchronized void putInMountPark(String packet) {
		if (this.perso.getInMountPark() != null) {
			final char c = packet.charAt(2);
			packet = packet.substring(3);
			int id = -1;
			final MountPark MP = this.perso.getCurMap().getMountPark();
			try {
				id = Integer.parseInt(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch (c) {
			case 'g': {
				final Dragodinde DD3 = World.getDragoByID(id);
				if (!MP.getEtable().contains(DD3)) {
					MP.getEtable().add(DD3);
				}
				DD3.setPerso(this.perso.getId());
				this.perso.getCurMap().getMountPark().delRaising(id);
				SocketManager.GAME_SEND_Ef_MOUNT_TO_ETABLE(this.perso, '-',
						new StringBuilder(String.valueOf(DD3.getId())).toString());
				if (DD3.getFecundatedAgo() >= DD3.minCalving() && DD3.getFecundatedAgo() <= 1440) {
					int crias = Formulas.getRandomValue(1, 2);
					if (DD3.getCapacites().contains(3)) {
						crias *= 2;
					}
					if (DD3.getReprod() + crias > 20) {
						crias = 20 - DD3.getReprod();
					}
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "1111;" + crias);
					final Dragodinde DragoPadre = World.getDragoByID(DD3.getCouple());
					for (int i = 0; i < crias; ++i) {
						int color;
						if (DragoPadre != null) {
							color = Constant.colorToEtable(DD3.getColor(), DragoPadre.getColor());
						} else {
							color = Constant.colorToEtable(DD3.getColor(), DD3.getColor());
						}
						final Dragodinde Drago = new Dragodinde(color, DD3, DragoPadre);
						Database.getStatique().getMounts_dataData().add(Drago);
						SocketManager.GAME_SEND_Ee_PACKET(this.perso, '~', Drago.parseToDinde());
						DD3.aumReprodution();
						MP.getEtable().add(Drago);
					}
					DD3.resAmour(7500);
					DD3.resResistence(7500);
					DD3.setFecundadaHace(-1);
				} else if (DD3.getFecundatedAgo() > 1440) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "1112");
					DD3.aumReprodution();
					DD3.resAmour(7500);
					DD3.resResistence(7500);
					DD3.setFecundadaHace(-1);
				}
				SocketManager.GAME_SEND_Ee_PACKET(this.perso, '+', DD3.parseToDinde());
				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), id);
				DD3.setMapCell((short) (-1), -1);
				break;
			}
			case 'p': {
				final org.aestia.map.Map map = this.perso.getCurMap();
				if (this.perso.getMount() != null) {
					synchronized (this.perso.getMount().getItems()) {
						if (this.perso.getMount().getItems().size() != 0) {
							SocketManager.GAME_SEND_Im_PACKET(this.perso, "1106");
							// monitorexit(this.perso.getMount().getItems())
							return;
						}
					}
					// monitorexit(this.perso.getMount().getItems())
				}
				if (map.getMountPark().getListOfRaising().size() >= map.getMountPark().getSize()) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "1107");
					return;
				}
				if (this.perso.getMount() != null && this.perso.getMount().getId() == id) {
					if (this.perso.isOnMount()) {
						this.perso.toogleOnMount();
					}
					if (this.perso.isOnMount()) {
						return;
					}
					this.perso.setMount(null);
				}
				final Dragodinde DD4 = World.getDragoByID(id);
				DD4.setPerso(this.perso.getId());
				MP.getEtable().remove(DD4);
				map.getMountPark().addRaising(id);
				SocketManager.GAME_SEND_Ef_MOUNT_TO_ETABLE(this.perso, '+', DD4.parseToDinde());
				SocketManager.GAME_SEND_Ee_PACKET(this.perso, '-',
						new StringBuilder(String.valueOf(DD4.getId())).toString());
				SocketManager.GAME_SEND_GM_MOUNT_TO_MAP(map, DD4);
				DD4.setMapCell(map.getId(), map.getMountPark().getPlaceOfSpawn());
				break;
			}
			}
		}
	}

	private void request(final String packet) {
		if (packet.substring(2, 4).equals("13")) {
			try {
				final String[] split = packet.split("\\|");
				final int id = Integer.parseInt(split[1]);
				final int skill = Integer.parseInt(split[2]);
				final Player player = World.getPersonnage(id);
				if (player == null) {
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
					return;
				}
				if (player.getCurMap() != this.perso.getCurMap() || !player.isOnline()) {
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
					return;
				}
				if (player.is_away() || this.perso.is_away() || player.get_isTradingWith() != 0) {
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'O');
					return;
				}
				final ArrayList<Job> jobs = player.getJobs();
				if (jobs == null) {
					return;
				}
				final org.aestia.object.Object object = player.getObjetByPos(1);
				if (object == null) {
					this.perso.send("BN");
					return;
				}
				boolean ok = false;
				for (final Job job : jobs) {
					if (job.getSkills().isEmpty()) {
						continue;
					}
					if (!job.isValidTool(object.getTemplate().getId())) {
						continue;
					}
					for (final Case cell : this.perso.getCurMap().getCases().values()) {
						if (cell.getObject() != null && cell.getObject().getTemplate() != null) {
							final int io = cell.getObject().getTemplate().getId();
							final ArrayList<Integer> skills = job.getSkills().get(io);
							if (skills == null) {
								continue;
							}
							for (final int arg : skills) {
								if (arg == skill && Pathfinding.getDistanceBetween(player.getCurMap(),
										player.getCurCell().getId(), cell.getId()) < 4) {
									ok = true;
									break;
								}
							}
						}
					}
					if (ok) {
						break;
					}
				}
				if (!ok) {
					this.perso.send("ERET");
					return;
				}
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_OK(this, this.perso.getId(), id, 12);
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_OK(player.getGameClient(), this.perso.getId(), id, 12);
				this.perso.set_isTradingWith(id);
				this.perso.getIsCraftingType().add(13);
				player.getIsCraftingType().add(12);
				this.perso.getIsCraftingType().add(skill);
				player.getIsCraftingType().add(skill);
				player.set_isTradingWith(this.perso.getId());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			return;
		}
		if (packet.substring(2, 4).equals("12")) {
			try {
				final String[] split = packet.split("\\|");
				final int id = Integer.parseInt(split[1]);
				final int skill = Integer.parseInt(split[2]);
				final Player player = World.getPersonnage(id);
				if (player == null) {
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
					return;
				}
				if (player.getCurMap() != this.perso.getCurMap() || !player.isOnline()) {
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
					return;
				}
				if (player.is_away() || this.perso.is_away() || player.get_isTradingWith() != 0) {
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'O');
					return;
				}
				final ArrayList<Job> jobs = this.perso.getJobs();
				if (jobs == null) {
					return;
				}
				final org.aestia.object.Object object = this.perso.getObjetByPos(1);
				if (object == null) {
					return;
				}
				boolean ok = false;
				for (final Job job : jobs) {
					if (job.getSkills().isEmpty()) {
						continue;
					}
					if (!job.isValidTool(object.getTemplate().getId())) {
						continue;
					}
					for (final Case cell : this.perso.getCurMap().getCases().values()) {
						if (cell.getObject() != null && cell.getObject().getTemplate() != null) {
							final int io = cell.getObject().getTemplate().getId();
							final ArrayList<Integer> skills = job.getSkills().get(io);
							if (skills == null) {
								continue;
							}
							for (final int arg : skills) {
								if (arg == skill && Pathfinding.getDistanceBetween(this.perso.getCurMap(),
										this.perso.getCurCell().getId(), cell.getId()) < 4) {
									ok = true;
									break;
								}
							}
						}
					}
					if (ok) {
						break;
					}
				}
				if (!ok) {
					this.perso.sendMessage("Tu es trop loin de l'atelier.");
					return;
				}
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_OK(this, this.perso.getId(), id, 12);
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_OK(player.getGameClient(), this.perso.getId(), id, 13);
				this.perso.set_isTradingWith(id);
				this.perso.getIsCraftingType().add(12);
				player.getIsCraftingType().add(13);
				this.perso.getIsCraftingType().add(skill);
				player.getIsCraftingType().add(skill);
				player.set_isTradingWith(this.perso.getId());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			return;
		}
		if (packet.substring(2, 4).equals("11")) {
			if (this.perso.get_isTradingWith() < 0) {
				SocketManager.GAME_SEND_EV_PACKET(this);
			}
			if (this.perso.getDeshonor() >= 5) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "183");
				return;
			}
			final Hdv toOpen = World.getHdv(this.perso.getCurMap().getId());
			if (toOpen == null) {
				return;
			}
			final String info = "1,10,100;" + toOpen.getStrCategory() + ";" + toOpen.parseTaxe() + ";"
					+ toOpen.getLvlMax() + ";" + toOpen.getMaxAccountItem() + ";-1;" + toOpen.getSellTime();
			SocketManager.GAME_SEND_ECK_PACKET(this.perso, 11, info);
			this.perso.set_isTradingWith(0 - this.perso.getCurMap().getId());
		} else {
			if (packet.substring(2, 4).equals("15")) {
				try {
					final Dragodinde monture = this.perso.getMount();
					final int idMontura = monture.getId();
					SocketManager.GAME_SEND_ECK_PACKET(this, 15,
							new StringBuilder(String.valueOf(this.perso.getMount().getId())).toString());
					SocketManager.GAME_SEND_EL_MOUNT_PACKET(this.perso, monture);
					SocketManager.GAME_SEND_Ew_PACKET(this.perso, monture.get_podsActuels(), monture.getMaxPod());
					this.perso.set_isTradingWith(idMontura);
					this.perso.setInDD(true);
					this.perso.set_away(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				return;
			}
			if (packet.substring(2, 4).equals("17")) {
				try {
					if (this.perso.get_isTradingWith() > 0 || this.perso.get_fight() != null || this.perso.is_away()) {
						return;
					}
					final int i1 = Integer.parseInt(packet.substring(5));
					if (this.perso.getCurMap().getNpc(i1) != null) {
						final PlayerExchange.NpcRessurectPets ech = new PlayerExchange.NpcRessurectPets(this.perso,
								this.perso.getCurMap().getNpc(i1).getTemplate());
						this.perso.setCurNpcRessurectPets(ech);
						this.perso.set_isTradingWith(i1);
						this.perso.set_away(true);
						SocketManager.GAME_SEND_ECK_PACKET(this.perso, 9,
								new StringBuilder(String.valueOf(i1)).toString());
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (packet.substring(2, 4).equals("10")) {
				if (this.perso.get_isTradingWith() < 0) {
					SocketManager.GAME_SEND_EV_PACKET(this);
				}
				if (this.perso.getDeshonor() >= 5) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "183");
					return;
				}
				final Hdv toOpen = World.getHdv(this.perso.getCurMap().getId());
				if (toOpen == null) {
					return;
				}
				final String info = "1,10,100;" + toOpen.getStrCategory() + ";" + toOpen.parseTaxe() + ";"
						+ toOpen.getLvlMax() + ";" + toOpen.getMaxAccountItem() + ";-1;" + toOpen.getSellTime();
				SocketManager.GAME_SEND_ECK_PACKET(this.perso, 10, info);
				this.perso.set_isTradingWith(0 - World.changeHdv(this.perso.getCurMap().getId()));
				SocketManager.GAME_SEND_HDVITEM_SELLING(this.perso);
				return;
			}
			switch (packet.charAt(2)) {
			case '0': {
				try {
					final int npcID = Integer.parseInt(packet.substring(4));
					final Npc npc = this.perso.getCurMap().getNpc(npcID);
					if (npc == null) {
						return;
					}
					SocketManager.GAME_SEND_ECK_PACKET(this, 0, new StringBuilder(String.valueOf(npcID)).toString());
					SocketManager.GAME_SEND_ITEM_VENDOR_LIST_PACKET(this, npc);
					this.perso.set_isTradingWith(npcID);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				break;
			}
			case '1': {
				try {
					final int guidTarget = Integer.parseInt(packet.substring(4));
					final Player target = World.getPersonnage(guidTarget);
					if (target == null) {
						SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
						return;
					}
					if (target.getCurMap() != this.perso.getCurMap() || !target.isOnline()) {
						SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
						return;
					}
					if (target.is_away() || this.perso.is_away() || target.get_isTradingWith() != 0) {
						SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'O');
						return;
					}
					if (target.getGroupe() != null && this.perso.getGroupe() == null
							&& !target.getGroupe().isPlayer()) {
						SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'E');
						return;
					}
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_OK(this, this.perso.getId(), guidTarget, 1);
					SocketManager.GAME_SEND_EXCHANGE_REQUEST_OK(target.getGameClient(), this.perso.getId(), guidTarget,
							1);
					this.perso.set_isTradingWith(guidTarget);
					this.perso.getIsCraftingType().add(1);
					target.getIsCraftingType().add(1);
					target.set_isTradingWith(this.perso.getId());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				break;
			}
			case '2': {
				try {
					if (this.perso.get_isTradingWith() > 0 || this.perso.get_fight() != null || this.perso.is_away()) {
						return;
					}
					final int i1 = Integer.parseInt(packet.substring(4));
					if (this.perso.getCurMap().getNpc(i1) != null) {
						final PlayerExchange.NpcExchange ech2 = new PlayerExchange.NpcExchange(this.perso,
								this.perso.getCurMap().getNpc(i1).getTemplate());
						this.perso.setCurNpcExchange(ech2);
						this.perso.set_isTradingWith(this.perso.getCurMap().getNpc(i1).getTemplate().get_id());
						this.perso.set_away(true);
						SocketManager.GAME_SEND_ECK_PACKET(this.perso, 2,
								new StringBuilder(String.valueOf(i1)).toString());
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			}
			case '4': {
				int pID = 0;
				try {
					pID = Integer.valueOf(packet.split("\\|")[1]);
				} catch (NumberFormatException e3) {
					e3.printStackTrace();
					return;
				}
				if (this.perso.get_isTradingWith() > 0 || this.perso.get_fight() != null || this.perso.is_away()) {
					return;
				}
				final Player seller = World.getPersonnage(pID);
				if (seller == null) {
					return;
				}
				if (!seller.isShowSeller() || seller.getCurMap() != this.perso.getCurMap()) {
					return;
				}
				this.perso.set_isTradingWith(pID);
				SocketManager.GAME_SEND_ECK_PACKET(this.perso, 4,
						new StringBuilder(String.valueOf(seller.getId())).toString());
				SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(seller, this.perso);
				break;
			}
			case '6': {
				if (this.perso.get_isTradingWith() > 0 || this.perso.get_fight() != null || this.perso.is_away()) {
					return;
				}
				this.perso.set_isTradingWith(this.perso.getId());
				SocketManager.GAME_SEND_ECK_PACKET(this.perso, 6, "");
				SocketManager.GAME_SEND_ITEM_LIST_PACKET_SELLER(this.perso, this.perso);
				break;
			}
			case '8': {
				try {
					final int CollectorID = Integer.parseInt(packet.substring(4));
					final Collector Collector = World.getCollector(CollectorID);
					if (Collector == null || Collector.getInFight() > 0 || Collector.getExchange()
							|| Collector.getGuildId() != this.perso.get_guild().getId()) {
						return;
					}
					if (Collector.getMap() != this.perso.getCurMap().getId()) {
						return;
					}
					if (!this.perso.getGuildMember().canDo(Constant.G_COLLPERCO)) {
						SocketManager.GAME_SEND_Im_PACKET(this.perso, "1101");
						return;
					}
					Collector.setExchange(true);
					SocketManager.GAME_SEND_ECK_PACKET(this, 8,
							new StringBuilder(String.valueOf(Collector.getId())).toString());
					SocketManager.GAME_SEND_ITEM_LIST_PACKET_PERCEPTEUR(this, Collector);
					this.perso.set_isTradingWith(Collector.getId());
					this.perso.set_isOnCollectorID(Collector.getId());
					this.perso.DialogTimer();
				} catch (NumberFormatException e4) {
					e4.printStackTrace();
				}
				break;
			}
			case '9': {
				try {
					if (this.perso.get_isTradingWith() <= 0 && this.perso.get_fight() == null && this.perso.is_away()) {
						return;
					}
					final int i2 = Integer.parseInt(packet.substring(4));
					if (this.perso.getCurMap().getNpc(i2) != null) {
						final PlayerExchange.NpcExchangePets ech3 = new PlayerExchange.NpcExchangePets(this.perso,
								this.perso.getCurMap().getNpc(i2).getTemplate());
						this.perso.setCurNpcExchangePets(ech3);
						this.perso.set_isTradingWith(i2);
						this.perso.set_away(true);
						SocketManager.GAME_SEND_ECK_PACKET(this.perso, 9,
								new StringBuilder(String.valueOf(i2)).toString());
					}
				} catch (Exception e5) {
					e5.printStackTrace();
				}
				break;
			}
			}
		}
	}

	private void sell(final String packet) {
		try {
			final String[] infos = packet.substring(2).split("\\|");
			final int guid = Integer.parseInt(infos[0]);
			final int qua = Integer.parseInt(infos[1]);
			if (!this.perso.hasItemGuid(guid)) {
				SocketManager.GAME_SEND_SELL_ERROR_PACKET(this);
				return;
			}
			this.perso.sellItem(guid, qua);
		} catch (Exception e) {
			e.printStackTrace();
			SocketManager.GAME_SEND_SELL_ERROR_PACKET(this);
		}
	}

	private void bookOfArtisant(final String packet) {
		switch (packet.charAt(2)) {
		case 'F': {
			final int Metier = Integer.parseInt(packet.substring(3));
			int cant = 0;
			for (final Player artissant : World.getOnlinePersos()) {
				if (artissant.getMetiers().isEmpty()) {
					continue;
				}
				String send = "";
				final int id = artissant.getId();
				final String name = artissant.getName();
				final String color = String.valueOf(artissant.getColor1()) + "," + artissant.getColor2() + ","
						+ artissant.getColor3();
				final String accesoire = artissant.getGMStuffString();
				final int sex = artissant.getSexe();
				final int map = artissant.getCurMap().getId();
				final int inJob = (map == 8731 || map == 8732) ? 1 : 0;
				final int classe = artissant.getClasse();
				for (final JobStat SM : artissant.getMetiers().values()) {
					if (SM.getTemplate().getId() != Metier) {
						continue;
					}
					++cant;
					send = "+" + SM.getTemplate().getId() + ";" + id + ";" + name + ";" + SM.get_lvl() + ";" + map + ";"
							+ inJob + ";" + classe + ";" + sex + ";" + color + ";" + accesoire + ";"
							+ SM.getOptBinValue() + "," + SM.getSlotsPublic();
					SocketManager.SEND_EJ_LIVRE(this.perso, send);
				}
			}
			if (cant == 0) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Dans ces moments il n'y a pas d'artisan disponible du m\u00e9tier que tu cherches.");
				break;
			}
			break;
		}
		}
	}

	private void setPublicMode(final String packet) {
		switch (packet.charAt(2)) {
		case '+': {
			this.perso.setMetierPublic(true);
			String metier = "";
			boolean first = false;
			for (final JobStat SM : this.perso.getMetiers().values()) {
				SocketManager.SEND_Ej_LIVRE(this.perso, "+" + SM.getTemplate().getId());
				if (first) {
					metier = String.valueOf(metier) + ";";
				}
				metier = String.valueOf(metier) + JobConstant.actionMetier(SM.getTemplate().getId());
				first = true;
			}
			SocketManager.SEND_EW_METIER_PUBLIC(this.perso, "+");
			SocketManager.SEND_EW_METIER_PUBLIC(this.perso, "+" + this.perso.getId() + "|" + metier);
			break;
		}
		case '-': {
			this.perso.setMetierPublic(false);
			for (final JobStat metiers : this.perso.getMetiers().values()) {
				SocketManager.SEND_Ej_LIVRE(this.perso, "-" + metiers.getTemplate().getId());
			}
			SocketManager.SEND_EW_METIER_PUBLIC(this.perso, "-");
			SocketManager.SEND_EW_METIER_PUBLIC(this.perso, "-" + this.perso.getId());
			break;
		}
		}
	}

	private void leaveExchange() {
		if (perso.boutique) {
			perso.boutique = false;
			SocketManager.GAME_SEND_EV_PACKET(this);
			return;
		}
		if (this.perso.get_isTradingWith() == 0 && this.perso.getCurExchange() == null
				&& this.perso.getCurJobAction() == null && this.perso.getInMountPark() == null && !this.perso.isInBank()
				&& this.perso.get_isOnCollectorID() == 0 && !this.perso.getLivreArtisant()
				&& this.perso.getConcasseur() == null && this.perso.getCurNpcExchangePets() == null
				&& this.perso.getCurNpcExchange() == null && this.perso.getInTrunk() == null) {
			return;
		}
		if (this.perso.getCurExchange() != null) {
			this.perso.getCurExchange().cancel();
			this.perso.set_away(false);
			return;
		}
		if (this.perso.getCurNpcExchangePets() != null) {
			this.perso.getCurNpcExchangePets().cancel();
			this.perso.set_away(false);
			return;
		}
		if (this.perso.getCurNpcExchange() != null) {
			this.perso.getCurNpcExchange().cancel();
			this.perso.set_away(false);
			return;
		}
		if (this.perso.getCurJobAction() != null) {
			this.perso.setDoAction(false);
			this.perso.getCurJobAction().resetCraft();
		}
		if (this.perso.getConcasseur() != null) {
			this.perso.set_away(false);
			this.perso.setConcasseur(null);
			SocketManager.GAME_SEND_EV_PACKET(this);
			return;
		}
		if (this.perso.getInDD()) {
			this.perso.set_away(false);
			this.perso.setInDD(false);
			SocketManager.GAME_SEND_EV_PACKET(this);
			return;
		}
		if (this.perso.getInMountPark() != null) {
			this.perso.leftMountPark();
		}
		if (this.perso.get_isTradingWith() > 0) {
			final Player p = World.getPersonnage(this.perso.get_isTradingWith());
			if (p != null && p.isOnline()) {
				final GameClient out = p.getGameClient();
				SocketManager.GAME_SEND_EV_PACKET(out);
				p.set_isTradingWith(0);
			}
			this.perso.setLivreArtisant(false);
			this.perso.set_isTradingWith(0);
			this.perso.set_away(false);
			this.perso.setCurJobAction(null);
			SocketManager.GAME_SEND_EV_PACKET(this);
			return;
		}
		if (this.perso.get_isOnCollectorID() != 0) {
			final Collector Collector = World.getCollector(this.perso.get_isOnCollectorID());
			if (Collector == null) {
				return;
			}
			for (final Player z : World.getGuild(Collector.getGuildId()).getMembers()) {
				if (z == null) {
					continue;
				}
				if (!z.isOnline()) {
					continue;
				}
				SocketManager.GAME_SEND_gITM_PACKET(z, org.aestia.entity.Collector.parseToGuild(z.get_guild().getId()));
				String str = "";
				str = String.valueOf(str) + "G" + Integer.toString(Collector.getN1(), 36) + ","
						+ Integer.toString(Collector.getN2(), 36);
				str = String.valueOf(str) + "|.|" + World.getMap(Collector.getMap()).getX() + "|"
						+ World.getMap(Collector.getMap()).getY() + "|";
				str = String.valueOf(str) + this.perso.getName() + "|";
				str = String.valueOf(str) + Collector.getXp();
				if (!Collector.getLogObjects().equals("")) {
					str = String.valueOf(str) + Collector.getLogObjects();
				}
				SocketManager.GAME_SEND_gT_PACKET(z, str);
			}
			this.perso.getGuildMember().giveXpToGuild(Collector.getXp());
			this.perso.getCurMap().RemoveNpc(Collector.getId());
			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), Collector.getId());
			Collector.reloadTimer();
			Collector.delCollector(Collector.getId());
			Database.getGame().getPercepteurData().delete(Collector.getId());
			this.perso.set_isOnCollectorID(0);
			this.perso.dialogTimer = null;
		}
		Database.getStatique().getPlayerData().update(this.perso, true);
		this.perso.set_isTradingWith(0);
		this.perso.set_away(false);
		this.perso.setInBank(false);
		if (this.perso.getInTrunk() != null) {
			this.perso.getInTrunk().setPlayer(null);
		}
		this.perso.setInTrunk(null);
		this.perso.set_isOnCollectorID(0);
		this.perso.setCurJobAction(null);
		this.perso.setLivreArtisant(false);
		SocketManager.GAME_SEND_EV_PACKET(this);
	}

	private void parseEnvironementPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'D': {
			this.setDirection(packet);
			break;
		}
		case 'U': {
			this.useEmote(packet);
			break;
		}
		}
	}

	private void setDirection(final String packet) {
		try {
			if (this.perso.get_fight() != null || this.perso.isDead() == 1) {
				return;
			}
			final int dir = Integer.parseInt(packet.substring(2));
			if (dir > 7 || dir < 0) {
				return;
			}
			this.perso.set_orientation(dir);
			SocketManager.GAME_SEND_eD_PACKET_TO_MAP(this.perso.getCurMap(), this.perso.getId(), dir);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void useEmote(final String packet) {
		int emote = -1;
		try {
			emote = Integer.parseInt(packet.substring(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (emote == -1) {
			return;
		}
		if (this.perso == null) {
			return;
		}
		if (this.perso.get_fight() != null) {
			return;
		}
		if (!this.perso.getStaticEmote().containsKey(emote)) {
			return;
		}
		if (emote != 1 || (emote != 19 && this.perso.isSitted())) {
			this.perso.setSitted(false);
		}
		switch (emote) {
		case 1:
		case 19:
		case 20: {
			this.perso.setSitted(!this.perso.isSitted());
			break;
		}
		}
		if (this.perso.emoteActive() == 1 || this.perso.emoteActive() == 19 || this.perso.emoteActive() == 21) {
			this.perso.setEmoteActive(0);
		} else {
			this.perso.setEmoteActive(emote);
		}
		final MountPark MP = this.perso.getCurMap().getMountPark();
		SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(this.perso.getCurMap(), this.perso.getId(), this.perso.emoteActive());
		if ((emote == 2 || emote == 4 || emote == 3 || emote == 6 || emote == 8 || emote == 10) && MP != null) {
			final ArrayList<Dragodinde> Mounts = new ArrayList<Dragodinde>();
			for (final Integer DD : MP.getListOfRaising()) {
				if (World.getDragoByID(DD).getPerso() == this.perso.getId()) {
					Mounts.add(World.getDragoByID(DD));
				}
			}
			if (Mounts.size() > 0) {
				int cells = 0;
				switch (emote) {
				case 2:
				case 4: {
					cells = 1;
					break;
				}
				case 3:
				case 8: {
					cells = Formulas.getRandomValue(2, 3);
					break;
				}
				case 6:
				case 10: {
					cells = Formulas.getRandomValue(4, 7);
					break;
				}
				}
				final boolean remove = emote != 2 && emote != 3 && emote != 10;
				final Dragodinde dd = Mounts.get(Formulas.getRandomValue(0, Mounts.size() - 1));
				dd.moveMounts(this.perso, cells, remove);
			}
		}
	}

	private void parseFrienDDacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			this.addFriend(packet);
			break;
		}
		case 'D': {
			this.removeFriend(packet);
			break;
		}
		case 'L': {
			SocketManager.GAME_SEND_FRIENDLIST_PACKET(this.perso);
			break;
		}
		case 'O': {
			switch (packet.charAt(2)) {
			case '-': {
				this.perso.SetSeeFriendOnline(false);
				SocketManager.GAME_SEND_BN(this.perso);
				break;
			}
			case '+': {
				this.perso.SetSeeFriendOnline(true);
				SocketManager.GAME_SEND_BN(this.perso);
				break;
			}
			}
			break;
		}
		case 'J': {
			this.joinWife(packet);
			break;
		}
		}
	}

	private void addFriend(String packet) {
		if (this.perso == null) {
			return;
		}
		int guid = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			final Player P = World.getPersoByName(packet);
			if (P != null) {
				if (P.isOnline()) {
					guid = P.getAccID();
					break;
				}
			}
			SocketManager.GAME_SEND_FA_PACKET(this.perso, "Ef");
			return;
		}
		case '*': {
			packet = packet.substring(3);
			final Account C = World.getCompteByPseudo(packet);
			if (C != null) {
				if (C.isOnline()) {
					guid = C.getGuid();
					break;
				}
			}
			SocketManager.GAME_SEND_FA_PACKET(this.perso, "Ef");
			return;
		}
		default: {
			packet = packet.substring(2);
			final Player Pr = World.getPersoByName(packet);
			if (Pr != null) {
				if (Pr.isOnline()) {
					guid = Pr.getAccount().getGuid();
					break;
				}
			}
			SocketManager.GAME_SEND_FA_PACKET(this.perso, "Ef");
			return;
		}
		}
		if (guid == -1) {
			SocketManager.GAME_SEND_FA_PACKET(this.perso, "Ef");
			return;
		}
		this.compte.addFriend(guid);
	}

	private void removeFriend(String packet) {
		if (this.perso == null) {
			return;
		}
		int guid = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			final Player P = World.getPersoByName(packet);
			if (P == null) {
				SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
				return;
			}
			guid = P.getAccID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			final Account C = World.getCompteByPseudo(packet);
			if (C == null) {
				SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
				return;
			}
			guid = C.getGuid();
			break;
		}
		default: {
			packet = packet.substring(2);
			final Player Pr = World.getPersoByName(packet);
			if (Pr != null) {
				if (Pr.isOnline()) {
					guid = Pr.getAccount().getGuid();
					break;
				}
			}
			SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
			return;
		}
		}
		if (guid == -1 || !this.compte.isFriendWith(guid)) {
			SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
			return;
		}
		this.compte.removeFriend(guid);
	}

	private void joinWife(final String packet) {
		final Player Wife = World.getPersonnage(this.perso.getWife());
		if (Wife == null) {
			return;
		}
		if (!Wife.isOnline()) {
			if (Wife.getSexe() == 0) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "140");
			} else {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "139");
			}
			SocketManager.GAME_SEND_FRIENDLIST_PACKET(this.perso);
			return;
		}
		switch (packet.charAt(2)) {
		case 'S': {
			if (Wife.getCurMap().noTP || Wife.getCurMap().haveMobFix()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Une barri\u00e8re magique vous emp\u00e8che de rejoindre votre conjoint.");
				return;
			}
			if (this.perso.get_fight() != null) {
				return;
			}
			this.perso.meetWife(Wife);
			break;
		}
		case 'C': {
			if (packet.charAt(3) == '+') {
				if (this.perso._Follows != null) {
					this.perso._Follows._Follower.remove(this.perso.getId());
				}
				SocketManager.GAME_SEND_FLAG_PACKET(this.perso, Wife);
				this.perso._Follows = Wife;
				Wife._Follower.put(this.perso.getId(), this.perso);
				break;
			}
			SocketManager.GAME_SEND_DELETE_FLAG_PACKET(this.perso);
			this.perso._Follows = null;
			Wife._Follower.remove(this.perso.getId());
			break;
		}
		}
	}

	private void parseFightPacket(final String packet) {
		try {
			switch (packet.charAt(1)) {
			case 'D': {
				int key = -1;
				try {
					key = Integer.parseInt(packet.substring(2).replace("0", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (key == -1) {
					return;
				}
				SocketManager.GAME_SEND_FIGHT_DETAILS(this, this.perso.getCurMap().getFights().get(key));
				break;
			}
			case 'H': {
				if (this.perso.get_fight() == null) {
					return;
				}
				this.perso.get_fight().toggleHelp(this.perso.getId());
				break;
			}
			case 'L': {
				SocketManager.GAME_SEND_FIGHT_LIST_PACKET(this, this.perso.getCurMap());
				break;
			}
			case 'N': {
				if (this.perso.get_fight() == null) {
					return;
				}
				this.perso.get_fight().toggleLockTeam(this.perso.getId());
				break;
			}
			case 'P': {
				if (this.perso.get_fight() == null || this.perso.getGroup() == null) {
					return;
				}
				this.perso.get_fight().toggleOnlyGroup(this.perso.getId());
				break;
			}
			case 'S': {
				if (this.perso.get_fight() == null) {
					return;
				}
				this.perso.get_fight().toggleLockSpec(this.perso.getId());
				break;
			}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	private void parseGamePacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			if (this.perso == null) {
				return;
			}
			this.sendActions(packet);
			break;
		}
		case 'C': {
			if (this.perso == null) {
				return;
			}
			this.perso.sendGameCreate();
			break;
		}
		case 'd': {
			this.showMonsterTarget(packet);
			break;
		}
		case 'f': {
			this.setFlag(packet);
			break;
		}
		case 'F': {
			this.perso.set_Ghosts();
			break;
		}
		case 'I': {
			this.getExtraInformations();
			break;
		}
		case 'K': {
			this.actionAck(packet);
			break;
		}
		case 'P': {
			this.perso.toggleWings(packet.charAt(2));
			break;
		}
		case 'p': {
			this.setPlayerPosition(packet);
			break;
		}
		case 'Q': {
			this.leaveFight(packet);
			break;
		}
		case 'R': {
			this.readyFight(packet);
			break;
		}
		case 't': {
			if (this.perso.get_fight() == null) {
				return;
			}
			this.perso.get_fight().playerPass(this.perso);
			break;
		}
		}
	}

	private synchronized void sendActions(final String packet) {
		if (this.perso.getDoAction()) {
			SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
			return;
		}
		int actionID;
		try {
			actionID = Integer.parseInt(packet.substring(2, 5));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		int nextGameActionID = 0;
		if (this.actions.size() > 0) {
			nextGameActionID = (int) this.actions.keySet().toArray()[this.actions.size() - 1] + 1;
		}
		final GameAction GA = new GameAction(nextGameActionID, actionID, packet);
		switch (actionID) {
		case 1: {
			this.gameParseDeplacementPacket(GA);
			break;
		}
		case 34: {
			this.gameCheckSign(packet);
		}
		case 300: {
			this.gameTryCastSpell(packet);
			break;
		}
		case 303: {
			this.gameTryCac(packet);
			break;
		}
		case 500: {
			this.gameAction(GA);
			this.perso.setGameAction(GA);
			break;
		}
		case 507: {
			this.houseAction(packet);
			break;
		}
		case 512: {
			if (this.perso.get_align() == -1) {
				return;
			}
			this.perso.openPrismeMenu();
			break;
		}
		case 618: {
			this.perso.setisOK(Integer.parseInt(packet.substring(5, 6)));
			SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(this.perso.getCurMap(), "", this.perso.getId(),
					this.perso.getName(), "Oui");
			if (World.getMarried(0).getisOK() > 0 && World.getMarried(1).getisOK() > 0) {
				World.Wedding(World.getMarried(0), World.getMarried(1), 1);
			}
			if (World.getMarried(0) != null && World.getMarried(1) != null) {
				World.PriestRequest((World.getMarried(0) == this.perso) ? World.getMarried(1) : World.getMarried(0),
						(World.getMarried(0) == this.perso) ? World.getMarried(1).getCurMap()
								: World.getMarried(0).getCurMap(),
						this.perso.get_isTalkingWith());
				break;
			}
			break;
		}
		case 619: {
			this.perso.setisOK(0);
			SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(this.perso.getCurMap(), "", this.perso.getId(),
					this.perso.getName(), "Non");
			World.Wedding(World.getMarried(0), World.getMarried(1), 0);
			break;
		}
		case 900: {
			if (Main.fightAsBlocked) {
				return;
			}
			this.gameAskDuel(packet);
			break;
		}
		case 901: {
			if (Main.fightAsBlocked) {
				return;
			}
			this.gameAcceptDuel(packet);
			break;
		}
		case 902: {
			if (Main.fightAsBlocked) {
				return;
			}
			this.gameCancelDuel(packet);
			break;
		}
		case 903: {
			if (Main.fightAsBlocked && this.perso.getGroupe() == null) {
				return;
			}
			this.gameJoinFight(packet);
			break;
		}
		case 906: {
			if (Main.fightAsBlocked) {
				return;
			}
			this.gameAggro(packet);
			break;
		}
		case 909: {
			if (Main.fightAsBlocked) {
				return;
			}
			this.gameCollector(packet);
			break;
		}
		case 912: {
			if (Main.fightAsBlocked) {
				return;
			}
			this.gamePrism(packet);
			break;
		}
		}
	}

	private void gameParseDeplacementPacket(final GameAction GA) {
		String path = GA.packet.substring(5);
		if (this.perso.get_fight() == null) {
			if (this.perso.getBlockMovement()) {
				SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
				this.removeAction(GA);
				return;
			}
			if (this.perso.isDead() == 1) {
				SocketManager.GAME_SEND_BN(this.perso);
				this.removeAction(GA);
				return;
			}
			if (this.perso.getDoAction()) {
				SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
				this.removeAction(GA);
				return;
			}
			if (this.perso.getMount() != null && !this.perso.isGhost() && !this.perso.getMorphMode()
					&& (this.perso.getPodUsed() > this.perso.getMaxPod()
							|| this.perso.getMount().get_podsActuels() > this.perso.getMount().getTotalPod())) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "112");
				SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
				this.removeAction(GA);
				return;
			}
			if (this.perso.getPodUsed() > this.perso.getMaxPod() && !this.perso.isGhost()
					&& !this.perso.getMorphMode()) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "112");
				SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
				this.removeAction(GA);
				return;
			}
			Case targetCell = this.perso.getCurMap()
					.getCase(CryptManager.cellCode_To_ID(path.substring(path.length() - 2)));
			if (!targetCell.isWalkable(false)) {
				SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
				this.removeAction(GA);
				return;
			}
			final AtomicReference<String> pathRef = new AtomicReference<String>(path);
			int result = Pathfinding.isValidPath(this.perso.getCurMap(), this.perso.getCurCell().getId(), pathRef, null,
					this.perso, targetCell.getId());
			if (this.perso.getCurJobAction() != null && this.perso.getCurJobAction().getJobCraft() != null) {
				this.perso.getCurJobAction().getJobCraft().jobAction.broken = true;
				System.err.println("curJob = " + perso.getCurJobAction());
				this.perso.setCurJobAction(null);
				System.err.println("curJob = null");
			}
			if (result <= -9999) {
				result += 10000;
				GA.tp = true;
			}
			if (result == 0) {
				if (targetCell != null && targetCell.getObject() != null) {
					if (Main.modDebug) {
						Console.println("#1# Object Interactif : " + targetCell.getObject().getId(),
								Console.Color.GAME);
						Console.println("#1# On cellule : " + targetCell.getId(), Console.Color.GAME);
					}
					InteractiveObject.getActionIO(this.perso, targetCell, targetCell.getObject().getId());
					InteractiveObject.getSignIO(this.perso, targetCell.getId(), targetCell.getObject().getId());
					SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
					this.removeAction(GA);
					return;
				}
				SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
				this.removeAction(GA);
			} else {
				if (result != -1000 && result < 0) {
					result = -result;
				}
				path = pathRef.get();
				if (result == -1000) {
					GameServer.addToLog(String.valueOf(this.perso.getName()) + "(" + this.perso.getId()
							+ ") Tentative de  deplacement avec un path invalide");
					path = String.valueOf(CryptManager.getHashedValueByInt(this.perso.get_orientation()))
							+ CryptManager.cellID_To_Code(this.perso.getCurCell().getId());
				}
				GA.args = path;
				if (this.perso.walkFast) {
					this.perso.getCurCell().removeCharacter(this.perso.getId());
					SocketManager.GAME_SEND_BN(this);
					final Case nextCell = this.perso.getCurMap()
							.getCase(CryptManager.cellCode_To_ID(path.substring(path.length() - 2)));
					targetCell = this.perso.getCurMap()
							.getCase(CryptManager.cellCode_To_ID(GA.packet.substring(GA.packet.length() - 2)));
					this.perso.setCurCell(nextCell);
					this.perso.set_orientation(CryptManager.getIntByHashedValue(path.charAt(path.length() - 3)));
					this.perso.getCurCell().addCharacter(this.perso);
					if (!this.perso.isGhost()) {
						this.perso.set_away(false);
					}
					this.perso.getCurMap().onPlayerArriveOnCell(this.perso, this.perso.getCurCell().getId());
					if (targetCell.getObject() != null) {
						if (Main.modDebug) {
							Console.println("## Object Interactif : " + targetCell.getObject().getId(),
									Console.Color.GAME);
							Console.println("## On cellule : " + targetCell.getId(), Console.Color.GAME);
						}
						InteractiveObject.getActionIO(this.perso, targetCell, targetCell.getObject().getId());
						InteractiveObject.getSignIO(this.perso, targetCell.getId(), targetCell.getObject().getId());
					}
					SocketManager.GAME_SEND_GA_PACKET(this, "", "0", "", "");
					this.removeAction(GA);
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), this.perso.getId());
					SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.perso.getCurMap(), this.perso);
					return;
				}
				SocketManager.GAME_SEND_GA_PACKET_TO_MAP(this.perso.getCurMap(),
						new StringBuilder().append(GA.id).toString(), 1,
						new StringBuilder(String.valueOf(this.perso.getId())).toString(),
						"a" + CryptManager.cellID_To_Code(this.perso.getCurCell().getId()) + path);
				this.addAction(GA);
				this.perso.setSitted(false);
				this.perso.set_away(true);
			}
		} else {
			final Fighter F = this.perso.get_fight().getFighterByPerso(this.perso);
			if (F == null) {
				return;
			}
			GA.args = path;
			this.perso.get_fight().onFighterDeplace(F, GA);
		}
	}

	private void gameCheckSign(final String packet) {
		final Quest quete = Quest.getQuestById(Integer.parseInt(packet.substring(5)));
		final Quest.Quest_Perso qp = this.perso.getQuestPersoByQuest(quete);
		if (qp == null) {
			quete.applyQuest(this.perso);
		} else {
			SocketManager.GAME_SEND_MESSAGE(this.perso, "Vous avez d\u00e9j\u00e0 appris la qu\u00eate.");
		}
	}

	private void gameTryCastSpell(final String packet) {
		try {
			final String[] splt = packet.split(";");
			final int spellID = Integer.parseInt(splt[0].substring(5));
			int caseID = -1;
			try {
				caseID = Integer.parseInt(splt[1]);
			} catch (Exception e2) {
				if (this.perso.get_fight() != null) {
					caseID = this.perso.get_fight().getFighterByPerso(this.perso).getCell().getId();
				}
			}
			if (this.perso.get_fight() != null) {
				final Spell.SortStats SS = this.perso.getSortStatBySortIfHas(spellID);
				if (SS == null) {
					return;
				}
				this.perso.get_fight().tryCastSpell(this.perso.get_fight().getFighterByPerso(this.perso), SS, caseID);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void gameTryCac(final String packet) {
		try {
			if (this.perso.get_fight() == null) {
				return;
			}
			int cellID = -1;
			try {
				cellID = Integer.parseInt(packet.substring(5));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			this.perso.get_fight().tryCaC(this.perso, cellID);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	private synchronized void gameAction(final GameAction GA) {
		final String packet = GA.packet.substring(5);
		int cellID = -1;
		int actionID = -1;
		try {
			cellID = Integer.parseInt(packet.split(";")[0]);
			actionID = Integer.parseInt(packet.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.walk) {
			this.actions.put(-1, GA);
			return;
		}
		if (cellID == -1 || actionID == -1 || this.perso == null || this.perso.getCurMap() == null
				|| this.perso.getCurMap().getCase(cellID) == null) {
			return;
		}
		GA.args = String.valueOf(cellID) + ";" + actionID;
		this.perso.getGameClient().addAction(GA);
		if (this.perso.isDead() == 0) {
			this.perso.startActionOnCell(GA);
		}
	}

	private void houseAction(final String packet) {
		final int actionID = Integer.parseInt(packet.substring(5));
		final House h = this.perso.getInHouse();
		if (h == null) {
			return;
		}
		switch (actionID) {
		case 81: {
			h.lock(this.perso);
			break;
		}
		case 97: {
			h.buyIt(this.perso);
			break;
		}
		case 98:
		case 108: {
			h.sellIt(this.perso);
			break;
		}
		}
	}

	private void gameAskDuel(final String packet) {
		if (this.perso.getCurMap().getPlaces().equalsIgnoreCase("|")) {
			SocketManager.GAME_SEND_DUEL_Y_AWAY(this, this.perso.getId());
			return;
		}
		try {
			if (this.perso.cantDefie()) {
				return;
			}
			final int guid = Integer.parseInt(packet.substring(5));
			if (this.perso.is_away() || this.perso.get_fight() != null || this.perso.isDead() == 1) {
				SocketManager.GAME_SEND_DUEL_Y_AWAY(this, this.perso.getId());
				return;
			}
			final Player Target = World.getPersonnage(guid);
			if (Target == null) {
				return;
			}
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			if (Target.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			if (Target.is_away() || Target.get_fight() != null
					|| Target.getCurMap().getId() != this.perso.getCurMap().getId() || Target.isDead() == 1) {
				SocketManager.GAME_SEND_DUEL_E_AWAY(this, this.perso.getId());
				return;
			}
			this.perso.set_duelID(guid);
			this.perso.set_away(true);
			World.getPersonnage(guid).set_duelID(this.perso.getId());
			World.getPersonnage(guid).set_away(true);
			SocketManager.GAME_SEND_MAP_NEW_DUEL_TO_MAP(this.perso.getCurMap(), this.perso.getId(), guid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void gameAcceptDuel(final String packet) {
		if (this.perso.cantDefie()) {
			return;
		}
		int guid = -1;
		try {
			guid = Integer.parseInt(packet.substring(5));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		if (this.perso.get_duelID() != guid || this.perso.get_duelID() == -1 || this.perso.isDead() == 1) {
			return;
		}
		SocketManager.GAME_SEND_MAP_START_DUEL_TO_MAP(this.perso.getCurMap(), this.perso.get_duelID(),
				this.perso.getId());
		final Fight fight = this.perso.getCurMap().newFight(World.getPersonnage(this.perso.get_duelID()), this.perso,
				0);
		this.perso.set_fight(fight);
		World.getPersonnage(this.perso.get_duelID()).set_fight(fight);
	}

	private void gameCancelDuel(final String packet) {
		try {
			if (this.perso.get_duelID() == -1) {
				return;
			}
			SocketManager.GAME_SEND_CANCEL_DUEL_TO_MAP(this.perso.getCurMap(), this.perso.get_duelID(),
					this.perso.getId());
			World.getPersonnage(this.perso.get_duelID()).set_away(false);
			World.getPersonnage(this.perso.get_duelID()).set_duelID(-1);
			this.perso.set_away(false);
			this.perso.set_duelID(-1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void gameJoinFight(final String packet) {
		if (this.perso.get_fight() != null) {
			return;
		}
		if (this.perso.isDead() == 1) {
			return;
		}
		final String[] infos = packet.substring(5).split(";");
		if (infos.length == 1) {
			try {
				final Fight F = this.perso.getCurMap().getFight(Integer.parseInt(infos[0]));
				if (F != null) {
					F.joinAsSpect(this.perso);
				}
				return;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		try {
			final int guid = Integer.parseInt(infos[1]);
			if (this.perso.is_away()) {
				SocketManager.GAME_SEND_GA903_ERROR_PACKET(this, 'o', guid);
				return;
			}
			final Player p = World.getPersonnage(guid);
			if (p == null) {
				return;
			}
			final Fight f = p.get_fight();
			if (f == null) {
				return;
			}
			if (f.getState() > 2) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "191");
				return;
			}
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			f.joinFight(this.perso, guid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void gameAggro(final String packet) {
		try {
			if (this.perso == null) {
				return;
			}
			if (this.perso.get_fight() != null) {
				return;
			}
			if (this.perso.isGhost()) {
				return;
			}
			if (this.perso.isDead() == 1) {
				return;
			}
			if (this.perso.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
				return;
			}
			if (this.perso.cantAgro()) {
				return;
			}
			final int id = Integer.parseInt(packet.substring(5));
			final Player target = World.getPersonnage(id);
			if (target == null || !target.isOnline() || target.get_fight() != null
					|| target.getCurMap().getId() != this.perso.getCurMap().getId()
					|| target.get_align() == this.perso.get_align()
					|| this.perso.getCurMap().getPlaces().equalsIgnoreCase("|") || !target.canAggro()
					|| target.isDead() == 1) {
				return;
			}
			if (this.perso.restriction.aggros.containsKey(target.getAccount().getCurIP())) {
				if (System.currentTimeMillis()
						- this.perso.restriction.aggros.get(target.getAccount().getCurIP()) < 3600000L) {
					SocketManager.GAME_SEND_MESSAGE(this.perso,
							"Il faut que tu attende encore " + (System.currentTimeMillis()
									- this.perso.restriction.aggros.get(target.getAccount().getCurIP())) / 60L / 1000L
									+ " minute(s).");
					return;
				}
				this.perso.restriction.aggros.remove(target.getAccount().getCurIP());
			}
			this.perso.restriction.aggros.put(target.getAccount().getCurIP(), System.currentTimeMillis());
			if (target.isInAreaNotSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(target.getGameClient(), 'S');
				return;
			}
			if (target.get_align() == 0) {
				this.perso.setDeshonor(this.perso.getDeshonor() + 1);
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "084;1");
			}
			this.perso.toggleWings('+');
			SocketManager.GAME_SEND_GA_PACKET_TO_MAP(this.perso.getCurMap(), "", 906,
					new StringBuilder(String.valueOf(this.perso.getId())).toString(),
					new StringBuilder(String.valueOf(id)).toString());
			this.perso.getCurMap().newFight(this.perso, target, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void gameCollector(final String packet) {
		try {
			if (this.perso == null) {
				return;
			}
			if (this.perso.get_fight() != null) {
				return;
			}
			if (this.perso.get_isTalkingWith() != 0 || this.perso.get_isTradingWith() != 0
					|| this.perso.getCurJobAction() != null || this.perso.getCurExchange() != null
					|| this.perso.isDead() == 1 || this.perso.is_away()) {
				return;
			}
			final int id = Integer.parseInt(packet.substring(5));
			final Collector target = World.getCollector(id);
			if (target == null || target.getInFight() > 0) {
				return;
			}
			if (this.perso.getCurMap().getId() != target.getMap()) {
				return;
			}
			if (target.getExchange()) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "1180");
				return;
			}
			SocketManager.GAME_SEND_GA_PACKET_TO_MAP(this.perso.getCurMap(), "", 909,
					new StringBuilder(String.valueOf(this.perso.getId())).toString(),
					new StringBuilder(String.valueOf(id)).toString());
			this.perso.getCurMap().startFightVersusPercepteur(this.perso, target);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void gamePrism(final String packet) {
		try {
			if (this.perso.isGhost()) {
				return;
			}
			if (this.perso == null) {
				return;
			}
			if (this.perso.get_fight() != null) {
				return;
			}
			if (this.perso.get_isTalkingWith() != 0 || this.perso.get_isTradingWith() != 0
					|| this.perso.getCurJobAction() != null) {
				return;
			}
			if (this.perso.get_align() == 0) {
				return;
			}
			if (this.perso.isDead() == 1) {
				return;
			}
			final int id = Integer.parseInt(packet.substring(5));
			final Prism Prisme = World.getPrisme(id);
			if (Prisme.getInFight() == 0 || Prisme.getInFight() == -2) {
				return;
			}
			SocketManager.SEND_GA_ACTION_TO_Map(this.perso.getCurMap(), "", 909,
					new StringBuilder(String.valueOf(this.perso.getId())).toString(),
					new StringBuilder(String.valueOf(id)).toString());
			this.perso.getCurMap().startFightVersusPrisme(this.perso, Prisme);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showMonsterTarget(final String packet) {
		int chalID = 0;
		chalID = Integer.parseInt(packet.split("i")[1]);
		if (chalID != 0 && this.perso.get_fight() != null) {
			final Fight fight = this.perso.get_fight();
			if (fight.getAllChallenges().containsKey(chalID)) {
				fight.getAllChallenges().get(chalID).showCibleToPerso(this.perso);
			}
		}
	}

	private void setFlag(final String packet) {
		if (this.perso == null) {
			return;
		}
		if (this.perso.get_fight() == null) {
			return;
		}
		int cellID = -1;
		try {
			cellID = Integer.parseInt(packet.substring(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cellID == -1) {
			return;
		}
		this.perso.get_fight().showCaseToTeam(this.perso.getId(), cellID);
	}

	private void getExtraInformations() {
		try {
			if (this.perso == null) {
				return;
			}
			if (this.perso.needEndFight() != -1) {
				this.perso.getWaiter().addNow(new Runnable() {
					@Override
					public void run() {
						GameClient.this.perso.getCurMap().applyEndFightAction(GameClient.this.perso);
						GameClient.this.perso.setNeededEndFight(-1, null);
						GameClient.this._getExtraInformations();
					}
				}, 500L);
			} else {
				this._getExtraInformations();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _getExtraInformations() {
		try {
			if (this.perso.get_fight() != null) {
				SocketManager.GAME_SEND_MAP_GMS_PACKETS(this.perso.get_fight().getMap(), this.perso);
				SocketManager.GAME_SEND_GDK_PACKET(this);
				if (this.perso.get_fight().playerReconnect(this.perso)) {
					return;
				}
			}
			House.load(this.perso, this.perso.getCurMap().getId());
			SocketManager.GAME_SEND_MAP_GMS_PACKETS(this.perso.getCurMap(), this.perso);
			SocketManager.GAME_SEND_MAP_MOBS_GMS_PACKETS(this.perso.getGameClient(), this.perso.getCurMap());
			SocketManager.GAME_SEND_MAP_NPCS_GMS_PACKETS(this, this.perso.getCurMap());
			SocketManager.GAME_SEND_MAP_PERCO_GMS_PACKETS(this, this.perso.getCurMap());
			SocketManager.GAME_SEND_MAP_OBJECTS_GDS_PACKETS(this, this.perso.getCurMap());
			SocketManager.GAME_SEND_GDK_PACKET(this);
			SocketManager.GAME_SEND_MAP_FIGHT_COUNT(this, this.perso.getCurMap());
			SocketManager.SEND_GM_PRISME_TO_MAP(this, this.perso.getCurMap());
			SocketManager.GAME_SEND_MERCHANT_LIST(this.perso, this.perso.getCurMap().getId());
			Fight.FightStateAddFlag(this.perso.getCurMap(), this.perso);
			SocketManager.GAME_SEND_Rp_PACKET(this.perso, this.perso.getCurMap().getMountPark());
			SocketManager.GAME_SEND_GDO_OBJECT_TO_MAP(this, this.perso.getCurMap());
			SocketManager.GAME_SEND_GM_MOUNT(this, this.perso.getCurMap());
			this.perso.getCurMap().sendFloorItems(this.perso);
			this.perso.getCurMap().verifDoor(this.perso);
			World.showPrismes(this.perso);
			for (final Player player : this.perso.getCurMap().getPersos()) {
				final ArrayList<Job> jobs = player.getJobs();
				if (jobs != null) {
					final org.aestia.object.Object object = player.getObjetByPos(1);
					if (object == null) {
						continue;
					}
					final String packet = "EW+" + player.getId() + "|";
					String data = "";
					for (final Job job : jobs) {
						if (job.getSkills().isEmpty()) {
							continue;
						}
						if (!job.isValidTool(object.getTemplate().getId())) {
							continue;
						}
						for (final Case cell : this.perso.getCurMap().getCases().values()) {
							if (cell.getObject() != null && cell.getObject().getTemplate() != null) {
								final int io = cell.getObject().getTemplate().getId();
								final ArrayList<Integer> skills = job.getSkills().get(io);
								if (skills == null) {
									continue;
								}
								for (final int skill : skills) {
									if (!data.contains(String.valueOf(skill))) {
										data = String.valueOf(data) + (data.isEmpty() ? skill : (";" + skill));
									}
								}
							}
						}
						if (!data.isEmpty()) {
							break;
						}
					}
					player.send(String.valueOf(packet) + data);
					this.perso.send(String.valueOf(packet) + data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actionAck(final String packet) {
		int GameActionId = -1;
		final String[] infos = packet.substring(3).split("\\|");
		try {
			GameActionId = Integer.parseInt(infos[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (GameActionId == -1) {
			return;
		}
		final GameAction GA = this.actions.get(GameActionId);
		if (GA == null) {
			return;
		}
		final boolean isOk = packet.charAt(2) == 'K';
		switch (GA.actionId) {
		case 1: {
			if (isOk) {
				if (this.perso.get_fight() != null) {
					this.perso.get_fight().onGK(this.perso);
					return;
				}
				this.perso.getCurCell().removeCharacter(this.perso.getId());
				SocketManager.GAME_SEND_BN(this);
				final String path = GA.args;
				final Case nextCell = this.perso.getCurMap()
						.getCase(CryptManager.cellCode_To_ID(path.substring(path.length() - 2)));
				final Case targetCell = this.perso.getCurMap()
						.getCase(CryptManager.cellCode_To_ID(GA.packet.substring(GA.packet.length() - 2)));
				this.perso.setCurCell(nextCell);
				this.perso.set_orientation(CryptManager.getIntByHashedValue(path.charAt(path.length() - 3)));
				this.perso.getCurCell().addCharacter(this.perso);
				if (!this.perso.isGhost()) {
					this.perso.set_away(false);
				}
				this.perso.getCurMap().onPlayerArriveOnCell(this.perso, this.perso.getCurCell().getId());
				if (targetCell.getObject() != null) {
					if (Main.modDebug) {
						Console.println("#2# Object Interactif : " + targetCell.getObject().getId(),
								Console.Color.GAME);
						Console.println("#2# On cellule : " + targetCell.getId(), Console.Color.GAME);
					}
					InteractiveObject.getActionIO(this.perso, targetCell, targetCell.getObject().getId());
					InteractiveObject.getSignIO(this.perso, targetCell.getId(), targetCell.getObject().getId());
				}
				if (GA.tp) {
					GA.tp = false;
					this.perso.teleport((short) 9864, 265);
					return;
				}
				break;
			} else {
				int newCellID = -1;
				try {
					newCellID = Integer.parseInt(infos[1]);
				} catch (Exception e2) {
					e2.printStackTrace();
					return;
				}
				if (newCellID == -1) {
					return;
				}
				final String path2 = GA.args;
				this.perso.getCurCell().removeCharacter(this.perso.getId());
				this.perso.setCurCell(this.perso.getCurMap().getCase(newCellID));
				this.perso.set_orientation(CryptManager.getIntByHashedValue(path2.charAt(path2.length() - 3)));
				this.perso.getCurCell().addCharacter(this.perso);
				SocketManager.GAME_SEND_BN(this);
				if (GA.tp) {
					GA.tp = false;
					this.perso.teleport((short) 9864, 265);
					return;
				}
				break;
			}
		}
		case 500: {
			this.perso.finishActionOnCell(GA);
			this.perso.setGameAction(null);
			break;
		}
		}
		this.removeAction(GA);
	}

	private void setPlayerPosition(final String packet) {
		if (this.perso.get_fight() == null) {
			return;
		}
		try {
			final int cell = Integer.parseInt(packet.substring(2));
			this.perso.get_fight().exchangePlace(this.perso, cell);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void leaveFight(final String packet) {
		int targetID = -1;
		if (!packet.substring(2).isEmpty()) {
			try {
				targetID = Integer.parseInt(packet.substring(2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final Fight combat = this.perso.get_fight();
		if (combat == null) {
			return;
		}
		if (targetID > 0) {
			final Player target = World.getPersonnage(targetID);
			if (target == null || target.get_fight() == null || target.get_fight()
					.getTeamId(target.getId()) != this.perso.get_fight().getTeamId(this.perso.getId())) {
				return;
			}
			combat.leftFight(this.perso, target);
		} else {
			combat.leftFight(this.perso, null);
		}
	}

	private void readyFight(final String packet) {
		if (this.perso.get_fight() == null) {
			return;
		}
		if (this.perso.get_fight().getState() != 2) {
			return;
		}
		this.perso.set_ready(packet.substring(2).equalsIgnoreCase("1"));
		this.perso.get_fight().verifIfAllReady();
		SocketManager.GAME_SEND_FIGHT_PLAYER_READY_TO_FIGHT(this.perso.get_fight(), 3, this.perso.getId(),
				packet.substring(2).equalsIgnoreCase("1"));
	}

	private void parseGuildPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'B': {
			this.boostCaracteristique(packet);
			break;
		}
		case 'b': {
			this.boostSpellGuild(packet);
			break;
		}
		case 'C': {
			this.createGuild(packet);
			break;
		}
		case 'f': {
			this.teleportToGuildFarm(packet.substring(2));
			break;
		}
		case 'F': {
			this.removeTaxCollector(packet.substring(2));
			break;
		}
		case 'h': {
			this.teleportToGuildHouse(packet.substring(2));
			break;
		}
		case 'H': {
			this.placeTaxCollector();
			break;
		}
		case 'I': {
			this.getInfos(packet.charAt(2));
			break;
		}
		case 'J': {
			this.invitationGuild(packet.substring(2));
			break;
		}
		case 'K': {
			this.banToGuild(packet.substring(2));
			break;
		}
		case 'P': {
			this.changeMemberProfil(packet.substring(2));
			break;
		}
		case 'T': {
			this.joinOrLeaveTaxCollector(packet.substring(2));
			break;
		}
		case 'V': {
			this.leavePanelGuildCreate();
			break;
		}
		}
	}

	private void boostCaracteristique(final String packet) {
		if (this.perso.get_guild() == null) {
			return;
		}
		final Guild G = this.perso.get_guild();
		if (!this.perso.getGuildMember().canDo(Constant.G_BOOST)) {
			return;
		}
		switch (packet.charAt(2)) {
		case 'p': {
			if (G.getCapital() < 1) {
				return;
			}
			if (G.getStats(176) >= 500) {
				return;
			}
			G.setCapital(G.getCapital() - 1);
			G.upgradeStats(176, 1);
			break;
		}
		case 'x': {
			if (G.getCapital() < 1) {
				return;
			}
			if (G.getStats(124) >= 400) {
				return;
			}
			G.setCapital(G.getCapital() - 1);
			G.upgradeStats(124, 1);
			break;
		}
		case 'o': {
			if (G.getCapital() < 1) {
				return;
			}
			if (G.getStats(158) >= 5000) {
				return;
			}
			G.setCapital(G.getCapital() - 1);
			G.upgradeStats(158, 20);
			break;
		}
		case 'k': {
			if (G.getCapital() < 10) {
				return;
			}
			if (G.getNbrPerco() >= 50) {
				return;
			}
			G.setCapital(G.getCapital() - 10);
			G.setNbrPerco(G.getNbrPerco() + 1);
			break;
		}
		}
		Database.getGame().getGuildData().update(G);
		SocketManager.GAME_SEND_gIB_PACKET(this.perso, this.perso.get_guild().parseCollectorToGuild());
	}

	private void boostSpellGuild(final String packet) {
		if (this.perso.get_guild() == null) {
			return;
		}
		final Guild G2 = this.perso.get_guild();
		if (!this.perso.getGuildMember().canDo(Constant.G_BOOST)) {
			return;
		}
		final int spellID = Integer.parseInt(packet.substring(2));
		if (G2.getSpells().containsKey(spellID)) {
			if (G2.getCapital() < 5) {
				return;
			}
			G2.setCapital(G2.getCapital() - 5);
			G2.boostSpell(spellID);
			Database.getGame().getGuildData().update(G2);
			SocketManager.GAME_SEND_gIB_PACKET(this.perso, this.perso.get_guild().parseCollectorToGuild());
		} else {
			GameServer.addToLog("[ERROR]Spell " + spellID + " unknow.");
		}
	}

	private void createGuild(final String packet) {
		if (this.perso == null) {
			return;
		}
		if (this.perso.get_guild() != null || this.perso.getGuildMember() != null) {
			SocketManager.GAME_SEND_gC_PACKET(this.perso, "Ea");
			return;
		}
		if (this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		try {
			final String[] infos = packet.substring(2).split("\\|");
			final String bgID = Integer.toString(Integer.parseInt(infos[0]), 36);
			final String bgCol = Integer.toString(Integer.parseInt(infos[1]), 36);
			final String embID = Integer.toString(Integer.parseInt(infos[2]), 36);
			final String embCol = Integer.toString(Integer.parseInt(infos[3]), 36);
			final String name = infos[4];
			if (World.guildNameIsUsed(name)) {
				SocketManager.GAME_SEND_gC_PACKET(this.perso, "Ean");
				return;
			}
			final String tempName = name.toLowerCase();
			boolean isValid = true;
			if (tempName.length() > 20 || tempName.contains("mj") || tempName.contains("modo")
					|| tempName.contains("fuck") || tempName.contains("admin")) {
				isValid = false;
			}
			if (isValid) {
				int tiretCount = 0;
				char[] charArray;
				for (int length = (charArray = tempName.toCharArray()).length, i = 0; i < length; ++i) {
					final char curLetter = charArray[i];
					if ((curLetter < 'a' || curLetter > 'z') && curLetter != '-' && curLetter != ' ') {
						isValid = false;
						break;
					}
					if (curLetter == '-') {
						if (tiretCount >= 2) {
							isValid = false;
							break;
						}
						++tiretCount;
					}
					if (curLetter == ' ') {
						if (tiretCount >= 2) {
							isValid = false;
							break;
						}
						++tiretCount;
					}
				}
			}
			if (!isValid) {
				SocketManager.GAME_SEND_gC_PACKET(this.perso, "Ean");
				return;
			}
			final String emblem = String.valueOf(bgID) + "," + bgCol + "," + embID + "," + embCol;
			if (World.guildEmblemIsUsed(emblem)) {
				SocketManager.GAME_SEND_gC_PACKET(this.perso, "Eae");
				return;
			}
			if (this.perso.getCurMap().getId() == 2196) {
				if (!this.perso.hasItemTemplate(1575, 1)) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "14");
					return;
				}
				this.perso.removeByTemplateID(1575, 1);
			}
			final Guild G = new Guild(this.perso, name, emblem);
			final Guild.GuildMember gm = G.addNewMember(this.perso);
			gm.setAllRights(1, (byte) 0, 1, this.perso);
			this.perso.setGuildMember(gm);
			World.addGuild(G, true);
			Database.getGame().getGuild_memberData().update(this.perso);
			SocketManager.GAME_SEND_gS_PACKET(this.perso, gm);
			SocketManager.GAME_SEND_gC_PACKET(this.perso, "K");
			SocketManager.GAME_SEND_gV_PACKET(this.perso);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void teleportToGuildFarm(final String packet) {
		if (this.perso.get_guild() == null) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1135");
			return;
		}
		if (this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		final short MapID = Short.parseShort(packet);
		final MountPark MP = World.getMap(MapID).getMountPark();
		if (MP.getGuild().getId() != this.perso.get_guild().getId()) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1135");
			return;
		}
		final int CellID = World.getEncloCellIdByMapId(MapID);
		if (this.perso.hasItemTemplate(9035, 1)) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "022;1~9035");
			this.perso.removeByTemplateID(9035, 1);
			this.perso.teleport(MapID, CellID);
			return;
		}
		SocketManager.GAME_SEND_Im_PACKET(this.perso, "1159");
	}

	private void removeTaxCollector(final String packet) {
		if (this.perso.get_guild() == null || this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		if (!this.perso.getGuildMember().canDo(Constant.G_POSPERCO)) {
			return;
		}
		final int idCollector = Integer.parseInt(packet);
		final Collector Collector = World.getCollector(idCollector);
		if (Collector == null || Collector.getInFight() > 0) {
			return;
		}
		Collector.reloadTimer();
		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), idCollector);
		Database.getGame().getPercepteurData().delete(Collector.getId());
		Collector.delCollector(Collector.getId());
		for (final Player z : this.perso.get_guild().getMembers()) {
			if (z.isOnline()) {
				SocketManager.GAME_SEND_gITM_PACKET(z, org.aestia.entity.Collector.parseToGuild(z.get_guild().getId()));
				String str = "";
				str = String.valueOf(str) + "R" + Integer.toString(Collector.getN1(), 36) + ","
						+ Integer.toString(Collector.getN2(), 36) + "|";
				str = String.valueOf(str) + Collector.getMap() + "|";
				str = String.valueOf(str) + World.getMap(Collector.getMap()).getX() + "|"
						+ World.getMap(Collector.getMap()).getY() + "|" + this.perso.getName();
				SocketManager.GAME_SEND_gT_PACKET(z, str);
			}
		}
	}

	private void teleportToGuildHouse(final String packet) {
		if (this.perso.get_guild() == null) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1135");
			return;
		}
		if (this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		final int HouseID = Integer.parseInt(packet);
		final House h = World.getHouses().get(HouseID);
		if (h == null) {
			return;
		}
		if (this.perso.get_guild().getId() != h.getGuildId()) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1135");
			return;
		}
		if (!h.canDo(Constant.H_GTELE)) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1136");
			return;
		}
		if (this.perso.hasItemTemplate(8883, 1)) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "022;1~8883");
			this.perso.removeByTemplateID(8883, 1);
			this.perso.teleport((short) h.getHouseMapId(), h.getHouseCellId());
			return;
		}
		SocketManager.GAME_SEND_Im_PACKET(this.perso, "1137");
	}

	private void placeTaxCollector() {
		if (this.perso.get_guild() == null || this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		if (!this.perso.getGuildMember().canDo(Constant.G_POSPERCO)) {
			return;
		}
		if (!this.perso.get_guild().haveTenMembers()) {
			return;
		}
		if (this.perso.isInAreaNotSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this.perso.getGameClient(), 'S');
			return;
		}
		final short price = (short) (1000 + 10 * this.perso.get_guild().getLvl());
		if (this.perso.get_kamas() < price) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "182");
			return;
		}
		if (Collector.getCollectorByGuildId(this.perso.getCurMap().getId()) > 0) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1168;1");
			return;
		}
		if (this.perso.getCurMap().getPlaces().length() < 5
				|| Config.contains(Config.arenaMap, this.perso.getCurMap().getId())
				|| this.perso.getCurMap().noCollector) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "113");
			return;
		}
		if (Collector.countCollectorGuild(this.perso.get_guild().getId()) >= this.perso.get_guild().getNbrPerco()) {
			return;
		}
		if (this.perso.get_guild().timePutCollector.get(this.perso.getCurMap().getId()) != null) {
			long time = this.perso.get_guild().timePutCollector.get(this.perso.getCurMap().getId());
			final Long t = time;
			if (t == null) {
				time = World.getCollectorByMap(this.perso.getCurMap().getId()).getDate();
				this.perso.get_guild().timePutCollector.put(this.perso.getCurMap().getId(), time);
			}
			if (System.currentTimeMillis() - time < 10 * this.perso.get_guild().getLvl() * 60 * 1000) {
				this.perso.send("Im1167;"
						+ (10 * this.perso.get_guild().getLvl() * 60 * 1000 - (System.currentTimeMillis() - time))
								/ 1000L / 60L);
				return;
			}
			this.perso.get_guild().timePutCollector.remove(this.perso.getCurMap().getId());
		}
		this.perso.get_guild().timePutCollector.put(this.perso.getCurMap().getId(), System.currentTimeMillis());
		this.perso.set_kamas(this.perso.get_kamas() - price);
		if (this.perso.get_kamas() <= 0L) {
			this.perso.set_kamas(0L);
		}
		SocketManager.GAME_SEND_STATS_PACKET(this.perso);
		final short random1 = (short) Formulas.getRandomValue(1, 129);
		final short random2 = (short) Formulas.getRandomValue(1, 227);
		final int id = Database.getGame().getPercepteurData().getId();
		final Collector Collector = new Collector(id, this.perso.getCurMap().getId(), this.perso.getCurCell().getId(),
				(byte) 3, this.perso.get_guild().getId(), random1, random2, this.perso, System.currentTimeMillis(), "",
				0L, 0L);
		World.addCollector(Collector);
		SocketManager.GAME_SEND_ADD_PERCO_TO_MAP(this.perso.getCurMap());
		Database.getGame().getPercepteurData().add(id, this.perso.getCurMap().getId(), this.perso.get_guild().getId(),
				this.perso.getId(), System.currentTimeMillis(), this.perso.getCurCell().getId(), 3, random1, random2);
		for (final Player z : this.perso.get_guild().getMembers()) {
			if (z != null && z.isOnline()) {
				SocketManager.GAME_SEND_gITM_PACKET(z, org.aestia.entity.Collector.parseToGuild(z.get_guild().getId()));
				String str = "";
				str = String.valueOf(str) + "S" + Integer.toString(Collector.getN1(), 36) + ","
						+ Integer.toString(Collector.getN2(), 36) + "|";
				str = String.valueOf(str) + Collector.getMap() + "|";
				str = String.valueOf(str) + World.getMap(Collector.getMap()).getX() + "|"
						+ World.getMap(Collector.getMap()).getY() + "|" + this.perso.getName();
				SocketManager.GAME_SEND_gT_PACKET(z, str);
			}
		}
	}

	private void getInfos(final char c) {
		switch (c) {
		case 'B': {
			SocketManager.GAME_SEND_gIB_PACKET(this.perso, this.perso.get_guild().parseCollectorToGuild());
			break;
		}
		case 'F': {
			SocketManager.GAME_SEND_gIF_PACKET(this.perso, World.parseMPtoGuild(this.perso.get_guild().getId()));
			break;
		}
		case 'G': {
			SocketManager.GAME_SEND_gIG_PACKET(this.perso, this.perso.get_guild());
			break;
		}
		case 'H': {
			SocketManager.GAME_SEND_gIH_PACKET(this.perso, House.parseHouseToGuild(this.perso));
			break;
		}
		case 'M': {
			SocketManager.GAME_SEND_gIM_PACKET(this.perso, this.perso.get_guild(), '+');
			break;
		}
		case 'T': {
			SocketManager.GAME_SEND_gITM_PACKET(this.perso, Collector.parseToGuild(this.perso.get_guild().getId()));
			Collector.parseAttaque(this.perso, this.perso.get_guild().getId());
			Collector.parseDefense(this.perso, this.perso.get_guild().getId());
			break;
		}
		}
	}

	private void invitationGuild(final String packet) {
		switch (packet.charAt(0)) {
		case 'R': {
			final Player P = World.getPersoByName(packet.substring(1));
			if (P == null || this.perso.get_guild() == null) {
				SocketManager.GAME_SEND_gJ_PACKET(this.perso, "Eu");
				return;
			}
			if (!P.isOnline()) {
				SocketManager.GAME_SEND_gJ_PACKET(this.perso, "Eu");
				return;
			}
			if (P.is_away()) {
				SocketManager.GAME_SEND_gJ_PACKET(this.perso, "Eo");
				return;
			}
			if (P.get_guild() != null) {
				SocketManager.GAME_SEND_gJ_PACKET(this.perso, "Ea");
				return;
			}
			if (!this.perso.getGuildMember().canDo(Constant.G_INVITE)) {
				SocketManager.GAME_SEND_gJ_PACKET(this.perso, "Ed");
				return;
			}
			if (this.perso.get_guild().getMembers().size() >= 40 + this.perso.get_guild().getLvl()) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "155;" + (40 + this.perso.get_guild().getLvl()));
				return;
			}
			this.perso.setInvitation(P.getId());
			P.setInvitation(this.perso.getId());
			SocketManager.GAME_SEND_gJ_PACKET(this.perso, "R" + packet.substring(1));
			SocketManager.GAME_SEND_gJ_PACKET(P,
					"r" + this.perso.getId() + "|" + this.perso.getName() + "|" + this.perso.get_guild().getName());
			break;
		}
		case 'E': {
			if (!packet.substring(1)
					.equalsIgnoreCase(new StringBuilder(String.valueOf(this.perso.getInvitation())).toString())) {
				break;
			}
			final Player p = World.getPersonnage(this.perso.getInvitation());
			if (p == null) {
				return;
			}
			SocketManager.GAME_SEND_gJ_PACKET(p, "Ec");
			break;
		}
		case 'K': {
			if (!packet.substring(1)
					.equalsIgnoreCase(new StringBuilder(String.valueOf(this.perso.getInvitation())).toString())) {
				break;
			}
			final Player p = World.getPersonnage(this.perso.getInvitation());
			if (p == null) {
				return;
			}
			final Guild G = p.get_guild();
			final Guild.GuildMember GM = G.addNewMember(this.perso);
			Database.getGame().getGuild_memberData().update(this.perso);
			this.perso.setGuildMember(GM);
			this.perso.setInvitation(-1);
			p.setInvitation(-1);
			if (G.getId() == 1) {
				this.perso.modifAlignement(3);
			}
			SocketManager.GAME_SEND_gJ_PACKET(p, "Ka" + this.perso.getName());
			SocketManager.GAME_SEND_gS_PACKET(this.perso, GM);
			SocketManager.GAME_SEND_gJ_PACKET(this.perso, "Kj");
			break;
		}
		}
	}

	private void banToGuild(final String name) {
		if (this.perso.get_guild() == null) {
			return;
		}
		final Player P = World.getPersoByName(name);
		int guid = -1;
		int guildId = -1;
		Guild toRemGuild;
		Guild.GuildMember toRemMember;
		if (P == null) {
			final int[] infos = Database.getGame().getGuild_memberData().isPersoInGuild(name);
			guid = infos[0];
			guildId = infos[1];
			if (guildId < 0 || guid < 0) {
				return;
			}
			toRemGuild = World.getGuild(guildId);
			toRemMember = toRemGuild.getMember(guid);
		} else {
			toRemGuild = P.get_guild();
			if (toRemGuild == null) {
				toRemGuild = World.getGuild(this.perso.get_guild().getId());
			}
			toRemMember = toRemGuild.getMember(P.getId());
			if (toRemMember == null) {
				return;
			}
			if (toRemMember.getGuild().getId() != this.perso.get_guild().getId()) {
				return;
			}
		}
		if (toRemGuild.getId() != this.perso.get_guild().getId()) {
			SocketManager.GAME_SEND_gK_PACKET(this.perso, "Ea");
			return;
		}
		if (!this.perso.getGuildMember().canDo(Constant.G_BAN)
				&& this.perso.getGuildMember().getGuid() != toRemMember.getGuid()) {
			SocketManager.GAME_SEND_gK_PACKET(this.perso, "Ed");
			return;
		}
		if (this.perso.getGuildMember().getGuid() != toRemMember.getGuid()) {
			if (toRemMember.getRank() == 1) {
				return;
			}
			toRemGuild.removeMember(toRemMember.getPerso());
			if (P != null) {
				P.setGuildMember(null);
			}
			if (toRemGuild.getId() == 1) {
				toRemMember.getPerso().modifAlignement(0);
			}
			SocketManager.GAME_SEND_gK_PACKET(this.perso, "K" + this.perso.getName() + "|" + name);
			if (P != null) {
				SocketManager.GAME_SEND_gK_PACKET(P, "K" + this.perso.getName());
			}
		} else {
			final Guild G = this.perso.get_guild();
			if (this.perso.getGuildMember().getRank() == 1 && G.getMembers().size() > 1) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Vous devez mettre un autre meneur pour devoir quitter la guilde !");
				return;
			}
			G.removeMember(this.perso);
			this.perso.setGuildMember(null);
			if (G.getId() == 1) {
				this.perso.modifAlignement(0);
			}
			if (G.getMembers().isEmpty()) {
				World.removeGuild(G.getId());
			}
			SocketManager.GAME_SEND_gK_PACKET(this.perso, "K" + name + "|" + name);
		}
	}

	private void changeMemberProfil(final String packet) {
		if (this.perso.get_guild() == null) {
			return;
		}
		final String[] infos = packet.split("\\|");
		final int guid = Integer.parseInt(infos[0]);
		int rank = Integer.parseInt(infos[1]);
		byte xpGive = Byte.parseByte(infos[2]);
		int right = Integer.parseInt(infos[3]);
		final Player p = World.getPersonnage(guid);
		final Guild.GuildMember changer = this.perso.getGuildMember();
		Guild.GuildMember toChange;
		if (p == null) {
			final int guildId = Database.getGame().getGuild_memberData().isPersoInGuild(guid);
			if (guildId < 0) {
				return;
			}
			if (guildId != this.perso.get_guild().getId()) {
				SocketManager.GAME_SEND_gK_PACKET(this.perso, "Ed");
				return;
			}
			toChange = World.getGuild(guildId).getMember(guid);
		} else {
			if (p.get_guild() == null) {
				return;
			}
			if (this.perso.get_guild().getId() != p.get_guild().getId()) {
				SocketManager.GAME_SEND_gK_PACKET(this.perso, "Ea");
				return;
			}
			toChange = p.getGuildMember();
		}
		if (changer.getRank() == 1) {
			if (changer.getGuid() == toChange.getGuid()) {
				rank = -1;
				right = -1;
			} else if (rank == 1) {
				changer.setAllRights(2, (byte) (-1), 29694, this.perso);
				rank = 1;
				xpGive = -1;
				right = 1;
			}
		} else {
			if (toChange.getRank() == 1) {
				rank = -1;
				right = -1;
			} else {
				if (!changer.canDo(Constant.G_RANK) || rank == 1) {
					rank = -1;
				}
				if (!changer.canDo(Constant.G_RIGHT) || right == 1) {
					right = -1;
				}
				if (!changer.canDo(Constant.G_HISXP) && !changer.canDo(Constant.G_ALLXP)
						&& changer.getGuid() == toChange.getGuid()) {
					xpGive = -1;
				}
			}
			if (!changer.canDo(Constant.G_ALLXP) && !changer.equals(toChange)) {
				xpGive = -1;
			}
		}
		toChange.setAllRights(rank, xpGive, right, this.perso);
		SocketManager.GAME_SEND_gS_PACKET(this.perso, this.perso.getGuildMember());
		if (p != null && p.getId() != this.perso.getId()) {
			SocketManager.GAME_SEND_gS_PACKET(p, p.getGuildMember());
		}
	}

	private void joinOrLeaveTaxCollector(final String packet) {
		int TiD = -1;
		final String CollectorID = Integer.toString(Integer.parseInt(packet.substring(1)), 36);
		try {
			TiD = Integer.parseInt(CollectorID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TiD == -1) {
			return;
		}
		final Collector collector = World.getCollector(TiD);
		if (collector == null) {
			return;
		}
		if (this.perso.isDead() == 1) {
			SocketManager.GAME_SEND_BN(this.perso);
			return;
		}
		switch (packet.charAt(0)) {
		case 'J': {
			if (this.perso.get_fight() != null || this.perso.is_away() || this.perso.isInPrison()) {
				break;
			}
			if (collector.getDefenseFight().size() >= World.getMap(collector.getMap()).getMaxTeam1()) {
				return;
			}
			collector.addDefenseFight(this.perso);
			break;
		}
		case 'V': {
			collector.delDefenseFight(this.perso);
			break;
		}
		}
		for (final Player z : World.getGuild(collector.getGuildId()).getMembers()) {
			if (z == null) {
				continue;
			}
			if (!z.isOnline()) {
				continue;
			}
			SocketManager.GAME_SEND_gITM_PACKET(z, Collector.parseToGuild(collector.getGuildId()));
			Collector.parseAttaque(z, collector.getGuildId());
			Collector.parseDefense(z, collector.getGuildId());
		}
	}

	private void leavePanelGuildCreate() {
		SocketManager.GAME_SEND_gV_PACKET(this.perso);
	}

	private void parseHousePacket(String packet) {
		switch (packet.charAt(1)) {
		case 'B': {
			packet = packet.substring(2);
			House.buy(this.perso);
			break;
		}
		case 'G': {
			packet = packet.substring(2);
			if (packet.isEmpty()) {
				packet = null;
			}
			House.parseHG(this.perso, packet);
			break;
		}
		case 'Q': {
			packet = packet.substring(2);
			House.leave(this.perso, packet);
			break;
		}
		case 'S': {
			packet = packet.substring(2);
			House.sell(this.perso, packet);
			break;
		}
		case 'V': {
			House.closeBuy(this.perso);
			break;
		}
		}
	}

	private void parseEnemyPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			this.addEnemy(packet);
			break;
		}
		case 'D': {
			this.removeEnemy(packet);
			break;
		}
		case 'L': {
			SocketManager.GAME_SEND_ENEMY_LIST(this.perso);
			break;
		}
		}
	}

	private void addEnemy(String packet) {
		if (this.perso == null) {
			return;
		}
		int guid = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			final Player P = World.getPersoByName(packet);
			if (P == null) {
				SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
				return;
			}
			guid = P.getAccID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			final Account C = World.getCompteByPseudo(packet);
			if (C == null) {
				SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
				return;
			}
			guid = C.getGuid();
			break;
		}
		default: {
			packet = packet.substring(2);
			final Player Pr = World.getPersoByName(packet);
			if (Pr != null) {
				if (Pr.isOnline()) {
					guid = Pr.getAccount().getGuid();
					break;
				}
			}
			SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
			return;
		}
		}
		if (guid == -1) {
			SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
			return;
		}
		this.compte.addEnemy(packet, guid);
	}

	private void removeEnemy(String packet) {
		if (this.perso == null) {
			return;
		}
		int guid = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			final Player P = World.getPersoByName(packet);
			if (P == null) {
				SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
				return;
			}
			guid = P.getAccID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			final Account C = World.getCompteByPseudo(packet);
			if (C == null) {
				SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
				return;
			}
			guid = C.getGuid();
			break;
		}
		default: {
			packet = packet.substring(2);
			final Player Pr = World.getPersoByName(packet);
			if (Pr != null) {
				if (Pr.isOnline()) {
					guid = Pr.getAccount().getGuid();
					break;
				}
			}
			SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
			return;
		}
		}
		if (guid == -1 || !this.compte.isEnemyWith(guid)) {
			SocketManager.GAME_SEND_FD_PACKET(this.perso, "Ef");
			return;
		}
		this.compte.removeEnemy(guid);
	}

	private void parseJobOption(final String packet) {
		switch (packet.charAt(1)) {
		case 'O': {
			final String[] infos = packet.substring(2).split("\\|");
			final int pos = Integer.parseInt(infos[0]);
			final int option = Integer.parseInt(infos[1]);
			final int slots = Integer.parseInt(infos[2]);
			final JobStat SM = this.perso.getMetiers().get(pos);
			if (SM == null) {
				return;
			}
			SM.setOptBinValue(option);
			SM.setSlotsPublic(slots);
			SocketManager.GAME_SEND_JO_PACKET(this.perso, SM);
			break;
		}
		}
	}

	private void parseHouseKodePacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'V': {
			House.closeCode(this.perso);
			break;
		}
		case 'K': {
			this.sendKey(packet);
			break;
		}
		}
	}

	private void sendKey(String packet) {
		switch (packet.charAt(2)) {
		case '0': {
			packet = packet.substring(4);
			if (this.perso.get_savestat() > 0) {
				try {
					int code = 0;
					code = Integer.parseInt(packet);
					if (code < 0) {
						return;
					}
					if (this.perso.get_capital() < code) {
						code = this.perso.get_capital();
					}
					this.perso.boostStatFixedCount(this.perso.get_savestat(), code);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				} finally {
					this.perso.set_savestat(0);
					SocketManager.GAME_SEND_KODE(this.perso, "V");
				}
				this.perso.set_savestat(0);
				SocketManager.GAME_SEND_KODE(this.perso, "V");
				break;
			}
			if (this.perso.getInTrunk() != null) {
				Trunk.open(this.perso, packet, false);
				break;
			}
			House.open(this.perso, packet, false);
			break;
		}
		case '1': {
			packet = packet.substring(4);
			if (this.perso.getInTrunk() != null) {
				Trunk.lock(this.perso, packet);
				break;
			}
			House.lockIt(this.perso, packet);
			break;
		}
		}
	}

	private void parseObjectPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'd': {
			this.destroyObject(packet);
			break;
		}
		case 'D': {
			this.dropObject(packet);
			break;
		}
		case 'M': {
			this.movementObject(packet);
			break;
		}
		case 'U': {
			this.useObject(packet);
			break;
		}
		case 'x': {
			this.dissociateObvi(packet);
			break;
		}
		case 'f': {
			this.feedObvi(packet);
			break;
		}
		case 's': {
			this.setSkinObvi(packet);
			break;
		}
		}
	}

	private void destroyObject(final String packet) {
		final String[] infos = packet.substring(2).split("\\|");
		try {
			final int guid = Integer.parseInt(infos[0]);
			int qua = 1;
			try {
				qua = Integer.parseInt(infos[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			final org.aestia.object.Object obj = World.getObjet(guid);
			if (obj == null || !this.perso.hasItemGuid(guid) || qua <= 0 || this.perso.get_fight() != null
					|| this.perso.is_away()) {
				return;
			}
			if (obj.getPosition() != -1) {
				return;
			}
			if (qua > obj.getQuantity()) {
				qua = obj.getQuantity();
			}
			final int newQua = obj.getQuantity() - qua;
			if (newQua <= 0) {
				this.perso.removeItem(guid);
				World.removeItem(guid);
				Database.getStatique().getItemData().delete(guid);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, guid);
			} else {
				obj.setQuantity(newQua);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
			}
			SocketManager.GAME_SEND_STATS_PACKET(this.perso);
			SocketManager.GAME_SEND_Ow_PACKET(this.perso);
		} catch (Exception e2) {
			e2.printStackTrace();
			SocketManager.GAME_SEND_DELETE_OBJECT_FAILED_PACKET(this);
		}
	}

	private void dropObject(final String packet) {
		if (this.perso.getCurJobAction() != null || this.perso.getCurNpcExchange() != null
				|| this.perso.getCurNpcExchangePets() != null || this.perso.getCurNpcRessurectPets() != null
				|| this.perso.getCurExchange() != null || this.perso.getConcasseur() != null) {
			return;
		}
		int guid = -1;
		int qua = -1;
		try {
			guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			qua = Integer.parseInt(packet.split("\\|")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (guid == -1 || qua <= 0 || !this.perso.hasItemGuid(guid) || this.perso.get_fight() != null
				|| this.perso.is_away()) {
			return;
		}
		final org.aestia.object.Object obj = World.getObjet(guid);
		this.perso.setCurCell(this.perso.getCurCell());
		final int cellPosition = Constant.getNearCellidUnused(this.perso);
		if (cellPosition < 0) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1145");
			return;
		}
		if (obj.getPosition() != -1) {
			obj.setPosition(-1);
			SocketManager.GAME_SEND_OBJET_MOVE_PACKET(this.perso, obj);
			if (obj.getPosition() == 1 || obj.getPosition() == 6 || obj.getPosition() == 8 || obj.getPosition() == 7
					|| obj.getPosition() == 15 || obj.getPosition() == -1) {
				SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.perso.getCurMap(), this.perso);
			}
		}
		if (qua >= obj.getQuantity()) {
			this.perso.removeItem(guid);
			this.perso.getCurMap().getCase(cellPosition).addDroppedItem(obj);
			obj.setPosition(-1);
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, guid);
		} else {
			obj.setQuantity(obj.getQuantity() - qua);
			final org.aestia.object.Object obj2 = org.aestia.object.Object.getCloneObjet(obj, qua);
			obj2.setPosition(-1);
			this.perso.getCurMap().getCase(cellPosition).addDroppedItem(obj2);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
		}
		SocketManager.GAME_SEND_Ow_PACKET(this.perso);
		SocketManager.GAME_SEND_GDO_PACKET_TO_MAP(this.perso.getCurMap(), '+',
				this.perso.getCurMap().getCase(cellPosition).getId(), obj.getTemplate().getId(), 0);
		SocketManager.GAME_SEND_STATS_PACKET(this.perso);
	}

	private synchronized void movementObject(final String packet) {
		final String[] infos = packet.substring(2).split("\n")[0].split("\\|");
		try {
			final int guid = Integer.parseInt(infos[0]);
			final int pos = Integer.parseInt(infos[1]);
			int qua;
			try {
				qua = Integer.parseInt(infos[2]);
			} catch (Exception e3) {
				qua = 1;
			}
			final org.aestia.object.Object obj = World.getObjet(guid);
			if (!this.perso.hasItemGuid(guid) || obj == null) {
				return;
			}
			if (this.perso.get_fight() != null && this.perso.get_fight().getState() > 3) {
				return;
			}
			if (pos == 8 && !this.perso.isSubscribe()) {
				SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'S');
				return;
			}
			boolean craftsecure = false;
			if (pos == 1 || (pos == -1 && obj.getPosition() == 1)) {
				craftsecure = true;
			}
			if (pos == 8 && obj.getTemplate().getType() != 18 && this.perso.getObjetByPos(pos) != null) {
				final org.aestia.object.Object pets = this.perso.getObjetByPos(pos);
				final Pet p = World.getPets(pets.getTemplate().getId());
				if (p == null) {
					return;
				}
				if (p.getEpo() == obj.getTemplate().getId()) {
					final PetEntry MyPets = World.getPetsEntry(pets.getGuid());
					if (MyPets == null) {
						return;
					}
					if (p.getEpo() == obj.getTemplate().getId()) {
						MyPets.giveEpo(this.perso);
					}
				} else {
					if (obj.getTemplate().getId() != 2239
							&& !p.canEat(obj.getTemplate().getId(), obj.getTemplate().getType(), -1)) {
						SocketManager.GAME_SEND_Im_PACKET(this.perso, "153");
						return;
					}
					int min = 0;
					int max = 0;
					try {
						min = Integer.parseInt(p.getGap().split(",")[0]);
						max = Integer.parseInt(p.getGap().split(",")[1]);
					} catch (Exception ex) {
					}
					final PetEntry MyPets2 = World.getPetsEntry(pets.getGuid());
					if (MyPets2 == null) {
						return;
					}
					if (p.getType() == 2 || p.getType() == 3 || obj.getTemplate().getId() == 2239) {
						if (obj.getQuantity() - 1 > 0) {
							obj.setQuantity(obj.getQuantity() - 1);
							SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
						} else {
							World.removeItem(obj.getGuid());
							this.perso.removeItem(obj.getGuid());
							SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, obj.getGuid());
						}
						if (obj.getTemplate().getId() == 2239) {
							MyPets2.restoreLife(this.perso);
						} else {
							MyPets2.eat(this.perso, min, max,
									p.statsIdByEat(obj.getTemplate().getId(), obj.getTemplate().getType(), -1), obj);
						}
						SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(this.perso, pets);
						SocketManager.GAME_SEND_Ow_PACKET(this.perso);
						this.perso.refreshStats();
						SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.perso.getCurMap(), this.perso);
						SocketManager.GAME_SEND_STATS_PACKET(this.perso);
						if (this.perso.getGroup() != null) {
							SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(this.perso.getGroup(), this.perso);
						}
					}
				}
			} else {
				final ObjectTemplate objTemplate = obj.getTemplate();
				final int ObjPanoID = objTemplate.getPanoId();
				if (((ObjPanoID >= 81 && ObjPanoID <= 92) || (ObjPanoID >= 201 && ObjPanoID <= 212))
						&& (pos == 2 || pos == 3 || pos == 4 || pos == 5 || pos == 6 || pos == 7 || pos == 0)) {
					final String[] stats = objTemplate.getStrTemplate().split(",");
					String[] array;
					for (int length = (array = stats).length, i = 0; i < length; ++i) {
						final String stat = array[i];
						final String[] val = stat.split("#");
						final int effect = Integer.parseInt(val[0], 16);
						final int spell = Integer.parseInt(val[1], 16);
						final int modif = Integer.parseInt(val[3], 16);
						final String modifi = String.valueOf(effect) + ";" + spell + ";" + modif;
						SocketManager.SEND_SB_SPELL_BOOST(this.perso, modifi);
						this.perso.addItemClasseSpell(spell, effect, modif);
					}
					this.perso.addItemClasse(objTemplate.getId());
				}
				if (((ObjPanoID >= 81 && ObjPanoID <= 92) || (ObjPanoID >= 201 && ObjPanoID <= 212)) && pos == -1) {
					final String[] stats = objTemplate.getStrTemplate().split(",");
					String[] array2;
					for (int length2 = (array2 = stats).length, j = 0; j < length2; ++j) {
						final String stat = array2[j];
						final String[] val = stat.split("#");
						final String modifi2 = String.valueOf(Integer.parseInt(val[0], 16)) + ";"
								+ Integer.parseInt(val[1], 16) + ";0";
						SocketManager.SEND_SB_SPELL_BOOST(this.perso, modifi2);
						this.perso.removeItemClasseSpell(Integer.parseInt(val[1], 16));
					}
					this.perso.removeItemClasse(objTemplate.getId());
				}
				if (!Constant.isValidPlaceForItem(obj.getTemplate(), pos) && pos != -1
						&& obj.getTemplate().getType() != 113) {
					return;
				}
				if (!obj.getTemplate().getConditions().equalsIgnoreCase("")
						&& !ConditionParser.validConditions(this.perso, obj.getTemplate().getConditions())) {
					SocketManager.GAME_SEND_Im_PACKET(this.perso, "119|44");
					return;
				}
				if ((pos == 15 && this.perso.getObjetByPos(1) != null)
						|| (pos == 1 && this.perso.getObjetByPos(15) != null)) {
					if (this.perso.getObjetByPos(1) != null) {
						if (this.perso.getObjetByPos(1).getTemplate().isTwoHanded()) {
							SocketManager.GAME_SEND_Im_PACKET(this.perso, "119|44");
							return;
						}
					} else if (obj.getTemplate().isTwoHanded()) {
						SocketManager.GAME_SEND_Im_PACKET(this.perso, "119|44");
						return;
					}
				}
				if (obj.getTemplate().getLevel() > this.perso.getLevel()) {
					SocketManager.GAME_SEND_OAEL_PACKET(this);
					return;
				}
				if (pos != -1 && (obj.getTemplate().getPanoId() != -1 || obj.getTemplate().getType() == 23)
						&& this.perso.hasEquiped(obj.getTemplate().getId())) {
					return;
				}
				final org.aestia.object.Object exObj = this.perso.getObjetByPos2(pos);
				final int objGUID = obj.getTemplate().getId();
				if (obj.getTemplate().getType() == 113) {
					if (exObj == null) {
						SocketManager.send(this.perso, "Im1161");
						return;
					}
					if (exObj.getObvijevanPos() != 0) {
						SocketManager.GAME_SEND_BN(this.perso);
						return;
					}
					exObj.setObvijevanPos(obj.getObvijevanPos());
					Database.getStatique().getParoliData().add(obj, exObj);
					this.perso.removeItem(obj.getGuid(), 1, false, false);
					SocketManager.send(this.perso, "OR" + obj.getGuid());
					Database.getStatique().getItemData().delete(obj.getGuid());
					final StringBuilder cibleNewStats = new StringBuilder();
					cibleNewStats.append(obj.parseStatsStringSansUserObvi()).append(",")
							.append(exObj.parseStatsStringSansUserObvi());
					cibleNewStats.append(",3ca#").append(Integer.toHexString(objGUID)).append("#0#0#0d0+")
							.append(objGUID);
					exObj.clearStats();
					exObj.parseStringToStats(cibleNewStats.toString());
					SocketManager.send(this.perso, exObj.obvijevanOCO_Packet(pos));
					SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.perso.getCurMap(), this.perso);
					if (obj.getQuantity() > 1) {
						if (qua > obj.getQuantity()) {
							qua = obj.getQuantity();
						}
						if (obj.getQuantity() - qua > 0) {
							final int newItemQua = obj.getQuantity() - qua;
							final org.aestia.object.Object newItem = org.aestia.object.Object.getCloneObjet(obj,
									newItemQua);
							this.perso.addObjet(newItem, false);
							World.addObjet(newItem, true);
							obj.setQuantity(qua);
							SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
						}
					} else {
						World.removeItem(obj.getGuid());
					}
					Database.getStatique().getPlayerData().update(this.perso, true);
				} else {
					if (exObj != null) {
						final ObjectTemplate exObjTpl = exObj.getTemplate();
						final int idSetExObj = exObj.getTemplate().getPanoId();
						final org.aestia.object.Object obj2;
						if ((obj2 = this.perso.getSimilarItem(exObj)) != null) {
							obj2.setQuantity(obj2.getQuantity() + exObj.getQuantity());
							SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj2);
							World.removeItem(exObj.getGuid());
							this.perso.removeItem(exObj.getGuid());
							SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, exObj.getGuid());
						} else {
							exObj.setPosition(-1);
							if ((idSetExObj >= 81 && idSetExObj <= 92) || (idSetExObj >= 201 && idSetExObj <= 212)) {
								final String[] stats2 = exObjTpl.getStrTemplate().split(",");
								String[] array3;
								for (int length3 = (array3 = stats2).length, k = 0; k < length3; ++k) {
									final String stat2 = array3[k];
									final String[] val2 = stat2.split("#");
									final String modifi3 = String.valueOf(Integer.parseInt(val2[0], 16)) + ";"
											+ Integer.parseInt(val2[1], 16) + ";0";
									SocketManager.SEND_SB_SPELL_BOOST(this.perso, modifi3);
									this.perso.removeItemClasseSpell(Integer.parseInt(val2[1], 16));
								}
								this.perso.removeItemClasse(exObjTpl.getId());
							}
							SocketManager.GAME_SEND_OBJET_MOVE_PACKET(this.perso, exObj);
						}
						if (this.perso.getObjetByPos(1) == null) {
							SocketManager.GAME_SEND_OT_PACKET(this, -1);
						}
						if (exObj.getTemplate().getPanoId() > 0) {
							SocketManager.GAME_SEND_OS_PACKET(this.perso, exObj.getTemplate().getPanoId());
						}
					} else {
						final org.aestia.object.Object obj2;
						if ((obj2 = this.perso.getSimilarItem(obj)) != null) {
							if (qua > obj.getQuantity()) {
								qua = obj.getQuantity();
							}
							obj2.setQuantity(obj2.getQuantity() + qua);
							SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj2);
							if (obj.getQuantity() - qua > 0) {
								obj.setQuantity(obj.getQuantity() - qua);
								SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
							} else {
								World.removeItem(obj.getGuid());
								this.perso.removeItem(obj.getGuid());
								SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, obj.getGuid());
							}
						} else if (obj.getPosition() > 16) {
							final int oldPos = obj.getPosition();
							obj.setPosition(pos);
							SocketManager.GAME_SEND_OBJET_MOVE_PACKET(this.perso, obj);
							if (obj.getQuantity() > 1) {
								if (qua > obj.getQuantity()) {
									qua = obj.getQuantity();
								}
								if (obj.getQuantity() - qua > 0) {
									final org.aestia.object.Object newItem = org.aestia.object.Object.getCloneObjet(obj,
											obj.getQuantity() - qua);
									newItem.setPosition(oldPos);
									obj.setQuantity(qua);
									SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
									if (this.perso.addObjet(newItem, false)) {
										World.addObjet(newItem, true);
									}
								}
							}
						} else {
							obj.setPosition(pos);
							SocketManager.GAME_SEND_OBJET_MOVE_PACKET(this.perso, obj);
							if (obj.getQuantity() > 1) {
								if (qua > obj.getQuantity()) {
									qua = obj.getQuantity();
								}
								if (obj.getQuantity() - qua > 0) {
									final int newItemQua = obj.getQuantity() - qua;
									final org.aestia.object.Object newItem = org.aestia.object.Object.getCloneObjet(obj,
											newItemQua);
									if (this.perso.addObjet(newItem, false)) {
										World.addObjet(newItem, true);
									}
									obj.setQuantity(qua);
									SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
								}
							}
						}
					}
					if (pos == 1) {
						switch (obj.getTemplate().getId()) {
						case 9544: {
							this.perso.setFullMorph(1, false, false);
							break;
						}
						case 9545: {
							this.perso.setFullMorph(5, false, false);
							break;
						}
						case 9546: {
							this.perso.setFullMorph(4, false, false);
							break;
						}
						case 9547: {
							this.perso.setFullMorph(3, false, false);
							break;
						}
						case 9548: {
							this.perso.setFullMorph(2, false, false);
							break;
						}
						case 10125: {
							this.perso.setFullMorph(7, false, false);
							break;
						}
						case 10126: {
							this.perso.setFullMorph(6, false, false);
							break;
						}
						case 10127: {
							this.perso.setFullMorph(8, false, false);
							break;
						}
						case 10133: {
							this.perso.setFullMorph(9, false, false);
							break;
						}
						}
					} else if (Constant.isIncarnationWeapon(obj.getTemplate().getId())) {
						this.perso.unsetFullMorph();
					}
					if (obj.getTemplate().getId() == 2157) {
						if (pos == 6) {
							this.perso.set_gfxID((this.perso.getSexe() == 1) ? 8009 : 8006);
							SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), this.perso.getId());
							SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.perso.getCurMap(), this.perso);
							SocketManager.GAME_SEND_MESSAGE(this.perso,
									"Vous avez \u00e9t\u00e9 transform\u00e9 en mercenaire.");
						} else if (pos == -1) {
							this.perso.set_gfxID(this.perso.getClasse() * 10 + this.perso.getSexe());
							SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), this.perso.getId());
							SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.perso.getCurMap(), this.perso);
							SocketManager.GAME_SEND_MESSAGE(this.perso, "Vous n'\u00eates plus mercenaire.");
						}
					}
					if (obj.getTemplate().getId() != 2157 && this.perso.isMorphMercenaire() && pos == 6) {
						this.perso.set_gfxID(this.perso.getClasse() * 10 + this.perso.getSexe());
						SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(this.perso.getCurMap(), this.perso.getId());
						SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(this.perso.getCurMap(), this.perso);
						SocketManager.GAME_SEND_MESSAGE(this.perso, "Vous n'\u00eates plus mercenaire.");
					}
					SocketManager.GAME_SEND_Ow_PACKET(this.perso);
					this.perso.refreshStats();
					if (this.perso.getGroup() != null) {
						SocketManager.GAME_SEND_PM_MOD_PACKET_TO_GROUP(this.perso.getGroup(), this.perso);
					}
					SocketManager.GAME_SEND_STATS_PACKET(this.perso);
					if (pos == 1 || pos == 6 || pos == 8 || pos == 7 || pos == 15 || pos == -1) {
						SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.perso.getCurMap(), this.perso);
					}
					if (pos == 8 && this.perso.isOnMount()) {
						this.perso.toogleOnMount();
					}
					if (pos == 16 && this.perso.getMount() != null) {
						if (Config.contains(Config.itemFeedMount, obj.getTemplate().getType())) {
							if (obj.getQuantity() > 0) {
								if (qua > obj.getQuantity()) {
									qua = obj.getQuantity();
								}
								if (obj.getQuantity() - qua > 0) {
									final int nuevaCant = obj.getQuantity() - qua;
									obj.setQuantity(nuevaCant);
									SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
								} else {
									this.perso.deleteItem(guid);
									World.removeItem(guid);
									SocketManager.SEND_OR_DELETE_ITEM(this, guid);
								}
							}
							this.perso.getMount().aumEnergie(obj.getTemplate().getLevel(), qua);
							SocketManager.GAME_SEND_Re_PACKET(this.perso, "+", this.perso.getMount());
							return;
						}
						SocketManager.GAME_SEND_Im_PACKET(this.perso, "190");
					} else {
						if (pos == -1 && this.perso.getObjetByPos(1) == null) {
							SocketManager.GAME_SEND_OT_PACKET(this, -1);
						}
						if (pos == 1 && this.perso.getObjetByPos(1) != null) {
							for (final Map.Entry<Integer, JobStat> e : this.perso.getMetiers().entrySet()) {
								if (e.getValue().getTemplate()
										.isValidTool(this.perso.getObjetByPos(1).getTemplate().getId())) {
									SocketManager.GAME_SEND_OT_PACKET(this, e.getValue().getTemplate().getId());
								}
							}
						}
						if (obj.getTemplate().getPanoId() > 0) {
							SocketManager.GAME_SEND_OS_PACKET(this.perso, obj.getTemplate().getPanoId());
						}
						if (this.perso.get_fight() != null) {
							SocketManager.GAME_SEND_ON_EQUIP_ITEM_FIGHT(this.perso,
									this.perso.get_fight().getFighterByPerso(this.perso), this.perso.get_fight());
						}
						if (craftsecure) {
							final ArrayList<Job> jobs = this.perso.getJobs();
							if (jobs != null) {
								final org.aestia.object.Object object = this.perso.getObjetByPos(1);
								if (object != null) {
									final String arg = "EW+" + this.perso.getId() + "|";
									String data = "";
									for (final Job job : jobs) {
										if (job.getSkills().isEmpty()) {
											continue;
										}
										if (job.isMaging()) {
											continue;
										}
										if (!job.isValidTool(object.getTemplate().getId())) {
											continue;
										}
										for (final Case cell : this.perso.getCurMap().getCases().values()) {
											if (cell.getObject() != null && cell.getObject().getTemplate() != null) {
												final int io = cell.getObject().getTemplate().getId();
												final ArrayList<Integer> skills = job.getSkills().get(io);
												if (skills == null) {
													continue;
												}
												for (final int skill : skills) {
													if (!data.contains(String.valueOf(skill))) {
														data = String.valueOf(data)
																+ (data.isEmpty() ? skill : (";" + skill));
													}
												}
											}
										}
										if (!data.isEmpty()) {
											break;
										}
									}
									for (final Player player : this.perso.getCurMap().getPersos()) {
										player.send(String.valueOf(arg) + data);
									}
								} else {
									for (final Player player2 : this.perso.getCurMap().getPersos()) {
										player2.send("EW+" + this.perso.getId());
									}
								}
							}
						}
						this.perso.verifEquiped();
						Database.getStatique().getItemData().save(obj, false);
						Database.getStatique().getPlayerData().update(this.perso, true);
					}
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			SocketManager.GAME_SEND_DELETE_OBJECT_FAILED_PACKET(this);
		}
	}

	private void useObject(final String packet) {
		int guid = -1;
		int targetGuid = -1;
		short cellID = -1;
		Player target = null;
		try {
			final String[] infos = packet.substring(2).split("\\|");
			guid = Integer.parseInt(infos[0]);
			try {
				targetGuid = Integer.parseInt(infos[1]);
			} catch (Exception ex) {
			}
			try {
				cellID = Short.parseShort(infos[2]);
			} catch (Exception ex2) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (World.getPersonnage(targetGuid) != null) {
			target = World.getPersonnage(targetGuid);
		}
		if (!this.perso.hasItemGuid(guid) || this.perso.get_fight() != null || this.perso.is_away()) {
			return;
		}
		if (target != null && (target.get_fight() != null || target.is_away())) {
			return;
		}
		final org.aestia.object.Object obj = World.getObjet(guid);
		if (obj == null) {
			return;
		}
		final ObjectTemplate T = obj.getTemplate();
		if (!obj.getTemplate().getConditions().equalsIgnoreCase("")
				&& !ConditionParser.validConditions(this.perso, obj.getTemplate().getConditions())) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "119|43");
			return;
		}
		T.applyAction(this.perso, target, guid, cellID);
		if (T.getType() == 33 || T.getType() == 69) {
			if (target != null) {
				SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(target.getCurMap(), target.getId(), 17);
			} else {
				SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(this.perso.getCurMap(), this.perso.getId(), 17);
			}
		} else if (T.getType() == 37) {
			if (target != null) {
				SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(target.getCurMap(), target.getId(), 18);
			} else {
				SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(this.perso.getCurMap(), this.perso.getId(), 18);
			}
		}
	}

	private void dissociateObvi(final String packet) {
		int guid = -1;
		int pos = -1;
		try {
			guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			pos = Integer.parseInt(packet.split("\\|")[1]);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (guid == -1 || !this.perso.hasItemGuid(guid)) {
			return;
		}
		final org.aestia.object.Object obj = World.getObjet(guid);
		int idOBVI = Database.getStatique().getParoliData().getAndDelete(obj, true);
		if (idOBVI == -1) {
			switch (obj.getTemplate().getType()) {
			case 1: {
				idOBVI = 9255;
				break;
			}
			case 9: {
				idOBVI = 9256;
				break;
			}
			case 16: {
				idOBVI = 9234;
				break;
			}
			case 17: {
				idOBVI = 9233;
				break;
			}
			default: {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Erreur d'obvijevan numero: 4. Merci de nous le signaler si le probleme est grave.", "000000");
				return;
			}
			}
		}
		final ObjectTemplate t = World.getObjTemplate(idOBVI);
		final org.aestia.object.Object obV = t.createNewItem(1, true);
		final String obviStats = obj.getObvijevanStatsOnly();
		if (obviStats == "") {
			SocketManager.GAME_SEND_MESSAGE(this.perso,
					"Erreur d'obvijevan numero: 3. Merci de nous le signaler si le probleme est grave.", "000000");
			return;
		}
		obV.clearStats();
		obV.parseStringToStats(obviStats);
		if (this.perso.addObjet(obV, true)) {
			World.addObjet(obV, true);
		}
		obj.removeAllObvijevanStats();
		SocketManager.send(this.perso, obj.obvijevanOCO_Packet(pos));
		SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.perso.getCurMap(), this.perso);
		Database.getStatique().getPlayerData().update(this.perso, true);
	}

	private void feedObvi(final String packet) {
		int guid = -1;
		int pos = -1;
		int victime = -1;
		try {
			guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			pos = Integer.parseInt(packet.split("\\|")[1]);
			victime = Integer.parseInt(packet.split("\\|")[2]);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (guid == -1 || !this.perso.hasItemGuid(guid)) {
			return;
		}
		final org.aestia.object.Object obj = World.getObjet(guid);
		final org.aestia.object.Object objVictime = World.getObjet(victime);
		obj.obvijevanNourir(objVictime);
		final int qua = objVictime.getQuantity();
		if (qua <= 1) {
			this.perso.removeItem(objVictime.getGuid());
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, objVictime.getGuid());
		} else {
			objVictime.setQuantity(qua - 1);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, objVictime);
		}
		SocketManager.send(this.perso, obj.obvijevanOCO_Packet(pos));
		Database.getStatique().getItemData().save(obj, true);
		Database.getStatique().getItemData().save(objVictime, true);
		Database.getStatique().getPlayerData().update(this.perso, true);
	}

	private void setSkinObvi(final String packet) {
		int guid = -1;
		int pos = -1;
		int val = -1;
		try {
			guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			pos = Integer.parseInt(packet.split("\\|")[1]);
			val = Integer.parseInt(packet.split("\\|")[2]);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (guid == -1 || !this.perso.hasItemGuid(guid)) {
			return;
		}
		final org.aestia.object.Object obj = World.getObjet(guid);
		if (val >= 21 || val <= 0) {
			return;
		}
		obj.obvijevanChangeStat(972, val);
		SocketManager.send(this.perso, obj.obvijevanOCO_Packet(pos));
		if (pos != -1) {
			SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.perso.getCurMap(), this.perso);
		}
	}

	private void parseGroupPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			this.acceptInvitation(packet);
			break;
		}
		case 'F': {
			this.followMember(packet);
			break;
		}
		case 'G': {
			this.followAllMember(packet);
			break;
		}
		case 'I': {
			this.inviteParty(packet);
			break;
		}
		case 'R': {
			this.refuseInvitation();
			break;
		}
		case 'V': {
			this.leaveParty(packet);
			break;
		}
		case 'W': {
			this.whereIsParty();
			break;
		}
		}
	}

	private void acceptInvitation(final String packet) {
		if (this.perso == null) {
			return;
		}
		if (this.perso.getInvitation() == 0) {
			return;
		}
		final Player t = World.getPersonnage(this.perso.getInvitation());
		if (t == null) {
			return;
		}
		Group g = t.getGroup();
		if (g == null) {
			g = new Group(t, this.perso);
			SocketManager.GAME_SEND_GROUP_CREATE(this, g);
			SocketManager.GAME_SEND_PL_PACKET(this, g);
			SocketManager.GAME_SEND_GROUP_CREATE(t.getGameClient(), g);
			SocketManager.GAME_SEND_PL_PACKET(t.getGameClient(), g);
			t.setGroup(g);
			SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(t.getGameClient(), g);
		} else {
			SocketManager.GAME_SEND_GROUP_CREATE(this, g);
			SocketManager.GAME_SEND_PL_PACKET(this, g);
			SocketManager.GAME_SEND_PM_ADD_PACKET_TO_GROUP(g, this.perso);
			g.addPerso(this.perso);
		}
		this.perso.setGroup(g);
		SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(this, g);
		SocketManager.GAME_SEND_PR_PACKET(t);
	}

	private void followMember(final String packet) {
		final Group g = this.perso.getGroup();
		if (g == null) {
			return;
		}
		int pGuid = -1;
		try {
			pGuid = Integer.parseInt(packet.substring(3));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		if (pGuid == -1) {
			return;
		}
		final Player P = World.getPersonnage(pGuid);
		if (P == null || !P.isOnline()) {
			return;
		}
		if (packet.charAt(2) == '+') {
			if (this.perso._Follows != null) {
				this.perso._Follows._Follower.remove(this.perso.getId());
			}
			SocketManager.GAME_SEND_FLAG_PACKET(this.perso, P);
			SocketManager.GAME_SEND_PF(this.perso, "+" + P.getId());
			this.perso._Follows = P;
			P._Follower.put(this.perso.getId(), this.perso);
			P.send("Im052;" + this.perso.getName());
		} else if (packet.charAt(2) == '-') {
			SocketManager.GAME_SEND_DELETE_FLAG_PACKET(this.perso);
			SocketManager.GAME_SEND_PF(this.perso, "-");
			this.perso._Follows = null;
			P._Follower.remove(this.perso.getId());
			P.send("Im053;" + this.perso.getName());
		}
	}

	private void followAllMember(final String packet) {
		final Group g2 = this.perso.getGroup();
		if (g2 == null) {
			return;
		}
		int pGuid2 = -1;
		try {
			pGuid2 = Integer.parseInt(packet.substring(3));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		if (pGuid2 == -1) {
			return;
		}
		final Player P2 = World.getPersonnage(pGuid2);
		if (P2 == null || !P2.isOnline()) {
			return;
		}
		if (packet.charAt(2) == '+') {
			for (final Player T : g2.getPersos()) {
				if (T.getId() == P2.getId()) {
					continue;
				}
				if (T._Follows != null) {
					T._Follows._Follower.remove(this.perso.getId());
				}
				SocketManager.GAME_SEND_FLAG_PACKET(T, P2);
				SocketManager.GAME_SEND_PF(T, "+" + P2.getId());
				T._Follows = P2;
				P2._Follower.put(T.getId(), T);
				P2.send("Im0178");
			}
		} else if (packet.charAt(2) == '-') {
			for (final Player T : g2.getPersos()) {
				if (T.getId() == P2.getId()) {
					continue;
				}
				SocketManager.GAME_SEND_DELETE_FLAG_PACKET(T);
				SocketManager.GAME_SEND_PF(T, "-");
				T._Follows = null;
				P2._Follower.remove(T.getId());
				P2.send("Im053;" + T.getName());
			}
		}
	}

	private void inviteParty(final String packet) {
		if (this.perso == null) {
			return;
		}
		final String name = packet.substring(2);
		final Player target = World.getPersoByName(name);
		if (target == null) {
			return;
		}
		if (!target.isOnline()) {
			SocketManager.GAME_SEND_GROUP_INVITATION_ERROR(this, "n" + name);
			return;
		}
		if (target.getGroup() != null) {
			SocketManager.GAME_SEND_GROUP_INVITATION_ERROR(this, "a" + name);
			return;
		}
		if (target.getGroupe() != null && this.perso.getGroupe() == null && !target.getGroupe().isPlayer()) {
			SocketManager.GAME_SEND_MESSAGE(this.perso, "Vous n'avez pas la permission d'inviter ce joueur en groupe.");
			return;
		}
		if (this.perso.getGroup() != null && this.perso.getGroup().getPersosNumber() == 8) {
			SocketManager.GAME_SEND_GROUP_INVITATION_ERROR(this, "f");
			return;
		}
		target.setInvitation(this.perso.getId());
		this.perso.setInvitation(target.getId());
		SocketManager.GAME_SEND_GROUP_INVITATION(this, this.perso.getName(), name);
		SocketManager.GAME_SEND_GROUP_INVITATION(target.getGameClient(), this.perso.getName(), name);
	}

	private void refuseInvitation() {
		if (this.perso == null) {
			return;
		}
		if (this.perso.getInvitation() == 0) {
			return;
		}
		SocketManager.GAME_SEND_BN(this.perso.getGameClient());
		final Player t = World.getPersonnage(this.perso.getInvitation());
		if (t == null) {
			return;
		}
		SocketManager.GAME_SEND_PR_PACKET(t);
		t.setInvitation(0);
		this.perso.setInvitation(0);
	}

	private void leaveParty(final String packet) {
		if (this.perso == null) {
			return;
		}
		final Group g = this.perso.getGroup();
		if (g == null) {
			return;
		}
		if (packet.length() == 2) {
			g.leave(this.perso);
			SocketManager.GAME_SEND_PV_PACKET(this, "");
			SocketManager.GAME_SEND_IH_PACKET(this.perso, "");
		} else if (g.isChief(this.perso.getId())) {
			int guid = -1;
			try {
				guid = Integer.parseInt(packet.substring(2));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return;
			}
			if (guid == -1) {
				return;
			}
			final Player t = World.getPersonnage(guid);
			g.leave(t);
			SocketManager.GAME_SEND_PV_PACKET(t.getGameClient(),
					new StringBuilder().append(this.perso.getId()).toString());
			SocketManager.GAME_SEND_IH_PACKET(t, "");
		}
	}

	private void whereIsParty() {
		if (this.perso == null) {
			return;
		}
		final Group g = this.perso.getGroup();
		if (g == null) {
			return;
		}
		String str = "";
		boolean isFirst = true;
		for (final Player GroupP : this.perso.getGroup().getPersos()) {
			if (!isFirst) {
				str = String.valueOf(str) + "|";
			}
			str = String.valueOf(str) + GroupP.getCurMap().getX() + ";" + GroupP.getCurMap().getY() + ";"
					+ GroupP.getCurMap().getId() + ";2;" + GroupP.getId() + ";" + GroupP.getName();
			isFirst = false;
		}
		SocketManager.GAME_SEND_IH_PACKET(this.perso, str);
	}

	private void parseMountPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'b': {
			this.buyMountPark(packet);
			break;
		}
		case 'd': {
			this.dataMount(packet, true);
			break;
		}
		case 'p': {
			this.dataMount(packet, false);
			break;
		}
		case 'f': {
			this.killMount(packet);
			break;
		}
		case 'n': {
			this.renameMount(packet.substring(2));
			break;
		}
		case 'r': {
			this.rideMount();
			break;
		}
		case 's': {
			this.sellMountPark(packet);
			break;
		}
		case 'v': {
			SocketManager.GAME_SEND_R_PACKET(this.perso, "v");
			break;
		}
		case 'x': {
			this.setXpMount(packet);
			break;
		}
		case 'c': {
			this.castrateMount();
			break;
		}
		case 'o': {
			this.removeObjectInMountPark(packet);
			break;
		}
		}
	}

	private void buyMountPark(final String packet) {
		SocketManager.GAME_SEND_R_PACKET(this.perso, "v");
		final MountPark MP = this.perso.getCurMap().getMountPark();
		final Player Seller = World.getPersonnage(MP.getOwner());
		if (MP.getOwner() == -1) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "196");
			return;
		}
		if (MP.getPrice() == 0) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "197");
			return;
		}
		if (this.perso.get_guild() == null) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1135");
			return;
		}
		if (this.perso.getGuildMember().getRank() != 1) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "198");
			return;
		}
		final byte enclosMax = (byte) Math.floor(this.perso.get_guild().getLvl() / 10);
		final byte TotalEncloGuild = (byte) World.totalMPGuild(this.perso.get_guild().getId());
		if (TotalEncloGuild >= enclosMax) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "1103");
			return;
		}
		if (this.perso.get_kamas() < MP.getPrice()) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "182");
			return;
		}
		final long NewKamas = this.perso.get_kamas() - MP.getPrice();
		this.perso.set_kamas(NewKamas);
		if (Seller != null) {
			final long NewSellerBankKamas = Seller.getBankKamas() + MP.getPrice();
			Seller.setBankKamas(NewSellerBankKamas);
			if (Seller.isOnline()) {
				SocketManager.GAME_SEND_MESSAGE(this.perso,
						"Vous venez de vendre votre enclos \u00e0 " + MP.getPrice() + ".");
			}
		}
		MP.setPrice(0);
		MP.setOwner(this.perso.getId());
		MP.setGuild(this.perso.get_guild());
		Database.getGame().getMountpark_dataData().update(MP);
		Database.getStatique().getPlayerData().update(this.perso, true);
		for (final Player z : this.perso.getCurMap().getPersos()) {
			SocketManager.GAME_SEND_Rp_PACKET(z, MP);
		}
	}

	private void dataMount(final String packet, final boolean b) {
		int DDid = 0;
		Dragodinde DD = null;
		try {
			DDid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (DDid == 0) {
			return;
		}
		if (b) {
			DD = World.getDragoByID(-DDid);
		}
		if (!b) {
			DD = World.getDragoByID(DDid);
		}
		if (DD == null) {
			return;
		}
		SocketManager.GAME_SEND_MOUNT_DESCRIPTION_PACKET(this.perso, DD);
	}

	private void killMount(final String packet) {
		synchronized (this.perso.getMount().getItems()) {
			if (this.perso.getMount().getItems().size() != 0) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "1106");
				// monitorexit(this.perso.getMount().getItems())
				return;
			}
		}
		// monitorexit(this.perso.getMount().getItems())
		if (this.perso.getMount() != null && this.perso.isOnMount()) {
			this.perso.toogleOnMount();
		}
		SocketManager.GAME_SEND_Re_PACKET(this.perso, "-", this.perso.getMount());
		Database.getStatique().getMounts_dataData().delete(this.perso.getMount().getId());
		World.removeDragodinde(this.perso.getMount().getId());
		this.perso.setMount(null);
	}

	private void renameMount(final String name) {
		if (this.perso.getMount() == null) {
			return;
		}
		this.perso.getMount().setName(name);
		SocketManager.GAME_SEND_Rn_PACKET(this.perso, name);
	}

	private void rideMount() {
		if (!this.perso.isSubscribe()) {
			SocketManager.GAME_SEND_EXCHANGE_REQUEST_ERROR(this, 'S');
			return;
		}
		this.perso.toogleOnMount();
	}

	private void sellMountPark(final String packet) {
		SocketManager.GAME_SEND_R_PACKET(this.perso, "v");
		final int price = Integer.parseInt(packet.substring(2));
		final MountPark MP1 = this.perso.getCurMap().getMountPark();
		if (!MP1.getData().isEmpty()) {
			SocketManager.GAME_SEND_MESSAGE(this.perso, "[ENCLO] Impossible de vendre un enclo plein.");
			return;
		}
		if (MP1.getOwner() == -1) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "194");
			return;
		}
		if (MP1.getOwner() != this.perso.getId()) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "195");
			return;
		}
		MP1.setPrice(price);
		Database.getGame().getMountpark_dataData().update(MP1);
		Database.getStatique().getPlayerData().update(this.perso, true);
		for (final Player z : this.perso.getCurMap().getPersos()) {
			SocketManager.GAME_SEND_Rp_PACKET(z, MP1);
		}
	}

	private void setXpMount(final String packet) {
		try {
			int xp = Integer.parseInt(packet.substring(2));
			if (xp < 0) {
				xp = 0;
			}
			if (xp > 90) {
				xp = 90;
			}
			this.perso.setMountGiveXp(xp);
			SocketManager.GAME_SEND_Rx_PACKET(this.perso);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void castrateMount() {
		if (this.perso.getMount() == null) {
			SocketManager.GAME_SEND_Re_PACKET(this.perso, "Er", null);
			return;
		}
		this.perso.getMount().setCastred();
		SocketManager.GAME_SEND_Re_PACKET(this.perso, "+", this.perso.getMount());
	}

	private void removeObjectInMountPark(final String packet) {
		final int cell = Integer.parseInt(packet.substring(2));
		final org.aestia.map.Map map = this.perso.getCurMap();
		if (map.getMountPark() == null) {
			return;
		}
		final MountPark MP = map.getMountPark();
		if (this.perso.getName() != "[Admin]") {
			if (this.perso.get_guild() == null) {
				SocketManager.GAME_SEND_BN(this);
				return;
			}
			if (!this.perso.getGuildMember().canDo(8192)) {
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "193");
				return;
			}
		}
		final int item = MP.getCellAndObject().get(cell);
		final ObjectTemplate t = World.getObjTemplate(item);
		final org.aestia.object.Object obj = t.createNewItem(1, false);
		int statNew = 0;
		for (final Map.Entry<Integer, Map<Integer, Integer>> entry : MP.getObjDurab().entrySet()) {
			if (entry.getKey().equals(cell)) {
				for (final Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
					statNew = entry2.getValue();
				}
			}
		}
		obj.getTxtStat().remove(812);
		obj.addTxtStat(812, Integer.toHexString(statNew));
		if (this.perso.addObjet(obj, true)) {
			World.addObjet(obj, true);
		}
		if (MP.delObject(cell)) {
			SocketManager.SEND_GDO_PUT_OBJECT_MOUNT(map, String.valueOf(cell) + ";0;0");
		}
	}

	private void parseQuestData(final String packet) {
		switch (packet.charAt(1)) {
		case 'L': {
			SocketManager.QuestList(this, this.perso);
			break;
		}
		case 'S': {
			final int QuestID = Integer.parseInt(packet.substring(2));
			final Quest quest = Quest.getQuestById(QuestID);
			SocketManager.QuestGep(this, quest, this.perso);
			break;
		}
		}
	}

	private void parseSpellPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'B': {
			this.boostSpell(packet);
			break;
		}
		case 'F': {
			this.forgetSpell(packet);
			break;
		}
		case 'M': {
			this.moveToUsed(packet);
			break;
		}
		}
	}

	private void boostSpell(final String packet) {
		try {
			final int id = Integer.parseInt(packet.substring(2));
			if (!this.perso.boostSpell(id)) {
				SocketManager.GAME_SEND_SPELL_UPGRADE_FAILED(this);
				return;
			}
			SocketManager.GAME_SEND_SPELL_UPGRADE_SUCCED(this, id, this.perso.getSortStatBySortIfHas(id).getLevel());
			SocketManager.GAME_SEND_STATS_PACKET(this.perso);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			SocketManager.GAME_SEND_SPELL_UPGRADE_FAILED(this);
		}
	}

	private void forgetSpell(final String packet) {
		if (!this.perso.isForgetingSpell()) {
			return;
		}
		final int id = Integer.parseInt(packet.substring(2));
		if (this.perso.forgetSpell(id)) {
			SocketManager.GAME_SEND_SPELL_UPGRADE_SUCCED(this, id, this.perso.getSortStatBySortIfHas(id).getLevel());
			SocketManager.GAME_SEND_STATS_PACKET(this.perso);
			this.perso.setisForgetingSpell(false);
		}
	}

	private void moveToUsed(final String packet) {
		try {
			final int SpellID = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			final int Position = Integer.parseInt(packet.substring(2).split("\\|")[1]);
			final Spell.SortStats Spell = this.perso.getSortStatBySortIfHas(SpellID);
			if (Spell != null) {
				this.perso.set_SpellPlace(SpellID, CryptManager.getHashedValueByInt(Position));
			}
			SocketManager.GAME_SEND_BN(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseWaypointPacket(final String packet) {
		switch (packet.charAt(1)) {
		case 'U': {
			this.waypointUse(packet);
			break;
		}
		case 'u': {
			this.zaapiUse(packet);
			break;
		}
		case 'p': {
			this.prismUse(packet);
			break;
		}
		case 'V': {
			this.waypointLeave();
			break;
		}
		case 'v': {
			this.zaapiLeave();
			break;
		}
		case 'w': {
			this.prismLeave();
			break;
		}
		}
	}

	private void waypointUse(final String packet) {
		short id = -1;
		try {
			id = Short.parseShort(packet.substring(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (id == -1) {
			return;
		}
		this.perso.useZaap(id);
	}

	private void zaapiUse(final String packet) {
		if (this.perso.getDeshonor() >= 2) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "183");
			return;
		}
		this.perso.Zaapi_use(packet);
	}

	private void prismUse(final String packet) {
		if (this.perso.getDeshonor() >= 2) {
			SocketManager.GAME_SEND_Im_PACKET(this.perso, "183");
			return;
		}
		this.perso.usePrisme(packet);
	}

	private void waypointLeave() {
		this.perso.stopZaaping();
	}

	private void zaapiLeave() {
		this.perso.Zaapi_close();
	}

	private void prismLeave() {
		this.perso.Prisme_close();
	}

	private void parseFoireTroll(final String packet) {
		final String[] param = packet.split("\\|");
		final Tutorial tuto = this.perso.getTutorial();
		this.perso.setTutorial(null);
		switch (packet.charAt(1)) {
		case 'V': {
			if (packet.charAt(2) != '0' && packet.charAt(2) != '4') {
				try {
					final int index = Integer.parseInt(new StringBuilder(String.valueOf(packet.charAt(2))).toString())
							- 1;
					tuto.getReward().get(index).apply(this.perso, null, -1, -1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				final Action end = tuto.getEnd();
				if (end != null && this.perso != null) {
					end.apply(this.perso, null, -1, -1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.perso.set_away(false);
			try {
				this.perso.set_orientation(Byte.parseByte(param[2]));
				this.perso.setCurCell(this.perso.getCurMap().getCase(Short.parseShort(param[1])));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		}
	}

	public void kick() {
		try {
			if (this.compte != null && this.perso != null) {
				this.compte.disconnect(this.perso);
			}
			this.session.close(true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void addAction(final GameAction GA) {
		this.actions.put(GA.id, GA);
		if (GA.actionId == 1) {
			this.walk = true;
		}
		if (Main.modDebug) {
			Console.println("Game > Create action id : " + GA.id, Console.Color.INFORMATION);
		}
		if (Main.modDebug) {
			Console.println("Game > Packet : " + GA.packet, Console.Color.INFORMATION);
		}
	}

	public synchronized void removeAction(final GameAction GA) {
		if (GA.actionId == 1) {
			this.walk = false;
		}
		if (Main.modDebug) {
			Console.println("Game >  Delete action id : " + GA.id, Console.Color.INFORMATION);
		}
		this.actions.remove(GA.id);
		if (this.actions.get(-1) == null || GA.actionId != 1) {
			return;
		}
		final String packet = this.actions.get(-1).packet.substring(5);
		final int cell = Integer.parseInt(packet.split(";")[0]);
		ArrayList<Integer> list = null;
		try {
			list = Pathfinding.getAllCaseIdAllDirrection(cell, this.perso.getCurMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.contains(this.perso.getCurCell().getId()) || this.distPecheur()) {
			this.perso.getGameClient().gameAction(this.actions.get(-1));
			this.actions.remove(-1);
			return;
		}
		this.actions.remove(-1);
	}

	private boolean distPecheur() {
		try {
			final String packet = this.actions.get(-1).packet.substring(5);
			final JobStat SM = this.perso.getMetierBySkill(Integer.parseInt(packet.split(";")[1]));
			if (SM == null) {
				return false;
			}
			if (SM.getTemplate() == null) {
				return false;
			}
			if (SM.getTemplate().getId() != 36) {
				return false;
			}
			final int dis = Pathfinding.getDistanceBetween(this.perso.getCurMap(),
					Integer.parseInt(packet.split(";")[0]), this.perso.getCurCell().getId());
			final int dist = JobConstant.getDistCanne(this.perso.getObjetByPos(1).getTemplate().getId());
			if (dis <= dist) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public IoSession getSession() {
		return this.session;
	}

	public void setSession(final IoSession ioSession) {
		this.session = ioSession;
	}

	public Player getPersonnage() {
		return this.perso;
	}

	public Account getAccount() {
		return this.compte;
	}

	public TimerWaiter getWaiter() {
		return this.waiter;
	}
}
