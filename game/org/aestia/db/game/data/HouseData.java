/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.client.Player;
import org.aestia.db.game.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.other.House;

public class HouseData extends AbstractDAO<House> {
	public HouseData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(House h) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `houses` SET `owner_id` = ?,`sale` = ?,`guild_id` = ?,`access` = ?,`key` = ?,`guild_rights` = ? WHERE id = ?");
			p.setInt(1, h.getOwnerId());
			p.setInt(2, h.getSale());
			p.setInt(3, h.getGuildId());
			p.setInt(4, h.getAccess());
			p.setString(5, h.getKey());
			p.setInt(6, h.getGuildRights());
			p.setInt(7, h.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public int load() {
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * from houses");

				while (RS.next()) {
					int id = RS.getInt("id");
					int owner = RS.getInt("owner_id");
					int sale = RS.getInt("sale");
					int guild = RS.getInt("guild_id");
					int access = RS.getInt("access");
					String key = RS.getString("key");
					int guildRights = RS.getInt("guild_rights");
					House house = World.getHouse(id);
					if (house == null)
						continue;
					if (owner != 0 && World.getAccounts().get(owner) == null) {
						new Exception("La maison " + id + " a un propri\u00e9taire inexistant.").printStackTrace();
					}
					house.setOwnerId(owner);
					house.setSale(sale);
					house.setGuildId(guild);
					house.setAccess(access);
					house.setKey(key);
					house.setGuildRightsWithParse(guildRights);
					++nbr;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				nbr = 0;
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public void buy(Player P, House h) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"UPDATE `houses` SET `sale`='0', `owner_id`=?, `guild_id`='0', `access`='0', `key`='-', `guild_rights`='0' WHERE `id`=?");
				p.setInt(1, P.getAccID());
				p.setInt(2, h.getId());
				this.execute(p);
				h.setSale(0);
				h.setOwnerId(P.getAccID());
				h.setGuildId(0);
				h.setAccess(0);
				h.setKey("-");
				h.setGuildRights(0);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void sell(House h, int price) {
		h.setSale(price);
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `houses` SET `sale`=? WHERE `id`=?");
				p.setInt(1, price);
				p.setInt(2, h.getId());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void updateCode(Player P, House h, String packet) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `houses` SET `key`=? WHERE `id`=? AND owner_id=?");
				p.setString(1, packet);
				p.setInt(2, h.getId());
				p.setInt(3, P.getAccID());
				this.execute(p);
				h.setKey(packet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void updateGuild(House h, int GuildID, int GuildRights) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `houses` SET `guild_id`=?, `guild_rights`=? WHERE `id`=?");
				p.setInt(1, GuildID);
				p.setInt(2, GuildRights);
				p.setInt(3, h.getId());
				this.execute(p);
				h.setGuildId(GuildID);
				h.setGuildRights(GuildRights);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void removeGuild(int GuildID) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"UPDATE `houses` SET `guild_rights`='0', `guild_id`='0' WHERE `guild_id`=?");
				p.setInt(1, GuildID);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}
