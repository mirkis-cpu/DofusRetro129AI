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
import org.aestia.kernel.Main;

public class ServerData extends AbstractDAO<Object> {
	public ServerData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public void updateTime(long time) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE servers SET `uptime` = ? WHERE `id` = ?");
				p.setLong(1, time);
				p.setInt(2, Main.serverId);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("ServerData updateTime", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void loggedZero() {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"UPDATE players SET `logged` = 0 WHERE `server` = '" + Main.serverId + "'");
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("ServerData loggedZero", e);
			}
		} finally {
			this.close(p);
		}
	}
}
