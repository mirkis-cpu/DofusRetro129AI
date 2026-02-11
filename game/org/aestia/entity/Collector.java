// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.common.ConditionParser;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.game.world.World;
import org.aestia.other.Guild;

public class Collector {
	private int id;
	private short map;
	private int cell;
	private byte orientation;
	private int guildId;
	private short N1;
	private short N2;
	private byte inFight;
	private int inFightId;
	private long kamas;
	private long xp;
	private boolean inExchange;
	private Player poseur;
	private long date;
	private long timeTurn;
	private Map<Integer, org.aestia.object.Object> logObjects;
	private Map<Integer, org.aestia.object.Object> objects;
	private Map<Integer, Player> defenserId;

	public Collector(final int id, final short map, final int cell, final byte orientation, final int aGuildID,
			final short N1, final short N2, final Player poseur, final long date, final String items, final long kamas,
			final long xp) {
		this.guildId = 0;
		this.N1 = 0;
		this.N2 = 0;
		this.inFight = 0;
		this.inFightId = -1;
		this.kamas = 0L;
		this.xp = 0L;
		this.inExchange = false;
		this.poseur = null;
		this.timeTurn = 45000L;
		this.logObjects = new TreeMap<Integer, org.aestia.object.Object>();
		this.objects = new TreeMap<Integer, org.aestia.object.Object>();
		this.defenserId = new TreeMap<Integer, Player>();
		this.id = id;
		this.map = map;
		this.cell = cell;
		this.orientation = orientation;
		this.guildId = aGuildID;
		this.N1 = N1;
		this.N2 = N2;
		this.poseur = poseur;
		this.date = date;
		String[] split;
		for (int length = (split = items.split("\\|")).length, i = 0; i < length; ++i) {
			final String item = split[i];
			if (!item.equals("")) {
				final String[] infos = item.split(":");
				final int itemId = Integer.parseInt(infos[0]);
				final org.aestia.object.Object obj = World.getObjet(itemId);
				if (obj != null) {
					this.objects.put(obj.getGuid(), obj);
				}
			}
		}
		this.xp = xp;
		this.kamas = kamas;
	}

	public void reloadTimer() {
		final Long time = World.getGuild(this.getGuildId()).timePutCollector.get(this.getMap());
		if (time != null) {
			return;
		}
		World.getGuild(this.getGuildId()).timePutCollector.put(this.getMap(), this.getDate());
	}

	public long getDate() {
		return this.date;
	}

	public Player getPoseur() {
		return this.poseur;
	}

	public void setPoseur(final Player poseur) {
		this.poseur = poseur;
	}

	public int getId() {
		return this.id;
	}

	public short getMap() {
		return this.map;
	}

	public int getCell() {
		return this.cell;
	}

	public void setCell(final int cell) {
		this.cell = cell;
	}

	public int getGuildId() {
		return this.guildId;
	}

	public int getN1() {
		return this.N1;
	}

	public int getN2() {
		return this.N2;
	}

	public int getInFight() {
		return this.inFight;
	}

	public void setInFight(final byte inFight) {
		this.inFight = inFight;
	}

	public void set_inFightID(final int inFightId) {
		this.inFightId = inFightId;
	}

	public int get_inFightID() {
		return this.inFightId;
	}

	public long getKamas() {
		return this.kamas;
	}

	public void setKamas(final long kamas) {
		this.kamas = kamas;
	}

	public long getXp() {
		return this.xp;
	}

	public void setXp(final long xp) {
		this.xp = xp;
	}

	public boolean getExchange() {
		return this.inExchange;
	}

	public void setExchange(final boolean inExchange) {
		this.inExchange = inExchange;
	}

	public long getTurnTimer() {
		return this.timeTurn;
	}

	public void setTimeTurn(final long timeTurn) {
		this.timeTurn = timeTurn;
	}

	public void removeTimeTurn(final int timeTurn) {
		this.timeTurn -= timeTurn;
	}

	public void addLogObjects(final int id, final org.aestia.object.Object obj) {
		this.logObjects.put(id, obj);
	}

	public String getLogObjects() {
		if (this.logObjects.isEmpty()) {
			return "";
		}
		final StringBuilder str = new StringBuilder();
		for (final org.aestia.object.Object obj : this.logObjects.values()) {
			str.append(";").append(obj.getTemplate().getId()).append(",").append(obj.getQuantity());
		}
		return str.toString();
	}

	public Map<Integer, org.aestia.object.Object> getOjects() {
		return this.objects;
	}

	public boolean haveObjects(final int id) {
		return this.objects.get(id) != null;
	}

	public int getPodsTotal() {
		int pod = 0;
		for (final org.aestia.object.Object object : this.objects.values()) {
			if (object != null) {
				pod += object.getTemplate().getPod() * object.getQuantity();
			}
		}
		return pod;
	}

	public int getMaxPod() {
		return World.getGuild(this.getGuildId()).getStats(158);
	}

