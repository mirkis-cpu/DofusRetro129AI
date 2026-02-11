package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.client.Player;
import org.aestia.db.game.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.other.Guild;

public class Guild_memberData extends AbstractDAO<Object> {
	public Guild_memberData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM guild_members");

				while (RS.next()) {
					Guild g = World.getGuild(RS.getInt("guild"));
					if (g == null)
						continue;
					g.addMember(RS.getInt("guid"), RS.getString("name"), RS.getInt("level"), RS.getInt("gfxid"),
							RS.getInt("rank"), RS.getByte("pxp"), RS.getLong("xpdone"), RS.getInt("rights"),
							RS.getByte("align"), RS.getString("lastConnection").replaceAll("-", "~"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	public void delete(int id) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM `guild_members` WHERE `guid` = ?");
				p.setInt(1, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void deleteAll(int id) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM `guild_members` WHERE `guild` = ?");
				p.setInt(1, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void update(Player player) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("REPLACE INTO `guild_members` VALUES(?,?,?,?,?,?,?,?,?,?,?)");
				Guild.GuildMember gm = player.getGuildMember();
				p.setInt(1, gm.getGuid());
				p.setInt(2, gm.getGuild().getId());
				p.setString(3, player.getName());
				p.setInt(4, gm.getLvl());
				int gfx = gm.getGfx();
				if (gfx > 121 || gfx < 10) {
					gfx = player.getClasse() * 10 + player.getSexe();
				}
				p.setInt(5, gfx);
				p.setInt(6, gm.getRank());
				p.setLong(7, gm.getXpGave());
				p.setInt(8, gm.getPXpGive());
				p.setInt(9, gm.getRights());
				p.setInt(10, gm.getAlign());
				p.setString(11, gm.getLastCo());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public int isPersoInGuild(int guid) {
		int guildId;
		ResultSet RS = null;
		guildId = -1;
		try {
			try {
				RS = this.getData("SELECT guild FROM `guild_members` WHERE guid=" + guid);
				boolean found = RS.first();
				if (found) {
					guildId = RS.getInt("guild");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
		return guildId;
	}

	public int[] isPersoInGuild(String name) {
		int guildId = -1;
		int guid = -1;
		ResultSet RS = null;
		try {
			RS = this.getData("SELECT guild,guid FROM `guild_members` WHERE name='" + name + "'");
			if (RS.first()) {
				guildId = RS.getInt("guild");
				guid = RS.getInt("guid");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			this.close(RS);
		}

		return new int[] { guid, guildId };
	}
}
