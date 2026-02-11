/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;
import org.aestia.kernel.Constant;

public class ZaapData extends AbstractDAO<Object> {
	public ZaapData(Connection dataSource) {
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
		int i;
		ResultSet RS = null;
		i = 0;
		try {
			try {
				RS = this.getData("SELECT mapID, cellID from zaaps");

				while (RS.next()) {
					Constant.ZAAPS.put(RS.getInt("mapID"), RS.getInt("cellID"));
					++i;
				}
			} catch (SQLException e) {
				super.sendError("ZaapData load", e);
			}
		} finally {
			this.close(RS);
		}
		return i;
	}
}
