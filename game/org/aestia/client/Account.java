// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.command.server.Groupes;
import org.aestia.common.CryptManager;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.GameClient;
import org.aestia.game.world.World;
import org.aestia.hdv.HdvEntry;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;

public class Account {
	private int guid;
	private String name;
	private String pass;
	private String pseudo;
	private String key;
	private String question;
	private String answer;
	private String curIP;
	private String lastIP;
	private String lastConnectionDate;
	private int vip;
	private int points;
	private int position;
	private long muteTime;
	private String muteRaison;
	private String mutePseudo;
	private boolean banned;
	private long subscriber;
	private long bankKamas;
	private Player curPerso;
	private GameClient gameClient;
	private byte state;
	private Map<Integer, org.aestia.object.Object> bank;
	private ArrayList<Integer> friendGuids;
	private ArrayList<Integer> enemyGuids;
	private Map<Integer, ArrayList<HdvEntry>> hdvsItems;
	private long lastConnectDay;
	private String lastVoteIP;
	private long heureVote;

	public Account(final int guid, final String name, final String pass, final String pseudo, final String question,
			final String answer, final int vip, final boolean banned, final String lastIp,
			final String lastConnectionDate, final String friends, final String enemy, final int points,
			final long subscriber, final long muteTime, final String muteRaison, final String mutePseudo,
			final String lastConnectDay, final String lastVoteIP, final String heureVote,String bank) {
		this.curIP = "";
		this.lastIP = "";
		this.lastConnectionDate = "";
		this.vip = 0;
		this.position = -1;
		this.muteTime = 0L;
		this.muteRaison = "";
		this.mutePseudo = "";
		this.banned = false;
		this.subscriber = 1L;
		this.bankKamas = 0L;
		this.bank = new TreeMap<Integer, org.aestia.object.Object>();
		this.friendGuids = new ArrayList<Integer>();
		this.enemyGuids = new ArrayList<Integer>();
		this.guid = guid;
		this.name = name;
		this.pass = pass;
		this.pseudo = pseudo;
		this.question = question;
		this.answer = answer;
		this.vip = vip;
		this.banned = banned;
		this.lastIP = lastIp;
		this.lastConnectionDate = lastConnectionDate;
		this.hdvsItems = World.getMyItems(guid);
		this.points = points;
		this.subscriber = subscriber;
		this.muteTime = muteTime;
		this.muteRaison = muteRaison;
		this.mutePseudo = mutePseudo;
		this.lastVoteIP = lastVoteIP;
		if (heureVote.equalsIgnoreCase("")) {
			this.heureVote = 0L;
		} else {
			this.heureVote = Long.parseLong(heureVote);
		}
		if (lastConnectDay.equalsIgnoreCase("") || lastConnectDay.equalsIgnoreCase("0")) {
			this.lastConnectDay = 0L;
		} else {
			this.lastConnectDay = Long.parseLong(lastConnectDay);
		}
			try {
				bankKamas = Integer.parseInt(bank.split(";")[0]);
			} catch (Exception e ) {
				this.bankKamas = 0;
			}
			String allItem = "";
			try {
				allItem = bank.split("\\;")[1];
			} catch (Exception ex) {
				
			}
			if (!allItem.equals("")) {
				String[] split;
				for (int length = (split = allItem.split("\\,")).length, i = 0; i < length; ++i) {
					final String item = split[i];
					if (!item.equals("")) {
						final int id = Integer.parseInt(item);
						final org.aestia.object.Object obj = World.getObjet(id);
						if (obj != null) {
							this.bank.put(obj.getGuid(), obj);
						}
					}
				}
			}
		if (!Database.getStatique().getGiftData().existByAccount(guid)) {
			Database.getStatique().getGiftData().create(guid);
		}
		if (!friends.equalsIgnoreCase("")) {
			String[] split2;
			for (int length2 = (split2 = friends.split(";")).length, j = 0; j < length2; ++j) {
				final String f = split2[j];
				try {
					this.friendGuids.add(Integer.parseInt(f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!enemy.equalsIgnoreCase("")) {
			String[] split3;
			for (int length3 = (split3 = enemy.split(";")).length, k = 0; k < length3; ++k) {
				final String e2 = split3[k];
				try {
					this.enemyGuids.add(Integer.parseInt(e2));
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		}
	}

	public void updateInfos(final int aGUID, final String aName, final String aPass, final String aPseudo,
			final String aQuestion, final String aAnswer, final boolean aBanned) {
		this.guid = aGUID;
		this.name = aName;
		this.pass = aPass;
		this.pseudo = aPseudo;
		this.question = aQuestion;
		this.answer = aAnswer;
		this.banned = aBanned;
	}

	public long getHeureVote() {
		return this.heureVote;
	}

	public String getLastVoteIP() {
		return this.lastVoteIP;
	}

	public int getGuid() {
		return this.guid;
	}

	public void setGuid(final int i) {
		this.guid = i;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String i) {
		this.name = i;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(final String i) {
		this.pass = i;
	}

	public boolean isValidPass(final String pass, final String hash) {
		return pass.equals(CryptManager.CryptPassword(hash, this.pass));
	}

	public String getPseudo() {
		if (this.pseudo.isEmpty() || this.pseudo == "") {
			this.pseudo = this.name;
		}
		return this.pseudo;
	}

	public void setPseudo(final String i) {
		this.pseudo = i;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(final String i) {
		this.key = i;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(final String i) {
		this.question = i;
	}

	public String getAwnser() {
		return this.answer;
	}

	public void setAwnser(final String i) {
		this.answer = i;
	}

	public String getCurIP() {
		return this.curIP;
	}

	public void setCurIP(final String i) {
		this.curIP = i;
	}

	public String getLastIP() {
		return this.lastIP;
	}

	public void setLastIP(final String i) {
		this.lastIP = i;
	}

	public String getLastConnectionDate() {
		return this.lastConnectionDate;
	}

	public void setLastConnectionDate(final String i) {
		this.lastConnectionDate = i;
	}

	public int getVip() {
		return this.vip;
	}

	public void setVip(final int i) {
		this.vip = i;
	}

	public void setPoints(final int i) {
		this.points = i;
		Database.getStatique().getAccountData().updatePoints(this.guid, this.points);
	}

	public int getPoints() {
		return this.points = Database.getStatique().getAccountData().loadPoints(this.name);
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(final int i) {
		this.position = i;
	}

	public void mute(final int nbMinutes, final String raison, final String pseudo) {
		if (nbMinutes <= 0) {
			return;
		}
		this.muteTime = System.currentTimeMillis() / 1000L + nbMinutes * 60;
		this.muteRaison = raison;
		this.mutePseudo = pseudo;
		Database.getStatique().getAccountData().update(this);
		if (this.curPerso != null) {
			final StringBuilder imMess = new StringBuilder("117;").append(pseudo).append("~").append(nbMinutes)
					.append("~").append(raison);
			SocketManager.GAME_SEND_Im_PACKET(this.curPerso, imMess.toString());
		}
	}

	public void unMute() {
		if (this.muteTime == 0L) {
			return;
		}
		this.muteTime = 0L;
		this.muteRaison = "";
		this.mutePseudo = "";
		if (this.curPerso != null) {
			SocketManager.GAME_SEND_MESSAGE(this.curPerso, "Vous avez \u00e9t\u00e9 d\u00e9mut\u00e9.");
		}
		Database.getStatique().getAccountData().update(this);
	}

	public void sendMutedIm() {
		if (!this.isMuted()) {
			return;
		}
		if (this.curPerso != null) {
			SocketManager.GAME_SEND_MESSAGE(this.curPerso, "Vous \u00eates toujours mute !");
		}
	}

	public boolean isMuted() {
		if (this.muteTime == 0L) {
			return false;
		}
		if (this.muteTime >= System.currentTimeMillis() / 1000L) {
			return true;
		}
		this.muteTime = 0L;
		this.muteRaison = "";
		this.mutePseudo = "";
		Database.getStatique().getAccountData().update(this);
		return false;
	}

	public long getMuteTime() {
		if (!this.isMuted()) {
			return 0L;
		}
		return this.muteTime;
	}

	public String getMuteRaison() {
		if (!this.isMuted()) {
			return "";
		}
		return this.muteRaison;
	}

	public String getMutePseudo() {
		if (!this.isMuted()) {
			return "";
		}
		return this.mutePseudo;
	}

	public Map<Integer, org.aestia.object.Object> getBank() {
		return this.bank;
	}

	public String parseBankObjetsToDB() {
		final StringBuilder str = new StringBuilder();
		if (this.bank.isEmpty()) {
			return "";
		}
		for (final org.aestia.object.Object entry : this.bank.values()) {
			str.append(entry.getGuid()).append("|");
		}
		return str.toString();
	}

	public long getBankKamas() {
		return this.bankKamas;
	}

	public void setBankKamas(final long i) {
		this.bankKamas = i;
		Database.getStatique().getAccountData().updateBank(this);
	}

	public void setGameClient(final GameClient t) {
		this.gameClient = t;
	}

	public GameClient getGameClient() {
		return this.gameClient;
	}

	public Map<Integer, Player> getPersos() {
		final Map<Integer, Player> aPlayers = new HashMap<Integer, Player>();
		for (final Player player : World.getPlayers().values()) {
			if (player == null) {
				continue;
			}
			if (player.getAccount() == null) {
				continue;
			}
			if (player.getAccount().getGuid() != this.getGuid()) {
				continue;
			}
			if (player.getAccount() == null || player.getGameClient() == null) {
				player.setAccount(this);
			}
			aPlayers.put(player.getId(), player);
		}
		return aPlayers;
	}

	public Player getCurPerso() {
		return this.curPerso;
	}

	public boolean isBanned() {
		return this.banned;
	}

	public void setBanned(final boolean banned) {
		this.banned = banned;
	}

	public boolean isOnline() {
		return this.gameClient != null;
	}

	public int getNumberOfPersos() {
		return this.getPersos().size();
	}

	public byte getState() {
		return this.state;
	}

	public void setState(final int state) {
		this.state = (byte) state;
		Database.getStatique().getAccountData().update(this);
	}

	public long getSubscribeRemaining() {
		if (!Main.useSubscribe) {
			return 525600L;
		}
		final long remaining = this.subscriber - System.currentTimeMillis();
		return (remaining <= 0L) ? 0L : remaining;
	}

	public boolean isSubscribe() {
		if (!Main.useSubscribe) {
			return true;
		}
		final long remaining = this.subscriber - System.currentTimeMillis();
		return remaining > 0L;
	}

	public long getSubscribe() {
		final long remaining = this.subscriber - System.currentTimeMillis();
		return (remaining <= 0L) ? 0L : this.subscriber;
	}

	public void setSubscribe(final long subscribe) {
		this.subscriber = subscribe;
	}

	public boolean createPerso(final String name, final int sexe, final int classe, final int color1, final int color2,
			final int color3) {
		final Player perso = Player.CREATE_PERSONNAGE(name, sexe, classe, color1, color2, color3, this);
		return perso != null;
	}

	public void deletePerso(final int guid) {
		if (!this.getPersos().containsKey(guid)) {
			return;
		}
		World.deletePerso(this.getPersos().get(guid));
	}

	public void setCurPerso(final Player perso) {
		this.curPerso = perso;
	}

	public void sendOnline() {
		for (final int i : this.friendGuids) {
			final Player perso = World.getPersonnage(i);
			if (perso != null && perso.is_showFriendConnection() && perso.isOnline()
					&& perso.getAccount().isFriendWith(this.guid)) {
				SocketManager.GAME_SEND_FRIEND_ONLINE(this.curPerso, perso);
			}
		}
	}

	public void addFriend(final int guid) {
		if (this.guid == guid) {
			SocketManager.GAME_SEND_FA_PACKET(this.curPerso, "Ey");
			return;
		}
		final Account a = World.getCompte(guid);
		if (a == null) {
			SocketManager.GAME_SEND_MESSAGE(this.curPerso, "Le compte n'existe pas.");
			return;
		}
		final Player p = a.getCurPerso();
		if (p == null) {
			SocketManager.GAME_SEND_MESSAGE(this.curPerso, "Le joueur n'existe pas.");
			return;
		}
		final Groupes g = p.getGroupe();
		if (g != null && !g.isPlayer()) {
			SocketManager.GAME_SEND_MESSAGE(this.curPerso, "Impossible d'ajouter un membre du staff en ami.");
			return;
		}
		if (!this.friendGuids.contains(guid)) {
			this.friendGuids.add(guid);
			SocketManager.GAME_SEND_FA_PACKET(this.curPerso, "K" + World.getCompte(guid).getPseudo()
					+ World.getCompte(guid).getCurPerso().parseToFriendList(guid));
			Database.getStatique().getAccountData().update(this);
		} else {
			SocketManager.GAME_SEND_FA_PACKET(this.curPerso, "Ea");
		}
	}

	public void removeFriend(final int guid) {
		if (this.friendGuids.contains(guid)) {
			final ArrayList<Integer> friend = new ArrayList<Integer>();
			friend.addAll(this.friendGuids);
			this.friendGuids.clear();
			for (final int i : friend) {
				if (i != guid) {
					this.friendGuids.add(i);
				}
			}
			Database.getStatique().getAccountData().update(this);
		}
		SocketManager.GAME_SEND_FD_PACKET(this.curPerso, "K");
	}

	public boolean isFriendWith(final int guid) {
		return this.friendGuids.contains(guid);
	}

	public String parseFriendListToDB() {
		String str = "";
		for (final int i : this.friendGuids) {
			if (!str.equalsIgnoreCase("")) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + i;
		}
		return str;
	}

	public String parseFriendList() {
		final StringBuilder str = new StringBuilder();
		if (this.friendGuids.isEmpty()) {
			return "";
		}
		for (final int i : this.friendGuids) {
			final Account C = World.getCompte(i);
			if (C == null) {
				continue;
			}
			str.append("|").append(C.getPseudo());
			if (!C.isOnline()) {
				continue;
			}
			final Player P = C.getCurPerso();
			if (P == null) {
				continue;
			}
			str.append(P.parseToFriendList(this.guid));
		}
		return str.toString();
	}

	public void addEnemy(final String packet, final int guid) {
		if (this.guid == guid) {
			SocketManager.GAME_SEND_FA_PACKET(this.curPerso, "Ey");
			return;
		}
		if (!this.enemyGuids.contains(guid)) {
			this.enemyGuids.add(guid);
			final Player Pr = World.getPersoByName(packet);
			SocketManager.GAME_SEND_ADD_ENEMY(this.curPerso, Pr);
			Database.getStatique().getAccountData().update(this);
		} else {
			SocketManager.GAME_SEND_iAEA_PACKET(this.curPerso);
		}
	}

	public void removeEnemy(final int guid) {
		if (this.enemyGuids.contains(guid)) {
			final ArrayList<Integer> enemy = new ArrayList<Integer>();
			enemy.addAll(this.enemyGuids);
			this.enemyGuids.clear();
			for (final int i : enemy) {
				if (i != guid) {
					this.enemyGuids.add(i);
				}
			}
			Database.getStatique().getAccountData().update(this);
		}
		SocketManager.GAME_SEND_iD_COMMANDE(this.curPerso, "K");
	}

	public boolean isEnemyWith(final int guid) {
		return this.enemyGuids.contains(guid);
	}

	public String parseEnemyListToDB() {
		String str = "";
		for (final int i : this.enemyGuids) {
			if (!str.equalsIgnoreCase("")) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + i;
		}
		return str;
	}

	public String parseEnemyList() {
		final StringBuilder str = new StringBuilder();
		if (this.enemyGuids.isEmpty()) {
			return "";
		}
		for (final int i : this.enemyGuids) {
			final Account C = World.getCompte(i);
			if (C == null) {
				continue;
			}
			str.append("|").append(C.getPseudo());
			if (!C.isOnline()) {
				continue;
			}
			final Player P = C.getCurPerso();
			if (P == null) {
				continue;
			}
			str.append(P.parseToEnemyList(this.guid));
		}
		return str.toString();
	}

	public boolean recoverItem(final int ligneID, final int amount) {
		if (this.curPerso == null) {
			return false;
		}
		if (this.curPerso.get_isTradingWith() >= 0) {
			return false;
		}
		final int hdvID = Math.abs(this.curPerso.get_isTradingWith());
		HdvEntry entry = null;
		try {
			final ArrayList<HdvEntry> entrys = this.hdvsItems.get(hdvID);
			if (entrys == null || entrys.isEmpty()) {
				return false;
			}
			for (final HdvEntry tempEntry : entrys) {
				if (tempEntry.getLineId() == ligneID) {
					entry = tempEntry;
					break;
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		if (entry == null) {
			return false;
		}
		this.hdvsItems.get(hdvID).remove(entry);
		final org.aestia.object.Object obj = entry.getObject();
		if (this.curPerso.addObjetSimiler(obj, true, -1)) {
			World.removeItem(obj.getGuid());
		} else {
			this.curPerso.addObjet(obj);
		}
		Database.getGame().getHdvs_itemData().delete(entry.getObject().getGuid());
		World.getHdv(hdvID).delEntry(entry);
		Database.getStatique().getPlayerData().update(this.curPerso, true);
		return true;
	}

	public HdvEntry[] getHdvItems(final int hdvID) {
		if (this.hdvsItems.get(hdvID) == null) {
			return new HdvEntry[1];
		}
		final HdvEntry[] toReturn = new HdvEntry[this.hdvsItems.get(hdvID).size()];
		for (int i = 0; i < this.hdvsItems.get(hdvID).size(); ++i) {
			toReturn[i] = this.hdvsItems.get(hdvID).get(i);
		}
		return toReturn;
	}

	public int countHdvItems(final int hdvID) {
		if (this.hdvsItems.get(hdvID) == null) {
			return 0;
		}
		return this.hdvsItems.get(hdvID).size();
	}

	public void resetAllChars(final boolean save) {
		synchronized (this.getPersos()) {
			for (final Player perso : this.getPersos().values()) {
				if (perso.getCurJobAction() != null) {
					perso.getCurJobAction().cancel(perso.getCurCell(),perso);
					perso.getCurJobAction().broken = true;
				}
				if (perso.get_fight() != null) {
					if (perso.getGroup() != null) {
						perso.getGroup().leave(perso);
					}
					perso.set_Online(true);
				}
				if (perso.getCurExchange() != null) {
					perso.getCurExchange().cancel();
				}
				if (perso.getGroup() != null) {
					perso.getGroup().leave(perso);
				}
				if (perso.getCurCell() != null) {
					perso.getCurCell().removeCharacter(perso.getId());
				}
				if (perso.getCurMap() != null && perso.isOnline()) {
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
				}
				perso.set_Online(false);
			}
		}
	}

	public void disconnect(final Player perso) {
		try {
			if(perso.getCurJobAction() != null)
				perso.getCurJobAction().cancel(perso.getCurCell(), perso);
			
			Database.getStatique().getPlayerData().update(perso, true);
			Database.getStatique().getAccountData().setLogged(this.getGuid(), 0);
			if (perso.getGroup() != null) {
				perso.getGroup().leave(perso);
			}
			if (perso.get_fight() != null && perso.get_fight().playerDisconnect(perso, false)) {
				Database.getStatique().getPlayerData().updateLogged(perso.getId(), 0);
				Database.getStatique().getPlayerData().update(perso, true);
				return;
			}
			if (this.curPerso != null) {
				this.curPerso.timeToSave = -1L;
			}
			this.curPerso = null;
			this.gameClient = null;
			this.curIP = "";
			Database.getStatique().getAccountData().update(this);
			for (final Player character : this.getPersos().values()) {
				Database.getStatique().getPlayerData().update(character, true);
			}
			perso.resetVars();
			this.resetAllChars(true);
			Database.getStatique().getPlayerData().updateAllLogged(this.getGuid(), 0);
			Console.println("Le joueur " + perso.getName() + " vient de se deconnecter.", Console.Color.EXCHANGE);
		} catch (Exception e) {
			Console.println("[ERREUR A REPORTER] - disconnect : " + e.getMessage(), Console.Color.RED);
			e.printStackTrace();
		}
	}

	public long getLastConnectDay() {
		return this.lastConnectDay;
	}

	public void setLastConnectDay(final long lastConnectDay) {
		this.lastConnectDay = lastConnectDay;
		Database.getStatique().getAccountData().setLastConnectDay(this);
	}

	public void updateVote(final String heure, final String ip) {
		if (heure.equalsIgnoreCase("")) {
			this.heureVote = 0L;
		} else {
			this.heureVote = Long.parseLong(heure);
		}
		this.lastVoteIP = ip;
	}
}
