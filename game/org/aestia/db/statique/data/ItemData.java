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
import org.aestia.object.Object;

public class ItemData extends AbstractDAO<Object> {
	public ItemData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(java.lang.Object obj) {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM `items` WHERE guid IN (" + obj + ")");

				while (RS.next()) {
					int guid = RS.getInt("guid");
					int tempID = RS.getInt("template");
					int qua = RS.getInt("qua");
					int pos = RS.getInt("pos");
					String stats = RS.getString("stats");
					int puit = RS.getInt("puit");
					World.addObjet(World.newObjet(guid, tempID, qua, pos, stats, puit), false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Main.stop();
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM `items`");

				while (RS.next()) {
					int guid = RS.getInt("guid");
					int tempID = RS.getInt("template");
					int qua = RS.getInt("qua");
					int pos = RS.getInt("pos");
					String stats = RS.getString("stats");
					int puit = RS.getInt("puit");
					World.addObjet(World.newObjet(guid, tempID, qua, pos, stats, puit), false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Main.stop();
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}

	@Override
	public boolean update(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getTemplate() == null) {
			return false;
		}
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `items` SET `qua` = ?, `pos` = ?, `puit` = ?, `stats` = ? WHERE `guid` = ? AND `template` = ?");
			p.setInt(1, obj.getQuantity());
			p.setInt(2, obj.getPosition());
			p.setInt(3, obj.getPuit());
			p.setString(4, obj.parseToSave());
			p.setInt(5, obj.getGuid());
			p.setInt(6, obj.getTemplate().getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public void saveNew(Object item) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("REPLACE INTO `items` VALUES(?,?,?,?,?,?)");
				p.setInt(1, item.getGuid());
				p.setInt(2, item.getTemplate().getId());
				p.setInt(3, item.getQuantity());
				p.setInt(4, item.getPosition());
				p.setString(5, item.parseToSave());
				p.setInt(6, item.getPuit());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
				this.close(p);
			}
		} finally {
			this.close(p);
		}
	}

	public void delete(int guid) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM `items` WHERE guid = ?");
				p.setInt(1, guid);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
				this.close(p);
			}
		} finally {
			this.close(p);
		}
	}

	public void save(Object item, boolean obvi) {
		if (item == null) {
			return;
		}
		if (item.getTemplate() == null) {
			return;
		}
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("REPLACE INTO `items` VALUES (?,?,?,?,?,?)");
				p.setInt(1, item.getGuid());
				p.setInt(2, item.getTemplate().getId());
				p.setInt(3, item.getQuantity());
				p.setInt(4, item.getPosition());
				p.setString(5, item.parseToSave());
				p.setInt(6, item.getPuit());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
				this.close(p);
			}
		} finally {
			this.close(p);
		}
	}

	public int getNextObjetID() {
		int guid;
		ResultSet RS = null;
		guid = 0;
		try {
			try {
				RS = this.getData("SELECT MAX(guid) AS max FROM `items`");

				boolean found = RS.first();
				if (found) {
					guid = RS.getInt("max");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Main.stop();
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
		return guid;
	}
}
