// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.world.World;
import org.aestia.kernel.Constant;

public class House {
	private int id;
	private short mapId;
	private int cellId;
	private int ownerId;
	private int sale;
	private int guildId;
	private int guildRights;
	private int access;
	private String key;
	private int houseMapId;
	private int houseCellId;
	private Map<Integer, Boolean> haveRight;

	public House(final int id, final short mapId, final int cellId, final int HouseMapId, final int HouseCellId) {
		this.haveRight = new TreeMap<Integer, Boolean>();
		this.id = id;
		this.mapId = mapId;
		this.cellId = cellId;
		this.houseMapId = HouseMapId;
		this.houseCellId = HouseCellId;
	}

	public void setGuildRightsWithParse(final int guildRights) {
		this.parseIntToRight(this.guildRights = guildRights);
	}

	public int getId() {
		return this.id;
	}

	public short getMapId() {
		return this.mapId;
	}

	public int getCellId() {
		return this.cellId;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(final int id) {
		this.ownerId = id;
	}

	public int getSale() {
		return this.sale;
	}

	public void setSale(final int price) {
		this.sale = price;
	}

	public int getGuildId() {
		return this.guildId;
	}

	public void setGuildId(final int guildId) {
		this.guildId = guildId;
	}

	public int getGuildRights() {
		return this.guildRights;
	}

	public void setGuildRights(final int guildRights) {
		this.guildRights = guildRights;
	}

	public int getAccess() {
		return this.access;
	}

	public void setAccess(final int access) {
		this.access = access;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public int getHouseMapId() {
		return this.houseMapId;
	}

	public int getHouseCellId() {
		return this.houseCellId;
	}

	public static House getHouseIdByCoord(final int map_id, final int cell_id) {
		for (final Map.Entry<Integer, House> house : World.getHouses().entrySet()) {
			if (house.getValue().getMapId() == map_id && house.getValue().getCellId() == cell_id) {
				return house.getValue();
			}
		}
		return null;
	}

	public static void load(final Player P, final int newMapID) {
		for (final Map.Entry<Integer, House> house : World.getHouses().entrySet()) {
			if (house.getValue().getMapId() == newMapID) {
				final StringBuilder packet = new StringBuilder();
				packet.append("P").append(house.getValue().getId()).append("|");
				if (house.getValue().getOwnerId() > 0) {
					final Account C = World.getCompte(house.getValue().getOwnerId());
					if (C == null) {
						packet.append("undefined;");
					} else {
						packet.append(World.getCompte(house.getValue().getOwnerId()).getPseudo()).append(";");
					}
				} else {
					packet.append(";");
				}
				if (house.getValue().getSale() > 0) {
					packet.append("1");
				} else {
					packet.append("0");
				}
				if (house.getValue().getGuildId() > 0) {
					final Guild G = World.getGuild(house.getValue().getGuildId());
					if (G != null) {
						final String Gname = G.getName();
						final String Gemblem = G.getEmblem();
						if (G.getMembers().size() < 10 && G.getId() > 2) {
							Database.getGame().getHouseData().updateGuild(house.getValue(), 0, 0);
						} else if (P.get_guild() != null && P.get_guild().getId() == house.getValue().getGuildId()
								&& house.getValue().canDo(Constant.H_GBLASON)) {
							packet.append(";").append(Gname).append(";").append(Gemblem);
						} else if (house.getValue().canDo(Constant.H_OBLASON)) {
							packet.append(";").append(Gname).append(";").append(Gemblem);
						}
					}
				}
				SocketManager.GAME_SEND_hOUSE(P, packet.toString());
				if (house.getValue().getOwnerId() != P.getAccID()) {
					continue;
				}
				final StringBuilder packet2 = new StringBuilder();
				packet2.append("L+|").append(house.getValue().getId()).append(";").append(house.getValue().getAccess())
						.append(";");
				if (house.getValue().getSale() <= 0) {
					packet2.append("0;").append(house.getValue().getSale());
				} else if (house.getValue().getSale() > 0) {
					packet2.append("1;").append(house.getValue().getSale());
				}
				SocketManager.GAME_SEND_hOUSE(P, packet2.toString());
			}
		}
	}

	public void enter(final Player P) {
		if (P.get_fight() != null || P.get_isTalkingWith() != 0 || P.get_isTradingWith() != 0
				|| P.getCurJobAction() != null || P.getCurExchange() != null) {
			return;
		}
		final House h = P.getInHouse();
		if (h == null) {
			return;
		}
		if (h.getOwnerId() == P.getAccID() || (P.get_guild() != null && P.get_guild().getId() == h.getGuildId()
				&& this.canDo(Constant.H_GNOCODE))) {
			open(P, "-", true);
		} else if (h.getOwnerId() > 0) {
			SocketManager.GAME_SEND_KODE(P, "CK0|8");
		} else {
			if (h.getOwnerId() != 0) {
				return;
			}
			open(P, "-", false);
		}
	}

	public static void open(final Player P, final String packet, final boolean isHome) {
		if (P.isOnMount()) {
			SocketManager.GAME_SEND_Im_PACKET(P, "1118");
			return;
		}
		final House h = P.getInHouse();
		if ((!h.canDo(Constant.H_OCANTOPEN) && packet.compareTo(h.getKey()) == 0) || isHome) {
			P.teleport((short) h.getHouseMapId(), h.getHouseCellId());
			closeCode(P);
		} else if (packet.compareTo(h.getKey()) != 0 || h.canDo(Constant.H_OCANTOPEN)) {
			SocketManager.GAME_SEND_KODE(P, "KE");
			SocketManager.GAME_SEND_KODE(P, "V");
		}
	}

	public void buyIt(final Player P) {
		final House h = P.getInHouse();
		final String str = "CK" + h.getId() + "|" + h.getSale();
		SocketManager.GAME_SEND_hOUSE(P, str);
	}

	public static void buy(final Player P) {
		final House h = P.getInHouse();
		if (alreadyHaveHouse(P)) {
			SocketManager.GAME_SEND_Im_PACKET(P, "132;1");
			return;
		}
		if (P.get_kamas() < h.getSale()) {
			return;
		}
		final long newkamas = P.get_kamas() - h.getSale();
		P.set_kamas(newkamas);
		int tKamas = 0;
		for (final Trunk t : Trunk.getTrunksByHouse(h)) {
			if (h.getOwnerId() > 0) {
				t.moveTrunkToBank(World.getCompte(h.getOwnerId()));
			}
			tKamas += (int) t.getKamas();
			t.setKamas(0L);
			t.setKey("-");
			t.setOwnerId(0);
			Database.getGame().getCoffreData().update(t);
		}
		if (h.getOwnerId() > 0) {
			final Account Seller = World.getCompte(h.getOwnerId());
			final long newbankkamas = Seller.getBankKamas() + h.getSale() + tKamas;
			Seller.setBankKamas(newbankkamas);
			if (Seller.getCurPerso() != null) {
				SocketManager.GAME_SEND_MESSAGE(Seller.getCurPerso(),
						"Une maison vous appartenant \u00e0 \u00e9t\u00e9 vendue " + h.getSale() + " kamas.");
				Database.getStatique().getPlayerData().update(Seller.getCurPerso(), true);
			}
			Database.getStatique().getAccountData().update(Seller);
		}
		Database.getStatique().getPlayerData().update(P, true);
		SocketManager.GAME_SEND_STATS_PACKET(P);
		closeBuy(P);
		Database.getGame().getHouseData().buy(P, h);
		for (final Player z : P.getCurMap().getPersos()) {
			load(z, z.getCurMap().getId());
		}
	}

	public void sellIt(final Player P) {
		final House h = P.getInHouse();
		if (this.isHouse(P, h)) {
			final String str = "CK" + h.getId() + "|" + h.getSale();
			SocketManager.GAME_SEND_hOUSE(P, str);
		}
	}

	public static void sell(final Player P, final String packet) {
		final House h = P.getInHouse();
		final int price = Integer.parseInt(packet);
		if (h.isHouse(P, h)) {
			SocketManager.GAME_SEND_hOUSE(P, "V");
			SocketManager.GAME_SEND_hOUSE(P, "SK" + h.getId() + "|" + price);
			Database.getGame().getHouseData().sell(h, price);
			for (final Player z : P.getCurMap().getPersos()) {
				load(z, z.getCurMap().getId());
			}
		}
	}

	public boolean isHouse(final Player P, final House h) {
		return h.getOwnerId() == P.getAccID();
	}

	public static void closeCode(final Player P) {
		SocketManager.GAME_SEND_KODE(P, "V");
	}

	public static void closeBuy(final Player P) {
		SocketManager.GAME_SEND_hOUSE(P, "V");
	}

	public void lock(final Player P) {
		SocketManager.GAME_SEND_KODE(P, "CK1|8");
	}

	public static void lockIt(final Player P, final String packet) {
		final House h = P.getInHouse();
		if (h.isHouse(P, h)) {
			Database.getGame().getHouseData().updateCode(P, h, packet);
			closeCode(P);
			return;
		}
		closeCode(P);
	}

	public static String parseHouseToGuild(final Player P) {
		boolean isFirst = true;
		String packet = "+";
		for (final Map.Entry<Integer, House> house : World.getHouses().entrySet()) {
			if (house.getValue().getGuildId() == P.get_guild().getId() && house.getValue().getGuildRights() > 0) {
				String name = "";
				final int id = house.getValue().getOwnerId();
				if (id != -1) {
					final Account a = World.getAccounts().get(id);
					if (a != null) {
						name = a.getPseudo();
					}
				}
				if (isFirst) {
					packet = String.valueOf(packet) + house.getKey() + ";";
					if (World.getPersonnage(house.getValue().getOwnerId()) == null) {
						packet = String.valueOf(packet) + name + ";";
					} else {
						packet = String.valueOf(packet)
								+ World.getPersonnage(house.getValue().getOwnerId()).getAccount().getPseudo() + ";";
					}
					packet = String.valueOf(packet) + World.getMap((short) house.getValue().getHouseMapId()).getX()
							+ "," + World.getMap((short) house.getValue().getHouseMapId()).getY() + ";";
					packet = String.valueOf(packet) + "0;";
					packet = String.valueOf(packet) + house.getValue().getGuildRights();
					isFirst = false;
				} else {
					packet = String.valueOf(packet) + "|";
					packet = String.valueOf(packet) + house.getKey() + ";";
					if (World.getPersonnage(house.getValue().getOwnerId()) == null) {
						packet = String.valueOf(packet) + name + ";";
					} else {
						packet = String.valueOf(packet)
								+ World.getPersonnage(house.getValue().getOwnerId()).getAccount().getPseudo() + ";";
					}
					packet = String.valueOf(packet) + World.getMap((short) house.getValue().getHouseMapId()).getX()
							+ "," + World.getMap((short) house.getValue().getHouseMapId()).getY() + ";";
					packet = String.valueOf(packet) + "0;";
					packet = String.valueOf(packet) + house.getValue().getGuildRights();
				}
			}
		}
		return packet;
	}

	public static boolean alreadyHaveHouse(final Player P) {
		for (final Map.Entry<Integer, House> house : World.getHouses().entrySet()) {
			if (house.getValue().getOwnerId() == P.getAccID()) {
				return true;
			}
		}
		return false;
	}

	public static void parseHG(final Player P, final String packet) {
		final House h = P.getInHouse();
		if (P.get_guild() == null) {
			return;
		}
		if (packet != null) {
			if (packet.charAt(0) == '+') {
				final byte HouseMaxOnGuild = (byte) Math.floor(P.get_guild().getLvl() / 10);
				if (houseOnGuild(P.get_guild().getId()) >= HouseMaxOnGuild && P.get_guild().getId() > 2) {
					P.send("Im1151");
					return;
				}
				if (P.get_guild().getMembers().size() < 10 && P.get_guild().getId() > 2) {
					return;
				}
				Database.getGame().getHouseData().updateGuild(h, P.get_guild().getId(), 0);
				parseHG(P, null);
			} else if (packet.charAt(0) == '-') {
				Database.getGame().getHouseData().updateGuild(h, 0, 0);
				parseHG(P, null);
			} else {
				Database.getGame().getHouseData().updateGuild(h, h.getGuildId(), Integer.parseInt(packet));
				h.parseIntToRight(Integer.parseInt(packet));
			}
		} else if (packet == null) {
			if (h.getGuildId() <= 0) {
				SocketManager.GAME_SEND_hOUSE(P, "G" + h.getId());
			} else if (h.getGuildId() > 0) {
				SocketManager.GAME_SEND_hOUSE(P, "G" + h.getId() + ";" + P.get_guild().getName() + ";"
						+ P.get_guild().getEmblem() + ";" + h.getGuildRights());
			}
		}
	}

	public static byte houseOnGuild(final int GuildID) {
		byte i = 0;
		for (final Map.Entry<Integer, House> house : World.getHouses().entrySet()) {
			if (house.getValue().getGuildId() == GuildID) {
				++i;
			}
		}
		return i;
	}

	public boolean canDo(final int rightValue) {
		return this.haveRight.get(rightValue);
	}

	public void initRight() {
		this.haveRight.put(Constant.H_GBLASON, false);
		this.haveRight.put(Constant.H_OBLASON, false);
		this.haveRight.put(Constant.H_GNOCODE, false);
		this.haveRight.put(Constant.H_OCANTOPEN, false);
		this.haveRight.put(Constant.C_GNOCODE, false);
		this.haveRight.put(Constant.C_OCANTOPEN, false);
		this.haveRight.put(Constant.H_GREPOS, false);
		this.haveRight.put(Constant.H_GTELE, false);
	}

	public void parseIntToRight(int total) {
		if (this.haveRight.isEmpty()) {
			this.initRight();
		}
		if (total == 1) {
			return;
		}
		if (this.haveRight.size() > 0) {
			this.haveRight.clear();
		}
		this.initRight();
		final Integer[] mapKey = this.haveRight.keySet().toArray(new Integer[this.haveRight.size()]);
		while (total > 0) {
			for (int i = this.haveRight.size() - 1; i < this.haveRight.size(); --i) {
				if (mapKey[i] <= total) {
					total ^= mapKey[i];
					this.haveRight.put(mapKey[i], true);
					break;
				}
			}
		}
	}

	public static void leave(final Player P, final String packet) {
		final House h = P.getInHouse();
		if (!h.isHouse(P, h)) {
			return;
		}
		final int Pguid = Integer.parseInt(packet);
		final Player Target = World.getPersonnage(Pguid);
		if (Target == null || !Target.isOnline() || Target.get_fight() != null
				|| Target.getCurMap().getId() != P.getCurMap().getId()) {
			return;
		}
		Target.teleport(h.getMapId(), h.getCellId());
		SocketManager.GAME_SEND_Im_PACKET(Target, "018;" + P.getName());
	}

	public static House getHouseByPerso(final Player P) {
		for (final Map.Entry<Integer, House> house : World.getHouses().entrySet()) {
			if (house.getValue().getOwnerId() == P.getAccID()) {
				return house.getValue();
			}
		}
		return null;
	}

	public static void removeHouseGuild(final int GuildID) {
		for (final Map.Entry<Integer, House> h : World.getHouses().entrySet()) {
			if (h.getValue().getGuildId() == GuildID) {
				h.getValue().setGuildRights(0);
				h.getValue().setGuildId(0);
			}
		}
		Database.getGame().getHouseData().removeGuild(GuildID);
	}
}