	public boolean addObjet(final org.aestia.object.Object newObj) {
		for (final Map.Entry<Integer, org.aestia.object.Object> entry : this.objects.entrySet()) {
			final org.aestia.object.Object obj = entry.getValue();
			if (ConditionParser.stackIfSimilar(obj, newObj, true)) {
				obj.setQuantity(obj.getQuantity() + newObj.getQuantity());
				Database.getStatique().getItemData().save(obj, false);
				return false;
			}
		}
		this.objects.put(newObj.getGuid(), newObj);
		return true;
	}

	public void removeObjet(final int id) {
		this.objects.remove(id);
	}

	public static String parseGM(final org.aestia.map.Map map) {
		final StringBuilder sock = new StringBuilder();
		sock.append("GM|");
		boolean isFirst = true;
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			final Collector c = Collector.getValue();
			if (c == null) {
				continue;
			}
			if (c.inFight > 0) {
				continue;
			}
			if (c.map != map.getId()) {
				continue;
			}
			final Guild G = World.getGuild(c.guildId);
			if (G == null) {
				c.reloadTimer();
				Database.getGame().getPercepteurData().delete(c.getId());
				World.getCollectors().remove(c.getId());
			} else {
				if (!isFirst) {
					sock.append("|");
				}
				sock.append("+");
				sock.append(c.cell).append(";");
				sock.append(c.orientation).append(";");
				sock.append("0").append(";");
				sock.append(c.id).append(";");
				sock.append(Integer.toString(c.N1, 36)).append(",").append(Integer.toString(c.N2, 36)).append(";");
				sock.append("-6").append(";");
				sock.append("6000^100;");
				sock.append(G.getLvl()).append(";");
				sock.append(G.getName()).append(";" + G.getEmblem());
				isFirst = false;
			}
		}
		return sock.toString();
	}

	public static String parseToGuild(final int GuildID) {
		StringBuilder packet = new StringBuilder();
		boolean isFirst = true;
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getGuildId() == GuildID) {
				final org.aestia.map.Map map = World.getMap(Collector.getValue().getMap());
				if (isFirst) {
					packet.append("+");
				}
				if (!isFirst) {
					packet.append("|");
				}
				final Collector perco = Collector.getValue();
				final int inFight = Collector.getValue().getInFight();
				String name = "";
				if (Collector.getValue().getPoseur() != null) {
					name = Collector.getValue().getPoseur().getName();
				}
				packet.append(perco.getId());
				packet.append(";");
				packet.append(Integer.toString(perco.N1, 36));
				packet.append(",");
				packet.append(Integer.toString(perco.N2, 36));
				packet.append(",");
				packet.append(name);
				packet.append(",");
				packet.append(Long.toString(perco.date));
				packet.append(",");
				packet.append("");
				packet.append(",");
				packet.append("-1");
				packet.append(",");
				packet.append(Long.toString(perco.date + World.getGuild(GuildID).getLvl() * 600000));
				packet.append(";");
				packet.append(Integer.toString(map.getId(), 36));
				packet.append(",");
				packet.append(map.getX());
				packet.append(",");
				packet.append(map.getY());
				packet.append(";");
				packet.append(inFight);
				packet.append(";");
				if (inFight == 1) {
					if (map.getFight(Collector.getValue().get_inFightID()) == null) {
						packet.append("45000");
						packet.append(";");
					} else {
						packet.append(Collector.getValue().getTurnTimer());
						packet.append(";");
					}
					packet.append("45000");
					packet.append(";");
					int numcase = World.getMap(Collector.getValue().getMap()).getMaxTeam1() - 1;
					if (numcase > 7) {
						numcase = 7;
					}
					packet.append(numcase);
					packet.append(";");
				} else {
					packet.append("0;");
					packet.append("45000;");
					packet.append("7;");
				}
				isFirst = false;
			}
		}
		if (packet.length() == 0) {
			packet = new StringBuilder("null");
		}
		return packet.toString();
	}

	public void delCollector(final int id) {
		for (final org.aestia.object.Object obj : this.objects.values()) {
			World.removeItem(obj.getGuid());
		}
		World.getCollectors().remove(id);
	}

	public static int getCollectorByGuildId(final int id) {
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getMap() == id) {
				return Collector.getValue().getGuildId();
			}
		}
		return 0;
	}

	public static Collector getCollectorByMapId(final short id) {
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getMap() == id) {
				return World.getCollectors().get(Collector.getValue().getId());
			}
		}
		return null;
	}

	public static int countCollectorGuild(final int GuildID) {
		int i = 0;
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getGuildId() == GuildID) {
				++i;
			}
		}
		return i;
	}

	public static void parseAttaque(final Player perso, final int guildID) {
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getInFight() > 0 && Collector.getValue().getGuildId() == guildID) {
				SocketManager.GAME_SEND_gITp_PACKET(perso, parseAttaqueToGuild(Collector.getValue().getId(),
						Collector.getValue().getMap(), Collector.getValue().get_inFightID()));
			}
		}
	}

	public static void parseDefense(final Player perso, final int guildID) {
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getInFight() > 0 && Collector.getValue().getGuildId() == guildID) {
				SocketManager.GAME_SEND_gITP_PACKET(perso, parseDefenseToGuild(Collector.getValue()));
			}
		}
	}

	public static String parseAttaqueToGuild(final int id, final short map, final int fightid) {
		final StringBuilder str = new StringBuilder();
		str.append("+").append(id);
		for (final Map.Entry<Integer, Fight> F : World.getMap(map).getFights().entrySet()) {
			if (F.getValue().getId() == fightid) {
				for (final Fighter f : F.getValue().getFighters(1)) {
					if (f.getPersonnage() == null) {
						continue;
					}
					str.append("|");
					str.append(Integer.toString(f.getPersonnage().getId(), 36)).append(";");
					str.append(f.getPersonnage().getName()).append(";");
					str.append(f.getPersonnage().getLevel()).append(";");
					str.append("0;");
				}
			}
		}
		return str.toString();
	}

	public static String parseDefenseToGuild(final Collector collector) {
		final StringBuilder str = new StringBuilder();
		str.append("+").append(collector.getId());
		for (final Player player : collector.getDefenseFight().values()) {
			if (player == null) {
				continue;
			}
			str.append("|");
			str.append(Integer.toString(player.getId(), 36)).append(";");
			str.append(player.getName()).append(";");
			str.append(player.get_gfxID()).append(";");
			str.append(player.getLevel()).append(";");
			str.append(Integer.toString(player.getColor1(), 36)).append(";");
			str.append(Integer.toString(player.getColor2(), 36)).append(";");
			str.append(Integer.toString(player.getColor3(), 36)).append(";");
		}
		return str.toString();
	}

	public String getItemCollectorList() {
		final StringBuilder items = new StringBuilder();
		if (!this.objects.isEmpty()) {
			for (final org.aestia.object.Object obj : this.objects.values()) {
				items.append("O").append(obj.parseItem()).append(";");
			}
		}
		if (this.kamas != 0L) {
			items.append("G").append(this.kamas);
		}
		return items.toString();
	}

	public String parseItemCollector() {
		String items = "";
		for (final org.aestia.object.Object obj : this.objects.values()) {
			items = String.valueOf(items) + obj.getGuid() + "|";
		}
		return items;
	}

	public void removeFromCollector(final Player P, final int id, final int qua) {
		if (qua <= 0) {
			return;
		}
		final org.aestia.object.Object CollectorObj = World.getObjet(id);
		org.aestia.object.Object PersoObj = P.getSimilarItem(CollectorObj);
		final int newQua = CollectorObj.getQuantity() - qua;
		if (PersoObj == null) {
			if (newQua <= 0) {
				this.removeObjet(id);
				P.addObjet(CollectorObj);
				final String str = "O-" + id;
				SocketManager.GAME_SEND_EsK_PACKET(P, str);
			} else {
				PersoObj = org.aestia.object.Object.getCloneObjet(CollectorObj, qua);
				World.addObjet(PersoObj, true);
				CollectorObj.setQuantity(newQua);
				P.addObjet(PersoObj);
				final String str = "O+" + CollectorObj.getGuid() + "|" + CollectorObj.getQuantity() + "|"
						+ CollectorObj.getTemplate().getId() + "|" + CollectorObj.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(P, str);
			}
		} else if (newQua <= 0) {
			this.removeObjet(id);
			World.removeItem(CollectorObj.getGuid());
			PersoObj.setQuantity(PersoObj.getQuantity() + CollectorObj.getQuantity());
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			final String str = "O-" + id;
			SocketManager.GAME_SEND_EsK_PACKET(P, str);
		} else {
			CollectorObj.setQuantity(newQua);
			PersoObj.setQuantity(PersoObj.getQuantity() + qua);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			final String str = "O+" + CollectorObj.getGuid() + "|" + CollectorObj.getQuantity() + "|"
					+ CollectorObj.getTemplate().getId() + "|" + CollectorObj.parseStatsString();
			SocketManager.GAME_SEND_EsK_PACKET(P, str);
		}
		SocketManager.GAME_SEND_Ow_PACKET(P);
		Database.getStatique().getPlayerData().update(P, true);
	}

	public static void removeCollector(final int GuildID) {
		for (final Map.Entry<Integer, Collector> Collector : World.getCollectors().entrySet()) {
			if (Collector.getValue().getGuildId() == GuildID) {
				World.getCollectors().remove(Collector.getKey());
				for (final Player p : World.getMap(Collector.getValue().getMap()).getPersos()) {
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(p.getCurMap(), Collector.getValue().getId());
				}
				Collector.getValue().reloadTimer();
				Database.getGame().getPercepteurData().delete(Collector.getKey());
			}
		}
	}

	public boolean addDefenseFight(final Player P) {
		if (this.defenserId.size() >= World.getMap(this.getMap()).getMaxTeam1()) {
			return false;
		}
		this.defenserId.put(P.getId(), P);
		return true;
	}

	public void delDefenseFight(final Player P) {
		if (this.defenserId.containsKey(P.getId())) {
			this.defenserId.remove(P.getId());
		}
	}

	public void clearDefenseFight() {
		this.defenserId.clear();
	}

	public Map<Integer, Player> getDefenseFight() {
		return this.defenserId;
	}

	public Collection<org.aestia.object.Object> getDrops() {
		return this.objects.values();
	}
}
