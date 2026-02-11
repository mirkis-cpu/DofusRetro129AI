package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.game.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.other.Guild;

public class GuildData extends AbstractDAO<Guild> {
	public GuildData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Guild g) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `guilds` SET `lvl` = ?, `xp` = ?, `capital` = ?, `nbrmax` = ?, `sorts` = ?, `stats` = ? WHERE id = ?");
			p.setInt(1, g.getLvl());
			p.setLong(2, g.getXp());
			p.setInt(3, g.getCapital());
			p.setInt(4, g.getNbrPerco());
			p.setString(5, g.compileSpell());
			p.setString(6, g.compileStats());
			p.setInt(7, g.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM guilds");

				while (RS.next()) {
					World.addGuild(new Guild(RS.getInt("id"), RS.getString("name"), RS.getString("emblem"),
							RS.getInt("lvl"), RS.getLong("xp"), RS.getInt("capital"), RS.getInt("nbrmax"),
							RS.getString("sorts"), RS.getString("stats")), false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	public void add(Guild g) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("INSERT INTO `guilds` VALUES (?,?,?,1,0,0,0,?,?)");
				p.setInt(1, g.getId());
				p.setString(2, g.getName());
				p.setString(3, g.getEmblem());
				p.setString(4, "462;0|461;0|460;0|459;0|458;0|457;0|456;0|455;0|454;0|453;0|452;0|451;0|");
				p.setString(5, "176;100|158;1000|124;100|");
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void delete(int id) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM `guilds` WHERE `id` = ?");
				p.setInt(1, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}
