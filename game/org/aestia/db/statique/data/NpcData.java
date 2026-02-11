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
import org.aestia.map.Map;

public class NpcData extends AbstractDAO<Object> {
	public NpcData(Connection dataSource) {
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
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * from npcs");

				while (RS.next()) {
					Map map = World.getMap(RS.getShort("mapid"));
					if (map == null)
						continue;
					map.addNpc(RS.getInt("npcid"), RS.getInt("cellid"), RS.getInt("orientation"),
							RS.getBoolean("isMovable"));
					++nbr;
				}
			} catch (SQLException e) {
				super.sendError("NpcData load", e);
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public boolean delete(int m, int c) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM npcs WHERE mapid = ? AND cellid = ?");
			p.setInt(1, m);
			p.setInt(2, c);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("NpcData delete", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean addOnMap(int m, int id, int c, int o, boolean mo) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO `npcs` VALUES (?,?,?,?,?)");
			p.setInt(1, m);
			p.setInt(2, id);
			p.setInt(3, c);
			p.setInt(4, o);
			p.setBoolean(5, mo);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("NpcData addOnMap", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}
