/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;

public class Mobgroups_fixData extends AbstractDAO<Object> {
	public Mobgroups_fixData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public boolean add(int mapID, int cellID, String groupData) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("REPLACE INTO `mobgroups_fix` VALUES(?,?,?)");
			p.setInt(1, mapID);
			p.setInt(2, cellID);
			p.setString(3, groupData);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Mobgroups_fixData add", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}
