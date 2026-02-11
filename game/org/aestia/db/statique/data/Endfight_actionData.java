/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;
import org.aestia.map.Map;
import org.aestia.other.Action;

public class Endfight_actionData extends AbstractDAO<Object> {
	public Endfight_actionData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public int load() {
		ResultSet RS = null;
		int nbr = 0;
		try {
			RS = this.getData("SELECT * FROM endfight_action");

			while (RS.next()) {
				Map map = World.getMap(RS.getShort("map"));
				if (map == null)
					continue;
				map.addEndFightAction(RS.getInt("fighttype"),
						new Action(RS.getInt("action"), RS.getString("args"), RS.getString("cond"), null));
				++nbr;
			}
			int n = nbr;
			return n;
		} catch (SQLException e) {
			e.printStackTrace();
			Main.stop();
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public int reload() {
		ResultSet RS = null;
		int nbr = 0;
		try {
			RS = this.getData("SELECT * FROM endfight_action");

			while (RS.next()) {
				Map map = World.getMap(RS.getShort("map"));
				if (map == null)
					continue;
				map.delAllEndFightAction();
				map.addEndFightAction(RS.getInt("fighttype"),
						new Action(RS.getInt("action"), RS.getString("args"), RS.getString("cond"), null));
				++nbr;
			}
			int n = nbr;
			return n;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public boolean add(int mapID, int type, int Aid, String args, String cond) {
		if (!this.delete(mapID, type, Aid)) {
			return false;
		}
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO `endfight_action` VALUES (?,?,?,?,?)");
			p.setInt(1, mapID);
			p.setInt(2, type);
			p.setInt(3, Aid);
			p.setString(4, args);
			p.setString(5, cond);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean delete(int mapID, int type, int aid) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"DELETE FROM `endfight_action` WHERE map = ? AND fighttype = ? AND action = ?");
			p.setInt(1, mapID);
			p.setInt(2, type);
			p.setInt(3, aid);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}
}
