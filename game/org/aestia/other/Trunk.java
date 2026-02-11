// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Constant;

public class Trunk {
	private int id;
	private int houseId;
	private short mapId;
	private int cellId;
	private String key;
	private int ownerId;
	private long kamas;
	private Player player;
	private Map<Integer, org.aestia.object.Object> object;

	public Trunk(final int id, final int houseId, final short mapId, final int cellId) {
		this.player = null;
		this.object = new TreeMap<Integer, org.aestia.object.Object>();
		this.id = id;
		this.houseId = houseId;
		this.mapId = mapId;
		this.cellId = cellId;
	}

	public void setObjects(final String object) {
		String[] split;
		for (int length = (split = object.split("\\|")).length, i = 0; i < length; ++i) {
			final String item = split[i];
			if (!item.equals("")) {
				final String[] infos = item.split(":");
				final int guid = Integer.parseInt(infos[0]);
				final org.aestia.object.Object obj = World.getObjet(guid);
				if (obj != null) {
					synchronized (this.object) {
						this.object.put(obj.getGuid(), obj);
					}
					// monitorexit(this.object)
				}
			}
		}
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getHouseId() {
		return this.houseId;
	}

	public void setHouseId(final int houseId) {
		this.houseId = houseId;
	}

	public short getMapId() {
		return this.mapId;
	}

	public void setMapId(final short mapId) {
		this.mapId = mapId;
	}

	public int getCellId() {
		return this.cellId;
	}

	public void setCellId(final int cellId) {
		this.cellId = cellId;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(final int ownerId) {
		this.ownerId = ownerId;
	}

	public long getKamas() {
		return this.kamas;
	}

	public void setKamas(final long kamas) {
		this.kamas = kamas;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public Map<Integer, org.aestia.object.Object> getObject() {
		return this.object;
	}

	public void setObject(final Map<Integer, org.aestia.object.Object> object) {
		synchronized (this.object) {
		}
		// monitorexit(this.object = object)
	}

	public void Lock(final Player P) {
		SocketManager.GAME_SEND_KODE(P, "CK1|8");
	}

	public static void closeCode(final Player P) {
		SocketManager.GAME_SEND_KODE(P, "V");
	}

	public static Trunk getTrunkIdByCoord(final int map_id, final int cell_id) {
		for (final Map.Entry<Integer, Trunk> trunk : World.getTrunks().entrySet()) {
			if (trunk.getValue().getMapId() == map_id && trunk.getValue().getCellId() == cell_id) {
				return trunk.getValue();
			}
		}
		return null;
	}

	public static void lock(final Player P, final String packet) {
		final Trunk t = P.getInTrunk();
		if (t == null) {
			return;
		}
		if (t.isTrunk(P, t)) {
			Database.getGame().getCoffreData().updateCode(P, t, packet);
			t.setKey(packet);
			closeCode(P);
		} else {
			closeCode(P);
		}
		P.setInTrunk(null);
	}

	public void enter(final Player P) {
		if (P.get_fight() != null || P.get_isTalkingWith() != 0 || P.get_isTradingWith() != 0
				|| P.getCurJobAction() != null || P.getCurExchange() != null) {
			return;
		}
		final Trunk t = P.getInTrunk();
		final House h = World.getHouse(this.getHouseId());
		if (t == null) {
			return;
		}
		Label_0099: {
			if (t.getOwnerId() != P.getAccID()) {
				if (P.get_guild() != null) {
					if (P.get_guild().getId() == h.getGuildId() && h.canDo(Constant.C_GNOCODE)) {
						break Label_0099;
					}
				}
				if (P.get_guild() == null && h.canDo(Constant.C_OCANTOPEN)) {
					SocketManager.GAME_SEND_MESSAGE(P,
							"Ce coffre ne peut \u00eatre ouvert que par les membres de la guilde !");
					return;
				}
				if (t.getOwnerId() > 0) {
					SocketManager.GAME_SEND_KODE(P, "CK0|8");
					return;
				}
				if (t.getOwnerId() == 0) {
					return;
				}
				return;
			}
		}
		open(P, "-", true);
	}

	public static void open(final Player P, final String packet, final boolean isTrunk) {
		final Trunk t = P.getInTrunk();
		if (t == null) {
			return;
		}
		if (packet.compareTo(t.getKey()) == 0 || isTrunk) {
			t.player = P;
			SocketManager.GAME_SEND_ECK_PACKET(P.getGameClient(), 5, "");
			SocketManager.GAME_SEND_EL_TRUNK_PACKET(P, t);
			closeCode(P);
		} else if (packet.compareTo(t.getKey()) != 0) {
			SocketManager.GAME_SEND_KODE(P, "KE");
			closeCode(P);
			P.setInTrunk(null);
		}
	}

	public boolean isTrunk(final Player P, final Trunk t) {
		return t.getOwnerId() == P.getAccID();
	}

	public static ArrayList<Trunk> getTrunksByHouse(final House h) {
		final ArrayList<Trunk> trunks = new ArrayList<Trunk>();
		for (final Map.Entry<Integer, Trunk> trunk : World.getTrunks().entrySet()) {
			if (trunk.getValue().getHouseId() == h.getId()) {
				trunks.add(trunk.getValue());
			}
		}
		return trunks;
	}

	public String parseToTrunkPacket() {
		final StringBuilder packet = new StringBuilder();
		synchronized (this.object) {
			for (final org.aestia.object.Object obj : this.object.values()) {
				packet.append("O").append(obj.parseItem()).append(";");
			}
		}
		// monitorexit(this.object)
		if (this.getKamas() != 0L) {
			packet.append("G").append(this.getKamas());
		}
		return packet.toString();
	}

	public void addInTrunk(final int guid, final int qua, final Player P) {
		if (qua <= 0) {
			return;
		}
		if (P.getInTrunk().getId() != this.getId()) {
			return;
		}
		synchronized (this.object) {
			if (this.object.size() >= 10000) {
				SocketManager.GAME_SEND_MESSAGE(P,
						"Le nombre d'objets maximal de ce coffre \u00e0 \u00e9t\u00e9 atteint !");
				// monitorexit(this.object)
				return;
			}
		}
		// monitorexit(this.object)
		final org.aestia.object.Object PersoObj = World.getObjet(guid);
		if (PersoObj == null) {
			return;
		}
		synchronized (P.getItems()) {
			if (P.getItems().get(guid) == null) {
				// monitorexit(P.getItems())
				return;
			}
		}
		// monitorexit(P.getItems())
		String str = "";
		if (PersoObj.getPosition() != -1) {
			return;
		}
		org.aestia.object.Object TrunkObj = this.getSimilarTrunkItem(PersoObj);
		final int newQua = PersoObj.getQuantity() - qua;
		if (TrunkObj == null) {
			if (newQua <= 0) {
				P.removeItem(PersoObj.getGuid());
				synchronized (this.object) {
					this.object.put(PersoObj.getGuid(), PersoObj);
				}
				// monitorexit(this.object)
				str = "O+" + PersoObj.getGuid() + "|" + PersoObj.getQuantity() + "|" + PersoObj.getTemplate().getId()
						+ "|" + PersoObj.parseStatsString();
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(P, guid);
			} else {
				PersoObj.setQuantity(newQua);
				TrunkObj = org.aestia.object.Object.getCloneObjet(PersoObj, qua);
				World.addObjet(TrunkObj, true);
				synchronized (this.object) {
					this.object.put(TrunkObj.getGuid(), TrunkObj);
				}
				// monitorexit(this.object)
				str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId()
						+ "|" + TrunkObj.parseStatsString();
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
				Database.getStatique().getItemData().save(PersoObj, false);
			}
		} else if (newQua <= 0) {
			P.removeItem(PersoObj.getGuid());
			World.removeItem(PersoObj.getGuid());
			TrunkObj.setQuantity(TrunkObj.getQuantity() + PersoObj.getQuantity());
			str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId() + "|"
					+ TrunkObj.parseStatsString();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(P, guid);
		} else {
			PersoObj.setQuantity(newQua);
			TrunkObj.setQuantity(TrunkObj.getQuantity() + qua);
			str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId() + "|"
					+ TrunkObj.parseStatsString();
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			Database.getStatique().getItemData().save(PersoObj, false);
		}
		if (TrunkObj != null) {
			Database.getStatique().getItemData().save(TrunkObj, false);
		}
		for (final Player perso : P.getCurMap().getPersos()) {
			if (perso.getInTrunk() != null && this.getId() == perso.getInTrunk().getId()) {
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			}
		}
		SocketManager.GAME_SEND_Ow_PACKET(P);
		Database.getGame().getCoffreData().update(this);
		Database.getStatique().getPlayerData().update(P, true);
	}

	public void removeFromTrunk(final int guid, final int qua, final Player P) {
		if (qua <= 0) {
			return;
		}
		if (P.getInTrunk().getId() != this.getId()) {
			return;
		}
		final org.aestia.object.Object TrunkObj = World.getObjet(guid);
		if (TrunkObj == null) {
			return;
		}
		synchronized (this.object) {
			if (this.object.get(guid) == null) {
				GameServer.addToLog(
						"Le joueur " + P.getName() + " a tenter de retirer un objet dans un coffre qu'il n'avait pas.");
				// monitorexit(this.object)
				return;
			}
		}
		// monitorexit(this.object)
		org.aestia.object.Object PersoObj = P.getSimilarItem(TrunkObj);
		String str = "";
		final int newQua = TrunkObj.getQuantity() - qua;
		if (PersoObj == null) {
			if (newQua <= 0) {
				synchronized (this.object) {
					this.object.remove(guid);
				}
				// monitorexit(this.object)
				synchronized (P.getItems()) {
					P.getItems().put(guid, TrunkObj);
				}
				// monitorexit(P.getItems())
				SocketManager.GAME_SEND_OAKO_PACKET(P, TrunkObj);
				str = "O-" + guid;
			} else {
				PersoObj = org.aestia.object.Object.getCloneObjet(TrunkObj, qua);
				World.addObjet(PersoObj, true);
				TrunkObj.setQuantity(newQua);
				synchronized (P.getItems()) {
					P.getItems().put(PersoObj.getGuid(), PersoObj);
				}
				// monitorexit(P.getItems())
				SocketManager.GAME_SEND_OAKO_PACKET(P, PersoObj);
				str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId()
						+ "|" + TrunkObj.parseStatsString();
				Database.getStatique().getItemData().save(TrunkObj, false);
			}
		} else if (newQua <= 0) {
			synchronized (this.object) {
				this.object.remove(TrunkObj.getGuid());
			}
			// monitorexit(this.object)
			World.removeItem(TrunkObj.getGuid());
			PersoObj.setQuantity(PersoObj.getQuantity() + TrunkObj.getQuantity());
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			str = "O-" + guid;
		} else {
			TrunkObj.setQuantity(newQua);
			PersoObj.setQuantity(PersoObj.getQuantity() + qua);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId() + "|"
					+ TrunkObj.parseStatsString();
			Database.getStatique().getItemData().save(TrunkObj, false);
		}
		if (PersoObj != null) {
			Database.getStatique().getItemData().save(PersoObj, false);
		}
		for (final Player perso : P.getCurMap().getPersos()) {
			if (perso.getInTrunk() != null && this.getId() == perso.getInTrunk().getId()) {
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			}
		}
		SocketManager.GAME_SEND_Ow_PACKET(P);
		Database.getGame().getCoffreData().update(this);
		Database.getStatique().getPlayerData().update(P, true);
	}

	private org.aestia.object.Object getSimilarTrunkItem(final org.aestia.object.Object obj) {
		synchronized (this.object) {
			for (final org.aestia.object.Object value : this.object.values()) {
				if (value.getTemplate().getId() == obj.getTemplate().getId() && value.getTemplate().getId() != 8378
						&& value.getStats().isSameStats(obj.getStats()) && obj.getTemplate().getType() != 77
						&& !Constant.isIncarnationWeapon(obj.getTemplate().getId()) && obj.getTemplate().getType() != 85
						&& obj.getTemplate().getType() != 93 && obj.getTemplate().getType() != 97
						&& obj.getTemplate().getType() != 113
						&& (obj.getTemplate().getType() != 24 || Constant.isFlacGelee(value.getTemplate().getId()))
						&& value.getPosition() == -1 && obj.getTemplate().getType() != 18) {
					// monitorexit(this.object)
					return value;
				}
			}
		}
		// monitorexit(this.object)
		return null;
	}

	public String parseTrunkObjetsToDB() {
		final StringBuilder str = new StringBuilder();
		synchronized (this.object) {
			for (final Map.Entry<Integer, org.aestia.object.Object> entry : this.object.entrySet()) {
				final org.aestia.object.Object obj = entry.getValue();
				str.append(obj.getGuid()).append("|");
			}
		}
		// monitorexit(this.object)
		return str.toString();
	}

	public void moveTrunkToBank(final Account Cbank) {
		synchronized (this.object) {
			for (final Map.Entry<Integer, org.aestia.object.Object> obj : this.object.entrySet()) {
				Cbank.getBank().put(obj.getKey(), obj.getValue());
			}
			this.object.clear();
		}
		// monitorexit(this.object)
	}
}
